package com.plog.demo.service.portfolio;

import com.plog.demo.dto.file.UploadFileDto;
import com.plog.demo.dto.portfolio.PortfolioResponseDto;
import com.plog.demo.dto.portfolio.PortfolioUpdateDto;
import com.plog.demo.dto.portfolio.PortfolioUploadDto;
import com.plog.demo.exception.CustomException;

import java.util.List;

public interface PortfolioService {

    PortfolioResponseDto getPortfolio(int portfolioId) throws CustomException;


    List<UploadFileDto> addPortfolios(PortfolioUploadDto portfolioUploadDto) throws CustomException;



    PortfolioUpdateDto updatePortfolio(PortfolioUpdateDto portfolioUpdateDto) throws CustomException;



    boolean deletePortfolio(int portfolioId) throws CustomException;



}
