package me.toddydev.service.mercadopago;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.okhttp.*;
import me.toddydev.model.payments.Order;
import me.toddydev.model.payments.state.OrderState;
import me.toddydev.response.OrderResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class MercadoPagoService {

    public Order createPayment(String auth, OrderResponse response) {
        JsonObject object = new JsonObject();
        object.addProperty("description", "Donation #" + response.getReferenceId());

        JsonObject payer = new JsonObject();

        payer.addProperty("entity_type", "individual");
        payer.addProperty("type", "customer");
        payer.addProperty("email", response.getReferenceId().substring(0, 4) + "@toddydev.me");

        object.add("payer", payer);

        object.addProperty("external_reference", response.getReferenceId());
        object.addProperty("payment_method_id", "pix");
        object.addProperty("transaction_amount", new BigDecimal(response.getItem().getPrice()));
        object.addProperty("notification_url", "https://api.toddydev.me/v1/payment/callback/" + response.getReferenceId());

        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

            RequestBody requestBody = RequestBody.create(mediaType, object.toString());
            Request request = new Request.Builder()
                    .url("https://api.mercadopago.com/v1/payments")
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    .addHeader("Authorization", "Bearer " + auth)
                    .post(requestBody)
                    .build();

            Response rs = okHttpClient.newCall(request).execute();
            JsonObject o = new JsonParser().parse(rs.body().string()).getAsJsonObject();
            String code = o.getAsJsonObject("point_of_interaction").getAsJsonObject("transaction_data").get("qr_code").getAsString();

            Order order = response.toOrder();
            order.setPaymentId(o.get("id").getAsLong());
            order.setQrCode(code);
            return order;
        }  catch (Exception e) {
            return null;
        }
    }

    public OrderState checkOrder(String auth, Order response) {
        try {
            OkHttpClient okHttpClient = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://api.mercadopago.com/v1/payments/" + response.getPaymentId())
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", "Bearer " + auth)
                    .get()
                    .build();

            Response rs = okHttpClient.newCall(request).execute();
            JsonObject object = new JsonParser().parse(rs.body().string()).getAsJsonObject();
            JsonElement element = object.get("status");
            
            if (element == null) {
                return OrderState.WAITING;
            }
            
            String status = element.getAsString();
            OrderState orderState = (status.equalsIgnoreCase("approved") ? OrderState.PAID : status.equalsIgnoreCase("cancelled") ? OrderState.CANCELLED : status.equalsIgnoreCase("refunded") ? OrderState.CANCELLED : status.equalsIgnoreCase("rejected") ? OrderState.CANCELLED : OrderState.WAITING);
            return orderState;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return OrderState.WAITING;
    }
}
