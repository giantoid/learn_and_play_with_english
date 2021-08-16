package com.example.skripsi.adapter;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.skripsi.R;
import com.example.skripsi.models.MAlfabet;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AlfabetAdapter extends RecyclerView.Adapter<AlfabetAdapter.AlfabetViewHolder> implements Filterable {

    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the Alfabets in a list
    private List<MAlfabet> mAlfabetList;
    private List<MAlfabet> filterList;
    private AlfabetAdapter.AlfabetAdapterListener listener;

    //getting the context and Alfabet list with constructor
    public AlfabetAdapter(Context mCtx, List<MAlfabet> Alfabetlist, AlfabetAdapter.AlfabetAdapterListener listener) {
        this.mCtx = mCtx;
        this.listener = listener;
        this.mAlfabetList = Alfabetlist;
        this.filterList = Alfabetlist;
    }

    @Override
    public AlfabetAdapter.AlfabetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.alfabet_layout, null);
        return new AlfabetAdapter.AlfabetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AlfabetAdapter.AlfabetViewHolder holder, final int position) {
        //getting the Alfabet of the specified position
        MAlfabet MAlfabet = filterList.get(position);

        //binding the data with the viewholder views
        holder.txtAlfabet.setText(MAlfabet.getAlfabet());

    }


    @Override
    public int getItemCount() {
        return filterList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filterList = mAlfabetList;
                } else {
                    List<MAlfabet> filteredList = new ArrayList<>();
                    for (MAlfabet row : mAlfabetList) {

                        if (row.getId().toLowerCase().contains(charString.toLowerCase()) || row.getAlfabet().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    filterList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filterList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filterList = (ArrayList<MAlfabet>) filterResults.values;

                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };
    }


    class AlfabetViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtAlfabet;
        ImageButton btnSpeaker;
        TextToSpeech TTS;

        public AlfabetViewHolder(View itemView) {
            super(itemView);

            txtAlfabet = itemView.findViewById(R.id.txtAlfabet);
            btnSpeaker = itemView.findViewById(R.id.btnSpeaker);

            TTS = new TextToSpeech(mCtx, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status != TextToSpeech.ERROR) {
                    TTS.setLanguage(Locale.US);
//                        TTS.setLanguage(new Locale("in_ID"));

                    } else {
                        Toast.makeText(mCtx, "Error", Toast.LENGTH_SHORT).show();

                    }
                }
            });

            btnSpeaker.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition(); // gets item position
            if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                MAlfabet MAlfabet = listener.onAlfabetSelected(filterList.get(position));

                String toSpeak = txtAlfabet.getText().toString().trim();
                TTS.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            }
        }
    }

    public interface AlfabetAdapterListener {
        MAlfabet onAlfabetSelected(MAlfabet mAlfabet);
    }
}
