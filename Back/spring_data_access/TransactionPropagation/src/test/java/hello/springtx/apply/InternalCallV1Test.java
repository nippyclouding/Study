package hello.springtx.apply;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@SpringBootTest
public class InternalCallV1Test {

    @Autowired
    CallService callService;

    @Test
    void printProxy(){
        log.info("callService class={}", callService.getClass());
    }
    // callService class=class hello.springtx.apply.InternalCallV1Test$CallService$$SpringCGLIB$$0
    // $$SpringCGLIB$$0 : 프록시 객체

    @Test
    void internalCall(){
        callService.internal();
    } // tx active=true

    @Test
    void externalCall(){
        callService.external();
    } // tx active=true

    @TestConfiguration
    static class InternalCallV1Config{
        @Bean
        CallService callService(){
            return new CallService();
        }
    }

    @Slf4j
    static class CallService{
        void external(){
            log.info("call external");
            printTxInfo();
            internal(); // 트랜잭션이 동작하기 위해서는 프록시를 통해 접근해야 한다. 현재는 this.internal() => 트랜잭션 X
        }

        @Transactional
        void internal(){
            log.info("call internal");
            printTxInfo();
        }

        void printTxInfo(){
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive(); // 현재 스레드에 트랜잭션이 적용되었는지 여부
            log.info("tx active={}", txActive);
        }
    }
}
