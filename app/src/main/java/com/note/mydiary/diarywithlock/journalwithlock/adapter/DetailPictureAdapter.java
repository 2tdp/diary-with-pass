package com.note.mydiary.diarywithlock.journalwithlock.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.note.mydiary.diarywithlock.journalwithlock.R;
import com.note.mydiary.diarywithlock.journalwithlock.callback.ICallBackItem;
import com.note.mydiary.diarywithlock.journalwithlock.model.picture.PicModel;

import java.util.ArrayList;

import io.realm.RealmList;

public class DetailPictureAdapter extends RecyclerView.Adapter<DetailPictureAdapter.DetailPictureHolder> {

    private Context context;
    private ArrayList<PicModel> lstPic;
    private final ICallBackItem callBack;

    private int w, count = 0;
    private final boolean isCover, isPicInDiary;

    public DetailPictureAdapter(Context context, boolean isPicInDiary, boolean isCover, ICallBackItem callBack) {
        this.context = context;
        this.isPicInDiary = isPicInDiary;
        this.isCover = isCover;
        this.callBack = callBack;
        lstPic = new ArrayList<>();
    }

    public void setData(ArrayList<PicModel> lstPic) {
        this.lstPic = lstPic;
        notifyChange();
    }

    @NonNull
    @Override
    public DetailPictureHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        w = parent.getResources().getDisplayMetrics().widthPixels;
        RoundedImageView iv = new RoundedImageView(context);
        iv.setCornerRadius(w / 100f);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        if (isPicInDiary) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) (8.33f * w / 100), (int) (8.33f * w / 100));
            params.setMargins(0, (int) (2.778f * w / 100), (int) (2.778f * w / 100), 0);
            iv.setLayoutParams(params);
        } else {
            iv.setLayoutParams(new ViewGroup.LayoutParams(-1, (int) (22.48f * w / 100)));
            iv.setPadding(w / 100, w / 100, w / 100, w / 100);
        }
        return new DetailPictureHolder(iv);
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

        RoundedImageView iv;

        public DetailPictureHolder(@NonNull View itemView) {
            super(itemView);

            iv = (RoundedImageView) itemView;
        }

        public void onBind(int position) {
            PicModel picture = lstPic.get(position);
            if (picture == null) return;

            if (isPicInDiary)
                Glide.with(context)
                        .load(picture.getArrPic())
                        .placeholder(R.drawable.ic_err)
                        .into(iv);
            else
                Glide.with(context)
                        .load(Uri.parse(picture.getUri()))
                        .placeholder(R.drawable.ic_err)
                        .into(iv);


            if (!isCover && !isPicInDiary)
                if (picture.isCheck()) {
                    iv.setBorderWidth(0.5f * w / 100);
                    iv.setBorderColor(context.getResources().getColor(R.color.orange));
                } else iv.setBorderWidth(0.1f);

            iv.setOnClickListener(v -> {
                if (isPicInDiary) return;
                if (count > 0) {
                    if (!picture.isCheck()) {
                        iv.setBorderWidth(0.5f * w / 100);
                        iv.setBorderColor(context.getResources().getColor(R.color.orange));
                        picture.setCheck(true);
                        count += 1;
                    } else {
                        iv.setBorderWidth(0.1f);
                        picture.setCheck(false);
                        count -= 1;
                    }
                } else callBack.callBackItem(picture, position);
            });

            iv.setOnLongClickListener(v -> {
                if (isCover) return false;

                iv.setBorderWidth(0.5f * w / 100);
                iv.setBorderColor(context.getResources().getColor(R.color.orange));
                picture.setCheck(true);
                count += 1;
                return true;
            });
        }
    }

    public RealmList<PicModel> getSelected() {
        RealmList<PicModel> lstPicSelected = new RealmList<>();
        for (int i = 0; i < lstPic.size(); i++)
            if (lstPic.get(i).isCheck()) lstPicSelected.add(lstPic.get(i));

        return lstPicSelected;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void notifyChange() {
        notifyDataSetChanged();
    }
}
