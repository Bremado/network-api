package me.toddydev.model.accounts.group;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import me.toddydev.model.accounts.group.perm.Permission;
import me.toddydev.model.accounts.group.rank.Rank;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class Group {

    private Rank rank;
    private Rank tag;

    private List<Permission> permissions;

    private long created;
    private long lastModified;

    private long expires;

    public Group() {
        this.rank = Rank.NORMAL;
        this.tag = Rank.NORMAL;
        this.permissions = new ArrayList<>();
        this.created = System.currentTimeMillis();
        this.lastModified = System.currentTimeMillis();
        this.expires = -1L;
    }
}
