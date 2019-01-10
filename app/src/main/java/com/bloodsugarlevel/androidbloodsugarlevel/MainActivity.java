package com.bloodsugarlevel.androidbloodsugarlevel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bloodsugarlevel.MyApplication;
import com.bloodsugarlevel.androidbloodsugarlevel.dto.UserDto;
import com.bloodsugarlevel.androidbloodsugarlevel.httpClient.EntityResponseListenerBase;
import com.bloodsugarlevel.androidbloodsugarlevel.httpClient.ErroResponseListenerImpl;
import com.bloodsugarlevel.androidbloodsugarlevel.httpClient.IUiUpdateEntityListener;
import com.bloodsugarlevel.androidbloodsugarlevel.httpClient.request.AuthRequest;
import com.bloodsugarlevel.androidbloodsugarlevel.httpClient.request.CookieRequest;
import com.bloodsugarlevel.androidbloodsugarlevel.httpClient.request.HttpRequestFactory;
import com.bloodsugarlevel.androidbloodsugarlevel.tabfragment.AlertDialogManager;
import com.bloodsugarlevel.androidbloodsugarlevel.tabfragment.EditFragment;
import com.bloodsugarlevel.androidbloodsugarlevel.tabfragment.GraphFragment;
import com.bloodsugarlevel.androidbloodsugarlevel.tabfragment.SugarInputFragment;

import static com.bloodsugarlevel.androidbloodsugarlevel.tabfragment.SugarInputFragment.SUGAR_CREATE_VOLEY_TAG;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String LOGOUT_VOLEY_TAG = "LOGOUT_VOLEY_TAG";

    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int[] tabIcons = {
            R.drawable.blood_drop,
            R.drawable.graph,
            R.drawable.edit
    };
    private RequestQueue mRequestQueue;
    private TextView loginNavHeaderTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRequestQueue = MyApplication.getInstance().getRequestQueue();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        createDrawer(toolbar);
        createFragmentAdapter();

    }

    private void createDrawer(Toolbar toolbar) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                String name = MyApplication.getInstance().getLoggedUserName();
                loginNavHeaderTextView = drawerView.findViewById(R.id.loginNavHeaderTextView);
                loginNavHeaderTextView.setText(name);
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    private void createFragmentAdapter() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        adapter = new TabAdapter(getSupportFragmentManager(), this);

        adapter.addFragment(new SugarInputFragment(), "Sugar", tabIcons[0]);
        adapter.addFragment(new GraphFragment(), "Graph", tabIcons[1]);
        adapter.addFragment(new EditFragment(), "Edit", tabIcons[2]);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        highLightCurrentTab(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                highLightCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startSettingsActivity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.logout_18dp) {
            if (MyApplication.getInstance().isInternetAllow()) {
                logoutOnline();
            } else {
                MyApplication.getInstance().logout();
                startLoginActivity();
            }
        } else if (id == R.id.update_session) {
            AuthRequest jsonObjectRequest = HttpRequestFactory.loginWithTokenUserRequest(this,
                    new IUiUpdateEntityListener<UserDto>() {
                        @Override
                        public void onResponse(UserDto userDto) {
                            showProgress(false);
                        }
                    },
                    MyApplication.getInstance().getToken(),
                    LOGOUT_VOLEY_TAG);
            mRequestQueue.add(jsonObjectRequest);
            showProgress(true);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logoutOnline() {
        String cookie = MyApplication.getInstance().getSessionCookies();
        CookieRequest jsonObjectRequest = HttpRequestFactory.signoutUserRequest(this,
                new IUiUpdateEntityListener<String>() {
                    @Override
                    public void onResponse(String object) {
                        MyApplication.getInstance().logout();
                        startLoginActivity();
                    }
                },
                new ErroResponseListenerImpl(this) {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        MyApplication.getInstance().logout();
                        startLoginActivity();

                    }
                },
                LOGOUT_VOLEY_TAG,
                cookie);
        mRequestQueue.add(jsonObjectRequest);
    }

    private void showProgress(boolean show) {
        View progressView = findViewById(R.id.tab_progress);
        View tabView = findViewById(R.id.tab_layout_id);
        if(show){
            progressView.setVisibility(View.VISIBLE);
            tabView.setVisibility(View.GONE);
        }else{
            progressView.setVisibility(View.GONE);
            tabView.setVisibility(View.VISIBLE);
        }
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void startSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
    /////////////////

    private void highLightCurrentTab(int position) {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            assert tab != null;
            tab.setCustomView(null);
            tab.setCustomView(adapter.getTabView(i));
        }
        TabLayout.Tab tab = tabLayout.getTabAt(position);
        assert tab != null;
        tab.setCustomView(null);
        tab.setCustomView(adapter.getSelectedTabView(position));
        if( position != 0){
            ((SugarInputFragment) adapter.getItem(0)).hideInputMethod();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(LOGOUT_VOLEY_TAG);
        }
    }


}
