package com.sheygam.java_221_31_10_18_hw;

import android.os.Handler;
import android.os.Message;

import java.util.Random;

public class Worker extends Thread{
    public static final int NEW_COUNT = 0x01;
    public static final int CURRENT_FILE = 0x02;
    public static final int PROGRESS_STATUS = 0x03;
    public static final int DONE = 0x04;
    private Handler handler;

    public Worker(Handler.Callback callback) {
        handler = new Handler(callback);
    }

    @Override
    public void run() {
        synchronized (this) {
            Random rnd = new Random();
            int count = rnd.nextInt(15) + 1;
            Message msg = Message.obtain();
            msg.what = NEW_COUNT;
            msg.arg1 = count;
            handler.sendMessage(msg);
            for (int i = 0; i < count; i++) {
                msg = Message.obtain();
                msg.what = CURRENT_FILE;
                msg.arg1 = i + 1;
                handler.sendMessage(msg);
                for (int j = 0; j < 100; j++) {
                    msg = Message.obtain();
                    msg.what = PROGRESS_STATUS;
                    msg.arg1 = j + 1;
                    handler.sendMessage(msg);
                    try {
                        sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            handler.sendEmptyMessage(DONE);
        }

    }
}
