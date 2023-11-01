/*
 * 개인프로젝트 - myAdapter.java
 * 개발자 : 컴퓨터공학과 20193037 김권수
 * 20193037@office.deu.ac.kr
 */

package deu.soft.a20193037;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;


public class myAdapter extends BaseAdapter {
    ArrayList<myAdapterData> memoList = new ArrayList<myAdapterData>();

    @Override
    public int getCount() {
        return memoList.size(); // 배열 크기 반환
    }

    @Override
    public Object getItem(int i) {
        return memoList.get(i); // 배열에 현재 위치값을 넣어 아이템을 가져옴
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Context context = viewGroup.getContext();

        // 리스트뷰에 아이템이 인플레이트 되어있는지 확인한 후
        // 아이템이 없다면 아래처럼 아이템 레이아웃을 인플레이트 하고 view 객체에 담는다.
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.memo_listview,viewGroup,false);
        }

        // 아이템에 존재하는 텍스트뷰 객체들을 view 객체에서 찾아 가져온다
//        TextView tvID = (TextView) view.findViewById(R.id.item_tv_id);
        TextView tvTitle = (TextView) view.findViewById(R.id.item_tv_title);
//        TextView tvContent = (TextView) view.findViewById(R.id.item_tv_content);
        TextView tvCreated = (TextView) view.findViewById(R.id.item_tv_created);
//        TextView tvModified = (TextView) view.findViewById(R.id.item_tv_modified);

        // 현재 포지션에 해당하는 아이템에 글자를 적용하기 위해 list 배열에서 객체를 가져온다.
        myAdapterData adapterData = memoList.get(i);

//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // 가져온 객체 안에 있는 글자들을 각 뷰에 적용한다.
//        tvID.setText(Integer.toString(adapterData.getId()));
        tvTitle.setText(adapterData.getTitle());
//        tvContent.setText(adapterData.getContent());
        tvCreated.setText(adapterData.getCreated_date());
//        tvModified.setText(adapterData.getLast_modified_date());

        return view;
    }

    // ArrayList로 선언된 memoList 변수에 목록을 채워주기 위함
    public  void addItemToList(int id, String title, String content, String created, String modified){
        myAdapterData adapterData = new myAdapterData();

        adapterData.setId(id);
        adapterData.setTitle(title);
        adapterData.setContent(content);
        adapterData.setCreated_date(created);
        adapterData.setLast_modified_date(modified);

        memoList.add(adapterData);
    }
}
