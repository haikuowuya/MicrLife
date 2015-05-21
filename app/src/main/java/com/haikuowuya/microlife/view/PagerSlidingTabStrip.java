package com.haikuowuya.microlife.view;

import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.StaticLayout;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**注意将ViewPager的页面滑动事件设置在当前view*/
public class PagerSlidingTabStrip extends HorizontalScrollView
{
	/**是否指示器的长度只是和文字的长度一直而不是整个TextView的长度*/
	private static final boolean INDICATOR_WIDTH_EQUALS_TEXTVIEW_VALID_WIDTH = false;

	public interface IconTabProvider
	{
		public int getPageIconResId(int position);
	}

	// @formatter:off
	private static final int[] ATTRS = new int[] {
		android.R.attr.textSize,
		android.R.attr.textColor
    };
	// @formatter:on

	private LinearLayout.LayoutParams defaultTabLayoutParams;
	private LinearLayout.LayoutParams expandedTabLayoutParams;

	private final PageListener pageListener = new PageListener();
	public OnPageChangeListener delegatePageListener;

	private LinearLayout tabsContainer;
	private ViewPagerEx pager;
	private int tabCount;
	private int currentPosition = 0;
	private float currentPositionOffset = 0f;

	private Paint rectPaint;
	private Paint dividerPaint;

	private boolean checkedTabWidths = false;

	private int indicatorColor = 0xFFFF0000;
	private int underlineColor = 0x1A000000;
	private int dividerColor = 0x1A000000;

	private boolean shouldExpand = true;
	private boolean textAllCaps = true;

	private int scrollOffset = 52;
	private int indicatorHeight = 1;//dp
	private int underlineHeight = 1;//dp
	private int dividerPadding = 12;
	private int tabPadding = 24;
	private int dividerWidth = 1;

	private int tabTextSize = 18;
	private int tabTextColor = 0xFF666666;
	private int tabCurrentTextColor = 0xFFFF0000;
	private Typeface tabTypeface = null;
	private int tabTypefaceStyle = Typeface.NORMAL;
	private int lastScrollX = 0;
	private int tabBackgroundResId = android.R.color.transparent;
	private Locale locale;

	public PagerSlidingTabStrip(Context context)
	{
		this(context, null);
	}

