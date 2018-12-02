package mobile.ufc.br.novosispu.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mobile.ufc.br.novosispu.MainActivity;
import mobile.ufc.br.novosispu.R;
import mobile.ufc.br.novosispu.components.CardViewComponent;
import mobile.ufc.br.novosispu.entities.Demand;
import mobile.ufc.br.novosispu.entities.Like;
import mobile.ufc.br.novosispu.entities.User;
import mobile.ufc.br.novosispu.service.DemandService;
import mobile.ufc.br.novosispu.service.LikeService;
import mobile.ufc.br.novosispu.service.UserService;

import static mobile.ufc.br.novosispu.Constants.CHANNEL_ID;
import static mobile.ufc.br.novosispu.Constants.FRAGMENT_HOME_ID;
import static mobile.ufc.br.novosispu.Constants.FRAGMENT_NEW_DEMAND_ID;

public class MyDemandsFragment extends Fragment {

    private FrameLayout thisPage;
    private LinearLayout contentDemands;

    private DemandService demandService;
    private LikeService likeService;
    private UserService userService;

    public MyDemandsFragment() {
        // Required empty public constructor
    }

    public static MyDemandsFragment newInstance() {
        MyDemandsFragment fragment = new MyDemandsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        demandService = new DemandService();
        likeService = new LikeService();
        userService = new UserService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_my_demands, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        thisPage = (FrameLayout) getView().findViewById(R.id.myDemandsFragment);
        contentDemands = (LinearLayout) thisPage.findViewById(R.id.contentMyDemands);

        DatabaseReference demandRef = demandService.getDemandRef();

        final String currentUserKey = userService.getCurrentUserKey();

        demandRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                contentDemands.removeAllViews();
                for (DataSnapshot item: dataSnapshot.getChildren()) {
                    final Demand demand = item.getValue(Demand.class);

                    if(demand.getUser().getKey().equals(currentUserKey)) {
                        final CardViewComponent card = new CardViewComponent(getContext());
                        card.setDemand(demand);

                        likeService.getLikesRef().addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                int count = 0;
                                for (DataSnapshot item : dataSnapshot.getChildren()) {
                                    Like like = item.getValue(Like.class);
                                    if (like.getDemandKey().equals(demand.getKey())) {
                                        count++;
                                    }
                                }
                                card.setLikeDemandButton(count);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        contentDemands.addView(card);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("The read failed: ", databaseError.getMessage());
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
