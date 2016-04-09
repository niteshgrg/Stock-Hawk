package com.sam_chordas.android.stockhawk.ui;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.db.chart.model.LineSet;
import com.db.chart.view.LineChartView;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;

/**
 * Created by niteshgarg on 09/04/16.
 */
public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = DetailActivity.class.getSimpleName();
    private static final int CURSOR_LOADER_ID = 0;
    private LineSet mLineSet;
    private LineChartView mLineChart;
    private Cursor mCursor;
    private Bundle args = new Bundle();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_graph);
        mLineSet = new LineSet();
        mLineChart = (LineChartView) findViewById(R.id.linechart);

        Intent intent = getIntent();
        Bundle args = new Bundle();
        args.putString("symbol", intent.getStringExtra("symbol"));
        Log.e(LOG_TAG, args.getString("symbol"));
        getLoaderManager().initLoader(CURSOR_LOADER_ID, args, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(CURSOR_LOADER_ID, args, this);
    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, QuoteProvider.Quotes.CONTENT_URI,
                new String[]{ QuoteColumns.BIDPRICE },
                QuoteColumns.SYMBOL + " = ?",
                new String[]{args.getString("symbol")},
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursor = data;
        mCursor.moveToFirst();
        int i = 1;
        while (mCursor.moveToNext()){
            float price = Float.parseFloat(mCursor.getString(mCursor.getColumnIndex(QuoteColumns.BIDPRICE)));
            mLineSet.addPoint("test " + i, price);
            i++;
        }

        mLineChart.addData(mLineSet);
        mLineChart.show();

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}