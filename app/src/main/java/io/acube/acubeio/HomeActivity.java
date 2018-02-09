/**
 * Jahasa Technology all rights reserved
 *
 * @author Uday
 * @since 01.02.2018
 * <p>
 * The class handles the home screen activity,
 * The class scans and list the BLE devices and connects to it on click.
 */


package io.acube.acubeio;

import android.Manifest;
import android.bluetooth.BluetoothManager;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.os.ParcelUuid;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import io.acube.acubeio.adapters.BLEDevicesRecyclerAdapter;
import io.acube.acubeio.dependencies.component.ActivityComponent;
import io.acube.acubeio.dependencies.component.DaggerActivityComponent;
import io.acube.acubeio.dependencies.module.ActivityModule;
import rx.Observable;
import rx.Subscription;

import rx.android.schedulers.AndroidSchedulers;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.polidea.rxandroidble.RxBleClient;
import com.polidea.rxandroidble.RxBleConnection;
import com.polidea.rxandroidble.RxBleDeviceServices;
import com.polidea.rxandroidble.exceptions.BleScanException;
import com.polidea.rxandroidble.scan.ScanFilter;
import com.polidea.rxandroidble.scan.ScanResult;
import com.polidea.rxandroidble.scan.ScanSettings;

import java.util.List;

import javax.inject.Inject;


public class HomeActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 101;
    BluetoothManager bluetoothManager;

    private static final int REQUEST_ENABLE_BT = 100;

    private View.OnClickListener scanBtnClickListener, logOutBtnClickListener;

    private ActivityComponent activityComponent;
    private BLEDevicesRecyclerAdapter bleDevicesRecyclerAdapter;
    private RxBleConnection rxBleConnection;
    private Subscription connectionSubscription;
    private Subscription scanSubscription;
    BLEDevicesRecyclerAdapter.RecyclerViewClickListener recyclerViewClickListener;

    private Button scanBtn, logOutBtn;
    private RecyclerView bleDeviceRecyclerView;

    public static final String DEVICE_UUID="ffffffff-ffff-ffff-ffff-fffffffffff0";

    @Inject
    RxBleClient rxBleClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initEventListeners();
        initViews();
    }

    /**
     * Initializes the view, by referencing all the required widgets and
     * actions to be taken after the view is rendered.
     **/
    private void initViews() {
        getActivityComponent();
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "BLE not supported", Toast.LENGTH_SHORT).show();
        }
        activityComponent.inject(this);
        bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter BTAdapter = bluetoothManager.getAdapter();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (BTAdapter == null || !BTAdapter.isEnabled()) {
            enableBluetooth();
        }
        checkPermissions();
        scanBtn = (Button) findViewById(R.id.scanBtn);
        scanBtn.setOnClickListener(scanBtnClickListener);

        logOutBtn = (Button) findViewById(R.id.logOutBtn);
        logOutBtn.setOnClickListener(logOutBtnClickListener);

        bleDeviceRecyclerView = (RecyclerView) findViewById(R.id.bleDeviceRecyclerView);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        bleDeviceRecyclerView.setLayoutManager(llm);
        bleDevicesRecyclerAdapter = new BLEDevicesRecyclerAdapter(recyclerViewClickListener);
        bleDeviceRecyclerView.setAdapter(bleDevicesRecyclerAdapter);
    }

    /**
     * Initializes the all the event handlers of the class.
     **/
    private void initEventListeners() {
        scanBtnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanLeDevices();
            }
        };

        logOutBtnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeActivity.this.onBackPressed();
            }
        };

        recyclerViewClickListener = new BLEDevicesRecyclerAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(ScanResult scanResult) {
                Intent bleSendRecieveActivityIntent = new Intent(HomeActivity.this,BLESendRecieve.class);
                bleSendRecieveActivityIntent.putExtra("macAddress",scanResult.getBleDevice().getMacAddress());
                startActivity(bleSendRecieveActivityIntent);
            }
        };
    }


    /**
     * Enables bluetooth
     **/
    public void enableBluetooth(){
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);

    }

    /**
     * Check if permissions are provided.
     **/
    private void checkPermissions(){
        if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
        }
    }

    /**
     * starts the BLE devices scan.
     **/
    public void scanLeDevices() {
        if (scanSubscription == null) {
            scanSubscription = rxBleClient.scanBleDevices(new ScanSettings.Builder()
                            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                            .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
                            .build(),
                    new ScanFilter.Builder()
                            .setServiceUuid(ParcelUuid.fromString(DEVICE_UUID))
                            .build()).observeOn(AndroidSchedulers.mainThread())
                    .doOnUnsubscribe(this::clearSubscription)
                    .subscribe(this::addScanResult, this::onScanFailure);
        }
    }

    private void clearSubscription() {
        scanSubscription = null;
    }

    /**
     * adds the Scan Result to the BLE Device Adapter
     **/
    private void addScanResult(ScanResult scanResult) {
        bleDevicesRecyclerAdapter.addDevice(scanResult);
    }

    /**
     * On Scan failure callback.
     **/
    private void onScanFailure(Throwable throwable) {

        if (throwable instanceof BleScanException) {

        }
    }

    /**
     * Initializes the activity component for this activity
     *
     * @return ActivityComponent - activity component
     **/
    public void getActivityComponent() {
        if (activityComponent == null) {
            activityComponent = DaggerActivityComponent.builder()
                    .activityModule(new ActivityModule(this))
                    .applicationComponent(AcubeAppApplication.get(this).getComponent())
                    .build();
        }
    }

}

