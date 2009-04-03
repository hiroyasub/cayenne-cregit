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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|map
operator|.
name|DataMap
import|;
end_import

begin_comment
comment|/**  * Interface that defines callback API used by ConfigLoader to process loaded  * configuration. Main responsibility of ConfigLoaderDelegate is to create  * objects, while ConfigLoader is mainly concerned with XML parsing.   *   */
end_comment

begin_interface
specifier|public
interface|interface
name|ConfigLoaderDelegate
block|{
comment|/**      * Callback methods invoked in the beginning of the configuration      * processing.      */
specifier|public
name|void
name|startedLoading
parameter_list|()
function_decl|;
comment|/**      * Callback methods invoked at the end of the configuration processing.      */
specifier|public
name|void
name|finishedLoading
parameter_list|()
function_decl|;
comment|/**      * Callback method invoked when a project version is read.      * @since 1.1      */
specifier|public
name|void
name|shouldLoadProjectVersion
parameter_list|(
name|String
name|version
parameter_list|)
function_decl|;
comment|/**      * Callback method invoked when a domain is encountered in the configuration      * file.      * @param name domain name.      */
specifier|public
name|void
name|shouldLoadDataDomain
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Callback method invoked when a DataView reference is encountered in the configuration      * file.      *       * @since 1.1      */
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
function_decl|;
comment|/**      * @since 1.1      */
specifier|public
name|void
name|shouldLoadDataMaps
parameter_list|(
name|String
name|domainName
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|DataMap
argument_list|>
name|locations
parameter_list|)
function_decl|;
comment|/**      * @since 1.1      */
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
function_decl|;
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
function_decl|;
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
function_decl|;
comment|/**      * Gives delegate an opportunity to process the error.      *       * @param th      * @return boolean indicating whether ConfigLoader should proceed with      * further processing. Ultimately it is up to the ConfigLoader to make this      * decision.      */
specifier|public
name|boolean
name|loadError
parameter_list|(
name|Throwable
name|th
parameter_list|)
function_decl|;
comment|/**      * @return status object indicating the state of the configuration loading.      */
specifier|public
name|ConfigStatus
name|getStatus
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

