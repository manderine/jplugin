package com.ladybuggames.jplugin;

import android.content.DialogInterface;

import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;

import android.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

public class jActivity {
    private UnityPlayerActivity _activity;
    private String _prefabName = "";
    private String _receiveName = "";

    public static Object createInstance( UnityPlayerActivity activity,  String prefab_name, String receive_name ) {
        return new jActivity( activity, prefab_name, receive_name );
    }

    public jActivity( UnityPlayerActivity activity, String prefabName, String receiveName ) {
        _activity = activity;
        _prefabName = prefabName;
        _receiveName = receiveName;
    }

    public void OnSendMessage( int what, int arg1, int arg2, String message ) {
        switch( what ) {
        case 0 : // EchoMessage
            UnityPlayer.UnitySendMessage( _prefabName, _receiveName, message );
            break;
        case 1 : // Toast
            Toast.makeText( _activity.getApplicationContext(), message, Toast.LENGTH_SHORT ).show();
            break;
        case 2 : // AlertDialog
            OnAlertDialogMessage( arg1, arg2, message );
            break;
        case 3 : // StatusBar
            OnStatusBar( arg1, arg2 );
            break;
        }
    }

    public void SendMessage( int what, int arg1, int arg2, String message ) {
        final int f_what = what;
        final int f_arg1 = arg1;
        final int f_arg2 = arg2;
        final String f_message = message;

        _activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                OnSendMessage( f_what, f_arg1, f_arg2, f_message );
            }
        });
    }

    public void OnAlertDialogMessage( int arg1, int arg2, String str ) {
        final String [] strs = str.split( "/" );

        final String f_msg = strs[0];
        final String f_yes = strs[1];
        final String f_no = strs[2];
        final String f_result = strs[3];

        AlertDialog.Builder builder = new AlertDialog.Builder( _activity );

        builder.setMessage( f_msg );//"프로그램을 종료하시겠습니까?" );
        builder.setCancelable( false );
        builder.setPositiveButton( f_yes/*"예"*/, new DialogInterface.OnClickListener() {
            @Override
            public void onClick( DialogInterface dialog, int which ) {
            UnityPlayer.UnitySendMessage( _prefabName, _receiveName, f_result );
            }
        });
        builder.setNegativeButton( f_no/*"아니오"*/, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
            }
        });

        builder.show();
    }

    public void OnStatusBar( int arg1, int arg2 ) {
        Window w = _activity.getWindow();
        w.setFlags( arg1, -1 );

        View v = w.getDecorView();
        v.setSystemUiVisibility( arg2 );
    }
}
