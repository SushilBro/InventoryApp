package com.example.inventoryapp.navigation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.inventoryapp.R;

public class TestFragment extends BaseTestFragment {
    Button open_Drawer;
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.test_frag, container, false);
        open_Drawer = view.findViewById(R.id.open_Drawer);
        open_Drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestFragment.super.pefprmOpenDrawer();
            }
        });
        return view;
    }
    public void setBaseFragment(TestFragmentListener testFragmentListener){
        super.setBAseTestFragment(testFragmentListener);
    }
}
