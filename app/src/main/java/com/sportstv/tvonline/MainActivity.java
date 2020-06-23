package com.sportstv.tvonline;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sportstv.adapter.NavAdapter;
import com.sportstv.fragment.AllChannelFragment;
import com.sportstv.fragment.HomeFragment;
import com.sportstv.fragment.LatestFragment;
import com.sportstv.fragment.SearchFragment;
import com.sportstv.item.ItemNav;
import com.sportstv.util.RecyclerTouchListener;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {

    public NavAdapter navAdapter;
    private FragmentManager fragmentManager;
    ArrayList<ItemNav> mNavItem;
    BottomNavigationView navigation;
    DrawerLayout drawer;
    MyApplication MyApp;
    TextView textName, textEmail;
    int previousSelect = 0;
    boolean doubleBackToExitPressedOnce = false;
    LinearLayout mAdViewLayout;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    HomeFragment homeFragment = new HomeFragment();
                    navigationItemSelected(0);
                    loadFrag(homeFragment, getString(R.string.menu_home), fragmentManager);
                    return true;
                case R.id.navigation_tv:
                    AllChannelFragment allChannelFragment = new AllChannelFragment();
                    navigationItemSelected(0);
                    loadFrag(allChannelFragment, "TV", fragmentManager);
                    return true;

                case R.id.navigation_video:
                    showdialog();
                    return true;

            }
            return false;
        }
    };


    private void  showdialog(){
        new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Install TV Online App")
                .setContentText("For Watching TV, You Must Install Our TV Online App")
                .setConfirmText("Install")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog
                                .setTitleText("Install From Playstore")
                                .setContentText("Please Wait, Open Playstore")
                                .setConfirmText("Go")
//                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                    @Override
//                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                        Intent intent = new Intent(Intent.ACTION_VIEW);
//                                        intent.setData(Uri.parse(
//                                                "https://play.google.com/store/apps/details?id=com.example.android"));
//                                        intent.setPackage("com.android.vending");
//                                        startActivity(intent);
//                                    }
//                                })

                                .changeAlertType(SweetAlertDialog.PROGRESS_TYPE);

                        final Handler handler = new Handler();
                        handler.postDelayed(() -> {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(
                                    "https://play.google.com/store/apps/details?id="+SplashActivity.movieapk));
                            intent.setPackage("com.android.vending");
                            startActivity(intent);
//                                Do something after 100ms
                        }, 3000);



                    }
                })

                .show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAdViewLayout = findViewById(R.id.adView);
        fragmentManager = getSupportFragmentManager();
        MyApp = MyApplication.getInstance();
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        mNavItem = new ArrayList<>();
        fillNavItem();
        textName = findViewById(R.id.nav_name);
        textEmail = findViewById(R.id.nav_email);
        RecyclerView recyclerView = findViewById(R.id.navigation_list);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setFocusable(false);
        navAdapter = new NavAdapter(MainActivity.this, mNavItem);
        recyclerView.setAdapter(navAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(MainActivity.this, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                navigationClick(mNavItem.get(position).getId());
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        HomeFragment homeFragment = new HomeFragment();
        loadFrag(homeFragment, getString(R.string.menu_home), fragmentManager);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        final MenuItem searchMenuItem = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) searchMenuItem.getActionView();

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (!hasFocus) {
                    searchMenuItem.collapseActionView();
                    searchView.setQuery("", false);
                }
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String arg0) {
                // TODO Auto-generated method stub
                hideShowBottomView(false);
                String categoryName = "Search";
                Bundle bundle = new Bundle();
                bundle.putString("search", arg0);

                SearchFragment searchFragment = new SearchFragment();
                searchFragment.setArguments(bundle);
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.hide(fragmentManager.findFragmentById(R.id.Container));
                ft.add(R.id.Container, searchFragment, categoryName);
                ft.addToBackStack(categoryName);
                ft.commit();
                setToolbarTitle(categoryName);
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String arg0) {
                // TODO Auto-generated method stub
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void navigationClick(int position) {
        drawer.closeDrawers();
        switch (position) {
            case 0:
                navigationItemSelected(0);
                navigation.getMenu().findItem(R.id.navigation_home).setChecked(true);
                HomeFragment homeFragment = new HomeFragment();
                loadFrag(homeFragment, getString(R.string.menu_home), fragmentManager);
                hideShowBottomView(true);
                break;
            case 1:
                navigationItemSelected(1);
                LatestFragment latestFragment = new LatestFragment();
                loadFrag(latestFragment, getString(R.string.menu_latest), fragmentManager);
                hideShowBottomView(false);
                break;




            case 13:
                navigationItemSelected(previousSelect);
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
                break;
        }
    }

    private void fillNavItem() {
        mNavItem.add(new ItemNav(0, R.drawable.ic_home, getResources().getString(R.string.menu_home)));
        mNavItem.add(new ItemNav(1, R.drawable.ic_latest, getResources().getString(R.string.menu_latest)));

        mNavItem.add(new ItemNav(13, R.drawable.ic_setting, getResources().getString(R.string.menu_setting)));
        if (MyApp.getIsLogin()) {

        } else {
            mNavItem.add(new ItemNav(12, R.drawable.ic_login, getResources().getString(R.string.login)));
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        View v1 = findViewById(R.id.view_fake);
        v1.requestFocus();
        if (MyApp.getIsLogin()) {
            textName.setText(MyApp.getUserName());
            textEmail.setText(MyApp.getUserEmail());
        }
    }



    public void loadFrag(Fragment f1, String name, FragmentManager fm) {
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
        FragmentTransaction ft = fm.beginTransaction();
        //  ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.replace(R.id.Container, f1, name);
        ft.commit();
        setToolbarTitle(name);
    }

    public void hideShowBottomView(boolean visibility) {
        navigation.setVisibility(visibility ? View.VISIBLE : View.GONE);
        mAdViewLayout.setVisibility(visibility ? View.GONE : View.VISIBLE);
    }

    public void navigationItemSelected(int position) {
        previousSelect = position;
        navAdapter.setSelected(position);
    }

    public void setToolbarTitle(String Title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(Title);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (fragmentManager.getBackStackEntryCount() != 0) {
            String tag = fragmentManager.getFragments().get(fragmentManager.getBackStackEntryCount() - 1).getTag();
            setToolbarTitle(tag);
            //when search is click and goes back if home
            assert tag != null;
            if (tag.equals(getString(R.string.menu_home)) || tag.equals("TV") || tag.equals("Movies")) {
                hideShowBottomView(true);
            }
            super.onBackPressed();
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }
}
