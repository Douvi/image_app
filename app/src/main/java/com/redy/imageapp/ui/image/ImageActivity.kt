package com.redy.imageapp.ui.image

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import com.redy.imageapp.R
import com.redy.imageapp.repository.ImageItem
import kotlinx.coroutines.launch

class ImageActivity : AppCompatActivity() {

    companion object {
        private const val KEY_IMAGE = "KEY_IMAGE"
        fun intent(context: Context, image: ImageItem): Intent =
            Intent(context, ImageActivity::class.java).apply {
                putExtra(KEY_IMAGE, image)
            }
    }

    private var image: ImageItem? = null
    private val viewModel: ImageViewModel by viewModels(factoryProducer = {
        ImageViewModel.create(
            image = image
        )
    })

    private lateinit var imageView: ImageView
    private lateinit var textView: TextView
    private lateinit var constraintLayout: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        image = intent.extras?.get(KEY_IMAGE) as? ImageItem

        setContentView(R.layout.activity_image)
        bindViews()

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                imageView.load(viewModel.imageUrl) {
                    placeholder(R.drawable.ic_launcher_foreground)
                }
                textView.text = viewModel.author
            }
        }
    }

    private fun bindViews() {
        imageView = findViewById(R.id.image_view)
        textView = findViewById(R.id.text_view)

        constraintLayout = findViewById(R.id.constraint_image)

        updateConstraint()
    }

    private fun updateConstraint() {
        val constraintSet = ConstraintSet().apply { clone(constraintLayout) }
        viewModel.updateConstraint(constraintSet, R.id.image_view, R.id.constraint_image)
        constraintLayout.setConstraintSet(constraintSet)
    }
}
