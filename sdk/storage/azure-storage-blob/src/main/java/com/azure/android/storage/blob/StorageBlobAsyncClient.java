// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.android.storage.blob;

import android.content.Context;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.work.Constraints;
import androidx.work.NetworkType;

import com.azure.android.core.http.Callback;
import com.azure.android.core.http.CallbackWithHeader;
import com.azure.android.core.http.ServiceClient;
import com.azure.android.core.http.interceptor.AddDateInterceptor;
import com.azure.android.core.http.interceptor.RequestIdInterceptor;
import com.azure.android.core.util.CancellationToken;
import com.azure.android.core.util.CoreUtil;
import com.azure.android.storage.blob.implementation.util.ModelHelper;
import com.azure.android.storage.blob.interceptor.MetadataInterceptor;
import com.azure.android.storage.blob.interceptor.NormalizeEtagInterceptor;
import com.azure.android.storage.blob.models.AccessTier;
import com.azure.android.storage.blob.models.BlobDeleteHeaders;
import com.azure.android.storage.blob.models.BlobDownloadHeaders;
import com.azure.android.storage.blob.models.BlobGetPropertiesHeaders;
import com.azure.android.storage.blob.models.BlobGetTagsHeaders;
import com.azure.android.storage.blob.models.BlobHttpHeaders;
import com.azure.android.storage.blob.models.BlobItem;
import com.azure.android.storage.blob.models.BlobRange;
import com.azure.android.storage.blob.models.BlobRequestConditions;
import com.azure.android.storage.blob.models.BlobSetHttpHeadersHeaders;
import com.azure.android.storage.blob.models.BlobSetMetadataHeaders;
import com.azure.android.storage.blob.models.BlobSetTagsHeaders;
import com.azure.android.storage.blob.models.BlobSetTierHeaders;
import com.azure.android.storage.blob.models.BlobTags;
import com.azure.android.storage.blob.models.BlobsPage;
import com.azure.android.storage.blob.models.BlockBlobCommitBlockListHeaders;
import com.azure.android.storage.blob.models.BlockBlobItem;
import com.azure.android.storage.blob.models.BlockBlobStageBlockHeaders;
import com.azure.android.storage.blob.models.ContainerCreateHeaders;
import com.azure.android.storage.blob.models.ContainerDeleteHeaders;
import com.azure.android.storage.blob.models.ContainerGetPropertiesHeaders;
import com.azure.android.storage.blob.models.ListBlobFlatSegmentHeaders;
import com.azure.android.storage.blob.models.CpkInfo;
import com.azure.android.storage.blob.models.DeleteSnapshotsOptionType;
import com.azure.android.storage.blob.models.ListBlobsFlatSegmentResponse;
import com.azure.android.storage.blob.models.ListBlobsIncludeItem;
import com.azure.android.storage.blob.models.ListBlobsOptions;
import com.azure.android.storage.blob.models.RehydratePriority;
import com.azure.android.storage.blob.models.PublicAccessType;
import com.azure.android.storage.blob.transfer.DownloadRequest;
import com.azure.android.storage.blob.transfer.StorageBlobClientMap;
import com.azure.android.storage.blob.transfer.TransferClient;
import com.azure.android.storage.blob.transfer.TransferInfo;
import com.azure.android.storage.blob.transfer.UploadRequest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.Interceptor;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Client for Storage Blob service.
 *
 * <p>
 * This client is instantiated through {@link StorageBlobAsyncClient.Builder}.
 */
public class StorageBlobAsyncClient {
    private final String id;
    private final ServiceClient serviceClient;
    private final StorageBlobServiceImpl storageBlobServiceClient;
    private final Constraints transferConstraints;

    private StorageBlobAsyncClient(String id, ServiceClient serviceClient, String serviceVersion, Constraints transferConstraints) {
        this.id = id;
        this.serviceClient = serviceClient;
        this.storageBlobServiceClient = new StorageBlobServiceImpl(this.serviceClient, serviceVersion);
        this.transferConstraints = transferConstraints;
    }

    /**
     * Creates a new {@link Builder} with initial configuration copied from this {@link StorageBlobAsyncClient}.
     *
     * @param storageBlobClientId The unique ID for the new {@link StorageBlobAsyncClient}. This identifier is used to
     *                            associate the {@link StorageBlobAsyncClient} with the upload and download transfers it
     *                            initiates. When a transfer is reloaded from disk (e.g. after an application crash),
     *                            it can only be resumed once a client with the same storageBlobClientId has been
     *                            initialized.
     * @return A new {@link Builder}.
     */
    public StorageBlobAsyncClient.Builder newBuilder(String storageBlobClientId) {
        return new Builder(storageBlobClientId, this);
    }

    /**
     * Gets the blob service base URL.
     *
     * @return The blob service base URL.
     */
    public String getBlobServiceUrl() {
        return this.serviceClient.getBaseUrl();
    }

    /**
     * Upload the content of a file.
     *
     * @param context       The application context.
     * @param containerName The container to upload the file to.
     * @param blobName      The name of the target blob holding the uploaded file.
     * @param computeMd5    Whether or not the library should calculate the md5 and send it for the service to verify.
     * @param file          The local file to upload.
     * @return A LiveData that streams {@link TransferInfo} describing the current state of the transfer.
     */
    public LiveData<TransferInfo> upload(Context context,
                                         String containerName,
                                         String blobName,
                                         Boolean computeMd5,
                                         File file) {
        final UploadRequest request = new UploadRequest.Builder()
            .storageClientId(this.id)
            .containerName(containerName)
            .blobName(blobName)
            .computeMd5(computeMd5)
            .file(file)
            .constraints(this.transferConstraints)
            .build();
        return TransferClient.getInstance(context)
            .upload(request);
    }

    /**
     * Upload content identified by a given URI.
     *
     * @param context       The application context.
     * @param containerName The container to upload the file to.
     * @param blobName      The name of the target blob holding the uploaded file.
     * @param computeMd5    Whether or not the library should calculate the md5 and send it for the service to verify.
     * @param contentUri    The URI to the Content to upload, the contentUri is resolved using
     *                      {@link android.content.ContentResolver#openAssetFileDescriptor(Uri, String)} with mode as
     *                      "r". The supported URI schemes are: 'content://', 'file://' and 'android.resource://'.
     * @return A LiveData that streams {@link TransferInfo} describing the current state of the transfer.
     */
    public LiveData<TransferInfo> upload(Context context,
                                         String containerName,
                                         String blobName,
                                         Boolean computeMd5,
                                         Uri contentUri) {
        final UploadRequest request = new UploadRequest.Builder()
            .storageClientId(this.id)
            .containerName(containerName)
            .blobName(blobName)
            .computeMd5(computeMd5)
            .contentUri(context, contentUri)
            .constraints(this.transferConstraints)
            .build();
        return TransferClient.getInstance(context)
            .upload(request);
    }

