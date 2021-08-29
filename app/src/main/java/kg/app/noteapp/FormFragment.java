package kg.app.noteapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import kg.app.noteapp.models.Note;
import kg.app.noteapp.utils.ProgressDialog;


public class FormFragment extends Fragment {

    private Note note;
    private EditText editText;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_form, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editText = view.findViewById(R.id.editText);
        if (getArguments() != null) {
            note = (Note) requireArguments().getSerializable("note");
        }
        if (note != null) {
            editText.setText(note.getTitle());
        }

        view.findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });

        progressDialog = new ProgressDialog(requireActivity());

        showKeyboard(editText);


    }



    private void save() {
        hideKeyboard();
        progressDialog.show();
        String text = editText.getText().toString().trim();
        if (note == null) {
            note = new Note(text);
            saveToFirestore(note);
            App.getAppDatabase().noteDao().insert(note);

        } else {
            note.setTitle(text);
            App.getAppDatabase().noteDao().update(note);
        }
        Note note = new Note(text);
        App.getAppDatabase().noteDao().insert(note);
        Bundle bundle = new Bundle();
        bundle.putSerializable("note", note);
        getParentFragmentManager().setFragmentResult("rk_form", bundle);
    }

    private void saveToFirestore(Note note) {
        FirebaseFirestore.getInstance().
                collection("notes").
                add(note).
                addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()){
                            close();
                            Toast.makeText(requireContext(), "Успешно", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void close() {
        NavController navController = Navigation.findNavController(requireActivity(),
                R.id.nav_host_fragment);
        navController.navigateUp();
    }
    private void showKeyboard(EditText editText) {
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    private void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = requireActivity().getCurrentFocus();
        if (view == null) view = new View(requireActivity());
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


}