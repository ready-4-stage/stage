package stage.server.lesson.type;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stage.common.model.LessonType;

import java.util.List;

@Service
public class LessonTypeService {
    private final LessonTypeRepository lessonTypeRepository;

    @Autowired
    public LessonTypeService(LessonTypeRepository lessonTypeRepository) {
        this.lessonTypeRepository = lessonTypeRepository;
    }

    public List<LessonType> getLessonTypes() {
        return lessonTypeRepository.getLessonTypes();
    }

    public LessonType getLessonType(Integer id) {
        return checkIsNull(id);
    }

    public void updateLessonType(Integer id, LessonType lessonType) {
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

    public void deleteLessonType(Integer id) {
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

    private LessonType checkIsNull(Integer id) {
        LessonType lessonType = lessonTypeRepository.getLessonType(id);
        if (lessonType == null) {
            throw new LessonTypeNotFoundException();
        }
        return lessonType;
    }
}
