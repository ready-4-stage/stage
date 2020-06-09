package stage.server.student;

import java.util.List;
import stage.common.model.*;

/**
 * // TODO description
 *
 * @author Julian Drees, Tobias Fuchs, Yannick Kirschen, Cevin Steve Oehne,
 * Tobias Tappert
 * @since 1.0.0
 */
public interface StudentRepository {
    List<Student> getStudents();

    Student getStudent(Integer id);

    Integer addStudent(Student student);

    void updateStudent(Integer id, Student oldStudent, Student newStudent);

    void deleteStudent(Integer id);

    Integer getId(String username);

    Integer getId(Role role);

    Role getRole(Integer id);

    boolean isUniqueUsername(String username);

    default Student getStudent(String username) {
        return getStudent(getId(username));
    }

    default void updateStudent(String username, Student oldStudent,
        Student newStudent) {
        updateStudent(getId(username), oldStudent, newStudent);
    }

    default void deleteStudent(String username) {
        deleteStudent(getId(username));
    }
}
