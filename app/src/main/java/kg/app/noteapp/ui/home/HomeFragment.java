package kg.app.noteapp.ui.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import kg.app.noteapp.App;
import kg.app.noteapp.R;
import kg.app.noteapp.interfaces.OnItemClickListener;
import kg.app.noteapp.models.Note;

import static android.content.ContentValues.TAG;

public class HomeFragment extends Fragment {

    protected RecyclerView recyclerView;
    private ArrayList<Note> list;
    private NoteAdapter noteAdapter;
    protected int thisPosition;
    protected Note note;
    protected boolean update = false;
    private FirebaseFirestore db;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        noteAdapter = new NoteAdapter();
        setHasOptionsMenu(true);
        loadData();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_add) openForm();
        if (item.getItemId() == R.id.sortAlph) {
            noteAdapter.sortList(App.getAppDatabase().noteDao().getNoteAlphabet());
            return true;
        } else

            return super.onOptionsItemSelected(item);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerView);
        view.findViewById(R.id.fab).setOnClickListener(view1 ->{
            update = false;
            openForm();
        });
        setFragmentListener();
        initList(view);
    }

    private void loadData(){
        List<Note> list = App.getAppDatabase().noteDao().getAll();
        noteAdapter.setList(list);
    }


    private void initList(View view){

        recyclerView.setAdapter(noteAdapter);
        noteAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                update = true;
                thisPosition = position;
                noteAdapter.getItem(position);
                openForm();

            }

            @Override
            public void onItemLongClick(int position) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext())
                        .setTitle("Вы хотите удалить?")
                        .setPositiveButton("Да", (dialog, which) ->
                        {
                            noteAdapter.delete(position);
                            //deleteFromFireStore(note);
                            App.getAppDatabase().noteDao().deletable();
                        })
                        .setNegativeButton("Нет", null);
                alert.create().show();

            }

        });


    }




    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.home_menu, menu);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)




    private void setFragmentListener(){
        getParentFragmentManager().setFragmentResultListener("rk_form",
                getViewLifecycleOwner(),
                new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                        if (update) {
                            noteAdapter.setItem(note, thisPosition);
                        } else {
                            noteAdapter.addItem(note);
                        }
                    }
                });

    }
    /*private void deleteFromFireStore(Note note) {
        db.collection("notes").document("DN")
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });

    }*/
    
    private void openForm() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("note", note);
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        navController.navigate(R.id.formFragment);

    }
}