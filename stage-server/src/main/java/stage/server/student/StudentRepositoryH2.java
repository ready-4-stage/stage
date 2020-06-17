package stage.server.student;

import java.sql.Date;
import java.sql.*;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import lombok.extern.log4j.Log4j2;
import stage.common.model.*;
import stage.server.database.SqlConnection;

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
    private final String select;
    private final String selectById;
    private final String insert;
    private final String update;
    private final String delete;
    private final String sequence;

    private final SqlConnection connection;

    @Autowired
    StudentRepositoryH2(SqlConnection connection) {
        this.connection = connection;

        select = readFile("sql/student/select.sql");
        selectById = readFile("sql/student/select_by_id.sql");
        insert = readFile("sql/student/insert.sql");
        update = readFile("sql/student/update.sql");
        delete = readFile("sql/student/delete.sql");
        sequence = readFile("sql/student/sequence.sql");
    }

    @Override
    public List<Student> getStudents() {
        List<Student> students = new ArrayList<>();
        try (ResultSet resultSet = connection.result(select)) {
            while (resultSet.next()) {
                students.add(buildStudent(resultSet));
            }
        } catch (SQLException ex) {
            log.error(ex);
        }

        return students;
    }

    @Override
    public Student getStudent(Integer id) {
        Student student = null;
        try (ResultSet resultSet = connection.result(selectById, id)) {
            if (resultSet.next()) {
                student = buildStudent(resultSet);
            }
        } catch (SQLException ex) {
            log.error(ex);
        }
        return student;
    }

    @Override
    public Integer addStudent(Student student) {
        int id = -1;
        try (ResultSet res = connection.result(sequence)) {
            if (res.next()) {
                id = res.getInt(1);
                connection.update(insert, //
                    id, //
                    student.getUsername(), //
                    student.getPassword(), //
                    student.getRole().toString(), //
                    student.getMail(), //
                    id, //
                    student.getLastName(), //
                    student.getFirstName(), //
                    student.getPlaceOfBirth(), //
                    student.getPhone(), //
                    student.getAddress(), //
                    student.getAddress(), //
                    new Date(student.getBirthday().toEpochDay()));
            }
        } catch (SQLException ex) {
            log.error(ex);
        }
        return id;
    }

    @Override
    public void updateStudent(Integer id, Student oldStudent,
        Student newStudent) {
        try {
            connection.update(update, //
                newStudent.getUsername(), //
                newStudent.getPassword(), //
                newStudent.getMail(), //
                oldStudent.getRole().toString(), //
                id, //
                newStudent.getLastName(), //
                newStudent.getFirstName(), //
                newStudent.getPlaceOfBirth(), //
                newStudent.getPhone(), //
                newStudent.getAddress(), //
                newStudent.getIban(), //
                new Date(oldStudent.getBirthday().toEpochDay()), //
                id);
        } catch (SQLException ex) {
            log.error(ex);
        }
    }

    @Override
    public void deleteStudent(Integer id) {
        try {
            connection.update(delete, id, id);
        } catch (SQLException ex) {
            log.error(ex);
        }
    }

    private Student buildStudent(ResultSet resultSet) throws SQLException {
        Student student = new Student();
        student.setId(resultSet.getInt("ID"));
        student.setUsername(resultSet.getString("USERNAME"));
        student.setPassword(resultSet.getString("PWD"));
        student.setMail(resultSet.getString("MAIL"));
        student.setRole(Role.valueOf(resultSet.getString("ROLE_DESCRIPTION")));
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
