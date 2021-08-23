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


import static com.omyser.myquiz.SubjectActivity.classList;
import static com.omyser.myquiz.BoardActivity.catList;
import static com.omyser.myquiz.ClassActivity.setsIDs;
import static com.omyser.myquiz.ClassActivity.selected_set_index;
import static com.omyser.myquiz.ChaptersActivity.selected_chapter_index;
import static com.omyser.myquiz.ChaptersActivity.ChaptersList;
import static com.omyser.myquiz.SubjectActivity.selected_class_index;


public class CategoryActivity extends AppCompatActivity {

    private GridView classGrid;
    private FirebaseFirestore firestore;
    public static List<CategoryModel> categList = new ArrayList<>();
    public static int selected_category_index = 0;
    private Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        classGrid = findViewById(R.id.catGridview);
        loadingDialog = new Dialog(CategoryActivity.this);
        loadingDialog.setContentView(R.layout.loading_progressbar);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawableResource(R.drawable.progress_background);
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();

        firestore = FirebaseFirestore.getInstance();

        loadData();




    }
    private void loadData()
    {
        categList.clear();

        firestore.collection("QUIZ").document(catList.get(BoardActivity.selected_cat_index).getId()).collection(setsIDs.get(selected_set_index)).document(classList.get(selected_class_index).getId())
                .collection(ChaptersList.get(selected_chapter_index)).document("CHAPTER_SETS").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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

                            categList.add(new CategoryModel(catID,catName));
                        }


                        CategoryAdapter adapter = new CategoryAdapter(categList);
                        classGrid.setAdapter(adapter);
                        loadingDialog.dismiss();

                    }
                    else
                    {
                        Toast.makeText(CategoryActivity.this,"No Category Document Exists!",Toast.LENGTH_SHORT).show();
                        finish();
                    }

                }
                else
                {

                    Toast.makeText(CategoryActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home)
        {
            CategoryActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
