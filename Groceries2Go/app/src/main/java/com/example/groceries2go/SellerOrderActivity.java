package com.example.groceries2go;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class SellerOrderActivity extends AppCompatActivity {

    private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();

    private Toolbar _toolbar;
    private String order = "";
    private String cart = "";
    private double totalcost = 0;
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
    private ImageView back;

    private Intent intent = new Intent();
    private SharedPreferences file;
    private DatabaseReference Cart = _firebase.getReference("" + cart + " ");
    private ChildEventListener _Cart_child_listener;

    private DatabaseReference Order = _firebase.getReference("" + order + " ");
    private ChildEventListener _Order_child_listener;
    private AlertDialog.Builder dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_order);
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

        totalcost = 00;
    }
    private void initialize(Bundle savedInstanceState) {
        listview1 = (ListView) findViewById(R.id.listview1);
        textview1 = (TextView) findViewById(R.id.txttotal);
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
                Order.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot _dataSnapshot) {
                        orderlistmap = new ArrayList<>();
                        try {
                            GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                            };
                            for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                                HashMap<String, Object> _map = _data.getValue(_ind);
                                orderlistmap.add(_map);
                            }
                        } catch (Exception _e) {
                            _e.printStackTrace();
                        }
                        listview1.setAdapter(new Listview1Adapter(orderlistmap));
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
                totalcost = 00;
                Order.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot _dataSnapshot) {
                        orderlistmap = new ArrayList<>();
                        try {
                            GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                            };
                            for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                                HashMap<String, Object> _map = _data.getValue(_ind);
                                orderlistmap.add(_map);
                            }
                        } catch (Exception _e) {
                            _e.printStackTrace();
                        }
                        listview1.setAdapter(new Listview1Adapter(orderlistmap));
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
                Order.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot _dataSnapshot) {
                        orderlistmap = new ArrayList<>();
                        try {
                            GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                            };
                            for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                                HashMap<String, Object> _map = _data.getValue(_ind);
                                orderlistmap.add(_map);
                            }
                        } catch (Exception _e) {
                            _e.printStackTrace();
                        }
                        listview1.setAdapter(new Listview1Adapter(orderlistmap));
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
                _v = _inflater.inflate(R.layout.custom_seller_order_add_remove, null);
            }

            final LinearLayout linear1 = (LinearLayout) _v.findViewById(R.id.linear1);
            final TextView pName = (TextView) _v.findViewById(R.id.pName);
            final TextView uName = (TextView) _v.findViewById(R.id.uName);
            final TextView address = (TextView) _v.findViewById(R.id.address);
            final TextView price = (TextView) _v.findViewById(R.id.price);
            final ImageView profile = (ImageView) _v.findViewById(R.id.profile);
            final ImageView status = (ImageView) _v.findViewById(R.id.status);

            pName.setText(orderlistmap.get((int) _position).get("pName").toString());
            uName.setText(orderlistmap.get((int) _position).get("userName").toString());
            address.setText(orderlistmap.get((int) _position).get("address").toString()+" | "
                            +orderlistmap.get((int) _position).get("city").toString()+" | "
                            +orderlistmap.get((int) _position).get("state").toString()+" | "
                            +orderlistmap.get((int) _position).get("country").toString());
            price.setText("$ "+orderlistmap.get((int) _position).get("price").toString());

            if(orderlistmap.get((int) _position).get("userPhoto").toString().trim().equals(""))
            {
                profile.setImageResource(R.drawable.default_profile);
            }else{
                Glide.with(getApplicationContext()).load(Uri.parse(orderlistmap.get((int) _position).get("userPhoto").toString())).into(profile);
            }
            if(orderlistmap.get((int) _position).get("status").toString().trim().toLowerCase().equals("done"))
            {
                status.setImageResource(R.drawable.ic_done_all_black);
            }else{
                status.setImageResource(R.drawable.ic_loading);
            }



            status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View _view) {
                    if (!(orderlistmap.get((int) _position).get("status").toString().toLowerCase().equals("done"))) {
                        dialog.setTitle(orderlistmap.get((int) _position).get("pName").toString());
                        dialog.setMessage("Is the order delivered?");
                        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface _dialog, int _which) {
                                ordermap = new HashMap<>();
                                ordermap.put("status", "Done");
                                Order.child(orderlistmap.get((int) _position).get("location").toString()).updateChildren(ordermap);
                                Cart.child(orderlistmap.get((int) _position).get("location").toString()).updateChildren(ordermap);
                            }
                        });
                        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface _dialog, int _which) {

                            }
                        });
                        dialog.create().show();
                    } else {
                        dialog.setTitle(orderlistmap.get((int) _position).get("pName").toString());
                        dialog.setMessage("The product is already delivered.");
                        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface _dialog, int _which) {

                            }
                        });
                        dialog.create().show();
                    }
                }
            });

            return _v;
        }
    }
}

//This activity is used to view the product which had been ordered by user, only seller can view this
//activity, seller can change the status of the order i.e. either Pending or Complete.