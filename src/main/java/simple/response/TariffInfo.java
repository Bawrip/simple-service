package simple.response;

public class TariffInfo {

    private String name;
    private String values;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return "name='" + name + '\'' +
               ", values='" + values +  '\'';
    }
}