    /**
     * Download a blob.
     *
     * @param context       The application context.
     * @param containerName The container to download the blob from.
     * @param blobName      The name of the target blob to download.
     * @param file          The local file to download to.
     * @return A LiveData that streams {@link TransferInfo} describing the current state of the download.
     */
    public LiveData<TransferInfo> download(Context context,
                                           String containerName,
                                           String blobName,
                                           File file) {
        final DownloadRequest request = new DownloadRequest.Builder()
            .storageClientId(this.id)
            .containerName(containerName)
            .blobName(blobName)
            .file(file)
            .constraints(this.transferConstraints)
            .build();
        return TransferClient.getInstance(context)
            .download(request);
    }

    /**
     * Download a blob.
     *
     * @param context       The application context.
     * @param containerName The container to download the blob from.
     * @param blobName      The name of the target blob to download.
     * @param contentUri    The URI to the local content where the downloaded blob will be stored.
     * @return LiveData that streams {@link TransferInfo} describing the current state of the download.
     */
    public LiveData<TransferInfo> download(Context context,
                                           String containerName,
                                           String blobName,
                                           Uri contentUri) {
        final DownloadRequest request = new DownloadRequest.Builder()
            .storageClientId(this.id)
            .containerName(containerName)
            .blobName(blobName)
            .contentUri(context, contentUri)
            .constraints(this.transferConstraints)
            .build();
        return TransferClient.getInstance(context)
            .download(request);
    }

    /**
     * Pause a transfer identified by the given transfer ID. The pause operation is a best-effort, and a transfer
     * that is already executing may continue to transfer.
     * <p>
     * Upon successful scheduling of the pause, any observer observing on {@link LiveData} for this
     * transfer receives a {@link TransferInfo} event with state {@link TransferInfo.State#USER_PAUSED}.
     *
     * @param context    The application context.
     * @param transferId The transfer ID identifies the transfer to pause.
     */
    public void pause(Context context, long transferId) {
        TransferClient.getInstance(context)
            .pause(transferId);
    }

    /**
     * Resume a paused transfer.
     *
     * @param context    The application context
     * @param transferId The transfer ID identifies the transfer to resume.
     * @return A LiveData that streams {@link TransferInfo} describing the current state of the transfer.
     */
    public LiveData<TransferInfo> resume(Context context, long transferId) {
        return TransferClient.getInstance(context)
            .resume(transferId);
    }

    /**
     * Cancel a transfer identified by the given transfer ID. The cancel operation is a best-effort, and a transfer
     * that is already executing may continue to transfer.
     * <p>
     * Upon successful scheduling of the cancellation, any observer observing on {@link LiveData} for
     * this transfer receives a {@link TransferInfo} event with state {@link TransferInfo.State#CANCELLED}.
     *
     * @param context    The application context.
     * @param transferId The transfer ID identifies the transfer to cancel.
     */
    public void cancel(Context context, long transferId) {
        TransferClient.getInstance(context)
            .cancel(transferId);
    }

    /**
     * Creates a new container within a storage account. If a container with the same name already exists, the operation
     * fails.
     *
     * @param containerName The container name.
     * @param callback      Callback that receives the response.
     */
    public void createContainer(String containerName,
                                CallbackWithHeader<Void, ContainerCreateHeaders> callback) {
        storageBlobServiceClient.createContainer(containerName, callback);
    }

    /**
     * Creates a new container within a storage account. If a container with the same name already exists, the operation
     * fails.
     *
     * @param containerName     The container name.
     * @param timeout           The timeout parameter is expressed in seconds. For more information, see
     *                          &lt;a href="https://docs.microsoft.com/en-us/rest/api/storageservices/setting-timeouts-for-blob-service-operations"&gt;Setting Timeouts for Blob Service Operations.&lt;/a&gt;.
     * @param metadata          Metadata to associate with the container.
     * @param publicAccessType  Specifies how the data in this container is available to the public. See the
     *                          x-ms-blob-public-access header in the Azure Docs for more information. Pass null
     *                          for no public access.
     * @param cancellationToken The token to request cancellation.
     * @param callback          Callback that receives the response.
     * @return The response information returned from the server when creating a container.
     */
    public void createContainer(String containerName,
                                Integer timeout,
                                Map<String, String> metadata,
                                PublicAccessType publicAccessType,
                                CancellationToken cancellationToken,
                                CallbackWithHeader<Void, ContainerCreateHeaders> callback) {

        storageBlobServiceClient.createContainer(containerName,
            timeout,
            metadata,
            publicAccessType,
            cancellationToken,
            callback);
    }

    /**
     * Gets a list of blobs identified by a page id in a given container.
     *
     * @param pageId        Identifies the portion of the list to be returned.
     * @param containerName The container name.
     * @param options       The page options.
     * @param callback      Callback that receives the retrieved blob list.
     */
    public void getBlobsInPage(String pageId,
                               String containerName,
                               ListBlobsOptions options,
                               Callback<BlobsPage> callback) {
        this.storageBlobServiceClient.listBlobFlatSegment(pageId,
            containerName,
            options,
            new CallbackWithHeader<ListBlobsFlatSegmentResponse, ListBlobFlatSegmentHeaders>() {
                @Override
                public void onSuccess(ListBlobsFlatSegmentResponse result, ListBlobFlatSegmentHeaders header, Response response) {
                    List<BlobItem> list = result.getSegment() == null
                        ? new ArrayList<>(0)
                        : result.getSegment().getBlobItems();
                    callback.onSuccess(new BlobsPage(list, pageId, result.getNextMarker()), response);
                }

                @Override
                public void onFailure(Throwable throwable, Response response) {
                    callback.onFailure(throwable, response);
                }
            });
    }

