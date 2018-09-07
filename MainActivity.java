package com.example.student.piano;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    TextView playing;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket socket;
    private Handler h;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private StringBuilder sb = new StringBuilder();
    private Sound drum;
    private Sound snare;
    private Sound hat;
    private Sound a5bass;
    private Sound csharpbass;
    private Sound fsharpbass;
    private Sound sample;
    private boolean latch = false;
    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drum = new Sound(this, "drum sample");
        snare = new Sound(this,"snare sample");
        hat = new Sound(this, "hat sample");
        a5bass = new Sound(this, "a5 bass");
        csharpbass = new Sound(this, "c# bass");
        fsharpbass = new Sound(this, "f# bass");
        sample = new Sound(this, "sample");
        playing = findViewById(R.id.sound);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice("00:06:66:7D:80:1A");
        try {
            socket = connect(device);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ConnectedThread c = new ConnectedThread(socket);
        c.start();

        h = new Handler() {
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case 1:
                        byte[] readBuf = (byte[]) msg.obj;
                        String strIncom = new String(readBuf, 0, msg.arg1);
                        sb.append(strIncom);
                        int endOfLineIndex = sb.indexOf("/");
                        if (endOfLineIndex > 0) {
                            String data = sb.substring(0, endOfLineIndex);
                            sb.delete(0, sb.length());
                            playSound(data);
                            setText(data);
                        }
                        break;
                }
            };
        };
    }

    private BluetoothSocket connect(BluetoothDevice device) throws IOException {
        try {
            final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", new Class[] { UUID.class });
            return (BluetoothSocket) m.invoke(device, MY_UUID);
        } catch (Exception e) {
        }
        return  device.createRfcommSocketToServiceRecord(MY_UUID);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    private void setText(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                playing.setText(text);
            }
        });
      }

    public void playSound(String text) {
        switch(text){
            case "0": sample.playSound(); break;
            case "4": hat.playSound(); break;
            case "5": csharpbass.playSound(); break;
            case "8": snare.playSound(); break;
            case "9": fsharpbass.playSound(); break;
            case "12": drum.playSound(); break;
            case "13": a5bass.playSound(); break;
            case "off_12": drum.playSound(); break;
            case "off_8": snare.playSound(); break;
            case "off_0": sample.stopSound(latch); break;
            case "off_4": hat.stopSound(latch); break;
            case "off_9": fsharpbass.stopSound(latch); break;
            case "off_5": csharpbass.stopSound(latch); break;
            case "off_13": a5bass.stopSound(latch); break;
            case "15": latch = !latch;
        }
    }

    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[256];
            int bytes;
            while (true) {
                try {
                    bytes = mmInStream.read(buffer);
                    h.obtainMessage(1, bytes, -1, buffer).sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }

        public void write(String message) {
            byte[] msgBuffer = message.getBytes();
            try {
                mmOutStream.write(msgBuffer);
            } catch (IOException e) {
            }
        }
    }
}
