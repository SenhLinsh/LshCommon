package com.linsh.lshapp.common;

import android.os.Bundle;

import com.linsh.lshapp.common.base.BaseActivity;
import com.linsh.lshapp.common.helper.view.TitleTextItemViewProtocol;
import com.linsh.protocol.Client;
import com.linsh.protocol.ui.layout.DataSetViewProtocol;
import com.linsh.protocol.ui.layout.ListViewProtocol;
import com.linsh.protocol.ui.layout.OnItemClickListener;
import com.linsh.protocol.ui.view.ViewProtocol;

public class MainActivity extends BaseActivity {

    private ListViewProtocol<String> protocol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        protocol = Client.ui().view().getProtocol(this, ListViewProtocol.class);
        setContentView(protocol.getView());
        protocol.addItemView(TitleTextItemViewProtocol.class, (dataSet, itemView, position) -> {
            itemView.setTitle("Title");
            itemView.setText(dataSet.getItemData(position));
        });
        protocol.setOnItemClickListener(new OnItemClickListener<String>() {
            @Override
            public void onItemClick(DataSetViewProtocol<String> dataSet, ViewProtocol itemView, int position) {
                Client.ui().toast().show(dataSet.getItemData(position));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        protocol.setData(new String[]{"Item1", "Item2", "Item3", "Item4"});
    }
}