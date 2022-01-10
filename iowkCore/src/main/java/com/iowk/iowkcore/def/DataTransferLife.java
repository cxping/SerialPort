package com.iowk.iowkcore.def;

public interface DataTransferLife {
    public void onDestroy();
    public void onStart() throws SerialPortException;
}
