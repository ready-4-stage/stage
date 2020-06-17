package stage.common;

import stage.common.model.User;

public interface CommonUserService {
    default void transferFromOldToNew(User oldUser, User newUser) {
        if (newUser.getId() == null) {
            newUser.setId(oldUser.getId());
        }

        if (newUser.getUsername() == null) {
            newUser.setUsername(oldUser.getUsername());
        }

        if (newUser.getPassword() == null) {
            newUser.setPassword(oldUser.getPassword());
        }

        if (newUser.getMail() == null) {
            newUser.setMail(oldUser.getMail());
        }
    }
}
