package com.application.ravnandroidclient;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.Socket;

public class Client {
    final static String TAG = "Client";
    final static String ADDRESS = "0.0.0.0";
    final static int PORT = 8381;
    Socket mSocket;

    public void connect() {
        //Check if it is already connected.
        if(mSocket != null && mSocket.isConnected()) {
            return; //If so, do nothing
        }
        else {
            new ConnectAsyncTask().execute();

        }
    }

    public void disconnect() {
        if(mSocket != null && mSocket.isClosed()) {
            return;
        }
        else {
            try {
                mSocket.close();
            }
            catch (IOException e) {
                Log.d(TAG, "Error disconnecting socket" + e);
            }
        }
    }

    class ConnectAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                mSocket = new Socket(ADDRESS, PORT);
            }
            catch(IOException e) {
                Log.d(TAG, "Error connecting socket" + e);
            }
            return null;
        }
    }

}
