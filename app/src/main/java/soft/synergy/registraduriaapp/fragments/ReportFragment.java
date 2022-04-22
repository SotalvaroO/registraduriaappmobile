package soft.synergy.registraduriaapp.fragments;

import static android.content.Context.DOWNLOAD_SERVICE;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.URLUtil;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import soft.synergy.registraduriaapp.R;
import soft.synergy.registraduriaapp.activities.PDFViewActivity;
import soft.synergy.registraduriaapp.services.ReportServiceAdapter;
import soft.synergy.registraduriaapp.utils.LoadingDialog;


public class ReportFragment extends Fragment {

    private LinearLayout submitButton;
    //    private LinearLayout inspectReport;
    private LoadingDialog loadingDialog;
    private String title;
    private static final int MY_PERMISSIONS_REQUEST = 100;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View mView = inflater.inflate(R.layout.fragment_report, container, false);

        if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST);
        }


        submitButton = mView.findViewById(R.id.generate_report);
        loadingDialog = new LoadingDialog(this.getActivity());
//        inspectReport = mView.findViewById(R.id.open_report);
        title = "";
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadReport();

            }
        });

        /*inspectReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openReport();
            }
        });*/

        return mView;
    }

    private void openReport() {

        if (!title.equals("")) {
            loadingDialog.startLoadingAnimation();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadingDialog.dismissDialog();
                    File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + title);
                    Uri uri = FileProvider.getUriForFile(getContext(), "soft.synergy.registraduriaapp" + ".provider", file);
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setDataAndType(uri, "application/pdf");
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(i);
                }
            }, 5000);


        } else {
            Toast.makeText(this.getContext(), "Genere primero un reporte", Toast.LENGTH_SHORT).show();
        }


    }

    private void downloadReport() {

        Call<ResponseBody> callDownload = ReportServiceAdapter.getReportService().getReport();
        callDownload.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    boolean writeToDisk = writeResponseBodyToDisk(response.body());
                    Log.d("FFFFFF", writeToDisk +"F");
                    loadingDialog.startLoadingAnimation();


                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadingDialog.dismissDialog();


                        }
                    }, 4000);
                    Intent intent = new Intent(getActivity(), PDFViewActivity.class);
                    intent.putExtra("title", title);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });





    }

    private boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
            String date = dateFormatter.format(new Date());

            title = "Report_" + date + ".pdf";

            File futureStudioIconFile = new File(getActivity().getExternalFilesDir(null) + File.separator + title);
            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];
                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                    Log.d("TAG", "file download: " + fileSizeDownloaded + " of " + fileSize);
                }
                outputStream.flush();
                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

}