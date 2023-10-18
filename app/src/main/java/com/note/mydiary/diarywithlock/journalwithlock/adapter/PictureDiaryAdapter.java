package com.note.mydiary.diarywithlock.journalwithlock.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.note.mydiary.diarywithlock.journalwithlock.R;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewpickpicture.ViewItemPicture;
import com.note.mydiary.diarywithlock.journalwithlock.callback.ICallBackItem;
import com.note.mydiary.diarywithlock.journalwithlock.model.picture.PicModel;

import io.realm.RealmList;

public class PictureDiaryAdapter extends RecyclerView.Adapter<PictureDiaryAdapter.DetailPictureHolder> {

    private final Context context;
    private RealmList<PicModel> lstPic;
    private final ICallBackItem callBack, callBackDel;
    private final boolean isRemove;

    public PictureDiaryAdapter(Context context, boolean isRemove, ICallBackItem callBack, ICallBackItem callBackDel) {
        this.context = context;
        this.isRemove = isRemove;
        this.callBack = callBack;
        this.callBackDel = callBackDel;
        lstPic = new RealmList<>();
    }

    public void setData(RealmList<PicModel> lstPic) {
        this.lstPic = lstPic;
        notifyChange();
    }

    @NonNull
    @Override
    public DetailPictureHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DetailPictureHolder(new ViewItemPicture(context));
    }

    @Override
    public void onBindViewHolder(@NonNull DetailPictureHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        if (!lstPic.isEmpty()) return lstPic.size();
        return 0;
    }

    public class DetailPictureHolder extends RecyclerView.ViewHolder {

        ViewItemPicture iv;

        public DetailPictureHolder(@NonNull View itemView) {
            super(itemView);

            iv = (ViewItemPicture) itemView;
            iv.setParam();
            if (!isRemove) iv.getIvRemove().setVisibility(View.GONE);
        }

        @SuppressLint("CheckResult")
        public void onBind(int position) {
            PicModel picModel = lstPic.get(position);
            if (picModel == null) return;

            if (picModel.getUri() != null)
                Glide.with(context)
                        .load(Uri.parse(picModel.getUri()))
                        .placeholder(R.drawable.ic_err)
                        .skipMemoryCache(false)
                        .into(iv.getIv());
            else Glide.with(context)
                    .load(picModel.getArrPic())
                    .placeholder(R.drawable.ic_err)
                    .skipMemoryCache(false)
                    .into(iv.getIv());

            iv.setOnClickListener(v -> callBack.callBackItem(picModel, position));
            iv.getIvRemove().setOnClickListener(v -> callBackDel.callBackItem(picModel, position));
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    public void notifyChange() {
        notifyDataSetChanged();
    }
}