    /**
     * Gets a list of blobs identified by a page id in a given container.
     *
     * @param pageId            Identifies the portion of the list to be returned.
     * @param containerName     The container name.
     * @param prefix            Filters the results to return only blobs whose name begins with the specified prefix.
     * @param maxResults        Specifies the maximum number of blobs to return.
     * @param include           Include this parameter to specify one or more datasets to include in the response.
     * @param timeout           The timeout parameter is expressed in seconds. For more information, see
     *                          &lt;a href="https://docs.microsoft.com/en-us/rest/api/storageservices/setting-timeouts-for-blob-service-operations"&gt;Setting Timeouts for Blob Service Operations.&lt;/a&gt;.
     * @param callback          Callback that receives the response.
     * @param cancellationToken The token to request cancellation.
     */
    public void getBlobsInPage(String pageId,
                               String containerName,
                               String prefix,
                               Integer maxResults,
                               List<ListBlobsIncludeItem> include,
                               Integer timeout,
                               CancellationToken cancellationToken,
                               Callback<BlobsPage> callback) {
        this.storageBlobServiceClient.listBlobFlatSegment(pageId,
            containerName,
            prefix,
            maxResults,
            include,
            timeout,
            cancellationToken,
            new CallbackWithHeader<ListBlobsFlatSegmentResponse, ListBlobFlatSegmentHeaders>() {
                @Override
                public void onSuccess(ListBlobsFlatSegmentResponse result, ListBlobFlatSegmentHeaders header, Response response) {
                    List<BlobItem> list = result.getSegment() == null
                        ? new ArrayList<>(0)
                        : result.getSegment().getBlobItems();
                    callback.onSuccess(new BlobsPage(list, pageId, result.getNextMarker()), response);
                }

                @Override
                public void onFailure(Throwable throwable, Response response) {
                    callback.onFailure(throwable, response);
                }
            });
    }

    /**
     * Reads the blob's metadata and properties.
     *
     * @param containerName The container name.
     * @param blobName      The blob name.
     * @param callback      Callback that receives the response.
     */
    public void getBlobProperties(String containerName,
                                  String blobName,
                                  CallbackWithHeader<Void, BlobGetPropertiesHeaders> callback) {
        storageBlobServiceClient.getBlobProperties(containerName,
            blobName,
            callback);
    }

    /**
     * Reads a blob's metadata and properties.
     *
     * @param containerName         The container name.
     * @param blobName              The blob name.
     * @param snapshot              The snapshot parameter is an opaque DateTime value that, when present, specifies
     *                              the blob snapshot to retrieve. For more information on working with blob snapshots,
     *                              see &lt;a href="https://docs.microsoft.com/en-us/rest/api/storageservices/creating-a-snapshot-of-a-blob"&gt;Creating a Snapshot of a Blob.&lt;/a&gt;.
     * @param timeout               The timeout parameter is expressed in seconds. For more information, see
     *                              &lt;a href="https://docs.microsoft.com/en-us/rest/api/storageservices/setting-timeouts-for-blob-service-operations"&gt;Setting Timeouts for Blob Service Operations.&lt;/a&gt;.
     * @param blobRequestConditions Object that contains values which will restrict the successful operation of a
     *                              variety of requests to the conditions present. These conditions are entirely
     *                              optional.
     * @param cpkInfo               Additional parameters for the operation.
     * @param cancellationToken     The token to request cancellation.
     * @param callback              Callback that receives the response.
     */
    public void getBlobProperties(String containerName,
                                  String blobName,
                                  String snapshot,
                                  Integer timeout,
                                  BlobRequestConditions blobRequestConditions,
                                  CpkInfo cpkInfo,
                                  CancellationToken cancellationToken,
                                  CallbackWithHeader<Void, BlobGetPropertiesHeaders> callback) {
        storageBlobServiceClient.getBlobProperties(containerName,
            blobName,
            snapshot,
            timeout,
            blobRequestConditions,
            cpkInfo,
            cancellationToken,
            callback);
    }

    /**
     * Changes a blob's HTTP header properties. If only one HTTP header is updated, the others will all be erased. In
     * order to preserve existing values, they must be passed alongside the header being changed.
     *
     * @param containerName The container name.
     * @param blobName      The blob name.
     * @param headers       {@link BlobHttpHeaders}
     * @param callback      Callback that receives the response.
     */
    public void setBlobHttpHeaders(String containerName,
                                   String blobName,
                                   BlobHttpHeaders headers,
                                   CallbackWithHeader<Void, BlobSetHttpHeadersHeaders> callback) {
        storageBlobServiceClient.setBlobHttpHeaders(containerName, blobName, headers, callback);
    }

    /**
     * Changes a blob's HTTP header properties. If only one HTTP header is updated, the others will all be erased. In
     * order to preserve existing values, they must be passed alongside the header being changed.
     *
     * @param containerName     The container name.
     * @param blobName          The blob name.
     * @param timeout           The timeout parameter is expressed in seconds. For more information, see
     *                          &lt;a href="https://docs.microsoft.com/en-us/rest/api/storageservices/fileservices/setting-timeouts-for-blob-service-operations"&gt;Setting Timeouts for Blob Service Operations.&lt;/a&gt;.
     * @param requestConditions {@link BlobRequestConditions}
     * @param headers           {@link BlobHttpHeaders}
     * @param cancellationToken The token to request cancellation.
     * @param callback          Callback that receives the response.
     */
    public void setBlobHttpHeaders(String containerName,
                                   String blobName,
                                   Integer timeout,
                                   BlobRequestConditions requestConditions,
                                   BlobHttpHeaders headers,
                                   CancellationToken cancellationToken,
                                   CallbackWithHeader<Void, BlobSetHttpHeadersHeaders> callback) {
        storageBlobServiceClient.setBlobHttpHeaders(containerName,
            blobName,
            timeout,
            requestConditions,
            headers,
            cancellationToken,
            callback);
    }

    /**
     * Changes a blob's metadata. The specified metadata in this method will replace existing metadata. If old values
     * must be preserved, they must be downloaded and included in the call to this method.
     *
     * @param containerName The container name.
     * @param blobName      The blob name.
     * @param metadata      Metadata to associate with the blob.
     * @param callback      Callback that receives the response.
     */
    public void setBlobMetadata(String containerName,
                                String blobName,
                                Map<String, String> metadata,
                                CallbackWithHeader<Void, BlobSetMetadataHeaders> callback) {
        storageBlobServiceClient.setBlobMetadata(containerName, blobName, metadata, callback);
    }

    /**
     * Changes a blob's metadata. The specified metadata in this method will replace existing metadata. If old values
     * must be preserved, they must be downloaded and included in the call to this method.
     *
     * @param containerName     The container name.
     * @param blobName          The blob name.
     * @param timeout           The timeout parameter is expressed in seconds. For more information, see
     *                          &lt;a href="https://docs.microsoft.com/en-us/rest/api/storageservices/fileservices/setting-timeouts-for-blob-service-operations"&gt;Setting Timeouts for Blob Service Operations.&lt;/a&gt;.
     * @param requestConditions {@link BlobRequestConditions}
     * @param metadata          Metadata to associate with the blob.
     * @param cpkInfo           Additional parameters for the operation.
     * @param cancellationToken The token to request cancellation.
     * @param callback          Callback that receives the response.
     */
    public void setBlobMetadata(String containerName,
                                String blobName,
                                Integer timeout,
                                BlobRequestConditions requestConditions,
                                Map<String, String> metadata,
                                CpkInfo cpkInfo,
                                CancellationToken cancellationToken,
                                CallbackWithHeader<Void, BlobSetMetadataHeaders> callback) {
        storageBlobServiceClient.setBlobMetadata(containerName,
            blobName,
            timeout,
            requestConditions,
            metadata,
            cpkInfo,
            cancellationToken,
            callback);
    }

