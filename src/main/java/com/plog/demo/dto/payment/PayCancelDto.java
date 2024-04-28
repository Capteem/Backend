package com.plog.demo.dto.payment;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class PayCancelDto {

    private String aid;
    private String tid;
    private String cid;

    private String status;
    private String partner_order_id;
    private String partner_user_id;
    private String payment_method_type;

    private AmountDto amount;
    private ApprovedCancelAmountDto approved_cancel_amount;
    private CancelAmountDto cancel_amount;
    private CancelAvailableAmountDto cancel_available_amount;

    private String item_name;
    private String item_code;
    private int quantity;
    private LocalDateTime created_at;
    private LocalDateTime approved_at;
    private LocalDateTime canceled_at;
    private String payload;
}
