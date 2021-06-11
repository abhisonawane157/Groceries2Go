package com.example.groceries2go;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

public class MainSellerActivity extends AppCompatActivity {

    private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
    private ArrayList<String> searchby= new ArrayList<>();
    private HashMap<String, Object> ordermap = new HashMap<>();
    private HashMap<String, Object> orderstatusmap = new HashMap<>();
    private ArrayList<HashMap<String, Object>> dishlistmap = new ArrayList<>();
    private HashMap<String, Object> hashmap = new HashMap<>();
    private ArrayList<HashMap<String, Object>> orderlistmap = new ArrayList<>();
    private DatabaseReference Dish = _firebase.getReference("Products");
    private ChildEventListener _Dish_child_listener;
    private DatabaseReference Seller = _firebase.getReference("Seller");
    private ChildEventListener _Seller_child_listener;
    private DatabaseReference Ratings = _firebase.getReference("Ratings");
    private ChildEventListener _Ratings_child_listener;

    private SharedPreferences file;
    private Intent intent = new Intent();
    private AlertDialog.Builder dialog;

    private double n = 0;
    private int flagRate = 0;
    private double len = 0;
    private Calendar calendar = Calendar.getInstance();
    private String email = "";
    private String setsearchby = "pId";

    private ListView listview1;
    private Spinner spinner1;
    private LinearLayout search_bar;
    private EditText edSearch;
    private FloatingActionButton _fab;
    private Button cancel;
    private TextView tvSellerName;
    private ImageView back, profile, cart, editProfile, logoutt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_seller);
        com.google.firebase.FirebaseApp.initializeApp(this);
        initialize();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
        } else {
        }
    }


    @Override
    public void onBackPressed() {
        if(flagRate == 0) {
            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MainSellerActivity.this, R.style.BottomSheetDialogTheme);
            View bottomSheetView = LayoutInflater.from(getApplicationContext())
                    .inflate(R.layout.custom_bottom_sheet,
                            (LinearLayout) findViewById(R.id.bottomSheetContainer));

            RatingBar ratingBar = bottomSheetView.findViewById(R.id.ratingBar);
            TextView name = bottomSheetView.findViewById(R.id.nametxt);
            ImageView profile = bottomSheetView.findViewById(R.id.profile);
            Button submit = bottomSheetView.findViewById(R.id.submit);

            name.setText(file.getString("sellerName",""));
            if (file.getString("sellerPhoto", "").trim().equals("")) {
                profile.setImageResource(R.drawable.default_profile);
            } else {
                Glide.with(getApplicationContext()).load(Uri.parse(file.getString("sellerPhoto", ""))).into(profile);
            }

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(ratingBar.getRating()==0.0)
                    {
                        Toast.makeText(MainSellerActivity.this, "Please Rate appropriately.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Random random = new Random();
                        String rand = "R" + String.valueOf(random.nextInt(9999));

                        hashmap = new HashMap<>();
                        hashmap.put("sellerName", file.getString("sellerName", ""));
                        hashmap.put("sellerId", file.getString("sellerId", ""));
                        hashmap.put("email", file.getString("sellerEmail", ""));
                        hashmap.put("ratingId", rand);
                        hashmap.put("rating", ratingBar.getRating());
                        Ratings.child(file.getString("sellerId", "")).updateChildren(hashmap);
                        Toast.makeText(MainSellerActivity.this, "Successfully Signed Up.", Toast.LENGTH_SHORT).show();
                        bottomSheetDialog.dismiss();
                        finish();
                    }
                }
            });
            bottomSheetDialog.setContentView(bottomSheetView);
            bottomSheetDialog.show();
        }else{
            finish();
        }
    }

    private void initialize() {
        listview1 = findViewById(R.id.listview1);
        back = findViewById(R.id.back);
        _fab = findViewById(R.id._fab);
        profile = findViewById(R.id.profile);
        tvSellerName = findViewById(R.id.tvSellerName);
        cart = findViewById(R.id.cart);
        editProfile = findViewById(R.id.sellerprofile);
        edSearch = findViewById(R.id.edSearch);
        spinner1 = findViewById(R.id.spinner1);
        logoutt = findViewById(R.id.logout);
        cancel = findViewById(R.id.cancel);
        file = getSharedPreferences("file", Activity.MODE_PRIVATE);
        email = file.getString("sellerEmail","");
        hashmap.clear();
        dishlistmap.clear();
        dialog = new AlertDialog.Builder(this);
        tvSellerName.setText("Hi, "+file.getString("sellerName",""));

        logoutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.setTitle("Logout");
                dialog.setMessage("Do you want to logout?");
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface _dialog, int _which) {
                        FirebaseAuth.getInstance().signOut();
                        file.edit().putString("sellerEmail", "").apply();
                        file.edit().putString("sellerPhoto", "").apply();
                        intent.setClass(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface _dialog, int _which) {

                    }
                });
                dialog.create().show();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagRate == 0) {
                    final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MainSellerActivity.this, R.style.BottomSheetDialogTheme);
                    View bottomSheetView = LayoutInflater.from(getApplicationContext())
                            .inflate(R.layout.custom_bottom_sheet,
                                    (LinearLayout) findViewById(R.id.bottomSheetContainer));

                    RatingBar ratingBar = bottomSheetView.findViewById(R.id.ratingBar);
                    TextView name = bottomSheetView.findViewById(R.id.nametxt);
                    ImageView profile = bottomSheetView.findViewById(R.id.profile);
                    Button submit = bottomSheetView.findViewById(R.id.submit);

                    name.setText(file.getString("sellerName",""));
                    if (file.getString("sellerPhoto", "").trim().equals("")) {
                        profile.setImageResource(R.drawable.default_profile);
                    } else {
                        Glide.with(getApplicationContext()).load(Uri.parse(file.getString("sellerPhoto", ""))).into(profile);
                    }

                    submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(ratingBar.getRating()==0.0)
                            {
                                Toast.makeText(MainSellerActivity.this, "Please Rate appropriately.", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Random random = new Random();
                                String rand = "R" + String.valueOf(random.nextInt(9999));

                                hashmap = new HashMap<>();
                                hashmap.put("sellerName", file.getString("sellerName", ""));
                                hashmap.put("sellerId", file.getString("sellerId", ""));
                                hashmap.put("email", file.getString("sellerEmail", ""));
                                hashmap.put("ratingId", rand);
                                hashmap.put("rating", ratingBar.getRating());
                                Ratings.child(file.getString("sellerId", "")).updateChildren(hashmap);
                                Toast.makeText(MainSellerActivity.this, "Successfully Signed Up.", Toast.LENGTH_SHORT).show();
                                bottomSheetDialog.dismiss();
                                finish();
                            }
                        }
                    });
                    bottomSheetDialog.setContentView(bottomSheetView);
                    bottomSheetDialog.show();
                }else{
                    finish();
                }
            }
        });
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(getApplicationContext(), SellerOrderActivity.class);
                startActivity(intent);
            }
        });

        _fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(getApplicationContext(),AddProduct.class);
                startActivity(intent);
            }
        });
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(getApplicationContext(), SellerProfileActivity.class);
                startActivity(intent);
            }
        });

        Glide.with(getApplicationContext()).load(file.getString("sellerPhoto","")).into(profile);

        searchby.add("PID");
        searchby.add("Name");
        searchby.add("Category");
        searchby.add("Details");
        spinner1.setAdapter(new ArrayAdapter<String>(getBaseContext(),android.R.layout.simple_spinner_dropdown_item,searchby));

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
                final int _position = _param3;
                ((TextView) _param1.getChildAt(0)).setTextColor(Color.WHITE);
                edSearch.setText("");
                if(spinner1.getSelectedItem() != null)
                {
                    if(spinner1.getSelectedItemId() == 0)
                    {
                        setsearchby = "pId";
                    }else if(spinner1.getSelectedItemId() == 1)
                    {
                        setsearchby = "pName";
                    }else if(spinner1.getSelectedItemId() == 2)
                    {
                        setsearchby = "category";
                    }else if(spinner1.getSelectedItemId() == 3)
                    {
                        setsearchby = "details";
                    }else{
                        setsearchby = "pId";
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> _param1) {

            }
        });

        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
                final String _charSeq = _param1.toString();
                Dish.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot _dataSnapshot) {
                        dishlistmap = new ArrayList<>();
                        try {
                            GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                            for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                                HashMap<String, Object> _map = _data.getValue(_ind);
                                dishlistmap.add(_map);
                            }
                        }
                        catch (Exception _e) {
                            _e.printStackTrace();
                        }
                        if (_charSeq.length() > 0) {
                            n = dishlistmap.size() - 1;
                            len = dishlistmap.size();
                            for(int _repeat20 = 0; _repeat20 < (int)(len); _repeat20++) {
                                if (dishlistmap.get((int)n).get(setsearchby).toString().toLowerCase().contains(_charSeq.toLowerCase())) {

                                }
                                else {
                                    dishlistmap.remove((int)(n));
                                }
                                n--;
                            }
                        }
                        listview1.setAdapter(new Listview1Adapter(dishlistmap));
                        ((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(DatabaseError _databaseError) {
                    }
                });
            }

            @Override
            public void beforeTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {

            }

            @Override
            public void afterTextChanged(Editable _param1) {

            }
        });


        _Ratings_child_listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot _param1, String _param2) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);
                if (!file.getString("sellerEmail", "").equals("")) {
                    if (_childValue.get("email").toString().equals(email)) {
                        flagRate = 1;
                    }
                    else {
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot _param1, String _param2) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);
                if (!file.getString("sellerEmail", "").equals("")) {
                    if (_childValue.get("email").toString().equals(email)) {
                        flagRate = 1;
                    }
                    else {
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
                if (!file.getString("sellerEmail", "").equals("")) {
                    if (_childValue.get("email").toString().equals(email)) {
                        flagRate = 1;
                    }
                    else {
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError _param1) {
                final int _errorCode = _param1.getCode();
                final String _errorMessage = _param1.getMessage();

            }
        };
        Ratings.addChildEventListener(_Ratings_child_listener);


        _Dish_child_listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot _param1, String _param2) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);
                Dish.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot _dataSnapshot) {
                        dishlistmap = new ArrayList<>();
                        try {
                            GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                            };
                            for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                                HashMap<String, Object> _map = _data.getValue(_ind);
                                dishlistmap.add(_map);
                            }
                        } catch (Exception _e) {
                            _e.printStackTrace();
                        }
                        listview1.setAdapter(new Listview1Adapter(dishlistmap));
                        ((BaseAdapter) listview1.getAdapter()).notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError _databaseError) {
                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot _param1, String _param2) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);
                Dish.removeEventListener(_Dish_child_listener);
                dishlistmap.clear();
                Dish.addChildEventListener(_Dish_child_listener);
            }

            @Override
            public void onChildMoved(DataSnapshot _param1, String _param2) {

            }

            @Override
            public void onChildRemoved(DataSnapshot _param1) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);
                Dish.removeEventListener(_Dish_child_listener);
                dishlistmap.clear();
                Dish.addChildEventListener(_Dish_child_listener);
            }

            @Override
            public void onCancelled(DatabaseError _param1) {
                final int _errorCode = _param1.getCode();
                final String _errorMessage = _param1.getMessage();

            }
        };
        Dish.addChildEventListener(_Dish_child_listener);

        _Seller_child_listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot _param1, String _param2) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);
                if (_childValue.get("email").toString().equals(file.getString("sellerEmail", ""))) {
                    file.edit().putString("address", _childValue.get("address").toString()).apply();
                    file.edit().putString("city", _childValue.get("city").toString()).apply();
                    file.edit().putString("country", _childValue.get("country").toString()).apply();
                    file.edit().putString("deliveryFee", _childValue.get("deliveryFee").toString()).apply();
                    file.edit().putString("emailid", _childValue.get("email").toString()).apply();
                    file.edit().putString("sellerId", _childValue.get("id").toString()).apply();
                    file.edit().putString("mobile", _childValue.get("mobile").toString()).apply();
                    file.edit().putString("sellerName", _childValue.get("sellerName").toString()).apply();
                    file.edit().putString("shopName", _childValue.get("shopName").toString()).apply();
                    file.edit().putString("state", _childValue.get("state").toString()).apply();
                    if(_childValue.get("photo").toString().equals("")){
                        file.edit().putString("sellerPhoto", "https://firebasestorage.googleapis.com/v0/b/database-1cc09.appspot.com/o/default_profile.png?alt=media&token=b9dd6c40-d47d-455e-8e56-c5f816a00263").apply();
                    }else{
                        file.edit().putString("sellerPhoto", _childValue.get("photo").toString()).apply();
                    }
                    tvSellerName.setText(_childValue.get("sellerName").toString());
                    Glide.with(getApplicationContext()).load(file.getString("sellerPhoto","")).into(profile);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot _param1, String _param2) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);
                Seller.removeEventListener(_Seller_child_listener);
                Seller.addChildEventListener(_Seller_child_listener);
            }

            @Override
            public void onChildMoved(DataSnapshot _param1, String _param2) {

            }

            @Override
            public void onChildRemoved(DataSnapshot _param1) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);
                Seller.removeEventListener(_Seller_child_listener);
                Seller.addChildEventListener(_Seller_child_listener);
            }

            @Override
            public void onCancelled(DatabaseError _param1) {
                final int _errorCode = _param1.getCode();
                final String _errorMessage = _param1.getMessage();

            }
        };
        Seller.addChildEventListener(_Seller_child_listener);

    }

    public class Listview1Adapter extends BaseAdapter {
        ArrayList<HashMap<String, Object>> _data;
        public Listview1Adapter(ArrayList<HashMap<String, Object>> _arr) {
            _data = _arr;
        }

        @Override
        public int getCount() {
            return _data.size();
        }

        @Override
        public HashMap<String, Object> getItem(int _index) {
            return _data.get(_index);
        }

        @Override
        public long getItemId(int _index) {
            return _index;
        }
        @Override
        public View getView(final int _position, View _view, ViewGroup _viewGroup) {
            LayoutInflater _inflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View _v = _view;
            if (_v == null) {
                _v = _inflater.inflate(R.layout.custom_seller_products_lists, null);
            }

            final ImageView photo = (ImageView) _v.findViewById(R.id.photo);
            final TextView pName = (TextView) _v.findViewById(R.id.pName);
            final TextView pDetails = (TextView) _v.findViewById(R.id.pDetails);
            final TextView pCategory = (TextView) _v.findViewById(R.id.pCategory);
            final TextView pPrice = (TextView) _v.findViewById(R.id.pPrice);
            final TextView pId = (TextView) _v.findViewById(R.id.pId);
            final LinearLayout parent = (LinearLayout) _v.findViewById(R.id.linear);
            final ImageView delete = (ImageView) _v.findViewById(R.id.delete);
            final ImageView status = (ImageView) _v.findViewById(R.id.toggle);

            pName.setText(dishlistmap.get((int)_position).get("pName").toString());
            pId.setText(dishlistmap.get((int)_position).get("pId").toString());
            pPrice.setText("$ "+dishlistmap.get((int)_position).get("price").toString());
            pDetails.setText(dishlistmap.get((int)_position).get("details").toString());
            pCategory.setText(dishlistmap.get((int)_position).get("category").toString());

            if(file.getString("sellerId","").equals(dishlistmap.get((int)_position).get("sellerId").toString()))
            {
                parent.setVisibility(View.VISIBLE);
            }else{
                parent.setVisibility(View.GONE);
            }

            if(dishlistmap.get((int) _position).get("photo").toString().trim().equals(""))
            {
                photo.setImageResource(R.drawable.default_profile);
            }else{
                Glide.with(getApplicationContext()).load(Uri.parse(dishlistmap.get((int) _position).get("photo").toString())).into(photo);
            }

            if((dishlistmap.get((int) _position).get("availability").toString()).equals("1")) {
                status.setImageResource(R.drawable.toggle_on);
            }else{
                status.setImageResource(R.drawable.toggle_off);
            }
            status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View _view) {
                    if((dishlistmap.get((int) _position).get("availability").toString()).equals("1")) {
                        dialog.setTitle(dishlistmap.get((int) _position).get("pName").toString());
                        dialog.setMessage("Product is Unavailable?");
                        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface _dialog, int _which) {
                                ordermap = new HashMap<>();
                                ordermap.put("availability", "0");
                                Dish.child(dishlistmap.get((int) _position).get("pId").toString()).updateChildren(ordermap);
                            }
                        });
                        dialog.setNeutralButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface _dialog, int _which) {

                            }
                        });
                        dialog.create().show();
                    }
                    else{
                        dialog.setTitle(dishlistmap.get((int) _position).get("pName").toString());
                        dialog.setMessage("Product is available?");
                        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface _dialog, int _which) {
                                ordermap = new HashMap<>();
                                ordermap.put("availability", "1");
                                Dish.child(dishlistmap.get((int) _position).get("pId").toString()).updateChildren(ordermap);
                            }
                        });
                        dialog.setNeutralButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface _dialog, int _which) {

                            }
                        });
                        dialog.create().show();
                    }
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainSellerActivity.this, dishlistmap.get((int)_position).get("pName").toString()+" Deleted.", Toast.LENGTH_SHORT).show();
                    Dish.child(dishlistmap.get((int) _position).get("pId").toString()).removeValue();

                }
            });
            return _v;
        }
    }
}


//
//    This activity is the dashboard for the seller, this will be the first activity for seller after successfully
//logged in. In this activity we are setting the appropriate profileimage of seller, with the help of their email
//id data will be fetched from database
//
//we used three instances of database of node Seller, User and Ratings node-seller node: we need to show
//list of seller available so with the help of instance we can fetch tha data. user-node: we need to set the
//name and profile name of user to fetch the details we need to set those instances. Ratings node will be used
//to push the ratings of user regarding the application.
//
//In this activity all the products will get visible of that particular seller, from here seller can make the
//product available or unavailable even seller can delete the product and add the product
