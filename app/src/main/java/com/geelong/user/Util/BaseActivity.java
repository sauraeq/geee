package com.geelong.user.Util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.whirl.Utils.DialogActionInterface;


public class BaseActivity extends AppCompatActivity {

    private ProgressCustomDialog progressCustomDialog;


    public boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context. getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    public void showCustomAlertDialog(Activity activity, String Message, String button , DialogActionInterface actionInterface)
    {
        CustomAlertDialog customAlertDialog=new CustomAlertDialog(activity,Message,button,actionInterface);
        customAlertDialog.showCustomAlertDialog();


    }


    public void showToastMessage(Context context, String message)
    {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public void  showProgressDialog()
    {
        if (progressCustomDialog!=null)
            progressCustomDialog.showCustomDialog();
        else
        {
            progressCustomDialog=new ProgressCustomDialog(this,"Please Wait!");
            progressCustomDialog.showCustomDialog();
        }
    }

    public void  hideProgressDialog()
    {
        if (progressCustomDialog!=null)
            progressCustomDialog.hideCustomDialog();
    }

}


