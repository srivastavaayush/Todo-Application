package com.symb.task.todoapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Createdoc extends AppCompatActivity {
    EditText mInputEt;
    Button mSaveBtn;
    String mText;
    private static final int WRITE_EXTERNAL_STRORAGE_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createdoc);
        mInputEt = findViewById(R.id.inputEt);
        mSaveBtn = findViewById(R.id.saveBtn);

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mText = mInputEt.getText().toString().trim();

                if(mText.isEmpty()) {
                    Toast.makeText(Createdoc.this, "Please enter something....", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED){
                            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                            requestPermissions(permissions, WRITE_EXTERNAL_STRORAGE_CODE);

                        }
                        else {
                                todoApplication(mText);
                        }
                    }
                    else {
                        todoApplication(mText);
                    }
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case WRITE_EXTERNAL_STRORAGE_CODE:{
                if (grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    todoApplication(mText);
                }
                else {
                    Toast.makeText(this, "Storage permission Is REquired To Store Data", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void todoApplication(String mText) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());
        try {
                File path = Environment.getExternalStorageDirectory();
                File dir = new File(path + "/Todo Files/");
                dir.mkdirs();
                String fileName = "TodoFile_" + timeStamp + ".txt";
                File file = new File(dir, fileName);
                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(mText);
                bw.close();
            Toast.makeText(this, fileName+" is saved to" + dir, Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
