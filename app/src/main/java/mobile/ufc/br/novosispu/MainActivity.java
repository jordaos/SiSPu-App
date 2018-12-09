package mobile.ufc.br.novosispu;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import mobile.ufc.br.novosispu.broadcast.MyBroadcastReceiver;
import mobile.ufc.br.novosispu.fragments.HomeFragment;
import mobile.ufc.br.novosispu.fragments.MapDemandsFragment;
import mobile.ufc.br.novosispu.fragments.MyDemandsFragment;
import mobile.ufc.br.novosispu.fragments.NewDemandFragment;
import mobile.ufc.br.novosispu.fragments.OptionsFragment;
import mobile.ufc.br.novosispu.service.NotificationService;

import static mobile.ufc.br.novosispu.Constants.*;


public class MainActivity extends AppCompatActivity {

    final Fragment homeFragment = HomeFragment.newInstance();
    final Fragment mapDemandFragment = MapDemandsFragment.newInstance();
    final Fragment newDemandFragment = NewDemandFragment.newInstance();
    final Fragment optionsFragment = OptionsFragment.newInstance();
    final Fragment myDemandsFragment = MyDemandsFragment.newInstance();
    final FragmentManager fm = getSupportFragmentManager();
    private Fragment active = homeFragment;

    private Intent notificacaoService;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mAuth = FirebaseAuth.getInstance();
        notificacaoService = new Intent(this, NotificationService.class);
        startService(notificacaoService);

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.SCREEN_OFF");
        filter.addAction("android.intent.action.SCREEN_ON");

        MyBroadcastReceiver myBroadcastReceiver = new MyBroadcastReceiver();
        registerReceiver(myBroadcastReceiver, filter);

        fm.beginTransaction().add(R.id.main_container, optionsFragment, FRAGMENT_OPTIONS_ID).hide(optionsFragment).commit();
        fm.beginTransaction().add(R.id.main_container, mapDemandFragment, FRAGMENT_MAP_ID).hide(mapDemandFragment).commit();
        fm.beginTransaction().add(R.id.main_container, myDemandsFragment, FRAGMENT_MY_DEMANDS_ID).hide(myDemandsFragment).commit();
        fm.beginTransaction().add(R.id.main_container, newDemandFragment, FRAGMENT_NEW_DEMAND_ID).hide(newDemandFragment).commit();
        fm.beginTransaction().add(R.id.main_container, homeFragment, FRAGMENT_HOME_ID).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    changeFragmentTo(FRAGMENT_HOME_ID);
                    return true;
                case R.id.navigation_map:
                    changeFragmentTo(FRAGMENT_MAP_ID);
                    return true;
                case R.id.navigation_options:
                    changeFragmentTo(FRAGMENT_OPTIONS_ID);
                    return true;
                case R.id.navigation_my_demands:
                    changeFragmentTo(FRAGMENT_MY_DEMANDS_ID);
                    return true;
            }
            return false;
        }
    };

    public void changeFragmentTo(String fragmentId) {
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        switch (fragmentId){
            case FRAGMENT_HOME_ID:
                fm.beginTransaction().hide(active).show(homeFragment).commit();
                active = homeFragment;
                break;
            case FRAGMENT_MAP_ID:
                fm.beginTransaction().hide(active).show(mapDemandFragment).commit();
                active = mapDemandFragment;
                break;
            case FRAGMENT_NEW_DEMAND_ID:
                fm.beginTransaction().hide(active).show(newDemandFragment).commit();
                active = newDemandFragment;
                break;
            case FRAGMENT_OPTIONS_ID:
                fm.beginTransaction().hide(active).show(optionsFragment).commit();
                active = optionsFragment;
                break;
            case FRAGMENT_MY_DEMANDS_ID:
                fm.beginTransaction().hide(active).show(myDemandsFragment).commit();
                active = myDemandsFragment;
                break;
            default:
                fm.beginTransaction().hide(active).show(homeFragment).commit();
                active = homeFragment;
                break;
        }
        fragmentTransaction.commit();
    }

    public void changeFragmentTo(String fragmentId, String paramValue) {
        switch (fragmentId) {
            case FRAGMENT_NEW_DEMAND_ID:
                NewDemandFragment newDemandFragmentParam = NewDemandFragment.newInstance(paramValue);
                fm.beginTransaction().add(R.id.main_container, newDemandFragmentParam, FRAGMENT_OPTIONS_ID).hide(newDemandFragmentParam).commit();
                fm.beginTransaction().hide(active).show(newDemandFragmentParam).commit();
                active = newDemandFragmentParam;
                break;
            default:
                changeFragmentTo(fragmentId);
                break;
        }
    }

    public void stopService() {
        stopService(notificacaoService);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        if(currentUser == null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

}