    /**
     * Sets the blob's tier.
     *
     * @param containerName The container name.
     * @param blobName      The blob name.
     * @param tier          The access tier.
     * @param callback      The callback that receives the response.
     */
    public void setBlobTier(String containerName,
                            String blobName,
                            AccessTier tier,
                            CallbackWithHeader<Void, BlobSetTierHeaders> callback) {
        storageBlobServiceClient.setBlobTier(containerName,
            blobName,
            tier,
            callback);
    }

    /**
     * Sets the blob's tier.
     *
     * @param containerName     The container name.
     * @param blobName          The blob name.
     * @param tier              The access tier.
     * @param snapshot          The snapshot parameter is an opaque DateTime value that, when present, specifies
     *                          the blob snapshot to retrieve. For more information on working with blob snapshots,
     *                          see &lt;a href="https://docs.microsoft.com/en-us/rest/api/storageservices/creating-a-snapshot-of-a-blob"&gt;Creating a Snapshot of a Blob.&lt;/a&gt;.
     * @param timeout           The timeout parameter is expressed in seconds. For more information, see
     *                          &lt;a href="https://docs.microsoft.com/en-us/rest/api/storageservices/setting-timeouts-for-blob-service-operations"&gt;Setting Timeouts for Blob Service Operations.&lt;/a&gt;.
     * @param rehydratePriority The rehydrate priority.
     */
    public void setBlobTier(String containerName,
                            String blobName,
                            AccessTier tier,
                            String snapshot,
                            Integer timeout,
                            RehydratePriority rehydratePriority,
                            String leaseId,
                            String tagsConditions,
                            CancellationToken cancellationToken,
                            CallbackWithHeader<Void, BlobSetTierHeaders> callback) {

        storageBlobServiceClient.setBlobTier(containerName,
            blobName,
            tier,
            snapshot,
            null, /* TODO: (gapra) Add version id when there is support for STG73 */
            timeout,
            rehydratePriority,
            leaseId,
            tagsConditions,
            cancellationToken,
            callback);
    }

    /**
     * Gets the container's properties.
     *
     * @param containerName The container name.
     * @param callback      Callback that receives the response.
     */
    public void getContainerProperties(String containerName,
                                       CallbackWithHeader<Void, ContainerGetPropertiesHeaders> callback) {
        storageBlobServiceClient.getContainerProperties(containerName,
            callback);
    }

    /**
     * Gets the container's properties.
     *
     * @param containerName         The container name.
     * @param timeout               The timeout parameter is expressed in seconds. For more information, see
     *                              &lt;a href="https://docs.microsoft.com/en-us/rest/api/storageservices/setting-timeouts-for-blob-service-operations"&gt;Setting Timeouts for Blob Service Operations.&lt;/a&gt;.
     * @param blobRequestConditions Object that contains values which will restrict the successful operation of a
     *                              variety of requests to the conditions present. These conditions are entirely
     *                              optional.
     * @param cancellationToken     The token to request cancellation.
     * @param callback              Callback that receives the response.
     */
    public void getContainerProperties(String containerName,
                                       Integer timeout,
                                       BlobRequestConditions blobRequestConditions,
                                       CancellationToken cancellationToken,
                                       CallbackWithHeader<Void, ContainerGetPropertiesHeaders> callback) {
        blobRequestConditions = blobRequestConditions == null ? new BlobRequestConditions() : blobRequestConditions;

        storageBlobServiceClient.getContainerProperties(containerName,
            timeout,
            blobRequestConditions.getLeaseId(),
            cancellationToken,
            callback);
    }

    /**
     * Reads the entire blob.
     *
     * <p>
     * This method will execute a raw HTTP GET in order to download a single blob to the destination.
     * It is **STRONGLY** recommended that you use the {@link StorageBlobAsyncClient#download(Context, String, String, File)}
     * or {@link StorageBlobAsyncClient#download(Context, String, String, Uri)} method instead - that method will
     * manage the transfer in the face of changing network conditions, and is able to transfer multiple
     * blocks in parallel.
     *
     * @param containerName The container name.
     * @param blobName      The blob name.
     * @param callback      Callback that receives the response.
     */
    public void rawDownload(String containerName,
                            String blobName,
                            CallbackWithHeader<ResponseBody, BlobDownloadHeaders> callback) {
        storageBlobServiceClient.download(containerName,
            blobName,
            callback);
    }

    /**
     * Reads a range of bytes from a blob.
     *
     * <p>
     * This method will execute a raw HTTP GET in order to download a single blob to the destination.
     * It is **STRONGLY** recommended that you use the {@link StorageBlobAsyncClient#download(Context, String, String, File)}
     * or {@link StorageBlobAsyncClient#download(Context, String, String, Uri)} method instead - that method will
     * manage the transfer in the face of changing network conditions, and is able to transfer multiple
     * blocks in parallel.
     *
     * @param containerName         The container name.
     * @param blobName              The blob name.
     * @param snapshot              The snapshot parameter is an opaque DateTime value that, when present, specifies
     *                              the blob snapshot to retrieve. For more information on working with blob snapshots,
     *                              see &lt;a href="https://docs.microsoft.com/en-us/rest/api/storageservices/creating-a-snapshot-of-a-blob"&gt;Creating a Snapshot of a Blob.&lt;/a&gt;.
     * @param timeout               The timeout parameter is expressed in seconds. For more information, see
     *                              &lt;a href="https://docs.microsoft.com/en-us/rest/api/storageservices/setting-timeouts-for-blob-service-operations"&gt;Setting Timeouts for Blob Service Operations.&lt;/a&gt;.
     * @param range                 Return only the bytes of the blob in the specified range.
     * @param blobRequestConditions Object that contains values which will restrict the successful operation of a
     *                              variety of requests to the conditions present. These conditions are entirely optional.
     * @param getRangeContentMd5    When set to true and specified together with the Range, the service returns the
     *                              MD5 hash for the range, as long as the range is less than or equal to 4 MB in size.
     * @param getRangeContentCrc64  When set to true and specified together with the Range, the service returns the
     *                              CRC64 hash for the range, as long as the range is less than or equal to 4 MB in size.
     * @param cpkInfo               Additional parameters for the operation.
     * @param cancellationToken     The token to request cancellation.
     * @param callback              Callback that receives the response.
     */
    public void rawDownload(String containerName,
                            String blobName,
                            String snapshot,
                            Integer timeout,
                            BlobRange range,
                            BlobRequestConditions blobRequestConditions,
                            Boolean getRangeContentMd5,
                            Boolean getRangeContentCrc64,
                            CpkInfo cpkInfo,
                            CancellationToken cancellationToken,
                            CallbackWithHeader<ResponseBody, BlobDownloadHeaders> callback) {
        range = range == null ? new BlobRange(0) : range;
        blobRequestConditions = blobRequestConditions == null ? new BlobRequestConditions() : blobRequestConditions;

        storageBlobServiceClient.rawDownload(containerName,
            blobName,
            snapshot,
            timeout,
            range.toHeaderValue(),
            getRangeContentMd5,
            getRangeContentCrc64,
            blobRequestConditions,
            cpkInfo,
            cancellationToken,
            callback);
    }

