package com.omyser.myquiz;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class SubjectAdapter extends BaseAdapter {

    private List<SubjectModel> catList;

    public SubjectAdapter(List<SubjectModel> catList) {
        this.catList = catList;
    }

    @Override
    public int getCount() {
        return catList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {


        View view;

        if(convertView == null)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cat_item_layout,parent,false);
        }
        else
        {
            view = convertView;
        }


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SubjectActivity.selected_class_index = position;
                Intent intent = new Intent(parent.getContext(),ChaptersActivity.class);
                parent.getContext().startActivity(intent);
            }
        });


        ((TextView) view.findViewById(R.id.catName)).setText(catList.get(position).getName());


        return view;
    }
}
