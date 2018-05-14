package chatview.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.cloud.android.speech.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by brwsr on 09/05/2018.
 */

public class ListQuestionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    public static int TYPE_LIST_QUESTION = 1;
    public static int TYPE_LIST_SUGGESTION = 2;
    int type;
    List<String> suggestion = new ArrayList<>();

    public ListQuestionAdapter(Context context, int type) {
        this.context = context;
        this.type = type;
        suggestion.add("Tểu sử");
        suggestion.add("Quá trình học tập");
        suggestion.add("Gia đình");
        suggestion.add("Đảng chính trị");
        suggestion.add("Chức sắc");
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
            holder1.textSuggestion.setText(suggestion.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return 5;
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
        }
    }
}
