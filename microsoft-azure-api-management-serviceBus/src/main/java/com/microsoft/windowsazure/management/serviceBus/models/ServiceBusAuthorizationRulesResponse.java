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

package com.microsoft.windowsazure.management.servicebus.models;

import com.microsoft.windowsazure.management.OperationResponse;
import java.util.ArrayList;
import java.util.Iterator;

/**
* A response to a request for a list of authorization rules.
*/
public class ServiceBusAuthorizationRulesResponse extends OperationResponse implements Iterable<ServiceBusSharedAccessAuthorizationRule>
{
    private ArrayList<ServiceBusSharedAccessAuthorizationRule> authorizationRules;
    
    /**
    * The list of authorization rules.
    */
    public ArrayList<ServiceBusSharedAccessAuthorizationRule> getAuthorizationRules() { return this.authorizationRules; }
    
    /**
    * The list of authorization rules.
    */
    public void setAuthorizationRules(ArrayList<ServiceBusSharedAccessAuthorizationRule> authorizationRules) { this.authorizationRules = authorizationRules; }
    
    /**
    * Initializes a new instance of the ServiceBusAuthorizationRulesResponse
    * class.
    *
    */
    public ServiceBusAuthorizationRulesResponse()
    {
        this.authorizationRules = new ArrayList<ServiceBusSharedAccessAuthorizationRule>();
    }
    
    /**
    * Gets the sequence of AuthorizationRules.
    *
    */
    public Iterator<ServiceBusSharedAccessAuthorizationRule> iterator()
    {
        return this.getAuthorizationRules().iterator();
    }
}