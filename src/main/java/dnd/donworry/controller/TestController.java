package dnd.donworry.controller;

import dnd.donworry.domain.constants.ResponseCode;
import dnd.donworry.domain.dto.test.TestResponseDto;
import dnd.donworry.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody TestResponseDto testResponseDto) {
        String username = "test"; // Authentication.getName()으로 변경
        testService.saveAfterSignup(username, testResponseDto);
        return ResponseCode.TEST_CREATED.toResponse(null);
    }
}
