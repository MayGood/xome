package com.xxhx.xome.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class GestureView extends ViewGroup {
	private static int DEFAULT_BACKGROUND_COLOR = Color.parseColor("#f8f8f8");
	private static int DEFAULT_LINE_COLOR = Color.parseColor("#660099cc");
	private static int DEFAULT_ERROR_LINE_COLOR = Color.parseColor("#66ee0000");
	
	public static int STATE_IDLE = 0;
	public static int STATE_TRACING = 1;
	public static int STATE_COMPLETED = 2;
	
	private int state;
	
	private float density = 1.0f;
	
	private float cx[] = new float[9];
	private float cy[] = new float[9];
	private float px = -1f;
	private float py = -1f;
	
	private int gesureCode = 0;
	private Paint mPaint;
	
	private int threshold;
	
	private boolean inStrictMode = false;
	private boolean isErrorShowing = false;
	
	public interface OnGestureListener {
		public void onGestureStart(GestureView view);
		public void onGestureCompleted(GestureView view, int gestureCode);
	}
	
	private OnGestureListener onGestureListener;

	public GestureView(Context context) {
		super(context);
		init();
	}

	public GestureView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
		initFromAttributes(context, attrs);
	}

	public GestureView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
		initFromAttributes(context, attrs);
	}
	
	@Override
	public void addView(View child, int index, LayoutParams params) {
		if(child instanceof PointView) {
			super.addView(child, index, params);
		}
	}
	
	@Override
	public PointView getChildAt(int index) {
		return (PointView) super.getChildAt(index);
	}
	
	private void init() {
		setWillNotDraw(false);
		
		density = getResources().getDisplayMetrics().density;
		for(int i=0; i<9; i++) {
			PointView pointView = new PointView(getContext());
			addView(pointView);
			this.cx[i] = 0;
			this.cy[i] = 0;
		}
		mPaint = new Paint();
		mPaint.setColor(DEFAULT_LINE_COLOR);
		mPaint.setStrokeWidth(3 * density);
	}
	
	private void initFromAttributes(Context context, AttributeSet attrs) {
		
	}
	
	public void setOnGestureListener(OnGestureListener onGestureListener) {
		this.onGestureListener = onGestureListener;
	}
	
	public void showError(int millisecs) {
		isErrorShowing = true;
		invalidateAllChildren();
		invalidate();
		postReset(millisecs);
	}
	
	public void displayGesture(int gestureCode) {
		state = STATE_COMPLETED;
		removeCallbacks(postReset);
		isErrorShowing = false;
		this.gesureCode = gestureCode;
		invalidateAllChildren();
		invalidate();
	}
	
	private void postReset() {
		postReset(1000);
	}
	
	Runnable postReset = new Runnable() {
		
		@Override
		public void run() {
			if(state == STATE_IDLE)
				reset();
		}
	};
	private void postReset(int millisecs) {
		removeCallbacks(postReset);
		postDelayed(postReset, millisecs);
	}
	
	public void reset() {
		isErrorShowing = false;
		px = -1f;
		py = -1f;
		gesureCode = 0;
		for(int i=0; i<9; i++) {
			getChildAt(i).check(false);
		}
		invalidate();
		
		state = STATE_IDLE;
	}
	
	private int getLastPointIndex() {
		int index = -1;
		if(gesureCode > 0)
			index = gesureCode%10 -1;
		return index;
	}
	
	private void invalidateAllChildren() {
		for(int i=0; i<9; i++) {
			getChildAt(i).invalidate();
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
			if(state == STATE_IDLE) {
				reset();
				state = STATE_TRACING;
				if(onGestureListener != null) {
					onGestureListener.onGestureStart(this);
				}
			}
			else {
				return true;
			}
			break;
		case MotionEvent.ACTION_UP:
			px = -1f;
			py = -1f;
			if(state == STATE_TRACING) {
				state = STATE_IDLE;
				if(onGestureListener != null) {
					onGestureListener.onGestureCompleted(this, gesureCode);
				}
				postReset();
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if(state != STATE_TRACING)
				return true;
			px = event.getX();
			py = event.getY();
			int childIndex = findChildIndex(px, py);
			if(childIndex != -1) {
				PointView pointView = getChildAt(childIndex);
				if(!pointView.isChecked) {
					if(!inStrictMode && gesureCode>0) {
						int lastPointIndex = getLastPointIndex();
						int needCheckIndex = -1;
						switch (childIndex) {
						case 0:// 2,6,8
							if(lastPointIndex == 2 && (!getChildAt(1).isChecked)) {
								needCheckIndex = 1;
							}
							else if(lastPointIndex == 6 && (!getChildAt(3).isChecked)) {
								needCheckIndex = 3;
							}
							else if(lastPointIndex == 8 && (!getChildAt(4).isChecked)) {
								needCheckIndex = 4;
							}
							break;
						case 1:// 7
							if(lastPointIndex == 7 && (!getChildAt(4).isChecked)) {
								needCheckIndex = 4;
							}
							break;
						case 2:// 0,6,8
							if(lastPointIndex == 0 && (!getChildAt(1).isChecked)) {
								needCheckIndex = 1;
							}
							else if(lastPointIndex == 6 && (!getChildAt(4).isChecked)) {
								needCheckIndex = 4;
							}
							else if(lastPointIndex == 8 && (!getChildAt(5).isChecked)) {
								needCheckIndex = 5;
							}
							break;
						case 3:// 5
							if(lastPointIndex == 5 && (!getChildAt(4).isChecked)) {
								needCheckIndex = 4;
							}
							break;
						case 5:// 3
							if(lastPointIndex == 3 && (!getChildAt(4).isChecked)) {
								needCheckIndex = 4;
							}
							break;
						case 6:// 0,2,8
							if(lastPointIndex == 0 && (!getChildAt(3).isChecked)) {
								needCheckIndex = 3;
							}
							else if(lastPointIndex == 2 && (!getChildAt(4).isChecked)) {
								needCheckIndex = 4;
							}
							else if(lastPointIndex == 8 && (!getChildAt(7).isChecked)) {
								needCheckIndex = 7;
							}
							break;
						case 7:// 1:
							if(lastPointIndex == 1 && (!getChildAt(4).isChecked)) {
								needCheckIndex = 4;
							}
							break;
						case 8:// 0,2,6
							if(lastPointIndex == 0 && (!getChildAt(4).isChecked)) {
								needCheckIndex = 4;
							}
							else if(lastPointIndex == 2 && (!getChildAt(5).isChecked)) {
								needCheckIndex = 5;
							}
							else if(lastPointIndex == 6 && (!getChildAt(7).isChecked)) {
								needCheckIndex = 7;
							}
						}
						if(needCheckIndex > 0) {
							getChildAt(needCheckIndex).check(true);
							gesureCode = gesureCode*10 + (needCheckIndex+1);
						}
					}
					pointView.check(true);
					gesureCode = gesureCode*10 + (childIndex+1);
				}
			}
			break;
		default:
			return true;
		}
		invalidate();
		return true;
	}
	
	private int findChildIndex(float ax, float ay) {
		for(int i=0; i<9; i++) {
			if(Math.abs(ax-this.cx[i])<threshold && Math.abs(ay-this.cy[i])<threshold) {
				return i;
			}
		}
		return -1;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(isErrorShowing) {
			mPaint.setColor(DEFAULT_ERROR_LINE_COLOR);
		}
		else {
			mPaint.setColor(DEFAULT_LINE_COLOR);
		}
		int tmpCode = gesureCode;
		int lastP = tmpCode % 10;
		tmpCode /= 10;
		int p = tmpCode % 10;
		while(p > 0) {
			tmpCode /= 10;
			
			// draw a line between p and lastP
			canvas.drawLine(this.cx[p-1], this.cy[p-1], this.cx[lastP-1], this.cy[lastP-1], mPaint);
			
			lastP = p;
			p = tmpCode % 10;
		}
		if(gesureCode >0 && px>0 && py>0) {
			p = gesureCode%10 - 1;
			canvas.drawLine(this.cx[p], this.cy[p], px, py, mPaint);
		}
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if(changed) {
			int parentWidth = r - l;
			int childSize = (int) (parentWidth * 0.3);
			int gapSize = (parentWidth - 3*childSize) / 2;
			int childCount = getChildCount();
			threshold = (int) (0.3 * childSize);
			for(int i=0; i<childCount; i++) {
				if(i >= 9)
					return;
				PointView pointView = getChildAt(i);
				int x = i % 3;
				int y = i / 3;
				int cl = (gapSize+childSize) * x;
				int ct = (gapSize+childSize) * y;
				pointView.measure(childSize, childSize);
				pointView.layout(cl, ct, cl+childSize, ct+childSize);
				this.cx[i] = (float) (cl + 0.5*childSize);
				this.cy[i] = (float) (ct + 0.5*childSize);
			}
		}
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int measureSize = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
		setMeasuredDimension(measureSize, measureSize);
	}
	
	class PointView extends View {
		private boolean isChecked;
		
		int colorUnchecked = Color.parseColor("#bababa");
		int colorChecked = Color.parseColor("#0099cc");
		int colorErrorChecked = Color.parseColor("#ee0000");
		Paint pointPaint;
		int radius;

		public PointView(Context context) {
			super(context);
			pointPaint = new Paint();
			radius = (int) (3 * density);
			isChecked = false;
		}
		
		public void check(boolean isChecked) {
			if(this.isChecked != isChecked) {
				this.isChecked = isChecked;
				invalidate();
			}
		}
		
		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			if(isChecked) {
				if(isErrorShowing) {
					pointPaint.setColor(colorErrorChecked);
				}
				else {
					pointPaint.setColor(colorChecked);
				}
			}
			else {
				pointPaint.setColor(colorUnchecked);
			}
			
			int width = getMeasuredWidth();
			int height = getMeasuredHeight();
			float cx = (float) (0.5 * width);
			float cy = (float) (0.5 * height);
			
			canvas.drawCircle(cx, cy, radius, pointPaint);
		}
		
		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			int specSize = MeasureSpec.getSize(widthMeasureSpec);
			setMeasuredDimension(specSize, specSize);
		}
		
	}

}
