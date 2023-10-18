package com.note.mydiary.diarywithlock.journalwithlock.fragment.vip;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.billingclient.api.SkuDetails;
import com.note.mydiary.diarywithlock.journalwithlock.R;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.vip.ViewVipTwo;
import com.note.mydiary.diarywithlock.journalwithlock.model.theme.ConfigAppThemeModel;
import com.note.mydiary.diarywithlock.journalwithlock.sharepref.DataLocalManager;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Constant;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Utils;
import com.note.mydiary.diarywithlock.journalwithlock.utils.UtilsTheme;
import com.note.remiads.a_pro.MyBilling;
import com.note.remiads.a_pro.MyBillingSubsResult;

public class VipTwoFragment extends Fragment {

    ViewVipTwo viewVipTwo;

    ConfigAppThemeModel config;

    private MyBilling myBilling;
    private String key;

    public VipTwoFragment() {
    }

    public static VipTwoFragment newInstance() {
        return new VipTwoFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewVipTwo = new ViewVipTwo(requireContext());
        if (!DataLocalManager.getOption(Constant.THEME_APP).equals("default")) {
            viewVipTwo.createTheme(requireContext(), "/theme_app/" + DataLocalManager.getOption(Constant.THEME_APP));
            String jsonConfig = Utils.readFromFile(requireContext(), "theme/theme_app/theme_app/" + DataLocalManager.getOption(Constant.THEME_APP) + "/config.txt");

            config = DataLocalManager.getConfigApp(jsonConfig);
        }
        key = getString(com.note.remiads.R.string.id_sub_month);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        evenClick();
        myBilling = new MyBilling(getActivity(), false, this::loadPrice);
        return viewVipTwo;
    }

    private void evenClick() {
        viewVipTwo.getIvExit().setOnClickListener(v -> {
            getParentFragmentManager().popBackStack(VipTwoFragment.class.getSimpleName(), 1);
            getParentFragmentManager().popBackStack(VipOneFragment.class.getSimpleName(), 1);
        });

        viewVipTwo.getViewItem1().setOnClickListener(v -> setUpChooseOption(0));
        viewVipTwo.getViewItem2().setOnClickListener(v -> setUpChooseOption(1));
        viewVipTwo.getViewItem3().setOnClickListener(v -> setUpChooseOption(2));
        viewVipTwo.getTvUpgrade().setOnClickListener(v -> myBilling.makePurchaseSubscription(key, new MyBillingSubsResult() {
            @Override
            public void onPurchasesDone() {
                new Handler().postDelayed(() -> {
                    if (getActivity() != null) {
                        getActivity().onBackPressed();
                    }
                }, 500);
            }

            @Override
            public void onPurchasesProcessing() {

            }

            @Override
            public void onPurchasesCancel() {

            }

            @Override
            public void onNotConnect() {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPurchaseError() {

            }
        }));
    }

    private void setUpChooseOption(int pos) {
        switch (pos) {
            case 0:
                key = getString(com.note.remiads.R.string.id_sub_year);
                if (!DataLocalManager.getOption(Constant.THEME_APP).equals("default") && config != null) {
                    UtilsTheme.changeIconSelectVip(requireContext(), viewVipTwo.getViewItem1().getIvChoose(),
                            Color.parseColor(config.getColorMain()), Color.parseColor(config.getColorUnSelect()), true);

                    UtilsTheme.changeIconSelectVip(requireContext(), viewVipTwo.getViewItem2().getIvChoose(),
                            Color.parseColor(config.getColorMain()), Color.parseColor(config.getColorUnSelect()), false);
                    UtilsTheme.changeIconSelectVip(requireContext(), viewVipTwo.getViewItem3().getIvChoose(),
                            Color.parseColor(config.getColorMain()), Color.parseColor(config.getColorUnSelect()), false);
                } else {
                    viewVipTwo.getViewItem1().getIvChoose().setImageResource(R.drawable.ic_selected);

                    viewVipTwo.getViewItem2().getIvChoose().setImageResource(R.drawable.ic_un_selected);
                    viewVipTwo.getViewItem3().getIvChoose().setImageResource(R.drawable.ic_un_selected);
                }
                break;
            case 1:
                key = getString(com.note.remiads.R.string.id_sub_month);
                if (!DataLocalManager.getOption(Constant.THEME_APP).equals("default") && config != null) {
                    UtilsTheme.changeIconSelectVip(requireContext(), viewVipTwo.getViewItem2().getIvChoose(),
                            Color.parseColor(config.getColorMain()), Color.parseColor(config.getColorUnSelect()), true);

                    UtilsTheme.changeIconSelectVip(requireContext(), viewVipTwo.getViewItem1().getIvChoose(),
                            Color.parseColor(config.getColorMain()), Color.parseColor(config.getColorUnSelect()), false);
                    UtilsTheme.changeIconSelectVip(requireContext(), viewVipTwo.getViewItem3().getIvChoose(),
                            Color.parseColor(config.getColorMain()), Color.parseColor(config.getColorUnSelect()), false);
                } else {
                    viewVipTwo.getViewItem2().getIvChoose().setImageResource(R.drawable.ic_selected);

                    viewVipTwo.getViewItem1().getIvChoose().setImageResource(R.drawable.ic_un_selected);
                    viewVipTwo.getViewItem3().getIvChoose().setImageResource(R.drawable.ic_un_selected);
                }
                break;
            case 2:
                key = getString(com.note.remiads.R.string.id_sub_week);
                if (!DataLocalManager.getOption(Constant.THEME_APP).equals("default") && config != null) {
                    UtilsTheme.changeIconSelectVip(requireContext(), viewVipTwo.getViewItem3().getIvChoose(),
                            Color.parseColor(config.getColorMain()), Color.parseColor(config.getColorUnSelect()), true);

                    UtilsTheme.changeIconSelectVip(requireContext(), viewVipTwo.getViewItem1().getIvChoose(),
                            Color.parseColor(config.getColorMain()), Color.parseColor(config.getColorUnSelect()), false);
                    UtilsTheme.changeIconSelectVip(requireContext(), viewVipTwo.getViewItem2().getIvChoose(),
                            Color.parseColor(config.getColorMain()), Color.parseColor(config.getColorUnSelect()), false);
                } else {
                    viewVipTwo.getViewItem3().getIvChoose().setImageResource(R.drawable.ic_selected);

                    viewVipTwo.getViewItem1().getIvChoose().setImageResource(R.drawable.ic_un_selected);
                    viewVipTwo.getViewItem2().getIvChoose().setImageResource(R.drawable.ic_un_selected);
                }
                break;
        }
    }

    private void loadPrice() {
        myBilling.getSkuList(arrPrice -> {
            if (getActivity() != null)
                getActivity().runOnUiThread(() -> {
                    for (SkuDetails skuDetails : arrPrice) {
                        if (skuDetails == null)
                            continue;
                        if (skuDetails.getSku().equals(getString(com.note.remiads.R.string.id_sub_year)))
                            viewVipTwo.getViewItem1().getTvPrice().setText("Then " + skuDetails.getPrice());
                        else if (skuDetails.getSku().equals(getString(com.note.remiads.R.string.id_sub_month)))
                            viewVipTwo.getViewItem2().getTvPrice().setText("Then " + skuDetails.getPrice());
                        else
                            viewVipTwo.getViewItem3().getTvPrice().setText("Then " + skuDetails.getPrice());
                    }
                });
        }, getString(com.note.remiads.R.string.id_sub_week), getString(com.note.remiads.R.string.id_sub_month), getString(com.note.remiads.R.string.id_sub_year));
    }
}