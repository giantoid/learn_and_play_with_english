package com.example.skripsi.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skripsi.R;
import com.example.skripsi.TebakGambar;
import com.example.skripsi.core.SharedPrefManager;
import com.example.skripsi.models.MTebakGambar;

import java.util.List;

public class TebakGambarAdapter extends RecyclerView.Adapter<TebakGambarAdapter.TebakGambarViewHolder> {
    private Context mCtx;

    private List<MTebakGambar> mTebakGambarList;

    String level = SharedPrefManager.getInstance(mCtx).getLevel();

    public TebakGambarAdapter(Context mCtx, List<MTebakGambar> mTebakGambarList) {
        this.mCtx = mCtx;
        this.mTebakGambarList = mTebakGambarList;
    }

    //INITIALIZE VH
    @Override
    public TebakGambarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.tebak_gambar_layout, null);
        return new TebakGambarAdapter.TebakGambarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TebakGambarViewHolder holder, int position) {
        MTebakGambar mTebakGambar = mTebakGambarList.get(position);
        holder.txtId.setText(String.valueOf(position+1));
        if (Integer.parseInt(level) >= position) {
            holder.imgLevel.setImageResource(R.drawable.ic_baseline_lock_open_24);
        }
        Log.d("d","level = "+level+", position = "+String.valueOf(position));
    }

    //TOTAL NUM
    @Override
    public int getItemCount() {
        return mTebakGambarList.size();
    }

    class TebakGambarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView btLevel;
        TextView txtId;
        ImageView imgLevel;

        public TebakGambarViewHolder(View itemView) {
            super(itemView);
            txtId = itemView.findViewById(R.id.txtId);
            btLevel = itemView.findViewById(R.id.btLevel);
            imgLevel = itemView.findViewById(R.id.imgLevel);

//            if (level.equals(String.valueOf(position))) {
//                imgLevel.setImageResource(R.drawable.ic_baseline_lock_open_24);
//            }

            btLevel.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition(); // gets item position
            if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it

                if (Integer.parseInt(level) >= position) {
                    Intent intent = new Intent(mCtx, TebakGambar.class);
                    intent.putExtra("id", String.valueOf(position));
                    mCtx.startActivity(intent);
                } else {
                    Toast.makeText(mCtx,"Selesaikan dulu soal no. "+String.valueOf(Integer.parseInt(level)+1),Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}

