package com.aariyan.expandablelistview.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aariyan.expandablelistview.Model.SubCategoryModel;
import com.aariyan.expandablelistview.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExAdapter extends BaseExpandableListAdapter {

    Context context;
    List<String> langs;
    //Map<String, List<String>> topics;
    Map<String, List<SubCategoryModel>> topics;

    public static List<SubCategoryModel> selectedItems = new ArrayList<>();

    public ExAdapter(Context context, List<String> langs, Map<String, List<SubCategoryModel>> topics) {
        this.context = context;
        this.langs = langs;
        this.topics = topics;
    }

    //size of the Title list
    @Override
    public int getGroupCount() {
        return langs.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return topics.get(langs.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return langs.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return topics.get(langs.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        String lang = (String) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_parent,null);
        }

        TextView textView = convertView.findViewById(R.id.txtParent);
        textView.setText(lang);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        //String topics = (String) getChild(groupPosition,childPosition);
        SubCategoryModel model = (SubCategoryModel) getChild(groupPosition,childPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_child,null);
        }

        TextView textView = convertView.findViewById(R.id.txtChild);
        textView.setText(model.getSub_category_name());

        //List<SubCategoryModel> selectedItems = new ArrayList<>();
        ImageView selection = convertView.findViewById(R.id.selection);

        //checking if already selected from the past
        if (model.isSelected()) {
            selection.setImageResource(R.drawable.check);

        } else {
            selection.setImageResource(R.drawable.add_icon);
        }

        selection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.setSelected(!model.isSelected());
                if (model.isSelected()) {
                    selection.setImageResource(R.drawable.check);
                    selectedItems.add(model);
                } else {
                   selection.setImageResource(R.drawable.add_icon);
                   selectedItems.remove(model);

                }
            }
        });

        return convertView;
    }

    //getting all the selected items
//    public List<SubCategoryModel>  getSelected() {
//        List<SubCategoryModel> selected = new ArrayList<>();
//
//        for (int i = 0; i < topics.size(); i++) {
//            if (top.get(i).isSelected()) {
//                selected.add(list.get(i));
//            }
//        }
//        return selected;
//
//    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
