package com.github_demo.views;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github_demo.Application;
import com.github_demo.R;
import com.github_demo.SearchPresenter;
import com.github_demo.model.User;
import com.google.android.material.textfield.TextInputEditText;
import com.weather.utils.RxHelper;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.SerialDisposable;

import javax.inject.Inject;

public class HeaderView extends LinearLayout implements View.OnClickListener {
	private TextInputEditText met_search;
	private ImageView img_user;
	private TextView txt_userName;
	private LinearLayout container_userInfo;
	@Inject SearchPresenter searchPresenter;
	private SerialDisposable disposable = new SerialDisposable();

	//region constructors
	public HeaderView(Context context) {
		super(context);
		init();
	}

	public HeaderView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public HeaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	public HeaderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init();
	}

	private void init() {
		LayoutInflater.from(getContext()).inflate(R.layout.main_header, this, true);
		Button btn_Search = findViewById(R.id.btn_search);
		btn_Search.setOnClickListener(this);
		met_search = findViewById(R.id.met_search);
		img_user = findViewById(R.id.img_user);
		txt_userName = findViewById(R.id.txt_userName);
		container_userInfo = findViewById(R.id.container_userInfo);
		Application.appComponent.inject(this);
	}
	//endregion

	@Override
	public void onClick(View v) {
		if (met_search.getText() == null || met_search.getText().toString().isEmpty()) {
			met_search.setError(getContext().getString(R.string.enter_a_github_user_id));
		} else {
			search();
		}
	}

	private void search() {
		String query = met_search.getText().toString();
		searchPresenter.search(query)
				.subscribe(new Observer<User>() {
					@Override
					public void onSubscribe(Disposable d) {
						disposable.set(d);
					}

					@Override
					public void onNext(User user) {
						showUserData(user);
					}

					@Override
					public void onError(Throwable e) {
						Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onComplete() {

					}
				});
	}

	private void showUserData(final User user) {
		Glide.with(getContext()).load(user.getAvatar_url()).into(img_user);
		txt_userName.setText(user.getName());
		container_userInfo.setVisibility(View.VISIBLE);
		Animation slide_down = AnimationUtils.loadAnimation(getContext(), R.anim.slide_up);
		container_userInfo.startAnimation(slide_down);
	}

	public void onDestroy() {
		RxHelper.INSTANCE.unsubscribe(disposable.get());
	}
}
