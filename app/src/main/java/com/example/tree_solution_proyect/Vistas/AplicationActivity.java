package com.example.tree_solution_proyect.Vistas;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.tree_solution_proyect.Objetos.Firebase.Libro;
import com.example.tree_solution_proyect.Persistencia.LibroDAO;
import com.example.tree_solution_proyect.Persistencia.UsuarioDAO;
import com.example.tree_solution_proyect.R;
import com.example.tree_solution_proyect.Vistas.ui.comunicacion.ComunicacionFragment;
import com.example.tree_solution_proyect.Vistas.ui.favorite.FavoriteFragment;
import com.example.tree_solution_proyect.Vistas.ui.home.HomeFragment;
import com.example.tree_solution_proyect.Vistas.ui.perfil.PerfilFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenImage;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AplicationActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference databaseReferenceLibro;
    private Dialog myDialog;
    private ImagePicker imagePicker;
    private TextView editTextAuthor,textViewExit,editTextISBN,
            editTextPrice,editTextName,editTextDescripcion;
    private Button buttonSubmitBook;
    private Spinner spinnerContidion,spinnerCategory;
    private ImageView imageView;
    protected Uri imageUri;
    private String StrinUrl,mGroupId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aplication);
        myDialog = new Dialog(this);


        database=FirebaseDatabase.getInstance();
        databaseReferenceLibro=database.getReference("Libros");


       imagePicker=new ImagePicker(this);

        imagePicker.setImagePickerCallback(new ImagePickerCallback() {
            @Override
            public void onImagesChosen(List<ChosenImage> list) {
                if(!list.isEmpty()){
                    String path=list.get(0).getOriginalPath();
                    imageUri= Uri.parse(path);
                    if(imageUri!=null) {
                        LibroDAO.getInstance().cambiarFotoUri(imageUri,mGroupId, new UsuarioDAO.IDevolverUrlFoto() {
                            @Override
                            public void DevolverUrlFoto(String uri) {
                                Glide.with(getApplication().getApplicationContext())
                                        .load(uri)
                                        .into(imageView);
                                Toast.makeText(getApplicationContext(),"Foto subida para subir otra repite el proceso",Toast.LENGTH_SHORT).show();
                               StrinUrl=uri;
                            }
                        });
                    }

                }
            }

            @Override
            public void onError(String s) {

            }
        });
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
                        editTextDescripcion = myDialog.findViewById(R.id.editTextTextMultiLineDesc);
                        imageView = myDialog.findViewById(R.id.imageViewImage);
                        buttonSubmitBook = myDialog.findViewById(R.id.buttonSubmitBook);
                        spinnerContidion = myDialog.findViewById(R.id.spinnerCondition);
                        spinnerCategory = myDialog.findViewById(R.id.spinnerCategory);

                        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        adapterCondition.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        //asignamos codigo para storage y database
                        mGroupId = databaseReferenceLibro.push().getKey();

                        spinnerCategory.setAdapter(adapterCategory);
                        spinnerContidion.setAdapter(adapterCondition);

                        spinnerContidion.setOnItemSelectedListener(new conditionCategory());
                        spinnerCategory.setOnItemSelectedListener(new spinnerCategory());

                        buttonSubmitBook.setOnClickListener(new submitBook()); //subir libro/pedido
                        textViewExit.setOnClickListener(new exit()); //cerrar
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                imagePicker.pickImage();

                            }
                        });

                        myDialog.show();
                        return true;
                    case R.id.navigation_favorite:
                        addFragment(new FavoriteFragment());

                        return true;
                    case R.id.navigation_comunicacion:
                        addFragment(new ComunicacionFragment());

                        return true;
                    case R.id.navigation_perfil:
                        addFragment(new PerfilFragment());

                        return true;

                }
                return false;
            }
        });

    }

    private void addFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment_activity_aplication2, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
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

            if ((!editTextAuthor.getText().toString().isEmpty()) && (!editTextName.getText().toString().isEmpty()) &&
                    (!editTextISBN.getText().toString().isEmpty()) && (!editTextPrice.getText().toString().isEmpty()) && (StrinUrl != null)) {
                if(editTextISBN.getText().length()==13){

                Libro libro = new Libro();
                libro.setAutor(editTextAuthor.getText().toString());
                libro.setNombre(editTextName.getText().toString());

                libro.setISBN(editTextISBN.getText().toString());
                if (!editTextPrice.getText().toString().isEmpty()) {
                    libro.setPrecio(Double.parseDouble(editTextPrice.getText().toString()));
                }

                libro.setCondition(spinnerContidion.getSelectedItem().toString());
                libro.setCategoria(spinnerCategory.getSelectedItem().toString());
                libro.setUserKey(LibroDAO.getKeyUsuario());
                libro.setFotoPrincipalUrl(StrinUrl);
                libro.setReferenceStorage(mGroupId);
                libro.setEsVendido("No");

                try {
                    if (libro != null) {
                        databaseReferenceLibro.child(mGroupId).setValue(libro);
                        Toast.makeText(getApplicationContext(), "Libro se subío correcto", Toast.LENGTH_SHORT).show();
                        myDialog.dismiss();
                    } else {
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }else{
                    Toast.makeText(getApplicationContext(), "Es obligatorio a que ISBN contiene 13 caracteres", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "No has rellenado todos los datos ,vuelva a revisar los datos introducidos", Toast.LENGTH_SHORT).show();
            }

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
    //Recoger datos de Intent producido en esta clase
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Picker.PICK_IMAGE_DEVICE && resultCode == RESULT_OK){
            imagePicker.submit(data);
        }
    }
}