package com.rusminanto.assignment.assignmentapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.rusminanto.assignment.assignmentapp.adapter.PromoAdapter;
import com.rusminanto.assignment.assignmentapp.model.Promo;
import com.rusminanto.assignment.assignmentapp.util.NetworkUtil;
import com.rusminanto.assignment.assignmentapp.util.UserUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class PromoActivity extends AppCompatActivity implements PromoAdapter.OnPromoItemClickListener, NetworkUtil.OnLoadDataFinishedListener {

    private boolean isMenuClicked;
    private NetworkUtil networkUtil;
    private List<Promo> promos;
    private PromoAdapter promoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.app_list_promo));
        if (toolbar != null && toolbar.getChildCount() > 0) {
            for (int i = 0; i < toolbar.getChildCount(); i++) {
                if (toolbar.getChildAt(i) instanceof TextView) {
                    TextView titleTextView = (TextView) toolbar.getChildAt(i);
                    titleTextView.setGravity(Gravity.CENTER);
                }
            }
        }

        networkUtil = new NetworkUtil();
        networkUtil.setOnLoadDataFinishedListener(this);

        promos = new ArrayList<>();
        promoAdapter = new PromoAdapter(promos);
        promoAdapter.setOnPromoItemClickListener(this);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_promo);
        assert recyclerView != null;
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(promoAdapter);

        if (networkUtil.isInternetConnected(this)) {
            try {
                networkUtil.getData(new String[]{BuildConfig.promo_api}, NetworkUtil.HttpMethod.GET, NetworkUtil.Case.LOAD_BY);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_promo, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_filter_promo) {
            isMenuClicked = true;
            try {
                networkUtil.getData(new String[]{BuildConfig.city_api}, NetworkUtil.HttpMethod.GET, NetworkUtil.Case.LOAD_BY);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (item.getItemId() == R.id.action_logout) {
            UserUtil userUtil = new UserUtil(this);
            userUtil.logOut();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void OnPromoItemClick(View view, int position) {
        Promo promo = promos.get(position);
        new AlertDialog.Builder(this)
                .setMessage(promo.getName() + " di " + promo.getCity())
                .setPositiveButton(getString(R.string.action_okay), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();

    }

    @Override
    public void onLoadDataFinished(final String result) {
        new Handler(getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    if (isMenuClicked) {
                        isMenuClicked = false;
                        synchronized (this) {
                            final String[] menuFilter = new String[jsonArray.length()];
                            for (int i = 0; i < jsonArray.length(); i++) {
                                menuFilter[i] = (String) jsonArray.get(i);
                            }

                            new AlertDialog.Builder(PromoActivity.this)
                                    .setItems(menuFilter, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            try {
                                                networkUtil.getData(new String[]{BuildConfig.promo_api, menuFilter[which]}, NetworkUtil.HttpMethod.POST, NetworkUtil.Case.LOAD_BY);
                                            } catch (ExecutionException | InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    })
                                    .show();
                        }
                    } else {
                        synchronized (this) {
                            if (!promos.isEmpty()) {
                                promos.clear();
                            }

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                Promo promo = new Promo((short) jsonObject.getInt("id"), jsonObject.getString("name"), jsonObject.getString("city"));
                                promos.add(promo);
                            }

                            promoAdapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

