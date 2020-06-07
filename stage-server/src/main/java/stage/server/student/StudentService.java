package stage.server.student;

import java.util.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stage.common.model.*;
import stage.database.*;
import stage.server.user.UserAlreadyExistsException;

@Log4j2
@Service
public class StudentService {
    private final StudentRepository repository;
    private final UserRepository userRepository;

    @Autowired
    public StudentService(StudentRepository repository,
        UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public List<Student> getStudents() {
        // TODO: authentication (admin + teacher only)

        return anonymize(repository.getStudents());
    }

    public Student getStudent(String username) {
        // TODO: authentication (admin + teacher only)

        Student student = getStudentByIdOrUsername(username);
        if (student == null) {
            throw new StudentNotFoundException();
        }
        return anonymize(student);
    }

    public Integer addUser(Student student) {
        // TODO: authentication (admin only)

        if (!userRepository.isUniqueUsername(student.getUsername())) {
            throw new UserAlreadyExistsException();
        }

        student.setId(null);
        student.setRole(Role.STUDENT);

        return repository.addStudent(student);
    }

    public void updateStudent(String id, Student newStudent) {
        // TODO: authentication (admin only)

        Student oldStudent = getStudentByIdOrUsername(id);
        if (oldStudent == null) {
            throw new StudentNotFoundException();
        }

        newStudent.setId(oldStudent.getId());
        newStudent.setRole(oldStudent.getRole());

        updateStudentByIdOrUsername(id, newStudent);
    }

    public void deleteStudent(String id) {
        // TODO: authentication (admin only)

        Student student = getStudentByIdOrUsername(id);
        if (student == null) {
            throw new StudentNotFoundException();
        }

        deleteStudentByIdOrUsername(id);
    }

    private Student getStudentByIdOrUsername(String username) {
        // TODO: replace exception by something else
        try {
            Integer id = Integer.parseInt(username);
            return repository.getStudent(id);
        } catch (NumberFormatException e) {
            return repository.getStudent(username);
        }
    }

    private void updateStudentByIdOrUsername(String username,
        Student newStudent) {
        // TODO: replace exception by something else
        try {
            Integer id = Integer.parseInt(username);
            repository.updateStudent(id, newStudent);
        } catch (NumberFormatException e) {
            repository.updateStudent(username, newStudent);
        }
    }

    private void deleteStudentByIdOrUsername(String username) {
        // TODO: replace exception by something else
        try {
            Integer id = Integer.parseInt(username);
            repository.deleteStudent(id);
        } catch (NumberFormatException e) {
            repository.deleteStudent(username);
        }
    }

    private List<Student> anonymize(List<Student> users) {
        List<Student> copies = new LinkedList<>();
        for (Student students : users) {
            copies.add(anonymize(students));
        }
        return copies;
    }

    private Student anonymize(Student user) {
        Student copy = new Student(user);
        copy.setPassword(null);
        return copy;
    }
}
