package ru.sis.statube.presentation.activity

import android.content.Intent
import ru.sis.statube.additional.VIDEO_DATA_KEY
import ru.sis.statube.model.Video
import ru.sis.statube.presentation.presenter.FullVideoLoadingPresenter
import ru.sis.statube.presentation.screens.video.VideoActivity

abstract class FullVideoLoadingActivity : BaseActivity() {

    abstract override val presenter: FullVideoLoadingPresenter

    protected fun loadFullVideo(video: Video) {
        presenter.loadFullVideo(this, video) {
            if (!this.isDestroyed) {
                val intent = Intent(this, VideoActivity::class.java)
                intent.putExtra(VIDEO_DATA_KEY, it)
                startActivity(intent)
            }
        }
    }

}
