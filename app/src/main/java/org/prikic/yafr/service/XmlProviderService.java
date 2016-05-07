package org.prikic.yafr.service;

import retrofit2.http.GET;
import retrofit2.http.Url;

public interface XmlProviderService {

    @GET
    Call<Feed> getUsers(@Url String url);

}
