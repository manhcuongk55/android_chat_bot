package chatview.data;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.cloud.android.speech.R;

import java.util.ArrayList;
import java.util.List;

import viettel.cyberspace.assitant.model.Answer;


/**
 * Created by brwsr on 09/05/2018.
 */

public class ListQuestionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    public static int TYPE_LIST_QUESTION = 1;
    public static int TYPE_LIST_SUGGESTION = 2;
    int type;
    List<Answer> suggestion = new ArrayList<>();

    public interface OnItemClick {
        void onClick(int position);
    }

    OnItemClick onItemClick;

    public ListQuestionAdapter(Context context, int type, List<Answer> suggestion, OnItemClick onItemClick) {
        this.context = context;
        this.type = type;
        this.suggestion = suggestion;
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (type == TYPE_LIST_QUESTION) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_question, parent, false);
            return new ListQuestionViewHolder(view);
        } else if (type == TYPE_LIST_SUGGESTION) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_suggest_question, parent, false);
            return new ListSuggestionViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ListSuggestionViewHolder) {
            ListSuggestionViewHolder holder1 = (ListSuggestionViewHolder) holder;
            holder1.textSuggestion.setText(suggestion.get(position).getDomain());
            if (suggestion.get(position).isIsfocus()) {
                int color = context.getResources().getColor(R.color.color_answer_0);
                switch (position) {
                    case 0:
                        color = context.getResources().getColor(R.color.color_answer_0);
                        break;
                    case 1:
                        color = context.getResources().getColor(R.color.color_answer_1);
                        break;
                    case 2:
                        color = context.getResources().getColor(R.color.color_answer_2);
                        break;
                    case 3:
                        color = context.getResources().getColor(R.color.color_answer_3);
                        break;
                }
                holder1.textSuggestion.setBackgroundColor(color);
                holder1.textSuggestion.setTextColor(Color.WHITE);
            } else {
                holder1.textSuggestion.setBackgroundColor(Color.WHITE);
                holder1.textSuggestion.setTextColor(Color.GRAY);
            }

        }
    }

    @Override
    public int getItemCount() {
        int count = 0;
        for (int i = 0; i < suggestion.size(); i++) {
            if (suggestion.get(i).getDomain() != null)
                count++;
        }
        return count;
    }

    public class ListQuestionViewHolder extends RecyclerView.ViewHolder {

        public ListQuestionViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class ListSuggestionViewHolder extends RecyclerView.ViewHolder {
        TextView textSuggestion;

        public ListSuggestionViewHolder(View itemView) {
            super(itemView);
            textSuggestion = itemView.findViewById(R.id.textSuggestion);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClick.onClick(getAdapterPosition());
                }
            });
        }
    }
}
