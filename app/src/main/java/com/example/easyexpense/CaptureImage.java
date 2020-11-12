package com.example.easyexpense;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class CaptureImage extends AppCompatActivity {


    private static int RESULT_LOAD_IMAGE = 1;
    private static final int CAMERA_REQUEST = 1888;
    private static final int OPEN_GALLARY = 999;
    String encodedImage = null;

    String TAG = "MAIN ACTIVITY";

    // Getting GUI data
    Button buttonLoadImage = null;
    Button camera_button = null;
    ImageView imageView = null;
    Bitmap bitmap = null;
    StringBuffer finalText = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_image);

        buttonLoadImage = findViewById(R.id.upload_btn);
        camera_button = findViewById(R.id.camera_button);

    }

    // This function will called when picture will be selected
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == OPEN_GALLARY && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri sourceUri = data.getData();
            File file = getImageFile();
            Uri destinationUri = Uri.fromFile(file);
            openCropActivity(sourceUri, destinationUri);

        }
        // This will be used once camera module will be activated...
        else if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            bitmap = (Bitmap) data.getExtras().get("data");//this is your bitmap image and now you can do whatever you want with this
        }
        else if(requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK)
        {
            Uri uri = UCrop.getOutput(data);
            showImage(uri);
        }

    }

    // This function will take photo
    String currentPhotoPath = "";
    private File getImageFile() {
        String imageFileName = "JPEG_" + System.currentTimeMillis() + "_";
        File storageDir = new File(
                Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DCIM
                ), "Camera"
        );
        File file = null;
        try {
            file = File.createTempFile(
                    imageFileName, ".jpg", storageDir
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        currentPhotoPath = "file:" + file.getAbsolutePath();
        return file;
    }

    private void openCropActivity(Uri sourceUri, Uri destinationUri) {
        UCrop.of(sourceUri, destinationUri)
//                .withMaxResultSize(450, 450)
//                .withAspectRatio(5f, 5f)
                .start(this);
    }

    private void showImage(Uri imageUri) {
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        imageView = (ImageView) findViewById(R.id.imgView);

        imageView.setImageBitmap(bitmap);

        // This module is for OCR
        img_to_text(bitmap);

    }

    private void img_to_text(Bitmap B)
    {
        if (B != null) {
            TextRecognizer textRecognizer = new TextRecognizer.Builder(this).build();

//            Toast.makeText(this, B.toString() , Toast.LENGTH_SHORT).show();
            Frame imageFrame = new Frame.Builder()
                    .setBitmap(B)
                    .build();

            SparseArray<TextBlock> textBlocks = textRecognizer.detect(imageFrame);

            finalText = new StringBuffer("Journalbytecode");

            for (int i = 0; i < textBlocks.size(); i++) {

                TextBlock textBlock = textBlocks.get(textBlocks.keyAt(i));

                String text = textBlock.getValue();

                finalText.append(text); // This is the OCR text

            }

        } else {
            Toast.makeText(this, "Bitmap Error", Toast.LENGTH_SHORT).show();
        }
    }

    public void textshow(View view) {

//         This is for OCR (Just uncomment it)
//        Intent intent = new Intent(CaptureImage.this, OCR_Text.class);
//        intent.putExtra("FinalText",finalText.toString());
//        startActivity(intent);

        // imageSend is for sending an image on database


//        imageSend imagesend = new imageSend(CaptureImage.this, finalText.toString());
//        imageSend imagesend = new imageSend(CaptureImage.this);
//        imagesend.execute(encodedImage);


        final SweetAlertDialog pDialog = new SweetAlertDialog(CaptureImage.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading ...");
        pDialog.setCancelable(true);
        pDialog.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                pDialog.dismiss();
                Intent intent = new Intent(CaptureImage.this,OCR_Text.class);
                intent.putExtra("encodedImage",encodedImage);
                startActivity(intent);
            }
        }, 3000);

    }

    public void captureImage(View view) {
        Intent pictureIntent = new Intent(Intent.ACTION_GET_CONTENT);
        pictureIntent.setType("image/*");  // 1
        pictureIntent.addCategory(Intent.CATEGORY_OPENABLE);  // 2
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            String[] mimeTypes = new String[]{"image/jpeg", "image/png"};  // 3
            pictureIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        }
        startActivityForResult(Intent.createChooser(pictureIntent,"Select Picture"), OPEN_GALLARY);
    }

    public void clickPhoto(View view) {
        AlertDialog.Builder adb = new AlertDialog.Builder(CaptureImage.this);
        adb.setTitle("Alert");
        adb.setMessage("This module is under development");
        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        adb.show();
//                Intent cameraIntent = new  Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }
}

