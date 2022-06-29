package com.relearn.mobileapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.relearn.mobileapp.helper.NotesHelper;
import com.relearn.mobileapp.model.Notes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotesDashboard extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private FragmentAdapter adapter;
    private MaterialSearchView searchView;
    private Chip activeChip;

    private Map<String,Integer> filtersMap;

    private ArrayList<Notes> mList;
    private String[] searchSuggestionList;
    private List<Notes> searchSuggestionNotes;
    boolean firstTime = true;
    private String branch;
    private String subject;

    Toolbar toolbar;
    FrameLayout frameLayout;
    ChipGroup chipGroup;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_dashboard);

        branch = getSharedPreferences(getString(R.string.user_details_sf),MODE_PRIVATE).getString("STUDENT_BRANCH",null);
        if(branch == null){
            // Handle the error
            Log.d("Main","Branch is null on Notes Dashboard");
        }

        subject = getIntent().getStringExtra("EXTRA_SUBJECT");
        if(subject == null){
            // Handle the error
            Log.d("Main","subject is null on Notes Dashboard");
        }

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        searchView = findViewById(R.id.search_view);
        frameLayout = findViewById(R.id.framelayout);
        chipGroup = findViewById(R.id.chips);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabLayout.addTab(tabLayout.newTab().setText("Notes"));
        tabLayout.addTab(tabLayout.newTab().setText("Papers"));

        FragmentManager fragmentManager = getSupportFragmentManager();
        adapter = new FragmentAdapter(fragmentManager , getLifecycle());
        viewPager.setAdapter(adapter);

        filtersMap = new HashMap<>();
        setFilterMap();
//        fragments = fragmentManager.getFragments();
//        Log.d("Main","no of Fragement: "+fragments.size());

        chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            mList = new ArrayList<>();
            if(checkedIds.isEmpty()){
                Log.d("Main","Chip " + activeChip.getText() + "is unactive");
                ArrayList<Integer> timeLeft = NotesHelper.initTimer(6000);
                //NotesHelper.applyNotesList(subject,mList,timeLeft);
                NotesHelper.applyNotesByFilterList(subject, Arrays.asList(1,2,3,4,5),mList,timeLeft);
                group.clearCheck();
                activeChip = null;
                return;
            }
            int id = group.getCheckedChipId();
            activeChip = findViewById(id);
            int filter = filtersMap.get(activeChip.getText());
            if(filter == 1){
                Snackbar.make(viewPager,"Not enough data available", Snackbar.LENGTH_SHORT).show();
            }
            else{
                Log.d("Main",String.valueOf(filter));
                ArrayList<Integer> timeLeft = NotesHelper.initTimer(6000);
                NotesHelper.applyNotesByFilterList(subject,Collections.singletonList(filter),mList,timeLeft);
            }

        });
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
                if(position == 0){
                    NotesHelper.setNotesFragment(NotesHelper.getMNotesFragement());
                    chipGroup.setVisibility(View.VISIBLE);
                }
                else if(position == 1){
                    try {
                        NotesHelper.setNotesFragment(NotesHelper.getMPapersFragement());
                    }
                    catch(Exception e){
                        Log.d("Main","ViewPager error");
                    }
                    finally {
                        chipGroup.setVisibility(View.GONE);
                    }
                }

            }
        });


        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                try{
                    if(firstTime){
                        initSuggestionList();
                        firstTime = false;
                    }
                    searchView.setSuggestions(searchSuggestionList);
                }
                catch (Exception e){
                    Snackbar.make(viewPager,"Wait to Load the data", Snackbar.LENGTH_SHORT).show();
                    searchView.closeSearch();
                }
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });
        searchView.setHint("Search Notes...");
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                for(Notes note: NotesHelper.getCompleteNotesList()){
                    if(note.getName().equals(query)){
                        Intent intent = new Intent(NotesDashboard.this,PdfActivity.class);
                        intent.putExtra("EXTRA_URL",note.getUrl());
                        startActivity(intent);
                        searchView.closeSearch();
                        break;
                    }
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                Log.d("Main","onTextchange: "+newText);
                return false;
            }
        });

    }

    private void initSuggestionList() {
        searchSuggestionNotes = NotesHelper.getCompleteNotesList();
        int n = searchSuggestionNotes.size();
        searchSuggestionList = new String[n];
        for(int i=0; i<n; i++){
            searchSuggestionList[i] = searchSuggestionNotes.get(i).getName();
        }
    }

    private void setFilterMap() {
        filtersMap.put("Papers",0);
        filtersMap.put("Most Viewed",1);
        filtersMap.put("Theory",2);
        filtersMap.put("Lab",3);
        filtersMap.put("Handwritten",4);
        filtersMap.put("Important",5);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            finish();
        }
    }
}