package com.example.GameEnglish;

import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;

import java.util.Locale;

public class TTS extends UtteranceProgressListener implements TextToSpeech.OnInitListener, TextToSpeech.OnUtteranceCompletedListener {

    public static TTS myTTS;

    public static TTS getInstance(Context context) {
        if (myTTS == null) {
            myTTS = new TTS(context);
        }
        return myTTS;
    }

    private Context context;
    private TextToSpeech tts;
    private Locale locale = Locale.getDefault();
    private String enginePackageName;
    private String message;
    private boolean isRunning;
    private int speakCount;

    public TTS(Context context) {
        this.context = context;
    }

    public void speak(String message) {
        this.message = message;

        if (tts == null || !isRunning) {
            speakCount = 0;

            if (enginePackageName != null && !enginePackageName.isEmpty()) {
                tts = new TextToSpeech(context, this, enginePackageName);
            } else {
                tts = new TextToSpeech(context, this);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                tts.setOnUtteranceProgressListener(this);
            } else {
                tts.setOnUtteranceCompletedListener(this);
            }

            isRunning = true;

        } else {
            startSpeak();
            //while (tts.isSpeaking()){}
        }
    }

    public TTS setEngine(String packageName) {
        enginePackageName = packageName;
        return this;
    }

    public TTS setLocale(Locale locale) {
        this.locale = locale;
        return this;
    }

    private void startSpeak() {
        speakCount++;
        if (locale != null) {
            tts.setLanguage(locale.ENGLISH);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(message, TextToSpeech.QUEUE_FLUSH, null, "");
        } else {
            tts.speak(message, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    private void clear() {
        speakCount--;

        if (speakCount == 0) {
            tts.shutdown();
            isRunning = false;
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            startSpeak();
        }
    }

    @Override
    public void onStart(String utteranceId) {
    }

    @Override
    public void onDone(String utteranceId) {
        clear();
    }

    @Override
    public void onError(String utteranceId) {
        clear();
    }

    @Override
    public void onUtteranceCompleted(String utteranceId) {
        clear();
    }
}

