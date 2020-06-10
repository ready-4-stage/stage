package stage.server.authentication.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import stage.server.authentication.AuthenticationService;

@Aspect
@Component
public class RequireAdminAspect {
    private final AuthenticationService authentication;

    @Autowired
    public RequireAdminAspect(AuthenticationService authentication) {
        this.authentication = authentication;
    }

    @Around("@annotation(stage.server.authentication.aop.RequireAdmin)")
    public Object logExecutionTime(ProceedingJoinPoint join) throws Throwable {
        authentication.requireAdmin();
        return join.proceed();
    }
}
