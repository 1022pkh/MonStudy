package steam.appjam.sopt.com.myapplication.network;

/**
 * Created by Min_Mac on 16. 4. 13..
 */


import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import steam.appjam.sopt.com.myapplication.afterFilter.type.FilterType;
import steam.appjam.sopt.com.myapplication.board.BoardRegister;
import steam.appjam.sopt.com.myapplication.board.type.BoardDetailType;
import steam.appjam.sopt.com.myapplication.board.type.BoardRegisterType;
import steam.appjam.sopt.com.myapplication.board.type.BoardType;
import steam.appjam.sopt.com.myapplication.board.type.Board_register;
import steam.appjam.sopt.com.myapplication.board.type.ReplyContent;
import steam.appjam.sopt.com.myapplication.board.type.ReplyType;
import steam.appjam.sopt.com.myapplication.board.type.Reply_register;
import steam.appjam.sopt.com.myapplication.join.model.JoinUser;
import steam.appjam.sopt.com.myapplication.login.model.Authentication;
import steam.appjam.sopt.com.myapplication.login.model.User;
import steam.appjam.sopt.com.myapplication.main.type.MainType;
import steam.appjam.sopt.com.myapplication.main.type.Main_info;
import steam.appjam.sopt.com.myapplication.profile.model.UserProfile;
import steam.appjam.sopt.com.myapplication.sliding.type.SpaceDeteilType;

public interface NetworkService {

    @GET("/session")
    Call<User> getSession();

    //로그인
    @POST("/session/sign/in")
    Call<User> login(@Body Authentication authentication);

    //로그아웃
    @GET("/session/sign/out")
    Call<Object> logout();

    //아이디 중복 확인
    @GET("/membership/{user_id}")
    Call<String> duplicationTest(@Path("user_id") String user_id);

    //닉네임 중복 확인
    @GET("/membership/name/{name}")
    Call<String> duplicationName(@Path("name") String name);

    //회원가입
    @POST("/membership")
    Call<User> registerUser(@Body JoinUser user);

    //프로필 가져오기
    @GET("/profile/{user_id}")
    Call<UserProfile> getUserProfile(@Path("user_id") String user_id);

    //상세페이지 가져오기
    @GET("/mainpage")
    Call<MainType> getMainData();


    //상세페이지 가져오기
    @GET("/space/{space_id}")
    Call<SpaceDeteilType> getRoomData(@Path("space_id") String space_id);

    //조건검색
    @GET("/search/{si}/{gu}/{scate}/{cap}/{order}")
    Call<FilterType> getSearchRoom(@Path("si") String si, @Path("gu") String gu, @Path("scate") String scate, @Path("cap") String cap, @Path("order") String order);


    //게시판 등록
    @POST("/post")
    Call<Board_register> RegisterBoard(@Body BoardRegisterType boardRegister);


    //게시판 가져오기
    @GET("/post/upload")
    Call<BoardType> getBoard();

    //게시글 상세 보기
    @GET("/post/{post}")
    Call<BoardDetailType> getBoardDetail(@Path("post") String post);


    //댓글 가져오기
    @GET("/reply/{post_id}")
    Call<ReplyType> getReply(@Path("post_id") String post_id);

    //댓글 쓰기
    //Reply_contents/Post_id
    @POST("/reply")
    Call<Reply_register> registerReply(@Body ReplyContent replyContent);

}
