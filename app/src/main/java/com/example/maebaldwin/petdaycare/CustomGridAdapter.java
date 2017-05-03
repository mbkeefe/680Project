package com.example.maebaldwin.petdaycare;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by maebaldwin on 4/22/17.
 */

// Custom Grid is for displaying the service name and its amount in different colors
// Used in the SitterProfile activity

public class CustomGridAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<BrowseSitters.Service> services;

    private class ViewHolder {
        TextView service;
        TextView fee;
    }

    public CustomGridAdapter(Context context, ArrayList<BrowseSitters.Service> objects) {
        inflater = LayoutInflater.from(context);
        this.services = objects;
    }
    public BrowseSitters.Service getItem(int position) {
        return services.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public void clear (){
        services.clear();
    }


    public int getCount(){
        return services.size();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        CustomGridAdapter.ViewHolder holder = null;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.service_list, null);
            holder.service = (TextView)convertView.findViewById(R.id.srv_name);
            holder.fee = (TextView)convertView.findViewById(R.id.srv_fee);
            convertView.setTag(holder);

        } else holder = (CustomGridAdapter.ViewHolder) convertView.getTag();

        holder.service.setText(services.get(position).getService());
        holder.fee.setText("$" + services.get(position).getFee());
        holder.fee.setTextColor(Color.parseColor("#ff0000"));


        return convertView;
    }
}
