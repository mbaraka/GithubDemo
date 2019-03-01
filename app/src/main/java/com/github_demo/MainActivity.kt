package com.github_demo

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.github_demo.model.Repo
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.weather.utils.rxError
import io.reactivex.disposables.SerialDisposable
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ViewHolderClicked<Repo> {

    private val searchPresenter = SearchPresenter()
    private lateinit var repoAdapter: RepoAdapter
    private val disposable = SerialDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        headerView.setSearchPresenter(searchPresenter)

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.setHasFixedSize(true)
        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        dividerItemDecoration.setDrawable(resources.getDrawable(R.drawable.divider))

        recycler.addItemDecoration(dividerItemDecoration)

        repoAdapter = RepoAdapter(this)
        listenToRepoChanges()
    }

    private fun listenToRepoChanges() {
        disposable.set(searchPresenter.listenToRepoChanges()
                .subscribe(Consumer {
                    repoAdapter.setData(it)
                    recycler.adapter = repoAdapter
                    animateRecycler()
                }, rxError("couldn't listen to repo changes")))
    }

    private fun animateRecycler() {
        recycler.visibility = View.VISIBLE
        val slideDown = AnimationUtils.loadAnimation(this, R.anim.slide_up)
        recycler.startAnimation(slideDown)
    }

    override fun onItemViewClicked(view: View, item: Repo) {
        val layoutBottomSheet = LayoutInflater.from(this).inflate(R.layout.repo_extra, null, false)
        fillBottomSheetData(layoutBottomSheet, item)

        val  bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(layoutBottomSheet)
        bottomSheetDialog.show()
    }

    private fun fillBottomSheetData(layoutBottomSheet: View, item: Repo) {
        layoutBottomSheet.findViewById<TextView>(R.id.txt_repoLastUpdated).text = item.updated_at
        layoutBottomSheet.findViewById<TextView>(R.id.txt_repoStars).text = item.stargazers_count
        layoutBottomSheet.findViewById<TextView>(R.id.txt_repoForks).text = item.forks
    }

    override fun onDestroy() {
        headerView.onDestroy()
        super.onDestroy()
    }
}
