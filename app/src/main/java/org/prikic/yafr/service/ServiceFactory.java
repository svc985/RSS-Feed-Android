package org.prikic.yafr.service;


import org.prikic.yafr.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public final class ServiceFactory {

    private OkHttpClient client;
    private static ServiceFactory instance;

    private ServiceFactory() {
        this.client = buildHttpClient();
    }

    private OkHttpClient buildHttpClient() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            clientBuilder.addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS));
        }

        clientBuilder.connectTimeout(20, TimeUnit.SECONDS);
        clientBuilder.readTimeout(20, TimeUnit.SECONDS);

        //no cache for now

        return clientBuilder.build();
    }

    public static ServiceFactory getInstance() {
        if (instance == null) {
            instance = new ServiceFactory();
        }
        return instance;
    }

    private OkHttpClient getClient() {
        return client;
    }

    public static XmlProviderService buildService() {

        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl("http://www.b92.net/info/rss/tehnopolis.xml/")
                        .client(ServiceFactory.getInstance().getClient())
                        .addConverterFactory(SimpleXmlConverterFactory.create());

        Retrofit retrofit = builder.build();
        return retrofit.create(XmlProviderService.class);
    }
}
