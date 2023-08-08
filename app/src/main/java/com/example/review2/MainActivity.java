package com.example.review2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Window;
import android.widget.Toast;

import com.example.review2.adapter.ComicAdapter;
import com.example.review2.api.ApiService;
import com.example.review2.databinding.ActivityMainBinding;
import com.example.review2.databinding.DialogAddBinding;
import com.example.review2.models.Comic;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private List<Comic> comics;
    private Comic comic;
    private ComicAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initUi();

    }

    private void initUi() {

        adapter = new ComicAdapter(this);

        getAllComic();

        binding.fabAdd.setOnClickListener(view->{
            dialogAdd();
        });

        binding.edtSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (newText != null){
                    adapter.filterList(newText);
                }

                return true;
            }
        });

    }



    private void getAllComic() {

        ApiService.instance.getAllComic().enqueue(new Callback<List<Comic>>() {
            @Override
            public void onResponse(Call<List<Comic>> call, Response<List<Comic>> response) {
                comics = response.body();
                binding.tvQuantity.setText("Total quantity: "+comics.size());
                adapter.setList(comics);
                binding.rcv.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Comic>> call, Throwable t) {

            }
        });

    }

    private void dialogAdd() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        DialogAddBinding binding = DialogAddBinding.inflate(LayoutInflater.from(this));
        dialog.setView(binding.getRoot());
        AlertDialog alertDialog = dialog.create();

        Window window = alertDialog.getWindow();
        assert window != null;
        window.setBackgroundDrawableResource(R.color.transparent_background);


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
            String formatDate= null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                LocalDate currentDate = LocalDate.now();
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                formatDate = currentDate.format(dateTimeFormatter);
            }

            comic = new Comic("", nameComic, art, desc, author, stories, formatDate);
            addComic(comic);
            alertDialog.dismiss();
        });

        alertDialog.show();
    }

    private void addComic(Comic comic) {
        ApiService.instance.addComic(comic).enqueue(new Callback<Comic>() {
            @Override
            public void onResponse(Call<Comic> call, Response<Comic> response) {
                Toast.makeText(MainActivity.this, "Add successfully", Toast.LENGTH_SHORT).show();
                getAllComic();
            }

            @Override
            public void onFailure(Call<Comic> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllComic();
    }
}