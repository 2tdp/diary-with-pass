package com.note.mydiary.diarywithlock.journalwithlock.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewadddiary.ViewItemContent;
import com.note.mydiary.diarywithlock.journalwithlock.callback.ICallBackDimensional;
import com.note.mydiary.diarywithlock.journalwithlock.callback.ICallBackItem;
import com.note.mydiary.diarywithlock.journalwithlock.callback.ICheckTouch;
import com.note.mydiary.diarywithlock.journalwithlock.model.diarymodel.AudioModel;
import com.note.mydiary.diarywithlock.journalwithlock.model.diarymodel.ContentModel;
import com.note.mydiary.diarywithlock.journalwithlock.sharepref.DataLocalManager;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Constant;

import java.io.File;

import io.realm.RealmList;

public class ContentDiaryAdapter extends RecyclerView.Adapter<ContentDiaryAdapter.ContentDiaryHolder> {

    private final Context context;
    private PictureDiaryAdapter pictureDiaryAdapter;
    private final ICheckTouch isHideOption;
    private final ICallBackItem callBack, callBackDel;
    private final ICallBackDimensional callBackDimensional;
    private RealmList<ContentModel> lstContent;

    public ContentDiaryAdapter(Context context, ICheckTouch isHideOption, ICallBackItem callBack,
                               ICallBackDimensional callBackDimensional, ICallBackItem callBackDel) {
        this.context = context;
        this.isHideOption = isHideOption;
        this.callBack = callBack;
        this.callBackDimensional = callBackDimensional;
        this.callBackDel = callBackDel;

        lstContent = new RealmList<>();
    }

    public void setData(RealmList<ContentModel> lstContent) {
        this.lstContent = lstContent;
        notifyChange();
    }

    @NonNull
    @Override
    public ContentDiaryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContentDiaryHolder(new ViewItemContent(context));
    }

    @Override
    public void onBindViewHolder(@NonNull ContentDiaryHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        if (!lstContent.isEmpty()) return lstContent.size();
        return 0;
    }

    class ContentDiaryHolder extends RecyclerView.ViewHolder {

        ViewItemContent itemContent;

        public ContentDiaryHolder(@NonNull View itemView) {
            super(itemView);

            itemContent = (ViewItemContent) itemView;
            itemContent.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
            if (!DataLocalManager.getOption(Constant.THEME_APP).equals("default"))
                itemContent.createTheme(context, "/theme_app/" + DataLocalManager.getOption(Constant.THEME_APP));

            itemContent.getEt().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    lstContent.get(getBindingAdapterPosition()).setContent(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }

        public void onBind(int position) {
            ContentModel content = lstContent.get(position);
            if (content == null) return;

            if (!content.getContent().equals("")) itemContent.getEt().setText(content.getContent());

            boolean showAudio = false;
            if (content.getAudio() != null)
                if (content.getAudio().getUriAudio() != null)
                    if (!content.getAudio().getUriAudio().equals("")) {
                        if (content.getAudio().isPlay()) playingAudio(0);
                        else playingAudio(1);
                        showAudio = true;
                        File file = new File(content.getAudio().getUriAudio());
                        itemContent.getTvRec().setText(file.getName().replace(".3gp", ""));
                    }
            if (showAudio) itemContent.getRlRec().setVisibility(View.VISIBLE);
            else itemContent.getRlRec().setVisibility(View.GONE);

            if (!content.getLstUriPic().isEmpty()) {
                itemContent.getRcvPic().setVisibility(View.VISIBLE);
                pictureDiaryAdapter = new PictureDiaryAdapter(context, true, callBack,
                        (o, pos) -> {
                            if (content.getLstUriPic().size() == 1) {
                                if (content.getContent().equals("")) {
                                    lstContent.remove(position);
                                    notifyChange();
                                } else {
                                    content.getLstUriPic().remove(pos);
                                    pictureDiaryAdapter.notifyChange();
                                }
                            } else {
                                content.getLstUriPic().remove(pos);
                                pictureDiaryAdapter.notifyChange();
                            }
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
                itemContent.getRcvPic().setLayoutManager(manager);
                itemContent.getRcvPic().setAdapter(pictureDiaryAdapter);
            } else itemContent.getRcvPic().setVisibility(View.GONE);

            itemContent.getRlRec().setOnClickListener(v -> {
                setSelected(position);
                if (!content.getAudio().getUriAudio().equals("")) {
                    File file = new File(content.getAudio().getUriAudio());
                    if (file.exists())
                        callBackDimensional.callBack(content.getAudio().getUriAudio(), position, isDone -> setSelected(-1));
                    else
                        callBackDimensional.callBack(content.getAudio().getArrAudio(), position, isDone -> setSelected(-1));
                }
                playingAudio(0);
            });

            itemContent.getIvRemove().setOnClickListener(v -> callBackDel.callBackItem(content, position));

            itemContent.getEt().setOnClickListener(v -> isHideOption.checkTouch(true));
        }

        private void playingAudio(int pos) {
            if (pos == 0) {
                itemContent.getTvRec().setVisibility(View.GONE);
                itemContent.getIvWave().setVisibility(View.VISIBLE);
            } else {
                itemContent.getTvRec().setVisibility(View.VISIBLE);
                itemContent.getIvWave().setVisibility(View.GONE);
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
