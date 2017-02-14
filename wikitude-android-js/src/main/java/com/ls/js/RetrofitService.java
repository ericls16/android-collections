package com.ls.js;

import android.support.annotation.NonNull;

import com.ls.js.model.Article;
import com.ls.js.model.ArticleDetail;
import com.ls.js.model.Categorie;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Jiang Chen on 16/4/13.
 */
public interface RetrofitService {

    @GET("http://116.228.234.237:3001/categories.json")
    Observable<List<Categorie>> categories();

    @GET("http://116.228.234.237:3001/categories/{id}/articles.json")
    Observable<List<Article>> articles(@NonNull @Path("id") Integer customerId);

    @GET("http://116.228.234.237:3001/articles/{id}.json")
    Observable<ArticleDetail> articledetail(@NonNull @Path("id") Integer customerId);
}
