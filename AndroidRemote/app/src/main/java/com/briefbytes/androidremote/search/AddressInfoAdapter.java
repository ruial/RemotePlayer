package com.briefbytes.androidremote.search;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.briefbytes.androidremote.R;
import com.briefbytes.remoteplayer.client.AddressInfo;

import java.util.ArrayList;
import java.util.List;

public class AddressInfoAdapter extends RecyclerView.Adapter<AddressInfoAdapter.ViewHolder> {

    private List<AddressInfo> addresses;
    private AddressInfoClickListener listener;


    public AddressInfoAdapter(AddressInfoClickListener listener) {
        this.addresses = new ArrayList<>();
        this.listener = listener;
    }

    public void updateAddresses(List<AddressInfo> list) {
        addresses.clear();
        addresses.addAll(list);
        notifyDataSetChanged();
    }

    public void clear() {
        addresses.clear();
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.address_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return addresses.size();
    }


    public interface AddressInfoClickListener {
        void onClick(AddressInfo addressInfo);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textName;
        public TextView textIp;

        public ViewHolder(View itemView) {
            super(itemView);
            textName = (TextView) itemView.findViewById(R.id.address_name);
            textIp = (TextView) itemView.findViewById(R.id.address_ip);
            itemView.setOnClickListener(this);
        }

        public void bind(int position) {
            AddressInfo addressInfo = addresses.get(position);
            textName.setText(addressInfo.getInfo());
            textIp.setText(addressInfo.address());
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            listener.onClick(addresses.get(pos));
        }
    }
}
