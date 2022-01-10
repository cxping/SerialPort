package com.iowk.iowkcore.def;


public interface IDataTransfer {
    public void onReceive(byte[] data, int iArr);

    public void onError(String str);

    public void onSendData(byte[] bArr);
}
