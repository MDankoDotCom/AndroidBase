package com.mdanko.tools.celladapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Cell<M, C extends Cell.Controller> {
    public interface Controller {
        <T extends Cell>void onSelect(T cell);
    }

    public M model;
    protected ViewHolder viewholder;
    protected C controller;
    protected Class<? extends ViewHolder> viewClass = ViewHolder.class;
    public boolean isaStickyHeader;
    public int adapterViewType;

    public Cell(M model, C controller) {
        this.model = model;
        this.controller = controller;
    }

    public int viewType() {
        return adapterViewType;
    }

    public <V extends ViewHolder> void bind(V holder) {
        viewholder = holder;
        viewholder.bind(model);

        if (controller != null)
            viewholder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    controller.onSelect(Cell.this);
                }
            });
    }

    @Override
    public String toString() {
        return model + " -> " + viewClass.getSimpleName();
    }

    abstract public static class ViewHolder<M, C> extends RecyclerView.ViewHolder {
        // Subclasses override with a custom view
        // called with REFLECTION so appears as not referenced
        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            // inflate custom layout into view and pass to super
            super(new View(inflater.getContext()));
        }

        // as per RecyclerView.ViewHolder but not preferred constructor - use 1 above
        protected ViewHolder(View view) {
            super(view);
        }

        // very generic method that each cell.viewholder needs to implement
        abstract public void bind(M model);
    }
}
