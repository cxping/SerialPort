package com.iowk.serialport;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.iowk.iowkcore.common.VLogUtil;
import com.iowk.iowkcore.def.DefaultPortListener;
import com.iowk.iowkcore.def.IDataTransfer;
import com.iowk.iowkcore.def.SerialPortException;
import com.iowk.iowkcore.serialport.ISerialPortListener;
import com.iowk.iowkcore.serialport.SerialPortOperator;
import com.iowk.iowkcore.utils.StringUtil;
import com.iowk.serialport.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private ActivityMainBinding binding;
    //实现默认的串口监听
    private DefaultPortListener defaultPortListener = new DefaultPortListener(new IDataTransfer() {
        @Override
        public void onReceive(byte[] data, int iArr) {

        }

        @Override
        public void onError(String str) {

        }

        @Override
        public void onSendData(byte[] bArr) {

        }
    }) {
        @Override
        public void onReceiveData(byte[] bArr, int i) {
            super.onReceiveData(bArr, i);
            VLogUtil.println("[onReceiveData]" + StringUtil.bytesToHexString(bArr, i));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        try {
            defaultPortListener.initPort("SimplePort", "/dev/ttyS3", 115200, 0);
            defaultPortListener.onStart();
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }
}