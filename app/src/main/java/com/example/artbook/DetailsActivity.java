package com.example.artbook;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.example.artbook.databinding.ActivityDetailsBinding;
import com.google.android.material.snackbar.Snackbar;

public class DetailsActivity extends AppCompatActivity {
    private ActivityDetailsBinding binding;
    ActivityResultLauncher <Intent> activityResultLauncher; // izin verildiğinde ne olacağı burdan oluyor ,galeriye gitmek için
    ActivityResultLauncher <String>  permissionLauncher;     // izin istemek için
    Bitmap selectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
          registerLauncher();

    }
    public void save(View view){

    }

    public void selectImage(View view){
      if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)   != PackageManager.PERMISSION_GRANTED){
          // izin verilmeme kısmı
            if(ActivityCompat.shouldShowRequestPermissionRationale(DetailsActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)  ){      //opsiyonel ?
                Snackbar.make(view,"Permission needed for gallery",Snackbar.LENGTH_INDEFINITE).setAction("Give Permisson", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                  permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                    }
                }).show();
            }
            else{
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            }

      }
      else{
          //izin verilme kısmı         A1
          Intent intent_to_gallery = new Intent(Intent.ACTION_PICK , MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
          activityResultLauncher.launch(intent_to_gallery);

      }


    }
    private void registerLauncher(){
        //ARL leri tanımla kısmı sonra oncreate te tanımlıcaz

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == RESULT_OK ){
                    Intent intent_from_result =result.getData();

                    if(intent_from_result != null){
                      Uri image_data =  intent_from_result.getData();
                      // binding.imageView.setImageURI(image_data); bu da işe yarar ama veritabanına kaydedemeyiz

                        try {
                            if(Build.VERSION.SDK_INT >=28 ){
                                ImageDecoder.Source source = ImageDecoder.createSource(DetailsActivity.this.getContentResolver(),image_data);
                                selectedImage = ImageDecoder.decodeBitmap(source);
                                binding.imageView.setImageBitmap(selectedImage);
                            }
                        else{
                            selectedImage = MediaStore.Images.Media.getBitmap(DetailsActivity.this.getContentResolver(),image_data);
                            binding.imageView.setImageBitmap(selectedImage);
                            }

                        }catch (Exception e ){
                            e.printStackTrace();
                        }

                    }

                }

            }

        });

        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
             if(result){
              //permission granted     A1 dediğim yerdeki aynı işlem
                 Intent intent_to_gallery = new Intent(Intent.ACTION_PICK , MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                 activityResultLauncher.launch(intent_to_gallery);
             }
             else{
                 // permission denied
                 Toast.makeText(DetailsActivity.this, "Permission needed bro ! ", Toast.LENGTH_LONG).show();

             }

            }
        });


    }


}