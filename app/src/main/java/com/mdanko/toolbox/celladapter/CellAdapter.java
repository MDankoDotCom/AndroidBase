package com.mdanko.toolbox.celladapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CellAdapter extends RecyclerView.Adapter {
    protected LayoutInflater inflater; // needed to inflate cells
    protected List<Cell> adapterItems = new CopyOnWriteArrayList<>(); // to allow async list removal (clearing)
    protected FetchMoreCell fetchMoreCell;

    public CellAdapter(Context context) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setItems(List<? extends Cell> items) {
        adapterItems.clear();
        adapterItems.addAll(items);
        if (fetchMoreCell != null)
            adapterItems.add(fetchMoreCell);
        notifyDataSetChanged();
    }

    public void setFetchMore(FetchMoreCell cell) {
        fetchMoreCell = cell;
        adapterItems.add(cell);
    }

    public void addItems(List<? extends Cell> more) {
        if (fetchMoreCell != null) {
            int i = adapterItems.size() - 1;
            adapterItems.addAll(i, more);
        } else {
            adapterItems.addAll(more);
        }
        notifyDataSetChanged();
    }

    public <T extends Cell> void addItem(T item) {
        int i = adapterItems.size() - 1;
        if (fetchMoreCell != null)
            --i;
        adapterItems.add(i, item);
        notifyItemInserted(i);
    }

    public <T extends Cell>void update(T item) {
        int pos = adapterItems.indexOf(item);
        if (pos >= 0) {
            notifyItemChanged(pos);
        }
    }

    public <T extends Cell>void remove(T item) {
        int pos = adapterItems.indexOf(item);
        if (pos >= 0) {
            adapterItems.remove(pos);
            notifyItemRemoved(pos);

            // if last item under sticky header then remove header too
            if (pos > 0 && pos < adapterItems.size()-1) {
                Cell prev = adapterItems.get(pos-1);
                Cell cell = adapterItems.get(pos);
                if (cell.isaStickyHeader && prev.isaStickyHeader) {
                    adapterItems.remove(pos-1);
                    notifyItemRemoved(pos-1);
                }
            }
        }
    }
    public void clearItems() {
        adapterItems.clear();
        if (fetchMoreCell != null)
            adapterItems.add(fetchMoreCell);
        notifyDataSetChanged();
    }

    public <T extends Cell> T getCell(int position) {
        return (T) adapterItems.get(position);
    }

    @Override
    public int getItemCount() {
        return adapterItems.size();
    }

    // dynamic itemViewType index
    @Override
    public int getItemViewType(int position) {
        Cell item = adapterItems.get(position);
        int i = cellViewType.indexOf(item.viewClass);
        if (i == -1) { // not found
            i = cellViewType.size();
            cellViewType.add(item.viewClass);
        }
        return item.adapterViewType = i;
    }

    List<Class<? extends RecyclerView.ViewHolder>> cellViewType = new ArrayList<>();
    private static int CNT; // count them for debugging

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i("MDanko", "creating new celladapter viewholder " + (++CNT));

        // construct a viewholder from only its class
        // ie. return new aViewHolder(inflater, parent);

        Class<? extends RecyclerView.ViewHolder> cls = cellViewType.get(viewType);
        try {
            Class[] argTypes = {LayoutInflater.class, ViewGroup.class};
            Constructor constructor = cls.getDeclaredConstructor(argTypes);
            Object[] args = {inflater, parent};
            Object obj = constructor.newInstance(args);
            return (Cell.ViewHolder) obj;
        } catch (Exception ex) {
            throw new RuntimeException(ex); // that didn't go as expected ??
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Cell cell = adapterItems.get(position);
        cell.bind((Cell.ViewHolder) holder);
        setDivider(holder, cell, position);
    }

    protected void setDivider(RecyclerView.ViewHolder holder, Cell cell, int position) {
//        Rect whichDivider = partialDivider;
//        int tag = R.id.cell_adapter_divider;
//        holder.itemView.setTag(tag, whichDivider);
    }
}
