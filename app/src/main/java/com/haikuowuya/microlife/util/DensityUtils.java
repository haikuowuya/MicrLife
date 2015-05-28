package com.haikuowuya.microlife.util;

import android.content.Context;

public class DensityUtils
{
	/**将像素转换为对应设备的density*/
	public static int pixelsToDp(Context context, int pixels)
	{
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) ((pixels - 0.5f) / scale);
	}
	public static float getDensity(Context context)
	{
		return context.getResources().getDisplayMetrics().density;
	}
	/**将dp值转换为对应的像素值*/
	public static int dpToPx(Context context, float dp)
	{
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}
	/**将sp值转换为对应的像素值，主要用于TextView的字体中*/
	public static int spToPx(Context context, int sp)
	{
		 return (int) (sp * context.getResources().getDisplayMetrics().scaledDensity);
	}
	/**将像素值值转换为对应的sp值，主要用于TextView的字体中*/
	public static int pxToSp(Context context, int pixels)
	{
		return (int) (pixels / context.getResources().getDisplayMetrics().scaledDensity);
	}
	public static int getScreenWidthInPx(Context context)
	{
		return  context.getResources().getDisplayMetrics().widthPixels;
	}
}
