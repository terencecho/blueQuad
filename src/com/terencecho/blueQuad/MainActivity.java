package com.terencecho.blueQuad;
 
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
  
public class MainActivity extends Activity {
  private static final String TAG = "bluetoothl";
    
  Button button1, button2;
  SeekBar brightness;
  BluetoothAdapter mBluetoothAdapter;
  BluetoothSocket mmSocket;
  BluetoothDevice mmDevice;
  OutputStream mmOutputStream;
  InputStream mmInputStream;
  Thread workerThread;
  byte[] readBuffer;
  int readBufferPosition;
  int counter;
  volatile boolean stopWorker;
  
    
  // SPP UUID service 
  private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
  
  // MAC-address of Bluetooth module (you must edit this line)
  private static String address = "20:13:11:14:09:47";
    
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  
    setContentView(R.layout.activity_main);
  
    button1 = (Button) findViewById(R.id.button1);
    button2 = (Button) findViewById(R.id.button2);
    brightness = (SeekBar) findViewById(R.id.brightness);
    
    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    mmDevice = mBluetoothAdapter.getRemoteDevice(address);
    //Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
    try {
		mmSocket = mmDevice.createRfcommSocketToServiceRecord(MY_UUID);
		mmSocket.connect();
		mmOutputStream = mmSocket.getOutputStream();
	    //mmInputStream = mmSocket.getInputStream();
	} catch (IOException e) {
		
	}

    
    //on Button
    button1.setOnClickListener(new View.OnClickListener()
    {
        public void onClick(View v)
        {
            try 
            {
                sendB((byte) 255);
            }
            catch (IOException ex) { }
        }
    });
    //off Button
    button2.setOnClickListener(new View.OnClickListener()
    {
        public void onClick(View v)
        {
            try 
            {
                sendB((byte) 254);
            }
            catch (IOException ex) { }
        }
    });
    //brightness
    brightness.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
    	
    	
        public void OnSeekBarChangeListener(View v) {}
    	
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {}
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            try 
            {
            	byte val = (byte) brightness.getProgress();
            	//if (val < 254) {
            		sendB(val);
            	//}
            }
            catch (IOException ex) { }
        }
    });
    
  }
  
  
  void sendData(String msg) throws IOException
  {
	  byte[] msgBuffer = msg.getBytes();
      mmOutputStream.write(msgBuffer);
  }
  
  void sendB(byte msg) throws IOException
  {
      mmOutputStream.write(msg);
  }
}