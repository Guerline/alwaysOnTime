package com.mymorningroutine;
import java.util.Map;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.widget.TextView;

public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener
{
	private SharedPreferences sharedPreferences;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		
		sharedPreferences.registerOnSharedPreferenceChangeListener(this);
		Map<String,?> mapPref = sharedPreferences.getAll();
		for(Map.Entry<String,?> entry : mapPref.entrySet()){
			Preference pref = findPreference(entry.getKey());
			
			
			updateSummary(pref);
		
		}
		
	}

	private void updateSummary(Preference pref)
	{
		
		if (pref instanceof ListPreference) {
			ListPreference listPref = (ListPreference) pref;
			pref.setSummary(listPref.getEntry());
			pref.setDefaultValue("1");
			
		} else if(pref instanceof EditTextPreference){
			EditTextPreference editPref = (EditTextPreference) pref;
			pref.setSummary(editPref.getText());
		} else if(pref instanceof CheckBoxPreference){
			CheckBoxPreference checkPref = (CheckBoxPreference) pref;
		}
		
	}
	
	

	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		Preference pref = findPreference(key);
		updateSummary(pref);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem)
	{
		switch (menuItem.getItemId()) {
			case android.R.id.home:
				finish();
				break;

		}
		return super.onOptionsItemSelected(menuItem);

	}
	
}
