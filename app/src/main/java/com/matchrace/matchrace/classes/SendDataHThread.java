package com.matchrace.matchrace.classes;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.StringTokenizer;

import android.os.HandlerThread;
import android.util.Log;

/**
 * HandlerThread for sending the data to DB.
 * 
 */
public class SendDataHThread extends HandlerThread {

	private HttpURLConnection urlConnection;
	private String lat, lng, speed, bearing, event;
	private String name, fullUserName;
    private URL url;
	public SendDataHThread(String name) {
		super(name);
		this.name = name;
	}

	@Override
	public void run() {
		httpConnSendData();
	}

	/**
	 * Creates the HTTP connection for sending data to DB.
	 */
	private void httpConnSendData() {
        if(name.equals("SendBuoys")){
           // Log.i("test","send buoys");

            String BuNum=fullUserName.substring(7,8);
            String EvNum=fullUserName.substring(9,getFullUserName().length());
            try {
                url = new URL(C.URL_BUOYS_TABLE + "&Latitude=" + lat +"&Longitude=" + lng +"&BuoysNumber="+BuNum+"&Event="+EvNum);
                urlConnection = (HttpURLConnection) url.openConnection();
                Log.i("test",url.getContent().toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                Log.i(name, "IOException");
                urlConnection.disconnect();
            }
        }
        // mUser + "_" + mPassword + "_" + mEvent
        else if(name.equals("CreateNewUser")){
            Log.i("test","create new user");
            String mUser;
            String mPass;
            String mEvent;
            StringTokenizer str=new StringTokenizer(fullUserName);
            mUser=str.nextToken("_");
            String temp=mUser.replaceAll("Sailor","");
            //String temp="blablabla";
            mPass=str.nextToken("_");
            mEvent=str.nextToken();
            Log.i("test","mEvent="+mEvent);
            try {
            //Log.i("test",url.toString());
                url = new URL(C.URL_USERS_TABLE + "&mUser="+temp+"&mPass="+mPass+"&mEvent="+mEvent);

                urlConnection = (HttpURLConnection) url.openConnection();
                Log.i("test",url.getContent().toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                Log.i(name, "IOException");
                urlConnection.disconnect();
            }
        }

        else if(name.equals("SendGPS")){
            Log.i("test","send gps");

            try {
                String temp=fullUserName.replaceAll("Sailor","");

                String time=new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
                url = new URL(C.URL_HISTORY_TABLE + "&Latitude=" + lat +"&Longitude=" + lng +"&Pressure=" + speed + "&Azimuth="+ bearing + "&Bearing=" + bearing + "&Information=" + temp + "&Event=" + event+"&curTime="+time);
                Log.i("test","sending to history"+url.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));
                    String result = br.readLine();
                    if (!result.startsWith("OK")) { // Something is wrong.
                        Log.i(name, "Not OK!");
                    }
                    else { // Data sent.
                        Log.i(name, "OK!");
                    }
                }
                catch (IOException e) {
                    Log.i(name, "IOException");
                    urlConnection.disconnect();
                }
                urlConnection.disconnect();
            }
            catch (MalformedURLException e) {
                Log.i(name, "MalformedURLException");
            }
            catch (IOException e) {
                Log.i(name, "IOException");
            }
        }



	}

	// Getters and Setters.
	public String getFullUserName() {
		return fullUserName;
	}

	public void setFullUserName(String fullUserName) {
		this.fullUserName = fullUserName;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getSpeed() {
		return speed;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
	}

	public String getBearing() {
		return bearing;
	}

	public void setBearing(String bearing) {
		this.bearing = bearing;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

}
