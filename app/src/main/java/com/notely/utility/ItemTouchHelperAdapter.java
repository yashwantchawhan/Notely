package com.notely.utility;

/**
 * Created by yashwant on 23/01/18.
 */

public interface ItemTouchHelperAdapter {
    boolean onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);
}
