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
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
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

public class ChatViewActivity extends AppCompatActivity implements MessageDialogFragment.Listener {


    HorizontalScrollView moreHSV;
    ExpandIconView expandIconView;
    MaterialRippleLayout galleryMRL;
    public static int imagePickerRequestCode = 10;
    public static int SELECT_VIDEO = 11;
    public static int CAMERA_REQUEST = 12;
    public static int SELECT_AUDIO = 13;
    ChatView chatView;
    ImageView sendIcon;
    EditText messageET;
    boolean switchbool = true;
    boolean more = false;
    List<Uri> mSelected;
    MaterialRippleLayout micMRL;
    AVLoadingIndicatorView avi;


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

    // Resource caches
    private int mColorHearing;
    private int mColorNotHearing;

    // View references
//    private TextView mStatus;
//    private TextView mText;
//    private RecyclerView mRecyclerView;

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
        setContentView(R.layout.activity_chat_view);

        chatView = findViewById(R.id.chatView);

        messageET = findViewById(R.id.messageET);
        messageET.requestFocus();

        //Initialization start
        moreHSV = findViewById(R.id.moreLL1);
        expandIconView = findViewById(R.id.expandIconView1);
        expandIconView.setState(1, false);
        galleryMRL = findViewById(R.id.galleryMRL1);
        mSelected = new ArrayList<>();

        micMRL = findViewById(R.id.micMRL);
        avi = findViewById(R.id.avi);

        micMRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEnableVoidButton(false);
                // Start listening to voices
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO)
                        == PackageManager.PERMISSION_GRANTED) {
                    startVoiceRecorder();
                } else if (ActivityCompat.shouldShowRequestPermissionRationale(ChatViewActivity.this,
                        Manifest.permission.RECORD_AUDIO)) {
                    showPermissionMessageDialog();
                } else {
                    ActivityCompat.requestPermissions(ChatViewActivity.this, new String[]{Manifest.permission.RECORD_AUDIO},
                            REQUEST_RECORD_AUDIO_PERMISSION);
                }
            }
        });
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
                    message1.setMessageType(Message.MessageType.ListSuggestion);
                    message1.setTime(getTime());
                    message1.setUserName("Hodor");
                    message1.setUserIcon(Uri.parse("android.resource://com.shrikanthravi.chatviewlibrary/drawable/hodor"));
                    chatView.addMessage(message1);

                    switchbool = true;
                }
            }
        });

        //Gallery button click listener
        chatView.setOnClickGalleryButtonListener(new ChatView.OnClickGalleryButtonListener() {
            @Override
            public void onGalleryButtonClick() {
                Matisse.from(ChatViewActivity.this)
                        .choose(MimeType.allOf())
                        .countable(true)
                        .maxSelectable(9)
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                        .thumbnailScale(0.85f)
                        .imageEngine(new PicassoEngine())
                        .forResult(imagePickerRequestCode);
            }
        });

        //Video button click listener
        chatView.setOnClickVideoButtonListener(new ChatView.OnClickVideoButtonListener() {
            @Override
            public void onVideoButtonClick() {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                i.setType("video/*");
                startActivityForResult(i, SELECT_VIDEO);
            }
        });

        //Camera button click listener
        chatView.setOnClickCameraButtonListener(new ChatView.OnClickCameraButtonListener() {
            @Override
            public void onCameraButtonClicked() {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file = new File(Environment.getExternalStorageDirectory(), "MyPhoto.jpg");
                file.delete();
                File file1 = new File(Environment.getExternalStorageDirectory(), "MyPhoto.jpg");

                Uri uri = FileProvider.getUriForFile(ChatViewActivity.this, getApplicationContext().getPackageName() + ".provider", file1);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });


        chatView.setOnClickAudioButtonListener(new ChatView.OnClickAudioButtonListener() {
            @Override
            public void onAudioButtonClicked() {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("audio/*");
                //String[] mimetypes = {"audio/3gp", "audio/AMR", "audio/mp3"};
                //i.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
                startActivityForResult(i, SELECT_AUDIO);
            }
        });


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


    public static String getRandomText() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(30);
        char tempChar;
        for (int i = 0; i < randomLength; i++) {
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        switch (requestCode) {
            case 10: {

                //Image Selection result
                if (resultCode == RESULT_OK) {
                    mSelected = Matisse.obtainResult(data);

                    if (switchbool) {
                        if (mSelected.size() == 1) {
                            Message message = new Message();
                            message.setBody(messageET.getText().toString().trim());
                            message.setMessageType(Message.MessageType.RightSingleImage);
                            message.setTime(getTime());
                            message.setUserName("Groot");
                            message.setImageList(mSelected);
                            message.setUserIcon(Uri.parse("android.resource://com.shrikanthravi.chatviewlibrary/drawable/groot"));
                            chatView.addMessage(message);
                            switchbool = false;
                        } else {

                            Message message = new Message();
                            message.setBody(messageET.getText().toString().trim());
                            message.setMessageType(Message.MessageType.RightMultipleImages);
                            message.setTime(getTime());
                            message.setUserName("Groot");
                            message.setImageList(mSelected);
                            message.setUserIcon(Uri.parse("android.resource://com.shrikanthravi.chatviewlibrary/drawable/groot"));
                            chatView.addMessage(message);
                            switchbool = false;
                        }
                    } else {

                        if (mSelected.size() == 1) {
                            Message message = new Message();
                            message.setBody(messageET.getText().toString().trim());
                            message.setMessageType(Message.MessageType.LeftSingleImage);
                            message.setTime(getTime());
                            message.setUserName("Hodor");
                            message.setImageList(mSelected);
                            message.setUserIcon(Uri.parse("android.resource://com.shrikanthravi.chatviewlibrary/drawable/hodor"));
                            chatView.addMessage(message);
                            switchbool = true;
                        } else {

                            Message message = new Message();
                            message.setBody(messageET.getText().toString().trim());
                            message.setMessageType(Message.MessageType.LeftMultipleImages);
                            message.setTime(getTime());
                            message.setUserName("Hodor");
                            message.setImageList(mSelected);
                            message.setUserIcon(Uri.parse("android.resource://com.shrikanthravi.chatviewlibrary/drawable/hodor"));
                            chatView.addMessage(message);
                            switchbool = true;
                        }

                    }
                }
                break;
            }
            case 11: {

                //Video Selection Result
                if (resultCode == RESULT_OK) {
                    if (switchbool) {
                        Message message = new Message();
                        message.setMessageType(Message.MessageType.RightVideo);
                        message.setTime(getTime());
                        message.setUserName("Groot");
                        message.setVideoUri(Uri.parse(getPathVideo(data.getData())));
                        message.setUserIcon(Uri.parse("android.resource://com.shrikanthravi.chatviewlibrary/drawable/groot"));
                        chatView.addMessage(message);
                        switchbool = false;
                    } else {
                        Message message = new Message();

                        message.setMessageType(Message.MessageType.LeftVideo);
                        message.setTime(getTime());
                        message.setUserName("Hodor");
                        message.setVideoUri(Uri.parse(getPathVideo(data.getData())));
                        message.setUserIcon(Uri.parse("android.resource://com.shrikanthravi.chatviewlibrary/drawable/hodor"));
                        chatView.addMessage(message);
                        switchbool = true;
                    }
                }
                break;
            }
            case 12: {

                //Image Capture result

                if (resultCode == RESULT_OK) {


                    if (switchbool) {
                        Message message = new Message();
                        message.setMessageType(Message.MessageType.RightSingleImage);
                        message.setTime(getTime());
                        message.setUserName("Groot");
                        mSelected.clear();
                        File file = new File(Environment.getExternalStorageDirectory(), "MyPhoto.jpg");
                        //Uri of camera image
                        Uri uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);
                        mSelected.add(uri);
                        message.setImageList(mSelected);
                        message.setUserIcon(Uri.parse("android.resource://com.shrikanthravi.chatviewlibrary/drawable/groot"));
                        chatView.addMessage(message);
                        switchbool = false;
                    } else {
                        Message message = new Message();

                        message.setMessageType(Message.MessageType.LeftSingleImage);
                        message.setTime(getTime());
                        message.setUserName("Hodor");
                        mSelected.clear();
                        File file = new File(Environment.getExternalStorageDirectory(), "MyPhoto.jpg");
                        //Uri of camera image
                        Uri uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);
                        mSelected.add(uri);
                        message.setImageList(mSelected);
                        message.setUserIcon(Uri.parse("android.resource://com.shrikanthravi.chatviewlibrary/drawable/hodor"));
                        chatView.addMessage(message);
                        switchbool = true;
                    }
                }
                break;
            }
            case 13: {
                if (resultCode == RESULT_OK) {
                    if (switchbool) {
                        Message message = new Message();
                        message.setMessageType(Message.MessageType.RightAudio);
                        message.setTime(getTime());
                        message.setUserName("Groot");
                        message.setAudioUri(Uri.parse(getPathAudio(data.getData())));
                        message.setUserIcon(Uri.parse("android.resource://com.shrikanthravi.chatviewlibrary/drawable/groot"));
                        chatView.addMessage(message);
                        switchbool = false;
                    } else {
                        Message message = new Message();

                        message.setMessageType(Message.MessageType.LeftAudio);
                        message.setTime(getTime());
                        message.setUserName("Hodor");
                        message.setAudioUri(Uri.parse(getPathAudio(data.getData())));
                        message.setUserIcon(Uri.parse("android.resource://com.shrikanthravi.chatviewlibrary/drawable/hodor"));
                        chatView.addMessage(message);
                        switchbool = true;
                    }
                }
                break;
            }
        }

    }


    public String getPathVideo(Uri uri) {
        System.out.println("getpath " + uri.toString());
        String[] projection = {MediaStore.Video.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else return null;
    }

    public String getPathAudio(Uri uri) {
        System.out.println("getpath " + uri.toString());
        String[] projection = {MediaStore.Audio.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);

        int columnIndex = cursor.getColumnIndex(projection[0]);
        cursor.moveToFirst();
        if (cursor != null) {
            return cursor.getString(columnIndex);
        } else return null;
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
                                messageET.setText(text);

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
                                messageET.setText("");
                            } else {
                                messageET.setText(text);
                            }
                            if (messageET.getText().toString().length() != 0)
                                messageET.setSelection(messageET.getText().toString().length());
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
