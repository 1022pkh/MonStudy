package steam.appjam.sopt.com.myapplication.sliding.PriceTime;

/**
 * Created by user on 2016-07-02.
 */
public class PriceTimeItem {
    String type;
    String price;

    public PriceTimeItem(String type, String price)
    {
        this.type = type;
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public String getPrice() {
        return price;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
