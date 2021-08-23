package com.omyser.myquiz;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


import static com.omyser.myquiz.BoardActivity.catList;
import static com.omyser.myquiz.BoardActivity.selected_cat_index;
import static com.omyser.myquiz.ClassActivity.setsIDs;
import static com.omyser.myquiz.ClassActivity.selected_set_index;

public class SubjectActivity extends AppCompatActivity {

    private GridView classGrid;
    private FirebaseFirestore firestore;
    public static List<SubjectModel> classList = new ArrayList<>();
    public static int selected_class_index = 0;
    private Dialog loadingDialog;
    SubjectAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        classGrid = findViewById(R.id.classGridview);
        loadingDialog = new Dialog(SubjectActivity.this);
        loadingDialog.setContentView(R.layout.loading_progressbar);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawableResource(R.drawable.progress_background);
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);


        firestore = FirebaseFirestore.getInstance();

        loadData();




    }
    private void loadData()
    {
        loadingDialog.show();
        classList.clear();

        firestore.collection("QUIZ").document(catList.get(selected_cat_index).getId()).collection(setsIDs.get(selected_set_index)).document("CHAPTER_LIST")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful())
                {
                    DocumentSnapshot doc = task.getResult();

                    if(doc.exists())
                    {
                        long count = (long)doc.get("COUNT");

                        for(int i=1; i <= count; i++)
                        {
                            String catName = doc.getString("CHAP" + String.valueOf(i) + "_NAME");
                            String catID = doc.getString("CHAP" + String.valueOf(i) + "_ID");

                            classList.add(new SubjectModel(catID,catName));
                        }


                        adapter = new SubjectAdapter(classList);
                        classGrid.setAdapter(adapter);


                    }
                    else
                    {
                        Toast.makeText(SubjectActivity.this,"No Category Document Exists!",Toast.LENGTH_SHORT).show();
                        finish();
                    }

                }
                else
                {

                    Toast.makeText(SubjectActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismiss();
            }
        });

    }
    @Override
    protected void onResume() {
        super.onResume();

        if(adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home)
        {
            SubjectActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
