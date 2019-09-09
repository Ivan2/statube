package ru.sis.statube.presentation.activity

import androidx.appcompat.app.AppCompatActivity
import ru.sis.statube.presentation.presenter.Presenter

abstract class BaseActivity : AppCompatActivity() {

    protected abstract val presenter: Presenter

}