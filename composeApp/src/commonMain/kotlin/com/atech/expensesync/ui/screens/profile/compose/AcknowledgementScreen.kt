package com.atech.expensesync.ui.screens.profile.compose

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.atech.expensesync.component.MainContainer
import com.atech.expensesync.ui.theme.ExpenseSyncTheme
import com.atech.expensesync.ui.theme.spacing
import expensesync.composeapp.generated.resources.Res
import expensesync.composeapp.generated.resources.ic_credits
import org.jetbrains.compose.resources.painterResource


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AcknowledgementScreen(
    modifier: Modifier = Modifier,
    onNavigationClick: () -> Unit = {},
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    MainContainer(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        title = "Acknowledgement",
        onNavigationClick = onNavigationClick,
        scrollBehavior = scrollBehavior,
    ){paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = paddingValues
        ) {
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MaterialTheme.spacing.medium)
                        .clip(MaterialTheme.shapes.large)
                        .clickable { }
                        .clearAndSetSemantics { },
                    color = MaterialTheme.colorScheme.primary.copy(
                        alpha = 0.2f
                    )
                ) {
                    Image(
                        modifier = Modifier
                            .aspectRatio(2.38f),
                        painter = painterResource(Res.drawable.ic_credits),
                        contentDescription = null
                    )
                }
            }
            items(credits) {
                CreditItem(
                    title = it.title,
                    license = it.licenses.joinToString { it1 -> it1.title },
                    onClick = {

                    }
                )
            }
        }
    }
}

@Composable
fun CreditItem(
    title: String,
    license: String? = null,
    onClick: () -> Unit = {},
) {
    Surface(modifier = Modifier.clickable { onClick() }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 10.dp)
            ) {
                with(MaterialTheme) {
                    Text(
                        text = title,
                        maxLines = 1,
                        style = typography.titleMedium,
                        color = colorScheme.onSurface
                    )
                    license?.let {
                        Text(
                            text = it,
                            color = colorScheme.onSurfaceVariant,
                            maxLines = 2, overflow = TextOverflow.Ellipsis,
                            style = typography.bodyMedium,
                        )
                    }
                }
            }
        }
    }

}

enum class Licenses(val title: String) {
    APACHE_2_0("Apache 2.0"), MIT(
        "MIT"
    )
}

data class Credit(
    val title: String, val url: String, val licenses: List<Licenses> = listOf()
)

