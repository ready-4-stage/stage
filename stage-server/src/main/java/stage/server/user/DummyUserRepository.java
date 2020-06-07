package stage.server.user;

import java.time.LocalDate;
import java.util.*;
import org.springframework.stereotype.Repository;
import stage.common.model.*;
import stage.database.UserRepository;

@Repository("stage.repository.user")
public class DummyUserRepository implements UserRepository {
    private final List<User> users = new LinkedList<>();

    public DummyUserRepository() {
        Student student1 = new Student();
        student1.setId(0);
        student1.setUsername("peter123");
        student1.setPassword("1234");
        student1.setMail("peter@mail.com");
        student1.setRole(Role.STUDENT);
        student1.setName("Pan");
        student1.setFirstName("Peter");
        student1.setPlaceOfBirth("Cologne");
        student1.setPhone("110");
        student1.setAddress("Am Domkloster 4");
        student1.setIban("xxx");
        student1.setBirthday(LocalDate.now());

        Student student2 = new Student();
        student2.setId(1);
        student2.setUsername("rappert");
        student2.setPassword("1234");
        student2.setMail("rappert@mail.com");
        student2.setRole(Role.STUDENT);
        student2.setName("Rappert");
        student2.setFirstName("Tobias");
        student2.setPlaceOfBirth("Cologne");
        student2.setPhone("112");
        student2.setAddress("Unter der Deutzer Brücke");
        student2.setIban("xxx");
        student2.setBirthday(LocalDate.now());

        users.add(student1);
        users.add(student2);
    }

    @Override
    public List<User> getUsers() {
        return users;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends User> T getUser(Integer id) {
        for (User user : users) {
            if (user.getId().equals(id)) {
                // TODO check unchecked
                return (T) user;
            }
        }
        return null;
    }

    @Override
    public Integer addUser(User user) {
        user.setId(generateId());
        users.add(user);
        return user.getId();
    }

    @Override
    public void updateUser(Integer id, User newUser) {

    }

    @Override
    public void deleteUser(Integer id) {

    }

    @Override
    public Integer getId(String userName) {
        return null;
    }

    @Override
    public boolean isUniqueUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public Integer generateId() {
        Integer max = 0;
        for (User user : users) {
            if (user.getId() > max) {
                max = user.getId();
            }
        }
        return max + 1;
    }
}
