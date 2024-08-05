package net.nonworkspace.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/")
    public ResponseEntity<String> adminIndex(@Parameter(hidden = true) @RequestHeader(
            name = "Authorization") String token) throws Exception {
        return ResponseEntity.ok("관리자용 API 접근 성공!");
    }
}
