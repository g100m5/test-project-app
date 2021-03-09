package com.answer;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

    @Controller
    public class ServiceController {

        private static final String template = "you asked %s, but our programmers still working on it";
        private final AtomicLong counter = new AtomicLong();

        @GetMapping("/ask")
        @ResponseBody
        public AnsweringDTO answer(@RequestParam(name="question", required=false, defaultValue="some question") String question) {
            return new AnsweringDTO(counter.incrementAndGet(), String.format(template, question));
        }
    }
