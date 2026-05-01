package FrontController.demo.frontcontroller.v1;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface ControllerV1 {
    // 요청을 받고 응답을 내보낸다
    void process(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException;
}
