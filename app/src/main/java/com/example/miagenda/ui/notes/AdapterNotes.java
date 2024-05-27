package com.example.miagenda.ui.notes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miagenda.R;
import com.example.miagenda.SessionManager;
import com.example.miagenda.api.Nota;
import com.example.miagenda.api.retrofit.PerfilAPI;
import com.example.miagenda.api.retrofit.RetrofitCliente;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterNotes extends RecyclerView.Adapter<AdapterNotes.MyViewHolder> {
    private final List<Nota> notas;

    AdapterNotes(List<Nota> notas) {
        this.notas = notas;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Nota nota = notas.get(position);
        holder.messageText.setText(nota.getNoteDesc());
        holder.noteId = nota.getId();  // Asegúrate de que el ID se asigna al holder
    }

    @Override
    public int getItemCount() {
        return notas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        CardView notaCard;
        String noteId;  // Añadir el campo para el ID de la nota

        MyViewHolder(View view) {
            super(view);
            messageText = view.findViewById(R.id.note_text);
            notaCard = view.findViewById(R.id.notaCV);
            notaCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(v.getContext())
                            .setTitle("Borrar Nota")
                            .setMessage("¿Estás seguro de que quieres borrar esta nota?")
                            .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    int position = getAdapterPosition();
                                    if (position != RecyclerView.NO_POSITION) {
                                        // Obtener el ID de la nota
                                        String noteId = notas.get(position).getId();
                                        // Llamar al método para borrar la nota en la base de datos
                                        deleteNoteFromDatabase(noteId, position);
                                    }
                                }
                            })
                            .setNegativeButton("No", null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            });
        }

        private void deleteNoteFromDatabase(String noteId, int position) {
            // Obtener el nombre de usuario
            SessionManager sessionManager = new SessionManager(itemView.getContext());
            String username = sessionManager.getUsername();

            // Verificar que el ID de la nota no sea null o vacío
            if (noteId == null || noteId.isEmpty()) {
                Toast.makeText(itemView.getContext(), "ID de la nota no encontrado", Toast.LENGTH_SHORT).show();
                return;
            }

            // Llamar a la API para eliminar la nota
            PerfilAPI notesAPI = RetrofitCliente.getInstance().create(PerfilAPI.class);
            Call<Void> call = notesAPI.deleteNote(username, noteId);

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        // La nota se eliminó correctamente
                        notas.remove(position);
                        notifyItemRemoved(position);
                        Toast.makeText(itemView.getContext(), "Nota eliminada correctamente", Toast.LENGTH_SHORT).show();
                    } else {
                        // Manejar el fallo en la eliminación de la nota
                        Toast.makeText(itemView.getContext(), "Error al eliminar la nota: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    // Manejar fallos de red
                    Toast.makeText(itemView.getContext(), "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
