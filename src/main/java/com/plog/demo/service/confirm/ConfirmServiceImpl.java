package com.plog.demo.service.confirm;

import com.plog.demo.dto.confirm.ConfirmRequestDto;
import com.plog.demo.dto.confirm.ConfirmResponseDto;
import com.plog.demo.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ConfirmServiceImpl implements ConfirmService{

    @Value("${openapi.authkey}")
    private String authKey;
    @Override
    public ConfirmResponseDto getBusinessStatus(String businessNumber) throws CustomException {

        String baseUrl = "https://api.odcloud.kr/api/nts-businessman/v1/status?serviceKey=" + authKey;

        URI uri = URI.create(baseUrl);

        LinkedMultiValueMap<String, String> jsonMap = new LinkedMultiValueMap<>();
        HttpHeaders headers = new HttpHeaders();
        ConfirmResponseDto confirmResponseDto;
        jsonMap.add("b_no", businessNumber);

        headers.set("Content-Type", "application/json");
        headers.set("accept", "*/*");

        HttpEntity<LinkedMultiValueMap<String, String>> request = new HttpEntity<>(jsonMap, headers);
        try {
            RestTemplate restTemplate = new RestTemplate();
            confirmResponseDto = restTemplate.postForObject(uri, request, ConfirmResponseDto.class);
            return confirmResponseDto;
        } catch (HttpClientErrorException e) {
            log.info("[getBusinessStatus]"+e.getMessage());
            throw new CustomException("[getBusinessStatus] failure to get business status");
        }
    }

}
