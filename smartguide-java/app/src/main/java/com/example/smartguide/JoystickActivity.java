package com.example.smartguide;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
//import com.google.common.primitives.Floats;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.*;


public class JoystickActivity extends AppCompatActivity implements JoystickView.JoystickListener{
    MainActivity.Partage partage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joystick);
        JoystickView joystick = new JoystickView(this);
        setContentView(joystick);

        /*monSerializable = (MainActivity.MonSerializable) getIntent().getSerializableExtra("MonStream");
        String filename = "file.ser";
        try
        {
            // Reading the object from a file
            FileInputStream file = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(file);

            // Method for deserialization of object
            monSerializable = (MainActivity.MonSerializable)in.readObject();

            in.close();
            file.close();

            System.out.println("Object has been deserialized ");
        }

        catch(IOException ex)
        {
            ex.printStackTrace();
        }

        catch(ClassNotFoundException ex)
        {
            ex.printStackTrace();
        }*/
    }

    public void neverReading(InputStream inputStream){
        byte [] buffer = new byte[1024];
        String message, line = "";
        int i=0;
        try{
            i = inputStream.read(buffer, 0, 1024); // The number of bytes actually read is returned as an integer.
        }
        catch(Exception ee){
            ee.printStackTrace();
        }

        //Boucle permettant de lire tous les messages envoyés par le raspberry
        while (true) {

            //Lecture de l'information ligne par ligne
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

            //En attente pour lecture de l'information dès écriture
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

    /*public void startReading (View view) {
        new AsyncTask<Integer, Void, Void>() {
            @Override
            protected Void doInBackground(Integer... params) {
                try {
                    outputStream = MainActivity.MonSerializable.monChannel.getOutputStream();
                    InputStream inputStream = MainActivity.MonSerializable.monChannel.getInputStream();

                    Appel de la fontion de lecture
                    neverReading(MainActivity.MonSerializable.inputStream);

                     Disconnection
                    MainActivity.MonSerializable.channel.disconnect();
                    MainActivity.MonSerializable.session.disconnect();
                } catch (
                        Exception ee) {
                    ee.printStackTrace();
                }
                return null;
            }
        }.execute(1);
    }*/

    public void onJoystickMoved(float xPercent, float yPercent, int id) {
        Log.d("Main Method", "X percent:" + xPercent + "Y percent:" + yPercent);
        String Joystick_x =  new StringBuilder().append(xPercent).append(" ").toString();
        String Joystick_y =  new StringBuilder().append(yPercent).append("\n").toString();
        try {
            MainActivity.Partage.printStream.print(Joystick_x);
            MainActivity.Partage.printStream.print(" ");
            MainActivity.Partage.printStream.println(Joystick_y);
            System.out.write(Joystick_x.getBytes(Charset.forName("UTF-8")));
            System.out.write(Joystick_y.getBytes(Charset.forName("UTF-8")));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}