package com.note.mydiary.diarywithlock.journalwithlock.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
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
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewshowall.ViewItemShowAll;
import com.note.mydiary.diarywithlock.journalwithlock.activity.database.DatabaseRealm;
import com.note.mydiary.diarywithlock.journalwithlock.callback.ICallBackItem;
import com.note.mydiary.diarywithlock.journalwithlock.callback.ICheckTouch;
import com.note.mydiary.diarywithlock.journalwithlock.model.diarymodel.ContentModel;
import com.note.mydiary.diarywithlock.journalwithlock.model.diarymodel.DiaryModel;
import com.note.mydiary.diarywithlock.journalwithlock.model.picture.PicModel;
import com.note.mydiary.diarywithlock.journalwithlock.model.theme.ConfigAppThemeModel;
import com.note.mydiary.diarywithlock.journalwithlock.sharepref.DataLocalManager;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Constant;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Utils;
import com.note.mydiary.diarywithlock.journalwithlock.utils.UtilsBitmap;
import com.note.mydiary.diarywithlock.journalwithlock.utils.UtilsTheme;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.realm.RealmList;

public class DiaryShowAllAdapter extends RecyclerView.Adapter<DiaryShowAllAdapter.DiaryShowAllHolder> {

    private final Context context;
    private ConfigAppThemeModel config;
    private List<DiaryModel> lstDiary;
    private final ICheckTouch isDel, isMultiDel, resetCalendar;
    private final ICallBackItem callBack;
    private final int w;
    private boolean isMultiDelete, isTheme;

