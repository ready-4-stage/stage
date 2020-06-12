package stage.server.lesson;

import stage.common.model.Lesson;
import stage.server.Repository;

import java.util.List;

/**
 * // TODO description
 *
 * @author Julian Drees, Tobias Fuchs, Yannick Kirschen, Cevin Steve Oehne,
 * Tobias Tappert
 * @since 1.0.0
 */
public interface LessonRepository extends Repository {
    List<Lesson> getLessons();

    Lesson getLesson(Integer id);

    Integer addLesson(Lesson lesson);

    void updateLesson(Integer id, Lesson lesson);

    void deleteLesson(Integer id);
}
