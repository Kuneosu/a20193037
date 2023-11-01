/*
 * 개인프로젝트 - CreateActivity.java
 * 개발자 : 컴퓨터공학과 20193037 김권수
 * 20193037@office.deu.ac.kr
 */

package deu.soft.a20193037;

import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class CreateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_activity);
        ActionBar ac = getSupportActionBar();
        ac.setTitle("새 메모 작성");

        Button createBtnBack = (Button) findViewById(R.id.create_btn_back);
        Button createBtnSave = (Button) findViewById(R.id.create_btn_save);

        EditText createEditTitle = (EditText) findViewById(R.id.create_edit_title);
        EditText createEditContent = (EditText) findViewById(R.id.create_edit_content);

        createBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = createEditTitle.getText().toString();
                String content = createEditContent.getText().toString();
                // 제목 란이 비었을 경우 메시지 출력
                if(title.length() == 0){
                    Toast.makeText(getApplicationContext(), "메모의 '제목'란이 비워졌습니다.", Toast.LENGTH_SHORT).show();
                }else
                    SaveMemo(title,content);
            }
        });

        createBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBack_Dialog();
            }
        });
    }


    // 입력받은 내용을 토대로 메모를 DB에 저장
    void SaveMemo(String title,String content){
        myDBHelper helper = new myDBHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();

        String sql = "INSERT INTO memo20193037(title,content) VALUES('"+title+"','"+content+"');";
        db.execSQL(sql);
        db.close();
        finish();
    }

    void showBack_Dialog(){
        AlertDialog.Builder msgBuilder = new AlertDialog.Builder(CreateActivity.this)
                .setMessage("현재 작성하는 메모를 취소하시겠습니까?")
                .setPositiveButton("확인",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton("취소", null);
        AlertDialog msgDlg = msgBuilder.create();
        msgDlg.show();
    }

    // 삭제 시 액티비티가 종료되면서 MainActivity의 리스트뷰를 갱신한다.
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((MainActivity)MainActivity.context).displayList();
    }
}
