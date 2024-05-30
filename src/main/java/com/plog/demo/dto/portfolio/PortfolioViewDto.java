package com.plog.demo.dto.portfolio;

import lombok.Data;

import java.util.List;

/**
 * 이미지 하나씩 받을거면 삭제
 */
@Data
public class PortfolioViewDto {

    List<String> imgUrls;
}
