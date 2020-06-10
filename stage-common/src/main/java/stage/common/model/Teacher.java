package stage.common.model;

import java.time.LocalDate;
import java.util.*;
import javax.validation.constraints.*;

import lombok.*;

/**
 * // TODO description
 *
 * @author Julian Drees, Tobias Fuchs, Yannick Kirschen, Cevin Steve Oehne,
 * Tobias Tappert
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Teacher extends User {
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

    @NotNull(message = "The hourly rate must not be null.")
    private Double hourlyRate;

    @NotNull(message = "The qualifications must not be null.")
    private List<String> qualifications;

    public Teacher(Teacher teacher) {
        super(teacher);
        lastName = teacher.getLastName();
        firstName = teacher.getFirstName();
        placeOfBirth = teacher.getPlaceOfBirth();
        phone = teacher.getPhone();
        address = teacher.getAddress();
        iban = teacher.getIban();
        birthday = teacher.getBirthday();
        hourlyRate = teacher.getHourlyRate();
        qualifications = new LinkedList<>(teacher.getQualifications());
    }
}
