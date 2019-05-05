package com.application.ravnandroidclient.client;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Client Push holds the socket and recieves
 * push updates from server
 */
public class ClientPush implements Runnable{
    final static String TAG = "ClientPush";
    final static String ADDRESS = ClientApi.ADDRESS;
    final static int PUSH_PORT = 8380;

    Socket mPushSocket;
    DataInputStream dPushIn;

    public List<ClientSubscriber> mClientSubscribers = new ArrayList<>();

    Gson gson;

    static ClientPush sClient;

    Handler handler = new Handler();


    public static ClientPush getClient() {
        if(sClient == null) sClient = new ClientPush();
        return sClient;
    }

    private ClientPush() {
        gson = ClientApi.getClient().gson;
    }

    String getFirstArgument(String arg) {
        int index = arg.indexOf("{");
        if(index != -1) {
            return arg.substring(0, index);
        }
        else {
            return arg;
        }
    }

    String getSecondArgument(String arg) {
        int index = arg.indexOf("/");
        if(index != -1) {
            return arg.substring(index+1);
        }
        else {
            return null;
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                Log.d(TAG, "Awaiting push request");
                String argument = dPushIn.readUTF();
                Log.d(TAG, "Got push request");
                String firstArgument = getFirstArgument(argument);
                String secondArgument = getSecondArgument(argument);
                if(firstArgument.toLowerCase().contains("list")) {
                    GiphyList giphyList = gson.fromJson(secondArgument, GiphyList.class);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            notifySubscribers(giphyList);
                        }
                    });

                }
                else if(firstArgument.toLowerCase().contains("exit")) {
                    System.out.println("Exit called");
                    break;
                }

            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        try
        {
            // closing resources
            this.dPushIn.close();
            System.out.println("Thread has ended");

        }catch(IOException e){
            e.printStackTrace();
        }
    }



    public void registerSubscriber(ClientSubscriber subscriber) {
        mClientSubscribers.add(subscriber);
    }

    public void removeSubscriber(ClientSubscriber s) {
        mClientSubscribers.remove(s);
    }

    private void notifySubscribers(GiphyList giphyModels) {
        ClientApi.getClient().mGiphyList = giphyModels;
        for(ClientSubscriber subscriber : mClientSubscribers) {
            subscriber.updateGiphyModels(ClientApi.getClient().mGiphyList);
        }
    }

    /**
     * Called within Client A
     */
    public String connect() {
        //Check if it is already connected.
        if(mPushSocket != null && mPushSocket.isConnected()) {
            Log.d(TAG, "Push Socket is already connected");
            return "Already Connected"; //If so, do nothing
        }
        else {
            try {
                mPushSocket = new Socket(ADDRESS, PUSH_PORT);
                dPushIn = new DataInputStream(mPushSocket.getInputStream());
                new Thread(sClient).start(); //Start self
                return null;
            }catch (IOException e) {
                Log.d(TAG, "Exception connecting:  " + e);
            }
        }
        return "Connection failed";
    }

    public String disconnect() {
        if(mPushSocket == null || mPushSocket.isClosed()) {
            return "Failed to Disconnect";
        }
        else {
            try {
                mPushSocket.close();
                mPushSocket = null;
                return null;
            }catch (IOException e) {
                Log.d(TAG, "Exception Disconnecting:  " + e);

            }

        }
        return "Failed to Disconnect";
    }


}
