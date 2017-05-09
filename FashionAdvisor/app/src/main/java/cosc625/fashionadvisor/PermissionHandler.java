package cosc625.fashionadvisor;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import static java.security.AccessController.getContext;

/**
 * Created by Matt on 5/9/2017.
 */

public final class PermissionHandler implements ActivityCompat.OnRequestPermissionsResultCallback {

    public static final int cameraPermission = 5;//this should have some constant value defined by android
    public static boolean cameriaIsAllowed = false;

    private PermissionHandler() {

    }

    public static boolean checkPermission(Activity currentActivity, String permission) {
        if(ContextCompat.checkSelfPermission(currentActivity, permission)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(currentActivity,
                    permission)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(currentActivity,
                        new String[]{permission},
                        cameraPermission);

                // cameraPermission is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
            return true;
        } else {
            return true;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 5: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // camera-related task you need to do.

                    //pass the message back to the point where checkPermission was called... what
                    cameriaIsAllowed = true;


                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    cameriaIsAllowed = false;
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}
