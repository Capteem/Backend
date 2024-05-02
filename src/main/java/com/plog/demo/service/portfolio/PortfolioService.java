package com.plog.demo.service.portfolio;

import com.plog.demo.dto.portfolio.PortfolioResponseDto;
import com.plog.demo.exception.CustomException;

public interface PortfolioService {

    PortfolioResponseDto getPortfolio(int providerId) throws CustomException;
}
