package com.bj.enterprise.simple.simple;

import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.transition.Transition;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;

import com.bj.enterprise.simple.simple.utils.DecodeBitmapTask;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class DetailsActivity extends AppCompatActivity implements DecodeBitmapTask.Listener {

    static final String BUNDLE_IMAGE_FILE = "BUNDLE_IMAGE_FILE";
    static final String BUNDLE_SMALLRES_LOC = "BUNDLE_IMAGE_FILE_LOCATION";
    private ImageView imageView;
    private DecodeBitmapTask decodeBitmapTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        final String smallResByteArray = getIntent().getStringExtra(BUNDLE_IMAGE_FILE);
        final String smallResFileLocation = getIntent().getStringExtra(BUNDLE_SMALLRES_LOC);
//        Bitmap smallResFile = BitmapFactory.decodeByteArray(smallResByteArray, 0, smallResByteArray.length);
  /*      if (smallResFile == null) {
            finish();
            return;
        }
*/
        imageView = (ImageView)findViewById(R.id.image);
       // imageView.setImageBitmap(smallResFile);

        Picasso.get()
                .load(String.valueOf(smallResFileLocation))
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(imageView);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DetailsActivity.super.onBackPressed();
            }
        });

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            loadFullSizeBitmap(smallResFileLocation);
        } else {
            getWindow().getSharedElementEnterTransition().addListener(new Transition.TransitionListener() {

                private boolean isClosing = false;

                @Override public void onTransitionPause(Transition transition) {}
                @Override public void onTransitionResume(Transition transition) {}
                @Override public void onTransitionCancel(Transition transition) {}

                @Override public void onTransitionStart(Transition transition) {
                    if (isClosing) {
                        addCardCorners();
                    }
                }

                @Override public void onTransitionEnd(Transition transition) {
                    if (!isClosing) {
                        isClosing = true;

                        removeCardCorners();
                        loadFullSizeBitmap(smallResFileLocation);
                    }
                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (isFinishing() && decodeBitmapTask != null) {
            decodeBitmapTask.cancel(true);
        }
    }

    private void addCardCorners() {
        final CardView cardView = (CardView) findViewById(R.id.card);
        cardView.setRadius(25f);
    }

    private void removeCardCorners() {
        final CardView cardView = (CardView)findViewById(R.id.card);
        ObjectAnimator.ofFloat(cardView, "radius", 0f).setDuration(50).start();
    }

    private void loadFullSizeBitmap(String filename) {

        String[] only_file_name = filename.split(".");
//        only_file_name[0]+= "_big";

      //  String bigBitmapFile = only_file_name[0]+"."+ only_file_name[1];
        String bigBitmapFile = filename;
    /*
        switch (smallResId) {
            case R.drawable.p6:
                bigResId = R.drawable.p6_big;
                break;
            case R.drawable.p7:
                bigResId = R.drawable.p7_big;
                break;
            case R.drawable.p8:
                bigResId = R.drawable.p8_big;
                break;
            case R.drawable.p9:
                bigResId = R.drawable.p9_big;
                break;
            case R.drawable.p10:
                bigResId = R.drawable.p10_big;
                break;

            case R.drawable.p11:
                bigResId = R.drawable.p11_big;
                break;

            case R.drawable.p12:
                bigResId = R.drawable.p12_big;
                break;

            case R.drawable.p13:
                bigResId = R.drawable.p13_big;
                break;
            case R.drawable.p14:
                bigResId = R.drawable.p14_big;
                break;

            case R.drawable.p15:
                bigResId = R.drawable.p15_big;
                break;

            case R.drawable.p16:
                bigResId = R.drawable.p16_big;
                break;

            case R.drawable.p17:
                bigResId = R.drawable.p17_big;
                break;

            case R.drawable.p18:
                bigResId = R.drawable.p18_big;
                break;

            case R.drawable.p19:
                bigResId = R.drawable.p19_big;
                break;

            case R.drawable.p20:
                bigResId = R.drawable.p20_big;
                break;

            default:
                bigResId = R.drawable.p6_big;
            */



        final DisplayMetrics metrics = new DisplayMetrics();
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

        final int h = (int) (displayMetrics.heightPixels / displayMetrics.density);
        final int w = (int) (displayMetrics.widthPixels / displayMetrics.density);

        //decodeBitmapTask = new DecodeBitmapTask(getResources(), new File(bigBitmapFile), w, h, this);
    //    decodeBitmapTask = new DecodeBitmapTask(getResources(), new File(bigBitmapFile), w, h, this);
    //    decodeBitmapTask.execute();
    }

    @Override
    public void onPostExecuted(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }

}
