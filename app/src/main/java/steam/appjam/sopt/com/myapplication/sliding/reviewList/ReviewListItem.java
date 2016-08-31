package steam.appjam.sopt.com.myapplication.sliding.reviewList;

/**
 * Created by user on 2016-07-03.
 */
public class ReviewListItem {
    String name;
    String date;
    String room;
    String content;
    String star;

    public ReviewListItem(String name, String date, String room, String content,String star)
    {
        this.name = name;
        this.date = date;
        this.room = room;
        this.content = content;
        this.star = star;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getRoom() {
        return room;
    }

    public String getContent() {
        return content;
    }

    public String getStar(){
        return star;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
