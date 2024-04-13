package com.plog.demo.service.sign;

import com.plog.demo.dto.UserDto;
import com.plog.demo.exception.CustomException;
import com.plog.demo.model.IdTable;
import com.plog.demo.repository.IdTableRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SignServiceImpl implements SignService{

    private final IdTableRepository idTableRepository;

    @Override
    public UserDto addUser(UserDto userDto) throws CustomException {

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
                .phoneNum(userDto.getPhoneNum())
                .nickname(userDto.getNickname())
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
    public UserDto login(UserDto userDto) {
        return null;
    }
}
