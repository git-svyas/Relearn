package com.example.mobileapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class FragmentAdapter extends FragmentStateAdapter {

    private Fragment currentFragment;
    public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    public Fragment getCurrentFragment(){
        return currentFragment;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1){
            currentFragment = new PapersFragment();
        }
        else{
            currentFragment = new NotesFragment();
        }
        return currentFragment;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}