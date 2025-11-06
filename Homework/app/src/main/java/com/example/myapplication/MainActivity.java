package com.example.myapplication;

import com.example.myapplication.R;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;       // 為了使用 Manifest.permission.CALL_PHONE
import android.content.pm.PackageManager; // 為了檢查權限狀態

import android.os.Build;         // 為了檢查 Android 版本
import android.widget.Toast;     // 可選，用來顯示提示訊息
import androidx.annotation.NonNull; // 為了在 onRequestPermissionsResult 裡使用

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    // 宣告全域變數
    private TextView txtShow; // <-- 【修正 1】: 拿掉 static
    private Button btnZero, btnOne, btnTwo, btnThree, btnFour;
    private Button btnFive, btnSix, btnSeven, btnEight, btnNine, btnClear;
    private Button btnCall;
    private static final int PERMISSION_REQUEST_CALL_PHONE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 取得資源類別檔中的介面元件
        txtShow = findViewById(R.id.txtShow); // <-- findViewById 不需要 (TextView) 強制轉型
        btnZero = (Button) findViewById(R.id.btnZero);
        btnOne = (Button) findViewById(R.id.btnOne);
        btnTwo = (Button) findViewById(R.id.btnTwo);
        btnThree = (Button) findViewById(R.id.btnThree);
        btnFour = (Button) findViewById(R.id.btnFour);
        btnFive = (Button) findViewById(R.id.btnFive);
        btnSix = (Button) findViewById(R.id.btnSix);
        btnSeven = (Button) findViewById(R.id.btnSeven);
        btnEight = (Button) findViewById(R.id.btnEight);
        btnNine = (Button) findViewById(R.id.btnNine);
        btnClear = (Button) findViewById(R.id.btnClear);
        btnCall = (Button) findViewById(R.id.btnCall); // <-- 新增綁定

        // 設定 button 元件 Click 事件共用   myListner
        btnZero.setOnClickListener(myListner);
        btnOne.setOnClickListener(myListner);
        btnTwo.setOnClickListener(myListner);
        btnThree.setOnClickListener(myListner);
        btnFour.setOnClickListener(myListner);
        btnFive.setOnClickListener(myListner);
        btnSix.setOnClickListener(myListner);
        btnSeven.setOnClickListener(myListner);
        btnEight.setOnClickListener(myListner);
        btnNine.setOnClickListener(myListner);
        btnClear.setOnClickListener(myListner);
        btnCall.setOnClickListener(myListner); // <-- 新增監聽器
    }

    // 真正執行撥號動作的方法
    private void callPhoneNumber(String phoneNumber) {
        try {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(intent);
        } catch (SecurityException e) {
            e.printStackTrace();
            Toast.makeText(this, "無法撥號：請授予撥打電話權限", Toast.LENGTH_SHORT).show();
        }
    }

    // 處理權限請求「回覆」的方法
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // 檢查是否是我們發出的撥號請求
        if (requestCode == PERMISSION_REQUEST_CALL_PHONE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 用戶點擊了「允許」，現在可以撥號了
                String phoneNumber = txtShow.getText().toString().replace("電話號碼：", "");
                callPhoneNumber(phoneNumber);
                Toast.makeText(this, "已取得撥號權限，準備撥號", Toast.LENGTH_SHORT).show();
            } else {
                // 用戶點擊了「拒絕」
                Toast.makeText(this, "需要撥號權限才能使用此功能", Toast.LENGTH_LONG).show();
            }
        }
    }

    // 定義 onClick() 方法 (OnClickListener)
    // 【修正 2】: 必須要放在 MainActivity 的 { } 內部
    private Button.OnClickListener myListner = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            String s = txtShow.getText().toString();

            // 取得被點擊元件的 ID
            int id = v.getId();

            // 使用 if-else if 結構取代 switch
            if (id == R.id.btnZero) {
                txtShow.setText(s + "0");
            } else if (id == R.id.btnOne) {
                txtShow.setText(s + "1");
            } else if (id == R.id.btnTwo) {
                txtShow.setText(s + "2");
            } else if (id == R.id.btnThree) {
                txtShow.setText(s + "3");
            } else if (id == R.id.btnFour) {
                txtShow.setText(s + "4");
            } else if (id == R.id.btnFive) {
                txtShow.setText(s + "5");
            } else if (id == R.id.btnSix) {
                txtShow.setText(s + "6");
            } else if (id == R.id.btnSeven) {
                txtShow.setText(s + "7");
            } else if (id == R.id.btnEight) {
                txtShow.setText(s + "8");
            } else if (id == R.id.btnNine) {
                txtShow.setText(s + "9");
            } else if (id == R.id.btnClear) {
                txtShow.setText("電話號碼：");
            } else if (id == R.id.btnCall) { // <-- 新增 CALL 按鈕的邏輯
                // 取得目前顯示的電話號碼 (從 "電話號碼：" 這個前綴後面開始)
                String phoneNumber = txtShow.getText().toString().replace("電話號碼：", "");

                // 檢查是否為空
                if (phoneNumber.isEmpty()) {
                    Toast.makeText(MainActivity.this, "請先輸入電話號碼", Toast.LENGTH_SHORT).show();
                    return; // 中斷執行
                }

                // --- 權限檢查邏輯 ---
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // 1. 檢查是否已經有權限
                    if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // 2. 如果沒有權限，就彈出對話框請求
                        requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PERMISSION_REQUEST_CALL_PHONE);
                    } else {
                        // 3. 如果已經有權限，直接撥號
                        callPhoneNumber(phoneNumber);
                    }
                } else {
                    // 舊版 Android (API < 23) 不用檢查，直接撥號
                    callPhoneNumber(phoneNumber);
                }
            }
        }
    };

} // <-- 這才是 MainActivity Class 真正的結尾