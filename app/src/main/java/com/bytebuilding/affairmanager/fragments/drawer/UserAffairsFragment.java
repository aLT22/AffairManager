package com.bytebuilding.affairmanager.fragments.drawer;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bytebuilding.affairmanager.R;

public class UserAffairsFragment extends Fragment {

    public UserAffairsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_affairs, container, false);

        return rootView;
    }

}
