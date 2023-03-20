package com.example.ausadibigya;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.google.firebase.ml.vision.text.RecognizedLanguage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Intent data;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.camerabata:
                camerakhol();
                break;
            case R.id.gallerybata:
                gallerykhol();
                break;
             default:
                break;
        }
    }


    ActivityResultLauncher<Intent> cameraintent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == RESULT_OK && result.getData() != null)
            {
                Bundle bundle = result.getData().getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");

                if(bitmap != null)
                {
                    FirebaseVisionImage vision = FirebaseVisionImage.fromBitmap(bitmap);
                    FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance()
                            .getOnDeviceTextRecognizer();
                            detector.processImage(vision)
                            .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                                @Override
                                public void onSuccess(FirebaseVisionText firebaseVisionText) {
                                    displayText(firebaseVisionText);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MainActivity.this, "Bhayena", Toast.LENGTH_SHORT).show();
                                }
                            });



                }

            }
        }
    });
    public void Pass_garni(String s) {
        Intent intent = new Intent(MainActivity.this, Details.class);
        intent.putExtra("ausadi", s);
        startActivity(intent);
    }

    public void displayText(FirebaseVisionText firebaseVisionText) {
        String resultText = firebaseVisionText.getText();
        TextView meow = findViewById(R.id.textView5);
        if (resultText.isEmpty()) {
            meow.setText("Text Not Found!");
        }
        else {

            for (FirebaseVisionText.TextBlock block: firebaseVisionText.getTextBlocks()) {
                String blockText = block.getText().toUpperCase().toString();
                Float blockConfidence = block.getConfidence();
                List<RecognizedLanguage> blockLanguages = block.getRecognizedLanguages();
                Point[] blockCornerPoints = block.getCornerPoints();
                Rect blockFrame = block.getBoundingBox();

                    if (blockText.contentEquals("PARACETAMOL")) {
                        Pass_garni("PARACETAMOL");
                    } else if(blockText.contentEquals("ACILOC")) {
                        Pass_garni("ACILOC");
                    }
                    else if (blockText.contentEquals("IBUPROFEN")) {
                        Pass_garni("IBUPROFEN");
                    }
                    else if (blockText.contentEquals("ASPIRIN")) {
                        Pass_garni("ASPIRIN");
                    }
                    else if (blockText.contentEquals("DICLOFENAC")) {
                        Pass_garni("DICLOFENAC");
                    }
                    else if (blockText.contentEquals("LOPERAMIDE")) {
                        Pass_garni("LOPERAMIDE");
                    }

                    else if (blockText.contentEquals("DULCOLAX LAXATIVE")) {
                        Pass_garni("DULCOLAX LAXATIVE");
                    }
                    else if (blockText.contentEquals("DULCOLAX")) {
                        Pass_garni("DULCOLAX LAXATIVE");
                    }
                    else if (blockText.contentEquals("ALLORIC")) {
                        Pass_garni("ALLORIC");
                    }
                    else {
                        meow.setText("Ausadi List Ma Xaina! = " + blockText);
                    }

            }

            }


        }

    ActivityResultLauncher<Intent> galleryintent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>(){
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == RESULT_OK && result.getData() != null) {
                    data = result.getData();

                if(data != null) {
                    Uri selectedImageUri = data.getData();
                    Bitmap selectedImageBitmap;
                    try {
                        selectedImageBitmap =  MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                        FirebaseVisionImage visionImage = FirebaseVisionImage.fromBitmap(selectedImageBitmap);
                        FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance()
                                .getOnDeviceTextRecognizer();
                        detector.processImage(visionImage)
                                .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                                    @Override
                                    public void onSuccess(FirebaseVisionText firebaseVisionText) {
                                        displayText(firebaseVisionText);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(MainActivity.this, "Error Occurred", Toast.LENGTH_SHORT).show();
                                    }
                                });



                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button camera = findViewById(R.id.camerabata);
        camera.setOnClickListener(this);

        Button gallery = findViewById(R.id.gallerybata);
        gallery.setOnClickListener(this);





    }



    public void camerakhol() {
        Intent camerakhol = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraintent.launch(camerakhol);


    }
    public void gallerykhol() {
        Intent gallerykhol = new Intent();
        gallerykhol.setType("image/*");
        gallerykhol.setAction(Intent.ACTION_GET_CONTENT);
        galleryintent.launch(gallerykhol);


    }
}