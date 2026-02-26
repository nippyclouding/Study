package spring.batch.jobListener;

import org.springframework.stereotype.Component;

@Component
public class EmailProvider {
    public void send(String sender, String title, String content) {
        System.out.println("메일 발송 성공, 받는 사람 : " + sender);
        System.out.println("제목 : " + title);
        System.out.println("내용 : " + content);
    }
}
