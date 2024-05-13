package com.plog.demo.service.payment;

import com.plog.demo.dto.payment.*;
import com.plog.demo.exception.CustomException;

public interface PaymentService {
    PayReadyResDto payReady(PayInfoDto payInfoDto);

    PayApproveResDto getApprove(String id, String pgToken) throws CustomException;

    PayCancelDto getCancelApprove(String tid, String id) throws CustomException;

    PaymentInfoDto getPaymentInfo(String id) throws CustomException;

    void payCancel(String tid, String id) throws CustomException;

    void payFail(String tid, String id) throws CustomException;
}
