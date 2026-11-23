package com.gebenbg.botz

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*

class MainActivity : Activity() {
    private val bg = Color.rgb(7, 12, 22)
    private val panel = Color.rgb(15, 23, 39)
    private val cyan = Color.rgb(34, 211, 238)
    private val muted = Color.rgb(148, 163, 184)
    private lateinit var root: LinearLayout
    private lateinit var content: LinearLayout
    private lateinit var statusLabel: TextView
    private lateinit var urlField: EditText
    private lateinit var codeField: EditText
    private lateinit var idField: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        buildShell()
        showHome()
    }

    private fun buildShell() {
        root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(bg)
            setPadding(dp(18), dp(20), dp(18), dp(12))
        }
        val header = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER_VERTICAL }
        val logo = TextView(this).apply {
            text = "KV"; textSize = 18f; typeface = Typeface.DEFAULT_BOLD; setTextColor(cyan)
            gravity = Gravity.CENTER; background = rounded(Color.rgb(10, 45, 58), cyan, 12); setPadding(dp(14), dp(10), dp(14), dp(10))
        }
        header.addView(logo)
        val titleCol = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(dp(12), 0, 0, 0) }
        titleCol.addView(label("KICRUTZ-VOID", 17f, Color.WHITE, true))
        titleCol.addView(label("NATIVE SUPPORT CENTER · v1.0", 9f, muted, false))
        header.addView(titleCol, LinearLayout.LayoutParams(0, -2, 1f))
        val badge = label("● READY", 10f, cyan, true); header.addView(badge)
        root.addView(header)
        val divider = View(this).apply { setBackgroundColor(Color.rgb(31, 46, 65)) }
        root.addView(divider, LinearLayout.LayoutParams(-1, dp(1)).apply { topMargin = dp(16); bottomMargin = dp(14) })
        content = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        val scroll = ScrollView(this).apply { addView(content) }
        root.addView(scroll, LinearLayout.LayoutParams(0, 0, 1f))
        val nav = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER; setPadding(0, dp(10), 0, dp(4)) }
        listOf("Overview", "Connection", "About").forEachIndexed { i, name ->
            val b = Button(this).apply {
                text = name; textSize = 11f; setTextColor(if (i == 0) cyan else muted)
                setBackgroundColor(Color.TRANSPARENT); isAllCaps = false
                setOnClickListener { when (i) { 0 -> showHome(); 1 -> showConnection(); else -> showAbout() } }
            }
            nav.addView(b, LinearLayout.LayoutParams(0, dp(44), 1f))
        }
        root.addView(nav)
        setContentView(root)
    }

    private fun showHome() {
        content.removeAllViews()
        content.addView(card {
            addView(label("DEVICE SUPPORT", 10f, muted, true))
            addView(label("Connection status", 20f, Color.WHITE, true), lp(top = 8))
            statusLabel = label("● DISCONNECTED", 13f, Color.rgb(251, 113, 133), true)
            addView(statusLabel, lp(top = 8))
            addView(label("Koneksi hanya berjalan saat kamu menekan Connect. Status perangkat yang dibagikan terbatas pada model, versi Android, dan baterai.", 12f, muted), lp(top = 12))
        })
        content.addView(card {
            addView(label("QUICK STATUS", 10f, muted, true))
            val row = LinearLayout(this@MainActivity).apply { orientation = LinearLayout.HORIZONTAL }
            row.addView(metric("Battery", "—", "Persentase"), LinearLayout.LayoutParams(0, -2, 1f))
            row.addView(metric("Device", "Local", "Perangkat ini"), LinearLayout.LayoutParams(0, -2, 1f))
            row.addView(metric("Privacy", "ON", "Kontrol pengguna"), LinearLayout.LayoutParams(0, -2, 1f))
            addView(row, lp(top = 14))
        })
        content.addView(card {
            addView(label("ACTIONS", 10f, muted, true))
            val connect = actionButton("Configure support connection", cyan)
            connect.setOnClickListener { showConnection() }
            addView(connect, lp(top = 12))
            val disconnect = actionButton("Disconnect service", Color.rgb(251, 113, 133))
            disconnect.setOnClickListener { stopService(Intent(this@MainActivity, SupportService::class.java)); statusLabel.text = "● DISCONNECTED"; statusLabel.setTextColor(Color.rgb(251,113,133)); toast("Service dihentikan") }
            addView(disconnect, lp(top = 8))
        })
        content.addView(card {
            addView(label("PRIVACY & SAFETY", 10f, muted, true))
            addView(label("• Koneksi transparan dengan notifikasi foreground.\n• Tidak mengakses kamera, mikrofon, clipboard, file pribadi, atau kendali layar.\n• Gunakan hanya pada perangkat milikmu atau perangkat yang pemiliknya menyetujui sesi.", 12f, Color.rgb(203,213,225)), lp(top = 10))
        })
    }

    private fun showConnection() {
        content.removeAllViews()
        content.addView(card {
            addView(label("SUPPORT SERVER", 10f, muted, true))
            urlField = field("WSS URL (contoh: wss://server.example/ws)")
            codeField = field("Pairing code")
            codeField.inputType = 129
            idField = field("Device label / ID")
            addView(urlField, lp(top = 12)); addView(codeField, lp(top = 8)); addView(idField, lp(top = 8))
            addView(label("Pairing code diperlukan untuk menghubungkan perangkat. Jangan bagikan kode ini.", 11f, muted), lp(top = 10))
            val start = actionButton("Connect", cyan)
            start.setOnClickListener {
                val url = urlField.text.toString().trim()
                val code = codeField.text.toString().trim()
                val device = idField.text.toString().trim()
                if (url.isBlank() || code.isBlank() || device.isBlank()) { toast("Lengkapi semua kolom"); return@setOnClickListener }
                if (!url.startsWith("wss://")) { toast("Gunakan koneksi WSS yang terenkripsi"); return@setOnClickListener }
                val intent = Intent(this@MainActivity, SupportService::class.java).apply {
                    putExtra("url", url); putExtra("code", code); putExtra("id", device)
                }
                startForegroundService(intent)
                statusLabel.text = "● CONNECTING"; statusLabel.setTextColor(cyan)
                toast("Permintaan koneksi dimulai")
                showHome()
            }
            addView(start, lp(top = 14))
        })
    }

    private fun showAbout() {
        content.removeAllViews()
        content.addView(card {
            addView(label("ABOUT KICRUTZ-VOID", 10f, muted, true))
            addView(label("Native Android support utility", 20f, Color.WHITE, true), lp(top = 8))
            addView(label("Versi 1.0\nAndroid API 23+\nJava 17 · Kotlin JVM 17\n\nAplikasi ini dibuat untuk status perangkat yang transparan dan sesi dukungan yang disetujui pemilik perangkat.", 13f, Color.rgb(203,213,225)), lp(top = 10))
        })
    }

    private fun card(block: LinearLayout.() -> Unit): LinearLayout = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL; setPadding(dp(15), dp(15), dp(15), dp(15))
        background = rounded(panel, Color.rgb(31, 46, 65), 16)
        block()
    }
    private fun metric(title: String, value: String, detail: String) = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        addView(label(title, 10f, muted)); addView(label(value, 18f, Color.WHITE, true), lp(top = 6)); addView(label(detail, 9f, muted), lp(top = 3))
    }
    private fun field(hintText: String) = EditText(this).apply {
        hint = hintText; textSize = 13f; setTextColor(Color.WHITE); setHintTextColor(muted)
        setSingleLine(true); setPadding(dp(12), dp(11), dp(12), dp(11))
        background = rounded(Color.rgb(5, 10, 18), Color.rgb(40, 55, 74), 10)
    }
    private fun actionButton(text: String, accent: Int) = Button(this).apply {
        this.text = text; textSize = 12f; isAllCaps = false; setTextColor(accent)
        background = rounded(Color.rgb(9, 24, 37), accent, 10)
    }
    private fun label(text: String, size: Float, color: Int, bold: Boolean = false) = TextView(this).apply {
        this.text = text; textSize = size; setTextColor(color)
        if (bold) typeface = Typeface.DEFAULT_BOLD
    }
    private fun rounded(fill: Int, stroke: Int, radius: Int) = GradientDrawable().apply {
        setColor(fill); setStroke(dp(1), stroke); cornerRadius = dp(radius).toFloat()
    }
    private fun lp(top: Int = 0, bottom: Int = 0) = LinearLayout.LayoutParams(-1, -2).apply {
        if (top > 0) topMargin = dp(top); if (bottom > 0) bottomMargin = dp(bottom)
    }
    private fun dp(v: Int) = (v * resources.displayMetrics.density).toInt()
    private fun toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}
