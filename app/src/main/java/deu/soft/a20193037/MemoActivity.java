/*
 * 개인프로젝트 - MemoActivity.java
 * 개발자 : 컴퓨터공학과 20193037 김권수
 * 20193037@office.deu.ac.kr
 */

package deu.soft.a20193037;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MemoActivity extends AppCompatActivity {
    TextView memoTvCreated, memoTvModified, memoTvTitle, memoTvContent;
    Button memoBtnEdit, memoBtnDel, memoBtnBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memo_activity);
        ActionBar ac = getSupportActionBar();

        // MainActivity로 부터 선택된 아이템의 ID를 intent 로 전달 받음
        Intent memoIntent = getIntent();
        int id = memoIntent.getIntExtra("id",0);

        // 선택된 아이템의 ID에 맞게 액션바 타이틀 수정
        ac.setTitle(id+"번 메모");

        displayMemo(id);

        memoBtnDel = (Button) findViewById(R.id.memo_btn_del);
        memoBtnBack = (Button) findViewById(R.id.memo_btn_back);
        memoBtnEdit = (Button) findViewById(R.id.memo_btn_edit);

        memoBtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditActivity.class);
                // 선택된 아이템의 ID를 intent로 전달
                intent.putExtra("id",id);
                startActivity(intent);
                finish();
            }
        });

        memoBtnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDel_Dialog(id);
            }
        });
        memoBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    void deleteMemo(int id){
        myDBHelper helper = new myDBHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();

        db.execSQL("DELETE FROM memo20193037 WHERE id="+id);
        Toast.makeText(getApplicationContext(), id+"번 메모가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
        db.close();
        finish();
    }

    void displayMemo(int id){
        // myDBHelper의 읽기모드 객체를 가져와 SQLiteDatabase에 담아 사용준비
        myDBHelper helper = new myDBHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();

        memoTvCreated = (TextView) findViewById(R.id.memo_tv_created);
        memoTvModified = (TextView) findViewById(R.id.memo_tv_modified);
        memoTvTitle = (TextView) findViewById(R.id.memo_tv_title);
        memoTvContent = (TextView) findViewById(R.id.memo_tv_content);

        memoTvContent.setMovementMethod(new ScrollingMovementMethod());

        Cursor cursor = db.rawQuery("SELECT * FROM memo20193037 WHERE id="+id,null);

        while (cursor.moveToNext()) {
            memoTvCreated.setText(memoTvCreated.getText()+cursor.getString(3));
            memoTvTitle.setText(memoTvTitle.getText()+cursor.getString(1));
            memoTvContent.setText(memoTvContent.getText()+cursor.getString(2));
            if(cursor.getString(4) != null){
                memoTvModified.setVisibility(View.VISIBLE);
                memoTvModified.setText(memoTvModified.getText()+cursor.getString(4));
            }
        }
        cursor.close();
        db.close();

    }

    void showDel_Dialog(int id){
        AlertDialog.Builder msgBuilder = new AlertDialog.Builder(MemoActivity.this)
                .setMessage("이 메모("+id+"번)를 삭제 하시겠습니까?")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteMemo(id);
                    }
                })
                .setNegativeButton("취소",null);
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
