package com.linsh.lshapp.common.subscribe;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;

import com.linsh.base.activity.ActivitySubscribe;
import com.linsh.lshutils.utils.IdUtilsEx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2019/12/02
 *    desc   : 辅助创建 OptionMenu 的 Act 订阅者, 内部封装了对 onCreateOptionsMenu 和 OnOptionsItemSelected 回调的处理.
 * </pre>
 */
public class OptionMenuSubscriber implements ActivitySubscribe.OnCreateOptionsMenu, ActivitySubscribe.OnOptionsItemSelected {

    private Activity activity;
    private Menu menu;
    private List<ItemInfo> additions = new ArrayList<>();
    private HashMap<Integer, ItemInfo> subscribeListeners = new HashMap<>();

    @Override
    public void attach(Activity activity) {
        this.activity = activity;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        for (ItemInfo itemInfo : additions) {
            addItem(menu, itemInfo);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (subscribeListeners.size() > 0) {
            ItemInfo itemInfo = subscribeListeners.get(item.hashCode());
            if (itemInfo != null && itemInfo.listener != null) {
                itemInfo.listener.onItemSelected(item);
                return true;
            }
        }
        return false;
    }

    public OptionMenuSubscriber addItem(String title) {
        return addItem(title, null);
    }

    public OptionMenuSubscriber addItem(String title, onItemSelectedListener listener) {
        return addItem(title, 0, 0, 0, listener);
    }

    public OptionMenuSubscriber addItem(String title, int itemId, int groupId, int order) {
        return addItem(title, itemId, groupId, order, null);
    }

    public OptionMenuSubscriber addItem(String title, int itemId, int groupId, int order, onItemSelectedListener listener) {
        ItemInfo itemInfo = new ItemInfo(title, itemId, groupId, order, listener);
        if (menu == null) {
            additions.add(itemInfo);
        } else {
            addItem(menu, itemInfo);
        }
        return this;
    }

    private void addItem(Menu menu, ItemInfo itemInfo) {
        MenuItem item = menu.add(itemInfo.groupId, itemInfo.itemId, itemInfo.order, itemInfo.title);
        if (itemInfo.listener != null) {
            subscribeListeners.put(item.hashCode(), itemInfo);
        }
    }

    public OptionMenuSubscriber invalidateOptionsMenu() {
        activity.invalidateOptionsMenu();
        return this;
    }

    public interface onItemSelectedListener {
        void onItemSelected(MenuItem item);
    }

    private static class ItemInfo {
        private String title;
        private int itemId;
        private int groupId;
        private int order;
        private onItemSelectedListener listener;

        public ItemInfo(String title, int itemId, int groupId, int order, onItemSelectedListener listener) {
            this.title = title;
            if (itemId == 0) {
                this.itemId = IdUtilsEx.generateId();
            } else {
                this.itemId = itemId;
            }
            this.groupId = groupId;
            this.order = order;
            this.listener = listener;
        }
    }
}
