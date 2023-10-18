package com.note.mydiary.diarywithlock.journalwithlock.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.note.mydiary.diarywithlock.journalwithlock.R;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewtheme.ViewItemThemeLock;
import com.note.mydiary.diarywithlock.journalwithlock.callback.ICallBackItem;
import com.note.mydiary.diarywithlock.journalwithlock.model.theme.ThemeModel;
import com.note.mydiary.diarywithlock.journalwithlock.sharepref.DataLocalManager;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Constant;

import java.util.ArrayList;

public class ThemeAdapter extends RecyclerView.Adapter<ThemeAdapter.ThemeHolder> {

    private final Context context;
    private final ICallBackItem callBack;
    private ArrayList<ThemeModel> lstTheme;

    private final int w;

    public ThemeAdapter(Context context, ICallBackItem callBack) {
        this.context = context;
        this.callBack = callBack;

        lstTheme = new ArrayList<>();
        w = context.getResources().getDisplayMetrics().widthPixels;
    }

    public void setData(ArrayList<ThemeModel> lstTheme) {
        this.lstTheme = lstTheme;

        notifyChange();
    }

    @NonNull
    @Override
    public ThemeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ThemeHolder(new CardView(context));
    }

    @Override
    public void onBindViewHolder(@NonNull ThemeHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        if (!lstTheme.isEmpty()) return lstTheme.size();
        return 0;
    }

    class ThemeHolder extends RecyclerView.ViewHolder {

        ViewItemThemeLock viewItemThemeLock;

        public ThemeHolder(@NonNull CardView itemView) {
            super(itemView);
            itemView.setCardElevation(2f * w / 100);
            itemView.setRadius(4f * w / 100);

            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams((int) (26.389f * w / 100), (int) (36.11f * w / 100));
            params.setMargins((int) (2.278f * w / 100), (int) (5.556f * w / 100), (int) (2.278f * w / 100), (int) (5.556f * w / 100));
            itemView.setLayoutParams(params);

            viewItemThemeLock = new ViewItemThemeLock(context);
            itemView.addView(viewItemThemeLock);
        }

        public void onBind(int position) {
            ThemeModel theme = lstTheme.get(position);
            if (theme == null) return;

            if (theme.isOnl()) {
                viewItemThemeLock.getIvChoose().setVisibility(View.GONE);
                viewItemThemeLock.getIvRule().setVisibility(View.VISIBLE);
                switch (theme.getPrice()) {
                    case "vip":
                        viewItemThemeLock.getIvRule().setImageResource(R.drawable.ic_theme_vip);
                        break;
                    case "free":
                        viewItemThemeLock.getIvRule().setImageResource(R.drawable.ic_theme_free);
                        break;
                    default:
                        viewItemThemeLock.getIvRule().setVisibility(View.GONE);
                }
            } else {
                viewItemThemeLock.getIvRule().setVisibility(View.GONE);
                if (theme.getName().equals("default") && DataLocalManager.getOption(Constant.THEME_LOCK).equals("default"))
                    viewItemThemeLock.getIvChoose().setVisibility(View.VISIBLE);
                else if (theme.isSelected())
                    viewItemThemeLock.getIvChoose().setVisibility(View.VISIBLE);
                else viewItemThemeLock.getIvChoose().setVisibility(View.GONE);
            }

            if (theme.getName().equals("default"))
                Glide.with(context)
                        .load(Uri.parse(theme.getPreview()))
                        .into(viewItemThemeLock.getIv());
            else
                Glide.with(context)
                        .load(theme.getPreview())
                        .placeholder(R.drawable.ic_err_theme_lock)
                        .into(viewItemThemeLock.getIv());

            if (!theme.getName().equals("default")) {
                if (DataLocalManager.getOption(Constant.PASS_QUESTION_ONE).equals("") || DataLocalManager.getOption(Constant.PASS_QUESTION_TWO).equals(""))
                    viewItemThemeLock.getIvNotSuitable().setVisibility(View.VISIBLE);
                else if (DataLocalManager.getInt(Constant.TYPE_LOCK) == 0)
                    viewItemThemeLock.getIvNotSuitable().setVisibility(theme.getName().contains("pincode") ? View.GONE : View.VISIBLE);
                else if (DataLocalManager.getInt(Constant.TYPE_LOCK) == 2)
                    viewItemThemeLock.getIvNotSuitable().setVisibility(theme.getName().contains("pattern") ? View.GONE : View.VISIBLE);
                else viewItemThemeLock.getIvNotSuitable().setVisibility(View.VISIBLE);
            } else viewItemThemeLock.getIvNotSuitable().setVisibility(View.GONE);


            itemView.setOnClickListener(v -> callBack.callBackItem(theme, position));
        }
    }

    public void setSelected(ThemeModel themeSelected) {
        for (ThemeModel theme : lstTheme)
            theme.setSelected(theme.getName().equals(themeSelected.getName()));

        notifyChange();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void notifyChange() {
        notifyDataSetChanged();
    }
}
