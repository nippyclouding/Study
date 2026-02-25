package spring.batch.tasklet2;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.StepContribution;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;

import java.io.File;
import java.time.LocalDate;

@RequiredArgsConstructor
public class FileCleanUpTasklet implements Tasklet {

    private final String rootPath;   // 파일을 삭제할 경로
    private final int retentionDays; // 보관 일자

    @Override
    public @Nullable RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        LocalDate cutOffDate = LocalDate.now().minusDays(retentionDays);// 금일을 기준으로 보관 일자를 뺸 날짜
        File folder = new File(rootPath);
        File[] files = folder.listFiles();

        if (files == null) return RepeatStatus.FINISHED;

        for (File file : files) {
            String name = file.getName();

            if (name.endsWith(".log") && name.length() >= 10) {
                // "access_2026-01-31.log" => "2026-01-31"
                name = name.substring(name.lastIndexOf("_") + 1, name.lastIndexOf("."));
                LocalDate fileDate = LocalDate.parse(name);

                if (fileDate.isBefore(cutOffDate)) {
                    file.delete();
                    System.out.println("삭제된 로그 파일 : " + name);
                }
            }
        }
        return RepeatStatus.FINISHED;
    }
}
