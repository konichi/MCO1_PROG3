public class Service {
    private String serviceCode;
    private String description;
    private int price;

    public Service(String serviceCode, String description, int price) {
        this.serviceCode = serviceCode;
        this.description = description;
        this.price = price;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
