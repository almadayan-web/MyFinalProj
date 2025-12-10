package com.alma.myfinalproj;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.alma.myfinalproj.model.Item;
import com.alma.myfinalproj.services.DatabaseService;
import com.alma.myfinalproj.utils.ImageUtil;

public class AddItem extends AppCompatActivity {

    private EditText etIname, etIPrice, etISize, etIDetails;
    private Spinner spIType;
    private Button btnGallery, btnCamera, btnAddItem;
    private ImageView ivIPic;
    private DatabaseService databaseService;
    private ActivityResultLauncher<Intent> captureImageLauncher;
    /// Activity result launcher for capturing image from camera


    // constant to compare
    // the activity result code
    int SELECT_PICTURE = 200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_item);

        InitViews();

        /// request permission for the camera and storage
        ImageUtil.requestPermission(this);

        /// get the instance of the database service
        databaseService = DatabaseService.getInstance();


        /// register the activity result launcher for capturing image from camera
        captureImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Bitmap bitmap = (Bitmap) result.getData().getExtras().get("data");
                        ivIPic.setImageBitmap(bitmap);
                    }
                });


        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImageFromGallery();


            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImageFromCamera();

            }
        });

        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemName = etIname.getText().toString();
                String itemDetails = etIDetails.getText().toString();
                String itemPrice = etIPrice.getText().toString();
                String itemType = spIType.getSelectedItem().toString();
                String itemSize = etISize.getText().toString();


                String imagePic = ImageUtil.convertTo64Base(ivIPic);
                double price = Double.parseDouble(itemPrice);

                if (itemName.isEmpty() || itemDetails.isEmpty() ||
                        itemPrice.isEmpty() || itemType.isEmpty() || itemSize.isEmpty()) {
                    Toast.makeText(AddItem.this, "אנא מלא את כל השדות", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddItem.this, "המוצר נוסף בהצלחה!", Toast.LENGTH_SHORT).show();
                }

                /// generate a new id for the item
                String id = databaseService.generateItemId();


                Item newItem = new Item(id, itemName, itemType, itemSize, price, itemDetails, imagePic);


                /// save the item to the database and get the result in the callback
                databaseService.createNewItem(newItem, new DatabaseService.DatabaseCallback<Void>() {
                    @Override
                    public void onCompleted(Void object) {
                        Log.d("TAG", "Item added successfully");
                        Toast.makeText(AddItem.this, "Item added successfully", Toast.LENGTH_SHORT).show();
                        /// clear the input fields after adding the item for the next item
                        Log.d("TAG", "Clearing input fields");

                        Intent intent = new Intent(AddItem.this, AdminActivity.class);
                        startActivity(intent);


                    }

                    @Override
                    public void onFailed(Exception e) {
                        Log.e("TAG", "Failed to add item", e);
                        Toast.makeText(AddItem.this, "Failed to add food", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void InitViews() {
        etIname = findViewById(R.id.etIname);
        etIPrice = findViewById(R.id.etIPrice);
        etISize = findViewById(R.id.etISize);
        etIDetails = findViewById(R.id.etIDetails);
        spIType = findViewById(R.id.spIType);
        btnGallery = findViewById(R.id.btnGallery);
        btnCamera = findViewById(R.id.btnCamera);
        btnAddItem = findViewById(R.id.btnAddItem);
        ivIPic = findViewById(R.id.ivIPic);
    }


    /// select image from gallery
    private void selectImageFromGallery() {

        imageChooser();
    }

    /// capture image from camera
    private void captureImageFromCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        captureImageLauncher.launch(takePictureIntent);
    }


    void imageChooser() {

        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    // this function is triggered when user
    // selects the image from the imageChooser
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    ivIPic.setImageURI(selectedImageUri);
                }
            }
        }
    }
}