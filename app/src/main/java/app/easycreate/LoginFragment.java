package app.easycreate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import Material.CircularProgressView;
import Material.ConnectionDetector;
import Material.Utilities;


public class LoginFragment extends Fragment {
	private FragmentActivity activity;
	Button register, login, forgot_password;
	TextView title;
	EditText email, password;
	Boolean isInternetPresent = false;
	ConnectionDetector cd;
	Typeface tf;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		activity = getActivity();
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.login, container, false);
		AssetManager am = getActivity().getApplicationContext().getAssets();
		tf = Typeface.createFromAsset(am,
				String.format(Locale.US, "fonts/%s", "CraftyCopycat.ttf"));
		register = (Button) rootView.findViewById(R.id.btn_register);
		title = (TextView) rootView.findViewById(R.id.title);
		title.setTypeface(tf);
		login = (Button) rootView.findViewById(R.id.btn_login);
		forgot_password = (Button) rootView.findViewById(R.id.forgot_password);
		email = (EditText) rootView.findViewById(R.id.email);
		password = (EditText) rootView.findViewById(R.id.password);
		register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				replaceFragment(new RegisterFragment());
			}
		});
		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (email.getText().toString().equals("")
						&& email.getText().toString().isEmpty())
					Toast.makeText(activity, "Please enter Email Address",
							Toast.LENGTH_LONG).show();
				if (password.getText().toString().equals("")
						&& password.getText().toString().isEmpty())
					Toast.makeText(activity, "Please enter the Password",
							Toast.LENGTH_LONG).show();
				else {
					cd = new ConnectionDetector(activity);
					isInternetPresent = cd.isConnectingToInternet();
					if (isInternetPresent) {
//						new Login().execute();
						Intent i = new Intent("app.easycreate.MainActivity");
						startActivity(i);
					} else {
						showAlertDialog(activity, "No Internet Connection",
								"You don't have internet connection.", false);
					}

				}
			}
		});

		forgot_password.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				replaceFragment(new ForgotPasswordFragment());
			}
		});
		return rootView;
	}

	public void showAlertDialog(Context context, String title, String message,
			Boolean status) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();

		// Setting Dialog Title
		alertDialog.setTitle(title);

		// Setting Dialog Message
		alertDialog.setMessage(message);

		// Setting alert dialog icon
		alertDialog.setIcon((status) ? R.drawable.sample_icon
				: R.drawable.sample_icon);

		// Showing Alert Message
		alertDialog.show();
	}

	public boolean isValidEmailAddress(String email) {
		String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
		java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
		java.util.regex.Matcher m = p.matcher(email);
		return m.matches();
	}

	private void replaceFragment(Fragment newFragment) {

		FragmentTransaction transaction = activity.getSupportFragmentManager()
				.beginTransaction();
		if (!newFragment.isAdded()) {
			try {
				activity.getSupportFragmentManager().beginTransaction();
				transaction.replace(R.id.frame_container, newFragment);
				transaction.addToBackStack(null);
				transaction.commit();

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else
			transaction.show(newFragment);
	}

	private class Login extends AsyncTask<String, String, String> {

		Dialog dialog;
		CircularProgressView progressView;
		TextView progress_text;
		String url, result = "";
		Boolean proceed = false;
		String cname,clientid,number;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			// custom dialog
			dialog = new Dialog(activity);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.custom_dialog);
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			progressView = (CircularProgressView) dialog
					.findViewById(R.id.progressView);
			progress_text = (TextView) dialog.findViewById(R.id.progresstext);
			progress_text.setText("Loading..");
			Utilities.startAnimationThreadStuff(500, progressView);
			progressView.setIndeterminate(true);
			dialog.show();
//			url = "http://"+getResources().getString(R.string.ip)+"/CredentialsOnDemand/loginclient/dologin";
			Log.i("loginurl", url);
			dialog.setOnKeyListener(new OnKeyListener() {

				@Override
				public boolean onKey(DialogInterface arg0, int keyCode,
						KeyEvent event) {
					// TODO Auto-generated method stub
					if (keyCode == KeyEvent.KEYCODE_BACK) {
						Login.this.cancel(true);
						dialog.dismiss();
					}
					return false;
				}
			});
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... arg) {
			// do above Server call here
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url);

			try {
				// Add your data
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						2);
				// Add all the variables
//				nameValuePairs.add(new BasicNameValuePair("cphone", email.getText().toString()));
//				nameValuePairs.add(new BasicNameValuePair("cpassword", password
//						.getText().toString()));

				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();

				InputStream in = entity.getContent();

				BufferedReader br = new BufferedReader(new InputStreamReader(
						in, "UTF-8"), 8);

				StringBuilder sb = new StringBuilder();
				String line = null;

				while ((line = br.readLine()) != null) {
					sb.append(line + "\n");

				}
				result = sb.toString();
				try {
					JSONObject rs = new JSONObject(result);
					cname = rs.getString("name");
					number = rs.getString("contact");
					proceed=true;
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					proceed = false;
					e.printStackTrace();
				}
				
				} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
					proceed=false;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				proceed=false;
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {

			Log.i("result", result);
			if (proceed) {
				dialog.dismiss();
				SharedPreferences preferences = PreferenceManager
						.getDefaultSharedPreferences(activity);
				SharedPreferences.Editor editor = preferences.edit();
//				editor.putString("accessToken", accessToken);
				editor.putString("clientid", number);
				editor.putString("email", email.getText().toString());
				editor.putString("progress", "main");
				editor.putString("name", cname);
				editor.putString("contact", number);
//				editor.putString("imgurl", "imgurl");
				editor.apply();
				Intent i = new Intent("com.expertondemand.MainActivity");
				startActivity(i);
			} else {
				dialog.dismiss();
				Toast.makeText(activity, "Something went wrong",
						Toast.LENGTH_LONG).show();
			}

		}
	}

}
