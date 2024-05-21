package com.example.miagenda.ui.events;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import com.example.miagenda.R;
import com.example.miagenda.api.Evento;

public class MyEventFragment extends Fragment {

    public MyEventFragment() {
        // Required empty public constructor
    }

    public static MyEventFragment newInstance(String param1, String param2) {
        MyEventFragment fragment = new MyEventFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_event, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            Evento evento = (Evento) bundle.getSerializable("evento");
            if (evento != null) {
                TextView nombreEventoTextView = view.findViewById(R.id.nombreMiEventoTextView);
                TextView descripcionEventoTextView = view.findViewById(R.id.descripcionMiEventoTextView);
                TextView fechaEventoTextView = view.findViewById(R.id.fechaMiEventoTextView);
                TextView avisoEventoTextView = view.findViewById(R.id.avisoMiEventoTextView);

                nombreEventoTextView.setText(evento.getName_event());
                descripcionEventoTextView.setText(evento.getEvent_desc());
                fechaEventoTextView.setText(evento.getEvent_date());
            }
        }

        Button editarEvento = view.findViewById(R.id.miEventoButton);
        editarEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.editEvent);
            }
        });

        ImageButton botonAtras = view.findViewById(R.id.boton_atras);
        botonAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });

        return view;
    }
}
