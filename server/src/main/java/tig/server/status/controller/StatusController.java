package tig.server.status.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/deploy")
public class StatusController {

    @Value("${server.port}")
    private int port;

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getStatus() {
        Map<String, Object> response = new HashMap<>();

        // 환경 정보
        response.put("environment", port == 8081 ? "BLUE" : "GREEN");
        response.put("port", port);


        // 서버 상태 정보
        response.put("serverTime", LocalDateTime.now().toString());
        response.put("status", "UP"); // 기본적으로 정상 상태를 표시

        return ResponseEntity.ok(response);
    }
}
