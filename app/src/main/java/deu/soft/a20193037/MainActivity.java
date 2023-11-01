/*
* 개인프로젝트 - MainActivity.java
* 개발자 : 컴퓨터공학과 20193037 김권수
* 20193037@office.deu.ac.kr
 */

package deu.soft.a20193037;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView memoList;
    Button btnSearch;
    TextView tvSearch;
    myAdapter adapter;

    // 타 액티비티에서 MainActivity 에 접근하기 위한 Context 변수
    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar ac = getSupportActionBar();
        ac.setTitle("내 메모");

        context = this;
        memoList = (ListView) findViewById(R.id.memoList);

        // 첫 실행 시 데이터베이스로 리스트뷰 채우기
        displayList();

        btnSearch = (Button) findViewById(R.id.btnSearch);
        tvSearch = (TextView) findViewById(R.id.tvSearch);

        // 검색 버튼 클릭시 입력된 텍스트로 검색하기
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = tvSearch.getText().toString();
                // 검색 단어를 입력하지 않았을 경우 예외처리
                if(str.length() == 0){
                    Toast.makeText(getApplicationContext(), "검색단어가 없습니다.", Toast.LENGTH_SHORT).show();
                    displayList();}
                else
                    SearchList(str);
            }
        });

        // 리스트뷰 아이템 클릭시 해당 메모 열기
        memoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // 선택된 아이템에 대한 myAdapterData 변수
                myAdapterData data = (myAdapterData) adapter.getItem(i);
                Intent intent = new Intent(getApplicationContext(), MemoActivity.class);
                Log.d("MainActivity INTENT", ""+data.getId());
                // 선택된 아이템의 ID를 intent로 전달
                intent.putExtra("id",data.getId());
                startActivity(intent);
            }
        });
    }

    void displayList(){
        // myDBHelper의 읽기모드 객체를 가져와 SQLiteDatabase에 담아 사용준비
        myDBHelper helper = new myDBHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        adapter = new myAdapter();

        // Cursor라는 그릇에 목록을 담아주기
        Cursor cursor = db.rawQuery("SELECT * FROM memo20193037",null);

        // 목록의 개수만큼 순회하여 adapter에 있는 list배열에 add
        while(cursor.moveToNext()){
            adapter.addItemToList(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4));
        }

        cursor.close();
        db.close();

        memoList.setAdapter(adapter);
    }

    void SearchList(String search){
        myDBHelper helper = new myDBHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM memo20193037 " +
                "WHERE title LIKE '%"+search+"%' OR content LIKE '%"+search+"%'",null);

        adapter = new myAdapter();

        while(cursor.moveToNext()){
            adapter.addItemToList(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4));
        }

        cursor.close();
        db.close();

        // 검색 결과가 없을 경우 예외처리
        if(adapter.getCount()==0)
            Toast.makeText(getApplicationContext(), "'"+search+"' 에 대한 검색 결과가 없습니다.", Toast.LENGTH_SHORT).show();

        memoList.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.createMemo:
                // 새 메모 작성 이벤트
                Intent intent = new Intent(getApplicationContext(), CreateActivity.class);
                startActivity(intent);
                return true;
            case R.id.devInfo:
                // 개발자 정보 이벤트
                showDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    void showDialog(){
        AlertDialog.Builder msgBuilder = new AlertDialog.Builder(MainActivity.this)
                .setTitle("개발자 정보")
                .setMessage("동의대 컴퓨터공학과 김권수\n(20193037@office.deu.ca.kr)")
                .setPositiveButton("확인",null);
        AlertDialog msgDlg = msgBuilder.create();
        msgDlg.show();
    }
}