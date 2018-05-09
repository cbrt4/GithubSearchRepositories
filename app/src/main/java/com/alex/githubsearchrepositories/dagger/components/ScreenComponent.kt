package com.alex.githubsearchrepositories.dagger.components

import com.alex.githubsearchrepositories.util.ScreenScope
import com.alex.githubsearchrepositories.view.activities.MainActivity
import dagger.Component

@ScreenScope
@Component(dependencies = [ApplicationComponent::class])
interface ScreenComponent {

    fun inject(activity: MainActivity)

}