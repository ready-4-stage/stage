package stage.server.student;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;
import stage.common.model.*;
import stage.server.authentication.aop.*;
import stage.server.user.*;

@Log4j2
@Service
public class StudentService {
    private final StudentRepository repository;
    private final UserService userService;

    @Autowired
    public StudentService(StudentRepository repository,
        UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    @RequireAdminOrTeacher
    public List<Student> getStudents() {
        return anonymize(repository.getStudents());
    }

    @RequireAdminOrTeacher
    public Student getStudent(String username) {
        Student student = getStudentByIdOrUsername(username);
        if (student == null) {
            throw new StudentNotFoundException();
        }
        return anonymize(student);
    }

    @RequireAdmin
    public Integer addUser(Student student) {
        if (!userService.isUniqueUserName(student.getUsername())) {
            throw new UserAlreadyExistsException();
        }

        student.setId(null);
        student.setRole(Role.STUDENT);

        return repository.addStudent(student);
    }

    @RequireAdmin
    public void updateStudent(String usernameOrId, Student newStudent) {
        Student oldStudent = getStudentByIdOrUsername(usernameOrId);

        if (oldStudent == null) {
            throw new StudentNotFoundException();
        }

        newStudent.setId(oldStudent.getId());
        newStudent.setRole(oldStudent.getRole());

        updateStudent(oldStudent.getId(), oldStudent, newStudent);
    }

    @RequireAdmin
    public void deleteStudent(String id) {
        Student student = getStudentByIdOrUsername(id);
        if (student == null) {
            throw new StudentNotFoundException();
        }

        repository.deleteStudent(userService.getId(id));
    }

    private Student getStudentByIdOrUsername(String usernameOrId) {
        if (userService.isNumeric(usernameOrId)) {
            return repository.getStudent(Integer.parseInt(usernameOrId));
        } else {
            return repository.getStudent(usernameOrId);
        }
    }

    private void updateStudent(Integer id, Student oldStudent,
        Student newStudent) {
        userService.transferFromOldToNew(oldStudent, newStudent);

        if (newStudent.getLastName() == null) {
            newStudent.setLastName(oldStudent.getLastName());
        }

        if (newStudent.getFirstName() == null) {
            newStudent.setFirstName(oldStudent.getFirstName());
        }

        if (newStudent.getPlaceOfBirth() == null) {
            newStudent.setPlaceOfBirth(oldStudent.getPlaceOfBirth());
        }

        if (newStudent.getPhone() == null) {
            newStudent.setPhone(oldStudent.getPhone());
        }

        if (newStudent.getAddress() == null) {
            newStudent.setAddress(oldStudent.getAddress());
        }

        if (newStudent.getIban() == null) {
            newStudent.setIban(oldStudent.getIban());
        }

        if (newStudent.getBirthday() == null) {
            newStudent.setBirthday(oldStudent.getBirthday());
        }

        repository.updateStudent(id, oldStudent, newStudent);
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
