package com.plog.demo.service.confirm;

import com.plog.demo.dto.confirm.ConfirmRequestDto;
import com.plog.demo.dto.confirm.ConfirmResponseDto;
import com.plog.demo.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.HashMap;


@Service
@Slf4j
@RequiredArgsConstructor
public class ConfirmServiceImpl implements ConfirmService{

    @Value("${openapi.authkey}")
    private String authKey;
    @Override
    public ConfirmResponseDto getBusinessStatus(ConfirmRequestDto confirmRequestDto) throws CustomException {

        String baseUrl = "https://api.odcloud.kr/api/nts-businessman/v1/status?serviceKey=" + authKey;
        log.info("[getBusinessStatus] baseUrl : {}", baseUrl);

        LinkedMultiValueMap<String, String> jsonMap = new LinkedMultiValueMap<>();
        HttpHeaders headers = new HttpHeaders();
        ConfirmResponseDto confirmResponseDto;

        jsonMap.add("b_no", confirmRequestDto.getBusinessId());

        headers.set("Content-Type", "application/json");
        headers.set("accept", "application/json");

        HttpEntity<LinkedMultiValueMap<String, String>> request = new HttpEntity<>(jsonMap, headers);
        log.info("[getBusinessStatus] request : {}", request);
        try {
            RestTemplate restTemplate = new RestTemplate();
            confirmResponseDto = restTemplate.postForObject(baseUrl, request, ConfirmResponseDto.class);
            log.info("[getBusinessStatus] confirmResponseDto : {}", confirmResponseDto);
            return confirmResponseDto;
        } catch (Exception e) {
            log.info("[getBusinessStatus] failure to get business status");
            throw new CustomException("[getBusinessStatus] failure to get business status");
        }
    }

}
