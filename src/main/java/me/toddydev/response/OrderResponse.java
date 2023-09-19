package me.toddydev.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import me.toddydev.Init;
import me.toddydev.model.payments.Order;
import me.toddydev.model.payments.item.Item;
import me.toddydev.model.payments.payer.Payer;
import me.toddydev.model.payments.state.OrderState;

@Builder
@Getter @Setter
@AllArgsConstructor
public class OrderResponse {

    private String referenceId;
    private String authorizationToken;

    private Long paymentId;

    private OrderState state;

    private Item item;
    private Payer payer;

    private String qrCode;

    public Order toOrder() {
        return Order.builder()
                .referenceId(referenceId)
                .authorizationToken(authorizationToken)
                .payer(Init.getGson().toJson(payer))
                .item(Init.getGson().toJson(item))
                .state(state.name())
                .paymentId(1L)
                .build();
    }

}
