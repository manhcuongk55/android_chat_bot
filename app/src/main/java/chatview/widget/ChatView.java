package chatview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.balysv.materialripple.MaterialRippleLayout;
import com.github.zagum.expandicon.ExpandIconView;
import com.google.cloud.android.speech.R;

import java.util.ArrayList;
import java.util.List;

import chatview.data.Message;
import chatview.data.MessageAdapter;
import jp.wasabeef.recyclerview.animators.ScaleInBottomAnimator;

/**
 * Created by shrikanthravi on 20/02/18.
 */

public class ChatView extends RelativeLayout implements MessageAdapter.RateMessageListener {
    public void rateMessageSuccess(int position, String rate) {
        messageList.get(position).setRateMessage(rate);
        messageAdapter.notifyDataSetChanged();
    }

    public interface RateMessageListener {
        void rateMessage(String rate, String mId, int position);
    }

    RateMessageListener rateMessageListener;

    public void setRateMessageListener(RateMessageListener rateMessageListener) {
        this.rateMessageListener = rateMessageListener;
    }

    public static int Personal = 1;
    public static int Group = 2;

    protected Context mContext;
    protected LayoutInflater mLayoutInflater;

    protected int mode = 1;
    protected boolean more = false;
    protected RelativeLayout mLayoutRoot;
    protected RecyclerView chatRV;
    protected LinearLayout sendLL;
    protected MaterialRippleLayout sendMRL;
    protected HorizontalScrollView moreHSV;
    protected MaterialRippleLayout galleryMRL, videoMRL, cameraMRL, audioMRL, micMRL;
    //    protected ExpandIconView expandIconView;
    protected MaterialRippleLayout showKeyBoard;
    protected CardView hideKeyBoardLayout;
    protected LinearLayout showKeyBoardLayout;
    protected List<Message> messageList;
    protected MessageAdapter messageAdapter;
    protected boolean showSenderLL = false;
    protected boolean showLeftBubbleIcon = true;
    protected boolean showRightBubbleIcon = true;
    protected boolean showSenderName = true;
    protected EditText messageET;

    private int leftBubbleLayoutColor = R.color.colorAccent2;
    private int rightBubbleLayoutColor = R.color.colorAccent1;
    private int leftBubbleTextColor = android.R.color.black;
    private int rightBubbleTextColor = android.R.color.white;
    private int chatViewBackgroundColor = android.R.color.white;
    private int timeTextColor = android.R.color.tab_indicator_text;
    private int senderNameTextColor = android.R.color.tab_indicator_text;
    private int ChatViewBackgroundColor = android.R.color.white;
    private Typeface typeface;
    private OnClickSendButtonListener onClickSendButtonListener;
    private OnClickGalleryButtonListener onClickGalleryButtonListener;
    private OnClickVideoButtonListener onClickVideoButtonListener;
    private OnClickCameraButtonListener onClickCameraButtonListener;
    private OnClickAudioButtonListener onClickAudioButtonListener;
    private OnTouchMicButtonListener onTouchMicButtonListener;


    public ChatView(Context context, AttributeSet attrs) {
        super(context, attrs);


        init(context);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ChatView,
                0, 0);
        setAttributes(a);
        a.recycle();

    }


    protected void init(Context context) {

        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);

        //load rootview from xml
        View rootView = mLayoutInflater.inflate(R.layout.widget_chatview, this, true);

        //initialize UI
        mLayoutRoot = rootView.findViewById(R.id.rootRL);
        chatRV = rootView.findViewById(R.id.chatRV);
        sendLL = rootView.findViewById(R.id.sendLL);
        sendMRL = rootView.findViewById(R.id.sendMRL);
        moreHSV = rootView.findViewById(R.id.moreLL);
        messageET = rootView.findViewById(R.id.messageET);
        galleryMRL = rootView.findViewById(R.id.galleryMRL);
        videoMRL = rootView.findViewById(R.id.videoMRL);
        cameraMRL = rootView.findViewById(R.id.cameraMRL);
        audioMRL = rootView.findViewById(R.id.audioMRL);
        micMRL = rootView.findViewById(R.id.micMRL);
