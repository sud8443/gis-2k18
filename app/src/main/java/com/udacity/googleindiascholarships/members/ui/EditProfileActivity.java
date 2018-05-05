package com.udacity.googleindiascholarships.members.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.udacity.googleindiascholarships.Manifest;
import com.udacity.googleindiascholarships.R;
import com.udacity.googleindiascholarships.utils.Constants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {

    ConstraintLayout profilePictureEditLayout;
    AlertDialog imagePickDialog;
    private final static int PICK_IMAGE_CODE = 123;
    private final static int TAKE_PICTURE_CODE = 133;
    private final static int STORAGE_PERMISSION_CODE = 135;
    ImageView userProfilePicture;
    private String mCurrentPhotoPath;
    private boolean clickedPicture = false;
    private String mCurrentPhotoGalleryPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        profilePictureEditLayout = (ConstraintLayout) findViewById(R.id.cl_edit_profile_pic);
        profilePictureEditLayout.setClickable(true);
        profilePictureEditLayout.setOnClickListener(this);

        userProfilePicture = (ImageView) findViewById(R.id.iv_user_profile);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cl_edit_profile_pic:
                AlertDialog.Builder imagePickerDialog = new AlertDialog.Builder(this);
                LayoutInflater layoutInflater = getLayoutInflater();
                View dialogView = layoutInflater.inflate(R.layout.dialog_profile_image_picker, null);
                imagePickerDialog.setView(dialogView);
                Button viewPicture, takePicture, pickFromGallery;
                viewPicture = (Button) dialogView.findViewById(R.id.btn_img_pick_dialog_view);
                takePicture = (Button) dialogView.findViewById(R.id.btn_img_pick_dialog_camera);
                pickFromGallery = (Button) dialogView.findViewById(R.id.btn_img_pick_dialog_gallery);

                viewPicture.setOnClickListener(this);
                takePicture.setOnClickListener(this);
                pickFromGallery.setOnClickListener(this);
                imagePickDialog = imagePickerDialog.create();
                imagePickDialog.show();
                break;
            case R.id.btn_img_pick_dialog_view:
                if(imagePickDialog.isShowing()){
                    imagePickDialog.cancel();
                }
                Intent profileDisplay = new Intent(EditProfileActivity.this, ProfilePictureDisplayActivity.class);
                if(clickedPicture) {
                    profileDisplay.putExtra("IMAGE_URI", mCurrentPhotoPath);
                }else{
                    profileDisplay.putExtra("IMAGE_URI", mCurrentPhotoGalleryPath);
                }
                startActivity(profileDisplay);
                break;
            case R.id.btn_img_pick_dialog_camera:
                if(imagePickDialog.isShowing()){
                    imagePickDialog.cancel();
                }
                if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        File photoFile = null;
                        try {
                            if(mCurrentPhotoPath == null) {
                                photoFile = createImageFile();
                            }else {
                                photoFile = new File(mCurrentPhotoPath);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        // If file was successfully created
                        if (photoFile != null) {
                            Uri photoUri = FileProvider.getUriForFile(this,
                                    Constants.APP_AUTHORITY,
                                    photoFile);
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                            startActivityForResult(takePictureIntent, TAKE_PICTURE_CODE);
                        }
                    }
                }else{
                    ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.WRITE_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
                }
                break;
            case R.id.btn_img_pick_dialog_gallery:
                if(imagePickDialog.isShowing()){
                    imagePickDialog.cancel();
                }
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(intent, PICK_IMAGE_CODE);
                break;
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = "Profile_Image";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == STORAGE_PERMISSION_CODE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // If file was successfully created
                    if (photoFile != null) {
                        Uri photoUri = FileProvider.getUriForFile(this,
                                Constants.APP_AUTHORITY,
                                photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                        startActivityForResult(takePictureIntent, TAKE_PICTURE_CODE);
                    }
                }
            }else {
                Toast.makeText(this, "You cannot take a picture without granting this permission", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PICK_IMAGE_CODE && resultCode == RESULT_OK){
                if(data != null){
                    clickedPicture = false;
                    mCurrentPhotoGalleryPath = getAbsolutePath(data.getData());
                    Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoGalleryPath);
                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
                    userProfilePicture.setImageBitmap(scaledBitmap);
                }
        }
        else if(requestCode == TAKE_PICTURE_CODE && resultCode == RESULT_OK){
            clickedPicture = true;
            Bitmap image = BitmapFactory.decodeFile(mCurrentPhotoPath);
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(image, 100, 100, false);
            userProfilePicture.setImageBitmap(scaledBitmap);
        }
    }


    public String getAbsolutePath(Uri uri) {
        String[] projection = { MediaStore.MediaColumns.DATA };
        @SuppressWarnings("deprecation")
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }
}
