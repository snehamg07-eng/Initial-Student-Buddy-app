package com.snehamg.studentbuddy

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.os.Bundle
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import android.app.DatePickerDialog
import androidx.compose.ui.graphics.asImageBitmap
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.horizontalScroll
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.Shapes
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.foundation.clickable
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch



import android.widget.Toast


import android.app.TimePickerDialog
import androidx.activity.compose.rememberLauncherForActivityResult
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver

import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ButtonDefaults





import java.util.concurrent.TimeUnit

import kotlinx.coroutines.delay
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.floor

data class StudentProfile(
    val name: String = "",
    val usn: String = "",
    val college: String = "",
    val photoUri: String = "",
    val branch: String = "",
    val year: String = "",
    val semester: String = "",
    val section: String = "",
    val father: String = "",
    val mother: String = "",
    val address: String = "",
    val pin: String = "",
    val bloodGroup:String="",
)

data class Subject(
    val name: String,
    val faculty: String,
    val day: String,
    val startTime: String,
    val endTime: String,
    val classType:  String = "Theory",
    val room : String = " "
)

data class AttendanceSubject(
    val name: String,
    val days: List<String>
)

data class AttendanceRecord(
    val subject: String,
    val date: String,
    val status: String // PRESENT, ABSENT, NO_CLASS
)

data class Assignment(
    val title: String,
    val subject: String,
    val dueDate: String,
    val type: String = "Other",
    val description: String = "",
    val dueTime: String = "",
    val priority: String = "Medium",
    val completed: Boolean = false,
    val attachmentUri: String = ""
)

data class Note(
    val title: String,
    val content: String
)
data class AcademicRecord(

    val semester: String,
    val subject: String,
    val subjectCode: String = "",
    val credits: String = "",
    val internalMarks: String = "",
    val assignmentMarks: String = "",
    val practicalMarks: String = "",
    val theoryMarks: String = "",
    val totalMarks: String = "",
    val grade: String = "",
    val result: String = "Pass",
    val id:Long = System.currentTimeMillis(),
)

data class Expense(
    val title: String,
    val amount: String,
    val date: String
)

object StudentBuddyStorage {

    private const val PREFS = "student_buddy_prefs"

    private fun prefs(context: Context) =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    fun saveProfile(context: Context, p: StudentProfile) {
        val o = JSONObject()
        o.put("name", p.name)
        o.put("usn", p.usn)
        o.put("college", p.college)
        o.put("photoUri", p.photoUri)
        o.put("branch", p.branch)
        o.put("year", p.year)
        o.put("semester", p.semester)
        o.put("section", p.section)
        o.put("father", p.father)
        o.put("mother", p.mother)
        o.put("address", p.address)
        o.put("pin", p.pin)
        o.put("bloodGroup", p.bloodGroup)
        prefs(context).edit().putString("profile", o.toString()).apply()
    }

    fun loadProfile(context: Context): StudentProfile {
        val s = prefs(context).getString("profile", null) ?: return StudentProfile()
        return try {
            val o = JSONObject(s)
            StudentProfile(
                o.optString("name"),
                o.optString("usn"),
                o.optString("college"),
                o.optString("photoUri"),
                o.optString("branch"),
                o.optString("year"),
                o.optString("semester"),
                o.optString("section"),
                o.optString("father"),
                o.optString("mother"),
                o.optString("address"),
                o.optString("pin"),
                bloodGroup = o.optString("bloodGroup")
            )
        } catch (_: Exception) {
            StudentProfile()
        }
    }

    fun saveSubjects(context: Context, list: List<Subject>) {
        val a = JSONArray()
        list.forEach {
            a.put(JSONObject().apply {
                put("name", it.name)
                put("day", it.day)
                put("start", it.startTime)
                put("end", it.endTime)
            })
        }
        prefs(context).edit().putString("subjects", a.toString()).apply()
    }

