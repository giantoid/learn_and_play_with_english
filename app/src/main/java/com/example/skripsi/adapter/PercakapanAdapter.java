package com.example.skripsi.adapter;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.skripsi.R;
import com.example.skripsi.models.MPercakapan;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PercakapanAdapter extends RecyclerView.Adapter<PercakapanAdapter.PercakapanViewHolder> implements Filterable {


    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the Percakapans in a list
    private List<MPercakapan> mPercakapanList;
    private List<MPercakapan> filterList;
    private PercakapanAdapterListener listener;

    //getting the context and Percakapan list with constructor
    public PercakapanAdapter(Context mCtx, List<MPercakapan> percakapanlist, PercakapanAdapterListener listener) {
        this.mCtx = mCtx;
        this.listener = listener;
        this.mPercakapanList = percakapanlist;
        this.filterList = percakapanlist;
    }

    @Override
    public PercakapanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.percakapan_layout, null);
        return new PercakapanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PercakapanViewHolder holder, final int position) {
        //getting the Percakapan of the specified position
        MPercakapan MPercakapan = filterList.get(position);

        //binding the data with the viewholder views
        holder.txtNama.setText(MPercakapan.getNama());
        holder.txtKalimat.setText(MPercakapan.getKalimat());

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
                    filterList = mPercakapanList;
                } else {
                    List<MPercakapan> filteredList = new ArrayList<>();
                    for (MPercakapan row : mPercakapanList) {

                        if (row.getNama().toLowerCase().contains(charString.toLowerCase()) || row.getKalimat().toLowerCase().contains(charString.toLowerCase())) {
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
                filterList = (ArrayList<MPercakapan>) filterResults.values;

                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };
    }


    class PercakapanViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtNama, txtKalimat;
//        ImageButton btnSpeaker;
        TextToSpeech TTS;

        public PercakapanViewHolder(View itemView) {
            super(itemView);

            txtNama = itemView.findViewById(R.id.txtNama);
            txtKalimat = itemView.findViewById(R.id.txtKalimat);
//            btnSpeaker = itemView.findViewById(R.id.btnSpeaker);

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

            txtKalimat.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition(); // gets item position
            if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                MPercakapan MPercakapan = listener.onPercakapanSelected(filterList.get(position));

//                Intent intent = new Intent(mCtx, DetailTravel.class);
                String itmId = String.valueOf(MPercakapan.getKalimat());
                Toast.makeText(mCtx,itmId,Toast.LENGTH_LONG).show();
//                intent.putExtra(EndPoint.ITM_ID, itmId);
//                mCtx.startActivity(intent);
                String toSpeak = MPercakapan.getKalimat();
                if (toSpeak.equals("")) {
                    Toast.makeText(mCtx, "Masukkan pesan", Toast.LENGTH_SHORT).show();

                } else {
                    TTS.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);

                }
            }
        }
    }

    public interface PercakapanAdapterListener {
        MPercakapan onPercakapanSelected(MPercakapan mPercakapan);
    }
}
