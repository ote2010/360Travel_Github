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



public class CustomAdapter extends BaseAdapter {

    public ArrayList<ItemData> itemDatas = null;
    public LayoutInflater layoutInflater = null;
    public Context  contexts=null;


    public CustomAdapter(ArrayList<ItemData> itemDatas, Context ctx){

        contexts = ctx;
        this.itemDatas = itemDatas;
        layoutInflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public void setItemData(ArrayList<ItemData> itemDatas){
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

        ViewHolder viewHolder = new ViewHolder();

        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.comment_item, parent ,false);

            viewHolder.comment_img = (ImageView)convertView.findViewById(R.id.comment_img);
            viewHolder.comment_id=(TextView)convertView.findViewById(R.id.comment_id);
            viewHolder.comment_txt = (TextView)convertView.findViewById(R.id.comment_txt);
             convertView.setTag(viewHolder);
        }
        else{
            viewHolder=(ViewHolder)convertView.getTag();
        }

        ItemData itemData = itemDatas.get(position);
       // viewHolder.comment_img.setImageBitmap(itemData.comment_img);
        viewHolder.comment_img.setImageDrawable(contexts.getResources().getDrawable(R.drawable.empty_profile));
        viewHolder.comment_id.setText(itemData.comment_id);
        viewHolder.comment_txt.setText(itemData.comment_txt);

        return convertView;
    }


    public void addListItem( String id ,String txt)
    {
        ItemData addinfo = null;
        addinfo = new ItemData();

        addinfo.comment_txt = txt;
        addinfo.comment_id = id;
    //   addinfo.comment_img = img;

        itemDatas.add(addinfo);
    }

    public void clear(){
        if(itemDatas.size()>0)
            itemDatas.clear();
    }

}
