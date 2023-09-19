package me.toddydev.model.payments.payer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter @Setter
@AllArgsConstructor
public class Payer {

    private String payerId;
    private String payerName;

}
