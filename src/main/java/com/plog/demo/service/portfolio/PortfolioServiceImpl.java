package com.plog.demo.service.portfolio;

import com.plog.demo.common.file.PortfolioFileStore;
import com.plog.demo.dto.file.DeleteFileDto;
import com.plog.demo.dto.file.UploadFileDto;
import com.plog.demo.dto.portfolio.*;
import com.plog.demo.dto.review.ReviewGetResponseDto;
import com.plog.demo.exception.CustomException;
import com.plog.demo.model.*;
import com.plog.demo.repository.*;
import com.plog.demo.service.review.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;


@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class PortfolioServiceImpl implements PortfolioService{

    private final PortfolioTableRepository portfolioTableRepository;
    private final ReviewService reviewService;
    private final ProviderTableRepository providerTableRepository;
    private final PortfolioFileStore portfolioFileStore;


    @Override
    public PortfolioResponseDto getPortfolio(int providerId) throws CustomException {

        log.info("[getPortfolio] 포트폴리오 조회 시작");


        ProviderTable providerTable = providerTableRepository.findById(providerId)
                .orElseThrow(() -> new CustomException("존재하지 않은 서비스 제공자 입니다.", HttpStatus.NOT_FOUND.value()));


        List<PortfolioTable> portfolios = portfolioTableRepository.findByProviderId(providerTable);

        if(portfolios.isEmpty()){
            throw new CustomException("해당 포토폴리오가 없습니다.", HttpStatus.NOT_FOUND.value());
        }


        //정상로직


        //포토폴리오 dto 생성
        List<PortfolioGetDto> portfolioGetDtos = createPortfolioGetDtos(portfolios);

        ReviewGetResponseDto reviewGetResponseDto = reviewService.getReviewsByProviderId(providerId);


        PortfolioResponseDto portfolioResponseDto = PortfolioResponseDto.builder()
                .providerId(providerId)
                .portfolioList(portfolioGetDtos)
                .reviewList(reviewGetResponseDto.getReviewList())
                .build();

        log.info("[getPortfolio] 포토폴리오 조회 완료");

        return portfolioResponseDto;
    }


    @Override
    public List<UploadFileDto> addPortfolios(PortfolioUploadDto portfolioUploadDto) throws CustomException {

        log.info("[addPortfolio] 포토폴리오 추가 시작");

        ProviderTable providerTable = providerTableRepository.findById(portfolioUploadDto.getProviderId())
                .orElseThrow(() -> new CustomException("존재 하지 않은 서비스 제공자입니다", HttpStatus.NOT_FOUND.value()));

        //파일 검증
        List<MultipartFile> portfolioUploadFiles = portfolioUploadDto.getPortfolioUploadFiles();
        portfolioFileStore.validateFiles(portfolioUploadFiles);

        //서버에 파일 저장
        List<UploadFileDto> storedFiles = portfolioFileStore.storeFiles(portfolioUploadDto.getPortfolioUploadFiles());

        if(storedFiles.isEmpty()){
            throw new CustomException("포트폴리오 파일 저장 오류");
        }


        //db에 포트폴리오 저장
        List<PortfolioTable> portfolioTables = storedFiles.stream()
                .map(uploadFileDto -> portfolioTableRepository.save(
                        PortfolioTable.builder()
                                .dateBasedImagePath(uploadFileDto.getDateBasedImagePath())
                                .storedFileName(uploadFileDto.getStoreFileName())
                                .providerId(providerTable)
                                .build())
                ).toList();

        if(portfolioTables.isEmpty()){
            throw new CustomException("포트폴리오 파일 저장 오류");
        }

        log.info("[addPortfolio] 포토폴리오 추가 완료");

        return storedFiles;
    }

    @Override
    public PortfolioImageDto getImage(String middleDir, String fileName) throws CustomException {

        String fileExtension = portfolioFileStore.extractExt(fileName);

        if(portfolioFileStore.isNotSupportedExtension(fileExtension)){
            throw new CustomException("지원되지 않는 파일 형식입니다.", HttpStatus.BAD_REQUEST.value());
        }

        return PortfolioImageDto.builder()
                .imgFullPath(portfolioFileStore.getFullPath(middleDir, fileName))
                .fileExtension(fileExtension)
                .build();
    }

    @Override
    public boolean deletePortfolio(int portfolioId) throws CustomException {

        //포트폴리오 조회
        PortfolioTable portfolioTable = portfolioTableRepository.findById(portfolioId)
                .orElseThrow(() -> new CustomException("포토폴리오가 존재하지 않습니다", HttpStatus.NOT_FOUND.value()));


        //deleteFileDto 생성
        DeleteFileDto deleteFileDto = DeleteFileDto.builder()
                .imgPath(portfolioTable.getDateBasedImagePath())
                .storedFileName(portfolioTable.getStoredFileName())
                .build();


        //파일 삭제
        if(!portfolioFileStore.deleteFile(deleteFileDto)){
            return false;
        }

        //db 삭제
        portfolioTableRepository.deleteById(portfolioTable.getPortfolioId());

        return true;
    }

    private String getImgUrl(PortfolioTable portfolio) {
        return portfolio.getDateBasedImagePath() + portfolio.getStoredFileName();
    }

    private List<PortfolioGetDto> createPortfolioGetDtos(List<PortfolioTable> portfolios) {


        return portfolios.stream().map(
                portfolio -> PortfolioGetDto.builder()
                        .portfolioId(portfolio.getPortfolioId())
                        .imgUrl(getImgUrl(portfolio))
                        .build()
        ).toList();
    }

}
