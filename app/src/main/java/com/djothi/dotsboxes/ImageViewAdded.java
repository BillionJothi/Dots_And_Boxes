package com.djothi.dotsboxes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.Nullable;

@SuppressLint("AppCompatCustomView")
class ImageViewAdded extends ImageView {
    boolean set = false;
    int player;

    public ImageViewAdded(Context context, ImageView imageView) {
        super(context);
        }
    public ImageViewAdded(Context context) {
        super(context);
    }
    public boolean isSet() {
        return set;
    }
    public void setSet(boolean set) {
        this.set = set;
    }
    public int getPlayer() { return player; }
    public void setPlayer(int player) { this.player = player; }


}
