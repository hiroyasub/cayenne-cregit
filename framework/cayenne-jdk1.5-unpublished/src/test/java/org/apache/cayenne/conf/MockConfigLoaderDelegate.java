begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|conf
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_class
specifier|public
class|class
name|MockConfigLoaderDelegate
implements|implements
name|ConfigLoaderDelegate
block|{
specifier|public
name|void
name|startedLoading
parameter_list|()
block|{
block|}
specifier|public
name|void
name|finishedLoading
parameter_list|()
block|{
block|}
specifier|public
name|void
name|shouldLoadProjectVersion
parameter_list|(
name|String
name|version
parameter_list|)
block|{
block|}
specifier|public
name|void
name|shouldLoadDataDomain
parameter_list|(
name|String
name|name
parameter_list|)
block|{
block|}
specifier|public
name|void
name|shouldRegisterDataView
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|location
parameter_list|)
block|{
block|}
specifier|public
name|void
name|shouldLoadDataMaps
parameter_list|(
name|String
name|domainName
parameter_list|,
name|Map
name|locations
parameter_list|)
block|{
block|}
specifier|public
name|void
name|shouldLoadDataDomainProperties
parameter_list|(
name|String
name|domainName
parameter_list|,
name|Map
name|properties
parameter_list|)
block|{
block|}
specifier|public
name|void
name|shouldLoadDataNode
parameter_list|(
name|String
name|domainName
parameter_list|,
name|String
name|nodeName
parameter_list|,
name|String
name|dataSource
parameter_list|,
name|String
name|adapter
parameter_list|,
name|String
name|factory
parameter_list|,
name|String
name|schemaUpdateStrategy
parameter_list|)
block|{
block|}
specifier|public
name|void
name|shouldLinkDataMap
parameter_list|(
name|String
name|domainName
parameter_list|,
name|String
name|nodeName
parameter_list|,
name|String
name|mapName
parameter_list|)
block|{
block|}
specifier|public
name|boolean
name|loadError
parameter_list|(
name|Throwable
name|th
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|ConfigStatus
name|getStatus
parameter_list|()
block|{
return|return
operator|new
name|ConfigStatus
argument_list|()
return|;
block|}
block|}
end_class

end_unit

