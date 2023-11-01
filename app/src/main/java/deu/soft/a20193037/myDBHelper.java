/*
 * 개인프로젝트 - myDBHelper.java
 * 개발자 : 컴퓨터공학과 20193037 김권수
 * 20193037@office.deu.ac.kr
 */

package deu.soft.a20193037;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class myDBHelper extends SQLiteOpenHelper {
    public myDBHelper(Context context){
        super(context, "prj20193037",null ,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // DB 생성
        db.execSQL("CREATE TABLE memo20193037 " +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT," +
                "content TEXT," +
                "created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "last_modified_date TIMESTAMP DEFAULT NULL );");

        // dummy 데이터 입력
//        db.execSQL("INSERT INTO memo20193037(title,content) VALUES('제목1','내용1')");
//        db.execSQL("INSERT INTO memo20193037(title,content) VALUES('제목2','내용2')");
//        db.execSQL("INSERT INTO memo20193037(title,content) VALUES('제목3','내용3')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS memo20193037");
        onCreate(db);
    }
}