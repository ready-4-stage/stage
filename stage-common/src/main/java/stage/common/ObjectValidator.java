package stage.common;

import java.util.*;
import javax.validation.*;

import org.springframework.stereotype.Service;

/**
 * The {@link ObjectValidator} uses the <code>javax-validation</code> api to
 * validate a POJO based on its annotations.
 *
 * @author Julian Drees
 * @author Tobias Fuchs
 * @author Yannick Kirschen
 * @author Cevin Steve Oehne
 * @author Tobias Tappert
 * @since 1.0.0
 */
@Service
public class ObjectValidator {
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    /**
     * Validates a POJO based on its annotations.
     *
     * @param t   The POJO to validate.
     * @param <T> Type of the POJO.
     * @return A list of violations. If the list is empty, there are no
     * violations.
     */
    public <T> List<String> validate(T t) {
        List<String> violations = new LinkedList<>();
        for (ConstraintViolation<T> violation : validator.validate(t)) {
            violations.add(violation.getMessage());
        }
        return violations;
    }
}
