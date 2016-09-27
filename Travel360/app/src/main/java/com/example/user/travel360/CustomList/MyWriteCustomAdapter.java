package com.example.user.travel360.CustomList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.travel360.R;

import java.util.ArrayList;


public class MyWriteCustomAdapter extends BaseAdapter {

    public ArrayList<MyWriteItemData> itemDatas = null;
    public LayoutInflater layoutInflater = null;
    public Context  contexts=null;


    public MyWriteCustomAdapter(ArrayList<MyWriteItemData> itemDatas, Context ctx){

        contexts = ctx;
        this.itemDatas = itemDatas;
        layoutInflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public void setItemData(ArrayList<MyWriteItemData> itemDatas){
        this.itemDatas = itemDatas;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return (itemDatas != null) ? itemDatas.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return (itemDatas != null && (0 <= position && position < itemDatas.size()) ? itemDatas.get(position) : null);
    }

    @Override
    public long getItemId(int position) {
        return (itemDatas != null && (0 <= position && position < itemDatas.size()) ? position : 0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MyWriteViewHolder viewHolder = new MyWriteViewHolder();

        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.context_item, parent ,false);

            viewHolder.context_title = (TextView)convertView.findViewById(R.id.context_title);
            viewHolder.context_text=(TextView)convertView.findViewById(R.id.context_text);
            viewHolder.context_location = (TextView)convertView.findViewById(R.id.context_location);
            viewHolder.context_date = (TextView)convertView.findViewById(R.id.context_date);
             convertView.setTag(viewHolder);
        }
        else{
            viewHolder=(MyWriteViewHolder)convertView.getTag();
        }

        MyWriteItemData itemData = itemDatas.get(position);
       // viewHolder.comment_img.setImageBitmap(itemData.comment_img);
        viewHolder.context_title.setText(itemData.context_title);
        viewHolder.context_text.setText(itemData.context_text);
        viewHolder.context_location.setText(itemData.context_location);
        viewHolder.context_date.setText(itemData.context_date);

        return convertView;
    }


    public void addListItem( String title ,String text, String location, String date)
    {
        MyWriteItemData addinfo = null;
        addinfo = new MyWriteItemData();

        addinfo.context_title = title;
        addinfo.context_text = text;
        addinfo.context_location = location;
        addinfo.context_date = date;

        itemDatas.add(addinfo);
    }

    public void clear(){
        if(itemDatas.size()>0)
            itemDatas.clear();
    }

}
