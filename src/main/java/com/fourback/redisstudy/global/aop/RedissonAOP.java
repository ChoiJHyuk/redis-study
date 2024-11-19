package com.fourback.redisstudy.global.aop;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Aspect
@Component
@RequiredArgsConstructor
public class RedissonAOP {
    private final RedissonClient redissonClient;

    @Around("execution(public void com.fourback.redisstudy.domain.item.service.ItemCommandService.create(String, ..))")
    public void locking(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        String lockName = "lock:" + args[2].toString();
        RLock lock = redissonClient.getLock(lockName);

        try {
            boolean lockable = lock.tryLock(1000, TimeUnit.MILLISECONDS);
            if (!lockable) {
                System.out.println("lock fail");
                return;
            }
            System.out.println("lock ok");
            joinPoint.proceed();
        } catch (InterruptedException e) {
            System.out.println("lock interrupted");
            e.printStackTrace();
        } finally {
            System.out.println("lock end");
            lock.unlock();
        }
    }
}
