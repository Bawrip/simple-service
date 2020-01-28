package simple.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import simple.response.ResponseDto;
import simple.response.TariffInfo;
import simple.service.TariffInfoService;

@Api
@RestController
@RequestMapping("api/tariff")
public class TariffInfoController {

    private TariffInfoService tariffInfoService;

    @Autowired
    public TariffInfoController(TariffInfoService tariffInfoService) {
        this.tariffInfoService = tariffInfoService;
    }

    @GetMapping("info")
    public ResponseDto<TariffInfo> getTariffInfo(){
        TariffInfo response = tariffInfoService.getConstantTariffInfo();
        return ResponseDto.success(response);
    }
}
