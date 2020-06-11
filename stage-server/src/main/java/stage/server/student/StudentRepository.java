package stage.server.student;

import java.util.List;

import stage.common.model.*;
import stage.server.Repository;

/**
 * // TODO description
 *
 * @author Julian Drees, Tobias Fuchs, Yannick Kirschen, Cevin Steve Oehne,
 * Tobias Tappert
 * @since 1.0.0
 */
public interface StudentRepository extends Repository {
    List<Student> getStudents();

    Student getStudent(Integer id);

    Integer addStudent(Student student);

    void updateStudent(Integer id, Student oldStudent, Student newStudent);

    void deleteStudent(Integer id);

    @Deprecated(since = "1.0.0", forRemoval = true)
    Integer getId(String username);

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
