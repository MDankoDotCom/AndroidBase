package com.mdanko.toolbox.celladapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HeaderCell extends Cell <String, HeaderCell.ViewHolder, Cell.Controller> {
    public HeaderCell(String model) {
        super(model, HeaderCell.ViewHolder.class, null); // null controller - no UI
        isaStickyHeader = true; // these cells have super powers (sticky header feature)
    }

    public static class ViewHolder extends Cell.ViewHolder<String> {
        protected TextView textView;
        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(new TextView(inflater.getContext()));
            setIsRecyclable(false);
            textView = (TextView) itemView;

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textView.setLayoutParams(lp);
            textView.setBackgroundColor(Color.BLACK);
        }

        @Override
        public void show(String model) {
            textView.setText(model);
        }
    }
}
