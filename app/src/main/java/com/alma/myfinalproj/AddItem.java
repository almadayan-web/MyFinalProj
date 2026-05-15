package com.alma.myfinalproj;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import com.alma.myfinalproj.model.Item;
import com.alma.myfinalproj.services.DatabaseService;
import com.alma.myfinalproj.utils.ImageUtil;
import com.google.firebase.auth.FirebaseAuth;

public class AddItem extends BaseAdminActivity {

    int SELECT_PICTURE = 200;
    private EditText etIname, etIPrice, etISize, etIDetails;
    private Spinner spIType;
    private Button btnGallery, btnCamera, btnAddItem;
    private ImageView ivIPic;
    private DatabaseService databaseService;
    private ActivityResultLauncher<Intent> captureImageLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_item);

        // ← הוספת כפתור התפריט
        ImageButton btnMenu = findViewById(R.id.btnMenu);
        if (btnMenu != null) {
            btnMenu.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
        }
        InitViews();
        ImageUtil.requestPermission(this);
        databaseService = DatabaseService.getInstance();

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

        btnAddItem.setOnClickListener(v -> {
            String itemName = etIname.getText().toString();
            String itemDetails = etIDetails.getText().toString();
            String itemPrice = etIPrice.getText().toString();
            String itemType = spIType.getSelectedItem().toString();
            String itemSize = etISize.getText().toString();
            String imagePic = ImageUtil.convertTo64Base(ivIPic);

            if (itemName.isEmpty() || itemDetails.isEmpty() ||
                    itemPrice.isEmpty() || itemType.isEmpty() || itemSize.isEmpty()) {
                Toast.makeText(AddItem.this, "אנא מלא את כל השדות", Toast.LENGTH_SHORT).show();
                return;
            }

            double price = Double.parseDouble(itemPrice);
            String id = databaseService.generateItemId();
            Item newItem = new Item(id, itemName, itemType, itemSize, price, itemDetails, imagePic);

            databaseService.createNewItem(newItem, new DatabaseService.DatabaseCallback<Void>() {
                @Override
                public void onCompleted(Void object) {
                    Toast.makeText(AddItem.this, "המוצר נוסף בהצלחה!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddItem.this, AdminActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onFailed(Exception e) {
                    Toast.makeText(AddItem.this, "שגיאה בהוספת המוצר", Toast.LENGTH_SHORT).show();
                }
            });
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
        if (resultCode == RESULT_OK && requestCode == SELECT_PICTURE) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                ivIPic.setImageURI(selectedImageUri);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_homeadmin) {
            startActivity(new Intent(this, AdminActivity.class));
        }
        else if (id == R.id.nav_signOutadmin) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, Landing.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (id == R.id.nav_additem) {
            startActivity(new Intent(this, AddItem.class));
        } else if (id == R.id.nav_listuser) {
            startActivity(new Intent(this, UsersListActivity.class));
        } else if (id == R.id.nav_listitem) {
            startActivity(new Intent(this, ItemsList.class));
        } else if (id == R.id.nav_showorders) {
            startActivity(new Intent(this, AllOrders.class));
        }

        return true;
    }
}