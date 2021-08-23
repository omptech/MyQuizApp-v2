package com.omyser.myquiz;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import static com.omyser.myquiz.ClassActivity.setsIDs;
public class ClassAdapter extends BaseAdapter {

    private int numOfSets;

    public ClassAdapter(int numOfSets) {
        this.numOfSets = numOfSets;
    }

    @Override
    public int getCount() {
        return numOfSets;
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
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.set_item_layout,parent,false);
        }
        else
        {
            view = convertView;
        }


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClassActivity.selected_set_index = position;
                Intent intent = new Intent(parent.getContext(), SubjectActivity.class);
                intent.putExtra("SETNO", position);
                parent.getContext().startActivity(intent);
            }
        });

        ((TextView) view.findViewById(R.id.setNo_tv)).setText(setsIDs.get(position));

        return view;
    }
}
