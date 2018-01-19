package com.anam.floatimage.model;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.anam.floatimage.R;


public class FloatingCircle extends Service {

    private ImageView smallCircle;
    private ImageView close;
    private LinearLayout layout;
    private WindowManager windowManager;
    private WindowManager windowManagerClose;
    private int screen_width;
    private int screen_height;
    private WindowManager.LayoutParams _closeParams;
    private WindowManager.LayoutParams myParams;
    private Animation shake;
    private Context context;
    private MoveAnimator animator;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeView();
        getScreenSize();
        showFloat();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void showFloat() {
        _closeParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        _closeParams.gravity = Gravity.BOTTOM | Gravity.CENTER;
        _closeParams.x = 0;
        _closeParams.y = 100;


        myParams = new WindowManager.LayoutParams(
                (int) (0.18 * screen_width),
                (int) (0.18 * screen_width),
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
                        | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );

        myParams.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN;
        myParams.x = screen_width / 2; // horizontal center for the image
        myParams.y = 0;
        //   myParams.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

        if (Build.VERSION.SDK_INT >= 23) {
            if (Settings.canDrawOverlays(context)) {

                if (!smallCircle.isShown()) {
                    windowManager.addView(smallCircle, myParams);
                    windowManagerClose.addView(layout, _closeParams);
                    layout.setVisibility(View.INVISIBLE);
                    close.startAnimation(shake);


                }
            }


        } else {

            if (!smallCircle.isShown()) {
                windowManager.addView(smallCircle, myParams);
                windowManagerClose.addView(layout, _closeParams);
                layout.setVisibility(View.INVISIBLE);
                close.startAnimation(shake);

            }
        }


        try {
            // for moving the picture on touch and slide
            smallCircle.setOnTouchListener(new View.OnTouchListener() {
                public WindowManager.LayoutParams myParams2;
                private int initialX;
                private int initialY;
                private float initialTouchX;
                private float initialTouchY;

                @Override
                public boolean onTouch(View v, final MotionEvent event) {

                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            initialX = myParams.x;
                            initialY = myParams.y;
                            initialTouchX = event.getRawX();
                            initialTouchY = event.getRawY();
                            animator.stop();
                            break;
                        case MotionEvent.ACTION_UP:
                            if (MathUtil.betweenExclusive(myParams.x, -100, 100) && !MathUtil.betweenExclusive(myParams.y, screen_height / 3, screen_height / 2)) { //moving to center range of screen


                                animator.start(screen_width / 2, myParams.y);
                                android.view.ViewGroup.LayoutParams layoutParams = smallCircle.getLayoutParams();
                                layoutParams.width = (int) (0.18 * screen_width);
                                layoutParams.height = (int) (0.18 * screen_width);
                                smallCircle.setLayoutParams(layoutParams);
                                windowManager.updateViewLayout(v, myParams);
                                layout.setVisibility(View.INVISIBLE);


                            } else if (MathUtil.betweenExclusive((int) event.getRawX(), 0, screen_width / 5)) {
                                //move to left of screen

                                if (MathUtil.betweenExclusive((int) event.getRawY(), 0, screen_height / 10)) {

                                    // myParams.y = 0 ;
                                    animator.start(-screen_width / 2, -((screen_height / 2) - 150));
                                    android.view.ViewGroup.LayoutParams layoutParams = smallCircle.getLayoutParams();
                                    layoutParams.width = (int) (0.18 * screen_width);
                                    layoutParams.height = (int) (0.18 * screen_width);
                                    smallCircle.setLayoutParams(layoutParams);
                                    windowManager.updateViewLayout(v, myParams);
                                    layout.setVisibility(View.INVISIBLE);
                                } else if (MathUtil.betweenExclusive((int) event.getRawY(), 9 * (screen_height / 10), screen_height)) {
                                    animator.start(-screen_width / 2, screen_height / 2 - 150);
                                    android.view.ViewGroup.LayoutParams layoutParams = smallCircle.getLayoutParams();
                                    layoutParams.width = (int) (0.18 * screen_width);
                                    layoutParams.height = (int) (0.18 * screen_width);
                                    smallCircle.setLayoutParams(layoutParams);
                                    windowManager.updateViewLayout(v, myParams);
                                    layout.setVisibility(View.INVISIBLE);
                                } else {
                                    animator.start(-screen_width / 2, myParams.y);
                                    android.view.ViewGroup.LayoutParams layoutParams = smallCircle.getLayoutParams();
                                    layoutParams.width = (int) (0.18 * screen_width);
                                    layoutParams.height = (int) (0.18 * screen_width);
                                    smallCircle.setLayoutParams(layoutParams);
                                    windowManager.updateViewLayout(v, myParams);
                                    layout.setVisibility(View.INVISIBLE);
                                }

                            } else if (MathUtil.betweenExclusive((int) event.getRawX(), screen_width - (screen_width / 5), screen_width)) {
                                //move to right of screen

                                if (MathUtil.betweenExclusive((int) event.getRawY(), 0, screen_height / 10)) {

                                    // myParams.y = 0 ;
                                    animator.start(screen_width / 2, -((screen_height / 2) - 150));
                                    android.view.ViewGroup.LayoutParams layoutParams = smallCircle.getLayoutParams();
                                    layoutParams.width = (int) (0.18 * screen_width);
                                    layoutParams.height = (int) (0.18 * screen_width);
                                    smallCircle.setLayoutParams(layoutParams);
                                    windowManager.updateViewLayout(v, myParams);
                                    layout.setVisibility(View.INVISIBLE);
                                } else if (MathUtil.betweenExclusive((int) event.getRawY(), 9 * (screen_height / 10), screen_height)) {
                                    animator.start(screen_width / 2, screen_height / 2 - 150);
                                    android.view.ViewGroup.LayoutParams layoutParams = smallCircle.getLayoutParams();
                                    layoutParams.width = (int) (0.18 * screen_width);
                                    layoutParams.height = (int) (0.18 * screen_width);
                                    smallCircle.setLayoutParams(layoutParams);
                                    windowManager.updateViewLayout(v, myParams);
                                    layout.setVisibility(View.INVISIBLE);
                                } else {
                                    animator.start(screen_width / 2, myParams.y);
                                    android.view.ViewGroup.LayoutParams layoutParams = smallCircle.getLayoutParams();
                                    layoutParams.width = (int) (0.18 * screen_width);
                                    layoutParams.height = (int) (0.18 * screen_width);
                                    smallCircle.setLayoutParams(layoutParams);
                                    windowManager.updateViewLayout(v, myParams);
                                    layout.setVisibility(View.INVISIBLE);
                                }

                            } else if (MathUtil.betweenExclusive((int) event.getRawX(), screen_width / 5, 2 * (screen_width / 5))) {
                                //move to left of screen
                                if (MathUtil.betweenExclusive((int) event.getRawY(), 0, screen_height / 10)) {

                                    // myParams.y = 0 ;
                                    animator.start(-screen_width / 2, -((screen_height / 2) - 150));
                                    android.view.ViewGroup.LayoutParams layoutParams = smallCircle.getLayoutParams();
                                    layoutParams.width = (int) (0.18 * screen_width);
                                    layoutParams.height = (int) (0.18 * screen_width);
                                    smallCircle.setLayoutParams(layoutParams);
                                    windowManager.updateViewLayout(v, myParams);
                                    layout.setVisibility(View.INVISIBLE);
                                } else if (MathUtil.betweenExclusive((int) event.getRawY(), 9 * (screen_height / 10), screen_height)) {
                                    animator.start(-screen_width / 2, screen_height / 2 - 150);
                                    android.view.ViewGroup.LayoutParams layoutParams = smallCircle.getLayoutParams();
                                    layoutParams.width = (int) (0.18 * screen_width);
                                    layoutParams.height = (int) (0.18 * screen_width);
                                    smallCircle.setLayoutParams(layoutParams);
                                    windowManager.updateViewLayout(v, myParams);
                                    layout.setVisibility(View.INVISIBLE);
                                } else {
                                    animator.start(-screen_width / 2, myParams.y);
                                    android.view.ViewGroup.LayoutParams layoutParams = smallCircle.getLayoutParams();
                                    layoutParams.width = (int) (0.18 * screen_width);
                                    layoutParams.height = (int) (0.18 * screen_width);
                                    smallCircle.setLayoutParams(layoutParams);
                                    windowManager.updateViewLayout(v, myParams);
                                    layout.setVisibility(View.INVISIBLE);
                                }
                            } else if (MathUtil.betweenExclusive((int) event.getRawX(), 3 * (screen_width / 5), screen_width)) {
                                //move to right of screen
                                if (MathUtil.betweenExclusive((int) event.getRawY(), 0, screen_height / 10)) {

                                    // myParams.y = 0 ;
                                    animator.start(screen_width / 2, -((screen_height / 2) - 150));
                                    android.view.ViewGroup.LayoutParams layoutParams = smallCircle.getLayoutParams();
                                    layoutParams.width = (int) (0.18 * screen_width);
                                    layoutParams.height = (int) (0.18 * screen_width);
                                    smallCircle.setLayoutParams(layoutParams);
                                    windowManager.updateViewLayout(v, myParams);
                                    layout.setVisibility(View.INVISIBLE);
                                } else if (MathUtil.betweenExclusive((int) event.getRawY(), 9 * (screen_height / 10), screen_height)) {
                                    animator.start(screen_width / 2, screen_height / 2 - 150);
                                    android.view.ViewGroup.LayoutParams layoutParams = smallCircle.getLayoutParams();
                                    layoutParams.width = (int) (0.18 * screen_width);
                                    layoutParams.height = (int) (0.18 * screen_width);
                                    smallCircle.setLayoutParams(layoutParams);
                                    windowManager.updateViewLayout(v, myParams);
                                    layout.setVisibility(View.INVISIBLE);
                                } else {
                                    animator.start(screen_width / 2, myParams.y);
                                    android.view.ViewGroup.LayoutParams layoutParams = smallCircle.getLayoutParams();
                                    layoutParams.width = (int) (0.18 * screen_width);
                                    layoutParams.height = (int) (0.18 * screen_width);
                                    smallCircle.setLayoutParams(layoutParams);
                                    windowManager.updateViewLayout(v, myParams);
                                    layout.setVisibility(View.INVISIBLE);
                                }
                            } else if (MathUtil.betweenExclusive(myParams.x, -50, 50) && MathUtil.betweenExclusive(myParams.y, screen_height / 3, screen_height / 2)) {

                                Visibility();
                                stopSelf();


                            } else {

                                //not in either of the above cases
                                animator.start(screen_width / 2, myParams.y);
                                android.view.ViewGroup.LayoutParams layoutParams = smallCircle.getLayoutParams();
                                layoutParams.width = (int) (0.18 * screen_width);
                                layoutParams.height = (int) (0.18 * screen_width);
                                smallCircle.setLayoutParams(layoutParams);
                                windowManager.updateViewLayout(v, myParams);
                                layout.setVisibility(View.INVISIBLE);

                            }
                            break;
                        case MotionEvent.ACTION_MOVE:
                            layout.setVisibility(View.VISIBLE);
                            myParams.x = initialX + (int) (event.getRawX() - initialTouchX);
                            myParams.y = initialY + (int) (event.getRawY() - initialTouchY);
                            windowManager.updateViewLayout(v, myParams);
                            if (MathUtil.betweenExclusive((int) event.getRawX(), 0, screen_width / 5) || MathUtil.betweenExclusive((int) event.getRawX(), screen_width - (screen_width / 5), screen_width)) {
                                android.view.ViewGroup.LayoutParams layoutParams = smallCircle.getLayoutParams();
                                layoutParams.width = (int) (0.18 * screen_width);
                                layoutParams.height = (int) (0.18 * screen_width);
                                smallCircle.setLayoutParams(layoutParams);
                                windowManager.updateViewLayout(v, myParams);
                            } else if (MathUtil.betweenExclusive((int) event.getRawX(), 2 * (screen_width / 5), 3 * (screen_width / 5))) {
                                android.view.ViewGroup.LayoutParams layoutParams = smallCircle.getLayoutParams();
                                layoutParams.width = (int) (0.18 * screen_width) + 100 + 100;
                                layoutParams.height = (int) (0.18 * screen_width) + 100 + 100;
                                smallCircle.setLayoutParams(layoutParams);
                                windowManager.updateViewLayout(v, myParams);
                            } else if (MathUtil.betweenExclusive((int) event.getRawX(), screen_width / 5, 2 * (screen_width / 5)) || MathUtil.betweenExclusive((int) event.getRawX(), 3 * (screen_width / 5), screen_width)) {
                                android.view.ViewGroup.LayoutParams layoutParams = smallCircle.getLayoutParams();
                                layoutParams.width = (int) (0.18 * screen_width) + 100;
                                layoutParams.height = (int) (0.18 * screen_width) + 100;
                                smallCircle.setLayoutParams(layoutParams);
                                windowManager.updateViewLayout(v, myParams);
                            }


                            break;
                    }
                    return true;

                }

            });

        } catch (Exception e) {

        }

    }

    private void Visibility() {
        if (windowManager != null) {
            windowManager.removeViewImmediate(smallCircle);
            windowManagerClose.removeViewImmediate(layout);
        }
    }

    private void initializeView() {
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowManagerClose = (WindowManager) getSystemService(WINDOW_SERVICE);
        smallCircle = new ImageView(this);
        smallCircle.setImageResource(R.mipmap.dummy);
        close = new ImageView(this);
        close.setImageResource(R.mipmap._close);
        context = FloatingCircle.this;
        shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.wiggle);
        shake.setRepeatCount(Animation.INFINITE);
        layout = new LinearLayout(this);
        layout.addView(close);
        animator = new MoveAnimator();
    }

    private void getScreenSize() {
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screen_width = size.x;
        screen_height = size.y;

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private static class MathUtil {
        public static boolean betweenExclusive(int x, int min, int max) {
            return x > min && x < max;
        }
    }

    private class MoveAnimator implements Runnable {

        private Handler handler = new Handler(Looper.getMainLooper());
        private float destinationX;
        private float destinationY;
        private long startingTime;

        private void start(float x, float y) {
            this.destinationX = x;
            this.destinationY = y;
            startingTime = System.currentTimeMillis();
            handler.post(this);
        }

        @Override
        public void run() {
            if (smallCircle != null && smallCircle.getParent() != null) {
                float progress = Math.min(1, (System.currentTimeMillis() - startingTime) / 400f);

                float deltaX = (destinationX - myParams.x) * progress;
                float deltaY = (destinationY - myParams.y) * progress;
                move(deltaX, deltaY);
                if (progress < 1) {
                    handler.post(this);
                }
            }
        }

        private void stop() {
            handler.removeCallbacks(this);
        }
    }

    protected void move(float deltaX, float deltaY) {
        myParams.x += deltaX;
        myParams.y += deltaY;
        windowManager.updateViewLayout(smallCircle, myParams);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Visibility();
        _closeParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        _closeParams.gravity = Gravity.BOTTOM | Gravity.CENTER;
        _closeParams.x = 0;
        _closeParams.y = 100;


        myParams = new WindowManager.LayoutParams(
                (int) (0.18 * screen_width),
                (int) (0.18 * screen_width),
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
                        | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );

        myParams.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN;
        myParams.x = screen_width / 2; // horizontal center for the image
        myParams.y = 0;
        windowManager.addView(smallCircle, myParams);


        windowManagerClose.addView(layout, _closeParams);
        layout.setVisibility(View.INVISIBLE);
        close.startAnimation(shake);

        return START_NOT_STICKY;
    }
}
