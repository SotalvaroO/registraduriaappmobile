package soft.synergy.registraduriaapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

import soft.synergy.registraduriaapp.R;

public class PDFViewActivity extends AppCompatActivity {

    PDFView pdfView;
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfview);

        pdfView = findViewById(R.id.pdfView);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                title= null;
            } else {
                title= extras.getString("title");
            }
        } else {
            title= (String) savedInstanceState.getSerializable("title");
        }

        File pdfFile = new File("/storage/emulated/0/Android/data/soft.synergy.registraduriaapp/files/"+title);
        String name = pdfFile.getName();

        pdfView.fromFile(pdfFile).load();
    }
}