package stage.server.lesson;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import lombok.extern.log4j.Log4j2;
import stage.common.model.Lesson;
import stage.server.database.SqlConnection;
import stage.server.lesson.type.LessonTypeRepository;
import stage.server.room.RoomService;
import stage.server.student.StudentService;

import static stage.common.FileUtil.readFile;

@Log4j2
@Repository
public class LessonRepositoryH2 implements LessonRepository {
    private final String delete;
    private final String selectById;
    private final String insert;
    private final String update;
    private final String selectStudents;
    private final String generateId;

    private final SqlConnection connection;
    private final StudentService studentService;
    private final RoomService roomService;
    private final LessonTypeRepository lessonTypeRepository;
    //todo
    //    private final TeacherRepository teacherRepository;

    @Autowired
    public LessonRepositoryH2(SqlConnection connection,
        StudentService studentService, RoomService roomService,
        LessonTypeRepository lessonTypeRepository /*TeacherRepositroy teacherRepositroy*/) {
        this.connection = connection;
        this.studentService = studentService;
        this.roomService = roomService;
        this.lessonTypeRepository = lessonTypeRepository;
        //        this.teacherRepository = teacherRepository;
        delete = readFile("sql/lesson/delete.sql");
        insert = readFile("sql/lesson/insert.sql");
        selectById = readFile("sql/lesson/select_by_id.sql");
        update = readFile("sql/lesson/update.sql");
        generateId = readFile("sql/lesson/generate_Lesson_id.sql");
        selectStudents = readFile("sql/student/select.sql");
    }

    @Override
    public List<Lesson> getLessons() {
        List<Lesson> lessons = new ArrayList<>();
        try (ResultSet resultSet = connection.result(selectStudents)) {
            while (resultSet.next()) {
                lessons.add(buildLesson(resultSet));
            }
            connection.commit();
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
            connection.commit();
        } catch (SQLException ex) {
            log.error(ex);
        }
        return lesson;
    }

    @Override
    public Integer addLesson(Lesson lesson) {
        Integer id = -1;
        try {
            id = generateId(generateId);
            connection.update(insert, id, lesson.getBegin(), lesson.getEnd(),
                lesson.getRoom(), lesson.getTeacher(), lesson.getType(),
                lesson.getContent());

            connection.commit();
        } catch (SQLException ex) {
            log.error(ex);
        }
        return id;

    }

    @Override
    public void updateLesson(Integer id, Lesson lesson) {
        try {
            connection.update(update, lesson.getBegin(), lesson.getEnd(),
                lesson.getRoom(), lesson.getTeacher(), lesson.getType(),
                lesson.getContent(), id);

        } catch (SQLException ex) {
            log.error(ex);
        }
    }

    private Lesson buildLesson(ResultSet resultSet) throws SQLException {
        Lesson lesson = new Lesson();
        lesson.setId(resultSet.getInt("ID"));
        lesson.setBegin(
            LocalDateTime.from(resultSet.getDate("BEGIN").toLocalDate()));
        lesson.setEnd(
            LocalDateTime.from(resultSet.getDate("END").toLocalDate()));
        lesson.setRoom((roomService.getRoom(resultSet.getInt("ROOM_ID"))));
        //todo
        //        lesson.setTeacher(teacherRepository.getTeacher(resultSet.getInt("TEACHER_ID")));
        lesson.setType(
            (lessonTypeRepository.getLessonType(resultSet.getInt("ROOM_ID"))));
        lesson.setContent(resultSet.getString("CONTENT"));
        lesson.setStudents(studentService.getStudents());
        return lesson;
    }

    public Integer generateId(String username) {
        int id = -1;
        try (ResultSet rs = connection.result(generateId,
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
    public void deleteLesson(Integer id) {
        try {
            connection.update(delete, id);
            connection.commit();
        } catch (SQLException ex) {
            log.error(ex);
        }
    }
}

