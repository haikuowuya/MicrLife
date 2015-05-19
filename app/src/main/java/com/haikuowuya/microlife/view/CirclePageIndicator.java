package com.haikuowuya.microlife.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewConfigurationCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.haikuowuya.microlife.R;
import com.haikuowuya.microlife.view.common.InfinitePagerAdapter;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;
import static android.widget.LinearLayout.HORIZONTAL;
import static android.widget.LinearLayout.VERTICAL;


public class CirclePageIndicator extends View
{
	private static final int INVALID_POINTER = -1;
	/**默认是否居中*/
	private static final boolean DEFAULT_CENTERED = true;
	/**默认是否跳跃方式*/
	private static final boolean DEFAULT_SNAP = false;
	/**默认圆圈内部的颜色*/
	private static final int DEFAULT_PAGE_COLOR = 0x88856934;
	/**默认生成的圆圈的布局方向*/
	private static final int DEFAULT_ORIENTATION = 0;
	/**默认当前圆圈的填充颜色*/
	private static final int DEFAULT_FILL_COLOR = 0xFFFFFFFF;
	/**默认圆圈的边框颜色*/
	private static final int DEFAULT_STROKE_COLOR =0x88856934;
	/**默认圆圈边框的厚度*/
	private static final float DEFAULT_STROKE_WIDTH = 1.f;
	/**默认圆圈的半径*/
	private static final float DEFAULT_RADIUS = 3.f;
	/** 圆圈的半径 */
	private float mRadius;
	/** 圆的默认填充颜色 */
	private final Paint mPaintPageFill = new Paint(ANTI_ALIAS_FLAG);
	/** 圆边框填充颜色 */
	private final Paint mPaintStroke = new Paint(ANTI_ALIAS_FLAG);
	/** 圆的填充颜色 */
	private final Paint mPaintFill = new Paint(ANTI_ALIAS_FLAG);
	/** 与之关联的 ViewPager */
	private ViewPagerEx mViewPager;
	private ViewPagerEx.OnPageChangeListener mListener;
	private int mCurrentPage;
	private int mSnapPage;
	private float mPageOffset;
	private int mScrollState;
	private int mOrientation;
	private boolean mCentered;
	private boolean mSnap;
	private int mTouchSlop;
	private float mLastMotionX = -1;
	private int mActivePointerId = INVALID_POINTER;
	private boolean mIsDragging;

	public CirclePageIndicator(Context context)
	{
		this(context, null);
	}

	public CirclePageIndicator(Context context, AttributeSet attrs)
	{
		this(context, attrs, R.attr.vpiCirclePageIndicatorStyle);
	}

	public CirclePageIndicator(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		if (isInEditMode())
			return;
		final Resources res = getResources();
		final float defaultStrokeWidth = DEFAULT_STROKE_WIDTH * res.getDisplayMetrics().density;
		final float defaultRadius = DEFAULT_RADIUS * res.getDisplayMetrics().density;
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CirclePageIndicator,
			defStyle, 0);
		try
		{
			mCentered = a.getBoolean(R.styleable.CirclePageIndicator_centered, DEFAULT_CENTERED);
			mOrientation = a.getInt(R.styleable.CirclePageIndicator_android_orientation,
				DEFAULT_ORIENTATION);
			mPaintPageFill.setStyle(Style.FILL);
			mPaintPageFill.setColor(a.getColor(R.styleable.CirclePageIndicator_pageColor, DEFAULT_PAGE_COLOR));
			mPaintStroke.setStyle(Style.STROKE);
			mPaintStroke.setColor(a.getColor(R.styleable.CirclePageIndicator_strokeColor, DEFAULT_STROKE_COLOR));
			mPaintStroke.setStrokeWidth(a.getDimension(R.styleable.CirclePageIndicator_strokeWidth, defaultStrokeWidth));
			mPaintFill.setStyle(Style.FILL);
			mPaintFill.setColor(a.getColor(R.styleable.CirclePageIndicator_fillColor,
				DEFAULT_FILL_COLOR));
			mRadius = a.getDimension(R.styleable.CirclePageIndicator_radius, defaultRadius);
			mSnap = a.getBoolean(R.styleable.CirclePageIndicator_snap, DEFAULT_SNAP);
			Drawable background = a.getDrawable(R.styleable.CirclePageIndicator_android_background);
			if (background != null)
			{
				setBackgroundDrawable(background);
			}
		}
		finally
		{
			a.recycle();
		}

