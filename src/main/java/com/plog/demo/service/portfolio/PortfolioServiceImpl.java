package com.plog.demo.service.portfolio;

import com.plog.demo.dto.portfolio.PortfolioResponseDto;
import com.plog.demo.dto.portfolio.ReviewResponseDto;
import com.plog.demo.exception.CustomException;
import com.plog.demo.model.PortfolioTable;
import com.plog.demo.model.ReviewTable;
import com.plog.demo.repository.PortfolioTableRepository;
import com.plog.demo.repository.ReviewTableRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class PortfolioServiceImpl implements PortfolioService{

    private final PortfolioTableRepository portfolioTableRepository;
    private final ReviewTableRepository reviewTableRepository;


    @Override
    public PortfolioResponseDto getPortfolio(int portfolioId) throws CustomException {

        log.info("[getPortfolio] 포토폴리오 조회 시작");

        Optional<PortfolioTable> portfolio = portfolioTableRepository.findById(portfolioId);

        PortfolioTable portfolioTable = portfolio.orElseThrow(() -> {
            log.warn("[getPortfolio] 포토폴리오 존재하지 않음");
            return new CustomException("해당 포토폴리오가 없습니다.", HttpStatus.NOT_FOUND.value());
        });


        //정상로직
        int providerId = portfolioTable.getProviderId().getProviderId();

        //리뷰리스트 가져오기
        List<ReviewTable> reviewTables = reviewTableRepository.findByProviderTable_ProviderId(providerId);

        //Dto로 변환
        List<ReviewResponseDto> reviewResponseDtos = reviewTables.stream()
                .map(reviewTable -> ReviewResponseDto.builder()
                        .reviewId(reviewTable.getReviewId())
                        .reviewContent(reviewTable.getReviewContent())
                        .reviewScore(reviewTable.getReviewScore())
                        .reviewDate(reviewTable.getReviewDate())
                        .build()
                ).toList();


        //TODO 이미지 리스트 추가해야함
        PortfolioResponseDto portfolioResponseDto = PortfolioResponseDto.builder()
                .providerId(providerId)
                .reviewList(reviewResponseDtos)
                .build();

        log.info("[getPortfolio] 포토폴리오 조회 완료");

        return portfolioResponseDto;
    }
}
