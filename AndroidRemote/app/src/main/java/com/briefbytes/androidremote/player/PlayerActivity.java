package com.briefbytes.androidremote.player;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.briefbytes.androidremote.R;
import com.briefbytes.remoteplayer.client.Client;
import com.briefbytes.remoteplayer.client.VideoFile;
import com.briefbytes.remoteplayer.client.action.*;

import java.io.IOException;
import java.util.List;

public class PlayerActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<VideoFile>>,
        VideoFileAdapter.VideoFileClickListener {

    private static final String LOG_TAG = PlayerActivity.class.getSimpleName();
    private static final int FILES_LOADER_ID = 2;

    public static final String ADDRESS_KEY = "address";

    private View loadingView;
    private TextView errorView;

    private VideoFileAdapter adapter;

    private String address;
    private Client client;
    private VideoFile currentVideo;

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

        adapter = new VideoFileAdapter(this);
        recyclerView.setAdapter(adapter);

        Intent intent = getIntent();
        if (intent != null) {
            address = intent.getStringExtra(ADDRESS_KEY);
            setTitle(address);
            LoaderManager loaderManager = getSupportLoaderManager();
            loaderManager.initLoader(FILES_LOADER_ID, null, PlayerActivity.this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (client != null) client.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.player, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh_settings:
                adapter.clear();
                loadingView.setVisibility(View.VISIBLE);
                errorView.setVisibility(View.GONE);
                getSupportLoaderManager().restartLoader(FILES_LOADER_ID, null, this);
                return true;
            case R.id.player_quit:
                if (client != null) new ClientActionTask().execute(new PlayerActionQuit());
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.player_shutdown:
                if (client != null) new ClientActionTask().execute(new PlayerActionShutdown());
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<VideoFile>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<List<VideoFile>>(this) {
            @Override
            protected void onStartLoading() {
                forceLoad();
            }

            @Override
            public List<VideoFile> loadInBackground() {
                try {
                    client = new Client(address);
                    return client.requestFiles();
                } catch (IOException | IllegalArgumentException e) {
                    // IllegalArgumentException is thrown when creating DatagramPacket with unresolved address
                    Log.e(LOG_TAG, e.getMessage());
                }
                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<VideoFile>> loader, List<VideoFile> data) {
        loadingView.setVisibility(View.GONE);
        if (data == null) {
            errorView.setVisibility(View.VISIBLE);
            errorView.setText(R.string.loading_error);
        } else if (data.isEmpty()) {
            errorView.setVisibility(View.VISIBLE);
            errorView.setText(R.string.files_not_found);
        } else {
            errorView.setVisibility(View.GONE);
            adapter.updateFiles(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<VideoFile>> loader) {
        adapter.clear();
    }

    @Override
    public void onClick(VideoFile file, PlayerAction action) {
        // if we click play on the same video twice, toggle pause
        if (action instanceof PlayerActionPlayFile && file.equals(currentVideo)) {
            action = new PlayerActionPause();
        }
        currentVideo = file;
        if (client != null) new ClientActionTask().execute(action);
    }

    /**
     * Async task to execute an action
     */
    private class ClientActionTask extends AsyncTask<PlayerAction, Void, Void> {
        @Override
        protected Void doInBackground(PlayerAction... playerActions) {
            for (PlayerAction action : playerActions) {
                try {
                    action.execute(client, currentVideo);
                } catch (IOException e) {
                    Log.e(LOG_TAG, e.getMessage());
                }
            }
            return null;
        }
    }

}
