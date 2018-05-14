package viettel.cyberspace.assitant.activity;

import android.Manifest;
import android.app.AlertDialog;
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
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomNavigationView.OnNavigationItemReselectedListener;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.github.zagum.expandicon.ExpandIconView;
import com.google.cloud.android.speech.MessageDialogFragment;
import com.google.cloud.android.speech.R;
import com.google.cloud.android.speech.SpeechService;
import com.google.cloud.android.speech.VoiceRecorder;
import com.microsoft.speech.tts.Synthesizer;
import com.microsoft.speech.tts.Voice;
import com.wang.avi.AVLoadingIndicatorView;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import chatview.data.Message;
import chatview.widget.ChatView;
import retrofit2.Call;
import retrofit2.Callback;
import viettel.cyberspace.assitant.model.BaseResponse;
import viettel.cyberspace.assitant.model.Response;
import viettel.cyberspace.assitant.model.ResponseMessage;
import viettel.cyberspace.assitant.rest.ApiClient;
import viettel.cyberspace.assitant.rest.ApiInterface;

public class ChatBotActivity extends AppCompatActivity implements MessageDialogFragment.Listener, NavigationView.OnNavigationItemSelectedListener {


    ChatView chatView;
    ImageView sendIcon;
    boolean switchbool = true;
    boolean more = false;
    List<Uri> mSelected;
    MaterialRippleLayout micMRL;
    MaterialRippleLayout micMRLWithKeyBoard;
    AVLoadingIndicatorView avi;
    AVLoadingIndicatorView aviWithKeyBoard;
    TextView tvVoice;

    DrawerLayout drawer;
    private static final String FRAGMENT_MESSAGE_DIALOG = "message_dialog";

    private static final String STATE_RESULTS = "results";

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 1;

    private SpeechService mSpeechService;

    ApiInterface apiService;

