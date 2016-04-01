package app.easycreate;


import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;

public class ForgotPasswordFragment extends Fragment {
	private FragmentActivity activity;
	Typeface tf;
	public static TextView toolbar_title;
	EditText email;
	Button enter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		activity = getActivity();
		setRetainInstance(true);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_forgotpassword, container,
				false);
		Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
		enter = (Button) rootView.findViewById(R.id.btn_forgot_password);
		email = (EditText) rootView.findViewById(R.id.email);
		toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
		toolbar_title.setTypeface(tf);
		email.setTypeface(tf);
		enter.setTypeface(tf);
		toolbar_title.setText("Forgot Password");
		AssetManager am = getActivity().getApplicationContext().getAssets();
		tf = Typeface.createFromAsset(am,
				String.format(Locale.US, "fonts/%s", "CaviarDreams.ttf"));
		return rootView;
	}

}
