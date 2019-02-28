package com.codexaxor.mac.googlevisionv1;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageActivity;
import com.theartofdev.edmodo.cropper.CropImageView;

public class MainActivity extends AppCompatActivity {

    EditText mResultEt;
    ImageView mPreviewIv;

    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 400;
    private static final int IMAGE_PICK_GALLERY_CODE = 1000;
    private static final int IMAGE_PICK_CAMERA_CODE = 1001;

    String cameraPermission[];
    String storagePermission[];


    Uri image_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setSubtitle("Click + Image Insert");

        mResultEt = findViewById(R.id.resultEt);
        mPreviewIv = findViewById(R.id.imageIv);

        /// Camera Permission

        cameraPermission = new String[] {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};

        /// Storage Permission
        storagePermission = new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    }


    // actionBar MEnu


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /// inFlate MEnu
        getMenuInflater().inflate(R.menu.menu_main,menu);

        return true;
    }


    // handle actionbar Item Clicks
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.addImage){
            showImageImportDialog();
        }
        if (id == R.id.settings){
            Toast.makeText(this,"Settings", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    private void showImageImportDialog() {

        // Item To Display inDialog

        String[] items = {" Camera", " Gallery"};
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        // Set Title

        dialog.setTitle("Select Image");
        dialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                if (which == 0){
                    // camera options is Clicked

                    if (!checkCameraPermission()){    /// if camera Permission not granted

                        requestCameraPermission();   /// ask for Permission


                    }
                    else{

                        /// take snaps

                        pickCamera();

                    }
                }
                if (which == 1){
                    // Gallery options is Clicked

                    if (!checkStoragePermission()){    /// if storage Permission not granted

                        requestStoragePermission();   /// ask for storage Permission


                    }
                    else{

                        /// take snaps

                        pickGallery();

                    }
                }
            }
        });
        dialog.create().show();


    }

    private void pickGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_PICK_GALLERY_CODE);
    }

    private void pickCamera() {
        ContentValues values = new ContentValues();

        values.put(MediaStore.Images.Media.TITLE, "New PIC");
        values.put(MediaStore.Images.Media.DESCRIPTION, "ImAgE to TEXT");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        //// Facing Problems camerAIntent

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent,IMAGE_PICK_CAMERA_CODE);

    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE);
    }

    private boolean checkStoragePermission() {

        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result;

    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermission, CAMERA_REQUEST_CODE);
    }

    private boolean checkCameraPermission() {

        /// inorder ot Save image in highQuality we need to save in ExternalStorage

        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result && result1;
    }


    /// Handle Permission Result

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){

            case CAMERA_REQUEST_CODE:
                if (grantResults.length>0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (cameraAccepted && writeStorageAccepted) pickCamera();
                    else Toast.makeText(this,"PERMISSION DENIED", Toast.LENGTH_SHORT).show();

                }
                break;


            case STORAGE_REQUEST_CODE:
                if (grantResults.length>0){
                   boolean writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (writeStorageAccepted) pickGallery();
                    else Toast.makeText(this,"PERMISSION DENIED", Toast.LENGTH_SHORT).show();

                }
                break;


        }
    }

    // Handle Image Result


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK){

            if (requestCode == IMAGE_PICK_GALLERY_CODE){
                // crop image from gallery

                CropImage.activity(data.getData()).setGuidelines(CropImageView.Guidelines.ON).start(this);

            }

            if (requestCode == IMAGE_PICK_CAMERA_CODE){
                // crop image from gallery

                CropImage.activity(image_uri).setGuidelines(CropImageView.Guidelines.ON).start(this);

            }


        }


        /// Get Cropped Image

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK){
                Uri resultUri = result.getUri();
                mPreviewIv.setImageURI(resultUri);

                /// get Image for Text Recognition from BitMAP

                BitmapDrawable bitmapDrawable = (BitmapDrawable)mPreviewIv.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();

                TextRecognizer recognizer = new TextRecognizer.Builder(getApplicationContext()).build();

                if (!recognizer.isOperational()){
                    Toast.makeText(this,"Error Generating",Toast.LENGTH_SHORT).show();
                }
                else {

                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<TextBlock> items = recognizer.detect(frame);
                    StringBuilder sb = new StringBuilder();

                    /// check text until sb is not empty

                    for(int i=0;i<items.size();i++){

                        TextBlock myItem = items.valueAt(i);
                        sb.append(myItem.getValue());
                        sb.append("\n");


                    }
                    /// Edit Text
                    mResultEt.setText(sb.toString());

                }
            }

            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){


                /// Show any other ERRORS
                Exception error = result.getError();
                Toast.makeText(this, ""+error, Toast.LENGTH_SHORT).show();


            }

        }
    }
}
