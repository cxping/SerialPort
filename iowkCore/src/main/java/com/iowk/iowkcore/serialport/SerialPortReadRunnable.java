package com.iowk.iowkcore.serialport;

import android.os.SystemClock;

import com.iowk.iowkcore.common.VLogUtil;
import com.iowk.iowkcore.utils.StringUtil;

public class SerialPortReadRunnable implements Runnable {
    private byte[] bytesReceived = new byte[512];
    private SerialPortOperator mSerialPortOperator = null;

    public SerialPortReadRunnable(SerialPortOperator serialPortOperator) {
        this.mSerialPortOperator = serialPortOperator;
    }

    public void run() {
        StringBuilder sb;
        VLogUtil.println(this.mSerialPortOperator.spoName + "->ThreadRead线程启动");
        while (!Thread.currentThread().isInterrupted()) {
            try {
                int available = this.mSerialPortOperator.serialPort.getInputStream().available();
                if (available > 0) {
                    VLogUtil.println(this.mSerialPortOperator.spoName + "->------------准备读取数据------------");
                    int read = this.mSerialPortOperator.serialPort.getInputStream().read(this.bytesReceived);
                    VLogUtil.println(this.mSerialPortOperator.spoName + "->收到数据大小 readSize: " + available + "  receiveCount: " + read);
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(this.mSerialPortOperator.spoName);
                    sb2.append("->收到原始数据:");
                    sb2.append(StringUtil.bytesToHexString(this.bytesReceived, read));
                    VLogUtil.println(sb2.toString());
                    reportData(this.bytesReceived, read);
                }
                SystemClock.sleep(200);
            } catch (Exception e) {
                VLogUtil.println(this.mSerialPortOperator.spoName + "->ThreadRead线程(catch):" + e.toString());
                reportError(e.toString());
                sb = new StringBuilder();
            } catch (Throwable th) {
                VLogUtil.println(this.mSerialPortOperator.spoName + "->ThreadRead 线程(finally)");
                this.mSerialPortOperator.dispose();
                throw th;
            }
        }
        VLogUtil.println(this.mSerialPortOperator.spoName + "->ThreadRead run() 线程正常退出");
        sb = new StringBuilder();
        sb.append(this.mSerialPortOperator.spoName);
        sb.append("->ThreadRead 线程(finally)");
        VLogUtil.println(sb.toString());
        this.mSerialPortOperator.dispose();
    }

    private void reportData(byte[] bArr, int i) {
        if (this.mSerialPortOperator.iSerialPortListener != null) {
            this.mSerialPortOperator.iSerialPortListener.onReceiveData(bArr, i);
        }
    }

    private void reportError(String str) {
        if (this.mSerialPortOperator.iSerialPortListener != null) {
            this.mSerialPortOperator.iSerialPortListener.onReceiveError(str);
        }
    }
}
