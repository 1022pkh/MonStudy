package steam.appjam.sopt.com.myapplication.board.type;

/**
 * Created by parkkyounghyun on 2016. 7. 8..
 */
public class BoardRegisterType {

    /**
     * request.body.Post_title,
     * request.body.Post_contents,
     * request.body.User_id,
     * request.body.Post_location_id,
     * request.body.Post_category_id
     */

    String Post_title;
    String Post_contents;
    String User_id;
    String Post_location_id;
    String Post_category_id;

    public BoardRegisterType(String Post_title, String Post_contents, String User_id, String Post_location_id, String Post_category_id) {
        this.Post_title = Post_title;
        this.Post_contents = Post_contents;
        this.User_id = User_id;
        this.Post_location_id = Post_location_id;
        this.Post_category_id = Post_category_id;
    }

}
