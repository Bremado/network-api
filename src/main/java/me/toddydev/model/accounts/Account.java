package me.toddydev.model.accounts;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter
@Entity(name = "accounts")
public class Account {

    @Id
    private String uniqueId;
    private String name;

    @Column(columnDefinition = "JSON")
    private Object prefs;

    @Column(columnDefinition = "JSON")
    private Object role;

    private Long firstLogin;
    private Long lastLogin;

}
