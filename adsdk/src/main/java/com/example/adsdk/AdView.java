package com.example.adsdk;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;



@Keep
public class AdView extends FrameLayout {
    public AdView(@NonNull Context context) {
        super(context);
        init();
    }


    public AdView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AdView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }
    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_ad_view,this);
    }


}
