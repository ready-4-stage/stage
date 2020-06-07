package stage.common.model;

import java.time.LocalDate;
import lombok.*;

/**
 * // TODO description
 *
 * @author Julian Drees, Tobias Fuchs, Yannick Kirschen, Cevin Steve Oehne,
 * Tobias Tappert
 * @since 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student extends User {
    private String name;
    private String firstName;
    private String placeOfBirth;
    private String phone;
    private String address;
    private String iban;
    private LocalDate birthday;

    public Student(Student student) {
        super(student);
        name = student.getName();
        firstName = student.getFirstName();
        placeOfBirth = student.getPlaceOfBirth();
        phone = student.getPhone();
        address = student.getAddress();
        iban = student.getIban();
        birthday = student.getBirthday();
    }
}

