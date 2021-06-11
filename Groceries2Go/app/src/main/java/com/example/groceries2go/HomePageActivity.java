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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

public class HomePageActivity extends AppCompatActivity {

    private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
    private ArrayList<String> searchby= new ArrayList<>();
    private HashMap<String, Object> ordermap = new HashMap<>();
    private HashMap<String, Object> hashmap = new HashMap<>();
    private HashMap<String, Object> orderstatusmap = new HashMap<>();
    private ArrayList<HashMap<String, Object>> sellerlistmap = new ArrayList<>();
    private ArrayList<HashMap<String, Object>> orderlistmap = new ArrayList<>();

    private DatabaseReference Seller = _firebase.getReference("Seller");
    private ChildEventListener _Seller_child_listener;
    private DatabaseReference Currdata = _firebase.getReference("User");
    private ChildEventListener _Currdata_child_listener;
    private DatabaseReference Ratings = _firebase.getReference("Ratings");
    private ChildEventListener _Ratings_child_listener;

    private SharedPreferences file;
    private Intent intent = new Intent();
    private AlertDialog.Builder dialog;
    private FloatingActionButton _fab;
    private double n = 0;
    private double len = 0;
    private int flagRate = 0;
    private String email = "";
    private String setsearchby = "id";

    private ListView listview1;
    private Spinner spinner1;
    private LinearLayout search_bar;
    private EditText edSearch;
    private Button cancel;
    private TextView userName;
    private ImageView back, profile, cart, editProfile, logoutt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
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
            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(HomePageActivity.this, R.style.BottomSheetDialogTheme);
            View bottomSheetView = LayoutInflater.from(getApplicationContext())
                    .inflate(R.layout.custom_bottom_sheet,
                            (LinearLayout) findViewById(R.id.bottomSheetContainer));

            RatingBar ratingBar = bottomSheetView.findViewById(R.id.ratingBar);
            TextView name = bottomSheetView.findViewById(R.id.nametxt);
            ImageView profile = bottomSheetView.findViewById(R.id.profile);
            Button submit = bottomSheetView.findViewById(R.id.submit);

