package com.wafflestudio.siksha.page.settings;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.wafflestudio.siksha.AnalyticsTrackers;
import com.wafflestudio.siksha.AppVersionActivity;
import com.wafflestudio.siksha.DeveloperActivity;
import com.wafflestudio.siksha.LicenseActivity;
import com.wafflestudio.siksha.MainActivity;
import com.wafflestudio.siksha.R;
import com.wafflestudio.siksha.SequenceActivity;
import com.wafflestudio.siksha.service.JSONDownloadReceiver;
import com.wafflestudio.siksha.util.NetworkChecker;
import com.wafflestudio.siksha.util.Fonts;
import com.wafflestudio.siksha.util.Preference;

public class SettingsRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_EMPTY = 1;
    private static final int TYPE_LABEL = 2;
    private static final int TYPE_SWITCH = 3;
    private static final int TYPE_NORMAL = 4;

    private Context context;

    public SettingsRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        private TextView headerView;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            headerView = (TextView) itemView.findViewById(R.id.header_view);
            headerView.setTypeface(Fonts.fontBMJua);
        }
    }

    private class EmptyViewHolder extends RecyclerView.ViewHolder {
        public EmptyViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class LabelTypeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView optionView;
        private TextView label;

        public LabelTypeViewHolder(View itemView) {
            super(itemView);
            optionView = (TextView) itemView.findViewById(R.id.label_type_option_view);
            label = (TextView) itemView.findViewById(R.id.option_label);
            optionView.setTypeface(Fonts.fontBMJua);
            label.setTypeface(Fonts.fontAPAritaDotumMedium);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (getAdapterPosition()) {
                case 1:
                    context.startActivity(new Intent(context, AppVersionActivity.class));
                    break;
                case 2:
                    if (NetworkChecker.isOnline(context))
                        ((MainActivity) context).downloadMenuData(JSONDownloadReceiver.ACTION_MENU_REFRESH, false);
                    else
                        Toast.makeText(context, R.string.check_network_state, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    private class SwitchTypeViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener {
        private TextView optionView;
        private SwitchCompat optionSwitch;

        public SwitchTypeViewHolder(View itemView) {
            super(itemView);
            optionView = (TextView) itemView.findViewById(R.id.switch_type_option_view);
            optionSwitch = (SwitchCompat) itemView.findViewById(R.id.option_switch);
            optionView.setTypeface(Fonts.fontBMJua);

            optionSwitch.setOnCheckedChangeListener(this);
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean checked) {
            if (checked) {
                Preference.save(context, Preference.PREF_APP_NAME, Preference.PREF_KEY_EMPTY_MENU_INVISIBLE, true);
                AnalyticsTrackers.getInstance().trackEvent("Settings", "메뉴 없는 식당 안 보기", "true");
            }
            else {
                Preference.save(context, Preference.PREF_APP_NAME, Preference.PREF_KEY_EMPTY_MENU_INVISIBLE, false);
                AnalyticsTrackers.getInstance().trackEvent("Settings", "메뉴 없는 식당 안 보기", "false");
            }
        }
    }

    private class NormalTypeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView optionView;

        public NormalTypeViewHolder(View itemView) {
            super(itemView);
            optionView = (TextView) itemView.findViewById(R.id.normal_type_option_view);
            optionView.setTypeface(Fonts.fontBMJua);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (getAdapterPosition()) {
                case 5:
                    if (Preference.loadStringValue(context, Preference.PREF_APP_NAME, Preference.PREF_KEY_BOOKMARKS).equals(""))
                        Toast.makeText(context, R.string.empty_bookmark, Toast.LENGTH_SHORT).show();
                    else {
                        Intent intent = new Intent(context, SequenceActivity.class);
                        intent.putExtra("about_bookmark", true);
                        context.startActivity(intent);
                    }
                    break;
                case 8:
                    context.startActivity(new Intent(context, SequenceActivity.class));
                    break;
                case 11:
                    context.startActivity(new Intent(context, LicenseActivity.class));
                    break;
                case 12:
                    context.startActivity(new Intent(context, DeveloperActivity.class));
                    break;
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewtype) {
        if (viewtype == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_view, parent, false);
            return new HeaderViewHolder(view);
        } else if (viewtype == TYPE_EMPTY) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_view, parent, false);
            return new EmptyViewHolder(view);
        } else if (viewtype == TYPE_LABEL) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.label_type_view, parent, false);
            return new LabelTypeViewHolder(view);
        } else if (viewtype == TYPE_SWITCH) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.switch_type_view, parent, false);
            return new SwitchTypeViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.normal_type_view, parent, false);
            return new NormalTypeViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return TYPE_EMPTY;
            case 1:
                return TYPE_LABEL;
            case 2:
                return TYPE_LABEL;
            case 3:
                return TYPE_EMPTY;
            case 4:
                return TYPE_HEADER;
            case 5:
                return TYPE_NORMAL;
            case 6:
                return TYPE_EMPTY;
            case 7:
                return TYPE_HEADER;
            case 8:
                return TYPE_NORMAL;
            case 9:
                return TYPE_SWITCH;
            case 10:
                return TYPE_EMPTY;
            case 11:
                return TYPE_NORMAL;
            case 12:
                return TYPE_NORMAL;
            default:
                return TYPE_NORMAL;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case TYPE_HEADER:
                HeaderViewHolder headerViewHolder = (HeaderViewHolder) viewHolder;

                if (position == 4) {
                    headerViewHolder.headerView.setText(R.string.settings_option_bookmark);
                } else if (position == 7) {
                    headerViewHolder.headerView.setText(R.string.settings_option_menu);
                }
                break;
            case TYPE_LABEL:
                LabelTypeViewHolder labelTypeViewHolder = (LabelTypeViewHolder) viewHolder;

                if (position == 1) {
                    labelTypeViewHolder.optionView.setText(R.string.settings_option_app_version);

                    String currentAppVersion = Preference.loadStringValue(context, Preference.PREF_APP_NAME, Preference.PREF_KEY_CURRENT_APP_VERSION);
                    String latestAppVersion = Preference.loadStringValue(context, Preference.PREF_APP_NAME, Preference.PREF_KEY_LATEST_APP_VERSION);

                    if (currentAppVersion.compareTo(latestAppVersion) > 0)
                        labelTypeViewHolder.label.setText(R.string.development_version_short);
                    else if (currentAppVersion.compareTo(latestAppVersion) == 0)
                        labelTypeViewHolder.label.setText(R.string.now_latest_short);
                    else if (currentAppVersion.compareTo(latestAppVersion) < 0)
                        labelTypeViewHolder.label.setText(R.string.not_latest_short);
                } else if (position == 2) {
                    labelTypeViewHolder.optionView.setText(R.string.settings_option_refresh);

                    String timestamp = Preference.loadStringValue(context, Preference.PREF_APP_NAME, Preference.PREF_KEY_REFRESH_TIMESTAMP);
                    labelTypeViewHolder.label.setText(timestamp);
                }
                break;
            case TYPE_SWITCH:
                SwitchTypeViewHolder switchTypeViewHolder = (SwitchTypeViewHolder) viewHolder;

                if (position == 9) {
                    switchTypeViewHolder.optionView.setText(R.string.settings_option_empty_menu_invisible);
                    switchTypeViewHolder.optionSwitch.setChecked(Preference.loadBooleanValue(context, Preference.PREF_APP_NAME, Preference.PREF_KEY_EMPTY_MENU_INVISIBLE));
                }
                break;
            case TYPE_NORMAL:
                NormalTypeViewHolder normalTypeViewHolder = (NormalTypeViewHolder) viewHolder;

                if (position == 5 || position == 8) {
                    normalTypeViewHolder.optionView.setText(R.string.settings_option_sequence);
                } else if (position == 11) {
                    normalTypeViewHolder.optionView.setText(R.string.settings_option_open_source_library);
                } else if (position == 12) {
                    normalTypeViewHolder.optionView.setText(R.string.settings_option_developer);
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 13;
    }
}
