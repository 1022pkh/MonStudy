package steam.appjam.sopt.com.myapplication.sliding.information;

/**
 * Created by user on 2016-07-02.
 */
public class informationItem {
    String iv_imageview;
    String tv_name;
    String tv_number;
    String tv_option;
    int tempimage;

    public informationItem(String iv_imageview, String tv_name, String tv_number, String tv_option, int tempimage)
    {
        this.iv_imageview = iv_imageview;
        this.tv_name = tv_name;
        this.tv_number = tv_number;
        this.tv_option = tv_option;
        this.tempimage = tempimage;
    }

    public String getIv_imageview() {
        return iv_imageview;
    }

    public String getTv_name() {
        return tv_name;
    }

    public String getTv_number() {
        return tv_number;
    }

    public String getTv_option() {
        return tv_option;
    }

    public int getTempimage() {
        return tempimage;
    }

    public void setIv_imageview(String iv_imageview) {
        this.iv_imageview = iv_imageview;
    }

    public void setTv_name(String tv_name) {
        this.tv_name = tv_name;
    }

    public void setTv_number(String tv_number) {
        this.tv_number = tv_number;
    }

    public void setTv_option(String tv_option) {
        this.tv_option = tv_option;
    }

    public void setTempimage(int tempimage) {
        this.tempimage = tempimage;
    }
}


