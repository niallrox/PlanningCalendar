package com.ifmo.lowlevel.planningcalendar.ext.google.calendar

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.util.DateTime
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.services.calendar.Calendar
import com.google.api.services.calendar.CalendarScopes
import com.google.api.services.calendar.model.Event
import com.google.api.services.calendar.model.EventDateTime
import com.ifmo.lowlevel.planningcalendar.ApplicationEnvironment
import com.ifmo.lowlevel.planningcalendar.model.Plan
import com.ifmo.lowlevel.planningcalendar.model.PlanState
import com.ifmo.lowlevel.planningcalendar.model.common.SysConsts
import mu.KotlinLogging
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStreamReader
import java.time.ZoneOffset
import java.util.stream.Collectors


object CalendarHandler {

    private val logger = KotlinLogging.logger {}

    private val calendarId = ApplicationEnvironment.getPropertiesByName("google").getProperty("calendar.id")

    private val JSON_FACTORY: JsonFactory = GsonFactory.getDefaultInstance()

    private val service: Calendar

    private const val TOKENS_DIRECTORY_PATH = "tokens"

    private val SCOPES = CalendarScopes.all().stream().collect(Collectors.toList())
    private val CREDENTIALS_FILE_PATH = ApplicationEnvironment.getPropertiesByName("google").getProperty("credentials")

    private fun getCredentials(HTTP_TRANSPORT: NetHttpTransport): Credential {
        val inputStream =
            CalendarHandler::class.java.getResourceAsStream(CREDENTIALS_FILE_PATH)
                ?: throw FileNotFoundException("Resource not found: $CREDENTIALS_FILE_PATH")
        val clientSecrets =
            GoogleClientSecrets.load(JSON_FACTORY, InputStreamReader(inputStream))

        val flow = GoogleAuthorizationCodeFlow.Builder(
            HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES
        )
            .setDataStoreFactory(FileDataStoreFactory(File(TOKENS_DIRECTORY_PATH)))
            .setAccessType("offline")
            .build()
        val receiver = LocalServerReceiver.Builder()
            .setPort(ApplicationEnvironment.getPropertiesByName("google").getProperty("calendar.port").toInt())
            .build()

        return AuthorizationCodeInstalledApp(flow, receiver).authorize("user")
    }

    init {
        val httpTransport = GoogleNetHttpTransport.newTrustedTransport()
        service = Calendar.Builder(httpTransport, JSON_FACTORY, getCredentials(httpTransport))
            .setApplicationName(SysConsts.APPLICATION_NAME)
            .build()
        logger.debug { "Calendar initialized" }
    }


    private fun findEvents(
        service: Calendar,
    ): List<Event> {
        val execute = service.Calendars().get(calendarId).execute()

        val events = service.events().list(execute.id)
            .setTimeMax(DateTime("endDateTime"))
            .setOrderBy(SysConsts.DEFAULT_ORDERING)
            .setSingleEvents(true)
            .execute()

        return events.items
    }

    fun insertEvent(plan: Plan) {
        val startTimeLocal =
            when (plan.getPlanState()) {
                PlanState.DAY -> plan.getDeadline()!!.toLocalDate().atStartOfDay()
                PlanState.MONTH -> plan.getDeadline()!!.toLocalDate().withDayOfMonth(1).atStartOfDay()
                PlanState.YEAR -> plan.getDeadline()!!.toLocalDate()
                    .withMonth(1).withDayOfMonth(1).atStartOfDay()
            }
        val startTime = DateTime(startTimeLocal.toInstant(ZoneOffset.UTC).toEpochMilli())
        val endTime = DateTime(plan.getDeadline()!!.toInstant(ZoneOffset.UTC).toEpochMilli())
        val summary = plan.getGoal()
        val description = plan.getDescription()
        service.Events().insert(calendarId, buildEvent(startTime, endTime, summary, description)).execute()
        logger.debug { "Event inserted into calendar: $calendarId" }
    }

    private fun buildEvent(startTime: DateTime,
                           endTime: DateTime,
                           summary: String,
                           description: String): Event {
        val insertingEvent = Event()
            .setSummary(summary)
            .setDescription(description)

        val start = EventDateTime()
            .setDateTime(startTime)
            .setTimeZone(SysConsts.TIME_ZONE)

        val end = EventDateTime()
            .setDateTime(endTime)
            .setTimeZone(SysConsts.TIME_ZONE)

        insertingEvent.start = start
        insertingEvent.end = end

        return insertingEvent
    }
}