	public PagerSlidingTabStrip(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public PagerSlidingTabStrip(Context context, AttributeSet attrs,
		int defStyle)
	{
		super(context, attrs, defStyle);
		setFillViewport(true);
		setWillNotDraw(false);
		tabsContainer = new LinearLayout(context);
		tabsContainer.setOrientation(LinearLayout.HORIZONTAL);
		tabsContainer.setLayoutParams(new RelativeLayout.LayoutParams(
			LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		addView(tabsContainer);
		DisplayMetrics dm = getResources().getDisplayMetrics();

		scrollOffset = (int) TypedValue.applyDimension(
			TypedValue.COMPLEX_UNIT_DIP, scrollOffset, dm);
		indicatorHeight = (int) TypedValue.applyDimension(
			TypedValue.COMPLEX_UNIT_DIP, indicatorHeight, dm);
		underlineHeight = (int) TypedValue.applyDimension(
			TypedValue.COMPLEX_UNIT_DIP, underlineHeight, dm);
		dividerPadding = (int) TypedValue.applyDimension(
			TypedValue.COMPLEX_UNIT_DIP, dividerPadding, dm);
		tabPadding = (int) TypedValue.applyDimension(
			TypedValue.COMPLEX_UNIT_DIP, tabPadding, dm);
		dividerWidth = (int) TypedValue.applyDimension(
			TypedValue.COMPLEX_UNIT_DIP, dividerWidth, dm);
		tabTextSize = (int) TypedValue.applyDimension(
			TypedValue.COMPLEX_UNIT_SP, tabTextSize, dm);
		TypedArray a = context.obtainStyledAttributes(attrs, ATTRS);
		tabTextSize = a.getDimensionPixelSize(0, tabTextSize);
		tabTextColor = a.getColor(1, tabTextColor);
		a.recycle();
		// get custom attrs

//		a = context.obtainStyledAttributes(attrs,
//			R.styleable.PagerSlidingTabStrip);
//		indicatorColor = a.getColor(
//			R.styleable.PagerSlidingTabStrip_indicatorColor, indicatorColor);
//		underlineColor = a.getColor(
//			R.styleable.PagerSlidingTabStrip_underlineColor, underlineColor);
//		dividerColor = a.getColor(
//			R.styleable.PagerSlidingTabStrip_dividerColor, dividerColor);
//		indicatorHeight = a.getDimensionPixelSize(
//			R.styleable.PagerSlidingTabStrip_indicatorHeight, indicatorHeight);
//		underlineHeight = a.getDimensionPixelSize(
//			R.styleable.PagerSlidingTabStrip_underlineHeight, underlineHeight);
//		dividerPadding = a.getDimensionPixelSize(
//			R.styleable.PagerSlidingTabStrip_dividerPadding, dividerPadding);
//		tabPadding = a.getDimensionPixelSize(
//			R.styleable.PagerSlidingTabStrip_tabPaddingLeftRight, tabPadding);
//		tabBackgroundResId = a.getResourceId(
//			R.styleable.PagerSlidingTabStrip_tabBackground, tabBackgroundResId);
//		shouldExpand = a.getBoolean(
//			R.styleable.PagerSlidingTabStrip_shouldExpand, shouldExpand);
//		scrollOffset = a.getDimensionPixelSize(
//			R.styleable.PagerSlidingTabStrip_scrollOffset, scrollOffset);
//		textAllCaps = a.getBoolean(
//			R.styleable.PagerSlidingTabStrip_textAllCaps, textAllCaps);
//		a.recycle();

		rectPaint = new Paint();
		rectPaint.setAntiAlias(true);
		rectPaint.setStyle(Style.FILL);

		dividerPaint = new Paint();
		dividerPaint.setAntiAlias(true);
		dividerPaint.setStrokeWidth(dividerWidth);

		defaultTabLayoutParams = new LinearLayout.LayoutParams(
			LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 1.0f);
		expandedTabLayoutParams = new LinearLayout.LayoutParams(0,
			LayoutParams.MATCH_PARENT, 1.0f);

		if (locale == null)
		{
			locale = getResources().getConfiguration().locale;
		}
	}

	public void setViewPager(ViewPagerEx pager)
	{
		this.pager = pager;
		if (pager.getAdapter() == null)
		{
			throw new IllegalStateException(
				"ViewPager does not have adapter instance.");
		}
		notifyDataSetChanged();
		pager.setOnPageChangeListener(pageListener);
	}

	public void setOnPageChangeListener(OnPageChangeListener listener)
	{
		this.delegatePageListener = listener;
	}

	public void notifyDataSetChanged()
	{
		tabsContainer.removeAllViews();
		tabCount = pager.getAdapter().getCount();
		tabsContainer.setWeightSum(tabCount);
		for (int i = 0; i < tabCount; i++)
		{
			if (pager.getAdapter() instanceof IconTabProvider)
			{
				addIconTab(i,
					((IconTabProvider) pager.getAdapter()).getPageIconResId(i));
			}
			else
			{
				addTextTab(i, pager.getAdapter().getPageTitle(i).toString());
			}
		}
		updateTabStyles();
		updateHorizonalBackground(0);
		checkedTabWidths = false;
		getViewTreeObserver().addOnGlobalLayoutListener(
			new OnGlobalLayoutListener()
			{
				@SuppressWarnings("deprecation")
				@SuppressLint("NewApi")
				@Override
				public void onGlobalLayout()
				{
					if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
					{
						getViewTreeObserver()
							.removeGlobalOnLayoutListener(this);
					}
					else
					{
						getViewTreeObserver()
							.removeOnGlobalLayoutListener(this);
					}

					currentPosition = pager.getCurrentItem();
					scrollToChild(currentPosition, 0);
				}
			});

	}

	private void addTextTab(final int position, String title)
	{
		final TextView tab = new TextView(getContext());
		tab.setText(title);
		tab.setLayoutParams(defaultTabLayoutParams);
		tab.setFocusable(true);
		int tabWidth = getResources().getDisplayMetrics().widthPixels
			/ tabCount;
		tab.setWidth(tabWidth);
		tab.setGravity(Gravity.CENTER);
		tab.setSingleLine();

		tab.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				pager.setCurrentItem(position);
				updateHorizonalBackground(position);
			}
		});
		// LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
		// LayoutParams.MATCH_PARENT, 1);
		tabsContainer.addView(tab);

	}

