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

@RestController("/v1")
public class PaymentController {

    @Autowired
    private OrderRepository repo;


    @RequestMapping(
            value = "/v1/payment/search/{referenceId}",
            method = RequestMethod.GET
    )
    public String getPayment(@RequestHeader("Authorization") String auth, @PathVariable("referenceId") String referenceId) {
        if (auth == null) return new RequestResponse("ERROR", AUTH_TOKEN_MISSING, "Auth token is missing").build();

        Order order = repo.findById(referenceId).orElse(null);
        if (order == null) {
            return new RequestResponse("ERROR", ORDER_NOT_FOUND, "This order does not exist").build();
        }

        if (!order.getAuthorizationToken().equals(auth)) {
            return new RequestResponse("ERROR", AUTH_TOKEN_INVALID, "This auth token doesn't exist.").build();
        }

        return new RequestResponse("SUCCESS", ORDER_FOUNDED, Init.getGson().toJson(order)).build();
    }

    @RequestMapping(
            value = "/v1/payment/create",
            method = RequestMethod.POST
    )
    public String createPayment(@RequestHeader("Authorization") String auth, @RequestBody String body) {
        OrderResponse order = toResponse(body);
        order.setAuthorizationToken(auth);

        if (repo.findById(order.getReferenceId()).orElse(null) != null) {
            return new RequestResponse("ERROR", ORDER_ALREADY_EXISTS, "This order already exists").build();
        }

        Order o = Services.getMercadoPagoService().createPayment(auth, order);
        if (o == null) {
            return new RequestResponse("ERROR", ORDER_ALREADY_EXISTS, "This order doesn't exists").build();
        }

        repo.save(o);

        return new RequestResponse("SUCCESS", ORDER_CREATED, Init.getGson().toJson(o)).build();
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

    public Order toOrder(OrderResponse response) {
        return Order.builder()
                .referenceId(response.getReferenceId())
                .authorizationToken(response.getAuthorizationToken())
                .payer(Init.getGson().toJson(response.getPayer()))
                .item(Init.getGson().toJson(response.getItem()))
                .state(response.getState().name())
                .build();
    }
}
