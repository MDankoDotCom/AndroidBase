package com.mdanko.tools;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.mdanko.tools.celladapter.Cell;
import com.mdanko.tools.celladapter.CellAdapter;
import com.mdanko.tools.celladapter.FetchMoreCell;
import com.mdanko.tools.celladapter.TextCell;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    CellAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        setupToolbar();
        setupAdapter();
    }

    private void setupToolbar() {
    }

    private void setupAdapter() {
        adapter = new CellAdapter(this);
        RecyclerView rvView = (RecyclerView) findViewById(R.id.rvList);
        rvView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvView.setAdapter(adapter);

        List<Cell> cells = new ArrayList<>();
        for (int i = 0; i< 20; ++i) {
            cells.add(new TextCell("TEST " + i));
        }
        cells.add(new FetchMoreCell("", new FetchMoreCell.Controller() {
            @Override
            public <T extends Cell> void onSelect(T cell) {

                
            }

            @Override
            public void onFetchMore(Cell cell) {
                Log.i("MDanko", "fetching more");
            }
        }));
        adapter.addItems(cells);
    }
}
