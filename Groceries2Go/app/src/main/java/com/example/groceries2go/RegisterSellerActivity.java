package com.example.groceries2go;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class RegisterSellerActivity extends AppCompatActivity{

    ImageButton btnBack;
    ImageView add_img, remove_img, profileImageview;
    Button btnRegister;

    EditText edShopName, edDeliveryFee, edName,edPhone,edCountry,edState,edCity,edAddress,edId;
    EditText edEmail,edPassword,edConfirmPassword;
    TextView tvRegisterAsUser;

//    DatabaseReference userDatabase,childDatabase;

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

    private String str,str1 ="";
    private Intent intent = new Intent();
    private DatabaseReference Currdata = _firebase.getReference("Seller");
    private ChildEventListener _Currdata_child_listener;
    private FirebaseAuth Currauth;
    private OnCompleteListener<AuthResult> _Currauth_create_user_listener;
    private OnCompleteListener<AuthResult> _Currauth_sign_in_listener;
    private OnCompleteListener<Void> _Currauth_reset_password_listener;
    private StorageReference profile = _firebase_storage.getReference("sellerprofile");
    private OnCompleteListener<Uri> _profile_upload_success_listener;
    private OnSuccessListener<FileDownloadTask.TaskSnapshot> _profile_download_success_listener;
    private OnSuccessListener _profile_delete_success_listener;
    private OnProgressListener _profile_upload_progress_listener;
    private OnProgressListener _profile_download_progress_listener;
    private OnFailureListener _profile_failure_listener;
    private Intent profilepic = new Intent(Intent.ACTION_GET_CONTENT);
    private SharedPreferences file;
    private ProgressDialog progressDialog;
    private long profileimgsize=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_seller);
        com.google.firebase.FirebaseApp.initializeApp(this);
        initialize();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);

        }
        img = 0;
    }
    private void initialize()
    {
    Currauth = FirebaseAuth.getInstance();
    add_img = findViewById(R.id.add_img);
    remove_img = findViewById(R.id.del_img);
    profileImageview = findViewById(R.id.imageview1);
    edName=findViewById(R.id.edName);
    edDeliveryFee = findViewById(R.id.edDeliveryFee);
    edShopName = findViewById(R.id.edShopName);
    edPhone=findViewById(R.id.edPhone);
    edCountry=findViewById(R.id.edCountry);
    edState=findViewById(R.id.edState);
    edCity=findViewById(R.id.edCity);
    edAddress=findViewById(R.id.edAddress);
    edEmail=findViewById(R.id.edEmail);
    edPassword=findViewById(R.id.edPassword);
    edConfirmPassword=findViewById(R.id.edConfirmPassword);
    edId= findViewById(R.id.edId);
    progressDialog = new ProgressDialog(RegisterSellerActivity.this);

    tvRegisterAsUser=findViewById(R.id.tvRegisterAsUser);
    btnRegister=findViewById(R.id.btnRegister);
    btnBack=findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        profilepic.setType("image/*");
        profilepic.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
    file = getSharedPreferences("file",Activity.MODE_PRIVATE);

    Random random = new Random();
        edId.setText("S"+String.valueOf(random.nextInt(9999)));

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
        public void onClick(View _view) {
            if (edId.getText().toString().trim().equals("") &&
                    (edName.getText().toString().trim().equals("") &&
                            (edShopName.getText().toString().trim().equals("") &&
                                    (edDeliveryFee.getText().toString().trim().equals("") &&
                                            (edPhone.getText().toString().trim().equals("") &&
                                    (edCountry.getText().toString().trim().equals("") &&
                                            (edState.getText().toString().trim().equals("")) &&
                                            (edCity.getText().toString().trim().equals("")) &&
                                            (edAddress.getText().toString().trim().equals("")) &&
                                            (edEmail.getText().toString().trim().equals("")) &&
                                            (edPassword.getText().toString().trim().equals("")) &&
                                            (edConfirmPassword.getText().toString().trim().equals(""))))))))
            {
                Toast.makeText(RegisterSellerActivity.this, "Please enter valid credentials", Toast.LENGTH_SHORT).show();
            }
            else {
                if(edPassword.getText().toString().trim().equals(edConfirmPassword.getText().toString().trim())) {
                    if (edPassword.length() >= 8) {
                        if (img == 0) {
                            progressDialog.show();
                            progressDialog.setContentView(R.layout.custom_loading_box);
                            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                            progressDialog.setCanceledOnTouchOutside(false);
                            n = 0;
                            exist = 0;
                            for (int _repeat156 = 0; _repeat156 < (int) (listmap.size()); _repeat156++) {
                                if (listmap.get((int) n).get("id").toString().toLowerCase().equals(edId.getText().toString().toLowerCase())) {
                                    Toast.makeText(RegisterSellerActivity.this, "Id Already in Use", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                    exist = 1;
                                }
                                n++;
                            }
                            if (exist == 0) {
                                Currauth.createUserWithEmailAndPassword(edEmail.getText().toString(), edPassword.getText().toString()).addOnCompleteListener(RegisterSellerActivity.this, _Currauth_create_user_listener);
                                hashmap = new HashMap<>();
                                hashmap.put("sellerName", edName.getText().toString().trim());
                                hashmap.put("email", edEmail.getText().toString().trim());
                                hashmap.put("password", edPassword.getText().toString().trim());
                                hashmap.put("id", edId.getText().toString().trim());
                                hashmap.put("address", edAddress.getText().toString().trim());
                                hashmap.put("city", edCity.getText().toString().trim());
                                hashmap.put("country", edCountry.getText().toString().trim());
                                hashmap.put("mobile", edPhone.getText().toString().trim());
                                hashmap.put("state", edState.getText().toString().trim());
                                hashmap.put("shopName", edShopName.getText().toString().trim());
                                hashmap.put("deliveryFee", edDeliveryFee.getText().toString().trim());
                                hashmap.put("photo", "");
                                Currdata.child(edId.getText().toString()).updateChildren(hashmap);
                                Toast.makeText(RegisterSellerActivity.this, "Successfully Signed Up.", Toast.LENGTH_SHORT).show();
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
                                if (listmap.get((int) n).get("id").toString().toLowerCase().equals(edId.getText().toString().toLowerCase())) {
                                    Toast.makeText(RegisterSellerActivity.this, "Id Already in Use", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                    exist = 1;
                                }
                                n++;
                            }
                            if (exist == 0) {
                                profile.child(profileimgname).putFile(Uri.fromFile(new File(profileimgpath))).addOnFailureListener(_profile_failure_listener).addOnProgressListener(_profile_upload_progress_listener).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                    @Override
                                    public Task<Uri> then(Task<UploadTask.TaskSnapshot> task) throws Exception {
                                        return profile.child(profileimgname).getDownloadUrl();
                                    }
                                }).addOnCompleteListener(_profile_upload_success_listener);
                                btnRegister.setEnabled(false);
                            } else {

                            }
                        }
                    } else {
                        edPassword.setText("");
                        edConfirmPassword.setText("");
                        Toast.makeText(RegisterSellerActivity.this, "Password Length is Small", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(RegisterSellerActivity.this, "Password must match", Toast.LENGTH_SHORT).show();
                }
            }
        }
    });

    _Currdata_child_listener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot _param1, String _param2) {
            GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
            final String _childKey = _param1.getKey();
            final HashMap<String, Object> _childValue = _param1.getValue(_ind);
            Currdata.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot _dataSnapshot) {
                    listmap = new ArrayList<>();
                    try {
                        GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                        for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                            HashMap<String, Object> _map = _data.getValue(_ind);
                            listmap.add(_map);
                        }
                    }
                    catch (Exception _e) {
                        _e.printStackTrace();
                    }

                }
                @Override
                public void onCancelled(DatabaseError _databaseError) {
                }
            });
        }

        @Override
        public void onChildChanged(DataSnapshot _param1, String _param2) {
            GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
            final String _childKey = _param1.getKey();
            final HashMap<String, Object> _childValue = _param1.getValue(_ind);

        }

        @Override
        public void onChildMoved(DataSnapshot _param1, String _param2) {

        }

        @Override
        public void onChildRemoved(DataSnapshot _param1) {
            GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
            final String _childKey = _param1.getKey();
            final HashMap<String, Object> _childValue = _param1.getValue(_ind);

        }

        @Override
        public void onCancelled(DatabaseError _param1) {
            final int _errorCode = _param1.getCode();
            final String _errorMessage = _param1.getMessage();

        }
    };
        Currdata.addChildEventListener(_Currdata_child_listener);


    _profile_upload_progress_listener = new OnProgressListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onProgress(UploadTask.TaskSnapshot _param1) {
            double _progressValue = (100.0 * _param1.getBytesTransferred()) / _param1.getTotalByteCount();

        }
    };

    _profile_download_progress_listener = new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
        @Override
        public void onProgress(FileDownloadTask.TaskSnapshot _param1) {
            double _progressValue = (100.0 * _param1.getBytesTransferred()) / _param1.getTotalByteCount();

        }
    };

    _profile_upload_success_listener = new OnCompleteListener<Uri>() {
        @Override
        public void onComplete(Task<Uri> _param1) {
            final String _downloadUrl = _param1.getResult().toString();
            Currauth.createUserWithEmailAndPassword(edEmail.getText().toString().trim(), edPassword.getText().toString().trim()).addOnCompleteListener(RegisterSellerActivity.this, _Currauth_create_user_listener);
            hashmap = new HashMap<>();
            hashmap.put("sellerName", edName.getText().toString().trim());
            hashmap.put("email", edEmail.getText().toString().trim());
            hashmap.put("password", edPassword.getText().toString().trim());
            hashmap.put("id", edId.getText().toString().trim());
            hashmap.put("address", edAddress.getText().toString().trim());
            hashmap.put("city", edCity.getText().toString().trim());
            hashmap.put("country", edCountry.getText().toString().trim());
            hashmap.put("mobile", edPhone.getText().toString().trim());
            hashmap.put("state", edState.getText().toString().trim());
            hashmap.put("shopName", edShopName.getText().toString().trim());
            hashmap.put("deliveryFee", edDeliveryFee.getText().toString().trim());
            hashmap.put("photo", _downloadUrl);
            Currdata.child(edId.getText().toString()).updateChildren(hashmap);
            Toast.makeText(RegisterSellerActivity.this, "Successfully Signed Up.", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            finish();
        }
    };

    _profile_download_success_listener = new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
        @Override
        public void onSuccess(FileDownloadTask.TaskSnapshot _param1) {
            final long _totalByteCount = _param1.getTotalByteCount();

        }
    };

    _profile_delete_success_listener = new OnSuccessListener() {
        @Override
        public void onSuccess(Object _param1) {

        }
    };

    _profile_failure_listener = new OnFailureListener() {
        @Override
        public void onFailure(Exception _param1) {
            final String _message = _param1.getMessage();

        }
    };
    _Currauth_create_user_listener = new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(Task<AuthResult> _param1) {
            final boolean _success = _param1.isSuccessful();
            final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";

        }
    };

    _Currauth_sign_in_listener = new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(Task<AuthResult> _param1) {
            final boolean _success = _param1.isSuccessful();
            final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";

        }
    };

    _Currauth_reset_password_listener = new OnCompleteListener<Void>() {
        @Override
        public void onComplete(Task<Void> _param1) {
            final boolean _success = _param1.isSuccessful();

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

//This activity is use as a signup page for seller, here seller can register themself by providing the
// appropriate inputs