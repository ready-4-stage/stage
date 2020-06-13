package stage.common.model;

/**
 * According to the ERD, the {@link Role} defines the status of a {@link User}
 * and the rights they have.
 *
 * @author Julian Drees
 * @author Tobias Fuchs
 * @author Yannick Kirschen
 * @author Cevin Steve Oehne
 * @author Tobias Tappert
 * @since 1.0.0
 */
public enum Role {
    ADMIN,
    TEACHER,
    STUDENT
}
