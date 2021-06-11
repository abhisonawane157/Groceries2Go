package com.example.groceries2go;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class UserCartActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_user_cart);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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
                totalcost = totalcost + Double.parseDouble(String.valueOf(Double.parseDouble(_childValue.get("price").toString())));
                textview2.setText("$ ".concat(String.valueOf(totalcost)));
            }

            @Override
            public void onChildChanged(DataSnapshot _param1, String _param2) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);
                totalcost = 00;
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
                totalcost = totalcost + Double.parseDouble(String.valueOf(Double.parseDouble(_childValue.get("price").toString())));
                textview2.setText("$ ".concat(String.valueOf(totalcost)));
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
                totalcost = 00;
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
                totalcost = totalcost + Double.parseDouble(String.valueOf(Double.parseDouble(_childValue.get("price").toString())));
                textview2.setText("$ ".concat(String.valueOf(totalcost)));
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
                totalcost = 00;
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
                _v = _inflater.inflate(R.layout.custom_order_add_remove, null);
            }

            final LinearLayout linear1 = (LinearLayout) _v.findViewById(R.id.linear1);
            final TextView dishname = (TextView) _v.findViewById(R.id.dishname);
            final TextView textview2 = (TextView) _v.findViewById(R.id.textview2);
            final TextView textview1 = (TextView) _v.findViewById(R.id.textview1);
            final Button button1 = (Button) _v.findViewById(R.id.button1);

            dishname.setText(cartlistmap.get((int) _position).get("pName").toString());
            textview1.setText("$ "+cartlistmap.get((int) _position).get("price").toString());
//            textview2.setText(cartlistmap.get((int) _position).get("quantity").toString());
            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View _view) {
                    if (!(cartlistmap.get((int) _position).get("status").toString().toLowerCase().equals("done") || cartlistmap.get((int) _position).get("status").toString().equals("Served"))) {
                        Order.removeEventListener(_Order_child_listener);
                        order = "Order/".concat(cartlistmap.get((int) _position).get("sellerId").toString());
                        Order = _firebase.getReference(order);
                        Order.addChildEventListener(_Order_child_listener);
                        Order.child(cartlistmap.get((int) _position).get("location").toString()).removeValue();
                        Cart.child(cartlistmap.get((int) _position).get("location").toString()).removeValue();
                        finish();
                    } else {
                        dialog.setTitle(cartlistmap.get((int) _position).get("pName").toString());
                        dialog.setMessage("We apologise, now your order won't be cancelled.");
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

//In this activity user can view their product, which he/she had added in cart to buy, this section
// is visble to user only from here user can delete the product if he/she wishes to do so. once the
//product is been delievered user will unavailable to remove it from cart