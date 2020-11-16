// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.android.communication.chat;

import com.azure.android.communication.chat.implementation.AzureCommunicationChatServiceImpl;
import com.azure.android.communication.chat.models.AddChatParticipantsRequest;
import com.azure.android.communication.chat.models.ChatMessage;
import com.azure.android.communication.chat.models.ChatThread;
import com.azure.android.communication.chat.models.ChatThreadInfo;
import com.azure.android.communication.chat.models.ChatParticipant;
import com.azure.android.communication.chat.models.CreateChatThreadRequest;
import com.azure.android.communication.chat.models.ErrorException;
import com.azure.android.communication.chat.models.MultiStatusResponse;
import com.azure.android.communication.chat.models.ReadReceipt;
import com.azure.android.communication.chat.models.SendChatMessageRequest;
import com.azure.android.communication.chat.models.SendChatMessageResult;
import com.azure.android.communication.chat.models.SendReadReceiptRequest;
import com.azure.android.communication.chat.models.UpdateChatMessageRequest;
import com.azure.android.communication.chat.models.UpdateChatThreadRequest;
import com.azure.android.core.http.Callback;
import com.azure.android.core.http.ServiceClient;
import com.azure.android.core.http.responsepaging.AsyncPagedDataCollection;
import com.azure.android.core.util.paging.Page;
import okhttp3.Interceptor;
import org.threeten.bp.OffsetDateTime;

/**
 * Initializes a new instance of the asynchronous AzureCommunicationChatService type.
 */
public final class AzureCommunicationChatServiceAsyncClient {
    private AzureCommunicationChatServiceImpl serviceClient;

    /**
     * Initializes an instance of AzureCommunicationChatService client.
     */
    AzureCommunicationChatServiceAsyncClient(AzureCommunicationChatServiceImpl serviceClient) {
        this.serviceClient = serviceClient;
    }

    /**
     * Gets read receipts for a thread.
     *
     * @param chatThreadId Thread id to get the read receipts for.
     * @param callback the Callback that receives the response.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ErrorException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     */
    public void listChatReadReceipts(String chatThreadId, final Callback<Page<ReadReceipt>> callback) {
        this.serviceClient.listChatReadReceipts(chatThreadId, callback);
    }

    /**
     * Gets read receipts for a thread.
     *
     * @param chatThreadId Thread id to get the read receipts for.
     * @param collectionCallback the Callback that receives the response collection.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ErrorException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     */
    public void listChatReadReceiptsPages(String chatThreadId, final Callback<AsyncPagedDataCollection<ReadReceipt, Page<ReadReceipt>>> collectionCallback) {
        this.serviceClient.listChatReadReceiptsPagesAsync(chatThreadId, collectionCallback);
    }

    /**
     * Sends a read receipt event to a thread, on behalf of a user.
     *
     * @param chatThreadId Thread id to send the read receipt event to.
     * @param body Request payload for sending a read receipt.
     * @param callback the Callback that receives the response.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ErrorException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     */
    public void sendChatReadReceipt(String chatThreadId, SendReadReceiptRequest body, final Callback<Void> callback) {
        this.serviceClient.sendChatReadReceipt(chatThreadId, body, callback);
    }

    /**
     * Sends a message to a thread.
     *
     * @param chatThreadId The thread id to send the message to.
     * @param body Details of the message to send.
     * @param callback the Callback that receives the response.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ErrorException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     */
    public void sendChatMessage(String chatThreadId, SendChatMessageRequest body, final Callback<SendChatMessageResult> callback) {
        this.serviceClient.sendChatMessage(chatThreadId, body, callback);
    }

    /**
     * Gets a list of messages from a thread.
     *
     * @param chatThreadId The thread id of the message.
     * @param maxPageSize The maximum number of messages to be returned per page.
     * @param startTime The earliest point in time to get messages up to. The timestamp should be in ISO8601 format: `yyyy-MM-ddTHH:mm:ssZ`.
     * @param callback the Callback that receives the response.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ErrorException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     */
    public void listChatMessages(String chatThreadId, Integer maxPageSize, OffsetDateTime startTime, final Callback<Page<ChatMessage>> callback) {
        this.serviceClient.listChatMessages(chatThreadId, maxPageSize, startTime, callback);
    }

