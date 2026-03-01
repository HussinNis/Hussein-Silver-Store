package com.husseinsilver.store.network;

import com.google.gson.annotations.SerializedName;
import java.util.Map;

public class SilverPriceResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("timestamp")
    private long timestamp;

    @SerializedName("base")
    private String base;

    @SerializedName("rates")
    private Map<String, Double> rates;

    public boolean isSuccess() { return success; }
    public long getTimestamp() { return timestamp; }
    public String getBase() { return base; }
    public Map<String, Double> getRates() { return rates; }

    public double getSilverPriceUSD() {
        if (rates != null && rates.containsKey("XAG")) {
            double xagRate = rates.get("XAG");
            if (xagRate != 0) {
                return 1.0 / xagRate;
            }
        }
        return 0.0;
    }
}
