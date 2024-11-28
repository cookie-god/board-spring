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
  // 리턴 타입은 자유로움, 패키지명 정의, 가로챌 메서드 어떤 클래스든 가능, 가로챌 메서드 어떤 메서드든 가능, 매개변수 어떤 것이라도 가능
  public Object timeLogging(ProceedingJoinPoint joinPoint) throws Throwable {
    long startTimeMs = System.currentTimeMillis();
    String signature = joinPoint.getSignature().toShortString();
    String id = getId();

    try {
      log.warn("[{}] [{}] started", id, signature);
      Object result = joinPoint.proceed();
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
