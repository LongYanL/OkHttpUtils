package com.baway.acer.okhttputils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by acer on 2017/3/18.
 */

public class MyView extends View {

    Paint paint=new Paint();
    private Region rectRegion;
    private Region inRegion;
    private Region outRegion;
    private Path rectPath;
    private Path inPath;
    private Path outPath;
    private int textsize;
    private int incircle;
    private int outcircle;
    private int inpaint;
    private int outpaint;
    public MyView(Context context) {
        this(context, null);
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
       init(context,attrs);
    }
    private void init(Context context, AttributeSet attrs) {
        //得到自定义属性
        TypedArray ta=context.obtainStyledAttributes(attrs,R.styleable.myCircle);
        //字体大小
        textsize = ta.getInteger(R.styleable.myCircle_textSize, 60);
        //小圆的半径
        incircle = ta.getInteger(R.styleable.myCircle_inCircle, 120);
        //大圆的半径
        outcircle = ta.getInteger(R.styleable.myCircle_outCircle, 240);


        //设置小圆的颜色
        inpaint = ta.getColor(R.styleable.myCircle_inPaint, Color.YELLOW);
        //设置大圆的颜色
        outpaint = ta.getColor(R.styleable.myCircle_outPaint, Color.RED);

        ta.recycle();

        rectRegion = new Region();

        inRegion = new Region();

        outRegion = new Region();

        rectPath = new Path();

        inPath = new Path();

        outPath = new Path();

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //为画笔设置颜色
        paint.setColor(Color.BLUE);
        //初始化矩形的路径
        Path rect=rectPath;
        //绘制正方形
        canvas.drawPath(rect,paint);


        //为画笔设置颜色
        paint.setColor(outpaint);
        //初始化大圆的路径
        Path out=outPath;
        //绘制大圆
        canvas.drawPath(out,paint);


        //为小圆赋颜色值
        paint.setColor(inpaint);
        //初始化小圆的路径
        Path in=inPath;
        //绘制小圆
        canvas.drawPath(in,paint);



        //绘制字体
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(2);
        paint.setTextSize(60);
        int twidth= (int) paint.measureText("圆环");
        canvas.drawText("圆环",(520-twidth)/2,(520+60)/2,paint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){

            case MotionEvent.ACTION_DOWN:

                int x= (int) event.getX();

                int y= (int) event.getY();
                //小圆监听
                if (inRegion.contains(x,y)){
                    Toast.makeText(this.getContext(), "内圆被点击", Toast.LENGTH_SHORT).show();
                    //大圆监听
                }else if (outRegion.contains(x,y)){

                    Toast.makeText(this.getContext(), "圆环被点击", Toast.LENGTH_SHORT).show();
                    //矩形监听
                }else if (rectRegion.contains(x,y)){
                    Toast.makeText(this.getContext(), "正方形被点击", Toast.LENGTH_SHORT).show();
                    //空白监听
                }else {
                    Toast.makeText(this.getContext(), "空白被点击", Toast.LENGTH_SHORT).show();

                }

                break;

        }
        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //设置矩形的坐标和长宽
        rectPath.addRect(20,20,500,500, Path.Direction.CW);
        //设置小圆的坐标和半径
        inPath.addCircle(260,260,incircle, Path.Direction.CW);
        //设置大圆的坐标和半径
        outPath.addCircle(260,260,outcircle, Path.Direction.CW);

        Region region=new Region(-w,-h,w,h);
         //绘制矩形
        rectRegion.setPath(rectPath,region);
        //绘制小圆
        inRegion.setPath(inPath,region);
        //绘制大圆
        outRegion.setPath(outPath,region);

    }
}
