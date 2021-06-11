package com.example.groceries2go;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderStatusActivity extends AppCompatActivity {

    private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();

    private Toolbar _toolbar;
    private String order = "";
    private String cart = "";
    private HashMap<String, Object> ordermap = new HashMap<>();

    private ArrayList<HashMap<String, Object>> cartlistmap = new ArrayList<>();
    private ArrayList<String> cartliststring = new ArrayList<>();

    private ArrayList<HashMap<String, Object>> orderlistmap = new ArrayList<>();
    private ArrayList<String> orderliststring = new ArrayList<>();

    private LinearLayout linear1;
    private LinearLayout linear2;
    private ListView listview1;
    private TextView textview1;
    private TextView textview2;

    private SharedPreferences file;
    private DatabaseReference Cart = _firebase.getReference("" + cart + " ");
    private ChildEventListener _Cart_child_listener;

    ImageView back;

    private DatabaseReference Order = _firebase.getReference("" + order + " ");
    private ChildEventListener _Order_child_listener;
    private AlertDialog.Builder dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);
        com.google.firebase.FirebaseApp.initializeApp(this);
        initialize(savedInstanceState);
        initializeLogic();
    }

    private void initializeLogic() {
        Cart.removeEventListener(_Cart_child_listener);
        cart = "Cart/".concat(file.getString("userId", ""));
        Cart = _firebase.getReference(cart);
        Cart.addChildEventListener(_Cart_child_listener);

        Order.removeEventListener(_Order_child_listener);
        order = "Order/".concat(file.getString("sellerId", ""));
        Order = _firebase.getReference(order);
        Order.addChildEventListener(_Order_child_listener);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void initialize(Bundle savedInstanceState) {
        listview1 = (ListView) findViewById(R.id.listview1);
//        textview1 = (TextView) findViewById(R.id.txttotal);
        textview2 = (TextView) findViewById(R.id.txtprice);
        back = findViewById(R.id.back);
        file = getSharedPreferences("file", Activity.MODE_PRIVATE);
        dialog = new AlertDialog.Builder(this);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        _Cart_child_listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot _param1, String _param2) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);
                Cart.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot _dataSnapshot) {
                        cartlistmap = new ArrayList<>();
                        try {
                            GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                            };
                            for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                                HashMap<String, Object> _map = _data.getValue(_ind);
                                cartlistmap.add(_map);
                            }
                        } catch (Exception _e) {
                            _e.printStackTrace();
                        }
                        listview1.setAdapter(new Listview1Adapter(cartlistmap));
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
                Cart.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot _dataSnapshot) {
                        cartlistmap = new ArrayList<>();
                        try {
                            GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                            };
                            for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                                HashMap<String, Object> _map = _data.getValue(_ind);
                                cartlistmap.add(_map);
                            }
                        } catch (Exception _e) {
                            _e.printStackTrace();
                        }
                        listview1.setAdapter(new Listview1Adapter(cartlistmap));
                        ((BaseAdapter) listview1.getAdapter()).notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError _databaseError) {
                    }
                });
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
                Cart.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot _dataSnapshot) {
                        cartlistmap = new ArrayList<>();
                        try {
                            GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                            };
                            for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                                HashMap<String, Object> _map = _data.getValue(_ind);
                                cartlistmap.add(_map);
                            }
                        } catch (Exception _e) {
                            _e.printStackTrace();
                        }
                        listview1.setAdapter(new Listview1Adapter(cartlistmap));
                        ((BaseAdapter) listview1.getAdapter()).notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError _databaseError) {
                    }
                });
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
            LayoutInflater _inflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View _v = _view;
            if (_v == null) {
                _v = _inflater.inflate(R.layout.custom_order_status, null);
            }

            final LinearLayout statusid = (LinearLayout) _v.findViewById(R.id.statusid);
            final TextView pName = (TextView) _v.findViewById(R.id.pName);
            final TextView sellerName = (TextView) _v.findViewById(R.id.sellerName);
            final TextView price = (TextView) _v.findViewById(R.id.price);
            final TextView address = (TextView) _v.findViewById(R.id.address);
            final ImageView status = (ImageView) _v.findViewById(R.id.status);
            final ImageView profile = (ImageView) _v.findViewById(R.id.profile);

            pName.setText("Product: "+cartlistmap.get((int) _position).get("pName").toString());
            price.setText("$ "+cartlistmap.get((int) _position).get("price").toString());
            sellerName.setText("Seller Name: "+cartlistmap.get((int) _position).get("sellerName").toString());
            address.setText("Address: "+cartlistmap.get((int) _position).get("address").toString());

            if(cartlistmap.get((int) _position).get("status").toString().toLowerCase().equals("pending"))
            {
                status.setImageResource(R.drawable.ic_loading);
                statusid.setBackgroundColor(Color.RED);
            }else{
                status.setImageResource(R.drawable.ic_done_all_black);
                statusid.setBackgroundColor(Color.GREEN);
            }
            if(cartlistmap.get((int) _position).get("productPhoto").toString().toLowerCase().trim().equals(""))
            {
                status.setImageResource(R.drawable.ic_product);
            }else{
                Glide.with(getApplicationContext()).load(Uri.parse(cartlistmap.get((int) _position).get("productPhoto").toString())).into(profile);

            }
            return _v;
        }
    }
}

//    this activity is visible to user only, here we have used Cart's node and order's node instances.
//here every product will be visible of cart for that particular user,
//
//here basically user get to the know the status of the product, user can simple get to know the status of
//products