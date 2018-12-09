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
public class JsonIdTextItemViewProtocol implements TextItemViewProtocol {

    private final View view;
    private final TextView tvText;

    public JsonIdTextItemViewProtocol(Context context) {
        this(JsonLayoutInflater.from(context).inflate(ResourceUtils.getTextFromAssets("TextItem.info"), null));
    }

    public JsonIdTextItemViewProtocol(View view) {
        this.view = view;
        tvText = (TextView) Client.ui().view().findViewById(view, "tv_text");
    }

    @Override
    public void setText(CharSequence text) {
        tvText.setText(text);
    }

    @Override
    public View getView() {
        return view;
    }
}
