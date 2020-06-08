package stage.server.student;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import stage.common.model.Role;
import stage.common.model.Student;
import stage.database.StudentRepository;
import stage.server.database.SqlConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * // TODO description
 *
 * @author Julian Drees
 * @since 1.0.0
 */
@Log4j2
@Repository
public class StudentDao implements StudentRepository {

    private final SqlConnection sqlConnection;

    @Autowired
    public StudentDao(SqlConnection sqlConnection) {
        this.sqlConnection = sqlConnection;
    }

    @Override
    public List<Student> getStudents() {
        List<Student> students = new ArrayList<>();
        try (ResultSet resultSet = this.sqlConnection.result("select * from students inner join users on stundents.user_id = users.id")) {
            students.add(this.buildStudent(resultSet));
        } catch (SQLException ex) {
            log.error(ex);
        }
        return students;
    }

    @Override
    public Student getStudent(Integer id) {
        Student student = null;
        try (ResultSet resultSet = this.sqlConnection.result("select * from students inner join users on students.user_id = users.id")) {
            if (resultSet.next()) {
                student = this.buildStudent(resultSet);
            }
        } catch (SQLException ex) {
            log.error(ex);
        }
        return student;
    }

    @Override
    public Integer addStudent(Student student) {
        String usersSql = "insert into users (id, username, password, role_id, mail) VALUES (?, ?, ?, ?, ?);";
        String studentsSql = "insert into students (user_id, name, firstName, placeOfBirth, phone, address, iban, birthday) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";

        int id = -1;
        try {
            id = this.generateId();
            this.sqlConnection.update(usersSql, id, student.getUsername(), student.getPassword(), student.getRole().ordinal(), student.getMail());
            this.sqlConnection.update(studentsSql, id, student.getName(), student.getFirstName(), student.getPlaceOfBirth(), student.getPhone(), student.getAddress(), student.getAddress());
            // TODO birthday
        } catch (SQLException ex) {
            log.error(ex);
        }
        return id;
    }

    @Override
    public void updateStudent(Integer id, Student newStudent) {
        String updateUsersSql = "update users set userName = ?, password = ?, mail = ?, role = ? where id = ?;";
        String updateStudentSql = "update students set name = ?, firstName = ?, placeOfBirth = ?, phone = ?, address = ?, iban = ?, birthDay = ? where user_id = ?;";

        try {
            this.sqlConnection.update(updateUsersSql, newStudent.getUsername(), newStudent.getPassword(), newStudent.getMail(), newStudent.getRole().ordinal(), id);
            this.sqlConnection.update(updateStudentSql, newStudent.getName(), newStudent.getFirstName(), newStudent.getPlaceOfBirth(), newStudent.getPhone(), newStudent.getAddress(), newStudent.getIban());
            // TODO birthday
        } catch (SQLException ex) {
            log.error(ex);
        }
    }

    @Override
    public void deleteStudent(Integer id) {
        try {
            this.sqlConnection.update("delete from students where user_id = ?;", id);
            this.sqlConnection.update("delete from users where id = ?;", id);
        } catch (SQLException ex) {
            log.error(ex);
        }
    }

    @Override
    public Integer getId(String userName) {
        int id = -1;
        try (ResultSet rs = this.sqlConnection.result("select id from users where UPPER(username) = ?;", userName.toUpperCase())) {
            if (rs.next()) {
                id = rs.getInt("id");
            }
        } catch (SQLException ex) {
            log.error(ex);
        }
        return id;
    }

    private Student buildStudent(ResultSet resultSet) throws SQLException {
        Student student = new Student();
        student.setId(resultSet.getInt("id"));
        student.setUsername(resultSet.getString("userName"));
        student.setPassword(resultSet.getString("password"));
        student.setMail(resultSet.getString("mail"));
        student.setRole(Role.valueOf(resultSet.getString("role")));
        // TODO im ERD steht die ordinal drin

        student.setName(resultSet.getString("name"));
        student.setFirstName(resultSet.getString("firstName"));
        student.setPlaceOfBirth(resultSet.getString("placeOfBirth"));
        student.setPhone(resultSet.getString("phone"));
        student.setAddress(resultSet.getString("address"));
        student.setIban(resultSet.getString("iban"));
        student.setBirthday(resultSet.getDate("birthday").toLocalDate());
        return student;
    }

    private Integer generateId() throws SQLException {
        int id = 0;
        try (ResultSet rs = this.sqlConnection.result("select max(id) from users;")) {
            if (rs.next()) {
                id = rs.getInt(1);
            }
        }
        return id + 1;
    }
}
