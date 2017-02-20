package com.ls.js;


import android.content.Intent;
import android.hardware.SensorManager;
import android.location.LocationListener;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.ls.js.wikitude.AbstractArchitectCamActivity;
import com.ls.js.wikitude.ArchitectViewHolderInterface;
import com.ls.js.wikitude.Constants;
import com.wikitude.architect.ArchitectView;
import com.wikitude.architect.ArchitectView.ArchitectUrlListener;
import com.wikitude.architect.ArchitectView.SensorAccuracyChangeListener;
import com.wikitude.common.camera.CameraSettings;

import java.io.File;


/**
 * 云识别
 * Created by liu song on 2016/12/19.
 */
public class WikitudeActivity extends AbstractArchitectCamActivity {

    /**
     * last time the calibration toast was shown, this avoids too many toast shown when compass needs calibration
     */
    private long lastCalibrationToastShownTimeMillis = System.currentTimeMillis();

    @Override
    public int getContentViewId() {
        return R.layout.activity_wikitude;
    }

    @Override
    public String getActivityTitle() {
        return "AR-Title";
    }

    @Override
    public int getArchitectViewId() {
        return R.id.architectView;
    }

    @Override
    public String getWikitudeSDKLicenseKey() {
        return Constants.WIKITUDE_SDK_KEY;
    }

    @Override
    protected CameraSettings.CameraPosition getCameraPosition() {
        return CameraSettings.CameraPosition.DEFAULT;
    }

    @Override
    public ArchitectView.SensorAccuracyChangeListener getSensorAccuracyListener() {
        return new SensorAccuracyChangeListener() {
            @Override
            public void onCompassAccuracyChanged(int accuracy) {
                /* UNRELIABLE = 0, LOW = 1, MEDIUM = 2, HIGH = 3 */
                if (accuracy < SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM && WikitudeActivity.this != null && !WikitudeActivity.this.isFinishing() && System.currentTimeMillis() - WikitudeActivity.this.lastCalibrationToastShownTimeMillis > 5 * 1000) {
                    Toast.makeText(WikitudeActivity.this, "Please re-calibrate compass by waving your device in a figure 8 motion.", Toast.LENGTH_LONG).show();
                    WikitudeActivity.this.lastCalibrationToastShownTimeMillis = System.currentTimeMillis();
                }
            }
        };
    }

    @Override
    public ArchitectView.ArchitectUrlListener getUrlListener() {
        return new ArchitectUrlListener() {

            @Override
            public boolean urlWasInvoked(String uriString) {
                Uri invokedUri = Uri.parse(uriString);
                Log.i("LOG_CAT", "url=" + invokedUri.toString());

                //跳转webview
                if ("link".equalsIgnoreCase(invokedUri.getHost())) {
                    String url = invokedUri.getQueryParameter("uri");
                    String title = invokedUri.getQueryParameter("title");
                    String content = invokedUri.getQueryParameter("content");

                    Intent intent = new Intent(WikitudeActivity.this, WebActivity.class);
                    intent.putExtra("url_type", "WEB_PAGE_URL");
                    intent.putExtra("url", url);
                    intent.putExtra("title", title);
                    intent.putExtra("content", content);
                    startActivity(intent);
                    finish();
                }
                return true;
            }
        };
    }

    @Override
    protected boolean hasGeo() {
        return false;
    }

    @Override
    protected boolean hasIR() {
        return false;
    }

    @Override
    public ILocationProvider getLocationProvider(final LocationListener locationListener) {
        return null;
    }

    @Override
    public String getARchitectWorldPath() {
        return "wikitude" + File.separator + "cloud" + File.separator + "index.html";
    }

    @Override
    public float getInitialCullingDistanceMeters() {
        // you need to adjust this in case your POIs are more than 50km away from user here while loading or in JS code (compare 'AR.context.scene.cullingDistance')
        return ArchitectViewHolderInterface.CULLING_DISTANCE_DEFAULT_METERS;
    }

    // new add
    @Override
    public ArchitectView.ArchitectWorldLoadedListener getWorldLoadedListener() {
        return new ArchitectView.ArchitectWorldLoadedListener() {
            @Override
            public void worldWasLoaded(String url) {
                Log.i("LOG_CAT", "worldWasLoaded: url: " + url);
            }

            @Override
            public void worldLoadFailed(int errorCode, String description, String failingUrl) {
                Log.e("LOG_CAT", "worldLoadFailed: url: " + failingUrl + " " + description);
            }
        };
    }

    @Override
    protected boolean hasInstant() {
        //??? what's meaning?
        return false;
    }

}
