package stage.database;

import java.util.List;
import stage.common.model.Student;

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

    void updateStudent(Integer id, Student newStudent);

    void deleteStudent(Integer id);

    Integer getId(String userName);

    default Student getStudent(String userName) {
        return getStudent(getId(userName));
    }

    default void updateStudent(String userName, Student newStudent) {
        updateStudent(getId(userName), newStudent);
    }

    default void deleteStudent(String userName) {
        deleteStudent(getId(userName));
    }
}
