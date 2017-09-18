package com.matteolobello.palazzovenezia.data.asset;

import android.content.Context;
import android.content.res.AssetManager;
import android.widget.Toast;

import com.matteolobello.palazzovenezia.data.model.Painting;
import com.matteolobello.palazzovenezia.data.preference.PreferenceHandler;
import com.matteolobello.palazzovenezia.data.qrcode.QRCodeScanning;

import java.io.File;
import java.util.ArrayList;

public class AssetPaintingsGenerator {

    public static ArrayList<Painting> generatePaintings(Context context) {
        ArrayList<Painting> paintingArrayList = new ArrayList<>();
        try {
            AssetManager assets = context.getAssets();
            String[] ids = assets.list("");

            String language = PreferenceHandler.get().getValue(context.getApplicationContext(), PreferenceHandler.LANGUAGE_KEY);
            if (language == null) {
                language = "en";
                PreferenceHandler.get().setValue(context.getApplicationContext(), PreferenceHandler.LANGUAGE_KEY, "en");
            }

            for (String id : ids) {
                if (!QRCodeScanning.isNumeric(id)) {
                    continue;
                }

                Painting painting = new Painting();

                painting.setId(id);

                String descriptionFilePath = assets.list(id + "/" + "txt" + "/" + language)[0];
                File descriptionFile = new File(descriptionFilePath);
                String description = AssetFileReader.readAssetsFile(context.getApplicationContext(),
                        id + "/" + "txt" + "/" + language + "/" + descriptionFilePath);

                painting.setName(descriptionFile.getName().replace("_", " "));
                painting.setDescription(description);

                String audioFileName = assets.list(id + "/" + "mp3" + "/" + language)[0];
                painting.setAudioPath(id + "/" + "mp3" + "/" + language + "/" + audioFileName);
                
                paintingArrayList.add(painting);
            }
        } catch (Exception e) {
            Toast.makeText(context, "Error: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }

        return paintingArrayList;
    }
}
