package com.block.memo.model;

import java.util.ArrayList;

public class MemoList {

    private String result;
    private ArrayList<Memo> items;
    private int count;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public ArrayList<Memo> getItems() {
        return items;
    }

    public void setItems(ArrayList<Memo> items) {
        this.items = items;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
