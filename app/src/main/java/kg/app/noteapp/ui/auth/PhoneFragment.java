package kg.app.noteapp.ui.auth;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import kg.app.noteapp.R;


public class PhoneFragment extends Fragment {

    private EditText editText;
    private EditText editCode;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private View viewPhone;
    private View viewCode;
    private String verificationId;
    private TextView redText;
    private TextView timer;



    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_phone, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editText = view.findViewById(R.id.editPhone);
        editCode = view.findViewById(R.id.editCode);
        viewPhone = view.findViewById(R.id.viewPhone);
        viewCode = view.findViewById(R.id.viewCode);
        redText = view.findViewById(R.id.textRed);
        timer = view.findViewById(R.id.timerView);
        redText.setVisibility(View.GONE);

        setListeners(view);
        setCallbacks();
        initView();
    }

    private void setListeners(View view) {
        view.findViewById(R.id.btnNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestSms();

            }
        });
        view.findViewById(R.id.btnConfirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirm();
            }
        });
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                requireActivity().finish();
            }
        });
    }

    private void confirm(){
        String code = editCode.getText().toString();
        if(code.length()==6 && TextUtils.isDigitsOnly(code)){
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            signIn(credential);

        }
    }

    private void initView(){
        showViewPhone();
    }
    private void showViewCode(){
        viewCode.setVisibility(View.VISIBLE);
        viewPhone.setVisibility(View.GONE);
    }

    private void showViewPhone(){
        viewPhone.setVisibility(View.VISIBLE);
        viewCode.setVisibility(View.GONE);
    }

    private void setCallbacks(){
        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                Log.e("TAG", "onVerificationCompleted");
                signIn(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.e("TAG", "onVerificationFailed" + e.getMessage());

            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationId = s;
                showViewCode();
                new CountDownTimer(60000, 1000) {
                    @Override
                    public void onTick(long l) {
                        timer.setVisibility(View.VISIBLE);
                        String text = "Отправить еще раз через " + (l / 1000);
                        timer.setText(text);
                    }

                    @Override
                    public void onFinish() {
                        timer.setVisibility(View.GONE);
                        redText.setVisibility(View.VISIBLE);

                    }
                }.start();
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                super.onCodeAutoRetrievalTimeOut(s);
            }
        };
    }

    private void signIn(PhoneAuthCredential phoneAuthCredential) {
        FirebaseAuth.getInstance().
                signInWithCredential(phoneAuthCredential).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    close();
                }else {
                    Toast.makeText(requireContext(), "Error" + task.
                            getException().
                            getMessage(), Toast.LENGTH_SHORT).
                            show();
                }
            }
        });
    }

    private void requestSms(){
        String phone = editText.getText().toString();
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                        .setPhoneNumber(phone)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(requireActivity())                 // Activity (for callback binding)
                        .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    private void close(){
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        navController.navigateUp();
    }
}