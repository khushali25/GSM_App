package com.example.xps.barcodescanner;

import android.content.Context;

import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.barcode.Barcode;

/**
 * Factory for creating a tracker and associated graphic to be associated with a new barcode.  The
 * multi-processor uses this factory to create barcode trackers as needed -- one for each barcode.
 */
class BarcodeTrackerFactory implements MultiProcessor.Factory<Barcode> {
    private GraphicOverlay<BarcodeGraphic> mGraphicOverlay;
    private Context mContext;
    private BarcodeGraphicTracker.BarcodeUpdateListener mBarcodeUpdateListener;

    public BarcodeTrackerFactory(GraphicOverlay<BarcodeGraphic> mGraphicOverlay,
                                 BarcodeGraphicTracker.BarcodeUpdateListener mBarcodeUpdateListener) {
        this.mGraphicOverlay = mGraphicOverlay;
        this.mBarcodeUpdateListener = mBarcodeUpdateListener;
    }

    @Override
    public Tracker<Barcode> create(Barcode barcode) {
        BarcodeGraphic graphic = new BarcodeGraphic(mGraphicOverlay);
        BarcodeGraphicTracker tracker = new BarcodeGraphicTracker(mGraphicOverlay, graphic);
        if (mBarcodeUpdateListener != null) tracker.setListener(mBarcodeUpdateListener);
        return tracker;
        //return new BarcodeGraphicTracker(mGraphicOverlay, graphic, mContext);
    }

}

