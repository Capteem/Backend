package com.plog.demo.service.user;


import com.plog.demo.dto.user.UserChangeDto;
import com.plog.demo.dto.user.UserInfoDto;
import com.plog.demo.exception.CustomException;
import com.plog.demo.model.IdTable;
import com.plog.demo.repository.IdTableRepository;
import com.plog.demo.service.confirm.ConfirmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService{

    private final IdTableRepository idTableRepository;
    private final PasswordEncoder passwordEncoder;
    private final ConfirmService confirmService;

    @Override
    public void changeUserInfo(UserInfoDto userInfoDto) throws CustomException {
        log.info("[changeUserInfo] 유저 정보 변경 서비스 로직 시작");
        IdTable idTable = idTableRepository.findById(userInfoDto.getId()).orElseThrow(() -> new CustomException("존재하지 않는 회원입니다.", HttpStatus.NOT_FOUND.value()));
        try {
            idTable.setNickname(userInfoDto.getNickname());
            idTable.setPhonenum(userInfoDto.getPhoneNum());
            idTable.setEmail(userInfoDto.getEmail());
            idTableRepository.save(idTable);
        } catch (Exception e) {
            log.info("[changeUserInfo] 데이터 베이스 접근 오류");
            throw new CustomException("데이터베이스 접근 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @Override
    public UserInfoDto getUserInfo(String userId) throws CustomException {
        log.info("[getUserInfo] 유저 정보 조회 서비스 로직 시작");
        IdTable idTable = idTableRepository.findById(userId).orElseThrow(() -> new CustomException("존재하지 않는 회원입니다.", HttpStatus.NOT_FOUND.value()));
        return UserInfoDto.builder()
                .id(idTable.getId())
                .email(idTable.getEmail())
                .nickname(idTable.getNickname())
                .phoneNum(idTable.getPhonenum())
                .build();
    }

    @Override
    public void changeUserPassword(UserChangeDto userChangeDto) throws CustomException {
        log.info("[changeUserPassword] 유저 비밀번호 변경 서비스 로직 시작");
        IdTable idTable = idTableRepository.findById(userChangeDto.getId()).orElseThrow(() -> new CustomException("존재하지 않는 회원입니다.", HttpStatus.NOT_FOUND.value()));
        try{
            idTable.setPassword(passwordEncoder.encode(userChangeDto.getPassword()));
            idTableRepository.save(idTable);
        } catch (Exception e) {
            log.info("[changeUserPassword] 데이터 베이스 접근 오류");
            throw new CustomException("데이터베이스 접근 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @Override
    public boolean checkUserPassword(UserChangeDto userChangeDto) throws CustomException {
        log.info("[checkUserPassword] 유저 비밀번호 확인 서비스 로직 시작");
        IdTable idTable = idTableRepository.findById(userChangeDto.getId()).orElseThrow(() -> new CustomException("존재하지 않는 회원입니다.", HttpStatus.NOT_FOUND.value()));
        return passwordEncoder.matches(userChangeDto.getPassword(), idTable.getPassword());
    }
    
    //ID 찾기
    @Override
    public String checkUserEmail(String email) throws CustomException {
        log.info("[checkUserEmail] 유저 이메일 중복 확인 서비스 로직 시작");
        try{
            IdTable idTable = idTableRepository.findByEmail(email);
            if(idTable != null){
                confirmService.joinEmail(email);
                return "인증번호가 전송되었습니다.";
            }else{
                throw new CustomException("아이디와 이메일이 맞지 않습니다.", HttpStatus.BAD_REQUEST.value());
            }
        } catch (Exception e) {
            log.info("[checkUserEmail] 데이터 베이스 접근 오류");
            throw new CustomException("데이터베이스 접근 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
    
    //비밀번호 찾기
    @Override
    public String checkUserEmailAndId(String email, String id) throws CustomException {
        try {
            IdTable idTable = idTableRepository.findById(id).orElseThrow(() -> new CustomException("존재하지 않는 회원입니다.", HttpStatus.NOT_FOUND.value()));
            if(!idTable.getEmail().equals(email)){
                throw new CustomException("아이디와 이메일이 맞지 않습니다.", HttpStatus.BAD_REQUEST.value());
            }
            confirmService.joinEmail(email);
            return "인증번호가 전송되었습니다.";
        } catch (Exception e) {
            log.info("[checkUserEmail] database connect error");
            throw new CustomException("데이터베이스 접근 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

}
