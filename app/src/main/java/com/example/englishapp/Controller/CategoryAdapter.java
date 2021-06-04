package com.example.englishapp.Controller;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishapp.Model.Category;
import com.example.englishapp.R;
import com.example.englishapp.WordActivity;
import com.example.englishapp.fragments.VocaFragment;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private Activity activity;
    private List<Category> list;
    private Context context;
    private VocaFragment vocaFragment;

    public CategoryAdapter(Activity activity, Context context, VocaFragment vocaFragment){
        this.activity = activity;
        this.context = context;
        this.vocaFragment = vocaFragment;
    }

    public void setData(List<Category> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = activity.getLayoutInflater().inflate(R.layout.category_card, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.CategoryViewHolder holder, int position) {
        Category category = list.get(position);
        holder.tvCategoryName.setText(category.getName());
    }

    @Override
    public int getItemCount() {
        if (list != null)
            return list.size();
        else return 0;
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView tvCategoryName;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, WordActivity.class);
            int position = getLayoutPosition();
            Category category = list.get(position);
            intent.putExtra("category_id", category.getId());
            context.startActivity(intent);
        }

        @Override
        public boolean onLongClick(View v) {
            int position = getLayoutPosition();
            Category category = list.get(position);
            UpDelDialog(category);
            return true;
        }

        private void UpDelDialog(Category category){
            Dialog dialog = new Dialog(itemView.getContext());
            dialog.setContentView(R.layout.up_del_dialog);
            //initView
            Button btUpdate, btDelete;
            btUpdate = dialog.findViewById(R.id.btUpdateCategory);
            btDelete = dialog.findViewById(R.id.btDeleteCategory);

            btUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UpdateDialog(category);
                    dialog.dismiss();
                }
            });

            btDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CategorySQLHelper helper = new CategorySQLHelper(context);
                    int res = helper.deleteCategory(category.getId());
                    if (res > 0) {
                        vocaFragment.onResume();
                        dialog.dismiss();
                    } else
                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                }
            });
            dialog.show();
        }

        private void UpdateDialog(Category category){
            Dialog dialog = new Dialog(itemView.getContext());
            dialog.setContentView(R.layout.update_category_dialog);
            //initView
            EditText eCategoryName = dialog.findViewById(R.id.eCategoryName);
            eCategoryName.setText(category.getName());
            Button btUpdate, btCancel;
            btUpdate = dialog.findViewById(R.id.btUpdateCategory);
            btCancel = dialog.findViewById(R.id.btCancelCategory);

            btUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = eCategoryName.getText().toString();
                    CategorySQLHelper helper = new CategorySQLHelper(context);
                    long res = helper.updateCategory(new Category(category.getId(), name));
                    if (res > 0) {
                        vocaFragment.onResume();
                        dialog.dismiss();
                    } else
                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                }
            });

            btCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }
}
