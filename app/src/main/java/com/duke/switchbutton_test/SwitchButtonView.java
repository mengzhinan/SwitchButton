package com.duke.switchbutton_test;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * @Author: duke
 * @DateTime: 2016-11-30 21:32
 * @Description: 单选切换控件 <br/>
 * <p>
 * 系统默认控件的宽高，如果非要修改大小，建议设置layout_width属性； <br/>
 * layout_height属性值无效，程序会根据layout_width属性值动态计算出合理的layout_height属性值。 <br/>
 */
public class SwitchButtonView extends View {
    private int widthDefault;
    private int mWidth;//宽度
    private int mHeight;//高度
    private int radius;//半径

    private final int WIDTH_DEFAULT = 70;//dp
    private final int INNER_PADDING_DEFAULT = 1;
    private final int BG_WIDTH_DEFAULT = 1;
    private int BG_COLOR1_DEFAULT = Color.parseColor("#D9D9D9");
    private int BG_COLOR2_DEFAULT = Color.WHITE;
    private int COVER_COLOR_DEFAULT = Color.parseColor("#4DC964");
    private int CIRCLE_DEFAULT_COLOR_DEFAULT = Color.WHITE;
    private int CIRCLE_SELECT_COLOR_DEFAULT = Color.parseColor("#EEEEEE");

    private int innerPaddingWidth;//圆角矩形与内圆的间距
    private int bgColor1 = BG_COLOR1_DEFAULT;//底纹颜色
    private int bgColor2 = BG_COLOR2_DEFAULT;//填充颜色
    private int bgWidth;//边框粗细
    private int coverColor = COVER_COLOR_DEFAULT;//选中颜色
    private int circleDefaultColor = CIRCLE_DEFAULT_COLOR_DEFAULT;//圆默认颜色
    private int circleSelectColor = CIRCLE_SELECT_COLOR_DEFAULT;//圆按下颜色
    private boolean isToggleOn;
    //背景描边层rectf
    private RectF mRoundRectFOut = new RectF();
    //背景默认颜色填充层rectf
    private RectF mRoundRectFFill = new RectF();

    //背景描边层画笔
    private Paint mPaintBgOut;
    //背景默认颜色填充层画笔
    private Paint mPaintBgFill;
    //背景选中层画笔
    private Paint mPaintCover;
    //圆描边层画笔
    private Paint mPaintCircleOut;
    //圆默认颜色和按下画笔
    private Paint mPaintCircleFill;

    //记录圆心最左边、最右边和当前位置
    private float centerXLeft, centerXRight, centerX;
    private float lastX;//记录上一次x位置
    //private int mTouchSlop;
    //圆运动动画
    private ValueAnimator animator;
    private final int ANIMATION_TIME = 100;

    private OnToggleChangeListener onToggleChangeListener;

    public void setOnToggleChangeListener(OnToggleChangeListener l) {
        this.onToggleChangeListener = l;
    }

    /**
     * 修改选中状态
     *
     * @param isToggleOn
     */
    public void setIsToggleOn(boolean isToggleOn) {
        if (this.isToggleOn != isToggleOn) {
            preAnimation();
        }
    }

    /**
     * 获取当前是否选中值
     *
     * @return
     */
    public boolean getIsToggleOn() {
        return this.isToggleOn;
    }

    public SwitchButtonView(Context context) {
        this(context, null, 0);
    }

