package com.github_demo.dagger;

import javax.inject.Singleton;


import com.github_demo.MainActivity;
import com.github_demo.views.HeaderView;
import dagger.Component;

@Singleton
@Component(modules = {
		AppContextModule.class
})
public interface AppComponent {

	void inject(MainActivity target);
	void inject(HeaderView target);

}
