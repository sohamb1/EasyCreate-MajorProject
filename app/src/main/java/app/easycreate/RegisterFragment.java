package app.easycreate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import Material.CircularProgressView;
import Material.Utilities;


public class RegisterFragment extends Fragment {
	private FragmentActivity activity;
	Button register;
	EditText name, email, password, repassword, contact;
	String gender;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		activity = getActivity();
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.activity_register, container,
				false);
		register = (Button) rootView.findViewById(R.id.register);
		name = (EditText) rootView.findViewById(R.id.name);
		email = (EditText) rootView.findViewById(R.id.email);
		password = (EditText) rootView.findViewById(R.id.password);
		repassword = (EditText) rootView.findViewById(R.id.retype_password);
		contact = (EditText) rootView.findViewById(R.id.mobile);
		register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				new Register().execute();
				// Intent i = new Intent("com.expertondemand.MainActivity");
				// startActivity(i);
			}
		});
		return rootView;
	}

	private class Register extends AsyncTask<String, String, String> {

		Dialog dialog;
		CircularProgressView progressView;
		TextView progress_text;
		String url, result = "";
		int errorcode;
		Boolean proceed = false, status;
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
//			url = "http://"+getResources().getString(R.string.ip)+"/CredentialsOnDemand/registerclient/doregister";
			Log.i("loginurl", url);
			dialog.setOnKeyListener(new OnKeyListener() {

				@Override
				public boolean onKey(DialogInterface arg0, int keyCode,
						KeyEvent event) {
					// TODO Auto-generated method stub
					if (keyCode == KeyEvent.KEYCODE_BACK) {
						Register.this.cancel(true);
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
//				nameValuePairs.add(new BasicNameValuePair("cname", name
//						.getText().toString()));
//				nameValuePairs.add(new BasicNameValuePair("cpassword", password
//						.getText().toString()));
//				nameValuePairs.add(new BasicNameValuePair("cgender", radioSexButton.getText().toString()));
//				nameValuePairs.add(new BasicNameValuePair("cemail", email
//						.getText().toString()));
//				nameValuePairs.add(new BasicNameValuePair("cphone", contact
//						.getText().toString()));

				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));

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
				proceed = false;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				proceed = false;
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
				Toast.makeText(activity,name
						.getText().toString()+password
						.getText().toString()+contact
						.getText().toString(),
						Toast.LENGTH_LONG).show();
			}

		}

	}
}
