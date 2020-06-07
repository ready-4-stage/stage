package stage.server.student;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import stage.common.model.Student;

@RestController
@RequestMapping("/v1/student")
public class StudentController {
    private final StudentService service;

    @Autowired
    public StudentController(StudentService service) {
        this.service = service;
    }

    @GetMapping
    public List<Student> get() {
        return service.getStudents();
    }

    @GetMapping("/{id}")
    public Student get(@PathVariable("id") String id) {
        return service.getStudent(id);
    }

    @PutMapping
    public StudentPutResponse put(@RequestBody Student student) {
        return new StudentPutResponse(service.addUser(student));
    }

    @PatchMapping("/{id}")
    public void patch(@PathVariable("id") String id,
        @RequestBody Student student) {
        service.updateStudent(id, student);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") String id) {
        service.deleteStudent(id);
    }
}
