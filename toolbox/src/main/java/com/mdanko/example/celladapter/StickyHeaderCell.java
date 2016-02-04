package com.mdanko.example.celladapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StickyHeaderCell extends Cell<String, Cell.Controller> {
    public StickyHeaderCell(String model) {
        super(model,  null); // null controller - no UI
        viewClass = ViewHolder.class;
        isaStickyHeader = true; // these cells have super powers (sticky header feature)
    }

    public static class ViewHolder extends Cell.ViewHolder<String, Cell.Controller> {
        protected TextView textView;

        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(new TextView(inflater.getContext()));
            setIsRecyclable(false);
            textView = (TextView) itemView;
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textView.setLayoutParams(lp);

            // keep background opaque
            textView.setBackgroundColor(Color.argb(0xFF, 0x21, 0x23, 0x30)); // TODO lighter_black from styles
        }

        @Override
        public void bind(String model) {
            textView.setText(model);
        }
    }
}
