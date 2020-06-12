package stage.server.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stage.common.model.User;

import java.util.regex.Pattern;

@Service
public class UserService {

    private final Pattern numericPattern = Pattern.compile("-?\\d+(\\.\\d+)?");

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUser(String id) {
        Integer numericId;
        if (isNumeric(id)) {
            numericId = Integer.parseInt(id);
        } else {
            numericId = userRepository.getId(id);
        }
        return userRepository.getUser(numericId);
    }

    private boolean isNumeric(String check) {
        return numericPattern.matcher(check).matches();
    }

}
