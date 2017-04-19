package com.example.maebaldwin.petdaycare;

/**
 * Created by maebaldwin on 4/17/17.
 */

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.view.MenuItem;
import java.util.ArrayList;
import android.widget.TextView;

public class CustomListAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<BrowseSitters.Sitter> sitters;

    private class ViewHolder {
        TextView sitter;
        TextView fee;
        TextView desc;
    }

    public CustomListAdapter(Context context, ArrayList<BrowseSitters.Sitter> objects) {
        inflater = LayoutInflater.from(context);
        this.sitters = objects;
    }
    public int getCount(){
        return sitters.size();
    }

    @Override
    public BrowseSitters.Sitter getItem(int position) {
        return sitters.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    public void add (BrowseSitters.Sitter sitter){
        sitters.add(sitter);
    }

    public void clear (){
        sitters.clear();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list, null);
            holder.sitter = (TextView) convertView.findViewById(R.id.sitter);
            holder.fee = (TextView) convertView.findViewById(R.id.fee);
            holder.desc = (TextView) convertView.findViewById(R.id.desc);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.sitter.setText(sitters.get(position).getName());
        holder.desc.setText(sitters.get(position).getDesc());
        if(sitters.get(position).getFee()!=0){
            holder.fee.setTextColor(Color.parseColor("#ff0000"));
            holder.fee.setText("$" + Integer.toString(sitters.get(position).getFee()));
        }


        return convertView;
    }


}
