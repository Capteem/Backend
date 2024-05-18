package com.plog.demo.service.Provider;

import com.plog.demo.dto.Provider.*;
import com.plog.demo.dto.workdate.WorkdateDto;
import com.plog.demo.exception.CustomException;
import com.plog.demo.model.ProviderTable;

import java.util.List;

public interface ProviderService {

    ProviderDto addProvider(ProviderDto providerDto) throws CustomException;

    List<ProviderResponseDto> getSelectedProvider(String userId) throws CustomException;


    List<ProviderListDto> getConfirmedProviderList() throws CustomException;

    List<ProviderResponseDto> getProviderListWithConfirm(String userId) throws CustomException;

    void updateProviderWorkDate(WorkdateDto workdateDto) throws CustomException;

    List<ProviderReservationDto> getProviderReservationList(int providerId) throws CustomException;

}
