package steam.appjam.sopt.com.myapplication.database;

import android.provider.BaseColumns;

/**
 * Created by parkkyounghyun on 2016. 7. 2..
 */
public final class DataBases {

    public static final class CreateDB implements BaseColumns {
        public static final String USERNAME = "userid";
        public static final String ROOMID = "roomid";
        public static final String ROOMNAME = "roomname";
        public static final String IMAGE = "image";
        public static final String CONTENT = "content";
        public static final String TAG = "tag";
        public static final String _TABLENAME = "bookmark";
        // id name number time image
        public static final String _CREATE =
                "create table "+_TABLENAME+"("
                        +_ID+" integer primary key autoincrement, "
                        +USERNAME+" text not null , "
                        +ROOMID+" text not null , "
                        +ROOMNAME+" text not null , "
                        +CONTENT+" text not null , "
                        +TAG+" text not null , "
                        +IMAGE+" text not null );";

    }

}