    public SwitchButtonView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwitchButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        widthDefault = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, WIDTH_DEFAULT, getResources().getDisplayMetrics());
        innerPaddingWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, INNER_PADDING_DEFAULT, getResources().getDisplayMetrics());
        bgWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, BG_WIDTH_DEFAULT, getResources().getDisplayMetrics());

        //初始化
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SwitchButtonView, defStyleAttr, 0);
        int size = array.getIndexCount();
        for (int i = 0; i < size; i++) {
            int attr = array.getIndex(i);
            switch (attr) {
                case R.styleable.SwitchButtonView_innerPaddingWidth:
                    //圆角矩形与内圆的间距
                    innerPaddingWidth = array.getDimensionPixelOffset(attr,
                            (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, INNER_PADDING_DEFAULT, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.SwitchButtonView_bgColor1:
                    bgColor1 = array.getColor(attr, BG_COLOR1_DEFAULT);
                    break;
                case R.styleable.SwitchButtonView_bgColor2:
                    bgColor2 = array.getColor(attr, BG_COLOR2_DEFAULT);
                    break;
                case R.styleable.SwitchButtonView_bgWidth:
                    bgWidth = array.getDimensionPixelOffset(attr,
                            (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, BG_WIDTH_DEFAULT, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.SwitchButtonView_coverColor:
                    //选中颜色
                    coverColor = array.getColor(attr, COVER_COLOR_DEFAULT);
                    break;
                case R.styleable.SwitchButtonView_circleDefaultColor:
                    //圆默认颜色
                    circleDefaultColor = array.getColor(attr, CIRCLE_DEFAULT_COLOR_DEFAULT);
                    break;
                case R.styleable.SwitchButtonView_circleSelectColor:
                    //圆按下颜色
                    circleSelectColor = array.getColor(attr, CIRCLE_SELECT_COLOR_DEFAULT);
                    break;
                case R.styleable.SwitchButtonView_isToggleOn:
                    //是否选中
                    isToggleOn = array.getBoolean(attr, false);
                    break;
            }
        }
        array.recycle();
        //背景描边层画笔
        mPaintBgOut = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintBgOut.setDither(true);
        mPaintBgOut.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaintBgOut.setColor(bgColor1);
        //背景默认颜色覆盖层画笔
        mPaintBgFill = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintBgFill.setDither(true);
        mPaintBgFill.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaintBgFill.setColor(bgColor2);
        //圆描边层画笔
        mPaintCircleOut = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintCircleOut.setDither(true);
        mPaintCircleOut.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaintCircleOut.setColor(bgColor1);
        //圆默认颜色覆盖层画笔
        mPaintCircleFill = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintCircleFill.setDither(true);
        mPaintCircleFill.setStyle(Paint.Style.FILL);
        mPaintCircleFill.setColor(circleDefaultColor);
        //背景选中层颜色画笔
        mPaintCover = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintCover.setDither(true);
        mPaintCover.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaintCover.setColor(coverColor);

        //获取系统指定的最小move距离
        //mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            int width = getMeasuredWidth();
            if (width > 0) {
                mWidth = width - width / 8;
                mHeight = width / 2;
                radius = mHeight / 2 - bgWidth - innerPaddingWidth;
            } else {
                mWidth = widthDefault - widthDefault / 8;
                mHeight = widthDefault / 2;
                radius = mHeight / 2 - bgWidth - innerPaddingWidth;
            }
        } else {
            mWidth = widthDefault - widthDefault / 8;
            mHeight = widthDefault / 2;
            radius = mHeight / 2 - bgWidth - innerPaddingWidth;
        }
        setMeasuredDimension(mWidth, mHeight);
        //圆环初始位置
        centerXLeft = mHeight / 2;
        centerXRight = mWidth - centerXLeft;
        if (this.isToggleOn) {
            centerX = centerXRight;
            if (onToggleChangeListener != null) {
                onToggleChangeListener.onChange(this.isToggleOn);
            }
        } else {
            centerX = centerXLeft;
            if (onToggleChangeListener != null) {
                onToggleChangeListener.onChange(this.isToggleOn);
            }
        }
        mRoundRectFOut.set(0, 0, getMeasuredWidth(), getMeasuredHeight());
        mRoundRectFFill.set(bgWidth, bgWidth, getMeasuredWidth() - bgWidth, getMeasuredHeight() - bgWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //画背景圆角矩形边框和填充
        canvas.drawRoundRect(mRoundRectFOut, getMeasuredHeight() / 2, getMeasuredHeight() / 2, mPaintBgOut);
        canvas.drawRoundRect(mRoundRectFFill, getMeasuredHeight() / 2, getMeasuredHeight() / 2, mPaintBgFill);

        //画覆盖层圆角矩形
        mPaintCover.setColor(calculateColor());
        canvas.drawRoundRect(mRoundRectFOut, getMeasuredHeight() / 2, getMeasuredHeight() / 2, mPaintCover);

        //画圆边框和填充
        canvas.drawCircle(centerX, getMeasuredHeight() / 2, radius, mPaintCircleOut);
        canvas.drawCircle(centerX, getMeasuredHeight() / 2, radius - bgWidth, mPaintCircleFill);
    }

    /**
     * 根据移动的距离，设置颜色透明度
     *
     * @return
     */
    private int calculateColor() {
        //总距离
        float total = centerXRight - centerXLeft;
        //当前移动距离
        float now = centerX - centerXLeft;
        //获取颜色的red，green，blue数值
        int red = Color.red(coverColor);
        int green = Color.green(coverColor);
        int blue = Color.blue(coverColor);
        //获取选中层颜色的透明度
        int alpha = Color.alpha(coverColor);
        alpha *= now / total;
        return Color.argb(alpha, red, green, blue);
    }

    private void updateCircle(MotionEvent event) {
        if (event.getX() > centerXRight) {
            centerX = centerXRight;
        } else if (event.getX() < centerXLeft) {
            centerX = centerXLeft;
        } else {
            centerX = event.getX();
        }
        invalidate();
    }

    /**
     * 根据touchX的距离，计算选中状态
     *
     * @param event   事件对象
     * @param isMoved 是否是移动
     */
    private void countToggle(MotionEvent event, boolean isMoved) {
        if (isMoved) {
            //移动事件
            boolean tempBool;
            if (event.getX() >= getMeasuredWidth() / 2) {
                centerX = centerXRight;
                tempBool = true;
            } else {
                centerX = centerXLeft;
                tempBool = false;
            }
            if (this.isToggleOn != tempBool) {
                this.isToggleOn = tempBool;
                if (onToggleChangeListener != null) {
                    onToggleChangeListener.onChange(isToggleOn);
                }
            }
            invalidate();
        } else {
            preAnimation();
        }
    }

    private void preAnimation() {
        //当作点击事件处理
        float start = centerX;//从当前圆所在的中心点开始动画
        float end;
        if (this.isToggleOn) {
            end = centerXLeft;
        } else {
            end = centerXRight;
        }
        //动画
        animation(start, end);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (animator != null && animator.isRunning())
            return true;
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                lastX = event.getX();
                mPaintCircleFill.setColor(circleSelectColor);
                break;
            case MotionEvent.ACTION_MOVE:
                mPaintCircleFill.setColor(circleSelectColor);
                //修改绘制状态
                updateCircle(event);
                break;
            case MotionEvent.ACTION_UP:
                mPaintCircleFill.setColor(circleDefaultColor);
                //手势弹起，根据手势所在位置计算是否该选中
                countToggle(event, (Math.abs(lastX - event.getX()) > 0));
                break;
        }
        return true;
    }

    /**
     * 移动动画
     *
     * @param start
     * @param end
     */
    public void animation(final float start, final float end) {
        animator = ValueAnimator.ofFloat(start, end);
        //下面方法有疑问
        //animator.setFloatValues(start,end);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(ANIMATION_TIME);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                centerX = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                SwitchButtonView.this.isToggleOn = !SwitchButtonView.this.isToggleOn;
                centerX = end;
                if (onToggleChangeListener != null) {
                    onToggleChangeListener.onChange(SwitchButtonView.this.isToggleOn);
                }
                postInvalidate();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                centerX = start;
                postInvalidate();
            }
        });
        //不要忘记了启动动画
        animator.start();
    }

    public interface OnToggleChangeListener {
        void onChange(boolean isToggleOn);
    }
}