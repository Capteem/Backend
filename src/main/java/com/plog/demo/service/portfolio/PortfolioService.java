package com.plog.demo.service.portfolio;

import com.plog.demo.dto.Provider.ProviderRepRequestDto;
import com.plog.demo.dto.file.UploadFileDto;
import com.plog.demo.dto.portfolio.PortfolioImageDto;
import com.plog.demo.dto.portfolio.PortfolioRandomResponseDto;
import com.plog.demo.dto.portfolio.PortfolioResponseDto;
import com.plog.demo.dto.portfolio.PortfolioUploadDto;
import com.plog.demo.exception.CustomException;

import java.util.List;

public interface PortfolioService {

    PortfolioResponseDto getPortfolio(int portfolioId) throws CustomException;

    List<PortfolioRandomResponseDto> getPortfolioRandom(int page) throws CustomException;


    List<UploadFileDto> addPortfolios(PortfolioUploadDto portfolioUploadDto) throws CustomException;

    PortfolioImageDto getImage(String middleDir, String fileName) throws CustomException;

    boolean deletePortfolio(int portfolioId) throws CustomException;

    UploadFileDto addProviderRep(ProviderRepRequestDto providerRepRequestDto) throws CustomException;

    boolean deleteProviderRep(int providerId) throws CustomException;


}
