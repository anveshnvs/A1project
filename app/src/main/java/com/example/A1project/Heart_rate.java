package com.example.A1project;


import android.app.Activity;
import android.graphics.Bitmap;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.TextureView;

import java.util.concurrent.CopyOnWriteArrayList;

public class Heart_rate {
    private final Activity activity;
    private AbsKeep absKeep;
    private final int inter = 45;
    private final int mlength = 45000;
    private final int clength = 3500;
    private int valley = 0;
    private int tickp = 0;
    private final CopyOnWriteArrayList<Long> valleys = new CopyOnWriteArrayList<>();
    private CountDownTimer timer;
    private final Handler mainHandler;

    Heart_rate(Activity activity, Handler mainHandler) {
        this.activity = activity;
        this.mainHandler = mainHandler;
    }

    private boolean checkValley() {
        final int valleyWindowSize = 13;
        CopyOnWriteArrayList<Abs<Integer>> subList = absKeep.getLastStdValues(valleyWindowSize);
        if (subList.size() < valleyWindowSize) {
            return false;
        } else {
            Integer referenceValue = subList.get((int) Math.ceil(valleyWindowSize / 2f)).val;
            for (Abs<Integer> measurement : subList) {
                if (measurement.val < referenceValue) return false;
            }
            return (!subList.get((int) Math.ceil(valleyWindowSize / 2f)).val.equals(
                    subList.get((int) Math.ceil(valleyWindowSize / 2f) - 1).val));
        }
    }
    void measureHeartRate(TextureView textureView, Connection_Camera cameraService) {
        absKeep = new AbsKeep();
        valley = 0;
        timer = new CountDownTimer(mlength, inter) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (clength > (++tickp * inter)) return;
                sendMessage(MainActivity.MESSAGE_TIMER_VALUE, millisUntilFinished / 1000 + "s");
                Thread thread = new Thread(() -> {
                    Bitmap currentBitmap = textureView.getBitmap();
                    int pixelCount = textureView.getWidth() * textureView.getHeight();
                    int reading = 0;
                    int[] pixels = new int[pixelCount];
                    currentBitmap.getPixels(pixels, 0, textureView.getWidth(), 0, 0, textureView.getWidth(), textureView.getHeight());
                    for (int pixelIndex = 0; pixelIndex < pixelCount; pixelIndex++) {
                        reading += (pixels[pixelIndex] >> 16) & 0xff;
                    }
                    absKeep.add(reading);
                    if (checkValley()) {
                        valley = valley + 1;
                        valleys.add(absKeep.getLastTimestamp().getTime());
                    }
                });
                thread.start();
            }

            @Override
            public void onFinish() {
                String currentValue = String.valueOf(60f * (valley - 1) / (Math.max(1, (valleys.get(valleys.size() - 1) - valleys.get(0)) / 1000f)));
                sendMessage(MainActivity.MESSAGE_HEART_RATE_FINAL, currentValue);
                cameraService.stop();
            }
        };
        timer.start();
    }

    void stop() {
        if (timer != null) {
            timer.cancel();
        }
    }

    void sendMessage(int what, Object message) {
        Message msg = new Message();
        msg.what = what;
        msg.obj = message;
        mainHandler.sendMessage(msg);
    }
}

