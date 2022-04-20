package soft.synergy.registraduriaapp.services;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import soft.synergy.registraduriaapp.models.PollingStationModel;
import soft.synergy.registraduriaapp.models.RegisterRequestModel;
import soft.synergy.registraduriaapp.models.RegisterResponseModel;
import soft.synergy.registraduriaapp.models.StandModel;

public interface IReportService {

    @POST("logs")
    Call<RegisterResponseModel> createLog(@Body RegisterRequestModel request);

    @GET("sites/station")
    Call<List<PollingStationModel>> getPollingStations();

    @GET("sites/station/{stationCode}/stand")
    Call<List<StandModel>> getStandsByStation(@Path("stationCode") String stationCode);

    @GET("logs/report")
    Call<ResponseBody> getReport();


}
