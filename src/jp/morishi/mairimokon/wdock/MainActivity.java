package jp.morishi.mairimokon.wdock;

import java.io.IOException;
import java.io.InputStream;

import jp.morishi.mairimokon.DataPicker;
import jp.morishi.mairimokon.Main;
import jp.morishi.mairimokon.data.MaiRimokonData;
import jp.morishi.mairimokon.data.SelectRimokon;
//import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Main {
    /** Called when the activity is first created. */
	private static final String RIMOKON_FILE = "walkmandock.mrxml";
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	int savedVersion = getSavedVersionCode();
    	int version = getVersionCode();
    	if(version != savedVersion)
    	{
    		updateRimokonData();
    		saveVersionCode(version);
    	}
    	
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main);
    }
    private int getVersionCode()
    {
    	int version = 0;
    	//最新のバージョン情報を取得する
		PackageInfo pi = null;
		try {
		    pi = getPackageManager().getPackageInfo(getPackageName(),
		            PackageManager.GET_META_DATA);
		    version = pi.versionCode;
		} catch (NameNotFoundException e) {
		    e.printStackTrace();
		}
		return version;
    }
    private int getSavedVersionCode()
    {
    	int version = 0;
    	//SharedPreferenceの設定
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        version = sp.getInt("VersionCode", version);
        return version;
    }
    private void saveVersionCode(int versionCode)
    {
    	//SharedPreferenceの設定
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("VersionCode",versionCode);
        editor.commit();        
    }
    private void updateRimokonData()
    {
        SelectRimokon selectRimokon = new SelectRimokon(this);
        if(selectRimokon.getSelectedDataInfo())
        {
			MaiRimokonData.deleteMaiRimokonData(this, selectRimokon.getRimokonNo());
        }
        AssetManager as = getResources().getAssets();   
        try {
			InputStream is = as.open(RIMOKON_FILE);
			MaiRimokonData rimokonData = DataPicker.createRimokonData(this, is);
			if(rimokonData != null)
			{
				rimokonData.save();
				SelectRimokon.select(this, null, rimokonData.getNo(), 0); 
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	return true;
    }
}