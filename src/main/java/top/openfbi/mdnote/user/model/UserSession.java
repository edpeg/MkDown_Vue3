package top.openfbi.mdnote.user.model;

import lombok.Data;

import java.io.Serializable;


@Data
public class UserSession implements Serializable {
    public UserSession() {

    }

    public UserSession(User user) {
        this.id = user.getId();
        this.userName = user.getUserName();
    }

    private long id;
    private String userName;
}
