package com.aariyan.expandablelistview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aariyan.expandablelistview.Adapter.ExAdapter;
import com.aariyan.expandablelistview.Model.SubCategoryModel;
import com.aariyan.expandablelistview.Utility.Common;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.aariyan.expandablelistview.Adapter.ExAdapter.selectedItems;

public class MainActivity extends AppCompatActivity {

    private ExpandableListView expandableListView;
    private List<String> lang = new ArrayList<>();
    Map<String, List<SubCategoryModel>> topics = new HashMap<>();

    ExpandableListAdapter listAdapter;

    //Button for show data on another activity
    private TextView saveBtn;

    private ProgressBar progressBar;


    //Volley member variable
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //instantiate volley:
        requestQueue = Volley.newRequestQueue(this);

        initUI();
    }

    private void initUI() {
        progressBar = findViewById(R.id.progressbar);

        saveBtn = findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Home.class));
                //Toast.makeText(MainActivity.this, ""+selectedItems.size(), Toast.LENGTH_SHORT).show();
            }
        });

        expandableListView = findViewById(R.id.expandableListView);
//        listAdapter = new ExAdapter(this,lang,topics);
//        expandableListView.setAdapter(listAdapter);

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                //Toast.makeText(MainActivity.this, "" + topics.get(lang.get(groupPosition)).get(childPosition), Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        String url = "https://www.test.api.liker.com/get_categories/";

        if (!Common.isInternetConnected(MainActivity.this)) {
            Toast.makeText(this, "Please check your internet connection!", Toast.LENGTH_LONG).show();

            //setting progressbar invisible:
            progressBar.setVisibility(View.GONE);
        } else {
            JsonObjectRequest categories = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    //clearing the list first before adding data:
                    //if everything is ok (Perfect response)
                    try {
                        //getting the parent array:
                        JSONArray categoriesArray = response.getJSONArray("categories");
                        //running a loop for traversing all the JSON Object:
                        for (int i = 0; i < categoriesArray.length(); i++) {
                            JSONObject particularObject = categoriesArray.getJSONObject(i);
                            String cat = particularObject.getString("category_name");
                            lang.add(cat);

                            JSONArray subCategory = particularObject.getJSONArray("subcatg");
                            List<SubCategoryModel> subList = new ArrayList<>();
                            if (subCategory.length() > 0) {
                                for (int j = 0; j < subCategory.length(); j++) {
                                    JSONObject object = subCategory.getJSONObject(j);
                                    //subList.add(object.getString("sub_category_name"));
                                    // Log.d("TEST_RESULT", lang+" : "+subList.get(j));


                                    SubCategoryModel model =
                                            new SubCategoryModel(object.getString("sub_category_name"));
                                    subList.add(model);
                                }
                            }

                            topics.put(lang.get(i), subList);
                        }

                        listAdapter = new ExAdapter(MainActivity.this, lang, topics);
                        expandableListView.setAdapter(listAdapter);

                        //setting progressbar invisible:
                        progressBar.setVisibility(View.GONE);

                    } catch (JSONException e) {
                        e.printStackTrace();

                        //setting progressbar invisible:
                        progressBar.setVisibility(View.GONE);
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //If something error is happen
                    Toast.makeText(MainActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();

                    //setting progressbar invisible:
                    progressBar.setVisibility(View.GONE);
                }
            });

            requestQueue.add(categories);
        }

//        lang.add("Java");
//        lang.add("C");

    }
}