//        expandIconView = rootView.findViewById(R.id.expandIconView);
        showKeyBoard = rootView.findViewById(R.id.showKeyBoard);
        hideKeyBoardLayout = rootView.findViewById(R.id.hideKeyBoardLayout);
        showKeyBoardLayout = rootView.findViewById(R.id.showKeyBoardLayout);
        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList, context, chatRV);
        messageAdapter.setRateMessageListener(this);
        WrapContentLinearLayoutManager layoutManager = new WrapContentLinearLayoutManager(context, LinearLayoutManager.VERTICAL, true);
        layoutManager.setStackFromEnd(true);
        chatRV.setLayoutManager(layoutManager);
        chatRV.setItemAnimator(new ScaleInBottomAnimator(new OvershootInterpolator(1f)));
        chatRV.setAdapter(messageAdapter);
        messageET.clearFocus();
        messageET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                chatRV.scrollToPosition(0);
                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        messageET.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                chatRV.scrollToPosition(0);
                messageAdapter.notifyDataSetChanged();
            }
        });

        showKeyBoard.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyBoardLayout.setVisibility(View.GONE);
                showKeyBoardLayout.setVisibility(View.VISIBLE);
                messageET.requestFocus();
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(messageET, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        sendMRL.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                sendButtonClicked();
            }
        });

        galleryMRL.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                galleryButtonClicked();
            }
        });

        videoMRL.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                videoButtonClicked();
            }
        });

        cameraMRL.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraButtonClicked();
            }
        });

        audioMRL.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                audioButtonClicked();
            }
        });

        micMRL.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                micButtonTouched();
            }
        });

      /*  micMRL.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    micButtonTouched();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    micButtonReleased();
                }
                return true;
            }
        });*/


    }

    public boolean onBackpress() {
        if (messageET.isFocused()) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(messageET.getWindowToken(), 0);
            hideKeyBoardLayout.setVisibility(View.VISIBLE);
            showKeyBoardLayout.setVisibility(View.GONE);
            return true;
        }
        return false;
    }

    protected void setAttributes(TypedArray attrs) {

        //set Attributes from xml
        showSenderLayout(attrs.getBoolean(R.styleable.ChatView_showSenderLayout, true));
        showLeftBubbleIcon(attrs.getBoolean(R.styleable.ChatView_showLeftBubbleIcon, showLeftBubbleIcon));
        showRightBubbleIcon(attrs.getBoolean(R.styleable.ChatView_showRightBubbleIcon, showRightBubbleIcon));
        setLeftBubbleLayoutColor(attrs.getColor(R.styleable.ChatView_leftBubbleLayoutColor, getResources().getColor(leftBubbleLayoutColor)));
        setRightBubbleLayoutColor(attrs.getColor(R.styleable.ChatView_rightBubbleLayoutColor, getResources().getColor(rightBubbleLayoutColor)));
        setLeftBubbleTextColor(attrs.getColor(R.styleable.ChatView_leftBubbleTextColor, getResources().getColor(leftBubbleTextColor)));
        setRightBubbleTextColor(attrs.getColor(R.styleable.ChatView_rightBubbleTextColor, getResources().getColor(rightBubbleTextColor)));
        setChatViewBackgroundColor(attrs.getColor(R.styleable.ChatView_chatViewBackgroundColor, mContext.getResources().getColor(chatViewBackgroundColor)));
        setTimeTextColor(attrs.getColor(R.styleable.ChatView_timeTextColor, mContext.getResources().getColor(timeTextColor)));
        setSenderNameTextColor(attrs.getColor(R.styleable.ChatView_senderNameTextColor, getResources().getColor(senderNameTextColor)));
        showSenderName(attrs.getBoolean(R.styleable.ChatView_showSenderName, showSenderName));
        setTextSize(attrs.getDimension(R.styleable.ChatView_textSize, 20));
        setChatViewBackgroundColor(attrs.getColor(R.styleable.ChatView_chatViewBackgroundColor, getResources().getColor(chatViewBackgroundColor)));

    }

    @Override
    public void rateMessage(String rate, String mId, int position) {
        rateMessageListener.rateMessage(rate, mId, position);
    }


    public interface OnClickSendButtonListener {
        public void onSendButtonClick(String body);
    }

    public interface OnClickGalleryButtonListener {
        public void onGalleryButtonClick();
    }

    public interface OnClickVideoButtonListener {
        public void onVideoButtonClick();
    }

    public interface OnClickCameraButtonListener {
        public void onCameraButtonClicked();
    }

    public interface OnClickAudioButtonListener {
        public void onAudioButtonClicked();
    }

    public interface OnTouchMicButtonListener {
        public void onMicButtonTouched();

        public void onMicButtonReleased();
    }

    public void setOnClickSendButtonListener(OnClickSendButtonListener onClickSendButtonListener) {
        this.onClickSendButtonListener = onClickSendButtonListener;
    }

    public void setOnClickGalleryButtonListener(OnClickGalleryButtonListener onClickGalleryButtonListener) {
        this.onClickGalleryButtonListener = onClickGalleryButtonListener;
    }

    public void setOnClickVideoButtonListener(OnClickVideoButtonListener onClickVideoButtonListener) {
        this.onClickVideoButtonListener = onClickVideoButtonListener;
    }

    public void setOnClickCameraButtonListener(OnClickCameraButtonListener onClickCameraButtonListener) {
        this.onClickCameraButtonListener = onClickCameraButtonListener;
    }

    public void setOnClickAudioButtonListener(OnClickAudioButtonListener onClickAudioButtonListener) {
        this.onClickAudioButtonListener = onClickAudioButtonListener;
    }

    public void setOnTouchMicButtonListener(OnTouchMicButtonListener onTouchMicButtonListener) {
        this.onTouchMicButtonListener = onTouchMicButtonListener;
    }

    public void sendButtonClicked() {
        if (onClickSendButtonListener != null) {

            onClickSendButtonListener.onSendButtonClick(messageET.getText().toString());
            messageET.setText("");
        }
    }

    public void galleryButtonClicked() {
        if (onClickGalleryButtonListener != null) {
            onClickGalleryButtonListener.onGalleryButtonClick();
        }
    }

    public void videoButtonClicked() {
        if (onClickVideoButtonListener != null) {
            onClickVideoButtonListener.onVideoButtonClick();
        }
    }

    public void cameraButtonClicked() {
        if (onClickCameraButtonListener != null) {
            onClickCameraButtonListener.onCameraButtonClicked();
        }
    }

    public void audioButtonClicked() {
        if (onClickAudioButtonListener != null) {
            onClickAudioButtonListener.onAudioButtonClicked();
        }
    }

    public void micButtonTouched() {
        if (onTouchMicButtonListener != null) {
            onTouchMicButtonListener.onMicButtonTouched();
        }
    }

    public void micButtonReleased() {
        if (onTouchMicButtonListener != null) {
            onTouchMicButtonListener.onMicButtonReleased();
        }
    }

    public List<Message> getMessageList() {
        return this.messageList;
    }

    //Use this method to add a message to chatview
    public void addMessage(Message message) {

        messageList.add(0, message);
        messageAdapter.notifyItemInserted(0);
        chatRV.smoothScrollToPosition(0);
        mLayoutRoot.invalidate();
    }

    //Use this method to remove a message from chatview
    public void removeMessage(Message message) {
        messageList.remove(message);
        messageAdapter.notifyDataSetChanged();
    }

    //Use this method to clear all messages
    public void clearMessages() {
        messageList.clear();
        messageAdapter.notifyDataSetChanged();
    }


    //For hiding or showing sender layout which contains an edittext ,send button and many others features
    public void showSenderLayout(boolean b) {
        this.showSenderLL = b;
        if (b) {
            sendLL.setVisibility(VISIBLE);
        } else {
            sendLL.setVisibility(GONE);
        }
    }

    //For groups (showing or hiding sender name which appears on top of the message)
    public void showSenderName(boolean b) {
        messageAdapter.showSenderName(b);
    }

    //For showing or hiding sender icon in left
    public void showLeftBubbleIcon(boolean b) {
        messageAdapter.showLeftBubbleIcon(b);
    }

    //For showing or hiding receiver icon in right
    public void showRightBubbleIcon(boolean b) {
        messageAdapter.showRightBubbleIcon(b);
    }


    //For changing left bubble layout color
    public void setLeftBubbleLayoutColor(int color) {
        messageAdapter.setLeftBubbleLayoutColor(color);
    }

    //for changing right bubble layout color
    public void setRightBubbleLayoutColor(int color) {
        messageAdapter.setRightBubbleLayoutColor(color);
    }

    //For changing left bubble text color
    public void setLeftBubbleTextColor(int color) {
        messageAdapter.setLeftBubbleTextColor(color);
    }

    //For changing right bubble text color
    public void setRightBubbleTextColor(int color) {
        messageAdapter.setRightBubbleTextColor(color);
    }

    //For changing chatview background color
    public void setChatViewBackgroundColor(int color) {
//        mLayoutRoot.setBackgroundColor(color);
//        mLayoutRoot.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_background));
    }

    //For changing time text color which is displayed (expands) when message is clicked
    public void setTimeTextColor(int color) {
        messageAdapter.setTimeTextColor(color);
    }

    //For changing typeface of text inside
    public void setTypeface(Typeface typeface) {
        messageAdapter.setTypeface(typeface);
    }

    public void setSenderNameTextColor(int color) {
        messageAdapter.setSenderNameTextColor(color);
    }

    public void setTextSize(float size) {
//        messageAdapter.setTextSize(size);
    }

    private class WrapContentLinearLayoutManager extends LinearLayoutManager {
        public WrapContentLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                super.onLayoutChildren(recycler, state);
            } catch (IndexOutOfBoundsException e) {
                Log.e("probe", "meet a IOOBE in RecyclerView");
            }
        }
    }
}
