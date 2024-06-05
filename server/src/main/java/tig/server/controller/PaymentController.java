package tig.server.controller;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.AccessToken;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;

@Controller
public class PaymentController {
    private final IamportClient iamportClient;

    public PaymentController() {
        this.iamportClient = new IamportClient("6386144644077220",
                "fbMphzxi2MGZu1LC16BXsaMQtmw7eCzVjoln9fJXGg2nyZNkZ15qElS1K7R76oK6LrSuDKwLXqNuLISq");
    }

    @GetMapping("pay")
    public String showPay() {
        return "pay";
    }

    @PostMapping("/verify/{imp_uid}")
    public IamportResponse<Payment> paymentByImpUid(@PathVariable("imp_uid") String imp_uid)
            throws IamportResponseException, IOException {
        IamportResponse<AccessToken> auth = iamportClient.getAuth();
        System.out.println("auth.getMessage() = " + auth.getMessage());
        System.out.println("auth.getCode() = " + auth.getCode());
        System.out.println("auth.getResponse().getToken() = " + auth.getResponse().getToken());

        IamportResponse<Payment> paymentIamportResponse = iamportClient.paymentByImpUid(imp_uid);
        System.out.println("paymentIamportResponse.getResponse() = " + paymentIamportResponse.getResponse());
        System.out.println("paymentIamportResponse.getResponse().getAmount() = " + paymentIamportResponse.getResponse().getAmount());

        CancelData cancelData = new CancelData(imp_uid, true);
        System.out.println("cancelData = " + cancelData);

        if (paymentIamportResponse.getResponse().getAmount().intValue() == 200) {
            iamportClient.cancelPaymentByImpUid(cancelData);
        }

        return iamportClient.paymentByImpUid(imp_uid);
    }
}