            name.setText(file.getString("userName",""));
            if (file.getString("photo", "").trim().equals("")) {
                profile.setImageResource(R.drawable.default_profile);
            } else {
                Glide.with(getApplicationContext()).load(Uri.parse(file.getString("photo", ""))).into(profile);
            }


            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(ratingBar.getRating()==0.0)
                    {
                        Toast.makeText(HomePageActivity.this, "Please Rate appropriately.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Random random = new Random();
                        String rand = "R" + String.valueOf(random.nextInt(9999));

                        hashmap = new HashMap<>();
                        hashmap.put("userName", file.getString("userName", ""));
                        hashmap.put("userId", file.getString("userId", ""));
                        hashmap.put("email", file.getString("email", ""));
                        hashmap.put("ratingId", rand);
                        hashmap.put("rating", ratingBar.getRating());
                        Ratings.child(file.getString("userId", "")).updateChildren(hashmap);
                        Toast.makeText(HomePageActivity.this, "Successfully Signed Up.", Toast.LENGTH_SHORT).show();
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
        profile = findViewById(R.id.profile);
        userName = findViewById(R.id.userName);
        cart = findViewById(R.id.cart);
        editProfile = findViewById(R.id.editProfile);
        edSearch = findViewById(R.id.edSearch);
        logoutt = findViewById(R.id.logout);
        spinner1 = findViewById(R.id.spinner1);
        _fab = findViewById(R.id._fab);
        cancel = findViewById(R.id.cancel);
        file = getSharedPreferences("file", Activity.MODE_PRIVATE);
        email = file.getString("email","");
        dialog = new AlertDialog.Builder(this);

        logoutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.setTitle("Logout");
                dialog.setMessage("Do you want to logout?");
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface _dialog, int _which) {
                        FirebaseAuth.getInstance().signOut();
                        file.edit().putString("emailid", "").apply();
                        file.edit().putString("userName", "").apply();
                        file.edit().putString("photo", "").apply();
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

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(getApplicationContext(), UserCartActivity.class);
                startActivity(intent);
            }
        });


        _fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(getApplicationContext(), OrderStatusActivity.class);
                startActivity(intent);
            }
        });
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(getApplicationContext(), UserProfileActivity.class);
                startActivity(intent);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagRate == 0) {
                    final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(HomePageActivity.this, R.style.BottomSheetDialogTheme);
                    View bottomSheetView = LayoutInflater.from(getApplicationContext())
                            .inflate(R.layout.custom_bottom_sheet,
                                    (LinearLayout) findViewById(R.id.bottomSheetContainer));

                    RatingBar ratingBar = bottomSheetView.findViewById(R.id.ratingBar);
                    TextView name = bottomSheetView.findViewById(R.id.nametxt);
                    ImageView profile = bottomSheetView.findViewById(R.id.profile);
                    Button submit = bottomSheetView.findViewById(R.id.submit);

                    name.setText(file.getString("userName",""));
                    if (file.getString("photo", "").trim().equals("")) {
                        profile.setImageResource(R.drawable.default_profile);
                    } else {
                        Glide.with(getApplicationContext()).load(Uri.parse(file.getString("photo", ""))).into(profile);
                    }

                    submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(ratingBar.getRating()==0.0)
                            {
                                Toast.makeText(HomePageActivity.this, "Please Rate appropriately.", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Random random = new Random();
                                String rand = "R" + String.valueOf(random.nextInt(9999));

                                hashmap = new HashMap<>();
                                hashmap.put("userName", file.getString("userName", ""));
                                hashmap.put("userId", file.getString("userId", ""));
                                hashmap.put("email", file.getString("email", ""));
                                hashmap.put("ratingId", rand);
                                hashmap.put("rating", ratingBar.getRating());
                                Ratings.child(file.getString("userId", "")).updateChildren(hashmap);
                                Toast.makeText(HomePageActivity.this, "Successfully Signed Up.", Toast.LENGTH_SHORT).show();
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
        userName.setText("Hi, "+file.getString("userName",""));
        searchby.add("Id");
        searchby.add("Name");
        searchby.add("Mobile");
        searchby.add("Shop Name");
        searchby.add("State");
        searchby.add("Country");
        searchby.add("City");
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
                        setsearchby = "id";
                    }else if(spinner1.getSelectedItemId() == 1)
                    {
                        setsearchby = "sellerName";
                    }else if(spinner1.getSelectedItemId() == 2)
                    {
                        setsearchby = "mobile";
                    }else if(spinner1.getSelectedItemId() == 3)
                    {
                        setsearchby = "shopName";
                    }else if(spinner1.getSelectedItemId() == 4)
                    {
                        setsearchby = "state";
                    }else if(spinner1.getSelectedItemId() == 5)
                    {
                        setsearchby = "country";
                    }else if(spinner1.getSelectedItemId() == 6)
                    {
                        setsearchby = "city";
                    }else{
                        setsearchby = "id";
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
                Seller.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot _dataSnapshot) {
                        sellerlistmap = new ArrayList<>();
                        try {
                            GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                            for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                                HashMap<String, Object> _map = _data.getValue(_ind);
                                sellerlistmap.add(_map);
                            }
                        }
                        catch (Exception _e) {
                            _e.printStackTrace();
                        }
                        if (_charSeq.length() > 0) {
                            n = sellerlistmap.size() - 1;
                            len = sellerlistmap.size();
                            for(int _repeat20 = 0; _repeat20 < (int)(len); _repeat20++) {
                                if (sellerlistmap.get((int)n).get(setsearchby).toString().toLowerCase().contains(_charSeq.toLowerCase())) {

                                }
                                else {
                                    sellerlistmap.remove((int)(n));
                                }
                                n--;
                            }
                        }
                        listview1.setAdapter(new Listview1Adapter(sellerlistmap));
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

        _Currdata_child_listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot _param1, String _param2) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);
                if (!file.getString("emailid", "").equals("")) {
                    if (_childValue.get("email").toString().equals(file.getString("emailid", ""))) {
                        file.edit().putString("userId",_childValue.get("id").toString()).apply();
                        userName.setText("Hi, ".concat(_childValue.get("userName").toString()));
                        Glide.with(getApplicationContext()).load(Uri.parse(_childValue.get("photo").toString())).into(profile);
                        file.edit().putString("userName", _childValue.get("userName").toString()).apply();
                        file.edit().putString("photo", _childValue.get("photo").toString()).apply();
                        file.edit().putString("address", _childValue.get("address").toString()).apply();
                        file.edit().putString("country", _childValue.get("country").toString()).apply();
                        file.edit().putString("city", _childValue.get("city").toString()).apply();
                        file.edit().putString("state", _childValue.get("state").toString()).apply();
                    }
                    else {
                        profile.setBackgroundResource(R.drawable.default_profile);
                    }
//                        memberid = _childValue.get("memberid").toString();
//                        map = new HashMap<>();
//                        map.put("user_uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
//                        Currdata.child(memberid).updateChildren(map);

                }
            }

            @Override
            public void onChildChanged(DataSnapshot _param1, String _param2) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);
                if (!file.getString("emailid", "").equals("")) {
                    if (_childValue.get("email").toString().equals(file.getString("emailid", ""))) {
                        file.edit().putString("userId",_childValue.get("id").toString()).apply();
                        userName.setText("Hi, ".concat(_childValue.get("userName").toString()));
                        Glide.with(getApplicationContext()).load(Uri.parse(_childValue.get("photo").toString())).into(profile);
                        file.edit().putString("userName", _childValue.get("userName").toString()).apply();
                        file.edit().putString("photo", _childValue.get("photo").toString()).apply();
                        file.edit().putString("address", _childValue.get("address").toString()).apply();
                        file.edit().putString("country", _childValue.get("country").toString()).apply();
                        file.edit().putString("city", _childValue.get("city").toString()).apply();
                        file.edit().putString("state", _childValue.get("state").toString()).apply();
                    }
                    else {
                        profile.setBackgroundResource(R.drawable.default_profile);
                    }
//                        memberid = _childValue.get("memberid").toString();
//                        map = new HashMap<>();
//                        map.put("user_uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
//                        Currdata.child(memberid).updateChildren(map);

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
                if (!file.getString("emailid", "").equals("")) {
                    if (_childValue.get("email").toString().equals(file.getString("emailid", ""))) {
                        file.edit().putString("userId",_childValue.get("id").toString()).apply();
                        userName.setText("Hi, ".concat(_childValue.get("userName").toString()));
                        Glide.with(getApplicationContext()).load(Uri.parse(_childValue.get("photo").toString())).into(profile);
                        file.edit().putString("userName", _childValue.get("userName").toString()).apply();
                        file.edit().putString("photo", _childValue.get("photo").toString()).apply();
                        file.edit().putString("address", _childValue.get("address").toString()).apply();
                        file.edit().putString("country", _childValue.get("country").toString()).apply();
                        file.edit().putString("city", _childValue.get("city").toString()).apply();
                        file.edit().putString("state", _childValue.get("state").toString()).apply();
                    }
                    else {
                        profile.setBackgroundResource(R.drawable.default_profile);
                    }
//                        memberid = _childValue.get("memberid").toString();
//                        map = new HashMap<>();
//                        map.put("user_uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
//                        Currdata.child(memberid).updateChildren(map);

                }
            }

            @Override
            public void onCancelled(DatabaseError _param1) {
                final int _errorCode = _param1.getCode();
                final String _errorMessage = _param1.getMessage();

            }
        };
        Currdata.addChildEventListener(_Currdata_child_listener);

        _Ratings_child_listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot _param1, String _param2) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);
                if (!file.getString("email", "").equals("")) {
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
                if (!file.getString("email", "").equals("")) {
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
                if (!file.getString("email", "").equals("")) {
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


        _Seller_child_listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot _param1, String _param2) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);
                Seller.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot _dataSnapshot) {
                        sellerlistmap = new ArrayList<>();
                        try {
                            GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                            };
                            for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                                HashMap<String, Object> _map = _data.getValue(_ind);
                                sellerlistmap.add(_map);
                            }
                        } catch (Exception _e) {
                            _e.printStackTrace();
                        }
                        listview1.setAdapter(new Listview1Adapter(sellerlistmap));
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
                Seller.removeEventListener(_Seller_child_listener);
                sellerlistmap.clear();
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
                sellerlistmap.clear();
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
                _v = _inflater.inflate(R.layout.custom_seller_name, null);
            }

            final ImageView photo = (ImageView) _v.findViewById(R.id.photo);
            final TextView pName = (TextView) _v.findViewById(R.id.txtSeller);
            final ConstraintLayout parent = (ConstraintLayout) _v.findViewById(R.id.parent);

            pName.setText(sellerlistmap.get((int)_position).get("id").toString()+" || "+sellerlistmap.get((int)_position).get("sellerName").toString());

            if(sellerlistmap.get((int) _position).get("photo").toString().trim().equals(""))
            {
                photo.setImageResource(R.drawable.default_profile);
            }else{
                Glide.with(getApplicationContext()).load(Uri.parse(sellerlistmap.get((int) _position).get("photo").toString())).into(photo);
            }


            parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(HomePageActivity.this, sellerlistmap.get((int)_position).get("sellerName").toString(), Toast.LENGTH_SHORT).show();
                    file.edit().putString("sellerName",sellerlistmap.get((int)_position).get("sellerName").toString()).apply();
                    file.edit().putString("sellerId",sellerlistmap.get((int)_position).get("id").toString()).apply();
                    file.edit().putString("sellerEmail",sellerlistmap.get((int)_position).get("email").toString()).apply();
                    file.edit().putString("sellerPhoto",sellerlistmap.get((int)_position).get("photo").toString()).apply();
                    intent.setClass(getApplicationContext(), UserProductActivity.class);
                    startActivity(intent);
                }
            });
            return _v;
        }

    }
}
//
//    This activity is the dashboard for the user, this will be the first activity for user after successfully
//logged in. In this activity we are setting the appropriate profileimage of user, with the help of their email
//id data will be fetched from database
//
//we used three instances of database of node Seller, User and Ratings node-seller node: we need to show
//list of seller available so with the help of instance we can fetch tha data. user-node: we need to set the
//name and profile name of user to fetch the details we need to set those instances. Ratings node will be used
//to push the ratings of user regarding the application.
//
//Line 106:
//    this event listener will trigger when a back button is pressed, this section is used to check whether
//this particlar user had rated this app or not, if yes then on backpressed it will navigate to login page
//if no then it will show a bottomsheet dialog box, from there user can post their rating and submit it.
//In the start of activity it will check whether that user had rated or not, for that we had used an flagvariable
//and accordingly we had used it in code.
//
//we have used this code twice because we had use an imageview to jump to previous activity and and inbuild
//event listener is been used i.e. onbackPressed(), both do the same thing, so in both the cases we need to
//write the code.
//
//Line 248-297:
//
//    this section set the spinner and filter out the listview, according to the parameter, listview will
//set.
//
//Line 350-451:
//
//    Fetch the data of the user.
//
//Line 453-509:
//
//    Search if the user have rated this app or not by using their email id, and set the flag variable value
//
//Line 512-577
//        Fetch the data of the seller and set the data in listmap for listview.
//
//Line 580-638
//    This section is used to set the listmap by using the listmap we can fetch the data according to their
//ID and set it, here we have used custom_seller_name as a custom layout for listview to set the details.
