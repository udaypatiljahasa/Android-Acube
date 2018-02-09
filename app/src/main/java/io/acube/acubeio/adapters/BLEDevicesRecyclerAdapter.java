/**
 * Jahasa Technology all rights reserved
 *
 * @author Uday
 * @since 01.02.2018
 * <p>
 * BlE Device Recycler view adapter class.
 */



package io.acube.acubeio.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.polidea.rxandroidble.scan.ScanResult;

import java.util.ArrayList;

import io.acube.acubeio.R;

public class BLEDevicesRecyclerAdapter extends RecyclerView.Adapter<BLEDevicesRecyclerAdapter.BLEDeviceViewHolder> {

    private ArrayList<ScanResult> leDevices;

    private RecyclerViewClickListener recyclerViewClickListener;

    public interface RecyclerViewClickListener {

        void onClick(ScanResult scanResult);
    }

    public BLEDevicesRecyclerAdapter(RecyclerViewClickListener listener) {
        super();
        leDevices = new ArrayList<ScanResult>();
        recyclerViewClickListener = listener;
    }
    public class BLEDeviceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        TextView deviceName;
        TextView deviceAddress;
        private RecyclerViewClickListener recyclerViewClickListener;

        public BLEDeviceViewHolder(View view ,RecyclerViewClickListener listener){
            super(view);
            recyclerViewClickListener = listener;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            recyclerViewClickListener.onClick(leDevices.get(getAdapterPosition()));
        }
    }

    @Override
    public BLEDeviceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.device_list_adapter, parent, false);
        BLEDeviceViewHolder bleDeviceViewHolder = new BLEDeviceViewHolder(itemView,recyclerViewClickListener);
        bleDeviceViewHolder.deviceAddress = (TextView) (TextView) itemView.findViewById(R.id.deviceAddressTxt);
        bleDeviceViewHolder.deviceName = (TextView) itemView.findViewById(R.id.deviceNameTxt);

        return bleDeviceViewHolder;
    }

    @Override
    public void onBindViewHolder(BLEDeviceViewHolder holder, int position) {
        ScanResult scanResult = leDevices.get(position);
        holder.deviceAddress.setText(scanResult.getBleDevice().getMacAddress());
        final String deviceName = scanResult.getBleDevice().getName();
        if (deviceName != null && deviceName.length() > 0)
            holder.deviceName.setText(deviceName);
        else
            holder.deviceName.setText("Un known");
        holder.deviceAddress.setText(scanResult.getBleDevice().getMacAddress());
    }

    @Override
    public int getItemCount() {
        return leDevices.size();
    }

    public void addDevice(ScanResult device) {
        for (int i = 0; i < leDevices.size(); i++) {

            if (leDevices.get(i).getBleDevice().equals(device.getBleDevice())) {
                return;
            }
        }

        leDevices.add(device);
        notifyDataSetChanged();
    }

    public ScanResult getDevice(int position) {
        return leDevices.get(position);
    }

    public void clear() {
        leDevices.clear();
    }
}
