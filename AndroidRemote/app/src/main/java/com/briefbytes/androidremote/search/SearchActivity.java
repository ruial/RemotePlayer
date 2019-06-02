package com.briefbytes.androidremote.search;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.briefbytes.androidremote.R;
import com.briefbytes.androidremote.player.PlayerActivity;
import com.briefbytes.remoteplayer.client.AddressInfo;

import java.util.List;

public class SearchActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<AddressInfo>>,
        AddressInfoAdapter.AddressInfoClickListener {

    private static final String LOG_TAG = SearchActivity.class.getSimpleName();
    private static final int ADDRESS_LOADER_ID = 1;

    private View loadingView;
    private TextView errorView;

    private AddressInfoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_items);

        loadingView = findViewById(R.id.loading_view);
        errorView = (TextView) findViewById(R.id.error_view);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.search_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        adapter = new AddressInfoAdapter(this);
        recyclerView.setAdapter(adapter);

        if (isConnected()) {
            LoaderManager loaderManager = getSupportLoaderManager();
            loaderManager.initLoader(ADDRESS_LOADER_ID, null, this);
        } else {
            loadingView.setVisibility(View.GONE);
            errorView.setText(R.string.not_connected);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh_settings:
                adapter.clear();
                loadingView.setVisibility(View.VISIBLE);
                errorView.setVisibility(View.GONE);
                getSupportLoaderManager().restartLoader(ADDRESS_LOADER_ID, null, this);
                return true;
            case R.id.address_settings:
                alertDialog();
                return true;
            case R.id.about_settings:
                openProjectUrl();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<AddressInfo>> onCreateLoader(int id, Bundle args) {
        return new AddressLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<AddressInfo>> loader, List<AddressInfo> data) {
        loadingView.setVisibility(View.GONE);
        if (data == null) {
            errorView.setVisibility(View.VISIBLE);
            errorView.setText(R.string.loading_error);
        } else if (data.isEmpty()) {
            errorView.setVisibility(View.VISIBLE);
            errorView.setText(R.string.addresses_not_found);
        } else {
            errorView.setVisibility(View.GONE);
            adapter.updateAddresses(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<AddressInfo>> loader) {
        adapter.clear();
    }

    @Override
    public void onClick(AddressInfo addressInfo) {
        openPlayerActivity(addressInfo.address());
    }

    private boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private void alertDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_address, null);
        final EditText input = (EditText) view.findViewById(R.id.address_input);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.address_settings)
                .setView(view)
                .setCancelable(true)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openPlayerActivity(input.getText().toString());
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    private void openPlayerActivity(String address) {
        Intent intent = new Intent(this, PlayerActivity.class);
        intent.putExtra(PlayerActivity.ADDRESS_KEY, address);
        startActivity(intent);
    }

    private void openProjectUrl() {
        Uri uri = Uri.parse(getResources().getString(R.string.project_url));
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (intent.resolveActivity(getPackageManager()) == null) {
            Toast.makeText(this, uri.toString(), Toast.LENGTH_SHORT).show();
        } else {
            startActivity(intent);
        }
    }

}
