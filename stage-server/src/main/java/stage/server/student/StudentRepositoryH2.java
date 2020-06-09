package stage.server.student;

import javax.annotation.PostConstruct;

import java.sql.Date;
import java.sql.*;
import java.util.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import stage.common.model.*;
import stage.server.database.SqlConnection;

import static stage.common.FileUtil.readFile;

/**
 * // TODO description
 *
 * @author Julian Drees, Tobias Fuchs, Yannick Kirschen, Cevin Steve Oehne,
 * Tobias Tappert
 * @since 1.0.0
 */
@Log4j2
@Repository
public class StudentRepositoryH2 implements StudentRepository {
    private final String initialFill;
    private final String selectStudents;
    private final String selectStudentById;
    private final String userInsert;
    private final String studentInsert;
    private final String userUpdate;
    private final String studentUpdate;
    private final String userDelete;
    private final String studentDelete;
    private final String userSelectId;
    private final String usernameUnique;
    private final String generateId;
    private final String roleSelectId;
    private final String roleSelectById;

    private final SqlConnection connection;

    @Autowired
    public StudentRepositoryH2(SqlConnection connection) {
        this.connection = connection;

        initialFill = readFile("sql/initial_fill.sql");
        selectStudents = readFile("sql/student/student_select_all.sql");
        selectStudentById = readFile("sql/student/student_select_by_id.sql");
        userInsert = readFile("sql/user/user_insert.sql");
        studentInsert = readFile("sql/student/student_insert.sql");
        userUpdate = readFile("sql/user/user_update.sql");
        studentUpdate = readFile("sql/student/student_update.sql");
        userDelete = readFile("sql/user/user_delete.sql");
        studentDelete = readFile("sql/student/student_delete.sql");
        userSelectId = readFile("sql/user/user_select_id.sql");
        usernameUnique = readFile("sql/username_unique.sql");
        generateId = readFile("sql/generate_id.sql");
        roleSelectId = readFile("sql/role/role_select_id.sql");
        roleSelectById = readFile("sql/role/role_select_by_id.sql");
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
            id = generateId();
            connection.update(userInsert, id, student.getUsername(),
                student.getPassword(), getId(student.getRole()),
                student.getMail());
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
            connection.update(userUpdate, newStudent.getUsername(),
                newStudent.getPassword(), newStudent.getMail(),
                getId(oldStudent.getRole()), id);
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
            connection.update(userDelete, id);
            connection.commit();
        } catch (SQLException ex) {
            log.error(ex);
        }
    }

    @Override
    public Integer getId(String username) {
        int id = -1;
        try (ResultSet rs = connection.result(userSelectId,
            username.toUpperCase())) {
            if (rs.next()) {
                id = rs.getInt("ID");
            }
            connection.commit();
        } catch (SQLException ex) {
            log.error(ex);
        }
        return id;
    }

    @Override
    public Integer getId(Role role) {
        int id = -1;
        try (ResultSet rs = connection.result(roleSelectId,
            role.name().toUpperCase())) {
            if (rs.next()) {
                id = rs.getInt("ID");
            }
            connection.commit();
        } catch (SQLException ex) {
            log.error(ex);
        }
        return id;
    }

    @Override
    public Role getRole(Integer id) {
        try (ResultSet rs = connection.result(roleSelectById, id)) {
            if (rs.next()) {
                String name = rs.getString("DESCRIPTION").toUpperCase();
                return Role.valueOf(name);
            }
            connection.commit();
        } catch (SQLException ex) {
            log.error(ex);
        }
        return null;
    }

    @Override
    public boolean isUniqueUsername(String username) {
        try (ResultSet rs = connection.result(usernameUnique,
            username.toUpperCase())) {
            if (rs.next()) {
                return false;
            }
            connection.commit();
        } catch (SQLException e) {
            log.error("Unable to perform SQL request: {}", e.getMessage());
        }
        return true;
    }

    private Student buildStudent(ResultSet resultSet) throws SQLException {
        Student student = new Student();
        student.setId(resultSet.getInt("ID"));
        student.setUsername(resultSet.getString("USERNAME"));
        student.setPassword(resultSet.getString("PASSWORD"));
        student.setMail(resultSet.getString("MAIL"));
        student.setRole(getRole(resultSet.getInt("ROLE_ID")));
        student.setLastName(resultSet.getString("LAST_NAME"));
        student.setFirstName(resultSet.getString("FIRST_NAME"));
        student.setPlaceOfBirth(resultSet.getString("PLACE_OF_BIRTH"));
        student.setPhone(resultSet.getString("PHONE"));
        student.setAddress(resultSet.getString("ADDRESS"));
        student.setIban(resultSet.getString("IBAN"));
        student.setBirthday(resultSet.getDate("BIRTHDAY").toLocalDate());
        return student;
    }

    private Integer generateId() throws SQLException {
        int id = 0;
        try (ResultSet rs = connection.result(generateId)) {
            if (rs.next()) {
                id = rs.getInt(1);
            }
        }
        connection.commit();
        return id + 1;
    }
}
