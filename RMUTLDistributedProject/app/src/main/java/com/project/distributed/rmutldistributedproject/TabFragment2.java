package com.project.distributed.rmutldistributedproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.TimeZone;
import java.util.UUID;

import io.socket.client.Socket;
import io.socket.client.IO;
import io.socket.emitter.Emitter;


public class TabFragment2 extends Fragment {

    String deviceName = null;
    String socket_id = UUID.randomUUID().toString();
    String ip = null;

    Date InizTimeDistri = new Date();

    private int PICK_IMAGE_REQUEST = 1;
    Bitmap ImageSelected = null;
    String ImageSelected_name = null;

    int do_process_gs = 0;
    int do_process_gb = 0;
    int do_process_bf = 0;


    ArrayList<String> pie = new ArrayList<String>();

    //--------------------------------------------------- The images to display -----------------------------
    //Integer[] imageIDs = {
    //        R.drawable.pic1,
    //        R.drawable.pic2,
    //        R.drawable.pic3,
    //        R.drawable.pic4,
    //        R.drawable.pic5,
    //};

    //Integer imageID = imageIDs[0];
    //--------------------------------------------------- End The images to display -----------------------------


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();



            String[] separated = uri.toString().split("/");
            ImageSelected_name = separated[4];

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));

                ImageSelected = Bitmap.createScaledBitmap(bitmap, 900, 900, false);
                ImageView imageView = (ImageView) getActivity().findViewById(R.id.imageView4);
                imageView.setImageBitmap(ImageSelected);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        Button buttonLoadImage = (Button)getActivity().findViewById(R.id.button);
        buttonLoadImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent();
                // Show only images, no videos or anything else
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                // Always show the chooser (if there are multiple options available)
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

            }
        });

        //--------------------------------------------------- The images to display -----------------------------
        // Note that Gallery view is deprecated in Android 4.1---
        /*
        Gallery gallery = (Gallery)getActivity().findViewById(R.id.gallery1);

        gallery.setAdapter(new ImageAdapter(getActivity()));


        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //Toast.makeText(getActivity().getApplicationContext(), "pic" + (position + 1) + " selected",
                //        Toast.LENGTH_SHORT).show();
                // display the images selected
                //ImageView imageView = (ImageView)getActivity().findViewById(R.id.image1);
                //imageView.setImageResource(imageIDs[position]);
                imageID = imageIDs[position];
            }
        });
        */
        /*
        Gallery gallery2 = (Gallery)getActivity().findViewById(R.id.gallery2);

        gallery2.setAdapter(new ImageAdapter(getActivity()));

        gallery2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //Toast.makeText(getActivity().getApplicationContext(), "pic" + (position + 1) + " selected",
                //        Toast.LENGTH_SHORT).show();
                // display the images selected
                //ImageView imageView = (ImageView)getActivity().findViewById(R.id.image1);
                //imageView.setImageResource(imageIDs[position]);
                //imageID = imageIDs[position];
                Intent i = new Intent(getActivity().getApplicationContext(), FullImageActivity.class);
                i.putExtra("id", position);
                startActivityForResult(i, 1);
            }
        });
        */
        //--------------------------------------------------- End The images to display -----------------------------

        //--------------------------------------------------- Set input display -----------------------------
        final Button button1 = (Button)getActivity().findViewById(R.id.button1);
        final ImageView imageView = (ImageView) getActivity().findViewById(R.id.imageView3);
        final ImageView imageView5 = (ImageView) getActivity().findViewById(R.id.imageView5);
        final ImageView imageView6 = (ImageView) getActivity().findViewById(R.id.imageView6);
        final TextView tv = (TextView)getActivity().findViewById(R.id.textView11);
        final TextView tv1 = (TextView)getActivity().findViewById(R.id.textView12);
        //--------------------------------------------------- End Set input display -----------------------------


        //--------------------------------------------------- Set button1  Normal Processing -----------------------------
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final CheckBox checkBox = (CheckBox)getActivity().findViewById(R.id.checkBox);
                final CheckBox checkBox2 = (CheckBox)getActivity().findViewById(R.id.checkBox2);
                final  CheckBox checkBox3 = (CheckBox)getActivity().findViewById(R.id.checkBox3);

                if(ImageSelected!=null) {
                /* Main Loop for----*/
                    if (checkBox.isChecked() || checkBox2.isChecked() || checkBox3.isChecked()) {
                        button1.setText("Waiting...");
                        button1.setEnabled(false);
                        imageView.setImageBitmap(null);
                        imageView5.setImageBitmap(null);
                        imageView6.setImageBitmap(null);
                        tv.setText("Normal Processing : -");
                        tv1.setText("Distibute Processing : -");

                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                Date interestingDate = new Date();

                                //Drawable myDrawable = getResources().getDrawable(imageID);
                                //myBitmap = ((BitmapDrawable) myDrawable).getBitmap();
                                //Bitmap myBitmap = TabFragment2.drawableToBitmap(myDrawable);

                                Bitmap myBitmap = ImageSelected;

                                if (checkBox.isChecked()) {
                                    Bitmap myGrayScale = TabFragment2.doGreyscale(myBitmap);
                                    imageView.setImageBitmap(myGrayScale);
                                }
                                if (checkBox2.isChecked()) {
                                    Bitmap myGaussianBlur = applyGaussianBlur(myBitmap);
                                    imageView5.setImageBitmap(myGaussianBlur);
                                }
                                if (checkBox3.isChecked()) {
                                    Bitmap myBlackFilter = applyBlackFilter(myBitmap);
                                    imageView6.setImageBitmap(myBlackFilter);
                                }

                                Toast.makeText(getActivity().getApplicationContext(), "Normal Processing Success. Time of Excecute:" + getTimeDifference(new Date(), interestingDate),
                                        Toast.LENGTH_LONG).show();

                                tv.setText("Normal Processing : Execution time " + getTimeDifference(new Date(), interestingDate));

                                button1.setText("Normal Processing");
                                button1.setEnabled(true);
                            }
                        }, 100);

                    }
                }else {
                    Toast.makeText(getActivity().getApplicationContext(), "Please Select Image",
                            Toast.LENGTH_LONG).show();
                }
                /* End Main Loop */
            }
        });
        //--------------------------------------------------- End set button1  Normal Processing -----------------------------

        //--------------------------------------------------- mSocket setting -----------------------------
        mSocket.connect(); // Set Connect
        Log.e("My App :", "Request to connect..");

        /* Set Name Connect */
        Log.e("My App :", "Connected.. ");
        JSONObject obj = new JSONObject();
        try {
            obj.put("name", "Client");
            obj.put("uniqueId", socket_id);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mSocket.emit("SND_CNT", obj);
        /* End Set Name Connect */
        mSocket.on("REC_CNT", REC_CNT); //Set Receive Connection Status from server
        mSocket.on("REC_WORK_RS", REC_WORK_RS); //Set Receive Work from Server
        mSocket.on("NO_WRK", NO_WRK); //Set Receive No Worker Connection

        //--------------------------------------------------- End mSocket setting -----------------------------


        //--------------------------------------------------- set button2  Distibute Processing -----------------------------
        final Button button2 = (Button)getActivity().findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                final CheckBox checkBox = (CheckBox) getActivity().findViewById(R.id.checkBox);
                final CheckBox checkBox2 = (CheckBox) getActivity().findViewById(R.id.checkBox2);
                final CheckBox checkBox3 = (CheckBox) getActivity().findViewById(R.id.checkBox3);

                /* Main Loop for----*/
                if(ImageSelected!=null) {
                    if (checkBox.isChecked() || checkBox2.isChecked() || checkBox3.isChecked()) {

                        button2.setText("Waiting...");
                        button2.setEnabled(false);
                        imageView.setImageBitmap(null);
                        imageView5.setImageBitmap(null);
                        imageView6.setImageBitmap(null);
                        tv.setText("Normal Processing : -");
                        tv1.setText("Distibute Processing : -");

                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                InizTimeDistri = new Date();

                                do_process_gs = 0;
                                do_process_bf = 0;
                                do_process_gb = 0;
                                if (checkBox.isChecked()) {
                                    do_process_gs = 1;
                                }
                                if (checkBox2.isChecked()) {
                                    do_process_gb = 1;
                                }
                                if (checkBox3.isChecked()) {
                                    do_process_bf = 1;

                                }

                                final JSONObject obj = new JSONObject();

                                try {
                                /* Encode Bitmap data */
                                    //Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.pic1);
                                    //Drawable myDrawable = getResources().getDrawable(imageID);
                                    //Bitmap bmp = ((BitmapDrawable) myDrawable).getBitmap();

                                    Bitmap bmp = ImageSelected;

                                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                                    byte[] byteArray = stream.toByteArray();
                                    String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                    obj.put("img_data", encodedImage);
                                /* Encode Bitmap data */
                                    obj.put("img_name", ImageSelected_name);
                                    obj.put("socket_id", socket_id);
                                    obj.put("name", deviceName);
                                    obj.put("process_gs", do_process_gs);
                                    obj.put("process_gb", do_process_gb);
                                    obj.put("process_bf", do_process_bf);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                mSocket.emit("SND_REQ", obj);
                                Log.e("My App :", "Sent work to server, id: " + socket_id + ",name: " + deviceName + ", img_name: pic1, process_gs: " + do_process_gs + ", process_bf: " + do_process_bf + ", process_gb: " + do_process_gb);

                                //Toast.makeText(getActivity().getApplicationContext(), "Distibute Processing Success. Time of Excecute:" + getTimeDifference(new Date(), interestingDate),
                                //        Toast.LENGTH_LONG).show();

                                //tv.setText("Result: Time of Excecute " + getTimeDifference(new Date(), interestingDate));

                                //button2.setText("Distibute Processing");
                                //button2.setEnabled(true);
                            }
                        }, 100);

                    }
                }else {
                    Toast.makeText(getActivity().getApplicationContext(), "Please Select Image",
                            Toast.LENGTH_LONG).show();
                }

                /* End Main Loop */

            }
        });
        //--------------------------------------------------- End set button2  Distibute Processing -----------------------------

    }

    //--------------------------------------------------- Message Setting  Distibute Processing -----------------------------
    private Emitter.Listener REC_WORK_RS = new Emitter.Listener() { //Receive Work from Server

        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    JSONObject data = (JSONObject) args[0];

                    String sort_id;
                    String worker_id;
                    String name;
                    String worker_name;
                    String img_name;
                    String task;
                    String StringImgByte;

                    try {
                        sort_id = data.getString("sort_id");
                        worker_id = data.getString("worker_id");
                        img_name = data.getString("img_name");
                        name = data.getString("name");
                        worker_name = data.getString("worker_name");
                        task = data.getString("task");

                        Log.e("My App :", "Received work  from server : sort_id: "+sort_id+", worker_id: "+worker_id+", worker_name: "+worker_name+", img_name: "+img_name+", task: "+task);
                        pie.add(worker_name);
                        //Log.e("My App :", "Received work");
                        /* Decode Bitmap data */
                        Toast.makeText(getActivity().getApplicationContext(), "Received work  from server : sort_id: "+sort_id+", worker_id: "+worker_id+", worker_name: "+worker_name+", img_name: "+img_name+", task: "+task,
                                Toast.LENGTH_LONG).show();

                        StringImgByte = data.getString("img_data");
                        byte[] bitMapData = Base64.decode(StringImgByte, Base64.DEFAULT);
                        Bitmap bmp1 = BitmapFactory.decodeByteArray(bitMapData, 0, bitMapData.length);
                        /* En Decode Bitmap data */
                        // Set to ImageView


                        if(task.compareTo("gs")==0) {
                            do_process_gs =0;
                            ImageView image = (ImageView)getActivity().findViewById(R.id.imageView3);
                            image.setImageBitmap(bmp1);
                        }else if(task.compareTo("gb")==0) {
                            do_process_gb =0;
                            ImageView image = (ImageView)getActivity().findViewById(R.id.imageView5);
                            image.setImageBitmap(bmp1);
                        }else if(task.compareTo("bf")==0) {
                            do_process_bf =0;
                            ImageView image = (ImageView)getActivity().findViewById(R.id.imageView6);
                            image.setImageBitmap(bmp1);
                        }

                        Button button2 = (Button)getActivity().findViewById(R.id.button2);
                        TextView tv = (TextView)getActivity().findViewById(R.id.textView12);

                        if(do_process_gs==0 && do_process_bf==0 && do_process_gb==0) {

                            String tmp_txt = ", Android :";
                            ArrayList<String> tmp_arr =new ArrayList<String>();
                            for (int counter = 0; counter < pie.size(); counter++) {
                                String tmp = pie.get(counter);
                                int num = 0;

                                for (int counter1 = 0; counter1 < pie.size(); counter1++) {
                                    if(tmp.compareTo(pie.get(counter1))==0) {
                                        num++;
                                    }
                                }

                                tmp_arr.add(pie.get(counter)+"("+num+" Process, "+(((float)num/pie.size())*100)+"%)");
                            }
                            HashSet<String> hashSet = new HashSet<String>();
                            hashSet.addAll(tmp_arr);
                            tmp_arr.clear();
                            tmp_arr.addAll(hashSet);

                            for (int counter = 0; counter < tmp_arr.size(); counter++) {
                                tmp_txt = tmp_txt+" "+tmp_arr.get(counter);
                            }
                            //tmp_txt = tmp_txt+" "+pie.get(counter)+"("+num+" Process)";

                            Toast.makeText(getActivity().getApplicationContext(), "Distibute Processing Success. Time of Excecute:" + getTimeDifference(new Date(), InizTimeDistri),
                                    Toast.LENGTH_LONG).show();

                            tv.setText("Distibute Processing : Execution time " + getTimeDifference(new Date(), InizTimeDistri)+tmp_txt);
                            button2.setText("Distibute Processing");
                            button2.setEnabled(true);
                            pie = new ArrayList<String>();
                        }

                    } catch (JSONException e) {
                        return;
                    }

                    //Log.e("My App :", "Image name : "+img_name);
                    // add the message to view
                    //addMessage(username, message);
                }
            });
        }
    };
    private Emitter.Listener NO_WRK = new Emitter.Listener() { //Receive No Worker Connection

        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    JSONObject data = (JSONObject) args[0];

                    String remark;

                    try {
                        remark = data.getString("remark");
                        Log.e("My App :", remark);
                        Toast.makeText(getActivity().getApplicationContext(), remark,
                                Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        return;
                    }
                }
            });
        }
    };
    private Emitter.Listener REC_CNT = new Emitter.Listener() { //Receive Connect Status from Server
        @Override
        public void call(final Object... args) { //Receive Connection Status from server
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];

                    String numWorker;
                    try {
                        if(data.getString("uniqueId").compareTo(socket_id)==0) {
                            socket_id = data.getString("id");
                            deviceName = data.getString("name");
                            ip = data.getString("ip");
                            //Log.e("My App : ", "socket_id : "+socket_id);
                            Log.e("My App : ", "Connected..");
                        }

                        numWorker = data.getString("numWorker");

                        ListView listView; /* ListView Device Tab */
                        listView = (ListView) getActivity().findViewById(R.id.listView);
                        int num = Integer.parseInt(numWorker);
                        String[] values = new String[num];
                        for(int i=0;i<num;i++ ){
                            values[i] = "Name: Worker "+(i+1)+", Status: Online";
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                                android.R.layout.simple_list_item_1, android.R.id.text1, values);
                        listView.setAdapter(adapter);

                        TextView tv2 = (TextView)getActivity().findViewById(R.id.textView2);
                        tv2.setText("Connect = " + numWorker);

                    } catch (JSONException e) {
                        return;
                    }
                    Log.e("My App : ", args[0].toString());
                    Log.e("My App :", "Connect = " + numWorker);

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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_fragment_2, container, false);
    }

    /*
    public class ImageAdapter extends BaseAdapter {
        private Context context;
        private int itemBackground;
        public ImageAdapter(Context c)
        {
            context = c;
            // sets a grey background; wraps around the images
            TypedArray a = c.obtainStyledAttributes(R.styleable.MyGallery);
            itemBackground = a.getResourceId(R.styleable.MyGallery_android_galleryItemBackground, 0);
            a.recycle();
        }
        // returns the number of images
        public int getCount() {
            return imageIDs.length;
        }
        // returns the ID of an item
        public Object getItem(int position) {
            return position;
        }
        // returns the ID of an item
        public long getItemId(int position) {
            return position;
        }
        // returns an ImageView view
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView = new ImageView(context);
            imageView.setImageResource(imageIDs[position]);
            imageView.setLayoutParams(new Gallery.LayoutParams(380, 380));
            imageView.setBackgroundResource(itemBackground);
            return imageView;
        }
    }
    */

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

