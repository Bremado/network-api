package me.toddydev.controller.v1.payments;

import me.toddydev.Init;
import me.toddydev.model.payments.Order;
import me.toddydev.model.payments.item.Item;
import me.toddydev.model.payments.payer.Payer;
import me.toddydev.model.payments.state.OrderState;
import me.toddydev.repository.OrderRepository;
import me.toddydev.response.OrderResponse;
import me.toddydev.service.Services;
import me.toddydev.utils.RequestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static me.toddydev.utils.RequestResponse.Action.*;

@RestController
public class CallbackController {

    @Autowired
    private OrderRepository repo;

    @RequestMapping(value = "/v1/payment/callback/{referenceId}", method = RequestMethod.POST)
    public String callback(@PathVariable(value = "referenceId") String referenceId, @RequestBody String body) {
        if (referenceId == null) return new RequestResponse("ERROR", AUTH_TOKEN_MISSING, "Auth token is missing").build();

        Order order = repo.findById(referenceId).orElse(null);

        if (order == null) {
            return new RequestResponse("ERROR", ORDER_NOT_FOUND, "This order doesn't exists.").build();
        }

        OrderState state = Services.getMercadoPagoService().checkOrder(order.getAuthorizationToken(), order);
        order.setState(state.name());
        repo.save(order);

        return new RequestResponse("SUCCESS", ORDER_UPDATED, "This order has updated.").build();
    }

    private OrderResponse toResponse(String order) {
        Order o = Init.getGson().fromJson(order, Order.class);

        String payerJson = Init.getGson().toJson(o.getPayer());
        String itemJson = Init.getGson().toJson(o.getItem());

        Payer payer = Init.getGson().fromJson(payerJson, Payer.class);
        Item item = Init.getGson().fromJson(itemJson, Item.class);

        return OrderResponse.builder()
                .referenceId(o.getReferenceId())
                .payer(payer)
                .item(item)
                .state(OrderState.find(o.getState()))
                .build();
    }
}
