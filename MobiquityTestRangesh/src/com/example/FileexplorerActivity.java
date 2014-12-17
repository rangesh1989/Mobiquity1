package com.example;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class FileexplorerActivity extends Activity implements OnClickListener{

	
	
	private static final int CAPTURE_IMAGE_REQUEST = 100;
	protected static final int SELECT_PICTURE = 2;
	private static final int REQUESTCODE_RECORDING = 10;
	
	private Uri selectedUri;
	private String selectedImagePath = "";
	Button btnTakePicture, btnGallary, btnUpload,btnFb,btnRecord;
	TextView uploadEntranceView;
	Intent dropboxIntent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fileexplorer);
		
		btnTakePicture = (Button) findViewById(R.id.take_picture);
		
		btnGallary = (Button) findViewById(R.id.upload_from_gallary);
		
		btnUpload = (Button) findViewById(R.id.upload_image);
		btnFb = (Button) findViewById(R.id.share_with_fb);
		
		btnRecord = (Button) findViewById(R.id.record_audio);
		uploadEntranceView = (TextView) findViewById(R.id.uploadEntranceView);
		
		dropboxIntent = getIntent();
		
		btnTakePicture.setOnClickListener(this);
		btnGallary.setOnClickListener(this);
		btnUpload.setOnClickListener(this);
		btnRecord.setOnClickListener(this);
		btnFb.setOnClickListener(this);
		
		btnUpload.setVisibility(View.GONE);
		
		btnFb.setVisibility(View.GONE);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.record_audio:
			recordAudio();
			break;
		case R.id.take_picture:
			captureImage();
			break;
			
		case R.id.upload_from_gallary:
			startGallary();
			break;
		
		case R.id.upload_image:
		{
			if(!isInternetConnected())
			{
				
				showToast("Please Check Your Internet Connection")	;
			}
			else
			{
				uploadFile();
				
			}
		}
			break;
			
		case R.id.share_with_fb:
		{

			if(isInternetConnected())
				{
						if(selectedUri!=null)
					{
					shareWithFb(selectedUri);
					}
					else
					{
						
						showToast("No Image selected")	;	
					}
				}
			else
				{
					showToast("Please Check Your Internet Connection")	;
				}
		}
		break;
		}
	}
	
	
	public void onResume()
	{
		super.onResume();
		if(selectedUri == null || selectedImagePath.equals(""))
		{
			btnUpload.setVisibility(View.GONE);
			
			btnFb.setVisibility(View.GONE);
		}
		
	}
	public static boolean isAvailable(Context ctx, Intent intent) {
		   final PackageManager mgr = ctx.getPackageManager();
		   List<ResolveInfo> list =
		      mgr.queryIntentActivities(intent, 
		         PackageManager.MATCH_DEFAULT_ONLY);
		   return list.size() > 0;
		}
	
	private void recordAudio(){
		Intent intent =
				
				      new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
				if (isAvailable(getApplicationContext(), intent)) {
				
				   startActivityForResult(intent,REQUESTCODE_RECORDING);
				
				}

	}
	
	private void shareWithFb(Uri uri)
	{
Intent sharingIntent = new Intent(Intent.ACTION_SEND);
		
		sharingIntent.setType("image/*");
		sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
		startActivity(Intent.createChooser(sharingIntent, "Share image using"));
	
	}
	private void captureImage()
	{
	    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

	    startActivityForResult(intent, CAPTURE_IMAGE_REQUEST);
	}
	// End captureImage

	@SuppressLint("InlinedApi")
	private void startGallary()
	{
		// in onCreate or any event where your want the user to
        // select a file
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_PICK);
        
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), SELECT_PICTURE);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
	    if(requestCode == CAPTURE_IMAGE_REQUEST)
	    {
	        if(resultCode == RESULT_OK) {
	        	// Successfully captured image

	        	
	        	 selectedUri = data.getData();
	        	 selectImagePathFromFile(selectedUri);
	        	 btnFb.setVisibility(View.VISIBLE);
	        	 btnUpload.setVisibility(View.VISIBLE);

	        }
	        
	    
	        else if(resultCode == RESULT_CANCELED) 
	        {
	        	// User cancelled image capture
	        	
	        	selectedImagePath = "";
	        }
	            
	        else    // Failed to capture image
	           showToast("Failed to capture image");
	        
	    }
	    
	    else if (requestCode == SELECT_PICTURE)
	    {
		    	 if(resultCode == RESULT_OK) {
			        
		    		 selectedUri = data.getData();
		    		 
		    		 selectImagePathFromFile(selectedUri);
		    		 btnFb.setVisibility(View.VISIBLE);
		        	 btnUpload.setVisibility(View.VISIBLE);
			        }
			    
			        else if(resultCode == RESULT_CANCELED) 
			        {
			        	// User cancelled image capture
			        	
			        	uploadEntranceView.setText(R.string.upload_activity_text_view);
			        	
			        	selectedImagePath = "";
			        }
			            
			        else  
			        {
			        	// Failed to capture image
			            
			        	
			        	showToast("Failed to capture image");
			            uploadEntranceView.setText(R.string.upload_activity_text_view);
			        	
			        	selectedImagePath = "";
			        }
		    }
	    else if (requestCode == REQUESTCODE_RECORDING)
		    {
	    		if(resultCode == RESULT_OK)
	    			{
	    			selectedUri = data.getData();
	    			selectImagePathFromFile(selectedUri);
	    			
	    			 btnFb.setVisibility(View.VISIBLE);
		        	 btnUpload.setVisibility(View.VISIBLE);
	    			}
		    }
	    }
	
	// End onActivityResult
	
	/*
	 * helps validate the Uri for the images and create the Path to upload
	 */
	private void selectImagePathFromFile(Uri selectedImageUri)
	{
		if (selectedImageUri != null) {
		      // now we get the path to the image file
		
			 Cursor cursor = getContentResolver().query(selectedImageUri, null,
		                                      null, null, null);
		    
		     cursor.moveToFirst();
		     int column_index = cursor
		             .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		     selectedImagePath = cursor.getString(column_index);
		     cursor.close();
		     uploadEntranceView.setText("The Image in the device "+selectedImagePath + " Has been selected. Select UPLOAD TO DROPBOX to move to the dropbox.");
		     }
		 else 
		 {
			 
			 showToast("No File was selected");
		 }
          
	}
	
	public void uploadFile() {
		if(selectedImagePath.equals(""))
		{
			showToast("No Image Selected");
		}
		else
		{
			
			dropboxIntent.putExtra("file", selectedImagePath);
			setResult(RESULT_OK, dropboxIntent);
			finish();
		}
	}
	private void showToast(String msg) {
		Toast error = Toast.makeText(this, msg, Toast.LENGTH_LONG);
		error.show();
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
