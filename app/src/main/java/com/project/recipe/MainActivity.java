package com.project.recipe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.project.recipe.fragments.FragmentFavorites;
import com.project.recipe.fragments.FragmentHome;

public class MainActivity extends AppCompatActivity {

    Button btnHome,btnFavorite;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnHome=findViewById(R.id.btnHome);
        btnFavorite=findViewById(R.id.btnFavourite);
        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentFavorites f = new FragmentFavorites();
                FragmentTransaction t = getSupportFragmentManager().beginTransaction().addToBackStack(null);
                t.replace(R.id.container, f).commit();
            }
        });
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentHome f = new FragmentHome();
                FragmentTransaction t = getSupportFragmentManager().beginTransaction().addToBackStack(null);
                t.replace(R.id.container, f).commit();
            }
        });
        FragmentHome f = new FragmentHome();
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.container, f).commit();
    }
}