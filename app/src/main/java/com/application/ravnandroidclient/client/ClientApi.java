package com.application.ravnandroidclient.client;

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
import java.util.Date;

/**
 * Client API holds the socket and handels all
 * Requests and Response from Client.
 */
public class ClientApi {
    final static String TAG = "ClientApi";
    final static String DEFAULT_ADDRESS = "192.168.0.4";
    final static int API_PORT = 8381;

    Socket mApiSocket;
    DataOutputStream dApiOut;
    DataInputStream dApiIn;
    public String currentAddress = DEFAULT_ADDRESS;

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



    public String connect(String address) {
        currentAddress = address;
        //Check if it is already connected.
        if(mApiSocket != null && mApiSocket.isConnected()) {
            Log.d(TAG, "Socket is already connected");
            return "Already Connected"; //If so, do nothing
        }
        else {
            Log.d(TAG, "starting connect async task");
            try {
                Log.d(TAG, "Attemping to connect to socket");
                mApiSocket = new Socket(currentAddress, API_PORT);
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
    public String add(GiphyModel giphyModel) {
        try {
            dApiOut.writeUTF("add/" + gson.toJson(giphyModel));
            String response = dApiIn.readUTF();
            if(response.toLowerCase().equals("ok")) {
//                Log.d(TAG, "Adding was successfull");
                return null;
            }
            Log.d(TAG, "Adding was not successfull");
            return response;
        }
        catch (IOException e) {
            return e.getMessage();
        }
    }

    /**
     * Is done synchronously, returns true if successful
     * @param sortField
     * @param sortType
     * @return
     */
    public String sort(GiphyList.SortField sortField, GiphyList.SortType sortType) {
        try {
            mGiphyList.setSortField(sortField);
            mGiphyList.setSortType(sortType);
//            Log.d(TAG, "Sort field: " + mGiphyList.sortField);
//            Log.d(TAG, "Sort type: " + mGiphyList.sortType);
            dApiOut.writeUTF("sort/" + mGiphyList.sortField + "&" + mGiphyList.sortType);
            String result = dApiIn.readUTF();
            if(result.contains("ok")) {
                return null;
            }
            else {
                return result;
            }

        }catch (IOException e) {
            return e.getMessage();
        }
    }

    /**
     *
     * @param giphyModel
     * @return
     */
    public String update(GiphyModel giphyModel) {
        try {

            dApiOut.writeUTF("update/" + gson.toJson(giphyModel));
            String response = dApiIn.readUTF();
            if(response.toLowerCase().equals("ok")) {
                Log.d(TAG, "Updating was successfull");
                return null;
            }
            else {
                return response;
            }
        }
        catch (IOException e) {
            return e.getMessage();
        }
    }

    public String remove(long giphyId) {
        try {
            dApiOut.writeUTF("remove/" + giphyId);
            String response = dApiIn.readUTF();
            if(response.toLowerCase().equals("ok")) {
                Log.d(TAG, "Deleting was successfull");
                return null;
            }
            return response;
        }
        catch (IOException e) {
            return e.getMessage();
        }
    }

    public void incrementViewCount(long giphyId) {
        try {
            dApiOut.writeUTF("viewcount/" + giphyId);
        }
        catch (IOException e) {
            Log.d(TAG, "View count incrementing");
        }
    }


    public GiphyModel getModel(long id) {
        for(GiphyModel model : mGiphyList.models) {
            if(model.id == id) return model;
        }
        return null;
    }



}
