package tig.server.status.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        response.put("environment", port == 8081 ? "BLUE" : "GREEN"); // 환경 정보
        response.put("port", port); // 현재 포트 정보
        return ResponseEntity.ok(response);
    }
}
