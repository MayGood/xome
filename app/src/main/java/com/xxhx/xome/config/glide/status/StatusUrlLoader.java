package com.xxhx.xome.config.glide.status;

import android.content.Context;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.GenericLoaderFactory;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import java.io.InputStream;
import okhttp3.Call;
import okhttp3.OkHttpClient;

/**
 * A simple model loader for fetching media over http/https using OkHttp.
 */
public class StatusUrlLoader implements ModelLoader<StatusUrl, InputStream> {

    private final Call.Factory client;

    public StatusUrlLoader(Call.Factory client) {
        this.client = client;
    }

    @Override
    public DataFetcher<InputStream> getResourceFetcher(StatusUrl model, int width, int height) {
        return new StatusStreamFetcher(client, model);
    }

    /**
     * The default factory for {@link StatusUrlLoader}s.
     */
    public static class Factory implements ModelLoaderFactory<StatusUrl, InputStream> {
        private static volatile Call.Factory internalClient;
        private Call.Factory client;

        /**
         * Constructor for a new Factory that runs requests using a static singleton client.
         */
        public Factory() {
            this(getInternalClient());
        }

        /**
         * Constructor for a new Factory that runs requests using given client.
         *
         * @param client this is typically an instance of {@code OkHttpClient}.
         */
        public Factory(Call.Factory client) {
            this.client = client;
        }

        private static Call.Factory getInternalClient() {
            if (internalClient == null) {
                synchronized (Factory.class) {
                    if (internalClient == null) {
                        internalClient = new OkHttpClient();
                    }
                }
            }
            return internalClient;
        }

        @Override
        public ModelLoader<StatusUrl, InputStream> build(Context context, GenericLoaderFactory factories) {
            return new StatusUrlLoader(client);
        }

        @Override
        public void teardown() {
            // Do nothing, this instance doesn't own the client.
        }
    }
}
