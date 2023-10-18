package com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewpasscode.patternview.customView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.ColorInt;

import com.note.mydiary.diarywithlock.journalwithlock.R;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewpasscode.patternview.bean.CellBean;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewpasscode.patternview.bean.CellFactory;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewpasscode.patternview.config.Config;
import com.note.mydiary.diarywithlock.journalwithlock.model.theme.ConfigAppThemeModel;
import com.note.mydiary.diarywithlock.journalwithlock.sharepref.DataLocalManager;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Constant;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */

public class PatternLockerView extends View {
    private static final String TAG = "PatternLockerView";

    static int w;

    private @ColorInt
    int normalColor;
    private @ColorInt
    int hitColor;
    private @ColorInt
    int errorColor;
    private @ColorInt
    int fillColor;
    private float lineWidth;
    private boolean enableAutoClean;

    private float endX;
    private float endY;

    private int hitSize;
    private boolean isError;
    private List<CellBean> cellBeanList;
    private List<Integer> hitList;
    private OnPatternChangeListener listener;

    private ILockerLinkedLineView linkedLineView;
    private INormalCellView normalCellView;
    private IHitCellView hitCellView;

    public PatternLockerView(Context context) {
        super(context);
        init(context);
    }

    public void setOnPatternChangedListener(OnPatternChangeListener listener) {
        this.listener = listener;
    }

    public void updateStatus(boolean isError) {
        this.isError = isError;
        postInvalidate();
    }

    public void clearHitState() {
        clearHitData();
        this.isError = false;
        if (this.listener != null) this.listener.onClear(this);

        postInvalidate();
    }

    public int getNormalColor() {
        return normalColor;
    }

    public PatternLockerView setNormalColor(int normalColor) {
        this.normalColor = normalColor;
        return this;
    }

    public int getHitColor() {
        return hitColor;
    }

    public PatternLockerView setHitColor(int hitColor) {
        this.hitColor = hitColor;
        return this;
    }

    public int getErrorColor() {
        return errorColor;
    }

    public PatternLockerView setErrorColor(int errorColor) {
        this.errorColor = errorColor;
        return this;
    }

    public int getFillColor() {
        return fillColor;
    }

    public PatternLockerView setFillColor(int fillColor) {
        this.fillColor = fillColor;
        return this;
    }

    public float getLineWidth() {
        return lineWidth;
    }

