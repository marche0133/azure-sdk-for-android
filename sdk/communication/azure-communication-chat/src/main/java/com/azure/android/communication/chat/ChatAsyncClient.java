// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.android.communication.chat;

import android.content.Context;

import com.azure.android.communication.chat.implementation.AzureCommunicationChatServiceImpl;
import com.azure.android.communication.chat.implementation.ChatsImpl;
import com.azure.android.communication.chat.models.ChatThread;
import com.azure.android.communication.chat.models.ChatThreadInfo;
import com.azure.android.communication.chat.models.ChatThreadsInfoCollection;
import com.azure.android.communication.chat.models.CommunicationErrorResponseException;
import com.azure.android.communication.chat.models.CreateChatThreadRequest;
import com.azure.android.communication.chat.models.CreateChatThreadResult;
import com.azure.android.communication.chat.signaling.CommunicationSignalingClient;
import com.azure.android.communication.chat.signaling.RealTimeNotificationCallback;
import com.azure.android.communication.chat.signaling.SignalingClient;
import com.azure.android.core.http.Callback;
import com.azure.android.core.http.Response;
import com.azure.android.core.http.ServiceClient;
import com.azure.android.core.http.exception.HttpResponseException;
import com.azure.android.core.http.responsepaging.AsyncPagedDataCollection;
import com.azure.android.core.http.responsepaging.AsyncPagedDataRetriever;
import com.azure.android.core.http.responsepaging.PagedDataResponseCollection;
import com.azure.android.core.http.responsepaging.PagedDataResponseRetriever;
import com.azure.android.core.util.paging.Page;
import com.azure.android.core.util.paging.PagedDataCollection;
import com.azure.android.core.util.paging.PagedDataRetriever;
import okhttp3.Interceptor;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import org.threeten.bp.OffsetDateTime;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Initializes a new instance of the asynchronous AzureCommunicationChatService type.
 */
public final class ChatAsyncClient {
    private ChatsImpl serviceClient;
    private SignalingClient signalingClient;
    private boolean isRealtimeNotificationsStarted;

    /**
     * Initializes an instance of Chats client.
     */
    ChatAsyncClient(ChatsImpl serviceClient, SignalingClient signalingClient) {
        this.serviceClient = serviceClient;
        this.signalingClient = signalingClient;
        this.isRealtimeNotificationsStarted = false;
    }

    /**
     * Creates a chat thread.
     *
     * @param createChatThreadRequest Request payload for creating a chat thread.
     * @param repeatabilityRequestID If specified, the client directs that the request is repeatable; that is, that the client can make the request multiple times with the same Repeatability-Request-ID and get back an appropriate response without the server executing the request multiple times. The value of the Repeatability-Request-ID is an opaque string representing a client-generated, globally unique for all time, identifier for the request. It is recommended to use version 4 (random) UUIDs.
     * @param callback the Callback that receives the response.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws CommunicationErrorResponseException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     */
    public void createChatThread(CreateChatThreadRequest createChatThreadRequest, String repeatabilityRequestID, final Callback<CreateChatThreadResult> callback) {
        this.serviceClient.createChatThread(createChatThreadRequest, repeatabilityRequestID, callback);
    }

    /**
     * Creates a chat thread.
     *
     * @param createChatThreadRequest Request payload for creating a chat thread.
     * @param callback the Callback that receives the response.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws CommunicationErrorResponseException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     */
    public void createChatThread(CreateChatThreadRequest createChatThreadRequest, final Callback<CreateChatThreadResult> callback) {
        this.serviceClient.createChatThread(createChatThreadRequest, callback);
    }

    /**
     * Gets the list of chat threads of a user.
     *
     * @param maxPageSize The maximum number of chat threads returned per page.
     * @param startTime The earliest point in time to get chat threads up to. The timestamp should be in RFC3339 format: `yyyy-MM-ddTHH:mm:ssZ`.
     * @param callback the Callback that receives the response.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws CommunicationErrorResponseException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     */
    public void listChatThreads(Integer maxPageSize, OffsetDateTime startTime, final Callback<Page<ChatThreadInfo>> callback) {
        this.serviceClient.listChatThreads(maxPageSize, startTime, callback);
    }

    /**
     * Gets the list of chat threads of a user.
     *
     * @param callback the Callback that receives the response.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws CommunicationErrorResponseException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     */
    public void listChatThreads(final Callback<Page<ChatThreadInfo>> callback) {
        this.serviceClient.listChatThreads(callback);
    }

    /**
     * Gets the list of chat threads of a user.
     *
     * @param maxPageSize The maximum number of chat threads returned per page.
     * @param startTime The earliest point in time to get chat threads up to. The timestamp should be in RFC3339 format: `yyyy-MM-ddTHH:mm:ssZ`.
     * @param callback the Callback that receives the response collection.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws CommunicationErrorResponseException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     */
    public void listChatThreadsPages(Integer maxPageSize, OffsetDateTime startTime, final Callback<AsyncPagedDataCollection<ChatThreadInfo, Page<ChatThreadInfo>>> callback) {
        this.serviceClient.listChatThreadsPagesAsync(maxPageSize, startTime, callback);
    }