    /**
     * Creates a new block to be committed as part of a blob.
     *
     * @param containerName The container name.
     * @param blobName      The blob name.
     * @param base64BlockId A valid Base64 string value that identifies the block. Prior to encoding, the string must
     *                      be less than or equal to 64 bytes in size. For a given blob, the length of the value specified
     *                      for the base64BlockId parameter must be the same size for each block.
     * @param blockContent  The block content in bytes.
     * @param contentMd5    The transactional MD5 for the body, to be validated by the service.
     * @param callback      Callback that receives the response.
     */
    public void stageBlock(String containerName,
                           String blobName,
                           String base64BlockId,
                           byte[] blockContent,
                           byte[] contentMd5,
                           CallbackWithHeader<Void, BlockBlobStageBlockHeaders> callback) {
        this.storageBlobServiceClient.stageBlock(containerName,
            blobName,
            base64BlockId,
            blockContent,
            contentMd5,
            callback);
    }

    /**
     * Creates a new block to be committed as part of a blob.
     *
     * @param containerName     The container name.
     * @param blobName          The blob name.
     * @param base64BlockId     A valid Base64 string value that identifies the block. Prior to encoding, the string must
     *                          be less than or equal to 64 bytes in size. For a given blob, the length of the value specified
     *                          for the base64BlockId parameter must be the same size for each block.
     * @param blockContent      The block content in bytes.
     * @param contentMd5        The transactional MD5 for the block content, to be validated by the service.
     * @param contentCrc64      Specify the transactional crc64 for the block content, to be validated by the service.
     * @param computeMd5        Whether or not the library should calculate the md5 and send it for the service to verify.
     * @param timeout           The timeout parameter is expressed in seconds. For more information,
     *                          see &lt;a href="https://docs.microsoft.com/en-us/rest/api/storageservices/fileservices/setting-timeouts-for-blob-service-operations"&gt;Setting Timeouts for Blob Service Operations.&lt;/a&gt;.
     * @param leaseId           If specified, the staging only succeeds if the resource's lease is active and matches this ID.
     * @param cpkInfo           Additional parameters for the operation.
     * @param cancellationToken The token to request cancellation.
     * @param callback          Callback that receives the response.
     */
    public void stageBlock(String containerName,
                           String blobName,
                           String base64BlockId,
                           byte[] blockContent,
                           byte[] contentMd5,
                           byte[] contentCrc64,
                           Boolean computeMd5,
                           Integer timeout,
                           String leaseId,
                           CpkInfo cpkInfo,
                           CancellationToken cancellationToken,
                           CallbackWithHeader<Void, BlockBlobStageBlockHeaders> callback) {
        this.storageBlobServiceClient.stageBlock(containerName,
            blobName,
            base64BlockId,
            blockContent,
            contentMd5,
            contentCrc64,
            computeMd5,
            timeout,
            leaseId,
            cpkInfo,
            cancellationToken,
            callback);
    }

    /**
     * The Commit Block List operation writes a blob by specifying the list of block IDs that make up the blob.
     * For a block to be written as part of a blob, the block must have been successfully written to the server in a prior
     * {@link StorageBlobAsyncClient#stageBlock(String, String, String, byte[], byte[], CallbackWithHeader)}. You can
     * call commit Block List to update a blob by uploading only those blocks that have changed, then committing the new
     * and existing blocks together. You can do this by specifying whether to commit a block from the committed block
     * list or from the uncommitted block list, or to commit the most recently uploaded version of the block,
     * whichever list it may belong to.
     *
     * @param containerName  The container name.
     * @param blobName       The blob name.
     * @param base64BlockIds The block IDs.
     * @param overwrite      Indicate whether to overwrite the block list if already exists.
     * @param callback       Callback that receives the response.
     */
    public void commitBlockList(String containerName,
                                String blobName,
                                List<String> base64BlockIds,
                                boolean overwrite,
                                CallbackWithHeader<BlockBlobItem, BlockBlobCommitBlockListHeaders> callback) {
        this.storageBlobServiceClient.commitBlockList(containerName,
            blobName,
            base64BlockIds,
            overwrite,
            callback);
    }

    /**
     * The Commit Block List operation writes a blob by specifying the list of block IDs that make up the blob.
     * For a block to be written as part of a blob, the block must have been successfully written to the server in a prior
     * {@link StorageBlobAsyncClient#stageBlock(String, String, String, byte[], byte[], CallbackWithHeader)}  operation. You can call
     * commit Block List to update a blob by uploading only those blocks that have changed, then committing the new and existing
     * blocks together. You can do this by specifying whether to commit a block from the committed block list or from
     * the uncommitted block list, or to commit the most recently uploaded version of the block, whichever list it may belong to.
     *
     * @param containerName     The container name.
     * @param blobName          The blob name.
     * @param base64BlockIds    The block IDs.
     * @param contentMD5        Specify the transactional md5 for the body, to be validated by the service.
     * @param contentCrc64      Specify the transactional crc64 for the body, to be validated by the service.
     * @param timeout           The timeout parameter is expressed in seconds. For more information,
     *                          see &lt;a href="https://docs.microsoft.com/en-us/rest/api/storageservices/fileservices/setting-timeouts-for-blob-service-operations"&gt;Setting Timeouts for Blob Service Operations.&lt;/a&gt;.
     * @param blobHttpHeaders   Additional Http headers for this operation.
     * @param metadata          Specifies a user-defined name-value pair associated with the blob.
     * @param requestConditions {@link BlobRequestConditions}.
     * @param cpkInfo           Additional parameters for the operation.
     * @param tier              Indicates the tier to be set on the blob.
     * @param cancellationToken The token to request cancellation.
     * @param callback          Callback that receives the response.
     */
    public void commitBlockList(String containerName,
                                String blobName,
                                List<String> base64BlockIds,
                                byte[] contentMD5,
                                byte[] contentCrc64,
                                Integer timeout,
                                BlobHttpHeaders blobHttpHeaders,
                                Map<String, String> metadata,
                                BlobRequestConditions requestConditions,
                                CpkInfo cpkInfo,
                                AccessTier tier,
                                CancellationToken cancellationToken,
                                CallbackWithHeader<BlockBlobItem, BlockBlobCommitBlockListHeaders> callback) {
        this.storageBlobServiceClient.commitBlockList(containerName,
            blobName,
            base64BlockIds,
            contentMD5,
            contentCrc64,
            timeout,
            blobHttpHeaders,
            metadata,
            requestConditions,
            cpkInfo,
            tier,
            cancellationToken,
            callback);
    }

