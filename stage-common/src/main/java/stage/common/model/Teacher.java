package stage.common.model;

import java.time.LocalDate;
import java.util.*;
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
    private String name;
    private String firstName;
    private String placeOfBirth;
    private String phone;
    private String address;
    private String iban;
    private LocalDate birthday;
    private Double hourlyRate;
    private List<String> qualifications;

    public Teacher(Teacher teacher) {
        super(teacher);
        name = teacher.getName();
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
