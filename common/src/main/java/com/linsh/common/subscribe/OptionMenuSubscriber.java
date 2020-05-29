package com.linsh.common.subscribe;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;

import com.linsh.base.activity.ActivitySubscribe;

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

    public OptionMenuSubscriber addItem(String title, int itemId, int groupId, int order, onItemSelectedListener listener) {
        return addItem(new ItemInfo(title, itemId, groupId, order, listener));
    }

    public OptionMenuSubscriber addItem(ItemInfo itemInfo) {
        if (menu == null) {
            additions.add(itemInfo);
        } else {
            addItem(menu, itemInfo);
        }
        return this;
    }

    private void addItem(Menu menu, ItemInfo itemInfo) {
        MenuItem item = menu.add(itemInfo.groupId, itemInfo.itemId, itemInfo.order, itemInfo.title);
        item.setShowAsAction(itemInfo.showAsAction);
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

    public static class ItemInfo {
        private String title;
        private int itemId;
        private int groupId;
        private int order;
        private int showAsAction;
        private onItemSelectedListener listener;

        public ItemInfo(String title) {
            this.title = title;
        }

        public ItemInfo(String title, onItemSelectedListener listener) {
            this.title = title;
            this.listener = listener;
        }

        public ItemInfo(String title, int itemId, int groupId, int order, onItemSelectedListener listener) {
            this.title = title;
            this.itemId = itemId;
            this.groupId = groupId;
            this.order = order;
            this.listener = listener;
        }

        public ItemInfo setTitle(String title) {
            this.title = title;
            return this;
        }

        public ItemInfo setItemId(int itemId) {
            this.itemId = itemId;
            return this;
        }

        public ItemInfo setGroupId(int groupId) {
            this.groupId = groupId;
            return this;
        }

        public ItemInfo setOrder(int order) {
            this.order = order;
            return this;
        }

        public ItemInfo setShowAsAction(int showAsAction) {
            this.showAsAction = showAsAction;
            return this;
        }

        public ItemInfo setListener(onItemSelectedListener listener) {
            this.listener = listener;
            return this;
        }
    }
}
