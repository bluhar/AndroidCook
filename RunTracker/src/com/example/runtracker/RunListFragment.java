package com.example.runtracker;

import com.example.runtracker.RunDatabaseHelper.RunCursor;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class RunListFragment extends ListFragment implements LoaderCallbacks<Cursor>{
    
    private static final int REQUEST_NEW_RUN = 0;

//    private RunCursor mCursor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setHasOptionsMenu(true);
        
        //这种将cursor的加载与关闭放在onCreate和onDestroy方法中是不合适的，这些方法是主线程，数据库查询不应该放在主线程中，应该使用Loader
//        mCursor = RunManager.getInstance(getActivity()).queryRuns();
//        RunCursorAdapter adapter = new RunCursorAdapter(getActivity(), mCursor);
//        setListAdapter(adapter);
        //Initialize the loader to load the list of runs
        getLoaderManager().initLoader(0, null, this);
    }

//    @Override
//    public void onDestroy() {
//        mCursor.close();
//        super.onDestroy();
//    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.run_list_options, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        
        switch (item.getItemId()) {
            case R.id.menu_item_new_run:
                Intent i = new Intent(getActivity(), RunActivity.class);
                startActivityForResult(i, REQUEST_NEW_RUN);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        
    }
    
    /**
     * 第三个参数id，正好是run对象的id属性，why???
     */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        
        Intent i = new Intent(getActivity(), RunActivity.class);
        i.putExtra(RunActivity.EXTRA_RUN_ID, id);
        startActivity(i);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_NEW_RUN) {
//            mCursor.requery();
//            ((RunCursorAdapter)getListAdapter()).notifyDataSetChanged();
            //Restart the loader to get any new run available
            getLoaderManager().restartLoader(0, null, this);
        }
        
    }
    
    
    
    @Override
    public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
        return new RunListCursorLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
        //Create an adapter to point at this cursor
        RunCursorAdapter adapter = new RunCursorAdapter(getActivity(), (RunCursor)cursor);
        setListAdapter(adapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> arg0) {
        setListAdapter(null);
    }





    private static class RunCursorAdapter extends CursorAdapter {

        private RunCursor mRunCursor;

        public RunCursorAdapter(Context context, RunCursor c) {
            super(context, c, 0);
            mRunCursor = c;
        }

        //bindView方法中使用的view来自newView方法的返回值
        @Override
        public void bindView(View view, Context ctx, Cursor c) {

            Run run = mRunCursor.getRun();
            TextView startDateTextView = (TextView) view;
            String cellText = ctx.getString(R.string.cell_text, run.getStartDate());

            startDateTextView.setText(cellText);
        }

        @Override
        public View newView(Context ctx, Cursor c, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            return inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        }

    }
    
    private static class RunListCursorLoader extends SQLiteCursorLoader {

        public RunListCursorLoader(Context context) {
            super(context);
        }

        @Override
        protected Cursor loadCursor() {
            return RunManager.getInstance(getContext()).queryRuns();
        }
        
    }



}
