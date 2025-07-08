package cn.asrobot.form.components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.OverScroller;

import java.util.ArrayList;
import java.util.List;

public class WheelView extends View {

    private List<String> items = new ArrayList<>();
    private int itemHeight = 100;
    private int selectedIndex = 0;
    private Paint textPaint;
    private int totalScrollY = 0;
    private OverScroller scroller;
    private GestureDetector gestureDetector;
    private OnItemSelectedListener listener;

    private int lastTouchY;

    public WheelView(Context context) {
        this(context, null);
    }

    public WheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(42);

        scroller = new OverScroller(getContext());

        gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                int maxScrollY = Math.max(0, (items.size() - 1) * itemHeight);
                scroller.fling(0, totalScrollY, 0, (int) -velocityY, 0, 0, 0, maxScrollY);
                invalidate();
                return true;
            }

            @Override
            public boolean onDown(MotionEvent e) {
                if (!scroller.isFinished()) {
                    scroller.abortAnimation();
                }
                return true;
            }
        });
    }

    public void setItems(List<String> items) {
        this.items = items;
        invalidate();
    }

    public void setItems(List<String> items, int selectedIndex) {
        this.items = items;
        setSelectedItem(selectedIndex);
    }

    public void setSelectedItem(int index) {
        selectedIndex = Math.max(0, Math.min(index, items.size() - 1));
        totalScrollY = selectedIndex * itemHeight;
        invalidate();
    }

    public String getSelectedItem() {
        if (items.isEmpty()) return "";
        return items.get(selectedIndex);
    }

    public int getSelectedItemIndex() {
        return selectedIndex;
    }

    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int centerY = getHeight() / 2;
        int centerItemIndex = totalScrollY / itemHeight;
        int offset = totalScrollY % itemHeight;

        for (int i = -4; i <= 4; i++) {
            int index = centerItemIndex + i;
            if (index >= 0 && index < items.size()) {
                float y = centerY + i * itemHeight - offset;

                // 透明度渐变
                int alpha = (int) (255 * Math.pow(0.65, Math.abs(i)));

                // 字体大小渐变
                float scale = (float) Math.pow(0.9, Math.abs(i));  // 每远一行缩小 10%
                float textSize = 48f * scale;  // 48sp 为选中行大小
                textPaint.setTextSize(textSize);
                textPaint.setAlpha(alpha);

                canvas.drawText(items.get(index), getWidth() / 2f, y + getTextBaselineOffset(textPaint), textPaint);
            }
        }
    }

    private float getTextBaselineOffset(Paint paint) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        return (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastTouchY = (int) event.getY();
                return true;
            case MotionEvent.ACTION_MOVE:
                int dy = (int) event.getY() - lastTouchY;
                totalScrollY -= dy;
                constrainScrollY();
                lastTouchY = (int) event.getY();
                invalidate();
                getParent().requestDisallowInterceptTouchEvent(true);
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                snapToNearest();
                getParent().requestDisallowInterceptTouchEvent(false);
                return true;
        }
        return true;
    }

    private void constrainScrollY() {
        int maxScroll = Math.max(0, (items.size() - 1) * itemHeight);
        totalScrollY = Math.max(0, Math.min(totalScrollY, maxScroll));
    }

    private void snapToNearest() {
        int nearestIndex = Math.round((float) totalScrollY / itemHeight);
        int dy = nearestIndex * itemHeight - totalScrollY;
        scroller.startScroll(0, totalScrollY, 0, dy, 300);
        selectedIndex = Math.max(0, Math.min(nearestIndex, items.size() - 1));
        invalidate();

        if (listener != null) {
            listener.onItemSelected(selectedIndex);
        }
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            totalScrollY = scroller.getCurrY();
            invalidate();
        } else if (!scroller.isFinished()) {
            snapToNearest();
        }
    }

    public interface OnItemSelectedListener {
        void onItemSelected(int index);
    }
}
