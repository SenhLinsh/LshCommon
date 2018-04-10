package com.linsh.lshapp.common.base;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.linsh.base.BaseContract;


/**
 * Created by Senh Linsh on 17/4/24.
 */

public abstract class BaseViewFragment extends BaseFragment implements BaseContract.View {

    @Override
    public Context getContext() {
        return getActivity().getApplicationContext();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
