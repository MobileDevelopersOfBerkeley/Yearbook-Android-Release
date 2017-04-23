package com.mdb.yearbook.android;


import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * Created by MEEEE on 4/4/17.
 */

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolderWrapper> {

    public static final int VIEW_TYPE_2_IMAGES = 0;
    public static final int VIEW_TYPE_1_IMAGES = 1;
    public static final int VIEW_TYPE_3_IMAGES = 2;

    private Context context;
    ArrayList<Photo> photoList;

    public FeedAdapter(Context context, ArrayList<Photo> photoList) {
        this.context = context;
        this.photoList = photoList;
    }

    public ViewHolderWrapper onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_1_IMAGES) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_row_layout_1, parent, false);
            return new CustomViewHolder(view);

        } else if (viewType == VIEW_TYPE_2_IMAGES) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_row_layout_2, parent, false);
            return new CustomViewHolder2(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_row_layout_3, parent, false);
            return new CustomViewHolder3(view);
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolderWrapper holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_1_IMAGES) {
            ((CustomViewHolder)holder).photo.setVisibility(View.VISIBLE);
            Photo p;
            try {
                p = photoList.get(getStartingArraylistPosition(position));
            } catch (Exception e) {
                p = null;
            }

            String url = p.getImageUrl();
            Log.d("this is the url", url);

//            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("photos/"+p.getImageUrl()+".jpg");
            StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(url);
            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
            {
                @Override
                public void onSuccess(Uri uri)
                {
                    Glide.with(context)
                            .load(uri)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(((CustomViewHolder)holder).photo);
                }

            });

        } else if (getItemViewType(position) == VIEW_TYPE_2_IMAGES) {
            ((CustomViewHolder2)holder).photo.setVisibility(View.VISIBLE);
            ((CustomViewHolder2)holder).photo2.setVisibility(View.VISIBLE);
            Photo p = photoList.get(getStartingArraylistPosition(position));
            String url = p.getImageUrl();

//            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("photos/"+p.getImageUrl()+".jpg");
            StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(url);
            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
            {
                @Override
                public void onSuccess(Uri uri)
                {
                    Glide.with(context)
                            .load(uri)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(((CustomViewHolder2)holder).photo);
                }

            });

            Photo p2;
            try {

                p2 = photoList.get(getStartingArraylistPosition(position) + 1);
                String url2 = p2.getImageUrl();

                Log.d("this is the url", url);


//                StorageReference storageRef2 = FirebaseStorage.getInstance().getReference().child("photos/"+p2.getImageUrl()+".jpg");
                StorageReference storageRef2 = FirebaseStorage.getInstance().getReferenceFromUrl(url2);
                storageRef2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                {
                    @Override
                    public void onSuccess(Uri uri)
                    {

                        Glide.with(context)
                                .load(uri)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(((CustomViewHolder2)holder).photo2);
                    }

                });
            } catch (Exception e) {
                ((CustomViewHolder2)holder).photo2.setVisibility(View.INVISIBLE);
//                ((CustomViewHolder2)holder).photo2 = null;
            }
        }
        else {
            ((CustomViewHolder3)holder).photo.setVisibility(View.VISIBLE);
            ((CustomViewHolder3)holder).photo2.setVisibility(View.VISIBLE);
            ((CustomViewHolder3)holder).photo3.setVisibility(View.VISIBLE);
            Photo p = photoList.get(getStartingArraylistPosition(position));
            String url = p.getImageUrl();

//            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("photos/"+p.getImageUrl()+".jpg");
            StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(url);
            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
            {
                @Override
                public void onSuccess(Uri uri)
                {
                    Glide.with(context)
                            .load(uri)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(((CustomViewHolder3)holder).photo);
                }
            });
            Photo p2;
            try {

                p2 = photoList.get(getStartingArraylistPosition(position) + 1);
                String url2 = p2.getImageUrl();

                Log.d("this is the url", url);

                StorageReference storageRef2 = FirebaseStorage.getInstance().getReferenceFromUrl(url2);
//                StorageReference storageRef2 = FirebaseStorage.getInstance().getReference().child("photos/"+p2.getImageUrl()+".jpg");
                storageRef2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                {
                    @Override
                    public void onSuccess(Uri uri)
                    {
                        Glide.with(YearbookActivity.context)
                                .load(uri)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(((CustomViewHolder3)holder).photo2);
                    }

                });
            } catch (Exception e) {
                ((CustomViewHolder3)holder).photo2.setVisibility(View.INVISIBLE);
//                ((CustomViewHolder3)holder).photo2 = null;
            }

            Photo p3;
            try {

                p3 = photoList.get(getStartingArraylistPosition(position) + 2);
                String url2 = p3.getImageUrl();

                Log.d("this is the url", url);


//                StorageReference storageRef3 = FirebaseStorage.getInstance().getReference().child("photos/"+p3.getImageUrl()+".jpg");
                StorageReference storageRef3 = FirebaseStorage.getInstance().getReferenceFromUrl(url2);
                storageRef3.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                {
                    @Override
                    public void onSuccess(Uri uri)
                    {
                        Glide.with(context)
                                .load(uri)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(((CustomViewHolder3)holder).photo3);
                    }

                });
            } catch (Exception e) {
                ((CustomViewHolder3)holder).photo3.setVisibility(View.INVISIBLE);
//                ((CustomViewHolder3)holder).photo3 = null;
            }
        }

    }

    public int getStartingArraylistPosition(int row) {
        int photoNum = 0;
        while (row / 3 >= 1) {
            photoNum += 6;
            row = row - 3;
        }

        if (row % 3 == 0) { //1st type of row with 2 items
            return photoNum;
        }
        else if (row % 3 == 1) { //2nd type of row with 1 item
            return photoNum + 2;
        }
        else { //last row with 3 items
            return  photoNum + 3;

        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position % 3;
    }

    @Override
    public int getItemCount() {
        int photoCount = photoList.size();
        int cycles = photoCount / 6;
        int remainder = photoCount % 6;
        if (photoCount % 6 == 0) {
            return cycles * 3;
        } else if (remainder == 1 || remainder == 2) {
            return cycles * 3 + 1;
        } else if (remainder == 3) {
            return cycles * 3 + 2;
        } else {
            return cycles * 3 + 3;
        }
    }



    class ViewHolderWrapper extends RecyclerView.ViewHolder  {

        public ViewHolderWrapper(View view) {
            super(view);
        }
    }

    class CustomViewHolder extends ViewHolderWrapper {
        ImageView photo;

        public CustomViewHolder(View view) {
            super(view);
            this.photo = (ImageView) view.findViewById(R.id.image1);
        }
    }

    class CustomViewHolder2 extends ViewHolderWrapper {
        ImageView photo;
        ImageView photo2;

        public CustomViewHolder2(View view) {
            super(view);
            this.photo = (ImageView) view.findViewById(R.id.image2);
            this.photo2 = (ImageView) view.findViewById(R.id.image3);
//            this.photo2.setVisibility(View.VISIBLE);
        }
    }

    class CustomViewHolder3 extends ViewHolderWrapper {
        ImageView photo;
        ImageView photo2;
        ImageView photo3;

        public CustomViewHolder3(View view) {
            super(view);

            this.photo = (ImageView) view.findViewById(R.id.image4);
            this.photo2 = (ImageView) view.findViewById(R.id.image5);
            this.photo3 = (ImageView) view.findViewById(R.id.image6);
//            this.photo2.setVisibility(View.VISIBLE);
//            this.photo3.setVisibility(View.VISIBLE);
        }
    }

    public void updateList(ArrayList<Photo> newList)
    {
        this.photoList = newList;
    }

}
