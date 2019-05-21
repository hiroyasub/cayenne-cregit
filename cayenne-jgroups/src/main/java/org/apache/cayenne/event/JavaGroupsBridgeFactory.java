begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    https://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|event
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * Factory to create JavaGroupsBridge instances. If JavaGroups library is not installed  * this factory will return a noop EventBridge as a failover mechanism.  *<p/>  * For further information about JavaGroups consult the<a href="http://www.jgroups.org/">documentation</a>.  *  * @since 1.1  */
end_comment

begin_class
specifier|public
class|class
name|JavaGroupsBridgeFactory
implements|implements
name|EventBridgeFactory
block|{
comment|/**      * Creates a JavaGroupsBridge instance. Since JavaGroups is not shipped with Cayenne      * and should be installed separately, a common misconfiguration problem may be the      * absence of JavaGroups jar file. This factory returns a dummy noop EventBridge, if      * this is the case. This would allow the application to continue to run, but without      * remote notifications.      */
specifier|public
name|EventBridge
name|createEventBridge
parameter_list|(
name|Collection
argument_list|<
name|EventSubject
argument_list|>
name|localSubjects
parameter_list|,
name|String
name|externalSubject
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|properties
parameter_list|)
block|{
return|return
operator|new
name|JavaGroupsBridge
argument_list|(
name|localSubjects
argument_list|,
name|externalSubject
argument_list|,
name|properties
argument_list|)
return|;
block|}
block|}
end_class

end_unit

