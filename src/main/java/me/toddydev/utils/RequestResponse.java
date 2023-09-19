package me.toddydev.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.toddydev.Init;

@Getter @Setter
@AllArgsConstructor
public class RequestResponse {

    private String status;
    private Action action;

    private String message;

    public String build() {
        return Init.getGson().toJson(this);
    }

    public enum Action {

        ORDER_CREATED,
        ORDER_UPDATED,
        ORDER_ALREADY_EXISTS,
        ORDER_FOUNDED,
        ORDER_NOT_FOUND,

        AUTH_TOKEN_MISSING,
        AUTH_TOKEN_INVALID,

        ACCOUNT_NOT_FOUND,
        ACCOUNT_FOUNDED,

        ACCOUNT_ALREADY_EXISTS,
        ACCOUNT_CREATED,

        ERROR_OCCURRED,

        PATH_NOT_FOUND,

    }
}
