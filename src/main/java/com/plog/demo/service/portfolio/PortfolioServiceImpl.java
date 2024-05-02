package com.plog.demo.service.portfolio;

import com.plog.demo.dto.portfolio.PortfolioAddDto;
import com.plog.demo.dto.portfolio.CommentResponseDto;
import com.plog.demo.dto.portfolio.PortfolioResponseDto;
import com.plog.demo.dto.portfolio.ReviewResponseDto;
import com.plog.demo.exception.CustomException;
import com.plog.demo.model.CommentTable;
import com.plog.demo.model.PortfolioTable;
import com.plog.demo.model.ProviderTable;
import com.plog.demo.model.ReviewTable;
import com.plog.demo.repository.CommentTableRepository;
import com.plog.demo.repository.PortfolioTableRepository;
import com.plog.demo.repository.ReviewTableRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class PortfolioServiceImpl implements PortfolioService{

    private final PortfolioTableRepository portfolioTableRepository;
    private final ReviewTableRepository reviewTableRepository;
    private final CommentTableRepository commentTableRepository;

//    @Override
//    public PortfolioResponseDto getPortfolio(int providerId) throws CustomException {
//        log.info("[getPortfolio] 포토폴리오 조회 시작");
//
//    }


    @Override
    public PortfolioResponseDto getPortfolio(int providerId) throws CustomException {

        log.info("[getPortfolio] 포토폴리오 조회 시작");

        ProviderTable providerTable = ProviderTable.builder()
                .providerId(providerId)
                .build();

        List<PortfolioTable> portfolios = portfolioTableRepository.findByProviderId(providerTable);

        if(portfolios.isEmpty()){
            log.warn("[getPortfolio] 포토폴리오 존재하지 않음");
            throw new CustomException("해당 포토폴리오가 없습니다.", HttpStatus.NOT_FOUND.value());
        }


        //정상로직

        List<ReviewResponseDto> reviewResponseDtos= new ArrayList<>();

        //리뷰리스트 가져오기
        List<ReviewTable> reviewTables = reviewTableRepository.findByProviderId(providerTable);

        for(ReviewTable reviewTable : reviewTables){

            // 댓글 가져오기
            Optional<CommentTable> comment = commentTableRepository.findByReviewId(reviewTable);

            // 리뷰 응답 dto 생성
            ReviewResponseDto reviewResponseDto = createReviewResponseDto(reviewTable, comment.orElse(null));

            // 리뷰 리스트에 추가
            reviewResponseDtos.add(reviewResponseDto);

        }


        //TODO 이미지 리스트 추가해야함
        PortfolioResponseDto portfolioResponseDto = PortfolioResponseDto.builder()
                .providerId(providerId)
                .reviewList(reviewResponseDtos)
                .build();

        log.info("[getPortfolio] 포토폴리오 조회 완료");

        return portfolioResponseDto;
    }

    @Override
    public void addPortfolio(PortfolioAddDto portfolioAddDto) throws CustomException {

        log.info("[addPortfolio] 포토폴리오 추가 시작");


        log.info("[addPortfolio] 포토폴리오 추가 완료");

    }

    /**
     *  리뷰 응답 dto 생성
     */
    private ReviewResponseDto createReviewResponseDto(ReviewTable reviewTable, CommentTable comment) {

        CommentResponseDto commentResponseDto = null;
        if(comment != null){

            commentResponseDto = CommentResponseDto.builder()
                        .commentId(comment.getCommentId())
                        .commentContent(comment.getCommentContent())
                        .commentDate(comment.getCommentDate())
                        .build();

        }

        return ReviewResponseDto.builder()
                .reviewId(reviewTable.getReviewId())
                .reviewContent(reviewTable.getReviewContent())
                .reviewScore(reviewTable.getReviewScore())
                .reviewDate(reviewTable.getReviewDate())
                .commentResponseDto(commentResponseDto)
                .build();
    }
}
