package com.iowk.iowkcore.serialport;

import android.os.SystemClock;

import com.iowk.iowkcore.common.VLogUtil;
import com.iowk.iowkcore.utils.StringUtil;

import java.io.File;

import android_serialport_api.SerialPort;

public class SerialPortOperator {
    ISerialPortListener iSerialPortListener = null;
    boolean openState = false;
    SerialPort serialPort = null;
    public String spoName = "spoName";
    Thread threadReadData = null;

    public void initSerialPort(String str, int i, int i2) {
        if (this.serialPort == null) {
            this.serialPort = new SerialPort(new File(str), i, i2);
            VLogUtil.println(this.spoName + "->initSerialPort() 初始化串口");
        }
        VLogUtil.println(this.spoName + "->initSerialPort() 串口已经被初始化");
    }

    public void setListener(ISerialPortListener iSerialPortListener2) {
        this.iSerialPortListener = iSerialPortListener2;
    }

    public void removeListener() {
        this.iSerialPortListener = null;
    }

    public boolean openSerialPort() {
        if (this.openState) {
            VLogUtil.println(this.spoName + "->SerialPortOperator openSerialPort() 此串口已经被打开");
            return this.openState;
        }
        SerialPort serialPort2 = this.serialPort;
        if (serialPort2 == null) {
            VLogUtil.println(this.spoName + "->SerialPortOperator openSerialPort() 请先初始化串口");
            return false;
        }
        if (serialPort2 != null) {
            this.openState = serialPort2.open();
            VLogUtil.println(this.spoName + "->SerialPortOperator openSerialPort() openState:" + this.openState);
            if (this.threadReadData == null && this.openState) {
                this.threadReadData = new Thread(new SerialPortReadRunnable(this));
                Thread thread = this.threadReadData;
                thread.setName(this.spoName + "_SerailPortReadRunable");
                this.threadReadData.start();
            }
        }
        return this.openState;
    }

    public void exitReadThread() {
        Thread thread = this.threadReadData;
        if (thread != null) {
            thread.interrupt();
        }
    }

    public void closeSerialPort() {
        VLogUtil.println(this.spoName + "->SerialPortOperator closeSerialPort() 开始终止线程");
        exitReadThread();
    }

    public void dispose() {
        if (this.threadReadData != null) {
            this.threadReadData = null;
        }
        SerialPort serialPort2 = this.serialPort;
        if (serialPort2 != null) {
            boolean closeStream = serialPort2.closeStream();
            VLogUtil.println(this.spoName + "->SerialPortOperator dispose() 关闭流结果:" + closeStream);
            this.openState = false;
            this.serialPort.shutOff();
            this.serialPort = null;
            VLogUtil.println(this.spoName + "->SerialPortOperator dispose() openState:" + this.openState);
        }
    }

    public boolean sendData(byte[] bArr) {
        if (this.serialPort == null || bArr == null) {
            return false;
        }
        try {
            SystemClock.sleep(10);
            this.serialPort.getOutputStream().write(bArr);
            this.serialPort.getOutputStream().flush();
            VLogUtil.println(this.spoName + "->SerialPortOperator sendData(" + StringUtil.bytesToHexString(bArr, bArr.length) + ") 长度: " + bArr.length + " 发送结果:" + true);
            if (this.iSerialPortListener != null) {
                this.iSerialPortListener.onSendData(bArr);
            }
            return true;
        } catch (Exception e) {
            ISerialPortListener iSerialPortListener2 = this.iSerialPortListener;
            if (iSerialPortListener2 == null) {
                return false;
            }
            iSerialPortListener2.onReceiveError(e.toString());
            return false;
        }
    }
}
