package stage.server.lesson;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import stage.common.model.Lesson;

@RestController
@RequestMapping("/v1/lesson")
public class LessonController {
    private final LessonService service;

    @Autowired
    public LessonController(LessonService service) {
        this.service = service;
    }

    @GetMapping
    public List<Lesson> get() {
        return service.getLessons();
    }

    @GetMapping("/{id}")
    public Lesson get(@PathVariable("id") Integer id, HttpServletResponse res) {
        Lesson lesson = service.getLesson(id);

        if (lesson == null) {
            res.setStatus(404);
            return null;
        }

        return lesson;
    }

    @PutMapping
    public LessonPutResponse put(@RequestBody Lesson lesson) {
        return new LessonPutResponse(service.addLesson(lesson));
    }

    @PatchMapping("/{id}")
    public void patch(@PathVariable("id") Integer id,
        @RequestBody Lesson lesson, HttpServletResponse res) {
        try {
            service.updateLesson(id, lesson);
        } catch (LessonNotFoundException e) {
            res.setStatus(404);
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer id,
        HttpServletResponse res) {
        try {
            service.deleteLesson(id);
        } catch (LessonNotFoundException e) {
            res.setStatus(404);
        }
    }
}
