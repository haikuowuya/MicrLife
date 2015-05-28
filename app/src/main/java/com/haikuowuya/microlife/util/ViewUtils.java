package com.haikuowuya.microlife.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

import java.lang.reflect.Method;

public class ViewUtils
{
	// temp variable
	private static TypedValue mTmpValue = new TypedValue();

	// This intro hides the system bars.
	@TargetApi(19)
	public static void hideSystemUI(Activity activity)
	{
		// Set the IMMERSIVE flag.
		// Set the content to appear under the system bars so that the content
		// doesn't resize when the system bars hideSelf and show.
		View decorView = activity.getWindow().getDecorView();
		decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
			| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
			| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
			| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hideSelf nav bar
			| View.SYSTEM_UI_FLAG_FULLSCREEN // hideSelf status bar
			| 0x00000800);// View.SYSTEM_UI_FLAG_IMMERSIVE
	}

	// This intro shows the system bars. It does this by removing all the flags
	// except for the ones that make the content appear under the system bars.
	@TargetApi(19)
	public static void showSystemUI(Activity activity)
	{
		View decorView = activity.getWindow().getDecorView();
		decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
			| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
			| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
	}

	/**
	 * 23      * Returns true if view's layout direction is right-to-left.
	 * 24      *
	 * 25      * @param view the View whose layout is being considered
	 * 26
	 */
	public static boolean isLayoutRtl(View view)
	{
		if (Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR1)
		{
			return view.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
		}
		else
		{
			// All layouts are LTR before JB MR1.
			return false;
		}
	}

	public static Point getScreenRawSize(Display display)
	{
		if (Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR1)
		{
			Point outPoint = new Point();
			DisplayMetrics metrics = new DisplayMetrics();
			display.getRealMetrics(metrics);
			outPoint.x = metrics.widthPixels;
			outPoint.y = metrics.heightPixels;
			return outPoint;
		}
		else
		{
			Point outPoint = new Point();
			Method mGetRawH;
			try
			{
				mGetRawH = Display.class.getMethod("getRawHeight");
				Method mGetRawW = Display.class.getMethod("getRawWidth");
				outPoint.x = (Integer) mGetRawW.invoke(display);
				outPoint.y = (Integer) mGetRawH.invoke(display);
				return outPoint;
			}
			catch (Throwable e)
			{
				return new Point(0, 0);
			}
		}
	}

	public static int getActionBarHeightInDp(Context context)
	{
		int actionBarHeight = 0;
		TypedValue tv = new TypedValue();
		final DisplayMetrics dm = context.getResources().getDisplayMetrics();
		if (Build.VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB)
		{
			if (context.getTheme().resolveAttribute(
				android.R.attr.actionBarSize, tv, true))
				actionBarHeight = (int) TypedValue.complexToFloat(tv.data);
		}
		else
		{
			tv.data = 48;
			actionBarHeight = (int) TypedValue.complexToFloat(tv.data);
		}
		return actionBarHeight;
	}

	public static int getActionBarHeight(Context context)
	{
		int actionBarHeight = 0;
		TypedValue tv = new TypedValue();
		final DisplayMetrics dm = context.getResources().getDisplayMetrics();
		if (Build.VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB)
		{
			if (context.getTheme().resolveAttribute(
				android.R.attr.actionBarSize, tv, true))
				actionBarHeight = TypedValue.complexToDimensionPixelSize(
					tv.data, dm);
		}
		else
		{
			tv.data = 48;
			actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,
				dm);
		}
		return actionBarHeight;
	}
	public static int getSystemBarHeight(Context context)
	{
		int result = 0;
		int resourceId = context.getResources().getIdentifier(
			"system_bar_height", "dimen", "android");
		if (resourceId > 0)
		{
			result = context.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

	/**
	 * 获取状态栏高度单位dp
	 * */
	public static int getStatusBarHeightInDp(Context context)
	{
		int result = 0;
		int resourceId = context.getResources().getIdentifier(
			"status_bar_height", "dimen", "android");
		if (resourceId > 0)
		{
			result = getResourceValue(context, resourceId);
		}
		return result;
	}

	/**
	 * 获取状态栏高度单位像素值
	 * */
	public static int getStatusBarHeight(Context context)
	{
		return DensityUtils
			.dpToPx(context, getStatusBarHeightInDp(context));
	}

	public static int getSystemBarHeightInDp(Context context)
	{
		int result = 0;
		int resourceId = context.getResources().getIdentifier(
			"system_bar_height", "dimen", "android");
		if (resourceId > 0)
		{
			result = getResourceValue(context, resourceId);
		}
		return result;
	}

	/**
	 * 获取资源中的数值，没有经过转换，比如dp,sp等
	 */
	public static int getResourceValue(Context context, int resId)
	{
		TypedValue value = mTmpValue;
		context.getResources().getValue(resId, value, true);
		return (int) TypedValue.complexToFloat(value.data);
	}

	/**API19以上的的设备上透明化状态栏和导航栏*/
	@TargetApi(19)
	public static void alphaStatusBarAndNavBar(Activity activity,
		int statusColor, int navColor)
	{
		// 如果不希望 APP 的内容被上拉到状态列 (Status bar) 的话，要记得在界面 (Layout) XML 档中，最外面的那层，要再加上一个属性 fitsSystemWindows为true
		if (Build.VERSION.SDK_INT > 18)
		{
			Window window = activity.getWindow();
			// 状态栏透明
			window.setFlags(0x04000000, 0x04000000);
			// 透明导航栏
			window.setFlags(0x08000000, 0x08000000);
			changeStatusNavBarColor(activity, statusColor, navColor);
		}
	}

	/**
	 * 4.4以上可以自定义状态栏和导航栏颜色
	 */
	private static void changeStatusNavBarColor(Activity activity,
		int statusColor, int navColor)
	{
		View statusBarTintView = new View(activity);
		LayoutParams statusParams = new LayoutParams(LayoutParams.MATCH_PARENT,
			ViewUtils.getStatusBarHeight(activity));
		statusParams.gravity = Gravity.TOP;
		LayoutParams navParams = new LayoutParams(LayoutParams.MATCH_PARENT,
			ViewUtils.getActionBarHeight(activity));
		navParams.gravity = Gravity.BOTTOM;
		FrameLayout decorView = (FrameLayout) activity.getWindow()
			.getDecorView();
		statusBarTintView.setLayoutParams(statusParams);
		statusBarTintView.setBackgroundColor(statusColor);
		decorView.addView(statusBarTintView);
		View mNavBarTintView = new View(activity);
		mNavBarTintView.setLayoutParams(navParams);
		mNavBarTintView.setBackgroundColor(navColor);
		decorView.addView(mNavBarTintView);
	}
}
