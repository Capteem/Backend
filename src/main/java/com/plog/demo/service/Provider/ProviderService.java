package com.plog.demo.service.Provider;

import com.plog.demo.dto.Provider.PhotoDto;
import com.plog.demo.dto.Provider.ProviderDto;
import com.plog.demo.exception.CustomException;

public interface ProviderService {

    ProviderDto addProvider(ProviderDto providerDto) throws CustomException;

    ProviderDto getProvider();

    PhotoDto addPhoto(PhotoDto photoDto) throws CustomException;

}
