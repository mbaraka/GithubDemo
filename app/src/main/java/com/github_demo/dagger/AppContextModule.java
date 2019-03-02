package com.github_demo.dagger;

import android.content.Context;
import com.github_demo.SearchPresenter;
import com.github_demo.utils.RequestHelper;
import dagger.Module;
import dagger.Provides;

import javax.inject.Qualifier;
import javax.inject.Singleton;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Module
public class AppContextModule {

    private final Context appContext;

    public AppContextModule(Context appContext) {
        this.appContext = appContext;
    }

    @Provides
    @AppContext
    @Singleton
    Context appContext() {
        return appContext;
    }

    @Provides
    RequestHelper provideRequestHelper() {
        return new RequestHelper();
    }

    @Provides
    @Singleton
    SearchPresenter provideSearchPresenter(RequestHelper requestHelper) {
        return new SearchPresenter(requestHelper);
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Qualifier
    public @interface AppContext {
    }
}
