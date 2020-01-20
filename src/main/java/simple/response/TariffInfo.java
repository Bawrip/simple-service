package simple.response;

import lombok.Data;

@Data
public class TariffInfo {

    private String name;
    private Value[] values;

    @Data
    public static class Value {

        private String name;
        private String value;
    }
}
