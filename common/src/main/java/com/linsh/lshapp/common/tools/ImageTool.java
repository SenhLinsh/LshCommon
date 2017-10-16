package com.linsh.lshapp.common.tools;

import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.linsh.lshutils.utils.Basic.LshApplicationUtils;
import com.linsh.lshutils.utils.Basic.LshStringUtils;

/**
 * Created by Senh Linsh on 17/4/25.
 */

public class ImageTool {

    public static RequestManager getGlide() {
        return Glide.with(LshApplicationUtils.getContext());
    }

    public static void setImage(ImageView imageView, String url) {
        setImage(imageView, url, 0, 0);
    }

    public static void setImage(ImageView imageView, String url, int placeholder, int error) {
        if (imageView == null) return;
        if (LshStringUtils.isEmpty(url)) {
            if (error > 0) setImage(imageView, error);
            return;
        }

        try {
            DrawableRequestBuilder<String> builder =
                    Glide.with(LshApplicationUtils.getContext())
                            .load(url)
                            .dontAnimate();
            if (placeholder > 0) {
                builder.placeholder(placeholder);
            }
            if (error > 0) {
                builder.error(error);
            }
            builder.into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
            setImage(imageView, error);
        }
    }

    public static void setImage(ImageView imageView, int res) {
        Glide.with(LshApplicationUtils.getContext()).load(res).into(imageView);
    }

    public static void setImage(ImageView imageView, Uri uri) {
        Glide.with(LshApplicationUtils.getContext()).load(uri).into(imageView);
    }
}