    /**
     * Gets a list of messages from a thread.
     *
     * @param chatThreadId The thread id of the message.
     * @param callback the Callback that receives the response.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ErrorException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     */
    public void listChatMessages(String chatThreadId, final Callback<Page<ChatMessage>> callback) {
        this.serviceClient.listChatMessages(chatThreadId, callback);
    }

    /**
     * Gets a list of messages from a thread.
     *
     * @param chatThreadId The thread id of the message.
     * @param collectionCallback the Callback that receives the response collection.
     * @param maxPageSize The maximum number of messages to be returned per page.
     * @param startTime The earliest point in time to get messages up to. The timestamp should be in ISO8601 format: `yyyy-MM-ddTHH:mm:ssZ`.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ErrorException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     */
    public void listChatMessagesPages(String chatThreadId, Integer maxPageSize, OffsetDateTime startTime, final Callback<AsyncPagedDataCollection<ChatMessage, Page<ChatMessage>>> collectionCallback) {
        this.serviceClient.listChatMessagesPagesAsync(chatThreadId, maxPageSize, startTime, collectionCallback);
    }

    /**
     * Gets a message by id.
     *
     * @param chatThreadId The thread id to which the message was sent.
     * @param chatMessageId The message id.
     * @param callback the Callback that receives the response.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ErrorException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     */
    public void getChatMessage(String chatThreadId, String chatMessageId, final Callback<ChatMessage> callback) {
        this.serviceClient.getChatMessage(chatThreadId, chatMessageId, callback);
    }

    /**
     * Updates a message.
     *
     * @param chatThreadId The thread id to which the message was sent.
     * @param chatMessageId The message id.
     * @param body Details of the request to update the message.
     * @param callback the Callback that receives the response.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ErrorException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     */
    public void updateChatMessage(String chatThreadId, String chatMessageId, UpdateChatMessageRequest body, final Callback<Void> callback) {
        this.serviceClient.updateChatMessage(chatThreadId, chatMessageId, body, callback);
    }

    /**
     * Deletes a message.
     *
     * @param chatThreadId The thread id to which the message was sent.
     * @param chatMessageId The message id.
     * @param callback the Callback that receives the response.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ErrorException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     */
    public void deleteChatMessage(String chatThreadId, String chatMessageId, final Callback<Void> callback) {
        this.serviceClient.deleteChatMessage(chatThreadId, chatMessageId, callback);
    }

    /**
     * Posts a typing event to a thread, on behalf of a user.
     *
     * @param chatThreadId Id of the thread.
     * @param callback the Callback that receives the response.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ErrorException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     */
    public void sendTypingNotification(String chatThreadId, final Callback<Void> callback) {
        this.serviceClient.sendTypingNotification(chatThreadId, callback);
    }

    /**
     * Gets the participants of a thread.
     *
     * @param chatThreadId Thread id to get participants for.
     * @param callback the Callback that receives the response.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ErrorException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     */
    public void listChatParticipants(String chatThreadId, final Callback<Page<ChatParticipant>> callback) {
        this.serviceClient.listChatParticipants(chatThreadId, callback);
    }

    /**
     * Gets the participants of a thread.
     *
     * @param chatThreadId Thread id to get participants for.
     * @param collectionCallback the Callback that receives the response collection.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ErrorException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     */
    public void listChatParticipantsPages(String chatThreadId, final Callback<AsyncPagedDataCollection<ChatParticipant, Page<ChatParticipant>>> collectionCallback) {
        this.serviceClient.listChatParticipantsPagesAsync(chatThreadId, collectionCallback);
    }

    /**
     * Adds thread participants to a thread. If participants already exist, no change occurs.
     *
     * @param chatThreadId Id of the thread to add participants to.
     * @param body Thread participants to be added to the thread.
     * @param callback the Callback that receives the response.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ErrorException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     */
    public void addChatParticipants(String chatThreadId, AddChatParticipantsRequest body, final Callback<Void> callback) {
        this.serviceClient.addChatParticipants(chatThreadId, body, callback);
    }

    /**
     * Remove a participant from a thread.
     *
     * @param chatThreadId Thread id to remove the participant from.
     * @param chatParticipantId Id of the thread participant to remove from the thread.
     * @param callback the Callback that receives the response.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ErrorException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     */
    public void removeChatParticipant(String chatThreadId, String chatParticipantId, final Callback<Void> callback) {
        this.serviceClient.removeChatParticipant(chatThreadId, chatParticipantId, callback);
    }

