package mrtequila.bookworm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by Michal on 2017-09-19.
 */

public class BarCodeScannerActivity extends Activity implements ZXingScannerView.ResultHandler{
    private ZXingScannerView mScannerView;

    @Override
    public void onCreate(Bundle state){
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
    }

    @Override
    public void onResume(){
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause(){
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
        Intent intent = new Intent(this, Main2Activity.class);
        intent.putExtra("result", result.toString());
        System.out.println("++++++++++++++++++++++++++++ Barcode +++++++++++++++++++++++++++ " + result.toString());
        setResult(Activity.RESULT_OK, intent);
        super.finish();
    }
}
