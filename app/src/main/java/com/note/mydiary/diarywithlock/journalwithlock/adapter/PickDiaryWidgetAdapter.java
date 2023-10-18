package com.note.mydiary.diarywithlock.journalwithlock.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.ViewContent;
import com.note.mydiary.diarywithlock.journalwithlock.activity.database.DatabaseRealm;
import com.note.mydiary.diarywithlock.journalwithlock.callback.ICallBackItem;
import com.note.mydiary.diarywithlock.journalwithlock.model.diarymodel.ContentModel;
import com.note.mydiary.diarywithlock.journalwithlock.model.diarymodel.DiaryModel;
import com.note.mydiary.diarywithlock.journalwithlock.model.picture.PicModel;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Constant;
import com.note.mydiary.diarywithlock.journalwithlock.utils.UtilsBitmap;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PickDiaryWidgetAdapter extends RecyclerView.Adapter<PickDiaryWidgetAdapter.PickDiaryWidgetHolder> {

    private Context context;
    private List<DiaryModel> lstDiary;
    private ICallBackItem callBack;

    public PickDiaryWidgetAdapter(Context context, ICallBackItem callBack) {
        this.context = context;
        this.callBack = callBack;
        lstDiary = new ArrayList<>();
    }

    public void setData(List<DiaryModel> lstDiary) {
        this.lstDiary = lstDiary;
        notifyChange();
    }

    @NonNull
    @Override
    public PickDiaryWidgetHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PickDiaryWidgetHolder(new ViewContent(context));
    }

    @Override
    public void onBindViewHolder(@NonNull PickDiaryWidgetHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        if (!lstDiary.isEmpty()) return lstDiary.size();
        return 0;
    }

    class PickDiaryWidgetHolder extends RecyclerView.ViewHolder {

        ViewContent viewItemDiary;

        public PickDiaryWidgetHolder(@NonNull View itemView) {
            super(itemView);

            viewItemDiary = (ViewContent) itemView;
            viewItemDiary.setParams();
        }

        public void onBind(int position) {
            DiaryModel diary = lstDiary.get(position);
            if (diary == null) return;

            Date date = new Date(diary.getDateTimeStamp());

            viewItemDiary.getTvDate().setText(new SimpleDateFormat(Constant.FULL_DATE_DAY, Locale.ENGLISH).format(date));

            viewItemDiary.getTvTitle().setText(diary.getTitleDiary());

            boolean checkTextContent = false, checkAudio = false;
            for (ContentModel ct : diary.getLstContent()) {
                if (ct.getAudio() != null && !checkAudio)
                    if (ct.getAudio().getUriAudio() != null)
                        if (!ct.getAudio().getUriAudio().equals("")) {
                            viewItemDiary.getLlRec().setVisibility(View.VISIBLE);
                            File file = new File(ct.getAudio().getUriAudio());
                            viewItemDiary.getTvRec().setText(file.getName().replace(".3gp", ""));
                            checkAudio = true;
                        }

                if (!checkTextContent) {
                    if (!ct.getContent().equals("")) {
                        viewItemDiary.getTvContent().setText(ct.getContent());
                        checkTextContent = true;
                    }
                }
            }

            viewItemDiary.getIvEmoji().setImageBitmap(UtilsBitmap.getBitmapFromAsset(context, diary.getEmojiModel().getFolder(), diary.getEmojiModel().getNameEmoji()));

            DatabaseRealm.getAllPicInDiary(diary.getId(), (o, pos) -> {
                List<PicModel> lstPic = (List<PicModel>) o;
                if (lstPic.isEmpty())
                    viewItemDiary.getRcvPic().setVisibility(View.GONE);
                else {
                    DetailPictureAdapter detailPictureAdapter = new DetailPictureAdapter(context, true, false, (ob, posi) -> {
                    });

                    detailPictureAdapter.setData((ArrayList<PicModel>) lstPic);
                    viewItemDiary.getRcvPic().setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                    viewItemDiary.getRcvPic().setAdapter(detailPictureAdapter);
                }
            });

            viewItemDiary.setOnClickListener(v -> callBack.callBackItem(diary, position));
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void notifyChange() {
        notifyDataSetChanged();
    }
}
