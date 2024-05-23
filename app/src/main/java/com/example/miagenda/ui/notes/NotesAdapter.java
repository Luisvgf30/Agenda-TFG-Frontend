package com.example.miagenda.ui.notes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miagenda.R;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.MyViewHolder> {
    private static List<String> messages = null;

    NotesAdapter(List<String> messages) {
        NotesAdapter.messages = messages;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String message = messages.get(position);
        holder.messageText.setText(message);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        CardView nota;

        MyViewHolder(View view) {
            super(view);
            messageText = view.findViewById(R.id.note_text);
            nota = view.findViewById(R.id.notaCV);
            nota.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(v.getContext())
                            .setTitle("Borrar Nota")
                            .setMessage("¿Estás seguro de que quieres borrar esta nota?")
                            .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    int position = getAdapterPosition();
                                    if (position != RecyclerView.NO_POSITION) {
                                        // Borrar la nota de tus datos
                                        messages.remove(position);
                                        // Notificar al adapter del cambio
                                        notifyItemRemoved(position);
                                    }
                                }
                            })
                            .setNegativeButton("No", null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            });
        }
    }
}
