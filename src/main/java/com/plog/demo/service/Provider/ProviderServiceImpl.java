package com.plog.demo.service.Provider;

import com.plog.demo.dto.Provider.PhotoDto;
import com.plog.demo.dto.Provider.ProviderDto;
import com.plog.demo.exception.CustomException;
import com.plog.demo.model.IdTable;
import com.plog.demo.model.ProviderTable;
import com.plog.demo.repository.IdTableRepository;
import com.plog.demo.repository.ProviderTableRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProviderServiceImpl implements ProviderService{

    private final ProviderTableRepository providerTableRepository;
    private final IdTableRepository idTableRepository;

    @Override
    public ProviderDto addProvider(ProviderDto providerDto) throws CustomException{

        Optional<ProviderTable> findProvider = providerTableRepository.findByProviderName(providerDto.getProviderName());
        IdTable idTable = idTableRepository.findById(providerDto.getUserId()).orElseThrow(() -> new CustomException("존재하지 않는 사용자입니다."));

        if(findProvider.isPresent()){
            log.info("[addProvider] 이미 등록된 제공자입니다.");
            throw new CustomException("이미 등록된 제공자입니다.");
        }
        ProviderTable providerTable = ProviderTable.builder()
                .userId(idTable)
                .providerName(providerDto.getProviderName())
                .providerType(providerDto.getProviderType())
                .providerArea(providerDto.getProviderArea())
                .providerSubArea(providerDto.getProviderSubArea())
                .providerDetailArea(providerDto.getProviderDetailArea())
                .build();

        try{
            log.info("[addProvider] 제공자 저장 로직 시작");
            providerTableRepository.save(providerTable);
        }catch (Exception e){
            log.info("[addProvider] 데이터 베이스 접근 오류");
            throw new RuntimeException("데이터베이스 접근 중 오류가 발생했습니다.", e);
        }

        return providerDto;
    }

    @Override
    public PhotoDto addPhoto(PhotoDto photoDto) throws CustomException {

        // TODO: Implement this method
        return null;

    }

    @Override
    public ProviderDto getProvider() {
        return null;
    }
}
