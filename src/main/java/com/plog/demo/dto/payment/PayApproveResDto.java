package com.plog.demo.dto.payment;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class PayApproveResDto {

    private AmountDto amount;
    private String item_name;
    private String created_at;
    private String approved_at;

}
