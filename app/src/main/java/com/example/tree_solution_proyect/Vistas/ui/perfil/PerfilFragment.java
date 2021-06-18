package com.example.tree_solution_proyect.Vistas.ui.perfil;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.tree_solution_proyect.Objetos.Firebase.Usuario;
import com.example.tree_solution_proyect.Persistencia.UsuarioDAO;
import com.example.tree_solution_proyect.R;
import com.example.tree_solution_proyect.Vistas.Login;
import com.google.firebase.auth.FirebaseAuth;
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
    private Dialog dialogResetpass;
    private Dialog myDialogResetPass;
    private ImagePicker imagePicker;
    private Uri fotoUriPerfil;
    private TextView editTextTextPassword,editTextTextPassword2,textViewAccept,textViewProducts
            ,textViewMisProductos;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View vista=inflater.inflate(R.layout.fragment_perfil, container, false);

        textViewResetPass = vista.findViewById(R.id.textViewResetPass);
        textViewProducts = vista.findViewById(R.id.textViewProducts);

        textViewProducts.setOnClickListener(new textViewMisProductos());

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

                //leeremos un objeto de tipo Estudiante
                GenericTypeIndicator<Usuario> usuario = new GenericTypeIndicator<Usuario>() {
                };
                Usuario usuario1 = dataSnapshot.getValue(usuario);
                Uri uri=Uri.parse(usuario1.getFotoPerfilUrl());
                if(uri!=null) {
                    try {
                        Picasso.with(getActivity().getApplicationContext())
                                .load(uri.toString()).resize(50,50)
                                .into(FotoCambioPerfil);
                        userName.setText(usuario1.getUserName());
                    } catch (Exception e) {
                        Toast.makeText(getActivity().getApplicationContext(),
                                e.getMessage(), Toast.LENGTH_SHORT).show();
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

    class textViewResetPass implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            myDialogResetPass.setContentView(R.layout.activity_resetpass);

            textViewProducts = myDialogResetPass.findViewById(R.id.textViewExitPass);
            editTextTextPassword = myDialogResetPass.findViewById(R.id.editTextTextPassword);
            editTextTextPassword2 = myDialogResetPass.findViewById(R.id.editTextTextPassword2);
            textViewAccept = myDialogResetPass.findViewById(R.id.textViewAccept);

            myDialogResetPass.show();

            textViewProducts.setOnClickListener(new resetPassExit());

        }

        class resetPassExit implements View.OnClickListener {

            @Override
            public void onClick(View v) {
                myDialogResetPass.dismiss();
            }
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

    class textViewMisProductos implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent i = new Intent(getActivity(), MisLibrosActivity.class);
            startActivity(i);
        }
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
