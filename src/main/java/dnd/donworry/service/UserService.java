package dnd.donworry.service;

import dnd.donworry.domain.dto.user.UserResponseDto;
import jakarta.servlet.http.HttpServletResponse;

public interface UserService {
    void deleteUser(String email);

    UserResponseDto.UPDATE updateUserNickname(String email, String nickname);

    void logout(String email, HttpServletResponse response);

    UserResponseDto.READ getUser(String email);
}
