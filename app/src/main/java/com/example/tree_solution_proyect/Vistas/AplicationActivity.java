package com.example.tree_solution_proyect.Vistas;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.tree_solution_proyect.R;
import com.example.tree_solution_proyect.Vistas.ui.comunicacion.ComunicacionFragment;
import com.example.tree_solution_proyect.Vistas.ui.favorite.FavoriteFragment;
import com.example.tree_solution_proyect.Vistas.ui.home.HomeFragment;
import com.example.tree_solution_proyect.Vistas.ui.perfil.PerfilFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class AplicationActivity extends AppCompatActivity {


    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    final private String Database_Path = "allImageUploadDatabase";
    final private String Storage_Path = "allImageUploads/";

    private int Image_Request_Code = 7;

    private boolean viewIsAtHome;
    Dialog myDialog;

    private TextView editTextAuthor;
    private TextView editTextName;
    private TextView editTextPrice;
    private TextView editTextISBN;
    private TextView textViewExit;

    private Button buttonSubmitBook;
    private Button buttonSubmitImage;

    private Spinner spinnerContidion;
    private Spinner spinnerCategory;

    private ImageView imageView;

    protected Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aplication);
        myDialog = new Dialog(this);

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);

        BottomNavigationView navView = findViewById(R.id.nav_view);

        ArrayAdapter<CharSequence> adapterCategory = ArrayAdapter.createFromResource(
                this,
                R.array.category,
                R.layout.support_simple_spinner_dropdown_item
        );
        ArrayAdapter<CharSequence> adapterCondition = ArrayAdapter.createFromResource(
                this,
                R.array.conditions,
                R.layout.support_simple_spinner_dropdown_item
        );

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        addFragment(new HomeFragment());
                        viewIsAtHome = true;
                        return true;
                    case R.id.navigation_newpost:
                        //asignamos layout a pop up
                        myDialog.setContentView(R.layout.activity_newpost);
                        //vinculamos los componentes
                        editTextAuthor = myDialog.findViewById(R.id.editTextAuthor);
                        editTextName = myDialog.findViewById(R.id.editTextName);
                        editTextPrice = myDialog.findViewById(R.id.editTextPrice);
                        editTextISBN = myDialog.findViewById(R.id.editTextISBN);
                        textViewExit = myDialog.findViewById(R.id.textViewExit);
                        imageView = myDialog.findViewById(R.id.imageViewImage);
                        buttonSubmitBook = myDialog.findViewById(R.id.buttonSubmitBook);
                        spinnerContidion = myDialog.findViewById(R.id.spinnerCondition);
                        spinnerCategory = myDialog.findViewById(R.id.spinnerCategory);

                        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        adapterCondition.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        spinnerCategory.setAdapter(adapterCategory);
                        spinnerContidion.setAdapter(adapterCondition);

                        spinnerContidion.setOnItemSelectedListener(new conditionCategory());
                        spinnerCategory.setOnItemSelectedListener(new spinnerCategory());

                        buttonSubmitBook.setOnClickListener(new submitBook()); //subir libro/pedido
                        textViewExit.setOnClickListener(new exit()); //cerrar
                        imageView.setOnClickListener(new submitImage()); //subir imagen

                        myDialog.show();
                        return true;
                    case R.id.navigation_favorite:
                        addFragment(new FavoriteFragment());
                        viewIsAtHome = false;
                        return true;
                    case R.id.navigation_comunicacion:
                        addFragment(new ComunicacionFragment());
                        viewIsAtHome = false;
                        return true;
                    case R.id.navigation_perfil:
                        addFragment(new PerfilFragment());
                        viewIsAtHome = false;
                        return true;

                }
                return false;
            }
        });
        navView.setSelectedItemId(R.id.navigation_home);

    }

    private void addFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment_activity_aplication, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (!viewIsAtHome) { //Si la vista actual no es el fragment Home
            BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_view);
            bottomNavigationView.setSelectedItemId(R.id.navigation_home); //Selecciona el fragment Home
        } else {
            moveTaskToBack(true);  //Si presionas Back cuando ya muestras el fragment Home, sale de la app
        }
    }

    class exit implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            myDialog.dismiss();
        }
    }


    class submitBook implements View.OnClickListener {
        @Override
        public void onClick(View v) {

        }
    }

    class spinnerCategory implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    class conditionCategory implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    class submitImage extends AppCompatActivity implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // Creating intent.
            Intent intent = new Intent();

            // Setting intent type as image to select image from phone storage.
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Please Select Image"), Image_Request_Code);
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
                imageUri = data.getData();
                imageView.setImageURI(imageUri);
            }
        }

    }
}