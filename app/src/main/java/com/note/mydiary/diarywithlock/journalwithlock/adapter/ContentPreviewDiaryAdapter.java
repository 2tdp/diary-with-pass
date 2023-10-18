package com.note.mydiary.diarywithlock.journalwithlock.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewpreview.ViewItemContentPreview;
import com.note.mydiary.diarywithlock.journalwithlock.callback.ICallBackDimensional;
import com.note.mydiary.diarywithlock.journalwithlock.callback.ICallBackItem;
import com.note.mydiary.diarywithlock.journalwithlock.model.diarymodel.AudioModel;
import com.note.mydiary.diarywithlock.journalwithlock.model.diarymodel.ContentModel;
import com.note.mydiary.diarywithlock.journalwithlock.sharepref.DataLocalManager;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Constant;

import java.io.File;

import io.realm.RealmList;

public class ContentPreviewDiaryAdapter extends RecyclerView.Adapter<ContentPreviewDiaryAdapter.ContentPreviewHolder> {

    private final Context context;
    private final ICallBackItem callBack;
    private final ICallBackDimensional callBackDimensional;
    private RealmList<ContentModel> lstContent;

    public ContentPreviewDiaryAdapter(Context context, ICallBackItem callBack, ICallBackDimensional callBackDimensional) {
        this.context = context;
        this.callBack = callBack;
        this.callBackDimensional = callBackDimensional;

        lstContent = new RealmList<>();
    }

    public void setData(RealmList<ContentModel> lstContent) {
        this.lstContent = lstContent;
        notifyChange();
    }

    @NonNull
    @Override
    public ContentPreviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContentPreviewHolder(new ViewItemContentPreview(context));
    }

    @Override
    public void onBindViewHolder(@NonNull ContentPreviewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        if (!lstContent.isEmpty()) return lstContent.size();
        return 0;
    }

    class ContentPreviewHolder extends RecyclerView.ViewHolder {

        ViewItemContentPreview viewItemContentPreview;

        public ContentPreviewHolder(@NonNull View itemView) {
            super(itemView);
            viewItemContentPreview = (ViewItemContentPreview) itemView;

            if (!DataLocalManager.getOption(Constant.THEME_APP).equals("default"))
                viewItemContentPreview.createTheme(context, "/theme_app/" + DataLocalManager.getOption(Constant.THEME_APP));
        }

        public void onBind(int position) {
            ContentModel content = lstContent.get(position);
            if (content == null) return;

            if (!content.getContent().equals(""))
                viewItemContentPreview.getTv().setText(content.getContent());

            if (content.getAudio() != null) {
                if (content.getAudio().getUriAudio() != null)
                    if (!content.getAudio().getUriAudio().equals("")) {
                        if (content.getAudio().isPlay()) playingAudio(0);
                        else playingAudio(1);
                        viewItemContentPreview.getLlRec().setVisibility(View.VISIBLE);
                        File file = new File(content.getAudio().getUriAudio());
                        viewItemContentPreview.getTvRec().setText(file.getName().replace(".3gp", ""));
                    }
            } else viewItemContentPreview.getLlRec().setVisibility(View.GONE);

            if (!content.getLstUriPic().isEmpty()) {
                viewItemContentPreview.getRcvPic().setVisibility(View.VISIBLE);
                PictureDiaryAdapter pictureDiaryAdapter = new PictureDiaryAdapter(context, false, callBack, (o, pos) -> {
                });
                pictureDiaryAdapter.setData(content.getLstUriPic());

                GridLayoutManager manager = new GridLayoutManager(context, 2);
                manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        if (content.getLstUriPic().get(position).getRatio() > 1) return 2;
                        else return 1;
                    }
                });
                viewItemContentPreview.getRcvPic().setLayoutManager(manager);
                viewItemContentPreview.getRcvPic().setAdapter(pictureDiaryAdapter);
            } else viewItemContentPreview.getRcvPic().setVisibility(View.GONE);

            viewItemContentPreview.getLlRec().setOnClickListener(v -> {
                setSelected(position);
                callBackDimensional.callBack(content.getAudio().getArrAudio(), position, isDone -> setSelected(-1));
                playingAudio(0);
            });
        }

        private void playingAudio(int pos) {
            if (pos == 0) {
                viewItemContentPreview.getTvRec().setVisibility(View.GONE);
                viewItemContentPreview.getIvWave().setVisibility(View.VISIBLE);
            } else {
                viewItemContentPreview.getTvRec().setVisibility(View.VISIBLE);
                viewItemContentPreview.getIvWave().setVisibility(View.GONE);
            }
        }
    }

    public void setSelected(int position) {
        for (int i = 0; i < lstContent.size(); i++) {
            ContentModel content = lstContent.get(i);
            if (content != null) {
                AudioModel audio = content.getAudio();
                if (audio != null) audio.setPlay(position == i);
            }
        }
        notifyChange();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void notifyChange() {
        notifyDataSetChanged();
    }
}
