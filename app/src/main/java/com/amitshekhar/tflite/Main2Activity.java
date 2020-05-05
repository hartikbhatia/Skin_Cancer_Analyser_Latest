package com.amitshekhar.tflite;

import android.annotation.SuppressLint;
import android.content.Intent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;

import android.view.View;
import android.widget.ImageView;

public class Main2Activity extends AppCompatActivity {

    CardView cardMelanoma,cardSquamous;
    CardView cardBasal,cardMerkel;
    FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        fab=(FloatingActionButton)findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent i=new Intent(Main2Activity.this,MainActivity.class);
                startActivity(i);
            }
        });
        final ImageView imageMelanoma=(ImageView)findViewById(R.id.imageMelanoma);
        cardMelanoma=(CardView)findViewById(R.id.cardMelanoma);
        cardMelanoma.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Main2Activity.this,melanoma.class);
                ActivityOptionsCompat options=ActivityOptionsCompat.makeSceneTransitionAnimation(Main2Activity.this,imageMelanoma,ViewCompat.getTransitionName(imageMelanoma));
                startActivity(i,options.toBundle());
            }
        });

        final ImageView imageBasal=(ImageView)findViewById(R.id.imageBasal);
        cardBasal=(CardView)findViewById(R.id.cardBasal);
        cardBasal.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                Intent j=new Intent(Main2Activity.this,basalCell.class);
               ActivityOptionsCompat option=ActivityOptionsCompat.makeSceneTransitionAnimation(Main2Activity.this,imageBasal,"abc");
                startActivity(j,option.toBundle());
            }
        });
        final ImageView imageSquamous=(ImageView)findViewById(R.id.imageSquamous);
        cardSquamous=(CardView)findViewById(R.id.cardSquamous);
        cardSquamous.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                Intent j=new Intent(Main2Activity.this,SquamousCell.class);
                ActivityOptionsCompat option=ActivityOptionsCompat.makeSceneTransitionAnimation(Main2Activity.this,imageSquamous,"pqr");
                startActivity(j,option.toBundle());
            }
        });
        final ImageView imageMerkel=(ImageView)findViewById(R.id.imageMerkel);
        cardMerkel=(CardView)findViewById(R.id.cardMerkel);
        cardMerkel.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
               Intent j=new Intent(Main2Activity.this,Merkel.class);
                ActivityOptionsCompat option=ActivityOptionsCompat.makeSceneTransitionAnimation(Main2Activity.this,imageMerkel,"cda");
                startActivity(j,option.toBundle());
            }
        });

    }
}
