package stage.server.lesson.type;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import stage.common.model.LessonType;

import java.util.List;

@Log4j2
@Service
public class LessonTypeService {
    private final LessonTypeRepository lessonTypeRepository;

    public LessonTypeService(LessonTypeRepository lessonTypeRepository) {
        this.lessonTypeRepository = lessonTypeRepository;
    }

    public List<LessonType> getLessonTypes() {
        return lessonTypeRepository.getLessonTypes();
    }

    LessonType getLessonType(Integer id) {
        return checkIsNull(id);
    }

    private LessonType checkIsNull(Integer id) {
        LessonType lessonType = lessonTypeRepository.getLessonType(id);
        if (lessonType == null) {
            throw new LessonTypeNotFoundException();
        }
        return lessonType;
    }

    void updateLessonType(Integer id, LessonType lessonType) {
        LessonType oldLesson = checkIsNull(id);
        lessonType.setId(oldLesson.getId());
        updateLessonTypeById(oldLesson, lessonType);
    }

    private void updateLessonTypeById(LessonType oldLessonType, LessonType newLessonType) {
        Integer id = newLessonType.getId();

        if (newLessonType.getDescription() == null) {
            newLessonType.setDescription(oldLessonType.getDescription());
        }

        lessonTypeRepository.updateLessonType(id, newLessonType);
    }

    void deleteLessonType(Integer id) {
        if (checkIsNotNullBoolean(id))
            lessonTypeRepository.deleteLessonType(id);
    }


    public Integer addLessonType(LessonType lessonType) {
        lessonType.setId(null);
        return lessonTypeRepository.addLessonType(lessonType);
    }

    private boolean checkIsNotNullBoolean(Integer id) {
        LessonType lessonType = lessonTypeRepository.getLessonType(id);
        if (lessonType != null) {
            return true;
        }
        throw new LessonTypeNotFoundException();
    }
}
