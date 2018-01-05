package com.yomii.core;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yomii.base.BaseActivity;
import com.yomii.core.adapter.QuickRecyclerAdapter;
import com.yomii.view.recycler.DecorationCallback;
import com.yomii.view.recycler.PinnedSectionDecoration;
import com.yomii.view.recycler.SimpleDividerDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by Yomii on 2017/4/20.
 *
 * RecyclerView的Decoration演示
 */

public class RecyclerDecorationActivity extends BaseActivity {

    private ArrayList<String> strings;
    private QuickRecyclerAdapter<String> decorationAdapter;

    @Override
    protected void initView() {
        setContentView(R.layout.recycler_decoration_act);

        RecyclerView recyclerView = findView(R.id.recycler);

        decorationAdapter = new QuickRecyclerAdapter<String>() {

                    @Override
                    protected int getLayoutRes(int viewType) {
                        return android.R.layout.simple_list_item_1;
                    }

                    @Override
                    protected void convertView(VH holder, String s, int position) {
                        holder.setText(android.R.id.text1, s);
                    }
                };
        recyclerView.setAdapter(decorationAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        int dividerColor = ContextCompat.getColor(this, R.color.normal_bg);
        recyclerView.addItemDecoration(new SimpleDividerDecoration(this, dividerColor));
        recyclerView.addItemDecoration(new PinnedSectionDecoration(this, new DecorationCallback() {
            @Override
            public long getGroupId(int position) {
                return Character.toUpperCase(strings.get(position).charAt(0));
            }

            @Override
            public String getGroupFirstLine(int position) {
                return strings.get(position).substring(0, 1).toUpperCase();
            }
        }));

    }

    @NonNull
    private ArrayList<String> randomStrings() {
        final ArrayList<String> strings = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < 50; i++) {
            char[] chars = new char[1 + random.nextInt(5)];
            for (int j = 0; j < chars.length; j++) {
                chars[j] = (char) (97 + random.nextInt(25));
            }
            strings.add(String.valueOf(chars));
        }
        Collections.sort(strings);
        return strings;
    }

    @Override
    protected void initData() {
        strings = randomStrings();
        decorationAdapter.setDataList(strings);
    }
}
