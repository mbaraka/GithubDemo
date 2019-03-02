package com.github_demo.utils;

import android.util.Log;

import com.weather.utils.RxHelper;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import javax.inject.Inject;

public class RequestHelper {
	private OkHttpClient client = new OkHttpClient();

	@Inject
	public RequestHelper() {
	}

	public Observable<String> request(final String url) {
		return Observable.create((ObservableOnSubscribe<String>) emitter -> {
			Log.i("RequestHelper", url);
			Request request = new Request.Builder()
					.url(url)
					.build();
			try {
				Response response = client.newCall(request).execute();
				if (!response.isSuccessful()) {
					emitter.onError(new Exception(response.body().string()));
				} else if (response.body() != null) {
					emitter.onNext(response.body().string());
				} else {
					emitter.onError(new NullPointerException("no body from request"));
				}
			} catch (IOException | NullPointerException ex) {
				emitter.onError(ex);
			}
		})
				.compose(RxHelper.INSTANCE.asyncToUiObservable());
	}

}
