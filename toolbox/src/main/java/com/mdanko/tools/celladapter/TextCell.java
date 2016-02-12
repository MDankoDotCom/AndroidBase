package com.mdanko.tools.celladapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TextCell extends Cell<String, Cell.Controller> {
    public TextCell(String model) {
        super(model,  null); // null controller - no UI
        viewClass = ViewHolder.class;
    }

    public static class ViewHolder extends Cell.ViewHolder<String, Controller> {
        protected TextView textView;

        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(new TextView(inflater.getContext()));
            textView = (TextView) itemView;
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textView.setLayoutParams(lp);
            textView.setTextColor(Color.BLUE);
        }

        @Override
        public void bind(String model) {
            textView.setText(model);
        }
    }
}
