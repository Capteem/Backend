package com.plog.demo.service.complaint;


import com.plog.demo.common.ComplaintStatus;
import com.plog.demo.dto.complaint.ComplaintDto;
import com.plog.demo.dto.complaint.ComplaintRequestDto;
import com.plog.demo.exception.CustomException;
import com.plog.demo.model.ComplaintTable;
import com.plog.demo.model.IdTable;
import com.plog.demo.repository.ComplaintTableRepository;
import com.plog.demo.repository.IdTableRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ComplaintServiceImpl implements ComplaintService{

    private final ComplaintTableRepository complaintTableRepository;
    private final IdTableRepository idTableRepository;

    @Override
    public ComplaintRequestDto addComplain(ComplaintRequestDto complaintRequestDto) throws CustomException{
        IdTable IdTable = idTableRepository.findById(complaintRequestDto.getUserId()).orElseThrow(() -> new CustomException("존재하지 않는 사용자입니다."));

        ComplaintTable complaintTable = ComplaintTable.builder()
                .complaintTitle(complaintRequestDto.getComplaintTitle())
                .complaintContent(complaintRequestDto.getComplaintContent())
                .complaintDate(complaintRequestDto.getComplaintDate())
                .complaintStatus(ComplaintStatus.WAITING.getCode())
                .userId(IdTable)
                .build();

        try{
            complaintTableRepository.save(complaintTable);
        }catch (Exception e){
            throw new RuntimeException("데이터베이스 접근 중 오류가 발생했습니다.", e);
        }

        return complaintRequestDto;
    }

    @Override
    public List<ComplaintTable> getComplain(String userId) throws CustomException{
        IdTable IdTable = idTableRepository.findById(userId).orElseThrow(() -> new CustomException("존재하지 않는 사용자입니다."));

        List<ComplaintTable> complaintTableList;
        if(IdTable.getRole().equals("admin")){
            complaintTableList = complaintTableRepository.findAll();
        }
        else{
            complaintTableList = complaintTableRepository.findAllByUserId(IdTable);
        }
        if(complaintTableList.isEmpty()){
            throw new CustomException("접수된 민원이 없습니다.");
        }
        return complaintTableList;
    }
}
