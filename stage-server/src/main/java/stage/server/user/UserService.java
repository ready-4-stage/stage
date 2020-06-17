package stage.server.user;

import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import stage.common.CommonUserService;
import stage.common.authentication.*;
import stage.common.model.User;

@Service
public class UserService implements JwtUserDatabase, CommonUserService {
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
        return repository.getUsers();
    }

    public User getUser(String id) {
        Integer numericId;
        if (isNumeric(id)) {
            numericId = Integer.parseInt(id);
        } else {
            numericId = repository.getId(id);
        }
        return repository.getUser(numericId);
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

    private boolean isNumeric(String check) {
        return numericPattern.matcher(check).matches();
    }

    @Override
    public JwtUser assertAndGet(String username) {
        return getUser(username);
    }

    private void updateUser(Integer id, User oldUser, User newUser) {
        transferFromOldToNew(oldUser, newUser);
        repository.updateUser(id, newUser);
    }
}
