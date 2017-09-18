package com.matteolobello.palazzovenezia.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.florent37.materialimageloading.MaterialImageLoading;
import com.matteolobello.palazzovenezia.R;
import com.matteolobello.palazzovenezia.data.asset.AssetImageSetter;
import com.matteolobello.palazzovenezia.data.model.Painting;
import com.matteolobello.palazzovenezia.ui.activity.PaintingActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PaintingsRecyclerViewAdapter extends RecyclerView.Adapter<PaintingsRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<Painting> mPaintingArrayList;

    public PaintingsRecyclerViewAdapter(ArrayList<Painting> paintingArrayList) {
        mPaintingArrayList = paintingArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_painting_rv, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Painting painting = mPaintingArrayList.get(position);
        final ImageView paintingImageView = holder.getPaintingImageView();
        final Activity activity = ((Activity) paintingImageView.getContext());

        AssetImageSetter.setImageByPaintingId(paintingImageView, painting.getId());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            paintingImageView.setTransitionName("painting");
        }

        MaterialImageLoading.animate(paintingImageView).setDuration(position * 300).start();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(activity, PaintingActivity.class);
                intent.putExtra("painting", painting);
                ActivityOptionsCompat options = ActivityOptionsCompat
                        .makeSceneTransitionAnimation(activity, paintingImageView, "painting");
                activity.startActivity(intent, options.toBundle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPaintingArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.painting_image_view) ImageView mPaintingImageView;

        ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        ImageView getPaintingImageView() {
            return mPaintingImageView;
        }
    }
}