package com.sheygam.java_221_31_10_18_hw;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private ViewGroup textProgressWrapper;
    private TextView totalCountTxt, currentProgressTxt, totalProgressTxt, resultTxt;
    private ProgressBar statusProgress, horProgress;
    private Button startBtn,pauseBtn;
    private Worker worker;
    private boolean isPaused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textProgressWrapper = findViewById(R.id.textProgressWrapper);
        totalCountTxt = findViewById(R.id.totalCountTxt);
        currentProgressTxt = findViewById(R.id.currentProgressTxt);
        totalProgressTxt = findViewById(R.id.totalProgressTxt);
        resultTxt = findViewById(R.id.resultTxt);
        statusProgress = findViewById(R.id.statusProgress);
        horProgress = findViewById(R.id.horProgress);
        startBtn = findViewById(R.id.startBtn);
        pauseBtn = findViewById(R.id.pauseBtn);

        textProgressWrapper.setVisibility(View.INVISIBLE);
        horProgress.setVisibility(View.INVISIBLE);
        statusProgress.setVisibility(View.INVISIBLE);
        resultTxt.setVisibility(View.INVISIBLE);

        startBtn.setOnClickListener(this);
        pauseBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.startBtn){
            worker = new Worker(callback);
            worker.start();
        }else if(v.getId() == R.id.pauseBtn){
            if(worker != null && worker.isAlive()) {
                if(isPaused){
                    worker.notify();
                    isPaused = false;
                }else {
                    try {
                        worker.wait();
                        isPaused = true;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        isPaused = false;
                    }
                }
            }
        }
    }

    private Handler.Callback callback = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Worker.NEW_COUNT:
                    totalCountTxt.setText("Count: " + msg.arg1);
                    startBtn.setEnabled(false);
                    horProgress.setProgress(0);
                    totalProgressTxt.setText("" + msg.arg1);
                    currentProgressTxt.setText("0");
                    resultTxt.setVisibility(View.INVISIBLE);
                    statusProgress.setVisibility(View.VISIBLE);
                    textProgressWrapper.setVisibility(View.VISIBLE);
                    horProgress.setVisibility(View.VISIBLE);
                    break;
                case Worker.CURRENT_FILE:
                    currentProgressTxt.setText(""+msg.arg1);
                    break;
                case Worker.PROGRESS_STATUS:
//                    Log.d("MY_TAG", "handleMessage: " + msg.arg1);
                    horProgress.setProgress(msg.arg1);

                    break;
                case Worker.DONE:
                    resultTxt.setText("All done");
                    startBtn.setEnabled(true);
                    textProgressWrapper.setVisibility(View.INVISIBLE);
                    statusProgress.setVisibility(View.INVISIBLE);
                    resultTxt.setVisibility(View.VISIBLE);
                    horProgress.setVisibility(View.INVISIBLE);
                    break;
            }
            return true;
        }
    };
}
