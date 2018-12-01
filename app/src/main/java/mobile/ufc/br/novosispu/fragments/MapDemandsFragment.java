package mobile.ufc.br.novosispu.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import mobile.ufc.br.novosispu.R;
import mobile.ufc.br.novosispu.components.CardViewComponent;
import mobile.ufc.br.novosispu.entities.Demand;
import mobile.ufc.br.novosispu.entities.Like;
import mobile.ufc.br.novosispu.service.DemandService;

public class MapDemandsFragment extends Fragment {

    MapView mMapView;
    private GoogleMap googleMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private Location location;

    private DemandService demandService;

    public MapDemandsFragment() {
        // Required empty public constructor
    }

    public static MapDemandsFragment newInstance() {
        MapDemandsFragment fragment = new MapDemandsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        demandService = new DemandService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map_demands, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                googleMap.setMyLocationEnabled(true);
                location = googleMap.getMyLocation();

                // For dropping a marker at a point on the Map
                LatLng currentLocation;
                if(location != null) {
                    currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                } else {
                    currentLocation = new LatLng(-4.969610, -39.015895);
                }

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(currentLocation).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                DatabaseReference demandRef = demandService.getDemandRef();
                demandRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot item : dataSnapshot.getChildren()) {
                            Demand demand = item.getValue(Demand.class);
                            LatLng latLng = new LatLng(demand.getLat(), demand.getLng());

                            googleMap.addMarker(new MarkerOptions().position(latLng).title(demand.getTitle()).snippet(demand.getDescription()));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });



        return rootView;
    }
}
