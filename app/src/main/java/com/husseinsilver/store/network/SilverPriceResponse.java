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

    /** Returns the XAG (silver ounce) price in USD. */
    public double getSilverPriceUSD() {
        if (rates != null && rates.containsKey("XAG")) {
            double xagRate = rates.get("XAG");
            if (xagRate != 0) {
                return 1.0 / xagRate;
            }
        }
        return 0.0;
    }

    /** Returns the USD→ILS exchange rate. */
    public double getUsdToIlsRate() {
        if (rates != null && rates.containsKey("ILS")) {
            return rates.get("ILS");
        }
        return 0.0;
    }

    /** Returns the XAG (silver ounce) price in ILS. */
    public double getSilverPriceILS() {
        double usdPrice = getSilverPriceUSD();
        double ilsRate = getUsdToIlsRate();
        if (usdPrice > 0 && ilsRate > 0) {
            return usdPrice * ilsRate;
        }
        return 0.0;
    }
}
