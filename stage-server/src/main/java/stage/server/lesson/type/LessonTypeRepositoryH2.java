package stage.server.lesson.type;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import stage.common.model.LessonType;
import stage.server.database.SqlConnection;

import javax.annotation.PostConstruct;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static stage.common.FileUtil.readFile;

@Log4j2
@Repository
public class LessonTypeRepositoryH2 implements LessonTypeRepository {
    private final SqlConnection connection;
    private String lessonTypeDelete;
    private String generateId;
    private String lessonTypeInsert;
    private String selectLessonTypes;
    private String selectLessonTypeById;
    private String lessonTypeUpdate;

    public LessonTypeRepositoryH2(SqlConnection connection) {
        this.connection = connection;
        lessonTypeDelete = readFile("sql/lessonType/lessonType_delete.sql");
        generateId = readFile("sql/lessonType/generate_lessonType_id.sql");
        lessonTypeInsert = readFile("sql/lessonType/lessonType_insert.sql");
        selectLessonTypes = readFile("sql/lessonType/lessonType_select_all.sql");
        selectLessonTypeById = readFile("sql/lessonType/lessonType_select_by_id.sql");
        lessonTypeUpdate = readFile("sql/lessonType/lessonType_update.sql");
    }

    @Override
    public List<LessonType> getLessonTypes() {
        List<LessonType> lessonTypes = new ArrayList<>();
        try (ResultSet resultSet = connection.result(selectLessonTypes)) {
            while (resultSet.next()) {
                lessonTypes.add(buildLessonType(resultSet));
            }
            connection.commit();
        } catch (SQLException ex) {
            log.error(ex);
        }
        return lessonTypes;
    }

    @Override
    public LessonType getLessonType(Integer id) {
        LessonType lessonType = null;
        try (ResultSet resultSet = connection.result(selectLessonTypes, id)) {
            if (resultSet.next()) {
                lessonType = buildLessonType(resultSet);
            }
            connection.commit();
        } catch (SQLException ex) {
            log.error(ex);
        }
        return lessonType;
    }

    @Override
    public Integer addLessonType(LessonType lessonType) {

        Integer id = -1;
        try {
            id = generateId();
            connection.update(lessonTypeInsert, id, lessonType.getDescription());

            connection.commit();
        } catch (SQLException ex) {
            log.error(ex);
        }
        return id;
    }


    @Override
    public void updateLessonType(Integer id, LessonType newLessonType) {
        try {
            connection.update(lessonTypeUpdate, newLessonType.getDescription());

        } catch (SQLException ex) {
            log.error(ex);
        }
    }

    @Override
    public void deleteLessonType(Integer id) {
        try {
            connection.update(lessonTypeDelete, id);
            connection.commit();
        } catch (SQLException ex) {
            log.error(ex);
        }
    }

    @PostConstruct
    public void onInitialize() {
        String createLessonTypeTableSql = readFile(
            "sql/lessonType/lessonType_table_create.sql");
        try {
            connection.update(createLessonTypeTableSql);
            connection.commit();
        } catch (SQLException e) {
            log.error("SQL error: {}", e.getMessage());
        }
    }

    private LessonType buildLessonType(ResultSet resultSet) throws SQLException {
        LessonType lessonType = new LessonType();
        lessonType.setId(resultSet.getInt("ID"));
        lessonType.setDescription(resultSet.getString("DESCRIPTION"));
        return lessonType;
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
