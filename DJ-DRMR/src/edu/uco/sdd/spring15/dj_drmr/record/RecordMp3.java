package edu.uco.sdd.spring15.dj_drmr.record;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;
import edu.uco.sdd.spring15.dj_drmr.DjdrmrMain;
import edu.uco.sdd.spring15.dj_drmr.R;
import edu.uco.sdd.spring15.dj_drmr.record.Encoder.Builder;

/**
 * This class support Lame to record a mp3 file through a microphone. The
 * library for this project has been already built using Android NDK. (under
 * libs folder)
 * 
 * @author Donghan
 *
 */
public class RecordMp3 {

	public static final String TAG = RecordMp3.class.getSimpleName();

	static {
		System.loadLibrary("mp3lame");
	}

	private String mFilePath;

	private int mSampleRate = 8000;

	private boolean mIsRecording = false;

	private Handler mHandler;
	
	public static final int MSG_REC_STARTED = 0;
	public static final int MSG_REC_STOPPED = 1;
	public static final int MSG_ERROR_GET_MIN_BUFFERSIZE = 2;
	public static final int MSG_ERROR_CREATE_FILE = 3;
	public static final int MSG_ERROR_REC_START = 4;
	public static final int MSG_ERROR_AUDIO_RECORD = 5;
	public static final int MSG_ERROR_AUDIO_ENCODE = 6;
	public static final int MSG_ERROR_WRITE_FILE = 7;
	public static final int MSG_ERROR_CLOSE_FILE = 8;
	
	private Encoder myEncoder;

	private Encoder.Builder myBuilder;
	
	private ProgressBar mProgressBar;
	
	private double amplitude = 0;
	
	protected DjdrmrMain context;

	public RecordMp3(Context context, String filePath, int inSampleRate, int outSampleRate,
			String album, String title, String artist, String comment,
			String year) {
		
		this.context = (DjdrmrMain) context;
		this.mFilePath = filePath;
		myBuilder = new Builder(inSampleRate, 1, outSampleRate, 32);
		myBuilder.id3tagAlbum(album);
		myBuilder.id3tagArtist(artist);
		myBuilder.id3tagTitle(title);
		myBuilder.id3tagComment(comment);
		myBuilder.id3tagYear(year);
	}

	public void start() {
		Log.d(TAG, "Start");
		
		
		if (mProgressBar== null) {
			Log.d(TAG, "progressbar is null");
		}
		if (mIsRecording) {
			return;
		}

		new Thread() {
			@SuppressWarnings("static-access")
			@Override
			public void run() {
				android.os.Process
						.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);

				final int minBufferSize = AudioRecord.getMinBufferSize(
						mSampleRate, AudioFormat.CHANNEL_IN_MONO,
						AudioFormat.ENCODING_PCM_16BIT);

				if (minBufferSize < 0) {
					if (mHandler != null) {
						mHandler.sendEmptyMessage(MSG_ERROR_GET_MIN_BUFFERSIZE);
					}
					return;
				}
				AudioRecord audioRecord = new AudioRecord(
						MediaRecorder.AudioSource.MIC, mSampleRate,
						AudioFormat.CHANNEL_IN_MONO,
						AudioFormat.ENCODING_PCM_16BIT, minBufferSize * 2);

				// PCM buffer size
				short[] buffer = new short[mSampleRate * (16 / 8) * 1 * 360]; // SampleRate[Hz]
																				// *
																				// 16bit
																				// *
																				// Mono
																				// *
																				// 180sec
				byte[] mp3buffer = new byte[(int) (7200 + buffer.length * 2 * 1.25)];

				FileOutputStream output = null;
				try {
					output = new FileOutputStream(new File(mFilePath));
				} catch (FileNotFoundException e) {
					if (mHandler != null) {
						mHandler.sendEmptyMessage(MSG_ERROR_CREATE_FILE);
					}
					return;
				}
				myEncoder = myBuilder.create();

				mIsRecording = true;

				try {
					try {
						audioRecord.startRecording();
					} catch (IllegalStateException e) {
						if (mHandler != null) {
							mHandler.sendEmptyMessage(MSG_ERROR_REC_START);
						}
						return;
					}

					try {
						if (mHandler != null) {
							mHandler.sendEmptyMessage(MSG_REC_STARTED);
						}

						int readSize = 0;
						while (mIsRecording) {
							readSize = audioRecord.read(buffer, 0,
									minBufferSize);
							
							double sum = 0;
							for (int i = 0; i < readSize; i++) {
								sum += buffer[i] * buffer[i];
							}
							
							if (readSize > 0) {
								amplitude = sum / readSize;
								context.mProgressBar = (ProgressBar) context.findViewById(R.id.record_progressBar);
								
								context.runOnUiThread(new Runnable() {
									
									@Override
									public void run() {
										context.mProgressBar.setProgress((int) Math.sqrt(amplitude));
										
									}
								});
							}
							if (readSize < 0) {
								if (mHandler != null) {
									mHandler.sendEmptyMessage(MSG_ERROR_AUDIO_RECORD);
								}
								break;
							} else if (readSize == 0) {
								;
							} else {
								int encResult = myEncoder.encode(buffer,
										buffer, readSize, mp3buffer);
								if (encResult < 0) {
									if (mHandler != null) {
										mHandler.sendEmptyMessage(MSG_ERROR_AUDIO_ENCODE);
									}
									break;
								}
								if (encResult != 0) {
									try {
										output.write(mp3buffer, 0, encResult);
									} catch (IOException e) {
										if (mHandler != null) {
											mHandler.sendEmptyMessage(MSG_ERROR_WRITE_FILE);
										}
										break;
									}
								}
							}
							
						}

						int flushResult = myEncoder.flush(mp3buffer);
						if (flushResult < 0) {
							if (mHandler != null) {
								mHandler.sendEmptyMessage(MSG_ERROR_AUDIO_ENCODE);
							}
						}
						if (flushResult != 0) {
							try {
								output.write(mp3buffer, 0, flushResult);
							} catch (IOException e) {
								if (mHandler != null) {
									mHandler.sendEmptyMessage(MSG_ERROR_WRITE_FILE);
								}
							}
						}

						try {
							output.close();
						} catch (IOException e) {
							if (mHandler != null) {
								mHandler.sendEmptyMessage(MSG_ERROR_CLOSE_FILE);
							}
						}
					} finally {
						
						// Set progressbar default.
						context.mProgressBar.setProgress(0);
						audioRecord.stop();
						audioRecord.release();
					}
				} finally {
					myEncoder.close();
					mIsRecording = false;
				}

				if (mHandler != null) {
					mHandler.sendEmptyMessage(MSG_REC_STOPPED);
				}
			}
		}.start();
	}

	public void stop() {
		mIsRecording = false;
	}

	public boolean isRecording() {
		return mIsRecording;
	}

	public void setHandle(Handler handler) {
		this.mHandler = handler;
	}

}
