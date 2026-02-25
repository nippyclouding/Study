package spring.batch.tasklet2;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LogGenerator {
    private static final String ROOT_PATH = "./test-logs";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void main(String[] args) throws IOException {
        File dir = new File(ROOT_PATH);

        if(!dir.exists()) {
            dir.mkdirs(); // 현재 파일 아래에 test-logs 파일 생성
        }

        // 파일 이름에 날짜를 포함하여 생성 (2026.02.25.log)
        createLogFile(dir, "access", 2); // 2일 전
        createLogFile(dir, "access", 0); // 0일 전 = 금일
        createLogFile(dir, "service", 50); // 50일 전
        createLogFile(dir, "service", 100); // 100일 전

        // 날짜 패턴이 없는 파일 생성, system_config.conf.log (conf.2026.02.25_log 처럼 중간에 날짜 패턴이 없다)
        createLogFile(dir, "system_config.conf", -1);

        System.out.println("Finish to create test log " + ROOT_PATH);
    }

    private static void createLogFile(File dir, String prefix, int daysAgo) throws IOException {
        String filename;

        if (daysAgo == -1) {
            // 날짜 패턴이 없는 일반 파일
            filename = prefix;
        } else {
            // 날짜 패턴 적용: prefix_yyyy-MM-dd.log
            LocalDate targetDate = LocalDate.now().minusDays(daysAgo);
            String dateStr = targetDate.format(DATE_TIME_FORMATTER);
            filename = prefix + "_" + dateStr + ".log";
        }

        File file = new File(dir, filename);

        if (file.createNewFile()) {
            System.out.println("create new file: " + filename);
        } else {
            System.out.println("already exists: " + filename);
        }
    }
}
