package com.briefbytes.androidremote.search;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import com.briefbytes.remoteplayer.client.AddressInfo;
import com.briefbytes.remoteplayer.client.Client;

import java.io.IOException;
import java.util.List;

public class AddressLoader extends AsyncTaskLoader<List<AddressInfo>> {

    private static final String LOG_TAG = AddressLoader.class.getSimpleName();

    public AddressLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<AddressInfo> loadInBackground() {
        Client client = null;
        try {
            client = new Client();
            return client.findAddresses();
        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage());
            return null;
        } finally {
            if (client != null) client.close();
        }
    }

}
