package veeresh.a3c.realm.networking;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import veeresh.a3c.realm.models.RecordList;

/**
 * Created by Veeresh on 3/11/17.
 */
public interface APIService {
    @POST("api/gyanmatrix")
    Call<RecordList> getBatsmen(@QueryMap Map<String, String> options);
}
