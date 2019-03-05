package com.example.emon.tourmate.Other_class;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.emon.tourmate.R;

import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {
    Context context;

    List<ImageUploadInfo> uriList;
    public PhotoAdapter(Context context, List<ImageUploadInfo> uriList) {
        this.context = context;
        this.uriList = uriList;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
          View view = LayoutInflater.from(context).inflate(R.layout.photo_item,viewGroup,false);

        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder photoViewHolder, int i) {

         String url= uriList.get(i).getImageURL();
                Glide.with(context).load(url).into(photoViewHolder.imageView);

    }

    @Override
    public int getItemCount() {
        return uriList.size();
    }

    public class PhotoViewHolder extends RecyclerView.ViewHolder{
   ImageView imageView;
        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.MyPhotoIVid);
        }
    }
}
