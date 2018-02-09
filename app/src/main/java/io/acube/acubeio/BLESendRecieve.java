/**
 * Jahasa Technology all rights reserved
 *
 * @author Uday
 * @since 01.02.2018
 * <p>
 * <p>
 * Handles the device send and recieve data from BLE devices.
 */

package io.acube.acubeio;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.polidea.rxandroidble.RxBleClient;
import com.polidea.rxandroidble.RxBleConnection;
import com.polidea.rxandroidble.RxBleDevice;
import com.polidea.rxandroidble.RxBleDeviceServices;
import com.polidea.rxandroidble.utils.ConnectionSharingAdapter;

import java.util.List;

import javax.inject.Inject;

import io.acube.acubeio.dependencies.component.ActivityComponent;
import io.acube.acubeio.dependencies.component.DaggerActivityComponent;
import io.acube.acubeio.dependencies.module.ActivityModule;
import rx.Observable;
import rx.Subscription;

public class BLESendRecieve extends AppCompatActivity {
    Button btnSendSuccess, btnSendFailure;
    View.OnClickListener btnSendAClickListener, btnSendBClickListener;
    private Subscription connectionSubscription;
    private RxBleDevice bleDevice;
    private ActivityComponent activityComponent;
    private RxBleConnection rxBleConnection;
    private Observable<RxBleConnection> observable;

    @Inject
    RxBleClient rxBleClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blesend_recieve);
        initEventListeners();
        initViews();
    }

    /**
     * Initializes the view, by referencing all the required widgets and
     * actions to be taken after the view is rendered.
     **/
    private void initViews() {
        getActivityComponent();
        activityComponent.inject(this);
        btnSendSuccess = findViewById(R.id.btnSendSuccess);
        btnSendFailure = findViewById(R.id.btnSendFailure);

        btnSendSuccess.setOnClickListener(btnSendAClickListener);
        btnSendFailure.setOnClickListener(btnSendBClickListener);

        String macAddress = getIntent().getStringExtra("macAddress");
        bleDevice = rxBleClient.getBleDevice(macAddress);


        if (!checkConnectionState()) {
            establishConnection();
        } else {
            if (observable != null) {
                connectionSubscription.unsubscribe();
                connectionSubscription = null;
            }
            establishConnection();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        connectionSubscription.unsubscribe();
        connectionSubscription = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        establishConnection();
    }

    /**
     * Establishes the connection to the BLE device.
     **/
    private void establishConnection() {
        observable = bleDevice
                .establishConnection(false)
                .compose(new ConnectionSharingAdapter());
        connectionSubscription = observable.subscribe(BLESendRecieve.this::onConnectionReceived, BLESendRecieve.this::onConnectionFailure);

    }

    /**
     * Checks the weather the device is connected returns the boolean value.
     *
     * @return boolean - true if connected else false.
     **/
    private boolean checkConnectionState() {
        return bleDevice.getConnectionState() == RxBleConnection.RxBleConnectionState.CONNECTED;
    }

    /**
     * Initializes the all the event handlers of the class.
     **/
    private void initEventListeners() {
        btnSendAClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rxBleConnection != null) {
                    rxBleConnection.discoverServices().subscribe(BLESendRecieve.this::onServiceDiscovered, BLESendRecieve.this::OnServiceDiscoveryFail);
                }
            }
        };

        btnSendBClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rxBleConnection != null) {
                    rxBleConnection.discoverServices().subscribe(BLESendRecieve.this::onServiceDiscovered, BLESendRecieve.this::OnServiceDiscoveryFail);
                }
            }
        };
    }

    /**
     * BLE Device successful connection call back, on connection starts discovering the services.
     **/
    private void onConnectionReceived(RxBleConnection connection) {
        rxBleConnection = connection;
    }

    /**
     * BLE Device successful service discovery call back. Gets the services and writes the data
     * to the services characteristics
     **/
    private void onServiceDiscovered(RxBleDeviceServices rxBleDeviceServices) {
        List<BluetoothGattService> services = rxBleDeviceServices.getBluetoothGattServices();
        Log.d(" Service Discovered", Integer.toString(services.size()));
        for (BluetoothGattService service : services) {
            Log.d("Home Activity", service.getUuid().toString());
            List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();

            for (BluetoothGattCharacteristic characteristic : characteristics) {
                rxBleConnection.writeCharacteristic(characteristic, getWriteDataBytes())
                        .flatMap(bytes -> rxBleConnection.readCharacteristic(characteristic))
                        .subscribe(this::onCharacteristicWrittenRead, this::onCharacteristicReaddWriteFailure);
            }
        }
    }

    /**
     * Handels the data after it is written to the BLE Device.
     */

    private void onCharacteristicWrittenRead(byte[] bytes) {
        Toast.makeText(this, "The data has been written to the device", Toast.LENGTH_LONG).show();
    }

    /**
     * Handels the failure in the read operation.
     */
    private void onCharacteristicReaddWriteFailure(Throwable throwable) {
        Log.d("HomeActivity", throwable.getMessage());
    }

    /**
     * returns the data to be written to the service characteristics in bytes array.
     *
     * @return Byte array of the string.
     **/
    private byte[] getWriteDataBytes() {
        return "ACube".getBytes();
    }

    /**
     * BLE Device serviceDiscovery failure callback.
     **/
    private void OnServiceDiscoveryFail(Throwable throwable) {
        Log.d("On exception", throwable.getLocalizedMessage());
    }

    /**
     * BLE Device connection failure call back
     **/
    private void onConnectionFailure(Throwable throwable) {
        Log.d("Home Activtiy", "Connection Error" + throwable.getLocalizedMessage());
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
