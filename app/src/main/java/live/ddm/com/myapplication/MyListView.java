package live.ddm.com.myapplication;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * Created by Administrator on 2018/5/5 0005.
 */

public class MyListView extends ListView {
    boolean listOnTop;
    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOnScrollListener(new AbsListView.OnScrollListener(){
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0) {
                    listOnTop = true;
                    Log.d("ListView", "##### 滚动到顶部 #####");
                } else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                    Log.d("ListView", "##### 滚动到底部 ######");
                    listOnTop = false;
                } else {
                    listOnTop = false;
                }
            }
        });
    }
    int lastY;
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//
//        switch (ev.getAction()){
//            case MotionEvent.ACTION_DOWN:
//                getParent().requestDisallowInterceptTouchEvent(true);
//                 lastY=(int) ev.getRawY();
//                break;
//            case MotionEvent.ACTION_MOVE:
//               int dy=(int)  ev.getRawY()-lastY;
//               if (dy>0 && listOnTop){
//                   getParent().requestDisallowInterceptTouchEvent(false);
//               }else if (dy<0 && !((CustomView)getParent()).onTop){
//                   getParent().requestDisallowInterceptTouchEvent(false);
//               }else {
//                   getParent().requestDisallowInterceptTouchEvent(true);
//               }
//                break;
//        }


       return super.dispatchTouchEvent(ev);
    }

}
