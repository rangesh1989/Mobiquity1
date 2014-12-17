package com.example;

import java.io.File;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.android.AuthActivity;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session.AccessType;
import com.dropbox.client2.session.TokenPair;

public class MainActivity extends Activity implements API_Listener, OnClickListener{

	File[] listFile;
	ArrayList<String> f = new ArrayList<String>();// list of file paths

	/* Replace this with your app key and secret assigned by Dropbox.
	 *  Note that this is a really insecure way to do this, and you shouldn't
	 *   ship code which contains your key & secret in such an obvious way.
	 *   Obfuscation is good.
	 */
	
	final static String APP_KEY = "pqbg0g2kq4f9f0w";
	final static String APP_SECRET = "j2zn2v7miisnc0r";
	
	
	public static final String OVERRIDEMSG = "File name with this name already exists.Do you want to replace this file?";
	final static AccessType ACCESS_TYPE = AccessType.DROPBOX;

	// You don't need to change these, leave them alone.
	final static String ACCOUNT_PREFS_NAME = "prefs";
	final static String ACCESS_KEY_NAME = "ACCESS_KEY";
	final static String ACCESS_SECRET_NAME = "ACCESS_SECRET";
	
	private final static String FILE_DIR = "";
	
	DropboxAPI<AndroidAuthSession> mApi;
	static boolean mLoggedIn;
	
	

	//........ Android widgets...............
	
	private Button btn_linkDropbox,btnDownload,btn_upload;
	private final String PHOTO_DIR = "";

	int count = 0;

	private String fileName;

	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
				super.onCreate(savedInstanceState);
				AndroidAuthSession session = buildSession();
				mApi = new DropboxAPI<AndroidAuthSession>(session);
		
		
				setContentView(R.layout.activity_main);
		
				
				checkAppKeySetup();
				
				// Display the proper UI state if logged in or not
				
				setLoggedIn(mApi.getSession().isLinked());
				getFromSdcard();
				
				
				btn_linkDropbox = (Button) findViewById(R.id.login);
				
				btn_linkDropbox.setOnClickListener(this);
				btn_upload =(Button) findViewById(R.id.upload);
				btn_upload.setOnClickListener(this);
				
