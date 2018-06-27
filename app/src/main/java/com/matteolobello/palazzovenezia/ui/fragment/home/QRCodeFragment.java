package com.matteolobello.palazzovenezia.ui.fragment.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.zxing.Result;
import com.matteolobello.palazzovenezia.R;
import com.matteolobello.palazzovenezia.data.asset.AssetFileReader;
import com.matteolobello.palazzovenezia.data.bundle.BundleKeys;
import com.matteolobello.palazzovenezia.data.model.Painting;
import com.matteolobello.palazzovenezia.data.qrcode.QRCodeScanning;
import com.matteolobello.palazzovenezia.ui.activity.PaintingActivity;

import java.io.File;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QRCodeFragment extends Fragment implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_qrcode_scan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mScannerView = view.findViewById(R.id.qr_decoder_view);
    }

    public void startCamera() {
        if (mScannerView != null) {
            mScannerView.setResultHandler(this);
            mScannerView.startCamera();
        }
    }

    public void stopCamera() {
        if (mScannerView != null) {
            mScannerView.stopCamera();
        }
    }

    @Override
    public void handleResult(Result result) {
        String id = result.getText();

        if (!QRCodeScanning.isQRCodeValid(getContext(), id)) {
            Toast.makeText(getActivity(), R.string.qr_code_not_valid, Toast.LENGTH_SHORT).show();

            return;
        }

        stopCamera();

        Painting painting = new Painting();

        try {
            String language = Locale.getDefault().getLanguage();

            painting.setId(id);

            String descriptionFilePath = getContext().getAssets().list(id + "/" + "txt" + "/" + language)[0];
            File descriptionFile = new File(descriptionFilePath);
            String description = AssetFileReader.readAssetsFile(getContext(),
                    id + "/" + "txt" + "/" + language + "/" + descriptionFilePath);

            painting.setName(descriptionFile.getName().replace("_", " "));
            painting.setDescription(description);

            String audioFileName = getContext().getAssets().list(id + "/" + "mp3" + "/" + language)[0];
            painting.setAudioPath(id + "/" + "mp3" + "/" + language + "/" + audioFileName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(getContext(), PaintingActivity.class);
        intent.putExtra(BundleKeys.EXTRA_PAINTING, painting);
        startActivity(intent);
    }
}
