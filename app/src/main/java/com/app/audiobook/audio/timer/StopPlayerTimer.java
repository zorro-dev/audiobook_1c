package com.app.audiobook.audio.timer;

import android.os.Handler;

public class StopPlayerTimer {

    public interface TimerCallbacks {

        void onTimerStarted();

        void onTimerFinished();

    }

    private TimerCallbacks timerCallbacks;

    public TimerCallbacks getTimerCallbacks() {
        return timerCallbacks;
    }

    public void setTimerCallbacks(TimerCallbacks timerCallbacks) {
        this.timerCallbacks = timerCallbacks;
    }

    private Handler handler;
    private Runnable runnable;
    private boolean isStarted = false;
    private int selectedTimeIndex = -1;

    public int getSelectedTimeIndex() {
        return selectedTimeIndex;
    }

    public void setSelectedTimeIndex(int selectedTimeIndex) {
        this.selectedTimeIndex = selectedTimeIndex;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public void startTimer(int sec){

        isStarted = true;

        if(timerCallbacks != null){
            timerCallbacks.onTimerStarted();
        }

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                isStarted = false;
                selectedTimeIndex = -1;
                if(timerCallbacks != null){
                    timerCallbacks.onTimerFinished();
                }
            }
        };

        handler.postDelayed(runnable, sec * 1000);
    }

    public void stopTimer(){
        isStarted = false;
        selectedTimeIndex = -1;
        if(handler != null){
            handler.removeCallbacks(runnable);
        }
    }
}
