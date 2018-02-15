package com.howaboutthis.satyaraj.smartbeings.main;

import android.app.SearchManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.howaboutthis.satyaraj.smartbeings.R;

import java.util.List;

/*
* Displays the Main Screen
*/

public class MainActivity extends AppCompatActivity implements MainContract.View {

    private MainPresenter mainPresenter;
    private String mUrl = "https://api.github.com/users";

    ListView listView;
    TextView emptyResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainPresenter = new MainPresenter(this);
        listView = findViewById(R.id.list_view);
        emptyResult = findViewById(R.id.no_result_text);

        mainPresenter.fetchData(mUrl); //Fetching the data
    }

    @Override
    public void setData(List<String> data) {
        if (data != null) {
            emptyResult.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, data);
            listView.setAdapter(adapter);
        }
        else {
            listView.setVisibility(View.GONE);   // Hide ListView on empty result
            emptyResult.setVisibility(View.VISIBLE); // Display a message EMPTY RESULT
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.main_menu, menu);

        final SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        if (null != searchView) {
            //noinspection ConstantConditions
            searchView.setSearchableInfo(searchManager
                    .getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(false);
        }

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {
                return true;
            }

            public boolean onQueryTextSubmit(String query) {
                String searchUrl = "https://api.github.com/search/users?q="+ query;
                mainPresenter.searchUser(searchUrl);  //fetching the user
                return true;
            }

        };

        if (searchView != null) {
            searchView.setOnQueryTextListener(queryTextListener);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

}
