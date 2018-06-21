package chatview.data;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.google.cloud.android.speech.R;
import com.lopei.collageview.CollageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.ohoussein.playpause.PlayPauseView;
import com.silencedut.expandablelayout.ExpandableLayout;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import chatview.activities.ImageFFActivity;
import chatview.activities.VideoFFActivity;
import chatview.utils.FontChanger;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import viettel.cyberspace.assitant.Webview.WebviewActivity;
import viettel.cyberspace.assitant.model.Answer;
import viettel.cyberspace.assitant.model.BaseResponse;
import viettel.cyberspace.assitant.model.ResponseAnswer;

import static android.content.ContentValues.TAG;

/**
 * Created by shrikanthravi on 16/02/18.
 */


public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int LeftSimpleMessage = 1;
    public static final int RightSimpleImage = 2;
    public static final int LeftSingleImage = 3;
    public static final int RightSingleImage = 4;
    public static final int LeftMultipleImages = 5;
    public static final int RightMultipleImages = 6;
    public static final int LeftVideo = 7;
    public static final int RightVideo = 8;
    public static final int LeftAudio = 9;
    public static final int RightAudio = 10;
    public static final int ListQuestion = 11;
    public static final int ListSuggestion = 12;
    public static final int LeftHtml = 13;
    public static final int TYPING = 20;


    private List<Message> messageList;
    private List<Message> filterList;
    Context context;
    MessageFilter filter;
    ImageLoader imageLoader;
    Typeface typeface;
    boolean checkFeedbackContentVisible = false;

    public static MediaPlayer mediaPlayer;

    String playingposition;
    //onCompletionListener method
    MediaPlayer.OnCompletionListener mCompletionListener;
    protected boolean showLeftBubbleIcon = true;
    protected boolean showRightBubbleIcon = true;
    protected boolean showSenderName = true;

    private int leftBubbleLayoutColor = R.color.colorAccent;
    private int rightBubbleLayoutColor = R.color.colorAccent1;
    private int leftBubbleTextColor = android.R.color.black;
    private int rightBubbleTextColor = android.R.color.white;
    private int timeTextColor = android.R.color.tab_indicator_text;
    private int senderNameTextColor = android.R.color.tab_indicator_text;
    private float textSize = 14;

    public MessageAdapter(List<Message> verticalList, Context context, RecyclerView recyclerView) {

        this.messageList = verticalList;
        this.context = context;
        this.filterList = verticalList;
        filter = new MessageFilter(verticalList, this);
        imageLoader = ImageLoader.getInstance();
//        typeface = Typeface.createFromAsset(context.getAssets(), "fonts/product_san_regular.ttf");
        mCompletionListener = new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
                mediaPlayer = null;
            }
        };

    }

    public interface RateMessageListener {
        void rateMessage(String rate, String mId, int position);

        void sendMaster(String mId, String message, int position);
    }

    RateMessageListener rateMessageListener;

    public void setRateMessageListener(RateMessageListener rateMessageListener) {
        this.rateMessageListener = rateMessageListener;
    }

    @Override
    public int getItemViewType(int position) {


        int type = 0;
        switch (messageList.get(position).getMessageType()) {
            case LeftSimpleMessage: {
                type = 1;
                break;
            }
            case RightSimpleImage: {
                type = 2;
                break;
            }
            case LeftSingleImage: {
                type = 3;
                break;
            }
            case RightSingleImage: {
                type = 4;
                break;
            }
            case LeftMultipleImages: {
                type = 5;
                break;
            }
            case RightMultipleImages: {
                type = 6;
                break;
            }
            case LeftVideo: {
                type = 7;
                break;
            }
            case RightVideo: {
                type = 8;
                break;
            }
            case LeftAudio: {
                type = 9;
                break;
            }
            case RightAudio: {
                type = 10;
                break;
            }
            case ListQuestion: {
                type = 11;
                break;
            }
            case ListSuggestion: {
                type = 12;
                break;
            }

            case LeftHtml: {
                type = 13;
                break;
            }
        }
        if (type == 0) {
            throw new RuntimeException("Set Message Type ( Message Type is Null )");
        } else {
            return type;
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;


        switch (viewType) {
            case LeftSimpleMessage: {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.left_text_layout, parent, false);
                viewHolder = new LeftTextViewHolder(view);
                break;
            }
            case RightSimpleImage: {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.right_text_layout, parent, false);
                viewHolder = new RightTextViewHolder(view);
                break;
            }
            case LeftSingleImage: {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.left_image_layout, parent, false);
                viewHolder = new LeftImageViewHolder(view);
                break;
            }
            case RightSingleImage: {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.right_image_layout, parent, false);
                viewHolder = new RightImageViewHolder(view);
                break;
            }
            case LeftMultipleImages: {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.left_images_layout, parent, false);
                viewHolder = new LeftImagesViewHolder(view);
                break;
            }
            case RightMultipleImages: {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.right_images_layout, parent, false);
                viewHolder = new RightImagesViewHolder(view);
                break;
            }
            case LeftVideo: {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.left_video_layout, parent, false);

                viewHolder = new LeftVideoViewHolder(view);
                break;
            }
            case RightVideo: {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.right_video_layout, parent, false);
                viewHolder = new RightVideoViewHolder(view);
                break;
            }
            case LeftAudio: {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.left_audio_layout, parent, false);
                viewHolder = new LeftAudioViewHolder(view);
                break;
            }
            case RightAudio: {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.right_audio_layout, parent, false);
                viewHolder = new RightAudioViewHolder(view);
                break;
            }
            case ListQuestion: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_question_layout, parent, false);
                viewHolder = new ListQuestionViewHolder(view);
                break;
            }
            case ListSuggestion: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_suggestion_layout, parent, false);
                viewHolder = new ListSuggestionViewHolder(view);
                break;
            }

            case LeftHtml: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.left_html_layout, parent, false);
                viewHolder = new LeftHtmlViewHolder(view);
                break;
            }

            case TYPING: {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.left_typing_layout, parent, false);
                viewHolder = new LeftTypingViewHolder(view);
                break;
            }
        }


        if (viewHolder == null) {
            throw new RuntimeException("View Holder is null");
        }
        return viewHolder;
    }

    protected class LeftTextViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView leftTV, leftTimeTV, questionFromChuyenGia, answerFromChuyenGia, leftTVSource;
        public ExpandableLayout leftEL;
        public ImageView lefttMessageStatusIV, leftBubbleIconIV;
        public CardView leftBubbleIconCV;
        View layoutFeedback;
        RelativeLayout layoutContentLike, layoutContentSairoi, layoutContentGuichuyengia, layoutContentAddFeedback, answerContent;
        LinearLayout layoutLike, layoutGuiChuyenGiaClick;
        LinearLayout layoutSairoi;
        View layoutFeedBackContent, layoutBottomTextview1, layoutAnswering, layoutAnswerText, layoutBottomTextview, layoutAnswerFromChuyenGia;
        RecyclerView moreAnswer;
        MaterialRippleLayout gotoLink;

        public LeftTextViewHolder(View view) {
            super(view);

            leftTV = view.findViewById(R.id.leftTV);
            leftTVSource = view.findViewById(R.id.leftTVSource);
            leftTimeTV = view.findViewById(R.id.leftTimeTV);
            leftEL = view.findViewById(R.id.leftEL);
            leftBubbleIconIV = view.findViewById(R.id.leftBubbleIconIV);
            leftBubbleIconCV = view.findViewById(R.id.leftBubbleIconCV);
            layoutFeedback = view.findViewById(R.id.layoutFeedback);
            layoutFeedBackContent = view.findViewById(R.id.layoutFeedBackContent);
            layoutContentLike = view.findViewById(R.id.layoutContentLike);
            layoutContentSairoi = view.findViewById(R.id.layoutContentSairoi);
            layoutContentGuichuyengia = view.findViewById(R.id.layoutContentGuichuyengia);
            layoutContentAddFeedback = view.findViewById(R.id.layoutContentAddFeedback);
            layoutLike = view.findViewById(R.id.layoutLike);
            layoutSairoi = view.findViewById(R.id.layoutSairoi);
//            layoutGuichuyengia = view.findViewById(R.id.layoutGuichuyengia);
            layoutGuiChuyenGiaClick = view.findViewById(R.id.layoutGuiChuyenGiaClick);
            answerContent = view.findViewById(R.id.answerContent);
            layoutBottomTextview1 = view.findViewById(R.id.layoutBottomTextview1);
            layoutBottomTextview = view.findViewById(R.id.layoutBottomTextview);
            layoutAnswerFromChuyenGia = view.findViewById(R.id.layoutAnswerFromChuyenGia);
            answerFromChuyenGia = view.findViewById(R.id.answerFromChuyenGia);
            questionFromChuyenGia = view.findViewById(R.id.questionFromChuyenGia);
            layoutAnswering = view.findViewById(R.id.layoutAnswering);
            layoutAnswerText = view.findViewById(R.id.layoutAnswerText);
            moreAnswer = view.findViewById(R.id.moreAnswer);
            gotoLink = view.findViewById(R.id.gotoLink);
            layoutLike.setOnClickListener(this);
            layoutSairoi.setOnClickListener(this);
            layoutGuiChuyenGiaClick.setOnClickListener(this);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (checkFeedbackContentVisible) {
                        notifyDataSetChanged();
                    }
                }
            });
            setTimeTextColor(timeTextColor);
            setSenderNameTextColor(senderNameTextColor);
            showSenderName(showSenderName);
            showLeftBubbleIcon(showLeftBubbleIcon);
        }

        public void setBackgroundColor(int color) {
            Drawable backgroundDrawable = DrawableCompat.wrap(leftTV.getBackground()).mutate();
            DrawableCompat.setTint(backgroundDrawable, color);
        }

        public void setTextColor(int color) {
            leftTV.setTextColor(color);
        }

        public void setTimeTextColor(int color) {
            leftTimeTV.setTextColor(color);
        }


        public void showLeftBubbleIcon(boolean b) {
            if (b) {
                leftBubbleIconCV.setVisibility(View.VISIBLE);
            } else {
                leftBubbleIconCV.setVisibility(View.GONE);
            }
        }

        public void setTextSize(float size) {
//            leftTV.setTextSize(size);
        }


        @Override
        public void onClick(View view) {
            String rate = "";
            switch (view.getId()) {
                case R.id.layoutLike:
                    layoutFeedback.setVisibility(View.GONE);
                    layoutBottomTextview1.setVisibility(View.GONE);
                    rate = "like";
                    rateMessageListener.rateMessage(rate, messageList.get(getAdapterPosition()).getMid(), getAdapterPosition());
                    break;
                case R.id.layoutSairoi:
                    layoutFeedback.setVisibility(View.GONE);
                    layoutBottomTextview1.setVisibility(View.GONE);
                    rate = "dislike";
                    rateMessageListener.rateMessage(rate, messageList.get(getAdapterPosition()).getMid(), getAdapterPosition());
                    break;
                case R.id.layoutGuiChuyenGiaClick:
                    layoutFeedback.setVisibility(View.GONE);
                    layoutBottomTextview1.setVisibility(View.GONE);
                    rateMessageListener.sendMaster(messageList.get(getAdapterPosition()).getMid(), messageList.get(getAdapterPosition()).getQuestion(), getAdapterPosition());
//                    setFeddbackContent(R.id.layoutGuichuyengia);
                    break;
            }
        }

        public void setFeddbackContent(int layoutID) {
            layoutFeedBackContent.setVisibility(View.VISIBLE);
            layoutContentLike.setVisibility(View.GONE);
            layoutContentSairoi.setVisibility(View.GONE);
            layoutContentGuichuyengia.setVisibility(View.GONE);
            switch (layoutID) {
                case R.id.layoutLike:
                    layoutContentLike.setVisibility(View.VISIBLE);
                    break;
                case R.id.layoutSairoi:
                    layoutContentSairoi.setVisibility(View.VISIBLE);
                    break;
                case R.id.layoutGuiChuyenGiaClick:
                    layoutContentGuichuyengia.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    protected class RightTextViewHolder extends RecyclerView.ViewHolder {

        public TextView rightTV, rightTimeTV;
        public ImageView rightMessageStatusIV, rightBubbleIconIV;
        public ExpandableLayout rightEL;
        public CardView rightBubbleIconCV;

        public RightTextViewHolder(View view) {
            super(view);

            rightTV = view.findViewById(R.id.rightTV);
            rightTimeTV = view.findViewById(R.id.rightTimeTV);
            rightEL = view.findViewById(R.id.rightEL);
            rightBubbleIconCV = view.findViewById(R.id.rightBubbleIconCV);
            rightBubbleIconIV = view.findViewById(R.id.rightBubbleIconIV);
//            setBackgroundColor(rightBubbleLayoutColor);
//            setTextColor(rightBubbleTextColor);
            setTimeTextColor(timeTextColor);
            setSenderNameTextColor(senderNameTextColor);
            showSenderName(showSenderName);
            showRightBubbleIcon(showRightBubbleIcon);
//            setTextSize(textSize);
            FontChanger fontChanger = new FontChanger(typeface);
            fontChanger.replaceFonts((ViewGroup) view);

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    int pos = getLayoutPosition();

                    return true;
                }
            });
        }

        public void setBackgroundColor(int color) {
            Drawable backgroundDrawable = DrawableCompat.wrap(rightTV.getBackground()).mutate();
            DrawableCompat.setTint(backgroundDrawable, color);
        }

        public void setTextColor(int color) {
            rightTV.setTextColor(color);
        }

        public void setTimeTextColor(int color) {
            rightTimeTV.setTextColor(color);
        }

        public void showRightBubbleIcon(boolean b) {
            if (b) {
                rightBubbleIconCV.setVisibility(View.VISIBLE);
            } else {
                rightBubbleIconCV.setVisibility(View.GONE);
            }
        }

        public void setTextSize(float size) {
            rightTV.setTextSize(size);
        }
    }

    protected class ListQuestionViewHolder extends RecyclerView.ViewHolder {
        RecyclerView rvListQuestion;

        public ListQuestionViewHolder(View itemView) {
            super(itemView);
            rvListQuestion = (RecyclerView) itemView.findViewById(R.id.rvListQuestion);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, WebviewActivity.class);
                    context.startActivity(intent);
                }
            });
        }
    }


    protected class ListSuggestionViewHolder extends RecyclerView.ViewHolder {
        RecyclerView rvListSuggestion;

        public ListSuggestionViewHolder(View itemView) {
            super(itemView);
            rvListSuggestion = (RecyclerView) itemView.findViewById(R.id.rvListSuggestion);
        }
    }

    protected class LeftImageViewHolder extends RecyclerView.ViewHolder {

        public TextView leftTimeTV, senderNameTV;
        public ExpandableLayout leftEL;
        public ImageView lefttMessageStatusIV, leftBubbleIconIV;
        public CardView leftBubbleIconCV;
        public CardView leftIVCV;
        public ImageView leftIV;

        public LeftImageViewHolder(View view) {
            super(view);


            leftTimeTV = view.findViewById(R.id.leftTimeTV);
            leftEL = view.findViewById(R.id.leftEL);
            leftIV = view.findViewById(R.id.leftIV);
            leftIVCV = view.findViewById(R.id.leftIVCV);
            senderNameTV = view.findViewById(R.id.senderNameTV);
            leftBubbleIconIV = view.findViewById(R.id.leftBubbleIconIV);
            leftBubbleIconCV = view.findViewById(R.id.leftBubbleIconCV);

            setBackgroundColor(leftBubbleLayoutColor);
            setSenderNameTextColor(senderNameTextColor);
            showSenderName(showSenderName);
            showLeftBubbleIcon(showLeftBubbleIcon);
            FontChanger fontChanger = new FontChanger(typeface);
            fontChanger.replaceFonts((ViewGroup) view);
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    int pos = getLayoutPosition();

                    return true;
                }
            });
        }

        public void setBackgroundColor(int color) {
            Drawable backgroundDrawable = DrawableCompat.wrap(leftIV.getBackground()).mutate();
            DrawableCompat.setTint(backgroundDrawable, color);
        }

        public void setSenderNameTextColor(int color) {
            senderNameTV.setTextColor(color);
        }

        public void showSenderName(boolean b) {
            if (b) {
                senderNameTV.setVisibility(View.VISIBLE);
            } else {
                senderNameTV.setVisibility(View.GONE);
            }
        }

        public void showLeftBubbleIcon(boolean b) {
            if (b) {
                leftBubbleIconCV.setVisibility(View.VISIBLE);
            } else {
                leftBubbleIconCV.setVisibility(View.GONE);
            }
        }
    }

    protected class RightImageViewHolder extends RecyclerView.ViewHolder {

        public TextView rightTV, rightTimeTV, senderNameTV;
        public ExpandableLayout rightEL;
        public ImageView rightMessageStatusIV, rightBubbleIconIV;
        public CardView rightBubbleIconCV;
        public CardView rightIVCV;
        public ImageView rightIV;

        public RightImageViewHolder(View view) {
            super(view);


            rightTimeTV = view.findViewById(R.id.rightTimeTV);
            rightEL = view.findViewById(R.id.rightEL);
            rightIV = view.findViewById(R.id.rightIV);
            rightIVCV = view.findViewById(R.id.rightIVCV);
            senderNameTV = view.findViewById(R.id.senderNameTV);
            rightBubbleIconCV = view.findViewById(R.id.rightBubbleIconCV);
            rightBubbleIconIV = view.findViewById(R.id.rightBubbleIconIV);
            FontChanger fontChanger = new FontChanger(typeface);
            fontChanger.replaceFonts((ViewGroup) view);
            setBackgroundColor(rightBubbleLayoutColor);
            setSenderNameTextColor(senderNameTextColor);
            showSenderName(showSenderName);
            showRightBubbleIcon(showRightBubbleIcon);
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    int pos = getLayoutPosition();

                    return true;
                }
            });
        }

        public void setBackgroundColor(int color) {
            Drawable backgroundDrawable = DrawableCompat.wrap(rightIV.getBackground()).mutate();
            DrawableCompat.setTint(backgroundDrawable, color);
        }

        public void setSenderNameTextColor(int color) {
            senderNameTV.setTextColor(color);
        }

        public void showSenderName(boolean b) {
            if (b) {
                senderNameTV.setVisibility(View.VISIBLE);
            } else {
                senderNameTV.setVisibility(View.GONE);
            }
        }

        public void showRightBubbleIcon(boolean b) {
            if (b) {
                rightBubbleIconCV.setVisibility(View.VISIBLE);
            } else {
                rightBubbleIconCV.setVisibility(View.GONE);
            }
        }
    }


    protected class LeftHtmlViewHolder extends RecyclerView.ViewHolder {

        public TextView leftTimeTV, senderNameTV;
        public ExpandableLayout leftEL;
        public ImageView leftBubbleIconIV;
        public CardView leftBubbleIconCV;
        public CollageView leftCollageView;
        public WebView webView;

        public LeftHtmlViewHolder(View view) {
            super(view);

            leftTimeTV = view.findViewById(R.id.leftTimeTV);
            leftEL = view.findViewById(R.id.leftEL);
            leftCollageView = view.findViewById(R.id.leftCollageView);
            senderNameTV = view.findViewById(R.id.senderNameTV);
            leftBubbleIconIV = view.findViewById(R.id.leftBubbleIconIV);
            leftBubbleIconCV = view.findViewById(R.id.leftBubbleIconCV);
            webView = view.findViewById(R.id.leftWV);
            setSenderNameTextColor(senderNameTextColor);
            showSenderName(showSenderName);
            showLeftBubbleIcon(showLeftBubbleIcon);
//            view.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//
//                    int pos = getLayoutPosition();
//
//                    return true;
//                }
//            });

            WebSettings settings = webView.getSettings();
            settings.setUseWideViewPort(true);
            settings.setLoadWithOverviewMode(true);
            settings.setJavaScriptEnabled(true);
            settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
            // wv.setBackgroundColor(0);
            webView.setVerticalScrollBarEnabled(false);
            webView.setHorizontalScrollBarEnabled(false);


        }

        public void setSenderNameTextColor(int color) {
            senderNameTV.setTextColor(color);
        }

        public void showSenderName(boolean b) {
            if (b) {
                senderNameTV.setVisibility(View.VISIBLE);
            } else {
                senderNameTV.setVisibility(View.GONE);
            }
        }

        public void showLeftBubbleIcon(boolean b) {
            if (b) {
                leftBubbleIconCV.setVisibility(View.VISIBLE);
            } else {
                leftBubbleIconCV.setVisibility(View.GONE);
            }
        }
    }

    protected class LeftImagesViewHolder extends RecyclerView.ViewHolder {

        public TextView leftTimeTV, senderNameTV;
        public ExpandableLayout leftEL;
        public ImageView lefttMessageStatusIV, leftBubbleIconIV;
        public CardView leftBubbleIconCV;
        public CollageView leftCollageView;

        public LeftImagesViewHolder(View view) {
            super(view);

            leftTimeTV = view.findViewById(R.id.leftTimeTV);
            leftEL = view.findViewById(R.id.leftEL);
            leftCollageView = view.findViewById(R.id.leftCollageView);
            senderNameTV = view.findViewById(R.id.senderNameTV);
            leftBubbleIconIV = view.findViewById(R.id.leftBubbleIconIV);
            leftBubbleIconCV = view.findViewById(R.id.leftBubbleIconCV);
            setSenderNameTextColor(senderNameTextColor);
            showSenderName(showSenderName);
            showLeftBubbleIcon(showLeftBubbleIcon);
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    int pos = getLayoutPosition();

                    return true;
                }
            });
        }

        public void setSenderNameTextColor(int color) {
            senderNameTV.setTextColor(color);
        }

        public void showSenderName(boolean b) {
            if (b) {
                senderNameTV.setVisibility(View.VISIBLE);
            } else {
                senderNameTV.setVisibility(View.GONE);
            }
        }

        public void showLeftBubbleIcon(boolean b) {
            if (b) {
                leftBubbleIconCV.setVisibility(View.VISIBLE);
            } else {
                leftBubbleIconCV.setVisibility(View.GONE);
            }
        }
    }

    protected class RightImagesViewHolder extends RecyclerView.ViewHolder {

        public TextView rightTimeTV, senderNameTV;
        public ExpandableLayout rightEL;
        public ImageView rightMessageStatusIV, rightBubbleIconIV;
        public CardView rightBubbleIconCV;
        public CollageView rightCollageView, leftCollageView;

        public RightImagesViewHolder(View view) {
            super(view);

            rightTimeTV = view.findViewById(R.id.rightTimeTV);
            rightEL = view.findViewById(R.id.rightEL);
            rightCollageView = view.findViewById(R.id.rightCollageView);
            leftCollageView = view.findViewById(R.id.leftCollageView);
            senderNameTV = view.findViewById(R.id.senderNameTV);
            rightBubbleIconCV = view.findViewById(R.id.rightBubbleIconCV);
            rightBubbleIconIV = view.findViewById(R.id.rightBubbleIconIV);
            setSenderNameTextColor(senderNameTextColor);
            showSenderName(showSenderName);
            showRightBubbleIcon(showRightBubbleIcon);
            FontChanger fontChanger = new FontChanger(typeface);
            fontChanger.replaceFonts((ViewGroup) view);
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    int pos = getLayoutPosition();

                    return true;
                }
            });
        }

        public void setSenderNameTextColor(int color) {
            senderNameTV.setTextColor(color);
        }

        public void showSenderName(boolean b) {
            if (b) {
                senderNameTV.setVisibility(View.VISIBLE);
            } else {
                senderNameTV.setVisibility(View.GONE);
            }
        }

        public void showRightBubbleIcon(boolean b) {
            if (b) {
                rightBubbleIconCV.setVisibility(View.VISIBLE);
            } else {
                rightBubbleIconCV.setVisibility(View.GONE);
            }
        }
    }

    protected class LeftVideoViewHolder extends RecyclerView.ViewHolder {

        public TextView leftTimeTV, senderNameTV;
        public ExpandableLayout leftEL;
        public ImageView lefttMessageStatusIV, leftBubbleIconIV;
        public CardView leftBubbleIconCV;
        public CardView leftIVCV;
        public ImageView leftIV;
        public LinearLayout videoLL;

        public LeftVideoViewHolder(View view) {
            super(view);


            leftIVCV = view.findViewById(R.id.leftIVCV);
            leftTimeTV = view.findViewById(R.id.leftTimeTV);
            leftEL = view.findViewById(R.id.leftEL);
            senderNameTV = view.findViewById(R.id.senderNameTV);
            leftBubbleIconIV = view.findViewById(R.id.leftBubbleIconIV);
            leftBubbleIconCV = view.findViewById(R.id.leftBubbleIconCV);
            videoLL = view.findViewById(R.id.videoLL);

            setBackgroundColor(leftBubbleLayoutColor);
            setSenderNameTextColor(senderNameTextColor);
            showSenderName(showSenderName);
            showLeftBubbleIcon(showLeftBubbleIcon);
            FontChanger fontChanger = new FontChanger(typeface);
            fontChanger.replaceFonts((ViewGroup) view);
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    int pos = getLayoutPosition();

                    return true;
                }
            });
        }

        public void setBackgroundColor(int color) {
            leftIVCV.setCardBackgroundColor(color);
        }


        public void setSenderNameTextColor(int color) {
            senderNameTV.setTextColor(color);
        }

        public void showSenderName(boolean b) {
            if (b) {
                senderNameTV.setVisibility(View.VISIBLE);
            } else {
                senderNameTV.setVisibility(View.GONE);
            }
        }

        public void showLeftBubbleIcon(boolean b) {
            if (b) {
                leftBubbleIconCV.setVisibility(View.VISIBLE);
            } else {
                leftBubbleIconCV.setVisibility(View.GONE);
            }
        }
    }

    protected class RightVideoViewHolder extends RecyclerView.ViewHolder {

        public TextView rightTimeTV, senderNameTV;
        public ExpandableLayout rightEL;
        public ImageView rightMessageStatusIV, rightBubbleIconIV;
        public CardView rightBubbleIconCV, rightIVCV;
        public LinearLayout videoLL;

        public RightVideoViewHolder(View view) {
            super(view);

            rightTimeTV = view.findViewById(R.id.rightTimeTV);
            rightEL = view.findViewById(R.id.rightEL);
            rightIVCV = view.findViewById(R.id.rightIVCV);
            senderNameTV = view.findViewById(R.id.senderNameTV);
            rightBubbleIconCV = view.findViewById(R.id.rightBubbleIconCV);
            rightBubbleIconIV = view.findViewById(R.id.rightBubbleIconIV);
            videoLL = view.findViewById(R.id.videoLL);

            setBackgroundColor(rightBubbleLayoutColor);
            setSenderNameTextColor(senderNameTextColor);
            showSenderName(showSenderName);
            showRightBubbleIcon(showRightBubbleIcon);
            FontChanger fontChanger = new FontChanger(typeface);
            fontChanger.replaceFonts((ViewGroup) view);
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    int pos = getLayoutPosition();

                    return true;
                }
            });
        }

        public void setBackgroundColor(int color) {
            rightIVCV.setCardBackgroundColor(color);
        }

        public void setSenderNameTextColor(int color) {
            senderNameTV.setTextColor(color);
        }

        public void showSenderName(boolean b) {
            if (b) {
                senderNameTV.setVisibility(View.VISIBLE);
            } else {
                senderNameTV.setVisibility(View.GONE);
            }
        }

        public void showRightBubbleIcon(boolean b) {
            if (b) {
                rightBubbleIconCV.setVisibility(View.VISIBLE);
            } else {
                rightBubbleIconCV.setVisibility(View.GONE);
            }
        }
    }

    protected class LeftAudioViewHolder extends RecyclerView.ViewHolder {

        public TextView leftTimeTV, senderNameTV;
        public ExpandableLayout leftEL;
        public ImageView leftBubbleIconIV;
        public CardView leftBubbleIconCV;
        public SeekBar audioSeekbar;
        public PlayPauseView playPauseView;
        public Message message;
        public android.os.Handler handler;

        public LeftAudioViewHolder(View view) {
            super(view);

            audioSeekbar = view.findViewById(R.id.audioSeekbar);
            playPauseView = view.findViewById(R.id.play_pause_view);
            leftTimeTV = view.findViewById(R.id.leftTimeTV);
            leftEL = view.findViewById(R.id.leftEL);

            senderNameTV = view.findViewById(R.id.senderNameTV);
            leftBubbleIconIV = view.findViewById(R.id.leftBubbleIconIV);
            leftBubbleIconCV = view.findViewById(R.id.leftBubbleIconCV);
            setBackgroundColor(leftBubbleLayoutColor);
            setSeekBarLineColor(leftBubbleTextColor);
            setSeekBarThumbColor(rightBubbleLayoutColor);
            setTimeTextColor(timeTextColor);
            setSenderNameTextColor(senderNameTextColor);
            showSenderName(showSenderName);
            showLeftBubbleIcon(showLeftBubbleIcon);
            handler = new android.os.Handler();
            audioSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    if (playingposition == message.getAudioUri().toString()) {
                        mediaPlayer.seekTo(seekBar.getProgress());
                    }
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    if (playingposition == message.getAudioUri().toString()) {
                        mediaPlayer.seekTo(seekBar.getProgress());
                    }
                }
            });
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (message != null) {
                        if (playingposition == message.getAudioUri().toString()) {
                            if (mediaPlayer != null) {

                                if (mediaPlayer.isPlaying()) {

                                    audioSeekbar.setProgress(mediaPlayer.getCurrentPosition());
                                    if (playPauseView.isPlay()) {
                                        playPauseView.change(false);
                                    }
                                } else {
                                    playPauseView.change(true);
                                }
                            } else {
                                playPauseView.change(true);
                            }

                        } else {

                            audioSeekbar.setProgress(0);
                            playPauseView.change(true);
                            playPauseView.change(true);
                        }
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            playPauseView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {

                        if (playingposition == message.getAudioUri().toString()) {
                            mediaPlayer.stop();
                            mediaPlayer.reset();
                            mediaPlayer.release();
                            mediaPlayer = null;
                            playPauseView.change(true);
                        } else {

                            mediaPlayer.stop();
                            mediaPlayer.reset();
                            mediaPlayer.release();
                            mediaPlayer = null;
                            mediaPlayer = MediaPlayer.create(v.getContext(), message.getAudioUri());
                            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                @Override
                                public void onPrepared(MediaPlayer mediaPlayer) {

                                    mediaPlayer.start();
                                    playingposition = message.getAudioUri().toString();
                                    audioSeekbar.setMax(mediaPlayer.getDuration());
                                    playPauseView.change(false);
                                }
                            });

                        }
                    } else {

                        mediaPlayer = MediaPlayer.create(v.getContext(), message.getAudioUri());
                        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mediaPlayer) {

                                mediaPlayer.start();
                                playingposition = message.getAudioUri().toString();
                                audioSeekbar.setMax(mediaPlayer.getDuration());
                                playPauseView.change(false);
                            }
                        });
                    }


                }
            });

            FontChanger fontChanger = new FontChanger(typeface);
            fontChanger.replaceFonts((ViewGroup) view);
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    int pos = getLayoutPosition();

                    return true;
                }
            });
        }

        public Message getMessage() {
            return message;
        }

        public void setMessage(final Message message) {
            this.message = message;

        }

        public void setBackgroundColor(int color) {
            Drawable backgroundDrawable = DrawableCompat.wrap(audioSeekbar.getBackground()).mutate();
            DrawableCompat.setTint(backgroundDrawable, color);
        }

        public void setSeekBarLineColor(int color) {
            audioSeekbar.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.MULTIPLY);

        }

        public void setSeekBarThumbColor(int color) {
            Drawable backgroundDrawable1 = DrawableCompat.wrap(audioSeekbar.getThumb()).mutate();
            DrawableCompat.setTint(backgroundDrawable1, color);
        }


        public void setTimeTextColor(int color) {
            leftTimeTV.setTextColor(color);
        }

        public void setSenderNameTextColor(int color) {
            senderNameTV.setTextColor(color);
        }

        public void showSenderName(boolean b) {
            if (b) {
                senderNameTV.setVisibility(View.VISIBLE);
            } else {
                senderNameTV.setVisibility(View.GONE);
            }
        }

        public void showLeftBubbleIcon(boolean b) {
            if (b) {
                leftBubbleIconCV.setVisibility(View.VISIBLE);
            } else {
                leftBubbleIconCV.setVisibility(View.GONE);
            }
        }


    }

    protected class RightAudioViewHolder extends RecyclerView.ViewHolder {

        public TextView rightTimeTV, senderNameTV;
        public ImageView rightMessageStatusIV, rightBubbleIconIV;
        public ExpandableLayout rightEL;
        public CardView rightBubbleIconCV;
        public Message message;
        public SeekBar audioSeekbar;
        public PlayPauseView playPauseView;
        public android.os.Handler handler;

        public RightAudioViewHolder(View view) {
            super(view);


            audioSeekbar = view.findViewById(R.id.audioSeekbar);
            playPauseView = view.findViewById(R.id.play_pause_view);
            rightTimeTV = view.findViewById(R.id.rightTimeTV);
            rightEL = view.findViewById(R.id.rightEL);
            senderNameTV = view.findViewById(R.id.senderNameTV);
            rightBubbleIconCV = view.findViewById(R.id.rightBubbleIconCV);
            rightBubbleIconIV = view.findViewById(R.id.rightBubbleIconIV);
            setBackgroundColor(rightBubbleLayoutColor);
            setSeekBarLineColor(rightBubbleTextColor);
            setSeekBarThumbColor(leftBubbleLayoutColor);
            setTimeTextColor(timeTextColor);
            setSenderNameTextColor(senderNameTextColor);
            showSenderName(showSenderName);
            showRightBubbleIcon(showRightBubbleIcon);
            handler = new android.os.Handler();
            audioSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    if (mediaPlayer != null) {
                        if (playingposition == message.getAudioUri().toString()) {
                            mediaPlayer.seekTo(seekBar.getProgress());
                        }
                    }
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    if (mediaPlayer != null) {
                        if (playingposition == message.getAudioUri().toString()) {
                            mediaPlayer.seekTo(seekBar.getProgress());
                        }
                    }
                }
            });
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (message != null) {
                        if (playingposition == message.getAudioUri().toString()) {
                            if (mediaPlayer != null) {
                                if (mediaPlayer.isPlaying()) {

                                    audioSeekbar.setProgress(mediaPlayer.getCurrentPosition());
                                    if (playPauseView.isPlay()) {
                                        playPauseView.change(false);
                                    }
                                } else {
                                    playPauseView.change(true);
                                }
                            } else {
                                playPauseView.change(true);
                            }

                        } else {

                            audioSeekbar.setProgress(0);
                            playPauseView.change(true);
                            playPauseView.change(true);
                        }
                    }
                    handler.postDelayed(this, 1000);

                }
            });

            playPauseView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {

                        if (playingposition == message.getAudioUri().toString()) {
                            mediaPlayer.stop();
                            mediaPlayer.reset();
                            mediaPlayer.release();
                            mediaPlayer = null;
                            playPauseView.change(true);
                        } else {

                            mediaPlayer.stop();
                            mediaPlayer.reset();
                            mediaPlayer.release();
                            mediaPlayer = null;
                            mediaPlayer = MediaPlayer.create(v.getContext(), message.getAudioUri());
                            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                @Override
                                public void onPrepared(MediaPlayer mediaPlayer) {

                                    mediaPlayer.start();
                                    playingposition = message.getAudioUri().toString();
                                    audioSeekbar.setMax(mediaPlayer.getDuration());
                                    playPauseView.change(false);
                                }
                            });

                        }
                    } else {

                        mediaPlayer = MediaPlayer.create(v.getContext(), message.getAudioUri());
                        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mediaPlayer) {

                                mediaPlayer.start();
                                playingposition = message.getAudioUri().toString();
                                audioSeekbar.setMax(mediaPlayer.getDuration());
                                playPauseView.change(false);
                            }
                        });
                    }


                }
            });
            FontChanger fontChanger = new FontChanger(typeface);
            fontChanger.replaceFonts((ViewGroup) view);

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    int pos = getLayoutPosition();

                    return true;
                }
            });
        }

        public Message getMessage() {
            return message;
        }

        public void setMessage(final Message message) {
            this.message = message;

        }

        public void setBackgroundColor(int color) {
            Drawable backgroundDrawable = DrawableCompat.wrap(audioSeekbar.getBackground()).mutate();
            DrawableCompat.setTint(backgroundDrawable, color);
        }

        public void setSeekBarLineColor(int color) {
            audioSeekbar.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.MULTIPLY);

        }

        public void setSeekBarThumbColor(int color) {
            Drawable backgroundDrawable1 = DrawableCompat.wrap(audioSeekbar.getThumb()).mutate();
            DrawableCompat.setTint(backgroundDrawable1, color);
        }

        public void setTimeTextColor(int color) {
            rightTimeTV.setTextColor(color);
        }

        public void setSenderNameTextColor(int color) {
            senderNameTV.setTextColor(color);
        }

        public void showSenderName(boolean b) {
            if (b) {
                senderNameTV.setVisibility(View.VISIBLE);
            } else {
                senderNameTV.setVisibility(View.GONE);
            }
        }

        public void showRightBubbleIcon(boolean b) {
            if (b) {
                rightBubbleIconCV.setVisibility(View.VISIBLE);
            } else {
                rightBubbleIconCV.setVisibility(View.GONE);
            }
        }

    }


    protected class LeftTypingViewHolder extends RecyclerView.ViewHolder {


        public LeftTypingViewHolder(View view) {
            super(view);


            FontChanger fontChanger = new FontChanger(typeface);
            fontChanger.replaceFonts((ViewGroup) view);
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    int pos = getLayoutPosition();

                    return true;
                }
            });
        }
    }

    public boolean onBackpress() {
        if (checkFeedbackContentVisible) {
            notifyDataSetChanged();
            return true;
        }
        return false;
    }

    public static String getDomainName(String url) throws URISyntaxException {
        if (url == null || url.equals("")) return null;
        URI uri = new URI(url);
        String domain = uri.getHost();
        return domain.startsWith("www.") ? domain.substring(4) : domain;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {


        final Message message = messageList.get(position);
        BaseResponse baseResponse = message.getBaseResponse();
        messageList.get(position).setIndexPosition(position);


        if (holder instanceof LeftTextViewHolder) {
            final LeftTextViewHolder holder1 = (LeftTextViewHolder) holder;
            holder1.gotoLink.setVisibility(View.GONE);
            holder1.layoutFeedback.setVisibility(View.GONE);
            holder1.layoutBottomTextview.setVisibility(View.GONE);
            holder1.layoutBottomTextview1.setVisibility(View.GONE);
            holder1.layoutAnswerText.setVisibility(View.GONE);
            holder1.layoutFeedBackContent.setVisibility(View.GONE);
            holder1.moreAnswer.setVisibility(View.GONE);
            holder1.layoutAnswering.setVisibility(View.GONE);
            holder1.layoutContentSairoi.setVisibility(View.GONE);
            holder1.layoutContentLike.setVisibility(View.GONE);
            holder1.layoutContentGuichuyengia.setVisibility(View.GONE);
            holder1.layoutGuiChuyenGiaClick.setVisibility(View.GONE);
            holder1.layoutAnswerFromChuyenGia.setVisibility(View.GONE);
            holder1.leftTVSource.setVisibility(View.GONE);
            holder1.leftTV.setBackground(context.getResources().getDrawable(R.drawable.left_tv_bg));
            if (message.isAnswer()) {
                holder1.layoutAnswering.setVisibility(View.VISIBLE);
                holder1.layoutAnswerText.setVisibility(View.GONE);

            } else {
                if (message.isAnswerFromChuyengia()) {
                    holder1.layoutAnswerFromChuyenGia.setVisibility(View.VISIBLE);
                    ResponseAnswer responseAnswer = message.getResponseAnswer();
                    if (responseAnswer != null) {
                        holder1.questionFromChuyenGia.setText(responseAnswer.getQuestion());
                        holder1.answerFromChuyenGia.setText(responseAnswer.getAnswer());
                    }
                } else {
                    holder1.layoutAnswerText.setVisibility(View.VISIBLE);
                }
                holder1.layoutBottomTextview.setVisibility(View.VISIBLE);
                holder1.layoutFeedBackContent.setVisibility(View.VISIBLE);
                if (message.getBody() != null)
                    holder1.leftTV.setText(Html.fromHtml(message.getBody()), TextView.BufferType.SPANNABLE);

                message.setWebUrl(message.getWebUrl());
                if (baseResponse != null) {
                    if (baseResponse.getMessage() != null) {
                        for (int i = 0; i < message.getBaseResponse().getMessage().size(); i++) {
                            if (message.getBaseResponse().getMessage().get(i).isIsfocus()) {
                                if (message.getBaseResponse().getMessage().get(i).getText() != null)
                                    holder1.leftTV.setText(Html.fromHtml(message.getBaseResponse().getMessage().get(i).getText()), TextView.BufferType.SPANNABLE);
                                message.setWebUrl(message.getBaseResponse().getMessage().get(i).getUrl());
                                if (message.getWebUrl() != null && !message.getWebUrl().equals("")) {
                                    Drawable background = context.getResources().getDrawable(R.drawable.left_tv_bg);
                                    switch (i) {
                                        case 0:
                                            if (message.getBaseResponse().getMessage().size() > 1)
                                                background = context.getResources().getDrawable(R.drawable.left_tv_bg_0);
                                            break;
                                        case 1:
                                            background = context.getResources().getDrawable(R.drawable.left_tv_bg_1);
                                            break;
                                        case 2:
                                            background = context.getResources().getDrawable(R.drawable.left_tv_bg_2);
                                            break;
                                        case 3:
                                            background = context.getResources().getDrawable(R.drawable.left_tv_bg_3);
                                            break;
                                    }
                                    holder1.leftTV.setBackground(background);
                                }
                            }
                        }
                    }
                }
                if (message.getWebUrl() != null && !message.getWebUrl().equals("")) {
                    holder1.gotoLink.setVisibility(View.VISIBLE);
                }


                holder1.leftTimeTV.setText(message.getTime());
                String rate = message.getRateMessage();
                if (rate != null) {
                    if (rate.equals("")) {
                    } else if (rate.equals("like")) {
                        holder1.layoutFeedBackContent.setVisibility(View.VISIBLE);
                        holder1.layoutBottomTextview.setVisibility(View.VISIBLE);
                        holder1.layoutContentLike.setVisibility(View.VISIBLE);
                    } else if (rate.equals("dislike")) {
                        holder1.layoutContentSairoi.setVisibility(View.VISIBLE);
                        holder1.layoutFeedBackContent.setVisibility(View.VISIBLE);
                        holder1.layoutBottomTextview.setVisibility(View.VISIBLE);
                    }
                }
                holder1.layoutAnswerText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (holder1.layoutFeedback.getVisibility() == View.VISIBLE) {
                            holder1.layoutBottomTextview1.setVisibility(View.GONE);
                            holder1.layoutFeedback.setVisibility(View.GONE);
                            checkFeedbackContentVisible = false;
                            return;
                        }
                    }
                });
                holder1.layoutFeedBackContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (holder1.layoutFeedback.getVisibility() == View.VISIBLE) {
                            holder1.layoutBottomTextview1.setVisibility(View.GONE);
                            holder1.layoutFeedback.setVisibility(View.GONE);
                            checkFeedbackContentVisible = false;
                            return;
                        }
                        holder1.layoutBottomTextview1.setVisibility(View.VISIBLE);
                        holder1.layoutFeedback.setVisibility(View.VISIBLE);
                        checkFeedbackContentVisible = true;
                    }

                });
                holder1.gotoLink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (message.getWebUrl() == null) return;
                        if (!message.getWebUrl().equals("")) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(message.getWebUrl()));
                            context.startActivity(browserIntent);
                        }
                    }
                });

            }
            if (baseResponse != null) {
                int answersCode = (int) baseResponse.getAnswerCode();
                if (answersCode == 2) {
                    holder1.layoutGuiChuyenGiaClick.setVisibility(View.VISIBLE);
                }
                boolean isSendMaster = message.isSendMaster();
                if (isSendMaster) {
                    holder1.layoutFeedBackContent.setVisibility(View.VISIBLE);
                    holder1.layoutBottomTextview.setVisibility(View.VISIBLE);
                    holder1.layoutContentGuichuyengia.setVisibility(View.VISIBLE);
                    holder1.layoutGuiChuyenGiaClick.setVisibility(View.GONE);
                }

                final List<Answer> answers = baseResponse.getMessage();
                if (answers != null) {
                    if (answers.size() > 1) {
                        for (int i = 0; i < answers.size(); i++) {
                            String url = answers.get(i).getUrl();
                            try {
                                answers.get(i).setDomain(getDomainName(url));
                            } catch (URISyntaxException e) {
                                e.printStackTrace();
                            }
                        }
                        ListQuestionAdapter listQuestionAdapter = new ListQuestionAdapter(context, ListQuestionAdapter.TYPE_LIST_SUGGESTION, answers, new ListQuestionAdapter.OnItemClick() {
                            @Override
                            public void onClick(int position) {
                                for (int i = 0; i < answers.size(); i++) {
                                    if (i == position) {
                                        answers.get(i).setIsfocus(true);
                                    } else answers.get(i).setIsfocus(false);
                                }
                                notifyDataSetChanged();
                                if (answers.get(position).getText() != null)
                                    holder1.leftTV.setText(Html.fromHtml(answers.get(position).getText()), TextView.BufferType.SPANNABLE);
                                int background = context.getResources().getIdentifier("left_tv_bg_" + position, "drawable", context.getPackageName());
                                holder1.leftTV.setBackgroundResource(background);

                                message.setWebUrl(answers.get(position).getUrl());
                            }
                        });
                        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                        holder1.moreAnswer.setLayoutManager(mLinearLayoutManager);
                        OverScrollDecoratorHelper.setUpOverScroll(holder1.moreAnswer, OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL);
                        holder1.moreAnswer.setAdapter(listQuestionAdapter);
                        holder1.moreAnswer.setVisibility(View.VISIBLE);
                    } else if (answers.size() == 1) {
                        try {
                            String domain = getDomainName(answers.get(0).getUrl());
                            if (domain != null && !domain.equals("")) {
                                holder1.leftTVSource.setText("Nguồn " + domain);
                                holder1.leftTVSource.setVisibility(View.VISIBLE);
                            }
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } else if (holder instanceof RightTextViewHolder) {
            final RightTextViewHolder holder1 = (RightTextViewHolder) holder;
            holder1.rightTV.setText(message.getBody());
            holder1.rightTimeTV.setText(message.getTime());
        } else if (holder instanceof LeftImageViewHolder) {
            final LeftImageViewHolder holder1 = (LeftImageViewHolder) holder;

            if (message.getUserIcon() != null) {
                Picasso.with(context).load(message.getUserIcon()).into(holder1.leftBubbleIconIV);
            }
            holder1.senderNameTV.setText(message.getUserName());
            if (message.getImageList().get(0) != null && !message.getImageList().get(0).equals("")) {
                final File image = DiskCacheUtils.findInCache(message.getImageList().get(0).toString(), imageLoader.getDiskCache());
                if (image != null && image.exists()) {
                    Picasso.with(context).load(image).into(holder1.leftIV);
                } else {
                    imageLoader.loadImage(message.getImageList().get(0).toString(), new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String s, View view) {
                            holder1.leftIV.setImageBitmap(null);
                        }

                        @Override
                        public void onLoadingFailed(String s, View view, FailReason failReason) {

                        }

                        @Override
                        public void onLoadingComplete(String s, View view, final Bitmap bitmap) {
                            Picasso.with(context).load(s).into(holder1.leftIV);

                        }

                        @Override
                        public void onLoadingCancelled(String s, View view) {

                        }
                    });
                }
            } else {
                holder1.leftIV.setImageBitmap(null);
            }

            holder1.leftTimeTV.setText(message.getTime());

            holder1.leftIV.setTransitionName("photoTransition");
            holder1.leftIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ImageFFActivity.class);
                    intent.putExtra("photoURI", message.getImageList().get(0).toString());
                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, holder1.leftIV, holder1.leftIV.getTransitionName());
                    context.startActivity(intent, optionsCompat.toBundle());
                }
            });
        } else if (holder instanceof RightImageViewHolder) {
            final RightImageViewHolder holder1 = (RightImageViewHolder) holder;

            if (message.getUserIcon() != null) {
                Picasso.with(context).load(message.getUserIcon()).into(holder1.rightBubbleIconIV);
            }
            holder1.senderNameTV.setText(message.getUserName());

            if (message.getImageList().get(0) != null && !message.getImageList().get(0).equals("")) {
                final File image = DiskCacheUtils.findInCache(message.getImageList().get(0).toString(), imageLoader.getDiskCache());
                if (image != null && image.exists()) {
                    Picasso.with(context).load(image).into(holder1.rightIV);
                } else {
                    imageLoader.loadImage(message.getImageList().get(0).toString(), new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String s, View view) {
                            holder1.rightIV.setImageBitmap(null);
                        }

                        @Override
                        public void onLoadingFailed(String s, View view, FailReason failReason) {

                        }

                        @Override
                        public void onLoadingComplete(String s, View view, final Bitmap bitmap) {
                            Picasso.with(context).load(s).into(holder1.rightIV);

                        }

                        @Override
                        public void onLoadingCancelled(String s, View view) {

                        }
                    });
                }
            } else {
                holder1.rightIV.setImageBitmap(null);
            }
            holder1.rightIV.setTransitionName("photoTransition");
            holder1.rightIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ImageFFActivity.class);
                    intent.putExtra("photoURI", message.getImageList().get(0).toString());
                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, holder1.rightIV, holder1.rightIV.getTransitionName());
                    context.startActivity(intent, optionsCompat.toBundle());
                }
            });
            holder1.rightTimeTV.setText(message.getTime());

        } else if (holder instanceof LeftImagesViewHolder) {
            final LeftImagesViewHolder holder1 = (LeftImagesViewHolder) holder;

            if (message.getUserIcon() != null) {
                Picasso.with(context).load(message.getUserIcon()).into(holder1.leftBubbleIconIV);
            }
            holder1.senderNameTV.setText(message.getUserName());

            List<String> imageList = new ArrayList<>();
            for (int i = 0; i < message.getImageList().size(); i++) {
                imageList.add(message.getImageList().get(i).toString());
            }
            holder1.leftTimeTV.setText(message.getTime());

            holder1.leftCollageView
                    .photoMargin(8)
                    .photoPadding(0)
                    .backgroundColor(leftBubbleLayoutColor)
                    .useFirstAsHeader(false) // makes first photo fit device widtdh and use full line
                    .defaultPhotosForLine(2) // sets default photos number for line of photos (can be changed by program at runtime)
                    .useCards(true)// adds cardview backgrounds to all photos
                    .loadPhotos(imageList);

            holder1.leftCollageView.setTransitionName("photoTransition");
            holder1.leftCollageView.setOnPhotoClickListener(new CollageView.OnPhotoClickListener() {
                @Override
                public void onPhotoClick(int i) {

                    Intent intent = new Intent(context, ImageFFActivity.class);
                    intent.putExtra("photoURI", message.getImageList().get(i).toString());
                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, holder1.leftCollageView, holder1.leftCollageView.getTransitionName());
                    context.startActivity(intent, optionsCompat.toBundle());
                }
            });
        } else if (holder instanceof RightImagesViewHolder) {
            final RightImagesViewHolder holder1 = (RightImagesViewHolder) holder;

            if (message.getUserIcon() != null) {
                Picasso.with(context).load(message.getUserIcon()).into(holder1.rightBubbleIconIV);
            }
            holder1.senderNameTV.setText(message.getUserName());
            List<String> imageList = new ArrayList<>();
            for (int i = 0; i < message.getImageList().size(); i++) {
                imageList.add(message.getImageList().get(i).toString());
            }
            holder1.rightTimeTV.setText(message.getTime());
            holder1.rightCollageView
                    .photoMargin(8)
                    .photoPadding(0)
                    .backgroundColor(rightBubbleLayoutColor)
                    .useFirstAsHeader(false) // makes first photo fit device widtdh and use full line
                    .defaultPhotosForLine(2) // sets default photos number for line of photos (can be changed by program at runtime)
                    .useCards(true)// adds cardview backgrounds to all photos
                    .loadPhotos(imageList);

            holder1.rightCollageView.setTransitionName("photoTransition");
            holder1.rightCollageView.setOnPhotoClickListener(new CollageView.OnPhotoClickListener() {
                @Override
                public void onPhotoClick(int i) {

                    Intent intent = new Intent(context, ImageFFActivity.class);
                    intent.putExtra("photoURI", message.getImageList().get(i).toString());
                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, holder1.rightCollageView, holder1.rightCollageView.getTransitionName());
                    context.startActivity(intent, optionsCompat.toBundle());
                }

            });
        } else if (holder instanceof LeftTypingViewHolder) {

        } else if (holder instanceof LeftVideoViewHolder) {
            final LeftVideoViewHolder holder1 = (LeftVideoViewHolder) holder;
            final VideoPlayer videoPlayer = new VideoPlayer(context);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            videoPlayer.setLayoutParams(params);
            videoPlayer.setScaleType(VideoPlayer.ScaleType.CENTER_CROP);
            //((LeftVideoViewHolder) holder).videoLL.getLayoutParams().height = getScreenWidth(context) * 9 /16;
            //holder1.videoLL.removeAllViews();
            holder1.videoLL.addView(videoPlayer);
            videoPlayer.loadVideo(message.getVideoUri().toString(), message);
            if (message.getUserIcon() != null) {
                Picasso.with(context).load(message.getUserIcon()).into(holder1.leftBubbleIconIV);
            }

            videoPlayer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                    }
                    videoPlayer.setTransitionName("videoFF");
                    Intent intent = new Intent(context, VideoFFActivity.class);
                    intent.putExtra("videoURI", message.getVideoUri().toString());
                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, videoPlayer, videoPlayer.getTransitionName());
                    context.startActivity(intent, optionsCompat.toBundle());
                }
            });
            holder1.senderNameTV.setText(message.getUserName());

            holder1.leftTimeTV.setText(message.getTime());

        } else if (holder instanceof RightVideoViewHolder) {
            final RightVideoViewHolder holder1 = (RightVideoViewHolder) holder;
            final VideoPlayer videoPlayer = new VideoPlayer(context);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            videoPlayer.setScaleType(VideoPlayer.ScaleType.CENTER_CROP);
            videoPlayer.setLayoutParams(params);
            //((RightVideoViewHolder) holder).videoLL.getLayoutParams().height = getScreenWidth(context) * 9 /16;
            //holder1.videoLL.removeAllViews();
            holder1.videoLL.addView(videoPlayer);
            videoPlayer.loadVideo(message.getVideoUri().toString(), message);
            //adjustAspectRatio(videoPlayer,videoPlayer.getMp().getVideoWidth(),videoPlayer.getMp().getVideoHeight());

            if (message.getUserIcon() != null) {
                Picasso.with(context).load(message.getUserIcon()).into(holder1.rightBubbleIconIV);
            }

            videoPlayer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                    }
                    videoPlayer.setTransitionName("videoFF");
                    Intent intent = new Intent(context, VideoFFActivity.class);
                    intent.putExtra("videoURI", message.getVideoUri().toString());
                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, videoPlayer, videoPlayer.getTransitionName());
                    context.startActivity(intent, optionsCompat.toBundle());
                }
            });
            holder1.senderNameTV.setText(message.getUserName());

            holder1.rightTimeTV.setText(message.getTime());
        } else if (holder instanceof LeftAudioViewHolder) {
            final LeftAudioViewHolder holder1 = (LeftAudioViewHolder) holder;

            holder1.leftTimeTV.setText(message.getTime());

            if (message.getUserIcon() != null) {
                Picasso.with(context).load(message.getUserIcon()).into(holder1.leftBubbleIconIV);
            }
            holder1.senderNameTV.setText(message.getUserName());
            holder1.setMessage(message);

        } else if (holder instanceof RightAudioViewHolder) {
            final RightAudioViewHolder holder1 = (RightAudioViewHolder) holder;

            holder1.rightTimeTV.setText(message.getTime());
            if (message.getUserIcon() != null) {
                Picasso.with(context).load(message.getUserIcon()).into(holder1.rightBubbleIconIV);
            }


            holder1.senderNameTV.setText(message.getUserName());

            holder1.setMessage(message);
        } else if (holder instanceof ListQuestionViewHolder) {
            final ListQuestionViewHolder holder1 = (ListQuestionViewHolder) holder;
            ListQuestionAdapter listQuestionAdapter = new ListQuestionAdapter(context, ListQuestionAdapter.TYPE_LIST_QUESTION, null, null);
            LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            holder1.rvListQuestion.setLayoutManager(mLinearLayoutManager);
            holder1.rvListQuestion.setAdapter(listQuestionAdapter);
        } else if (holder instanceof ListSuggestionViewHolder) {
            final ListSuggestionViewHolder holder1 = (ListSuggestionViewHolder) holder;
            ListQuestionAdapter listQuestionAdapter = new ListQuestionAdapter(context, ListQuestionAdapter.TYPE_LIST_SUGGESTION, null, null);
            LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            holder1.rvListSuggestion.setLayoutManager(mLinearLayoutManager);
            holder1.rvListSuggestion.setAdapter(listQuestionAdapter);
        } else if (holder instanceof LeftHtmlViewHolder) {
            final LeftHtmlViewHolder holder1 = (LeftHtmlViewHolder) holder;
            holder1.senderNameTV.setText(message.getUserName());
            holder1.webView.loadDataWithBaseURL("", message.getBody(), "text/html", "UTF-8", "");
            // holder1.webView.loadUrl(message.getBody());
            // webView.loadUrl("file:///android_asset/thoitiet.html");
        }
    }


    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        Log.d(TAG, "onViewRecycledCalled");
        if (holder instanceof LeftVideoViewHolder) {
            ((LeftVideoViewHolder) holder).videoLL.removeAllViews();
        } else {
            if (holder instanceof RightVideoViewHolder) {
                ((RightVideoViewHolder) holder).videoLL.removeAllViews();
            }
        }

    }


    @Override
    public int getItemCount() {
        return filterList.size();
    }

