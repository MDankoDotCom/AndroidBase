package com.mdanko.toolbox.celladapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Cell<M, V extends Cell.ViewHolder<M>, C extends Cell.Controller> {
    public M model;
    public V viewholder;
    C controller;
    public Class<V> viewClass;
    public boolean isaStickyHeader;
    public int adapterViewType;

    public Cell(M model, Class<V> viewClass, C controller) {
        this.model = model;
        this.viewClass = viewClass;
        this.controller = controller;
    }

    public void bind(V holder) {
        viewholder = holder;
        viewholder.show(model);

        if (controller != null)
            viewholder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    controller.onSelected(Cell.this);
                }
            });
    }

    @Override
    public String toString() {
        return model + " -> " + viewClass.getSimpleName();
    }

    public interface Controller {
        void onSelected(Cell cell);
    }

    abstract public static class ViewHolder<M> extends RecyclerView.ViewHolder {
        // Subclasses override with a custom view
        // called with REFLECTION so appears as not referenced
        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            // inflate custom layout into view and pass to super
            super(new View(inflater.getContext()));
        }

        // as per RecyclerView.ViewHolder but not my preferred constructor
        protected ViewHolder(View view) {
            super(view);
        }

        // very generic method that each cell.viewholder needs to implement
        abstract public void show(M model);
    }
}
