package it.matteolobello.palazzovenezia.data.asset;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class AssetFileReader {

    private static final String CHARSET_NAME = "UTF-8";

    public static String readAssetsFile(Context context, String fileName) {
        try {
            BufferedReader lineReader = new BufferedReader(new InputStreamReader(
                    context.getAssets().open(fileName), Charset.forName(CHARSET_NAME)));

            StringBuilder returnValue = new StringBuilder();
            String line;
            try {
                while ((line = lineReader.readLine()) != null) {
                    returnValue.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return returnValue.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
