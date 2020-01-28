package simple.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import ru.mts.trading.server.exceptions.base.CommonException;
import simple.response.TariffInfo;

import java.io.InputStream;
import java.text.MessageFormat;

@Service
public class TariffInfoService {
    private ObjectMapper objectMapper;

    @Autowired
    public TariffInfoService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Value("${application.tariff.response.filename}")
    private Resource tariffResource;

    @Cacheable("tariffInfo")
    public TariffInfo getConstantTariffInfo() {
        TariffInfo tariffInfo;
        try (InputStream inputStream = tariffResource.getInputStream()) {
            tariffInfo = objectMapper.readValue(inputStream, TariffInfo.class);
        } catch (Exception ex) {
            String errorMessage = MessageFormat.format("Failed to load contents from \"{0}\".", tariffResource.getFilename());
            throw new CommonException(errorMessage, ex);
        }
        return tariffInfo;
    }
}
