package com.plog.demo.controller.provider;

import com.plog.demo.dto.Provider.PhotoDto;
import com.plog.demo.dto.Provider.ProviderDto;
import com.plog.demo.exception.CustomException;
import com.plog.demo.service.Provider.ProviderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/service")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProviderController {

    private final ProviderService providerService;

    @PostMapping("/service")
    public ResponseEntity<Map<String, String>> signUpProvider(@RequestBody ProviderDto providerDto){

        Map<String, String> responseData = new HashMap<>();

        try {
            providerService.addProvider(providerDto);
            responseData.put("message", "서비스 등록 성공");
            return ResponseEntity.status(HttpStatus.OK).body(responseData);
        }catch (CustomException e){
            responseData.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }catch (RuntimeException e) {
            responseData.put("message", "서버에서 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
    }

    @PostMapping("/photo")
    public ResponseEntity<Map<String ,String>> addPhoto(@RequestBody PhotoDto pictureDto){
        Map<String, String> responseData = new HashMap<>();

        try {
            providerService.addPhoto(pictureDto);
            responseData.put("message", "사진 등록 성공");
            return ResponseEntity.status(HttpStatus.OK).body(responseData);
        }catch (CustomException e){
            responseData.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }catch (RuntimeException e) {
            responseData.put("message", "서버에서 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
    }
}
