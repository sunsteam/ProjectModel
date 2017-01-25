package cn.yomii.www.projectmodel.utils;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import cn.yomii.www.projectmodel.App;
import cn.yomii.www.projectmodel.R;


/**
 *
 * Created by Yomii on 2016/5/18.
 */
public class GuideUtils {

    private ImageView imgView;
    private WindowManager windowManager;

    private GuideUtils() {
    }

    public static GuideUtils getInstance() {
        return new GuideUtils();
    }

    private Handler handler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    WindowManager.LayoutParams params = new WindowManager.LayoutParams();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        params.type = WindowManager.LayoutParams.TYPE_PHONE;
                    }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        params.type = WindowManager.LayoutParams.TYPE_TOAST;
                    }else {
                        params.type = WindowManager.LayoutParams.TYPE_PHONE;
                    }

                    params.format = PixelFormat.RGBA_8888;
                    params.gravity = Gravity.START | Gravity.TOP;
                    params.width = App.getMetrics().widthPixels;
                    params.height = App.getMetrics().heightPixels;
                    params.windowAnimations = R.style.fade_anim;
                    //params.systemUiVisibility = View.SYSTEM_UI_FLAG_LOW_PROFILE;

                    // 添加到当前的窗口上
                    windowManager.addView(imgView, params);
                    break;
            }
        }
    };

    /**
     * @param drawableResourcesId：引导图片的资源Id
     */
    public void initGuide(Activity context, int drawableResourcesId, boolean show) {
        if (!show) {
            return;
        }
        windowManager = context.getWindowManager();

        //动态初始化图层
        imgView = new ImageView(context);
        imgView.setLayoutParams(new WindowManager.LayoutParams(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT));
        imgView.setScaleType(ImageView.ScaleType.FIT_XY);
        imgView.setClickable(true);

        //imgView.setImageResource(drawableResourcesId);
        Glide.with(context).load(drawableResourcesId).into(imgView);

        //预留动画时间
        handler.sendEmptyMessageDelayed(1,300);

        imgView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // 点击图层之后，将图层移除
                windowManager.removeView(imgView);
            }
        });
    }
}
