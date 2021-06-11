package com.example.groceries2go;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class UserProfileActivity extends AppCompatActivity {


    private ArrayList<HashMap<String, Object>> listmap = new ArrayList<>();

    private TextView memberid;
    private TextView name;
    private TextView phonenumber;
    private TextView flattype;
    private TextView status;
    private Button back;
    private ImageView imageview1;
    private Button edit;
    private Button update;
    private EditText etname;
    private EditText etphonenumber;
    private TextInputLayout etnamelayout;
    private TextInputLayout etphonenumberlayout;
    //    private LinearLayout editprofile;
//    private ImageView addprofile;
//    private ImageView delprofile;
    private ProgressDialog progressDialog;

//    BottomNavigationView bottomNavigationView;

    private String namee = "";
    private String string = "";
    private double img = 0;
    public final int REQ_CD_FILEPICKER = 101;
    public final int REQ_CD_CAMERA = 102;
    private String id = "";
    private String url = "";

    private String imgurl = "";
    private double imgsize = 0;
    private HashMap<String, Object> map = new HashMap<>();
    private String imgpath = "";
    private String naam = "";
    private String contact = "";
    private String profilename = "";
    private double flag = 0;
    private int profileimg = 0;

    private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
    private FirebaseStorage _firebase_storage = FirebaseStorage.getInstance();
    private DatabaseReference Currdata = _firebase.getReference("User");
//    private StorageReference profile = _firebase_storage.getReference("profile");
//    private OnCompleteListener<Uri> _profile_upload_success_listener;
//    private OnSuccessListener<FileDownloadTask.TaskSnapshot> _profile_download_success_listener;
//    private OnSuccessListener _profile_delete_success_listener;
//    private OnProgressListener<UploadTask.TaskSnapshot> _profile_upload_progress_listener;
//    private OnProgressListener<FileDownloadTask.TaskSnapshot> _profile_download_progress_listener;
//    private OnFailureListener _profile_failure_listener;
    private ChildEventListener _Currdata_child_listener;
    private SharedPreferences file;
    private AlertDialog.Builder dialog;
//    private Intent filepicker = new Intent(Intent.ACTION_GET_CONTENT);
//    private Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//    private File _file_camera;
    private Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        initialize();
        initializeLogic();
    }

    private void initialize() {
        memberid = (TextView) findViewById(R.id.memberid);
        name = (TextView) findViewById(R.id.name);
        phonenumber = (TextView) findViewById(R.id.phonenumber);
        flattype = (TextView) findViewById(R.id.flattype);
        status = (TextView) findViewById(R.id.status);
        back = (Button) findViewById(R.id.backbtn);
        imageview1 =  findViewById(R.id.imageview1);
        etphonenumber = (EditText) findViewById(R.id.etphonenumber);
        etname = (EditText) findViewById(R.id.etname);
        edit = (Button) findViewById(R.id.editbtn);
        update = (Button) findViewById(R.id.updatebtn);
        etnamelayout = (TextInputLayout) findViewById(R.id.etnamelayout);
        progressDialog = new ProgressDialog(UserProfileActivity.this);
//        bottomNavigationView = findViewById(R.id.UI_bottomnavbar);
        etphonenumberlayout = (TextInputLayout) findViewById(R.id.etphonenumberlayout);
//        editprofile = findViewById(R.id.editprofile);
//        delprofile = findViewById(R.id.delprofile);
//        addprofile = findViewById(R.id.addprofile);
        dialog = new AlertDialog.Builder(this);
//        filepicker.setType("image/*");



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        filepicker.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//        _file_camera = FileUtil.createNewPictureFile(getApplicationContext());
//        Uri _uri_camera = null;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            _uri_camera= FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", _file_camera);
//        }
//        else {
//            _uri_camera = Uri.fromFile(_file_camera);
//        }
//        camera.putExtra(MediaStore.EXTRA_OUTPUT, _uri_camera);
//        camera.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        file = getSharedPreferences("file", Activity.MODE_PRIVATE);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                supportFinishAfterTransition();
                update.setVisibility(View.GONE);
                etnamelayout.setVisibility(View.GONE);
                etphonenumberlayout.setVisibility(View.GONE);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag == 0) {
                    update.setVisibility(View.VISIBLE);
                    etnamelayout.setVisibility(View.VISIBLE);
                    etphonenumberlayout.setVisibility(View.VISIBLE);
                    name.setVisibility(View.GONE);
                    phonenumber.setVisibility(View.GONE);
                    //delprofile.setVisibility(View.GONE);
                    //addprofile.setVisibility(View.GONE);
                    flag=1;
                    if (profileimg == 1)
                    {
                        //delprofile.setVisibility(View.VISIBLE);
                        //addprofile.setVisibility(View.GONE);
                    }else{
                        //addprofile.setVisibility(View.VISIBLE);
                        //delprofile.setVisibility(View.GONE);
                    }
                }else{
                    update.setVisibility(View.GONE);
                    etnamelayout.setVisibility(View.GONE);
                    etphonenumberlayout.setVisibility(View.GONE);
                    name.setVisibility(View.VISIBLE);
                    phonenumber.setVisibility(View.VISIBLE);
                    //delprofile.setVisibility(View.GONE);
                    //addprofile.setVisibility(View.GONE);
                    flag=0;
                }
            }
        });

