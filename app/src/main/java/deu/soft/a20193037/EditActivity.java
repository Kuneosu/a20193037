/*
 * 개인프로젝트 - EditActivity.java
 * 개발자 : 컴퓨터공학과 20193037 김권수
 * 20193037@office.deu.ac.kr
 */

package deu.soft.a20193037;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class EditActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_activity);
        ActionBar ac = getSupportActionBar();

        // MemoActivity 부터 선택된 아이템의 ID를 intent 로 전달 받음
        Intent memoIntent = getIntent();
        int id = memoIntent.getIntExtra("id",0);

        // 선택된 아이템의 ID에 맞게 액션바 타이틀 수정
        ac.setTitle(id+"번 메모 수정");

        originMemo(id);

        Button editBtnSave = (Button) findViewById(R.id.edit_btn_save);
        Button editBtnBack = (Button) findViewById(R.id. edit_btn_back);

        editBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEdit_Dialog(id);
            }
        });

        editBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBack_Dialog();
            }
        });


    }

    // 기존 메모를 불러옵니다
    void originMemo(int id){
        myDBHelper helper = new myDBHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();

        EditText editEditTitle = (EditText) findViewById(R.id.edit_edit_title);
        EditText editEditContent = (EditText) findViewById(R.id.edit_edit_content);

        Cursor cursor = db.rawQuery("SELECT * FROM memo20193037 WHERE id="+id,null);

        while (cursor.moveToNext()) {
            editEditTitle.setText(cursor.getString(1));
            editEditContent.setText(cursor.getString(2));
        }

        cursor.close();
        db.close();
    }

    // 새로 작성한 메모를 DB에 저장합니다
    void EditMemo(String title, String content,int id){
        myDBHelper helper = new myDBHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();

        String sql = "UPDATE memo20193037 SET title='"+title+"', content='"+content+"', last_modified_date=CURRENT_TIMESTAMP " +
                "WHERE id="+id+";";
        db.execSQL(sql);
        db.close();
        finish();
    }

    void showBack_Dialog(){
        AlertDialog.Builder msgBuilder = new AlertDialog.Builder(EditActivity.this)
                .setMessage("현재 수정하는 메모를 취소하시겠습니까?")
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

    void showEdit_Dialog(int id){
        AlertDialog.Builder msgBuilder = new AlertDialog.Builder(EditActivity.this)
                .setMessage("정말 "+id+"번 메모를 수정 할까요?")
                .setPositiveButton("확인",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText editEditTitle = (EditText) findViewById(R.id.edit_edit_title);
                        EditText editEditContent = (EditText) findViewById(R.id.edit_edit_content);

                        String title = editEditTitle.getText().toString();
                        String content = editEditContent.getText().toString();

                        EditMemo(title,content,id);
                    }
                })
                .setNegativeButton("취소", null);
        AlertDialog msgDlg = msgBuilder.create();
        msgDlg.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((MainActivity)MainActivity.context).displayList();
    }
}
