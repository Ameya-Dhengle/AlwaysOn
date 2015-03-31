/**
 * 
 */
package com.bestbuy.alwayson;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.bestbuy.alwayson.constants.Constants;

import android.os.AsyncTask;


/**
 * @author Ameya Dhengle
 *
 */
public class DownloadVideoTask extends AsyncTask<String, Integer, String> {

	@Override
	protected String doInBackground(String... params) {

		InputStream _inputStream = null;
        OutputStream _outputStream = null;
        HttpURLConnection _connection = null;
        
		try {
			URL _url = new URL(Constants.DEFAULT_VIDEO_SEVER_URL);
			_connection = (HttpURLConnection) _url.openConnection();
			_connection.connect();
			if (_connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

				_inputStream = _connection.getInputStream();
				_outputStream = new FileOutputStream(params[0]);
				byte _data[] = new byte[4096];
				int _count;
				while ((_count = _inputStream.read(_data)) != -1) {

					_outputStream.write(_data, 0, _count);
				}
			}
		} catch (MalformedURLException e) {

			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		} finally {
			try {
				if (_outputStream != null)
					_outputStream.close();
				if (_inputStream != null)
					_inputStream.close();
			} catch (IOException e) {
				
				e.printStackTrace();
			}

			if (_connection != null)
				_connection.disconnect();
		}
		return null;
	}

}
