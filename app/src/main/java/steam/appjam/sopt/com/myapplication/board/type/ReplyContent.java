package steam.appjam.sopt.com.myapplication.board.type;

public class ReplyContent {

    public String Post_id;
    public String Reply_contents;
    public String User_id;

    //post_id,replyContent.getText().toString(),LoginUserID
    public ReplyContent(String Post_id, String Reply_contents,String User_id) {
        this.User_id = User_id;
        this.Post_id = Post_id;
        this.Reply_contents = Reply_contents;
    }
}
