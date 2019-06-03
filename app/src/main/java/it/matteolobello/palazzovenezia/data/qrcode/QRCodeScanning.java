package it.matteolobello.palazzovenezia.data.qrcode;

import android.content.Context;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParsePosition;

public class QRCodeScanning {

    public static boolean isNumeric(String string) {
        NumberFormat numberFormat = NumberFormat.getInstance();
        ParsePosition parsePosition = new ParsePosition(0);
        numberFormat.parse(string, parsePosition);
        return string.length() == parsePosition.getIndex();
    }

    public static boolean isQRCodeValid(Context context, String id) {
        if (!QRCodeScanning.isNumeric(id)) {
            return false;
        }

        try {
            String[] ids = context.getAssets().list("");

            for (int i = ids.length - 1; i >= 0; i--) {
                if (QRCodeScanning.isNumeric(ids[i])) {
                    if (Integer.valueOf(id) > Integer.valueOf(ids[i])) {
                        return false;
                    }

                    break;
                }
            }

            for (String idInAsset : ids) {
                if (QRCodeScanning.isNumeric(idInAsset)) {
                    if (Integer.valueOf(id) < Integer.valueOf(idInAsset)) {
                        return false;
                    }

                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();

            return false;
        }

        return true;
    }
}
