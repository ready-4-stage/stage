package stage.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * // TODO description
 *
 * @author Julian Drees, Tobias Fuchs, Yannick Kirschen, Cevin Steve Oehne, Tobias Tappert
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Teacher extends User {
    private String name;
    private String lastName;
    private String placeOfBirth;
    private String phone;
    private String address;
    private String iban;
    private LocalDate birthday;
    private Double hourlyRate;
    private List<String> qualifications;
}
