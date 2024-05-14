package com.plog.demo.service.Provider;


import com.plog.demo.common.UserStatus;

import com.plog.demo.dto.Provider.ProviderAdminDto;
import com.plog.demo.dto.Provider.ProviderDto;
import com.plog.demo.dto.Provider.ProviderResponseDto;

import com.plog.demo.dto.workdate.WorkdateDto;
import com.plog.demo.dto.workdate.DateListDto;
import com.plog.demo.exception.CustomException;
import com.plog.demo.model.IdTable;

import com.plog.demo.model.ProviderTable;
import com.plog.demo.model.WorkdateTable;
import com.plog.demo.repository.IdTableRepository;
import com.plog.demo.repository.ProviderTableRepository;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Provider;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProviderServiceImpl implements ProviderService{

    private final ProviderTableRepository providerTableRepository;
    private final IdTableRepository idTableRepository;

    @Override
    public ProviderDto addProvider(ProviderDto providerDto) throws CustomException{

        IdTable idTable = idTableRepository.findById(providerDto.getUserId()).orElseThrow(() -> new CustomException("존재하지 않는 사용자입니다."));

        ProviderTable providerTable = ProviderTable.builder()
                .userId(idTable)
                .providerName(providerDto.getProviderName())
                .providerType(providerDto.getProviderType())
                .providerArea(providerDto.getProviderArea())
                .providerSubArea(providerDto.getProviderSubArea())
                .providerDetailArea(providerDto.getProviderDetail())
                .providerPhoneNum(providerDto.getProviderPhoneNum())
                .providerStatus(UserStatus.STOP.getCode())
                .build();

        try{
            log.info("[addProvider] save제공자 저장 로직 시작");
            providerTableRepository.save(providerTable);
        }catch (Exception e){
            log.error("[addProvider] db데이터 베이스 접근 오류");
            throw new RuntimeException("데이터베이스 접근 중 오류가 발생했습니다.", e);
        }

        return providerDto;
    }

    @Override
    public List<ProviderResponseDto> getSelectedProvider(String userId) throws CustomException{

        IdTable idTable = idTableRepository.findById(userId).orElseThrow(() -> new CustomException("존재하지 않는 사용자입니다."));
        try{
            log.info("[getProvider] 제공자 조회 로직 시작");
            List<ProviderTable> providerTables = providerTableRepository.findAllByUserId(idTable);
            List<ProviderResponseDto> providerDtos = providerTables.stream().map(providerTable -> ProviderResponseDto.builder()
                    .providerName(providerTable.getProviderName())
                    .providerType(providerTable.getProviderType())
                    .providerStatus(providerTable.getProviderStatus())
                    .build()).toList();
            if(providerTables.isEmpty()){
                log.error("[getProvider] 제공자가 존재하지 않습니다.");
                throw new CustomException("제공자가 존재하지 않습니다.");
            }
            return providerDtos;
        } catch (Exception e){
            log.error("[getProvider] db데이터 베이스 접근 오류");
            throw new RuntimeException("데이터베이스 접근 중 오류가 발생했습니다.", e);
        }
    }


    @Override
    @Operation(summary = "허가된 제공자 목록 조회", description = "허가된 제공자 목록을 조회합니다.")
    public List<ProviderTable> getConfirmedProviderList() throws CustomException {
        try {
            log.info("[getConfirmedProviderList] 제공자 리스트 조회 로직 시작");
            List<ProviderTable> providerTables = providerTableRepository.findByProviderStatus(UserStatus.ACTIVE.getCode());
            if (providerTables.isEmpty()) {
                log.error("[getConfirmedProviderList] 제공자가 존재하지 않습니다.");
                throw new CustomException("제공자가 존재하지 않습니다.");
            }
            return providerTables;
        } catch (Exception e) {
            log.error("[getConfirmedProviderList] db데이터 베이스 접근 오류");
            throw new RuntimeException("데이터베이스 접근 중 오류가 발생했습니다.", e);
        }
    }

    @Override
    public void updateProviderWorkDate(WorkdateDto workdateDto) throws CustomException {
        IdTable idTable = idTableRepository.findById(workdateDto.getUserId()).orElseThrow(() -> new CustomException("존재하지 않는 사용자입니다."));
        ProviderTable providerTable = providerTableRepository.findByUserId(idTable).orElseThrow(() -> new CustomException("존재하지 않는 제공자입니다."));

        for(DateListDto dateListDto : workdateDto.getDateList()){
            for(String date : dateListDto.getTime()){
                WorkdateTable workdateTable = WorkdateTable.builder()
                        .providerId(providerTable)
                        .workDate(dateListDto.getDate())
                        .workTime(date)
                        .build();
                try{
                    log.info("[updateProviderWorkDate] save제공자 근무일 저장 로직 시작");
                    providerTableRepository.save(providerTable);
                }catch (Exception e){
                    log.error("[updateProviderWorkDate] db데이터 베이스 접근 오류");
                    throw new RuntimeException("데이터베이스 접근 중 오류가 발생했습니다.", e);
                }
            }
        }
    }


}
