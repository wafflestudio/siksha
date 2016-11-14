package com.wafflestudio.siksha;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.wafflestudio.siksha.page.settings.SequenceRecyclerViewAdapter;
import com.wafflestudio.siksha.util.Fonts;
import com.wafflestudio.siksha.util.Preference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SequenceActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView messageView;
    private RecyclerView recyclerView;
    private FloatingActionButton actionButton;

    private SequenceRecyclerViewAdapter adapter;

    private boolean isAboutBookmark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sequence);

        isAboutBookmark = getIntent().getBooleanExtra("about_bookmark", false);

        if (isAboutBookmark)
            AnalyticsTrackers.getInstance().trackScreenView("BookmarkSequenceActivity");
        else
            AnalyticsTrackers.getInstance().trackScreenView("SequenceActivity");

        messageView = (TextView) findViewById(R.id.activity_sequence_message_view);
        recyclerView = (RecyclerView) findViewById(R.id.activity_sequence_recycler_view);
        actionButton = (FloatingActionButton) findViewById(R.id.activity_sequence_floating_action_button);

        messageView.setTypeface(Fonts.fontAPAritaDotumMedium);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final ItemTouchHelper itemTouchHelper = getItemHelper();
        itemTouchHelper.attachToRecyclerView(recyclerView);

        adapter = new SequenceRecyclerViewAdapter(getCurrentSequence(), new SequenceRecyclerViewAdapter.OnStartDragListener() {
            @Override
            public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
                itemTouchHelper.startDrag(viewHolder);
            }
        });
        recyclerView.setAdapter(adapter);

        actionButton.setOnClickListener(this);
    }

    private ItemTouchHelper getItemHelper() {
        return new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                adapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return false;
            }

            @Override
            public boolean isItemViewSwipeEnabled() { // make swipe disabled
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) { }
        });
    }

    private List<String> getCurrentSequence() {
        List<String> list = new ArrayList<String>();
        String[] restaurants;

        if (!isAboutBookmark) {
            restaurants = Preference.loadStringValue(this, Preference.PREF_APP_NAME, Preference.PREF_KEY_CURRENT_SEQUENCE).split("/");
        } else {
            restaurants = Preference.loadStringValue(this, Preference.PREF_APP_NAME, Preference.PREF_KEY_BOOKMARKS).split("/");
        }
        Collections.addAll(list, restaurants);

        return list;
    }

    private void updateCurrentSequence() {
        StringBuilder stringBuilder = new StringBuilder();
        List<String> currentSequence = adapter.getData();

        for (int i = 0; i < currentSequence.size(); i++) {
            if (i == 0) {
                stringBuilder.append(currentSequence.get(i));
            } else {
                stringBuilder.append("/").append(currentSequence.get(i));
            }
        }

        if (!isAboutBookmark) {
            Preference.save(this, Preference.PREF_APP_NAME, Preference.PREF_KEY_CURRENT_SEQUENCE, stringBuilder.toString());
            Log.d(Preference.PREF_KEY_CURRENT_SEQUENCE, stringBuilder.toString());
        } else {
            Preference.save(this, Preference.PREF_APP_NAME, Preference.PREF_KEY_BOOKMARKS, stringBuilder.toString());
            Log.d(Preference.PREF_KEY_BOOKMARKS, stringBuilder.toString());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_sequence_floating_action_button:

                updateCurrentSequence();
                messageView.setText("저장되었습니다.");

//                if (isEditMode) {
//                    actionButton.setImageResource(R.drawable.ic_confirm);
//                    messageView.setText(R.string.message_to_touch_confirm_button);
//                    ObjectAnimator fadeInAnimator = Animations.makeFadeAnimator(recyclerView, 0.25f, 1.0f, 300);
//                    fadeInAnimator.setInterpolator(new AccelerateInterpolator());
//                    fadeInAnimator.start();
//                } else {
//                    updateCurrentSequence();
//                    actionButton.setImageResource(R.drawable.ic_edit);
//                    messageView.setText(R.string.message_to_touch_edit_button);
//                    ObjectAnimator fadeOutAnimator = Animations.makeFadeAnimator(recyclerView, 1.0f, 0.25f, 300);
//                    fadeOutAnimator.setInterpolator(new AccelerateInterpolator());
//                    fadeOutAnimator.start();
//                }
                break;
        }
    }
}
