package com.plog.demo.service.sign;

import com.plog.demo.config.JwtTokenProvider;
import com.plog.demo.dto.sign.LoginResponseDto;
import com.plog.demo.dto.user.UserDto;
import com.plog.demo.exception.CustomException;
import com.plog.demo.model.IdTable;
import com.plog.demo.repository.IdTableRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SignServiceImpl implements SignService{

    private final IdTableRepository idTableRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public UserDto register(UserDto userDto) throws CustomException {

        log.info("[addUser] 유저 저장 로직 시작");

        Optional<IdTable> findUser = idTableRepository.findById(userDto.getId());

        //유저 존재
        if(findUser.isPresent()){
            log.info("[addUser] 유저 이미 존재");
            throw new CustomException("이미 존재하는 유저입니다.");
        }

        //정상 로직
        IdTable user = IdTable.builder()
                .id(userDto.getId())
                .email(userDto.getEmail())
                .name(userDto.getName())
                .phonenum(userDto.getPhoneNum())
                .nickname(userDto.getNickname())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .role("USER")
                .build();

        try {
            idTableRepository.save(user);
        } catch (DataAccessException e) {
            log.info("[addUser] 데이터 베이스 접근 오류");
            throw new RuntimeException("데이터베이스 접근 중 오류가 발생했습니다.", e);
        }



        return userDto;
    }

    @Override
    public LoginResponseDto login(String userId, String password) throws CustomException {
        log.info("[login] 시작");

        Optional<IdTable> user = idTableRepository.findById(userId);

        //유저 존재
        if(user.isEmpty()){
            log.info("[login] 유저 존재 암함");
            throw new CustomException("존재 하지 않는 유저입니다.", HttpStatus.UNAUTHORIZED.value());
        }

        if(!passwordEncoder.matches(password, user.get().getPassword())){
            log.info("[login] 패스워드 불일치");
            throw new CustomException("패스워드 불일치", HttpStatus.BAD_REQUEST.value());
        }

        log.info("[login] 패스워드 일치");

        String accessToken = jwtTokenProvider.createAccessToken(userId);
        String refreshToken = jwtTokenProvider.createRefreshToken();

        LoginResponseDto loginResponseDto = LoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        return loginResponseDto;
    }
}