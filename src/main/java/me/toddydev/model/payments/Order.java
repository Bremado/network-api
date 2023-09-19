package me.toddydev.model.payments;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import me.toddydev.Init;
import me.toddydev.model.payments.item.Item;
import me.toddydev.model.payments.payer.Payer;
import me.toddydev.model.payments.state.OrderState;
import me.toddydev.response.OrderResponse;

@Builder
@Getter @Setter
@Entity(name = "orders")
public class Order {

    @Id
    private String referenceId;

    private Long paymentId;

    @Column(columnDefinition = "TEXT")
    private String authorizationToken;

    @Column(columnDefinition = "TEXT")
    private String state;

    @Lob
    @Column(columnDefinition = "JSON")
    private Object item;

    @Lob
    @Column(columnDefinition = "JSON")
    private Object payer;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String qrCode;

    public Order() {}

    public Order(String referenceId, long paymentId, String authorizationToken, String state, Object item, Object payer, String qrCode) {
        this.referenceId = referenceId;
        this.paymentId = paymentId;
        this.authorizationToken = authorizationToken;
        this.state = state;
        this.item = item;
        this.payer = payer;
        this.qrCode = qrCode;
    }

    public OrderResponse toResponse() {
        String payerJson = Init.getGson().toJson(payer);
        String itemJson = Init.getGson().toJson(item);

        Payer payer = Init.getGson().fromJson(payerJson, Payer.class);
        Item item = Init.getGson().fromJson(itemJson, Item.class);

        return OrderResponse.builder()
                .referenceId(referenceId)
                .payer(payer)
                .item(item)
                .state(OrderState.find(state))
                .build();
    }
}
