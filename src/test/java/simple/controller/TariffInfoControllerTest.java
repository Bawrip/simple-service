package simple.controller;

import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import ru.mts.trading.server.transport.response.ResponseDto;
import simple.BaseApplicationTest;
import simple.response.TariffInfo;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class TariffInfoControllerTest extends BaseApplicationTest {

    private static final ParameterizedTypeReference<ResponseDto<TariffInfo>> TARIFF_INFO_ARRAY_RESPONSE_PARAM_TYPE =
            new ParameterizedTypeReference<ResponseDto<TariffInfo>>() {
            };

    @Test
    void tariffInfoControllerTest() {
        TariffInfo expectedResponse = loadFromJson("expected.tariff.info.response.json", TariffInfo.class);
        ResponseDto<TariffInfo> actualResponse = makeFullPathRequest(null, TARIFF_INFO_ARRAY_RESPONSE_PARAM_TYPE,
                HttpMethod.GET, generatePathUrl("api/tariff/info"), HttpStatus.OK, null);

        assertThat(actualResponse.getData().toString()).isEqualTo(expectedResponse.toString());
    }
}
