package project.an.readnewsapp.Service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static DatabaseHelper instance;
    private static final String DATABASE_NAME = "ReadNews.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "bookmark";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_LINK = "link";
    private static final String COLUMN_IMG = "image_path";
    private static final String COLUMN_CONTENT = "content";
    private static final String COLUMN_DATE = "pub_date";
    private static final String COLUMN_CAT = "category";
    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " ("+
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    COLUMN_TITLE + " TEXT, "+
                    COLUMN_LINK + " TEXT, "+
                    COLUMN_IMG + " TEXT, "+
                    COLUMN_CONTENT + " TEXT, "+
                    COLUMN_DATE + " TEXT, "+
                    COLUMN_CAT + " TEXT)";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DatabaseHelper getInstance(Context context){
        if(instance == null){
            Log.i("Dữ liệu", "Tạo mới cơ sở dữ liệu");
            return new DatabaseHelper(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long insertBookmark(String title, String link, String img, String content, String pubDate, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_LINK, link);
        values.put(COLUMN_IMG, img);
        values.put(COLUMN_DATE, pubDate);
        values.put(COLUMN_CONTENT, content);
        values.put(COLUMN_CAT, category);
        return db.insert(TABLE_NAME, null, values);
    }

    public int deleteBookmark(String title){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("bookmark", "title =?", new String[]{title});
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    public boolean isBookmarked(String title) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM bookmark WHERE title = ?", new String[]{title});
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public boolean isDatabaseEmpty() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM bookmark", null);

        if (cursor != null) {
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            cursor.close();
            return count == 0;
        }

        return true;
    }
}
