package soft.synergy.registraduriaapp.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import soft.synergy.registraduriaapp.R;
import soft.synergy.registraduriaapp.models.PollingStationModel;
import soft.synergy.registraduriaapp.models.RegisterRequestModel;
import soft.synergy.registraduriaapp.models.RegisterResponseModel;
import soft.synergy.registraduriaapp.models.StandModel;
import soft.synergy.registraduriaapp.services.ReportServiceAdapter;
import soft.synergy.registraduriaapp.utils.LoadingDialog;


public class RegisterFragment extends Fragment {

    private View mView;
    private List<PollingStationModel> stations;
    private List<StandModel> stands;
    private Spinner stationsSpinner;
    private Spinner standsSpinner;
    private EditText totalPollsET;
    private Button submitButton;
    private String stationCodeSelected;
    private String standCodeSelected;
    private long totalPolls;
    private LoadingDialog loadingDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_register, container, false);
        stationsSpinner = mView.findViewById(R.id.station_input);
        standsSpinner = mView.findViewById(R.id.stand_input);
        submitButton = mView.findViewById(R.id.submit_button);
        totalPollsET = mView.findViewById(R.id.total_polls);
        loadingDialog = new LoadingDialog(this.getActivity());

        stationCodeSelected = "";
        standCodeSelected = "";
        totalPolls = 0;

        //Call<List<StandModel>> callStands = ReportServiceAdapter.getReportService().getStandsByStation();


        //ArrayAdapter<PollingStationModel> stationsAdapter = new ArrayAdapter<PollingStationModel>(this.getContext(), android.R.layout.simple_list_item_1, stations);

        getStations();

        stationsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                PollingStationModel station = (PollingStationModel) stationsSpinner.getAdapter().getItem(i);
                stationCodeSelected = station.getCode();
                Log.d("FFFF", stationCodeSelected);
                if (!stationCodeSelected.equals("0")) {
                    getStands(stationCodeSelected);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        standsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                StandModel stand = (StandModel) standsSpinner.getAdapter().getItem(i);
                standCodeSelected = stand.getCode();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });


        return mView;
    }

    private void submit() {
        if (stationCodeSelected != null && !stationCodeSelected.equals("0")) {
            if (standCodeSelected != null && !standCodeSelected.equals("0")) {
                loadingDialog.startLoadingAnimation();
                totalPolls = Long.valueOf(totalPollsET.getText().toString());
                RegisterRequestModel log = new RegisterRequestModel();
                log.setPollingStationCode(stationCodeSelected);
                log.setStandCode(standCodeSelected);
                log.setTotalPolls(totalPolls);


                Call<RegisterResponseModel> callSubmit = ReportServiceAdapter.getReportService().createLog(log);
                callSubmit.enqueue(new Callback<RegisterResponseModel>() {
                    @Override
                    public void onResponse(Call<RegisterResponseModel> call, Response<RegisterResponseModel> response) {
                        if (response.isSuccessful()) {

                            stationsSpinner.setSelection(0);
                            standsSpinner.setSelection(0);
                            totalPollsET.setText("");

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    loadingDialog.dismissDialog();
                                }
                            }, 3000);


                        }
                    }

                    @Override
                    public void onFailure(Call<RegisterResponseModel> call, Throwable t) {
                        loadingDialog.dismissDialog();

                    }
                });

            }
        }
    }

    private void getStands(String code) {
        Call<List<StandModel>> callStand = ReportServiceAdapter.getReportService().getStandsByStation(code);
        callStand.enqueue(new Callback<List<StandModel>>() {
            @Override
            public void onResponse(Call<List<StandModel>> call, Response<List<StandModel>> response) {
                if (response.isSuccessful()) {
                    stands = new ArrayList<>();
                    stands.add(new StandModel("Seleccione la mesa"));
                    stands.addAll(response.body());

                    ArrayAdapter<StandModel> spinStandAdapter = new ArrayAdapter<StandModel>(getContext(), android.R.layout.simple_spinner_item, stands) {
                        @Override
                        public boolean isEnabled(int position) {
                            if (position == 0) {
                                return false;
                            } else {
                                return true;
                            }
                        }

                        @Override
                        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                            View view = super.getDropDownView(position, convertView, parent);
                            TextView tv = (TextView) view;
                            if (position == 0) {
                                // Set the hint text color gray
                                tv.setTextColor(Color.GRAY);
                            } else {
                                tv.setTextColor(Color.BLACK);
                            }
                            return view;
                        }
                    };
                    spinStandAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    standsSpinner.setAdapter(spinStandAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<StandModel>> call, Throwable t) {

            }
        });
    }

    private void getStations() {
        Call<List<PollingStationModel>> callStation = ReportServiceAdapter.getReportService().getPollingStations();
        callStation.enqueue(new Callback<List<PollingStationModel>>() {
            @Override
            public void onResponse(Call<List<PollingStationModel>> call, Response<List<PollingStationModel>> response) {
                if (response.isSuccessful()) {
                    stations = new ArrayList<>();
                    stations.add(new PollingStationModel("0", "Seleccione uno", "none"));
                    stations.addAll(response.body());

                    ArrayAdapter<PollingStationModel> spinStationAdapter = new ArrayAdapter<PollingStationModel>(getContext(), android.R.layout.simple_spinner_item, stations) {
                        @Override
                        public boolean isEnabled(int position) {
                            if (position == 0) {
                                return false;
                            } else {
                                return true;
                            }
                        }

                        @Override
                        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                            View view = super.getDropDownView(position, convertView, parent);
                            TextView tv = (TextView) view;
                            if (position == 0) {
                                // Set the hint text color gray
                                tv.setTextColor(Color.GRAY);
                            } else {
                                tv.setTextColor(Color.BLACK);
                            }
                            return view;
                        }
                    };
                    spinStationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    stationsSpinner.setAdapter(spinStationAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<PollingStationModel>> call, Throwable t) {

            }
        });
    }


}