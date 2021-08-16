package com.example.skripsi.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.skripsi.KamusDetail;
import com.example.skripsi.R;
import com.example.skripsi.models.MKamusIsi;

import java.util.ArrayList;
import java.util.List;

public class KamusIsiAdapter extends RecyclerView.Adapter<KamusIsiAdapter.KamusIsiViewHolder> implements Filterable {


    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the KamusIsis in a list
    private List<MKamusIsi> mKamusIsiList;
    private List<MKamusIsi> filterList;
    private KamusIsiAdapterListener listener;

    //getting the context and KamusIsi list with constructor
    public KamusIsiAdapter(Context mCtx, List<MKamusIsi> KamusIsilist, KamusIsiAdapterListener listener) {
        this.mCtx = mCtx;
        this.listener = listener;
        this.mKamusIsiList = KamusIsilist;
        this.filterList = KamusIsilist;
    }

    @Override
    public KamusIsiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.kamus_isi_layout, null);
        return new KamusIsiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(KamusIsiViewHolder holder, final int position) {
        //getting the KamusIsi of the specified position
        MKamusIsi MKamusIsi = filterList.get(position);

        //binding the data with the viewholder views
        holder.txtEnglish.setText(MKamusIsi.getEnglish());

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
                    filterList = mKamusIsiList;
                } else {
                    List<MKamusIsi> filteredList = new ArrayList<>();
                    for (MKamusIsi row : mKamusIsiList) {

                        if (row.getIdKamus().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getEnglish().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getIndonesia().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getIdKategori().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getGambar().toLowerCase().contains(charString.toLowerCase())) {
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
                filterList = (ArrayList<MKamusIsi>) filterResults.values;

                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };
    }


    class KamusIsiViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

       TextView txtEnglish;

        public KamusIsiViewHolder(View itemView) {
            super(itemView);

            txtEnglish = itemView.findViewById(R.id.txtEnglish);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition(); // gets item position
            if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                MKamusIsi MKamusIsi = listener.onKamusIsiSelected(filterList.get(position));

                Intent intent = new Intent(mCtx, KamusDetail.class);
                String itmId = MKamusIsi.getIdKamus();
                intent.putExtra("id", itmId);
                mCtx.startActivity(intent);
            }
        }
    }

    public interface KamusIsiAdapterListener {
        MKamusIsi onKamusIsiSelected(MKamusIsi mKamusIsi);
    }
}
