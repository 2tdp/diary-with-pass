package com.note.mydiary.diarywithlock.journalwithlock.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.note.mydiary.diarywithlock.journalwithlock.R;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewpickpicture.ViewItemBucket;
import com.note.mydiary.diarywithlock.journalwithlock.callback.ICallBackItem;
import com.note.mydiary.diarywithlock.journalwithlock.model.picture.BucketPicModel;
import com.note.mydiary.diarywithlock.journalwithlock.sharepref.DataLocalManager;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Constant;

import java.util.ArrayList;

public class BucketAdapter extends RecyclerView.Adapter<BucketAdapter.BucketHolder> {


    private final Context context;
    private ArrayList<BucketPicModel> lstBucket;
    private final ICallBackItem callBack;

    public BucketAdapter(Context context, ICallBackItem callBack) {
        this.context = context;
        this.callBack = callBack;
        lstBucket = new ArrayList<>();
    }

    public void setData(ArrayList<BucketPicModel> lstBucket) {
        this.lstBucket = lstBucket;
        notifyChange();
    }

    @NonNull
    @Override
    public BucketHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int w = parent.getResources().getDisplayMetrics().widthPixels;
        ViewItemBucket viewItemBucket = new ViewItemBucket(parent.getContext());
        viewItemBucket.setLayoutParams(new ViewGroup.LayoutParams(-1, (int) (20.376f * w / 100)));
        return new BucketHolder(viewItemBucket);
    }

    @Override
    public void onBindViewHolder(@NonNull BucketHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        if (!lstBucket.isEmpty()) return lstBucket.size();
        return 0;
    }

    class BucketHolder extends RecyclerView.ViewHolder {

        ViewItemBucket viewItemBucket;

        public BucketHolder(@NonNull View itemView) {
            super(itemView);
            viewItemBucket = (ViewItemBucket) itemView;
            if (!DataLocalManager.getOption(Constant.THEME_APP).equals("default"))
                viewItemBucket.createThemeApp(context, "/theme_app/" + DataLocalManager.getOption(Constant.THEME_APP));
        }

        public void onBind(int position) {
            BucketPicModel bucket = lstBucket.get(position);
            if (bucket == null) return;

            viewItemBucket.setOnClickListener(v -> callBack.callBackItem(bucket, position));

            int index = 0;
            Glide.with(context)
                    .load(Uri.parse(bucket.getLstPic().get(index).getUri()))
                    .placeholder(R.drawable.ic_err)
                    .skipMemoryCache(false)
                    .into(viewItemBucket.getIvThumb());

            viewItemBucket.getViewNamePicture().getTvName().setText(bucket.getBucket());
            viewItemBucket.getViewNamePicture().getTvQuantity().setText(String.valueOf(bucket.getLstPic().size()));
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void notifyChange() {
        notifyDataSetChanged();
    }
}
