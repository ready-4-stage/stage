package stage.server.lesson;

import java.sql.*;
import java.time.*;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import lombok.extern.log4j.Log4j2;
import stage.common.model.*;
import stage.server.database.SqlConnection;

import static stage.common.FileUtil.readFile;

@Log4j2
@Repository
public class LessonRepositoryH2 implements LessonRepository {
    private final SqlConnection connection;

    private final String delete;
    private final String selectById;
    private final String insert;
    private final String update;
    private final String selectStudents;
    private final String sequence;

    @Autowired
    public LessonRepositoryH2(SqlConnection connection) {
        this.connection = connection;

        delete = readFile("sql/lesson/delete.sql");
        insert = readFile("sql/lesson/insert.sql");
        selectById = readFile("sql/lesson/select_by_id.sql");
        update = readFile("sql/lesson/update.sql");
        selectStudents = readFile("sql/student/select.sql");
        sequence = readFile("sql/student/sequence.sql");
    }

    @Override
    public List<Lesson> getLessons() {
        List<Lesson> lessons = new ArrayList<>();
        try (ResultSet resultSet = connection.result(selectStudents)) {
            while (resultSet.next()) {
                lessons.add(buildLesson(resultSet));
            }
        } catch (SQLException ex) {
            log.error(ex);
        }
        return lessons;
    }

    @Override
    public Lesson getLesson(Integer id) {
        Lesson lesson = null;
        try (ResultSet resultSet = connection.result(selectById, id)) {
            if (resultSet.next()) {
                lesson = buildLesson(resultSet);
            }
        } catch (SQLException ex) {
            log.error(ex);
        }
        return lesson;
    }

    @Override
    public Integer addLesson(Lesson lesson) {
        Integer id = -1;
        try (ResultSet res = connection.result(sequence)) {
            if (res.next()) {
                connection.update(insert, //
                    id, lesson.getBegin(), //
                    new Timestamp(lesson.getBegin()
                        .atZone(ZoneId.systemDefault())
                        .toEpochSecond()), //
                    new Timestamp(lesson.getEnd()
                        .atZone(ZoneId.systemDefault())
                        .toEpochSecond()), //
                    lesson.getTeacher().getId(), //
                    lesson.getType().getId(), //
                    lesson.getContent());
            }
        } catch (SQLException ex) {
            log.error(ex);
        }
        return id;

    }

    @Override
    public void updateLesson(Integer id, Lesson lesson) {
        try {
            connection.update(update, //
                new Timestamp(lesson.getBegin()
                    .atZone(ZoneId.systemDefault())
                    .toEpochSecond()), //
                new Timestamp(lesson.getEnd()
                    .atZone(ZoneId.systemDefault())
                    .toEpochSecond()), //
                lesson.getRoom().getId(), //
                lesson.getTeacher().getId(), //
                lesson.getType().getId(), //
                lesson.getContent(), //
                id);
        } catch (SQLException ex) {
            log.error(ex);
        }
    }

    private Lesson buildLesson(ResultSet result) throws SQLException {
        Lesson lesson = new Lesson();
        lesson.setId(result.getInt("LESSON_ID"));
        lesson.setBegin(
            LocalDateTime.from(result.getTimestamp("BEGIN").toLocalDateTime()));
        lesson.setEnd(
            LocalDateTime.from(result.getTimestamp("END").toLocalDateTime()));
        lesson.setContent(result.getString("CONTENT"));

        Room room = new Room();
        Teacher teacher = new Teacher();
        return lesson;
    }

    @Override
    public void deleteLesson(Integer id) {
        try {
            connection.update(delete, id);
            connection.commit();
        } catch (SQLException ex) {
            log.error(ex);
        }
    }
}

