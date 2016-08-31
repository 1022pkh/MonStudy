package steam.appjam.sopt.com.myapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * DB에 대한 함수가 정의된 곳
 *
 */
public class DbOpenHelper {

    private static final String DATABASE_NAME = "bookmarklist.db";
    private static final int DATABASE_VERSION = 1;
    public static SQLiteDatabase mDB;
    private DatabaseHelper mDBHelper;
    private Context mCtx;

    private ArrayList<ItemData> itemDatas = null;
    private class DatabaseHelper extends SQLiteOpenHelper {

        // 생성자
        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        // 최초 DB를 만들때 한번만 호출된다.
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DataBases.CreateDB._CREATE);

        }

        // 버전이 업데이트 되었을 경우 DB를 다시 만들어 준다.
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+ DataBases.CreateDB._TABLENAME);
            onCreate(db);
        }
    }

    public DbOpenHelper(Context context){
        this.mCtx = context;
    }

    public DbOpenHelper open() throws SQLException {
        mDBHelper = new DatabaseHelper(mCtx, DATABASE_NAME, null, DATABASE_VERSION);
        mDB = mDBHelper.getWritableDatabase();
        return this;
    }
    /**
     * DB에 데이터 추가
    */
    public void DbInsert(String userId,String roomId, String roomName, String image ,String content, String tag ){

        mDB = mDBHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("userid",userId);
        values.put("roomid",roomId);
        values.put("roomname",roomName);
        values.put("content",content);
        values.put("tag", tag);
        values.put("image",image);


        mDB.insert("bookmark",null,values);

    }

    /**
     * DB항목 업그레이드 - 수정할 때 사용
     */
    public void DbUpdate(String id,String roomId, String roomName, String image ,String content, String tag ){

        ContentValues values = new ContentValues();
        values.put("userid",roomId);
        values.put("roomid",roomId);
        values.put("roomname",roomName);
        values.put("content",content);
        values.put("tag", tag);
        values.put("image",image);

        mDB.update("bookmark", values, "_id=?", new String[]{id});

    }

    /**
     * 항목 삭제하는 함수
     * @param id
     */
    public void DbDelete(String id) {

        mDB.delete("bookmark", "_id=?", new String[]{id});

    }


    /**
     * bookmark테이블에 저장되어있는 값들을 반환하는 함수 - 리스트뷰 뿌릴 때 호출
     * @return
     */
    public ArrayList<ItemData> DbSelect(){
        SQLiteDatabase getDb;
        getDb = mDBHelper.getReadableDatabase();
        Cursor c = getDb.rawQuery( "select * from bookmark" , null);
//getDb.query("bookmark", null, null, null, null, null, null);

        itemDatas = new ArrayList<ItemData>();

//        Log.i("dbtest" , "갯수 : " + String.valueOf(c.getCount()));

        while(c.moveToNext()){
            int _id = c.getInt(c.getColumnIndex("_id"));
            String userId = c.getString(c.getColumnIndex("userid"));
            String roomId = c.getString(c.getColumnIndex("roomid"));
            String roomName = c.getString(c.getColumnIndex("roomname"));
            String content = c.getString(c.getColumnIndex("content"));
            String tag = c.getString(c.getColumnIndex("tag"));
            String image = c.getString(c.getColumnIndex("image"));


            ItemData listViewItem = new ItemData();
            listViewItem.Id = _id;
            listViewItem.userId = userId;
            listViewItem.roomId = roomId;
            listViewItem.roomName = roomName;
            listViewItem.content = content;
            listViewItem.tag = tag;
            listViewItem.image = image;

            itemDatas.add(0,listViewItem);

        }


        return itemDatas;
    }

    public void close(){
        mDB.close();
    }

}
