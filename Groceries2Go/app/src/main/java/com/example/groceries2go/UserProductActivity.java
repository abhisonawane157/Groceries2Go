package com.example.groceries2go;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;


public class UserProductActivity extends AppCompatActivity {

    private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
    private ArrayList<String> searchby= new ArrayList<>();
    private HashMap<String, Object> ordermap = new HashMap<>();
    private HashMap<String, Object> orderstatusmap = new HashMap<>();
    private ArrayList<HashMap<String, Object>> dishlistmap = new ArrayList<>();
    private HashMap<String, Object> hashmap = new HashMap<>();
    private ArrayList<HashMap<String, Object>> orderlistmap = new ArrayList<>();
    private DatabaseReference Dish = _firebase.getReference("Products");
    private ChildEventListener _Dish_child_listener;
    private DatabaseReference Cart = _firebase.getReference("Cart");
    private ChildEventListener _Cart_child_listener;
    private DatabaseReference Order = _firebase.getReference("Order");
    private ChildEventListener _Order_child_listener;
//    private DatabaseReference Currdata = _firebase.getReference("User");
//    private ChildEventListener _Currdata_child_listener;

    private SharedPreferences file;
    private Intent intent = new Intent();
    private AlertDialog.Builder dialog;

    private double n = 0;
    private double len = 0;
    private Calendar calendar = Calendar.getInstance();
    private String email = "";
    private String setsearchby = "pId";

    private ListView listview1;
    private Spinner spinner1;
    private LinearLayout search_bar;
    private EditText edSearch;
    private Button cancel;
    private TextView userName;
    private ImageView back, profile, cart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_product);
        com.google.firebase.FirebaseApp.initializeApp(this);
        initialize();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
        } else {
        }
    }

    private void initialize() {

        listview1 = findViewById(R.id.listview1);
        back = findViewById(R.id.back);
        profile = findViewById(R.id.profile);
        userName = findViewById(R.id.userName);
        cart = findViewById(R.id.cart);
//        editProfile = findViewById(R.id.editProfile);
        edSearch = findViewById(R.id.edSearch);
        spinner1 = findViewById(R.id.spinner1);
        cancel = findViewById(R.id.cancel);
        file = getSharedPreferences("file", Activity.MODE_PRIVATE);
        email = file.getString("email","");
        hashmap.clear();
        dishlistmap.clear();
        userName.setText(file.getString("sellerName","")+"'s Products");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(getApplicationContext(), UserCartActivity.class);
                startActivity(intent);
            }
        });
//        editProfile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                intent.setClass(getApplicationContext(), UserProfileActivity.class);
//                startActivity(intent);
//            }
//        });

        if(file.getString("sellerPhoto","").equals(""))
        {
            profile.setImageResource(R.drawable.default_image);
        }else{
            Glide.with(getApplicationContext()).load(file.getString("sellerPhoto","")).into(profile);
        }

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

        _Cart_child_listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot _param1, String _param2) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);

            }

            @Override
            public void onChildChanged(DataSnapshot _param1, String _param2) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);

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

            }

            @Override
            public void onCancelled(DatabaseError _param1) {
                final int _errorCode = _param1.getCode();
                final String _errorMessage = _param1.getMessage();

            }
        };
        Cart.addChildEventListener(_Cart_child_listener);

        _Order_child_listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot _param1, String _param2) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);

            }

            @Override
            public void onChildChanged(DataSnapshot _param1, String _param2) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);

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

            }

            @Override
            public void onCancelled(DatabaseError _param1) {
                final int _errorCode = _param1.getCode();
                final String _errorMessage = _param1.getMessage();

            }
        };
        Order.addChildEventListener(_Order_child_listener);
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
                _v = _inflater.inflate(R.layout.custom_products_lists, null);
            }

            final ImageView photo = (ImageView) _v.findViewById(R.id.photo);
            final TextView pName = (TextView) _v.findViewById(R.id.pName);
            final TextView pDetails = (TextView) _v.findViewById(R.id.pDetails);
            final TextView pCategory = (TextView) _v.findViewById(R.id.pCategory);
            final TextView pPrice = (TextView) _v.findViewById(R.id.pPrice);
            final TextView pId = (TextView) _v.findViewById(R.id.pId);
            final LinearLayout parent = (LinearLayout) _v.findViewById(R.id.linear);
            final ImageView addCart = (ImageView) _v.findViewById(R.id.addCart);

            pName.setText(dishlistmap.get((int)_position).get("pName").toString());
            pId.setText(dishlistmap.get((int)_position).get("pId").toString());
            pPrice.setText("$ "+dishlistmap.get((int)_position).get("price").toString());
            pDetails.setText(dishlistmap.get((int)_position).get("details").toString());
            pCategory.setText(dishlistmap.get((int)_position).get("category").toString());

            if(file.getString("sellerId","").equals(dishlistmap.get((int)_position).get("sellerId").toString()))
            {
                if(dishlistmap.get((int)_position).get("availability").toString().equals("1")) {
                    parent.setVisibility(View.VISIBLE);
                }else{
                    parent.setVisibility(View.GONE);
                }
            }else{
                parent.setVisibility(View.GONE);
            }



            if(dishlistmap.get((int) _position).get("photo").toString().trim().equals(""))
            {
                photo.setImageResource(R.drawable.default_image);
            }else{
                Glide.with(getApplicationContext()).load(Uri.parse(dishlistmap.get((int) _position).get("photo").toString())).into(photo);
            }

            addCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    calendar = Calendar.getInstance();
                    Random random = new Random();
                    String rand = String.valueOf(random.nextInt(99999));
                    String userId = file.getString("userId","");
                    String sellerId = dishlistmap.get((int)_position).get("sellerId").toString().trim();
                    ordermap = new HashMap<>();
                    ordermap.put("cartId",rand);
                    ordermap.put("sellerId", dishlistmap.get((int)_position).get("sellerId").toString());
                    ordermap.put("userId",file.getString("userId",""));
                    ordermap.put("userName",file.getString("userName",""));
                    ordermap.put("userPhoto",file.getString("photo",""));
                    ordermap.put("pName",dishlistmap.get((int)_position).get("pName").toString());
                    ordermap.put("productPhoto",dishlistmap.get((int)_position).get("photo").toString());
                    ordermap.put("country",file.getString("country",""));
                    ordermap.put("address",file.getString("address",""));
                    ordermap.put("sellerName",file.getString("sellerName",""));
                    ordermap.put("city",file.getString("city",""));
                    ordermap.put("state",file.getString("state",""));
                    ordermap.put("pId",dishlistmap.get((int)_position).get("pId").toString());
                    ordermap.put("price",dishlistmap.get((int)_position).get("price").toString());
                    ordermap.put("details",dishlistmap.get((int)_position).get("details").toString());
                    ordermap.put("category",dishlistmap.get((int)_position).get("category").toString());
                    ordermap.put("status","Pending");
                    ordermap.put("location",rand+" | "+dishlistmap.get((int)_position).get("pName").toString());
                    Cart.child(userId+"/"+rand+" | "+dishlistmap.get((int)_position).get("pName").toString()).updateChildren(ordermap);
                    Order.child(sellerId+"/"+rand+" | "+dishlistmap.get((int)_position).get("pName").toString()).updateChildren(ordermap);
                    Toast.makeText(UserProductActivity.this, dishlistmap.get((int)_position).get("pName").toString()+" added to cart.", Toast.LENGTH_SHORT).show();
                }
            });
            return _v;
        }
    }
}

//This ativity will appear after user select a particular seller and according to the seller, the products
//will get visible, from here user can put the products in cart