package Material;


import android.os.Handler;
import android.os.SystemClock;


public class Utilities {
	public static Thread updateThread;
	public static void startAnimationThreadStuff(long delay,
			final CircularProgressView progressView) {
		if (updateThread != null && updateThread.isAlive())
			updateThread.interrupt();
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				// Start animation after a delay so there's no missed frames
				// while the app loads up
				progressView.setProgress((int) 0f);
				progressView.startAnimation(); // Alias for resetAnimation, it's
												// all the same
				// Run thread to update progress every half second until full
				updateThread = new Thread(new Runnable() {
					@Override
					public void run() {
						while (progressView.getProgress() < progressView
								.getMaxProgress() && !Thread.interrupted()) {
							// Must set progress in UI thread
							handler.post(new Runnable() {
								@Override
								public void run() {
									progressView.setProgress(progressView
											.getProgress() + 10);
								}
							});
							SystemClock.sleep(250);
						}
					}
				});
				updateThread.start();
			}
		}, delay);
	}
	
	

}
