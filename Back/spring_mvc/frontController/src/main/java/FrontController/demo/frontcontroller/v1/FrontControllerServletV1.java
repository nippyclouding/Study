package FrontController.demo.frontcontroller.v1;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "frontControllerServletV1", urlPatterns = "/front-controller/v1/*")
public class FrontControllerServletV1 extends HttpServlet {
    private Map<String, ControllerV1> controllerV1Map = new HashMap<>();

    // 생성자
    public FrontControllerServletV1() {
        controllerV1Map.put("/front-controller/v1/members/new-form", new MemberFormControllerV1());
        controllerV1Map.put("/front-controller/v1/members/save", new MemberSaveControllerV1());
        controllerV1Map.put("/front-controller/v1/members", new MemberListControllerV1());
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("FrontControllerServletV1.service");

        // 요청 URI 조회
        String requestURI = req.getRequestURI();

        // 맵에서 현재 요청 URI를 처리할 수 있는 컨트롤러 확인, 반환
        ControllerV1 controllerV1 = controllerV1Map.get(requestURI);

        // 컨트롤러 유효성 검증
        if (controllerV1 == null) {
            resp.setStatus(HttpServletResponse.SC_FOUND);
            return;
        }

        // 처리 가능한 컨트롤러에 요청 정보, 응답 정보 전달
        controllerV1.process(req, resp);
    }
}
