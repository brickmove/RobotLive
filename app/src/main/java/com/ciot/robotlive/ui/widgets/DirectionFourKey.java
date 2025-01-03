package com.ciot.robotlive.ui.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.ciot.robotlive.R;

// 方向控件
public class DirectionFourKey extends ConstraintLayout {
    private static final String TAG = DirectionFourKey.class.getSimpleName();
    private AppCompatImageView mUpIv;
    private AppCompatImageView mDownIv;
    private AppCompatImageView mLeftIv;
    private AppCompatImageView mRightIv;

    public DirectionFourKey(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_direction_four_key, this, true);
        initView();
        initEvent();
    }

    private void initView() {
        mUpIv = findViewById(R.id.iv_direction_up);
        mDownIv = findViewById(R.id.iv_direction_down);
        mLeftIv = findViewById(R.id.iv_direction_left);
        mRightIv = findViewById(R.id.iv_direction_right);
    }

    private void initEvent() {
        mUpIv.setOnTouchListener(new MyOnTouchListener());
        mDownIv.setOnTouchListener(new MyOnTouchListener());
        mLeftIv.setOnTouchListener(new MyOnTouchListener());
        mRightIv.setOnTouchListener(new MyOnTouchListener());
    }

    private OnDirectionListener mOnDirectionListener;

    /**
     * 设置方向监听
     *
     * @param onDirectionListener
     */
    public void setOnDirectionListener(OnDirectionListener onDirectionListener) {
        mOnDirectionListener = onDirectionListener;
    }

    /**
     * 方向监听
     */
    public interface OnDirectionListener {
        /**
         * 前进抬起
         */
        public void upUp();

        /**
         * 后退抬起
         */
        public void downUp();

        /**
         * 左转抬起
         */
        public void leftUp();

        /**
         * 右转抬起
         */
        public void rightUp();

        /**
         * 前进按下
         */
        public void upDown();

        /**
         * 后退按下
         */
        public void downDown();

        /**
         * 左转按下
         */
        public void leftDown();

        /**
         * 右转按下
         */
        public void rightDown();
    }


    class MyOnTouchListener implements OnTouchListener, OnClickListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int id = v.getId();
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (id == R.id.iv_direction_up) {
                    upUp();
                } else if (id == R.id.iv_direction_down) {
                    downUp();
                } else if (id == R.id.iv_direction_left) {
                    leftUp();
                } else if (id == R.id.iv_direction_right) {
                    rightUp();
                }
            } else if (event.getAction() == MotionEvent.ACTION_MOVE || event.getAction() == MotionEvent.ACTION_DOWN) {
                if (id == R.id.iv_direction_up) {
                    upDown();
                } else if (id == R.id.iv_direction_down) {
                    downDown();
                } else if (id == R.id.iv_direction_left) {
                    leftDown();
                } else if (id == R.id.iv_direction_right) {
                    rightDown();
                }
            }
            return true;
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {

        }
    }

    private void upUp() {
        if (mOnDirectionListener != null) {
            mOnDirectionListener.upUp();
        }
    }

    private void downUp() {
        if (mOnDirectionListener != null) {
            mOnDirectionListener.downUp();
        }
    }

    private void leftUp() {
        if (mOnDirectionListener != null) {
            mOnDirectionListener.leftUp();
        }
    }

    private void rightUp() {
        if (mOnDirectionListener != null) {
            mOnDirectionListener.rightUp();
        }
    }

    private void upDown() {
        if (mOnDirectionListener != null) {
            mOnDirectionListener.upDown();
        }
    }

    private void downDown() {
        if (mOnDirectionListener != null) {
            mOnDirectionListener.downDown();
        }
    }

    private void leftDown() {
        if (mOnDirectionListener != null) {
            mOnDirectionListener.leftDown();
        }
    }

    private void rightDown() {
        if (mOnDirectionListener != null) {
            mOnDirectionListener.rightDown();
        }
    }

}
