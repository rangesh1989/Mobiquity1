package com.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.exception.DropboxFileSizeException;
import com.dropbox.client2.exception.DropboxIOException;
import com.dropbox.client2.exception.DropboxParseException;
import com.dropbox.client2.exception.DropboxPartialFileException;
import com.dropbox.client2.exception.DropboxServerException;
import com.dropbox.client2.exception.DropboxUnlinkedException;
import com.example.MainActivity;

/*
 * This Async Task performs the operation of Uploading the Image or Audio 
 * File onto the DropBox.
 */
public class Upload extends AsyncTask<Void, Long, Boolean> 
{

	private DropboxAPI<?> mApi;
	private String mPath;
	private long mFileLen;

	 
	private Context mContext;
	private ProgressDialog mDialog;

	private String mErrorMsg;

	//new class variables:
	int mFilesUploaded;
	private File[] mFilesToUpload;
	int mCurrentFileIndex;
	int totalBytes = 0;

	API_Listener api_Listener;
	MainActivity uploadPhotos_Activity;
	private int requestNumber;
	
	public Upload(int request_num,MainActivity activity, DropboxAPI<?> api, String dropboxPath, File[] filesToUpload) 
	{
		// We set the context this way so we don't accidentally leak activities
		mContext = activity;		
		api_Listener=activity;
		mApi = api;
		mPath = dropboxPath;
		requestNumber=request_num;

		//set number of files uploaded to zero.
		mFilesUploaded = 0;
		mFilesToUpload = filesToUpload;
		mCurrentFileIndex = 0;
		
		mDialog = new ProgressDialog(mContext);
		
		mDialog.setMax(mFilesToUpload.length);
		
		mDialog.setMessage("Uploading...");
		
		mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		
		mDialog.setProgress(0);  
		
		mDialog.setCancelable(false);
		
		mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		
		mDialog.show();
		
	}


	@Override
	protected void onPreExecute() 
	{ 
		super.onPreExecute();

	}
	
	
	@Override
	protected Boolean doInBackground(Void... params) 
	{
		FileInputStream fis;
		try 
		{			
			for (int i = 0; i < mFilesToUpload.length; i++) 
			{
				mCurrentFileIndex = i;
				File file = mFilesToUpload[i];

				publishProgress(Long.parseLong(""+i));

				// By creating a request, we get a handle to the putFile
				// operation,
				// so we can cancel it later if we want to
				
				fis = new FileInputStream(file);				
				String path = mPath + file.getName();
//				mRequest = mApi.putFileOverwriteRequest(path, fis,file.length(),null);			
				com.dropbox.client2.DropboxAPI.Entry response = mApi.putFile(path, fis,file.length(),null,null);


				if (!isCancelled()) 
				{
					mFilesUploaded++;
				}
				else 
				{
					return false;
				}
			}
			return true;
		} catch (DropboxUnlinkedException e) {
			
			// This session wasn't authenticated properly or user unlinked
			mErrorMsg = "This app wasn't authenticated properly.";
		} catch (DropboxFileSizeException e) {
			
			// File size too big to upload via the API
			mErrorMsg = "This file is too big to upload";
		} catch (DropboxPartialFileException e) {
			
			// We canceled the operation
			mErrorMsg = "Upload canceled";
		} catch (DropboxServerException e) {
			
			// Server-side exception. These are examples of what could happen,
			// but we don't do anything special with them here.
			
			if (e.error == DropboxServerException._401_UNAUTHORIZED) {
				// Unauthorized, so we should unlink them. You may want to
				// automatically log the user out in this case.
			} else if (e.error == DropboxServerException._403_FORBIDDEN) {
				// Not allowed to access this
				mErrorMsg = "Access Denied";
			} else if (e.error == DropboxServerException._404_NOT_FOUND) {
				// path not found (or if it was the thumbnail, can't be
				// thumbnailed)
				mErrorMsg = "Specified Path was not found";
			} else if (e.error == DropboxServerException._507_INSUFFICIENT_STORAGE) {
				// user is over quota
			} else {
				// Something else
			}
			// This gets the Dropbox error, translated into the user's language
			mErrorMsg = e.body.userError;
			if (mErrorMsg == null) {
				mErrorMsg = e.body.error;
			}
		} catch (DropboxIOException e) {
			// Happens all the time, probably want to retry automatically.
			
			showToast("IO EXCEPTION");
		} catch (DropboxParseException e) {
			// Probably due to Dropbox server restarting, should retry
			
			showToast("PARSE EXCEPTION");
		} catch (DropboxException e) {
			// Unknown error
			
			showToast( "UNKNOWN EXCEPTION");
		} catch (FileNotFoundException e) {
			showToast( "FILE NOT FOUND EXCEPTION");
		}

		return false;
	}
	
	@Override
	protected void onProgressUpdate(Long... progress) {
		int percent = (int) (100.0 * (double) progress[0] / mFileLen + 0.5);
		mDialog.setProgress(percent);
	}

	@Override
	protected void onPostExecute(Boolean result) 
	{
		mDialog.dismiss();

		if (result) 
		{
			api_Listener.onSuccess(requestNumber, result);			
		} 
		else 
		{
			showToast(mErrorMsg);
		}
	}
	
	private void showToast(String msg) 
	{
		Toast error = Toast.makeText(mContext, msg, Toast.LENGTH_LONG);
		error.show();
	}
	
}