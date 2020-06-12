package stage.server.lesson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import stage.common.model.Lesson;

import java.util.List;

@RestController
@RequestMapping("/v1/lesson")
public class LessonController {

    LessonService service;

    @Autowired
    public LessonController(LessonService service) {
        this.service = service;
    }

    @GetMapping
    public List<Lesson> get() {
        return service.getLessons();
    }

    @GetMapping("/{id}")
    public Lesson get(@PathVariable("id") Integer id) {
        return service.getLesson(id);
    }

    @PutMapping
    public LessonPutResponse put(@RequestBody Lesson lesson) {
        return new LessonPutResponse(service.addLesson(lesson));
    }

    @PatchMapping("/{id}")
    public void patch(@PathVariable("id") Integer id,
                      @RequestBody Lesson lesson) {
        service.updateLesson(id, lesson);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer id) {
        service.deleteLesson(id);
    }
}
