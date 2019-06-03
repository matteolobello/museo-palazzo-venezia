package it.matteolobello.palazzovenezia.ui.adapter.recyclerview;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.matteolobello.palazzovenezia.R;
import it.matteolobello.palazzovenezia.data.asset.AssetImageSetter;
import it.matteolobello.palazzovenezia.data.bundle.BundleKeys;
import it.matteolobello.palazzovenezia.data.model.Painting;
import it.matteolobello.palazzovenezia.ui.activity.PaintingActivity;
import com.yayandroid.parallaxrecyclerview.ParallaxViewHolder;

import java.util.ArrayList;

public class SearchRecyclerViewAdapter extends RecyclerView.Adapter<SearchRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<Painting> mPaintingArrayList = new ArrayList<>();
    private final ArrayList<Painting> mAllPaintings;

    public SearchRecyclerViewAdapter(ArrayList<Painting> allPaintings) {
        mAllPaintings = allPaintings;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search_painting_rv, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Painting painting = mPaintingArrayList.get(position);
        final ImageView paintingImageView = holder.getPaintingImageView();
        final TextView paintingTextView = holder.getPaintingTextView();
        final Activity activity = ((Activity) paintingImageView.getContext());

        paintingImageView.setColorFilter(Color.rgb(123, 123, 123), PorterDuff.Mode.MULTIPLY);
        AssetImageSetter.setImageByPaintingId(paintingImageView, painting.getId());

        paintingTextView.setText(painting.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    public int searchPainting(String query) {
        mPaintingArrayList.clear();

        if (TextUtils.isEmpty(query)) {
            notifyDataSetChanged();

            return 0;
        }

        ArrayList<Painting> paintingsFound = new ArrayList<>();

        for (Painting painting : mAllPaintings) {
            if (painting.getName().toLowerCase().contains(query.toLowerCase())) {
                paintingsFound.add(painting);
            }
        }

        mPaintingArrayList.addAll(paintingsFound);

        notifyDataSetChanged();

        return paintingsFound.size();
    }

    class ViewHolder extends ParallaxViewHolder {

        private ImageView mPaintingImageView;
        private TextView mPaintingTitle;

        ViewHolder(View itemView) {
            super(itemView);

            mPaintingImageView = itemView.findViewById(R.id.painting_image_view);
            mPaintingTitle = itemView.findViewById(R.id.painting_title);
        }

        @Override
        public int getParallaxImageId() {
            return R.id.painting_image_view;
        }

        ImageView getPaintingImageView() {
            return mPaintingImageView;
        }

        TextView getPaintingTextView() {
            return mPaintingTitle;
        }
    }
}