package com.example.server.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController implements org.springframework.boot.web.servlet.error.ErrorController {
//    @RequestMapping(value = "/{path:[^\\.]*}")
//    public String forward(@PathVariable String path) {
//        return "forward:/index.html";
//    }
    @RequestMapping("/error")
    public ResponseEntity<String> handleError() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Custom Error: Page not found or unsupported method.");
    }
}

