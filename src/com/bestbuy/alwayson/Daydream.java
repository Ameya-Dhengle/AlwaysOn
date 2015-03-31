/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bestbuy.alwayson;

import java.io.File;
import com.bestbuy.alwayson.R;
import com.bestbuy.alwayson.constants.Constants;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Environment;
import android.provider.Browser;
import android.service.dreams.DreamService;
import android.util.DisplayMetrics;
import android.view.SurfaceView;
import android.widget.MediaController;
import android.widget.VideoView;

/**
 * Class that extends the daydream service and displays the video on the lock screen whenever the device is either docked or charging
 * 
 * @author VaibhavS<br>
 *         Ameya Dhengle
 * 
 */
public class Daydream extends DreamService {

	VideoView		video_player_view;
	DisplayMetrics	dm;
	SurfaceView		sur_View;
	MediaController	media_Controller;

	@Override
	public void onDreamingStarted() {
		super.onDreamingStarted();
		setFullscreen(true);
		setScreenBright(true);
		setContentView(R.layout.fullscreen_clock);
		setVideo();
	}

	private void clearBrowserHistory() {
		Browser.clearHistory(getContentResolver());
		Browser.clearSearches(getContentResolver());
	}


	private void setVideo() {

		video_player_view = (VideoView) findViewById(R.id.video_player_view);
		media_Controller = new MediaController(this);

		dm = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int height = dm.heightPixels;
		int width = dm.widthPixels;
		video_player_view.setMinimumWidth(width);
		video_player_view.setMinimumHeight(height);
		video_player_view.setMediaController(media_Controller);

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

					// file doesn't exist, play from bundle and start download
					video_player_view.setVideoURI((Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.loop)));

					// Attempt download
					DownloadVideoTask _downloadVideoTask = new DownloadVideoTask();
					_downloadVideoTask.execute(_appMediaFile.getAbsolutePath());
				} else {

					// play it
					video_player_view.setVideoPath(_appMediaFile.getAbsolutePath());
					clearBrowserHistory();
					deleteAllCapturedImages();
				}
			} else {

				// do nothing -> tried to create, did not work
				// play it from bundle
				video_player_view.setVideoURI((Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.loop)));
			}
		} catch (Exception e) {
			e.printStackTrace();

		}

		video_player_view.start();
		video_player_view.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				video_player_view.seekTo(0);
				video_player_view.start();
			}
		});
	}

	private void deleteAllCapturedImages() {
		File _publicPictureDirectory = android.os.Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM); // folder where camera captures are stored
		DeleteRecursive(_publicPictureDirectory);
	}

	public static void DeleteRecursive(File fileOrDirectory) {
		if (fileOrDirectory.isDirectory())
			for (File child : fileOrDirectory.listFiles())
				DeleteRecursive(child);

		fileOrDirectory.delete();
	}
}