//        delprofile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (file.getString("photo", "").equals("")) {
//                    Toast.makeText(UserProfileActivity.this, "Can't able to delete profile", Toast.LENGTH_SHORT).show();
//                } else {
//                    imgurl = "";
//                    imgpath = "";
//                    _firebase_storage.getReferenceFromUrl(file.getString("photo", "")).delete().addOnSuccessListener(_profile_delete_success_listener).addOnFailureListener(_profile_failure_listener);
//                    map = new HashMap<>();
//                    map.put("photo", "");
//                    Currdata.child(id).updateChildren(map);
//                    //delprofile.setVisibility(View.GONE);
//                    //addprofile.setVisibility(View.VISIBLE);
//                    imageview1.setImageResource(R.drawable.default_profile);
//                    img = 0;
//                    profileimg =0;
//                }
//            }
//        });
//
//        addprofile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.setTitle("Profile Picture");
//                dialog.setMessage("Choose picture from");
//                dialog.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface _dialog, int _which) {
//                        startActivityForResult(camera, REQ_CD_CAMERA);
//                    }
//                });
//                dialog.setNeutralButton("File", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface _dialog, int _which) {
//                        startActivityForResult(filepicker, REQ_CD_FILEPICKER);
//                    }
//                });
//                dialog.show();
//            }
//        });
//
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etname.getText().toString().equals("") && etphonenumber.getText().toString().equals(""))
                {
                    Toast.makeText(UserProfileActivity.this, "Enter values to Update", Toast.LENGTH_SHORT).show();
                }else {
//                    if (img == 0) {
                        if (etname.getText().toString().equals(naam) && etphonenumber.getText().toString().equals(contact)) {
                            Toast.makeText(UserProfileActivity.this, "Same Records are stored in our database", Toast.LENGTH_SHORT).show();
                        } else {
                            map = new HashMap<>();
                            map.put("userName", etname.getText().toString());
                            map.put("mobile", etphonenumber.getText().toString());
                            Currdata.child(id).updateChildren(map);
                            Toast.makeText(UserProfileActivity.this, "Successfully Updated", Toast.LENGTH_SHORT).show();
                            back.setVisibility(View.VISIBLE);
                            update.setVisibility(View.GONE);
                            edit.setVisibility(View.VISIBLE);
                            etnamelayout.setVisibility(View.GONE);
                            etphonenumberlayout.setVisibility(View.GONE);
                        }
//                    }
//                    else {
//                        progressDialog.show();
//                        progressDialog.setContentView(R.layout.custom_loading_box);
//                        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//                        progressDialog.setCanceledOnTouchOutside(false);
//                        profile.child(imgurl).putFile(Uri.fromFile(new File(imgpath))).addOnFailureListener(_profile_failure_listener).addOnProgressListener(_profile_upload_progress_listener).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//                            @Override
//                            public Task<Uri> then(Task<UploadTask.TaskSnapshot> task) throws Exception {
//                                return profile.child(imgurl).getDownloadUrl();
//                            }}).addOnCompleteListener(_profile_upload_success_listener);
//                        update.setEnabled(false);
//                    }


                }
            }
        });


        //Currdata

        _Currdata_child_listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot _param1, String _param2) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);

                if (_childValue.get("email").toString().equals(file.getString("email", ""))) {
                    name.setText(_childValue.get("userName").toString());
                    memberid.setText(_childValue.get("id").toString());
                    phonenumber.setText(_childValue.get("mobile").toString());
//                    flattype.setText(_childValue.get("flattype").toString());
//                    status.setText(_childValue.get("status").toString());
                    etname.setText(_childValue.get("userName").toString());
                    etphonenumber.setText(_childValue.get("mobile").toString());
                    id = _childValue.get("id").toString();
                    naam = _childValue.get("userName").toString();
                    contact = _childValue.get("mobile").toString();
                    if (!_childValue.get("photo").toString().equals("")) {
                        Glide.with(getApplicationContext()).load(Uri.parse(_childValue.get("photo").toString())).into(imageview1);
                        file.edit().putString("photo", _childValue.get("photo").toString()).commit();
                        profileimg = 1;
//                        //delprofile.setVisibility(View.VISIBLE);
//                        //addprofile.setVisibility(View.GONE);
                    }
                    else {
                        imageview1.setImageResource(R.drawable.default_profile);
                        profileimg = 0;
//                        //delprofile.setVisibility(View.GONE);
//                        //addprofile.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot _param1, String _param2) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);
                if (_childValue.get("email").toString().equals(file.getString("email", ""))) {
                    name.setText(_childValue.get("userName").toString());
                    memberid.setText(_childValue.get("id").toString());
                    phonenumber.setText(_childValue.get("mobile").toString());
//                    flattype.setText(_childValue.get("flattype").toString());
//                    status.setText(_childValue.get("status").toString());
                    etname.setText(_childValue.get("userName").toString());
                    etphonenumber.setText(_childValue.get("mobile").toString());
                    id = _childValue.get("id").toString();
                    naam = _childValue.get("userName").toString();
                    contact = _childValue.get("mobile").toString();
                    if (!_childValue.get("photo").toString().equals("")) {
                        Glide.with(getApplicationContext()).load(Uri.parse(_childValue.get("photo").toString())).into(imageview1);
                        file.edit().putString("photo", _childValue.get("photo").toString()).commit();
                        profileimg = 1;
//                        //delprofile.setVisibility(View.VISIBLE);
//                        //addprofile.setVisibility(View.GONE);
                    }
                    else {
                        imageview1.setImageResource(R.drawable.default_profile);
                        profileimg = 0;
//                        //delprofile.setVisibility(View.GONE);
//                        //addprofile.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onChildMoved(DataSnapshot _param1, String _param2) {

            }

            @Override
            public void onChildRemoved(DataSnapshot _param1) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);
                if (_childValue.get("email").toString().equals(file.getString("email", ""))) {
                    name.setText(_childValue.get("userName").toString());
                    memberid.setText(_childValue.get("id").toString());
                    phonenumber.setText(_childValue.get("mobile").toString());
//                    flattype.setText(_childValue.get("flattype").toString());
//                    status.setText(_childValue.get("status").toString());
                    etname.setText(_childValue.get("userName").toString());
                    etphonenumber.setText(_childValue.get("mobile").toString());
                    id = _childValue.get("id").toString();
                    naam = _childValue.get("userName").toString();
                    contact = _childValue.get("mobile").toString();
                    if (!_childValue.get("photo").toString().equals("")) {
                        Glide.with(getApplicationContext()).load(Uri.parse(_childValue.get("photo").toString())).into(imageview1);
                        file.edit().putString("photo", _childValue.get("photo").toString()).commit();
                        profileimg = 1;
//                        //delprofile.setVisibility(View.VISIBLE);
//                        //addprofile.setVisibility(View.GONE);
                    }
                    else {
                        imageview1.setImageResource(R.drawable.default_profile);
                        profileimg = 0;
//                        //delprofile.setVisibility(View.GONE);
//                        //addprofile.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError _param1) {
                final int _errorCode = _param1.getCode();
                final String _errorMessage = _param1.getMessage();

            }
        };
        Currdata.addChildEventListener(_Currdata_child_listener);

//        _profile_upload_progress_listener = new OnProgressListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onProgress(UploadTask.TaskSnapshot _param1) {
//                double _progressValue = (100.0 * _param1.getBytesTransferred()) / _param1.getTotalByteCount();
//
//            }
//        };
//
//        _profile_download_progress_listener = new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
//            @Override
//            public void onProgress(FileDownloadTask.TaskSnapshot _param1) {
//                double _progressValue = (100.0 * _param1.getBytesTransferred()) / _param1.getTotalByteCount();
//
//            }
//        };
//
//        _profile_upload_success_listener = new OnCompleteListener<Uri>() {
//            @Override
//            public void onComplete(Task<Uri> _param1) {
//                final String _downloadUrl = _param1.getResult().toString();
//                if (!(img == 0)) {
//
//
//                    map = new HashMap<>();
//                    map.put("userName", etname.getText().toString());
//                    map.put("mobile", etphonenumber.getText().toString());
//                    map.put("photo", _downloadUrl);
//                    Currdata.child(id).updateChildren(map);
//                    Toast.makeText(UserProfileActivity.this, "Successfully Updated", Toast.LENGTH_SHORT).show();
//                    back.setVisibility(View.VISIBLE);
//                    update.setVisibility(View.GONE);
//                    edit.setVisibility(View.VISIBLE);
//                    etnamelayout.setVisibility(View.GONE);
//                    etphonenumberlayout.setVisibility(View.GONE);
//                    progressDialog.dismiss();
//                }
//                else {
//                    Toast.makeText(UserProfileActivity.this, "Unknown Error Occured", Toast.LENGTH_SHORT).show();
//
//                }
//            }
//        };
//
//        _profile_download_success_listener = new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(FileDownloadTask.TaskSnapshot _param1) {
//                final long _totalByteCount = _param1.getTotalByteCount();
//
//            }
//        };
//
//        _profile_delete_success_listener = new OnSuccessListener() {
//            @Override
//            public void onSuccess(Object _param1) {
//
//            }
//        };
//
//        _profile_failure_listener = new OnFailureListener() {
//            @Override
//            public void onFailure(Exception _param1) {
//                final String _message = _param1.getMessage();
//
//            }
//        };
//

        //Image PopUp

        imageview1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (profileimg == 1) {
                    ImageView imageView, imageView1;
                    Button btnok, btncancel;
                    AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileActivity.this);

                    View view1 = getLayoutInflater().inflate(R.layout.custom_image_pop_up, null);

                    builder.setView(view1);
                    final AlertDialog alertDialog = builder.create();
                    imageView = view1.findViewById(R.id.imageview1);
                    Glide.with(getApplicationContext()).load(Uri.parse(file.getString("photo", ""))).into(imageView);

                    alertDialog.show();
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                }else{
                    ImageView imageView, imageView1;
                    Button btnok, btncancel;
                    AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileActivity.this);

                    View view1 = getLayoutInflater().inflate(R.layout.custom_image_pop_up, null);

                    builder.setView(view1);
                    final AlertDialog alertDialog = builder.create();
                    imageView = view1.findViewById(R.id.imageview1);
                    imageView.setImageResource(R.drawable.default_profile);
                    alertDialog.show();
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                }
            }

        });

        //Image PopUp

    }
    @Override
    protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
        super.onActivityResult(_requestCode, _resultCode, _data);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
        update.setVisibility(View.GONE);
        etnamelayout.setVisibility(View.GONE);
        etphonenumberlayout.setVisibility(View.GONE);
    }


    private void initializeLogic() {
        update.setVisibility(View.GONE);
        etnamelayout.setVisibility(View.GONE);
        etphonenumberlayout.setVisibility(View.GONE);
        img = 0;
        flag = 0;
        //addprofile.setVisibility(View.GONE);
        //delprofile.setVisibility(View.GONE);
    }
}
//In this activity user can view his/her profile details, even user have access to modify/update
//the details