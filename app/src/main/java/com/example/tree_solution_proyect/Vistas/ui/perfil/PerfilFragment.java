package com.example.tree_solution_proyect.Vistas.ui.perfil;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.tree_solution_proyect.Adaptadores.Adapter_Libro;
import com.example.tree_solution_proyect.Objetos.Constantes;
import com.example.tree_solution_proyect.Objetos.Firebase.Libro;
import com.example.tree_solution_proyect.Objetos.Firebase.Usuario;
import com.example.tree_solution_proyect.Objetos.Logica.LLibro;
import com.example.tree_solution_proyect.Persistencia.UsuarioDAO;
import com.example.tree_solution_proyect.R;
import com.example.tree_solution_proyect.Vistas.Login;
import com.example.tree_solution_proyect.Vistas.MainActivity;
import com.example.tree_solution_proyect.Vistas.MisLibrosActivity;
import com.example.tree_solution_proyect.Vistas.MisLibrosVendidosActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class PerfilFragment extends Fragment {
    private TextView salir,userName,textViewResetPass;
    private ImageView FotoCambioPerfil;
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReferenceUsuario;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Dialog myDialog, myDialogResetPass;
    private Adapter_Libro alibro;
    private ImagePicker imagePicker;
    private Uri fotoUriPerfil;
    private Button btndarBajaAceptar,btndarBajaCancelar;
    private TextView editTextTextPassword,editTextTextPassword2,editTextContraseñaActual,textViewAccept,textViewMisLibros
            ,textViewLibrosVendidos,textViewExit,textViewBaja;
    private String key;
    private AuthCredential credential;
    ArrayList<LLibro> lLibros=new ArrayList<LLibro>();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View vista=inflater.inflate(R.layout.fragment_perfil, container, false);

        textViewResetPass = vista.findViewById(R.id.textViewResetPass);
        textViewMisLibros = vista.findViewById(R.id.textViewMisLibros);
        textViewLibrosVendidos = vista.findViewById(R.id.textViewLibrosVendidos);
        textViewBaja = vista.findViewById(R.id.textViewBaja);

        textViewMisLibros.setOnClickListener(new textViewMisLibros());
        textViewLibrosVendidos.setOnClickListener(new textViewLibrosVendidos());
        textViewBaja.setOnClickListener(new textViewBaja());

        myDialog = new Dialog(getActivity());
        myDialogResetPass = new Dialog(getActivity());

        //reset pass action
        textViewResetPass.setOnClickListener(new textViewResetPass());

        database=FirebaseDatabase.getInstance();
        mAuth=FirebaseAuth.getInstance();
        storage= FirebaseStorage.getInstance();;
        userName=vista.findViewById(R.id.NameLibroChat);

        imagePicker=new ImagePicker(this);

        imagePicker.setImagePickerCallback(new ImagePickerCallback() {
            @Override
            public void onImagesChosen(List<ChosenImage> list) {
                if(!list.isEmpty()){

                    String path=list.get(0).getOriginalPath();
                    fotoUriPerfil= Uri.parse(path);
                    if(fotoUriPerfil!=null) {
                        UsuarioDAO.getInstance().cambiarFotoUri(fotoUriPerfil,
                                uri -> FirebaseDatabase.getInstance()
                                        .getReference().child("Usuarios")
                                        .child(mAuth.getCurrentUser().getUid())
                                        .child("fotoPerfilUrl").setValue(uri));
                    }

                }
            }

            @Override
            public void onError(String s) {

            }
        });
        FotoCambioPerfil=vista.findViewById(R.id.FotoPerfil);
        FotoCambioPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagePicker.pickImage();
            }
        });

        databaseReferenceUsuario = database.getReference("Usuarios/"+mAuth
                .getCurrentUser().getUid());
        databaseReferenceUsuario.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //leeremos un objeto de tipo Usuario
                GenericTypeIndicator<Usuario> usuario = new GenericTypeIndicator<Usuario>() {
                };
                if (dataSnapshot.exists()) {
                    Usuario usuario1 = dataSnapshot.getValue(usuario);
                    Uri uri = Uri.parse(usuario1.getFotoPerfilUrl());
                    if (uri != null) {
                        try {

                            Picasso.with(getActivity().getApplicationContext())
                                    .load(uri.toString()).resize(50, 50)
                                    .into(FotoCambioPerfil);

                            userName.setText(usuario1.getUserName());
                        } catch (Exception e) {
                            Toast.makeText(getActivity().getApplicationContext(),
                                    e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });

        salir=vista.findViewById(R.id.textViewLogOut);
        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), Login.class);
                startActivity(intent);
                try {
                    this.finalize();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        });


        return vista;

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentuser=mAuth.getCurrentUser();

        if(currentuser!=null){

        }else{
            startActivity(new Intent(getActivity().getApplicationContext(), Login.class));
            try {
                finalize();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }

    class textViewResetPass implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            myDialogResetPass.setContentView(R.layout.activity_resetpass);

            textViewExit = myDialogResetPass.findViewById(R.id.textViewExitPass);
            editTextTextPassword = myDialogResetPass.findViewById(R.id.editTextTextPassword);
            editTextTextPassword2 = myDialogResetPass.findViewById(R.id.editTextTextPassword2);
            editTextContraseñaActual=myDialogResetPass.findViewById(R.id.editTextContraseñaActual);
            textViewAccept = myDialogResetPass.findViewById(R.id.textViewAccept);

            myDialogResetPass.show();

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            textViewExit.setOnClickListener(new resetPassExit());
            textViewAccept.setOnClickListener(v1 -> {
                FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
                if((!editTextTextPassword.getText().toString().isEmpty())&&(!editTextTextPassword2.getText().toString().isEmpty())&&(!editTextContraseñaActual.getText().toString().isEmpty())){
                    if(editTextTextPassword.getText().toString().equals(editTextTextPassword2.getText().toString())){
                        if(validContracena()) {


                            AuthCredential credential = EmailAuthProvider
                                    .getCredential(user1.getEmail(), editTextContraseñaActual.getText().toString());
                            user1.reauthenticate(credential).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    if(editTextTextPassword.getText().toString().equals(editTextContraseñaActual.getText().toString())) {
                                        user1.updatePassword(editTextTextPassword.getText().toString())
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(getActivity().getApplicationContext(), "Contraseña cambiada con exito", Toast.LENGTH_SHORT).show();
                                                            myDialogResetPass.dismiss();
                                                        }
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull @NotNull Exception e) {
                                                Toast.makeText(getActivity().getApplicationContext(), "Ups, algo ha ido mal " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    } else {
                                        Toast.makeText(getActivity().getApplicationContext(), "Contraseña nueva no debe coincidir con la antigua", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull @NotNull Exception e) {
                                    Toast.makeText(getActivity().getApplicationContext(), "Сontraseña actual incorrecta", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else{
                            Toast.makeText(getActivity().getApplicationContext(),"Contraseña tiene que tener minimo 6 y maximo 16 caracteres",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getActivity().getApplicationContext(),"Las contraseñas no coinciden",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getActivity().getApplicationContext(),"Los campos no pueden estar vacios",Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    public boolean validContracena(){
        String contrasena,contrasenarepetida;
        contrasena=editTextTextPassword.getText().toString();
        contrasenarepetida=editTextTextPassword.getText().toString();

        if(contrasena.equals(contrasenarepetida)){
            if(contrasena.length()>=6 && contrasena.length()<=16){
                return true;
            }
            else{
                return false;
            }
        }else{

            return  false;
        }
    }

    class resetPassExit implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            myDialogResetPass.dismiss();
        }
    }


    class textViewLibrosVendidos implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent i = new Intent(getActivity(), MisLibrosVendidosActivity.class);
            startActivity(i);
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

    class textViewMisLibros implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent i = new Intent(getActivity(), MisLibrosActivity.class);
            startActivity(i);
        }
    }

    class textViewBaja implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            myDialog.setContentView(R.layout.layout_dialog_dar_baja);

            btndarBajaAceptar=myDialog.findViewById(R.id.btn_aceptar_darbaja);
            btndarBajaCancelar=myDialog.findViewById(R.id.btn_cancelar_darbaja);
            btndarBajaAceptar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if(Login.getTokenID()==null) {

                        myDialogResetPass.setContentView(R.layout.activity_darbaja);
                        textViewExit = myDialogResetPass.findViewById(R.id.textViewExitPass_darbaja);
                        textViewAccept = myDialogResetPass.findViewById(R.id.textViewAccept_darbaja);

                        editTextTextPassword = myDialogResetPass.findViewById(R.id.editTextContraseñaActual_darbaja);
                        editTextTextPassword2 = myDialogResetPass.findViewById(R.id.editTextContraseñaActual2_darbaja);

                        myDialogResetPass.show();
                        textViewExit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                myDialogResetPass.dismiss();
                            }
                        });

                        textViewAccept.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if((!editTextTextPassword.getText().toString().isEmpty())&&(!editTextTextPassword2.getText().toString().isEmpty())){
                                    if(editTextTextPassword.getText().toString().equals(editTextTextPassword2.getText().toString())) {
                                        if (validContracena()) {
                                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                            credential = EmailAuthProvider
                                                    .getCredential(mAuth.getCurrentUser().getEmail(), editTextTextPassword.getText().toString());

                                            user.reauthenticate(credential).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    key=FirebaseAuth.getInstance().getCurrentUser().getUid();

                                                    FirebaseAuth.getInstance().signOut();
                                                    user.delete()
                                                            .addOnCompleteListener(task -> {
                                                                if (task.isSuccessful()) {
                                                                    startActivity(new Intent(getActivity().getApplicationContext(), MainActivity.class));
                                                                    try {
                                                                        DatabaseReference databaseReference = database.getReference(Constantes.NODO_LIBROS);
                                                                        databaseReference.addValueEventListener(new ValueEventListener() {
                                                                            @Override
                                                                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                                                                for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                                                                                    final Libro m = snapshot1.getValue(Libro.class);
                                                                                    final LLibro lLibro = new LLibro(m, snapshot1.getKey());
                                                                                    lLibros.add(lLibro);

                                                                                    if (lLibro.getLibro().getUserKey().equals(key)) {
                                                                                        database.getReference(Constantes.NODO_LIBROS).child(lLibro.getKey()).removeValue();
                                                                                        borrarDatosFavoritos(lLibro);
                                                                                    }
                                                                                }

                                                                            }
                                                                            @Override
                                                                            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                                                                                Toast.makeText(getActivity().getApplicationContext(), "Operacion cancelada", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        });

                                                                        database.getReference(Constantes.NODO_USUARIOS).child(key).removeValue();
                                                                        borrarDatosChats();
                                                                        Toast.makeText(getActivity().getApplicationContext(), "Usuario se ha borrado correctamente", Toast.LENGTH_SHORT).show();

                                                                        Toast.makeText(getActivity().getApplicationContext(), "Muchas gracias", Toast.LENGTH_LONG).show();
                                                                    } catch (Exception e) {
                                                                        Toast.makeText(getActivity().getApplicationContext(), e.getMessage() + "", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                                else{
                                                                    Toast.makeText(getActivity().getApplicationContext(), "Usuario no se ha borrado correctamente", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull @NotNull Exception e) {
                                                            Toast.makeText(getActivity().getApplicationContext(), "Usuario no se ha borrado correctamente", Toast.LENGTH_SHORT).show();
                                                        }

                                                    });
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull @NotNull Exception e) {
                                                    Toast.makeText(getActivity().getApplicationContext(), "Contraseña no es válida", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                            myDialog.dismiss();
                                        }else{
                                            Toast.makeText(getActivity().getApplicationContext(),"Contraseña tiene que tener minimo 6 y maximo 16 caracteres",Toast.LENGTH_SHORT).show();
                                        }
                                    }else{
                                        Toast.makeText(getActivity().getApplicationContext(),"Las contraseñas no coinciden",Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    Toast.makeText(getActivity().getApplicationContext(),"Los campos no pueden estar vacios",Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    }else{
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        credential = GoogleAuthProvider.getCredential(Login.getTokenID(), null);
                        user.reauthenticate(credential).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                key=FirebaseAuth.getInstance().getCurrentUser().getUid();

                                FirebaseAuth.getInstance().signOut();
                                user.delete()
                                        .addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {
                                                startActivity(new Intent(getActivity().getApplicationContext(), MainActivity.class));
                                                try {
                                                    DatabaseReference databaseReference = database.getReference(Constantes.NODO_LIBROS);
                                                    databaseReference.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                                            for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                                                                final Libro m = snapshot1.getValue(Libro.class);
                                                                final LLibro lLibro = new LLibro(m, snapshot1.getKey());

                                                                if (lLibro.getLibro().getUserKey().equals(key)) {
                                                                    database.getReference(Constantes.NODO_LIBROS).child(lLibro.getKey()).removeValue();
                                                                }
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull @NotNull DatabaseError error) {
                                                            Toast.makeText(getActivity().getApplicationContext(), "Operacion cancelada", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                    database.getReference(Constantes.NODO_USUARIOS).child(key).removeValue();
                                                    Toast.makeText(getActivity().getApplicationContext(), "Usuario se ha borrado correctamente", Toast.LENGTH_SHORT).show();

                                                    Toast.makeText(getActivity().getApplicationContext(), "Muchas gracias", Toast.LENGTH_LONG).show();
                                                } catch (Exception e) {
                                                    Toast.makeText(getActivity().getApplicationContext(), e.getMessage() + "", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                            else{
                                                Toast.makeText(getActivity().getApplicationContext(), "Usuario no se ha borrado correctamente", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull @NotNull Exception e) {
                                        Toast.makeText(getActivity().getApplicationContext(), "Usuario no se ha borrado correctamente", Toast.LENGTH_SHORT).show();
                                    }

                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                Toast.makeText(getActivity().getApplicationContext(), "Contraseña no es válida", Toast.LENGTH_SHORT).show();
                            }
                        });
                        myDialog.dismiss();
                    }
                }
            });


            btndarBajaCancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myDialog.dismiss();
                }
            });
            myDialog.show();
        }
    }
    public void borrarDatosChats() {
        DatabaseReference databaseReferenceChatDatos=database.getReference(Constantes.NODO_CHAT_DATOS);
        databaseReferenceChatDatos.addValueEventListener(new ValueEventListener() {
            String keyEmisor;
            String keyreceptor;
            String keyLibro;
            DataSnapshot snapshot1;
            DataSnapshot snapshot2;
            DataSnapshot snapshot3;

            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    this.snapshot1=snapshot1;
                    keyEmisor=snapshot1.getKey();
                    for(DataSnapshot snapshot2:this.snapshot1.getChildren()) {
                        this.snapshot2 = snapshot2;
                        keyreceptor = snapshot2.getKey();
                        for (DataSnapshot snapshot3 : snapshot2.getChildren()) {
                            this.snapshot3 = snapshot3;
                            keyLibro = snapshot3.getKey();

                            if((keyreceptor.equals(key))||((keyEmisor.equals(key)))) {
                                database.getReference(Constantes.NODO_CHATS).child(keyEmisor).child(keyreceptor).child(keyLibro).removeValue();
                                database.getReference(Constantes.NODO_CHAT_DATOS).child(keyEmisor).child(keyreceptor).child(keyLibro).removeValue();
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
    public void borrarDatosFavoritos(LLibro lLibro){
        DatabaseReference reference= database.getReference(Constantes.NODO_LIB_FAV);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                database.getReference(Constantes.NODO_LIB_FAV).child(snapshot.getKey()).child(lLibro.getKey());
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            this.finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
