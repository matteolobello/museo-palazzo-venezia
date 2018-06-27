package com.matteolobello.palazzovenezia.ui.fragment.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.matteolobello.palazzovenezia.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AboutFragment extends Fragment implements View.OnClickListener {

    private static final String MATTEO_LINK = "https://www.matteolobello.com";
    private static final String ANDREA_LINK = "https://www.instagram.com/alpaca_furioso";
    private static final String ALESSANDRO_LINK = "https://www.facebook.com/alessandro.currenti.1";
    private static final String FEDERICO_LINK = "https://www.facebook.com/federico.fiorio.9";
    private static final String ROLANDO_LINK = "https://www.facebook.com/RoyanOfficial";
    private static final String LEONARDO_LINK = "https://www.instagram.com/leolance";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.matteo_wrapper).setOnClickListener(this);
        view.findViewById(R.id.andrea_wrapper).setOnClickListener(this);
        view.findViewById(R.id.alessandro_wrapper).setOnClickListener(this);
        view.findViewById(R.id.federico_wrapper).setOnClickListener(this);
        view.findViewById(R.id.rolando_wrapper).setOnClickListener(this);
        view.findViewById(R.id.leonardo_wrapper).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String url = null;

        switch (view.getId()) {
            case R.id.matteo_wrapper:
                url = MATTEO_LINK;
                break;
            case R.id.andrea_wrapper:
                url = ANDREA_LINK;
                break;
            case R.id.alessandro_wrapper:
                url = ALESSANDRO_LINK;
                break;
            case R.id.federico_wrapper:
                url = FEDERICO_LINK;
                break;
            case R.id.rolando_wrapper:
                url = ROLANDO_LINK;
                break;
            case R.id.leonardo_wrapper:
                url = LEONARDO_LINK;
                break;
        }

        if (url != null) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        }
    }
}
