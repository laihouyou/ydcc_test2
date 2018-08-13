package com.movementinsome.app.pub;

import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Response;

/**
 * Created by zzc on 2017/1/10.
 */

public abstract class UserCallback extends Callback<Response>{
    @Override
    public Response parseNetworkResponse(Response response, int id) throws Exception {
        return response;
    }
}