    private Synthesizer m_syn;

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
        setContentView(R.layout.activity_main);
        apiService =
                ApiClient.getClient().create(ApiInterface.class);
        chatView = findViewById(R.id.chatView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Initialization start
        mSelected = new ArrayList<>();
        getSupportActionBar().setTitle("Virtual Assistant");
        micMRL = findViewById(R.id.micMRL2);
        micMRLWithKeyBoard = findViewById(R.id.micMRL);
        avi = findViewById(R.id.avi2);
        aviWithKeyBoard = findViewById(R.id.avi);
        tvVoice = findViewById(R.id.tvVoice);

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

        micMRLWithKeyBoard.setOnClickListener(new View.OnClickListener() {
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

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
/*        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();*/

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
/*        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (drawer.isDrawerOpen(Gravity.RIGHT)) {
                    drawer.closeDrawer(Gravity.RIGHT);
                } else {
                    drawer.openDrawer(Gravity.RIGHT);
                }
            }
        });*/
        //Send button click listerer
        chatView.setOnClickSendButtonListener(new ChatView.OnClickSendButtonListener() {
            @Override
            public void onSendButtonClick(String body) {
                if (switchbool) {
                    Message message = new Message();
                    message.setBody(body);
                    message.setMessageType(Message.MessageType.RightSimpleImage);
                    message.setTime(getTime());
                    message.setUserName("Groot");
                    message.setUserIcon(Uri.parse("android.resource://com.shrikanthravi.chatviewlibrary/drawable/groot"));
                    chatView.addMessage(message);

                    switchbool = false;
                } else {
                    Message message1 = new Message();
                    message1.setBody(body);
                    message1.setMessageType(Message.MessageType.LeftSimpleMessage);
                    message1.setTime(getTime());
                    message1.setUserName("Hodor");
                    message1.setUserIcon(Uri.parse("android.resource://com.shrikanthravi.chatviewlibrary/drawable/hodor"));
                    chatView.addMessage(message1);

                    switchbool = true;
                }
            }
        });

        if (getString(R.string.api_key).startsWith("Please")) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.add_subscription_key_tip_title))
                    .setMessage(getString(R.string.add_subscription_key_tip))
                    .setCancelable(false)
                    .show();
        } else {
            if (m_syn == null) {
                // Create Text To Speech Synthesizer.
                m_syn = new Synthesizer(getString(R.string.api_key));
            }
        }

    }


    @Override
    protected void onPause() {
        super.onPause();
        // Stop listening to voice
        stopVoiceRecorder();

        // Stop Cloud Speech API
        if (mSpeechService != null)
            mSpeechService.removeListener(mSpeechServiceListener);
        unbindService(mServiceConnection);
        mSpeechService = null;

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (chatView.onBackpress()) return;
        else
            super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chatview_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.user_information:
                if (drawer.isDrawerOpen(Gravity.RIGHT)) {
                    drawer.closeDrawer(Gravity.RIGHT);
                } else {
                    drawer.openDrawer(Gravity.RIGHT);
                }
                chatView.onBackpress();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

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
                                micMRLWithKeyBoard.setEnabled(false);
                                micMRLWithKeyBoard.setAlpha(0.4f);
                                tvVoice.setText(text);


                                getTextFromVoice(text);
                                //test
                                //  receiveTextFromServer("Biet Rui");

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
                                micMRLWithKeyBoard.setEnabled(true);
                                micMRLWithKeyBoard.setAlpha(1.0f);
                            }
                        });
                        Log.i("duypq4", "time1=" + (System.currentTimeMillis() - s1));
                    }
                }
            };

    public void setEnableVoidButton(boolean isEnable) {
        if (isEnable) {
            micMRL.setVisibility(View.VISIBLE);
            micMRLWithKeyBoard.setVisibility(View.VISIBLE);
            avi.setVisibility(View.GONE);
            aviWithKeyBoard.setVisibility(View.GONE);
        } else {
            micMRL.setVisibility(View.GONE);
            micMRLWithKeyBoard.setVisibility(View.GONE);
            avi.setVisibility(View.VISIBLE);
            aviWithKeyBoard.setVisibility(View.VISIBLE);
        }
    }


    public void getTextFromVoice(String text) {
        Message message = new Message();
        message.setMessageType(Message.MessageType.RightSimpleImage);
        message.setTime(getTime());
        message.setBody(text);
        message.setUserIcon(Uri.parse("android.resource://com.shrikanthravi.chatviewlibrary/drawable/groot"));
        chatView.addMessage(message);

        sendMessage(text, "fafa");
    }

    public void receiveTextFromServer(String text, String nameuser, String mid) {
        Message message = new Message();
        message.setMessageType(Message.MessageType.LeftSimpleMessage);
        message.setTime(getTime());
        message.setBody(text);
        message.setUserIcon(Uri.parse("android.resource://com.shrikanthravi.chatviewlibrary/drawable/hodor"));
        message.setMid(mid);
        chatView.addMessage(message);
        playVoice(text);
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.END);
        return true;
    }

    private void sendMessage(String messa, final String nameuser) {

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        HashMap<String, String> map = new HashMap<>();
        map.put("username", "namnh475");
        map.put("message", messa);
        map.put("timestamp", timestamp.toString());
        map.put("type", "text");

        Call<ResponseMessage> call = apiService.sendMessage(map);
        call.enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, retrofit2.Response<ResponseMessage> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    //success
                    Log.i("duypq3", "sendMessage:success");
                    String s = response.body().getMid();
                    Log.i("duypq3", "sendMessage:success=" + s);
                    getAnswer(s, nameuser);
                } else {
                    //not success
                    Log.i("duypq3", "sendMessage:not success");
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable t) {
                Log.i("duypq3", "sendMessage:onFailure");
                receiveTextFromServer(getString(R.string.error_network), nameuser, null);
            }
        });

    }

    private void getAnswer(final String mid, final String nameuser) {
        Log.i("duypq3", "getAnswer:mid=" + mid);

        HashMap<String, String> map = new HashMap<>();
        map.put("username", nameuser);
        map.put("mid", mid);

        Call<BaseResponse> call2 = apiService.getAnswer(map);
        call2.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call2, retrofit2.Response<BaseResponse> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    //success
                    Log.i("duypq3", "getAnswer:success");
                    String s = response.body().getMessage().toString();
                    Log.i("duypq3", "getAnswer:success=" + s);

                    receiveTextFromServer(response.body().getMessage().getText(), nameuser, mid);
                } else {
                    //not success
                    Log.i("duypq3", "getAnswer:not success");
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Log.i("duypq3", "getAnswer:onFailure");
                receiveTextFromServer(getString(R.string.error_network), nameuser, mid);
            }
        });

    }


    public void playVoice(String text){
        if (getString(R.string.api_key).startsWith("Please")) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.add_subscription_key_tip_title))
                    .setMessage(getString(R.string.add_subscription_key_tip))
                    .setCancelable(false)
                    .show();
        } else {
            if (m_syn == null) {
                // Create Text To Speech Synthesizer.
                m_syn = new Synthesizer(getString(R.string.api_key));
            }
            m_syn.SetServiceStrategy(Synthesizer.ServiceStrategy.AlwaysService);

            Voice v = new Voice("vi-VN", "Microsoft Server Speech Text to Speech Voice (vi-VN, An)", Voice.Gender.Male, true);
            m_syn.SetVoice(v, null);
            // Use a string for speech.
            m_syn.SpeakToAudio(text);
        }
    }
}
