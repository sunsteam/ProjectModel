package cn.yomii.www.frame.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * 自滚动ViewPager
 * Created by Yomii on 2016/3/9.
 */
public class BannerViewPager extends RelativeLayout implements ViewPager.OnPageChangeListener,
        View.OnTouchListener {

    private static String TAG = "CustomViewPager";

    public static int DOTS_ID = 0x01;

    private List<ViewPagerBean> data;
    private LayoutParams viewPagerLp;
    private LayoutParams textLp;

    private ViewPager viewPager;
    private LinearLayout dotsLayout;
    private TextView title;

    private boolean isEmpty;
    private boolean pagerIdle;
    private Timer timer;

    private static float widthDpi;
    private static float density;

    public boolean isEmpty() {
        return isEmpty;
    }


    public BannerViewPager(Context context) {
        super(context);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

        density = displayMetrics.density;
        widthDpi = displayMetrics.widthPixels / displayMetrics.density;


        Log.i(TAG,"CustomViewPager初始化");
        data = new ArrayList<>();
        data.add(new ViewPagerBean("", "", false, null));
        isEmpty = true;
        //--------------------初始化布局参数----------------
        //this.setPadding(dp2px(5), dp2px(5), dp2px(5), dp2px(5));
        viewPagerLp = new LayoutParams(-1,-1);
        //--------------------初始化ViewPager----------------
        viewPager = new ViewPager(context);
        viewPager.setOnPageChangeListener(this);
        ViewPagerAdapter adapter = new ViewPagerAdapter();
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(5000);
        viewPager.setOnTouchListener(this);
        this.addView(viewPager, viewPagerLp);
        //--------------------初始化ShadowView----------------
        /*ImageView imageView = new ImageView(getContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(R.drawable.discover_banner_shadow);
        LayoutParams shadowLp = new LayoutParams(-1,-2);
        shadowLp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        this.addView(imageView, shadowLp);*/
        //--------------------初始化DotsLayout----------------
        dotsLayout = new LinearLayout(context);
        dotsLayout.setPadding(0, 0, 0, dp2px(4));
        //noinspection ResourceType
        dotsLayout.setId(DOTS_ID);
        LayoutParams dotsLp = new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        dotsLp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        dotsLp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        dotsLp.setMargins(0, 0, dp2px(15), dp2px(15));
        this.addView(dotsLayout, dotsLp);
        //--------------------初始化TextTitle----------------
        title = new TextView(context);
        title.setSingleLine();
        //title.setBackgroundColor(Color.argb(88, 0, 0, 0));
        title.setTextColor(Color.WHITE);
        title.setText("测试效果");
        title.setGravity(Gravity.CENTER_VERTICAL);
        textLp = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        textLp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        textLp.addRule(RelativeLayout.LEFT_OF, DOTS_ID);
        textLp.height = dp2px(30);
        textLp.setMargins(dp2px(5), 0, dp2px(5), dp2px(5));
        //隐藏title
        this.addView(title, textLp);

        //--------------------准备阶段----------------
//        dotsLayout.bringToFront();
//        title.bringToFront();
    }

    /**
     * 设置数据列表,并根据数据动态加点
     *
     * @param data data
     */
    public void setData(List<ViewPagerBean> data) {
        if (data == null || data.size() == 0)
            return;

        isEmpty = false;
        this.data = data;

        //根据List的数据数量动态加点
        dotsLayout.removeAllViews();
        int size = this.data.size();
        for (int i = 0; i < size; i++) {
            LinearLayout.LayoutParams dotLp = new LinearLayout.LayoutParams(dp2px(5),
                    dp2px(5));
            View dot = new View(getContext());
            //状态选择器
            StateListDrawable seletor = new StateListDrawable();
            GradientDrawable selectedDrawable = new GradientDrawable();
            selectedDrawable.setShape(GradientDrawable.RECTANGLE);
            //selectedDrawable.setCornerRadius(16f);
            selectedDrawable.setColor(Color.WHITE);
            seletor.addState(SELECTED_STATE_SET, selectedDrawable);
            GradientDrawable normalDrawable = new GradientDrawable();
            normalDrawable.setShape(GradientDrawable.RECTANGLE);
            //normalDrawable.setCornerRadius(16f);
            normalDrawable.setColor(Color.parseColor("#777777"));
            seletor.addState(EMPTY_STATE_SET, normalDrawable);
            dot.setBackgroundDrawable(seletor);
            if (i == 0) {
                dot.setSelected(true);
            } else {
                dotLp.setMargins(dp2px(6), 0, 0, 0);
            }
            dotsLayout.addView(dot, dotLp);
        }
        if (viewPager != null) {
            viewPager.getAdapter().notifyDataSetChanged();
            viewPager.setCurrentItem(5000);
            startPageScroll(5000);
        }

    }

    public int getViewPagerHeight() {
        return viewPagerLp.height;
    }

    public void setViewPagerHeight(int viewPagerHeight) {
        this.viewPagerLp.height = viewPagerHeight;
        viewPager.setLayoutParams(viewPagerLp);
    }

    public int getTitleViewHeight() {
        return textLp.height;
    }

    public void setTitleViewHeight(int viewPagerHeight) {
        this.textLp.height = viewPagerHeight;
        title.setLayoutParams(textLp);
    }

    public View getRootView() {
        return this;
    }

    public ViewPager getViewPager() {
        return viewPager;
    }

    /**
     * @return 当前页面的数据
     */
    public ViewPagerBean getCurrentPageBean() {
        return data.get(getRealPostion(viewPager.getCurrentItem()));
    }

    /**
     * 设置当前页面的点击事件
     *
     * @param listener View.OnClickListener
     */
    public void setOnPagerClickListener(OnClickListener listener) {
        viewPager.setOnClickListener(listener);
    }


    private class ViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return data.size() == 1 ? 1 : Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(getContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            if (data != null) {
                int realPosition = getRealPostion(position);
                container.addView(imageView, ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT);
                if (data.get(realPosition).getPicUrl() != null) {
//                    Glide.with(getContext()).load(data.get(realPosition).getPicUrl()).centerCrop()
//                            .into(imageView);
                    // TODO: 2017/6/14
                } else if (data.get(realPosition).getPicDrawable() != null) {
                    imageView.setImageDrawable(data.get(realPosition).getPicDrawable());
                } else if (data.get(realPosition).getPicBitmap() != null) {
                    imageView.setImageBitmap(data.get(realPosition).getPicBitmap());
                } else if (data.get(realPosition).getPicResID() > 0) {
                    imageView.setImageResource(data.get(realPosition).getPicResID());
                }
            }
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    /**
     * 根据当前position计算实际数据position
     */
    private int getRealPostion(int position) {
        return position % data.size();
    }


    @Override
    public void onPageScrolled(int position, float positionOffset,
                               int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        int realPosition = getRealPostion(position);
        int childCount = dotsLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (i == realPosition) {
                dotsLayout.getChildAt(i).setSelected(true);
            } else {
                dotsLayout.getChildAt(i).setSelected(false);
            }
        }
        if (data.get(realPosition).isHasTitle()) {
            String title = data.get(realPosition).getTitle();
            if (title == null) {
                title = "";
            }
            this.title.setText(title);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        pagerIdle = state == ViewPager.SCROLL_STATE_IDLE;
    }

    //----------------实现自动轮播--------------------------


    private int delayTime = 5000;

    /**
     * @return 获取轮播间隔时间
     */
    public int getDelayTime() {
        return delayTime;
    }

    /**
     * @param delayTime 设置轮播间隔时间
     */
    public void setDelayTime(int delayTime) {
        this.delayTime = delayTime;
    }

    public void stopPageScroll() {
        Log.i(TAG,"CustomViewPager stop");
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
    }

    public void startPageScroll(int delayTime) {
        Log.i(TAG,"CustomViewPager move");
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            }
        }, delayTime, 5000);
    }

    //----------------自动轮播实现完毕--------------------------


    private long lastClickTime;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        requestDisallowInterceptTouchEvent(true);
        //长按停止轮播,短按点击
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastClickTime = System.currentTimeMillis();
                stopPageScroll();
                break;
            case MotionEvent.ACTION_UP:
                startPageScroll(3000);
                if (pagerIdle && System.currentTimeMillis() - lastClickTime < 300L) {
                    v.performClick();
                }
                break;
        }
        return false;
    }

    /**
     * dp转px
     *
     * @param dpValue dp值
     * @return px值
     */
    private static int dp2px(float dpValue) {
        return (int) (dpValue * density * widthDpi / 360f + 0.5f);
    }


    public static class ViewPagerBean {
        private boolean hasTitle;
        private String picUrl;
        private String pageUrl;
        private Drawable picDrawable;
        private Bitmap picBitmap;
        private int picResID;
        private String title;

        private int pageSrc;

        public ViewPagerBean(Drawable picDrawable, String pageUrl,
                             boolean hasTitle, String title) {
            this(pageUrl, hasTitle, title);
            this.picDrawable = picDrawable;
        }

        public ViewPagerBean(Bitmap picBitmap, String pageUrl, boolean hasTitle,
                             String title) {
            this(pageUrl, hasTitle, title);
            this.picBitmap = picBitmap;
        }

        public ViewPagerBean(int picResID, String pageUrl, boolean hasTitle,
                             String title) {
            this(pageUrl, hasTitle, title);
            this.picResID = picResID;
        }

        public ViewPagerBean(String picUrl, String pageUrl, boolean hasTitle,
                             String title) {
            this(pageUrl, hasTitle, title);
            this.picUrl = picUrl;
        }

        private ViewPagerBean(String pageUrl, boolean hasTitle, String title) {
            super();
            this.hasTitle = hasTitle;
            this.pageUrl = pageUrl;
            this.title = title;
        }

        public ViewPagerBean(String picUrl, int pageSrc) {
            this.hasTitle = false;
            this.title = null;
            this.picUrl = picUrl;
            this.pageSrc = pageSrc;
        }

        public String getPageUrl() {
            return pageUrl;
        }

        public void setPageUrl(String pageUrl) {
            this.pageUrl = pageUrl;
        }

        public boolean isHasTitle() {
            return hasTitle;
        }

        public void setHasTitle(boolean hasTitle) {
            this.hasTitle = hasTitle;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public Drawable getPicDrawable() {
            return picDrawable;
        }

        public void setPicDrawable(Drawable picDrawable) {
            this.picDrawable = picDrawable;
        }

        public Bitmap getPicBitmap() {
            return picBitmap;
        }

        public void setPicBitmap(Bitmap picBitmap) {
            this.picBitmap = picBitmap;
        }

        public int getPicResID() {
            return picResID;
        }

        public void setPicResID(int picResID) {
            this.picResID = picResID;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getPageSrc() {
            return pageSrc;
        }

        public void setPageSrc(int pageSrc) {
            this.pageSrc = pageSrc;
        }
    }
}
