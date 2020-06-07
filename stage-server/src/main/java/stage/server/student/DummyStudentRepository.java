package stage.server.student;

import java.time.LocalDate;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import stage.common.model.*;
import stage.database.*;
import stage.server.user.DummyUserRepository;

@Repository
public class DummyStudentRepository implements StudentRepository {
    private final List<Student> students = new LinkedList<>();

    private final UserRepository userRepository;

    @Autowired
    public DummyStudentRepository(DummyUserRepository userRepository) {
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

        students.add(student1);
        students.add(student2);

        this.userRepository = userRepository;
    }

    @Override
    public List<Student> getStudents() {
        return students;
    }

    @Override
    public Student getStudent(Integer id) {
        for (Student student : students) {
            if (student.getId().equals(id)) {
                return student;
            }
        }
        return null;
    }

    @Override
    public Student getStudent(String userName) {
        for (Student student : students) {
            if (student.getUsername().equals(userName)) {
                return student;
            }
        }
        return null;
    }

    @Override
    public Integer addStudent(Student student) {
        student.setId(userRepository.generateId());
        students.add(student);
        userRepository.addUser(student);
        return student.getId();
    }

    @Override
    public void updateStudent(Integer id, Student newStudent) {
        for (Student student : students) {
            if (student.getId().equals(id)) {
                student.setUsername(newStudent.getUsername());
                student.setPassword(newStudent.getPassword());
                student.setMail(newStudent.getMail());
                student.setFirstName(newStudent.getFirstName());
                student.setName(newStudent.getName());
                student.setPlaceOfBirth(newStudent.getPlaceOfBirth());
                student.setPhone(newStudent.getPhone());
                student.setAddress(newStudent.getAddress());
                student.setIban(newStudent.getIban());
                student.setBirthday(newStudent.getBirthday());
            }
        }
    }

    @Override
    public void updateStudent(String userName, Student newStudent) {
        for (Student student : students) {
            if (student.getUsername().equals(userName)) {
                student.setUsername(newStudent.getUsername());
                student.setPassword(newStudent.getPassword());
                student.setMail(newStudent.getMail());
                student.setFirstName(newStudent.getFirstName());
                student.setName(newStudent.getName());
                student.setPlaceOfBirth(newStudent.getPlaceOfBirth());
                student.setPhone(newStudent.getPhone());
                student.setAddress(newStudent.getAddress());
                student.setIban(newStudent.getIban());
                student.setBirthday(newStudent.getBirthday());
            }
        }
    }

    @Override
    public void deleteStudent(Integer id) {
        students.remove(getStudent(id));
    }

    @Override
    public Integer getId(String userName) {
        return null;
    }
}