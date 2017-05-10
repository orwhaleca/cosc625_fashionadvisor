package cosc625.fashionadvisor;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;


import fragments.*;

public class MainTabbed extends AppCompatActivity  {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int[] tabIcons = {
            R.drawable.ic_action_home,
            R.drawable.ic_action_calendar,
            R.drawable.ic_action_wardrobe,
            R.drawable.ic_action_settings };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tabbed);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().hide();

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setTabIcons();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Today(), "ONE");
        adapter.addFragment(new Plan(), "TWO");
        adapter.addFragment(new Wardrobe(), "THREE");
        adapter.addFragment(new Settings(), "FOUR");
        viewPager.setAdapter(adapter);
    }

    /**
     * This method relies on the arrow tabIcons to have the same
     * length as our tabLayout has number of tabs. When refactoring
     * tabs and tab layout, this method may also need evolved
     */
    private void setTabIcons() {
        for(int i = 0; i < tabIcons.length; i++) {
            tabLayout.getTabAt(i).setIcon(tabIcons[i]);
        }
    }
}