	private void addIconTab(final int position, int resId)
	{

		ImageButton tab = new ImageButton(getContext());
		tab.setFocusable(true);
		tab.setImageResource(resId);

		tab.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				pager.setCurrentItem(position);
			}
		});

		tabsContainer.addView(tab);

	}

	@SuppressLint("NewApi")
	private void updateTabStyles()
	{
		for (int i = 0; i < tabCount; i++)
		{
			View v = tabsContainer.getChildAt(i);
			v.setLayoutParams(defaultTabLayoutParams);
			v.setBackgroundResource(tabBackgroundResId);
			if (shouldExpand)
			{
				v.setPadding(0, 0, 0, 0);
			}
			else
			{
				v.setPadding(tabPadding, 0, tabPadding, 0);
			}

			if (v instanceof TextView)
			{
				TextView tab = (TextView) v;
				tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, tabTextSize);
				tab.setTypeface(tabTypeface, tabTypefaceStyle);
				tab.setTextColor(tabTextColor);
				// setAllCaps() is only available from API 14, so the upper case is made manually if we are on
				// a pre-ICS-build
				if (textAllCaps)
				{
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
					{
						tab.setAllCaps(true);
					}
					else
					{
						tab.setText(tab.getText().toString()
							.toUpperCase(locale));
					}
				}
			}

		}

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		if (!shouldExpand
			|| MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.UNSPECIFIED)
		{
			return;
		}

		int myWidth = getMeasuredWidth();
		int childWidth = 0;
		for (int i = 0; i < tabCount; i++)
		{
			childWidth += tabsContainer.getChildAt(i).getMeasuredWidth();
		}

		if (!checkedTabWidths && childWidth > 0 && myWidth > 0)
		{
			if (childWidth <= myWidth)
			{
				for (int i = 0; i < tabCount; i++)
				{
					//custom
					tabsContainer.getChildAt(i).setLayoutParams(
						defaultTabLayoutParams);
					// default
					 // tabsContainer.getChildAt(i).setLayoutParams(expandedTabLayoutParams);
				}
			}

			checkedTabWidths = true;
		}
	}

	private void scrollToChild(int position, int offset)
	{

		if (tabCount == 0)
		{
			return;
		}

		int newScrollX = tabsContainer.getChildAt(position).getLeft() + offset;

		if (position > 0 || offset > 0)
		{
			newScrollX -= scrollOffset;
		}

		if (newScrollX != lastScrollX)
		{
			lastScrollX = newScrollX;
			scrollTo(newScrollX, 0);
		}
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		if (isInEditMode() || tabCount == 0)
		{
			return;
		}
		final int height = getHeight();
		// draw underline
		drawUnderLine(canvas, height);
		// draw indicator line
		drawIndicatorLine(canvas, height);
		// draw divider
		drawDividerLine(canvas, height);
	}

	/**
	 * 绘制指示器
		 * @param canvas：画布
	 * @param height：指示器高度
	 */
	private void drawIndicatorLine(Canvas canvas, final int height)
	{
		rectPaint.setColor(indicatorColor);
		// default: line below current tab
		View currentTab = tabsContainer.getChildAt(currentPosition);
		float lineLeft = currentTab.getLeft();
		float lineRight = currentTab.getRight();
		float textWidth = 0f;
		if (currentTab instanceof TextView)
		{
			TextView textView = (TextView) currentTab;
			textWidth = getTextWidth(textView);
		}

		// if there is an offset, start interpolating left and right coordinates between current and next tab
		if (currentPositionOffset > 0f && currentPosition < tabCount - 1)
		{
			View nextTab = tabsContainer.getChildAt(currentPosition + 1);
			final float nextTabLeft = nextTab.getLeft();
			final float nextTabRight = nextTab.getRight();

			lineLeft = (currentPositionOffset * nextTabLeft + (1f - currentPositionOffset)
				* lineLeft);
			lineRight = (currentPositionOffset * nextTabRight + (1f - currentPositionOffset)
				* lineRight);
		}
		if (INDICATOR_WIDTH_EQUALS_TEXTVIEW_VALID_WIDTH)
		{
			float padding = (lineRight - lineLeft - textWidth) / 2;
			lineLeft += padding;
			lineRight -= padding;
		}
		canvas.drawRect(lineLeft, height - indicatorHeight, lineRight, height,
			rectPaint);
	}

	/***
	 * 获取TextView中的文字所占的宽度
	 * @param textView
	 * @return 文字宽度
	 */
	private float getTextWidth(TextView textView)
	{
		return StaticLayout.getDesiredWidth(textView.getText(),
			textView.getPaint());
	}

	/***
	 * 绘制两个Tab之间的分割线
		 * @param canvas：画布
	 * @param height：分割线高度
	 */
	private void drawDividerLine(Canvas canvas, final int height)
	{
		dividerPaint.setColor(dividerColor);
		for (int i = 0; i < tabCount - 1; i++)
		{
			View tab = tabsContainer.getChildAt(i);
			canvas.drawLine(tab.getRight(), dividerPadding, tab.getRight(),
				height - dividerPadding, dividerPaint);
		}
	}

	/***
	 * 绘制下划线
	 * @param canvas：画布
	 * @param height：下划线高度
	 */
	private void drawUnderLine(Canvas canvas, final int height)
	{
		rectPaint.setColor(underlineColor);
		canvas.drawRect(0, height - underlineHeight, tabsContainer.getWidth(),
			height, rectPaint);
	}

	private class PageListener implements ViewPagerEx.OnPageChangeListener
	{
		@Override
		public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels)
		{
			currentPosition = position;
			currentPositionOffset = positionOffset;
			scrollToChild(position, (int) (positionOffset * tabsContainer
				.getChildAt(position).getWidth()));
			invalidate();
			if (delegatePageListener != null)
			{
				delegatePageListener.onPageScrolled(position, positionOffset,
					positionOffsetPixels);
			}
		}

		@Override
		public void onPageScrollStateChanged(int state)
		{
			if (state == ViewPager.SCROLL_STATE_IDLE)
			{
				scrollToChild(pager.getCurrentItem(), 0);
			}

			if (delegatePageListener != null)
			{
				delegatePageListener.onPageScrollStateChanged(state);
			}
		}

		@Override
		public void onPageSelected(int position)
		{
			if (delegatePageListener != null)
			{
				delegatePageListener.onPageSelected(position);
			}
			updateHorizonalBackground(position);
		}

	}

	public void setIndicatorColor(int indicatorColor)
	{
		this.indicatorColor = indicatorColor;
		invalidate();
	}

	public void setIndicatorColorResource(int resId)
	{
		this.indicatorColor = getResources().getColor(resId);
		invalidate();
	}

	public int getIndicatorColor()
	{
		return this.indicatorColor;
	}

	public void setIndicatorHeight(int indicatorLineHeightPx)
	{
		this.indicatorHeight = indicatorLineHeightPx;
		invalidate();
	}

	public int getIndicatorHeight()
	{
		return indicatorHeight;
	}

	public void setUnderlineColor(int underlineColor)
	{
		this.underlineColor = underlineColor;
		invalidate();
	}

	public void setUnderlineColorResource(int resId)
	{
		this.underlineColor = getResources().getColor(resId);
		invalidate();
	}

	public int getUnderlineColor()
	{
		return underlineColor;
	}

	public void setDividerColor(int dividerColor)
	{
		this.dividerColor = dividerColor;
		invalidate();
	}

	public void setDividerColorResource(int resId)
	{
		this.dividerColor = getResources().getColor(resId);
		invalidate();
	}

	public int getDividerColor()
	{
		return dividerColor;
	}

	public void setUnderlineHeight(int underlineHeightPx)
	{
		this.underlineHeight = underlineHeightPx;
		invalidate();
	}

	public int getUnderlineHeight()
	{
		return underlineHeight;
	}

	public void setDividerPadding(int dividerPaddingPx)
	{
		this.dividerPadding = dividerPaddingPx;
		invalidate();
	}

	public int getDividerPadding()
	{
		return dividerPadding;
	}

	public void setScrollOffset(int scrollOffsetPx)
	{
		this.scrollOffset = scrollOffsetPx;
		invalidate();
	}

	public int getScrollOffset()
	{
		return scrollOffset;
	}

	public void setShouldExpand(boolean shouldExpand)
	{
		this.shouldExpand = shouldExpand;
		requestLayout();
	}

	public boolean getShouldExpand()
	{
		return shouldExpand;
	}

	public boolean isTextAllCaps()
	{
		return textAllCaps;
	}

	public void setAllCaps(boolean textAllCaps)
	{
		this.textAllCaps = textAllCaps;
	}

	public void setTextSize(int textSizePx)
	{
		this.tabTextSize = textSizePx;
		updateTabStyles();
	}

	public int getTextSize()
	{
		return tabTextSize;
	}

	public void setTextColor(int textColor)
	{
		this.tabTextColor = textColor;
		updateTabStyles();
	}
	public void setSelectedTextColor(int textColor)
	{
		this.tabCurrentTextColor = textColor;
		updateHorizonalBackground(currentPosition);
	}

	public void setTextColorResource(int resId)
	{
		this.tabTextColor = getResources().getColor(resId);
		updateTabStyles();
	}

	public int getTextColor()
	{
		return tabTextColor;
	}

	public void setTypeface(Typeface typeface, int style)
	{
		this.tabTypeface = typeface;
		this.tabTypefaceStyle = style;
		updateTabStyles();
	}

	public void setTabBackground(int resId)
	{
		this.tabBackgroundResId = resId;
	}

	public int getTabBackground()
	{
		return tabBackgroundResId;
	}

	public void setTabPaddingLeftRight(int paddingPx)
	{
		this.tabPadding = paddingPx;
		updateTabStyles();
	}

	public int getTabPaddingLeftRight()
	{
		return tabPadding;
	}

	@Override
	public void onRestoreInstanceState(Parcelable state)
	{
		SavedState savedState = (SavedState) state;
		super.onRestoreInstanceState(savedState.getSuperState());
		currentPosition = savedState.currentPosition;
		requestLayout();
	}

	@Override
	public Parcelable onSaveInstanceState()
	{
		Parcelable superState = super.onSaveInstanceState();
		SavedState savedState = new SavedState(superState);
		savedState.currentPosition = currentPosition;
		return savedState;
	}

	static class SavedState extends BaseSavedState
	{
		int currentPosition;

		public SavedState(Parcelable superState)
		{
			super(superState);
		}

		private SavedState(Parcel in)
		{
			super(in);
			currentPosition = in.readInt();
		}

		@Override
		public void writeToParcel(Parcel dest, int flags)
		{
			super.writeToParcel(dest, flags);
			dest.writeInt(currentPosition);
		}

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

	public void updateHorizonalBackground(int position)
	{
		for (int i = 0; i < tabsContainer.getChildCount(); i++)
		{
			if (position == i)
			{
				if (tabsContainer.getChildAt(i) instanceof TextView)
				{
					((TextView) tabsContainer.getChildAt(i))
						.setTextColor(tabCurrentTextColor);
				}
			}
			else
			{
				if (tabsContainer.getChildAt(i) instanceof TextView)
				{
					((TextView) tabsContainer.getChildAt(i)).setTextColor(getTextColor());
				}
				tabsContainer.getChildAt(i).setBackgroundResource(
					tabBackgroundResId);
			}
		}
	}
}
/*
 * Custom Attr : <declare-styleable name="PagerSlidingTabStrip"> <attr name="indicatorColor" format="color" />
 * <attr name="underlineColor" format="color" /> <attr name="dividerColor" format="color" /> <attr
 * name="indicatorHeight" format="dimension" /> <attr name="underlineHeight" format="dimension" /> <attr
 * name="dividerPadding" format="dimension" /> <attr name="tabPaddingLeftRight" format="dimension" /> <attr
 * name="scrollOffset" format="dimension" /> <attr name="tabBackground" format="reference" /> <attr
 * name="shouldExpand" format="boolean" /> <attr name="textAllCaps" format="boolean" /> </declare-styleable>
 */

