package com.haikuowuya.microlife.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haikuowuya.microlife.view.common.InfinitePagerAdapter;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 
 * @author bo.li
 * 2014-10-11 下午1:17:11
 * TODO 自定义的View,里面包含一个可以无限滑动的ViewPager和一个页面指示器。用于滚动展示
 */
public class InfiniteViewPagerIndicatorView extends RelativeLayout
{
	private static final long DELAY_TIME = 1000L;
	private static final long PERIOD_TIME = 3 * DELAY_TIME;
	private static final boolean DEFAULT_AUTO_CYCLE = false;
	/**默认指示器高度*/
	private static final int DEFAULT_INDICATOR_HEIGHT_IN_DIP = 32;
	/**默认TextView距离左边缘8dp*/
	private static final int DEFAULT_TEXTVIEW_PADDING_LEFT_IN_DIP = 8;
	private Context mContext;
	private InfiniteViewPager mViewPager;
	private CirclePageIndicator mCicleIndicator;
	private TextView mTextView;
	private Timer mCycleTimer;
	private TimerTask mCycleTask;
	/**默认ViewPagerEx的PagerAdapter,没有进行Infinite包装过的*/
	private PagerAdapter mPagerAdapter;
	private Timer mResumingTimer;
	private TimerTask mResumingTask;
	private boolean mCycling;
	private boolean mAutoRecover;
	private boolean mAutoCycle = DEFAULT_AUTO_CYCLE;
	private ViewPagerEx.PageTransformer mViewPagerTransformer;
	LinearLayout mIndicatorContainer;
	private final Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			mViewPager.nextItem();
		}
	};

	public InfiniteViewPagerIndicatorView(Context context)
	{
		this(context, null);
	}

	public InfiniteViewPagerIndicatorView(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public InfiniteViewPagerIndicatorView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		mContext = context;
		FrameLayout viewPagerIndicatorContainer = new FrameLayout(mContext);
		// 将ViewPager添加的FrameLayout中
		addViewPagerToContainer(viewPagerIndicatorContainer);
		// 将mCicleIndicator添加的FrameLayout中
		addIndicatorToContainer(viewPagerIndicatorContainer);
		addView(viewPagerIndicatorContainer, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}

	public void hideIndicator()
	{
		mIndicatorContainer.setVisibility(View.GONE);

	}
	private void setViewPagerListener()
	{
		mViewPager.setOnPageChangeListener(new OnViewPagerChangedListenerImpl());
		mViewPager.setOnTouchListener(new OnViewPagerTouchListenerImpl());
	}

	/***
	 * 让自定义View的ViewPager与之对应的数据进行关联
	 * @param adapter：ViewPager的默认适配器， 在该方法中进行包装
	 */
	public void setViewPagerAdapter(PagerAdapter adapter)
	{
		mPagerAdapter = adapter;
		InfinitePagerAdapter wrappedAdapter = new InfinitePagerAdapter(adapter);
		mViewPager.setAdapter(wrappedAdapter);
		mTextView.setText(adapter.getPageTitle(0));
		mCicleIndicator.setViewPager(mViewPager);
		afterSetViewPagerAdapterOpt();
 
	}

	/**ViewPager与之对应的数据关联后进行的操作*/
	private void afterSetViewPagerAdapterOpt()
	{
		setViewPagerListener();
		if (mAutoCycle)
		{
			startAutoCycle();
		}
	}

	@Override
	public boolean shouldDelayChildPressedState()
	{
		return super.shouldDelayChildPressedState();
	}

	/** 将mCicleIndicator添加的FrameLayout中*/
	private void addIndicatorToContainer(FrameLayout viewPagerIndicatorContainer)
	{
		mIndicatorContainer = new LinearLayout(mContext);
		mIndicatorContainer.setBackgroundColor(0x88000000);
		mIndicatorContainer.setOrientation(LinearLayout.HORIZONTAL);
		mIndicatorContainer.setGravity(Gravity.CENTER);
		int defaultHeight = (int) (getResources().getDisplayMetrics().density * DEFAULT_INDICATOR_HEIGHT_IN_DIP);
		mTextView = new TextView(mContext);
		setTextViewStyle();
		mCicleIndicator = new CirclePageIndicator(mContext);
		mIndicatorContainer.addView(mTextView, new LinearLayout.LayoutParams(
			LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1f));
		mIndicatorContainer.addView(mCicleIndicator, new LinearLayout.LayoutParams(
			defaultHeight * 2, LayoutParams.MATCH_PARENT));
		viewPagerIndicatorContainer.addView(mIndicatorContainer, new FrameLayout.LayoutParams(
			LayoutParams.MATCH_PARENT, defaultHeight, Gravity.BOTTOM));
	}

	private void setTextViewStyle()
	{
		int left = (int) (DEFAULT_TEXTVIEW_PADDING_LEFT_IN_DIP * getResources().getDisplayMetrics().density);
		mTextView.setPadding(left, 0, 0, 0);
		mTextView.setTextColor(0xFFFFFFFF);
		mTextView.setSingleLine(true);
		mTextView.setEllipsize(TruncateAt.END);
		mTextView.setGravity(Gravity.CENTER_VERTICAL);
		mTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18.f);
	}

	/**将ViewPager添加的FrameLayout中*/
	private void addViewPagerToContainer(FrameLayout viewPagerIndicatorContainer)
	{
		mViewPager = new InfiniteViewPager(mContext);
		viewPagerIndicatorContainer.addView(mViewPager, new FrameLayout.LayoutParams(
			LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}

	public void startAutoCycle()
	{
		startAutoCycle(DELAY_TIME, PERIOD_TIME, true);
	}

	/**
	 * start auto cycle.
	 * @param delay delay time
	 * @param period period time.
	 * @param autoRecover
	 */
	public void startAutoCycle(long delay, long period, boolean autoRecover)
	{
		mCycleTimer = new Timer();
		mAutoRecover = autoRecover;
		mCycleTask = new TimerTask()
		{
			public void run()
			{
				mHandler.sendEmptyMessage(0);
			}
		};
		mCycleTimer.schedule(mCycleTask, delay, period);
		mCycling = true;
	}

	/**
	 * pause auto cycle.
	 */
	private void pauseAutoCycle()
	{
		if (mCycling)
		{
			mCycleTimer.cancel();
			mCycleTask.cancel();
			mCycling = false;
		}
		else
		{
			if (mResumingTimer != null && mResumingTask != null)
			{
				recoverCycle();
			}
		}
	}

	/**
	 * stop the auto circle
	 */
	public void stopAutoCycle()
	{
		if (mCycleTask != null)
		{
			mCycleTask.cancel();
		}
		if (mCycleTimer != null)
		{
			mCycleTimer.cancel();
		}
		if (mResumingTimer != null)
		{
			mResumingTimer.cancel();
		}
		if (mResumingTask != null)
		{
			mResumingTask.cancel();
		}
	}

	/**
	 * when paused cycle, this method can weak it up.
	 */
	private void recoverCycle()
	{

		if (!mAutoRecover)
		{
			return;
		}

		if (!mCycling)
		{
			if (mResumingTask != null && mResumingTimer != null)
			{
				mResumingTimer.cancel();
				mResumingTask.cancel();
			}
			mResumingTimer = new Timer();
			mResumingTask = new TimerTask()
			{
				@Override
				public void run()
				{
					startAutoCycle();
				}
			};
			mResumingTimer.schedule(mResumingTask, 6000);
		}
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev)
	{
		int action = ev.getAction();
		switch (action)
		{
		case MotionEvent.ACTION_DOWN:
			pauseAutoCycle();
			break;
		}
		return false;
	}

	/**
	 * set ViewPager transformer.
	 * 
	 * @param reverseDrawingOrder
	 * @param transformer
	 */
	public void setPagerTransformer(boolean reverseDrawingOrder,
		ViewPagerEx.PageTransformer transformer)
	{
		mViewPagerTransformer = transformer;
		// mViewPagerTransformer.setCustomAnimationInterface(mCustomAnimation);
		mViewPager.setPageTransformer(reverseDrawingOrder, mViewPagerTransformer);
	}

	/**
	 * preset transformers and their names
	 */
	public enum Transformer
	{
		Default("Default"), Accordion("Accordion"), Background2Foreground("Background2Foreground"), CubeIn(
			"CubeIn"), DepthPage("DepthPage"), Fade("Fade"), FlipHorizontal("FlipHorizontal"), FlipPage(
			"FlipPage"), Foreground2Background("Foreground2Background"), RotateDown("RotateDown"), RotateUp(
			"RotateUp"), Stack("Stack"), Tablet("Tablet"), ZoomIn("ZoomIn"), ZoomOutSlide(
			"ZoomOutSlide"), ZoomOut("ZoomOut");

		private final String name;

		private Transformer(String s)
		{
			name = s;
		}

		@Override
		public String toString()
		{
			return name;
		}

		public boolean equals(String other)
		{
			return (other == null) ? false : name.equals(other);
		}
	};

	 

	private class OnViewPagerTouchListenerImpl implements OnTouchListener
	{
		public boolean onTouch(View v, MotionEvent event)
		{
			int action = event.getAction();
			switch (action)
			{
			case MotionEvent.ACTION_UP:
				recoverCycle();
				break;
			}
			return false;
		}
	}

	private class OnViewPagerChangedListenerImpl implements ViewPagerEx.OnPageChangeListener
	{
		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
		{

		}

		@Override
		public void onPageSelected(int position)
		{
			 int acturalPosition = position % mPagerAdapter.getCount();
			mCicleIndicator.setCurrentItem(acturalPosition);
			mTextView.setText(mPagerAdapter.getPageTitle(acturalPosition));
		}

		@Override
		public void onPageScrollStateChanged(int state)
		{

		}

	}
}
