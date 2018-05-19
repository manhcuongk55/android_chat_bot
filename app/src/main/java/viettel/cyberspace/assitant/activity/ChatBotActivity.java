package viettel.cyberspace.assitant.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.balysv.materialripple.MaterialRippleLayout;
import com.google.cloud.android.speech.MessageDialogFragment;
import com.google.cloud.android.speech.R;
import com.google.cloud.android.speech.SpeechService;
import com.google.cloud.android.speech.VoiceRecorder;
import com.microsoft.speech.tts.Synthesizer;
import com.microsoft.speech.tts.Voice;
import com.wang.avi.AVLoadingIndicatorView;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import chatview.data.Message;
import chatview.data.MessageHistory;
import chatview.widget.ChatView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import viettel.cyberspace.assitant.model.BaseResponse;
import viettel.cyberspace.assitant.model.RateMessageResponse;
import viettel.cyberspace.assitant.model.ResponseMessage;
import viettel.cyberspace.assitant.model.ResponseQuestionExperts;
import viettel.cyberspace.assitant.model.ResponseQuestionMaster;
import viettel.cyberspace.assitant.model.User;
import viettel.cyberspace.assitant.rest.ApiClient;
import viettel.cyberspace.assitant.rest.ApiInterface;
import viettel.cyberspace.assitant.storage.StorageManager;

import static chatview.widget.ChatView.getMessageHistory;

public class ChatBotActivity extends AppCompatActivity implements MessageDialogFragment.Listener,
        NavigationView.OnNavigationItemSelectedListener, ChatView.RateMessageListener {


    ChatView chatView;
    ImageView sendIcon;
    boolean switchbool = true;
    boolean more = false;
    List<Uri> mSelected;
    MaterialRippleLayout micMRL;
    MaterialRippleLayout micMRLWithKeyBoard;
    MaterialRippleLayout avi;
    MaterialRippleLayout aviWithKeyBoard, notification, user_info, volume_change, logout;
    ImageView imageVolume;
    TextView tvVoice;
    boolean volume = true;
    DrawerLayout drawer;
    TextView tvUserName;
    TextView tvUserRule;
    public static
    int LIMIT_QUERY_HISTORY = 20;
    public static long currentTimeStamp = Long.MAX_VALUE;
    LinearLayout layoutLogout;

    public int COUNT_DOWNT_CALL_ANSWER;
    public final int MAX_CALL_ANSWER = 20;
    public final int TIME_TO_CALL_API_AGAIN = 1000;
    private static final String FRAGMENT_MESSAGE_DIALOG = "message_dialog";

    private static String NAME_USER_REQUEST;

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
                Log.v("trungbd", "onVoiceStart");
                mSpeechService.startRecognizing(mVoiceRecorder.getSampleRate());
            }
        }

        @Override
        public void onVoice(byte[] data, int size) {
            if (mSpeechService != null) {
                Log.v("trungbd", "onVoice");
                mSpeechService.recognize(data, size);
            }
        }

        @Override
        public void onVoiceEnd() {
            if (mSpeechService != null) {
                Log.v("trungbd", "onVoiceEnd");
                mSpeechService.finishRecognizing();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setEnableVoidButton(true);
                        stopVoiceRecorder();
                    }
                });
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
        ActiveAndroid.initialize(this);
        setContentView(R.layout.activity_main);

        messageAnswering.setMessageType(Message.MessageType.LeftSimpleMessage);
        messageAnswering.setAnswer(true);
        apiService =
                ApiClient.getClient().create(ApiInterface.class);
        chatView = findViewById(R.id.chatView);
        chatView.setRateMessageListener(this);
