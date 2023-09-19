package me.toddydev.service;

import lombok.Getter;
import lombok.Setter;
import me.toddydev.service.mercadopago.MercadoPagoService;
import me.toddydev.service.redis.RedisService;

public class Services {

    @Getter @Setter
    private static RedisService redisService = new RedisService();

    @Getter @Setter
    private static MercadoPagoService mercadoPagoService = new MercadoPagoService();
}