// set adapter filtered list

    public void setList(List<Message> list) {
        this.filterList = list;
    }

    //call when you want to filter
    public void filterList(String text) {
        filter.filter(text);
    }

    public String getTime() {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("dd MMM yyyy HH:mm");
        String time = mdformat.format(calendar.getTime());
        return time;
    }


    public void showLeftBubbleIcon(boolean b) {
        this.showLeftBubbleIcon = b;
    }

    public void showRightBubbleIcon(boolean b) {
        this.showRightBubbleIcon = b;
    }

    public void setLeftBubbleLayoutColor(int color) {
        this.leftBubbleLayoutColor = color;
    }

    public void setRightBubbleLayoutColor(int color) {
        this.rightBubbleLayoutColor = color;


    }

    public void setLeftBubbleTextColor(int color) {
        this.leftBubbleTextColor = color;
    }

    public void setRightBubbleTextColor(int color) {
        this.rightBubbleTextColor = color;
    }

    public void setTimeTextColor(int color) {
        this.timeTextColor = color;
    }

    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
    }

    public void showSenderName(boolean b) {
        this.showSenderName = b;
    }

    public void setSenderNameTextColor(int color) {
        this.senderNameTextColor = color;
    }

    public void setTextSize(float size) {
        this.textSize = size;
    }

    public static int getScreenWidth(Context c) {
        int screenWidth = 0; // this is part of the class not the method
        if (screenWidth == 0) {
            WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenWidth = size.x;
        }

        return screenWidth;
    }

    private void adjustAspectRatio(VideoPlayer m_TextureView, int videoWidth, int videoHeight) {
        int viewWidth = m_TextureView.getWidth();
        int viewHeight = m_TextureView.getHeight();
        double aspectRatio = (double) videoHeight / videoWidth;

        int newWidth, newHeight;
        if (viewHeight > (int) (viewWidth * aspectRatio)) {
            // limited by narrow width; restrict height
            newWidth = viewWidth;
            newHeight = (int) (viewWidth * aspectRatio);
        } else {
            // limited by short height; restrict width
            newWidth = (int) (viewHeight / aspectRatio);
            newHeight = viewHeight;
        }
        int xoff = (viewWidth - newWidth) / 2;
        int yoff = (viewHeight - newHeight) / 2;
        Log.v(TAG, "video=" + videoWidth + "x" + videoHeight +
                " view=" + viewWidth + "x" + viewHeight +
                " newView=" + newWidth + "x" + newHeight +
                " off=" + xoff + "," + yoff);

        Matrix txform = new Matrix();
        m_TextureView.getTransform(txform);
        txform.setScale((float) newWidth / viewWidth, (float) newHeight / viewHeight);
        //txform.postRotate(10);          // just for fun
        txform.postTranslate(xoff, yoff);
        m_TextureView.setTransform(txform);
    }

    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }


}
