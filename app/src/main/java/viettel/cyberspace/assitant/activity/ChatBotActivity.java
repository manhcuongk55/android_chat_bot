package viettel.cyberspace.assitant.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.github.zagum.expandicon.ExpandIconView;
import com.google.cloud.android.speech.MessageDialogFragment;
import com.google.cloud.android.speech.R;
import com.google.cloud.android.speech.SpeechService;
import com.google.cloud.android.speech.VoiceRecorder;
import com.wang.avi.AVLoadingIndicatorView;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import chatview.data.Message;
import chatview.widget.ChatView;

public class ChatBotActivity extends AppCompatActivity implements MessageDialogFragment.Listener {


    ChatView chatView;
    ImageView sendIcon;
    EditText messageET;
    boolean switchbool = true;
    boolean more = false;
    List<Uri> mSelected;
    MaterialRippleLayout micMRL;
    AVLoadingIndicatorView avi;
    TextView tvVoice;


    private static final String FRAGMENT_MESSAGE_DIALOG = "message_dialog";

    private static final String STATE_RESULTS = "results";

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 1;

    private SpeechService mSpeechService;

    private VoiceRecorder mVoiceRecorder;
    private final VoiceRecorder.Callback mVoiceCallback = new VoiceRecorder.Callback() {

        @Override
        public void onVoiceStart() {
            if (mSpeechService != null) {
                mSpeechService.startRecognizing(mVoiceRecorder.getSampleRate());
            }
        }

        @Override
        public void onVoice(byte[] data, int size) {
            if (mSpeechService != null) {
                mSpeechService.recognize(data, size);
            }
        }

        @Override
        public void onVoiceEnd() {
            if (mSpeechService != null) {
                mSpeechService.finishRecognizing();
            }
        }

    };


    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder binder) {
            mSpeechService = SpeechService.from(binder);
            mSpeechService.addListener(mSpeechServiceListener);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mSpeechService = null;
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_bot);

        chatView = findViewById(R.id.chatView);

        messageET = findViewById(R.id.messageET);
        messageET.requestFocus();

        //Initialization start
        mSelected = new ArrayList<>();

        micMRL = findViewById(R.id.micMRL2);
        avi = findViewById(R.id.avi2);
        tvVoice= findViewById(R.id.tvVoice);

        micMRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEnableVoidButton(false);
                // Start listening to voices
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO)
                        == PackageManager.PERMISSION_GRANTED) {
                    startVoiceRecorder();
                } else if (ActivityCompat.shouldShowRequestPermissionRationale(ChatBotActivity.this,
                        Manifest.permission.RECORD_AUDIO)) {
                    showPermissionMessageDialog();
                } else {
                    ActivityCompat.requestPermissions(ChatBotActivity.this, new String[]{Manifest.permission.RECORD_AUDIO},
                            REQUEST_RECORD_AUDIO_PERMISSION);
                }
            }
        });


        //Send button click listerer
//        chatView.setOnClickSendButtonListener(new ChatView.OnClickSendButtonListener() {
//            @Override
//            public void onSendButtonClick(String body) {
//                if (switchbool) {
//                    Message message = new Message();
//                    message.setBody(body);
//                    message.setMessageType(Message.MessageType.RightSimpleImage);
//                    message.setTime(getTime());
//                    message.setUserName("Groot");
//                    message.setUserIcon(Uri.parse("android.resource://com.shrikanthravi.chatviewlibrary/drawable/groot"));
//                    chatView.addMessage(message);
//
//                    switchbool = false;
//                } else {
//                    Message message1 = new Message();
//                    message1.setBody(body);
//                    message1.setMessageType(Message.MessageType.ListQuestion);
//                    message1.setTime(getTime());
//                    message1.setUserName("Hodor");
//                    message1.setUserIcon(Uri.parse("android.resource://com.shrikanthravi.chatviewlibrary/drawable/hodor"));
//                    chatView.addMessage(message1);
//
//                    switchbool = true;
//                }
//            }
//        });

    }


    @Override
    protected void onPause() {
        super.onPause();
        // Stop listening to voice
        stopVoiceRecorder();

        // Stop Cloud Speech API
        mSpeechService.removeListener(mSpeechServiceListener);
        unbindService(mServiceConnection);
        mSpeechService = null;

    }


    public String getTime() {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("dd MMM yyyy HH:mm");
        String time = mdformat.format(calendar.getTime());
        return time;
    }


    @Override
    public void onMessageDialogDismissed() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},
                REQUEST_RECORD_AUDIO_PERMISSION);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    private final SpeechService.Listener mSpeechServiceListener =
            new SpeechService.Listener() {
                @Override
                public void onSpeechRecognized(final String text, final boolean isFinal) {
                    Log.i("duypq3", "text=" + text + "  isFinal  = " + isFinal);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (isFinal) {
                                setEnableVoidButton(true);
                                micMRL.setEnabled(false);
                                micMRL.setAlpha(0.4f);
                                tvVoice.setText(text);

                                if (switchbool) {
                                    Message message = new Message();
                                    message.setMessageType(Message.MessageType.LeftSimpleMessage);
                                    message.setTime(getTime());
                                    message.setUserName("Groot");
                                    message.setBody(text);
                                    message.setUserIcon(Uri.parse("android.resource://com.shrikanthravi.chatviewlibrary/drawable/groot"));
                                    chatView.addMessage(message);
                                    switchbool = false;
                                } else {
                                    Message message = new Message();

                                    message.setMessageType(Message.MessageType.RightSimpleImage);
                                    message.setTime(getTime());
                                    message.setUserName("Hodor");
                                    message.setBody(text);
                                    message.setUserIcon(Uri.parse("android.resource://com.shrikanthravi.chatviewlibrary/drawable/hodor"));
                                    chatView.addMessage(message);
                                    switchbool = true;
                                }
                                tvVoice.setText("");
                            } else {
                                tvVoice.setText(text);
                            }
                        }
                    });

                    if (isFinal) {
                        Long s1 = System.currentTimeMillis();
                        stopVoiceRecorder();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                micMRL.setEnabled(true);
                                micMRL.setAlpha(1.0f);
                            }
                        });
                        Log.i("duypq4", "time1=" + (System.currentTimeMillis() - s1));
                    }
                }
            };

    public void setEnableVoidButton(boolean isEnable) {
        if (isEnable) {
            micMRL.setVisibility(View.VISIBLE);
            avi.setVisibility(View.GONE);
        } else {
            micMRL.setVisibility(View.GONE);
            avi.setVisibility(View.VISIBLE);

        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        // Prepare Cloud Speech API
        bindService(new Intent(this, SpeechService.class), mServiceConnection, BIND_AUTO_CREATE);
    }

    private void startVoiceRecorder() {
        if (mVoiceRecorder != null) {
            mVoiceRecorder.stop();
        }
        mVoiceRecorder = new VoiceRecorder(mVoiceCallback);
        mVoiceRecorder.start();
    }

    private void stopVoiceRecorder() {
        if (mVoiceRecorder != null) {
            mVoiceRecorder.stop();
            mVoiceRecorder = null;
        }
    }

    private void showPermissionMessageDialog() {
        MessageDialogFragment
                .newInstance(getString(R.string.permission_message))
                .show(getSupportFragmentManager(), FRAGMENT_MESSAGE_DIALOG);
    }

}
