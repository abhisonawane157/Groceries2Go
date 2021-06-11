package com.example.groceries2go;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class SellerProfileActivity extends AppCompatActivity {


    private ArrayList<HashMap<String, Object>> listmap = new ArrayList<>();

    private TextView sellerid;
    private TextView name;
    private TextView phonenumber;
    private TextView shopName;
    private TextView address, city, dFee;
    private Button back;
    private ImageView imageview1;
    private AlertDialog.Builder dialog;
    private Button edit;
    private Button update;
    private EditText etname;
    private EditText etphonenumber, etAddress, etCity, etedFee, etShopname;
    private TextInputLayout etnamelayout, edAddresslayout, edCitylayout, eddFeelayout;
    private TextInputLayout etphonenumberlayout, edShopNamelayout;
    private ProgressDialog progressDialog;
    private String sName = "";
    private String saddress = "";
    private String scity = "";
    private String sdfee = "";
    private String url = "";

    private HashMap<String, Object> map = new HashMap<>();
    private String naam = "";
    private String id = "";
    private String contact = "";
    private double flag = 0;
    private int profileimg = 0;

    private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
    private FirebaseStorage _firebase_storage = FirebaseStorage.getInstance();
    private DatabaseReference Currdata = _firebase.getReference("Seller");
    private ChildEventListener _Currdata_child_listener;
    private SharedPreferences file;
    private Calendar calendar = Calendar.getInstance();
    Intent intent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_profile);
        initialize();
        initializeLogic();
    }

    private void initialize() {
        sellerid = (TextView) findViewById(R.id.sellerid);
        name = (TextView) findViewById(R.id.name);
        etname = (EditText) findViewById(R.id.etname);
        phonenumber = (TextView) findViewById(R.id.phonenumber);
        etphonenumber = (EditText) findViewById(R.id.etphonenumber);
        shopName = (TextView) findViewById(R.id.shopName);
        etShopname = findViewById(R.id.etShopName);
        address = (TextView) findViewById(R.id.address);
        etAddress = findViewById(R.id.etAddress);
        city = findViewById(R.id.city);
        etCity = findViewById(R.id.etCity);
        dFee = findViewById(R.id.dFee);
        etedFee = findViewById(R.id.etedFee);


        back = (Button) findViewById(R.id.backbtn);
        imageview1 =  findViewById(R.id.imageview1);
        edit = (Button) findViewById(R.id.editbtn);
        update = (Button) findViewById(R.id.updatebtn);

        etnamelayout = (TextInputLayout) findViewById(R.id.etnamelayout);
        etphonenumberlayout = (TextInputLayout) findViewById(R.id.etphonenumberlayout);
        edShopNamelayout = (TextInputLayout) findViewById(R.id.edShopName);
        edAddresslayout = (TextInputLayout) findViewById(R.id.edAddress);
        edCitylayout = (TextInputLayout) findViewById(R.id.edCity);
        eddFeelayout = (TextInputLayout) findViewById(R.id.eddFee);

        dialog = new AlertDialog.Builder(this);
        progressDialog = new ProgressDialog(SellerProfileActivity.this);


        file = getSharedPreferences("file", Activity.MODE_PRIVATE);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                update.setVisibility(View.GONE);
                etnamelayout.setVisibility(View.GONE);
                etphonenumberlayout.setVisibility(View.GONE);
                edShopNamelayout.setVisibility(View.GONE);
                edAddresslayout.setVisibility(View.GONE);
                edCitylayout.setVisibility(View.GONE);
                eddFeelayout.setVisibility(View.GONE);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag == 0) {
                    update.setVisibility(View.VISIBLE);
                    etnamelayout.setVisibility(View.VISIBLE);
                    etphonenumberlayout.setVisibility(View.VISIBLE);
                    edShopNamelayout.setVisibility(View.VISIBLE);
                    edAddresslayout.setVisibility(View.VISIBLE);
                    edCitylayout.setVisibility(View.VISIBLE);
                    eddFeelayout.setVisibility(View.VISIBLE);
                    name.setVisibility(View.GONE);
                    phonenumber.setVisibility(View.GONE);
                    shopName.setVisibility(View.GONE);
                    address.setVisibility(View.GONE);
                    city.setVisibility(View.GONE);
                    dFee.setVisibility(View.GONE);

                    flag=1;
                }else{
                    update.setVisibility(View.GONE);
                    name.setVisibility(View.VISIBLE);
                    phonenumber.setVisibility(View.VISIBLE);
                    shopName.setVisibility(View.VISIBLE);
                    address.setVisibility(View.VISIBLE);
                    city.setVisibility(View.VISIBLE);
                    dFee.setVisibility(View.VISIBLE);
                    etnamelayout.setVisibility(View.GONE);
                    etphonenumberlayout.setVisibility(View.GONE);
                    edShopNamelayout.setVisibility(View.GONE);
                    edAddresslayout.setVisibility(View.GONE);
                    edCitylayout.setVisibility(View.GONE);
                    eddFeelayout.setVisibility(View.GONE);
                    flag=0;
                }
            }
        });


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etname.getText().toString().equals("") && etphonenumber.getText().toString().equals(""))
                {
                    Toast.makeText(SellerProfileActivity.this, "Enter values to Update", Toast.LENGTH_SHORT).show();
                }else {
//                    if (img == 0) {
                    if (etname.getText().toString().equals(naam) &&
                            etShopname.getText().toString().equals(sName) &&
                            etphonenumber.getText().toString().equals(contact) &&
                                etCity.getText().toString().equals(scity) &&
                                    etAddress.getText().toString().equals(saddress) &&
                                        etedFee.getText().toString().equals(sdfee) ) {
                        Toast.makeText(SellerProfileActivity.this, "Same Records are stored in our database", Toast.LENGTH_SHORT).show();
                    } else {
                        map = new HashMap<>();
                        map.put("sellerName", etname.getText().toString());
                        map.put("mobile", etphonenumber.getText().toString());
                        map.put("shopName", etShopname.getText().toString());
                        map.put("address", etAddress.getText().toString());
                        map.put("city", etCity.getText().toString());
                        map.put("deliveryFee", etedFee.getText().toString());
                        Currdata.child(id).updateChildren(map);
                        Toast.makeText(SellerProfileActivity.this, "Successfully Updated", Toast.LENGTH_SHORT).show();
                        back.setVisibility(View.VISIBLE);
                        update.setVisibility(View.GONE);
                        edit.setVisibility(View.VISIBLE);
                        etnamelayout.setVisibility(View.GONE);
                        etphonenumberlayout.setVisibility(View.GONE);
                        edShopNamelayout.setVisibility(View.GONE);
                        edAddresslayout.setVisibility(View.GONE);
                        edCitylayout.setVisibility(View.GONE);
                        eddFeelayout.setVisibility(View.GONE);
                        finish();
                    }
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
                if (_childValue.get("email").toString().equals(file.getString("sellerEmail", ""))) {
                    name.setText(_childValue.get("sellerName").toString());
                    etname.setText(_childValue.get("sellerName").toString());

                    sellerid.setText(_childValue.get("id").toString());

                    phonenumber.setText(_childValue.get("mobile").toString());
                    etphonenumber.setText(_childValue.get("mobile").toString());

                    shopName.setText(_childValue.get("shopName").toString());
                    etShopname.setText(_childValue.get("shopName").toString());

                    address.setText(_childValue.get("address").toString());
                    etAddress.setText(_childValue.get("address").toString());

                    city.setText(_childValue.get("city").toString());
                    etCity.setText(_childValue.get("city").toString());

                    dFee.setText(_childValue.get("deliveryFee").toString());
                    etedFee.setText(_childValue.get("deliveryFee").toString());

                    id = _childValue.get("id").toString();
                    naam = _childValue.get("sellerName").toString();
                    contact = _childValue.get("mobile").toString();
                    sName = _childValue.get("shopName").toString();
                    saddress = _childValue.get("address").toString();
                    scity = _childValue.get("city").toString();
                    sdfee = _childValue.get("deliveryFee").toString();

                    if (!_childValue.get("photo").toString().equals("")) {
                        Glide.with(getApplicationContext()).load(Uri.parse(_childValue.get("photo").toString())).into(imageview1);
                        file.edit().putString("photo", _childValue.get("photo").toString()).commit();
                        profileimg = 1;
                    }
                    else {
                        imageview1.setImageResource(R.drawable.default_profile);
                        profileimg = 0;
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot _param1, String _param2) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);
                if (_childValue.get("email").toString().equals(file.getString("sellerEmail", ""))) {
                    name.setText(_childValue.get("sellerName").toString());
                    etname.setText(_childValue.get("sellerName").toString());

                    sellerid.setText(_childValue.get("id").toString());

                    phonenumber.setText(_childValue.get("mobile").toString());
                    etphonenumber.setText(_childValue.get("mobile").toString());

                    shopName.setText(_childValue.get("shopName").toString());
                    etShopname.setText(_childValue.get("shopName").toString());

                    address.setText(_childValue.get("address").toString());
                    etAddress.setText(_childValue.get("address").toString());

                    city.setText(_childValue.get("city").toString());
                    etCity.setText(_childValue.get("city").toString());

                    dFee.setText(_childValue.get("deliveryFee").toString());
                    etedFee.setText(_childValue.get("deliveryFee").toString());

                    id = _childValue.get("id").toString();
                    naam = _childValue.get("sellerName").toString();
                    contact = _childValue.get("mobile").toString();
                    sName = _childValue.get("shopName").toString();
                    saddress = _childValue.get("address").toString();
                    scity = _childValue.get("city").toString();
                    sdfee = _childValue.get("deliveryFee").toString();

                    if (!_childValue.get("photo").toString().equals("")) {
                        Glide.with(getApplicationContext()).load(Uri.parse(_childValue.get("photo").toString())).into(imageview1);
                        file.edit().putString("photo", _childValue.get("photo").toString()).commit();
                        profileimg = 1;
                    }
                    else {
                        imageview1.setImageResource(R.drawable.default_profile);
                        profileimg = 0;
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
                if (_childValue.get("email").toString().equals(file.getString("sellerEmail", ""))) {
                    name.setText(_childValue.get("sellerName").toString());
                    etname.setText(_childValue.get("sellerName").toString());

                    sellerid.setText(_childValue.get("id").toString());

                    phonenumber.setText(_childValue.get("mobile").toString());
                    etphonenumber.setText(_childValue.get("mobile").toString());

                    shopName.setText(_childValue.get("shopName").toString());
                    etShopname.setText(_childValue.get("shopName").toString());

                    address.setText(_childValue.get("address").toString());
                    etAddress.setText(_childValue.get("address").toString());

                    city.setText(_childValue.get("city").toString());
                    etCity.setText(_childValue.get("city").toString());

                    dFee.setText(_childValue.get("deliveryFee").toString());
                    etedFee.setText(_childValue.get("deliveryFee").toString());

                    id = _childValue.get("id").toString();
                    naam = _childValue.get("sellerName").toString();
                    contact = _childValue.get("mobile").toString();
                    sName = _childValue.get("shopName").toString();
                    saddress = _childValue.get("address").toString();
                    scity = _childValue.get("city").toString();
                    sdfee = _childValue.get("deliveryFee").toString();

                    if (!_childValue.get("photo").toString().equals("")) {
                        Glide.with(getApplicationContext()).load(Uri.parse(_childValue.get("photo").toString())).into(imageview1);
                        file.edit().putString("photo", _childValue.get("photo").toString()).commit();
                        profileimg = 1;
                    }
                    else {
                        imageview1.setImageResource(R.drawable.default_profile);
                        profileimg = 0;
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

        imageview1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (profileimg == 1) {
                    ImageView imageView, imageView1;
                    Button btnok, btncancel;
                    AlertDialog.Builder builder = new AlertDialog.Builder(SellerProfileActivity.this);

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
                    AlertDialog.Builder builder = new AlertDialog.Builder(SellerProfileActivity.this);

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
        edShopNamelayout.setVisibility(View.GONE);
        edAddresslayout.setVisibility(View.GONE);
        edCitylayout.setVisibility(View.GONE);
        eddFeelayout.setVisibility(View.GONE);
    }


    private void initializeLogic() {
        update.setVisibility(View.GONE);
        etnamelayout.setVisibility(View.GONE);
        etphonenumberlayout.setVisibility(View.GONE);
        edShopNamelayout.setVisibility(View.GONE);
        edAddresslayout.setVisibility(View.GONE);
        edCitylayout.setVisibility(View.GONE);
        eddFeelayout.setVisibility(View.GONE);
        flag = 0;
    }
}

//In this activity seller can view his/her profile details, even seller have access to modify/update
//the details