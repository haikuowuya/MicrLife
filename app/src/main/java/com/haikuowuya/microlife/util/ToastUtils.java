 package com.haikuowuya.microlife.util;

import android.app.Activity;
import android.view.ViewGroup;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class ToastUtils
{
    /**显示自定义的Toast*/
    public static void showCrouton(Activity activity, CharSequence text, ViewGroup viewGroup )
    {
        //View view = LayoutInflater.from(mHostActivity).inflate(R.layout.layout_crouton_custom_view, null);
        Crouton crouton = Crouton.makeText(activity, text, Style.ALERT, viewGroup);
        crouton.show();

    }
    /**显示自定义的Toast*/
    public static void showCrouton(Activity activity, CharSequence text,Style style,  ViewGroup viewGroup )
    {

        Crouton crouton = Crouton.makeText(activity, text, style, viewGroup);
        crouton.show();

    }
}
