package com.plog.demo.service.admin;

import com.plog.demo.common.ComplaintStatus;
import com.plog.demo.common.UserStatus;
import com.plog.demo.dto.admin.AdminProviderApproveDto;
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
    public void changeStatus(AdminRequestDto adminRequestDto) {
        if(adminRequestDto.getProviderId() == null){
            IdTable idTable = idTableRepository.findById(adminRequestDto.getUserId()).orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));
            idTable.setStatus(adminRequestDto.getStatus());
            try{
                idTableRepository.save(idTable);
            }catch (Exception e){
                log.info("[changeStatus] db데이터 베이스 접근 오류");
                throw new RuntimeException("데이터베이스 접근 중 오류가 발생했습니다.", e);
            }
        }
        else {
            ProviderTable providerTable = providerTableRepository.findById(adminRequestDto.getProviderId()).orElseThrow(() -> new RuntimeException("존재하지 않는 제공자입니다."));
            providerTable.setProviderStatus(adminRequestDto.getStatus());
            try{
                providerTableRepository.save(providerTable);
            }catch (Exception e){
                log.info("[changeStatus] db데이터 베이스 접근 오류");
                throw new RuntimeException("데이터베이스 접근 중 오류가 발생했습니다.", e);
            }
        }
    }

    @Override
    @Operation(summary = "제공자 승인", description = "제공자를 승인합니다.")
    public void approveProvider(AdminProviderApproveDto adminProviderApproveDto) throws CustomException {
        ProviderTable providerTable = providerTableRepository.findById(adminProviderApproveDto.getProviderId()).orElseThrow(() -> new CustomException("not exist provider.", HttpStatus.BAD_REQUEST.value()));
        IdTable idTable = idTableRepository.findById(adminProviderApproveDto.getUserId()).orElseThrow(() -> new CustomException("not exist user.", HttpStatus.BAD_REQUEST.value()));

        if(!providerTable.getUserId().equals(idTable)){
            log.error("[approveProvider] not match provider and user.");
            throw new CustomException("유저 정보와 제공자 정보가 다릅니다.", HttpStatus.BAD_REQUEST.value());
        }
        try{
            providerTable.setProviderStatus(UserStatus.ACTIVE.getCode());
            idTable.setRole("PROVIDER");
            providerTableRepository.save(providerTable);
            idTableRepository.save(idTable);
        }catch (Exception e){
            log.info("[approveProvider] db connection error.");
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
    @Operation(summary = "제공자 거절", description = "제공자를 거절합니다.")
    public void refuseProvider(AdminProviderApproveDto adminProviderApproveDto) throws CustomException {
        ProviderTable providerTable = providerTableRepository.findById(adminProviderApproveDto.getProviderId()).orElseThrow(() -> new CustomException("not exist provider.", HttpStatus.BAD_REQUEST.value()));
        IdTable idTable = idTableRepository.findById(adminProviderApproveDto.getUserId()).orElseThrow(() -> new CustomException("not exist user.", HttpStatus.BAD_REQUEST.value()));

        if(!providerTable.getUserId().equals(idTable)){
            log.error("[refuseProvider] not match provider and user.");
            throw new CustomException("유저 정보와 제공자 정보가 다릅니다.", HttpStatus.BAD_REQUEST.value());
        }

        try{
            providerTable.setProviderStatus(UserStatus.BANNED.getCode());
            providerTableRepository.save(providerTable);
        }catch (Exception e){
            log.info("[refuseProvider] db connection error.");
            throw new CustomException("데이터베이스 접근 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }


    @Override
    @Operation(summary = "제공자 목록 조회", description = "제공자 목록을 조회합니다.")
    public List<ProviderTable> getProviderList(String adminId) {
        try{
            if(!adminId.equals("admin")){
                throw new RuntimeException("관리자만 접근 가능합니다.");
            }
            return providerTableRepository.findAll();
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
