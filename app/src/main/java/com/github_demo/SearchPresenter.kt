package com.github_demo

import com.github_demo.model.Repo
import com.github_demo.model.User
import com.github_demo.utils.RequestHelper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.weather.utils.emptyConsumer
import com.weather.utils.rxError
import io.reactivex.Observable
import io.reactivex.disposables.SerialDisposable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject


class SearchPresenter @Inject constructor(private val requestHelper: RequestHelper) {

    private val repoEmitter = PublishSubject.create<List<Repo>>()
    private val disposable = SerialDisposable()

    fun search(name: String): Observable<User> {
        return requestHelper.request(URL_USER + name)
                .map {
                    Gson().fromJson(it, User::class.java)
//                    fakeUser()
                }
                .doOnNext {
                    searchForRepo(it)
                }
    }

    fun listenToRepoChanges(): Observable<List<Repo>> {
        return repoEmitter
    }

    private fun searchForRepo(user: User) {
        disposable.set(requestHelper.request(URL_REPO.replace(USER_ID, user.login))
                .map { s ->
                    val listType = object : TypeToken<ArrayList<Repo>>() {}.type

                    repoEmitter.onNext(Gson().fromJson<ArrayList<Repo>>(s, listType))
//                    repoEmitter.onNext(fakeRepoList())
                }
                .subscribe(emptyConsumer("searching for repo is done"), rxError("Failed to get repo")))
    }


    companion object {
        const val URL_USER = "https://api.github.com/users/"

        const val USER_ID = "{userId}"
        const val URL_REPO = "https://api.github.com/users/{userId}/repos"

        fun fakeUser(): User {
            return User("octocat", "The Octocat", "https://avatars3.githubusercontent.com/u/583231?v=4")
        }

        fun fakeRepoList(): ArrayList<Repo> {
            val list = ArrayList<Repo>()
            for(i in 1 ..10) {
                list.add(Repo("this is description $i",
                        "${i*i - 1}",
                        "This is the name of repo $i",
                        "${i*i}",
                        "2018-$i-19T04:41:29Z"))
            }
            return list
        }
    }


}
