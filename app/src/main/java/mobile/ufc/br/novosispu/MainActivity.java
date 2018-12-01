package mobile.ufc.br.novosispu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import mobile.ufc.br.novosispu.fragments.HomeFragment;
import mobile.ufc.br.novosispu.fragments.MapDemandsFragment;
import mobile.ufc.br.novosispu.fragments.NewDemandFragment;
import mobile.ufc.br.novosispu.fragments.OptionsFragment;
import mobile.ufc.br.novosispu.service.NotificationService;

import static mobile.ufc.br.novosispu.Constants.*;


public class MainActivity extends AppCompatActivity {

    final Fragment homeFragment = HomeFragment.newInstance();
    final Fragment mapDemandFragment = MapDemandsFragment.newInstance();
    final Fragment newDemandFragment = NewDemandFragment.newInstance();
    final Fragment optionsFragment = OptionsFragment.newInstance();
    final FragmentManager fm = getSupportFragmentManager();
    private Fragment active = homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fm.beginTransaction().add(R.id.main_container, homeFragment).commit();

        Intent notificacaoService = new Intent(this, NotificationService.class);
        startService(notificacaoService);
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
            }
            return false;
        }
    };

    public void changeFragmentTo(String fragmentId) {
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        switch (fragmentId){
            case FRAGMENT_HOME_ID:
                fragmentTransaction.replace(R.id.main_container, homeFragment);
                break;
            case FRAGMENT_MAP_ID:
                fragmentTransaction.replace(R.id.main_container, mapDemandFragment);
                break;
            case FRAGMENT_NEW_DEMAND_ID:
                fragmentTransaction.replace(R.id.main_container, newDemandFragment);
                break;
            case FRAGMENT_OPTIONS_ID:
                fragmentTransaction.replace(R.id.main_container, optionsFragment);
                break;
            default:
                fragmentTransaction.replace(R.id.main_container, homeFragment);
        }
        fragmentTransaction.commit();
    }

}
