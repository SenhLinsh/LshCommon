package com.linsh.lshapp.common.helper.view;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.linsh.protocol.Client;
import com.linsh.protocol.impl.ui.view.JsonLayoutInflater;
import com.linsh.utilseverywhere.ResourceUtils;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2018/12/05
 *    desc   :
 * </pre>
 */
public class JsonIdTitleTextItemViewProtocol implements TitleTextItemViewProtocol {

    private final View view;
    private final TextView tvTitle;
    private final TextView tvText;

    public JsonIdTitleTextItemViewProtocol(Context context) {
        this(JsonLayoutInflater.from(context).inflate(ResourceUtils.getTextFromAssets("TitleTextItem.info"), null));
    }

    public JsonIdTitleTextItemViewProtocol(View view) {
        this.view = view;
        tvTitle = (TextView) Client.ui().view().findViewById(view, "tv_title");
        tvText = (TextView) Client.ui().view().findViewById(view, "tv_text");
    }

    @Override
    public View getView() {
        return view;
    }

    @Override
    public void setTitle(CharSequence title) {
        tvTitle.setText(title);
    }

    @Override
    public void setText(CharSequence text) {
        tvText.setText(text);
    }
}
