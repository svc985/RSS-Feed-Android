package org.prikic.yafr.service;

import org.prikic.yafr.model.xmlService.Feed;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

interface XmlProviderService {

    @GET
    Call<Feed> getFeeds(@Url String url);

}
