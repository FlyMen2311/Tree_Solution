package com.example.tree_solution_proyect.Vistas.ui.perfil;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.tree_solution_proyect.Objetos.Constantes;
import com.example.tree_solution_proyect.Objetos.Firebase.Usuario;
import com.example.tree_solution_proyect.Persistencia.UsuarioDAO;
import com.example.tree_solution_proyect.R;
import com.example.tree_solution_proyect.Vistas.Login;
import com.example.tree_solution_proyect.Vistas.ui.comunicacion.ComunicacionFragment;
import com.example.tree_solution_proyect.databinding.FragmentPerfilBinding;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.Executor;

import static android.app.Activity.RESULT_OK;


public class PerfilFragment extends Fragment {
    private Button salir;
    private TextView userName;
    private ImageView FotoCambioPerfil;
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReferenceUsuario;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private ImagePicker imagePicker;
    private Uri fotoUriPerfil;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View vista=inflater.inflate(R.layout.fragment_perfil, container, false);
        database=FirebaseDatabase.getInstance();
        mAuth=FirebaseAuth.getInstance();
        storage= FirebaseStorage.getInstance();;
        userName=vista.findViewById(R.id.UserNamePerfil);

        imagePicker=new ImagePicker(this);

        imagePicker.setImagePickerCallback(new ImagePickerCallback() {
            @Override
            public void onImagesChosen(List<ChosenImage> list) {
                if(!list.isEmpty()){

                    String path=list.get(0).getOriginalPath();
                    fotoUriPerfil= Uri.parse(path);
                    if(fotoUriPerfil!=null) {
                        UsuarioDAO.getInstance().cambiarFotoUri(fotoUriPerfil, new UsuarioDAO.IDevolverUrlFoto() {
                            @Override
                            public void DevolverUrlFoto(String uri) {
                                FirebaseDatabase.getInstance().getReference().child("Usuarios").child(mAuth.getCurrentUser().getUid()).child("fotoPerfilUrl").setValue(uri);
                            }
                        });
                    }

                }
            }

            @Override
            public void onError(String s) {

            }
        });
        FotoCambioPerfil=vista.findViewById(R.id.FotoCambiarPerfil);
        FotoCambioPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagePicker.pickImage();
            }
        });

        databaseReferenceUsuario = database.getReference("Usuarios/"+mAuth.getCurrentUser().getUid());
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
                        Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });

        salir=vista.findViewById(R.id.button);
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

    //Recoger datos de Intent producido en esta clase
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Picker.PICK_IMAGE_DEVICE && resultCode == RESULT_OK){
            imagePicker.submit(data);
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
