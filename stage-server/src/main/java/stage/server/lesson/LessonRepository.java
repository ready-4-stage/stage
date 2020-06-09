package stage.server.lesson;

import java.util.List;
import stage.common.model.Lesson;

/**
 * // TODO description
 *
 * @author Julian Drees, Tobias Fuchs, Yannick Kirschen, Cevin Steve Oehne,
 * Tobias Tappert
 * @since 1.0.0
 */
public interface LessonRepository {
    List<Lesson> getLessons();

    Lesson getLesson(Integer id);

    Integer addLesson(Lesson lesson);

    void updateLesson(Integer id, Lesson lesson);

    void deleteLesson(Integer id);
}
