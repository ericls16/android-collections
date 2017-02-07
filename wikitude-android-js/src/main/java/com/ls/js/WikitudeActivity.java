package com.ls.js;

import android.location.LocationListener;
import android.net.Uri;
import android.util.Log;

import com.ls.js.wikitude.AbstractArchitectCamActivity;
import com.ls.js.wikitude.ArchitectViewHolderInterface;
import com.ls.js.wikitude.Constants;
import com.wikitude.architect.ArchitectView;
import com.wikitude.common.camera.CameraSettings;

import java.io.File;

/**
 * 云识别
 * Created by liu song on 2016/12/19.
 */

public class WikitudeActivity extends AbstractArchitectCamActivity {

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
        return null;
    }

    @Override
    public ArchitectView.ArchitectUrlListener getUrlListener() {
        return new ArchitectView.ArchitectUrlListener() {

            @Override
            public boolean urlWasInvoked(String uriString) {
                Uri invokedUri = Uri.parse(uriString);
                Log.i("LOG_CAT", "url=" + invokedUri.toString());

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
    public ILocationProvider getLocationProvider(LocationListener locationListener) {
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
