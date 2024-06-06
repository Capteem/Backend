package com.plog.demo.service.complaint;


import com.plog.demo.common.ComplaintStatus;
import com.plog.demo.dto.complaint.*;
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

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ComplaintServiceImpl implements ComplaintService{

    private final ComplaintTableRepository complaintTableRepository;
    private final IdTableRepository idTableRepository;
//    private final ComplaintFileStore complaintFileStore;
//    private final ComplaintPhotoTableRepository complaintPhotoTablesRepository;

    @Override
    public void addComplain(ComplaintRequestDto complaintRequestDto) throws CustomException{
        IdTable IdTable = idTableRepository.findById(complaintRequestDto.getUserId()).orElseThrow(() -> new CustomException("존재하지 않는 사용자입니다."));

        ComplaintTable complaintTable = ComplaintTable.builder()
                .complaintTitle(complaintRequestDto.getComplaintTitle())
                .complaintContent(complaintRequestDto.getComplaintContent())
                .complaintDate(complaintRequestDto.getComplaintDate())
                .complaintStatus(ComplaintStatus.WAITING.getCode())
                .complaintType(complaintRequestDto.getComplaintType())
                .userId(IdTable)
                .complaintAnswerId(null)
                .complaintUuid(complaintRequestDto.getComplaintUuid())
                .build();

        try{
            complaintTableRepository.save(complaintTable);
        }catch (Exception e){
            throw new RuntimeException("데이터베이스 접근 중 오류가 발생했습니다.", e);
        }

    }

//    @Override
//    public void addPhotoToComplaint(ComplaintPhotoDto complaintPhotoDto) throws CustomException{
//
//        try {
//            List<ProviderCheckFileDto> complaintPhotoDtos =
//                    complaintFileStore.storeFiles(complaintPhotoDto.getComplaintCheckFiles());
//
//            List<ComplaintPhotoTable> complaintPhotoTables = complaintPhotoDtos.stream().map(providerCheckFileDto
//                    -> getComplaintPhotoTable(providerCheckFileDto, complaintPhotoDto.getUuid())).toList();
//            complaintPhotoTablesRepository.saveAll(complaintPhotoTables);
//        }catch (Exception e){
//            throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
//        }
//    }



    @Override
    public List<ComplaintResponseDto> getComplain(String userId) throws CustomException{
        IdTable IdTable = idTableRepository.findById(userId).orElseThrow(() -> new CustomException("존재하지 않는 사용자입니다."));

        List<ComplaintResponseDto> complaintTableList;
        try{
            if(IdTable.getId().equals("admin")){
                List<ComplaintTable> complaintTables = complaintTableRepository.findAll();
                complaintTableList = complaintTables.stream().map(complaintTable -> ComplaintResponseDto.builder()
                        .complaintId(complaintTable.getComplaintId())
                        .complaintTitle(complaintTable.getComplaintTitle())
                        .complaintContent(complaintTable.getComplaintContent())
                        .complaintDate(complaintTable.getComplaintDate())
                        .complaintStatus(complaintTable.getComplaintStatus())
                        .complaintType(complaintTable.getComplaintType())
                        .userId(complaintTable.getUserId().getId())
                        .complaintAnswerTable(complaintTable.getComplaintAnswerId() == null ? null : ComplaintReplyResponseDto.builder()
                                .complaintReplyContent(complaintTable.getComplaintAnswerId().getComplaintAnswerContent())
                                .complaintReplyDate(complaintTable.getComplaintAnswerId().getComplaintAnswerDate())
                                .complaintReplyId(complaintTable.getComplaintAnswerId().getComplaintAnswerId()).build())
                        .build()).toList();

            }
            else{
                List<ComplaintTable> complaintTables = complaintTableRepository.findAllByUserId(IdTable);
                complaintTableList = complaintTables.stream().map(complaintTable -> ComplaintResponseDto.builder()
                        .complaintId(complaintTable.getComplaintId())
                        .complaintTitle(complaintTable.getComplaintTitle())
                        .complaintContent(complaintTable.getComplaintContent())
                        .complaintDate(complaintTable.getComplaintDate())
                        .complaintStatus(complaintTable.getComplaintStatus())
                        .complaintType(complaintTable.getComplaintType())
                        .userId(complaintTable.getUserId().getId())
                        .complaintAnswerTable(complaintTable.getComplaintAnswerId() == null ? null : ComplaintReplyResponseDto.builder()
                                .complaintReplyContent(complaintTable.getComplaintAnswerId().getComplaintAnswerContent())
                                .complaintReplyDate(complaintTable.getComplaintAnswerId().getComplaintAnswerDate())
                                .complaintReplyId(complaintTable.getComplaintAnswerId().getComplaintAnswerId()).build())
                        .build()).toList();
            }
            if(complaintTableList.isEmpty()){
                return complaintTableList;
            }
        } catch (Exception e){
            throw new RuntimeException("데이터베이스 접근 중 오류가 발생했습니다.", e);
        }
        return complaintTableList;
    }

//    private ComplaintPhotoTable getComplaintPhotoTable(ProviderCheckFileDto providerCheckFileDto, String uuid){
//        return ComplaintPhotoTable.builder()
//                .StoreFileName(providerCheckFileDto.getStoreFileName())
//                .complaintUuid(uuid)
//                .build();
//    }
}
