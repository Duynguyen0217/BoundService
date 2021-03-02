package com.example.boundservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    MyBoundService mMyBoundService;
    TextView mTvCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTvCount = findViewById(R.id.textView);

        Intent intent = new Intent(MainActivity.this , MyBoundService.class);
        ContextCompat.startForegroundService(MainActivity.this ,intent);
    }

    @Override
    protected void onStart() {
        Intent intent = new Intent(this, MyBoundService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE); //biến connection dùng để lắng nghe kết nối hay ko kết nối trong service
        super.onStart();
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MyBoundService.LocalIBinder localIBinder = (MyBoundService.LocalIBinder) iBinder;
            mMyBoundService = localIBinder.getService();
            mMyBoundService.setOnDataChange(new OnDataChange() {
                @Override
                public void changeCount(int count) {
                    mTvCount.setText("Đang xử lý : " + count);
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @Override
    protected void onStop() {
        unbindService(connection);
        super.onStop();
    }
}
