package com.azure.android.communication.chat;

import com.azure.android.communication.chat.models.ChatMessage;
import com.azure.android.communication.chat.models.ChatThreadInfo;
import com.azure.android.communication.chat.models.ChatThreadMember;
import com.azure.android.core.http.Response;
import com.azure.android.core.http.ServiceClient;
import com.azure.android.core.http.responsepaging.PagedDataResponseCollection;
import com.azure.android.core.util.paging.Page;
import com.azure.android.core.util.paging.PagedDataCollection;

import org.junit.After;
import org.junit.Test;
import org.threeten.bp.OffsetDateTime;

import java.util.concurrent.TimeUnit;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static junit.framework.TestCase.assertEquals;

public class chatClientTest {

    private static final MockWebServer mockWebServer = new MockWebServer();
    private static final String BASE_URL = mockWebServer.url("/").toString();
    private static AzureCommunicationChatServiceClient chatServiceClient =
        new AzureCommunicationChatServiceClient.Builder()
            .serviceClientBuilder(new ServiceClient.Builder().setBaseUrl(BASE_URL))
            .build();

    @After
    public void tearDown() throws InterruptedException {
        // For ensuring the responses enqueued are consumed before making the next call.
        mockWebServer.takeRequest(20, TimeUnit.MILLISECONDS);
    }

    @Test
    public void listThreadPages() {
        mockThreadsResponse(5);

        final PagedDataCollection<ChatThreadInfo, Page<ChatThreadInfo>> pages = chatServiceClient.listChatThreadsWithPage(5, OffsetDateTime.now());
        final Page<ChatThreadInfo> firstPage = pages.getFirstPage();
        assertEquals(5, firstPage.getItems().size());

        mockThreadsResponse(3);

        final Page<ChatThreadInfo> nextPage = pages.getPage(firstPage.getNextPageId());
        assertEquals(3, nextPage.getItems().size());

        assertEquals(5, pages.getPage(nextPage.getPreviousPageId()).getItems().size());
    }

    @Test
    public void listThreadPagesWithResponse() {
        mockThreadsResponse(5);

        final PagedDataResponseCollection<ChatThreadInfo, Page<ChatThreadInfo>> pagesWithResponse = chatServiceClient.listChatThreadsWithPageResponse(5, OffsetDateTime.now());
        final Response<Page<ChatThreadInfo>> firstPage = pagesWithResponse.getFirstPage();
        assertEquals(5, firstPage.getValue().getItems().size());

        mockThreadsResponse(3);

        final Response<Page<ChatThreadInfo>> nextPage = pagesWithResponse.getPage(firstPage.getValue().getNextPageId());
        assertEquals(3, nextPage.getValue().getItems().size());

        assertEquals(5, pagesWithResponse.getPage(nextPage.getValue().getPreviousPageId()).getValue().getItems().size());
    }

    @Test
    public void listThreadPagesWithRestResponse() {
        mockThreadsResponse(5);

        final Response<Page<ChatThreadMember>> firstPage = chatServiceClient.listChatThreadMembersWithRestResponse("threadId");
        assertEquals(5, firstPage.getValue().getItems().size());

        mockThreadsResponse(3);

        final Response<Page<ChatThreadInfo>> nextPage = chatServiceClient.listChatThreadsNextWithRestResponse(firstPage.getValue().getNextPageId());
        assertEquals(3, nextPage.getValue().getItems().size());
    }

    @Test
    public void listMessagePages() {
        mockMessagesResponse(5);

        final PagedDataCollection<ChatMessage, Page<ChatMessage>> messages = chatServiceClient.listChatMessagesWithPage("threadId", 5, OffsetDateTime.now());
        final Page<ChatMessage> firstPage = messages.getFirstPage();
        assertEquals(5, firstPage.getItems().size());

        mockMessagesResponse(3);

        final Page<ChatMessage> nextPage = messages.getPage(firstPage.getNextPageId());
        assertEquals(3, nextPage.getItems().size());

        assertEquals(5, messages.getPage(nextPage.getPreviousPageId()).getItems().size());
    }

    @Test
    public void listMessagePagesWithResponse() {
        mockMessagesResponse(5);

        final PagedDataResponseCollection<ChatMessage, Page<ChatMessage>> pagesWithResponse = chatServiceClient.listChatMessagesWithPageResponse("threadId",5, OffsetDateTime.now());
        final Response<Page<ChatMessage>> firstPage = pagesWithResponse.getFirstPage();
        assertEquals(5, firstPage.getValue().getItems().size());

        mockMessagesResponse(3);

        final Response<Page<ChatMessage>> nextPage = pagesWithResponse.getPage(firstPage.getValue().getNextPageId());
        assertEquals(3, nextPage.getValue().getItems().size());

        assertEquals(5, pagesWithResponse.getPage(nextPage.getValue().getPreviousPageId()).getValue().getItems().size());
    }

    @Test
    public void listMessagePagesWithRestResponse() {
        mockMessagesResponse(5);

        final Response<Page<ChatMessage>> firstPage = chatServiceClient.listChatMessagesWithRestResponse("threadId", 5, OffsetDateTime.now());
        assertEquals(5, firstPage.getValue().getItems().size());

        mockMessagesResponse(3);

        final Response<Page<ChatMessage>> nextPage = chatServiceClient.listChatMessagesNextWithRestResponse(firstPage.getValue().getNextPageId());
        assertEquals(3, nextPage.getValue().getItems().size());
    }

    private void mockThreadsResponse(int n) {
        MockResponse mockResponse = new MockResponse()
            .setResponseCode(200)
            .setBody(ThreadsMocker.mockThreads(n));
        mockWebServer.enqueue(mockResponse);
    }

    private void mockMessagesResponse(int n) {
        String body = MessagesMocker.mockThreadMessages(n);
        MockResponse mockResponse = new MockResponse()
            .setResponseCode(200)
            .setBody(body);
        mockWebServer.enqueue(mockResponse);
    }
}
