package com.plog.demo.service.Provider;

import com.plog.demo.dto.Provider.ProviderAdminDto;
import com.plog.demo.dto.Provider.ProviderCheckRequestDto;
import com.plog.demo.dto.Provider.ProviderDto;
import com.plog.demo.dto.Provider.ProviderResponseDto;
import com.plog.demo.exception.CustomException;
import com.plog.demo.model.ProviderTable;

import java.util.List;

public interface ProviderService {

    ProviderDto addProvider(ProviderDto providerDto) throws CustomException;

    List<ProviderResponseDto> getSelectedProvider(String userId) throws CustomException;

    List<ProviderAdminDto> getProviderList() throws CustomException;


}
