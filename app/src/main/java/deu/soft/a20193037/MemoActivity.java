/*
 * 개인프로젝트 - MemoActivity.java
 * 개발자 : 컴퓨터공학과 20193037 김권수
 * 20193037@office.deu.ac.kr
 */

package deu.soft.a20193037;

import android.annotation.SuppressLint;
import android.app.Activity;
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

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MemoActivity extends AppCompatActivity {
    TextView memoTvCreated, memoTvModified, memoTvTitle, memoTvContent;
    Button memoBtnEdit, memoBtnDel, memoBtnBack;
    AppDatabase db;

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
                setResult(Activity.RESULT_OK);
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
                Log.d("CHECKSUM", "setResult: "+Activity.RESULT_OK);
                //setResult(Activity.RESULT_OK);
                finish();
            }
        });
    }

    void deleteMemo(int id){
        db = AppDatabase.getDBInstance(this);

        db.memoDao().deleteMemo(id);
        Toast.makeText(getApplicationContext(), id+"번 메모가 삭제되었습니다.", Toast.LENGTH_SHORT).show();

        Log.d("CHECKSUM", "setResult: "+Activity.RESULT_OK);
        setResult(Activity.RESULT_OK);
        finish();
    }

    void displayMemo(int id){
        db = AppDatabase.getDBInstance(this);

        memoTvCreated = (TextView) findViewById(R.id.memo_tv_created);
        memoTvModified = (TextView) findViewById(R.id.memo_tv_modified);
        memoTvTitle = (TextView) findViewById(R.id.memo_tv_title);
        memoTvContent = (TextView) findViewById(R.id.memo_tv_content);

        memoTvContent.setMovementMethod(new ScrollingMovementMethod());

        // 해당 id 의 메모 내용을 가져오는 SQL 문
        MemoEntity memoData = db.memoDao().loadMemo(id);

        memoTvCreated.setText(memoTvCreated.getText()+memoData.createdDate);
        memoTvModified.setText(memoTvModified.getText()+memoData.lastModifiedDate);
        memoTvTitle.setText(memoTvTitle.getText()+memoData.title);
        memoTvContent.setText(memoData.content);

        if(memoData.lastModifiedDate != null){
            memoTvModified.setVisibility(View.VISIBLE);
        }

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
        Log.d("CHECKSUM", "onDestroy: Memo");
        super.onDestroy();
    }
    
    // 뒤로가기 두 번 클릭시 액티비티 종료
    private long backKeyPressedTime = 0;
    // 기존 onBackPressed 기능을 버리기 위해 super call 을 사용하지 않음.
    @SuppressLint("MissingSuperCall")
    public void onBackPressed(){
        if(System.currentTimeMillis() > backKeyPressedTime + 2000){
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(this, "한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(System.currentTimeMillis()<=backKeyPressedTime + 2000){
            Log.d("CHECKSUM", "setResult: "+Activity.RESULT_OK);
            //setResult(Activity.RESULT_OK);
            finish();
        }
    }
}
