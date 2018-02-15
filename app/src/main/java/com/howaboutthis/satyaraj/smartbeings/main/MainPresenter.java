package com.howaboutthis.satyaraj.smartbeings.main;

/*
* Responsible for handling action from  the view and updating the UI as required.
 */


import android.os.AsyncTask;
import java.util.List;

class MainPresenter implements MainContract.Presenter {

    private static MainContract.View view;
    private static int fetchRequest = 0;
    private static int searchRequest = 1;

    MainPresenter(MainContract.View view){
        MainPresenter.view = view;
    }

    @Override
    public void fetchData(String mUrl) {
        new RequestAsyncTask().execute(mUrl);  // calling the AsyncTask
    }

    @Override
    public void searchUser(String searchUrl) {
        new SearchAsyncTask().execute(searchUrl);  // calling the AsyncTask
    }

    public static class RequestAsyncTask extends AsyncTask<String, Void, List<String>> {

        @Override
        protected List<String> doInBackground(String... url) {

            return Utils.fetchNewsData(url[0],fetchRequest); // request to fetch data from the api
        }

        @Override
        protected void onPostExecute(List<String> result) {
            view.setData(result); // set result data to the view
        }
    }

    public static class SearchAsyncTask extends AsyncTask<String, Void, List<String>> {

        @Override
        protected List<String> doInBackground(String... url) {

            return Utils.fetchNewsData(url[0],searchRequest); // request to fetch data from api
        }

        @Override
        protected void onPostExecute(List<String> result) {
            view.setData(result);  // set result data to the view
        }
    }
}
