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

    @Value("${APP_ENV:unknown}")
    private String appEnv;

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getStatus() {
        Map<String, Object> response = new HashMap<>();
        response.put("environment", appEnv);
        response.put("serverTime", LocalDateTime.now().toString());
        response.put("status", "UP");
        return ResponseEntity.ok(response);
    }
}
