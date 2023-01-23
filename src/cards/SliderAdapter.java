package com.bj.enterprise.simple.simple.cards;



import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.bj.enterprise.simple.simple.MainActivity;
import com.bj.enterprise.simple.simple.R;
import com.bj.enterprise.simple.simple.model.Crystal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class SliderAdapter extends RecyclerView.Adapter<SliderCard> {

  //  private final int count;
  //  private final List<String> content;
   // private final View.OnClickListener listener;
  //  List items;
   Map<Integer, Crystal> deletedItems;
   Context context;
   ArrayList<Crystal> listCrystals;

    public SliderAdapter(ArrayList<Crystal> arrayList, Context ctx) {

        this.listCrystals = arrayList;
        deletedItems = new HashMap<>();
        this.context = ctx;

    }



    public interface OnRestaurantSelectedListener {

        void onRestaurantSelected(DocumentSnapshot restaurant);

    }

    private OnRestaurantSelectedListener mListener;
/*
    public SliderAdapter(Query query, OnRestaurantSelectedListener listener) {
        super(query);
        mListener = listener;
    }
*/
    @NonNull
    @Override
    public SliderCard onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new SliderCard(inflater.inflate(R.layout.layout_slider_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SliderCard holder, int position) {

        holder.bind(listCrystals.get(position), context);
        //holder.setContent(content.get(position % content.size()));


        Log.d(TAG, "onBindViewHolder: ");

    }



/*
    public void hideItem(final int position) {
        deletedItems.put(position,content.get(position));
        items.remove(position);
        notifyItemRemoved(position);
    }
*/

    @Override
    public int getItemCount()
    {
        return listCrystals.size();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


public void hideItem(final int position) {
    deletedItems.put(position,listCrystals.get(position));
   // items.remove(position);
    notifyItemRemoved(position);
}

/*
    @Override
    public SliderCard onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.layout_slider_card, parent, false);

        if (listener != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(view);
                }
            });
        }

        return new SliderCard(view);
    }

*/
/*
    @Override
    public void onBindViewHolder(SliderCard holder, int position) {
        holder.setContent(content.get(position % content.size()));



        Log.d(TAG, "onBindViewHolder: ");
        
        //favoriteButton.setFavorite(isFavorite(data.get(position)), false);
    }
*/
    @Override
    public void onViewRecycled(SliderCard holder) {
        holder.clearContent();
    }


}
