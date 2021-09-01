package com.iowk.serialport;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.iowk.iowkcore.common.VLogUtil;
import com.iowk.iowkcore.serialport.ISerialPortListener;
import com.iowk.iowkcore.serialport.SerialPortOperator;
import com.iowk.iowkcore.utils.StringUtil;
import com.iowk.serialport.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private SerialPortOperator serialPortOperator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        MainActivity.this.serialPortOperator.spoName = "设备A";
        //第一步：初始化串口数据信息
        MainActivity.this.serialPortOperator.initSerialPort("/dev/ttyO2", 9600, 0);
        //第二步:设置数据监听
        MainActivity.this.serialPortOperator.setListener(new OnSerialPortListener());
        //第三步：打开串口
//        MainActivity.this.serialPortOperator.openSerialPort();
        //退出时关闭串口
//        MainActivity.this.serialPortOperator.closeSerialPort();
        //发送数据
//        MainActivity.this.serialPortOperator.sendData(new byte[]{1, 2, 3, 4, 5});
    }

    private class OnSerialPortListener implements ISerialPortListener {
        private OnSerialPortListener() {
        }

        @Override
        public void onReceiveData(byte[] bArr, int i) {
            VLogUtil.println("接收到的数据:" + StringUtil.bytesToHexString(bArr, i));
        }

        @Override
        public void onSendData(byte[] bArr) {
            VLogUtil.println("MainActivity 发送的数据:" + StringUtil.bytesToHexString(bArr, bArr.length));
        }

        @Override
        public void onReceiveError(String str) {
            VLogUtil.println("MainActivity 接收到的错误:" + str);
        }
    }
}