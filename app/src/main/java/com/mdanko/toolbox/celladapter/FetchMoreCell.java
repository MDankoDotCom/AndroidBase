package com.mdanko.toolbox.celladapter;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

public class FetchMoreCell extends Cell<String, FetchMoreCell.ViewHolder, FetchMoreCell.Controller> {
    public interface Controller extends Cell.Controller {
        void onFetchMore();
    }

    public FetchMoreCell(String model, FetchMoreCell.Controller controller) {
        super(model, ViewHolder.class, controller);
    }

    @Override
    public void bind(FetchMoreCell.ViewHolder holder) {
        super.bind(holder);
        if (controller != null) {
            controller.onFetchMore();
        }
    }

    public static class ViewHolder extends Cell.ViewHolder<String> {
        public ProgressBar progressBar;

        // Subclasses override with a custom view
        // called with REFLECTION so appears as not referenced
        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(new ProgressBar(inflater.getContext()));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.CENTER;
            itemView.setLayoutParams(lp);
            progressBar = (ProgressBar)itemView;
        }

        @Override
        public void show(String model) {
            String moreKey = model;
            progressBar.setVisibility(moreKey == null ? View.INVISIBLE : View.VISIBLE);
        }
    }
}
