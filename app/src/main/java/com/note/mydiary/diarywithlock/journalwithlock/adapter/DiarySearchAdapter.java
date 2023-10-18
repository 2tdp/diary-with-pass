package com.note.mydiary.diarywithlock.journalwithlock.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.note.mydiary.diarywithlock.journalwithlock.R;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.ViewContent;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewdialog.ViewDialogMoreDiary;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewdialog.ViewDialogTextDiary;
import com.note.mydiary.diarywithlock.journalwithlock.activity.database.DatabaseRealm;
import com.note.mydiary.diarywithlock.journalwithlock.callback.ICallBackItem;
import com.note.mydiary.diarywithlock.journalwithlock.callback.ICheckTouch;
import com.note.mydiary.diarywithlock.journalwithlock.model.diarymodel.ContentModel;
import com.note.mydiary.diarywithlock.journalwithlock.model.diarymodel.DiaryModel;
import com.note.mydiary.diarywithlock.journalwithlock.model.picture.PicModel;
import com.note.mydiary.diarywithlock.journalwithlock.sharepref.DataLocalManager;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Constant;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Utils;
import com.note.mydiary.diarywithlock.journalwithlock.utils.UtilsBitmap;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DiarySearchAdapter extends RecyclerView.Adapter<DiarySearchAdapter.DiarySearchHolder> {

    private final Context context;
    private final ICallBackItem callBack;
    private final ICheckTouch isDel, resetCalendar;
    private List<DiaryModel> lstDiary;

    private final int w;

    public DiarySearchAdapter(Context context, ICheckTouch isDel, ICheckTouch resetCalendar, ICallBackItem callBack) {
        this.context = context;
        this.isDel = isDel;
        this.resetCalendar = resetCalendar;
        this.callBack = callBack;

        w = context.getResources().getDisplayMetrics().widthPixels;
        lstDiary = new ArrayList<>();
    }

    public void setData(List<DiaryModel> lstDiary) {
        this.lstDiary = lstDiary;
        notifyChanger();
    }

    @NonNull
    @Override
    public DiarySearchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewContent viewContent = new ViewContent(parent.getContext());
        RelativeLayout.LayoutParams paramsContent = new RelativeLayout.LayoutParams(-1, -2);
        paramsContent.setMargins(0, 0, 0, (int) (2.778f * w / 100));
        viewContent.setLayoutParams(paramsContent);
        return new DiarySearchHolder(viewContent);
    }

    @Override
    public void onBindViewHolder(@NonNull DiarySearchHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        if (!lstDiary.isEmpty()) return lstDiary.size();
        return 0;
    }

    class DiarySearchHolder extends RecyclerView.ViewHolder {

        ViewContent viewContent;

        public DiarySearchHolder(@NonNull View itemView) {
            super(itemView);

            viewContent = (ViewContent) itemView;
            if (!DataLocalManager.getOption(Constant.THEME_APP).equals("default")) {
                viewContent.createTheme(context, "/theme_app/" + DataLocalManager.getOption(Constant.THEME_APP));
            }
        }

        @SuppressLint("SimpleDateFormat")
        public void onBind(int position) {
            DiaryModel diary = lstDiary.get(position);
            if (diary == null) return;

            viewContent.getTvDate().setText(new SimpleDateFormat(Constant.FULL_DATE_DAY, Locale.ENGLISH).format(diary.getDateTimeStamp()));
            viewContent.getTvTitle().setText(diary.getTitleDiary());

            boolean checkTextContent = false, checkAudio = false;
            for (ContentModel ct : diary.getLstContent()) {
                if (checkAudio && checkTextContent) break;
                if (ct.getAudio() != null && !checkAudio)
                    if (ct.getAudio().getUriAudio() != null)
                        if (!ct.getAudio().getUriAudio().equals("")) {
                            viewContent.getLlRec().setVisibility(View.VISIBLE);
                            File file = new File(ct.getAudio().getUriAudio());
                            viewContent.getTvRec().setText(file.getName().replace(".3gp", ""));
                            checkAudio = true;
                        }

                if (!checkTextContent) {
                    if (!ct.getContent().equals("")) {
                        viewContent.getTvContent().setText(ct.getContent());
                        checkTextContent = true;
                    } else viewContent.getTvContent().setText("");
                }
            }

            viewContent.getIvEmoji().setImageBitmap(UtilsBitmap.getBitmapFromAsset(context, diary.getEmojiModel().getFolder(), diary.getEmojiModel().getNameEmoji()));

            DatabaseRealm.getAllPicInDiary(diary.getId(), (o, pos) -> {
                List<PicModel> lstPic = (List<PicModel>) o;
                if (lstPic.isEmpty()) {
                    viewContent.getRcvPic().setVisibility(View.GONE);
                    return;
                }

                viewContent.getRcvPic().setVisibility(View.VISIBLE);
                DetailPictureAdapter detailPictureAdapter = new DetailPictureAdapter(context, true, false, (ob, posi) -> {
                });

                detailPictureAdapter.setData((ArrayList<PicModel>) lstPic);
                viewContent.getRcvPic().setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                viewContent.getRcvPic().setAdapter(detailPictureAdapter);
            });

            evenClick(diary, position);
        }

        private void evenClick(DiaryModel diary, int position) {
            viewContent.getIvMore().setOnClickListener(v -> {
                Utils.hideKeyboard(context, v);
                ViewDialogMoreDiary viewDialogMoreDiary = new ViewDialogMoreDiary(context);
                PopupWindow popUp = new PopupWindow(viewDialogMoreDiary, (int) (45f * w / 100), RelativeLayout.LayoutParams.WRAP_CONTENT, true);
                popUp.showAsDropDown(v, (int) (-34f * w / 100), 0);

                viewDialogMoreDiary.getViewItemDel().setOnClickListener(vDel -> {
                    popUp.dismiss();
                    ViewDialogTextDiary viewDialogTextDiary = new ViewDialogTextDiary(context);
                    viewDialogTextDiary.getTvTitle().setText(context.getResources().getString(R.string.del_diary));
                    viewDialogTextDiary.getTvContent().setText(context.getResources().getString(R.string.are_you_sure_want_to_delete_this_diary));

                    AlertDialog dialog = new AlertDialog.Builder(context, R.style.SheetDialog).create();
                    dialog.setView(viewDialogTextDiary);
                    dialog.setCancelable(false);
                    dialog.show();
                    viewDialogTextDiary.getLayoutParams().width = (int) (84.444f * w / 100);

                    viewDialogTextDiary.getViewYesNo().getTvNo().setOnClickListener(vNo -> dialog.cancel());
                    viewDialogTextDiary.getViewYesNo().getTvYes().setOnClickListener(vYes -> {
                        DatabaseRealm.delOtherDiary(diary.getId(), isDone -> isDel.checkTouch(true), resetCalendar);
                        dialog.cancel();
                    });
                });
                viewDialogMoreDiary.getViewItemShare().setOnClickListener(vShare -> {
                });
                viewDialogMoreDiary.getViewItemPreview().setOnClickListener(vPre -> {
                    callBack.callBackItem(diary, position);
                    popUp.dismiss();
                });
            });

            viewContent.setOnClickListener(v -> {
                Utils.hideKeyboard(context, v);
                callBack.callBackItem(diary, position);
            });
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void notifyChanger() {
        notifyDataSetChanged();
    }
}
