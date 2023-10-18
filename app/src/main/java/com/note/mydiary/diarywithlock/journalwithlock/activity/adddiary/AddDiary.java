package com.note.mydiary.diarywithlock.journalwithlock.activity.adddiary;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.note.mydiary.diarywithlock.journalwithlock.R;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewadddiary.ViewAddDiary;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewdialog.ViewDialogEmoji;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewdialog.ViewDialogTextDiary;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewdialog.ViewYesNo;
import com.note.mydiary.diarywithlock.journalwithlock.activity.base.BaseActivity;
import com.note.mydiary.diarywithlock.journalwithlock.activity.database.DatabaseRealm;
import com.note.mydiary.diarywithlock.journalwithlock.adapter.ContentDiaryAdapter;
import com.note.mydiary.diarywithlock.journalwithlock.adapter.EmojiAdapter;
import com.note.mydiary.diarywithlock.journalwithlock.callback.ICheckTouch;
import com.note.mydiary.diarywithlock.journalwithlock.customview.CustomCalendar.CalendarListener;
import com.note.mydiary.diarywithlock.journalwithlock.customview.CustomCalendar.CustomCalendarView;
import com.note.mydiary.diarywithlock.journalwithlock.data.DataEmoji;
import com.note.mydiary.diarywithlock.journalwithlock.fragment.pickpicture.PickPictureFragment;
import com.note.mydiary.diarywithlock.journalwithlock.fragment.pickpicture.PreviewPictureFragment;
import com.note.mydiary.diarywithlock.journalwithlock.fragment.record.RecordFragment;
import com.note.mydiary.diarywithlock.journalwithlock.model.EmojiModel;
import com.note.mydiary.diarywithlock.journalwithlock.model.diarymodel.AudioModel;
import com.note.mydiary.diarywithlock.journalwithlock.model.diarymodel.ContentModel;
import com.note.mydiary.diarywithlock.journalwithlock.model.diarymodel.DiaryModel;
import com.note.mydiary.diarywithlock.journalwithlock.model.picture.PicModel;
import com.note.mydiary.diarywithlock.journalwithlock.model.theme.ConfigAppThemeModel;
import com.note.mydiary.diarywithlock.journalwithlock.sharepref.DataLocalManager;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Constant;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Utils;
import com.note.mydiary.diarywithlock.journalwithlock.utils.UtilsBitmap;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.RealmList;

public class AddDiary extends BaseActivity {

    private ViewAddDiary viewAddDiary;
    private LinearLayout llDialogCalendar;
    private CustomCalendarView calendarView;
    private ViewYesNo viewYesNo;

    private PickPictureFragment pictureFragment;
    private ContentDiaryAdapter contentDiaryAdapter;

    private DiaryModel diaryModel;
    private ArrayList<EmojiModel> lstEmoji;
    private RealmList<ContentModel> lstContent;
    private Date dateSelected;
    private int idDiary, w, oldPosition;
    private boolean isFinish, isChange, isPlayAudio;
    private Uri photoUri;
    private Animation animation;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewAddDiary = new ViewAddDiary(this);
        viewAddDiary.setFitsSystemWindows(true);