    /**
     * Deletes the specified blob or snapshot. Note that deleting a blob also deletes all its snapshots.
     *
     * @param containerName The container name.
     * @param blobName      The blob name.
     * @param callback      Callback that receives the response.
     */
    public void deleteBlob(String containerName,
                           String blobName,
                           CallbackWithHeader<Void, BlobDeleteHeaders> callback) {
        storageBlobServiceClient.deleteBlob(containerName,
            blobName,
            callback);
    }

    /**
     * Deletes the specified blob or snapshot. Note that deleting a blob also deletes all its snapshots.
     * <p>
     * If the storage account's soft delete feature is disabled then, when a blob is deleted, it is permanently
     * removed from the storage account. If the storage account's soft delete feature is enabled, then, when a blob
     * is deleted, it is marked for deletion and becomes inaccessible immediately. However, the blob service retains
     * the blob or snapshot for the number of days specified by the DeleteRetentionPolicy section of
     * &lt;a href="https://docs.microsoft.com/en-us/rest/api/storageservices/set-blob-service-properties"&gt; Storage service properties.&lt;/a&gt;.
     * After the specified number of days has passed, the blob's data is permanently removed from the storage account.
     * Note that you continue to be charged for the soft-deleted blob's storage until it is permanently removed. Use
     * the List Blobs API and specify the "include=deleted" query parameter to discover which blobs and snapshots
     * have been soft deleted. You can then use the Undelete Blob API to restore a soft-deleted blob. All other
     * operations on a soft-deleted blob or snapshot causes the service to return an HTTP status code of 404
     * (ResourceNotFound). If the storage account's automatic snapshot feature is enabled, then, when a blob is
     * deleted, an automatic snapshot is created. The blob becomes inaccessible immediately. All other operations on
     * the blob causes the service to return an HTTP status code of 404 (ResourceNotFound). You can access automatic
     * snapshot using snapshot timestamp or version ID. You can restore the blob by calling Put or Copy Blob API with
     * automatic snapshot as source. Deleting automatic snapshot requires shared key or special SAS/RBAC permissions.
     *
     * @param containerName     The container name.
     * @param blobName          The blob name.
     * @param snapshot          The snapshot parameter is an opaque DateTime value that, when present, specifies the
     *                          blob snapshot to retrieve. For more information on working with blob snapshots, see &lt;a href="https://docs.microsoft.com/en-us/rest/api/storageservices/fileservices/creating-a-snapshot-of-a-blob"&gt;Creating a Snapshot of a Blob.&lt;/a&gt;.
     * @param timeout           The timeout parameter is expressed in seconds. For more information, see
     *                          &lt;a href="https://docs.microsoft.com/en-us/rest/api/storageservices/fileservices/setting-timeouts-for-blob-service-operations"&gt;Setting Timeouts for Blob Service Operations.&lt;/a&gt;.
     * @param deleteSnapshots   Required if the blob has associated snapshots. Specify one of the following two
     *                          options: include: Delete the base blob and all of its snapshots. only: Delete only the blob's snapshots and not the blob itself. Possible values include: 'include', 'only'.
     * @param requestConditions {@link BlobRequestConditions}
     * @param cancellationToken The token to request cancellation.
     * @param callback          Callback that receives the response.
     */
    public void deleteBlob(String containerName,
                           String blobName,
                           String snapshot,
                           Integer timeout,
                           DeleteSnapshotsOptionType deleteSnapshots,
                           BlobRequestConditions requestConditions,
                           CancellationToken cancellationToken,
                           CallbackWithHeader<Void, BlobDeleteHeaders> callback) {
        storageBlobServiceClient.deleteBlob(containerName,
            blobName,
            snapshot,
            timeout,
            deleteSnapshots,
            requestConditions,
            cancellationToken,
            callback);
    }

    /**
     * Deletes a container.
     *
     * @param containerName The container name.
     * @param callback      Callback that receives the response.
     */
    public void deleteContainer(String containerName,
                                CallbackWithHeader<Void, ContainerDeleteHeaders> callback) {
        storageBlobServiceClient.deleteContainer(containerName,
            callback);
    }

    /**
     * Deletes a container
     *
     * @param containerName     The container name.
     * @param timeout           The timeout parameter is expressed in seconds. For more information, see
     *                          &lt;a href="https://docs.microsoft.com/en-us/rest/api/storageservices/fileservices/setting-timeouts-for-blob-service-operations"&gt;Setting Timeouts for Blob Service Operations.&lt;/a&gt;.
     * @param requestConditions {@link BlobRequestConditions}
     * @param cancellationToken The token to request cancellation.
     * @param callback          Callback that receives the response.
     */
    public void deleteContainer(String containerName,
                                Integer timeout,
                                BlobRequestConditions requestConditions,
                                CancellationToken cancellationToken,
                                CallbackWithHeader<Void, ContainerDeleteHeaders> callback) {
        storageBlobServiceClient.deleteContainer(containerName,
            timeout,
            requestConditions,
            cancellationToken,
            callback);
    }

    /**
     * Gets tags associated with a blob.
     *
     * @param containerName The container name.
     * @param blobName      The blob name.
     * @param callback      Callback that receives the response.
     */
    public void getBlobTags(String containerName,
                            String blobName,
                            CallbackWithHeader<Map<String, String>, BlobGetTagsHeaders> callback) {
        this.storageBlobServiceClient.getTags(containerName,
            blobName,
            new CallbackWithHeader<BlobTags, BlobGetTagsHeaders>() {
                @Override
                public void onSuccess(BlobTags result, BlobGetTagsHeaders header, Response response) {
                    callback.onSuccess(ModelHelper.populateBlobTags(result), header, response);
                }

                @Override
                public void onFailure(Throwable throwable, Response response) {
                    callback.onFailure(throwable, response);
                }
            });
    }

