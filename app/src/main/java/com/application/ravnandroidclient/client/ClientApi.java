package com.application.ravnandroidclient.client;

import android.os.AsyncTask;
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
 * Client API holds the socket and handels all
 * Requests and Response from Client.
 */
public class ClientApi {
    final static String TAG = "ClientApi";
    final static String ADDRESS = "192.168.0.4";
    final static int API_PORT = 8381;

    Socket mApiSocket;
    DataOutputStream dApiOut;
    DataInputStream dApiIn;


    public GiphyList mGiphyList;

    Gson gson;

    static ClientApi sClient;

    public static ClientApi getClient() {
        if(sClient == null) sClient = new ClientApi();
        return sClient;
    }

    private ClientApi() {
        gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (
                        json, typeOfT, context) -> new Date(json.getAsJsonPrimitive().getAsLong()))
                .registerTypeAdapter(Date.class, (JsonSerializer<Date>) (
                        date, type, jsonSerializationContext) -> new JsonPrimitive(date.getTime()))
                .create();
    }



    public String connect() {
        //Check if it is already connected.
        if(mApiSocket != null && mApiSocket.isConnected()) {
            Log.d(TAG, "Socket is already connected");
            return "Already Connected"; //If so, do nothing
        }
        else {
            Log.d(TAG, "starting connect async task");
            try {
                Log.d(TAG, "Attemping to connect to socket");
                mApiSocket = new Socket(ADDRESS, API_PORT);
                dApiOut = new DataOutputStream(mApiSocket.getOutputStream());
                dApiIn = new DataInputStream(mApiSocket.getInputStream());
                return null;
            }
            catch(IOException e) {
                Log.d(TAG, "Error connecting socket" + e);
            }
        }
        return "Connection failed";
    }

    public String disconnect() {
        if(mApiSocket == null || mApiSocket.isClosed()) {
            return "Already Disconnected";
        }
        else {
            try {
                dApiOut.writeUTF("exit");
                mApiSocket.close();
                mApiSocket = null;
                return null;
            }
            catch(IOException e) {
                Log.d(TAG, "Error connecting socket" + e);
            }
        }
        return "Failed to Disconnect";
    }

    public String list() {
        if(mApiSocket == null || mApiSocket.isClosed()) {
            return "Not Connected";
        }
        else {
            try {
                dApiOut.writeUTF("list");
                GiphyList giphyList = gson.fromJson(dApiIn.readUTF(), GiphyList.class);
                mGiphyList = giphyList;
                return null;
            }
            catch(IOException e) {
                Log.d(TAG, "Error connecting socket" + e);
            }
            return "Error retrieving List";
        }
    }

    /**
     * Is done synchronously, returns true if succesful
     * @param giphyModel
     */
    public boolean add(GiphyModel giphyModel) {
        try {
            dApiOut.writeUTF("add/" + gson.toJson(giphyModel));
            String response = dApiIn.readUTF();
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
     * Is done synchronously, returns true if successful
     * @param sortField
     * @param sortType
     * @return
     */
    public boolean sort(GiphyList.SortField sortField, GiphyList.SortType sortType) {
        mGiphyList.setSortField(sortField);
        mGiphyList.setSortType(sortType);
        Log.d(TAG, "Sort field: " + mGiphyList.sortField);
        Log.d(TAG, "Sort type: " + mGiphyList.sortType);
        return true;
    }



        /**
         *
         * @param giphyModel
         * @return
         */
    public boolean update(GiphyModel giphyModel) {
        try {

            dApiOut.writeUTF("update/" + gson.toJson(giphyModel));
            String response = dApiIn.readUTF();
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
            dApiOut.writeUTF("remove/" + giphyId);
            String response = dApiIn.readUTF();
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
        for(GiphyModel model : mGiphyList.models) {
            if(model.id == id) return model;
        }
        return null;
    }



}
