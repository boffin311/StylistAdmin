package com.example.stylistadmin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.stylistadmin.R;

import java.util.ArrayList;

public class AdapterImagesList extends RecyclerView.Adapter<AdapterImagesList.MyHolder> {
    ArrayList<String> arrayList;
    Context context;
    public AdapterImagesList(ArrayList<String> arrayList,Context context){
        this.context=context;
        this.arrayList=arrayList;
    }
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater li= (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=li.inflate(R.layout.adapter_image_list,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        Glide.with(context).load(arrayList.get(position)).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageList);
        }
    }
}
