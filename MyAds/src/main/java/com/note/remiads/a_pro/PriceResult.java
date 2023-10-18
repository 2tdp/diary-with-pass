package com.note.remiads.a_pro;

import com.android.billingclient.api.SkuDetails;

import java.util.List;

public interface PriceResult {

    void onListPrice(List<SkuDetails> arrPrice);

}
