package stage.server.lesson.type;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import stage.common.model.LessonType;

import java.util.List;

@RestController
@RequestMapping("/v1/lessonType")
public class LessonTypeController {
    private LessonTypeService service;

    @Autowired
    public LessonTypeController(LessonTypeService service) {
        this.service = service;
    }

    @GetMapping
    private List<LessonType> get() {
        return service.getLessonTypes();
    }

    @GetMapping("/{id}")
    public LessonType get(@PathVariable("id") Integer id) {
        return service.getLessonType(id);
    }

    @PutMapping
    public LessonTypePutResponse put(@RequestBody LessonType lessonType) {
        return new LessonTypePutResponse(service.addLessonType(lessonType));
    }

    @PatchMapping("/{id}")
    public void patch(@PathVariable("id") Integer id,
                      @RequestBody LessonType lessonType) {
        service.updateLessonType(id, lessonType);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer id) {
        service.deleteLessonType(id);
    }
}