				btnDownload = (Button) findViewById(R.id.display);
				btnDownload.setOnClickListener(this);
				}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch(v.getId())
		{
		case R.id.login:
		{
			
		if(isInternetConnected() || mLoggedIn)
			{
			logInScreen();
			}	
			else
			{
				showToast("Please Check Your Internet Connection.");	
			}
		}
			break;

		case R.id.upload:
		{

			if (mLoggedIn) {
				startImageCaptureActivity();					
			}
			else
			{
				showToast("Please Login To Dropbox");
			}
		}
			break;

		case R.id.display:
		{
		if(isInternetConnected())
		{
			if (mLoggedIn){
				
				startDropboxFileDisplayActivity();
			}
			else
			{
				showToast("Please Login To Dropbox");
			}
			}
		else
			{
			showToast("Please Check Your Internet Connection.");	
			}	
		}
		
			break;
			
		}
	}
	
	@Override
	protected void onResume()
	{
			super.onResume();
			AndroidAuthSession session = mApi.getSession();
	
			// The next part must be inserted in the onResume() method of the
			// activity from which session.startAuthentication() was called, so
			// that Dropbox authentication completes properly.
			
			if (session.authenticationSuccessful()) {
				try {
					// Mandatory call to complete the auth
					session.finishAuthentication();
	
					// Store it locally in our app for later use
					TokenPair tokens = session.getAccessTokenPair();
					storeKeys(tokens.key, tokens.secret);
					setLoggedIn(true);
				} catch (IllegalStateException e) {
					showToast("Couldn't authenticate with Dropbox:"
							+ e.getLocalizedMessage());
	
				}
			}
	}

	/*
	 * interface to verify the upload of file in Dropbox
	 * @see com.example.API_Listener#onSuccess(int, java.lang.Object)
	 */
	
	@Override
	public void onSuccess(int requestnumber, Object obj)
	{	
		try {
			if (requestnumber == Constants.UploadPhotos_Code) {
				Log.d("upload_fix", "1");
				boolean sucess = (Boolean) obj;
				if (sucess) {
					Log.d("upload_fix", "2");
					Toast.makeText(MainActivity.this,
							"File uploaded successfully", Toast.LENGTH_LONG)
							.show();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	
		@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == 110) {
			if (resultCode == RESULT_OK) {
				Log.d("dropbox", "we are here!");
				fileName = data.getStringExtra("file");
				Log.d("dropbox", fileName);
				File mFile = new File(fileName);
				if(!mFile.exists()){
					Log.d("dropbox","mFile does not exist");
				}
				File[] mFiles = { mFile };
				Log.d("dropbox", mFiles.length+"");
				Upload upload = new Upload(Constants.UploadPhotos_Code,
						MainActivity.this, mApi, PHOTO_DIR, mFiles);
				
				upload.execute();
			}
		}
		
			}
	

		
	//...................PRIVATE HELPER METHODS ................................
	
	
	
	
	
	/**
	 * Shows keeping the access keys returned from Trusted Authenticator in a
	 * local store, rather than storing user name & password, and
	 * re-authenticating each time (which is not to be done, ever).
	 */
		
		
	private void storeKeys(String key, String secret) {
		// Save the access key for later
		SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
		Editor edit = prefs.edit();
		edit.putString(ACCESS_KEY_NAME, key);
		edit.putString(ACCESS_SECRET_NAME, secret);
		edit.commit();
	}
	@Override
	public void onFail(String errormessage) {
		// TODO Auto-generated method stub
		
	}
	
	private void logOut() {
		// Remove credentials from the session
		mApi.getSession().unlink();

		// Clear our stored keys
		clearKeys();
		// Change UI state to display logged out version
		setLoggedIn(false);
	}
	
	private void clearKeys() {
		SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
		Editor edit = prefs.edit();
		edit.clear();
		edit.commit();
	}
	
	private void setLoggedIn(boolean loggedIn) {
		btn_linkDropbox = (Button) findViewById(R.id.login);
		TextView mainTextView = (TextView) findViewById(R.id.textView1);
		btn_upload =(Button) findViewById(R.id.upload);
		
		
		btnDownload = (Button) findViewById(R.id.display);
		
		mLoggedIn = loggedIn;
		if (loggedIn) {
			mainTextView.setText(R.string.logged_in_screen);
			btn_linkDropbox.setText(R.string.logout);
			
		} 
		else 
		{
			btn_linkDropbox.setText(R.string.login);
			mainTextView.setText(R.string.welcome_message);
			
		}
	}
	
	public void getFromSdcard() {

		File file = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				Constants.PhotoSaveDirectory);

		if (file.isDirectory()) {
			listFile = file.listFiles();

			for (int i = 0; i < listFile.length; i++) {

				f.add(listFile[i].getAbsolutePath());

			}
		}
	}
	
	

	private void logInScreen()
	{
		if (mLoggedIn) {
			logOut();
		} else {
			// Start the remote authentication
			mApi.getSession().startAuthentication(
					MainActivity.this);
		}
	}
	
	private AndroidAuthSession buildSession() {
		AppKeyPair appKeyPair = new AppKeyPair(APP_KEY, APP_SECRET);
		AndroidAuthSession session;

		String[] stored = getKeys();
		if (stored != null) {
			AccessTokenPair accessToken = new AccessTokenPair(stored[0],
					stored[1]);
			session = new AndroidAuthSession(appKeyPair, ACCESS_TYPE,
					accessToken);
		} else {
			session = new AndroidAuthSession(appKeyPair, ACCESS_TYPE);
		}

		return session;
	}
	
	
	/**
	 * Shows keeping the access keys returned from Trusted Authenticator in a
	 * local store, rather than storing user name & password, and
	 * re-authenticating each time (which is not to be done, ever).
	 * 
	 * @return Array of [access_key, access_secret], or null if none stored
	 */
	
	private String[] getKeys() {
		SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
		String key = prefs.getString(ACCESS_KEY_NAME, null);
		String secret = prefs.getString(ACCESS_SECRET_NAME, null);
		if (key != null && secret != null) {
			String[] ret = new String[2];
			ret[0] = key;
			ret[1] = secret;
			return ret;
		} else {
			return null;
		}
	}

	
	private void checkAppKeySetup()
	{
		// Check to make sure that we have a valid app key
		if (APP_KEY.startsWith("CHANGE") || APP_SECRET.startsWith("CHANGE")) {
			showToast("You must apply for an app key and secret from developers.dropbox.com, and add them to the Cloud Storage ap before trying it.");
			
			finish();
			return;
		}

		
		// Check if the app has set up its manifest properly.
		Intent testIntent = new Intent(Intent.ACTION_VIEW);
		String scheme = "db-" + APP_KEY;
		String uri = scheme + "://" + AuthActivity.AUTH_VERSION + "/test";
		testIntent.setData(Uri.parse(uri));
		PackageManager pm = getPackageManager();
		if (0 == pm.queryIntentActivities(testIntent, 0).size()) {
			showToast("URL scheme in your app's "
					+ "manifest is not set up correctly. You should have a "
					+ "com.dropbox.client2.android.AuthActivity with the "
					+ "scheme: " + scheme);
			
			finish();
		}
	}

	private void showToast(String msg) {
		Toast error = Toast.makeText(this, msg, Toast.LENGTH_LONG);
		error.show();
	}

	private void startImageCaptureActivity()
	{
		Intent i = new Intent(MainActivity.this, FileexplorerActivity.class);
		startActivityForResult(i, 110);
	}

	/*
	 * This method handler the retreival of dropbox file names
	 */
	@SuppressLint("HandlerLeak")
	private final Handler handler = new Handler()
	{
				public void handleMessage(Message msg) {
					
					String fileList = "";
					ArrayList<String> result = msg.getData().getStringArrayList("data");
		
					for (String fileName : result) {
						Log.i("ListFiles", fileName);
		
						fileList = fileList +"\n"+ fileName;
					}
					TextView tv = (TextView) findViewById(R.id.textView1);
					tv.setText(fileList);
					
				}
	};

	
		private void startDropboxFileDisplayActivity()
		{
			
			ListDropboxFiles list = new ListDropboxFiles(mApi, FILE_DIR,
					handler);
			list.execute();
			        
		}
	
		public boolean isInternetConnected() {
		    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		    NetworkInfo netInfo = cm.getActiveNetworkInfo();
		    if (netInfo != null && netInfo.isConnected()) {
		        return true;
		    } else {
		        return false;
		    }
		}
	
}
