package com.omyser.myquiz;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import static com.omyser.myquiz.ChaptersActivity.ChaptersList;
import static com.omyser.myquiz.ChaptersActivity.selected_chapter_index;
import static com.omyser.myquiz.BoardActivity.catList;
import static com.omyser.myquiz.SubjectActivity.classList;
import static com.omyser.myquiz.ClassActivity.setsIDs;
import static com.omyser.myquiz.ClassActivity.selected_set_index;
import static com.omyser.myquiz.CategoryActivity.categList;
import static com.omyser.myquiz.CategoryActivity.selected_category_index;
import static com.omyser.myquiz.SubjectActivity.selected_class_index;

public class CatSetActivity extends AppCompatActivity {
    public static int selected_cat_set__index = 0;
    private GridView sets_grid;
    private FirebaseFirestore firestore;
    private Dialog loadingDialog;

    public static List<String> CatSetList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_set);

        sets_grid = findViewById(R.id.catset_gridview);


        loadingDialog = new Dialog(CatSetActivity.this);
        loadingDialog.setContentView(R.layout.loading_progressbar);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawableResource(R.drawable.progress_background);
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();

        firestore = FirebaseFirestore.getInstance();

        loadSets();

    }


    public void loadSets()
    {

        CatSetList.clear();

        firestore.collection("QUIZ").document(catList.get(BoardActivity.selected_cat_index).getId()).collection(setsIDs.get(selected_set_index)).document(classList.get(selected_class_index).getId())
                .collection(ChaptersList.get(selected_chapter_index)).document(categList.get(selected_category_index).getId())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                long noOfSets = (long)documentSnapshot.get("SETS");

                for(int i=1; i <= noOfSets; i++)
                {
                    CatSetList.add(documentSnapshot.getString("SET" + String.valueOf(i) + "_ID"));
                }

                CatSetAdapter adapter = new CatSetAdapter(CatSetList.size());
                sets_grid.setAdapter(adapter);

                loadingDialog.dismiss();

            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CatSetActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        loadingDialog.dismiss();
                    }
                });


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home)
        {
            CatSetActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
