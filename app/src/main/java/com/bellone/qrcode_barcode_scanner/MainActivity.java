package com.bellone.qrcode_barcode_scanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.budiyev.android.codescanner.AutoFocusMode;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ErrorCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.google.zxing.Result;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final int REQUEST_CAMERA_CODE = 10;

    private CodeScanner codeScanner;
    private TextView lblResult;

    private boolean found;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        codeScanner = null;
        found = false;

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED){

            hoIPermessi_avvia();
        }else{
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUEST_CAMERA_CODE && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            hoIPermessi_avvia();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(codeScanner != null) {
            codeScanner.startPreview();
        }
    }

    @Override
    protected void onPause() {
        if(codeScanner != null) {
            codeScanner.releaseResources();
        }
        super.onPause();
    }

    private void hoIPermessi_avvia(){
        Button btnSearch = findViewById(R.id.btnSearchOnBrowser);
        ImageButton imgBtnInfo = findViewById(R.id.imgBtnInfo);
        lblResult = findViewById(R.id.lblResult);
        CodeScannerView scannerView = findViewById(R.id.scanner_view);

        btnSearch.setOnClickListener(this);
        imgBtnInfo.setOnClickListener(this);
        scannerView.setOnClickListener(this);


        codeScanner = new CodeScanner(this, scannerView);
        codeScanner.setCamera(CodeScanner.CAMERA_BACK);
        codeScanner.setFormats(CodeScanner.ALL_FORMATS);
        codeScanner.setAutoFocusMode(AutoFocusMode.CONTINUOUS);
        codeScanner.setScanMode(ScanMode.SINGLE);
        codeScanner.setAutoFocusEnabled(true);
        codeScanner.setFlashEnabled(true);


        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lblResult.setTextSize(20);
                        lblResult.setText(result.getText());

                        lblResult.setBackgroundColor(Color.GREEN);

                        found = true;
                    }
                });
            }
        });
        codeScanner.setErrorCallback(new ErrorCallback() {
            @Override
            public void onError(@NonNull Exception error) {
                lblResult.setTextSize(25);
                lblResult.setText(getString(R.string.scanError));
                error.printStackTrace();
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.scanner_view:
                codeScanner.startPreview();

                lblResult.setText(getString(R.string.lblResult));
                lblResult.setBackgroundColor(Color.parseColor("#BD842F"));

                found = false;
                break;
            case R.id.btnSearchOnBrowser:
                if(found) {
                    Intent intent = new Intent(Intent.ACTION_SEARCH);
                    intent.putExtra(SearchManager.QUERY, lblResult.getText().toString());
                    startActivity(intent);
                }
                break;
            case R.id.imgBtnInfo:
                Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
