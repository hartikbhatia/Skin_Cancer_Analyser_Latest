package com.amitshekhar.tflite;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.wonderkiln.camerakit.CameraView;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;



public class MainActivity extends AppCompatActivity {

    private static final String MODEL_PATH = "skin_model.tflite";
    private static final int ser=2;
    private static final boolean QUANT = false;
    private static final String LABEL_PATH = "labels.txt";
    private static final int INPUT_SIZE = 224;
    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    ImageView imageView;
    ImageButton Browse;
    TextView textMarque;
    Bitmap bitmap;


    private Classifier classifier;

    private Executor executor = Executors.newSingleThreadExecutor();
    private TextView textViewResult;
    private ImageButton btnDetectObject, btnToggleCamera;
    private ImageView imageViewResult;
    private CameraView cameraView;
    MeowBottomNavigation bottomNavigation;
    int c=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //btnDetectObject = (ImageButton)findViewById(R.id.btnDetectObject);
        //Browse = (ImageButton) findViewById(R.id.browse);
        imageView = (ImageView) findViewById(R.id.imageView);
        textViewResult = findViewById(R.id.textViewResult);
        textMarque = findViewById(R.id.textMarque);


        //marque text


        textViewResult.setMovementMethod(new ScrollingMovementMethod());

         bottomNavigation = (MeowBottomNavigation)findViewById(R.id.bottomnav);
        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.ic_add_a_photo_black_24dp));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_search_black_24dp));
      //  bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_search_black_24dp));


        bottomNavigation.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model item) {
                int id=item.getId();
                if(id== 1){

                    checkandroidversion();
                     c=2;

                }if(id==2){
                    if(c==2) {
                        bitmap = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, false);
                        final List<Classifier.Recognition> results = classifier.recognizeImage(bitmap);
                        textViewResult.setText("Analysis:" + results.toString());
                    }else{
                        Toast.makeText(MainActivity.this, "Please add image first", Toast.LENGTH_SHORT).show();
                    }

                }



                return null;
            }
        });

       /* Browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //CropImage.startPickImageActivity(camera.this);
                checkandroidversion();
            }
        });*/



       /* btnDetectObject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                bitmap = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, false);
                final List<Classifier.Recognition> results = classifier.recognizeImage(bitmap);
                textViewResult.setText("Analysis:"+results.toString());

            }
        });*/


        initTensorFlowAndLoadModel();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                classifier.close();
            }
        });
    }

    private void initTensorFlowAndLoadModel() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    classifier = TensorFlowImageClassifier.create(
                            getAssets(),
                            MODEL_PATH,
                            LABEL_PATH,
                            INPUT_SIZE,
                            QUANT);

                } catch (final Exception e) {
                    throw new RuntimeException("Error initializing TensorFlow!", e);
                }
            }
        });
    }



    public void checkandroidversion(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            try{
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA},555);
            }catch (Exception e){

            }


        }else{
            pickImage();
        }
    }

    private void pickImage() {
        CropImage.startPickImageActivity(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==555 &&grantResults[0] == PackageManager.PERMISSION_GRANTED){
            pickImage();
        }else{
            checkandroidversion();
        }
    }










    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode==CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE
                && resultCode == Activity.RESULT_OK){
            Uri imageUri=CropImage.getPickImageResultUri(this,data);
            croprequest(imageUri);
        }
        if(requestCode ==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            if(resultCode==RESULT_OK){
                try {
                     bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),result.getUri());
                    imageView.setImageBitmap(bitmap);


                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void croprequest(Uri imageuri) {
        CropImage.activity(imageuri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu1,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.reset){
            //imageView.setImageResource(R.drawable.placeholder);
            //textViewResult.setText("Analysis:");
            finish();
            startActivity(getIntent());

        }
        return true;
    }
}


//xml
/*
<ImageButton
        android:id="@+id/browse"
                android:layout_width="70dp"
                android:layout_height="70dp"


                android:layout_alignParentLeft="true"
                android:layout_alignParentStart="true"
                android:layout_alignParentTop="true"
                android:layout_marginLeft="30dp"
                android:layout_marginTop="410dp"
                android:background="@drawable/circle"
                android:elevation="8dp"
                app:srcCompat="@drawable/ic_add_a_photo_black_24dp" />


<ImageButton
        android:id="@+id/btnDetectObject"
                android:layout_width="70dp"
                android:layout_height="70dp"


                android:layout_alignParentRight="true"
                android:layout_alignParentEnd="true"
                android:layout_alignParentTop="true"
                android:layout_marginRight="30dp"
                android:layout_marginTop="410dp"
                android:background="@drawable/circle"
                android:elevation="8dp"
                app:srcCompat="@drawable/ic_search_black_24dp" />
*/