/*        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        //Initialization start
        mSelected = new ArrayList<>();
//        getSupportActionBar().setTitle("Virtual Assistant");
        micMRL = findViewById(R.id.micMRL2);
        micMRLWithKeyBoard = findViewById(R.id.micMRL);
        avi = findViewById(R.id.avi2);
        aviWithKeyBoard = findViewById(R.id.avi);
        notification = findViewById(R.id.notification);
        user_info = findViewById(R.id.user_info);
        volume_change = findViewById(R.id.volume_change);
        imageVolume = findViewById(R.id.imageVolume);
        logout = findViewById(R.id.logout);
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                NotificationFragment notificationFragment = new NotificationFragment();
                notificationFragment.show(fm, "NotificationFragment");
            }
        });
        user_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawer.isDrawerOpen(Gravity.RIGHT)) {
                    drawer.closeDrawer(Gravity.RIGHT);
                } else {
                    drawer.openDrawer(Gravity.RIGHT);
                }
                chatView.onBackpress();
            }
        });
        if (volume) {
            imageVolume.setImageDrawable(getResources().getDrawable(R.drawable.volume_on));
        } else imageVolume.setImageDrawable(getResources().getDrawable(R.drawable.volume_off));
        volume_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (volume) {
                    volume = false;
                    imageVolume.setImageDrawable(getResources().getDrawable(R.drawable.volume_off));
                } else {
                    volume = true;
                    imageVolume.setImageDrawable(getResources().getDrawable(R.drawable.volume_on));
                }
                changeVolume(volume);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
        aviWithKeyBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEnableVoidButton(true);
                stopVoiceRecorder();
            }
        });
        avi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEnableVoidButton(true);
                stopVoiceRecorder();
            }
        });
        tvVoice = findViewById(R.id.tvVoice);

        micMRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Start listening to voices
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO)
                        == PackageManager.PERMISSION_GRANTED) {
                    setEnableVoidButton(false);
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

                // Start listening to voices
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO)
                        == PackageManager.PERMISSION_GRANTED) {
                    setEnableVoidButton(false);
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
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                chatView.onBackpress();
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        tvUserName = header.findViewById(R.id.tvUserName);
        tvUserRule = header.findViewById(R.id.tvUserRule);
        layoutLogout = header.findViewById(R.id.layoutLogout);

        chatView.setOnClickSendButtonListener(new ChatView.OnClickSendButtonListener() {
            @Override
            public void onSendButtonClick(String body) {
                if (body.equals("")) {
                    Toast.makeText(getApplicationContext(), "Vui lòng nhập câu hỏi", Toast.LENGTH_SHORT).show();
                    return;
                }
                Message message = new Message();
                message.setBody(body);
                message.setMessageType(Message.MessageType.RightSimpleImage);
                message.setTime(getTime());
                message.setUserName("Groot");
                message.setTimeStamp(System.currentTimeMillis());
                message.setUserIcon(Uri.parse("android.resource://com.shrikanthravi.chatviewlibrary/drawable/groot"));
                chatView.addMessage(message);
                sendMessage(body, NAME_USER_REQUEST);
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
        List<MessageHistory> messageHistories = getMessageHistory();
        if (messageHistories != null && messageHistories.size() > 0) {
            for (int i = 0; i < messageHistories.size(); i++) {
                Log.v("MessageHistory   ", messageHistories.get(i).getBody() + "     " + messageHistories.get(i).getTimeStamp());
            }
            currentTimeStamp = messageHistories.get(messageHistories.size() - 1).getTimeStamp();
            Log.v("currentTimeStamp   ", currentTimeStamp + "");
            List<Message> messages = new ArrayList<>();
            for (MessageHistory messageHistory : messageHistories) {
                messages.add(messageHistory.toMessage());
            }

            chatView.addListMessage(messages);
            chatView.scrollto(0);
        }

        User user = StorageManager.getUser(this);
        if (user != null) {
            tvUserName.setText(StorageManager.getStringValue(getApplicationContext(), "account", ""));
            tvUserRule.setText(user.getUser_type());
            NAME_USER_REQUEST = StorageManager.getStringValue(getApplicationContext(), "account", "");
            if (user.getUser_type().equals("Experts"))
                getExpertsQuestion();
        }
        layoutLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(Gravity.RIGHT);
                logout();
            }
        });
    }

    public void logout() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setMessage("Bạn có muốn đăng xuất không?");
        builder.setCancelable(false);
        builder.setPositiveButton("Đăng xuất", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                StorageManager.saveUser(getApplicationContext(), null);
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();

            }
        });
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        android.support.v7.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void changeVolume(boolean volume) {
        AudioManager amanager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (volume) {
            amanager.setStreamMute(AudioManager.STREAM_MUSIC, false);
        } else {
            amanager.setStreamMute(AudioManager.STREAM_MUSIC, true);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        chatView.onDestroy();
        currentTimeStamp = Long.MAX_VALUE;
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindService(new Intent(this, SpeechService.class), mServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopVoiceRecorder();

        // Stop Cloud Speech API
        if (mSpeechService != null)
            mSpeechService.removeListener(mSpeechServiceListener);
        unbindService(mServiceConnection);
        mSpeechService = null;

    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop listening to voice

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
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

                return true;

            case R.id.notification:

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
        message.setTimeStamp(System.currentTimeMillis());
        message.setBody(text);
        message.setUserIcon(Uri.parse("android.resource://com.shrikanthravi.chatviewlibrary/drawable/groot"));
        chatView.addMessage(message);
        sendMessage(text, NAME_USER_REQUEST);
    }

    public void receiveTextFromServer(String text, String url, BaseResponse baseResponse, String nameuser, String mid, String question) {
        Message message = new Message();
        message.setMessageType(Message.MessageType.LeftSimpleMessage);
        message.setTime(getTime());
        message.setBody(text);
        message.setUserIcon(Uri.parse("android.resource://com.shrikanthravi.chatviewlibrary/drawable/hodor"));
        message.setMid(mid);
        if (baseResponse != null) {
            if (baseResponse.getMessage() != null && baseResponse.getMessage().size() > 0)
                baseResponse.getMessage().get(0).setIsfocus(true);
            message.setBaseResponse(baseResponse);
        }
        message.setTimeStamp(System.currentTimeMillis());
        message.setWebUrl(url);
        message.setQuestion(question);
        message.setAnswer(false);
        chatView.addMessage(message);
        playVoice(text);
    }


    @Override
    protected void onResume() {
        super.onResume();
        // Prepare Cloud Speech API
//        bindService(new Intent(this, SpeechService.class), mServiceConnection, BIND_AUTO_CREATE);
    }

    private void startVoiceRecorder() {
        if (m_syn != null)
            m_syn.stopSound();
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

    public static Message messageAnswering = new Message();

    private void sendMessage(final String messa, final String name_user) {

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        HashMap<String, String> map = new HashMap<>();
        map.put("username", name_user);
        map.put("message", messa);
        map.put("timestamp", timestamp.toString());
        map.put("type", "text");
        map.put("token", StorageManager.getUser(getApplicationContext()).getToken());
        map.put("userId", StorageManager.getUser(getApplicationContext()).getUserId() + "");

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
                    chatView.addMessage(messageAnswering);
                    getAnswer(s, name_user, messa);
                } else {
                    //not success
                    Log.i("duypq3", "sendMessage:not success");
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable t) {
                Log.i("duypq3", "sendMessage:onFailure");
                receiveTextFromServer(getString(R.string.error_send_message), "", null, name_user, null, "");
            }
        });

    }


    public void getExpertsQuestion() {
        HashMap<String, String> map = new HashMap<>();
        map.put("username", NAME_USER_REQUEST);
        map.put("token", StorageManager.getUser(getApplicationContext()).getToken());
        map.put("userId", StorageManager.getUser(getApplicationContext()).getUserId() + "");
        Call<ResponseQuestionExperts> call = apiService.getExpertsQuestion(map);
        call.enqueue(new Callback<ResponseQuestionExperts>() {
            @Override
            public void onResponse(Call<ResponseQuestionExperts> call, Response<ResponseQuestionExperts> response) {
                if (response.isSuccessful()) {
                } else {
                }
            }

            @Override
            public void onFailure(Call<ResponseQuestionExperts> call, Throwable throwable) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 5s = 1000ms
                        getExpertsQuestion();
                    }
                }, 10000);
            }
        });
    }

    public void setRate(final String rate, String mId, final int position) {

        HashMap<String, String> map = new HashMap<>();
        map.put("username", NAME_USER_REQUEST);
        map.put("mid", mId);
        map.put("rate", rate);
        map.put("token", StorageManager.getUser(getApplicationContext()).getToken());
        map.put("userId", StorageManager.getUser(getApplicationContext()).getUserId() + "");

        Call<RateMessageResponse> call2 = apiService.rateMessage(map);
        call2.enqueue(new Callback<RateMessageResponse>() {
            @Override
            public void onResponse(Call<RateMessageResponse> call2, retrofit2.Response<RateMessageResponse> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    //success
                    chatView.rateMessageSuccess(position, rate);
                } else {
                    //not success
                    Log.i("duypq3", "getAnswer:not success");
                    Toast.makeText(getApplicationContext(), "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RateMessageResponse> call, Throwable t) {
                Log.i("duypq3", "getAnswer:onFailure " + t.toString());
                Toast.makeText(getApplicationContext(), "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void sendQuestionMaster(String mId, String message, final int position) {
        HashMap<String, String> map = new HashMap<>();
        map.put("username", NAME_USER_REQUEST);
        map.put("mid", mId);
        map.put("token", StorageManager.getUser(getApplicationContext()).getToken());
        map.put("userId", StorageManager.getUser(getApplicationContext()).getUserId() + "");
        map.put("message", message);

        Call<ResponseQuestionMaster> call2 = apiService.sendQuestionMaster(map);
        call2.enqueue(new Callback<ResponseQuestionMaster>() {
            @Override
            public void onResponse(Call<ResponseQuestionMaster> call2, retrofit2.Response<ResponseQuestionMaster> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    chatView.sendMasterSuccess(position);
                    Toast.makeText(getApplicationContext(), "Đã gửi câu hỏi tới chuyên gia", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseQuestionMaster> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getAnswer(final String mid, final String name_user, final String question) {
        Log.i("duypq3", "getAnswer:mid=" + mid);

        HashMap<String, String> map = new HashMap<>();
        map.put("username", name_user);
        map.put("mid", mid);
        map.put("token", StorageManager.getUser(getApplicationContext()).getToken());
        map.put("userId", StorageManager.getUser(getApplicationContext()).getUserId() + "");

        Call<BaseResponse> call2 = apiService.getAnswer(map);
        call2.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call2, retrofit2.Response<BaseResponse> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    //success
                    String s = response.body().toString();
                    Log.i("duypq3", "getAnswer:success=" + s);
                    COUNT_DOWNT_CALL_ANSWER = 0;

                    Log.v("trungbd", chatView.getMessageList().toString());
                    chatView.removeMessage(messageAnswering);
                    Log.v("trungbd", chatView.getMessageList().toString());
                    receiveTextFromServer(response.body().getMessage().get(0).getText(), response.body().getMessage().get(0).getUrl(), response.body(), name_user, mid, question);
                } else {
                    //not success
                    chatView.removeMessage(messageAnswering);
                    Log.i("duypq3", "getAnswer:not success");
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Log.i("duypq3", "getAnswer:onFailure=  " + COUNT_DOWNT_CALL_ANSWER + "   " + t.toString());
                COUNT_DOWNT_CALL_ANSWER++;
                if (COUNT_DOWNT_CALL_ANSWER == MAX_CALL_ANSWER) {
                    chatView.removeMessage(messageAnswering);
                    receiveTextFromServer(getString(R.string.error_get_answer), "", null, name_user, mid, question);
                    COUNT_DOWNT_CALL_ANSWER = 0;
                } else {
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Do something after 5s = 1000ms
                            getAnswer(mid, name_user, question);
                        }
                    }, TIME_TO_CALL_API_AGAIN);

                }
            }
        });

    }


    public void playVoice(String text) {
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

            // m_syn.stopSound();
        }
    }

    @Override
    public void rateMessage(String rate, String mId, int position) {
        setRate(rate, mId, position);
    }

    @Override
    public void sendMaster(String mId, String message, int position) {
        Log.v("trungbd", "gui chuyen gia2");
        sendQuestionMaster(mId, message, position);

    }
}
