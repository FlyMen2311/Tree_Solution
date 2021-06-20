package com.example.tree_solution_proyect.Vistas;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.tree_solution_proyect.Adaptadores.Adapter_Libro;
import com.example.tree_solution_proyect.Adaptadores.Adapter_MisLibros;
import com.example.tree_solution_proyect.Holders.Holder_Chats;
import com.example.tree_solution_proyect.Objetos.Constantes;
import com.example.tree_solution_proyect.Objetos.Firebase.Chat;
import com.example.tree_solution_proyect.Objetos.Firebase.Libro;
import com.example.tree_solution_proyect.Objetos.Firebase.Usuario;
import com.example.tree_solution_proyect.Objetos.Logica.LChat;
import com.example.tree_solution_proyect.Objetos.Logica.LLibro;
import com.example.tree_solution_proyect.Objetos.Logica.LUsuario;
import com.example.tree_solution_proyect.Persistencia.LibroDAO;
import com.example.tree_solution_proyect.Persistencia.UsuarioDAO;
import com.example.tree_solution_proyect.R;
import com.example.tree_solution_proyect.Vistas.ui.home.HomeFragment;
import com.example.tree_solution_proyect.Vistas.ui.perfil.PerfilFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.lang.invoke.ConstantCallSite;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MisLibrosClickActivity extends AppCompatActivity {
    private ImageView foto_libro,imageViewdialog;
    private ImageView favorit;
    private ImageView foto_libro_propietario;
    private TextView  nombre_libro_propietario;
    private TextView nombre;
    private TextView autor;
    private TextView categoria;
    private TextView ISBN;
    private TextView condition;
    private TextView precio;
    private TextView hora;
    private Context context;
    private RatingBar ratingBar;
    private TextView descripcion;
    private DatabaseReference databaseReferenceUsuario;
    private DatabaseReference databaseReferenceDatosLibro;
    private FirebaseDatabase database;
    private Adapter_Libro alibro;
    private FirebaseAuth mAuth;
    private Button btnVolver,btnVendido,btnModificar,btnEliminar
            ,aceptarEliminar,cancelEliminar,empezarModificar,cancelModificar,aceptarModificar;
    private  Usuario receptor;
    private LUsuario lreceptor;
    private Usuario emisor;
    private LUsuario lemisor;
    private LLibro lLibro;
    private Dialog dialog, confirmacionDialog;
    private MisLibrosActivity misLibrosActivity;
    private EditText editTextAuthor,editTextName,editTextPrice,editTextDescripcion;
    private TextView textViewExit;
    private Spinner spinnerContidion,spinnerCategory;
    public  ImagePicker imagePicker2;
    private Uri imageUri;
    private String urlImage;
    private int posCondition;
    private int posCategoria;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mislibros_click);

        dialog = new Dialog(this);
        dialog.setCanceledOnTouchOutside(false);


        confirmacionDialog = new Dialog(this);
        confirmacionDialog.setCanceledOnTouchOutside(false);



        misLibrosActivity=new MisLibrosActivity();

        foto_libro=findViewById(R.id.foto_libro_mislibrosclick);
        nombre=findViewById(R.id.nombre_mislibrosclick);
        autor=findViewById(R.id.autor_mislibrosclick);
        categoria=findViewById(R.id.holder_categoria_mislibrosclick);
        ISBN=findViewById(R.id.holder_Isbn_mislibrosclick);
        condition=findViewById(R.id.holder_condition_mislibrosclick);
        precio=findViewById(R.id.holder_precio_mislibrosclick);
        hora=findViewById(R.id.holder_fechacreacion_mislibrosclick);
        ratingBar=findViewById(R.id.ratingBar_libro_mislibrosclick);
        foto_libro_propietario =findViewById(R.id.foto_user_libro_mislibrosclick);
        nombre_libro_propietario=findViewById(R.id.nombre_user_mislibrosclick);
        btnVendido=findViewById(R.id.btns_vendido_mislibrosclick);
        btnVolver=findViewById(R.id.btn_volver_mislibrosclick);
        btnEliminar=findViewById(R.id.btn_eliminar_mislibrosclick);
        btnModificar=findViewById(R.id.btn_modificar_mislibrosclick);
        descripcion=findViewById(R.id.descripcion_mislibrosclick);


        btnVolver.setOnClickListener(new btnVolver());
        btnVendido.setOnClickListener(new btnVendido());
        btnEliminar.setOnClickListener(new btnEliminar());
        btnModificar.setOnClickListener(new btnModificar());

        lLibro = (LLibro) getIntent().getExtras().getSerializable("objectLibro");

        database=FirebaseDatabase.getInstance();
        mAuth=FirebaseAuth.getInstance();
        databaseReferenceUsuario =database.getReference("Usuarios/"+lLibro.getLibro().getUserKey());
        databaseReferenceDatosLibro=database.getReference(Constantes.NODO_LIBROS);
        mAuth=FirebaseAuth.getInstance();
        LoadLibros(lLibro);

        DatabaseReference reference=database.getReference(Constantes.NODO_USUARIOS).child(lLibro.getLibro().getUserKey());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                //leeremos un objeto de tipo Usuario
                GenericTypeIndicator<Usuario> u = new GenericTypeIndicator<Usuario>() {
                };
                receptor = snapshot.getValue(u);
                lreceptor=new LUsuario(receptor,snapshot.getKey());
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        DatabaseReference reference2=database.getReference(Constantes.NODO_USUARIOS).child(mAuth.getCurrentUser().getUid());
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                //leeremos un objeto de tipo Usuario
                GenericTypeIndicator<Usuario> u = new GenericTypeIndicator<Usuario>() {
                };
                emisor = snapshot.getValue(u);
                lemisor=new LUsuario(emisor,snapshot.getKey());
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        imagePicker2=new ImagePicker(this);

        imagePicker2.setImagePickerCallback(new ImagePickerCallback() {
            @Override
            public void onImagesChosen(List<ChosenImage> list) {
                if(!list.isEmpty()){
                    String path=list.get(0).getOriginalPath();
                    imageUri= Uri.parse(path);
                    if(imageUri!=null) {
                        LibroDAO.getInstance().cambiarFotoUri(imageUri,lLibro.getKey(), new UsuarioDAO.IDevolverUrlFoto() {
                            @Override
                            public void DevolverUrlFoto(String uri) {
                                Glide.with(getApplication().getApplicationContext())
                                        .load(uri)
                                        .into(imageViewdialog);
                                Toast.makeText(getApplicationContext(),"Foto subida para subir otra repite el proceso",Toast.LENGTH_SHORT).show();
                                urlImage=uri;
                            }
                        });
                    }

                }
            }

            @Override
            public void onError(String s) {
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void LoadLibros(LLibro libro){
        Picasso.with(getApplicationContext()).load(libro.getLibro().getFotoPrincipalUrl()).into(foto_libro);
        nombre.setText(libro.getLibro().getNombre());
        autor.setText("by "+libro.getLibro().getAutor());
        categoria.setText(libro.getLibro().getCategoria());
        ISBN.setText(libro.getLibro().getISBN());
        condition.setText(libro.getLibro().getCondition());
        precio.setText(String.valueOf(libro.getLibro().getPrecio()+"€"));
        descripcion.setText(libro.getLibro().getDescripcion());
        hora.setText(libro.obtenerFechaDeCreacionLibro());


        if(libro.getLibro().getCondition().equals("Nuevo")){
            ratingBar.setRating(5);
        }else if(libro.getLibro().getCondition().equals("Muy buen estado")){
            ratingBar.setRating(4);
        }
        else if(libro.getLibro().getCondition().equals("Buen estado")){
            ratingBar.setRating(3);
        }
        else if(libro.getLibro().getCondition().equals("Defecto estético")){
            ratingBar.setRating(2);
        }
        else if(libro.getLibro().getCondition().equals("Mala condición")){
            ratingBar.setRating(1);
        }
        else{
            ratingBar.setRating(0);
        }
        databaseReferenceUsuario.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                //leeremos un objeto de tipo Usuario
                GenericTypeIndicator<Usuario> u = new GenericTypeIndicator<Usuario>() {};
                Usuario usuario = snapshot.getValue(u);
                Picasso.with(getApplicationContext()).load(usuario.getFotoPerfilUrl()).into(foto_libro_propietario);
                nombre_libro_propietario.setText(usuario.getUserName());
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    class btnVolver implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            startActivity(new Intent(getApplicationContext(),MisLibrosActivity.class));
        }
    }
    class btnEliminar implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            //asignamos layout a pop up
            dialog.setContentView(R.layout.layout_dialog_eliminar_libro);

            aceptarEliminar=dialog.findViewById(R.id.btn_aceptar_eliminarlibro);
            cancelEliminar=dialog.findViewById(R.id.btn_cancelar_eliminarlibro);
            aceptarEliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    database.getReference(Constantes.NODO_LIBROS).child(lLibro.getKey()).removeValue();
                    dialog.dismiss();
                    startActivity(new Intent(getApplicationContext(),MisLibrosActivity.class));
                }
            });
            cancelEliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }
    class btnModificar implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            asignarDatosDialogModificar();

        }
    }

    private void asignarDatosDialogModificar() {

        dialog.setContentView(R.layout.activity_modificar);

        ArrayAdapter<CharSequence> adapterCategory = ArrayAdapter.createFromResource(
                getApplicationContext(),
                R.array.category,
                R.layout.support_simple_spinner_dropdown_item
        );
        ArrayAdapter<CharSequence> adapterCondition = ArrayAdapter.createFromResource(
                getApplicationContext(),
                R.array.conditions,
                R.layout.support_simple_spinner_dropdown_item
        );


        editTextAuthor = dialog.findViewById(R.id.editTextAuthor_modificar);
        editTextName = dialog.findViewById(R.id.editTextName_modificar);
        editTextPrice = dialog.findViewById(R.id.editTextPrice_modificar);
        textViewExit = dialog.findViewById(R.id.textViewExit_modificar);
        editTextDescripcion = dialog.findViewById(R.id.editTextTextMultiLineDesc_modificar);

        imageViewdialog = dialog.findViewById(R.id.imageViewImage_modificar);
        imageViewdialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePicker2.pickImage();
            }
        });

        empezarModificar=dialog.findViewById(R.id.buttonModificarLibro_modificar);
        spinnerContidion = dialog.findViewById(R.id.spinnerCondition_modificar);
        spinnerCategory = dialog.findViewById(R.id.spinnerCategory_modificar);

        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterCondition.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerCategory.setAdapter(adapterCategory);
        spinnerContidion.setAdapter(adapterCondition);

        spinnerContidion.setOnItemSelectedListener(new conditionCategory());
        spinnerCategory.setOnItemSelectedListener(new spinnerCategory());




        editTextAuthor.setText(lLibro.getLibro().getAutor());
        editTextName.setText(lLibro.getLibro().getNombre());
        editTextPrice.setText(lLibro.getLibro().getPrecio().toString());
        editTextDescripcion.setText(lLibro.getLibro().getDescripcion());
        if(lLibro.getLibro().getCondition().equals("Nuevo")){
            posCondition=0;
        }else if(lLibro.getLibro().getCondition().equals("Muy buen estado")){
            posCondition=1;
        }else if(lLibro.getLibro().getCondition().equals("Buen estado")){
            posCondition=2;
        }else if(lLibro.getLibro().getCondition().equals("Defecto estético")){
            posCondition=3;
        }else if(lLibro.getLibro().getCondition().equals("Mala condición")){
            posCondition=4;
        }

        if(lLibro.getLibro().getCategoria().equals("Primaría")){
            posCategoria=0;
        }else if(lLibro.getLibro().getCategoria().equals("Segundaría")){
            posCategoria=1;
        }else if(lLibro.getLibro().getCategoria().equals("Bachillerato")){
            posCategoria=2;
        }
        spinnerContidion.setSelection(posCondition);
        spinnerCategory.setSelection(posCategoria);

        Glide.with(getApplication().getApplicationContext())
                .load(lLibro.getLibro().getFotoPrincipalUrl())
                .into(imageViewdialog);


        textViewExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        empezarModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmacionDialog.setContentView(R.layout.layout_dialog_modificar_libro);
                aceptarModificar=confirmacionDialog.findViewById(R.id.btn_aceptar_modificarlibro);
                cancelModificar=confirmacionDialog.findViewById(R.id.btn_cancelar_modificarlibro);

                aceptarModificar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {

                            Map<String, Object> libroUpdate = new HashMap<>();
                            libroUpdate.put("autor", editTextAuthor.getText().toString());
                            libroUpdate.put("categoria", spinnerCategory.getSelectedItem().toString());
                            libroUpdate.put("condition", spinnerContidion.getSelectedItem().toString());
                            libroUpdate.put("descripcion", editTextDescripcion.getText().toString());
                            libroUpdate.put("fotoPrincipalUrl", urlImage);
                            libroUpdate.put("nombre", editTextName.getText().toString());
                            libroUpdate.put("precio", Double.parseDouble(editTextPrice.getText().toString()));
                            database.getReference(Constantes.NODO_LIBROS).child(lLibro.getKey()).updateChildren(libroUpdate);
                            Toast.makeText(getApplicationContext(), "Datosmodificados", Toast.LENGTH_SHORT).show();

                            dialog.dismiss();
                            confirmacionDialog.dismiss();

                            Libro libro=new Libro();
                            libro.setAutor(editTextAuthor.getText().toString());
                            libro.setNombre(editTextName.getText().toString());
                            libro.setISBN(lLibro.getLibro().getISBN());
                            libro.setDescripcion(editTextDescripcion.getText().toString());
                            if(!urlImage.equals("")){
                                libro.setFotoPrincipalUrl(urlImage);
                            }else{
                                libro.setFotoPrincipalUrl(lLibro.getLibro().getFotoPrincipalUrl());
                            }
                            libro.setCategoria(spinnerCategory.getSelectedItem().toString());
                            libro.setCondition(spinnerContidion.getSelectedItem().toString());

                            LLibro lLibro2=new LLibro(libro,lLibro.getKey());
                            Intent intent= new Intent(getApplicationContext(),MisLibrosClickActivity.class);
                            intent.putExtra("objectLibro",lLibro2);
                            startActivity(intent);

                        }catch(Exception e){
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
                cancelModificar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        confirmacionDialog.dismiss();
                    }
                });
               confirmacionDialog.show();
            }
        });
        dialog.show();
    }

    class btnVendido implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            dialog.setContentView(R.layout.layout_dialog_vender_libro);

            aceptarEliminar=dialog.findViewById(R.id.btn_aceptar_venderlibro);
            cancelEliminar=dialog.findViewById(R.id.btn_cancelar_venderlibro);
            aceptarEliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map<String, Object> hopperUpdates = new HashMap<>();
                    hopperUpdates.put("esVendido", "Si");
                    database.getReference(Constantes.NODO_LIBROS).child(lLibro.getKey()).updateChildren(hopperUpdates);
                    dialog.dismiss();
                    startActivity(new Intent(getApplicationContext(),MisLibrosVendidosActivity.class));
                }
            });
            cancelEliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
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

}
