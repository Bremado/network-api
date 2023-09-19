package me.toddydev.model.payments.state;

public enum OrderState {

    WAITING,
    PAID,
    CANCELLED;


    public static OrderState find(String name) {
        for (OrderState state : values()) {
            if (state.name().equalsIgnoreCase(name)) {
                return state;
            }
        }
        return WAITING;
    }
}