    /**
     * Gets tags associated with a blob.
     *
     * @param containerName     The container name.
     * @param blobName          The blob name.
     * @param snapshot          The snapshot parameter is an opaque DateTime value that, when present, specifies the
     *                          blob snapshot to retrieve. For more information on working with blob snapshots, see &lt;a href="https://docs.microsoft.com/en-us/rest/api/storageservices/fileservices/creating-a-snapshot-of-a-blob"&gt;Creating a Snapshot of a Blob.&lt;/a&gt;.
     * @param timeout           The timeout parameter is expressed in seconds. For more information, see
     *                          &lt;a href="https://docs.microsoft.com/en-us/rest/api/storageservices/fileservices/setting-timeouts-for-blob-service-operations"&gt;Setting Timeouts for Blob Service Operations.&lt;/a&gt;.
     * @param cancellationToken The token to request cancellation.
     * @param callback          The callback that receives the response.
     */
    public void getBlobTags(String containerName,
                            String blobName,
                            String snapshot,
                            Integer timeout,
                            String tagsConditions,
                            CancellationToken cancellationToken,
                            CallbackWithHeader<Map<String, String>, BlobGetTagsHeaders> callback) {
        this.storageBlobServiceClient.getTags(containerName,
            blobName,
            snapshot,
            null,
            timeout,
            tagsConditions,
            cancellationToken,
            new CallbackWithHeader<BlobTags, BlobGetTagsHeaders>() {
                @Override
                public void onSuccess(BlobTags result, BlobGetTagsHeaders header, Response response) {
                    callback.onSuccess(ModelHelper.populateBlobTags(result), header, response);
                }

                @Override
                public void onFailure(Throwable throwable, Response response) {
                    callback.onFailure(throwable, response);
                }
            });
    }

    /**
     * Changes a blob's tags. The specified tags in this method will replace existing tags. If old values
     * must be preserved, they must be downloaded and included in the call to this method.
     *
     * @param containerName The container name.
     * @param blobName      The blob name.
     * @param tags          Tags to associate with the blob.
     * @param callback      Callback that receives the response.
     */
    public void setBlobTags(String containerName,
                            String blobName,
                            Map<String, String> tags,
                            CallbackWithHeader<Void, BlobSetTagsHeaders> callback) {
        storageBlobServiceClient.setBlobTags(containerName, blobName, tags, callback);
    }

    /**
     * Changes a blob's tags. The specified tags in this method will replace existing tags. If old values
     * must be preserved, they must be downloaded and included in the call to this method.
     *
     * @param containerName     The container name.
     * @param blobName          The blob name.
     * @param timeout           The timeout parameter is expressed in seconds. For more information, see
     *                          &lt;a href="https://docs.microsoft.com/en-us/rest/api/storageservices/fileservices/setting-timeouts-for-blob-service-operations"&gt;Setting Timeouts for Blob Service Operations.&lt;/a&gt;.
     * @param tagsConditions    Specifies a SQL query to apply to the blob's tags.
     * @param tags              Tags to associate with the blob.
     * @param cancellationToken The token to request cancellation.
     * @param callback          Callback that receives the response.
     */
    public void setBlobTags(String containerName,
                            String blobName,
                            Integer timeout,
                            String tagsConditions, /*TODO: Should this be BlobRequestConditions? */
                            Map<String, String> tags,
                            CancellationToken cancellationToken,
                            CallbackWithHeader<Void, BlobSetTagsHeaders> callback) {
        storageBlobServiceClient.setBlobTags(containerName,
            blobName,
            timeout,
            null, // TODO: Add back with versioning support
            tagsConditions,
            tags,
            cancellationToken,
            callback);
    }

    /**
     * Builder for {@link StorageBlobAsyncClient}.
     * A builder to configure and build a {@link StorageBlobAsyncClient}.
     */
    public static class Builder {
        private final String storageBlobClientId;
        private BlobServiceVersion serviceVersion;
        private final ServiceClient.Builder serviceClientBuilder;
        private final Constraints.Builder transferConstraintsBuilder;
        private static final StorageBlobClientMap STORAGE_BLOB_CLIENTS;

        static {
            STORAGE_BLOB_CLIENTS = StorageBlobClientMap.getInstance();
        }

        /**
         * Creates a {@link Builder}.
         *
         * @param storageBlobClientId The unique ID for the {@link StorageBlobAsyncClient} this builder builds. This
         *                            identifier is used to associate this {@link StorageBlobAsyncClient} with the upload and
         *                            download transfers it initiates. When a transfer is reloaded from disk (e.g.
         *                            after an application crash), it can only be resumed once a client with the same
         *                            storageBlobClientId has been initialized.
         */
        public Builder(String storageBlobClientId) {
            this(storageBlobClientId, new ServiceClient.Builder());

        }

        /**
         * Creates a {@link Builder} that uses the provided {@link com.azure.android.core.http.ServiceClient.Builder}
         * to build a {@link ServiceClient} for the {@link StorageBlobAsyncClient}.
         *
         * <p>
         * The builder produced {@link ServiceClient} is used by the {@link StorageBlobAsyncClient} to make Rest API calls.
         * Multiple {@link StorageBlobAsyncClient} instances can share the same {@link ServiceClient} instance, for e.g.
         * when a new {@link StorageBlobAsyncClient} is created from an existing {@link StorageBlobAsyncClient} through
         * {@link StorageBlobAsyncClient#newBuilder(String)} ()} then both shares the same {@link ServiceClient}.
         * The {@link ServiceClient} composes HttpClient, HTTP settings (such as connection timeout, interceptors)
         * and Retrofit for Rest calls.
         *
         * @param storageBlobClientId  The unique ID for the {@link StorageBlobAsyncClient} this builder builds.
         * @param serviceClientBuilder The {@link com.azure.android.core.http.ServiceClient.Builder}.
         */
        public Builder(String storageBlobClientId, ServiceClient.Builder serviceClientBuilder) {
            this(storageBlobClientId, serviceClientBuilder, new Constraints.Builder());
            addStandardInterceptors();
            this.transferConstraintsBuilder
                .setRequiredNetworkType(NetworkType.CONNECTED);
        }

        private Builder(String storageBlobClientId,
                        ServiceClient.Builder serviceClientBuilder,
                        Constraints.Builder transferConstraintsBuilder) {
            if (CoreUtil.isNullOrEmpty(storageBlobClientId)) {
                throw new IllegalArgumentException("'storageBlobClientId' cannot be null or empty.");
            }
            if (Builder.STORAGE_BLOB_CLIENTS.contains(storageBlobClientId)) {
                throw new IllegalArgumentException("A StorageBlobClient with id '" + storageBlobClientId + "' already exists.");
            }
            this.storageBlobClientId = storageBlobClientId;
            this.serviceClientBuilder
                = Objects.requireNonNull(serviceClientBuilder, "serviceClientBuilder cannot be null.");
            this.transferConstraintsBuilder
                = Objects.requireNonNull(transferConstraintsBuilder, "transferConstraintsBuilder cannot be null.");

            addStandardInterceptors();
        }

