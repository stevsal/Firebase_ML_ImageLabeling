package com.example.image_labeling

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AlertDialog
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val loadBtn = findViewById<Button>(R.id.loadUrl)
        val myImageView = findViewById<ImageView>(R.id.ImageToLearn)
        var urlBox = findViewById<EditText>(R.id.ImageUrl)
        DeviceLabels.setOnClickListener{
            runImageLabelingOnDevice((myImageView.drawable as BitmapDrawable).bitmap)
        }

        loadBtn.setOnClickListener{
            Picasso.get().load(urlBox.text.toString()).into(myImageView)
        }

        galleryBtn.setOnClickListener{
            //
        }
    }

    fun runImageLabelingOnDevice (bitmap: Bitmap){
        val image = FirebaseVisionImage.fromBitmap(bitmap)
        val labelDetector = FirebaseVision.getInstance().getOnDeviceImageLabeler()

        labelDetector.processImage(image).addOnSuccessListener {
            // Task completed successfully
            // ...
            processImageLabeling(it)
            }

            .addOnFailureListener {
                // Task failed with an exception
                // ...
                Log.d("LABELFAILURE",it.toString())
            }

    }

    fun processImageLabeling(labels : MutableList<FirebaseVisionImageLabel>){
        val labelsToConv = StringBuilder()
        labels.forEach{
            Log.d("imagelabeling",it.text)
            Log.d("imageconf", it.confidence.toString())
            labelsToConv.append(it.text).append(" ").append(it.confidence).appendln()
        }

        AlertDialog.Builder(this).setTitle("Labels from ML-labeling").setMessage(labelsToConv.toString()).create().show()
    }


}
