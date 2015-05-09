package prototype.feedme.cat.prototype.db;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;

/**
 * Created by AT on 2015/1/17.
 */
public abstract class SimpleCursorLoader extends AsyncTaskLoader<Cursor> {
    private Cursor Sim_cursor;
    public SimpleCursorLoader(Context context) {
        super(context);
    }

    @Override
    public abstract Cursor loadInBackground();


    @Override
    public void deliverResult(Cursor cursor) {
        if (isReset()) {
            if (cursor != null)
                cursor.close();
            return;
        }
        Cursor prev_cur = Sim_cursor;
        Sim_cursor = cursor;

        if (isStarted())
            super.deliverResult(cursor);

        if (prev_cur != null && prev_cur != cursor && !prev_cur.isClosed()) {
            prev_cur.close();
        }
     }

    @Override
    protected void onStartLoading() {
        if(Sim_cursor != null)
            deliverResult(Sim_cursor);

        if(takeContentChanged() || Sim_cursor == null)
            forceLoad();
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    public void onCanceled(Cursor cursor) {
        if(cursor != null && !cursor.isClosed())
            cursor.close();
    }

    @Override
    protected void onReset() {
        super.onReset();

        onStopLoading();
        if(Sim_cursor != null && !Sim_cursor.isClosed())
            Sim_cursor.close();
        Sim_cursor = null;
    }
}
