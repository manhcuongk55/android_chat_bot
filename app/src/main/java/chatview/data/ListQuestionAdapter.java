package chatview.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.cloud.android.speech.R;


/**
 * Created by brwsr on 09/05/2018.
 */

public class ListQuestionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;

    public ListQuestionAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_question, parent, false);
        return new ListQuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

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
}
