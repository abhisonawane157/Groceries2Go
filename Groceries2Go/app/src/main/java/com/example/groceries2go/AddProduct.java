package com.example.groceries2go;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class AddProduct extends AppCompatActivity {


    ImageButton btnBack;
    ImageView add_img, remove_img, profileImageview;
    Button btnRegister;

    EditText edName, edDetails, edPrice, edCategory, edpid, edsid;

    public final int REQ_CD_PROFILEPIC = 101;
    private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
    private FirebaseStorage _firebase_storage = FirebaseStorage.getInstance();

    private HashMap<String, Object> hashmap = new HashMap<>();
    private String profileimgpath = "";
    private String profileimgname = "";
    private double img = 0;
    private double n = 0;
    private double exist = 0;

    private ArrayList<String> liststring = new ArrayList<>();
    private ArrayList<HashMap<String, Object>> listmap = new ArrayList<>();

    private String str, str1 = "";
    private Intent intent = new Intent();
    private DatabaseReference Products = _firebase.getReference("Products");
    private ChildEventListener _Products_child_listener;
    private StorageReference productpic = _firebase_storage.getReference("productpic");
    private OnCompleteListener<Uri> _productpic_upload_success_listener;
    private OnSuccessListener<FileDownloadTask.TaskSnapshot> _productpic_download_success_listener;
    private OnSuccessListener _productpic_delete_success_listener;
    private OnProgressListener _productpic_upload_progress_listener;
    private OnProgressListener _productpic_download_progress_listener;
    private OnFailureListener _productpic_failure_listener;
    private Intent profilepic = new Intent(Intent.ACTION_GET_CONTENT);
    private SharedPreferences file;
    private ProgressDialog progressDialog;
    private long profileimgsize = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        com.google.firebase.FirebaseApp.initializeApp(this);
        initialize();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);

        }
        img = 0;
    }

    private void initialize() {
        add_img = findViewById(R.id.add_img);
        remove_img = findViewById(R.id.del_img);
        profileImageview = findViewById(R.id.imageview1);
        edName = findViewById(R.id.edName);
        edDetails = findViewById(R.id.edDetails);
        edPrice = findViewById(R.id.edPrice);
//        edCity = findViewById(R.id.edCity);
        edCategory = findViewById(R.id.edCategory);
        edpid = findViewById(R.id.edpid);
        edsid = findViewById(R.id.edsid);
        progressDialog = new ProgressDialog(AddProduct.this);

        btnRegister = findViewById(R.id.btnRegister);
        btnBack = findViewById(R.id.btnBack);

        // imgProfile=findViewById(R.id.imgProfile);
//        tvRegisterAsSeller.setOnClickListener(this);
//
//        btnBack.setOnClickListener(this);
//        btnRegister.setOnClickListener(this);
        // imgProfile.setOnClickListener(this);

//        userDatabase= FirebaseDatabase.getInstance().getReference("User");

        edCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(AddProduct.this, edCategory);
                Menu menu = popupMenu.getMenu();
                menu.add("Beverages");
                menu.add("Beauty & Personal Care");
                menu.add("Biscuits Snacks & Chocolates");
                menu.add("Breakfast & Dairy");
                menu.add("Cooking Needs");
                menu.add("Frozen Food");
                menu.add("Fruits");
                menu.add("Vegetables");
                menu.add("Others");
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch(item.getTitle().toString())
                        {
                            case "Beverages":
                                edCategory.setText("Beverages");
                                break;
                            case "Beauty & Personal Care":
                                edCategory.setText("Beauty & Personal Care");
                                break;
                            case "Biscuits Snacks & Chocolates":
                                edCategory.setText("Biscuits Snacks & Chocolates");
                                break;
                            case "Breakfast & Dairy":
                                edCategory.setText("Breakfast & Dairy");
                                break;
                            case "Cooking Needs":
                                edCategory.setText("Cooking Needs");
                                break;
                            case "Frozen Food":
                                edCategory.setText("Frozen Food");
                                break;
                            case "Fruits":
                                edCategory.setText("Fruits");
                                break;
                            case "Vegetables":
                                edCategory.setText("Vegetables");
                                break;
                            case "Others":
                                edCategory.setText("Others");
                                break;
                        }

                        return true;
                    }
                });
                popupMenu.show();
            }
        });
        profilepic.setType("image/*");
        profilepic.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        file = getSharedPreferences("file", Activity.MODE_PRIVATE);
        edsid.setText(file.getString("sellerId",""));
        Random random = new Random();
        edpid.setText("P"+String.valueOf(random.nextInt(9999)));

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        profileImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                startActivityForResult(profilepic, REQ_CD_PROFILEPIC);
            }
        });

        add_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(profilepic, REQ_CD_PROFILEPIC);
            }
        });
        remove_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileimgpath = "";
                profileimgname = "";
                profileImageview.setImageResource(R.drawable.default_profile);
                img = 0;
                add_img.setVisibility(View.VISIBLE);
                remove_img.setVisibility(View.GONE);
            }
        });



    btnRegister.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick (View _view){
        if (edpid.getText().toString().trim().equals("") && (edName.getText().toString().trim().equals("") && ((edDetails.getText().toString().trim().equals("") && (edPrice.getText().toString().trim().equals("")) && (edCategory.getText().toString().trim().equals("")))))) {
            Toast.makeText(AddProduct.this, "Please enter valid credentials", Toast.LENGTH_SHORT).show();
        } else {
            if (img == 0)
            {
                progressDialog.show();
                progressDialog.setContentView(R.layout.custom_loading_box);
                progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                progressDialog.setCanceledOnTouchOutside(false);
                n = 0;
                exist = 0;
                for (int _repeat156 = 0; _repeat156 < (int) (listmap.size()); _repeat156++) {
                    if (listmap.get((int) n).get("pId").toString().toLowerCase().equals(edpid.getText().toString().toLowerCase())) {
                        Toast.makeText(AddProduct.this, "Id Already in Use", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        exist = 1;
                    }
                    n++;
                }
                if (exist == 0) {

                    hashmap = new HashMap<>();
                    hashmap.put("sellerId", edsid.getText().toString().trim());
                    hashmap.put("pId", edpid.getText().toString().trim());
                    hashmap.put("pName", edName.getText().toString().trim());
                    hashmap.put("details", edDetails.getText().toString().trim());
                    hashmap.put("category", edCategory.getText().toString().trim());
                    hashmap.put("price", edPrice.getText().toString().trim());
                    hashmap.put("availability","1");
                    hashmap.put("photo", "");
                    Products.child(edpid.getText().toString()).updateChildren(hashmap);
                    Toast.makeText(AddProduct.this, "Added Product Successfully.", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    finish();
                } else {

                }
            } else {
                progressDialog.show();
                progressDialog.setContentView(R.layout.custom_loading_box);
                progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                progressDialog.setCanceledOnTouchOutside(false);

                n = 0;
                exist = 0;
                for (int _repeat196 = 0; _repeat196 < (int) (listmap.size()); _repeat196++) {
                    if (listmap.get((int) n).get("pId").toString().toLowerCase().equals(edpid.getText().toString().toLowerCase())) {
                        Toast.makeText(AddProduct.this, "Id Already in Use", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        exist = 1;
                    }
                    n++;
                }
                if (exist == 0) {
                    productpic.child(profileimgname).putFile(Uri.fromFile(new File(profileimgpath))).addOnFailureListener(_productpic_failure_listener).addOnProgressListener(_productpic_upload_progress_listener).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(Task<UploadTask.TaskSnapshot> task) throws Exception {
                            return productpic.child(profileimgname).getDownloadUrl();
                        }
                    }).addOnCompleteListener(_productpic_upload_success_listener);
                    btnRegister.setEnabled(false);
                } else {

                }
            }
        }
    }
    });

    _Products_child_listener =new ChildEventListener(){
        @Override
        public void onChildAdded (DataSnapshot _param1, String _param2){
            GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
            };
            final String _childKey = _param1.getKey();
            final HashMap<String, Object> _childValue = _param1.getValue(_ind);
            Products.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot _dataSnapshot) {
                    listmap = new ArrayList<>();
                    try {
                        GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                        };
                        for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                            HashMap<String, Object> _map = _data.getValue(_ind);
                            listmap.add(_map);
                        }
                    } catch (Exception _e) {
                        _e.printStackTrace();
                    }

                }

                @Override
                public void onCancelled(DatabaseError _databaseError) {
                }
            });
        }

        @Override
        public void onChildChanged (DataSnapshot _param1, String _param2){
            GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
            };
            final String _childKey = _param1.getKey();
            final HashMap<String, Object> _childValue = _param1.getValue(_ind);

        }

        @Override
        public void onChildMoved (DataSnapshot _param1, String _param2){

        }

        @Override
        public void onChildRemoved (DataSnapshot _param1){
            GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
            };
            final String _childKey = _param1.getKey();
            final HashMap<String, Object> _childValue = _param1.getValue(_ind);

        }

        @Override
        public void onCancelled (DatabaseError _param1){
            final int _errorCode = _param1.getCode();
            final String _errorMessage = _param1.getMessage();

        }
    };
    Products.addChildEventListener(_Products_child_listener);


    _productpic_upload_progress_listener =new OnProgressListener<UploadTask.TaskSnapshot>(){
        @Override
        public void onProgress (UploadTask.TaskSnapshot _param1){
        double _progressValue = (100.0 * _param1.getBytesTransferred()) / _param1.getTotalByteCount();

    }
    };
    _productpic_download_progress_listener =new OnProgressListener<FileDownloadTask.TaskSnapshot>(){
        @Override
        public void onProgress (FileDownloadTask.TaskSnapshot _param1){
        double _progressValue = (100.0 * _param1.getBytesTransferred()) / _param1.getTotalByteCount();

    }
    };
    _productpic_upload_success_listener =new OnCompleteListener<Uri>(){
        @Override
        public void onComplete (Task < Uri > _param1) {
        final String _downloadUrl = _param1.getResult().toString();
            hashmap = new HashMap<>();
            hashmap.put("sellerId", edsid.getText().toString().trim());
            hashmap.put("pId", edpid.getText().toString().trim());
            hashmap.put("pName", edName.getText().toString().trim());
            hashmap.put("details", edDetails.getText().toString().trim());
            hashmap.put("category", edCategory.getText().toString().trim());
            hashmap.put("price", edPrice.getText().toString().trim());
            hashmap.put("availability","1");
            hashmap.put("photo", _downloadUrl);
            Products.child(edpid.getText().toString()).updateChildren(hashmap);
            Toast.makeText(AddProduct.this, "Added Product Successfully.", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            finish();
    }
    };
    _productpic_download_success_listener =new OnSuccessListener<FileDownloadTask.TaskSnapshot>(){
        @Override
        public void onSuccess (FileDownloadTask.TaskSnapshot _param1){
        final long _totalByteCount = _param1.getTotalByteCount();

    }
    };
    _productpic_delete_success_listener =new OnSuccessListener() {
        @Override
        public void onSuccess (Object _param1){

        }
    };
    _productpic_failure_listener =new OnFailureListener() {
        @Override
        public void onFailure (Exception _param1){
            final String _message = _param1.getMessage();

        }
    };
}

    @Override
    protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
        super.onActivityResult(_requestCode, _resultCode, _data);

        switch (_requestCode) {
            case REQ_CD_PROFILEPIC:
                if (_resultCode == Activity.RESULT_OK) {
                    ArrayList<String> _filePath = new ArrayList<>();
                    if (_data != null) {
                        if (_data.getClipData() != null) {
                            for (int _index = 0; _index < _data.getClipData().getItemCount(); _index++) {
                                ClipData.Item _item = _data.getClipData().getItemAt(_index);
                                _filePath.add(FileUtil.convertUriToFilePath(getApplicationContext(), _item.getUri()));
                            }
                        }
                        else {
                            _filePath.add(FileUtil.convertUriToFilePath(getApplicationContext(), _data.getData()));
                        }
                    }
                    add_img.setVisibility(View.GONE);
                    remove_img.setVisibility(View.VISIBLE);

                    profileimgname = "";
                    profileimgpath = "";
                    profileimgsize = 0;
                    img = 1;
                    profileimgpath = _filePath.get((int)(0));
                    profileimgname = Uri.parse(_filePath.get((int)(0))).getLastPathSegment();
                    profileImageview.setImageBitmap(FileUtil.decodeSampleBitmapFromPath(_filePath.get((int)(0)), 1024, 1024));
                    profileImageview.setVisibility(View.VISIBLE);
                    profileimgsize= new java.io.File(profileimgpath).length()/1024;
                    if ((profileimgsize < 2000) && (profileimgsize > 0)) {
                        profileimgpath = _filePath.get((int)(0));
                        profileimgname = Uri.parse(profileimgpath).getLastPathSegment();
                        profileImageview.setImageBitmap(FileUtil.decodeSampleBitmapFromPath(profileimgpath, 1024, 1024));
                        img=1;
                        add_img.setVisibility(View.GONE);
                        remove_img.setVisibility(View.VISIBLE);
                    }
                    else {
                        Toast.makeText(this, "Profile Pic must be less than 2Mb", Toast.LENGTH_SHORT).show();
                        profileimgpath = "";
                        profileimgname = "";
                        profileImageview.setImageResource(R.drawable.default_profile);
                        img = 0;
                        add_img.setVisibility(View.VISIBLE);
                        remove_img.setVisibility(View.GONE);
                    }

                }
                else {

                }
                break;
            default:
                break;
        }
    }

}
//
//    In this activity, we have used an instance of Realtime Db's Products node and Firebase Storage's
//productpic folder. As we are going to use this activity to push the data in database, basically the data
//regarding the products in the database, so to push text data we will use realtime db and for product image
// we will be using firbase storage.
//
//Line 112:
//
//Initially, we are checking the appropriate permission, whether the application have permission to access
//the files or not, if no permission is given then again it will ask for permission, without permission
//application will be unable to set the image of product.class
//
//Line 145-198:
//
//    Here we had used a popupmenu to view the category of the product, which must be choosen by end user
//, those categories will be visible on OnCLick event listener.class
//
//Line 212-224:
//
//    In this On Button click listener we are triggering an intent to jump to explorer to choose an image
//from gallery, intent can be used to view the gallery and choose an image, for that we had used commands on
//line 199 and 200 image/* means after jumping from app to explorer to choose image, it will show only image
//format files, if you set it as */* it will show all format files.
//
//
//Line 239-427:
//
//    This section is all about communication between Database and application,
//On button click firstly it will check whether all the inputs are proper or not, not a single input must be
//null, as many tester for testing purpose they gave input as a blank space to nullify those loophole of code
//we had used trim() to remove extra blank spaces from input. After that we had set flag variable, by using
//this flag variable we can make sure whether the input of image for product is given or not, if no image is
//choosen for product then only text inputs will be pushed in Db and following commands will get executed, if
//image is been choosen the different set of code will get executed.
//
//line 247 we had use progressdialog box, it will visible during the process of pushing or uploading the data
//on database. after uploading it will be invisible. After that we are checking whether the randomly generated
// productId is already exit or not, if it exist a toast will show and again end user can try.
//
//Line 311:
//
//    In this section, application is contacting to Product named node of Database, this contains 4 event
// listener, onChildAdded, onChildChanged, onChildRemoved, onChildMoved, 5th one is rarely used but every
//event listener is important to mention in code
//
//
//if end user have choosen an image as product image then Line 280 and 388 will be executing
//if not no image is been choosen then from line 247 execution will start
//
//Line 430-488
//
//    As we are using intent to choose an image, then we have to write the code to receive it too. This section
//will work for that by using the section's code application can access the filename file path, and even file size
//and set it according to the imageview dimensions.
