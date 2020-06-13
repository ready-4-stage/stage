package stage.server.user;

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

    public User getUser(String id) {
        Integer numericId;
        if (isNumeric(id)) {
            numericId = Integer.parseInt(id);
        } else {
            numericId = repository.getId(id);
        }
        return repository.getUser(numericId);
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
}
