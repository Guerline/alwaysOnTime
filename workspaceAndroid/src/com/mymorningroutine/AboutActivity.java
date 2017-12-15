package com.mymorningroutine;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.Builder;
import com.google.android.gms.security.ProviderInstaller;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AboutActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayShowHomeEnabled(true);
		getActionBar().setDisplayUseLogoEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_about);
		TextView textHelpTitle = (TextView)findViewById(R.id.activity_aboutTextView_title);
		textHelpTitle.setText(Html.fromHtml(getResources().getString(R.string.activity_about_title)));

		TextView textHelp = (TextView)findViewById(R.id.activity_aboutTextView);
		textHelp.setText(Html.fromHtml(getResources().getString( R.string.help_text)));
		TextView textHelp1 = (TextView)findViewById(R.id.activity_aboutTextView1);
		textHelp1.setText(Html.fromHtml(getResources().getString( R.string.help_text1)));
		ImageView imageView1 = (ImageView)findViewById(R.id.activity_aboutImageView1);
		imageView1.setImageResource(R.drawable.about_activities_view);
		ImageView imageView12 = (ImageView)findViewById(R.id.activity_aboutImageView12);
		imageView12.setImageResource(R.drawable.about_activity_detail_view);

		TextView textHelp2 = (TextView)findViewById(R.id.activity_aboutTextView2);

		textHelp2.setText(Html.fromHtml(getResources().getString( R.string.help_text2)));










		// Place the ad view.
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	//	adContainer.addView(adView, params);

		ImageView imageView2 = (ImageView)findViewById(R.id.activity_aboutImageView2);
		imageView2.setImageResource(R.drawable.about_routines_view);

		TextView textHelp3 = (TextView)findViewById(R.id.activity_aboutTextView3);
		textHelp3.setText(Html.fromHtml(getResources().getString( R.string.help_text3)));
		ImageView imageView31 = (ImageView)findViewById(R.id.activity_aboutImageView31);
		imageView31.setImageResource(R.drawable.about_routine_choice);
		ImageView imageView32 = (ImageView)findViewById(R.id.activity_aboutImageView32);
		imageView32.setImageResource(R.drawable.about_current_routine);

		TextView textHelp4 = (TextView)findViewById(R.id.activity_aboutTextView4);
		textHelp4.setText(Html.fromHtml(getResources().getString( R.string.help_text4)));
		ImageView imageView41 = (ImageView)findViewById(R.id.activity_aboutImageView41);
		imageView41.setImageResource(R.drawable.about_activity_status_countdown);
		ImageView imageView42 = (ImageView)findViewById(R.id.activity_aboutImageView42);
		imageView42.setImageResource(R.drawable.about_activity_status_time);

		TextView textHelp5 = (TextView)findViewById(R.id.activity_aboutTextView5);
		textHelp5.setText(Html.fromHtml(getResources().getString( R.string.help_text5)));
		ImageView imageView51 = (ImageView)findViewById(R.id.activity_aboutImageView51);
		imageView51.setImageResource(R.drawable.about_activity_dring);
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
