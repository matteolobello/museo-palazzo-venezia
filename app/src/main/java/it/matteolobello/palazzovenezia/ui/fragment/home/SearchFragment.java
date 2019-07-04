package it.matteolobello.palazzovenezia.ui.fragment.home;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Field;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;
import it.matteolobello.palazzovenezia.R;
import it.matteolobello.palazzovenezia.data.asset.AssetPaintingsGenerator;
import it.matteolobello.palazzovenezia.ui.activity.HomeActivity;
import it.matteolobello.palazzovenezia.ui.adapter.recyclerview.SearchRecyclerViewAdapter;
import it.matteolobello.palazzovenezia.util.DpPxUtil;
import it.matteolobello.palazzovenezia.util.SystemBarsUtil;

public class SearchFragment extends Fragment {

    private BlurView mSearchWrapper;
    private EditText mSearchEditText;
    private ImageView mSearchClearImageView;
    private NestedScrollView mOutputScrollView;
    private RecyclerView mSearchRecyclerView;
    private View mEmptyResultView;

    private SearchRecyclerViewAdapter mSearchAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSearchWrapper = view.findViewById(R.id.search_blur_view);
        mSearchEditText = view.findViewById(R.id.search_view);
        mSearchClearImageView = view.findViewById(R.id.search_clear);
        mOutputScrollView = view.findViewById(R.id.search_output_nested_scroll_view);
        mSearchRecyclerView = view.findViewById(R.id.search_rv);
        mEmptyResultView = view.findViewById(R.id.empty_result_wrapper);

        setupSearchView();
        setupRecyclerView();

        ((LinearLayout.LayoutParams) view.findViewById(R.id.scroll_view_padding_bottom_view).getLayoutParams()).height =
                SystemBarsUtil.getNavigationBarHeight(getContext())
                        + DpPxUtil.convertDpToPixel(56);

        ((LinearLayout.LayoutParams) view.findViewById(R.id.scroll_view_padding_top_view).getLayoutParams()).height =
                SystemBarsUtil.getStatusBarHeight(getContext())
                        + DpPxUtil.convertDpToPixel(56);

        Drawable windowBackground = view.getBackground();
        mSearchWrapper.setPadding(0, SystemBarsUtil.getStatusBarHeight(getContext()), 0, 0);
        mSearchWrapper.getLayoutParams().height = DpPxUtil.convertDpToPixel(56) + SystemBarsUtil.getStatusBarHeight(getContext());
        mSearchWrapper.setupWith((ViewGroup) view)
                .setFrameClearDrawable(windowBackground)
                .setBlurAlgorithm(new RenderScriptBlur(getContext()))
                .setBlurRadius(20f)
                .setOverlayColor(HomeActivity.BLUR_OVERLAY_COLOR)
                .setHasFixedTransformationMatrix(true);

    }

    private void setupSearchView() {
        try {
            // Set cursor colour to white
            // http://stackoverflow.com/a/26544231/1692770
            // https://github.com/android/platform_frameworks_base/blob/kitkat-release/core/java/android/widget/TextView.java#L562-564
            Field field = TextView.class.getDeclaredField("mCursorDrawableRes");
            field.setAccessible(true);
            field.set(mSearchEditText, R.drawable.black_cursor);
        } catch (Exception ignored) {
        }

        mSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(final CharSequence query, int start, int before, int count) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (query.equals(mSearchEditText.getText())) {
                            if (mSearchAdapter.searchPainting(query.toString()) > 0) {
                                mOutputScrollView.setVisibility(View.VISIBLE);
                                mEmptyResultView.setVisibility(View.GONE);
                            } else {
                                mOutputScrollView.setVisibility(View.GONE);
                                mEmptyResultView.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }, 400);

                mSearchClearImageView.animate()
                        .alpha(TextUtils.isEmpty(query) ? 0 : 1)
                        .setDuration(200)
                        .start();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mSearchClearImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchEditText.setText("");

                mOutputScrollView.setVisibility(View.GONE);
                mEmptyResultView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setupRecyclerView() {
        mSearchRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mSearchAdapter = new SearchRecyclerViewAdapter(AssetPaintingsGenerator.generatePaintings(getContext()));
        mSearchRecyclerView.setAdapter(mSearchAdapter);
    }
}
