package steam.appjam.sopt.com.myapplication.favorite;

import android.widget.ImageView;

/**
 * Created by 즤 on 2016-07-02.
 */
public class FavoriteSpaceData {
    String bookmarkId;
    String img;
    String userId;        //공간
    String roomId;        //공간
    String roomName;        //공간이름
    String info1;           //공간정보
    String info2;   //공간정보
    ImageView deleteMark;
    ImageView sendKakao;

    public FavoriteSpaceData(String bookmarkId,String userId, String roomId, String img,String roomName, String info1, String info2)
    {
        this.bookmarkId = bookmarkId;
        this.userId = userId;
        this.roomId = roomId;
        this.img = img;
        this.roomName = roomName;
        this.info1 = info1;
        this.info2 = info2;
    }

}
