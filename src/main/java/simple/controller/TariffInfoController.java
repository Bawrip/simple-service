package simple.controller;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mts.trading.server.transport.response.ResponseDto;
import simple.response.TariffInfo;
import simple.service.TariffInfoService;

@Api
@RestController
@RequestMapping("api/tariff")
@RequiredArgsConstructor
public class TariffInfoController {

    private final TariffInfoService tariffInfoService;

    @GetMapping("info")
    public ResponseDto<TariffInfo[]> getTariffInfo(){
        TariffInfo[] response = tariffInfoService.getConstantTariffInfo();
        return ResponseDto.success(response);
    }
}
