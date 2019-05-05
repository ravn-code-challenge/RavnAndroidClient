package com.application.ravnandroidclient.ConnectionActivity;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class ConnectionTask extends AsyncTask<Void, Void, String> {

    private static final String TAG = "ConnectionTask";
    private static int UDP_PORT = 8382;
    DatagramSocket c;
    String ipAddress;

    @Override
    protected String doInBackground(Void... voids) {
        try {
            c = new DatagramSocket();
            c.setBroadcast(true);
            byte[] sendData = "DISCOVER_FUIFSERVER_REQUEST".getBytes();

            //Try the 255.255.255.255 first
            try {
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
                        InetAddress.getByName("255.255.255.255"), UDP_PORT);
                c.send(sendPacket);
                Log.d(TAG, ">>> Request packet sent to: 255.255.255.255 (DEFAULT)");
            }catch (Exception e) {}

            // Broadcast the message over all the network interfaces
            Enumeration interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = (NetworkInterface)interfaces.nextElement();
                Enumeration address = networkInterface.getInetAddresses();
                while (address.hasMoreElements()) {
                    InetAddress inetAddress = (InetAddress) address.nextElement();
                    Log.d(TAG, "Your ip: " + inetAddress.getHostAddress());
//                    if (inetAddress.isSiteLocalAddress()) {
//                        Log.d(TAG, "Your ip: " + inetAddress.getHostAddress());
////                        System.out.println("Your ip: " + inetAddress.getHostAddress());  /// gives ip address of your device
//                    }
                }



                if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                    continue; // Don't want to broadcast to the loopback interface
                }

                for(InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
                    InetAddress broadcast = interfaceAddress.getBroadcast();
                    if(broadcast == null) {
                        continue;
                    }

                    // Send the broadcast package!
                    try {
                        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
                                broadcast, UDP_PORT);
                        c.send(sendPacket);
                    }catch (Exception e) {}
                    Log.d(TAG, ">>> Request packet sent to: " + broadcast.getHostAddress()
                            + "; Interface: " + networkInterface.getDisplayName());

                }
            }
            Log.d(TAG,  ">>> Done looping over all network interfaces. " +
                    "Now waiting for a reply!");

            //Wait for a response
            byte[] recvBuf = new byte[15000];
            DatagramPacket receivePacket = new DatagramPacket(recvBuf, recvBuf.length);
            c.receive(receivePacket);

            //We have a response
            Log.d(TAG, ">>> Broadcast response from server: "
                    + receivePacket.getAddress().getHostAddress());

            //Check if the message is correct
            String message = new String(receivePacket.getData()).trim();
            if (message.equals("DISCOVER_FUIFSERVER_RESPONSE")) {
                //WE GOT THE IP ADRESS
                ipAddress = receivePacket.getAddress().getHostAddress();
                Log.d(TAG, "Got address: " + ipAddress);
            }
            c.close();
        }catch (IOException e) {

        }
        return ipAddress;
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

    }
}