    /**
     * Creates a chat thread.
     *
     * @param body Request payload for creating a chat thread.
     * @param callback the Callback that receives the response.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ErrorException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     */
    public void createChatThread(CreateChatThreadRequest body, final Callback<MultiStatusResponse> callback) {
        this.serviceClient.createChatThread(body, callback);
    }

    /**
     * Gets the list of chat threads of a user.
     *
     * @param maxPageSize The maximum number of chat threads returned per page.
     * @param startTime The earliest point in time to get chat threads up to. The timestamp should be in ISO8601 format: `yyyy-MM-ddTHH:mm:ssZ`.
     * @param callback the Callback that receives the response.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ErrorException thrown if the request is rejected by server.
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
     * @throws ErrorException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     */
    public void listChatThreads(final Callback<Page<ChatThreadInfo>> callback) {
        this.serviceClient.listChatThreads(callback);
    }

    /**
     * Gets the list of chat threads of a user.
     *
     * @param collectionCallback the Callback that receives the response collection.
     * @param maxPageSize The maximum number of chat threads returned per page.
     * @param startTime The earliest point in time to get chat threads up to. The timestamp should be in ISO8601 format: `yyyy-MM-ddTHH:mm:ssZ`.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ErrorException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     */
    public void listChatThreadsPages(Integer maxPageSize, OffsetDateTime startTime, final Callback<AsyncPagedDataCollection<ChatThreadInfo, Page<ChatThreadInfo>>> collectionCallback) {
        this.serviceClient.listChatThreadsPagesAsync(maxPageSize, startTime, collectionCallback);
    }

    /**
     * Updates a thread's properties.
     *
     * @param chatThreadId The id of the thread to update.
     * @param body Request payload for updating a chat thread.
     * @param callback the Callback that receives the response.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ErrorException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     */
    public void updateChatThread(String chatThreadId, UpdateChatThreadRequest body, final Callback<Void> callback) {
        this.serviceClient.updateChatThread(chatThreadId, body, callback);
    }

    /**
     * Gets a chat thread.
     *
     * @param chatThreadId Thread id to get.
     * @param callback the Callback that receives the response.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ErrorException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     */
    public void getChatThread(String chatThreadId, final Callback<ChatThread> callback) {
        this.serviceClient.getChatThread(chatThreadId, callback);
    }

    /**
     * Deletes a thread.
     *
     * @param chatThreadId Thread id to delete.
     * @param callback the Callback that receives the response.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ErrorException thrown if the request is rejected by server.
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
     * @throws ErrorException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     */
    public void listChatReadReceiptsNext(String nextLink, final Callback<Page<ReadReceipt>> callback) {
        this.serviceClient.listChatReadReceiptsNext(nextLink, callback);
    }

    /**
     * Get the next page of items.
     *
     * @param nextLink null
     * @param callback the Callback that receives the response.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ErrorException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     */
    public void listChatMessagesNext(String nextLink, final Callback<Page<ChatMessage>> callback) {
        this.serviceClient.listChatMessagesNext(nextLink, callback);
    }

    /**
     * Get the next page of items.
     *
     * @param nextLink null
     * @param callback the Callback that receives the response.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ErrorException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     */
    public void listChatParticipantsNext(String nextLink, final Callback<Page<ChatParticipant>> callback) {
        this.serviceClient.listChatParticipantsNext(nextLink, callback);
    }

    /**
     * Get the next page of items.
     *
     * @param nextLink null
     * @param callback the Callback that receives the response.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ErrorException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     */
    public void listChatThreadsNext(String nextLink, final Callback<Page<ChatThreadInfo>> callback) {
        this.serviceClient.listChatThreadsNext(nextLink, callback);
    }

    /**
     * A builder for creating a new instance of the AzureCommunicationChatServiceAsyncClient type.
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

        /**
         * Builds an instance of AzureCommunicationChatServiceAsyncClient with the provided parameters.
         *
         * @return an instance of AzureCommunicationChatServiceAsyncClient.
         */
        public AzureCommunicationChatServiceAsyncClient build() {
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
            AzureCommunicationChatServiceImpl internalClient = new AzureCommunicationChatServiceImpl(serviceClientBuilder.build(), endpoint);
            return new AzureCommunicationChatServiceAsyncClient(internalClient);
        }
    }
}
