package com.uk.ac.tees.w9214627.scooble;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.uk.ac.tees.w9214627.scooble.userFragments.bookingsFragment;
import com.uk.ac.tees.w9214627.scooble.userFragments.homeFragment;
import com.uk.ac.tees.w9214627.scooble.userFragments.profileFragment;
import com.uk.ac.tees.w9214627.scooble.userFragments.savedFragment;

import java.util.Objects;


public class homeActivity extends AppCompatActivity {

    private MeowBottomNavigation gBNV;
    private FrameLayout gFrameLayout;

    private static final int ID_HOME = 1;
    private static final int ID_BOOKINGS = 2;
    private static final int ID_SAVED = 3;
    private static final int ID_PROFILE = 4;

    private Fragment homeFrag = new homeFragment();
    private Fragment bookingsFrag = new bookingsFragment();
    private Fragment savedFrag = new savedFragment();
    private Fragment profileFrag = new profileFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        gBNV = (MeowBottomNavigation)findViewById(R.id.home_BNV);
        gFrameLayout = (FrameLayout)findViewById(R.id.home_frameLayout);

        gBNV.add(new MeowBottomNavigation.Model(ID_HOME,R.drawable.ic_home));
        gBNV.add(new MeowBottomNavigation.Model(ID_BOOKINGS,R.drawable.ic_car));
        gBNV.add(new MeowBottomNavigation.Model(ID_SAVED,R.drawable.ic_bookmark));
        gBNV.add(new MeowBottomNavigation.Model(ID_PROFILE,R.drawable.ic_user));

        getSupportFragmentManager().beginTransaction().replace(R.id.home_frameLayout,homeFrag).commit();

        gBNV.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {

            }
        });
        gBNV.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                Fragment selected_fragment = null;
                switch (item.getId())
                {
                    case ID_HOME:
                        selected_fragment = homeFrag;
                        break;
                    case ID_BOOKINGS:
                        selected_fragment = bookingsFrag;
                        break;
                    case ID_PROFILE:
                        selected_fragment = profileFrag;
                        break;
                    case ID_SAVED:
                        selected_fragment = savedFrag;
                        break;

                }
                getSupportFragmentManager().beginTransaction().replace(R.id.home_frameLayout, Objects.requireNonNull(selected_fragment)).commit();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        gBNV.show(ID_HOME,true);
    }
}