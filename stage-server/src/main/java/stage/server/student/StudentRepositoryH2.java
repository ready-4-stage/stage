package stage.server.student;

import java.sql.Date;
import java.sql.*;
import java.util.*;
import javax.annotation.PostConstruct;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import stage.common.model.Student;
import stage.server.database.SqlConnection;
import stage.server.role.RoleRepository;
import stage.server.user.UserRepository;

import static stage.common.FileUtil.readFile;

/**
 * The {@link StudentRepositoryH2} provides access to an H2 database.
 *
 * @author Julian Drees
 * @author Tobias Fuchs
 * @author Yannick Kirschen
 * @author Cevin Steve Oehne
 * @author Tobias Tappert
 * @since 1.0.0
 */
@Log4j2
@Repository
class StudentRepositoryH2 implements StudentRepository {
    private final String initialFill;
    private final String selectStudents;
    private final String selectStudentById;
    private final String studentInsert;
    private final String studentUpdate;
    private final String studentDelete;

    private final SqlConnection connection;

    // TODO: Replace repositories by services
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    StudentRepositoryH2(SqlConnection connection, UserRepository userRepository,
        RoleRepository roleRepository) {
        this.connection = connection;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;

        initialFill = readFile("sql/initial_fill.sql");
        selectStudents = readFile("sql/student/student_select_all.sql");
        selectStudentById = readFile("sql/student/student_select_by_id.sql");
        studentInsert = readFile("sql/student/student_insert.sql");
        studentUpdate = readFile("sql/student/student_update.sql");
        studentDelete = readFile("sql/student/student_delete.sql");
    }

    @PostConstruct
    public void initialize() {
        String createRoleTableSql = readFile("sql/role/role_table_create.sql");
        String createUserTableSql = readFile("sql/user/user_table_create.sql");
        String createStudentTableSql = readFile(
            "sql/student/student_table_create.sql");

        try {
            connection.update(createRoleTableSql);
            connection.update(createUserTableSql);
            connection.update(createStudentTableSql);

            // TODO: darf nur in einem Service ausgeführt werden, ansonsten aufteilen
            connection.update(initialFill);
            connection.commit();
        } catch (SQLException e) {
            log.error("SQL error: {}", e.getMessage());
        }
    }

    @Override
    public List<Student> getStudents() {
        List<Student> students = new ArrayList<>();
        try (ResultSet resultSet = connection.result(selectStudents)) {
            while (resultSet.next()) {
                students.add(buildStudent(resultSet));
            }
            connection.commit();
        } catch (SQLException ex) {
            log.error(ex);
        }

        return students;
    }

    @Override
    public Student getStudent(Integer id) {
        Student student = null;
        try (ResultSet resultSet = connection.result(selectStudentById, id)) {
            if (resultSet.next()) {
                student = buildStudent(resultSet);
            }
            connection.commit();
        } catch (SQLException ex) {
            log.error(ex);
        }
        return student;
    }

    @Override
    public Integer addStudent(Student student) {
        int id = -1;
        try {
            id = userRepository.addUser(student);
            connection.update(studentInsert, id, student.getLastName(),
                student.getFirstName(), student.getPlaceOfBirth(),
                student.getPhone(), student.getAddress(), student.getAddress(),
                new Date(student.getBirthday().toEpochDay()));
            connection.commit();
        } catch (SQLException ex) {
            log.error(ex);
        }
        return id;
    }

    @Override
    public void updateStudent(Integer id, Student oldStudent,
        Student newStudent) {
        try {
            userRepository.updateUser(id, newStudent);
            connection.update(studentUpdate, newStudent.getLastName(),
                newStudent.getFirstName(), newStudent.getPlaceOfBirth(),
                newStudent.getPhone(), newStudent.getAddress(),
                newStudent.getIban(),
                new Date(oldStudent.getBirthday().toEpochDay()), id);
            connection.commit();
        } catch (SQLException ex) {
            log.error(ex);
        }
    }

    @Override
    public void deleteStudent(Integer id) {
        try {
            connection.update(studentDelete, id);
            connection.commit();

            userRepository.deleteUser(id);
        } catch (SQLException ex) {
            log.error(ex);
        }
    }

    private Student buildStudent(ResultSet resultSet) throws SQLException {
        Student student = new Student();
        student.setId(resultSet.getInt("ID"));
        student.setUsername(resultSet.getString("USERNAME"));
        student.setPassword(resultSet.getString("PASSWORD"));
        student.setMail(resultSet.getString("MAIL"));
        student.setRole(roleRepository.getRole(resultSet.getInt("ROLE_ID")));
        student.setLastName(resultSet.getString("LAST_NAME"));
        student.setFirstName(resultSet.getString("FIRST_NAME"));
        student.setPlaceOfBirth(resultSet.getString("PLACE_OF_BIRTH"));
        student.setPhone(resultSet.getString("PHONE"));
        student.setAddress(resultSet.getString("ADDRESS"));
        student.setIban(resultSet.getString("IBAN"));
        student.setBirthday(resultSet.getDate("BIRTHDAY").toLocalDate());
        return student;
    }
}