    fun loadSubjects(context: Context): List<Subject> {
        val s = prefs(context).getString("subjects", null) ?: return emptyList()
        return try {
            val a = JSONArray(s)
            buildList {
                for (i in 0 until a.length()) {
                    val o = a.getJSONObject(i)
                    add(
                        Subject(
                            name = o.optString("name"),
                            faculty = o.optString("faculty"),
                            day = o.optString("day"),
                            startTime = o.optString("start"),
                            endTime = o.optString("end")
                        )
                    )
                }
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    fun saveAttendanceSubjects(context: Context, list: List<AttendanceSubject>) {
        val a = JSONArray()
        list.forEach { subject ->
            a.put(JSONObject().apply {
                put("name", subject.name)
                put("days", JSONArray(subject.days))
            })
        }
        prefs(context).edit().putString("attendance_subjects", a.toString()).apply()
    }

    fun loadAttendanceSubjects(context: Context): List<AttendanceSubject> {
        val s = prefs(context).getString("attendance_subjects", null) ?: return emptyList()
        return try {
            val a = JSONArray(s)
            buildList {
                for (i in 0 until a.length()) {
                    val o = a.getJSONObject(i)
                    val daysArray = o.optJSONArray("days")
                    val days = if (daysArray != null) {
                        buildList {
                            for (j in 0 until daysArray.length()) add(daysArray.optString(j))
                        }
                    } else {
                        emptyList()
                    }
                    add(AttendanceSubject(o.optString("name"), days))
                }
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    fun saveAttendance(context: Context, list: List<AttendanceRecord>) {
        val a = JSONArray()
        list.forEach {
            a.put(JSONObject().apply {
                put("subject", it.subject)
                put("date", it.date)
                put("status", it.status)
            })
        }
        prefs(context).edit().putString("attendance", a.toString()).apply()
    }

    fun loadAttendance(context: Context): List<AttendanceRecord> {
        val s = prefs(context).getString("attendance", null) ?: return emptyList()
        return try {
            val a = JSONArray(s)
            buildList {
                for (i in 0 until a.length()) {
                    val o = a.getJSONObject(i)
                    val status = if (o.has("status")) {
                        o.optString("status", "ABSENT")
                    } else {
                        if (o.optBoolean("present", false)) "PRESENT" else "ABSENT"
                    }
                    add(AttendanceRecord(o.optString("subject"), o.optString("date"), status))
                }
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    fun saveAssignments(context: Context, list: List<Assignment>) {
        val a = JSONArray()
        list.forEach {
            a.put(JSONObject().apply {
                put("title", it.title)
                put("subject", it.subject)
                put("dueDate", it.dueDate)
            })
        }
        prefs(context).edit().putString("assignments", a.toString()).apply()
    }

    fun loadAssignments(context: Context): List<Assignment> {
        val s = prefs(context).getString("assignments", null) ?: return emptyList()
        return try {
            val a = JSONArray(s)
            buildList {
                for (i in 0 until a.length()) {
                    val o = a.getJSONObject(i)
                    add(
                        Assignment(
                            o.optString("title"),
                            o.optString("subject"),
                            o.optString("dueDate")
                        )
                    )
                }
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    fun saveNotes(context: Context, list: List<Note>) {
        val a = JSONArray()
        list.forEach {
            a.put(JSONObject().apply {
                put("title", it.title)
                put("content", it.content)
            })
        }
        prefs(context).edit().putString("notes", a.toString()).apply()
    }

    fun loadNotes(context: Context): List<Note> {
        val s = prefs(context).getString("notes", null) ?: return emptyList()
        return try {
            val a = JSONArray(s)
            buildList {
                for (i in 0 until a.length()) {
                    val o = a.getJSONObject(i)
                    add(Note(o.optString("title"), o.optString("content")))
                }
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    fun saveAcademic(context: Context, list: List<AcademicRecord>) {
        val a = JSONArray()

        list.forEach {
            a.put(
                JSONObject().apply {
                    put("id",it.id)
                    put("semester", it.semester)
                    put("subject", it.subject)
                    put("subjectCode", it.subjectCode)
                    put("credits", it.credits)
                    put("internalMarks", it.internalMarks)
                    put("assignmentMarks", it.assignmentMarks)
                    put("practicalMarks", it.practicalMarks)
                    put("theoryMarks", it.theoryMarks)
                    put("totalMarks", it.totalMarks)
                    put("grade", it.grade)
                    put("result", it.result)
                }
            )
        }

        prefs(context)
            .edit()
            .putString("academic", a.toString())
            .apply()
    }

    fun loadAcademic(context: Context): List<AcademicRecord> {
        val s = prefs(context).getString("academic", null) ?: return emptyList()
        return try {
            val a = JSONArray(s)
            buildList {
                for (i in 0 until a.length()) {
                    val o = a.getJSONObject(i)
                    add(
                        AcademicRecord(
                            o.optString("semester"),
                            o.optString("subject"),
                            o.optString("marks")
                        )
                    )
                }
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    fun saveExpenses(context: Context, list: List<Expense>) {
        val a = JSONArray()
        list.forEach {
            a.put(JSONObject().apply {
                put("title", it.title)
                put("amount", it.amount)
                put("date", it.date)
            })
        }
        prefs(context).edit().putString("expenses", a.toString()).apply()
    }

    fun loadExpenses(context: Context): List<Expense> {
        val s = prefs(context).getString("expenses", null) ?: return emptyList()
        return try {
            val a = JSONArray(s)
            buildList {
                for (i in 0 until a.length()) {
                    val o = a.getJSONObject(i)
                    add(
                        Expense(
                            o.optString("title"),
                            o.optString("amount"),
                            o.optString("date")
                        )
                    )
                }
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    fun saveLastScreen(context: Context, screen: String) {
        prefs(context).edit().putString("last_screen", screen).apply()
    }

    fun loadLastScreen(context: Context): String {
        return prefs(context).getString("last_screen", "home") ?: "home"
    }

    fun saveBrightness(context: Context, value: Float) {
        prefs(context).edit().putFloat("brightness", value).apply()
    }

    fun loadBrightness(context: Context): Float {
        return prefs(context).getFloat("brightness", 1f)
    }

    fun saveSuggestion(context: Context, text: String) {
        prefs(context).edit().putString("last_suggestion", text).apply()
    }

    fun saveBugReport(context: Context, text: String) {
        prefs(context).edit().putString("last_bug", text).apply()
    }

    fun savePhotoUri(
        context: Context,
        uri: Uri
    ): Uri? {

        return try {

            val inputStream =
                context.contentResolver.openInputStream(uri)
                    ?: return null

            val file = java.io.File(
                context.filesDir,
                "profile_photo.jpg"
            )

            java.io.FileOutputStream(file).use { output ->
                inputStream.use { input ->
                    input.copyTo(output)
                }
            }

            val savedUri =
                Uri.fromFile(file)

            prefs(context)
                .edit()
                .putString(
                    "profile_photo",
                    savedUri.toString()
                )
                .apply()

            savedUri

        } catch (_: Exception) {
            null
        }
    }

    fun loadPhotoUri(
        context: Context
    ): Uri? {

        val value =
            prefs(context)
                .getString(
                    "profile_photo",
                    null
                )

        return value?.let {
            Uri.parse(it)
        }
    }

    fun hasRegisteredUser(context: Context): Boolean {
        return prefs(context)
            .getString("login_mobile", "")
            ?.isNotBlank() == true
    }

    fun checkLogin(
        context: Context,
        mobile: String,
        password: String
    ): Boolean {
        val savedMobile = prefs(context)
            .getString("login_mobile", "")

        val savedPassword = prefs(context)
            .getString("login_password", "")

        return savedMobile == mobile && savedPassword == password
    }

    fun saveLoggedIn(
        context: Context,
        value: Boolean
    ) {
        prefs(context)
            .edit()
            .putBoolean("logged_in", value)
            .apply()
    }

    fun isLoggedIn(context: Context): Boolean {
        return prefs(context)
            .getBoolean("logged_in", false)
    }

    fun resetPassword(
        context: Context,
        mobile: String,
        newPassword: String
    ): Boolean {

        val savedMobile = prefs(context)
            .getString("login_mobile", "") ?: ""

        if (savedMobile == mobile) {
            prefs(context)
                .edit()
                .putString("login_password", newPassword)
                .apply()

            return true
        }

        return false
    }


    fun saveLoginCredentials(
        context: Context,
        mobile: String,
        password: String
    ) {
        prefs(context)
            .edit()
            .putString("login_mobile", mobile)
            .putString("login_password", password)
            .apply()


    }


    fun openEmail(context: Context, subject: String, body: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:snehamg531@gmail.com")
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, body)
        }

        try {
            context.startActivity(intent)
        } catch (_: Exception) {
            // No email app installed
        }
    }
}


fun safeCutoffText(present: Int, total: Int): String {
    if (total == 0) {
        return "No attendance recorded yet."
    }

    val maxMisses = kotlin.math.floor(
        present / 0.75 - total
    ).toInt().coerceAtLeast(0)

    return if (present.toDouble() / total < 0.75) {
        "Attendance is below 75%."
    } else if (maxMisses == 0) {
        "You cannot miss the next class without dropping below 75%."
    } else {
        "You can miss the next $maxMisses class(es) without dropping below 75%."
    }
}
object StudentBuddyNotifications {

    const val CHANNEL_ID = "student_buddy_reminders"

    fun createChannel(context: Context) {
        if (android.os.Build.VERSION.SDK_INT >= 26) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Student Buddy Reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Attendance, assignment and timetable reminders"
            }

            val manager =
                context.getSystemService(
                    Context.NOTIFICATION_SERVICE
                ) as NotificationManager

            manager.createNotificationChannel(channel)
        }
    }

    fun showNotification(
        context: Context,
        title: String,
        message: String
    ) {
        createChannel(context)

        if (
            android.os.Build.VERSION.SDK_INT >= 33 &&
            ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val notification =
            NotificationCompat.Builder(
                context,
                CHANNEL_ID
            )
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .build()

        NotificationManagerCompat
            .from(context)
            .notify(
                System.currentTimeMillis().toInt(),
                notification
            )
    }
}
fun scheduleAttendanceReminder(
    context: Context,
    subjects: List<Subject>
) {
    val alarmManager =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    subjects.forEachIndexed { index, subject ->

        if (
            subject.day.isBlank() ||
            subject.startTime.isBlank() ||
            subject.name.isBlank()
        ) {
            return@forEachIndexed
        }

        val days = listOf(
            "Sunday",
            "Monday",
            "Tuesday",
            "Wednesday",
            "Thursday",
            "Friday",
            "Saturday"
        )

        val dayNumber =
            days.indexOf(subject.day) + 1

        if (dayNumber <= 0) {
            return@forEachIndexed
        }

        val timeText =
            subject.startTime
                .trim()
                .uppercase()

        val isPM = timeText.contains("PM")

        val cleanTime =
            timeText
                .replace("AM", "")
                .replace("PM", "")
                .trim()

        val parts = cleanTime.split(":")

        if (parts.size != 2) {
            return@forEachIndexed
        }

        var hour =
            parts[0].trim().toIntOrNull()
                ?: return@forEachIndexed

        val minute =
            parts[1].trim().toIntOrNull()
                ?: return@forEachIndexed

        if (isPM && hour != 12) {
            hour += 12
        }

        if (!isPM && hour == 12) {
            hour = 0
        }

        val calendar =
            Calendar.getInstance().apply {

                set(
                    Calendar.DAY_OF_WEEK,
                    dayNumber
                )

                set(
                    Calendar.HOUR_OF_DAY,
                    hour
                )

                set(
                    Calendar.MINUTE,
                    minute
                )

                set(
                    Calendar.SECOND,
                    0
                )

                set(
                    Calendar.MILLISECOND,
                    0
                )

                if (
                    timeInMillis <=
                    System.currentTimeMillis()
                ) {
                    add(
                        Calendar.WEEK_OF_YEAR,
                        1
                    )
                }
            }

        val intent =
            Intent(
                context,
                StudentBuddyReminderReceiver::class.java
            ).apply {
                putExtra(
                    "type",
                    "attendance"
                )
                putExtra(
                    "subject",
                    subject.name
                )
            }

        val pendingIntent =
            PendingIntent.getBroadcast(
                context,
                2000 + index,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or
                        PendingIntent.FLAG_IMMUTABLE
            )

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY * 7,
            pendingIntent
        )
    }
}
class StudentBuddyReminderReceiver : BroadcastReceiver() {

    override fun onReceive(
        context: Context,
        intent: Intent
    ) {

        val type =
            intent.getStringExtra("type") ?: "general"

        val title: String
        val message: String

        when (type) {

            "attendance" -> {
                title = "Attendance Reminder"
                message = "Don't forget to update your attendance."
            }

            "assignment" -> {
                title = "Assignment Reminder"
                message = "Check your assignments and upcoming deadlines."
            }

            "timetable" -> {
                title = "Timetable Reminder"
                message = "Check your timetable for today's classes."
            }

            else -> {
                title = "Student Buddy"
                message = "Don't forget to check Student Buddy."
            }
        }

        StudentBuddyNotifications.showNotification(
            context,
            title,
            message
        )
    }
}
// ============================================================
//                 STUDENT BUDDY BLUE THEME
// Theme-only styling: all existing screens, data, navigation,
// storage, buttons, and features remain unchanged.
// ============================================================

private val StudentBuddyLightColors = lightColorScheme(
    primary = Color(0xFF1769E0),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFDCE9FF),
    onPrimaryContainer = Color(0xFF062B63),
    secondary = Color(0xFF3B82F6),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE5EEFF),
    onSecondaryContainer = Color(0xFF102E5C),
    tertiary = Color(0xFF00A7C4),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFD2F4FA),
    onTertiaryContainer = Color(0xFF00363E),
    background = Color(0xFFF5F8FD),
    onBackground = Color(0xFF101A2B),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF101A2B),
    surfaceVariant = Color(0xFFE8EEF7),
    onSurfaceVariant = Color(0xFF44546A),
    outline = Color(0xFF91A0B5)
)

private val StudentBuddyDarkColors = darkColorScheme(
    primary = Color(0xFF5C9CFF),
    onPrimary = Color(0xFF002E6B),
    primaryContainer = Color(0xFF123E7A),
    onPrimaryContainer = Color(0xFFDCE9FF),
    secondary = Color(0xFF70B7FF),
    onSecondary = Color(0xFF00315A),
    secondaryContainer = Color(0xFF16456C),
    onSecondaryContainer = Color(0xFFD7EBFF),
    tertiary = Color(0xFF4DD5E7),
    onTertiary = Color(0xFF00363E),
    tertiaryContainer = Color(0xFF07515C),
    onTertiaryContainer = Color(0xFFB6F0F6),
    background = Color(0xFF06101D),
    onBackground = Color(0xFFEAF2FF),
    surface = Color(0xFF0B1B2E),
    onSurface = Color(0xFFEAF2FF),
    surfaceVariant = Color(0xFF162A42),
    onSurfaceVariant = Color(0xFFB8C7DB),
    outline = Color(0xFF6F829C)
)

private val StudentBuddyShapes = Shapes(
    small = RoundedCornerShape(14.dp),
    medium = RoundedCornerShape(18.dp),
    large = RoundedCornerShape(24.dp)
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (android.os.Build.VERSION.SDK_INT >= 33) {
            requestPermissions(
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                1001
            )
        }
        setContent {
            var themeMode by rememberSaveable {
                mutableStateOf("system")
            }

            val isDark = when (themeMode) {
                "dark" -> true
                "light" -> false
                else -> isSystemInDarkTheme()
            }

            MaterialTheme(
                colorScheme = if (isDark) {
                    StudentBuddyDarkColors
                } else {
                    StudentBuddyLightColors
                },
                shapes = StudentBuddyShapes
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    StudentBuddyApp(
                        themeMode = themeMode,
                        onThemeChange = {
                            themeMode = it
                        }
                    )
                }
            }
        }
    }

    @Composable
    fun StudentBuddyApp(
        themeMode: String,
        onThemeChange: (String) -> Unit
    ) {
        val context = LocalContext.current
        val activity = context as? Activity

        var splashFinished by remember { mutableStateOf(false) }
        var restoring by remember { mutableStateOf(true) }
        var currentScreen by remember { mutableStateOf("profile") }
        var loggedIn by remember {
            mutableStateOf(StudentBuddyStorage.isLoggedIn(context))
        }


        var profile by remember { mutableStateOf(StudentBuddyStorage.loadProfile(context)) }
        val subjects = remember {
            mutableStateListOf<Subject>().apply {
                addAll(StudentBuddyStorage.loadSubjects(context))
            }
        }
        val attendanceSubjects = remember {
            mutableStateListOf<AttendanceSubject>().apply {
                addAll(StudentBuddyStorage.loadAttendanceSubjects(context))
            }
        }
        val attendance = remember {
            mutableStateListOf<AttendanceRecord>().apply {
                addAll(StudentBuddyStorage.loadAttendance(context))
            }
        }
        val assignments = remember {
            mutableStateListOf<Assignment>().apply {
                addAll(StudentBuddyStorage.loadAssignments(context))
            }
        }
        val notes = remember {
            mutableStateListOf<Note>().apply {
                addAll(StudentBuddyStorage.loadNotes(context))
            }
        }
        val academic = remember {
            mutableStateListOf<AcademicRecord>().apply {
                addAll(StudentBuddyStorage.loadAcademic(context))
            }
        }
        val expenses = remember {
            mutableStateListOf<Expense>().apply {
                addAll(StudentBuddyStorage.loadExpenses(context))
            }
        }

        var brightness by remember {
            mutableFloatStateOf(StudentBuddyStorage.loadBrightness(context))
        }

        LaunchedEffect(Unit) {
            delay(2300)
            splashFinished = true
            delay(300)

            currentScreen = if (profile.name.isBlank()) {
                "profile"
            } else {
                StudentBuddyStorage.loadLastScreen(context)
            }
            restoring = false
        }

        LaunchedEffect(currentScreen, restoring) {
            if (!restoring && splashFinished) {
                StudentBuddyStorage.saveLastScreen(context, currentScreen)
            }
        }

        fun setBrightness(value: Float) {
            brightness = value.coerceIn(0.1f, 1f)
            StudentBuddyStorage.saveBrightness(context, brightness)
            val window = activity?.window ?: return
            val params = window.attributes
            params.screenBrightness = brightness
            window.attributes = params
        }

        if (!splashFinished || restoring) {
            SplashScreen()
            return
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {

            if(!loggedIn) {
                LoginScreen(
                    context = context,
                    onLoginSuccess = {
                        StudentBuddyStorage.saveLoggedIn(context, true)
                        loggedIn = true
                        currentScreen = "profile"
                    }
                )
            } else {
                when (currentScreen) {

                    "settings" -> AppearanceScreen(
                        currentTheme = themeMode,
                        onThemeChange = onThemeChange,
                        onBack = {
                            currentScreen = "home"
                        }
                    )

                    "reminders" -> ReminderScreen(
                        subjects = subjects,
                        onBack = {
                            currentScreen = "home"
                        }
                    )
                    "login" -> LoginScreen(
                        context = context,
                        onLoginSuccess = {
                            StudentBuddyStorage.saveLoggedIn(context, true)
                            loggedIn = true
                            currentScreen = "profile"
                        }
                    )

                    "profile" -> ProfileScreen(
                        profile = profile,
                        onSave = {
                            profile = it
                            StudentBuddyStorage.saveProfile(context, it)
                            currentScreen = "home"
                        },
                        onNext = {
                            currentScreen = "home"
                        }
                    )

                    "home" -> HomeScreen(
                        profile = profile,
                        primary = MaterialTheme.colorScheme.primary,
                        context = LocalContext.current,
                        onNavigate = { currentScreen = it },
                        onLogout = {
                            currentScreen = "login"
                        },
                        onTheme = onThemeChange
                    )
                    "settings" -> {
                        AppearanceScreen(
                            currentTheme = themeMode,
                            onThemeChange = onThemeChange,
                            onBack = {
                                currentScreen = "home"
                            }
                        )
                    }
                    "overall" -> OverallAttendanceScreen(
                        attendance = attendance,
                        onBack = {
                            currentScreen = "home"
                        }
                    )

                    "attendance" -> AttendanceScreen(
                        attendanceSubjects = attendanceSubjects,
                        attendance = attendance,
                        onBack = { currentScreen = "home" },
                        onEditSubject = { oldName, updatedSubject ->
                            val cleanName = updatedSubject.name.trim()

                            if (cleanName.isNotEmpty() && updatedSubject.days.isNotEmpty()) {
                                val index = attendanceSubjects.indexOfFirst {
                                    it.name.equals(oldName, true)
                                }

                                if (index >= 0) {
                                    attendanceSubjects[index] = AttendanceSubject(
                                        cleanName,
                                        updatedSubject.days.distinct()
                                    )

                                    for (i in attendance.indices) {
                                        if (attendance[i].subject.equals(oldName, true)) {
                                            attendance[i] = attendance[i].copy(
                                                subject = cleanName
                                            )
                                        }
                                    }

                                    StudentBuddyStorage.saveAttendanceSubjects(
                                        context,
                                        attendanceSubjects
                                    )

                                    StudentBuddyStorage.saveAttendance(
                                        context,
                                        attendance
                                    )
                                }
                            }
                        },
                        onAddSubject = { subject ->
                            val cleanName = subject.name.trim()
                            if (cleanName.isNotEmpty() && subject.days.isNotEmpty()) {
                                val index =
                                    attendanceSubjects.indexOfFirst {
                                        it.name.equals(
                                            cleanName,
                                            true
                                        )
                                    }
                                val item =
                                    AttendanceSubject(cleanName, subject.days.distinct())
                                if (index >= 0) {
                                    attendanceSubjects[index] = item
                                } else {
                                    attendanceSubjects.add(item)
                                }
                                StudentBuddyStorage.saveAttendanceSubjects(
                                    context,
                                    attendanceSubjects
                                )
                            }
                        },
                        onDeleteSubject = { subjectName ->
                            attendanceSubjects.removeAll {
                                it.name.equals(subjectName, ignoreCase = true)
                            }
                        },
                        onSetAttendance = { subject, date, status ->
                            val index = attendance.indexOfFirst {
                                it.subject.equals(subject, true) && it.date == date
                            }
                            val record = AttendanceRecord(subject, date, status)
                            if (index >= 0) attendance[index] = record else attendance.add(
                                record
                            )
                            StudentBuddyStorage.saveAttendance(context, attendance)
                        }
                    )

                    "timetable" -> TimetableScreen(
                        subjects = subjects,
                        onBack = { currentScreen = "home" },
                        onSave = {
                            StudentBuddyStorage.saveSubjects(context, subjects)
                        }
                    )

                    "assignments" -> AssignmentsScreen(
                        assignments = assignments,
                        onBack = { currentScreen = "home" },
                        onSave = {
                            assignments.clear()
                            assignments.addAll(it)
                            StudentBuddyStorage.saveAssignments(context, assignments)
                        }
                    )

                    "notes" -> NotesScreen(
                        notes = notes,
                        onBack = { currentScreen = "home" },
                        onSave = {
                            notes.clear()
                            notes.addAll(it)
                            StudentBuddyStorage.saveNotes(context, notes)
                        }
                    )

                    "academic" -> AcademicScreen(
                        records = academic,
                        onBack = { currentScreen = "home" },
                        onSave = {
                            academic.clear()
                            academic.addAll(it)
                            StudentBuddyStorage.saveAcademic(context, academic)
                        }
                    )

                    "expenses" -> ExpensesScreen(
                        onBack = {
                            currentScreen = "home"
                        }
                    )

                    "overall_attendance" -> OverallAttendanceScreen(
                        attendance = attendance,
                        onBack = { currentScreen = "home" }
                    )

                    "all_subjects" -> AllSubjectsScreen(
                        subjects = attendanceSubjects,
                        attendance = attendance,
                        onBack = { currentScreen = "home" }
                    )

                    "suggestion" -> SuggestionScreen(
                        onBack = { currentScreen = "home" },
                        onSave = {
                            StudentBuddyStorage.saveSuggestion(context, it)
                        }
                    )

                    "bug" -> BugReportScreen(
                        onBack = { currentScreen = "home" },
                        onSave = {
                            StudentBuddyStorage.saveBugReport(context, it)
                        }
                    )

                    else -> currentScreen = "home"
                }
            }
        }
    }

    @Composable
    fun SplashScreen() {
        var visibleLetters by remember { mutableStateOf(0) }
        var blink by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            val title = "STUDENT BUDDY"
            for (i in 1..title.length) {
                visibleLetters = i
                blink = true
                delay(100)
                blink = false
                delay(70)
            }
            delay(700)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "WELCOME TO",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(18.dp))

            Text(
                text = "STUDENT BUDDY".take(visibleLetters),
                fontSize = 40.sp,
                fontWeight = FontWeight.ExtraBold,
                color = if (blink) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.tertiary
                },
                maxLines = 1,
                softWrap = false,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(24.dp))

            Text(
                text = "Developed by Sneha M.G.",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Student Developer",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(20.dp))

            Text("Loading...", fontSize = 16.sp)
        }
    }

    @Composable
    fun ScreenHeader(
        title: String,
        onBack: (() -> Unit)? = null
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (onBack != null) {
                    TextButton(onClick = onBack) {
                        Text("BACK")
                    }
                    Spacer(Modifier.width(8.dp))
                }

                Text(
                    text = title,
                    modifier = Modifier.weight(1f),
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            HorizontalDivider()
            Spacer(Modifier.height(12.dp))
        }
    }

    @Composable
    fun LoginScreen(
        context: Context,
        onLoginSuccess: () -> Unit
    ) {
        var mobile by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var confirmPassword by remember { mutableStateOf("") }

        var showPassword by remember { mutableStateOf(false) }
        var isRegistering by remember { mutableStateOf(false) }
        var isForgotPassword by remember { mutableStateOf(false) }

        var message by remember { mutableStateOf("") }
        var isError by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF3EEFF))
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(60.dp))

            Text(
                text = "STUDENT BUDDY",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF7C4DFF)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = when {
                    isForgotPassword -> "Reset Password"
                    isRegistering -> "Create Account"
                    else -> "Welcome Back"
                },
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF311B92)
            )

            Spacer(modifier = Modifier.height(30.dp))
            OutlinedTextField(
                value = mobile,
                onValueChange = {
                    mobile = it.filter { char -> char.isDigit() }
                    message = ""
                },
                label = {
                    Text(
                        "Mobile Number",
                        color = Color(0xFF555555)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color(0xFF222222),
                    unfocusedTextColor = Color(0xFF222222),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = Color(0xFF7C4DFF),
                    unfocusedBorderColor = Color(0xFF777777)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (!isForgotPassword) {

                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        message = ""
                    },
                    label = {
                        Text(
                            "Password",
                            color = Color(0xFF555555)
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    visualTransformation =
                        if (showPassword)
                            VisualTransformation.None
                        else
                            PasswordVisualTransformation(),
                    trailingIcon = {
                        TextButton(
                            onClick = {
                                showPassword = !showPassword
                            }
                        ) {
                            Text(
                                if (showPassword) "HIDE" else "SHOW",
                                color = Color(0xFF7C4DFF)
                            )
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color(0xFF222222),
                        unfocusedTextColor = Color(0xFF222222),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = Color(0xFF7C4DFF),
                        unfocusedBorderColor = Color(0xFF777777)
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            if (isRegistering || isForgotPassword) {

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        message = ""
                    },
                    label = {
                        Text(
                            if (isForgotPassword)
                                "Confirm New Password"
                            else
                                "Confirm Password",
                            color = Color(0xFF555555)
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    visualTransformation =
                        if (showPassword)
                            VisualTransformation.None
                        else
                            PasswordVisualTransformation(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color(0xFF222222),
                        unfocusedTextColor = Color(0xFF222222),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = Color(0xFF7C4DFF),
                        unfocusedBorderColor = Color(0xFF777777)
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))
            }

            if (message.isNotBlank()) {
                Text(
                    text = message,
                    color = if (isError)
                        Color(0xFFD32F2F)
                    else
                        Color(0xFF2E7D32),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            Button(
                onClick = {

                    if (mobile.length < 7) {
                        message = "Enter a valid mobile number"
                        isError = true
                        return@Button
                    }

                    if (isForgotPassword) {

                        if (password.isBlank()) {
                            message = "Enter a new password"
                            isError = true
                            return@Button
                        }

                        if (password != confirmPassword) {
                            message = "Passwords do not match"
                            isError = true
                            return@Button
                        }

                        val success =
                            StudentBuddyStorage.resetPassword(
                                context,
                                mobile,
                                password
                            )

                        if (success) {
                            message = "Password reset successfully"
                            isError = false

                            isForgotPassword = false
                            isRegistering = false
                            password = ""
                            confirmPassword = ""
                        } else {
                            message = "Mobile number not registered"
                            isError = true
                        }

                    } else if (isRegistering) {

                        if (password.isBlank()) {
                            message = "Enter a password"
                            isError = true
                            return@Button
                        }

                        if (password != confirmPassword) {
                            message = "Passwords do not match"
                            isError = true
                            return@Button
                        }

                        StudentBuddyStorage.saveLoginCredentials(
                            context,
                            mobile,
                            password
                        )

                        StudentBuddyStorage.saveLoggedIn(
                            context,
                            true
                        )

                        onLoginSuccess()

                    } else {

                        val success =
                            StudentBuddyStorage.checkLogin(
                                context,
                                mobile,
                                password
                            )

                        if (success) {

                            StudentBuddyStorage.saveLoggedIn(
                                context,
                                true
                            )

                            onLoginSuccess()

                        } else {
                            message = "Incorrect mobile number or password"
                            isError = true
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF7C4DFF),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(
                    when {
                        isForgotPassword -> "RESET PASSWORD"
                        isRegistering -> "REGISTER"
                        else -> "LOGIN"
                    }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (!isRegistering && !isForgotPassword) {

                TextButton(
                    onClick = {
                        isForgotPassword = true
                        message = ""
                        password = ""
                        confirmPassword = ""
                    }
                ) {
                    Text("Forgot Password?")
                }

                TextButton(
                    onClick = {
                        isRegistering = true
                        message = ""
                        password = ""
                        confirmPassword = ""
                    }
                ) {
                    Text("New user? REGISTER")
                }

            } else {

                TextButton(
                    onClick = {
                        isRegistering = false
                        isForgotPassword = false
                        message = ""
                        password = ""
                        confirmPassword = ""
                    }
                ) {
                    Text("← Back to Login")
                }
            }
        }
    }
    @Composable
    fun ScreenHeader(
        title: String,
        onBack: () -> Unit
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(
                onClick = onBack
            ) {
                Text(
                    text = "←",
                    fontSize = 24.sp
                )
            }

            Text(
                text = title,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }

    @Composable
    fun ProfileScreen(
        profile: StudentProfile,
        onSave: (StudentProfile) -> Unit,
        onNext: () -> Unit
    ) {
        var name by remember(profile) { mutableStateOf(profile.name) }
        var usn by remember(profile) { mutableStateOf(profile.usn) }
        var college by remember(profile) { mutableStateOf(profile.college) }
        var branch by remember(profile) { mutableStateOf(profile.branch) }
        var year by remember(profile) { mutableStateOf(profile.year) }
        var semester by remember(profile) { mutableStateOf(profile.semester) }
        var section by remember(profile) { mutableStateOf(profile.section) }
        var father by remember(profile) { mutableStateOf(profile.father) }
        var mother by remember(profile) { mutableStateOf(profile.mother) }
        var address by remember(profile) { mutableStateOf(profile.address) }
        var pin by remember(profile) { mutableStateOf(profile.pin) }
        var bloodGroup by remember(key1 = profile) { mutableStateOf(profile.bloodGroup) }
        var error by remember { mutableStateOf("") }
        var isEditing by remember(profile) {
            mutableStateOf(profile.name.isBlank())
        }


        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ScreenHeader("STUDENT PROFILE")

            Text(
                "Enter your details",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(12.dp))

            ProfileField("Student Name *", name, enabled = isEditing) { name = it }
            ProfileField("USN (Optional)", usn, enabled = isEditing) { usn = it }
            ProfileField("College Name *", college, enabled = isEditing) { college = it }
            ProfileField("Branch / Department *", branch, enabled = isEditing) { branch = it }
            ProfileField("Year *", year, enabled = isEditing) { year = it }
            ProfileField("Semester *", semester, enabled = isEditing) { semester = it }
            ProfileField("Section *", section, enabled = isEditing) { section = it }
            ProfileField("Father Name *", father, enabled = isEditing) { father = it }
            ProfileField("Mother Name *", mother, enabled = isEditing) { mother = it }
            ProfileField(
                "Address *",
                address,
                singleLine = false,
                enabled = isEditing
            ) { address = it }

            ProfileField("PIN Code *", pin, enabled = isEditing) { pin = it }

            ProfileField(
                label = "Blood Group",
                value = bloodGroup,
                enabled = isEditing
            ) { bloodGroup = it }
            if (error.isNotBlank()) {
                Spacer(Modifier.height(8.dp))
                Text(
                    error,
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                Button(
                    modifier = Modifier.weight(1f),
                    enabled = isEditing,
                    onClick = {
                        val required = listOf(
                            name, college, branch, year, semester,
                            section, father, mother, address, pin
                        )

                        if (required.any { it.isBlank() }) {
                            error = "Please fill all the required details."
                        } else {
                            error = ""

                            onSave(
                                StudentProfile(
                                    name = name.trim(),
                                    usn = usn.trim(),
                                    college = college.trim(),
                                    photoUri = profile.photoUri,
                                    branch = branch.trim(),
                                    year = year.trim(),
                                    semester = semester.trim(),
                                    section = section.trim(),
                                    father = father.trim(),
                                    mother = mother.trim(),
                                    address = address.trim(),
                                    pin = pin.trim(),
                                    bloodGroup = bloodGroup.trim()
                                )
                            )

                            isEditing = false
                        }
                    }
                ) {
                    Text("SAVE")
                }

                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        isEditing = true
                        error = ""
                    }
                ) {
                    Text("EDIT")
                }
            }
            Spacer(Modifier.height(20.dp))
        }
    }

    @Composable
    fun ProfileField(
        label: String,
        value: String,
        singleLine: Boolean = true,
        enabled: Boolean = true,
        onValueChange: (String) -> Unit
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp),
            singleLine = singleLine,
            readOnly = !enabled
        )
    }
    @Composable
    fun HomeScreen(
        profile: StudentProfile,
        primary: Color,
        context: Context,
        onNavigate: (String) -> Unit,
        onLogout: () -> Unit,
        onTheme: (String) -> Unit
    ) {
        var menu by remember { mutableStateOf(false) }

        var brightness by remember {
            mutableFloatStateOf(
                StudentBuddyStorage.loadBrightness(context)
            )
        }

        var photoUri by remember {
            mutableStateOf(
                StudentBuddyStorage.loadPhotoUri(context)
            )
        }

        var uploadingPhoto by remember {
            mutableStateOf(false)}
        var showPhotoOptions by remember { mutableStateOf(false) }



        val picker = rememberLauncherForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri ->

            if (uri != null) {

                uploadingPhoto = true

                val newPhotoUri = uri

                android.os.Handler(
                    android.os.Looper.getMainLooper()
                ).postDelayed({
                    val savedPhotoUri =
                        StudentBuddyStorage.savePhotoUri(
                            context,
                            newPhotoUri
                        )

                    photoUri = savedPhotoUri
                    uploadingPhoto = false

                }, 700)
            }
        }

        val hour = Calendar.getInstance()
            .get(Calendar.HOUR_OF_DAY)

        val greeting = when (hour) {
            in 5..11 -> "Good morning"
            in 12..16 -> "Good afternoon"
            else -> "Good evening"
        }

        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .statusBarsPadding()
                .padding(16.dp)
        ) {

            // ---------- TOP BAR ----------

            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    "STUDENT BUDDY",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = primary
                )

                Spacer(Modifier.weight(1f))

                Box {

                    TextButton(
                        onClick = { menu = true }
                    ) {
                        Text(
                            "☰",
                            fontSize = 25.sp
                        )
                    }

                    DropdownMenu(
                        expanded = menu,
                        onDismissRequest = {
                            menu = false
                        }
                    ) {

                        DropdownMenuItem(
                            text = { Text("View Profile") },
                            onClick = {
                                menu = false
                                onNavigate("profile")
                            }
                        )



                        DropdownMenuItem(
                            text = { Text("Notifications") },
                            onClick = {
                                menu = false
                                onNavigate("reminders")
                            }
                        )

                        DropdownMenuItem(
                            text = { Text("Overall Attendance") },
                            onClick = {
                                menu = false
                                onNavigate("overall")
                            }
                        )

                        DropdownMenuItem(
                            text = { Text("Appearance") },
                            onClick = {
                                menu = false
                                onNavigate("settings")
                            }
                        )

                        DropdownMenuItem(
                            text = { Text("Suggestion") },
                            onClick = {
                                menu = false
                                onNavigate("suggestion")
                            }
                        )

                        DropdownMenuItem(
                            text = { Text("Report a Bug") },
                            onClick = {
                                menu = false
                                onNavigate("bug")
                            }
                        )

                        DropdownMenuItem(
                            text = { Text("Logout") },
                            onClick = {
                                menu = false
                                onLogout()
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(18.dp))

            // ---------- PROFILE PHOTO ----------

            if (photoUri!=null) {

                val uri = photoUri

                if (uri != null) {

                    val bitmap = runCatching {

                        context.contentResolver
                            .openInputStream(uri)
                            ?.use {
                                android.graphics.BitmapFactory
                                    .decodeStream(it)
                            }

                    }.getOrNull()

                    if (bitmap != null) {

                        Card(
                            modifier = Modifier
                                .size(120.dp)
                                .align(Alignment.CenterHorizontally)
                                .clickable {
                                    showPhotoOptions = true
                                },
                            shape = androidx.compose.foundation.shape.CircleShape
                        ) {

                            Image(
                                bitmap.asImageBitmap(),
                                contentDescription = "Profile Photo",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = androidx.compose.ui.layout.ContentScale.Crop
                            )
                        }
                    }
                }
            }
            IconButton(
                onClick = {
                    picker.launch("image/*")
                },
                enabled = !uploadingPhoto,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "📷",
                    fontSize = 24.sp
                )
            }

            // ---------- UPLOADING MESSAGE ----------

            if (uploadingPhoto) {

                Spacer(Modifier.height(10.dp))

                Text(
                    "Please wait, uploading photo...",
                    modifier = Modifier.align(
                        Alignment.CenterHorizontally
                    ),
                    fontWeight = FontWeight.Bold,
                    color = primary
                )
            }
            if (showPhotoOptions) {
                AlertDialog(
                    onDismissRequest = {
                        showPhotoOptions = false
                    },
                    title = {
                        Text("Profile Photo")
                    },
                    text = {
                        Text("What would you like to do?")
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                showPhotoOptions = false
                                picker.launch("image/*")
                            }
                        ) {
                            Text("EDIT / CHANGE")
                        }
                    },
                    dismissButton = {
                        Row {
                            TextButton(
                                onClick = {
                                    StudentBuddyStorage.savePhotoUri(
                                        context,
                                        android.net.Uri.parse("")
                                    )
                                    photoUri = null
                                    showPhotoOptions = false
                                }
                            ) {
                                Text("DELETE")
                            }

                            TextButton(
                                onClick = {
                                    showPhotoOptions = false
                                }
                            ) {
                                Text("CANCEL")
                            }
                        }
                    }
                )
            }

            Spacer(Modifier.height(16.dp))

            // ---------- GREETING ----------

            Text(
                "$greeting, ${profile.name.ifBlank { "Student" }}",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            // College name removed from here

            Spacer(Modifier.height(16.dp))
            // ---------- ATTENDANCE SUMMARY ----------

            val attendanceRecords =
                StudentBuddyStorage.loadAttendance(context)

            val attendedClasses =
                attendanceRecords.count { it.status == "PRESENT" }

            val absentClasses =
                attendanceRecords.count { it.status == "ABSENT" }

            val totalClasses =
                attendedClasses + absentClasses

            val attendancePercentage =
                if (totalClasses > 0) {
                    (attendedClasses * 100f) / totalClasses
                } else {
                    0f
                }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(18.dp)
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(
                        "Attendance",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = primary
                    )

                    Spacer(Modifier.height(10.dp))

                    Text(
                        "Attendance: ${"%.1f".format(attendancePercentage)}%",
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        "Classes Attended: $attendedClasses"
                    )

                    Text(
                        "Classes Absent: $absentClasses"
                    )
                }
            }

            // ---------- CHANGE PROFILE PHOTO ----------


            // ---------- DELETE PROFILE PHOTO ----------



            Spacer(Modifier.height(12.dp))

            // ---------- MAIN FEATURES ----------

            val items = listOf(
                "ATTENDANCE" to "attendance",
                "TIMETABLE" to "timetable",
                "ASSIGNMENTS" to "assignments",
                "NOTES" to "notes",
                "ACADEMIC RECORDS" to "academic",
                "EXPENSES" to "expenses"
            )

            items.forEach { (label, key) ->

                Card(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp),
                    shape = RoundedCornerShape(18.dp)
                ) {

                    TextButton(
                        onClick = {
                            onNavigate(key)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(58.dp)
                    ) {

                        Text(
                            label,
                            fontWeight = FontWeight.Bold,
                            color = primary
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // ---------- BRIGHTNESS ----------

            Text(
                "Brightness",
                fontWeight = FontWeight.Bold
            )

            Slider(
                value = brightness,
                onValueChange = { value ->

                    brightness = value

                    StudentBuddyStorage.saveBrightness(
                        context,
                        value
                    )

                    (context as? MainActivity)?.let { activity ->

                        val params =
                            activity.window.attributes

                        params.screenBrightness = value

                        activity.window.attributes = params
                    }
                }
            )
        }
    }
    @Composable
    fun HomeButton(text: String, onClick: () -> Unit) {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp),
            onClick = onClick
        ) {
            Text(text)
        }
    }






}
@Composable
fun AppearanceScreen(
    currentTheme: String,
    onThemeChange: (String) -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(18.dp)
    ) {

        ScreenHeader(
            "APPEARANCE",
            onBack
        )

        Spacer(Modifier.height(12.dp))

        Text(
            "Choose app appearance",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(16.dp))

        AppearanceOption(
            title = "Light Mode",
            selected = currentTheme == "light",
            onClick = {
                onThemeChange("light")
            }
        )

        AppearanceOption(
            title = "Dark Mode",
            selected = currentTheme == "dark",
            onClick = {
                onThemeChange("dark")
            }
        )

        AppearanceOption(
            title = "System Default",
            selected = currentTheme == "system",
            onClick = {
                onThemeChange("system")
            }
        )
    }
}

@Composable
fun AppearanceOption(
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable {
                onClick()
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            RadioButton(
                selected = selected,
                onClick = onClick
            )

            Spacer(Modifier.width(10.dp))

            Text(
                title,
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun AttendanceScreen(
    attendanceSubjects: List<AttendanceSubject>,
    attendance: List<AttendanceRecord>,
    onBack: () -> Unit,
    onAddSubject: (AttendanceSubject) -> Unit,
    onEditSubject: (String, AttendanceSubject) -> Unit,
    onDeleteSubject: (String) -> Unit,
    onSetAttendance: (String, String, String) -> Unit,

    ) {
    val context = LocalContext.current

    val days =
        listOf(
            "Monday",
            "Tuesday",
            "Wednesday",
            "Thursday",
            "Friday",
            "Saturday",
            "Sunday"
        )

    var newSubject by remember { mutableStateOf("") }
    var selectedDays by remember { mutableStateOf(emptySet<String>()) }
    var showAddSubject by remember { mutableStateOf(false) }

    var editingSubject by remember {
        mutableStateOf<AttendanceSubject?>(null)
    }

    var selectedDate by remember {
        mutableStateOf(Date())
    }

    // Holiday dates stored as dd-MM-yyyy
    var holidays by remember {
        mutableStateOf(emptySet<String>())
    }

    val selectedDateText = SimpleDateFormat(
        "dd-MM-yyyy",
        Locale.getDefault()
    ).format(selectedDate)

    val selectedDayText = SimpleDateFormat(
        "EEEE",
        Locale.getDefault()
    ).format(selectedDate)

    val isHoliday = selectedDateText in holidays

    // Subjects scheduled for the SELECTED DATE
    val scheduledSubjects = attendanceSubjects
        .filter { subject ->
            subject.days.any {
                it.equals(selectedDayText, ignoreCase = true)
            }
        }
        .map { it.name }
        .distinct()

    val savedSubjects =
        attendanceSubjects.map { it.name }.distinct()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(18.dp)
    ) {

        ScreenHeader("ATTENDANCE", onBack)

        // SELECTED DATE CARD
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                Modifier.padding(14.dp)
            ) {
                Text(
                    selectedDateText,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    selectedDayText,
                    fontSize = 17.sp
                )

                if (isHoliday) {
                    Spacer(Modifier.height(6.dp))

                    Text(
                        "🏖️ HOLIDAY",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // CHANGE DATE
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                val calendar = Calendar.getInstance().apply {
                    time = selectedDate
                }

                DatePickerDialog(
                    context,
                    { _, year, month, dayOfMonth ->
                        selectedDate = Calendar.getInstance().apply {
                            set(
                                year,
                                month,
                                dayOfMonth
                            )
                        }.time
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
        ) {
            Text("CHANGE DATE")
        }

        Spacer(Modifier.height(8.dp))

        // HOLIDAY BUTTON
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                holidays =
                    if (isHoliday) {
                        holidays - selectedDateText
                    } else {
                        holidays + selectedDateText
                    }
            }
        ) {
            Text(
                if (isHoliday)
                    "REMOVE HOLIDAY"
                else
                    "MARK AS HOLIDAY"
            )
        }

        Spacer(Modifier.height(16.dp))

        // ADD SUBJECT
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                showAddSubject = !showAddSubject
            }
        ) {
            Text(
                if (showAddSubject)
                    "CLOSE ADD SUBJECT"
                else
                    "ADD NEW SUBJECT"
            )
        }

        if (showAddSubject) {

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = newSubject,
                onValueChange = {
                    newSubject = it
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Subject Name")
                },
                singleLine = true
            )

            Spacer(Modifier.height(8.dp))

            Text(
                "Select days",
                fontWeight = FontWeight.Bold
            )

            days.forEach { day ->

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    androidx.compose.material3.Checkbox(
                        checked = day in selectedDays,
                        onCheckedChange = { checked ->

                            selectedDays =
                                if (checked) {
                                    selectedDays + day
                                } else {
                                    selectedDays - day
                                }
                        }
                    )

                    Text(day)
                }
            }

            Spacer(Modifier.height(6.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {

                    val clean =
                        newSubject.trim()

                    if (
                        clean.isNotEmpty() &&
                        selectedDays.isNotEmpty()
                    ) {

                        onAddSubject(
                            AttendanceSubject(
                                clean,
                                days.filter {
                                    it in selectedDays
                                }
                            )
                        )

                        newSubject = ""
                        selectedDays = emptySet()
                        showAddSubject = false
                    }
                }
            ) {
                Text("SAVE SUBJECT")
            }

            if (
                newSubject.trim().isNotEmpty() &&
                selectedDays.isEmpty()
            ) {

                Text(
                    "Select at least one day.",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        Spacer(Modifier.height(18.dp))

        // SELECTED DATE INFORMATION
        Text(
            text = "DATE: $selectedDateText",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "DAY: $selectedDayText",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(12.dp))

        // HOLIDAY / NO CLASSES
        if (isHoliday) {

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        "🏖️ HOLIDAY",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(6.dp))

                    Text(
                        "NO CLASSES TODAY",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(6.dp))

                    Text(
                        "Attendance is not counted for this date."
                    )
                }
            }

        } else {

            Text(
                "TODAY'S CLASSES",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(8.dp))

            if (scheduledSubjects.isEmpty()) {

                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            "NO CLASSES TODAY",
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.height(4.dp))

                        Text(
                            "No subjects are scheduled for $selectedDayText."
                        )
                    }
                }

            } else {

                scheduledSubjects.forEach { subject ->

                    AttendanceSubjectCard(
                        subject = subject,
                        date = selectedDateText,
                        attendance = attendance,
                        onSetAttendance = onSetAttendance,
                        showButtons = true
                    )

                    Spacer(
                        Modifier.height(8.dp)
                    )
                }
            }
        }

        Spacer(Modifier.height(18.dp))

        // ALL SAVED SUBJECTS
        Text(
            "ALL SAVED SUBJECTS",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        if (savedSubjects.isEmpty()) {

            Text(
                "Add a subject to start recording attendance."
            )
        }

        savedSubjects.forEach { subject ->

            val subjectInfo =
                attendanceSubjects.firstOrNull {
                    it.name.equals(subject, true)
                }

            Card(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
            ) {

                Column(
                    Modifier.padding(12.dp)
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            subject,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )
                        Column(
                            horizontalAlignment = Alignment.End
                        ) {

                            TextButton(
                                onClick = {
                                    subjectInfo?.let {
                                        editingSubject = it
                                    }
                                }
                            ) {
                                Text("EDIT")
                            }

                            TextButton(
                                onClick = {
                                    onDeleteSubject(subject)
                                }
                            ) {
                                Text("DELETE")
                            }
                        }   }
                }

                Text(
                    "Days: ${
                        subjectInfo?.days?.joinToString(", ")
                            ?: "Not set"
                    }"
                )

                val list =
                    attendance.filter {

                        it.subject.equals(
                            subject,
                            true
                        ) &&
                                it.status != "NO_CLASS"
                    }

                val total = list.size

                val present =
                    list.count {
                        it.status == "PRESENT"
                    }

                val pct =
                    if (total == 0)
                        0.0
                    else
                        present * 100.0 / total

                Text(
                    String.format(
                        Locale.US,
                        "Attendance: %.1f%%",
                        pct
                    )
                )
            }
        }


        Spacer(Modifier.height(20.dp))
    }

    // EDIT SUBJECT DIALOG
    editingSubject?.let { subject ->

        EditAttendanceSubjectDialog(
            subject = subject,
            days = days,

            onDismiss = {
                editingSubject = null
            },

            onSave = { updatedSubject ->

                onEditSubject(
                    subject.name,
                    updatedSubject
                )

                editingSubject = null
            }
        )
    }
}
@Composable
fun EditAttendanceSubjectDialog(
    subject: AttendanceSubject,
    days: List<String>,
    onDismiss: () -> Unit,
    onSave: (AttendanceSubject) -> Unit
) {
    var name by remember(subject) {
        mutableStateOf(subject.name)
    }

    var selectedDays by remember(subject) {
        mutableStateOf(subject.days.toSet())
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("EDIT SUBJECT")
        },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Subject Name") },
                    singleLine = true
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    "Select Days",
                    fontWeight = FontWeight.Bold
                )

                days.forEach { day ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        androidx.compose.material3.Checkbox(
                            checked = day in selectedDays,
                            onCheckedChange = { checked ->
                                selectedDays =
                                    if (checked) {
                                        selectedDays + day
                                    } else {
                                        selectedDays - day
                                    }
                            }
                        )

                        Text(day)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (
                        name.trim().isNotEmpty() &&
                        selectedDays.isNotEmpty()
                    ) {
                        onSave(
                            AttendanceSubject(
                                name.trim(),
                                days.filter { it in selectedDays }
                            )
                        )
                    }
                }
            ) {
                Text("SAVE")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("CANCEL")
            }
        }
    )
}

@Composable
fun AttendanceSubjectCard(
    subject: String,
    date: String,
    attendance: List<AttendanceRecord>,
    onSetAttendance: (String, String, String) -> Unit,
    showButtons: Boolean = true
) {
    val record = attendance.firstOrNull {
        it.subject.equals(subject, ignoreCase = true) &&
                it.date == date
    }

    val status = record?.status ?: "NOT_MARKED"

    val stats = attendance.filter {
        it.subject.equals(subject, ignoreCase = true)
    }

    val present = stats.count {
        it.status == "PRESENT"
    }

    val absent = stats.count {
        it.status == "ABSENT"
    }

    val total = present + absent

    val percentage = if (total == 0) {
        0.0
    } else {
        present * 100.0 / total
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = subject,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(6.dp))

            Text("Date: $date")

            Spacer(Modifier.height(6.dp))

            Text(
                text = when (status) {
                    "PRESENT" -> "Status: PRESENT"
                    "ABSENT" -> "Status: ABSENT"
                    "NO_CLASS" -> "Status: NO CLASS"
                    else -> "Status: NOT MARKED"
                },
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(8.dp))

            Text("Classes Attended: $present")
            Text("Classes Absent: $absent")
            Text("Total Classes: $total")
            Text(
                text = "Attendance: %.1f%%".format(percentage),
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(12.dp))

            if (showButtons) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {

                    AttendanceButton(
                        text = "PRESENT",
                        color = Color(0xFF2E7D32),
                        modifier = Modifier.weight(1f)
                    ) {
                        onSetAttendance(subject, date, "PRESENT")
                    }

                    AttendanceButton(
                        text = "ABSENT",
                        color = Color(0xFFC62828),
                        modifier = Modifier.weight(1f)
                    ) {
                        onSetAttendance(subject, date, "ABSENT")
                    }

                    AttendanceButton(
                        text = "NO CLASS",
                        color = Color.Gray,
                        modifier = Modifier.weight(1f)
                    ) {
                        onSetAttendance(subject, date, "NO_CLASS")
                    }
                }
            }
        }
    }
}