    public PatternLockerView setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
        return this;
    }

    public ILockerLinkedLineView getLinkedLineView() {
        return linkedLineView;
    }

    public PatternLockerView setLinkedLineView(ILockerLinkedLineView linkedLineView) {
        this.linkedLineView = linkedLineView;
        return this;
    }

    public INormalCellView getNormalCellView() {
        return normalCellView;
    }

    public PatternLockerView setNormalCellView(INormalCellView normalCellView) {
        this.normalCellView = normalCellView;
        return this;
    }

    public IHitCellView getHitCellView() {
        return hitCellView;
    }

    public PatternLockerView setHitCellView(IHitCellView hitCellView) {
        this.hitCellView = hitCellView;
        return this;
    }

    public void buildWithDefaultStyle() {
        this.setNormalCellView(new DefaultLockerNormalCustomCellView()
                .setNormalColor(this.getNormalColor())
                .setFillColor(this.getFillColor())
                .setLineWidth(this.getLineWidth())
        ).setHitCellView(new DefaultLockerHitCellView()
                .setHitColor(this.getHitColor())
                .setErrorColor(this.getErrorColor())
                .setFillColor(this.getFillColor())
                .setLineWidth(this.getLineWidth())
                .setNormalColor(this.getNormalColor())
        ).setLinkedLineView(new DefaultLockerLinkedLineView()
                .setNormalColor(this.getNormalColor())
                .setErrorColor(this.getErrorColor())
                .setLineWidth(this.getLineWidth())
                .setHitColor(this.getHitColor())
        ).build();
    }

    public void build() {
        if (getNormalCellView() == null) {
            Log.e(TAG, "build(), normalCellView is null");
            return;
        }

        if (getHitCellView() == null) {
            Log.e(TAG, "build(), hitCellView is null");
            return;
        }

        if (getLinkedLineView() == null) {
            Log.w(TAG, "build(), linkedLineView is null");
        }

        postInvalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int a = Math.min(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(a, a);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.cellBeanList == null)
            this.cellBeanList = new CellFactory(getWidth(), getHeight()).getCellBeanList();
        drawLinkedLine(canvas);
        drawCells(canvas);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) return super.onTouchEvent(event);

        boolean isHandle = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                handleActionDown(event);
                isHandle = true;
                break;
            case MotionEvent.ACTION_MOVE:
                handleActionMove(event);
                isHandle = true;
                break;
            case MotionEvent.ACTION_UP:
                handleActionUp(event);
                isHandle = true;
                break;
            default:
                break;
        }
        postInvalidate();
        return isHandle || super.onTouchEvent(event);
    }

    private void init(Context context) {
        this.initAttrs(context);
        this.initData();
    }

    private void initAttrs(Context context) {

        w = getResources().getDisplayMetrics().widthPixels;

        this.normalColor = getResources().getColor(R.color.gray_2);
        this.hitColor = getResources().getColor(R.color.orange);

        this.errorColor = getResources().getColor(R.color.red);
        this.fillColor = getResources().getColor(R.color.orange_2);
        this.lineWidth = 1.11f * w / 100;
        this.enableAutoClean = false;

        if (!DataLocalManager.getOption(Constant.THEME_APP).equals("default")) {
            String jsonConfig = Utils.readFromFile(context, "theme/theme_app/theme_app/" + DataLocalManager.getOption(Constant.THEME_APP) + "/config.txt");

            ConfigAppThemeModel config = DataLocalManager.getConfigApp(jsonConfig);
            if (config == null) return;

            this.hitColor = Color.parseColor(config.getColorMain());
            this.fillColor = Color.parseColor(config.getColorMainLight());
        }

        this.setNormalColor(this.normalColor);
        this.setHitColor(this.hitColor);
        this.setErrorColor(this.errorColor);
        this.setFillColor(this.fillColor);
        this.setLineWidth(this.lineWidth);
    }

    private void initData() {
        this.hitList = new ArrayList<>();
        this.buildWithDefaultStyle();
    }

    private void drawLinkedLine(Canvas canvas) {
        if ((this.hitList != null) && !this.hitList.isEmpty() && (getLinkedLineView() != null))
            getLinkedLineView().draw(canvas,
                    this.hitList,
                    this.cellBeanList,
                    this.endX,
                    this.endY,
                    this.isError);
    }

    private void drawCells(Canvas canvas) {
        if (getHitCellView() == null) {
            Log.e(TAG, "drawCells(), hitCellView is null");
            return;
        }

        if (getNormalCellView() == null) {
            Log.e(TAG, "drawCells(), normalCellView is null");
            return;
        }

        for (int i = 0; i < this.cellBeanList.size(); i++) {
            final CellBean item = this.cellBeanList.get(i);
            if (item.isHit) getHitCellView().draw(canvas, item, this.isError);
            else getNormalCellView().draw(canvas, item);
        }
    }

    private void handleActionDown(MotionEvent event) {
        //1. reset to default state
        clearHitData();

        //2. update hit state
        updateHitState(event);

        //3. notify listener
        if (this.listener != null) this.listener.onStart(this);
    }

    private void handleActionMove(MotionEvent event) {
        //1. update hit state
        updateHitState(event);

        //2. update end point
        this.endX = event.getX();
        this.endY = event.getY();

        //3. notify listener if needed
        final int size = this.hitList.size();
        if ((this.listener != null) && (this.hitSize != size)) {
            this.hitSize = size;
            this.listener.onChange(this, this.hitList);
        }
    }

    private void handleActionUp(MotionEvent event) {
        //1. update hit state
        updateHitState(event);
        this.endX = 0;
        this.endY = 0;

        //2. notify listener
        if (this.listener != null) this.listener.onComplete(this, this.hitList);

        //3. startTimer if needed
        if (this.enableAutoClean && this.hitList.size() > 0) startTimer();
    }

    private void updateHitState(MotionEvent event) {
        final float x = event.getX();
        final float y = event.getY();
        for (CellBean c : this.cellBeanList) {
            if (!c.isHit && c.of(x, y)) {
                c.isHit = true;
                this.hitList.add(c.id);
            }
        }
    }

    private void clearHitData() {
        for (int i = 0; i < this.hitList.size(); i++)
            this.cellBeanList.get(hitList.get(i)).isHit = false;

        this.hitList.clear();
        this.hitSize = 0;
    }

    private final Runnable action = () -> {
        setEnabled(true);
        clearHitState();
    };

    @Override
    protected void onDetachedFromWindow() {
        this.setOnPatternChangedListener(null);
        this.removeCallbacks(this.action);
        super.onDetachedFromWindow();
    }

    public List<Integer> getHitList() {
        return hitList;
    }

    private void startTimer() {
        setEnabled(false);
        this.postDelayed(this.action, Config.getDefaultDelayTime());
    }
}