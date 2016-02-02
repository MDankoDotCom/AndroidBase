package com.mdanko.toolbox.celladapter;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * View for {@link CellAdapter} - see documentation there.
 * "stick" previous headers to the top of the recycler view as the user scrolls.
 *      Only works for vertical lists, a {@link LinearLayoutManager} is set internally.
 * </p>
 * NOTE: The smooth translation of headers displacing each other only works if the header height is
 * equal to or less than the size the of normal list items.
 */
public class StickyHeaderAdapterView extends RecyclerView {
    private static final String LOG_TAG = StickyHeaderAdapterView.class.getSimpleName();

    private boolean stickyHeadersEnabled = true;
    private CellAdapter adapter;
    private View drawStickyHeader;
    private int drawStickyShiftY;

    public StickyHeaderAdapterView(Context context) {
        super(context);
        init();
    }

    public StickyHeaderAdapterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StickyHeaderAdapterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        if (!(adapter instanceof CellAdapter)) {
            throw new RuntimeException("StickyHeaderRecyclerView must be used with a StickyAdapter");
        }
        super.setAdapter(adapter);
        this.adapter = (CellAdapter) adapter;
        adapter.registerAdapterDataObserver(adapter_observer);
    }

    public void setStickyHeadersEnabled(boolean sticky_headers_enabled) {
        this.stickyHeadersEnabled = sticky_headers_enabled;
    }

    private void init() {
        setLayoutManager(new LinearLayoutManager(getContext(), VERTICAL, false));
        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recycler_view, int dx, int dy) {
                if (stickyHeadersEnabled)
                    updateStickyHeader();
            }
        });
    }

    private void updateStickyHeader() {
        View newStickyHeader = chooseHeader();
        if (newStickyHeader != null)
            refreshHeader(newStickyHeader);
    }

    private View chooseHeader() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();
        // Iterate backwards to find the previous header
        for (int i = layoutManager.findFirstVisibleItemPosition(); i > NO_POSITION; i--) {
            Cell cell = adapter.getCell(i);
            if (cell.isaStickyHeader) {
//                Log.d(LOG_TAG, "use sticky header @ " + i + " of " + cell.model);
                return cell.viewholder.itemView;
            }
        }
        Log.w(LOG_TAG, "no sticky header found");
        return null;
    }

    private void refreshHeader(View newStickyHeader) {
        int newStickyShiftY = getStickyShiftY(newStickyHeader);
        if (drawStickyHeader != newStickyHeader || drawStickyShiftY != newStickyShiftY) { // changed since last draw ?
            drawStickyHeader = newStickyHeader;
            drawStickyShiftY = newStickyShiftY;
            invalidate();
        }
    }

    private int getStickyShiftY(View newStickyHeader) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

        int nextVisibleItem = firstVisibleItemPosition + 1;
        Cell cell = adapter.getCell(nextVisibleItem);
        if (cell != null && cell.isaStickyHeader) {
            // when another header enters the sticky header zone then they share the space by shifting
            View child0 = getChildAt(0);
            int firstChildHeight = child0.getHeight();
            int firstChildTop = child0.getTop();
            int stickyHeaderHeight = newStickyHeader.getHeight();

            // FIXME: Only works if the header is the same size or smaller than the normal list rows
            if (firstChildHeight >= stickyHeaderHeight && firstChildHeight + firstChildTop < stickyHeaderHeight) {
                return firstChildTop + firstChildHeight - stickyHeaderHeight;
            }
        }
        return 0;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas); // draw regular view

        // then superimpose sticky header
        if (stickyHeadersEnabled) {
            if (drawStickyHeader != null) {
                canvas.save();
                canvas.translate(0, drawStickyShiftY);
                drawStickyHeader.draw(canvas);
                canvas.restore();
            } else {
                Log.e(LOG_TAG, "no sticky header to draw.");
            }
        }
    }

    private final AdapterDataObserver adapter_observer = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();
            layoutManager.scrollToPosition(0);
        }
    };
}
