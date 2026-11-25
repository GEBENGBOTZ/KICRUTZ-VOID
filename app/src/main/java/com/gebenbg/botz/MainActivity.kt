package com.gebenbg.botz

import android.app.Activity
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import android.graphics.drawable.GradientDrawable
import android.widget.FrameLayout

class MainActivity : Activity() {
    private val bg = Color.rgb(3, 7, 18)
    private val panel = Color.rgb(8, 16, 30)
    private val border = Color.rgb(24, 48, 67)
    private val cyan = Color.rgb(34, 211, 238)
    private val purple = Color.rgb(168, 85, 247)
    private val crimson = Color.rgb(251, 113, 133)
    private val green = Color.rgb(52, 211, 153)
    private val muted = Color.rgb(100, 116, 139)
    private lateinit var content: LinearLayout
    private lateinit var statusText: TextView
    private lateinit var urlField: EditText
    private lateinit var codeField: EditText
    private lateinit var deviceField: EditText
    private var selectedTab = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = bg
        window.navigationBarColor = bg
        window.decorView.systemUiVisibility = 0
        buildShell()
        showPage(0)
    }

    private fun buildShell() {
        val frame = FrameLayout(this)
        frame.addView(CyberBackground(this), FrameLayout.LayoutParams(-1, -1))
        val shell = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(18), dp(18), dp(18), dp(8))
        }
        val header = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER_VERTICAL }
        val mark = TextView(this).apply {
            text = "KV"; textSize = 17f; typeface = mono(true); gravity = Gravity.CENTER
            setTextColor(cyan); background = rounded(Color.rgb(5, 31, 43), cyan, 12)
            setPadding(dp(13), dp(11), dp(13), dp(11))
        }
        header.addView(mark)
        val heading = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(dp(11), 0, 0, 0) }
        heading.addView(txt("KICRUTZ-VOID", 16f, Color.WHITE, true, true))
        heading.addView(txt("REMOTE DEVICE CONTROL CENTER", 8f, muted, true, true))
        header.addView(heading, LinearLayout.LayoutParams(0, -2, 1f))
        header.addView(txt("v3.7", 10f, purple, true, true).apply {
            background = rounded(Color.rgb(25, 12, 40), Color.rgb(90, 45, 130), 8)
            setPadding(dp(9), dp(6), dp(9), dp(6))
        })
        shell.addView(header)
        val line = View(this).apply { setBackgroundColor(border) }
        shell.addView(line, LinearLayout.LayoutParams(-1, dp(1)).apply { topMargin = dp(15); bottomMargin = dp(13) })

        val connectionStrip = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            background = rounded(Color.rgb(5, 13, 24), border, 12)
            setPadding(dp(12), dp(10), dp(12), dp(10))
        }
        val row = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER_VERTICAL }
        row.addView(txt("RELAY", 9f, muted, true, true))
        row.addView(txt("  •  ", 10f, muted, true))
        statusText = txt("OFFLINE", 10f, crimson, true, true)
        row.addView(statusText)
        row.addView(txt("                         ", 8f, muted))
        row.addView(txt("TARGET", 9f, muted, true, true))
        row.addView(txt("  LOCAL DEVICE", 9f, cyan, true, true))
        connectionStrip.addView(row)
        val connectBtn = miniButton("CONFIGURE RELAY  ↗", cyan)
        connectBtn.setOnClickListener { showPage(1) }
        connectionStrip.addView(connectBtn, LinearLayout.LayoutParams(-1, dp(34)).apply { topMargin = dp(9) })
        shell.addView(connectionStrip)

        content = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        val scroll = ScrollView(this).apply {
            isFillViewport = false
            addView(content)
        }
        shell.addView(scroll, LinearLayout.LayoutParams(0, 0, 1f).apply { topMargin = dp(12) })

        val nav = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            background = rounded(Color.rgb(5, 12, 23), border, 14)
            setPadding(dp(4), dp(5), dp(4), dp(5))
        }
        val navNames = listOf("UTAMA", "CLIP", "FILE", "NOTIF")
        val navIcons = listOf("⌂", "▣", "▤", "◉")
        navNames.forEachIndexed { index, name ->
            val item = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL; gravity = Gravity.CENTER
                setPadding(0, dp(5), 0, dp(5))
                setOnClickListener { showPage(index) }
            }
            item.addView(txt(navIcons[index], 17f, if (index == selectedTab) cyan else muted, true, true))
            item.addView(txt(name, 8f, if (index == selectedTab) cyan else muted, true, true))
            nav.addView(item, LinearLayout.LayoutParams(0, dp(48), 1f))
        }
        shell.addView(nav, LinearLayout.LayoutParams(-1, -2).apply { topMargin = dp(9) })
        frame.addView(shell, FrameLayout.LayoutParams(-1, -1))
        setContentView(frame)
    }

    private fun showPage(page: Int) {
        selectedTab = page
        content.removeAllViews()
        when (page) {
            0 -> showHome()
            1 -> showClipInfo()
            2 -> showFileInfo()
            3 -> showNotifInfo()
            else -> showHome()
        }
    }

    private fun showHome() {
        content.addView(card {
            addView(txt("SYSTEM OVERVIEW  /  01", 9f, cyan, true, true))
            addView(txt("Remote Support\nDashboard", 24f, Color.WHITE, true), lp(top = 8))
            addView(txt("Monitor status koneksi perangkat secara transparan.", 11f, muted), lp(top = 6))
            val status = LinearLayout(this@MainActivity).apply {
                orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER_VERTICAL
                background = rounded(Color.rgb(26, 10, 22), Color.rgb(90, 35, 50), 9)
                setPadding(dp(10), dp(8), dp(10), dp(8))
            }
            status.addView(txt("●", 12f, crimson, true))
            status.addView(txt("  DISCONNECTED", 10f, crimson, true, true))
            status.addView(txt("   ·   waiting for session", 9f, muted, true))
            addView(status, lp(top = 14))
        })
        content.addView(card {
            addView(txt("QUICK METRICS", 9f, muted, true, true))
            val metrics = LinearLayout(this@MainActivity).apply { orientation = LinearLayout.HORIZONTAL }
            metrics.addView(metric("SESSION", "OFF", crimson, "No active link"), LinearLayout.LayoutParams(0, -2, 1f))
            metrics.addView(metric("DEVICE", "LOCAL", cyan, "This device"), LinearLayout.LayoutParams(0, -2, 1f))
            metrics.addView(metric("PRIVACY", "LOCKED", green, "User consent"), LinearLayout.LayoutParams(0, -2, 1f))
            addView(metrics, lp(top = 12))
        })
        content.addView(card {
            addView(txt("CONTROL PANEL", 9f, muted, true, true))
            addView(txt("Connection setup", 16f, Color.WHITE, true), lp(top = 7))
            addView(txt("Configure relay URL and pairing details to start an explicitly approved support session.", 11f, muted), lp(top = 5))
            val b = actionButton("OPEN CONNECTION SETTINGS", cyan)
            b.setOnClickListener { showPage(1) }
            addView(b, lp(top = 12))
        })
        content.addView(card {
            addView(txt("SECURITY NOTICE", 9f, crimson, true, true))
            addView(txt("Session hanya aktif setelah tindakan Connect. Aplikasi tidak mengambil clipboard, file pribadi, kamera, mikrofon, atau mengendalikan layar.", 11f, Color.rgb(190, 205, 220)), lp(top = 8))
        })
    }

    private fun showConnection() {
        content.addView(card {
            addView(txt("RELAY CONFIGURATION  /  02", 9f, cyan, true, true))
            addView(txt("Connect to support relay", 20f, Color.WHITE, true), lp(top = 8))
            addView(txt("Gunakan server WSS terenkripsi dan kode pairing yang diberikan secara aman.", 11f, muted), lp(top = 6))
            urlField = field("wss://relay.example/ws")
            codeField = field("Pairing code").apply { inputType = 129 }
            deviceField = field("Device label")
            addView(urlField, lp(top = 14)); addView(codeField, lp(top = 8)); addView(deviceField, lp(top = 8))
            val connect = actionButton("START SESSION", cyan)
            connect.setOnClickListener {
                val url = urlField.text.toString().trim()
                val code = codeField.text.toString().trim()
                val device = deviceField.text.toString().trim()
                if (url.isBlank() || code.isBlank() || device.isBlank()) { toast("Isi semua kolom terlebih dahulu"); return@setOnClickListener }
                if (!url.startsWith("wss://")) { toast("Relay wajib menggunakan WSS"); return@setOnClickListener }
                val intent = Intent(this@MainActivity, SupportService::class.java).apply {
                    putExtra("url", url); putExtra("code", code); putExtra("id", device)
                }
                startForegroundService(intent)
                statusText.text = "CONNECTING"; statusText.setTextColor(cyan)
                toast("Sesi dimulai — periksa notifikasi layanan")
                showPage(0)
            }
            addView(connect, lp(top = 14))
            val stop = actionButton("STOP SESSION", crimson)
            stop.setOnClickListener {
                stopService(Intent(this@MainActivity, SupportService::class.java))
                statusText.text = "OFFLINE"; statusText.setTextColor(crimson)
                toast("Sesi dihentikan")
            }
            addView(stop, lp(top = 8))
        })
        content.addView(card {
            addView(txt("PAIRING CHECKLIST", 9f, muted, true, true))
            addView(txt("• Pastikan pemilik perangkat menyetujui sesi.\n• Jangan membagikan pairing code.\n• Putuskan sesi jika tidak lagi diperlukan.", 11f, Color.rgb(190, 205, 220)), lp(top = 8))
        })
    }

    private fun showClipInfo() {
        content.addView(card {
            addView(txt("CLIPBOARD MODULE  /  03", 9f, cyan, true, true))
            addView(txt("Clipboard access disabled", 18f, Color.WHITE, true), lp(top = 8))
            addView(txt("Untuk melindungi data sensitif, aplikasi ini tidak membaca atau mengirim clipboard perangkat. Tab ini hanya menampilkan status modul.", 12f, muted), lp(top = 8))
            addView(txt("MODULE STATUS   •   LOCKED", 10f, green, true, true), lp(top = 14))
        })
    }

    private fun showFileInfo() {
        content.addView(card {
            addView(txt("FILE MODULE  /  04", 9f, cyan, true, true))
            addView(txt("Private files are not accessible", 18f, Color.WHITE, true), lp(top = 8))
            addView(txt("Tidak ada browser file jarak jauh. Pengguna dapat mengelola file lokal melalui aplikasi file Android sendiri.", 12f, muted), lp(top = 8))
            addView(txt("MODULE STATUS   •   DISABLED", 10f, green, true, true), lp(top = 14))
        })
    }

    private fun showNotifInfo() {
        content.addView(card {
            addView(txt("NOTIFICATION MODULE  /  05", 9f, cyan, true, true))
            addView(txt("Session notifications", 18f, Color.WHITE, true), lp(top = 8))
            addView(txt("Notifikasi sistem Android akan digunakan untuk memberi tahu saat layanan dukungan berjalan atau dihentikan.", 12f, muted), lp(top = 8))
            addView(txt("TRANSPARENCY   •   ENABLED", 10f, green, true, true), lp(top = 14))
        })
        content.addView(card {
            addView(txt("ACTIVITY LOG", 9f, muted, true, true))
            addView(txt("No activity recorded in this screen.", 11f, muted), lp(top = 8))
        })
    }

    private fun card(block: LinearLayout.() -> Unit): LinearLayout = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        setPadding(dp(14), dp(14), dp(14), dp(14))
        background = rounded(panel, border, 15)
        block()
    }

    private fun metric(name: String, value: String, accent: Int, caption: String) = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        background = rounded(Color.rgb(5, 12, 22), border, 10)
        setPadding(dp(9), dp(10), dp(8), dp(9))
        addView(txt(name, 8f, muted, true, true))
        addView(txt(value, 15f, accent, true, true), lp(top = 6))
        addView(txt(caption, 8f, muted), lp(top = 3))
    }

    private fun field(hintText: String) = EditText(this).apply {
        hint = hintText; textSize = 12f; setTextColor(Color.WHITE); setHintTextColor(muted)
        setSingleLine(true); setPadding(dp(12), dp(11), dp(12), dp(11))
        background = rounded(Color.rgb(3, 8, 16), border, 10)
    }

    private fun actionButton(text: String, accent: Int) = Button(this).apply {
        this.text = text; textSize = 10f; isAllCaps = false; typeface = mono(true)
        setTextColor(accent); background = rounded(Color.rgb(5, 20, 31), accent, 10)
    }

    private fun miniButton(text: String, accent: Int) = Button(this).apply {
        this.text = text; textSize = 9f; isAllCaps = false; typeface = mono(true)
        setTextColor(accent); background = rounded(Color.rgb(4, 18, 28), accent, 8)
    }

    private fun txt(text: String, size: Float, color: Int, bold: Boolean = false, monospace: Boolean = false) = TextView(this).apply {
        this.text = text; textSize = size; setTextColor(color)
        typeface = if (monospace) mono(bold) else if (bold) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
    }

    private fun mono(bold: Boolean) = Typeface.create("monospace", if (bold) Typeface.BOLD else Typeface.NORMAL)
    private fun rounded(fill: Int, stroke: Int, radius: Int) = GradientDrawable().apply {
        setColor(fill); setStroke(dp(1), stroke); cornerRadius = dp(radius).toFloat()
    }
    private fun lp(top: Int = 0, bottom: Int = 0) = LinearLayout.LayoutParams(-1, -2).apply {
        if (top > 0) topMargin = dp(top); if (bottom > 0) bottomMargin = dp(bottom)
    }
    private fun dp(v: Int) = (v * resources.displayMetrics.density).toInt()
    private fun toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

    private inner class CyberBackground(context: MainActivity) : View(context) {
        private val gridPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.argb(18, 34, 211, 238); strokeWidth = dp(1).toFloat() }
        private val scanPaint = Paint().apply { color = Color.argb(9, 160, 190, 220) }
        private val glowPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)
            canvas.drawColor(bg)
            glowPaint.shader = LinearGradient(0f, 0f, width.toFloat(), height.toFloat(),
                intArrayOf(Color.argb(45, 0, 170, 210), Color.argb(5, 70, 40, 130), Color.TRANSPARENT),
                null, Shader.TileMode.CLAMP)
            canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), glowPaint)
            val spacing = dp(22).toFloat()
            var x = 0f
            while (x < width) { canvas.drawLine(x, 0f, x, height.toFloat(), gridPaint); x += spacing }
            var y = 0f
            while (y < height) { canvas.drawLine(0f, y, width.toFloat(), y, gridPaint); y += spacing }
            var sy = 0f
            while (sy < height) { canvas.drawRect(0f, sy, width.toFloat(), sy + dp(1), scanPaint); sy += dp(4) }
        }
    }
}
