package com.haikuowuya.microlife.base;

import android.util.SparseArray;
import android.view.View;

public class ViewHolder
{
	@SuppressWarnings("unchecked")
	public static <T extends View> T getView(View convertView, int viewId)
	{
		SparseArray<View> viewHolder = null;
		if (null != convertView.getTag())
		{
			viewHolder = (SparseArray<View>) convertView.getTag();
		}
		if (null == viewHolder)
		{
			viewHolder = new SparseArray<View>();
			convertView.setTag(viewHolder);
		}
		View childView = viewHolder.get(viewId);
		if (null == childView)
		{
			childView = convertView.findViewById(viewId);
			viewHolder.put(viewId, childView);
		}
		return (T) childView;
	}
}
