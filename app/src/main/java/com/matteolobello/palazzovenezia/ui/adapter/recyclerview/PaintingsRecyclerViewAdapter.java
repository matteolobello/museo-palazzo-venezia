package com.matteolobello.palazzovenezia.ui.adapter.recyclerview;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.florent37.materialimageloading.MaterialImageLoading;
import com.matteolobello.palazzovenezia.R;
import com.matteolobello.palazzovenezia.data.asset.AssetImageSetter;
import com.matteolobello.palazzovenezia.data.bundle.BundleKeys;
import com.matteolobello.palazzovenezia.data.model.Painting;
import com.matteolobello.palazzovenezia.ui.activity.PaintingActivity;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PaintingsRecyclerViewAdapter extends RecyclerView.Adapter<PaintingsRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<Painting> mPaintingArrayList;

    public PaintingsRecyclerViewAdapter(ArrayList<Painting> paintingArrayList) {
        mPaintingArrayList = paintingArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_painting_rv, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Painting painting = mPaintingArrayList.get(position);
        final ImageView paintingImageView = holder.getPaintingImageView();
        final Activity activity = ((Activity) paintingImageView.getContext());

        AssetImageSetter.setImageByPaintingId(paintingImageView, painting.getId());

        MaterialImageLoading.animate(paintingImageView).setDuration(position * 300).start();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, PaintingActivity.class);
                intent.putExtra(BundleKeys.EXTRA_PAINTING, painting);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPaintingArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView mPaintingImageView;

        ViewHolder(View itemView) {
            super(itemView);

            mPaintingImageView = itemView.findViewById(R.id.painting_image_view);
        }

        ImageView getPaintingImageView() {
            return mPaintingImageView;
        }
    }
}