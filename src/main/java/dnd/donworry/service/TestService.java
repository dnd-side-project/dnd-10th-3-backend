package dnd.donworry.service;


import dnd.donworry.domain.dto.test.TestResponseDto;


public interface TestService {
    void saveAfterSignup(String username, TestResponseDto testResponseDto);

}
