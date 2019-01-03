package com.test.walker.scanview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * author 刘鉴钊
 * create at 2018/12/12
 * description
 */
public class ScanView extends View {

    private Bitmap srcBitmap;

    private int mWidth;

    private int mHeight;

    private Paint mPaint;

    /**
     * 扇形所在的Rect
     */
    private RectF arcRectF;

    private int sweepAngle;

    /**
     * 圆心
     */
    private int centerX, centerY;

    /**
     * 外圆半径
     */
    private int outerRadius;

    private int middleRadius;

    private PorterDuffXfermode porterDuffXfermode;

    private Bitmap xfermod_dstBitmap;

    private Bitmap xfermod_srcBitmap;


    public int getSweepAngle() {
        return sweepAngle;
    }

    public void setSweepAngle(int sweepAngle) {
        this.sweepAngle = sweepAngle;
        invalidate();
    }

    public ScanView(Context context) {
        this(context, null);
    }

    public ScanView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScanView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ScanView);
        BitmapDrawable bd = (BitmapDrawable) typedArray.getDrawable(R.styleable.ScanView_svSrc);
        if (null != bd) {
            srcBitmap = bd.getBitmap();
        } else {
            srcBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon);
        }

        typedArray.recycle();
        init(srcBitmap);

    }

    private void init(Bitmap bitmap) {
        mHeight = bitmap.getHeight() + getPaddingTop() + getPaddingBottom();
        mWidth = bitmap.getWidth() + getPaddingLeft() + getPaddingRight();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);


        centerX = bitmap.getWidth() / 2 + getPaddingLeft();
        centerY = bitmap.getHeight() / 2 + getPaddingTop();

        outerRadius = bitmap.getWidth() / 2;

        middleRadius = outerRadius - 10;


        //删除交集，保留目标图模式
        porterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);

        xfermod_dstBitmap = makeDst();
        xfermod_srcBitmap = makeSrc();

        arcRectF = new RectF(getPaddingLeft() + 20, getPaddingTop() +20, mWidth - getPaddingRight() - 20, mHeight - getPaddingBottom() - 20);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制源图
        mPaint.reset();
        canvas.drawBitmap(srcBitmap, getPaddingLeft(), getPaddingTop(), mPaint);
        //裁出一个外圈圆环
        //Xfermode需要使用离屏缓冲才能达到预期效果
        int saved = canvas.saveLayer(null, null, Canvas.ALL_SAVE_FLAG);
//        //绘制Dst圆
        canvas.drawBitmap(xfermod_dstBitmap, 0, 0, mPaint);
//        //设置Xfermode
        mPaint.setXfermode(porterDuffXfermode);
          //绘制Src圆
        canvas.drawBitmap(xfermod_srcBitmap, 0, 0, mPaint);

        //==========直接绘制圆并不能实现删除交集，保留目标图=================
//        canvas.drawCircle(centerX, centerY, middleRadius, mPaint);
//        mPaint.setXfermode(porterDuffXfermode);
//        mPaint.setColor(Color.parseColor("#33000000"));
//        canvas.drawCircle(centerX, centerY, outerRadius, mPaint);
        //==========直接绘制圆并不能实现图形取反=================
        //绘制完成需要置null
        mPaint.setXfermode(null);
        //缓冲完毕复原
        canvas.restoreToCount(saved);
        mPaint.setColor(Color.parseColor("#33000000"));
        canvas.drawArc(arcRectF,-90,sweepAngle -360,true,mPaint);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = resolveSize(mWidth, widthMeasureSpec);
        mHeight = resolveSize(mHeight, heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
    }


    Bitmap makeSrc() {
        Bitmap bm = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        canvas.drawCircle(centerX, centerY, middleRadius, p);
        return bm;
    }

    Bitmap makeDst() {
        Bitmap bm = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(Color.parseColor("#33000000"));
        canvas.drawCircle(centerX, centerY, outerRadius, p);
        return bm;
    }

}