        if (!DataLocalManager.getOption(Constant.THEME_APP).equals("default")) {
            viewAddDiary.createThemeApp(this, "/theme_app/" + DataLocalManager.getOption(Constant.THEME_APP));
            viewAddDiary.getViewToolbar().createTheme(this, "/theme_app/" + DataLocalManager.getOption(Constant.THEME_APP));
        }
        setContentView(viewAddDiary);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                List<Fragment> lstFragment = getSupportFragmentManager().getFragments();
                if (lstFragment.size() == 1) {
                    if (!isFinish) {
                        String title = viewAddDiary.getContentAddDiary().getWriteTitle().getEtContent().getText().toString();
                        ContentModel content = lstContent.get(0);
                        if (content != null)
                            if (!content.getContent().equals("")) isChange = true;

                        if (!title.equals("") || lstContent.size() != 1 || isChange)
                            clickSave();
                        else finish();
                    } else {
                        finish();
                        Utils.setAnimExit(AddDiary.this);
                    }
                } else {
                    if (pictureFragment != null)
                        if (pictureFragment.getViewPickPictureFragment().getRcvPicture().getVisibility() == View.VISIBLE)
                            pictureFragment.switchLayoutPicture(0);
                        else if (pictureFragment.getViewPickPictureFragment().getRcvBucket().getVisibility() == View.VISIBLE)
                            getSupportFragmentManager().popBackStack();
                        else getSupportFragmentManager().popBackStack();
                    else getSupportFragmentManager().popBackStack();
                }
            }
        });

        w = getResources().getDisplayMetrics().widthPixels;
        dateSelected = (Date) getIntent().getSerializableExtra("selectedDate");

        idDiary = DataLocalManager.getInt(Constant.KEY_ID_DIARY);

        if (idDiary != -1) initEdit();
        else init();

        evenClick();
    }

    private void init() {
        viewAddDiary.getViewToolbar().getTvTitle().setText(getResources().getString(R.string.write));
        //setDateCurrent
        SimpleDateFormat format = new SimpleDateFormat(Constant.FULL_DATE_DAY, Constant.LOCALE_DEFAULT);
        if (dateSelected != null)
            viewAddDiary.getContentAddDiary().getViewPickDate().getTvDate().setText(format.format(dateSelected.getTime()));
        else
            viewAddDiary.getContentAddDiary().getViewPickDate().getTvDate().setText(format.format(Calendar.getInstance(Constant.LOCALE_DEFAULT).getTime()));
        //addFile
        viewAddDiary.getViewOptionPic().setVisibility(View.GONE);

        diaryModel = new DiaryModel();
        lstContent = new RealmList<>();
        lstEmoji = DataEmoji.getEmoji(this);
        setUpCalendarDialog();

        upLoadDataContent(new ContentModel(-1, -1, "", new AudioModel(), new RealmList<>()));
    }

    @SuppressLint("SimpleDateFormat")
    private void initEdit() {
        viewAddDiary.getViewToolbar().getTvTitle().setText(getResources().getString(R.string.write));
        viewAddDiary.getViewOptionPic().setVisibility(View.GONE);
        viewAddDiary.getIvLoading().setVisibility(View.GONE);
        DatabaseRealm.getOtherDiary(idDiary, (o, pos) -> {
            diaryModel = (DiaryModel) o;
            lstContent = new RealmList<>();
            lstEmoji = DataEmoji.getEmoji(this);
            viewAddDiary.getContentAddDiary().getViewPickDate().getTvDate().setText(new SimpleDateFormat(Constant.FULL_DATE, Constant.LOCALE_DEFAULT).format(diaryModel.getDateTimeStamp()));
            setUpCalendarDialog();

            viewAddDiary.getContentAddDiary().getWriteTitle().getEtContent().setText(diaryModel.getTitleDiary());
            viewAddDiary.getContentAddDiary().getWriteTitle().getViewTitle().setLength(diaryModel.getTitleDiary().length());
            viewAddDiary.getContentAddDiary().getIvPickEmoji().setImageBitmap(UtilsBitmap.getBitmapFromAsset(this,
                    diaryModel.getEmojiModel().getFolder(), diaryModel.getEmojiModel().getNameEmoji()));

            for (ContentModel contentModel : diaryModel.getLstContent())
                upLoadDataContent(contentModel);
        });
    }

    private void evenClick() {
        viewAddDiary.getViewToolbar().getIvBack().setOnClickListener(v -> {
            if (idDiary != -1) clickSave();
            else onBackPressed(false, false);
        });
        viewAddDiary.setOnClickListener(v -> Utils.hideKeyboard(this, v));

        viewAddDiary.getContentAddDiary().getWriteTitle().getEtContent().setOnClickListener(v -> {
            if (viewAddDiary.getViewOptionPic().getVisibility() == View.VISIBLE) {
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
                viewAddDiary.getViewOptionPic().setAnimation(animation);
                viewAddDiary.getViewOptionPic().setVisibility(View.GONE);
            }
        });

        //addPic
        viewAddDiary.getIvPic().setOnClickListener(v -> {
            Utils.hideKeyboard(this, v);
            clickAddPic();
        });

        //addRecord
        viewAddDiary.getIvRec().setOnClickListener(v -> {
            Utils.hideKeyboard(this, v);
            clickAddRec();
        });

        //closeAddOption
        viewAddDiary.getViewOptionPic().getViewToolbar().setOnClickListener(v -> {
            if (viewAddDiary.getViewOptionPic().getVisibility() == View.VISIBLE) {
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
                viewAddDiary.getViewOptionPic().setAnimation(animation);
                viewAddDiary.getViewOptionPic().setVisibility(View.GONE);
            }
        });

        //addEmoji
        viewAddDiary.getContentAddDiary().getIvPickEmoji().setOnClickListener(this::clickAddEmoji);

        //addCalendar
        viewAddDiary.getContentAddDiary().getViewPickDate().setOnClickListener(v -> clickAddDate());

        //save
        viewAddDiary.getViewToolbar().getTvSave().setOnClickListener(v -> {
            if (idDiary != -1) clickSave();
            else saveDiary();
        });
    }

    private void clickSave() {
        ViewDialogTextDiary saveDiary = new ViewDialogTextDiary(this);
        AlertDialog dialog = new AlertDialog.Builder(this, R.style.SheetDialog).create();
        dialog.setView(saveDiary);
        dialog.setCancelable(true);
        dialog.show();

        saveDiary.getLayoutParams().width = (int) (84.444f * w / 100);

        saveDiary.getViewYesNo().getTvNo().setOnClickListener(v -> {
            dialog.cancel();
            isFinish = true;
            onBackPressed(false, false);
        });
        saveDiary.getViewYesNo().getTvYes().setOnClickListener(v -> {
            saveDiary();
            dialog.cancel();
        });
    }

    private void saveDiary() {
        viewAddDiary.getIvLoading().setVisibility(View.VISIBLE);

        String title = viewAddDiary.getContentAddDiary().getWriteTitle().getEtContent().getText().toString();

        diaryModel.setTitleDiary(title);

        if (diaryModel.getDateTimeStamp() == 0) {
            if (dateSelected == null)
                diaryModel.setDateTimeStamp(calendarView.getDayCurrent().getTime());
            else diaryModel.setDateTimeStamp(dateSelected.getTime());
        }

        if (diaryModel.getEmojiModel() == null) diaryModel.setEmojiModel(lstEmoji.get(0));

        diaryModel.setLstContent(lstContent);

        if (idDiary == -1)
            DatabaseRealm.insert(this, diaryModel, isDone -> {
                viewAddDiary.getIvLoading().setVisibility(View.GONE);
                isFinish = true;
                DataLocalManager.setCheck("resetCalendar", true);
                onBackPressed(false, false);
            });
        else
            DatabaseRealm.update(this, diaryModel, isDone -> {
                viewAddDiary.getIvLoading().setVisibility(View.GONE);
                isFinish = true;
                DataLocalManager.setCheck("resetCalendar", true);
                onBackPressed(false, false);
            });
    }

    private void clickAddDate() {
        setUpCalendarDialog();
        AlertDialog dialog = new AlertDialog.Builder(this, R.style.SheetDialog).create();
        dialog.setView(llDialogCalendar);
        dialog.setCancelable(false);
        dialog.show();

        llDialogCalendar.getLayoutParams().width = (int) (84.44f * w / 100);
        llDialogCalendar.getLayoutParams().height = (int) (100.5f * w / 100);

        viewYesNo.getTvNo().setOnClickListener(v -> {
            llDialogCalendar.removeAllViews();
            dialog.cancel();
        });
        viewYesNo.getTvYes().setOnClickListener(v -> {
            if (dateSelected == null) dateSelected = calendarView.getDayCurrent();
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat df = new SimpleDateFormat(Constant.FULL_DATE_DAY, Constant.LOCALE_DEFAULT);
            viewAddDiary.getContentAddDiary().getViewPickDate().getTvDate().setText(df.format(dateSelected));
            diaryModel.setDateTimeStamp(dateSelected.getTime());
            dialog.cancel();
        });
    }

    private void clickAddEmoji(View v) {
        int w = getResources().getDisplayMetrics().widthPixels;
        ViewDialogEmoji viewDialogEmoji = new ViewDialogEmoji(this);
        PopupWindow popUp = new PopupWindow(viewDialogEmoji, (int) (90f * w / 100), RelativeLayout.LayoutParams.WRAP_CONTENT, true);
        popUp.showAsDropDown(v, (int) (2.5f * w / 100) * -1, (int) (3.33f * w / 100));

        EmojiAdapter emojiAdapter = new EmojiAdapter(this, (o, pos) -> {
            EmojiModel emojiModel = (EmojiModel) o;
            viewAddDiary.getContentAddDiary().getIvPickEmoji().setImageBitmap(UtilsBitmap.getBitmapFromAsset(this, emojiModel.getFolder(), emojiModel.getNameEmoji()));
            diaryModel.setEmojiModel(emojiModel);
            popUp.dismiss();
        });

        if (!lstEmoji.isEmpty()) emojiAdapter.setData(lstEmoji);

        GridLayoutManager manager = new GridLayoutManager(this, 4);
        viewDialogEmoji.getRcvEmoji().setLayoutManager(manager);
        viewDialogEmoji.getRcvEmoji().setAdapter(emojiAdapter);
    }

    private void clickAddRec() {
        RecordFragment recordFragment = RecordFragment.newInstance((o, pos) -> {
            String fileDirAudio = (String) o;
            isChange = true;
            upLoadDataContent(new ContentModel(-1, idDiary, "", new AudioModel(-1, idDiary, fileDirAudio, null), new RealmList<>()));
        });
        replaceFragment(getSupportFragmentManager(), recordFragment, false, true, false);
    }

    @SuppressLint("QueryPermissionsNeeded")
    private void clickAddPic() {
        if (viewAddDiary.getViewOptionPic().getVisibility() == View.GONE) {
            animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
            viewAddDiary.getViewOptionPic().setAnimation(animation);
            viewAddDiary.getViewOptionPic().setVisibility(View.VISIBLE);
            viewAddDiary.getViewOptionPic().setFocusable(true);
            viewAddDiary.getViewOptionPic().setClickable(true);
        }

        viewAddDiary.getViewOptionPic().getViewOptionDetail().getOptionGallery().setOnClickListener(v -> {
            pictureFragment = PickPictureFragment.newInstance(false, (o, pos) -> {
                isChange = true;
                if (pos == -1)
                    upLoadDataContent(new ContentModel(-1, -1, "", new AudioModel(), ((RealmList<PicModel>) o)));
                else {
                    RealmList<PicModel> lstPic = new RealmList<>();
                    lstPic.add(((PicModel) o));
                    upLoadDataContent(new ContentModel(-1, -1, "", new AudioModel(), lstPic));
                }

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
                viewAddDiary.getViewOptionPic().setAnimation(animation);
                viewAddDiary.getViewOptionPic().setVisibility(View.GONE);
                pictureFragment = null;
            });
            replaceFragment(getSupportFragmentManager(), pictureFragment, false, true, true);
        });

        viewAddDiary.getViewOptionPic().getViewOptionDetail().getOptionCamera().setOnClickListener(v -> {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File photoFile;
            photoFile = createImageFile();
            if (photoFile != null) {
                photoUri = FileProvider.getUriForFile(this,
                        "com.note.mydiary.diarywithlock.journalwithlock",
                        photoFile);

                Dexter.withContext(this)
                        .withPermission(Manifest.permission.CAMERA)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                                startActivityForResult(takePictureIntent, Constant.REQUEST_IMAGE_CAPTURE);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();
            }
        });
    }

    private File createImageFile() {
        @SuppressLint("SimpleDateFormat")
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Constant.LOCALE_DEFAULT).format(new Date());
        String imageFileName = "REMI_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,
                    ".png",
                    storageDir
            );
            String currentPhotoPath = image.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    private void upLoadDataContent(ContentModel contentModel) {
        lstContent.add(contentModel);

        if (contentDiaryAdapter == null) {

            contentDiaryAdapter = new ContentDiaryAdapter(this,
                    isHide -> {
                        if (viewAddDiary.getViewOptionPic().getVisibility() == View.VISIBLE) {
                            animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
                            viewAddDiary.getViewOptionPic().setAnimation(animation);
                            viewAddDiary.getViewOptionPic().setVisibility(View.GONE);
                        }
                    },
                    (o, pos) -> {
                        PicModel picModel = (PicModel) o;
                        PreviewPictureFragment previewPictureFragment;
                        if (picModel.getUri().equals(""))
                            previewPictureFragment = PreviewPictureFragment.newInstance(picModel.getArrPic(), "");
                        else
                            previewPictureFragment = PreviewPictureFragment.newInstance(null, ((PicModel) o).getUri());
                        replaceFragment(getSupportFragmentManager(), previewPictureFragment, true, true, true);
                    },
                    (o, position, isDone) -> {

                        String uriAudio;
                        FileDescriptor fileDescriptor = null;

                        if (o instanceof String) uriAudio = (String) o;
                        else {
                            fileDescriptor = Utils.createFileDescriptorFromByteArray(this, (byte[]) o);
                            uriAudio = "";
                        }

                        if (uriAudio.equals("") && fileDescriptor == null) {
                            Toast.makeText(this, getResources().getString(R.string.cant_play_audio), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (isPlayAudio) {
                            handStop();
                            if (oldPosition != position) handPlay(uriAudio, fileDescriptor, isDone);
                            else isDone.checkTouch(true);
                        } else handPlay(uriAudio, fileDescriptor, isDone);

                        oldPosition = position;
                    },
                    (o, pos) -> {
                        ContentModel content = (ContentModel) o;
                        if (content != null) {
                            AudioModel audio = content.getAudio();
                            if (audio != null && !audio.getUriAudio().equals("")) {
                                File file = new File(audio.getUriAudio());
                                if (file.exists()) file.delete();

                                audio.setUriAudio("");
                            }

                            if (content.getContent().equals("")) lstContent.remove(content);
                        }
                        contentDiaryAdapter.notifyChange();
                    });

            contentDiaryAdapter.setData(lstContent);
        } else contentDiaryAdapter.setData(lstContent);

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        viewAddDiary.getContentAddDiary().getWriteContent().getRcvContent().setLayoutManager(manager);
        viewAddDiary.getContentAddDiary().getWriteContent().getRcvContent().setAdapter(contentDiaryAdapter);
    }

    private void setUpCalendarDialog() {
        llDialogCalendar = new LinearLayout(this);
        llDialogCalendar.setBackgroundResource(R.drawable.boder_dialog);
        llDialogCalendar.setGravity(Gravity.CENTER_HORIZONTAL);
        llDialogCalendar.setOrientation(LinearLayout.VERTICAL);

        calendarView = new CustomCalendarView(this);
        if (!DataLocalManager.getOption(Constant.THEME_APP).equals("default")) {
            String jsonConfig = Utils.readFromFile(this, "theme/theme_app/theme_app" + DataLocalManager.getOption(Constant.THEME_APP) + "/config.txt");

            ConfigAppThemeModel config = DataLocalManager.getConfigApp(jsonConfig);

            if (config != null)
                calendarView.setThemeBackgroundColor(
                        Color.parseColor("#00000000"),
                        Color.parseColor(config.getColorTextDate()),
                        Color.parseColor("#383838"),
                        Utils.createBackground(new int[]{Color.parseColor(config.getColorMain())}, (int) (1.5f * w / 100), -1, -1),
                        Utils.createBackground(new int[]{Color.parseColor(config.getColorDiaryCalendar())}, (int) (5f * w / 100), -1, -1));
        }

        calendarView.setCalendarListener(new CalendarListener() {
            @Override
            public void onDateSelected(Date date) {
                dateSelected = date;
            }

            @Override
            public void onMonthChanged(Date date) {
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat df = new SimpleDateFormat("MM-yyyy", Constant.LOCALE_DEFAULT);
            }
        });

        LinearLayout.LayoutParams paramsCalendar = new LinearLayout.LayoutParams((int) (74.167f * w / 100), (int) (78.5f * w / 100));
        paramsCalendar.setMargins(0, (int) (3.5f * w / 100), 0, 0);
        llDialogCalendar.addView(calendarView, paramsCalendar);

        viewYesNo = new ViewYesNo(this);
        LinearLayout.LayoutParams paramsYesNo = new LinearLayout.LayoutParams((int) (72.778f * w / 100), (int) (11.11f * w / 100));
        paramsYesNo.setMargins(0, (int) (3.5f * w / 100), 0, 0);
        llDialogCalendar.addView(viewYesNo, paramsYesNo);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            float[] size = UtilsBitmap.getImageSize(this, photoUri);
            float ratio = size[0] / size[1];

            PicModel picModel = new PicModel(-1, -1, -1, "", ratio, photoUri.toString(), new byte[]{}, false);
            RealmList<PicModel> lstPic = new RealmList<>();
            lstPic.add(picModel);
            isChange = true;
            upLoadDataContent(new ContentModel(-1, -1, "", new AudioModel(), lstPic));
        }
    }

    public void handStop() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        mediaPlayer = null;
        isPlayAudio = false;
    }

    private void handPlay(String uriAudio, FileDescriptor fileDescriptor, ICheckTouch isDone) {

        try {
            mediaPlayer = new MediaPlayer();
            if (uriAudio.equals("")) mediaPlayer.setDataSource(fileDescriptor);
            else mediaPlayer.setDataSource(uriAudio);

            mediaPlayer.setOnCompletionListener(mediaPlayer -> {
                Log.d("2tdp", "setOnCompletionListener: ");
                handStop();
                isDone.checkTouch(true);
            });

            mediaPlayer.setOnPreparedListener(mp -> {
                isPlayAudio = true;
                mediaPlayer.start();
            });

            mediaPlayer.setOnErrorListener((mp, what, extra) -> {
                Log.d("2tdp", "onError: " + what + "-" + extra);
                return false;
            });

            mediaPlayer.prepareAsync();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
