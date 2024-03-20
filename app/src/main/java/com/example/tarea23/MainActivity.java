package com.example.tarea23;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int peticion_acceso_camara = 101;
    private static final int peticion_toma_fotografia = 102;

    private ImageView imageView;
    private EditText descriptionEditText;
    private Button captureButton;
    private Button saveButton;
    private Button listButton;

    private DatabaseHelper databaseHelper;
    private String currentPhotoPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.placeholder_image);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        captureButton = findViewById(R.id.captureButton);
        saveButton = findViewById(R.id.saveButton);
        listButton = findViewById(R.id.listButton);
        databaseHelper = new DatabaseHelper(this);

        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permisos();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
                if (drawable != null) {
                    String description = descriptionEditText.getText().toString();
                    if (!description.trim().isEmpty()) {
                        savePhotograph();
                    } else {
                        Toast.makeText(MainActivity.this, "Por favor, ingresa una descripción", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Por favor, toma una foto primero", Toast.LENGTH_SHORT).show();
                }
            }
        });

        listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, activity_listview.class);
                startActivity(intent);
            }
        });
    }

    private void permisos() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, peticion_acceso_camara);
        } else {
            dispatchTakePictureIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == peticion_acceso_camara) {
            if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(getApplicationContext(), "Permiso denegado", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            try {
                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                imageView.setImageBitmap(imageBitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    private void savePhotograph() {
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap imageBitmap = drawable.getBitmap();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        String description = descriptionEditText.getText().toString();

        long id = databaseHelper.insertPhotograph(byteArray, description);
        if (id != -1) {

            Toast.makeText(this, "Foto guardada con éxito", Toast.LENGTH_SHORT).show();

            clearFields();
        } else {

            Toast.makeText(this, "Error al guardar la foto", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearFields() {

        imageView.setImageResource(R.drawable.placeholder_image);

        descriptionEditText.setText("");
    }
}