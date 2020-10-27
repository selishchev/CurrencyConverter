public class Currency {
    private final String currency;
    private final String nominal;
    private final String value;

    public Currency(String currency, String nominal, String value) {
        this.currency = currency;
        this.nominal = nominal;
        this.value = value;
    }

    public String getCurrency() {
        return currency;
    }

    public String getNominal() {
        return nominal;
    }

    public String getValue() {
        return value;
    }
}
