package com.arkam.pockemonku.view.ui.main

import android.content.ContentValues
import android.content.Intent
import android.graphics.Point
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.View
import android.widget.Toast
import com.arkam.pockemonku.R
import com.google.ar.core.Plane
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import kotlinx.android.synthetic.main.activity_ar.*


class ArActivity : AppCompatActivity() {
    private lateinit var arFrag: ArFragment
    private var modelRenderable: ModelRenderable? = null

    private fun getScreenCenter(): Point {
        // find the root view of the activity
        val vw = findViewById<View>(android.R.id.content)
        // returns center of the screen as a Point object
        return Point(vw.width / 2, vw.height / 2)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ar)

        my_btn.setOnClickListener {
            add3dObject()
        }

        arFrag = supportFragmentManager.findFragmentById(R.id.sceneform_fragment) as ArFragment

        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .permitAll().build()
        )

        ModelRenderable.builder()
            .setSource(
                this,
                Uri.parse("https://github.com/ZiangZhao1227/poke_pocket/raw/main/app/src/main/java/com/example/pokepocket/armodel/pokeball.gltf")
            )
            .setIsFilamentGltf(true)
            .setAsyncLoadEnabled(true)
            .setRegistryId("pokeball")
            .build()
            .thenAccept { modelRenderable = it }
            .exceptionally {
                Log.e(ContentValues.TAG, "something went wrong ${it.localizedMessage}")
                null
            }
        Toast.makeText(
            this,
            "Move around the phone, until you see the field where to place the Pokeball",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun add3dObject() {
        val frame = arFrag.arSceneView.arFrame
        if (frame != null && modelRenderable != null) {
            my_btn.visibility = View.GONE
            val pt = getScreenCenter()
            // get list of HitResult of the given location in the camera view
            val hits = frame.hitTest(pt.x.toFloat(), pt.y.toFloat())
            for (hit in hits) {
                val trackable = hit.trackable
                if (trackable is Plane) {
                    val anchor = hit!!.createAnchor()
                    val anchorNode = AnchorNode(anchor)
                    anchorNode.setParent(arFrag.arSceneView.scene)
                    val mNode = TransformableNode(arFrag.transformationSystem)
                    mNode.setOnTapListener { hitTestResult, motionEvent ->
                        Toast.makeText(
                            this,
                            "Got a Poké Ball",
                            Toast.LENGTH_SHORT
                        ).show()

                        val intent = Intent(this@ArActivity, MapsActivity::class.java)
                        startActivity(intent)

                    }
                    mNode.scaleController.minScale = 0.05f
                    mNode.scaleController.maxScale = 2.0f
                    mNode.localScale = Vector3(0.2f, 0.2f, 0.2f)
                    mNode.renderable = modelRenderable
                    mNode.setParent(anchorNode)
                    mNode.select()
                    break
                }
            }
        }
    }
}