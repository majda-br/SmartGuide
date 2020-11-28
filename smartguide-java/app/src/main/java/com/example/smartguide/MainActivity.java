package com.example.smartguide;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.widget.*;

import android.view.View;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


public class MainActivity extends AppCompatActivity {
    OutputStream outputStream;
    InputStream inputStream;
    PrintStream printStream;

    static class Partage { //partage
        static PrintStream printStream = null;
        static InputStream inputStream = null;
        static Channel channel = null;
        static Session session = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void ChangeAct(View view) {
        Intent TransitionToJoystickView = new Intent();
        TransitionToJoystickView.setClass(this, JoystickActivity.class);
        //Partage partage = new Partage();
        Partage.printStream = printStream;
        Partage.inputStream = inputStream;
        //TransitionToJoystickView.putExtra("MonStream", partage);
        startActivity(TransitionToJoystickView);

    }
    public void startConnection (View view) {
        // We open another thread to deal with the network communication since it is forbidden to do it in the Main
        new AsyncTask<Integer, Void, Void>(){
            @Override
            protected Void doInBackground(Integer... params) {
                try {
                    executeSSHcommand();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute(1);
    }
    public void neverReading(InputStream inputStream){
        byte [] buffer = new byte[1024];
        String message, line = "";
        int i=0;
        try{
            i = inputStream.read(buffer, 0, 1024); // The number of bytes actually read is returned as an integer
        }
        catch(Exception ee){
            ee.printStackTrace();
        }

        //Loop allowing to read all the messages sent by the raspberry
        while (true) {

            //Reading information line by line
            for (int j = 0; j < i; j++){
                if(buffer[j]!=13){ // 13 = code ascii pour le retour à la ligne
                    line = line + new String(new byte[] { buffer[j] }, StandardCharsets.UTF_8);
                }else{
                    j++;
                    System.out.print(line);
                    line = "";
                    System.out.print("\n");
                }
            }

            //Waiting to read information upon writing
            try{
                i = inputStream.read(buffer, 0, 1024);
            }catch(Exception ee){}
            if (i < 0) {
                break;
            }

            //message = new String(buffer, 0, i, StandardCharsets.UTF_8);
            //System.out.print(message);  //It is printing the response to console

        }
    }

    public void executeSSHcommand(){
        String user = "telephone";
        String password = "ior29sazQ21sf";
        String host = "92.188.127.17";
        int port=41022;
        try {

            JSch jsch = new JSch();
            Session session = jsch.getSession(user, host, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setTimeout(10000);
            session.connect();
            Channel channel = session.openChannel("shell");
            channel.connect();
            Partage.channel = channel;
            Partage.session = session;

            try {
                outputStream = channel.getOutputStream();
                inputStream = channel.getInputStream();
                printStream = new PrintStream(outputStream, true);
                //printStream.println("bonjour");

                //Calling up the read function
                neverReading(inputStream);

                // Disconnection
                //channel.disconnect();
                //session.disconnect();
            } catch (Exception ee) {
            }
            //System.console(ee);
        }
        catch(JSchException e){
            e.printStackTrace();
        }
    }
}
