package stage.server.lesson;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stage.common.model.Lesson;

import java.util.List;

@Log4j2
@Service
public class LessonService {

    private final LessonRepository repository;

    @Autowired
    public LessonService(LessonRepository repository) {
        this.repository = repository;
    }

    List<Lesson> getLessons() {
        return repository.getLessons();
    }

    Lesson getLesson(Integer id) {
        return checkIsNull(id);
    }

    Integer addLesson(Lesson lesson) {
        lesson.setId(null);
        return repository.addLesson(lesson);
    }

    void updateLesson(Integer id, Lesson lesson) {
        Lesson oldLesson = checkIsNull(id);
        lesson.setId(oldLesson.getId());
        updateLessonById(oldLesson, lesson);
    }

    private void updateLessonById(Lesson oldLesson, Lesson newLesson) {
        Integer id = newLesson.getId();

        if (newLesson.getBegin() == null) {
            newLesson.setBegin(oldLesson.getBegin());
        }
        if (newLesson.getEnd() == null) {
            newLesson.setEnd(oldLesson.getEnd());

        }
        if (newLesson.getRoom() == null) {
            newLesson.setRoom(oldLesson.getRoom());
        }
        if (newLesson.getTeacher() == null) {
            newLesson.setTeacher(oldLesson.getTeacher());
        }

        if (newLesson.getStudents() == null) {
            newLesson.setStudents(oldLesson.getStudents());
        }

        if (newLesson.getType() == null) {
            newLesson.setType(oldLesson.getType());
        }

        if (newLesson.getContent() == null) {
            newLesson.setContent(oldLesson.getContent());
        }
        repository.updateLesson(id, newLesson);
    }

    private Lesson checkIsNull(Integer id) {
        Lesson lesson = repository.getLesson(id);
        if (lesson == null) {
            throw new LessonNotFoundException();
        }
        return lesson;
    }

    private boolean checkIsNotNullBoolean(Integer id) {
        Lesson lesson = repository.getLesson(id);
        if (lesson != null) {
            return true;
        }
        throw new LessonNotFoundException();
    }

    void deleteLesson(Integer id) {
        if (checkIsNotNullBoolean(id))
            repository.deleteLesson(id);
    }
}
