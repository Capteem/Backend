package com.plog.demo.controller.complaint;


import com.plog.demo.dto.complaint.ComplaintRequestDto;
import com.plog.demo.dto.complaint.ComplaintResponseDto;
import com.plog.demo.service.complaint.ComplaintService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/complaint")
@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "Complaint", description = "불만 신고 API")
public class ComplaintController {

    private final ComplaintService complaintService;

    @PostMapping("/add")
    @Operation(summary = "불만 신고", description = "불만을 게시합니다.")
    public ResponseEntity<Object> addComplain(@RequestBody ComplaintRequestDto complaintRequestDto){
        try{
            complaintService.addComplain(complaintRequestDto);
            return ResponseEntity.status(HttpStatus.OK).body("불만 신고 성공");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("불만 신고 중 오류가 발생했습니다.");
        }
    }

    @GetMapping("/get")
    @Operation(summary = "불만 신고 조회", description = "불만 신고를 조회합니다.")
    public ResponseEntity<Object> getComplain(@RequestParam String userId){
        try{
            List<ComplaintResponseDto> complaintResponseDtoList = complaintService.getComplain(userId);
            if(complaintResponseDtoList.isEmpty()){
                return ResponseEntity.status(HttpStatus.OK).body("불만 신고가 없습니다.");
            }
            return ResponseEntity.status(HttpStatus.OK).body(complaintService.getComplain(userId));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("불만 신고 조회 중 오류가 발생했습니다.");
        }
    }
}
