package stage.common.authentication;

@FunctionalInterface
public interface JwtUserDatabase {
    JwtUser assertAndGet(String username);
}
