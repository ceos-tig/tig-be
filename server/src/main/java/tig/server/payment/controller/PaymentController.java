package tig.server.payment.controller;

import com.siot.IamportRestClient.exception.IamportResponseException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import tig.server.error.ApiResponse;
import tig.server.payment.dto.PaymentCompleteResponseDto;
import tig.server.payment.dto.PaymentRequestDto;
import tig.server.payment.dto.PaymentResponseDto;
import tig.server.payment.service.PaymentService;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/pay")
public class PaymentController {
    private final PaymentService paymentService;

    @Operation(summary = "결제 후 검증")
    @PostMapping("/verification")
    public ResponseEntity<ApiResponse<PaymentCompleteResponseDto>> postVerification(@RequestBody PaymentRequestDto paymentRequestDto, Authentication authentication) throws IamportResponseException, IOException {
        log.info("payment_id : {}", paymentRequestDto.getPaymentId());
        PaymentCompleteResponseDto paymentCompleteResponseDto = paymentService.completePayment(paymentRequestDto);
        ApiResponse<PaymentCompleteResponseDto> response = ApiResponse.of(200, "payment success", paymentCompleteResponseDto);
        return ResponseEntity.ok(response);
    }

}
