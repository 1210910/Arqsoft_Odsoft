package pt.psoft.g1.psoftg1.usermanagement.api;

import lombok.Getter;
import lombok.Setter;

public class UserViewAMQP {
    @Getter
    private String id;
    @Getter
    private String name;
    @Getter
    private String email;
    @Getter
    @Setter
    private Long version;


    public UserViewAMQP(String id, String name, String email, Long version) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.version = version;
    }


}
