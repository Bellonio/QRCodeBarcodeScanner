package com.bellone.qrcode_barcode_scanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class InfoActivity extends AppCompatActivity {

    private Button btnTurnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        btnTurnBack = findViewById(R.id.btnTurnBack);
    }

    @Override
    protected void onResume() {
        super.onResume();

        btnTurnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { turnBack(); }
        });
    }

    @Override
    public void onBackPressed() { turnBack(); }

    private void turnBack(){
        Intent intent = new Intent(InfoActivity.this, MainActivity.class);
        startActivity(intent);
    }
}