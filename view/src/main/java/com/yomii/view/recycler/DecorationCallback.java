package com.yomii.view.recycler;

public interface DecorationCallback {
    long getGroupId(int position);

    String getGroupFirstLine(int position);
}