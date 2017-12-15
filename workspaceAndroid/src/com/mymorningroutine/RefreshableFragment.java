package com.mymorningroutine;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public abstract class RefreshableFragment extends Fragment{
	protected int textHelpId;
	protected  View rootView;
	protected boolean showHelpByDefault;

	protected SharedPreferences sharedPreferences;
	

	
		public RefreshableFragment(){
			super();
		}
		public abstract void refreshFragment();
	
		
	public void initiateViews(){
		setListView((ListView) rootView.findViewById(android.R.id.list));
		setTextHelp((TextView) rootView.findViewById(textHelpId));
		updateHTMLText();
		
	}
	public void switchView()
	{
		// TODO: Implement this method
		Log.e(this.getClass().toString(),"menu item selecte uid");
		if(View.INVISIBLE == getTextHelp().getVisibility()){
			showTextHelp();
				} else {
					showListView();
				}
		
	}
	
	protected void showTextHelp(){
		getTextHelp().setVisibility(View.VISIBLE);
		getListView().setVisibility(View.INVISIBLE);
		getListView().invalidateViews();
	}
	
	protected void showListView(){
		getTextHelp().setVisibility(View.INVISIBLE);
		getListView().setVisibility(View.VISIBLE);
		getListView().invalidateViews();
		getListView().refreshDrawableState();
		
	}
	
	protected abstract void setListView(ListView listView);
	
	protected abstract ListView getListView();
	protected void updateHTMLText(){
		
		getTextHelp().setText(Html.fromHtml(getTextHelp().getText().toString()));
	}
	
	protected abstract void setTextHelp(TextView textView);

	protected abstract TextView getTextHelp();
	
	protected abstract void addItems();
	
	}
