package viettel.cyberspace.assitant.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.google.cloud.android.speech.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import chatview.data.ListQuestionAdapter;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import viettel.cyberspace.assitant.model.QuestionExperts;
import viettel.cyberspace.assitant.model.ResponseAnswer;
import viettel.cyberspace.assitant.model.ResponseGetExpertsAnswer;
import viettel.cyberspace.assitant.model.ResponsePutExpertsAnswer;
import viettel.cyberspace.assitant.model.ResponseQuestionExperts;
import viettel.cyberspace.assitant.model.User;
import viettel.cyberspace.assitant.rest.ApiClient;
import viettel.cyberspace.assitant.rest.ApiInterface;
import viettel.cyberspace.assitant.storage.StorageManager;

/**
 * Created by brwsr on 17/05/2018.
 */

@SuppressLint("ValidFragment")
public class NotificationFragment extends DialogFragment implements NotificationAdapter.NotificationListener {
    private View rootView;
    RecyclerView rvNotification;
    SwipeRefreshLayout refresh_layout;
    ApiInterface apiService;
    NotificationAdapter notificationExpertAdapter;
    NotificationAdapter notificationUserAdapter;
    List<QuestionExperts> questionExpertsList;
    List<ResponseAnswer> responseAnswers;
    ProgressDialog progressDialog;
    MaterialRippleLayout back;
    public boolean isExpert = false;
    Timer timer;

    public interface NotificationListener {
        void getQuestion();

        void getAnswer();

    }

    NotificationListener notificationListener;

    @SuppressLint("ValidFragment")
    public NotificationFragment(boolean isExpert, NotificationListener notificationListener) {
        this.isExpert = isExpert;
        this.notificationListener = notificationListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.layout_notification, container, false);
        return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();


        Window window = getDialog().getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams windowParams = window.getAttributes();
        windowParams.dimAmount = 0.0f;
        window.setAttributes(windowParams);
        // ... other stuff you want to do in your onStart() method
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        apiService =
                ApiClient.getClient().create(ApiInterface.class);
        rvNotification = view.findViewById(R.id.rvNotification);
        refresh_layout = view.findViewById(R.id.refresh_layout);
        back = view.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        refresh_layout.setColorSchemeResources(
                R.color.dtbutton_color_alizarin,
                R.color.dtbutton_color_wisteria,
                R.color.dtbutton_color_carrot);
        refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                User user = StorageManager.getUser(getContext());
                if (user != null) {
                    if (isExpert) {
                        notificationListener.getQuestion();
                    } else {
                        notificationListener.getAnswer();
                    }
                } else {
                    refresh_layout.setRefreshing(false);
                }
            }
        });
/*        User user = StorageManager.getUser(getContext());
        if (user != null) {
            if (user.getUser_type().equals("Experts")) {
                isExpert = true;
                getExpertsQuestion();
            } else {
                isExpert = false;
                getExpertAnswers();
            }
        }*/

        questionExpertsList = new ArrayList<>();
        responseAnswers = new ArrayList<>();
        notificationExpertAdapter = new NotificationAdapter(getContext(), questionExpertsList, this);
        notificationUserAdapter = new NotificationAdapter(getContext(), responseAnswers, this, true);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvNotification.setLayoutManager(mLinearLayoutManager);
        if (isExpert)
            rvNotification.setAdapter(notificationExpertAdapter);
        else rvNotification.setAdapter(notificationUserAdapter);
        if (isExpert) {
            notificationListener.getQuestion();
        } else {
            notificationListener.getAnswer();
        }
