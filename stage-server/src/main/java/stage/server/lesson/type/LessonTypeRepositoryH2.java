package stage.server.lesson.type;

import java.util.List;

import org.springframework.stereotype.Repository;
import stage.common.model.LessonType;

// TODO: Implementation
@Repository
public class LessonTypeRepositoryH2 implements LessonTypeRepository {
    @Override
    public List<LessonType> getLessonTypes() {
        return null;
    }

    @Override
    public LessonType getLessonType(Integer id) {
        return null;
    }

    @Override
    public Integer addLessonType(LessonType lessonType) {
        return null;
    }

    @Override
    public void updateLessonType(Integer id, LessonType newLessonType) {

    }

    @Override
    public void deleteLessonType(Integer id) {

    }
}
