package com.plog.demo.controller.confirm;

import com.plog.demo.dto.confirm.ConfirmRequestDto;
import com.plog.demo.dto.confirm.ConfirmResponseDto;
import com.plog.demo.service.confirm.ConfirmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/confirm")
@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ConfirmController {

    private final ConfirmService confirmService;

    @PostMapping("/check")
    public ResponseEntity<Object> checkBusinessStatus(@RequestBody ConfirmRequestDto confirmRequestDto){
        try{
            ConfirmResponseDto confirmResponseDto = confirmService.getBusinessStatus(confirmRequestDto);
            return ResponseEntity.ok(confirmResponseDto.getConfirmResponseDataDto().getTax_type());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("사업자 번호 확인 중 오류가 발생했습니다..");
        }
    }
}
