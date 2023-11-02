/*
 * 개인프로젝트 - AppDatabase.java
 * 개발자 : 컴퓨터공학과 20193037 김권수
 * 20193037@office.deu.ac.kr
 */

package deu.soft.a20193037;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {MemoEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract MemoDao memoDao();

    private static AppDatabase INSTANCE;

    public static AppDatabase getDBInstance(Context context){
        //INSTANCE 가 null 이면 초기화
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "prj20193037")
                    .allowMainThreadQueries().build();
        }
        return INSTANCE;
    }
}
