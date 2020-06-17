package stage.server.user;

import java.util.*;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import stage.common.authentication.*;
import stage.common.model.User;

@Service
public class UserService implements JwtUserDatabase {
    private final Pattern numericPattern = Pattern.compile("-?\\d+(\\.\\d+)?");
    private final UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public Integer getId(String username) {
        return repository.getId(username);
    }

    public List<User> getUsers() {
        return anonymize(repository.getUsers());
    }

    public User getUser(String id) {
        Integer numericId;
        if (isNumeric(id)) {
            numericId = Integer.parseInt(id);
        } else {
            numericId = repository.getId(id);
        }
        return anonymize(repository.getUser(numericId));
    }

    public Integer addUser(User user) {
        if (!repository.isUniqueUserName(user.getUsername())) {
            throw new UserAlreadyExistsException();
        }
        user.setId(null);
        return repository.addUser(user);
    }

    public void updateUser(String id, User user) {
        User oldUser = getUser(id);
        if (oldUser == null) {
            throw new UserNotFoundException();
        }
        user.setId(oldUser.getId());
        user.setRole(oldUser.getRole());
        updateUser(oldUser.getId(), oldUser, user);
    }

    public void deleteUser(String id) {
        User user = getUser(id);
        if (user == null) {
            throw new UserNotFoundException();
        }
        repository.deleteUser(user.getId());
    }

    public boolean isUniqueUserName(String username) {
        return repository.isUniqueUserName(username);
    }

    public void transferFromOldToNew(User oldUser, User newUser) {
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

    public boolean isNumeric(String check) {
        return numericPattern.matcher(check).matches();
    }

    @Override
    public JwtUser assertAndGet(String username) {
        return getUserWithPassword(username);
    }

    public List<User> anonymize(List<User> users) {
        List<User> copies = new LinkedList<>();
        for (User user : users) {
            copies.add(anonymize(user));
        }
        return copies;
    }

    public User anonymize(User user) {
        User copy = new User(user);
        copy.setPassword(null);
        return copy;
    }

    private User getUserWithPassword(String id) {
        Integer numericId;
        if (isNumeric(id)) {
            numericId = Integer.parseInt(id);
        } else {
            numericId = repository.getId(id);
        }
        return repository.getUser(numericId);
    }

    private void updateUser(Integer id, User oldUser, User newUser) {
        transferFromOldToNew(oldUser, newUser);
        repository.updateUser(id, newUser);
    }
}
