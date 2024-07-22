package tig.server.payment.service;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import tig.server.club.domain.Club;
import tig.server.club.repository.ClubRepository;
import tig.server.enums.Type;
import tig.server.global.exception.BusinessExceptionHandler;
import tig.server.global.code.ErrorCode;
import tig.server.payment.dto.PaymentCompleteResponseDto;
import tig.server.payment.dto.PaymentRequestDto;
import tig.server.payment.dto.PaymentResponseDto;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class PaymentService {

    private final ClubRepository clubRepository;

    private final IamportClient iamportClient;
    private final WebClient webClient;
    private final String PORTONE_HOST = "https://api.portone.io";

    @Value("${portone.api_key}")
    private String apiKey;

    @Value("${portone.api_secret}")
    private String apiSecret;

    @Value("${portone.v2_secret}")
    private String v2Secret;

    public PaymentService(ClubRepository clubRepository, WebClient.Builder webClientBuilder) {
        this.clubRepository = clubRepository;
        this.webClient = webClientBuilder.baseUrl(PORTONE_HOST).build();
        this.iamportClient = new IamportClient(apiKey, apiSecret);
    }

    private CancelData createCancelData(IamportResponse<Payment> response, int refundAmount) {
        if (refundAmount == 0) { //전액 환불일 경우
            return new CancelData(response.getResponse().getImpUid(), true);
        }
        //부분 환불일 경우 checksum을 입력해 준다.
        return new CancelData(response.getResponse().getImpUid(), true, new BigDecimal(refundAmount));

    }

    /*=====================*/
    public PaymentCompleteResponseDto completePayment(PaymentRequestDto paymentRequestDto) {
        Integer paymentPrice = 0; // BE에서 검증한 가격
        Club paymentTargetClub = clubRepository.findById(Long.valueOf(paymentRequestDto.getClubId()))
                .orElseThrow(() -> new BusinessExceptionHandler("club not found", ErrorCode.NOT_FOUND_ERROR));
        String paymentId = paymentRequestDto.getPaymentId();

        /**
         *  예상 결제 금액 계산
         *  */
        if (paymentTargetClub.getType().equals(Type.GAME)) { // '게임당' 업체
            paymentPrice = paymentRequestDto.getClubPrice() * paymentRequestDto.getGameCount();
        } else { // '시간당' 업체
            // 날짜 형식 지정
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

            // 문자열을 LocalDateTime으로 변환
            LocalDateTime startTime = LocalDateTime.parse(paymentRequestDto.getStartTime(), formatter);
            LocalDateTime endTime = LocalDateTime.parse(paymentRequestDto.getEndTime(), formatter);

            // 시간 차이 계산
            Duration duration = Duration.between(startTime, endTime);
            long minutes = duration.toMinutes(); // 분 단위 차이
            double hours = minutes / 60.0; // 시간 단위로 변환 (소수점 포함)

            // 결제 금액 계산
            paymentPrice = (int) Math.round(paymentRequestDto.getClubPrice() * hours);
        }

        // 포트원 결제내역 단건조회 API 호출
        PaymentResponseDto paymentResponseDto = getPaymentResponse(paymentId).block();
        log.info(paymentResponseDto.getOrderName());
        /**
         * 가격 비교 진행
         * */
        PaymentCompleteResponseDto response = null;
        System.out.println("BE에서 계산한 금액 = " + paymentPrice);
        System.out.println("포트원에서 조회한 금액 = " + paymentResponseDto.getAmount().getTotal());
        if(paymentPrice.equals(paymentResponseDto.getAmount().getTotal())){ // 결제된 금액과 상품의 금액이 같을 경우
            switch (paymentResponseDto.getStatus()) {
                case "VIRTUAL_ACCOUNT_ISSUED":
                    // 가상 계좌가 발급된 상태입니다.
                    // 이 부분은 일단 구현 하지 않음
                    break;
                case "PAID":
                    // 결제 완료
                    response = PaymentCompleteResponseDto.fromPay(paymentResponseDto.getStatus(),
                            paymentResponseDto.getId(),
                            paymentResponseDto.getMethod().getProvider(),
                            paymentResponseDto.getOrderName(),
                            paymentResponseDto.getAmount().getTotal())
                    ;
                    break;
                default:
                    throw new BusinessExceptionHandler("Unknown payment status",ErrorCode.BAD_REQUEST_ERROR);
            }
        } else {
            // 결제 금액이 불일치하여 위/변조 시도가 의심됩니다.
            throw new BusinessExceptionHandler("Payment amount mismatch",ErrorCode.BAD_REQUEST_ERROR);
        }

        return response;
    }

    private Mono<PaymentResponseDto> getPaymentResponse(String paymentId) {
        return webClient.get()
                .uri("/payments/{paymentId}", paymentId)
                .header(HttpHeaders.AUTHORIZATION, "PortOne " + v2Secret)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> Mono.error(new BusinessExceptionHandler("Invalid payment response", ErrorCode.BAD_REQUEST_ERROR)))
                .bodyToMono(PaymentResponseDto.class);
    }

}