    /**
     * Gets a chat thread.
     *
     * @param chatThreadId Id of the thread.
     * @param callback the Callback that receives the response.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws CommunicationErrorResponseException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     */
    public void getChatThread(String chatThreadId, final Callback<ChatThread> callback) {
        this.serviceClient.getChatThread(chatThreadId, callback);
    }

    /**
     * Deletes a thread.
     *
     * @param chatThreadId Id of the thread to be deleted.
     * @param callback the Callback that receives the response.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws CommunicationErrorResponseException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     */
    public void deleteChatThread(String chatThreadId, final Callback<Void> callback) {
        this.serviceClient.deleteChatThread(chatThreadId, callback);
    }

    /**
     * Get the next page of items.
     *
     * @param nextLink null
     * @param callback the Callback that receives the response.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws CommunicationErrorResponseException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     */
    public void listChatThreadsNext(String nextLink, final Callback<Page<ChatThreadInfo>> callback) {
        this.serviceClient.listChatThreadsNext(nextLink, callback);
    }

    /**
     * Receive real-time messages and notifications.
     */
    public void startRealtimeNotifications() {
        if (this.signalingClient == null) {
            throw new Error("Signaling client not initialized");
        }

        if (this.isRealtimeNotificationsStarted) {
            return;
        }

        this.isRealtimeNotificationsStarted = true;
        this.signalingClient.start();
    }

    /**
     * Stop receiving real-time messages and notifications.
     */
    public void stopRealtimeNotifications() {
        if (this.signalingClient == null) {
            throw new Error("Signaling client not initialized");
        }

        this.isRealtimeNotificationsStarted = false;
        this.signalingClient.stop();
    }

    /**
     * Listen to a chat event.
     */
    public void on(String chatEventId, String listenerId, RealTimeNotificationCallback listener) {
        if (this.signalingClient == null) {
            throw new Error("Realtime notification parameters (context, userToken) are not set");
        }

        if (!this.isRealtimeNotificationsStarted) {
            throw new Error(
                "You must call startRealtimeNotifications before you can subscribe to events."
            );
        }

        this.signalingClient.on(chatEventId, listenerId, listener);
    }

    /**
     * Stop listening to a chat event.
     */
    public void off(String chatEventId, String listenerId) {
        if (this.signalingClient == null) {
            throw new Error("Signaling client not initialized");
        }

        this.signalingClient.off(chatEventId, listenerId);
    }

    /**
     * A builder for creating a new instance of the ChatAsyncClient type.
     */
    public static final class Builder {
        /*
         * The endpoint of the Azure Communication resource.
         */
        private String endpoint;

        /**
         * Sets The endpoint of the Azure Communication resource.
         *
         * @param endpoint the endpoint value.
         * @return the Builder.
         */
        public Builder endpoint(String endpoint) {
            this.endpoint = endpoint;
            return this;
        }

        /*
         * The Azure Core generic ServiceClient Builder.
         */
        private ServiceClient.Builder serviceClientBuilder;

        /**
         * Sets The Azure Core generic ServiceClient Builder.
         *
         * @param serviceClientBuilder the serviceClientBuilder value.
         * @return the Builder.
         */
        public Builder serviceClientBuilder(ServiceClient.Builder serviceClientBuilder) {
            this.serviceClientBuilder = serviceClientBuilder;
            return this;
        }

        /*
         * The Interceptor to set intercept request and set credentials.
         */
        private Interceptor credentialInterceptor;

        private Interceptor userAgentInterceptor;

        private Context context;

        private String userToken;

        private CommunicationSignalingClient communicationSignalingClient;

        /**
         * Sets The Interceptor to set intercept request and set credentials.
         *
         * @param credentialInterceptor the credentialInterceptor value.
         * @return the Builder.
         */
        public Builder credentialInterceptor(Interceptor credentialInterceptor) {
            this.credentialInterceptor = credentialInterceptor;
            return this;
        }

        public Builder userAgentInterceptor(Interceptor userAgentInterceptor) {
            this.userAgentInterceptor = userAgentInterceptor;
            return this;
        }

        public Builder realtimeNotificationParams(Context context, String userToken) {
            this.context = context;
            this.userToken = userToken;
            return this;
        }

        /**
         * Builds an instance of ChatAsyncClient with the provided parameters.
         *
         * @return an instance of ChatAsyncClient.
         */
        public ChatAsyncClient build() {
            if (serviceClientBuilder == null) {
                if (endpoint == null) {
                    throw new IllegalArgumentException("Missing required parameters 'endpoint'.");
                }
                this.serviceClientBuilder = new ServiceClient.Builder();
            }
            if (endpoint != null) {
                final String retrofitBaseUrl = endpoint;
                serviceClientBuilder.setBaseUrl(retrofitBaseUrl);
            }
            if (credentialInterceptor != null) {
                serviceClientBuilder.setCredentialsInterceptor(credentialInterceptor);
            }
            if (userAgentInterceptor != null) {
                serviceClientBuilder.addInterceptor(userAgentInterceptor);
            }
            if (context != null && userToken != null) {
                communicationSignalingClient = new CommunicationSignalingClient(userToken, context);
            }
            AzureCommunicationChatServiceImpl internalClient = new AzureCommunicationChatServiceImpl(serviceClientBuilder.build(), endpoint);
            return new ChatAsyncClient(internalClient.getChats(), communicationSignalingClient);
        }
    }
}
