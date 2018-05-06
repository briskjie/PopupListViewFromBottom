package live.ddm.com.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Scroller;

import java.util.List;

public class CustomView extends LinearLayout {

    private static final String TAG = "Scroller";

    private Scroller mScroller;
    ListView listView;

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        listView = (ListView) getChildAt(0);
    }

    //调用此方法滚动到目标位置
    public void smoothScrollTo(int fx, int fy) {
        int dx = fx - mScroller.getFinalX();
        int dy = fy - mScroller.getFinalY();
        smoothScrollBy(dx, dy);
    }

    //调用此方法设置滚动的相对偏移   
    public void smoothScrollBy(int dx, int dy) {

        //设置mScroller的滚动偏移量   
//        mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), dx, dy,1000);
//        invalidate();//这里必须调用invalidate()才能保证computeScroll()会被调用，否则不一定会刷新界面，看不到滚动效果
    }

    public void smoothScrollBy(int startX, int startY, int endX, int endY) {

        //设置mScroller的滚动偏移量
        mScroller.startScroll(startX, startY, endX - startX, endY - startY, 1000);
        invalidate();//这里必须调用invalidate()才能保证computeScroll()会被调用，否则不一定会刷新界面，看不到滚动效果
    }

    @Override
    public void computeScroll() {

        //先判断mScroller滚动是否完成   
        if (mScroller.computeScrollOffset()) {

            //这里调用View的scrollTo()完成实际的滚动   
//            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());

            offsetTopAndBottom(mScroller.getCurrY() - getTop());
            //必须调用该方法，否则不一定能看到滚动效果
            System.out.println("cxx_top_bottom:" + getTop() + ":" + getBottom());
            postInvalidate();
        }
        super.computeScroll();
    }


    public boolean onTop;
    boolean listOnTop;

    public void setOnTop(boolean onTop) {
        this.onTop = onTop;
    }

    int preY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
                      if (ev.getRawY()-preY>0 && listOnTop ){
                          preY=(int) ev.getRawY();
                          return true;
                      }
        preY=(int) ev.getRawY();
        Log.d("cxx-intecept:", ev.getRawY() + "");

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0) {
                    listOnTop = true;
                    Log.d("cxx-ListView", "##### 滚动到顶部 #####");
                } else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                    Log.d("cxx-ListView", "##### 滚动到底部 ######");
                    listOnTop = false;
                } else {
                    Log.d("cxx-ListView", "##### 不在顶部 ######");
                    listOnTop = false;
                }
            }
        });

//        listView.setOnTouchListener(onTouchListener);
//
        if (!onTop) {
            return true;
        }
        return false;
//        return true;


//        return true;
//        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//            return false;
//        } else {
//            return true;
//        }
    }

    int lastY;
    OnTouchListener onTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent ev) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    lastY = (int) ev.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    int dy = (int) ev.getRawY() - lastY;
//                    if (dy>0 && listOnTop && onTop || !listOnTop){
//                        requestDisallowInterceptTouchEvent(true);//不拦截
//                    }else {
//                        requestDisallowInterceptTouchEvent(true);
//                    }
                    requestDisallowInterceptTouchEvent(false);
                    break;
            }
            return false;
        }
    };
}