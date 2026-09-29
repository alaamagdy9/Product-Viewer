package com.nweave.productviewer.ui.common

import androidx.core.text.HtmlCompat

// The descriptions come from the server with HTML in them (<br />, &amp; ...),
// so they are parsed instead of being printed as they are.
object HtmlUtils {

    @JvmStatic
    fun fromHtml(html: String?): CharSequence =
        if (html == null) "" else HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_COMPACT).trim()
}