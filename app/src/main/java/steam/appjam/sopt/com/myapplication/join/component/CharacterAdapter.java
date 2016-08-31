package steam.appjam.sopt.com.myapplication.join.component;

import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import steam.appjam.sopt.com.myapplication.R;

/**
 * Created by Isoft on 2016-07-04.
 */
public class CharacterAdapter extends PagerAdapter {

    LayoutInflater inflater;

    public CharacterAdapter(LayoutInflater inflater) {
        // TODO Auto-generated constructor stub

        //전달 받은 LayoutInflater를 멤버변수로 전달
        this.inflater=inflater;
    }

    //PagerAdapter가 가지고 잇는 View의 개수를 리턴
    //보통 보여줘야하는 이미지 배열 데이터의 길이를 리턴
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 3; //이미지 개수 리턴(monstudy character 3개)
    }

    //ViewPager가 현재 보여질 Item(View객체)를 생성할 필요가 있는 때 자동으로 호출
    //스크롤을 통해 현재 보여져야 하는 View를 만들어냄.
    //첫번째 파라미터 : ViewPager
    //두번째 파라미터 : ViewPager가 보여줄 View의 위치(가장 처음부터 0,1,2,3...)
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // TODO Auto-generated method stub

        View view=null;

        //새로운 View 객체를 Layoutinflater를 이용해서 생성
        view= inflater.inflate(R.layout.activity_join_char_img, null);

        //만들어진 View안에 있는 ImageView 객체 참조
        //위에서 inflated 되어 만들어진 view로부터 findViewById()를 해야 하는 것에 주의.
        ImageView img= (ImageView)view.findViewById(R.id.img_viewpager_character);

        //ImageView에 현재 position 번째에 해당하는 이미지를 보여주기 위한 작업
        //현재 position에 해당하는 이미지를 setting
        TextView char_name = (TextView)view.findViewById(R.id.char_name);
        TextView textView1 = (TextView)view.findViewById(R.id.info1);
        TextView textView2 = (TextView)view.findViewById(R.id.info2);
        TextView textView3 = (TextView)view.findViewById(R.id.info3);
        TextView textView4 = (TextView)view.findViewById(R.id.info4);

        textView1.setText("관심사");
        textView3.setText("자주 찾는 공간");

        if(position==0) {
            img.setImageResource(R.drawable.ic_character_hobby_big);

            char_name.setTextColor(Color.parseColor("#E23E1E"));
            textView1.setTextColor(Color.parseColor("#E23E1E"));
            textView2.setTextColor(Color.parseColor("#E23E1E"));
            textView3.setTextColor(Color.parseColor("#E23E1E"));
            textView4.setTextColor(Color.parseColor("#E23E1E"));

            char_name.setText("하비");
            textView1.setText("  예술, 문화");
            textView4.setText("  취미활동을 위한 스터디룸");
        }

        else if(position==1) {
            img.setImageResource(R.drawable.ic_character_ready_big);

            char_name.setTextColor(Color.parseColor("#60B0CC"));
            textView1.setTextColor(Color.parseColor("#60B0CC"));
            textView2.setTextColor(Color.parseColor("#60B0CC"));
            textView3.setTextColor(Color.parseColor("#60B0CC"));
            textView4.setTextColor(Color.parseColor("#60B0CC"));

            char_name.setText("레디");
            textView1.setText("  취업");
            textView4.setText("  조용한 공간");
        }
        else {
            img.setImageResource(R.drawable.ic_character_teach_big);

            char_name.setTextColor(Color.parseColor("#7B69FF"));
            textView1.setTextColor(Color.parseColor("#7B69FF"));
            textView2.setTextColor(Color.parseColor("#7B69FF"));
            textView3.setTextColor(Color.parseColor("#7B69FF"));
            textView4.setTextColor(Color.parseColor("#7B69FF"));

            char_name.setText("티치");
            textView1.setText("  강의");
            textView4.setText("  칠판이 있는 곳 어디든");
        }
        //ViewPager에 만들어 낸 View 추가
        container.addView(view);

        //Image가 세팅된 View를 리턴
        return view;
    }

    //화면에 보이지 않은 View는파쾨를 해서 메모리를 관리함.
    //첫번째 파라미터 : ViewPager
    //두번째 파라미터 : 파괴될 View의 인덱스(가장 처음부터 0,1,2,3...)
    //세번째 파라미터 : 파괴될 객체(더 이상 보이지 않은 View 객체)
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // TODO Auto-generated method stub

        //ViewPager에서 보이지 않는 View는 제거
        //세번째 파라미터가 View 객체 이지만 데이터 타입이 Object여서 형변환 실시
        container.removeView((View)object);

    }

    //instantiateItem() 메소드에서 리턴된 Ojbect가 View가  맞는지 확인하는 메소드
    @Override
    public boolean isViewFromObject(View v, Object obj) {
        // TODO Auto-generated method stub
        return v==obj;
    }

}