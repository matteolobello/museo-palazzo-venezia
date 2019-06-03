package it.matteolobello.palazzovenezia.data.asset;

import android.content.Context;
import android.widget.ImageView;

public class AssetImageSetter {

    public static void setImageByPaintingId(ImageView imageView, String id) {
        imageView.setImageResource(getImageResByName(imageView.getContext(), "img_" + id));
    }

    public static int getImageResByName(Context context, String name) {
        return context.getResources().getIdentifier(name, "drawable", context.getPackageName());
    }
}