@Composable
fun AttendanceButton(
    text: String,
    color: Color,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = color)
    ) {
        Text(text, color = Color.White, fontSize = 11.sp)
    }
}

@Composable
fun OverallAttendanceScreen(
    attendance: List<AttendanceRecord>,
    onBack: () -> Unit
) {
    val validRecords = attendance.filter {
        it.status == "PRESENT" || it.status == "ABSENT"
    }

    val totalClasses = validRecords.size
    val presentClasses = validRecords.count {
        it.status == "PRESENT"
    }

    val overallPercentage =
        if (totalClasses == 0) {
            0.0
        } else {
            presentClasses * 100.0 / totalClasses
        }

    val subjects = validRecords
        .map { it.subject }
        .distinct()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(18.dp)
    ) {

        ScreenHeader(
            "OVERALL ATTENDANCE",
            onBack
        )

        Spacer(Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    "Overall Attendance",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    String.format(
                        Locale.US,
                        "%.1f%%",
                        overallPercentage
                    ),
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    "$presentClasses Present / $totalClasses Classes"
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        Text(
            "SUBJECT-WISE ATTENDANCE",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(8.dp))

        if (subjects.isEmpty()) {

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "No attendance records available yet.",
                    modifier = Modifier.padding(18.dp)
                )
            }

        } else {

            subjects.forEach { subject ->

                val subjectRecords = validRecords.filter {
                    it.subject.equals(subject, ignoreCase = true)
                }

                val subjectTotal = subjectRecords.size

                val subjectPresent = subjectRecords.count {
                    it.status == "PRESENT"
                }

                val subjectPercentage =
                    if (subjectTotal == 0) {
                        0.0
                    } else {
                        subjectPresent * 100.0 / subjectTotal
                    }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp)
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Text(
                            subject,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.height(6.dp))

                        Text(
                            String.format(
                                Locale.US,
                                "Attendance: %.1f%%",
                                subjectPercentage
                            )
                        )

                        Text(
                            "$subjectPresent Present / $subjectTotal Classes"
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun AllSubjectsScreen(
    subjects: List<AttendanceSubject>,
    attendance: List<AttendanceRecord>,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(18.dp)
    ) {
        ScreenHeader("ALL SUBJECTS", onBack)
        if (subjects.isEmpty()) Text("No subjects saved yet.")
        subjects.forEach { subject ->
            val list = attendance.filter {
                it.subject.equals(subject.name, true) && it.status != "NO_CLASS"
            }
            val total = list.size
            val present = list.count { it.status == "PRESENT" }
            val absent = list.count { it.status == "ABSENT" }
            val percentage = if (total == 0) 0.0 else present * 100.0 / total
            Card(Modifier.fillMaxWidth().padding(vertical = 5.dp)) {
                Column(Modifier.padding(12.dp)) {
                    Text(subject.name, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text("Days: ${subject.days.joinToString(", ")}")
                    Text("Total: $total   Present: $present   Absent: $absent")
                    Text(String.format(Locale.US, "Percentage: %.1f%%", percentage))
                    Text(safeCutoffText(present, total), fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}




@Composable
fun OverallAttendanceScreen(
    attendance: List<AttendanceRecord>,
    onBack: () -> Unit,
    safeCutoffText: (Int, Int) -> Unit
) {
    val counted = attendance.filter { it.status != "NO_CLASS" }
    val total = counted.size
    val present = counted.count { it.status == "PRESENT" }
    val absent = counted.count { it.status == "ABSENT" }
    val percentage = if (total == 0) 0.0 else present * 100.0 / total

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(18.dp)
    ) {
        ScreenHeader("OVERALL ATTENDANCE", onBack)

        Text(
            String.format(Locale.US, "%.1f%%", percentage),
            fontSize = 42.sp,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(Modifier.height(10.dp))

        Text("Total classes: $total")
        Text("Present: $present")
        Text("Absent: $absent")

        Spacer(Modifier.height(12.dp))
        Text(
            text = safeCutoffText(present, total).toString(), // Ensure it's a String
            fontWeight = FontWeight.Bold // Or FontWeight.Normal, FontWeight.Medium, etc.
        )

        Spacer(Modifier.height(20.dp))

        attendance
            .map { it.subject }
            .distinct()
            .forEach { subject ->
                val list = attendance.filter {
                    it.subject.equals(subject, true) &&
                            it.status != "NO_CLASS"
                }
                val t = list.size
                val p = list.count { it.status == "PRESENT" }
                val a = list.count { it.status == "ABSENT" }
                val pct = if (t == 0) 0.0 else p * 100.0 / t

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp)
                ) {
                    Column(Modifier.padding(12.dp)) {
                        Text(
                            subject,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                        Text("Total: $t   Present: $p   Absent: $a")
                        Text(
                            String.format(
                                Locale.US,
                                "Attendance: %.1f%%",
                                pct
                            )
                        )
                    }
                }
            }
    }
}

@Composable
fun TimetableScreen(
    subjects: MutableList<Subject>,
    onBack: () -> Unit,
    onSave: (List<Subject>) -> Unit
) {
    val days = listOf(
        "Monday",
        "Tuesday",
        "Wednesday",
        "Thursday",
        "Friday",
        "Saturday"
    )

    var selectedDay by remember { mutableStateOf("Monday") }

    var showAddClass by remember { mutableStateOf(false) }
    var showAddSubject by remember { mutableStateOf(false) }

    var editingIndex by remember { mutableStateOf(-1) }

    var name by remember { mutableStateOf("") }
    var faculty by remember { mutableStateOf("") }
    var start by remember { mutableStateOf("") }
    var end by remember { mutableStateOf("") }
    var classType by remember { mutableStateOf("Theory") }
    var room by remember { mutableStateOf("") }
    var day by remember { mutableStateOf("Monday") }

    var newSubjectName by remember { mutableStateOf("") }

    var dayMenu by remember { mutableStateOf(false) }
    var typeMenu by remember { mutableStateOf(false) }

    val visibleClasses = subjects
        .mapIndexed { index, subject -> index to subject }
        .filter { it.second.day == selectedDay }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        ScreenHeader(
            title = "MY TIMETABLE",
            onBack = onBack
        )

        Text(
            text = "Select Day",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )

        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
        ) {
            days.forEach { item ->

                OutlinedButton(
                    onClick = {
                        selectedDay = item
                    },
                    modifier = Modifier.padding(end = 6.dp)
                ) {
                    Text(item)
                }
            }
        }

        Spacer(Modifier.height(18.dp))

        Text(
            text = selectedDay,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(10.dp))

        if (visibleClasses.isEmpty()) {

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "No classes scheduled",
                    modifier = Modifier.padding(20.dp)
                )
            }

        } else {

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                ) {

                    Row(
                        modifier = Modifier
                            .width(850.dp)
                            .padding(
                                horizontal = 10.dp,
                                vertical = 12.dp
                            )
                    ) {

                        Text(
                            text = "TIME",
                            modifier = Modifier.width(120.dp),
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "SUBJECT",
                            modifier = Modifier.width(180.dp),
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "FACULTY",
                            modifier = Modifier.width(160.dp),
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "TYPE",
                            modifier = Modifier.width(100.dp),
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "ROOM / LAB",
                            modifier = Modifier.width(140.dp),
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "ACTION",
                            modifier = Modifier.width(150.dp),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    visibleClasses.forEach { pair ->

                        val index = pair.first
                        val subject = pair.second

                        HorizontalDivider()

                        Row(
                            modifier = Modifier
                                .width(850.dp)
                                .padding(
                                    horizontal = 10.dp,
                                    vertical = 12.dp
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(
                                text = "${subject.startTime}\n${subject.endTime}",
                                modifier = Modifier.width(120.dp)
                            )

                            Text(
                                text = subject.name,
                                modifier = Modifier.width(180.dp),
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = subject.faculty,
                                modifier = Modifier.width(160.dp)
                            )

                            Text(
                                text = subject.classType,
                                modifier = Modifier.width(100.dp)
                            )

                            Text(
                                text = subject.room.ifBlank { "-" },
                                modifier = Modifier.width(140.dp)
                            )

                            Row(
                                modifier = Modifier.width(150.dp)
                            ) {

                                TextButton(
                                    onClick = {

                                        editingIndex = index

                                        name = subject.name
                                        faculty = subject.faculty
                                        day = subject.day
                                        start = subject.startTime
                                        end = subject.endTime
                                        classType = subject.classType
                                        room = subject.room

                                        showAddClass = true
                                    }
                                ) {
                                    Text("EDIT")
                                }

                                TextButton(
                                    onClick = {

                                        subjects.removeAt(index)

                                        onSave(subjects.toList())
                                    }
                                ) {
                                    Text("DELETE")
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {

                editingIndex = -1

                name = ""
                faculty = ""
                start = ""
                end = ""
                classType = "Theory"
                room = ""
                day = selectedDay

                showAddClass = true
            }
        ) {
            Text("ADD CLASS")
        }

        Spacer(Modifier.height(24.dp))

        Text(
            text = "MY SUBJECTS",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(8.dp))

        val subjectNames = subjects
            .map { it.name }
            .distinct()

        if (subjectNames.isEmpty()) {

            Text("No subjects added yet.")

        } else {

            subjectNames.forEach { subjectName ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp)
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = subjectName,
                            modifier = Modifier.weight(1f)
                        )

                        TextButton(
                            onClick = {

                                subjects.removeAll {
                                    it.name == subjectName &&
                                            it.day.isBlank() &&
                                            it.startTime.isBlank() &&
                                            it.endTime.isBlank()
                                }

                                onSave(subjects.toList())
                            }
                        ) {
                            Text("DELETE")
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                newSubjectName = ""
                showAddSubject = true
            }
        ) {
            Text("ADD SUBJECT")
        }
    }

    if (showAddClass) {

        AlertDialog(
            onDismissRequest = {
                showAddClass = false
            },

            title = {
                Text(
                    if (editingIndex == -1)
                        "Add Class"
                    else
                        "Edit Class"
                )
            },

            text = {

                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                ) {

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Subject") },
                        singleLine = true
                    )

                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = faculty,
                        onValueChange = { faculty = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Faculty") },
                        singleLine = true
                    )

                    Spacer(Modifier.height(8.dp))

                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        OutlinedButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                dayMenu = true
                            }
                        ) {
                            Text("Day: $day")
                        }

                        DropdownMenu(
                            expanded = dayMenu,
                            onDismissRequest = {
                                dayMenu = false
                            }
                        ) {

                            days.forEach { item ->

                                DropdownMenuItem(
                                    text = {
                                        Text(item)
                                    },
                                    onClick = {
                                        day = item
                                        dayMenu = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = start,
                        onValueChange = { start = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Start Time") },
                        placeholder = {
                            Text("Example: 9:00 AM")
                        },
                        singleLine = true
                    )

                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = end,
                        onValueChange = { end = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("End Time") },
                        placeholder = {
                            Text("Example: 10:00 AM")
                        },
                        singleLine = true
                    )

                    Spacer(Modifier.height(8.dp))

                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        OutlinedButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                typeMenu = true
                            }
                        ) {
                            Text("Type: $classType")
                        }

                        DropdownMenu(
                            expanded = typeMenu,
                            onDismissRequest = {
                                typeMenu = false
                            }
                        ) {

                            listOf(
                                "Theory",
                                "Lab"
                            ).forEach { item ->

                                DropdownMenuItem(
                                    text = {
                                        Text(item)
                                    },
                                    onClick = {
                                        classType = item
                                        typeMenu = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = room,
                        onValueChange = { room = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Room / Lab") },
                        placeholder = {
                            Text("Example: Room 204 / Computer Lab 2")
                        },
                        singleLine = true
                    )
                }
            },

            confirmButton = {

                Button(
                    onClick = {

                        if (
                            name.trim().isNotEmpty() &&
                            faculty.trim().isNotEmpty() &&
                            start.trim().isNotEmpty() &&
                            end.trim().isNotEmpty()
                        ) {

                            val newClass = Subject(
                                name = name.trim(),
                                faculty = faculty.trim(),
                                day = day,
                                startTime = start.trim(),
                                endTime = end.trim(),
                                classType = classType,
                                room = room.trim()
                            )

                            if (editingIndex == -1) {

                                subjects.add(newClass)

                            } else {

                                subjects[editingIndex] = newClass
                            }

                            onSave(subjects.toList())

                            selectedDay = day

                            showAddClass = false

                            name = ""
                            faculty = ""
                            start = ""
                            end = ""
                            classType = "Theory"
                            room = ""
                            day = selectedDay

                            editingIndex = -1
                        }
                    }
                ) {
                    Text(
                        if (editingIndex == -1)
                            "ADD CLASS"
                        else
                            "SAVE CHANGES"
                    )
                }
            },

            dismissButton = {

                TextButton(
                    onClick = {
                        showAddClass = false
                    }
                ) {
                    Text("CANCEL")
                }
            }
        )
    }

    if (showAddSubject) {

        AlertDialog(
            onDismissRequest = {
                showAddSubject = false
            },

            title = {
                Text("Add Subject")
            },

            text = {

                OutlinedTextField(
                    value = newSubjectName,
                    onValueChange = {
                        newSubjectName = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("Subject Name")
                    },
                    singleLine = true
                )
            },

            confirmButton = {

                Button(
                    onClick = {

                        if (newSubjectName.trim().isNotEmpty()) {

                            val newSubject = Subject(
                                name = newSubjectName.trim(),
                                faculty = "",
                                day = "",
                                startTime = "",
                                endTime = ""
                            )

                            subjects.add(newSubject)

                            onSave(subjects.toList())

                            newSubjectName = ""

                            showAddSubject = false
                        }
                    }
                ) {
                    Text("ADD SUBJECT")
                }
            },

            dismissButton = {

                TextButton(
                    onClick = {
                        showAddSubject = false
                    }
                ) {
                    Text("CANCEL")
                }
            }
        )
    }
}

@Composable
fun ReminderScreen(
    subjects: MutableList<Subject>,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    val prefs = context.getSharedPreferences(
        "student_buddy_prefs",
        Context.MODE_PRIVATE
    )

    var attendanceReminder by remember {
        mutableStateOf(
            prefs.getBoolean("attendance_reminder", false)
        )
    }

    var assignmentReminder by remember {
        mutableStateOf(
            prefs.getBoolean("assignment_reminder", false)
        )
    }

    var timetableReminder by remember {
        mutableStateOf(
            prefs.getBoolean("timetable_reminder", false)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(18.dp)
    ) {

        ScreenHeader(
            "NOTIFICATIONS & REMINDERS",
            onBack
        )

        Spacer(Modifier.height(16.dp))

        Text(
            "Reminder Settings",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(10.dp))

        ReminderOption(
            title = "Attendance Reminder",
            description = "Remind me to update attendance",
            checked = attendanceReminder,
            onCheckedChange = {
                attendanceReminder = it

                prefs.edit()
                    .putBoolean("attendance_reminder", it)
                    .apply()

                if (it) {
                    scheduleAttendanceReminder(
                        context,
                        subjects
                    )
                } else {
                    cancelAttendanceReminder(
                        context,
                        subjects
                    )
                }
            }


        )

        ReminderOption(
            title = "Timetable Reminder",
            description = "Remind me to check today's timetable",
            checked = timetableReminder,
            onCheckedChange = {
                timetableReminder = it

                prefs.edit()
                    .putBoolean("timetable_reminder", it)
                    .apply()
            }
        )
        Spacer(Modifier.height(10.dp))

        ReminderOption(
            title = "Assignment Reminder",
            description = "Remind me about assignments",
            checked = assignmentReminder,
            onCheckedChange = {
                assignmentReminder = it

                prefs.edit()
                    .putBoolean(
                        "assignment_reminder",
                        it
                    )
                    .apply()
            }
        )

        Spacer(Modifier.height(20.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                StudentBuddyNotifications.showNotification(
                    context,
                    "Student Buddy",
                    "Notifications are working successfully!"
                )
            }
        ) {
            Text("SEND TEST NOTIFICATION")
        }
    }
}

fun cancelAttendanceReminder(
    context: Context,
    subjects: List<Subject>
) {
    val alarmManager =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    subjects.forEachIndexed { index, _ ->

        val intent =
            Intent(
                context,
                StudentBuddyReminderReceiver::class.java
            )

        val pendingIntent =
            PendingIntent.getBroadcast(
                context,
                2000 + index,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or
                        PendingIntent.FLAG_IMMUTABLE
            )

        alarmManager.cancel(pendingIntent)
    }
}
@Composable
fun ReminderOption(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    title,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(3.dp))

                Text(description)
            }

            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        }
    }
}
@Composable
fun AssignmentsScreen(
    assignments: MutableList<Assignment>,
    onBack: () -> Unit,
    onSave: (List<Assignment>) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf("") }
    var dueTime by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    var selectedType by remember { mutableStateOf("Other") }
    var selectedPriority by remember { mutableStateOf("Medium") }

    var editingIndex by remember { mutableStateOf(-1) }
    var filter by remember { mutableStateOf("All") }

    var attachmentUri by remember { mutableStateOf("") }

    var refresh by remember { mutableStateOf(0) }

    val context = LocalContext.current

    val attachmentLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.OpenDocument()
        ) { uri ->
            if (uri != null) {
                attachmentUri = uri.toString()
            }
        }

    fun clearForm() {
        title = ""
        subject = ""
        dueDate = ""
        dueTime = ""
        description = ""
        selectedType = "Other"
        selectedPriority = "Medium"
        attachmentUri = ""
        editingIndex = -1
    }

    fun dueStatus(item: Assignment): String {

        if (item.completed) {
            return "✅ COMPLETED"
        }

        if (item.dueDate.trim().isEmpty()) {
            return "🟡 NO DUE DATE"
        }

        return try {

            val formatter =
                SimpleDateFormat(
                    "dd MMM yyyy",
                    Locale.US
                )

            formatter.isLenient = false

            val due =
                formatter.parse(
                    item.dueDate.trim()
                ) ?: return ""

            val today =
                Calendar.getInstance()

            today.set(Calendar.HOUR_OF_DAY, 0)
            today.set(Calendar.MINUTE, 0)
            today.set(Calendar.SECOND, 0)
            today.set(Calendar.MILLISECOND, 0)

            val dueCalendar =
                Calendar.getInstance()

            dueCalendar.time = due

            dueCalendar.set(Calendar.HOUR_OF_DAY, 0)
            dueCalendar.set(Calendar.MINUTE, 0)
            dueCalendar.set(Calendar.SECOND, 0)
            dueCalendar.set(Calendar.MILLISECOND, 0)

            val difference =
                dueCalendar.timeInMillis -
                        today.timeInMillis

            val days =
                TimeUnit.MILLISECONDS.toDays(
                    difference
                )

            when {
                days < 0 ->
                    "🔴 Overdue by ${-days} day(s)"

                days == 0L ->
                    "🔴 Due today"

                days == 1L ->
                    "🟡 Due tomorrow"

                days <= 3L ->
                    "🟡 $days days remaining"

                else ->
                    "⏳ $days days remaining"
            }

        } catch (e: Exception) {
            ""
        }
    }

    val completedCount =
        assignments.count {
            it.completed
        }

    val overdueCount =
        assignments.count {
            !it.completed &&
                    dueStatus(it)
                        .startsWith("🔴 Overdue")
        }

    val dueSoonCount =
        assignments.count {
            !it.completed &&
                    (
                            dueStatus(it) == "🔴 Due today" ||
                                    dueStatus(it) == "🟡 Due tomorrow" ||
                                    dueStatus(it).contains("days remaining")
                            )
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                rememberScrollState()
            )
            .padding(18.dp)
    ) {

        ScreenHeader(
            "ASSIGNMENTS",
            onBack
        )

        Spacer(Modifier.height(8.dp))

        // SUMMARY
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {

            Column(
                Modifier.padding(14.dp)
            ) {

                Text(
                    "📚 ${assignments.size} Total",
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(5.dp))

                Text(
                    "⏰ $dueSoonCount Due Soon"
                )

                Text(
                    "✅ $completedCount Completed"
                )

                Text(
                    "⚠️ $overdueCount Overdue"
                )
            }
        }

        Spacer(Modifier.height(18.dp))

        // ADD / EDIT TITLE
        Text(
            if (editingIndex >= 0)
                "EDIT ASSIGNMENT"
            else
                "ADD ASSIGNMENT",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(8.dp))

        // TITLE
        OutlinedTextField(
            value = title,
            onValueChange = {
                title = it
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Assignment Title")
            },
            singleLine = true
        )

        Spacer(Modifier.height(8.dp))

        // SUBJECT
        OutlinedTextField(
            value = subject,
            onValueChange = {
                subject = it
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Subject")
            },
            singleLine = true
        )

        Spacer(Modifier.height(10.dp))

        // ASSIGNMENT TYPE
        Text(
            "Assignment Type",
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(5.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(
                    rememberScrollState()
                ),
            horizontalArrangement =
                Arrangement.spacedBy(6.dp)
        ) {

            listOf(
                "Theory",
                "Lab",
                "Project",
                "Seminar",
                "Other"
            ).forEach { type ->

                androidx.compose.material3.FilterChip(
                    selected =
                        selectedType == type,

                    onClick = {
                        selectedType = type
                    },

                    label = {
                        Text(type)
                    }
                )
            }
        }

        Spacer(Modifier.height(10.dp))

        // DESCRIPTION
        OutlinedTextField(
            value = description,
            onValueChange = {
                description = it
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Description")
            },
            minLines = 3
        )

        Spacer(Modifier.height(10.dp))

        // DUE DATE
        OutlinedTextField(
            value = dueDate,
            onValueChange = {
                dueDate = it
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Due Date")
            },
            placeholder = {
                Text("Select date")
            },
            readOnly = true,

            trailingIcon = {

                TextButton(
                    onClick = {

                        val calendar =
                            Calendar.getInstance()

                        DatePickerDialog(
                            context,

                            { _, year, month, day ->

                                val selected =
                                    Calendar.getInstance()

                                selected.set(
                                    year,
                                    month,
                                    day
                                )

                                val formatter =
                                    SimpleDateFormat(
                                        "dd MMM yyyy",
                                        Locale.US
                                    )

                                dueDate =
                                    formatter.format(
                                        selected.time
                                    )
                            },

                            calendar.get(
                                Calendar.YEAR
                            ),

                            calendar.get(
                                Calendar.MONTH
                            ),

                            calendar.get(
                                Calendar.DAY_OF_MONTH
                            )
                        ).show()
                    }
                ) {
                    Text("📅")
                }
            }
        )

        Spacer(Modifier.height(8.dp))

        // DUE TIME
        OutlinedTextField(
            value = dueTime,
            onValueChange = {
                dueTime = it
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Due Time")
            },
            placeholder = {
                Text("Select time")
            },
            readOnly = true,

            trailingIcon = {

                TextButton(
                    onClick = {

                        val calendar =
                            Calendar.getInstance()

                        TimePickerDialog(
                            context,

                            { _, hour, minute ->

                                val selected =
                                    Calendar.getInstance()

                                selected.set(
                                    Calendar.HOUR_OF_DAY,
                                    hour
                                )

                                selected.set(
                                    Calendar.MINUTE,
                                    minute
                                )

                                val formatter =
                                    SimpleDateFormat(
                                        "hh:mm a",
                                        Locale.US
                                    )

                                dueTime =
                                    formatter.format(
                                        selected.time
                                    )
                            },

                            calendar.get(
                                Calendar.HOUR_OF_DAY
                            ),

                            calendar.get(
                                Calendar.MINUTE
                            ),

                            false
                        ).show()
                    }
                ) {
                    Text("🕐")
                }
            }
        )

        Spacer(Modifier.height(10.dp))

        // PRIORITY
        Text(
            "Priority",
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(5.dp))

        Row(
            horizontalArrangement =
                Arrangement.spacedBy(6.dp)
        ) {

            listOf(
                "Low",
                "Medium",
                "High"
            ).forEach { priority ->

                androidx.compose.material3.FilterChip(
                    selected =
                        selectedPriority == priority,

                    onClick = {
                        selectedPriority =
                            priority
                    },

                    label = {
                        Text(priority)
                    }
                )
            }
        }

        Spacer(Modifier.height(10.dp))


        // SAVE / UPDATE
        Button(
            modifier = Modifier.fillMaxWidth(),

            onClick = {

                if (title.trim().isNotEmpty()) {

                    if (editingIndex >= 0) {

                        val old =
                            assignments[editingIndex]

                        assignments[editingIndex] =
                            Assignment(

                                title =
                                    title.trim(),

                                subject =
                                    subject.trim(),

                                dueDate =
                                    dueDate.trim(),

                                type =
                                    selectedType,

                                description =
                                    description.trim(),

                                dueTime =
                                    dueTime.trim(),

                                priority =
                                    selectedPriority,

                                completed =
                                    old.completed,

                                attachmentUri =
                                    attachmentUri
                            )

                    } else {

                        assignments.add(
                            Assignment(

                                title =
                                    title.trim(),

                                subject =
                                    subject.trim(),

                                dueDate =
                                    dueDate.trim(),

                                type =
                                    selectedType,

                                description =
                                    description.trim(),

                                dueTime =
                                    dueTime.trim(),

                                priority =
                                    selectedPriority,

                                completed =
                                    false,

                                attachmentUri =
                                    attachmentUri
                            )
                        )
                    }

                    onSave(
                        assignments.toList()
                    )

                    clearForm()

                    refresh++
                }
            }
        ) {

            Text(
                if (editingIndex >= 0)
                    "UPDATE ASSIGNMENT"
                else
                    "SAVE ASSIGNMENT"
            )
        }

        // CANCEL EDIT
        if (editingIndex >= 0) {

            Spacer(Modifier.height(5.dp))

            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),

                onClick = {
                    clearForm()
                }
            ) {
                Text("CANCEL EDIT")
            }
        }

        Spacer(Modifier.height(20.dp))

        // FILTERS
        Text(
            "YOUR ASSIGNMENTS",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(
                    rememberScrollState()
                ),
            horizontalArrangement =
                Arrangement.spacedBy(6.dp)
        ) {

            listOf(
                "All",
                "Pending",
                "Due Soon",
                "Completed",
                "Overdue"
            ).forEach { option ->

                androidx.compose.material3.FilterChip(
                    selected =
                        filter == option,

                    onClick = {
                        filter = option
                    },

                    label = {
                        Text(option)
                    }
                )
            }
        }

        Spacer(Modifier.height(10.dp))

        // ASSIGNMENT CARDS
        assignments.forEachIndexed { index, item ->

            val status =
                dueStatus(item)

            val showItem =
                when (filter) {

                    "Pending" ->
                        !item.completed

                    "Completed" ->
                        item.completed

                    "Due Soon" ->
                        !item.completed &&
                                (
                                        status == "🔴 Due today" ||
                                                status == "🟡 Due tomorrow" ||
                                                status.contains(
                                                    "days remaining"
                                                )
                                        )

                    "Overdue" ->
                        !item.completed &&
                                status.startsWith(
                                    "🔴 Overdue"
                                )

                    else ->
                        true
                }

            if (showItem) {

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = 5.dp
                        )
                ) {

                    Column(
                        Modifier.padding(14.dp)
                    ) {

                        // PRIORITY
                        Text(
                            when (item.priority) {

                                "High" ->
                                    "🔴 HIGH PRIORITY"

                                "Low" ->
                                    "🟢 LOW PRIORITY"

                                else ->
                                    "🟡 MEDIUM PRIORITY"
                            },
                            fontWeight =
                                FontWeight.Bold
                        )

                        Spacer(Modifier.height(6.dp))

                        // TITLE
                        Text(
                            item.title,
                            fontSize = 18.sp,
                            fontWeight =
                                FontWeight.Bold
                        )

                        Spacer(Modifier.height(4.dp))

                        if (
                            item.subject.isNotEmpty()
                        ) {

                            Text(
                                "Subject: ${item.subject}"
                            )
                        }

                        Text(
                            "Type: ${item.type}"
                        )

                        Spacer(Modifier.height(5.dp))

                        if (
                            item.description.isNotEmpty()
                        ) {

                            Text(
                                item.description
                            )

                            Spacer(Modifier.height(5.dp))
                        }

                        Text(
                            "📅 Due: ${
                                if (
                                    item.dueDate.isNotEmpty()
                                )
                                    item.dueDate
                                else
                                    "Not set"
                            }"
                        )

                        if (
                            item.dueTime.isNotEmpty()
                        ) {

                            Text(
                                "🕐 ${item.dueTime}"
                            )
                        }

                        Spacer(Modifier.height(5.dp))

                        // COUNTDOWN
                        Text(
                            status,
                            fontWeight =
                                FontWeight.Bold
                        )


                        Spacer(Modifier.height(8.dp))

                        // COMPLETE
                        Button(
                            modifier =
                                Modifier.fillMaxWidth(),

                            onClick = {

                                assignments[index] =
                                    item.copy(
                                        completed =
                                            !item.completed
                                    )

                                onSave(
                                    assignments.toList()
                                )

                                refresh++
                            }
                        ) {

                            Text(
                                if (item.completed)
                                    "MARK AS PENDING"
                                else
                                    "MARK AS COMPLETE ✓"
                            )
                        }

                        Spacer(Modifier.height(4.dp))

                        // EDIT + DELETE
                        Row(
                            modifier =
                                Modifier.fillMaxWidth(),

                            horizontalArrangement =
                                Arrangement.SpaceEvenly
                        ) {

                            TextButton(
                                onClick = {

                                    title =
                                        item.title

                                    subject =
                                        item.subject

                                    dueDate =
                                        item.dueDate

                                    dueTime =
                                        item.dueTime

                                    description =
                                        item.description

                                    selectedType =
                                        item.type

                                    selectedPriority =
                                        item.priority

                                    attachmentUri =
                                        item.attachmentUri

                                    editingIndex =
                                        index
                                }
                            ) {

                                Text("EDIT")
                            }

                            TextButton(
                                onClick = {

                                    assignments.removeAt(
                                        index
                                    )

                                    onSave(
                                        assignments.toList()
                                    )

                                    if (
                                        editingIndex ==
                                        index
                                    ) {
                                        clearForm()
                                    }

                                    refresh++
                                }
                            ) {

                                Text("DELETE")
                            }
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun ScreenHeader(
    title: String,
    onBack: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBack
        ) {
            Text(
                text = "←",
                fontSize = 24.sp
            )
        }

        Text(
            text = title,
            modifier = Modifier.weight(1f),
            fontWeight = FontWeight.Bold
        )
    }
}
@Composable
fun NotesScreen(
    notes: MutableList<Note>,
    onBack: () -> Unit,
    onSave: (List<Note>) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(18.dp)
    ) {
        ScreenHeader("NOTES", onBack)

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Note Title") },
            singleLine = true
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Note") },
            minLines = 5
        )

        Spacer(Modifier.height(10.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                if (title.trim().isNotEmpty() || content.trim().isNotEmpty()) {
                    notes.add(
                        Note(
                            title.trim(),
                            content.trim()
                        )
                    )
                    onSave(notes.toList())
                    title = ""
                    content = ""
                }
            }
        ) {
            Text("SAVE NOTE")
        }

        Spacer(Modifier.height(15.dp))

        notes.forEachIndexed { index, note ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Column(Modifier.padding(12.dp)) {
                    Text(
                        note.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(note.content)

                    TextButton(
                        onClick = {
                            notes.removeAt(index)
                            onSave(notes.toList())
                        }
                    ) {
                        Text("DELETE")
                    }
                }
            }
        }
    }
}

// ============================================================
//                    ACADEMIC RECORDS
// ============================================================


data class AcademicExtraRecord(
    val id: Long = System.currentTimeMillis(),
    val section: String = "",
    val values: Map<String, String> = emptyMap(),

    // Each field can have its own files
    val attachments: Map<String, List<String>> = emptyMap()
)

private val academicSemesters = listOf(
    "Semester 1",
    "Semester 2",
    "Semester 3",
    "Semester 4",
    "Semester 5",
    "Semester 6",
    "Semester 7",
    "Semester 8"
)

private val academicSections =
    linkedMapOf<String, List<String>>(

        "Projects" to listOf(
            "Project Title",
            "Category / Subject",
            "Description",
            "Individual / Team",
            "Your Role",
            "Technologies / Tools",
            "Start Date",
            "End Date",
            "Status",
            "Project Link"
        ),

        "Internships & Training" to listOf(
            "Organization",
            "Role",
            "Department / Field",
            "Start Date",
            "End Date",
            "Mode",
            "Description",
            "Skills Learned",
            "Certificate / Document"
        ),

        "Certifications" to listOf(
            "Certification Name",
            "Organization",
            "Obtained Date",
            "Expiry Date",
            "Certificate ID",
            "Certificate Link / Document"
        ),

        "Achievements" to listOf(
            "Achievement Title",
            "Category",
            "Event / Organization",
            "Date",
            "Position / Result",
            "Description",
            "Certificate / Photo"
        ),

        "Clubs & Organizations" to listOf(
            "Club / Organization Name",
            "Position",
            "Activity / Event",
            "Start Date",
            "End Date",
            "Responsibilities",
            "Certificate / Document"
        ),

        "Workshops & Seminars" to listOf(
            "Workshop / Seminar Name",
            "Topic",
            "Organizer",
            "Date",
            "Duration",
            "Mode",
            "Skills / Topics Learned",
            "Certificate / Document"
        ),

        "Research & Publications" to listOf(
            "Title",
            "Research Area",
            "Faculty Guide",
            "Institution",
            "Date",
            "Your Role",
            "Publication / Presentation Status",
            "Paper / Publication Link",
            "Document"
        ),

        "Volunteering & Social Activities" to listOf(
            "Organization",
            "Activity Name",
            "Role",
            "Date",
            "Duration / Hours",
            "Description",
            "Certificate / Document"
        ),

        "Competitions" to listOf(
            "Competition Name",
            "Category",
            "Organizer",
            "Date",
            "Individual / Team",
            "Position / Result",
            "Team Members",
            "Certificate / Document"
        ),

        "Career Skills" to listOf(
            "Skill Name",
            "Technical / Soft Skill",
            "Level"
        ),

        "Awards & Scholarships" to listOf(
            "Award / Scholarship Name",
            "Organization",
            "Year",
            "Category",
            "Amount",
            "Description",
            "Certificate / Document"
        ),

        "Documents" to listOf(
            "Document Name",
            "Document Type",
            "Date",
            "Description",
            "File / Link"
        )
    )

// ============================================================
//                         STORAGE
// ============================================================

private fun academicPrefs(context: Context) =
    context.getSharedPreferences(
        "student_buddy_prefs",
        Context.MODE_PRIVATE
    )

fun saveAcademic(
    context: Context,
    records: List<AcademicRecord>
) {
    val array = JSONArray()

    records.forEach { record ->

        val obj = JSONObject()

        obj.put("id", record.id)
        obj.put("semester", record.semester)
        obj.put("subject", record.subject)
        obj.put("subjectCode", record.subjectCode)
        obj.put("credits", record.credits)
        obj.put("internalMarks", record.internalMarks)
        obj.put("assignmentMarks", record.assignmentMarks)
        obj.put("practicalMarks", record.practicalMarks)
        obj.put("theoryMarks", record.theoryMarks)
        obj.put("totalMarks", record.totalMarks)
        obj.put("grade", record.grade)
        obj.put("result", record.result)

        array.put(obj)
    }

    academicPrefs(context)
        .edit()
        .putString(
            "academic_records",
            array.toString()
        )
        .apply()
}

fun loadAcademic(
    context: Context
): List<AcademicRecord> {

    val result = mutableListOf<AcademicRecord>()

    val saved = academicPrefs(context)
        .getString(
            "academic_records",
            null
        )
        ?: return result

    try {

        val array = JSONArray(saved)

        for (i in 0 until array.length()) {

            val obj = array.getJSONObject(i)

            result.add(
                AcademicRecord(
                    id = obj.optLong(
                        "id",
                        System.currentTimeMillis() + i
                    ),
                    semester = obj.optString(
                        "semester",
                        ""
                    ),
                    subject = obj.optString(
                        "subject",
                        ""
                    ),
                    subjectCode = obj.optString(
                        "subjectCode",
                        ""
                    ),
                    credits = obj.optString(
                        "credits",
                        ""
                    ),
                    internalMarks = obj.optString(
                        "internalMarks",
                        ""
                    ),
                    assignmentMarks = obj.optString(
                        "assignmentMarks",
                        ""
                    ),
                    practicalMarks = obj.optString(
                        "practicalMarks",
                        ""
                    ),
                    theoryMarks = obj.optString(
                        "theoryMarks",
                        ""
                    ),
                    totalMarks = obj.optString(
                        "totalMarks",
                        ""
                    ),
                    grade = obj.optString(
                        "grade",
                        ""
                    ),
                    result = obj.optString(
                        "result",
                        "Pass"
                    )
                )
            )
        }

    } catch (_: Exception) {
    }

    return result
}

private fun saveAcademicExtra(
    context: Context,
    records: List<AcademicExtraRecord>
) {

    val array = JSONArray()

    records.forEach { record ->

        val obj = JSONObject()

        obj.put("id", record.id)
        obj.put("section", record.section)

        // SAVE TEXT VALUES
        val values = JSONObject()

        record.values.forEach { entry ->
            values.put(
                entry.key,
                entry.value
            )
        }

        obj.put(
            "values",
            values
        )

        // SAVE ATTACHMENTS
        val attachments = JSONObject()

        record.attachments.forEach { entry ->

            val filesArray = JSONArray()

            entry.value.forEach { fileUri ->
                filesArray.put(fileUri)
            }

            attachments.put(
                entry.key,
                filesArray
            )
        }

        obj.put(
            "attachments",
            attachments
        )

        array.put(obj)
    }

    academicPrefs(context)
        .edit()
        .putString(
            "academic_extra_records",
            array.toString()
        )
        .apply()
}


private fun loadAcademicExtra(
    context: Context
): List<AcademicExtraRecord> {

    val result =
        mutableListOf<AcademicExtraRecord>()

    val saved =
        academicPrefs(context)
            .getString(
                "academic_extra_records",
                null
            )
            ?: return result

    try {

        val array = JSONArray(saved)

        for (i in 0 until array.length()) {

            val obj =
                array.getJSONObject(i)

            // LOAD TEXT VALUES
            val values =
                mutableMapOf<String, String>()

            val valuesObject =
                obj.optJSONObject("values")

            if (valuesObject != null) {

                val keys =
                    valuesObject.keys()

                while (keys.hasNext()) {

                    val key = keys.next()

                    values[key] =
                        valuesObject.optString(
                            key,
                            ""
                        )
                }
            }

            // LOAD ATTACHMENTS
            val attachments =
                mutableMapOf<String, List<String>>()

            val attachmentsObject =
                obj.optJSONObject("attachments")

            if (attachmentsObject != null) {

                val keys =
                    attachmentsObject.keys()

                while (keys.hasNext()) {

                    val key = keys.next()

                    val filesArray =
                        attachmentsObject.optJSONArray(key)

                    val files =
                        mutableListOf<String>()

                    if (filesArray != null) {

                        for (j in 0 until filesArray.length()) {

                            val fileUri =
                                filesArray.optString(
                                    j,
                                    ""
                                )

                            if (fileUri.isNotEmpty()) {
                                files.add(fileUri)
                            }
                        }
                    }

                    attachments[key] = files
                }
            }

            result.add(
                AcademicExtraRecord(
                    id = obj.optLong(
                        "id",
                        System.currentTimeMillis() + i
                    ),
                    section = obj.optString(
                        "section",
                        ""
                    ),
                    values = values,
                    attachments = attachments
                )
            )
        }

    } catch (_: Exception) {
    }

    return result
}

// ============================================================
//                     MAIN ACADEMIC SCREEN
// ============================================================

@Composable
fun AcademicScreen(
    records: MutableList<AcademicRecord>,
    onBack: () -> Unit,
    onSave: (List<AcademicRecord>) -> Unit
) {

    val context = LocalContext.current

    val subjectRecords =
        remember {
            mutableStateListOf<AcademicRecord>().apply {
                addAll(records)
            }
        }

    val extraRecords =
        remember {
            mutableStateListOf<AcademicExtraRecord>().apply {
                addAll(
                    loadAcademicExtra(context)
                )
            }
        }

    var showSubjectDialog by remember {
        mutableStateOf(false)
    }

    var editingSubject by remember {
        mutableStateOf<AcademicRecord?>(null)
    }

    var selectedSection by remember {
        mutableStateOf<String?>(null)
    }

    var editingExtra by remember {
        mutableStateOf<AcademicExtraRecord?>(null)
    }

    fun saveSubjects() {

        records.clear()
        records.addAll(subjectRecords)

        saveAcademic(
            context,
            subjectRecords.toList()
        )

        onSave(
            subjectRecords.toList()
        )
    }

    fun saveExtras() {

        saveAcademicExtra(
            context,
            extraRecords.toList()
        )
    }
    fun openAcademicAttachment(
        fileUri: String
    ) {

        try {

            val uri =
                Uri.parse(fileUri)

            val mimeType =
                context.contentResolver
                    .getType(uri)
                    ?: "*/*"

            val intent =
                Intent(
                    Intent.ACTION_VIEW
                ).apply {

                    setDataAndType(
                        uri,
                        mimeType
                    )

                    addFlags(
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                }

            context.startActivity(intent)

        } catch (e: Exception) {

            Toast.makeText(
                context,
                "Unable to open this file",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    fun deleteAcademicAttachment(
        record: AcademicExtraRecord,
        field: String,
        fileUri: String
    ) {

        val recordIndex =
            extraRecords.indexOfFirst {
                it.id == record.id
            }

        if (recordIndex >= 0) {

            val currentAttachments =
                extraRecords[recordIndex]
                    .attachments
                    .toMutableMap()

            val currentFiles =
                currentAttachments[field]
                    ?.toMutableList()
                    ?: mutableListOf()

            currentFiles.remove(fileUri)

            if (currentFiles.isEmpty()) {

                currentAttachments.remove(field)

            } else {

                currentAttachments[field] =
                    currentFiles
            }

            extraRecords[recordIndex] =
                extraRecords[recordIndex].copy(
                    attachments =
                        currentAttachments
                )

            saveExtras()
        }
    }

    val semestersCompleted =
        subjectRecords
            .map { it.semester }
            .filter { it.isNotBlank() }
            .distinct()
            .size

    val totalCredits =
        subjectRecords.sumOf {
            it.credits.toDoubleOrNull() ?: 0.0
        }

    val backlogs =
        subjectRecords.count {
            !it.result.equals(
                "Pass",
                ignoreCase = true
            )
        }

    val projects =
        extraRecords.count {
            it.section == "Projects"
        }

    val certifications =
        extraRecords.count {
            it.section == "Certifications"
        }

    val achievements =
        extraRecords.count {
            it.section == "Achievements"
        }

    val cgpa =
        academicCalculateCGPA(
            subjectRecords
        )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                rememberScrollState()
            )
            .padding(18.dp)
    ) {

        ScreenHeader(
            "ACADEMIC RECORDS",
            onBack
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "My Academic Journey",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        // ---------------- SUMMARY ----------------

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.spacedBy(8.dp)
        ) {

            AcademicSummaryCard(
                title = "CGPA",
                value =
                    if (cgpa > 0) {
                        String.format(
                            Locale.getDefault(),
                            "%.2f",
                            cgpa
                        )
                    } else {
                        "—"
                    },
                modifier = Modifier.weight(1f)
            )

            AcademicSummaryCard(
                title = "Semesters",
                value =
                    semestersCompleted.toString(),
                modifier = Modifier.weight(1f)
            )

            AcademicSummaryCard(
                title = "Projects",
                value =
                    projects.toString(),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.spacedBy(8.dp)
        ) {

            AcademicSummaryCard(
                title = "Certificates",
                value =
                    certifications.toString(),
                modifier = Modifier.weight(1f)
            )

            AcademicSummaryCard(
                title = "Achievements",
                value =
                    achievements.toString(),
                modifier = Modifier.weight(1f)
            )

            AcademicSummaryCard(
                title = "Credits",
                value =
                    academicFormatNumber(
                        totalCredits
                    ),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        Text(
            text = "Backlogs: $backlogs",
            fontWeight = FontWeight.SemiBold
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        // ====================================================
        // ACADEMIC PERFORMANCE
        // ====================================================

        Text(
            text = "Academic Performance",
            fontSize = 21.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(6.dp)
        )

        Text(
            text =
                "Add every subject separately. You can edit or delete only that subject later."
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        Button(
            onClick = {

                editingSubject = null
                showSubjectDialog = true
            },

            modifier = Modifier.fillMaxWidth()
        ) {

            Text("+ ADD SUBJECT")
        }

        Spacer(
            modifier = Modifier.height(14.dp)
        )

        val years =
            listOf(
                "1st Year" to
                        listOf(
                            "Semester 1",
                            "Semester 2"
                        ),

                "2nd Year" to
                        listOf(
                            "Semester 3",
                            "Semester 4"
                        ),

                "3rd Year" to
                        listOf(
                            "Semester 5",
                            "Semester 6"
                        ),

                "4th Year" to
                        listOf(
                            "Semester 7",
                            "Semester 8"
                        )
            )

        years.forEach { year ->

            val yearRecords =
                subjectRecords.filter {
                    it.semester in year.second
                }

            if (yearRecords.isNotEmpty()) {

                Text(
                    text = year.first,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    modifier =
                        Modifier.padding(
                            top = 10.dp,
                            bottom = 8.dp
                        )
                )

                year.second.forEach { semester ->

                    val semesterRecords =
                        subjectRecords.filter {
                            it.semester == semester
                        }

                    if (semesterRecords.isNotEmpty()) {

                        AcademicSemesterSummary(
                            semester =
                                semester,

                            records =
                                semesterRecords
                        )

                        Spacer(
                            modifier =
                                Modifier.height(8.dp)
                        )

                        semesterRecords.forEach { record ->

                            AcademicSubjectCard(
                                record = record,

                                onEdit = {

                                    editingSubject =
                                        record

                                    showSubjectDialog =
                                        true
                                },

                                onDelete = {

                                    subjectRecords.removeAll {
                                        it.id == record.id
                                    }

                                    saveSubjects()
                                }
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(10.dp)
                            )
                        }
                    }
                }
            }
        }

        if (subjectRecords.isEmpty()) {

            Card(
                modifier =
                    Modifier.fillMaxWidth()
            ) {

                Text(
                    text =
                        "No subjects added yet.",
                    modifier =
                        Modifier.padding(16.dp)
                )
            }
        }

        Spacer(
            modifier =
                Modifier.height(24.dp)
        )

        // ====================================================
        // OTHER SECTIONS
        // ====================================================

        academicSections.forEach { entry ->

            val section =
                entry.key

            val sectionRecords =
                extraRecords.filter {
                    it.section == section
                }

            AcademicSection(
                title = section,
                records = sectionRecords,

                onAdd = {
                    editingExtra = null
                    selectedSection = section
                },

                onEdit = { record ->
                    editingExtra = record
                    selectedSection = section
                },

                onDelete = { record ->
                    extraRecords.removeAll {
                        it.id == record.id
                    }

                    saveExtras()
                },

                onViewAttachment = { fileUri ->

                    openAcademicAttachment(
                        fileUri
                    )
                },

                onDeleteAttachment = { record, field, fileUri ->

                    deleteAcademicAttachment(
                        record,
                        field,
                        fileUri
                    )
                }
            )
            Spacer(
                modifier =
                    Modifier.height(14.dp)
            )
        }
    }

    // ========================================================
    // SUBJECT DIALOG
    // ========================================================

    if (showSubjectDialog) {

        AcademicSubjectDialog(
            existingRecord =
                editingSubject,

            onDismiss = {

                showSubjectDialog = false
                editingSubject = null
            },

            onSave = { newRecord ->

                if (editingSubject == null) {

                    subjectRecords.add(
                        newRecord
                    )

                } else {

                    val index =
                        subjectRecords.indexOfFirst {
                            it.id ==
                                    editingSubject!!.id
                        }

                    if (index >= 0) {

                        subjectRecords[index] =
                            newRecord
                    }
                }

                saveSubjects()

                showSubjectDialog = false
                editingSubject = null
            }
        )
    }

    // ========================================================
    // EXTRA RECORD DIALOG
    // ========================================================

    if (selectedSection != null) {

        AcademicExtraDialog(
            section =
                selectedSection!!,

            existingRecord =
                editingExtra,

            onDismiss = {

                selectedSection = null
                editingExtra = null
            },

            onSave = { newRecord ->

                if (editingExtra == null) {

                    extraRecords.add(
                        newRecord
                    )

                } else {

                    val index =
                        extraRecords.indexOfFirst {
                            it.id ==
                                    editingExtra!!.id
                        }

                    if (index >= 0) {

                        extraRecords[index] =
                            newRecord
                    }
                }

                saveExtras()

                selectedSection = null
                editingExtra = null
            }
        )
    }
}

// ============================================================
//                       SUMMARY CARD
// ============================================================

@Composable
private fun AcademicSummaryCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier
    ) {

        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(10.dp),

            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Text(
                text = value,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier =
                    Modifier.height(3.dp)
            )

            Text(
                text = title,
                fontSize = 11.sp
            )
        }
    }
}

// ============================================================
//                   SEMESTER SUMMARY
// ============================================================

@Composable
private fun AcademicSemesterSummary(
    semester: String,
    records: List<AcademicRecord>
) {

    val sgpa =
        academicCalculateSGPA(records)

    val credits =
        records.sumOf {
            it.credits.toDoubleOrNull() ?: 0.0
        }

    val backlogs =
        records.count {
            !it.result.equals(
                "Pass",
                ignoreCase = true
            )
        }

    Card(
        modifier =
            Modifier.fillMaxWidth()
    ) {

        Column(
            modifier =
                Modifier.padding(14.dp)
        ) {

            Text(
                text = semester,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier =
                    Modifier.height(5.dp)
            )

            Text(
                text =
                    "SGPA: " +
                            if (sgpa > 0) {
                                String.format(
                                    Locale.getDefault(),
                                    "%.2f",
                                    sgpa
                                )
                            } else {
                                "—"
                            }
            )

            Text(
                text =
                    "Credits: " +
                            academicFormatNumber(
                                credits
                            )
            )

            Text(
                text =
                    "Backlogs: $backlogs"
            )

            Text(
                text =
                    "Result: " +
                            if (backlogs == 0) {
                                "Pass"
                            } else {
                                "Backlog"
                            }
            )
        }
    }
}

// ============================================================
//                       SUBJECT CARD
// ============================================================

@Composable
private fun AcademicSubjectCard(
    record: AcademicRecord,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {

    Card(
        modifier =
            Modifier.fillMaxWidth()
    ) {

        Column(
            modifier =
                Modifier.padding(16.dp)
        ) {

            Text(
                text = record.subject,
                fontSize = 18.sp,
                fontWeight =
                    FontWeight.Bold
            )

            if (record.subjectCode.isNotBlank()) {

                Text(
                    text =
                        "Code: ${record.subjectCode}"
                )
            }

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )

            AcademicSubjectInfo(
                "Credits",
                record.credits
            )

            AcademicSubjectInfo(
                "Internal Marks",
                record.internalMarks
            )

            AcademicSubjectInfo(
                "Assignment Marks",
                record.assignmentMarks
            )

            AcademicSubjectInfo(
                "Practical Marks",
                record.practicalMarks
            )

            AcademicSubjectInfo(
                "Theory / Exam Marks",
                record.theoryMarks
            )

            AcademicSubjectInfo(
                "Total Marks",
                record.totalMarks
            )

            AcademicSubjectInfo(
                "Grade",
                record.grade
            )

            AcademicSubjectInfo(
                "Result",
                record.result
            )

            Spacer(
                modifier =
                    Modifier.height(10.dp)
            )

            Row(
                modifier =
                    Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.spacedBy(8.dp)
            ) {

                Button(
                    onClick = onEdit,
                    modifier =
                        Modifier.weight(1f)
                ) {

                    Text("EDIT")
                }

                Button(
                    onClick = onDelete,
                    modifier =
                        Modifier.weight(1f)
                ) {

                    Text("DELETE")
                }
            }
        }
    }
}

@Composable
private fun AcademicSubjectInfo(
    label: String,
    value: String
) {

    if (value.isNotBlank()) {

        Row(
            modifier =
                Modifier.fillMaxWidth()
        ) {

            Text(
                text = "$label: ",
                fontWeight =
                    FontWeight.SemiBold
            )

            Text(value)
        }

        Spacer(
            modifier =
                Modifier.height(3.dp)
        )
    }
}

// ============================================================
//                   SUBJECT DIALOG
// ============================================================

@Composable
private fun AcademicSubjectDialog(
    existingRecord: AcademicRecord?,
    onDismiss: () -> Unit,
    onSave: (AcademicRecord) -> Unit
) {

    var semester by remember(existingRecord) {
        mutableStateOf(
            existingRecord?.semester ?: ""
        )
    }

    var subject by remember(existingRecord) {
        mutableStateOf(
            existingRecord?.subject ?: ""
        )
    }

    var subjectCode by remember(existingRecord) {
        mutableStateOf(
            existingRecord?.subjectCode ?: ""
        )
    }

    var credits by remember(existingRecord) {
        mutableStateOf(
            existingRecord?.credits ?: ""
        )
    }

    var internalMarks by remember(existingRecord) {
        mutableStateOf(
            existingRecord?.internalMarks ?: ""
        )
    }

    var assignmentMarks by remember(existingRecord) {
        mutableStateOf(
            existingRecord?.assignmentMarks ?: ""
        )
    }

    var practicalMarks by remember(existingRecord) {
        mutableStateOf(
            existingRecord?.practicalMarks ?: ""
        )
    }

    var theoryMarks by remember(existingRecord) {
        mutableStateOf(
            existingRecord?.theoryMarks ?: ""
        )
    }

    var totalMarks by remember(existingRecord) {
        mutableStateOf(
            existingRecord?.totalMarks ?: ""
        )
    }

    var grade by remember(existingRecord) {
        mutableStateOf(
            existingRecord?.grade ?: ""
        )
    }

    var result by remember(existingRecord) {
        mutableStateOf(
            existingRecord?.result ?: "Pass"
        )
    }

    var semesterExpanded by remember {
        mutableStateOf(false)
    }

    AlertDialog(

        onDismissRequest =
            onDismiss,

        title = {

            Text(
                if (existingRecord == null) {
                    "Add Subject"
                } else {
                    "Edit Subject"
                }
            )
        },

        text = {

            Column(
                modifier =
                    Modifier.verticalScroll(
                        rememberScrollState()
                    )
            ) {

                Box(
                    modifier =
                        Modifier.fillMaxWidth()
                ) {

                    OutlinedButton(
                        onClick = {
                            semesterExpanded = true
                        },
                        modifier =
                            Modifier.fillMaxWidth()
                    ) {

                        Text(
                            if (semester.isBlank()) {
                                "Select Semester"
                            } else {
                                semester
                            }
                        )
                    }

                    DropdownMenu(
                        expanded =
                            semesterExpanded,

                        onDismissRequest = {
                            semesterExpanded = false
                        }
                    ) {

                        academicSemesters.forEach {
                                semesterOption ->

                            DropdownMenuItem(

                                text = {
                                    Text(
                                        semesterOption
                                    )
                                },

                                onClick = {

                                    semester =
                                        semesterOption

                                    semesterExpanded =
                                        false
                                }
                            )
                        }
                    }
                }

                Spacer(
                    modifier =
                        Modifier.height(8.dp)
                )

                AcademicTextField(
                    value = subject,
                    label = "Subject Name *",
                    onValueChange = {
                        subject = it
                    }
                )

                AcademicTextField(
                    value = subjectCode,
                    label = "Subject Code (Optional)",
                    onValueChange = {
                        subjectCode = it
                    }
                )

                AcademicTextField(
                    value = credits,
                    label = "Credits",
                    onValueChange = {
                        credits = it
                    }
                )

                AcademicTextField(
                    value = internalMarks,
                    label = "Internal Marks",
                    onValueChange = {
                        internalMarks = it
                    }
                )

                AcademicTextField(
                    value = assignmentMarks,
                    label = "Assignment Marks (Optional)",
                    onValueChange = {
                        assignmentMarks = it
                    }
                )

                AcademicTextField(
                    value = practicalMarks,
                    label = "Practical Marks (Optional)",
                    onValueChange = {
                        practicalMarks = it
                    }
                )

                AcademicTextField(
                    value = theoryMarks,
                    label = "Theory / Exam Marks",
                    onValueChange = {
                        theoryMarks = it
                    }
                )

                AcademicTextField(
                    value = totalMarks,
                    label = "Total Marks",
                    onValueChange = {
                        totalMarks = it
                    }
                )

                AcademicTextField(
                    value = grade,
                    label = "Grade",
                    onValueChange = {
                        grade = it
                    }
                )

                AcademicTextField(
                    value = result,
                    label = "Result Status (Pass / Fail)",
                    onValueChange = {
                        result = it
                    }
                )
            }
        },

        confirmButton = {

            Button(
                onClick = {

                    if (
                        semester.isNotBlank() &&
                        subject.isNotBlank()
                    ) {

                        onSave(
                            AcademicRecord(

                                id =
                                    existingRecord?.id
                                        ?: System.currentTimeMillis(),

                                semester =
                                    semester.trim(),

                                subject =
                                    subject.trim(),

                                subjectCode =
                                    subjectCode.trim(),

                                credits =
                                    credits.trim(),

                                internalMarks =
                                    internalMarks.trim(),

                                assignmentMarks =
                                    assignmentMarks.trim(),

                                practicalMarks =
                                    practicalMarks.trim(),

                                theoryMarks =
                                    theoryMarks.trim(),

                                totalMarks =
                                    totalMarks.trim(),

                                grade =
                                    grade.trim(),

                                result =
                                    result.trim()
                                        .ifBlank {
                                            "Pass"
                                        }
                            )
                        )
                    }
                }
            ) {

                Text(
                    if (existingRecord == null) {
                        "ADD"
                    } else {
                        "UPDATE"
                    }
                )
            }
        },

        dismissButton = {

            TextButton(
                onClick = onDismiss
            ) {

                Text("CANCEL")
            }
        }
    )
}

// ============================================================
//                    TEXT FIELD
// ============================================================

@Composable
private fun AcademicTextField(
    value: String,
    label: String,
    onValueChange: (String) -> Unit
) {

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(label)
        },
        modifier =
            Modifier.fillMaxWidth()
    )

    Spacer(
        modifier =
            Modifier.height(7.dp)
    )
}

// ============================================================
//                  OTHER SECTIONS
// ============================================================

@Composable
private fun AcademicSection(
    title: String,
    records: List<AcademicExtraRecord>,
    onAdd: () -> Unit,
    onEdit: (AcademicExtraRecord) -> Unit,
    onDelete: (AcademicExtraRecord) -> Unit,
    onViewAttachment: (String) -> Unit,
    onDeleteAttachment: (AcademicExtraRecord, String, String) -> Unit
) {

    Card(
        modifier =
            Modifier.fillMaxWidth()
    ) {

        Column(
            modifier =
                Modifier.padding(16.dp)
        ) {

            Row(
                modifier =
                    Modifier.fillMaxWidth(),
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight =
                        FontWeight.Bold,
                    modifier =
                        Modifier.weight(1f)
                )

                Button(
                    onClick = onAdd
                ) {

                    Text("+ ADD")
                }
            }

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )

            if (records.isEmpty()) {

                Text("No records yet.")

            } else {

                records.forEach { record ->

                    AcademicExtraCard(
                        record = record,

                        onEdit = {
                            onEdit(record)
                        },

                        onDelete = {
                            onDelete(record)
                        },

                        onViewAttachment =
                            onViewAttachment,

                        onDeleteAttachment = {
                                fileField,
                                fileUri ->

                            onDeleteAttachment(
                                record,
                                fileField,
                                fileUri
                            )
                        }
                    )

                    Spacer(
                        modifier =
                            Modifier.height(8.dp)
                    )
                }
            }
        }
    }
}
// ============================================================
//                 EXTRA RECORD CARD
// ============================================================

@Composable
private fun AcademicExtraCard(
    record: AcademicExtraRecord,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onViewAttachment: (String) -> Unit,
    onDeleteAttachment: (String, String) -> Unit
) {

    Card(
        modifier =
            Modifier.fillMaxWidth()
    ) {

        Column(
            modifier =
                Modifier.padding(12.dp)
        ) {

            // ================================
            // TEXT INFORMATION
            // ================================

            record.values.forEach { entry ->

                if (entry.value.isNotBlank()) {

                    Text(
                        text =
                            "${entry.key}: ${entry.value}"
                    )

                    Spacer(
                        modifier =
                            Modifier.height(3.dp)
                    )
                }
            }

            // ================================
            // UPLOADED FILES
            // ================================

            if (record.attachments.isNotEmpty()) {

                Spacer(
                    modifier =
                        Modifier.height(10.dp)
                )

                Text(
                    text = "📎 Uploaded Files",
                    fontWeight =
                        FontWeight.Bold
                )

                Spacer(
                    modifier =
                        Modifier.height(6.dp)
                )

                record.attachments.forEach { entry ->

                    val field =
                        entry.key

                    val files =
                        entry.value

                    if (files.isNotEmpty()) {

                        Text(
                            text = field,
                            fontWeight =
                                FontWeight.SemiBold
                        )

                        files.forEachIndexed {
                                index,
                                fileUri ->

                            Row(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            vertical = 4.dp
                                        ),

                                verticalAlignment =
                                    Alignment.CenterVertically
                            ) {

                                Text(
                                    text =
                                        "📄 File ${index + 1}",

                                    modifier =
                                        Modifier.weight(1f)
                                )

                                TextButton(
                                    onClick = {

                                        onViewAttachment(
                                            fileUri
                                        )
                                    }
                                ) {

                                    Text("VIEW")
                                }

                                TextButton(
                                    onClick = {

                                        onDeleteAttachment(
                                            field,
                                            fileUri
                                        )
                                    }
                                ) {

                                    Text("DELETE")
                                }
                            }
                        }

                        Spacer(
                            modifier =
                                Modifier.height(4.dp)
                        )
                    }
                }
            }

            // ================================
            // EDIT / DELETE RECORD
            // ================================

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )

            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.spacedBy(8.dp)
            ) {

                Button(
                    onClick = onEdit,
                    modifier =
                        Modifier.weight(1f)
                ) {

                    Text("EDIT")
                }

                Button(
                    onClick = onDelete,
                    modifier =
                        Modifier.weight(1f)
                ) {

                    Text("DELETE")
                }
            }
        }
    }
}
// ============================================================
//                 EXTRA SECTION DIALOG
// ============================================================
@Composable
private fun AcademicExtraDialog(
    section: String,
    existingRecord: AcademicExtraRecord?,
    onDismiss: () -> Unit,
    onSave: (AcademicExtraRecord) -> Unit
) {

    val context = LocalContext.current

    val fields = academicSections[section] ?: emptyList()

    // ========================================================
    // TEXT VALUES
    // ========================================================

    var values by remember(section, existingRecord) {
        mutableStateOf(
            fields.associateWith { field ->
                existingRecord?.values?.get(field) ?: ""
            }
        )
    }

    // ========================================================
    // FILE ATTACHMENTS
    // field name -> list of file URIs
    // ========================================================

    var attachments by remember(section, existingRecord) {
        mutableStateOf<Map<String, List<String>>>(
            existingRecord?.attachments ?: emptyMap()
        )
    }

    var selectedFileField by remember {
        mutableStateOf<String?>(null)
    }

    // ========================================================
    // FILE PICKER
    // ========================================================

    val filePicker =
        rememberLauncherForActivityResult(
            ActivityResultContracts.OpenDocument()
        ) { uri ->

            if (uri != null && selectedFileField != null) {

                try {
                    context.contentResolver.takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                } catch (_: Exception) {
                }

                val field = selectedFileField!!
                val uriString = uri.toString()

                val currentFiles =
                    attachments[field].orEmpty()

                if (!currentFiles.contains(uriString)) {

                    attachments =
                        attachments +
                                (
                                        field to
                                                (currentFiles + uriString)
                                        )
                }

                selectedFileField = null
            }
        }

    // ========================================================
    // OPEN FILE
    // ========================================================

    fun openAttachment(uriString: String) {

        try {

            val uri = Uri.parse(uriString)

            val mimeType =
                context.contentResolver.getType(uri)
                    ?: "*/*"

            val intent =
                Intent(Intent.ACTION_VIEW).apply {

                    setDataAndType(
                        uri,
                        mimeType
                    )

                    addFlags(
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                }

            context.startActivity(intent)

        } catch (_: Exception) {

            Toast.makeText(
                context,
                "Unable to open this file",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // ========================================================
    // DELETE FILE
    // ========================================================

    fun deleteAttachment(
        field: String,
        uriString: String
    ) {

        val remainingFiles =
            attachments[field]
                .orEmpty()
                .filter {
                    it != uriString
                }

        attachments =
            if (remainingFiles.isEmpty()) {

                attachments - field

            } else {

                attachments +
                        (
                                field to remainingFiles
                                )
            }
    }

    // ========================================================
    // DIALOG
    // ========================================================

    AlertDialog(

        onDismissRequest = {
            onDismiss()
        },

        title = {

            Text(
                if (existingRecord == null) {
                    "Add $section"
                } else {
                    "Edit $section"
                }
            )
        },

        text = {

            Column(
                modifier =
                    Modifier.verticalScroll(
                        rememberScrollState()
                    )
            ) {

                fields.forEach { field ->

                    // ====================================================
                    // TEXT FIELD
                    // ====================================================

                    AcademicTextField(

                        value =
                            values[field] ?: "",

                        label =
                            field,

                        onValueChange = { newValue ->

                            values =
                                values +
                                        (
                                                field to newValue
                                                )
                        }
                    )

                    // ====================================================
                    // CHECK WHETHER THIS FIELD SUPPORTS FILES
                    // ====================================================

                    val supportsFile =
                        field.contains(
                            "Certificate",
                            ignoreCase = true
                        ) ||
                                field.contains(
                                    "Document",
                                    ignoreCase = true
                                ) ||
                                field.contains(
                                    "Photo",
                                    ignoreCase = true
                                ) ||
                                field.contains(
                                    "File",
                                    ignoreCase = true
                                ) ||
                                field.contains(
                                    "Paper",
                                    ignoreCase = true
                                )

                    if (supportsFile) {

                        Spacer(
                            modifier =
                                Modifier.height(6.dp)
                        )

                        // ====================================================
                        // ADD FILE
                        // ====================================================

                        Button(

                            onClick = {

                                selectedFileField =
                                    field

                                filePicker.launch(
                                    arrayOf(
                                        "application/pdf",
                                        "image/*"
                                    )
                                )
                            },

                            modifier =
                                Modifier.fillMaxWidth()
                        ) {

                            Text(
                                "📎 ADD FILE"
                            )
                        }

                        // ====================================================
                        // SHOW FILES FOR THIS EXACT FIELD
                        // ====================================================

                        val fieldFiles =
                            attachments[field]
                                ?: emptyList()

                        fieldFiles.forEachIndexed { index, fileUri ->

                            Spacer(
                                modifier =
                                    Modifier.height(6.dp)
                            )

                            Card(
                                modifier =
                                    Modifier.fillMaxWidth()
                            ) {

                                Row(

                                    modifier =
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp),

                                    verticalAlignment =
                                        Alignment.CenterVertically
                                ) {

                                    Text(

                                        text =
                                            "📎 File ${index + 1}",

                                        modifier =
                                            Modifier.weight(1f)
                                    )

                                    // VIEW
                                    TextButton(

                                        onClick = {

                                            openAttachment(
                                                fileUri
                                            )
                                        }

                                    ) {

                                        Text(
                                            "VIEW"
                                        )
                                    }

                                    // DELETE
                                    TextButton(

                                        onClick = {

                                            deleteAttachment(
                                                field,
                                                fileUri
                                            )
                                        }

                                    ) {

                                        Text(
                                            "DELETE"
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(
                        modifier =
                            Modifier.height(8.dp)
                    )
                }
            }
        },

        // ========================================================
        // SAVE / UPDATE
        // ========================================================

        confirmButton = {

            Button(

                onClick = {

                    val cleanedValues =
                        values.mapValues { entry ->
                            entry.value.trim()
                        }

                    val cleanedAttachments =
                        attachments.mapValues { entry ->
                            entry.value.toList()
                        }

                    onSave(

                        AcademicExtraRecord(

                            id =
                                existingRecord?.id
                                    ?: System.currentTimeMillis(),

                            section =
                                section,

                            values =
                                cleanedValues,

                            attachments =
                                cleanedAttachments
                        )
                    )
                }

            ) {

                Text(
                    if (existingRecord == null) {
                        "ADD"
                    } else {
                        "UPDATE"
                    }
                )
            }
        },

        // ========================================================
        // CANCEL
        // ========================================================

        dismissButton = {

            TextButton(
                onClick = {
                    onDismiss()
                }
            ) {

                Text(
                    "CANCEL"
                )
            }
        }
    )
}
// ============================================================
//                    GPA CALCULATION
// ============================================================

private fun academicGradePoint(
    grade: String
): Double {

    return when (
        grade.trim().uppercase()
    ) {

        "O",
        "A+" -> 10.0

        "A" -> 9.0

        "B+" -> 8.0

        "B" -> 7.0

        "C" -> 6.0

        "D" -> 5.0

        "E" -> 4.0

        "F" -> 0.0

        else -> 0.0
    }
}

private fun academicCalculateSGPA(
    records: List<AcademicRecord>
): Double {

    var weightedPoints = 0.0
    var totalCredits = 0.0

    records.forEach { record ->

        val credits =
            record.credits.toDoubleOrNull()
                ?: 0.0

        if (credits > 0) {

            weightedPoints +=
                academicGradePoint(
                    record.grade
                ) * credits

            totalCredits += credits
        }
    }

    return if (totalCredits > 0) {
        weightedPoints / totalCredits
    } else {
        0.0
    }
}

private fun academicCalculateCGPA(
    records: List<AcademicRecord>
): Double {

    return academicCalculateSGPA(
        records
    )
}

private fun academicFormatNumber(
    number: Double
): String {

    return if (
        number == number.toInt().toDouble()
    ) {

        number.toInt().toString()

    } else {

        String.format(
            Locale.getDefault(),
            "%.1f",
            number
        )
    }
}

// ============================================================
//                  END OF ACADEMIC RECORDS
// ============================================================
// ============================================================
//                         EXPENSES
// ============================================================

data class ExpenseRecord(
    val id: Long = System.currentTimeMillis(),
    val amount: String = "",
    val category: String = "Food",
    val description: String = "",
    val date: String = ""
)

private val expenseCategories = listOf(
    "Food",
    "Transport",
    "Study",
    "Hostel",
    "Personal",
    "Recharge",
    "Other"
)

private fun expensePrefs(context: Context) =
    context.getSharedPreferences(
        "student_buddy_prefs",
        Context.MODE_PRIVATE
    )

private fun saveExpenseRecords(
    context: Context,
    expenses: List<ExpenseRecord>
) {
    val array = JSONArray()

    expenses.forEach { expense ->
        val obj = JSONObject()

        obj.put("id", expense.id)
        obj.put("amount", expense.amount)
        obj.put("category", expense.category)
        obj.put("description", expense.description)
        obj.put("date", expense.date)

        array.put(obj)
    }

    expensePrefs(context)
        .edit()
        .putString(
            "expense_records_v2",
            array.toString()
        )
        .apply()
}

private fun loadExpenseRecords(
    context: Context
): List<ExpenseRecord> {

    val result = mutableListOf<ExpenseRecord>()

    val saved =
        expensePrefs(context)
            .getString(
                "expense_records_v2",
                null
            )
            ?: return result

    try {
        val array = JSONArray(saved)

        for (i in 0 until array.length()) {

            val obj = array.getJSONObject(i)

            result.add(
                ExpenseRecord(
                    id = obj.optLong(
                        "id",
                        System.currentTimeMillis() + i
                    ),
                    amount = obj.optString(
                        "amount",
                        ""
                    ),
                    category = obj.optString(
                        "category",
                        "Other"
                    ),
                    description = obj.optString(
                        "description",
                        ""
                    ),
                    date = obj.optString(
                        "date",
                        ""
                    )
                )
            )
        }

    } catch (_: Exception) {
    }

    return result
}

@Composable
fun ExpensesScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current

    val expenses =
        remember {
            mutableStateListOf<ExpenseRecord>().apply {
                addAll(
                    loadExpenseRecords(context)
                )
            }
        }

    var showAddDialog by remember {
        mutableStateOf(false)
    }

    var editingExpense by remember {
        mutableStateOf<ExpenseRecord?>(null)
    }

    var deletingExpense by remember {
        mutableStateOf<ExpenseRecord?>(null)
    }

    val today = remember {
        Calendar.getInstance()
    }

    val currentMonth =
        today.get(Calendar.MONTH)

    val currentYear =
        today.get(Calendar.YEAR)

    fun isCurrentMonth(
        dateString: String
    ): Boolean {

        if (dateString.isBlank()) {
            return false
        }

        return try {

            val format =
                SimpleDateFormat(
                    "dd/MM/yyyy",
                    Locale.getDefault()
                )

            val date =
                format.parse(dateString)
                    ?: return false

            val calendar =
                Calendar.getInstance()

            calendar.time = date

            calendar.get(Calendar.MONTH) ==
                    currentMonth &&
                    calendar.get(Calendar.YEAR) ==
                    currentYear

        } catch (_: Exception) {
            false
        }
    }

    val monthlyExpenses =
        expenses.filter {
            isCurrentMonth(it.date)
        }

    val monthlyTotal =
        monthlyExpenses.sumOf {
            it.amount.toDoubleOrNull() ?: 0.0
        }

    fun categoryTotal(
        category: String
    ): Double {
        return monthlyExpenses
            .filter {
                it.category == category
            }
            .sumOf {
                it.amount.toDoubleOrNull() ?: 0.0
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                rememberScrollState()
            )
            .padding(18.dp)
    ) {

        // HEADER
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            TextButton(
                onClick = onBack
            ) {
                Text("←")
            }

            Text(
                text = "Expenses",
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )

            Button(
                onClick = {
                    editingExpense = null
                    showAddDialog = true
                }
            ) {
                Text("+")
            }
        }

        Spacer(
            Modifier.height(16.dp)
        )

        // THIS MONTH
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                horizontalAlignment =
                    Alignment.CenterHorizontally
            ) {

                Text(
                    text = "This Month",
                    fontSize = 16.sp
                )

                Spacer(
                    Modifier.height(6.dp)
                )

                Text(
                    text =
                        "₹" +
                                expenseFormatAmount(
                                    monthlyTotal
                                ),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(
            Modifier.height(18.dp)
        )

        // CATEGORY SUMMARY
        Text(
            text = "Category Summary",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            Modifier.height(10.dp)
        )

        ExpenseCategoryRow(
            "🍔",
            "Food",
            categoryTotal("Food")
        )

        ExpenseCategoryRow(
            "🚌",
            "Transport",
            categoryTotal("Transport")
        )

        ExpenseCategoryRow(
            "📚",
            "Study",
            categoryTotal("Study")
        )

        ExpenseCategoryRow(
            "🏠",
            "Hostel",
            categoryTotal("Hostel")
        )

        ExpenseCategoryRow(
            "🛍️",
            "Personal",
            categoryTotal("Personal")
        )

        ExpenseCategoryRow(
            "📱",
            "Recharge",
            categoryTotal("Recharge")
        )

        ExpenseCategoryRow(
            "📦",
            "Other",
            categoryTotal("Other")
        )

        Spacer(
            Modifier.height(12.dp)
        )

        HorizontalDivider()

        Spacer(
            Modifier.height(10.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = "Total",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )

            Text(
                text =
                    "₹" +
                            expenseFormatAmount(
                                monthlyTotal
                            ),
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(
            Modifier.height(18.dp)
        )

        Button(
            onClick = {
                editingExpense = null
                showAddDialog = true
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("ADD EXPENSE")
        }

        Spacer(
            Modifier.height(24.dp)
        )

        // EXPENSE HISTORY
        Text(
            text = "Expense History",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            Modifier.height(10.dp)
        )

        if (expenses.isEmpty()) {

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    text = "No expenses added yet.",
                    modifier = Modifier.padding(16.dp)
                )
            }

        } else {

            val sortedExpenses =
                expenses.sortedByDescending {
                    expenseDateToMillis(it.date)
                }

            sortedExpenses.forEach { expense ->

                ExpenseHistoryCard(
                    expense = expense,

                    onEdit = {
                        editingExpense = expense
                        showAddDialog = true
                    },

                    onDelete = {
                        deletingExpense = expense
                    }
                )

                Spacer(
                    Modifier.height(10.dp)
                )
            }
        }
    }

    // ADD / EDIT
    if (showAddDialog) {

        ExpenseDialog(
            existingExpense = editingExpense,

            onDismiss = {
                showAddDialog = false
                editingExpense = null
            },

            onSave = { newExpense ->

                if (editingExpense == null) {

                    expenses.add(newExpense)

                } else {

                    val index =
                        expenses.indexOfFirst {
                            it.id ==
                                    editingExpense!!.id
                        }

                    if (index >= 0) {
                        expenses[index] = newExpense
                    }
                }

                saveExpenseRecords(
                    context,
                    expenses.toList()
                )

                showAddDialog = false
                editingExpense = null
            }
        )
    }

    // DELETE
    if (deletingExpense != null) {

        AlertDialog(

            onDismissRequest = {
                deletingExpense = null
            },

            title = {
                Text("Delete Expense?")
            },

            text = {
                Text(
                    "This expense will be permanently removed."
                )
            },

            confirmButton = {

                Button(
                    onClick = {

                        expenses.removeAll {
                            it.id ==
                                    deletingExpense!!.id
                        }

                        saveExpenseRecords(
                            context,
                            expenses.toList()
                        )

                        deletingExpense = null
                    }
                ) {
                    Text("DELETE")
                }
            },

            dismissButton = {

                TextButton(
                    onClick = {
                        deletingExpense = null
                    }
                ) {
                    Text("CANCEL")
                }
            }
        )
    }
}

@Composable
private fun ExpenseCategoryRow(
    icon: String,
    name: String,
    amount: Double
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 7.dp),
        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Text(
            text = icon,
            fontSize = 20.sp
        )

        Spacer(
            Modifier.width(10.dp)
        )

        Text(
            text = name,
            modifier = Modifier.weight(1f)
        )

        Text(
            text =
                "₹" +
                        expenseFormatAmount(
                            amount
                        ),
            fontWeight =
                FontWeight.SemiBold
        )
    }
}

@Composable
private fun ExpenseHistoryCard(
    expense: ExpenseRecord,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {

    val icon =
        when (expense.category) {

            "Food" -> "🍔"
            "Transport" -> "🚌"
            "Study" -> "📚"
            "Hostel" -> "🏠"
            "Personal" -> "🛍️"
            "Recharge" -> "📱"
            else -> "📦"
        }

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.padding(14.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Text(
                    text = icon,
                    fontSize = 22.sp
                )

                Spacer(
                    Modifier.width(10.dp)
                )

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text =
                            if (
                                expense.description.isBlank()
                            ) {
                                expense.category
                            } else {
                                expense.description
                            },
                        fontSize = 17.sp,
                        fontWeight =
                            FontWeight.Bold
                    )

                    Text(
                        text =
                            "${expense.category} • ${expense.date}"
                    )
                }

                Text(
                    text =
                        "₹" +
                                expenseFormatAmount(
                                    expense.amount
                                        .toDoubleOrNull()
                                        ?: 0.0
                                ),
                    fontWeight =
                        FontWeight.Bold
                )
            }

            Spacer(
                Modifier.height(10.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.spacedBy(8.dp)
            ) {

                Button(
                    onClick = onEdit,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("EDIT")
                }

                Button(
                    onClick = onDelete,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("DELETE")
                }
            }
        }
    }
}

@Composable
private fun ExpenseDialog(
    existingExpense: ExpenseRecord?,
    onDismiss: () -> Unit,
    onSave: (ExpenseRecord) -> Unit
) {

    var amount by remember(existingExpense) {
        mutableStateOf(
            existingExpense?.amount ?: ""
        )
    }

    var category by remember(existingExpense) {
        mutableStateOf(
            existingExpense?.category ?: "Food"
        )
    }

    var description by remember(existingExpense) {
        mutableStateOf(
            existingExpense?.description ?: ""
        )
    }

    var date by remember(existingExpense) {

        mutableStateOf(
            existingExpense?.date
                ?: SimpleDateFormat(
                    "dd/MM/yyyy",
                    Locale.getDefault()
                ).format(Date())
        )
    }

    var categoryExpanded by remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current

    AlertDialog(

        onDismissRequest = onDismiss,

        title = {
            Text(
                if (existingExpense == null) {
                    "Add Expense"
                } else {
                    "Edit Expense"
                }
            )
        },

        text = {

            Column(
                modifier =
                    Modifier.verticalScroll(
                        rememberScrollState()
                    )
            ) {

                OutlinedTextField(

                    value = amount,

                    onValueChange = { value ->

                        if (
                            value.isEmpty() ||
                            value.all {
                                it.isDigit() ||
                                        it == '.'
                            }
                        ) {
                            amount = value
                        }
                    },

                    label = {
                        Text("Amount")
                    },

                    prefix = {
                        Text("₹ ")
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    singleLine = true
                )

                Spacer(
                    Modifier.height(10.dp)
                )

                Box(
                    modifier =
                        Modifier.fillMaxWidth()
                ) {

                    OutlinedButton(
                        onClick = {
                            categoryExpanded = true
                        },
                        modifier =
                            Modifier.fillMaxWidth()
                    ) {

                        Text(
                            "Category: $category"
                        )
                    }

                    DropdownMenu(

                        expanded =
                            categoryExpanded,

                        onDismissRequest = {
                            categoryExpanded = false
                        }

                    ) {

                        expenseCategories.forEach { option ->

                            DropdownMenuItem(

                                text = {
                                    Text(option)
                                },

                                onClick = {

                                    category = option

                                    categoryExpanded =
                                        false
                                }
                            )
                        }
                    }
                }

                Spacer(
                    Modifier.height(10.dp)
                )

                OutlinedTextField(

                    value = description,

                    onValueChange = {
                        description = it
                    },

                    label = {
                        Text("Description")
                    },

                    modifier =
                        Modifier.fillMaxWidth()
                )

                Spacer(
                    Modifier.height(10.dp)
                )

                OutlinedButton(

                    onClick = {

                        val calendar =
                            Calendar.getInstance()

                        try {

                            val format =
                                SimpleDateFormat(
                                    "dd/MM/yyyy",
                                    Locale.getDefault()
                                )

                            val selectedDate =
                                format.parse(date)

                            if (
                                selectedDate != null
                            ) {
                                calendar.time =
                                    selectedDate
                            }

                        } catch (_: Exception) {
                        }

                        DatePickerDialog(

                            context,

                            { _, year, month, day ->

                                val selected =
                                    Calendar.getInstance()

                                selected.set(
                                    year,
                                    month,
                                    day
                                )

                                date =
                                    SimpleDateFormat(
                                        "dd/MM/yyyy",
                                        Locale.getDefault()
                                    ).format(
                                        selected.time
                                    )
                            },

                            calendar.get(
                                Calendar.YEAR
                            ),

                            calendar.get(
                                Calendar.MONTH
                            ),

                            calendar.get(
                                Calendar.DAY_OF_MONTH
                            )
                        ).show()
                    },

                    modifier =
                        Modifier.fillMaxWidth()

                ) {

                    Text(
                        "📅  Date: $date"
                    )
                }
            }
        },

        confirmButton = {

            Button(

                onClick = {

                    val cleanAmount =
                        amount.trim()

                    if (
                        cleanAmount.isNotBlank() &&
                        cleanAmount.toDoubleOrNull() != null
                    ) {

                        onSave(

                            ExpenseRecord(

                                id =
                                    existingExpense?.id
                                        ?: System.currentTimeMillis(),

                                amount =
                                    cleanAmount,

                                category =
                                    category,

                                description =
                                    description.trim(),

                                date =
                                    date
                            )
                        )
                    }
                }

            ) {

                Text(
                    if (existingExpense == null) {
                        "SAVE"
                    } else {
                        "UPDATE"
                    }
                )
            }
        },

        dismissButton = {

            TextButton(
                onClick = onDismiss
            ) {
                Text("CANCEL")
            }
        }
    )
}

private fun expenseDateToMillis(
    dateString: String
): Long {

    if (dateString.isBlank()) {
        return 0L
    }

    return try {

        SimpleDateFormat(
            "dd/MM/yyyy",
            Locale.getDefault()
        )
            .parse(dateString)
            ?.time
            ?: 0L

    } catch (_: Exception) {
        0L
    }
}

private fun expenseFormatAmount(
    amount: Double
): String {

    return if (
        amount == amount.toLong().toDouble()
    ) {

        amount.toLong().toString()

    } else {

        String.format(
            Locale.getDefault(),
            "%.2f",
            amount
        )
    }
}

// ============================================================
//                    END OF EXPENSES
// ============================================================

@Composable
fun SuggestionScreen(
    onBack: () -> Unit,
    onSave: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }
    var saved by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(18.dp)
    ) {
        ScreenHeader("SUGGESTION", onBack)

        Text(
            "Give your suggestion to improve STUDENT BUDDY.",
            fontSize = 17.sp
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = text,
            onValueChange = {
                text = it
                saved = false
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Your suggestion") },
            minLines = 6
        )

        Spacer(Modifier.height(10.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                if (text.trim().isNotEmpty()) {
                    onSave(text.trim())
                    StudentBuddyStorage.openEmail(
                        context,
                        "STUDENT BUDDY - Suggestion",
                        text.trim()
                    )
                    saved = true
                }
            }
        ) {
            Text("SAVE SUGGESTION")
        }

        if (saved) {
            Spacer(Modifier.height(10.dp))
            Text(
                "Suggestion saved on this device.",
                color = Color(0xFF2E7D32),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun BugReportScreen(
    onBack: () -> Unit,
    onSave: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }
    var saved by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(18.dp)
    ) {
        ScreenHeader("REPORT A BUG", onBack)


        Text(
            "Describe the problem you found in the app.",
            fontSize = 17.sp
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = text,
            onValueChange = {
                text = it
                saved = false
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Bug description") },
            minLines = 6
        )

        Spacer(Modifier.height(10.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                if (text.trim().isNotEmpty()) {
                    onSave(text.trim())
                    StudentBuddyStorage.openEmail(
                        context,
                        "STUDENT BUDDY - Bug Report",
                        text.trim()
                    )
                    saved = true
                }
            }
        ) {
            Text("SAVE BUG REPORT")
        }

        if (saved) {
            Spacer(Modifier.height(10.dp))
            Text(
                "Bug report saved on this device.",
                color = Color(0xFF2E7D32),
                fontWeight = FontWeight.Bold
            )
        }

    }
}
