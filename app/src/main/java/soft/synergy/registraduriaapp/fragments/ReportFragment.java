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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.URLUtil;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import soft.synergy.registraduriaapp.R;
import soft.synergy.registraduriaapp.utils.LoadingDialog;


public class ReportFragment extends Fragment {

    private LinearLayout submitButton;
    private LinearLayout inspectReport;
    private LoadingDialog loadingDialog;
    private String title;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View mView = inflater.inflate(R.layout.fragment_report, container, false);


        submitButton = mView.findViewById(R.id.generate_report);
        loadingDialog = new LoadingDialog(this.getActivity());
        inspectReport = mView.findViewById(R.id.open_report);
        title = "";
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadReport();

            }
        });

        inspectReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openReport();
            }
        });

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
        String url = "http://192.168.1.5:8080/api/logs/report";
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String date = dateFormatter.format(new Date());

        title = "Report_" + date + ".pdf";
        request.setTitle(title);
        request.setDescription("Descargando archivo...");
        String cookie = CookieManager.getInstance().getCookie(url);
        request.addRequestHeader("cookie", cookie);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.allowScanningByMediaScanner();
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title);

        DownloadManager downloadManager = (DownloadManager) getActivity().getSystemService(DOWNLOAD_SERVICE);
        long downloadId = downloadManager.enqueue(request);
        loadingDialog.startLoadingAnimation();


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingDialog.dismissDialog();


            }
        }, 4000);


    }

}