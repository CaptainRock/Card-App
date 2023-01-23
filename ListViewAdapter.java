package com.bj.enterprise.simple.simple.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bj.enterprise.simple.simple.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListViewAdapter extends ArrayAdapter<String> {

    // Declare Variables

    Context mContext;
    LayoutInflater inflater;
    private List<String> namesList = null;
    private ArrayList<String> arraylist;

    public ListViewAdapter(Context context, ArrayList<String> namesList) {

        super(context,0,namesList);

        mContext = context;
      //  this.animalNamesList = animalNamesList;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<String>();
        this.arraylist.addAll(namesList);
        this.namesList = namesList;
    }

    public class ViewHolder {
        TextView arup;
    }

    @Override
    public int getCount() {
        return namesList.size();
    }

    @Override
    public String getItem(int position) {
        return namesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.listview_item, null);
            // Locate the TextViews in listview_item.xml
            holder.arup = (TextView) view.findViewById(R.id.name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        //holder.name.setText(namesList.get(position));
        holder.arup.setText(namesList.get(position));

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        namesList.clear();
        if (charText.length() == 0) {

            //Do nothing when there is no search string
            // namesList.addAll(arraylist);
        } else {
            for (String wp : arraylist) {
                if (wp.toLowerCase(Locale.getDefault()).contains(charText)) {
                    namesList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }






}
