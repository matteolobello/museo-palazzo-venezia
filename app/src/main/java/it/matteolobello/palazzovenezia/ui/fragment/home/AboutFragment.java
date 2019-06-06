package it.matteolobello.palazzovenezia.ui.fragment.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import it.matteolobello.palazzovenezia.R;

public class AboutFragment extends Fragment implements View.OnClickListener {

    private static final String MATTEO_LOBELLO_LINK = "https://www.matteolobello.it";
    private static final String ANDREA_CAROSIO_LINK = "https://www.instagram.com/alpaca_furioso";
    private static final String ALESSANDRO_CURRENTI_LINK = "https://www.instagram.com/alessahndro";
    private static final String FEDERICO_FIORIO_LINK = "https://www.federicofiorio.it";
    private static final String ROLANDO_GONZALEZ_LINK = "https://www.royan.altervista.org";
    private static final String LEONARDO_LANCELLOTTI_LINK = "https://www.instagram.com/leolance";
    private static final String BENEDETTA_SERRA_LINK = "https://www.instagram.com/_benniserra";
    private static final String SOFIA_MARICA_LINK = "https://www.instagram.com/sofiamarica_";
    private static final String MARTINA_CADANG_LINK = "https://instagram.com/m.vrtina";
    private static final String SILVIA_DI_MARTINO_LINK = "https://www.instagram.com/skepsi__";
    private static final String SUSANNA_PELLE_LINK = "https://www.instagram.com/_susannapelle";
    private static final String FEDERICO_PROSPERI_LINK = "https://www.instagram.com/federico.prosperi";
    private static final String ELISA_PATTUGLIA_LINK = "https://www.instagram.com/elysdiary";
    private static final String ALESSIA_LUONGO_LINK = "https://www.instagram.com/luongo_alessia";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.matteo_lobello_wrapper).setOnClickListener(this);
        view.findViewById(R.id.andrea_carosio_wrapper).setOnClickListener(this);
        view.findViewById(R.id.alessandro_currenti_wrapper).setOnClickListener(this);
        view.findViewById(R.id.federico_fiorio_wrapper).setOnClickListener(this);
        view.findViewById(R.id.rolando_gonzalez_wrapper).setOnClickListener(this);
        view.findViewById(R.id.leonardo_lancellotti_wrapper).setOnClickListener(this);
        view.findViewById(R.id.benedetta_serra_wrapper).setOnClickListener(this);
        view.findViewById(R.id.sofia_marica_wrapper).setOnClickListener(this);
        view.findViewById(R.id.martina_cadang_wrapper).setOnClickListener(this);
        view.findViewById(R.id.silvia_di_martino_wrapper).setOnClickListener(this);
        view.findViewById(R.id.susanna_pelle_wrapper).setOnClickListener(this);
        view.findViewById(R.id.federico_prosperi_wrapper).setOnClickListener(this);
        view.findViewById(R.id.elisa_pattuglia_wrapper).setOnClickListener(this);
        view.findViewById(R.id.alessia_luongo_wrapper).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String url = null;

        switch (view.getId()) {
            case R.id.matteo_lobello_wrapper:
                url = MATTEO_LOBELLO_LINK;
                break;
            case R.id.andrea_carosio_wrapper:
                url = ANDREA_CAROSIO_LINK;
                break;
            case R.id.alessandro_currenti_wrapper:
                url = ALESSANDRO_CURRENTI_LINK;
                break;
            case R.id.federico_fiorio_wrapper:
                url = FEDERICO_FIORIO_LINK;
                break;
            case R.id.rolando_gonzalez_wrapper:
                url = ROLANDO_GONZALEZ_LINK;
                break;
            case R.id.leonardo_lancellotti_wrapper:
                url = LEONARDO_LANCELLOTTI_LINK;
                break;
            case R.id.benedetta_serra_wrapper:
                url = BENEDETTA_SERRA_LINK;
                break;
            case R.id.sofia_marica_wrapper:
                url = SOFIA_MARICA_LINK;
                break;
            case R.id.martina_cadang_wrapper:
                url = MARTINA_CADANG_LINK;
                break;
            case R.id.silvia_di_martino_wrapper:
                url = SILVIA_DI_MARTINO_LINK;
                break;
            case R.id.susanna_pelle_wrapper:
                url = SUSANNA_PELLE_LINK;
                break;
            case R.id.federico_prosperi_wrapper:
                url = FEDERICO_PROSPERI_LINK;
                break;
            case R.id.elisa_pattuglia_wrapper:
                url = ELISA_PATTUGLIA_LINK;
                break;
            case R.id.alessia_luongo_wrapper:
                url = ALESSIA_LUONGO_LINK;
                break;
        }

        if (url != null) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        }
    }
}
