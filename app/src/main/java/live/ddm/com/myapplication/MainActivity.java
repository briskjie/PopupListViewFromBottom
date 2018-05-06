package live.ddm.com.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Scroller;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    CustomView llListview;
    Scroller mScroller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        llListview = (CustomView) findViewById(R.id.llListview);
        mScroller = new Scroller(this);
        inttListView();
    }

    private void inttListView() {

        ListView lv = (ListView) findViewById(R.id.list);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "onItemClick", Toast.LENGTH_SHORT).show();
            }
        });

        List<String> your_array_list = Arrays.asList(
                "This",
                "Is",
                "An",
                "Example",
                "ListView",
                "That",
                "You",
                "Can",
                "Scroll",
                ".",
                "It",
                "Shows",
                "How",
                "Any",
                "Scrollable",
                "View",
                "Can",
                "Be",
                "Included",
                "As",
                "A",
                "Child",
                "Of",
                "SlidingUpPanelLayout"
        );

        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                R.layout.item_layout,
                your_array_list );

        lv.setAdapter(arrayAdapter);
    }

    int initTop;
    int initBottom;

    public void move(View view) {
        initTop = llListview.getTop();
        initBottom = llListview.getBottom();
        llListview.smoothScrollBy(0, llListview.getTop(), 0, llListview.getTop() - DensityUtils.dp2px(this, 250));
        llListview.setOnTouchListener(new LLTouchListener());
    }


    public class LLTouchListener implements View.OnTouchListener {
        int lastX;
        int delatY;
        int lastY;
        int scrollY;
        boolean scrollUp;
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int screenHeight = displayMetrics.heightPixels;

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    lastX = (int) event.getRawX();
                    lastY = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    int dy = (int) event.getRawY() - lastY;
                    if (dy < 0) {
                        scrollUp = true;
                    } else {
                        scrollUp = false;
                    }
                    int l = v.getLeft();
                    int r = v.getRight();
                    int t = v.getTop() + dy;
                    int b = v.getBottom() + dy;


                    scrollY = initTop - t;
                    System.out.println("cxx:scrollY" + scrollY);
                    //下面判断移动是否超出屏幕
                    if (scrollY < DensityUtils.dp2px(MainActivity.this, 500)) {
                        v.layout(l, t, r, b);
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        v.postInvalidate();
                    }

                    break;
                case MotionEvent.ACTION_UP:
                    if (scrollY > (DensityUtils.dp2px(MainActivity.this, 500/2)) && scrollUp) {
                        llListview.smoothScrollBy(0, v.getTop(), 0, initTop - DensityUtils.dp2px(MainActivity.this, 500));
                        v.postInvalidate();
                        ((CustomView)v).setOnTop(true);
                    } else if (scrollY <= DensityUtils.dp2px(MainActivity.this, 500/2) && scrollUp) {
                        ((CustomView)v).setOnTop(false);
                        llListview.smoothScrollBy(0, v.getTop(), 0, initTop - DensityUtils.dp2px(MainActivity.this, 250));
                        v.postInvalidate();
                    } else if (!scrollUp && scrollY > DensityUtils.dp2px(MainActivity.this, 500 / 2)) {
                        ((CustomView)v).setOnTop(false);
                        llListview.smoothScrollBy(0, v.getTop(), 0, initTop - DensityUtils.dp2px(MainActivity.this, 250));
                        v.postInvalidate();
                    } else if (!scrollUp && scrollY <= DensityUtils.dp2px(MainActivity.this, 500 / 2)) {
                        ((CustomView)v).setOnTop(false);
                        llListview.smoothScrollBy(0, v.getTop(), 0, initTop);
                        v.postInvalidate();
                    }

                    break;
            }
            return true;
        }
    }


}
