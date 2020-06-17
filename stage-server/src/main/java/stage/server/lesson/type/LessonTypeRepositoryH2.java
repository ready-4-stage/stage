package stage.server.lesson.type;

import java.sql.*;
import java.util.*;

import org.springframework.stereotype.Repository;

import lombok.extern.log4j.Log4j2;
import stage.common.model.LessonType;
import stage.server.database.SqlConnection;

import static stage.common.FileUtil.readFile;

@Log4j2
@Repository
public class LessonTypeRepositoryH2 implements LessonTypeRepository {
    private final SqlConnection connection;

    private final String delete;
    private final String insert;
    private final String select;
    private final String selectById;
    private final String update;
    private final String sequence;

    public LessonTypeRepositoryH2(SqlConnection connection) {
        this.connection = connection;

        delete = readFile("sql/lessonType/delete.sql");
        insert = readFile("sql/lessonType/insert.sql");
        select = readFile("sql/lessonType/select.sql");
        selectById = readFile("sql/lessonType/selectById.sql");
        update = readFile("sql/lessonType/update.sql");
        sequence = readFile("sql/lessonType/sequence.sql");
    }

    @Override
    public List<LessonType> getLessonTypes() {
        List<LessonType> lessonTypes = new ArrayList<>();
        try (ResultSet resultSet = connection.result(select)) {
            while (resultSet.next()) {
                lessonTypes.add(buildLessonType(resultSet));
            }
        } catch (SQLException ex) {
            log.error(ex);
        }
        return lessonTypes;
    }

    @Override
    public LessonType getLessonType(Integer id) {
        LessonType lessonType = null;
        try (ResultSet resultSet = connection.result(selectById, id)) {
            if (resultSet.next()) {
                lessonType = buildLessonType(resultSet);
            }
        } catch (SQLException ex) {
            log.error(ex);
        }
        return lessonType;
    }

    @Override
    public Integer addLessonType(LessonType lessonType) {
        int id = -1;
        try (ResultSet res = connection.result(sequence)) {
            if (res.next()) {
                id = res.getInt(1);
                connection.update(insert, id, lessonType.getDescription());
            }
        } catch (SQLException ex) {
            log.error(ex);
        }
        return id;
    }

    @Override
    public void updateLessonType(Integer id, LessonType newLessonType) {
        try {
            connection.update(update, newLessonType.getDescription());
        } catch (SQLException ex) {
            log.error(ex);
        }
    }

    @Override
    public void deleteLessonType(Integer id) {
        try {
            connection.update(delete, id);
        } catch (SQLException ex) {
            log.error(ex);
        }
    }

    private LessonType buildLessonType(ResultSet resultSet)
        throws SQLException {
        LessonType lessonType = new LessonType();
        lessonType.setId(resultSet.getInt("ID"));
        lessonType.setDescription(resultSet.getString("DESCRIPTION"));
        return lessonType;
    }
}
