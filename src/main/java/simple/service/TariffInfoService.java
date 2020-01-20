package simple.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import ru.mts.trading.server.exceptions.base.CommonException;
import simple.response.TariffInfo;

import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Arrays;

@Service
@Slf4j
@RequiredArgsConstructor
public class TariffInfoService {
    private final ObjectMapper objectMapper;

    @Value("${application.tariff.response.filename}")
    private Resource tariffResource;

    @Cacheable("tariffInfo")
    public TariffInfo[] getConstantTariffInfo() {
        log.trace("getConstantTariffInfo: Started");
        TariffInfo[] tariffInfo;
        try (InputStream inputStream = tariffResource.getInputStream()) {
            tariffInfo = objectMapper.readValue(inputStream, TariffInfo[].class);
        } catch (Exception ex) {
            String errorMessage = MessageFormat.format("Failed to load contents from \"{0}\".", tariffResource.getFilename());
            log.error(errorMessage, ex);
            throw new CommonException(errorMessage, ex);
        }
        log.info("getConstantTariffInfo: Finished with: {}", Arrays.toString(tariffInfo));
        return tariffInfo;
    }
}
