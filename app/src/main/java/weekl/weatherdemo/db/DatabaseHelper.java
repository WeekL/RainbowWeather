package weekl.weatherdemo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import weekl.weatherdemo.base.BaseApplication;

public class DatabaseHelper extends SQLiteOpenHelper {
    private final String TB_WEATHER = "T_WEATHER_DATA";

    private static DatabaseHelper sInstance;

    /**
     * 创建天气数据表
     */
    private final String CREATE_TABLE_WEATHER = "CREATE TABLE IF NOT EXISTS " + TB_WEATHER
            + "(id int PRIMARY KEY AUTOINCREMENT," +
            "location text NOT NULL," +
            "udpateAt text NOT NULL," +
            "responseText text NOT NULL)";

    public static DatabaseHelper getInstance(){
        synchronized (DatabaseHelper.class){
            if (sInstance == null){
                synchronized (DatabaseHelper.class){
                    sInstance = new DatabaseHelper(BaseApplication.getContext(),"WEATHER",null,1);
                }
            }
        }
        return sInstance;
    }

    private DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_WEATHER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TB_WEATHER);
        onCreate(sqLiteDatabase);
    }

    public void save(String location, String updateTime, String responseText){
        // TODO: 2018/6/17 0017 保存数据
    }

    public void delete(String location){
        // TODO: 2018/6/17 0017 删除数据
    }

    public List<String> getData(){
        List<String> datas = new ArrayList<>();
        // TODO: 2018/6/17 0017 获取数据
        return datas;
    }

    public void update(String location, String updateTime){
        // TODO: 2018/6/17 0017 更新数据
    }
}
