// 
// Copyright (c) Microsoft and contributors.  All rights reserved.
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//   http://www.apache.org/licenses/LICENSE-2.0
// 
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// 
// See the License for the specific language governing permissions and
// limitations under the License.
// 

// Warning: This code was generated by a tool.
// 
// Changes to this file may cause incorrect behavior and will be lost if the
// code is regenerated.

package com.microsoft.windowsazure.management.compute.models;

import com.microsoft.windowsazure.management.OperationResponse;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;

/**
* The Get Hosted Service operation response.
*/
public class HostedServiceListResponse extends OperationResponse implements Iterable<HostedServiceListResponse.HostedService>
{
    private ArrayList<HostedServiceListResponse.HostedService> hostedServices;
    
    /**
    * The hosted services associated with your subscription.
    */
    public ArrayList<HostedServiceListResponse.HostedService> getHostedServices() { return this.hostedServices; }
    
    /**
    * The hosted services associated with your subscription.
    */
    public void setHostedServices(ArrayList<HostedServiceListResponse.HostedService> hostedServices) { this.hostedServices = hostedServices; }
    
    /**
    * Initializes a new instance of the HostedServiceListResponse class.
    *
    */
    public HostedServiceListResponse()
    {
        this.hostedServices = new ArrayList<HostedServiceListResponse.HostedService>();
    }
    
    /**
    * Gets the sequence of HostedServices.
    *
    */
    public Iterator<HostedServiceListResponse.HostedService> iterator()
    {
        return this.getHostedServices().iterator();
    }
    
    /**
    * A hosted service associated with your subscription.
    */
    public static class HostedService
    {
        private HostedServiceProperties properties;
        
        /**
        * The properties that are assigned to the cloud service.
        */
        public HostedServiceProperties getProperties() { return this.properties; }
        
        /**
        * The properties that are assigned to the cloud service.
        */
        public void setProperties(HostedServiceProperties properties) { this.properties = properties; }
        
        private String serviceName;
        
        /**
        * The name of the cloud service. This name is the DNS prefix name and
        * can be used to access the cloud service. For example, if the cloud
        * service name is MyService you could access the cloud service by
        * calling: http://MyService.cloudapp.net
        */
        public String getServiceName() { return this.serviceName; }
        
        /**
        * The name of the cloud service. This name is the DNS prefix name and
        * can be used to access the cloud service. For example, if the cloud
        * service name is MyService you could access the cloud service by
        * calling: http://MyService.cloudapp.net
        */
        public void setServiceName(String serviceName) { this.serviceName = serviceName; }
        
        private URI uri;
        
        /**
        * The Service Management API request URI used to perform Get Hosted
        * Service Properties requests against the cloud service.
        */
        public URI getUri() { return this.uri; }
        
        /**
        * The Service Management API request URI used to perform Get Hosted
        * Service Properties requests against the cloud service.
        */
        public void setUri(URI uri) { this.uri = uri; }
        
        /**
        * Initializes a new instance of the HostedService class.
        *
        */
        public HostedService()
        {
        }
    }
}