        private void addStandardInterceptors() {
            this.serviceClientBuilder
                .addInterceptor(new RequestIdInterceptor())
                .addInterceptor(new AddDateInterceptor())
                .addInterceptor(new MetadataInterceptor())
                .addInterceptor(new NormalizeEtagInterceptor());
            //.addInterceptor(new ResponseHeadersValidationInterceptor()); // TODO: Uncomment when we add a request id interceptor
        }

        /**
         * Sets the base URL for the {@link StorageBlobAsyncClient}.
         *
         * @param blobServiceUrl The blob service base URL.
         * @return An updated {@link Builder} with the provided blob service URL set.
         */
        public Builder setBlobServiceUrl(String blobServiceUrl) {
            Objects.requireNonNull(blobServiceUrl, "blobServiceUrl cannot be null.");
            this.serviceClientBuilder.setBaseUrl(blobServiceUrl);
            return this;
        }

        /**
         * Sets the service version for the {@link StorageBlobAsyncClient}.
         *
         * @param serviceVersion {@link BlobServiceVersion}
         * @return An updated {@link Builder} with the provided blob service version set.
         */
        public Builder setServiceVersion(BlobServiceVersion serviceVersion) {
            this.serviceVersion = serviceVersion;
            return this;
        }

        /**
         * Sets an interceptor used to authenticate the blob service request.
         *
         * @param credentialInterceptor The credential interceptor.
         * @return An updated {@link Builder} with the provided credentials interceptor set.
         */
        public Builder setCredentialInterceptor(Interceptor credentialInterceptor) {
            this.serviceClientBuilder.setCredentialsInterceptor(credentialInterceptor);
            return this;
        }

        /**
         * Sets whether device should be charging for running the transfers. The default value is {@code false}.
         *
         * @param requiresCharging {@code true} if the device must be charging for the transfer to run.
         * @return An updated {@link Builder} with the provided charging requirement set.
         */
        public Builder setTransferRequiresCharging(boolean requiresCharging) {
            this.transferConstraintsBuilder.setRequiresCharging(requiresCharging);
            return this;
        }

        /**
         * Sets whether device should be idle for running the transfers. The default value is {@code false}.
         *
         * @param requiresDeviceIdle {@code true} if the device must be idle for transfers to run.
         * @return An updated {@link Builder} with the provided setting set.
         */
        @RequiresApi(23)
        public Builder setTransferRequiresDeviceIdle(boolean requiresDeviceIdle) {
            if (Build.VERSION.SDK_INT >= 23) {
                this.transferConstraintsBuilder.setRequiresDeviceIdle(requiresDeviceIdle);
            }
            return this;
        }

        /**
         * Sets the particular {@link NetworkType} the device should be in for running the transfers.
         * <p>
         * The default network type that {@link TransferClient} uses is {@link NetworkType#CONNECTED}.
         *
         * @param networkType The type of network required for transfers to run.
         * @return An updated {@link Builder} with the provided network type set.
         */
        public Builder setTransferRequiredNetworkType(@NonNull NetworkType networkType) {
            Objects.requireNonNull(networkType, "'networkType' cannot be null.");
            if (networkType == NetworkType.NOT_REQUIRED) {
                throw new IllegalArgumentException(
                    "The network type NOT_REQUIRED is not a valid transfer configuration.");
            }
            this.transferConstraintsBuilder.setRequiredNetworkType(networkType);
            return this;
        }

        /**
         * Sets whether device battery should be at an acceptable level for running the transfers. The default value
         * is {@code false}.
         *
         * @param requiresBatteryNotLow {@code true} if the battery should be at an acceptable level for the
         *                              transfers to run.
         * @return An updated {@link Builder} with the provided battery requirement set.
         */
        public Builder setTransferRequiresBatteryNotLow(boolean requiresBatteryNotLow) {
            this.transferConstraintsBuilder.setRequiresBatteryNotLow(requiresBatteryNotLow);
            return this;
        }

        /**
         * Sets whether the device's available storage should be at an acceptable level for running
         * the transfers. The default value is {@code false}.
         *
         * @param requiresStorageNotLow {@code true} if the available storage should not be below a
         *                              a critical threshold for the transfer to run.
         * @return An updated {@link Builder} with the provided storage requirement set.
         */
        public Builder setTransferRequiresStorageNotLow(boolean requiresStorageNotLow) {
            this.transferConstraintsBuilder.setRequiresStorageNotLow(requiresStorageNotLow);
            return this;
        }

        /**
         * Builds a {@link StorageBlobAsyncClient} based on this {@link Builder}'s configuration.
         *
         * @return A {@link StorageBlobAsyncClient}.
         */
        public StorageBlobAsyncClient build() {
            Constraints transferConstraints = this.transferConstraintsBuilder.build();
            NetworkType networkType = transferConstraints.getRequiredNetworkType();
            if (networkType == null || networkType == NetworkType.NOT_REQUIRED) {
                throw new IllegalArgumentException(
                    "The null or NOT_REQUIRED NetworkType is not a valid transfer configuration.");
            }
            BlobServiceVersion version = this.serviceVersion == null ? BlobServiceVersion.getLatest()
                : this.serviceVersion;
            StorageBlobAsyncClient client = new StorageBlobAsyncClient(this.storageBlobClientId,
                this.serviceClientBuilder.build(), version.getVersion(),
                transferConstraints);
            Builder.STORAGE_BLOB_CLIENTS.add(storageBlobClientId, client);
            return client;
        }

        private Builder(String storageBlobClientId, final StorageBlobAsyncClient storageBlobAsyncClient) {
            this(storageBlobClientId,
                storageBlobAsyncClient.serviceClient.newBuilder(),
                newBuilder(storageBlobAsyncClient.transferConstraints));
        }

        private static androidx.work.Constraints.Builder newBuilder(androidx.work.Constraints constraints) {
            Constraints.Builder builder = new Constraints.Builder();
            builder.setRequiresCharging(constraints.requiresCharging());
            if (Build.VERSION.SDK_INT >= 23) {
                builder.setRequiresDeviceIdle(constraints.requiresDeviceIdle());
            }
            builder.setRequiredNetworkType(constraints.getRequiredNetworkType());
            builder.setRequiresBatteryNotLow(constraints.requiresBatteryNotLow());
            builder.setRequiresStorageNotLow(constraints.requiresStorageNotLow());
            return builder;
        }
    }
}
