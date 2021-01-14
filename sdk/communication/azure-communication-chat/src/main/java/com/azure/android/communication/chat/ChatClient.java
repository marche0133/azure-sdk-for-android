// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.android.communication.chat;

import com.azure.android.communication.chat.implementation.AzureCommunicationChatServiceImpl;
import com.azure.android.communication.chat.implementation.ChatsImpl;
import com.azure.android.communication.chat.models.ChatThread;
import com.azure.android.communication.chat.models.ChatThreadInfo;
import com.azure.android.communication.chat.models.ChatThreadsInfoCollection;
import com.azure.android.communication.chat.models.CreateChatThreadRequest;
import com.azure.android.communication.chat.models.CreateChatThreadResult;
import com.azure.android.communication.chat.models.ErrorException;
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
 * Initializes a new instance of the synchronous AzureCommunicationChatService type.
 */
public final class ChatClient {
    private ChatsImpl serviceClient;

    /**
     * Initializes an instance of Chats client.
     */
    ChatClient(ChatsImpl serviceClient) {
        this.serviceClient = serviceClient;
    }

    /**
     * Creates a chat thread.
     *
     * @param createChatThreadRequest Request payload for creating a chat thread.
     * @param repeatabilityRequestID If specified, the client directs that the request is repeatable; that is, that the client can make the request multiple times with the same Repeatability-Request-ID and get back an appropriate response without the server executing the request multiple times. The value of the Repeatability-Request-ID is an opaque string representing a client-generated, globally unique for all time, identifier for the request. It is recommended to use version 4 (random) UUIDs.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ErrorException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return result of the create chat thread operation.
     */
    public Response<CreateChatThreadResult> createChatThreadWithRestResponse(CreateChatThreadRequest createChatThreadRequest, String repeatabilityRequestID) {
        return this.serviceClient.createChatThreadWithRestResponse(createChatThreadRequest, repeatabilityRequestID);
    }

    /**
     * Creates a chat thread.
     *
     * @param createChatThreadRequest Request payload for creating a chat thread.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ErrorException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return result of the create chat thread operation.
     */
    public CreateChatThreadResult createChatThread(CreateChatThreadRequest createChatThreadRequest) {
        return this.serviceClient.createChatThread(createChatThreadRequest);
    }

    /**
     * Gets the list of chat threads of a user.
     *
     * @param maxPageSize The maximum number of chat threads returned per page.
     * @param startTime The earliest point in time to get chat threads up to. The timestamp should be in RFC3339 format: `yyyy-MM-ddTHH:mm:ssZ`.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ErrorException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return the list of chat threads of a user.
     */
    public PagedDataResponseCollection<ChatThreadInfo, Page<ChatThreadInfo>> listChatThreadsWithPageResponse(Integer maxPageSize, OffsetDateTime startTime) {
        return this.serviceClient.listChatThreadsWithPageResponse(maxPageSize, startTime);
    }

    /**
     * Gets the list of chat threads of a user.
     *
     * @param maxPageSize The maximum number of chat threads returned per page.
     * @param startTime The earliest point in time to get chat threads up to. The timestamp should be in RFC3339 format: `yyyy-MM-ddTHH:mm:ssZ`.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ErrorException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return the list of chat threads of a user.
     */
    public PagedDataCollection<ChatThreadInfo, Page<ChatThreadInfo>> listChatThreadsWithPage(Integer maxPageSize, OffsetDateTime startTime) {
        return this.serviceClient.listChatThreadsWithPage(maxPageSize, startTime);
    }

    /**
     * Gets a chat thread.
     *
     * @param chatThreadId Id of the thread.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ErrorException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return a chat thread.
     */
    public Response<ChatThread> getChatThreadWithRestResponse(String chatThreadId) {
        return this.serviceClient.getChatThreadWithRestResponse(chatThreadId);
    }

    /**
     * Deletes a thread.
     *
     * @param chatThreadId Id of the thread to be deleted.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ErrorException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return the completion.
     */
    public Response<Void> deleteChatThreadWithRestResponse(String chatThreadId) {
        return this.serviceClient.deleteChatThreadWithRestResponse(chatThreadId);
    }

    /**
     * A builder for creating a new instance of the ChatClient type.
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

        /*
         * base url of the service
         */
        private String baseUrl;

        /**
         * Sets base url of the service.
         *
         * @param baseUrl the baseUrl value.
         * @return the Builder.
         */
        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        /**
         * Builds an instance of ChatClient with the provided parameters.
         *
         * @return an instance of ChatClient.
         */
        public ChatClient build() {
            if (baseUrl == null) {
                this.baseUrl = "{endpoint}";
            }
            if (serviceClientBuilder == null) {
                if (endpoint == null) {
                    throw new IllegalArgumentException("Missing required parameters 'endpoint'.");
                }
                this.serviceClientBuilder = new ServiceClient.Builder();
            }
            if (endpoint != null) {
                final String retrofitBaseUrl = baseUrl.replace("{endpoint}", endpoint);
                serviceClientBuilder.setBaseUrl(retrofitBaseUrl);
            }
            if (credentialInterceptor != null) {
                serviceClientBuilder.setCredentialsInterceptor(credentialInterceptor);
            }

            if (userAgentInterceptor != null) {
                serviceClientBuilder.addInterceptor(userAgentInterceptor);
            }

            AzureCommunicationChatServiceImpl internalClient = new AzureCommunicationChatServiceImpl(serviceClientBuilder.build(), endpoint);
            return new ChatClient(internalClient.getChats());
        }
    }
}
