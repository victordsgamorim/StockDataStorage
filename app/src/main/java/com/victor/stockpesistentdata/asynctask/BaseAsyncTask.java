package com.victor.stockpesistentdata.asynctask;

import android.os.AsyncTask;

public class BaseAsyncTask<T> extends AsyncTask<Void, Void, T> {

    private final ExecuteListener<T> executeListener;
    private final FinishListener<T> finishListener;

    public BaseAsyncTask(ExecuteListener<T> executeListener, FinishListener<T> finishListener) {
        this.executeListener = executeListener;
        this.finishListener = finishListener;
    }

    @Override
    protected T doInBackground(Void... voids) {
        return executeListener.onExecute();
    }

    @Override
    protected void onPostExecute(T result) {
        super.onPostExecute(result);
        finishListener.onFinish(result);
    }

    public interface ExecuteListener<T> {
        T onExecute();
    }

    public interface FinishListener<T> {
        void onFinish(T result);
    }
}
