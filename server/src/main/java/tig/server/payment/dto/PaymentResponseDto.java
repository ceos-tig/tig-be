package tig.server.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentResponseDto {

    @Schema(type = "string", example = "PAID")
    private String status;

    @Schema(type = "string", example = "ed432870991f0ff268dd29ac1082d689")
    private String id;

    @Schema(type = "string", example = "0190d8e2-43e6-8e3f-4dd4-384bf52a3b10")
    private String transactionId;

    @Schema(type = "string", example = "merchant-9d3fdbd7-3fff-47fc-ae20-39df72c4a9c0")
    private String merchantId;

    @Schema(type = "string", example = "store-5c0c7a07-30ff-4fed-be32-222ac3542558")
    private String storeId;

    @Schema(type = "object")
    private Method method;

    @Schema(type = "object")
    private Channel channel;

    @Schema(type = "string", example = "V2")
    private String version;

    @Schema(type = "string", example = "2024-07-22T05:20:25.609864446Z")
    private String requestedAt;

    @Schema(type = "string", example = "2024-07-22T05:20:49.775624547Z")
    private String updatedAt;

    @Schema(type = "string", example = "2024-07-22T05:20:49.748901556Z")
    private String statusChangedAt;

    @Schema(type = "string", example = "나이키 와플 트레이너 2 SD")
    private String orderName;

    @Schema(type = "object")
    private Amount amount;

    @Schema(type = "string", example = "KRW")
    private String currency;

    @Schema(type = "object")
    private Customer customer;

    @Schema(type = "boolean", example = "false")
    private boolean isCulturalExpense;

    @Schema(type = "string", example = "2024-07-22T05:20:49.748901556Z")
    private String paidAt;

    @Schema(type = "string", example = "T69dec19257c75a5435a")
    private String pgTxId;

    @Schema(type = "string", example = "{\"aid\":\"A69dec2f01d33ed2d03d\",\"tid\":\"T69dec19257c75a5435a\",\"cid\":\"TC0ONETIME\",\"partner_order_id\":\"ed432870991f0ff268dd29ac1082d689\",\"partner_user_id\":\"port-customer-id-0190d8e2-43ee-c40c-effc-4c3cd85fb086\",\"payment_method_type\":\"MONEY\",\"item_name\":\"나이키 와플 트레이너 2 SD\",\"quantity\":1,\"amount\":{\"total\":15500,\"tax_free\":0,\"vat\":1409,\"point\":0,\"discount\":0,\"green_deposit\":0},\"created_at\":\"2024-07-22T14:20:26\",\"approved_at\":\"2024-07-22T14:20:49\"}")
    private String pgResponse;

    @Schema(type = "string", example = "https://mockup-pg-web.kakao.com/v1/confirmation/p/T69dec19257c75a5435a/896F94C887FCBE8AD6CFE6550117154CABCB222C7051B72063FA7E64642AD6DB")
    private String receiptUrl;

    @Getter
    @Setter
    public static class Method {
        @Schema(type = "string", example = "PaymentMethodEasyPay")
        private String type;

        @Schema(type = "string", example = "KAKAOPAY")
        private String provider;

        @Schema(type = "object")
        private EasyPayMethod easyPayMethod;

        @Getter
        @Setter
        public static class EasyPayMethod {
            @Schema(type = "string", example = "PaymentMethodEasyPayMethodCharge")
            private String type;
        }
    }

    @Getter
    @Setter
    public static class Channel {
        @Schema(type = "string", example = "TEST")
        private String type;

        @Schema(type = "string", example = "channel-id-96bea4fe-1474-4cd0-82be-218a24446cc2")
        private String id;

        @Schema(type = "string", example = "channel-key-140a124d-0587-42b4-a8e5-1bebb4029e0a")
        private String key;

        @Schema(type = "string", example = "카카오페이 일반결제")
        private String name;

        @Schema(type = "string", example = "KAKAOPAY")
        private String pgProvider;

        @Schema(type = "string", example = "TC0ONETIME")
        private String pgMerchantId;
    }

    @Getter
    @Setter
    public static class Amount {
        @Schema(type = "integer", example = "15500")
        private Integer total;

        @Schema(type = "integer", example = "0")
        private Integer taxFree;

        @Schema(type = "integer", example = "1409")
        private Integer vat;

        @Schema(type = "integer", example = "14091")
        private Integer supply;

        @Schema(type = "integer", example = "0")
        private Integer discount;

        @Schema(type = "integer", example = "15500")
        private Integer paid;

        @Schema(type = "integer", example = "0")
        private Integer cancelled;

        @Schema(type = "integer", example = "0")
        private Integer cancelledTaxFree;
    }

    @Getter
    @Setter
    public static class Customer {
        @Schema(type = "string", example = "port-customer-id-0190d8e2-43ee-c40c-effc-4c3cd85fb086")
        private String id;
    }
}