package com.example.miagenda.ui.events;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import com.example.miagenda.R;
import com.example.miagenda.api.Evento;
import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private List<Evento> events = new ArrayList<>();

    public void setEvents(List<Evento> events) {
        this.events = events;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_card, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Evento event = events.get(position);
        holder.bind(event);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    class EventViewHolder extends RecyclerView.ViewHolder {

        TextView nombreEvento;
        TextView fechaYhoraEvento;
        CardView cardView;
        Button deleteButton;

        EventViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreEvento = itemView.findViewById(R.id.nombreEvento);
            fechaYhoraEvento = itemView.findViewById(R.id.fechaYhoraEvento);
            cardView = itemView.findViewById(R.id.eventoCV);
            deleteButton = itemView.findViewById(R.id.deleteEvento);
        }

        void bind(Evento event) {
            nombreEvento.setText(event.getName_event());
            fechaYhoraEvento.setText(event.getEvent_date()); // Formatear la fecha si es necesario

            deleteButton.setOnClickListener(v -> {
                // Lógica para eliminar el evento
                // removeItem(getAdapterPosition()); // Este método debe ser implementado
            });

            cardView.setOnClickListener(v -> {
                NavController navController = Navigation.findNavController(v);
                Bundle bundle = new Bundle();
                bundle.putSerializable("evento", event);
                navController.navigate(R.id.myEvent, bundle);
            });
        }
    }
}
