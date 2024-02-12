package dnd.donworry.service.impl;

import dnd.donworry.domain.entity.User;
import dnd.donworry.domain.constants.ErrorCode;
import dnd.donworry.domain.dto.user.UserResponseDto;
import dnd.donworry.exception.CustomException;
import dnd.donworry.repository.RefreshTokenRepository;
import dnd.donworry.repository.UserRepository;
import dnd.donworry.service.UserService;
import dnd.donworry.util.CookieUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final CookieUtil cookieUtil;

    @Transactional
    public void deleteUser(String email) {
        Optional<User> user = userRepository.findUserByEmail(email);
        user.ifPresent(userRepository::delete);
        if (refreshTokenRepository.findById(email).isPresent()) {
            refreshTokenRepository.deleteById(email);
        }
    }

    @Transactional
    public UserResponseDto.UPDATE updateUserNickname(String email, String nickname) {
        User user = userRepository.findUserByEmail(email).get();

        if (user.getNickname().equals(nickname) || userRepository.existsByNickname(nickname)) {
            throw new CustomException(ErrorCode.EMAIL_DUPLICATION);
        }

        user.updateNickname(nickname);
        userRepository.save(user);

        return UserResponseDto.UPDATE.of(user);
    }

    @Transactional
    public void logout(String email, HttpServletResponse response) {
        cookieUtil.deleteCookie(response, "ACCESS_TOKEN", "REFRESH_TOKEN");
        refreshTokenRepository.deleteById(email);
    }
}
