package kg.app.noteapp.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import kg.app.noteapp.R;
import kg.app.noteapp.interfaces.OnItemClickListener;
import kg.app.noteapp.models.Note;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private ArrayList<Note> list;
    protected OnItemClickListener onItemClickListener;

    public NoteAdapter() {
        list = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_note, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(list.get(position));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public Note getItem(int position){
        return list.get(position);
    }

    public void sortList(List<Note> sort){
        list.clear();
        list.addAll(sort);
        notifyDataSetChanged();
    }

    public void addItem(Note note){
        list.add(0,note);
        notifyItemChanged(list.indexOf(0));
    }


    public void delete(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }


    public void setList(List<Note> list){
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }
    public void setItem(Note note, int id){
        list.set(id, note);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView textTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitle);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(getAdapterPosition());
                }
            });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                onItemClickListener.onItemLongClick(getAdapterPosition());
                return true;
            }
        });
        }

        public void bind(Note note) {
            if (note!=null){
            textTitle.setText(note.getTitle());
        }}
    }
}
