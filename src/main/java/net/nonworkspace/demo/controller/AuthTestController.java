package net.nonworkspace.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
public class AuthTestController {

    @GetMapping("/admin")
    public ResponseEntity<String> testAdmin(@Parameter(hidden = true) @RequestHeader(
            name = "Authorization", required = false) String token) throws Exception {
        return ResponseEntity.ok("관리자용 API 접근 성공!");
    }

    @GetMapping("/user")
    public ResponseEntity<String> testUser(@Parameter(hidden = true) @RequestHeader(
            name = "Authorization", required = false) String token) throws Exception {
        return ResponseEntity.ok("유저용 API 접근 성공!");
    }
}
