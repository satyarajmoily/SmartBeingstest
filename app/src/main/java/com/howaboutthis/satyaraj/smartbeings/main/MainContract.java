package com.howaboutthis.satyaraj.smartbeings.main;

/*
 *Defines the contract between the view {@link MainActivity} and the Presenter {@MainPresenter}.
 */

import java.util.List;

public interface MainContract {

    interface View{
        void setData(List<String> data);  // setting the data into the view

    }

    interface Presenter{
        void fetchData(String mUrl);  // fetching all the user data from the api

        void searchUser(String searchUrl); // searching specific user from the api

    }
}
