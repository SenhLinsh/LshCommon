package com.linsh.lshapp.common.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.linsh.lshapp.common.R;
import com.linsh.utilseverywhere.ResourceUtils;
import com.linsh.utilseverywhere.ScreenUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by linsh on 17/2/5.
 */

public class LshPopupWindow extends PopupWindow {
    private Context context;
    private ViewGroup mLayout;

    public LshPopupWindow(Context context) {
        this(new FrameLayout(context), ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        this.context = context;
    }

    public LshPopupWindow(View contentView, int width, int height, boolean focusable) {
        super(contentView, width, height, focusable);
        mLayout = (ViewGroup) contentView;
    }

    public ListPopupWindowBuilder BuildList() {
        return new ListPopupWindowBuilder();
    }

    private abstract class BasePopupWindowBuilder<T extends BasePopupWindowBuilder> implements BasePopupWindowInterface<T> {

        public BasePopupWindowBuilder() {
            // 必须设置透明背景
            setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            initView(mLayout);
        }

        @Override
        public LshPopupWindow showAsDropDown(View anchor, int xoff, int yoff) {
            // 显示在指定的View下面
            LshPopupWindow.this.showAsDropDown(anchor, xoff, yoff);
            return LshPopupWindow.this;
        }

        public LshPopupWindow getPopupWindow() {
            return LshPopupWindow.this;
        }

        protected abstract void initView(ViewGroup rootLayout);
    }

    public class ListPopupWindowBuilder extends BasePopupWindowBuilder<ListPopupWindowBuilder>
            implements ListPopupWindowInterface<ListPopupWindowBuilder, String> {
        private LshPopupWindowAdapter mAdapter;

        @Override
        public ListPopupWindowBuilder setItems(List<String> items, LshPopupWindow.OnItemClickListener listener) {
            mAdapter.setData(items, listener);
            return this;
        }

        public ListPopupWindowBuilder setItems(String[] items, LshPopupWindow.OnItemClickListener listener) {
            List<String> list = new ArrayList<>();
            if (items != null && items.length > 0) {
                Collections.addAll(list, items);
            }
            return setItems(list, listener);
        }

        @Override
        protected void initView(ViewGroup rootLayout) {
            int dp10 = dp2px(10);
            RecyclerView recyclerView = new RecyclerView(context);
            recyclerView.setMinimumWidth(ScreenUtils.getScreenWidth() / 3);
            recyclerView.setBackgroundColor(ResourceUtils.getColor(R.color.color_theme_dark_blue));
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setPadding(dp10, 0, dp10, 0);

            mAdapter = new LshPopupWindowAdapter();
            recyclerView.setAdapter(mAdapter);
            rootLayout.addView(recyclerView);
        }

        @Override
        public LshPopupWindow showAsDropDown(View anchor, int xoff, int yoff) {
            return super.showAsDropDown(anchor, xoff, yoff);
        }
    }

    private class LshPopupWindowAdapter extends RecyclerView.Adapter implements View.OnClickListener {
        private List<String> data;
        private LshPopupWindow.OnItemClickListener mOnItemClickListener;

        private void setData(List<String> list, OnItemClickListener listener) {
            data = list;
            mOnItemClickListener = listener;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            int dp5 = dp2px(6);
            // 生成LinearLayout布局
            LinearLayout linearLayout = new LinearLayout(parent.getContext());
            linearLayout.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setGravity(Gravity.CENTER);
            // 添加TextView
            TextView textView = new TextView(parent.getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textView.setLayoutParams(params);
            textView.setPadding(dp5, dp5, dp5, dp5);
            textView.setGravity(Gravity.CENTER_VERTICAL);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            textView.setTextColor(Color.WHITE);
            textView.setGravity(Gravity.CENTER);
            linearLayout.addView(textView);
            // 添加分割线
            if (viewType != 2) {
                View line = new View(parent.getContext());
                line.setTag("line");
                line.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
                line.setBackgroundColor(Color.parseColor("#D9D9D9"));
                linearLayout.addView(line);
            }
            return new RecyclerView.ViewHolder(linearLayout) {
            };
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            LinearLayout layout = (LinearLayout) holder.itemView;
            TextView textView = (TextView) layout.getChildAt(0);
            textView.setText(data.get(position));
            layout.setTag(position);
            layout.setOnClickListener(this);
            // 去掉最后一行的底线
        }

        @Override
        public int getItemCount() {
            return data == null ? 0 : data.size();
        }

        @Override
        public int getItemViewType(int position) {
            if (position < getItemCount() - 1) {
                return 1;
            }
            return 2;
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                int position = (int) v.getTag();
                mOnItemClickListener.onClick(LshPopupWindow.this, position);
            }
        }
    }

    private interface BasePopupWindowInterface<T extends BasePopupWindowBuilder> {
        LshPopupWindow showAsDropDown(View anchor, int xoff, int yoff);
    }

    private interface ListPopupWindowInterface<T extends ListPopupWindowBuilder, S> extends BasePopupWindowInterface<T> {
        T setItems(List<S> items, LshPopupWindow.OnItemClickListener listener);
    }

    public interface OnItemClickListener {
        void onClick(LshPopupWindow window, int index);
    }

    private int dp2px(float dpValue) {
        final float scale = mLayout.getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public int getWidth() {
        int width = super.getWidth();
        if (width <= 0) {
            mLayout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            width = mLayout.getMeasuredWidth();
        }
        return width;
    }

    @Override
    public int getHeight() {
        int height = super.getHeight();
        if (height <= 0) {
            mLayout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            height = mLayout.getMeasuredHeight();
        }
        return height;
    }
}
