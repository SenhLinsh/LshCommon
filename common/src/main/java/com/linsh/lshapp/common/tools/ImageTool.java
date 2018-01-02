package com.linsh.lshapp.common.tools;

import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.linsh.utilseverywhere.ContextUtils;
import com.linsh.utilseverywhere.StringUtils;

/**
 * Created by Senh Linsh on 17/4/25.
 */

public class ImageTool {

    public static RequestManager getGlide() {
        return Glide.with(ContextUtils.get());
    }

    public static void setImage(ImageView imageView, String url) {
        setImage(imageView, url, 0, 0);
    }

    public static void setImage(ImageView imageView, String url, int placeholder, int error) {
        if (imageView == null) return;
        if (StringUtils.isEmpty(url)) {
            if (error > 0) setImage(imageView, error);
            return;
        }

        try {
            DrawableRequestBuilder<String> builder =
                    Glide.with(ContextUtils.get())
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
        Glide.with(ContextUtils.get()).load(res).into(imageView);
    }

    public static void setImage(ImageView imageView, Uri uri) {
        Glide.with(ContextUtils.get()).load(uri).into(imageView);
    }
}
