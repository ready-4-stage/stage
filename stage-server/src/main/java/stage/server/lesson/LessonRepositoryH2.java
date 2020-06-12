package stage.server.lesson;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import stage.common.model.Lesson;
import stage.server.database.SqlConnection;
import stage.server.lesson.type.LessonTypeRepository;
import stage.server.room.RoomRepository;
import stage.server.student.StudentRepository;

import javax.annotation.PostConstruct;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static stage.common.FileUtil.readFile;

@Log4j2
@Repository
public class LessonRepositoryH2 implements LessonRepository {

    private final String initialFill;
    private final String lessonDelete;
    private final String selectLessons;
    private final String selectLessonById;
    private final String lessonInsert;
    private final String lessonUpdate;
    private final String selectStudents;
    private final String teacherSelectById;
    private final String lessonTypeSelectById;
    private final String generateId;
    private final String roomSelectById;


    private final SqlConnection connection;
    private final StudentRepository studentRepository;
    private final RoomRepository roomRepository;
    private final LessonTypeRepository lessonTypeRepository;
    //todo
//    private final TeacherRepository teacherRepository;


    @Autowired
    public LessonRepositoryH2(SqlConnection connection, StudentRepository studentRepository, RoomRepository roomRepository , LessonTypeRepository lessonTypeRepository /*TeacherRepositroy teacherRepositroy*/) {
        this.connection = connection;
        this.studentRepository = studentRepository;
        this.roomRepository = roomRepository;
        this.lessonTypeRepository = lessonTypeRepository;
//        this.teacherRepository = teacherRepository;

        initialFill = readFile("sql/initial_fill.sql");
        lessonDelete = readFile("sql/lesson/lesson_delete.sql");
        lessonInsert = readFile("sql/lesson/lesson_insert.sql");
        selectLessons = readFile("sql/lesson/lesson_select_all.sql");
        selectLessonById = readFile("sql/lesson/lesson_select_by_id.sql");
        lessonUpdate = readFile("sql/lesson/lesson_update.sql");
        generateId = readFile("sql/generate_Lesson_id.sql");
        selectStudents = readFile("sql/student/student_select_all.sql");
        teacherSelectById = readFile("sql/teacher/teacher_select_id.sql");
        lessonTypeSelectById = readFile("sql/lessonType/lessonType_select_id.sql");
        roomSelectById = readFile("sql/room/room_select_by_id.sql");
    }

    @Override
    @PostConstruct
    public void onInitialize() {
        String createLessonTableSql = readFile("sql/lesson/lesson_table_create.sql");
        String createStudentTableSql = readFile("sql/student/student_table_create.sql");
        String createTeacherTableSql = readFile("sql/teacher/teacher_table_create.sql");
        String createRoomTableSql = readFile("sql/room/room_table_create.sql");
        String createLessonTypeTableSql = readFile("sql/lessonType/lessonType_table_create.sql");


        try {
            connection.update(createStudentTableSql);
            connection.update(createTeacherTableSql);
            connection.update(createRoomTableSql);
            connection.update(createLessonTypeTableSql);
            connection.update(createLessonTableSql);

            connection.update(initialFill);

            connection.commit();
        } catch (SQLException e) {
            log.error("SQL error: {}", e.getMessage());
        }
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
        try (ResultSet resultSet = connection.result(selectLessonById, id)) {
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
            connection.update(lessonInsert, id, lesson.getBegin(),
                lesson.getEnd(), lesson.getRoom(), lesson.getTeacher(),
                lesson.getType(), lesson.getContent());

            connection.commit();
        } catch (SQLException ex) {
            log.error(ex);
        }
        return id;

    }

    @Override
    public void updateLesson(Integer id, Lesson lesson) {
        try {
            connection.update(lessonUpdate, lesson.getBegin(),
                lesson.getEnd(), lesson.getRoom(), lesson.getTeacher(), lesson.getType(),
                lesson.getContent(), id);

        } catch (SQLException ex) {
            log.error(ex);
        }
    }

    private Lesson buildLesson(ResultSet resultSet) throws SQLException {
        Lesson lesson = new Lesson();
        lesson.setId(resultSet.getInt("ID"));
        lesson.setBegin(LocalDateTime.from(resultSet.getDate("BEGIN").toLocalDate()));
        lesson.setEnd(LocalDateTime.from(resultSet.getDate("END").toLocalDate()));
        lesson.setRoom((roomRepository.getRoom(resultSet.getInt("ROOM_ID"))));
        //todo
//        lesson.setTeacher(teacherRepository.getTeacher(resultSet.getInt("TEACHER_ID")));
        lesson.setType((lessonTypeRepository.getLessonType(resultSet.getInt("ROOM_ID"))));
        lesson.setContent(resultSet.getString("CONTENT"));
        lesson.setStudents(studentRepository.getStudents());
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
            connection.update(lessonDelete, id);
            connection.commit();
        } catch (SQLException ex) {
            log.error(ex);
        }
    }
}
