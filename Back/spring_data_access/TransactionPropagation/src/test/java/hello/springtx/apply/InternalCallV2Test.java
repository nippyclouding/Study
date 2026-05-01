package hello.springtx.apply;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@SpringBootTest
public class InternalCallV2Test {
    @Autowired
    CallService callService;

    @Test
    void externalCall(){
        callService.external();
    }


    @TestConfiguration
    static class InternalCallV2Config{
        @Bean
        CallService callService(){
            return new CallService(innerService());
        }
        @Bean
        InternalService innerService(){
            return new InternalService();
        }
    }

    @Slf4j
    @RequiredArgsConstructor
    static class CallService{

        private final InternalService internalService;

        void external(){
            log.info("call external");
            printTxInfo();
            //internal(); //this.internal(), CallService 프록시를 통하지 않고 직접 실행 => 트랜잭션 적용 x
            internalService.internal(); // internalService 프록시를 통해 트랜잭션 실행 => 트랜잭션 적용 o
        }

        @Transactional
        void internal(){
            log.info("call internal");
            printTxInfo();
        }

        void printTxInfo(){
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx active={}", txActive);
        }
    }

    @Slf4j
    static class InternalService{

        @Transactional
        void internal(){
            log.info("call internal");
            printTxInfo();
        }

        void printTxInfo(){
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx active={}", txActive);
        }
    }
}
