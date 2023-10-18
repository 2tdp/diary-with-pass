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
import com.note.mydiary.diarywithlock.journalwithlock.utils.UtilsBitmap;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DiaryInDayCalendarAdapter extends RecyclerView.Adapter<DiaryInDayCalendarAdapter.DiaryInDayCalendarHolder> {

    private final Context context;
    private final ICheckTouch isDel, resetCalendar;
    private final ICallBackItem callBack;
    private List<DiaryModel> lstDiary;

    private final int w;

    public DiaryInDayCalendarAdapter(Context context, ICheckTouch isDel, ICheckTouch resetCalendar, ICallBackItem callBack) {
        this.context = context;
        this.isDel = isDel;
        this.resetCalendar = resetCalendar;
        this.callBack = callBack;

        w = context.getResources().getDisplayMetrics().widthPixels;
        lstDiary = new ArrayList<>();
    }

    public void setData(List<DiaryModel> lstDiary) {
        this.lstDiary = lstDiary;
        notifyChange();
    }

    @NonNull
    @Override
    public DiaryInDayCalendarHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DiaryInDayCalendarHolder(new ViewContent(context));
    }

    @Override
    public void onBindViewHolder(@NonNull DiaryInDayCalendarHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        if (!lstDiary.isEmpty()) return lstDiary.size();
        return 0;
    }

    class DiaryInDayCalendarHolder extends RecyclerView.ViewHolder {

        ViewContent viewContent;

        public DiaryInDayCalendarHolder(@NonNull View itemView) {
            super(itemView);

            viewContent = (ViewContent) itemView;
            viewContent.setParams();
            if (!DataLocalManager.getOption(Constant.THEME_APP).equals("default"))
                viewContent.createTheme(context, "/theme_app/" + DataLocalManager.getOption(Constant.THEME_APP));
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
                if (ct.getAudio() != null && !checkAudio) {
                    if (ct.getAudio().getUriAudio() != null) {
                        if (!ct.getAudio().getUriAudio().equals("")) {
                            File file = new File(ct.getAudio().getUriAudio());
                            viewContent.getTvRec().setText(file.getName().replace(".3gp", ""));
                            checkAudio = true;
                        }
                    }
                }

                if (!checkTextContent) {
                    if (!ct.getContent().equals("")) {
                        viewContent.getTvContent().setText(ct.getContent());
                        checkTextContent = true;
                    } else viewContent.getTvContent().setText("");
                }
            }

            if (checkAudio) viewContent.getLlRec().setVisibility(View.VISIBLE);
            else viewContent.getLlRec().setVisibility(View.GONE);

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
            viewContent.setOnClickListener(v -> callBack.callBackItem(diary, position));
            viewContent.getIvMore().setOnClickListener(v -> {
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

                viewDialogMoreDiary.getViewItemPreview().setOnClickListener(vPre -> {
                    callBack.callBackItem(diary, position);
                    popUp.dismiss();
                });
            });
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void notifyChange() {
        notifyDataSetChanged();
    }
}
