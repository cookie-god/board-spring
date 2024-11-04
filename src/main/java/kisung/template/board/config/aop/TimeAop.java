package kisung.template.board.config.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
@Aspect
public class TimeAop {
  private ThreadLocal<String> idHolder = new ThreadLocal<>();

  @Around("execution(* kisung.template.board.controller..*.*(..))")
  public Object timeLogging(ProceedingJoinPoint pjp) throws Throwable {
    long startTimeMs = System.currentTimeMillis();
    String signature = pjp.getSignature().toShortString();
    String id = getId();

    try {
      log.warn("[{}] [{}] started", id, signature);
      Object result = pjp.proceed();
      log.warn("[{}] [{}] ended. time = {}ms", id, signature, System.currentTimeMillis() - startTimeMs);

      return result;
    } catch (Exception e) {
      log.error("[{}] [{}] exception occurred. time = {}ms, ex={}", id, signature, System.currentTimeMillis() - startTimeMs, e.toString());

      throw e;
    } finally {
      if (signature.toLowerCase().contains("controller"))
        removeId();
    }
  }

  private String getId() {
    String id = idHolder.get();
    if (id == null) {
      id = UUID.randomUUID().toString().substring(0, 8);
      idHolder.set(id);
    }
    return id;
  }

  private void removeId() {
    idHolder.remove();
  }
}