		final ViewConfiguration configuration = ViewConfiguration.get(context);
		mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
	}

	public void setCentered(boolean centered)
	{
		mCentered = centered;
		invalidate();
	}

	public boolean isCentered()
	{
		return mCentered;
	}

	public void setPageColor(int pageColor)
	{
		mPaintPageFill.setColor(pageColor);
		invalidate();
	}

	public int getPageColor()
	{
		return mPaintPageFill.getColor();
	}

	public void setFillColor(int fillColor)
	{
		mPaintFill.setColor(fillColor);
		invalidate();
	}

	public int getFillColor()
	{
		return mPaintFill.getColor();
	}

	public void setOrientation(int orientation)
	{
		switch (orientation)
		{
		case HORIZONTAL:
		case VERTICAL:
			mOrientation = orientation;
			requestLayout();
			break;
		default:
			throw new IllegalArgumentException("Orientation must be either HORIZONTAL or VERTICAL.");
		}
	}

	public int getOrientation()
	{
		return mOrientation;
	}

	public void setStrokeColor(int strokeColor)
	{
		mPaintStroke.setColor(strokeColor);
		invalidate();
	}

	public int getStrokeColor()
	{
		return mPaintStroke.getColor();
	}

	public void setStrokeWidth(float strokeWidth)
	{
		mPaintStroke.setStrokeWidth(strokeWidth);
		invalidate();
	}

	public float getStrokeWidth()
	{
		return mPaintStroke.getStrokeWidth();
	}

	public void setRadius(float radius)
	{
		mRadius = radius;
		invalidate();
	}

	public float getRadius()
	{
		return mRadius;
	}

	public void setSnap(boolean snap)
	{
		mSnap = snap;
		invalidate();
	}

	public boolean isSnap()
	{
		return mSnap;
	}

	public boolean onTouchEvent(MotionEvent ev)
	{
		if (super.onTouchEvent(ev))
		{
			return true;
		}
		if ((mViewPager == null) || (mViewPager.getAdapter().getCount() == 0))
		{
			return false;
		}

		final int action = ev.getAction() & MotionEventCompat.ACTION_MASK;
		switch (action)
		{
		case MotionEvent.ACTION_DOWN:
			mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
			mLastMotionX = ev.getX();
			break;

		case MotionEvent.ACTION_MOVE:
		{
			final int activePointerIndex = MotionEventCompat.findPointerIndex(ev, mActivePointerId);
			final float x = MotionEventCompat.getX(ev, activePointerIndex);
			final float deltaX = x - mLastMotionX;

			if (!mIsDragging)
			{
				if (Math.abs(deltaX) > mTouchSlop)
				{
					mIsDragging = true;
				}
			}

			if (mIsDragging)
			{
				mLastMotionX = x;
				if (mViewPager.isFakeDragging() || mViewPager.beginFakeDrag())
				{
					mViewPager.fakeDragBy(deltaX);
				}
			}
			break;
		}
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			if (!mIsDragging)
			{
				final int count = mViewPager.getAdapter().getCount();
				final int width = getWidth();
				final float halfWidth = width / 2f;
				final float sixthWidth = width / 6f;

				if ((mCurrentPage > 0) && (ev.getX() < halfWidth - sixthWidth))
				{
					if (action != MotionEvent.ACTION_CANCEL)
					{
						mViewPager.setCurrentItem(mCurrentPage - 1);
					}
					return true;
				}
				else if ((mCurrentPage < count - 1) && (ev.getX() > halfWidth + sixthWidth))
				{
					if (action != MotionEvent.ACTION_CANCEL)
					{
						mViewPager.setCurrentItem(mCurrentPage + 1);
					}
					return true;
				}
			}

			mIsDragging = false;
			mActivePointerId = INVALID_POINTER;
			if (mViewPager.isFakeDragging())
				mViewPager.endFakeDrag();
			break;

		case MotionEventCompat.ACTION_POINTER_DOWN:
		{
			final int index = MotionEventCompat.getActionIndex(ev);
			mLastMotionX = MotionEventCompat.getX(ev, index);
			mActivePointerId = MotionEventCompat.getPointerId(ev, index);
			break;
		}

		case MotionEventCompat.ACTION_POINTER_UP:
			final int pointerIndex = MotionEventCompat.getActionIndex(ev);
			final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);
			if (pointerId == mActivePointerId)
			{
				final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
				mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
			}
			mLastMotionX = MotionEventCompat.getX(ev,
				MotionEventCompat.findPointerIndex(ev, mActivePointerId));
			break;
		}

		return true;
	}

	public void setViewPager(ViewPagerEx view)
	{
		if (mViewPager == view)
		{
			return;
		}
		if (mViewPager != null)
		{
			mViewPager.setOnPageChangeListener(null);
		}
		if (view.getAdapter() == null)
		{
			throw new IllegalStateException("ViewPager does not have adapter instance.");
		}
		mViewPager = view;
		mViewPager.setOnPageChangeListener(mListener);
		invalidate();
	}

	public void setViewPager(ViewPagerEx view, int initialPosition)
	{
		setViewPager(view);
		setCurrentItem(initialPosition);
	}

	public void setCurrentItem(int item)
	{
		if (mViewPager == null)
		{
			throw new IllegalStateException("ViewPager has not been bound.");
		}
		// mViewPager.setCurrentItem(item);
		mCurrentPage = item;
		invalidate();
	}

	public void setCurrentItem(int item, boolean isInfinite)
	{
		if (mViewPager == null)
		{
			throw new IllegalStateException("ViewPager has not been bound.");
		}
		if (!isInfinite)
		{
			mViewPager.setCurrentItem(item);
		}
		mCurrentPage = item;
		invalidate();
	}

	public void notifyDataSetChanged()
	{
		invalidate();
	}

	public void onPageScrollStateChanged(int state)
	{
		mScrollState = state;
		if (mListener != null)
		{
			mListener.onPageScrollStateChanged(state);
		}
	}

	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
	{
		mCurrentPage = position;
		mPageOffset = positionOffset;
		invalidate();
		if (mListener != null)
		{
			mListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
		}
	}

	public void onPageSelected(int position)
	{
		if (mSnap || mScrollState == ViewPager.SCROLL_STATE_IDLE)
		{
			mCurrentPage = position;
			mSnapPage = position;
			invalidate();
		}
		if (mListener != null)
		{
			mListener.onPageSelected(position);
		}
	}

	public void setOnPageChangeListener(ViewPagerEx.OnPageChangeListener listener)
	{
		mListener = listener;
	}

	/*
	 * (non-Javadoc)
	 * @see android.view.View#onMeasure(int, int)
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		if (mOrientation == HORIZONTAL)
		{
			setMeasuredDimension(measureLong(widthMeasureSpec), measureShort(heightMeasureSpec));
		}
		else
		{
			setMeasuredDimension(measureShort(widthMeasureSpec), measureLong(heightMeasureSpec));
		}
	}

	/**
	 * Determines the width of this view
	 * 
	 * @param measureSpec
	 *            A measureSpec packed into an int
	 * @return The width of the view, honoring constraints from measureSpec
	 */
	private int measureLong(int measureSpec)
	{
		int result;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if ((specMode == MeasureSpec.EXACTLY) || (mViewPager == null))
		{
			// We were told how big to be
			result = specSize;
		}
		else
		{
			// Calculate the width according the views count
			final int count = mViewPager.getAdapter().getCount();
			result = (int) (getPaddingLeft() + getPaddingRight() + (count * 2 * mRadius)
				+ (count - 1) * mRadius + 1);
			// Respect AT_MOST value if that was what is called for by
			// measureSpec
			if (specMode == MeasureSpec.AT_MOST)
			{
				result = Math.min(result, specSize);
			}
		}
		return result;
	}

	/**
	 * Determines the height of this view
	 * 
	 * @param measureSpec
	 *            A measureSpec packed into an int
	 * @return The height of the view, honoring constraints from measureSpec
	 */
	private int measureShort(int measureSpec)
	{
		int result;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.EXACTLY)
		{
			// We were told how big to be
			result = specSize;
		}
		else
		{
			// Measure the height
			result = (int) (2 * mRadius + getPaddingTop() + getPaddingBottom() + 1);
			// Respect AT_MOST value if that was what is called for by
			// measureSpec
			if (specMode == MeasureSpec.AT_MOST)
			{
				result = Math.min(result, specSize);
			}
		}
		return result;
	}

	@Override
	public void onRestoreInstanceState(Parcelable state)
	{
		SavedState savedState = (SavedState) state;
		super.onRestoreInstanceState(savedState.getSuperState());
		mCurrentPage = savedState.currentPage;
		mSnapPage = savedState.currentPage;
		requestLayout();
	}

	@Override
	public Parcelable onSaveInstanceState()
	{
		Parcelable superState = super.onSaveInstanceState();
		SavedState savedState = new SavedState(superState);
		savedState.currentPage = mCurrentPage;
		return savedState;
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		if (mViewPager == null)
		{
			return;
		}
		int count = mViewPager.getAdapter().getCount();
		// ==================================================================
		// ==================================================================
		// 对无限循环 ViewPager 适配器的处理
		if (mViewPager.getAdapter() instanceof InfinitePagerAdapter)
		{
			InfinitePagerAdapter infinitePagerAdapter = (InfinitePagerAdapter) mViewPager
				.getAdapter();
			count = infinitePagerAdapter.getRealCount();
		}
		// ==================================================================
		// ==================================================================
		// System.out.println("mCurrentPage = " + mCurrentPage + " count = " + count);
		if (count == 0)
		{
			return;
		}
		if (mCurrentPage >= count)
		{
			setCurrentItem(count - 1);
			return;
		}
		int longSize;
		int longPaddingBefore;
		int longPaddingAfter;
		int shortPaddingBefore;
		if (mOrientation == HORIZONTAL)
		{
			longSize = getWidth();
			longPaddingBefore = getPaddingLeft();
			longPaddingAfter = getPaddingRight();
			shortPaddingBefore = getPaddingTop();
		}
		else
		{
			longSize = getHeight();
			longPaddingBefore = getPaddingTop();
			longPaddingAfter = getPaddingBottom();
			shortPaddingBefore = getPaddingLeft();
		}

		final float threeRadius = mRadius * 3;
		float shortOffset = shortPaddingBefore + mRadius;
		float longOffset = longPaddingBefore + mRadius;
		if (mCentered)
		{
			longOffset += ((longSize - longPaddingBefore - longPaddingAfter) / 2.0f)
				- ((count * threeRadius) / 2.0f);
			shortOffset = getHeight() / 2;

		}

		float dX;
		float dY;

		float pageFillRadius = mRadius;
		if (mPaintStroke.getStrokeWidth() > 0)
		{
			pageFillRadius -= mPaintStroke.getStrokeWidth() / 2.0f;
		}

		// Draw stroked circles
		for (int iLoop = 0; iLoop < count; iLoop++)
		{
			float drawLong = longOffset + (iLoop * threeRadius);
			if (mOrientation == HORIZONTAL)
			{
				dX = drawLong;
				dY = shortOffset;
			}
			else
			{
				dX = shortOffset;
				dY = drawLong;
			}
			// Only paint fill if not completely transparent
			if (mPaintPageFill.getAlpha() > 0)
			{
				canvas.drawCircle(dX, dY, pageFillRadius, mPaintPageFill);
			}

			// Only paint stroke if a stroke width was non-zero
			if (pageFillRadius != mRadius)
			{
				canvas.drawCircle(dX, dY, mRadius, mPaintStroke);
			}
		}

		// Draw the filled circle according to the current scroll
		float cx = (mSnap ? mSnapPage : mCurrentPage) * threeRadius;
		if (!mSnap)
		{
			cx += mPageOffset * threeRadius;
		}
		if (mOrientation == HORIZONTAL)
		{
			dX = longOffset + cx;
			dY = shortOffset;
		}
		else
		{
			dX = shortOffset;
			dY = longOffset + cx;
		}
		// System.out.println("dx = " + dX + " dy = " + dY + " mRadius = " + mRadius);
		canvas.drawCircle(dX, dY, mRadius, mPaintFill);
	}

	static class SavedState extends BaseSavedState
	{
		int currentPage;

		public SavedState(Parcelable superState)
		{
			super(superState);
		}

		private SavedState(Parcel in)
		{
			super(in);
			currentPage = in.readInt();
		}

		@Override
		public void writeToParcel(Parcel dest, int flags)
		{
			super.writeToParcel(dest, flags);
			dest.writeInt(currentPage);
		}

		@SuppressWarnings("UnusedDeclaration")
		public static final Creator<SavedState> CREATOR = new Creator<SavedState>()
		{
			@Override
			public SavedState createFromParcel(Parcel in)
			{
				return new SavedState(in);
			}

			@Override
			public SavedState[] newArray(int size)
			{
				return new SavedState[size];
			}
		};
	}
}
/*
 * 自定义属性值xml文件 <?xml version="1.0" encoding="utf-8"?> <resources> <declare-styleable
 * name="ViewPagerIndicator"> <!-- Style of the circle indicator. --> <attr name="vpiCirclePageIndicatorStyle"
 * format="reference"/> </declare-styleable> <attr name="centered" format="boolean" /> <attr
 * name="selectedColor" format="color" /> <attr name="strokeWidth" format="dimension" /> <attr
 * name="unselectedColor" format="color" /> <declare-styleable name="CirclePageIndicator"> <!-- Whether or not
 * the indicators should be centered. --> <attr name="centered" /> <!-- Color of the filled circle that
 * represents the current page. --> <attr name="fillColor" format="color" /> <!-- Color of the filled circles
 * that represents pages. --> <attr name="pageColor" format="color" /> <!-- Orientation of the indicator. -->
 * <attr name="android:orientation"/> <!-- Radius of the circles. This is also the spacing between circles.
 * --> <attr name="radius" format="dimension" /> <!-- Whether or not the selected indicator snaps to the
 * circles. --> <attr name="snap" format="boolean" /> <!-- Color of the open circles. --> <attr
 * name="strokeColor" format="color" /> <!-- Width of the stroke used to draw the circles. --> <attr
 * name="strokeWidth" /> <!-- View background --> <attr name="android:background"/> </declare-styleable>
 * </resources>
 */

