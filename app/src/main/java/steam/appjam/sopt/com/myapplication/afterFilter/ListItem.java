package steam.appjam.sopt.com.myapplication.afterFilter;

/**
 * Created by user on 2016-06-30.
 */
public class ListItem {
    String id;
    String image;
    String name; // 스터디룸 이름
    String price; // 가격
    String time; // 시간
    String grade;

    public ListItem(String id,String image, String name, String price, String time, String grade)
    {
        this.id = id;
        this.image = image;
        this.name = name;
        this.price = price;
        this.time = time;
        this.grade = grade;
    }
    public String getId() {
        return id;
    }
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public String getGrade() {
        return grade;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getTime() {
        return time;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
