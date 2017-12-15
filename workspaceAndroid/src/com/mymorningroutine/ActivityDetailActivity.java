package com.mymorningroutine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import com.mymorningroutine.server.ActivityTable;
import com.mymorningroutine.utils.TimeUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityDetailActivity extends Activity {
    private static final String POSTPONE = "postpone" ;
    private static final String DURATION = "duration" ;
    private Integer itemId;
	private com.mymorningroutine.obj.Activity activity;
	private static final int SELECTED_PICTURE = 4;
	private static final int SELECTED_AUDIO = 5;
	private static final String TAG = "ActivityDetailActivity";
	private TextView textTitleView;

	private TextView textDescriptionView;

	private TextView textDurationView;

	private TextView textDaysView;

	private TextView textBackgroundView;

	private TextView textPostponeView;

	private TextView textVolumeView;

	private EditText editTextTitle;

	private EditText editTextDescription;

	// private NumberPicker editTextDuration;

	private NumberPicker npMinutesDuration;
	private NumberPicker npSecondsDuration;

	private NumberPicker npMinutesPostpone;
	private NumberPicker npSecondsPostpone;

	private ActivityTable activityTable;

	private TextView textAlarmView;

	private NumberPicker editTextPostpone;

	private SeekBar editSeekbarVolume;

	public String[] days;

	private boolean[] _selection;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayShowHomeEnabled(true);
		getActionBar().setDisplayUseLogoEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_activity_detail);
		activityTable = Singleton.getActivityTable(getApplicationContext());
		days = getResources().getStringArray(R.array.daysValues);
		Intent i = getIntent();
		Integer itemId = i.getIntExtra("activityId", 0);
		this.itemId = itemId;
		_selection = new boolean[days.length];
		if (itemId > 0) {

			activity = activityTable.getActivity(itemId);
		} else {
			activity = new com.mymorningroutine.obj.Activity();
		}

		setTitle(activity.getTitle());
		editTextTitle = new EditText(this);
		editTextDescription = new EditText(this);
		// editTextDuration = new NumberPicker(this);
		// editTextDuration.setMinValue(1);
		// editTextDuration.setMaxValue(360);
		editTextPostpone = new NumberPicker(this);
		editTextPostpone.setMinValue(1);
		editTextPostpone.setMaxValue(60);
		editSeekbarVolume = new SeekBar(this);
		editSeekbarVolume.setMax(100);
		editSeekbarVolume.setProgress(activity.getVolume());

		updateDetails();

		ContextThemeWrapper ctw = new ContextThemeWrapper(ActivityDetailActivity.this, R.style.defaultDialogTheme);
		final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctw);

		LinearLayout titleLAyout = (LinearLayout) findViewById(R.id.layout_title);
		alertDialogBuilder.setTitle(getResources().getString(R.string.activity_title));
		alertDialogBuilder.setView(editTextTitle).setPositiveButton(getResources().getString(R.string.action_save),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						textTitleView.setText(editTextTitle.getText());
						activity.setTitle(editTextTitle.getText().toString());
					}
				}).setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		final AlertDialog alertDialogTitle = alertDialogBuilder.create();
		titleLAyout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View p1) {

				alertDialogTitle.show();
			}
		});

		LinearLayout descriptionLAyout = (LinearLayout) findViewById(R.id.layout_description);
		alertDialogBuilder.setTitle(getResources().getString(R.string.activity_description));
		alertDialogBuilder.setView(editTextDescription).setCancelable(true).setPositiveButton(
				getResources().getString(R.string.action_save), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						textDescriptionView.setText(editTextDescription.getText());
						activity.setDescription(editTextDescription.getText().toString());
						// activityTable.updateActivity(activity);
					}
				}).setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		final AlertDialog alertDialogDescription = alertDialogBuilder.create();

		descriptionLAyout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View p1) {

				alertDialogDescription.show();
			}
		});

		LinearLayout durationLAyout = (LinearLayout) findViewById(R.id.layout_duration);

		LinearLayout durationView = (LinearLayout) View.inflate(this, R.layout.dialog_time_picker, null);
		alertDialogBuilder.setTitle(getResources().getString(R.string.activity_duration));
		alertDialogBuilder.setView(durationView).setCancelable(true);
		final AlertDialog alertDialogDuration = alertDialogBuilder.create();
		// alertDialogDuration.setContentView(R.layout.dialog_time_picker);

		npMinutesDuration = (NumberPicker) durationView.findViewById(R.id.numberPicker1);
		npSecondsDuration = (NumberPicker) durationView.findViewById(R.id.numberPicker2);
		alertDialogDuration.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.action_save),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// textDurationView.setText(String.valueOf(np.getValue())
						// + ":" + String.valueOf(np2.getValue()) );
						Integer seconds = npSecondsDuration.getValue() * 5;
						Integer minutes = npMinutesDuration.getValue() * 60;
						if (activity != null)
							activity.setDurationTime(minutes + seconds);

						textDurationView.setText(TimeUtils.getTimeFormat(minutes + seconds));

					}
				});
		// alertDialogDuration.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(Color.BLACK);

		alertDialogDuration.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.cancel),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

		// alertDialogDuration.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(Color.BLACK);
		npMinutesDuration.setMaxValue(59);
		npMinutesDuration.setMinValue(0);
		npSecondsDuration.setMaxValue((11));
		npSecondsDuration.setMinValue(0);
		String[] secondValues = new String[12];

		for (int j = 0; j < secondValues.length; j++) {
			String number = Integer.toString(j * 5);
			secondValues[j] = number.length() < 2 ? "0" + number : number;
		}

		npSecondsDuration.setDisplayedValues(secondValues);

		npSecondsDuration.setFormatter(new NumberPicker.Formatter() {
			@Override
			public String format(int p1) {
				String formattedValue = "";
				formattedValue = (p1 < 10 ? "0" + p1 : "" + p1);
				return formattedValue;
			}
		});

		durationLAyout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View p1) {
				updateTimeView(DURATION);
				alertDialogDuration.show();
			}

		});

		LinearLayout postponeLAyout = (LinearLayout) findViewById(R.id.layout_postpone_time);
		alertDialogBuilder.setTitle(getResources().getString(R.string.title_postpone_time));
		LinearLayout postponeView = (LinearLayout) View.inflate(this, R.layout.dialog_time_picker_postpone, null);
		alertDialogBuilder.setView(postponeView);

		final AlertDialog alertDialogPostpone = alertDialogBuilder.create();

		npMinutesPostpone = (NumberPicker) postponeView.findViewById(R.id.numberPicker1);
		npSecondsPostpone = (NumberPicker) postponeView.findViewById(R.id.numberPicker2);

		npMinutesPostpone.setMaxValue(59);
		npMinutesPostpone.setMinValue(0);
		npSecondsPostpone.setMaxValue((11));
		npSecondsPostpone.setMinValue(0);

		npSecondsPostpone.setDisplayedValues(secondValues);
		npSecondsPostpone.setFormatter(new NumberPicker.Formatter() {
			@Override
			public String format(int p1) {
				String formattedValue = "";
				formattedValue = (p1 < 10 ? "0" + p1 : "" + p1);
				return formattedValue;
			}
		});
		alertDialogPostpone.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.action_save),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Integer seconds = npSecondsPostpone.getValue() * 5;
						Integer minutes = npMinutesPostpone.getValue() * 60;
						if (activity != null)
							activity.setPostponeTime(minutes + seconds);

						textPostponeView.setText(TimeUtils.getTimeFormat(minutes + seconds));

					}
				});

		alertDialogPostpone.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.cancel),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

		postponeLAyout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View p1) {
				updateTimeView(POSTPONE);
				alertDialogPostpone.show();
			}
		});

		LinearLayout volumeLAyout = (LinearLayout) findViewById(R.id.layout_volume);
		alertDialogBuilder.setTitle(getResources().getString(R.string.activity_volume));
		alertDialogBuilder.setView(editSeekbarVolume).setCancelable(false).setPositiveButton(
				getResources().getString(R.string.action_save), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						textVolumeView.setText(editSeekbarVolume.getProgress() + "%");
						activity.setVolume(editSeekbarVolume.getProgress());
					}
				}).setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

		final AlertDialog alertDialogVolume = alertDialogBuilder.create();

		volumeLAyout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View p1) {

				alertDialogVolume.show();
			}
		});

		LinearLayout daysLAyout = (LinearLayout) findViewById(R.id.layout_days);
		alertDialogBuilder.setTitle(getResources().getString(R.string.activity_days)).setView(null)
				.setMultiChoiceItems(days, _selection, new DialogSelectionClickHandler())
				.setPositiveButton(getResources().getString(R.string.action_save), new DialogButtonClickHandler())
				.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

		final AlertDialog alertDialogDays = alertDialogBuilder.create();

		daysLAyout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View p1) {

				alertDialogDays.show();
			}
		});

		LinearLayout backgroundLayout = (LinearLayout) findViewById(R.id.layout_background);
		backgroundLayout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View p1) {
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
			//	intent.addCategory(Intent.CATEGORY_OPENABLE);
				startActivityForResult(intent, SELECTED_PICTURE);
			}

		});
		LinearLayout musicAlarmLayout = (LinearLayout) findViewById(R.id.layout_alarm);
		musicAlarmLayout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View p1) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);

              /*  intent.setType("audio/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);*/
				startActivityForResult(intent, SELECTED_AUDIO);
			}
		});

		Button buttonCancel = (Button) findViewById(R.id.button_cancel);
		buttonCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View p1) {
				setResult(2);
				finish();
			}

		});

		Button buttonSave = (Button) findViewById(R.id.button_save_activity);
		buttonSave.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View p1) {
				boolean saved = false;
				boolean controleOk = controleValues();
				if (controleOk) {
					if (activity.getId() > 0) {
						saved = activityTable.updateActivity(activity);
					} else {
						saved = activityTable.insertActivity(activity);

					}

					if (saved) {
						Toast.makeText(getApplicationContext(), getResources().getString(R.string.message_saved),
								Toast.LENGTH_SHORT).show();;
						Intent intent = new Intent();
						intent.putExtra("reload", true);
						setResult(2, intent);
						finish();
					} else {
						Toast.makeText(getApplicationContext(), getResources().getString(R.string.message_not_saved),
								Toast.LENGTH_SHORT).show();
					}
				}
			}

		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_detail, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * Control that the title of the activity is not empty
	 * 
	 * @return
	 */
	protected boolean controleValues() {
		boolean controleOk = true;
		if (editTextTitle.getText().toString().isEmpty()) {
			controleOk = false;
			AlertDialog.Builder builder = new AlertDialog.Builder(this);

			builder.setMessage(R.string.title_empty).setTitle(R.string.title_activities).setCancelable(false)
					.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					});
			AlertDialog alert = builder.create();
			alert.show();
		}
		return controleOk;
	}

    /**
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data == null || resultCode != Activity.RESULT_OK) {
			return;
		}
		Uri uri = data.getData();
		String TAG = "ActivityDetailActivity";
		Log.v(TAG, uri.getPath());
        String filePath = getPath(this, uri);
		switch (requestCode) {
		case SELECTED_PICTURE:

			int indexOfLastSlash = filePath.lastIndexOf("/");
			String fileName = filePath.substring(indexOfLastSlash);

				// Bitmap yourSelectedImage=BitmapFactory.decodeFile(filePath);
				// Drawable d=new BitmapDrawable(yourSelectedImage);
				textBackgroundView.setText(fileName);
				activity.setBackgroundFilePath(filePath);
				// activityTable.updateActivity(activity);


            break;
		case SELECTED_AUDIO:
			// String songTitle =
			// musiccursor.getString(musiccursor.getColumnIndex(
            File file = new File(filePath);

			textAlarmView.setText( file.getName());
			activity.setMusicFilePath(filePath);
			// activityTable.updateActivity(activity);

			break;

		default:
			break;
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		updateDetails();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		switch (menuItem.getItemId()) {
		case android.R.id.home:
			finish();
			break;

		}
		return super.onOptionsItemSelected(menuItem);

	}

	/**
	 * Get a file path from a Uri. This will get the the path for Storage Access
	 * Framework Documents, as well as the _data field for the MediaStore and
	 * other file-based ContentProviders.
	 *
	 * @param context The context.
	 * @param uri The Uri to query.
	 * @author paulburke
	 */
	public static String getPath(final Context context, final Uri uri) {

		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/" + split[1];
				}

				// TODO handle non-primary volumes
			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {

				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[] {
						split[1]
				};

				return getDataColumn(context, contentUri, selection, selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {

			// Return the remote address
			if (isGooglePhotosUri(uri))
				return uri.getLastPathSegment();

			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 *
	 * @param context The context.
	 * @param uri The Uri to query.
	 * @param selection (Optional) Filter used in the query.
	 * @param selectionArgs (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */
	public static String getDataColumn(Context context, Uri uri, String selection,
									   String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = {
				column
		};

		try {
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
					null);
			if (cursor != null && cursor.moveToFirst()) {
				final int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}


	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	public static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri.getAuthority());
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if (item.getItemId() == R.id.action_delete_activity) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ActivityDetailActivity.this);
			// set title
			alertDialogBuilder.setTitle(getResources().getString(R.string.activity_title));
			// set dialog message
			alertDialogBuilder.setCancelable(true).setMessage(R.string.confirmation_delete_activity)
					.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							// if this button is clicked, close
							// current activity
							// Delete activity
							Singleton.getActivityTable(getApplicationContext()).deleteActivity(activity.getId());
							finish();
						}
					}).setNegativeButton(getResources().getString(R.string.cancel),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									// if this button is clicked, just close
									// the dialog box and do nothing
									dialog.cancel();
								}
							});
			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();
			// show it
			alertDialog.show();

		}
		return super.onMenuItemSelected(featureId, item);
	}


	protected void updateTimeView(String field){
		int s = 0;
		int m = 0;
		if(POSTPONE.equals(field)){

			if (activity.getPostponeTime() != 0) {
				s = (activity.getPostponeTime() % 60) / 5 ;
				m = activity.getPostponeTime() / 60;
			}
			npMinutesPostpone.setValue(m);
			npSecondsPostpone.setValue(s);
		} else if (DURATION.equals(field)){
			if (activity.getDurationTime() != 0) {
				s = (activity.getDurationTime() % 60) / 5;
				m = activity.getDurationTime() / 60;
			}
			npMinutesDuration.setValue(m);
			npSecondsDuration.setValue(s);
		}

	}
	protected void updateDetails() {

		if (activity != null) {
			textTitleView = (TextView) findViewById(R.id.textTitle);
			textTitleView.setText(activity.getTitle());
			editTextTitle.setText(activity.getTitle());

			textDescriptionView = (TextView) findViewById(R.id.textDescription);
			textDescriptionView.setText(activity.getDescription());
			editTextDescription.setText(activity.getDescription());

			textDurationView = (TextView) findViewById(R.id.textDuration);
			// textDurationView.setText(activity.getDurationMins() + "
			// minutes");
			String viewDuration = TimeUtils.getTimeFormat(activity.getDurationTime());

			// editTextDuration.setCurrentMinute(activity.getDurationMins());
			textDurationView.setText(viewDuration);

			textPostponeView = (TextView) findViewById(R.id.textPostponeTime);
			// textPostponeView.setText(activity.getPostponeTime() + "
			// minutes");
			editTextPostpone.setValue(activity.getPostponeTime());
			textPostponeView.setText(TimeUtils.getTimeFormat(activity.getPostponeTime()));

			textDaysView = (TextView) findViewById(R.id.textDays);
			String days = "";
			List<Day> daysList = Arrays.asList(Day.values());
			for (Day day : activity.getDays()) {
				if (day != null) {
					days += Day.getNameLocale(getResources(), day) + ", ";
					_selection[daysList.indexOf(day)] = true;
				}
			}
			textDaysView.setText(days);

			textBackgroundView = (TextView) findViewById(R.id.textBackground);

			if (activity.getBackgroundFilePath() != null && !activity.getBackgroundFilePath().isEmpty()) {
				String filePath=activity.getBackgroundFilePath();
				int indexOfLastSlash = filePath.lastIndexOf("/");
				String fileName = filePath.substring(indexOfLastSlash);
				textBackgroundView.setText(fileName);
				Log.e("background", activity.getBackgroundFilePath());
			}

			textVolumeView = (TextView) findViewById(R.id.textVolume);
			textVolumeView.setText(activity.getVolume() + "%");
			editSeekbarVolume.setProgress(activity.getVolume());

			textAlarmView = (TextView) findViewById(R.id.textAlarm);
			if (activity.getMusicFilePath() != null && !activity.getMusicFilePath().isEmpty()) {

				Log.v("music", activity.getMusicFilePath());

			    File file = new File(activity.getMusicFilePath());

						textAlarmView.setText(
								file.getName());

			}

		}
	}

	public class DialogSelectionClickHandler implements DialogInterface.OnMultiChoiceClickListener {
		public void onClick(DialogInterface dialog, int clicked, boolean selected) {

		}
	}

	public class DialogButtonClickHandler implements DialogInterface.OnClickListener {
		public void onClick(DialogInterface dialog, int clicked) {
			switch (clicked) {
			case DialogInterface.BUTTON_POSITIVE:
				activity.getDays().clear();
				for (int i = 0; i < days.length; i++) {
					if (_selection[i]) {
						activity.getDays().add(Day.values()[i]);
					}

				}
				textDaysView.setText(Day.getTableTextFromDays(getResources(), activity.getDays()));
				break;
			}
		}
	}

}
