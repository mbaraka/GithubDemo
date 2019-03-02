package com.github_demo

import com.github_demo.model.User
import com.github_demo.utils.RequestHelper
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Matchers.any
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnit
import rules.SchedulersOverrideRule
import java.lang.NullPointerException

class SearchPresenterTest {
    @Rule
    @JvmField
    val schedulersOverride = SchedulersOverrideRule()
    @Rule @JvmField val mockitoRule = MockitoJUnit.rule()!!
    @Mock private lateinit var requestHelper: RequestHelper
    private lateinit var searchPresenter: SearchPresenter

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        searchPresenter = SearchPresenter(requestHelper)
    }


    @Test
    fun `when user click search, make sure request helper is requesting user data then their repo`() {
        whenever(requestHelper.request(any())).thenReturn(Observable.just(JSON_USER))
        searchPresenter.search(QUERY).test()
            .assertValue(SearchPresenter.fakeUser())

        verify(requestHelper).request(SearchPresenter.URL_USER + QUERY)
        verify(requestHelper).request(SearchPresenter.URL_REPO.replace(SearchPresenter.USER_ID, QUERY))
    }

    @Test
    fun `when user try to search and request fails, make sure only the user request has been sent`() {
        val exception = NullPointerException()

        whenever(requestHelper.request(any())).thenReturn(Observable.error(exception))
        searchPresenter.search(QUERY).test()
            .assertError(exception)

        verify(requestHelper).request(SearchPresenter.URL_USER + QUERY)
        verifyZeroInteractions(requestHelper)
    }

    private val QUERY = "octocat"

    private  val JSON_USER = "{\n" +
            "  \"login\": \"octocat\",\n" +
            "  \"avatar_url\": \"https://avatars3.githubusercontent.com/u/583231?v=4\",\n" +
            "  \"name\": \"The Octocat\",\n" +
            "}"

}