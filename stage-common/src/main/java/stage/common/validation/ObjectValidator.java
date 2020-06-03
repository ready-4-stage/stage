package stage.common.validation;

import java.util.List;

/**
 * // TODO description
 *
 * @author Julian Drees, Tobias Fuchs, Yannick Kirschen, Cevin Steve Oehne, Tobias Tappert
 * @since 1.0.0
 */
public interface ObjectValidator {
    List<String> validate(Object o);
}
