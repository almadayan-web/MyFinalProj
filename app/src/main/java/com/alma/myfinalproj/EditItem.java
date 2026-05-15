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
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.alma.myfinalproj.model.Item;
import com.alma.myfinalproj.services.DatabaseService;
import com.alma.myfinalproj.utils.ImageUtil;

public class EditItem extends BaseActivity {  // ← שינוי

    int SELECT_PICTURE = 200;
    private EditText etIname, etIPrice, etISize, etIDetails;
    private Spinner spIType;
    private Button btnGallery, btnCamera, btnAddItem;
    private ImageView ivIPic;
    private DatabaseService databaseService;
    private ActivityResultLauncher<Intent> captureImageLauncher;
    private Intent takeit;
    private String itemId = "";
    private Item currentItem = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_item);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // ← הוספת כפתור התפריט
        ImageButton btnMenu = findViewById(R.id.btnMenu);
        if (btnMenu != null) {
            btnMenu.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
        }

        InitViews();

        takeit = getIntent();
        itemId = takeit.getStringExtra("itemId");

        databaseService = DatabaseService.getInstance();

        if (!itemId.isEmpty()) {
            databaseService.getItem(itemId, new DatabaseService.DatabaseCallback<Item>() {
                @Override
                public void onCompleted(Item item) {
                    currentItem = item;
                    if (currentItem != null) {
                        etIname.setText(currentItem.getName());
                        etISize.setText(currentItem.getSize());
                        etIPrice.setText(currentItem.getPrice() + "");
                        etIDetails.setText(currentItem.getDetails());
                        ivIPic.setImageBitmap(ImageUtil.convertFromivIPic(currentItem.getPic()));
                    }
                }

                @Override
                public void onFailed(Exception e) {}
            });
        }

        ImageUtil.requestPermission(this);

        captureImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Bitmap bitmap = (Bitmap) result.getData().getExtras().get("data");
                        ivIPic.setImageBitmap(bitmap);
                    }
                });

        btnGallery.setOnClickListener(v -> selectImageFromGallery());

        btnCamera.setOnClickListener(v -> captureImageFromCamera());

        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemName = etIname.getText().toString();
                String itemDetails = etIDetails.getText().toString();
                String itemPrice = etIPrice.getText().toString();
                String itemType = spIType.getSelectedItem().toString();
                String itemSize = etISize.getText().toString();
                String imagePic = ImageUtil.convertTo64Base(ivIPic);

                if (itemName.isEmpty() || itemDetails.isEmpty() ||
                        itemPrice.isEmpty() || itemType.isEmpty() || itemSize.isEmpty()) {
                    Toast.makeText(EditItem.this, "אנא מלא את כל השדות", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditItem.this, "המוצר נוסף בהצלחה!", Toast.LENGTH_SHORT).show();
                }

                double price = Double.parseDouble(itemPrice);
                String id = databaseService.generateItemId();
                Item newItem = new Item(id, itemName, itemType, itemSize, price, itemDetails, imagePic);

                databaseService.createNewItem(newItem, new DatabaseService.DatabaseCallback<Void>() {
                    @Override
                    public void onCompleted(Void object) {
                        Log.d("TAG", "Item added successfully");
                        Toast.makeText(EditItem.this, "Item added successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditItem.this, AdminActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailed(Exception e) {
                        Log.e("TAG", "Failed to add item", e);
                        Toast.makeText(EditItem.this, "Failed to add food", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void InitViews() {
        etIname = findViewById(R.id.etInameEdit);
        etIPrice = findViewById(R.id.etIPriceEdit);
        etISize = findViewById(R.id.etISizeEdit);
        etIDetails = findViewById(R.id.etIDetailsEdit);
        spIType = findViewById(R.id.spITypeEdit);
        btnGallery = findViewById(R.id.btnGalleryEdit);
        btnCamera = findViewById(R.id.btnCameraEdit);
        btnAddItem = findViewById(R.id.btnAddItemEdit);
        ivIPic = findViewById(R.id.ivIPicEdit);
    }

    private void selectImageFromGallery() {
        imageChooser();
    }

    private void captureImageFromCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        captureImageLauncher.launch(takePictureIntent);
    }

    void imageChooser() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    ivIPic.setImageURI(selectedImageUri);
                }
            }
        }
    }
}