package com.project.distributed.rmutldistributedprojectworker;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;
import java.util.UUID;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends AppCompatActivity {


    String deviceName = null;
    String socket_id = UUID.randomUUID().toString();
    String ip = null;

    ListView listView; /* Work Processing Listview */
    ListView listView1; /* Work Success Listview */
    ListView listView2; /* Work Failed Listview */

    ArrayList<String> ListSuccess = new ArrayList<String>();
    String[] ListFailed = new String[]{};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* Work Processing Listview */
        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.listView);
        // Defined Array values to show in ListView
        String[] values = new String[] {};

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);

        // Assign adapter to ListView
        listView.setAdapter(adapter);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition     = position;

                // ListView Clicked item value
                String  itemValue    = (String) listView.getItemAtPosition(position);

                // Show Alert
                Toast.makeText(getApplicationContext(),
                        itemValue, Toast.LENGTH_LONG)
                        .show();

            }

        });
        listView.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        /* End Work Success Listview */

         /* Work Success Listview */
        // Get ListView object from xml
        listView1 = (ListView) findViewById(R.id.listView1);
        // Defined Array values to show in ListView
        String[] values1 = new String[] {};

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values1);

        // Assign adapter to ListView
        listView1.setAdapter(adapter1);

        // ListView Item Click Listener
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition     = position;

                // ListView Clicked item value
                String  itemValue    = (String) listView1.getItemAtPosition(position);

                // Show Alert
                Toast.makeText(getApplicationContext(),
                        itemValue, Toast.LENGTH_LONG)
                        .show();

            }

        });
        listView1.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        /* End Work Success Listview */


         /* Work Failed Listview */
        // Get ListView object from xml
        listView2 = (ListView) findViewById(R.id.listView2);
        // Defined Array values to show in ListView
        String[] values2 = new String[] {};

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values2);

        // Assign adapter to ListView
        listView2.setAdapter(adapter2);

        // ListView Item Click Listener
        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition     = position;

                // ListView Clicked item value
                String  itemValue    = (String) listView2.getItemAtPosition(position);

                // Show Alert
                Toast.makeText(getApplicationContext(),
                        itemValue, Toast.LENGTH_LONG)
                        .show();

            }

        });
        listView2.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        /* End Work Success Listview */



        //--------------------------------------------------- mSocket setting -----------------------------
        mSocket.connect(); // Set Connect
        Log.e("My App :", "Request to connect..");

        /* Set Name Connect */
        Log.e("My App :", "Connected.. ");
        JSONObject obj = new JSONObject();
        try {
            obj.put("name", "Worker");
            obj.put("uniqueId", socket_id);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mSocket.emit("SND_CNT", obj);
        /* End Set Name Connect */
        mSocket.on("REC_CNT", REC_CNT); //Set Receive Connection Status from server
        mSocket.on("REC_TASK", REC_TASK); //Set Receive Task from Server


        //--------------------------------------------------- End mSocket setting -----------------------------
    }

    //--------------------------------------------------- Message Setting  Distibute Processing -----------------------------
    private Emitter.Listener REC_TASK = new Emitter.Listener() {

        @Override
        public void call(final Object... args) { //Receive Task from Server
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    JSONObject data = (JSONObject) args[0];

                    String sort_id;
                    String worker_id;
                    String name;
                    String img_name;
                    String task;
                    String StringImgByte;

                    try {
                        sort_id = data.getString("sort_id");
                        worker_id = data.getString("worker_id");

                        //Toast.makeText(MainActivity.this.getApplicationContext(), worker_id+":"+socket_id,
                        //        Toast.LENGTH_LONG).show();

                        if(worker_id.compareTo(socket_id)==0) {
                            img_name = data.getString("img_name");
                            name = data.getString("name");
                            task = data.getString("task");

                            Log.e("My App :", "Received task  from server, sort_id: " + sort_id + ", worker_id: " + worker_id + ", name: " + name + ", img_name: " + img_name + ", task: " + task);

                            //Update Listview Work Processing
                            String task_tmp = null;
                            if(task.compareTo("gs")==0) {
                                task_tmp = "Gray Scale";
                            }else if(task.compareTo("gb")==0) {
                                task_tmp = "Gaussianblur";
                            }else if(task.compareTo("bf")==0) {
                                task_tmp = "Black Filter";
                            }
                            String[] values = new String[1];
                            values[0] = "Image Name: "+img_name+", Task: "+task_tmp;
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                                    android.R.layout.simple_list_item_1, android.R.id.text1, values);
                            listView.setAdapter(adapter);

                            /* Decode Bitmap data */
                            StringImgByte = data.getString("img_data");
                            byte[] bitMapData = Base64.decode(StringImgByte, Base64.DEFAULT);
                            Bitmap bmp1 = BitmapFactory.decodeByteArray(bitMapData, 0, bitMapData.length);
                            //Process Bitmap to Greyscale
                            //ImageView image = (ImageView)getActivity().findViewById(R.id.imageView3);

                            //Check Process to do Work
                            Bitmap rs = null;
                            if(task.compareTo("gs")==0) {
                                rs = doGreyscale(bmp1);
                            }else if(task.compareTo("gb")==0) {
                                rs = applyGaussianBlur(bmp1);
                            }else if(task.compareTo("bf")==0) {
                                rs = applyBlackFilter(bmp1);
                            }
                            //image.setImageBitmap(myGrayScale);

                            //Return result
                            JSONObject obj = new JSONObject();
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            rs.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                            byte[] byteArray = stream.toByteArray();
                            String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

                            //Sent Result Task to Server
                            obj.put("sort_id", sort_id);
                            obj.put("worker_id", worker_id);
                            obj.put("img_name", img_name);
                            obj.put("name", name);
                            obj.put("worker_name", deviceName);
                            obj.put("task", task);
                            obj.put("img_data", encodedImage);
                            mSocket.emit("SND_TASK_RS", obj);
                            Log.e("My App :", "Send result task to server, sort_id: " + sort_id + ", worker_id:" + worker_id + ", name:" + name + ", img_name:" + img_name + ", task:" + task);

                            //Update Listview Work Processing
                            adapter = new ArrayAdapter<String>(MainActivity.this,
                                    android.R.layout.simple_list_item_1, android.R.id.text1, new String[]{});
                            listView.setAdapter(adapter);

                            //Update Listview Work Success
                            task_tmp = null;
                            if(task.compareTo("gs")==0) {
                                task_tmp = "Gray Scale";
                            }else if(task.compareTo("gb")==0) {
                                task_tmp = "Gaussianblur";
                            }else if(task.compareTo("bf")==0) {
                                task_tmp = "Black Filter";
                            }
                            ListSuccess.add("Image Name: "+img_name+", Task: "+task_tmp);
                            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(MainActivity.this,
                                    android.R.layout.simple_list_item_1, android.R.id.text1, ListSuccess);
                            listView1.setAdapter(adapter1);

                            //ImageView image = (ImageView)getActivity().findViewById(R.id.imageView3);
                            //image.setImageBitmap(bmp1);

                            /* En Decode Bitmap data */
                            //Log.e("My App :", "Image name : " + img_name);
                            // add the message to view
                            //addMessage(username, message);

                        }
                    } catch (JSONException e) {
                        return;
                    }
                }
            });
        }
    };
    private Emitter.Listener REC_CNT = new Emitter.Listener() {
         @Override
         public void call(final Object... args) { //Receive Connect Status from Server
             runOnUiThread(new Runnable() {
                 @Override
                 public void run() {
                     JSONObject data = (JSONObject) args[0];
                     //String numWorker;
                     try {
                         if (data.getString("uniqueId").compareTo(socket_id) == 0) {
                             socket_id = data.getString("id");
                             deviceName = data.getString("name");
                             ip = data.getString("ip");
                             //Log.e("My App : ", "socket_id : "+socket_id);

                             TextView tv = (TextView) findViewById(R.id.textView2);
                             tv.setText("Android name : "+deviceName);

                             Log.e("My App : ", "Connected..");
                         }
                         //numWorker = data.getString("numWorker");
                     } catch (JSONException e) {
                         return;
                     }
                     Log.e("My App : ", args[0].toString());
                    //Log.e("My App :", "Connect = " + numWorker);
                }
            });
        }
    };
    //--------------------------------------------------- End Message Setting  Distibute Processing -----------------------------

    //--------------------------------------------------- mSocket Setting -----------------------------
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://192.168.0.102:9999");
        } catch (URISyntaxException e) {}
    }
    @Override
    public void onDestroy() {
        super.onDestroy();

        mSocket.disconnect();
        //mSocket.off("new message", onNewMessage);
    }
    //--------------------------------------------------- End mSocket Setting -----------------------------


    public String getTimeDifference(Date endDate,Date startDate) {
        Date diff = new Date(endDate.getTime() - startDate.getTime());
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTime(diff);
        //int day=calendar.get(Calendar.DAY_OF_MONTH);
        String str="";

        int hours = calendar.get(Calendar.HOUR);
        if(hours>0) {
            str = Integer.toString(hours)+" Hours ";
        }

        int minute = calendar.get(Calendar.MINUTE);
        if(minute>0) {
            str = str + Integer.toString(minute) + " Minute ";
        }

        int second = calendar.get(Calendar.SECOND);
        if(second>0) {
            str = str + Integer.toString(second)+ " Second ";
        }

        int msecond = calendar.get(Calendar.MILLISECOND);
        if(msecond>0) {
            str = str + Integer.toString(msecond)+ " Millisecond ";
        }

        return str;
    }

    public static Bitmap doGreyscale(Bitmap src) {
        // constant factors
        final double GS_RED = 0.299;
        final double GS_GREEN = 0.587;
        final double GS_BLUE = 0.114;

        // create output bitmap
        Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
        // pixel information
        int A, R, G, B;
        int pixel;

        // get image size
        int width = src.getWidth();
        int height = src.getHeight();

        // scan through every single pixel
        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) {
                // get one pixel color
                pixel = src.getPixel(x, y);
                // retrieve color of all channels
                A = Color.alpha(pixel);
                R = Color.red(pixel);
                G = Color.green(pixel);
                B = Color.blue(pixel);
                // take conversion up to one single value
                R = G = B = (int)(GS_RED * R + GS_GREEN * G + GS_BLUE * B);
                // set new pixel color to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }

        // return final image
        return bmOut;
    }

    public static Bitmap drawableToBitmap (Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        width = width > 0 ? width : 1;
        int height = drawable.getIntrinsicHeight();
        height = height > 0 ? height : 1;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public static Bitmap applyGaussianBlur(Bitmap src) {
        //set gaussian blur configuration
        double[][] GaussianBlurConfig = new double[][] {
                { 1, 2, 1 },
                { 2, 4, 2 },
                { 1, 2, 1 }
        };
        // create instance of Convolution matrix
        ConvolutionMatrix convMatrix = new ConvolutionMatrix(3);
        // Apply Configuration
        convMatrix.applyConfig(GaussianBlurConfig);
        convMatrix.Factor = 16;
        convMatrix.Offset = 0;
        //return out put bitmap
        return ConvolutionMatrix.computeConvolution3x3(src, convMatrix);
    }

    public static Bitmap applyBlackFilter(Bitmap source) {
        // get image size
        int width = source.getWidth();
        int height = source.getHeight();
        int[] pixels = new int[width * height];
        // get pixel array from source
        source.getPixels(pixels, 0, width, 0, 0, width, height);
        // random object
        Random random = new Random();

        int R, G, B, index = 0, thresHold = 0;
        // iteration through pixels
        for(int y = 0; y < height; ++y) {
            for(int x = 0; x < width; ++x) {
                // get current index in 2D-matrix
                index = y * width + x;
                // get color
                R = Color.red(pixels[index]);
                G = Color.green(pixels[index]);
                B = Color.blue(pixels[index]);
                // generate threshold
                thresHold = random.nextInt(0xFF);
                if(R < thresHold && G < thresHold && B < thresHold) {
                    pixels[index] = Color.rgb(0x00, 0x00, 0x00);
                }
            }
        }
        // output bitmap
        Bitmap bmOut = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bmOut.setPixels(pixels, 0, width, 0, 0, width, height);
        return bmOut;
    }


}
