package team.iscode.igor.calculosinais;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import java.util.Objects;

import team.iscode.igor.calculosinais.adapters.MyViewPagerAdapter;

public class TabsActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    MyViewPagerAdapter myViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager2 = findViewById(R.id.view_pager);
        myViewPagerAdapter = new MyViewPagerAdapter(this);

        viewPager2.setAdapter(myViewPagerAdapter);

        int selectedTab = getIntent().getIntExtra("selectedTab", -1);

        // Verifica se o valor é válido antes de selecionar a guia
        if (selectedTab >= 0 && selectedTab < tabLayout.getTabCount()) {
            // Seleciona a guia indicada
            tabLayout.setScrollPosition(selectedTab, 0f, true);
            viewPager2.setCurrentItem(selectedTab);
        }



        Intent intent = getIntent();
        if (intent != null) {
            float zeroInput = intent.getFloatExtra("zeroInput", 0.0f);
            float zeroOutput = intent.getFloatExtra("zeroOutput", 0.0f);
            float cemInput = intent.getFloatExtra("cemInput", 0.0f);
            float cemOutput = intent.getFloatExtra("cemOutput", 0.0f);
            // Obtém referência ao fragmento correspondente (use getSupportFragmentManager() ou getChildFragmentManager() conforme necessário)
            //SinalEntrada_fragment fragment = (SinalEntrada_fragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);


            Bundle bundle = new Bundle();
            bundle.putFloat("zeroInput", zeroInput);
            bundle.putFloat("zeroOutput", zeroOutput);
            bundle.putFloat("cemInput", cemInput);
            bundle.putFloat("cemOutput", cemOutput);

            myViewPagerAdapter.setSinalEntradaFragmentArguments(bundle);
            myViewPagerAdapter.setSinalSaidaFragmentArguments(bundle);


            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
            Objects.requireNonNull(getSupportActionBar()).hide();

        }


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Objects.requireNonNull(tabLayout.getTabAt(position)).select();
            }
        });

    }

}