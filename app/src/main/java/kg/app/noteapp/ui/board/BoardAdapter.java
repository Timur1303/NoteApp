package kg.app.noteapp.ui.board;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;

import kg.app.noteapp.R;
import kg.app.noteapp.interfaces.OnItemClickListener;

public class BoardAdapter  extends RecyclerView.Adapter<BoardAdapter.ViewHolder>{

    private String[] titles = new String[]{"Fast", "Free","Powerful"};
    private String[] descriptions = new String[]{"Very, very fast", "Absolutely free", "True powerful"};
    private int[] animations = new int[]{R.raw.global_icon, R.raw.mail_icon, R.raw.smart_book};

    private OnItemClickListener onItemClickListener;

    public BoardAdapter(){

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pager_board, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(position);

    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class  ViewHolder extends RecyclerView.ViewHolder{

        private TextView textTitle;
        private TextView textDesc;
        private Button btnStart;
        private LottieAnimationView lottieAnimationView;

         public ViewHolder(@NonNull View itemView) {
             super(itemView);
             textTitle = itemView.findViewById(R.id.textTitle);
             textDesc = itemView.findViewById(R.id.textDesc);
             lottieAnimationView = itemView.findViewById(R.id.animationView);
             btnStart = itemView.findViewById(R.id.btnStart);
             btnStart.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     onItemClickListener.onItemClick(getAdapterPosition());
                 }
             });
         }

        public void bind(int position) {
             textTitle.setText(titles[position]);
             textDesc.setText(descriptions[position]);
             lottieAnimationView.setAnimation(animations[position]);
        }
    }
}
