package com.plog.demo.service.admin;

import com.plog.demo.common.UserStatus;
import com.plog.demo.dto.admin.AdminProviderDto;
import com.plog.demo.dto.admin.AdminRequestDto;
import com.plog.demo.model.IdTable;
import com.plog.demo.model.ProviderTable;
import com.plog.demo.repository.IdTableRepository;
import com.plog.demo.repository.ProviderTableRepository;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService{

    private final ProviderTableRepository providerTableRepository;
    private final IdTableRepository idTableRepository;

    @Override
    @Operation(summary = "제공자 상태 변경", description = "제공자의 상태를 변경합니다.")
    public void approveProvider(AdminProviderDto adminProviderDto) {
        ProviderTable providerTable = providerTableRepository.findById(adminProviderDto.getProviderId()).orElseThrow(() -> new RuntimeException("존재하지 않는 제공자입니다."));
        providerTable.setProviderStatus(adminProviderDto.getProviderStatus());

        try{
            providerTableRepository.save(providerTable);
        }catch (Exception e){
            log.info("[approveProvider] db데이터 베이스 접근 오류");
            throw new RuntimeException("데이터베이스 접근 중 오류가 발생했습니다.", e);
        }
    }

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


}
