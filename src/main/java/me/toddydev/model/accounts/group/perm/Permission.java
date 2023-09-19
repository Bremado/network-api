package me.toddydev.model.accounts.group.perm;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Permission {

    private String permission;
    private long expires;

    public Permission(String permission, long expires) {
        this.permission = permission;
        this.expires = expires;
    }

    public boolean isExpired() {
        if (expires == -1L) {
            return false;
        }
        return System.currentTimeMillis() >= expires;
    }
}
