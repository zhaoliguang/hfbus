package com.keli.hfbus.view;

import java.util.Vector;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/** */
/**
 * 
 * 单行文本跑马灯控件
 */
public class AutoScrollTextView extends TextView implements OnClickListener {
	public final static String TAG = AutoScrollTextView.class.getSimpleName();

	private float textLength = 0f;// 文本长度
	private float viewWidth = 0f;
	private float step = 1f;// 文字的横坐标

	public boolean isStarting = false;// 是否开始滚动
	private Paint paint = new Paint();// 绘图样式
	private String mtext = "";// 文本内容
	int speed = 1;

	public AutoScrollTextView(Context context) {
		super(context);
		initView();
	}

	public AutoScrollTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public AutoScrollTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	/** */
	/**
	 * 初始化控件
	 */
	private void initView() {
		mString = new Vector<String>();
		setOnClickListener(this);
		mtext = getText().toString();
		mStrText = mtext;
	}

	StringBuffer sBuffer;
	int num;
	int totalnum;
	int mctrlwidth;
	int mctrlheight;
	boolean isFirst = true;

	@Override
	public Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();
		SavedState ss = new SavedState(superState);

		ss.step = step;
		ss.isStarting = isStarting;

		return ss;
	}

	@Override
	public void onRestoreInstanceState(Parcelable state) {
		if (!(state instanceof SavedState)) {
			super.onRestoreInstanceState(state);
			return;
		}
		SavedState ss = (SavedState) state;
		super.onRestoreInstanceState(ss.getSuperState());

		step = ss.step;
		isStarting = ss.isStarting;
	}

	public static class SavedState extends BaseSavedState {
		public boolean isStarting = false;
		public float step = 0.0f;

		SavedState(Parcelable superState) {
			super(superState);
		}

		@Override
		public void writeToParcel(Parcel out, int flags) {
			super.writeToParcel(out, flags);
			out.writeBooleanArray(new boolean[] { isStarting });
			out.writeFloat(step);
		}

		public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {

			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}

			@Override
			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}
		};

		private SavedState(Parcel in) {
			super(in);
			boolean[] b = null;
			in.readBooleanArray(b);
			if (b != null && b.length > 0)
				isStarting = b[0];
			step = in.readFloat();
		}
	}

	/** */
	/**
	 * 开始滚动
	 */
	public void startScroll() {
		isStarting = true;
		invalidate();
	}

	/** */
	/**
	 * 停止滚动
	 */
	public void stopScroll() {
		isStarting = false;
		invalidate();
	}

	private int mCurrentLine = 0;// 当前行
	int mTextPosx = 0, mTextPosy = 0;

	@Override
	public void onDraw(Canvas canvas) {
		if (isFirst) {
			GetTextIfon();
			isFirst = false;
		}

		for (int i = this.mCurrentLine, j = 0; i < this.mRealLine; i++, j++) {
			canvas.drawText((String) (mString.elementAt(i)), this.mTextPosx,
					this.mTextPosy + this.mFontHeight * j, paint);
		}

		if (!isStarting) {
			return;
		}
		mTextPosy -= speed;
		if (mTextPosy < -(mRealLine - 1) * mFontHeight) {
			mTextPosy = mctrlheight + mFontHeight;
		}
		invalidate();
	}

	@Override
	public void onClick(View v) {
		if (isStarting)
			stopScroll();
		else
			startScroll();
	}

	int mFontHeight, mPageLineNum, mRealLine;
	String mStrText;
	Vector<String> mString = null;

	/**
	 * 获得字符串信息，包括行数、页数等
	 */
	public void GetTextIfon() {
		if (paint == null) {
			return;
		}
		mctrlheight = getHeight();
		mctrlwidth = getWidth();
		char ch;
		int w = 0;
		int istart = 0;
		mRealLine = 0;

		FontMetrics fm = paint.getFontMetrics();// 得到系统默认字体属性
		mFontHeight = (int) (Math.ceil(fm.descent - fm.top) + 2);// 获得字体高度

		mPageLineNum = (int) (mctrlheight / mFontHeight);// 获得行数
		mTextPosy = mctrlheight + mFontHeight;
		int count = this.mStrText.length();
		for (int i = 0; i < count; i++) {
			ch = this.mStrText.charAt(i);
			float[] widths = new float[1];
			String str = String.valueOf(ch);
			paint.getTextWidths(str, widths);
			if (ch == '\n') {
				mRealLine++;// 真实的行数加一
				mString.addElement(this.mStrText.substring(istart, i));
				istart = i + 1;
				w = 0;
			} else {
				w += (int) Math.ceil(widths[0]);
				if (w > getWidth()) {
					mRealLine++;// 真实的行数加一
					mString.addElement(this.mStrText.substring(istart, i));
					istart = i;
					i--;
					w = 0;
				} else {
					if (i == count - 1) {
						mRealLine++;// 真实的行数加一
						mString.addElement(this.mStrText.substring(istart,
								count));
					}
				}
			}
		}
	}

	@Override
	protected void onTextChanged(CharSequence text, int start, int before,
			int after) {
		// TODO Auto-generated method stub
		super.onTextChanged(text, start, before, after);
		mStrText = text.toString();
		GetTextIfon();
	}

}
