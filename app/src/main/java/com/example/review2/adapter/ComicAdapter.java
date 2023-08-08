package com.example.review2.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.review2.Detail;
import com.example.review2.MainActivity;
import com.example.review2.R;
import com.example.review2.api.ApiService;
import com.example.review2.databinding.DialogAddBinding;
import com.example.review2.databinding.DialogPreviewBinding;
import com.example.review2.models.Comic;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ComicAdapter extends RecyclerView.Adapter<ComicAdapter.ComicViewHolder> {

    private Context context;
    private List<Comic> list;
    private List<Comic> fullList;

    public ComicAdapter(Context context) {
        this.context = context;
        this.list = new ArrayList<>();
        this.fullList = new ArrayList<>();
    }

    public void setList(List<Comic> list){
        this.list = list;
        this.fullList = new ArrayList<>(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ComicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ComicViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_comic, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ComicViewHolder holder, int position) {
        Comic current = list.get(holder.getAdapterPosition());
        if (current == null){
            return;
        }

        Glide.with(context).load(current.getArt()).error(R.drawable.i8).into(holder.art);
        holder.title.setText(current.getNameComic());
        holder.itemSelected.setOnClickListener(v->{
            showDialogPreview(current, holder.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void filterList(String newText){
       list.clear();
       String searchLowerCase = newText.toLowerCase();
       for (Comic item: fullList){
           if(item.getNameComic() != null && item.getNameComic().toLowerCase().contains(searchLowerCase)){
               list.add(item);
           }
       }
       notifyDataSetChanged();
    }

    private void showDialogPreview(Comic comic, int position) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        DialogPreviewBinding binding = DialogPreviewBinding.inflate(LayoutInflater.from(context));
        dialog.setView(binding.getRoot());
        AlertDialog alertDialog = dialog.create();

        Window window = alertDialog.getWindow();
        assert window != null;
        window.setBackgroundDrawableResource(R.color.transparent_background);

        Glide.with(context).load(comic.getArt()).error(R.drawable.i8).into(binding.imgArt);
        binding.tvAuthor.setText(comic.getAuthor());
        binding.tvNameComic.setText(comic.getNameComic());
        binding.tvDesc.setText(comic.getDesc());
        binding.tvCreateDate.setText(comic.getCreatedAt());

        binding.btnDetail.setOnClickListener(v->{
            Intent intent = new Intent(context, Detail.class);
            intent.putExtra("comic", comic);
            context.startActivity(intent);
            alertDialog.dismiss();
        });

        binding.btnOptions.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(context, view);
            popupMenu.inflate(R.menu.menu_option);

            popupMenu.setOnMenuItemClickListener(item->{

                int itemId = item.getItemId();

                if (itemId == R.id.action_edit) {
                    showDialogUpdate(comic);
                    alertDialog.dismiss();
                    return true;
                } else if (itemId == R.id.action_delete) {
                    confirmDelete(comic, position);
                    alertDialog.dismiss();
                    return true;
                }

                return false;
            });

            popupMenu.show();
        });

        alertDialog.show();

    }

    private void showDialogUpdate(Comic comic) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        DialogAddBinding binding = DialogAddBinding.inflate(LayoutInflater.from(context));
        dialog.setView(binding.getRoot());
        AlertDialog alertDialog = dialog.create();

        Window window = alertDialog.getWindow();
        assert window != null;
        window.setBackgroundDrawableResource(R.color.transparent_background);

        binding.edtNameComic.setText(comic.getNameComic());
        binding.edtAuthor.setText(comic.getAuthor());
        binding.edtLinkArt.setText(comic.getArt());
        binding.edtDesc.setText(comic.getDesc());
        binding.edtStories.setText(comic.getStories());

        binding.btnCancel.setOnClickListener(v->{
            binding.edtNameComic.setText("");
            binding.edtAuthor.setText("");
            binding.edtLinkArt.setText("");
            binding.edtDesc.setText("");
            binding.edtStories.setText("");
            alertDialog.dismiss();
        });

        binding.btnSave.setOnClickListener(v->{
            String nameComic = binding.edtNameComic.getText().toString();
            String author = binding.edtAuthor.getText().toString();
            String art = binding.edtLinkArt.getText().toString();
            String desc = binding.edtDesc.getText().toString();
            String stories = binding.edtStories.getText().toString();

            comic.setNameComic(nameComic);
            comic.setAuthor(author);
            comic.setArt(art);
            comic.setDesc(desc);
            comic.setStories(stories);

            updateComic(comic.getId(), comic);
            alertDialog.dismiss();
        });

        alertDialog.show();
    }

    private void updateComic(String id, Comic comic) {
        ApiService.instance.editComic(id, comic).enqueue(new Callback<Comic>() {
            @Override
            public void onResponse(Call<Comic> call, Response<Comic> response) {
                notifyDataSetChanged();
                Toast.makeText(context, "update successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Comic> call, Throwable t) {
                Toast.makeText(context, "update fail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void confirmDelete(Comic comic, int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete this!!!");
        builder.setMessage("Are you sure you want to delete?");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteComic(comic.getId(), position);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.show();

    }

    private void deleteComic(String id, int position) {
        ApiService.instance.deleteComic(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                list.remove(position);
                notifyItemRemoved(position);
                Toast.makeText(context, "Delete successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Delete fail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    class ComicViewHolder extends RecyclerView.ViewHolder{

        private TextView title;
        private ImageView art;
        private CardView itemSelected;
        public ComicViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_title);
            art = itemView.findViewById(R.id.img_art);
            itemSelected = itemView.findViewById(R.id.item_selected);
        }
    }

}
