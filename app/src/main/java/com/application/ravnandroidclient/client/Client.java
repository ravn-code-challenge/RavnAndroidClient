package com.application.ravnandroidclient.client;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Client {
    final static String TAG = "Client";
    final static String ADDRESS = "192.168.0.4";
    final static int PORT = 8381;
    Socket mSocket;
    DataOutputStream dOut;
    DataInputStream dIn;


    public List<GiphyModel> mGiphyModels = new ArrayList<>();
    public List<ClientSubscriber> mClientSubscribers = new ArrayList<>();

    Gson gson;

    static Client sClient;

    public static Client getClient() {
        if(sClient == null) sClient = new Client();
        return sClient;
    }

    private Client() {
        gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (
                        json, typeOfT, context) -> new Date(json.getAsJsonPrimitive().getAsLong()))
                .registerTypeAdapter(Date.class, (JsonSerializer<Date>) (
                        date, type, jsonSerializationContext) -> new JsonPrimitive(date.getTime()))
                .create();
    }

    public void registerSubscriber(ClientSubscriber subscriber) {
        mClientSubscribers.add(subscriber);
    }

    public void removeSubscriber(ClientSubscriber s) {
        mClientSubscribers.remove(s);
    }

    private void notifySubscribers(List<GiphyModel> giphyModels) {
        mGiphyModels = giphyModels;
        for(ClientSubscriber subscriber : mClientSubscribers) {
            subscriber.updateGiphyModels(mGiphyModels);
        }
    }

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

    /**
     * Is done synchronously, returns true if succesfull
     * @param giphyModel
     */
    public boolean add(GiphyModel giphyModel) {
        try {
            dOut.writeUTF("add/" + gson.toJson(giphyModel));
            String response = dIn.readUTF();
            if(response.toLowerCase().equals("ok")) {
                Log.d(TAG, "Adding was successfull");
                list();
                return true;
            }
            Log.d(TAG, "Adding was not successfull");
            return false;
        }
        catch (IOException e) {
            return false;
        }
    }

    /**
     *
     * @param giphyModel
     * @return
     */
    public boolean update(GiphyModel giphyModel) {
        try {

            dOut.writeUTF("update/" + gson.toJson(giphyModel));
            String response = dIn.readUTF();
            if(response.toLowerCase().equals("ok")) {
                Log.d(TAG, "Updating was successfull");
                list();
                return true;
            }
            Log.d(TAG, "Updating was not successfull");
            return false;
        }
        catch (IOException e) {
            return false;
        }
    }

    public boolean remove(long giphyId) {
        try {
            dOut.writeUTF("remove/" + giphyId);
            String response = dIn.readUTF();
            if(response.toLowerCase().equals("ok")) {
                Log.d(TAG, "Deleting was successfull");
                list(); //Get the new list
                return true;
            }
            Log.d(TAG, "Deleting was not successfull");
            return false;
        }
        catch (IOException e) {
            return false;
        }
    }

    public GiphyModel getModel(long id) {
        for(GiphyModel model : mGiphyModels) {
            if(model.id == id) return model;
        }
        return null;
    }


    class ListAsyncTask extends AsyncTask<Void, Void, List<GiphyModel>> {

        @Override
        protected List<GiphyModel> doInBackground(Void... voids) {
            try {
                dOut.writeUTF("list");
                TypeToken<List<GiphyModel>> token = new TypeToken<List<GiphyModel>>() {};
                List<GiphyModel> animals = gson.fromJson(dIn.readUTF(), token.getType());
                return animals;
            }
            catch(IOException e) {
                Log.d(TAG, "Error connecting socket" + e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<GiphyModel> models) {
            if(models != null) {
                notifySubscribers(models);
            }
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
