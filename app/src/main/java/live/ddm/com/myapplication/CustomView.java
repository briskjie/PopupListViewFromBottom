package live.ddm.com.myapplication;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Scroller;

public class CustomView extends LinearLayout {
    private static final String TAG = "CustomView";
    private Scroller mScroller;
    private ListView listView;
    private int lastY;
    private int scrollY;
    private boolean scrollUp;
    private int initTop;

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        initTop = getTop();
        listView = (ListView) getChildAt(0);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0) {//滑动顶部
                    listOnTop = true;
                } else {
                    listOnTop = false;
                }
            }
        });
    }


    public void smoothScrollBy(int startX, int startY, int endX, int endY) {
        //设置mScroller的滚动偏移量
        mScroller.startScroll(startX, startY, endX - startX, endY - startY, 1000);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            offsetTopAndBottom(mScroller.getCurrY() - getTop());
            postInvalidate();
        }
        super.computeScroll();
    }


    public boolean onTop;
    boolean listOnTop;
    int preY;

    public void setOnTop(boolean onTop) {
        this.onTop = onTop;
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getRawY() - preY > 0 && listOnTop) {
            preY = (int) ev.getRawY();
            return true;
        }
        preY = (int) ev.getRawY();
        if (!onTop) {
            return true;
        }
        return false;

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int dy = (int) event.getRawY() - lastY;
                if (dy < 0) {
                    scrollUp = true;
                } else {
                    scrollUp = false;
                }
                int l = getLeft();
                int r = getRight();
                int t = getTop() + dy;
                int b = getBottom() + dy;


                scrollY = initTop - t;
                System.out.println("cxx:scrollY" + scrollY);
                //下面判断移动是否超出屏幕
                if (scrollY < DensityUtils.dp2px(getContext(), 500)) {
                    layout(l, t, r, b);
                    lastY = (int) event.getRawY();
                    postInvalidate();
                }

                break;
            case MotionEvent.ACTION_UP:
                if (scrollY > (DensityUtils.dp2px(getContext(), 500 / 2)) && scrollUp) {
                    smoothScrollBy(0, getTop(), 0, initTop - DensityUtils.dp2px(getContext(), 500));
                    postInvalidate();
                    setOnTop(true);
                } else if (scrollY <= DensityUtils.dp2px(getContext(), 500 / 2) && scrollUp) {
                    setOnTop(false);
                    smoothScrollBy(0, getTop(), 0, initTop - DensityUtils.dp2px(getContext(), 250));
                    postInvalidate();
                } else if (!scrollUp && scrollY > DensityUtils.dp2px(getContext(), 500 / 2)) {
                    setOnTop(false);
                    smoothScrollBy(0, getTop(), 0, initTop - DensityUtils.dp2px(getContext(), 250));
                    postInvalidate();
                } else if (!scrollUp && scrollY <= DensityUtils.dp2px(getContext(), 500 / 2)) {
                    setOnTop(false);
                    smoothScrollBy(0, getTop(), 0, initTop);
                    postInvalidate();
                }

                break;
        }
        return true;
    }

    public void show() {
        smoothScrollBy(0, initTop, 0, initTop - DensityUtils.dp2px(getContext(), 250));
    }
}