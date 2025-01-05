package pt.psoft.g1.psoftg1.usermanagement.api;

import pt.psoft.g1.psoftg1.usermanagement.model.User;

public class UserViewAMQPMapper {
    public UserViewAMQP toUserViewAMQP(User user) {
        return new UserViewAMQP(user.getId(), user.getName().getName(), user.getUsername() , user.getVersion());
    }
}
