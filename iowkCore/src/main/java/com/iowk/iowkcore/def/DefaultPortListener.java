package com.iowk.iowkcore.def;

import android.util.Log;

import com.iowk.iowkcore.common.VLogUtil;
import com.iowk.iowkcore.serialport.ISerialPortListener;
import com.iowk.iowkcore.serialport.SerialPortOperator;


/**
 * 默认的串口实现
 */
public  abstract class DefaultPortListener implements ISerialPortListener, DataTransferLife {
    private static final String TAG = "DefaultPortListener";
    private IDataTransfer dataTransfer;
    private SerialPortOperator mSerialPortOperator;

    private String tty = "/dev/ttyS3";
    private int baudrate = 115200;


    /**
     * @param dataTransfer
     */
    public DefaultPortListener(IDataTransfer dataTransfer) {
        this.dataTransfer = dataTransfer;
    }

    /**
     * @param spoName  调用串口名称
     * @param tty      串口tty
     * @param baudrate 波特率
     * @param flag     flag
     */
    public void initPort(String spoName, String tty, int baudrate, int flag) {
        this.tty = tty;
        this.baudrate = baudrate;
        //初始化串口
        this.mSerialPortOperator = new SerialPortOperator();
        this.mSerialPortOperator.spoName = spoName;
        this.mSerialPortOperator.initSerialPort(this.tty, this.baudrate, flag);
        this.mSerialPortOperator.setListener(this);
    }

    @Override
    public void onReceiveData(byte[] bArr, int i) {
        this.dataTransfer.onReceive(bArr, i);
    }

    @Override
    public void onReceiveError(String str) {
        this.dataTransfer.onError(str);
    }

    @Override
    public void onSendData(byte[] bArr) {
        this.dataTransfer.onSendData(bArr);
    }

    @Override
    public void onDestroy() {
        if (null != this.mSerialPortOperator) {
            this.mSerialPortOperator.closeSerialPort();
            this.mSerialPortOperator.removeListener();
        }

    }

    @Override
    public void onStart() throws SerialPortException {
        if (null != this.mSerialPortOperator) {
            //如果串口打开失败，则尝试周期检查串口是否正常
            while (!this.mSerialPortOperator.openSerialPort()) {
                //如果串口无法打开则，尝试重新打开，直至成功
                try {
                    VLogUtil.println("onStart:串口打开失败!");
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }else {
            throw  new SerialPortException("SerialPort Not initPort");
        }

    }
}
