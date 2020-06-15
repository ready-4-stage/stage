package stage.common.model;

import java.time.LocalDate;
import javax.validation.constraints.*;

import lombok.*;

/**
 * According to the ERD, the {@link Student} represents a music student working
 * with the software.
 * <p>
 * It extends {@link User} for some additional attributes.
 *
 * @author Julian Drees
 * @author Tobias Fuchs
 * @author Yannick Kirschen
 * @author Cevin Steve Oehne
 * @author Tobias Tappert
 * @since 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student extends User {
    @NotBlank(message = "The last name must not be blank.")
    private String lastName;

    @NotBlank(message = "The first name must not be blank.")
    private String firstName;

    @NotBlank(message = "The place of birth must not be blank.")
    private String placeOfBirth;

    @NotBlank(message = "The phone must not be blank.")
    private String phone;

    @NotBlank(message = "The address must not be blank.")
    private String address;

    @NotBlank(message = "The phone iban not be blank.")
    private String iban;

    @NotNull(message = "The birthday must not be null.")
    private LocalDate birthday;

    public Student(Student student) {
        super(student);
        lastName = student.getLastName();
        firstName = student.getFirstName();
        placeOfBirth = student.getPlaceOfBirth();
        phone = student.getPhone();
        address = student.getAddress();
        iban = student.getIban();
        birthday = student.getBirthday();
    }
}

