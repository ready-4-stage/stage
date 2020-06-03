package stage.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * // TODO description
 *
 * @author Julian Drees, Tobias Fuchs, Yannick Kirschen, Cevin Steve Oehne, Tobias Tappert
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lesson {
    private Integer id;
    private LocalDateTime begin;
    private LocalDateTime end;
    private Room room;
    private Teacher teacher;
    private List<Student> students;
    private LessonType type;
    private String content;
}
