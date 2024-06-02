package com.plog.demo.service.admin;

import com.plog.demo.common.ComplaintStatus;
import com.plog.demo.common.UserStatus;
import com.plog.demo.dto.admin.AdminProviderApproveDto;
import com.plog.demo.dto.admin.AdminProviderDto;
import com.plog.demo.dto.admin.AdminRequestDto;
import com.plog.demo.dto.complaint.ComplaintReplyDto;
import com.plog.demo.exception.CustomException;
import com.plog.demo.model.ComplaintAnswerTable;
import com.plog.demo.model.ComplaintTable;
import com.plog.demo.model.IdTable;
import com.plog.demo.model.ProviderTable;
import com.plog.demo.repository.ComplaintAnswerTableRepository;
import com.plog.demo.repository.ComplaintTableRepository;
import com.plog.demo.repository.IdTableRepository;
import com.plog.demo.repository.ProviderTableRepository;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.Optional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService{

    private final ProviderTableRepository providerTableRepository;
    private final IdTableRepository idTableRepository;
    private final ComplaintAnswerTableRepository complaintAnswerTableRepository;
    private final ComplaintTableRepository complaintTableRepository;

    @Override
    @Operation(summary = "유저 및 제공자 상태 변경", description = "유저 및 제공자의 상태를 변경합니다.")
    public void changeUserStatus(AdminRequestDto adminRequestDto) throws CustomException {
        IdTable idTable= idTableRepository.findById(adminRequestDto.getUserId()).orElseThrow(() -> new CustomException("not exist user.", HttpStatus.BAD_REQUEST.value()));

        if(idTable.getStatus() == adminRequestDto.getStatus()){
            log.error("[changeUserStatus] not match user status.");
            throw new CustomException("유저 상태가 같습니다.", HttpStatus.BAD_REQUEST.value());
        }

        try{
            idTable.setStatus(adminRequestDto.getStatus());
            idTableRepository.save(idTable);
        }catch (Exception e){
            log.info("[changeUserStatus] db connection error.");
            throw new CustomException("데이터베이스 접근 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @Override
    @Operation(summary = "제공자 상태 변경", description = "제공자의 상태를 변경합니다.")
    public void changeProviderStatus(AdminProviderDto adminProviderDto) throws CustomException {
        IdTable idTable = idTableRepository.findById(adminProviderDto.getUserId()).orElseThrow(() -> new CustomException("not exist user.", HttpStatus.BAD_REQUEST.value()));
        ProviderTable providerTable = providerTableRepository.findByUserIdAndProviderId(idTable, adminProviderDto.getProviderId()).orElseThrow(() -> new CustomException("not exist provider.", HttpStatus.BAD_REQUEST.value()));
        log.info("[changeProviderStatus] provider status : {}", providerTable);
        if(providerTable.getProviderStatus() == adminProviderDto.getProviderStatus()){
            log.error("[changeProviderStatus] not match provider status.");
            throw new CustomException("제공자 상태가 같습니다.", HttpStatus.BAD_REQUEST.value());
        }
        if(providerTable.getProviderStatus() == UserStatus.ACTIVE.getCode() && adminProviderDto.getProviderStatus() == UserStatus.STOP.getCode()){
            log.error("[changeProviderStatus] already active provider.");
            throw new CustomException("이미 활성화된 제공자입니다.", HttpStatus.BAD_REQUEST.value());
        }

        try{
            providerTable.setProviderStatus(adminProviderDto.getProviderStatus());
            if(adminProviderDto.getProviderStatus() == UserStatus.ACTIVE.getCode()){
                idTable.setRole("PROVIDER");
            }
            providerTableRepository.save(providerTable);
            idTableRepository.save(idTable);
        }catch (Exception e){
            log.info("[changeProviderStatus] db connection error.");
            throw new CustomException("데이터베이스 접근 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @Override
    public void addComplainReply(ComplaintReplyDto complaintReplyDto) throws CustomException{
        ComplaintTable complaintTable = complaintTableRepository.findById(complaintReplyDto.getComplaintId()).orElseThrow(() -> new CustomException("존재하지 않는 댓글입니다."));
        try{
            ComplaintAnswerTable complaintAnswerTable = ComplaintAnswerTable.builder()
                    .complaintAnswerContent(complaintReplyDto.getComplaintReplyContent())
                    .complaintAnswerDate(complaintReplyDto.getComplaintReplyDate())
                    .complaintId(complaintTable)
                    .build();

            complaintAnswerTableRepository.save(complaintAnswerTable);
            complaintTable.setComplaintStatus(ComplaintStatus.COMPLETED.getCode());
            complaintTable.setComplaintAnswerId(complaintAnswerTable);
            complaintTableRepository.save(complaintTable);
        }catch (Exception e){
            throw new RuntimeException("데이터베이스 접근 중 오류가 발생했습니다.", e);
        }
    }

    @Override
    @Operation(summary = "제공자 목록 조회", description = "제공자 목록을 조회합니다.")
    public List<ProviderTable> getProviderList(String adminId) {
        try{
            if(!adminId.equals("admin")){
                throw new CustomException("관리자만 접근 가능합니다.", HttpStatus.BAD_REQUEST.value());
            }
            return providerTableRepository.findAllByOrderByProviderIdDesc();
        }catch (Exception e){
            log.info("[getProviderList] db데이터 베이스 접근 오류");
            throw new RuntimeException("데이터베이스 접근 중 오류가 발생했습니다.", e);
        }
    }

    @Override
    @Operation(summary = "유저 목록 조회", description = "유저 목록을 조회합니다.")
    public List<IdTable> getUserList(String adminId) {
        try{
            if (!adminId.equals("admin")){
                throw new RuntimeException("관리자만 접근 가능합니다.");
            }
            return idTableRepository.findAll();
        }catch (Exception e){
            log.info("[getUserList] db데이터 베이스 접근 오류");
            throw new RuntimeException("데이터베이스 접근 중 오류가 발생했습니다.", e);
        }
    }

}
