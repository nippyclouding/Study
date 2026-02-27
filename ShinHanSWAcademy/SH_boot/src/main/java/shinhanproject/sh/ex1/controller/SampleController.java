package shinhanproject.sh.ex1.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import shinhanproject.sh.ex1.dto.SampleDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@Slf4j
public class SampleController {
    @GetMapping("/hello")
    public String[] hello() {
        return new String[]{"hello", "world"};
    }

    @GetMapping("/ex1")
    public void ex1(Model model) {
        model.addAttribute("name", "홍길동");
        log.info("ex1.....................");
    }

    @GetMapping("/ex2")
    public String ex2(Model model) {
        List<SampleDTO> list = IntStream.rangeClosed(1,20).asLongStream().mapToObj(i-> {
            SampleDTO dto = SampleDTO.builder()
                    .sno(i)
                    .first("First.."+i)
                    .last("Last.."+i)
                    .regTime(LocalDateTime.now())
                    .build();
            return dto;
        }).collect(Collectors.toList());
        model.addAttribute("list", list);
        return "sample/ex2";
    }

    @GetMapping("/exInline")
    public String exInline(RedirectAttributes redirectAttributes) {
        SampleDTO dto = SampleDTO.builder()
                .sno(100L)
                .first("First..100")
                .last("Last..100")
                .regTime(LocalDateTime.now())
                .build();
        redirectAttributes.addFlashAttribute("result", "success");
        redirectAttributes.addFlashAttribute("dto", dto);
        redirectAttributes.addFlashAttribute("name", "<i>홍길동</i>");
        return "redirect:/sample/ex3";
    }

    @GetMapping("/ex3")
    public void ex3(Model model) {
        log.info("ex3.....................");
    }
}