private val credits = listOf<Credit>(
    // Android Libraries
    Credit("AndroidX Activity Compose", "https://developer.android.com/jetpack/androidx/releases/activity", listOf(Licenses.APACHE_2_0)),
    Credit("AndroidX Appcompat", "https://developer.android.com/jetpack/androidx/releases/appcompat", listOf(Licenses.APACHE_2_0)),
    Credit("AndroidX ConstraintLayout", "https://developer.android.com/jetpack/androidx/releases/constraintlayout", listOf(Licenses.APACHE_2_0)),
    Credit("AndroidX Core KTX", "https://developer.android.com/jetpack/androidx/releases/core", listOf(Licenses.APACHE_2_0)),
    Credit("AndroidX Espresso Core", "https://developer.android.com/jetpack/androidx/releases/test", listOf(Licenses.APACHE_2_0)),
    Credit("AndroidX Lifecycle", "https://developer.android.com/jetpack/androidx/releases/lifecycle", listOf(Licenses.APACHE_2_0)),
    Credit("AndroidX Material", "https://github.com/material-components/material-components-android", listOf(Licenses.APACHE_2_0)),
    Credit("AndroidX Test JUnit", "https://developer.android.com/jetpack/androidx/releases/test", listOf(Licenses.APACHE_2_0)),
    Credit("AndroidX Navigation Compose", "https://developer.android.com/jetpack/androidx/releases/navigation", listOf(Licenses.APACHE_2_0)),
    Credit("AndroidX Room", "https://developer.android.com/jetpack/androidx/releases/room", listOf(Licenses.APACHE_2_0)),
    Credit("AndroidX DataStore", "https://developer.android.com/jetpack/androidx/releases/datastore", listOf(Licenses.APACHE_2_0)),
    Credit("AndroidX Credentials", "https://developer.android.com/jetpack/androidx/releases/credentials", listOf(Licenses.APACHE_2_0)),
    Credit("AndroidX SQLite", "https://developer.android.com/jetpack/androidx/releases/sqlite", listOf(Licenses.APACHE_2_0)),

    // Compose & UI
    Credit("Compose Multiplatform", "https://github.com/JetBrains/compose-multiplatform", listOf(Licenses.APACHE_2_0)),
    Credit("Accompanist SystemUI Controller", "https://github.com/google/accompanist", listOf(Licenses.APACHE_2_0)),
    Credit("Material Navigation", "https://developer.android.com/jetpack/compose/navigation", listOf(Licenses.APACHE_2_0)),
    Credit("Adaptive Components", "https://developer.android.com/jetpack/compose/layouts/material3-adaptive", listOf(Licenses.APACHE_2_0)),
    Credit("Adaptive Layout", "https://developer.android.com/jetpack/compose/layouts/material3-adaptive", listOf(Licenses.APACHE_2_0)),
    Credit("Adaptive Navigation", "https://developer.android.com/jetpack/compose/layouts/material3-adaptive", listOf(Licenses.APACHE_2_0)),
    Credit("Jetlime", "https://github.com/pushpalroy/jetlime", listOf(Licenses.APACHE_2_0)),
    Credit("Calendar", "https://github.com/kizitonwose/Calendar", listOf(Licenses.MIT)),
    Credit("Chart", "https://github.com/thechance101/chart", listOf(Licenses.APACHE_2_0)),

    // Networking & Data
    Credit("Ktor", "https://github.com/ktorio/ktor", listOf(Licenses.APACHE_2_0)),
    Credit("Coil Compose", "https://github.com/coil-kt/coil", listOf(Licenses.APACHE_2_0)),
    Credit("Coil Network OkHttp", "https://github.com/coil-kt/coil", listOf(Licenses.APACHE_2_0)),
    Credit("Landscapist Coil3", "https://github.com/skydoves/landscapist", listOf(Licenses.APACHE_2_0)),
    Credit("Gson", "https://github.com/google/gson", listOf(Licenses.APACHE_2_0)),

    // Dependency Injection
    Credit("Koin Core", "https://github.com/InsertKoinIO/koin", listOf(Licenses.APACHE_2_0)),
    Credit("Koin Compose", "https://github.com/InsertKoinIO/koin", listOf(Licenses.APACHE_2_0)),
    Credit("Koin Compose Multiplatform", "https://github.com/InsertKoinIO/koin", listOf(Licenses.APACHE_2_0)),
    Credit("Koin Compose ViewModel", "https://github.com/InsertKoinIO/koin", listOf(Licenses.APACHE_2_0)),

    // Firebase
    Credit("Firebase BOM", "https://firebase.google.com/", listOf(Licenses.APACHE_2_0)),
    Credit("Firebase Admin", "https://firebase.google.com/docs/admin/setup", listOf(Licenses.APACHE_2_0)),
    Credit("Firebase Analytics", "https://firebase.google.com/products/analytics", listOf(Licenses.APACHE_2_0)),
    Credit("Firebase Auth", "https://firebase.google.com/products/auth", listOf(Licenses.APACHE_2_0)),
    Credit("Firebase Firestore", "https://firebase.google.com/products/firestore", listOf(Licenses.APACHE_2_0)),
    Credit("Firebase Messaging", "https://firebase.google.com/products/cloud-messaging", listOf(Licenses.APACHE_2_0)),

    // Kotlin Libraries
    Credit("Kotlin", "https://github.com/JetBrains/kotlin", listOf(Licenses.APACHE_2_0)),
    Credit("Kotlinx Coroutines", "https://github.com/Kotlin/kotlinx.coroutines", listOf(Licenses.APACHE_2_0)),
    Credit("Kotlinx Datetime", "https://github.com/Kotlin/kotlinx-datetime", listOf(Licenses.APACHE_2_0)),

    // Utility Libraries
    Credit("ZXing Core", "https://github.com/zxing/zxing", listOf(Licenses.APACHE_2_0)),
    Credit("Scanner", "https://github.com/kalinjul/EasyQRScan", listOf(Licenses.APACHE_2_0)),
    Credit("LogBack", "https://github.com/qos-ch/logback", listOf(Licenses.MIT, Licenses.APACHE_2_0)),
    Credit("JUnit", "https://github.com/junit-team/junit4", listOf(Licenses.MIT)),
    Credit("PerfMark API", "https://github.com/perfmark/perfmark", listOf(Licenses.APACHE_2_0)),
    Credit("PerfMark Implementation", "https://github.com/perfmark/perfmark", listOf(Licenses.APACHE_2_0)),
    Credit("Google ID", "https://developers.google.com/identity", listOf(Licenses.APACHE_2_0))
)

@Preview()
@Composable
private fun AcknowledgementScreenPreview() {
    ExpenseSyncTheme {
        AcknowledgementScreen()
    }
}