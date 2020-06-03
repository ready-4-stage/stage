package stage.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * // TODO description
 *
 * @author Julian Drees, Tobias Fuchs, Yannick Kirschen, Cevin Steve Oehne, Tobias Tappert
 * @since 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student extends User {
    private String name;
    private String lastName;
    private String placeOfBirth;
    private String phone;
    private String address;
    private String iban;
    private LocalDate birthday;
}

