package stage.server.student;

import java.util.List;

import stage.common.model.Student;

/**
 * The {@link StudentRepository} provides access to the student database.
 *
 * @author Julian Drees
 * @author Tobias Fuchs
 * @author Yannick Kirschen
 * @author Cevin Steve Oehne
 * @author Tobias Tappert
 * @since 1.0.0
 */
interface StudentRepository {
    /**
     * Finds all students.
     *
     * @return All students.
     */
    List<Student> getStudents();

    /**
     * Finds a student.
     *
     * @param id The ID of the student.
     * @return A specific student.
     */
    Student getStudent(Integer id);

    /**
     * Creates a new student.
     *
     * @param student The student to add. The ID is being ignored.
     * @return The auto-generated ID of the student.
     */
    Integer addStudent(Student student);

    /**
     * Updates a student.
     *
     * @param id         ID of the student to update.
     * @param oldStudent The student's old values.
     * @param newStudent The student's new value. The ID is being ignored.
     */
    void updateStudent(Integer id, Student oldStudent, Student newStudent);

    /**
     * Removes a student.
     *
     * @param id ID of the student.
     */
    void deleteStudent(Integer id);
}
