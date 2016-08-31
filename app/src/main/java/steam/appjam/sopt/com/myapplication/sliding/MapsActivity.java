package steam.appjam.sopt.com.myapplication.sliding;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import steam.appjam.sopt.com.myapplication.R;

public class MapsActivity extends FragmentActivity  implements OnMapReadyCallback {

    private GoogleMap mMap;
    View marker_root_view;
    ImageView iv_marker;

    private LinearLayout address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        TextView toolbar_title = ((TextView) findViewById(R.id.main_toolbar_title));
        toolbar_title.setTypeface(Typeface.createFromAsset(getAssets(),"Quicksand_Bold.otf"));
        toolbar_title.setText("MONSTUDY");


//         Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        TextView sdAddress = (TextView)findViewById(R.id.sdAddress);
        Intent intent = getIntent();

        sdAddress.setText(intent.getExtras().getString("address"));


        ImageView close = (ImageView)findViewById(R.id.mapClose);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        address = (LinearLayout)findViewById(R.id.TopAddress);
        address.bringToFront();

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Intent intent = getIntent();
        double lat = Double.valueOf( intent.getExtras().getString("latitude"));
        double log = Double.valueOf(intent.getExtras().getString("longitude"));



        LatLng seoul = new LatLng(lat, log);
//        mMap.addMarker(new MarkerOptions().position(seoul).title("Marker in Seoul"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(seoul));

//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom( currentPosition, 17));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17), 2000, null);


        /**
         * 현재 위치로 가는 버튼 보이도록 해주는 것...true하면 보임.
         */
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                == PackageManager.PERMISSION_GRANTED) {
//            mMap.setMyLocationEnabled(true);
//        } else {
//            // Show rationale and request permission.
//        }

        setCustomMarkerView();
        getSampleMarkerItems();

    }


    private void drawMarker(Location location) {

        //기존 마커 지우기
        mMap.clear();
        LatLng currentPosition = new LatLng(location.getLatitude(), location.getLongitude());

        //currentPosition 위치로 카메라 중심을 옮기고 화면 줌을 조정한다. 줌범위는 2~21, 숫자클수록 확대
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 17));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17), 2000, null);

        //마커 추가
        mMap.addMarker(new MarkerOptions()
                .position(currentPosition)
                .snippet("Lat:" + location.getLatitude() + "Lng:" + location.getLongitude())
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .title("현재위치"));
    }

    private void setCustomMarkerView() {

        marker_root_view = LayoutInflater.from(this).inflate(R.layout.activity_maps_tag, null);
        iv_marker = (ImageView) marker_root_view.findViewById(R.id.iv_marker);
    }


    private void getSampleMarkerItems() {
        ArrayList<MarkerItem> sampleList = new ArrayList();

        Intent intent = getIntent();
        double lat = Double.valueOf( intent.getExtras().getString("latitude"));
        double log = Double.valueOf(intent.getExtras().getString("longitude"));

        //임의로 위치 4개 지정
        sampleList.add(new MarkerItem(lat, log, R.drawable.ic_picker));


        for (MarkerItem markerItem : sampleList) {
            addMarker(markerItem, false);
        }
    }

    private Marker addMarker(MarkerItem markerItem, boolean isSelectedMarker) {


        LatLng position = new LatLng(markerItem.getLat(), markerItem.getLon());

        if (isSelectedMarker) {
            iv_marker.setBackgroundResource(R.drawable.ic_picker);
        } else {
            iv_marker.setBackgroundResource(R.drawable.ic_picker);
        }

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(position);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, marker_root_view)));

        return mMap.addMarker(markerOptions);
    }

    // View를 Bitmap으로 변환
    private Bitmap createDrawableFromView(Context context, View view) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }
}
