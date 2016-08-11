package com.bd.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

/**
 * Description : <Content><br>
 * CreateTime : 2016/8/1 17:06
 *
 * @author KevinLiu
 * @version <v1.0>
 * @Editor : KevinLiu
 * @ModifyTime : 2016/8/1 17:06
 * @ModifyDescription : <Content>
 */
public class FocusView extends View {

    private float DEFAULT_BORDER_WIDTH;
    private float DEFAULT_CORNER_BORDER;
    private float DEFAULT_CENTER_APEX;
    private int DEFAULT_SHAPE;
    private int DEFAULT_NORMAL_BORDER_COLOR = Color.WHITE;

    private int mNormalBorderColor;
    private int mFocusBorderColor;
    private float mCornerBorder;
    private float mBorderWidth;
    private float mCenterApex;
    private int mShape;
    private static final int RECTANGLE = 0;
    private static final int CIRCLE = 1;
    private static final int HEXAGON = 2;
    private static final int OCTAGON = 3;
    private Paint mBorderPaint;


    public FocusView(Context context) {
        this(context, null);
    }

    public FocusView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.focusViewStyle);
    }

    public FocusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        DEFAULT_BORDER_WIDTH = Utils.dp2px(getContext(), 2.0f);
        DEFAULT_CORNER_BORDER = Utils.dp2px(getContext(), 20.0f);
        DEFAULT_CENTER_APEX = Utils.dp2px(getContext(), 80.0f);
        /*default rectangle*/
        DEFAULT_SHAPE = 0;

        // load styled attributes.
        final TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.FocusView,
                defStyleAttr, 0);

        mCornerBorder = attributes.getDimension(R.styleable.FocusView_corner_border, DEFAULT_CORNER_BORDER);
        mBorderWidth = attributes.getDimension(R.styleable.FocusView_border_width, DEFAULT_BORDER_WIDTH);
        /*中心点到顶点距离*/
        mCenterApex = attributes.getDimension(R.styleable.FocusView_center_apex, DEFAULT_CENTER_APEX);
        mShape = attributes.getInt(R.styleable.FocusView_shape, DEFAULT_SHAPE);
        mNormalBorderColor = attributes.getColor(R.styleable.FocusView_normal_border_color, DEFAULT_NORMAL_BORDER_COLOR);
        mFocusBorderColor = attributes.getColor(R.styleable.FocusView_normal_border_color, getContext().getResources().getColor(R.color.default_focus_border_color));
        mCenterX = attributes.getDimension(R.styleable.FocusView_centerX, 300.0f);
        mCenterY = attributes.getDimension(R.styleable.FocusView_centerY, 300.0f);

        mBeforeAnimCenterApex = mCenterApex;
        attributes.recycle();

        initPaints();
    }


    private void initPaints() {
        mBorderPaint = new Paint();
        mBorderPaint.setColor(mNormalBorderColor);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setStrokeWidth(mBorderWidth);
        mBorderPaint.setAntiAlias(false);
    }

    private float mCenterX;
    private float mCenterY;

    /**
     * 设置中心点
     *
     * @param centerX
     * @param centerY
     */
    public void setCenter(float centerX, float centerY) {
        release();
        this.mCenterX = centerX;
        this.mCenterY = centerY;
        invalidate();
    }

    public void setCenterX(float centerX) {
        this.mCenterX = centerX;
    }

    public void setCenterY(float centerY) {
        this.mCenterY = centerY;
    }

    public float getCenterX() {
        return mCenterX;
    }

    public float getCenterY() {
        return mCenterY;
    }

    private float mBeforeAnimCenterApex;

    private int mState;

    private final int STATE_FOCUS = 0x01;

    private final int STATE_RELEASE = 0x02;

    private final int DESTROY_TOKEN = 0x07;

    private Handler finishHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == DESTROY_TOKEN) {
                ((ViewGroup) getParent()).removeView(FocusView.this);
            }
        }
    };

    public synchronized void setState(int state) {
        mState = state;
        notifyStateChanged();
    }

    public void notifyStateChanged() {
        finishHandler.removeMessages(DESTROY_TOKEN);
    }

    private OnFocusListener mFocusListener;

    public void setFocusListener(OnFocusListener listener) {
        this.mFocusListener = listener;
    }

    public synchronized void focus(float x, float y, long focusDuration) {
        setState(STATE_FOCUS);
        sendDestroyMessage();
        final ScaleAnimation animation = new ScaleAnimation(1f, 0.8f, 1f, 0.8f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(200l);
        animation.setFillAfter(true);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        this.startAnimation(animation);
        setFocus(focusDuration + 200l);
        if (null != this.mFocusListener) {
            this.mFocusListener.onFocus(x, y);
        }
    }

    public void sendDestroyMessage() {
        finishHandler.sendEmptyMessageDelayed(DESTROY_TOKEN, 3000l);
    }

    public void setFocus(long focusDuration) {
        new Handler(getContext().getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                mBorderPaint.setColor(mFocusBorderColor);
                FocusView.this.postInvalidate();
                if (android.os.Build.VERSION.SDK_INT < 20) {
                    FocusView.this.setScaleX(0.8f);
                    FocusView.this.setScaleY(0.8f);
                }
            }
        }, focusDuration);

    }


    public synchronized void release() {
        setState(STATE_RELEASE);
        if (android.os.Build.VERSION.SDK_INT < 20) {
            FocusView.this.setScaleX(1.0f);
            FocusView.this.setScaleY(1.0f);
        }
        this.clearAnimation();
        mBorderPaint.setColor(mNormalBorderColor);
    }

    /**
     * 设置中心点到顶点距离
     *
     * @param centerApex
     */
    public void setCenterApex(float centerApex) {
        this.mCenterApex = centerApex;
    }

    public float getCenterApex() {
        return mCenterApex;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        drawRectCornerBorder(canvas);
    }


    private void drawRectCornerBorder(Canvas canvas) {
        float distanceX = (int) (mCenterApex * Math.sqrt(2.0d) / 2.0d);
        float distanceY = distanceX;
        Point leftTopPoint = new Point((int) (mCenterX - distanceX), (int) (mCenterY - distanceY));
        Point rightTopPoint = new Point((int) (mCenterX + distanceX), (int) (mCenterY - distanceY));
        Point rightBottomPoint = new Point((int) (mCenterX + distanceX), (int) (mCenterY + distanceY));
        Point leftBottomPoint = new Point((int) (mCenterX - distanceX), (int) (mCenterY + distanceY));
        canvas.clipRect(leftTopPoint.x - mBorderWidth, leftTopPoint.y - mBorderWidth, rightBottomPoint.x + mBorderWidth, rightBottomPoint.y + mBorderWidth);

        Path path = new Path();

        path.moveTo(leftTopPoint.x, leftTopPoint.y + mCornerBorder);
        path.rLineTo(0f, -mCornerBorder);
        path.rLineTo(mCornerBorder, 0f);

        path.moveTo(rightTopPoint.x - mCornerBorder, rightTopPoint.y);
        path.rLineTo(mCornerBorder, 0f);
        path.rLineTo(0f, mCornerBorder);

        path.moveTo(rightBottomPoint.x, rightBottomPoint.y - mCornerBorder);
        path.rLineTo(0f, mCornerBorder);
        path.rLineTo(-mCornerBorder, 0f);

        path.moveTo(leftBottomPoint.x + mCornerBorder, leftBottomPoint.y);
        path.rLineTo(-mCornerBorder, 0f);
        path.rLineTo(0f, -mCornerBorder);

        canvas.drawPath(path, mBorderPaint);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int startX;
        int startY;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                startX = (int) event.getX();
                startY = (int) event.getY();
                setCenter(startX, startY);
                return true;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                focus(event.getX(), event.getY(), 500l);
                return true;
        }

        return super.onTouchEvent(event);
    }
}
