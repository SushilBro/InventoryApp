package com.example.inventoryapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.inventoryapp.avilable_goods.model.AvilableGoods;
import com.example.inventoryapp.navigation.BaseTestFragment;
import com.example.inventoryapp.navigation.GraphActivity;
import com.example.inventoryapp.navigation.Settings;
import com.example.inventoryapp.navigation.TestFragment;

import java.util.ArrayList;
import java.util.Arrays;

import nl.psdcompany.duonavigationdrawer.views.DuoDrawerLayout;
import nl.psdcompany.duonavigationdrawer.views.DuoMenuView;
import nl.psdcompany.duonavigationdrawer.widgets.DuoDrawerToggle;

public class HomePage extends AppCompatActivity implements DuoMenuView.OnMenuClickListener {
    private MenuAdapter mMenuAdapter;
    private ViewHolder mViewHolder;
    private LinearLayout btnAddProdut;
    private LinearLayout btnSellProduct;
    private LinearLayout btnAvilableGoods;
    private LinearLayout btnViewProgress;


    private ArrayList<String> mTitles = new ArrayList<>();
    private FragmentManager fragmentManager;
    private TestFragment testFragment;
    private Settings settings;
    public static String TEST_FRAGMENT_TAG = "frg_test";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        mTitles = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.menuOptions)));
        btnAddProdut = (LinearLayout) findViewById(R.id.btn_addProduct);
        btnSellProduct = (LinearLayout) findViewById(R.id.sellProduct);
        btnAvilableGoods = (LinearLayout) findViewById(R.id.avilableGoods);
        btnViewProgress = (LinearLayout) findViewById(R.id.viewProgress);

        fragmentManager = getSupportFragmentManager();
        testFragment = new TestFragment();
        settings=new Settings();
        // Initialize the views
        mViewHolder = new ViewHolder();
        // Handle toolbar actions
        handleToolbar();
        // Handle menu actions
        handleMenu();
        // Handle drawer actions
        handleDrawer();
        //ButtonClicked method calling
        buttonClicked();

        mMenuAdapter.setViewSelected(0, true);
        setTitle(mTitles.get(0));
        testFragment.setBaseFragment(new BaseTestFragment.TestFragmentListener() {
            @Override
            public void openDrawer() {
                mViewHolder.mDuoDrawerLayout.openDrawer();
            }
        });
        settings.setBaseFragment(new BaseTestFragment.TestFragmentListener() {
            @Override
            public void openDrawer() {
                mViewHolder.mDuoDrawerLayout.openDrawer();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);    }

    private void buttonClicked() {
        btnAddProdut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this, AddProduct.class);
                startActivity(intent);
            }
        });
        btnSellProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this, SellProduct.class);
                startActivity(intent);
            }
        });
        btnAvilableGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this, AvilableGoods.class);
                startActivity(intent);
            }
        });
        btnViewProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this, GraphActivity.class);
                startActivity(intent);
            }
        });
    }

    public void fragments(String items) {
        Fragment fragment;
//        if (items==mTitles.get(0)){
//            fragment=new Settings();
//            FragmentManager fm= getFragmentManager();
//            FragmentTransaction ft=fm.beginTransaction();
//            ft
//        }
    }

    private void handleToolbar() {
        setSupportActionBar(mViewHolder.mToolbar);
    }

    private void handleDrawer() {
        DuoDrawerToggle duoDrawerToggle = new DuoDrawerToggle(this,
                mViewHolder.mDuoDrawerLayout,
                mViewHolder.mToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        mViewHolder.mDuoDrawerLayout.setDrawerListener(duoDrawerToggle);

        duoDrawerToggle.syncState();
    }

    private void handleMenu() {
        mMenuAdapter = new MenuAdapter(mTitles);
        mViewHolder.mDuoMenuView.setOnMenuClickListener(this);
        mViewHolder.mDuoMenuView.setAdapter(mMenuAdapter);
    }

    @Override
    public void onFooterClicked() {
        Intent intent = new Intent(HomePage.this, Login.class);
        startActivity(intent);
        finish();
        Toast.makeText(this, "Log out", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onHeaderClicked() {
        Toast.makeText(this, "onHeaderClicked", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onOptionClicked(int position, Object objectClicked) {
        // Set the toolbar title
        setTitle(mTitles.get(position));


        // Set the right options selected
        mMenuAdapter.setViewSelected(position, true);

        // Navigate to the right fragment
        switch (position) {
            case 0:
                Toast.makeText(getApplicationContext(), "Home", Toast.LENGTH_LONG).show();
                if (fragmentManager.findFragmentByTag(TEST_FRAGMENT_TAG) != null) {
                    popFragment();
                }
                break;
            case 1:
                Intent intent = new Intent(HomePage.this, GraphActivity.class);
                startActivity(intent);
                break;
            case 2:
                if (fragmentManager.findFragmentByTag(TEST_FRAGMENT_TAG) == null) {

                    fragmentManager.beginTransaction().add(R.id.home_container, testFragment, TEST_FRAGMENT_TAG).addToBackStack("testFragment").commit();
                } else {
                    mViewHolder.mDuoDrawerLayout.closeDrawer();
                }
                break;
            case 3:
                if (fragmentManager.findFragmentByTag(TEST_FRAGMENT_TAG)==null){
                    fragmentManager.beginTransaction().add(R.id.home_container,settings,TEST_FRAGMENT_TAG).addToBackStack("testFragment").commit();
                }
                else{
                    mViewHolder.mDuoDrawerLayout.closeDrawer();
                }
                break;
            default:
                break;
        }

        // Close the drawer
        mViewHolder.mDuoDrawerLayout.closeDrawer();
    }

    private void popFragment() {
        fragmentManager = this
                .getSupportFragmentManager();
        fragmentManager.popBackStack("testFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }


    public class ViewHolder {
        private DuoDrawerLayout mDuoDrawerLayout;
        private DuoMenuView mDuoMenuView;
        private DuoDrawerLayout mDuoDrawerLayout2;
        private Toolbar mToolbar;
        private Toolbar mToolbar1;

        ViewHolder() {
            mDuoDrawerLayout = (DuoDrawerLayout) findViewById(R.id.drawer);
            mDuoDrawerLayout2 = (DuoDrawerLayout) findViewById(R.id.purchase);
            mDuoMenuView = (DuoMenuView) mDuoDrawerLayout.getMenuView();
            mToolbar1 = (Toolbar) findViewById(R.id.toolbar1);
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
        }
    }

}