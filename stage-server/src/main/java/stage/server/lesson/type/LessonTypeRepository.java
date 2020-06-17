package stage.server.lesson.type;

import stage.common.model.LessonType;

import java.util.List;

/**
 * // TODO description
 *
 * @author Julian Drees, Tobias Fuchs, Yannick Kirschen, Cevin Steve Oehne,
 * Tobias Tappert
 * @since 1.0.0
 */
public interface LessonTypeRepository {
    List<LessonType> getLessonTypes();

    LessonType getLessonType(Integer id);

    Integer addLessonType(LessonType lessonType);

    void updateLessonType(Integer id, LessonType newLessonType);

    void deleteLessonType(Integer id);
}
