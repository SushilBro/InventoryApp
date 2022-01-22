package com.example.inventoryapp.navigation;

import androidx.fragment.app.Fragment;

public class BaseTestFragment extends Fragment {
    TestFragmentListener testFragmentListener;

    public void setBAseTestFragment(TestFragmentListener testFragmentListener) {
        this.testFragmentListener = testFragmentListener;
    }

    public void pefprmOpenDrawer() {
        testFragmentListener.openDrawer();
    }

    public interface TestFragmentListener {
        void openDrawer();
    }
}
