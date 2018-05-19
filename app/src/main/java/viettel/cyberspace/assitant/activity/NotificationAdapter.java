package viettel.cyberspace.assitant.activity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.cloud.android.speech.R;

import java.util.List;
import java.util.zip.Inflater;

import chatview.data.MessageAdapter;
import viettel.cyberspace.assitant.model.QuestionExperts;
import viettel.cyberspace.assitant.model.ResponseAnswer;

import static viettel.cyberspace.assitant.utils.TextUtil.formatDateTime;

/**
 * Created by brwsr on 17/05/2018.
 */

public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<QuestionExperts> questionExpertsList;
    List<ResponseAnswer> responseAnswers;
    Context mContext;
    boolean isUser;

    public interface NotificationListener {
        public void onItemClick(int position, boolean isUser);

    }

    NotificationListener notificationListener;

    public NotificationAdapter(Context context, List<QuestionExperts> questionExpertsList, NotificationListener notificationListener) {
        this.questionExpertsList = questionExpertsList;
        this.mContext = context;
        this.notificationListener = notificationListener;
    }

    public NotificationAdapter(Context context, List<ResponseAnswer> responseAnswers, NotificationListener notificationListener, boolean isUser) {
        this.responseAnswers = responseAnswers;
        this.mContext = context;
        this.notificationListener = notificationListener;
        this.isUser = isUser;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        return new NotificationExpertViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        NotificationExpertViewHolder holder1 = (NotificationExpertViewHolder) holder;
        if (!isUser) {
            QuestionExperts questionExperts = questionExpertsList.get(position);
            holder1.from.setText(questionExperts.getUsername());
            holder1.time.setText(formatDateTime(questionExperts.getCreatedTime()));
            holder1.question.setText(questionExperts.getQuestion());
        } else {
            ResponseAnswer responseAnswer = responseAnswers.get(position);
            holder1.from.setText(responseAnswer.getExpertUsername());
            holder1.time.setText(formatDateTime(responseAnswer.getCreatedTime()));
            holder1.question.setText(responseAnswer.getQuestion());
        }
    }

    @Override
    public int getItemCount() {
        if (isUser) return responseAnswers.size();
        else
            return questionExpertsList.size();
    }

    public class NotificationExpertViewHolder extends RecyclerView.ViewHolder {
        TextView from;
        TextView time;
        TextView question;

        public NotificationExpertViewHolder(View itemView) {
            super(itemView);
            from = itemView.findViewById(R.id.from);
            time = itemView.findViewById(R.id.time);
            question = itemView.findViewById(R.id.question);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    notificationListener.onItemClick(getAdapterPosition(), isUser);
                }
            });
        }
    }

}
