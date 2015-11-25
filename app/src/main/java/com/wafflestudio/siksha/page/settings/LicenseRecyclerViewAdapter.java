package com.wafflestudio.siksha.page.settings;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wafflestudio.siksha.R;
import com.wafflestudio.siksha.util.Fonts;

public class LicenseRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private String[] libraries;
    private String[] links;
    private String[] licenses;

    public LicenseRecyclerViewAdapter(String[] libraries, String[] links, String[] licenses) {
        this.libraries = libraries;
        this.links = links;
        this.licenses = licenses;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.license_item, parent, false);
        return new LicenseItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        LicenseItemViewHolder licenseItemViewHolder = (LicenseItemViewHolder) viewHolder;
        licenseItemViewHolder.nameView.setText(libraries[position]);
        licenseItemViewHolder.linkView.setText(Html.fromHtml("<a href=\"" + links[position] + "\">" + links[position] + "</a>"));
        licenseItemViewHolder.messageView.setText(licenses[position]);
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    private class LicenseItemViewHolder extends RecyclerView.ViewHolder {
        private TextView nameView;
        private TextView linkView;
        private TextView messageView;

        public LicenseItemViewHolder(View itemView) {
            super(itemView);
            nameView = (TextView) itemView.findViewById(R.id.license_item_name_view);
            linkView = (TextView) itemView.findViewById(R.id.license_item_link_view);
            messageView = (TextView) itemView.findViewById(R.id.license_item_message_view);
            nameView.setTypeface(Fonts.fontAPAritaDotumSemiBold);
            linkView.setTypeface(Fonts.fontAPAritaDotumMedium);
            messageView.setTypeface(Fonts.fontAPAritaDotumMedium);
        }
    }
}
