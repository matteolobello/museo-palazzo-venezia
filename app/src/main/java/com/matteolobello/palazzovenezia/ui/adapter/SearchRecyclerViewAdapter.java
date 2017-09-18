package com.matteolobello.palazzovenezia.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.matteolobello.palazzovenezia.R;
import com.matteolobello.palazzovenezia.data.asset.AssetImageSetter;
import com.matteolobello.palazzovenezia.data.model.Painting;
import com.matteolobello.palazzovenezia.ui.activity.PaintingActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchRecyclerViewAdapter extends RecyclerView.Adapter<SearchRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<Painting> mPaintingArrayList = new ArrayList<>();
    private final ArrayList<Painting> mAllPaintings;

    public SearchRecyclerViewAdapter(ArrayList<Painting> allPaintings) {
        mAllPaintings = allPaintings;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search_painting_rv, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Painting painting = mPaintingArrayList.get(position);
        final ImageView paintingImageView = holder.getPaintingImageView();
        final TextView paintingTextView = holder.getPaintingTextView();
        final Activity activity = ((Activity) paintingImageView.getContext());

        paintingImageView.setColorFilter(Color.rgb(123, 123, 123), PorterDuff.Mode.MULTIPLY);
        AssetImageSetter.setImageByPaintingId(paintingImageView, painting.getId());

        paintingTextView.setText(painting.getName().replace("_", " "));

        paintingImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    public void searchPainting(String query) {
        mPaintingArrayList.clear();

        if (TextUtils.isEmpty(query)) {
            notifyDataSetChanged();

            return;
        }

        ArrayList<Painting> paintingsFound = new ArrayList<>();

        for (Painting painting : mAllPaintings) {
            if (painting.getName().toLowerCase().contains(query.toLowerCase())) {
                paintingsFound.add(painting);
            }
        }

        mPaintingArrayList.addAll(paintingsFound);

        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.painting_image_view) protected ImageView mPaintingImageView;
        @BindView(R.id.painting_title)      protected TextView mPaintingTitle;

        ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        ImageView getPaintingImageView() {
            return mPaintingImageView;
        }

        TextView getPaintingTextView() {
            return mPaintingTitle;
        }
    }
}