package steam.appjam.sopt.com.myapplication.sliding;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

/**
 * Created by Isoft on 2016-07-03.
 */
public class ListInScroll extends ListView {

    public ListInScroll(Context context) {
        super(context);
    }

    public ListInScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListInScroll(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, View.MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}