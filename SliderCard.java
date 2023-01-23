package com.bj.enterprise.simple.simple.cards;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bj.enterprise.simple.simple.R;
import com.bj.enterprise.simple.simple.model.Crystal;
import com.bj.enterprise.simple.simple.utils.DecodeBitmapTask;
import com.squareup.picasso.Picasso;
import com.bj.enterprise.simple.simple.MainActivity;
import java.io.File;

public class SliderCard extends RecyclerView.ViewHolder implements DecodeBitmapTask.Listener {

    private static int viewWidth = 0;
    private static int viewHeight = 0;

    private final ImageView imageView;
    //private final TextView textView;

    private DecodeBitmapTask task;

    public SliderCard(View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.image);

    }

    void bind(final Crystal crys, Context ctx) {

        Crystal crystal = crys;
        //CrystalArrayList.crystals.add(crystal.getName());

        Intent intent = new Intent("custom-message");
        //            intent.putExtra("quantity",Integer.parseInt(quantity.getText().toString()));
        intent.putExtra("crystal",crystal.getName());
        intent.putExtra("desc", crystal.getDesc());
        LocalBroadcastManager.getInstance(ctx).sendBroadcast(intent);


        Picasso.get()
                .load(String.valueOf(crystal.getUrls()))
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(imageView);
   //    TextView textView = (TextView) mainAct.findViewById(R.id.tv_country_1);
   //     textView.setText(crystal.getName());
        /*
        if (viewWidth == 0) {
            itemView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    itemView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                    viewWidth = itemView.getWidth();
                    viewHeight = itemView.getHeight();

                    //viewWidth = 800;
                    //viewHeight = 800;


                    loadURIBitmap(bitmap);
                }
            });
        } else {
            loadURIBitmap(bitmap);
        }
        */

    }

    void clearContent() {
        if (task != null) {
            task.cancel(true);
        }
    }

    private void loadURIBitmap(Uri image){



    }

    private void loadBitmap(File bmpfile) {
        task = new DecodeBitmapTask(itemView.getResources(), bmpfile, viewWidth, viewHeight, this);
        task.execute();
    }

    @Override
    public void onPostExecuted(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }

}