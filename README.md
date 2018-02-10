# Android-Acube

The Project used to send and receive data from a BLE Device.

## Description
      The Project verifies the user with **OAuth** and fetches and stores the encrypted **Access Token** and **Refresh Token** in the **SQlite** database , after successful login the user can scan for the BLE Devices in the range, all the devices available in the range are listed. User can tap on the list item to connect to the device and send and receive the data from it.


### Prerequisites
      1. Android studio
      2. Android SDK
      3. Minimum SDK level required is 18

### Running the Project
      Download or clone the project from github, open the project in the android studio
      and press run on the tools bar to run the project.
      
### Built With
      [Dagger 2](https://google.github.io/dagger/) - Android dependency injection library.
      [RXAndroidBLE](https://github.com/Polidea/RxAndroidBle) - Library used to connect and communicate with BLE Device.
      [Volley](https://developer.android.com/training/volley/index.html) - Library used for HTTP network operation.
      [OAuth2](https://github.com/corcoran/okhttp-oauth2-client) - OkHttp OAuth2 client library for OAuth authentication
