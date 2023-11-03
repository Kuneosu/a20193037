/*
* 개인프로젝트 - MainActivity.java
* 개발자 : 컴퓨터공학과 20193037 김권수
* 20193037@office.deu.ac.kr
 */

package deu.soft.a20193037;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
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

import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView memoList;
    Button btnSearch;
    TextView tvSearch;
    MyAdapter adapter;
    List<MemoEntity> memoData;
    AppDatabase db;
    boolean sort = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar ac = getSupportActionBar();
        ac.setTitle("내 메모");

        memoList = (ListView) findViewById(R.id.memoList);

        // 첫 실행 시 데이터베이스로 리스트뷰 채우기
        displayList(sort);

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
                    displayList(sort);}
                else
                    SearchList(str);
            }
        });

        // 리스트뷰 아이템 클릭시 해당 메모 열기
        memoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // 선택된 아이템에 대한 MemoEntity
                MemoEntity data = (MemoEntity) adapter.getItem(i);
                Intent intent = new Intent(getApplicationContext(), MemoActivity.class);
                // 선택된 아이템의 ID를 intent로 전달
                intent.putExtra("id",data.id);
                resultLauncher.launch(intent);
            }
        });
    }

    // MainActivity로 돌아올 때 displayList()를 실행시켜 화면을 초기화
    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        displayList(sort);
                    }
                }});

    // 화면에 표시된 리스트뷰에 현재 데이터베이스에 있는 메모들을 불러와 어댑터로 부착
    void displayList(boolean sort){
        // db 변수에 데이터베이스 불러오기
        db = AppDatabase.getDBInstance(this);
        // memoData 변수에 데이터베이스에 저장된 메모 튜플을 모두 불러와 저장
        memoData = sort ? db.memoDao().getAllMemos() : db.memoDao().getAllMemosDesc();
        adapter = new MyAdapter();
        // 어댑터에 불러온 메모 데이터들 저장
        adapter.setData(memoData);
        // 불러온 메모 데이터들이 저장된 어댑터를 리스트뷰에 부착
        memoList.setAdapter(adapter);
    }

    // 입력받은 검색어로 검색 SQL문 실행 후 어댑터 업데이트
    void SearchList(String search){
        db = AppDatabase.getDBInstance(this);
        // memoDAO에 저장해둔 검색 SQL문을 불러와 입력받은 검색어 대입 후 실행
        // 결과값을 memoData에 저장
        memoData = db.memoDao().searchMemo(search);
        adapter = new MyAdapter();
        // 어댑터에 불러온 메모 데이터를 저장
        adapter.setData(memoData);
        // 검색 결과가 없을 경우 예외처리
        if(adapter.getCount()==0)
            Toast.makeText(getApplicationContext(), "'"+search+"' 에 대한 검색 결과가 없습니다.", Toast.LENGTH_SHORT).show();
        // 업데이트된 어댑터를 리스트뷰에 부착
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
                resultLauncher.launch(intent);
                return true;
            case R.id.devInfo:
                // 개발자 정보 이벤트
                showDialog();
                return true;
            case R.id.sort_menu:
                sort = !sort;
                item.setTitle(sort ? "NEW" : "OLD");
                displayList(sort);
                return true;
            case R.id.dummy_data:
                db = AppDatabase.getDBInstance(this);
                db.memoDao().createMemo("오늘의 할일 (11/03)","오늘의 할일\n1. 임베디드 프로젝트 과제" +
                        "\n2. 웹 프로그래밍 수업" +
                        "\n3. 캡스톤 디자인 회의");
                db.memoDao().createMemo("여행 일정","진주 여행 일정\n" +
                        "13:00~15:00 진주성 투어\n" +
                        "15:00~16:00 진주 남강 산책\n" +
                        "16:00~18:00 부산으로 복귀\n");
                db.memoDao().createMemo("캡스톤 디자인 일정","캡스톤 디자인 해야할 것\n" +
                        "- 앱 디자인 확정\n" +
                        "- 시작품 주문\n" +
                        "- 어플리케이션 개발 파트 나누기\n" +
                        "- 어플리케이션 개발 시작\n");
                db.memoDao().createMemo("저녁 장볼거","바나나,우유,와인,치즈,소고기\n");
                db.memoDao().createMemo("회식비 정산","1차 : 210,000원\n" +
                        "2차 : 72,000원\n" +
                        "3차 : 50,000원\n" +
                        "합계 : 332,000원\n");
                db.memoDao().createMemo("2023-2학기 수강 과목","1. 임베디드시스템\n" +
                        "2. 데이터베이스 프로그래밍\n" +
                        "3. 웹 프로그래밍\n" +
                        "4. 운영체제\n" +
                        "5. 캡스톤디자인\n" +
                        "6. 지도교수세미나");
                db.memoDao().createMemo("해야할 과제 정리","1. 임베디드 개인 프로젝트\n" +
                        "2. 운영체제 개인 텀 프로젝트\n" +
                        "3. 데이터베이스 팀 프로젝트\n" +
                        "4. 캡스톤디자인 팀 프로젝트\n");
                displayList(sort);
                return true;
            case R.id.delete_all:
                db = AppDatabase.getDBInstance(this);
                db.memoDao().deleteAllMemo();
                displayList(sort);
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

    @Override
    protected void onDestroy() {
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
            finish();
        }
    }
}