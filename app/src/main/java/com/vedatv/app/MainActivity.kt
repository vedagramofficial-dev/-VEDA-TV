
package com.vedatv.app

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.text.InputType
import android.widget.*
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

class MainActivity : Activity() {

    private val bg = Color.rgb(15, 16, 22)
    private val card = Color.rgb(31, 33, 44)
    private val purple = Color.rgb(124, 77, 255)

    private var player: ExoPlayer? = null
    private lateinit var body: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = bg
        window.navigationBarColor = bg

        home()
    }

    private fun home() {
        player?.release()
        player = null

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(18), dp(24), dp(18), dp(18))
            setBackgroundColor(bg)
        }

        root.addView(TextView(this).apply {
            text = "VEDA TV"
            textSize = 30f
            setTextColor(Color.WHITE)
            setTypeface(null, android.graphics.Typeface.BOLD)
        })

        root.addView(TextView(this).apply {
            text = "Ֆիլմեր և սերիալներ՝ մեկ տեղում"
            textSize = 15f
            setTextColor(Color.LTGRAY)
            setPadding(0, dp(4), 0, dp(18))
        })

        val scroll = ScrollView(this)

        body = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }

        scroll.addView(body)

        root.addView(
            scroll,
            LinearLayout.LayoutParams( -1, 0, 1f)
        )

        body.addView(section("Բարի գալուստ VEDA TV"))

        body.addView(note(
            "Մեր նոր ֆիլմերի և սերիալների հավելվածը։ " +
            "Հաջորդ փուլում կավելացնենք կատալոգը, որոնումը " +
            "և դիտման աղբյուրների ընտրությունը։"
        ))

        body.addView(section("Դիտել տեսանյութ"))

        body.addView(note(
            "Մուտքագրիր քո սեփական կամ դիտելու " +
            "թույլտվություն ունեցող HTTPS տեսանյութի հղումը։ " +
            "Աջակցվում են համատեղելի MP4 և HLS M3U8 հոսքերը։"
        ))

        val url = EditText(this).apply {
            hint = "https://example.com/video.m3u8"
            setSingleLine(true)
            setTextColor(Color.WHITE)
            setHintTextColor(Color.GRAY)
            setPadding(dp(12), dp(12), dp(12), dp(12))
            setBackgroundColor(card)

            inputType =
                InputType.TYPE_CLASS_TEXT or
                InputType.TYPE_TEXT_VARIATION_URI

            imeOptions = EditorInfo.IME_ACTION_DONE
        }

        body.addView(url, params())

        body.addView(
            actionButton("▶ Նվագարկել") {
                val value = url.text.toString().trim()

                if (!value.startsWith("https://")) {
                    Toast.makeText(
                        this,
                        "Մուտքագրիր վավեր HTTPS հղում",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    play(value)
                }
            },
            params()
        )

        body.addView(note(
            "Նշում․ ֆիլմերի ավտոմատ որոնման համակարգը " +
            "դեռ պետք է ավելացնենք։ Այս տարբերակում " +
            "նվագարկիչը բացում է ուղիղ տեսանյութի հղումը։"
        ))

        setContentView(root)
    }

    private fun play(url: String) {
        body.removeAllViews()

        val pv = PlayerView(this)

        body.addView(
            pv,
            LinearLayout.LayoutParams(-1, dp(240))
        )

        body.addView(
            actionButton("← Վերադառնալ") {
                home()
            },
            params()
        )

        player = ExoPlayer.Builder(this).build().also { exo ->
            pv.player = exo

            val item = MediaItem.Builder().setUri(url)

            if (url.substringBefore("?")
                    .endsWith(".m3u8", ignoreCase = true)) {
                item.setMimeType(MimeTypes.APPLICATION_M3U8)
            }

            exo.setMediaItem(item.build())
            exo.prepare()
            exo.playWhenReady = true
        }
    }

    private fun section(text: String): TextView {
        return TextView(this).apply {
            this.text = text
            textSize = 21f
            setTextColor(Color.WHITE)
            setTypeface(null, android.graphics.Typeface.BOLD)
            setPadding(0, dp(14), 0, dp(8))
        }
    }

    private fun note(text: String): TextView {
        return TextView(this).apply {
            this.text = text
            textSize = 14f
            setTextColor(Color.LTGRAY)
            setPadding(0, dp(4), 0, dp(12))
        }
    }

    private fun actionButton(
        text: String,
        action: () -> Unit
    ): Button {
        return Button(this).apply {
            this.text = text
            isAllCaps = false
            setTextColor(Color.WHITE)

            backgroundTintList =
                android.content.res.ColorStateList.valueOf(purple)

            setOnClickListener {
                action()
            }
        }
    }

    private fun params(): LinearLayout.LayoutParams {
        return LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            topMargin = dp(6)
            bottomMargin = dp(8)
        }
    }

    private fun dp(value: Int): Int {
        return (value * resources.displayMetrics.density).toInt()
    }

    override fun onDestroy() {
        player?.release()
        player = null
        super.onDestroy()
    }
}
