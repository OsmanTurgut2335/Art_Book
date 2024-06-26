package com.example.artbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.artbook.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
     private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);



    }

    @Override  //this place is for binding the menu
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.art_menu,menu);



        return super.onCreateOptionsMenu(menu);
    }

    @Override    //  sağ üstteki bu sikilerden opsiyonlardan birisi seçilirse ne olacak
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
             if(item.getItemId()  == R.id.add_art  ){// add art menusu seçildiyse ne olucak
                 Intent intent = new Intent(this,DetailsActivity.class);

                 startActivity(intent);
             }

        return super.onOptionsItemSelected(item);
    }
}