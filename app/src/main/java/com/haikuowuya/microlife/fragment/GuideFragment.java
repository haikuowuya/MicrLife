package com.haikuowuya.microlife.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.haikuowuya.microlife.R;
import com.haikuowuya.microlife.base.BaseFragment;

/**
 * Created by raiyi-suzhou on 2015/5/11 0011.
 */
public class GuideFragment extends BaseFragment
{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
      View view = inflater.inflate(R.layout.fragment_guide,null);
        return  view;
    }

    @Override
    public String getFragmentTitle()
    {
        return null;
    }
}
