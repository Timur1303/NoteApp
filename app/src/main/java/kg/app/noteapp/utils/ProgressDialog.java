package kg.app.noteapp.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.view.View;

import kg.app.noteapp.R;

public class ProgressDialog {

    private Dialog dialog;

     public ProgressDialog(Activity activity){
         AlertDialog.Builder builder = new AlertDialog.Builder(activity);
         View view = activity.getLayoutInflater().inflate(R.layout.progress, null);
         builder.setView(view);
         dialog = builder.create();
     }

     public void show(){
         dialog.show();
     }

     public void dismiss(){
         dialog.dismiss();
     }
}
