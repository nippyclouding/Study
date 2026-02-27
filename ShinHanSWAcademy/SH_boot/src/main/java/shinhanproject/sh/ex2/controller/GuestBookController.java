package shinhanproject.sh.ex2.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/guestBook")
@Slf4j
public class GuestBookController {
    @GetMapping("/")
    public String list() {
        log.info("list....");
        return "/guestBook/list";
    }
}
