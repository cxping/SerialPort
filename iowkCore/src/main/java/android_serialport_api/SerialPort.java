/*
 * Copyright 2009 Cedric Priscal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android_serialport_api;

import com.iowk.iowkcore.common.VLogUtil;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SerialPort {

    private static final String TAG = "SerialPort";

    /*
     * Do not remove or rename the field mFd: it is used by native method close();
     */
    private FileDescriptor mFd;
    private FileInputStream mFileInputStream;
    private FileOutputStream mFileOutputStream;
    private int baudRate;
    private File device;
    private int flags;

    public SerialPort(File device, int baudrate, int flags) {
        /* Check access permission */
        if (!device.canRead() || !device.canWrite()) {
            try {
                /* Missing read/write permission, trying to chmod the file */
                Process su;
                su = Runtime.getRuntime().exec("/system/bin/su");
                VLogUtil.println("SerialPort:初始化串口:" + device + " /system/bin/su");
                String cmd = "chmod 666 " + device.getAbsolutePath() + "\n"
                        + "exit\n";
                su.getOutputStream().write(cmd.getBytes());
                if ((su.waitFor() != 0) || !device.canRead()
                        || !device.canWrite()) {
                    VLogUtil.println(device + "SerialPort:初始化串口 出错 ");
//                    throw new SecurityException();
                }
            } catch (Exception e) {
                e.printStackTrace();
                VLogUtil.println(device + " SerialPort:初始化串口 出错:" + e.toString());
            }
            this.baudRate = baudrate;
            this.device = device;
            this.flags = flags;
        }
    }

    public boolean open() {
        try {
            this.mFd = open(this.device.getAbsolutePath(), this.baudRate, this.flags);
            if (this.mFd == null) {
                VLogUtil.println("SerialPort:open() is null");
                return false;
            }
            this.mFileInputStream = new FileInputStream(this.mFd);
            this.mFileOutputStream = new FileOutputStream(this.mFd);
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    // Getters and setters
    public InputStream getInputStream() {
        return mFileInputStream;
    }

    public OutputStream getOutputStream() {
        return mFileOutputStream;
    }


    // JNI
    private native static FileDescriptor open(String path, int baudrate, int flags);

    public native void close();

    static {
        System.loadLibrary("iowkcore");
    }
    public boolean closeStream() {
        boolean z;
        FileInputStream fileInputStream = this.mFileInputStream;
        if (fileInputStream != null) {
            try {
                fileInputStream.close();
                this.mFileInputStream = null;
            } catch (IOException e) {
                VLogUtil.println("SerialPort:mFileInputStream closeStream()" + e.toString());
                z = false;
            }
        }
        z = true;
        FileOutputStream fileOutputStream = this.mFileOutputStream;
        if (fileOutputStream == null) {
            return z;
        }
        try {
            fileOutputStream.close();
            this.mFileOutputStream = null;
            return z;
        } catch (IOException e2) {
            VLogUtil.println("SerialPort:mFileOutputStream closeStream()" + e2.toString());
            return false;
        }
    }

    //彻底关闭
    public void shutOff() {
        close();
        this.mFd = null;
    }
}