    public DiaryShowAllAdapter(Context context, ICheckTouch isDel, ICheckTouch isMultiDel, ICheckTouch resetCalendar, ICallBackItem callBack) {
        this.context = context;
        this.isDel = isDel;
        this.isMultiDel = isMultiDel;
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
    public DiaryShowAllHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DiaryShowAllHolder(new ViewItemShowAll(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(@NonNull DiaryShowAllHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        if (!lstDiary.isEmpty()) return lstDiary.size();
        return 0;
    }

    class DiaryShowAllHolder extends RecyclerView.ViewHolder {

        ViewItemShowAll viewItemShowAll;

        public DiaryShowAllHolder(@NonNull View itemView) {
            super(itemView);
            viewItemShowAll = (ViewItemShowAll) itemView;
            if (!DataLocalManager.getOption(Constant.THEME_APP).equals("default")) {
                viewItemShowAll.createTheme(context, "/theme_app/" + DataLocalManager.getOption(Constant.THEME_APP));
                viewItemShowAll.getViewContent().createTheme(context, "/theme_app/" + DataLocalManager.getOption(Constant.THEME_APP));

                String jsonConfig = Utils.readFromFile(context, "theme/theme_app/theme_app/" + DataLocalManager.getOption(Constant.THEME_APP) + "/config.txt");

                config = DataLocalManager.getConfigApp(jsonConfig);
                isTheme = true;
            }
        }

        @SuppressLint("SimpleDateFormat")
        public void onBind(int position) {
            DiaryModel diary = lstDiary.get(position);
            if (diary == null) return;

            if (isMultiDelete) {
                if (diary.isSelected()) {
                    if (isTheme)
                        UtilsTheme.changeIconSelectDiary(context, viewItemShowAll.getIvTick(),
                                Color.parseColor(config.getColorMain()),
                                Color.parseColor(config.getColorMain()),
                                Color.parseColor(config.getColorIconInColor()),
                                true);
                    else viewItemShowAll.getIvTick().setImageResource(R.drawable.ic_tick_del);
                } else {
                    if (isTheme)
                        UtilsTheme.changeIconSelectDiary(context, viewItemShowAll.getIvTick(),
                                Color.parseColor(config.getColorMain()),
                                Color.parseColor(config.getColorMain()),
                                Color.parseColor(config.getColorIconInColor()),
                                false);
                    else viewItemShowAll.getIvTick().setImageResource(R.drawable.ic_un_tick_del);
                }

                viewItemShowAll.getIvTick().setVisibility(View.VISIBLE);
                viewItemShowAll.getViewContent().getIvMore().setVisibility(View.GONE);
                viewItemShowAll.setMarginsViewContent(true);
                viewItemShowAll.setTranslationX(8.33f * w / 100);
            } else {
                viewItemShowAll.getIvTick().setVisibility(View.GONE);
                viewItemShowAll.getViewContent().getIvMore().setVisibility(View.VISIBLE);
                viewItemShowAll.setMarginsViewContent(false);
                viewItemShowAll.setTranslationX(0);
            }

            viewItemShowAll.getViewContent().getTvDate().setText(new SimpleDateFormat(Constant.FULL_DATE_DAY, Locale.ENGLISH).format(diary.getDateTimeStamp()));
            viewItemShowAll.getViewContent().getTvTitle().setText(diary.getTitleDiary());

            boolean checkTextContent = false, checkAudio = false;
            for (ContentModel ct : diary.getLstContent()) {
                if (checkAudio && checkTextContent) break;
                if (ct.getAudio() != null && !checkAudio)
                    if (ct.getAudio().getUriAudio() != null)
                        if (!ct.getAudio().getUriAudio().equals("")) {
                            viewItemShowAll.getViewContent().getLlRec().setVisibility(View.VISIBLE);
                            File file = new File(ct.getAudio().getUriAudio());
                            viewItemShowAll.getViewContent().getTvRec().setText(file.getName().replace(".3gp", ""));
                            checkAudio = true;
                        }

                if (!checkTextContent) {
                    if (!ct.getContent().equals("")) {
                        viewItemShowAll.getViewContent().getTvContent().setText(ct.getContent());
                        checkTextContent = true;
                    } else viewItemShowAll.getViewContent().getTvContent().setText("");
                }
            }

            viewItemShowAll.getViewContent().getIvEmoji().setImageBitmap(UtilsBitmap.getBitmapFromAsset(context, diary.getEmojiModel().getFolder(), diary.getEmojiModel().getNameEmoji()));

            DatabaseRealm.getAllPicInDiary(diary.getId(), (o, pos) -> {
                List<PicModel> lstPic = (List<PicModel>) o;
                if (lstPic.isEmpty())
                    viewItemShowAll.getViewContent().getRcvPic().setVisibility(View.GONE);
                else {
                    DetailPictureAdapter detailPictureAdapter = new DetailPictureAdapter(context, true, false, (ob, posi) -> {
                    });

                    detailPictureAdapter.setData((ArrayList<PicModel>) lstPic);
                    viewItemShowAll.getViewContent().getRcvPic().setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                    viewItemShowAll.getViewContent().getRcvPic().setAdapter(detailPictureAdapter);
                }
            });

            evenClick(diary, position);
        }

        private void evenClick(DiaryModel diary, int position) {
            viewItemShowAll.getViewContent().getIvMore().setOnClickListener(v -> {
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

            viewItemShowAll.setOnLongClickListener(v -> {
                isMultiDelete = true;
                isMultiDel.checkTouch(true);
                lstDiary.get(position).setSelected(true);
                notifyChange();
                return true;
            });

            viewItemShowAll.setOnClickListener(v -> {
                if (!isMultiDelete) callBack.callBackItem(diary, position);
                if (diary.isSelected()) {
                    diary.setSelected(false);
                    if (isTheme)
                        UtilsTheme.changeIconSelectDiary(context, viewItemShowAll.getIvTick(),
                                Color.parseColor(config.getColorMain()),
                                Color.parseColor(config.getColorMain()),
                                Color.parseColor(config.getColorIconInColor()),
                                false);
                    else viewItemShowAll.getIvTick().setImageResource(R.drawable.ic_un_tick_del);
                } else {
                    diary.setSelected(true);
                    if (isTheme)
                        UtilsTheme.changeIconSelectDiary(context, viewItemShowAll.getIvTick(),
                                Color.parseColor(config.getColorMain()),
                                Color.parseColor(config.getColorMain()),
                                Color.parseColor(config.getColorIconInColor()),
                                true);
                    else viewItemShowAll.getIvTick().setImageResource(R.drawable.ic_tick_del);
                }
            });
        }
    }

    public void unMultiDel() {
        isMultiDelete = false;
        for (int i = 0; i < lstDiary.size(); i++) lstDiary.get(i).setSelected(false);
        notifyChange();
    }

    public RealmList<DiaryModel> getSelected() {
        RealmList<DiaryModel> lstDiarySelected = new RealmList<>();
        for (int i = 0; i < lstDiary.size(); i++)
            if (lstDiary.get(i).isSelected()) lstDiarySelected.add(lstDiary.get(i));

        return lstDiarySelected;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void notifyChange() {
        notifyDataSetChanged();
    }
}
