package me.toddydev.model.payments.item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter @Setter
@AllArgsConstructor
public class Item {

    private String title;
    private Integer price;

}
