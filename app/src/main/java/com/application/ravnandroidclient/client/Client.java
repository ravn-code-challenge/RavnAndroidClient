package com.application.ravnandroidclient.client;

import android.os.AsyncTask;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client {
    final static String TAG = "Client";
    final static String ADDRESS = "10.39.66.57";
    final static int PORT = 8381;
    Socket mSocket;
    DataOutputStream dOut;
    DataInputStream dIn;
    public List<GiphyModel> mGiphyModels = new ArrayList<>();

    public void connect() {
        //Check if it is already connected.
        if(mSocket != null && mSocket.isConnected()) {
            Log.d(TAG, "Socket is already connected");
            return; //If so, do nothing
        }
        else {
            Log.d(TAG, "starting connect async task");
            new ConnectAsyncTask().execute();
        }
    }

    public void disconnect() {
        if(mSocket == null || mSocket.isClosed()) {
            return;
        }
        else {
            new DisconnectAsyncTask().execute();
        }
    }

    public void list() {
        if(mSocket == null || mSocket.isClosed()) {
            return;
        }
        else {
            new ListAsyncTask().execute();
        }
    }


    class ListAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                dOut.writeUTF("list");
                Log.d(TAG, "Got Results: " + dIn.readUTF());
            }
            catch(IOException e) {
                Log.d(TAG, "Error connecting socket" + e);
            }
            return null;
        }
    }


    class ConnectAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Log.d(TAG, "Attemping to connect to socket");
                mSocket = new Socket(ADDRESS, PORT);
                dOut = new DataOutputStream(mSocket.getOutputStream());
                dIn = new DataInputStream(mSocket.getInputStream());
            }
            catch(IOException e) {
                Log.d(TAG, "Error connecting socket" + e);
            }
            return null;
        }
    }

    class DisconnectAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                dOut.writeUTF("exit");
                mSocket.close();
                mSocket = null;
            }
            catch(IOException e) {
                Log.d(TAG, "Error connecting socket" + e);
            }
            return null;
        }
    }

}