/*        TimerTask timerTask = new MyTimerTask();
        timer = new Timer(true);
        timer.scheduleAtFixedRate(timerTask, 0, 10 * 1000);*/
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    // in case user is Expert
    public void updateViewExpertQuestion(List<QuestionExperts> questionExperts) {
        questionExpertsList.clear();
        questionExpertsList.addAll(questionExperts);
        notificationExpertAdapter.notifyDataSetChanged();
        refresh_layout.setRefreshing(false);
    }

    // in case user is normal user
    public void updateViewExpertAnswer(List<ResponseAnswer> responseAnswersList) {
        responseAnswers.clear();
        responseAnswers.addAll(responseAnswersList);
        notificationUserAdapter.notifyDataSetChanged();
        refresh_layout.setRefreshing(false);
    }

    private void getExpertAnswers() {
        User user = StorageManager.getUser(getContext());
        if (user == null) return;
        HashMap<String, String> map = new HashMap<>();
        map.put("username", StorageManager.getStringValue(getContext(), "account", ""));
        map.put("token", user.getToken());
        map.put("userId", user.getUserId() + "");
        Call<ResponseGetExpertsAnswer> call = apiService.getExpertsAnswer(map);
        call.enqueue(new Callback<ResponseGetExpertsAnswer>() {
            @Override
            public void onResponse(Call<ResponseGetExpertsAnswer> call, Response<ResponseGetExpertsAnswer> response) {
                refresh_layout.setRefreshing(false);
                if (response.isSuccessful()) {
                    if (response.isSuccessful()) {
                        responseAnswers.clear();
                        responseAnswers.addAll(response.body().getAnswerList());
                        notificationUserAdapter.notifyDataSetChanged();
                    } else {
                    }
                } else {
                }

            }

            @Override
            public void onFailure(Call<ResponseGetExpertsAnswer> call, Throwable throwable) {
                refresh_layout.setRefreshing(false);

            }
        });
    }

    public void getExpertsQuestion() {
        User user = StorageManager.getUser(getContext());
        if (user == null) return;
        HashMap<String, String> map = new HashMap<>();
        map.put("username", StorageManager.getStringValue(getContext(), "account", ""));
        map.put("token", user.getToken());
        map.put("userId", user.getUserId() + "");
        Call<ResponseQuestionExperts> call = apiService.getExpertsQuestion(map);
        call.enqueue(new Callback<ResponseQuestionExperts>() {
            @Override
            public void onResponse(Call<ResponseQuestionExperts> call, Response<ResponseQuestionExperts> response) {
                refresh_layout.setRefreshing(false);
                if (response.isSuccessful()) {
                    questionExpertsList.clear();
                    questionExpertsList.addAll(response.body().getQuestionList());
                    notificationExpertAdapter.notifyDataSetChanged();
                } else {
                }

            }

            @Override
            public void onFailure(Call<ResponseQuestionExperts> call, Throwable throwable) {
                refresh_layout.setRefreshing(false);
            }
        });
    }


    public class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            if (isExpert) {
                getExpertsQuestion();
            } else {
                getExpertAnswers();
            }
        }
    }

    @Override
    public void onItemClick(int position, boolean isUser) {
        openPopupAnswer(position, isUser);
    }

    AlertDialog alertDialog = null;

    private void openPopupAnswer(final int position, final boolean isUser) {
        View dialogView;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.layout_send_question, null);
        TextView tvquestion = dialogView.findViewById(R.id.tvquestion);
        TextView tvAnswer = dialogView.findViewById(R.id.tvAnswer);
        final EditText etAnswer = dialogView.findViewById(R.id.etAnswer);
        Button btCancel = dialogView.findViewById(R.id.btCancel);
        Button btSend = dialogView.findViewById(R.id.btSend);
        Button btDone = dialogView.findViewById(R.id.btDone);
        String questionContent = "";
        if (isUser) {
            questionContent = responseAnswers.get(position).getQuestion();
        } else {
            questionContent = questionExpertsList.get(position).getQuestion();
        }
        String question = "<font color='#01C7FE'>Câu hỏi: </font><font color='#000000'>" + questionContent + "</font>";
        tvquestion.setText(Html.fromHtml(question), TextView.BufferType.SPANNABLE);
        String answer = "<font color='#01C7FE'>Câu trả lời: </font>";
        tvAnswer.setText(Html.fromHtml(answer), TextView.BufferType.SPANNABLE);
        if (isUser) {
            btCancel.setVisibility(View.GONE);
            btSend.setVisibility(View.GONE);
            btDone.setVisibility(View.VISIBLE);
            tvAnswer.setVisibility(View.VISIBLE);
            etAnswer.setText(responseAnswers.get(position).getAnswer());
        } else {
            btCancel.setVisibility(View.VISIBLE);
            btSend.setVisibility(View.VISIBLE);
            btDone.setVisibility(View.GONE);
            tvAnswer.setVisibility(View.GONE);
        }
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etAnswer.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Vui lòng nhập câu trả lời!", Toast.LENGTH_SHORT).show();
                    return;
                }
                sendAnswer(questionExpertsList.get(position), etAnswer.getText().toString());
                progressDialog = ProgressDialog.show(getActivity(), "Vui lòng đợi",
                        "Đang xử lý...", true);
            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        btDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        dialogBuilder.setView(dialogView);
        alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    private void sendAnswer(QuestionExperts questionExperts, String s) {
        HashMap<String, String> map = new HashMap<>();
        map.put("username", "hungpv39");
        map.put("mid", questionExperts.getMid());
        map.put("answer", s);
        map.put("token", StorageManager.getUser(getContext()).getToken());
        map.put("userId", StorageManager.getUser(getContext()).getUserId() + "");
        Call<ResponsePutExpertsAnswer> call = apiService.putExpertsAnswer(map);
        call.enqueue(new Callback<ResponsePutExpertsAnswer>() {
            @Override
            public void onResponse(Call<ResponsePutExpertsAnswer> call, Response<ResponsePutExpertsAnswer> response) {
                if (progressDialog != null) progressDialog.dismiss();
                if (response.isSuccessful()) {
                    Toast.makeText(getActivity(), "Bạn đã gửi câu trả lời thành công!", Toast.LENGTH_SHORT).show();
                    if (alertDialog != null)
                        alertDialog.dismiss();
                } else {
                    Toast.makeText(getActivity(), "Đã có lỗi xảy ra trong quá trình gửi câu trả lời, vui lòng thử lại!!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponsePutExpertsAnswer> call, Throwable throwable) {
                if (progressDialog != null) progressDialog.dismiss();
                Toast.makeText(getActivity(), "Đã có lỗi xảy ra trong quá trình gửi câu trả lời, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
