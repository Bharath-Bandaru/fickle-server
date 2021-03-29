package info.fickle.fickleserver.activity;

import android.content.DialogInterface;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import info.fickle.fickleserver.CustomViewPager;
import info.fickle.fickleserver.fragment.HomeFragment;
import info.fickle.fickleserver.adapter.MainAdapter;
import info.fickle.fickleserver.model.MainModel;
import info.fickle.fickleserver.R;

public class MainActivity extends AppCompatActivity {
    private List<MainModel> offers;
    RecyclerView rv;
    FloatingActionButton f;
    EditText off;
    private TabLayout mTabLayout;
    private int[] mTabsIcons = {
            R.drawable.ic_recents_selector,
            R.drawable.ic_home_selector,
            R.drawable.ic_place_selector};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CustomViewPager viewPager = (CustomViewPager) findViewById(R.id.view_pager);
        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());

        if (viewPager != null) {
            viewPager.setPagingEnabled(false);
            viewPager.setAdapter(pagerAdapter);
        }

        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        if (mTabLayout != null) {
            mTabLayout.setupWithViewPager(viewPager);

            for (int i = 0; i < mTabLayout.getTabCount(); i++) {
                TabLayout.Tab tab = mTabLayout.getTabAt(i);
                if (tab != null)
                    tab.setCustomView(pagerAdapter.getTabView(i));
            }

            mTabLayout.getTabAt(0).getCustomView().setSelected(true);
        }
        mTabLayout.getTabAt(1).select();
       /* rv=(RecyclerView)findViewById(R.id.recycler_view);
        off = (EditText) findViewById(R.id.offer_text);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        MainAdapter adapter = new MainAdapter(offers);
        rv.setAdapter(adapter);
        initializeData();
        initializeAdapter();*/
    }

    private void initializeData(){
        offers = new ArrayList<>();
        offers.add(new MainModel("\n" +
                "Amazing Recharge Offers on MyAirtel App\n" +
                "+ Upto 3.5% Cashback From CouponDunia ",false));
        offers.add(new MainModel("Get amazing offers on International Roaming Smartpicks. Offer is valid only for postpaid sims.",false));
        offers.add(new MainModel("Get amazing offers and promo codes on various transactions made via Airtel Money. Offer valid for prepaid recharges only.",false));
    }
    private void initializeAdapter(){
        MainAdapter adapter = new MainAdapter(offers);
        rv.setAdapter(adapter);
    }
    public void onClickNew(View view){

    }
    public void onBackPressed() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setPositiveButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        quit();
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = builder1.create();
        alertDialog.setTitle("Do you want to Exit?");
        alertDialog.setIcon(R.drawable.logo_140);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }
    public void quit(){
        super.onBackPressed();

    }
    private class MyPagerAdapter extends FragmentPagerAdapter {

        public final int PAGE_COUNT = 3;

        private final String[] mTabsTitle = {"Profile", "Home", "Address"};

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public View getTabView(int position) {
            // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.custom_tab, null);
            TextView title = (TextView) view.findViewById(R.id.title);
            title.setText(mTabsTitle[position]);
            ImageView icon = (ImageView) view.findViewById(R.id.icon);
            icon.setImageResource(mTabsIcons[position]);
            return view;
        }

        @Override
        public Fragment getItem(int pos) {
            switch (pos) {

                case 0:
                    return HomeFragment.newInstance(1);

                case 1:
                    return HomeFragment.newInstance(2);

                case 2:
                    return HomeFragment.newInstance(3);

            }
            return null;
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabsTitle[position];
        }
    }

}
