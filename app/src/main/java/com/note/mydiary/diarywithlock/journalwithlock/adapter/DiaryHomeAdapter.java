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
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewdialog.ViewDialogMoreDiary;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewdialog.ViewDialogTextDiary;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewhome.ViewItemDiaryHome;
import com.note.mydiary.diarywithlock.journalwithlock.activity.database.DatabaseRealm;
import com.note.mydiary.diarywithlock.journalwithlock.callback.ICallBackClickItemDiary;
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
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DiaryHomeAdapter extends RecyclerView.Adapter<DiaryHomeAdapter.DiaryHomeHolder> {

    private final Context context;
    private final ICallBackClickItemDiary callBack;
    private final ICheckTouch isDel, resetCalendar;
    private List<DiaryModel> lstDiary;
    private long[] oldDate;
    private final int w;

    public DiaryHomeAdapter(Context context, ICheckTouch isDel, ICheckTouch resetCalendar, ICallBackClickItemDiary callBack) {
        this.context = context;
        this.isDel = isDel;
        this.resetCalendar = resetCalendar;
        this.callBack = callBack;

        w = context.getResources().getDisplayMetrics().widthPixels;
        oldDate = new long[]{0, 0};
        lstDiary = new ArrayList<>();
    }

    public void setData(List<DiaryModel> lstDiary) {
        this.lstDiary = lstDiary;

        for (DiaryModel diary : lstDiary) {
            long[] timeMonth = DatabaseRealm.getTimeInMonth(diary.getDateTimeStamp());
            if (timeMonth[0] < oldDate[0] || timeMonth[1] > oldDate[1]) {
                diary.setSelected(true);
                oldDate = timeMonth;
            }
        }
        notifyChange();
    }

    @NonNull
    @Override
    public DiaryHomeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewItemDiaryHome viewItemDiaryHome = new ViewItemDiaryHome(context);
        return new DiaryHomeHolder(viewItemDiaryHome);
    }

    @Override
    public void onBindViewHolder(@NonNull DiaryHomeHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        if (!lstDiary.isEmpty()) return lstDiary.size();
        return 0;
    }

    class DiaryHomeHolder extends RecyclerView.ViewHolder {

        ViewItemDiaryHome viewItemDiary;

        public DiaryHomeHolder(@NonNull View itemView) {
            super(itemView);

            viewItemDiary = (ViewItemDiaryHome) itemView;
            viewItemDiary.setParams();
            if (!DataLocalManager.getOption(Constant.THEME_APP).equals("default")) {
                viewItemDiary.createTheme(context, "/theme_app/" + DataLocalManager.getOption(Constant.THEME_APP));
                viewItemDiary.getViewContent().createTheme(context, "/theme_app/" + DataLocalManager.getOption(Constant.THEME_APP));
            }
        }

        @SuppressLint("SimpleDateFormat")
        public void onBind(int position) {
            DiaryModel diary = lstDiary.get(position);
            if (diary == null) return;

            Date date = new Date(diary.getDateTimeStamp());

            if (diary.isSelected()) {
                String dateTime = new SimpleDateFormat(Constant.MONTH_YEAR, Constant.LOCALE_DEFAULT).format(date);

                viewItemDiary.getRlTop().setVisibility(View.VISIBLE);
                viewItemDiary.getTvDateTime().setText(dateTime);
            } else viewItemDiary.getRlTop().setVisibility(View.GONE);

            viewItemDiary.getViewContent().getTvDate().setText(new SimpleDateFormat(Constant.FULL_DATE_DAY, Locale.ENGLISH).format(date));

            viewItemDiary.getViewContent().getTvTitle().setText(diary.getTitleDiary());

            boolean checkTextContent = false, checkAudio = false;
            for (ContentModel ct : diary.getLstContent()) {
                if (checkAudio && checkTextContent) break;
                if (ct.getAudio() != null && !checkAudio) {
                    if (ct.getAudio().getUriAudio() != null)
                        if (!ct.getAudio().getUriAudio().equals("")) {
                            File file = new File(ct.getAudio().getUriAudio());
                            viewItemDiary.getViewContent().getTvRec().setText(file.getName().replace(".3gp", ""));
                            checkAudio = true;
                        }
                }

                if (!checkTextContent) {
                    if (!ct.getContent().equals("")) {
                        viewItemDiary.getViewContent().getTvContent().setText(ct.getContent());
                        checkTextContent = true;
                    } else viewItemDiary.getViewContent().getTvContent().setText("");
                }
            }

            if (checkAudio) viewItemDiary.getViewContent().getLlRec().setVisibility(View.VISIBLE);
            else viewItemDiary.getViewContent().getLlRec().setVisibility(View.GONE);

            viewItemDiary.getViewContent().getIvEmoji().setImageBitmap(UtilsBitmap.getBitmapFromAsset(context,
                    diary.getEmojiModel().getFolder(), diary.getEmojiModel().getNameEmoji()));

            DatabaseRealm.getAllPicInDiary(diary.getId(), (o, pos) -> {
                List<PicModel> lstPic = (List<PicModel>) o;
                if (lstPic.isEmpty()) {
                    viewItemDiary.getViewContent().getRcvPic().setVisibility(View.GONE);
                    return;
                }

                viewItemDiary.getViewContent().getRcvPic().setVisibility(View.VISIBLE);
                DetailPictureAdapter detailPictureAdapter = new DetailPictureAdapter(context, true, false, (ob, posi) -> {
                });

                detailPictureAdapter.setData((ArrayList<PicModel>) lstPic);
                viewItemDiary.getViewContent().getRcvPic().setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                viewItemDiary.getViewContent().getRcvPic().setAdapter(detailPictureAdapter);
            });

            evenClick(diary, position);
        }

        private void evenClick(DiaryModel diary, int position) {
            viewItemDiary.getTvSeeAll().setOnClickListener(v -> callBack.clickCallback(diary, position, true));
            viewItemDiary.setOnClickListener(v -> callBack.clickCallback(diary, position, false));
            viewItemDiary.getViewContent().getIvMore().setOnClickListener(v -> {
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
                    callBack.clickCallback(diary, position, false);
                    popUp.dismiss();
                });
            });
        }
    }

    public void setDateDefault() {
        oldDate = new long[]{0, 0};
    }

    @SuppressLint("NotifyDataSetChanged")
    public void notifyChange() {
        notifyDataSetChanged();
    }
}
