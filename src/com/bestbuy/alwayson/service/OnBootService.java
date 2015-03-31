package com.bestbuy.alwayson.service;

import java.io.File;
import com.bestbuy.alwayson.DownloadVideoTask;
import com.bestbuy.alwayson.constants.Constants;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;

/**
 * Service implemented to move Analytics and Error Log files of previous days to Archived folder and Log Device Boot Time to Analytics Log File on
 * Device Boot
 * 
 * @author Vaibhav Shukla<br>
 *         Ameya Dhengle
 * 
 */
public class OnBootService extends IntentService {

	public OnBootService() {
		super(OnBootService.class.getName());
	}

	public OnBootService(String name) {
		super(name);
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		return Service.START_NOT_STICKY; // tells the system not to bother to
											// restart the service, even when it
											// has sufficient memory.
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		File _appDirectory = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.DEFAULT_DEVICE_FOLDER_PATH); // app
		// directory
		try {
			if (!(_appDirectory.exists())) {
				_appDirectory.mkdirs();
			}

			if (_appDirectory.exists()) {

				File _appMediaFile = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath()
						+ Constants.DEFAULT_DEVICE_FOLDER_PATH + Constants.DEFAULT_DEVICE_MEDIA_FILE_NAME);
				if (!_appMediaFile.exists()) {

					// Attempt download
					DownloadVideoTask _downloadVideoTask = new DownloadVideoTask();
					_downloadVideoTask.execute(_appMediaFile.getAbsolutePath());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
