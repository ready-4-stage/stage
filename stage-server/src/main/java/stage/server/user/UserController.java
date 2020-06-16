package stage.server.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import stage.common.model.User;

import java.util.List;

/**
 * // TODO description
 *
 * @author Julian Drees
 * @since 15.06.2020
 */
@RestController
@RequestMapping("v1/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> get() {
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public User get(@PathVariable("id") String id) {
        return userService.getUser(id);
    }

    @PutMapping("/{id}")
    public UserPutResponse put(@RequestBody User user) {
        return new UserPutResponse(userService.addUser(user));
    }

    @PatchMapping("/{id}")
    public void patch(@PathVariable("id") String id, @RequestBody User user) {
        userService.updateUser(id, user);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") String id) {
        userService.deleteUser(id);
    }

}
