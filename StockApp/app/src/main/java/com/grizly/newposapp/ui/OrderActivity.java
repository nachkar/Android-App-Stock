package com.grizly.newposapp.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.grizly.newposapp.Config;
import com.grizly.newposapp.Methods;
import com.grizly.newposapp.R;
import com.grizly.newposapp.beans.Order;
import com.grizly.newposapp.beans.SpinnerItem;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderActivity extends AppCompatActivity {

    public ArrayList<Order> orderList = new ArrayList<Order>();
    public ArrayList<Order> useorderList = new ArrayList<Order>();

    ArrayList productlist = new ArrayList<SpinnerItem>();
    SpinnerAdapter orderAdapter;
    Spinner spinner;

    AppCompatEditText name, type;
    AppCompatButton btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        AppCompatTextView title = (AppCompatTextView) toolbar.findViewById(R.id.title);
        title.setText("Create Order");
        setSupportActionBar(toolbar);

        name = (AppCompatEditText) findViewById(R.id.customer_et);
        type = (AppCompatEditText) findViewById(R.id.type_et);
        btn = (AppCompatButton) findViewById(R.id.register);

        productlist = SpinnerItem.getPrefArraylist(Config.PREF_KEY_LIST_SPINNER, OrderActivity.this);
//        productlist.add(new SpinnerItem("1", "Pen"));
//        productlist.add(new SpinnerItem("1", "Pencil"));
//        productlist.add(new SpinnerItem("1", "laptop"));
//        productlist.add(new SpinnerItem("1", "Dossier"));

        orderAdapter = new SpinnerAdapter();
        orderAdapter.addItems(productlist);
        spinner = (Spinner) findViewById(R.id.product_spinner);
        spinner.setAdapter(orderAdapter);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().trim().length() < 1 ||
                        type.getText().toString().trim().length() < 1) {
                    Toast.makeText(OrderActivity.this, "Missing Fields", Toast.LENGTH_SHORT).show();
                } else {
                    ArrayList<Order> orderList = Order.getPrefArraylist(Config.PREF_KEY_LIST_ORDERS, OrderActivity.this);

                    orderList.add(new Order(type.getText().toString(), DateFormat.getDateTimeInstance().format(new Date())));
                    Methods.savePrefObject(orderList, Config.PREF_KEY_LIST_ORDERS, OrderActivity.this);
                    finish();
                }
            }
        });

        orderAdapter.notifyDataSetChanged();
    }

    public class SpinnerAdapter extends BaseAdapter {
        private List<SpinnerItem> mItems = new ArrayList<SpinnerItem>();
        LayoutInflater mInflater;

        public void clear() {
            mItems.clear();
        }

        public void addItem(SpinnerItem item) {
            mItems.add(item);
        }

        public void addItems(List<SpinnerItem> item) {
            mItems.addAll(item);
        }

        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public SpinnerItem getItem(int position) {
            return mItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getDropDownView(int position, View view, ViewGroup parent) {
            if (view == null || !view.getTag().toString().equals("DROPDOWN")) {
                mInflater = LayoutInflater.from(OrderActivity.this);
                view = mInflater.inflate(R.layout.spinner_item_dropdown, parent, false);
                view.setTag("DROPDOWN");
            }

            TextView textView = (TextView) view.findViewById(R.id.item_text);
            textView.setText(getTitle(position));

            return view;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if (view == null || !view.getTag().toString().equals("NON_DROPDOWN")) {
                mInflater = LayoutInflater.from(OrderActivity.this);
                view = mInflater.inflate(R.layout.
                        spinner_item, parent, false);
                view.setTag("NON_DROPDOWN");
            }
            TextView textView = (TextView) view.findViewById(R.id.item_text);
            textView.setText(getTitle(position));
            return view;
        }

        private String getTitle(int position) {
            return position >= 0 && position < mItems.size() ? mItems.get(position).name : "";
        }
    }
}