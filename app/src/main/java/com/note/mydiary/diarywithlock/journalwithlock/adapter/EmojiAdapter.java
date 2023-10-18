package com.note.mydiary.diarywithlock.journalwithlock.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.note.mydiary.diarywithlock.journalwithlock.callback.ICallBackItem;
import com.note.mydiary.diarywithlock.journalwithlock.model.EmojiModel;

import java.util.ArrayList;

public class EmojiAdapter extends RecyclerView.Adapter<EmojiAdapter.EmojiHolder> {

    private Context context;
    private ArrayList<EmojiModel> lstEmoji;
    private ICallBackItem callBack;

    public EmojiAdapter(Context context, ICallBackItem callBack) {
        this.context = context;
        this.callBack = callBack;
    }

    public void setData(ArrayList<EmojiModel> lstEmoji) {
        this.lstEmoji = lstEmoji;
        notifyChange();
    }

    @NonNull
    @Override
    public EmojiHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int w = context.getResources().getDisplayMetrics().widthPixels;
        ImageView ivEmoji = new ImageView(context);
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams((int) (13.89f * w / 100), (int) (13.89f * w / 100));
        params.setMargins((int) (4.167f * w / 100), (int) (4.167f * w / 100), (int) (4.167f * w / 100), (int) (4.167f * w / 100));
        ivEmoji.setLayoutParams(params);
        return new EmojiHolder(ivEmoji);
    }

    @Override
    public void onBindViewHolder(@NonNull EmojiHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        if (!lstEmoji.isEmpty()) return lstEmoji.size();
        return 0;
    }

    class EmojiHolder extends RecyclerView.ViewHolder {

        private final ImageView ivEmoji;

        public EmojiHolder(@NonNull View itemView) {
            super(itemView);

            ivEmoji = (ImageView) itemView;
        }

        public void onBind(int position) {
            EmojiModel emoji = lstEmoji.get(position);
            if (emoji == null) return;

            Glide.with(context)
                    .load(Uri.parse("file:///android_asset/emoji/" + emoji.getNameEmoji()))
                    .into(ivEmoji);

            itemView.setOnClickListener(v -> callBack.callBackItem(emoji, position));
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void notifyChange() {
        notifyDataSetChanged();
    }
}
