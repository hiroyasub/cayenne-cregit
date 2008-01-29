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
name|project
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|lang
operator|.
name|Validate
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
name|conf
operator|.
name|ConfigLoader
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
name|conf
operator|.
name|ConfigLoaderDelegate
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
name|conf
operator|.
name|ConfigSaverDelegate
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
name|conf
operator|.
name|ConfigStatus
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
name|conf
operator|.
name|DriverDataSourceFactory
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
name|conf
operator|.
name|JNDIDataSourceFactory
import|;
end_import

begin_comment
comment|/**  * PartialProject is a "lightweight" project implementation. It can work with  * projects even when some of the resources are missing. It never instantiates  * Cayenne stack objects, using other, lightweight, data structures instead.  *   * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|PartialProject
extends|extends
name|Project
block|{
specifier|protected
name|String
name|projectVersion
decl_stmt|;
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|DomainMetaData
argument_list|>
name|domains
decl_stmt|;
specifier|protected
name|ConfigLoaderDelegate
name|loadDelegate
decl_stmt|;
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|dataViewLocations
decl_stmt|;
comment|/**      * Constructor PartialProjectHandler.      * @param projectFile      */
specifier|public
name|PartialProject
parameter_list|(
name|File
name|projectFile
parameter_list|)
block|{
name|super
argument_list|(
name|projectFile
argument_list|)
expr_stmt|;
block|}
comment|/**      * @since 1.1      */
annotation|@
name|Override
specifier|public
name|void
name|upgrade
parameter_list|()
throws|throws
name|ProjectException
block|{
comment|// upgrades not supported in this type of project
throw|throw
operator|new
name|ProjectException
argument_list|(
literal|"'PartialProject' does not support upgrades."
argument_list|)
throw|;
block|}
comment|/**      * Loads internal project and rewrites its nodes according to the list of      * DataNodeConfigInfo objects. Only main project file gets updated, the rest      * are assumed to be in place.      */
specifier|public
name|void
name|updateNodes
parameter_list|(
name|List
argument_list|<
name|?
extends|extends
name|DataNodeConfigInfo
argument_list|>
name|list
parameter_list|)
throws|throws
name|ProjectException
block|{
name|Iterator
argument_list|<
name|?
extends|extends
name|DataNodeConfigInfo
argument_list|>
name|it
init|=
name|list
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|DataNodeConfigInfo
name|nodeConfig
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|domainName
init|=
name|nodeConfig
operator|.
name|getDomain
argument_list|()
decl_stmt|;
if|if
condition|(
name|domainName
operator|==
literal|null
operator|&&
name|domains
operator|.
name|size
argument_list|()
operator|!=
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Node must have domain set explicitly if there is no default domain."
argument_list|)
throw|;
block|}
if|if
condition|(
name|domainName
operator|==
literal|null
condition|)
block|{
name|domainName
operator|=
operator|(
operator|(
name|DomainMetaData
operator|)
name|domains
operator|.
name|values
argument_list|()
operator|.
name|toArray
argument_list|()
index|[
literal|0
index|]
operator|)
operator|.
name|name
expr_stmt|;
block|}
name|NodeMetaData
name|node
init|=
name|findNode
argument_list|(
name|domainName
argument_list|,
name|nodeConfig
operator|.
name|getName
argument_list|()
argument_list|,
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|node
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|nodeConfig
operator|.
name|getAdapter
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|node
operator|.
name|adapter
operator|=
name|nodeConfig
operator|.
name|getAdapter
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|nodeConfig
operator|.
name|getDataSource
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|node
operator|.
name|dataSource
operator|=
name|nodeConfig
operator|.
name|getDataSource
argument_list|()
expr_stmt|;
name|node
operator|.
name|factory
operator|=
name|JNDIDataSourceFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
if|else if
condition|(
name|nodeConfig
operator|.
name|getDriverFile
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|node
operator|.
name|dataSource
operator|=
name|node
operator|.
name|name
operator|+
name|DataNodeFile
operator|.
name|LOCATION_SUFFIX
expr_stmt|;
name|node
operator|.
name|factory
operator|=
name|DriverDataSourceFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
specifier|protected
name|void
name|prepareSave
parameter_list|(
name|List
name|filesToSave
parameter_list|,
name|List
name|wrappedObjects
parameter_list|)
throws|throws
name|ProjectException
block|{
name|filesToSave
operator|.
name|addAll
argument_list|(
name|files
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|postInitialize
parameter_list|(
name|File
name|projectFile
parameter_list|)
block|{
name|loadDelegate
operator|=
operator|new
name|LoadDelegate
argument_list|()
expr_stmt|;
name|domains
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|DomainMetaData
argument_list|>
argument_list|()
expr_stmt|;
try|try
block|{
name|FileInputStream
name|in
init|=
operator|new
name|FileInputStream
argument_list|(
name|projectFile
argument_list|)
decl_stmt|;
try|try
block|{
operator|new
name|ConfigLoader
argument_list|(
name|loadDelegate
argument_list|)
operator|.
name|loadDomains
argument_list|(
name|in
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|ProjectException
argument_list|(
literal|"Error creating PartialProject."
argument_list|,
name|ex
argument_list|)
throw|;
block|}
finally|finally
block|{
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|ioex
parameter_list|)
block|{
throw|throw
operator|new
name|ProjectException
argument_list|(
literal|"Error creating PartialProject."
argument_list|,
name|ioex
argument_list|)
throw|;
block|}
name|super
operator|.
name|postInitialize
argument_list|(
name|projectFile
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|List
name|getChildren
parameter_list|()
block|{
return|return
operator|new
name|ArrayList
argument_list|<
name|DomainMetaData
argument_list|>
argument_list|(
name|domains
operator|.
name|values
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|checkForUpgrades
parameter_list|()
block|{
comment|// do nothing...
block|}
comment|/**      * @see org.apache.cayenne.project.Project#buildFileList()      */
annotation|@
name|Override
specifier|public
name|List
name|buildFileList
parameter_list|()
block|{
name|List
argument_list|<
name|ProjectFile
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|ProjectFile
argument_list|>
argument_list|()
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
name|projectFileForObject
argument_list|(
name|this
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|list
return|;
block|}
comment|/**      * @see org.apache.cayenne.project.Project#getLoadStatus()      */
annotation|@
name|Override
specifier|public
name|ConfigStatus
name|getLoadStatus
parameter_list|()
block|{
return|return
name|loadDelegate
operator|.
name|getStatus
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|ProjectFile
name|projectFileForObject
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
name|obj
operator|!=
name|this
condition|)
block|{
return|return
literal|null
return|;
block|}
name|ApplicationProjectFile
name|projectFile
init|=
operator|new
name|ApplicationProjectFile
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|projectFile
operator|.
name|setSaveDelegate
argument_list|(
operator|new
name|SaveDelegate
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|projectFile
return|;
block|}
specifier|private
name|DomainMetaData
name|findDomain
parameter_list|(
name|String
name|domainName
parameter_list|)
block|{
name|DomainMetaData
name|domain
init|=
name|domains
operator|.
name|get
argument_list|(
name|domainName
argument_list|)
decl_stmt|;
if|if
condition|(
name|domain
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Can't find domain: "
operator|+
name|domainName
argument_list|)
throw|;
block|}
return|return
name|domain
return|;
block|}
specifier|private
name|NodeMetaData
name|findNode
parameter_list|(
name|String
name|domainName
parameter_list|,
name|String
name|nodeName
parameter_list|,
name|boolean
name|failIfNotFound
parameter_list|)
block|{
name|DomainMetaData
name|domain
init|=
name|findDomain
argument_list|(
name|domainName
argument_list|)
decl_stmt|;
name|NodeMetaData
name|node
init|=
name|domain
operator|.
name|nodes
operator|.
name|get
argument_list|(
name|nodeName
argument_list|)
decl_stmt|;
if|if
condition|(
name|node
operator|==
literal|null
operator|&&
name|failIfNotFound
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Can't find node: "
operator|+
name|domainName
operator|+
literal|"."
operator|+
name|nodeName
argument_list|)
throw|;
block|}
return|return
name|node
return|;
block|}
specifier|protected
class|class
name|DomainMetaData
block|{
specifier|protected
name|String
name|name
decl_stmt|;
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|NodeMetaData
argument_list|>
name|nodes
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|NodeMetaData
argument_list|>
argument_list|()
decl_stmt|;
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|MapMetaData
argument_list|>
name|maps
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|MapMetaData
argument_list|>
argument_list|()
decl_stmt|;
specifier|protected
name|Map
name|mapDependencies
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
specifier|protected
name|Map
name|properties
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
specifier|public
name|DomainMetaData
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
block|}
specifier|protected
class|class
name|NodeMetaData
block|{
specifier|protected
name|String
name|name
decl_stmt|;
specifier|protected
name|String
name|dataSource
decl_stmt|;
specifier|protected
name|String
name|adapter
decl_stmt|;
specifier|protected
name|String
name|factory
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|maps
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|NodeMetaData
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
block|}
specifier|protected
class|class
name|MapMetaData
block|{
specifier|protected
name|String
name|name
decl_stmt|;
specifier|protected
name|String
name|location
decl_stmt|;
specifier|public
name|MapMetaData
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
block|}
class|class
name|LoadDelegate
implements|implements
name|ConfigLoaderDelegate
block|{
specifier|protected
name|ConfigStatus
name|status
init|=
operator|new
name|ConfigStatus
argument_list|()
decl_stmt|;
specifier|public
name|void
name|shouldLoadProjectVersion
parameter_list|(
name|String
name|version
parameter_list|)
block|{
name|PartialProject
operator|.
name|this
operator|.
name|projectVersion
operator|=
name|version
expr_stmt|;
block|}
specifier|protected
name|DomainMetaData
name|findDomain
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|DomainMetaData
name|domain
init|=
name|domains
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|domain
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ProjectException
argument_list|(
literal|"Can't find domain: "
operator|+
name|name
argument_list|)
throw|;
block|}
return|return
name|domain
return|;
block|}
specifier|protected
name|MapMetaData
name|findMap
parameter_list|(
name|String
name|domainName
parameter_list|,
name|String
name|mapName
parameter_list|)
block|{
name|DomainMetaData
name|domain
init|=
name|findDomain
argument_list|(
name|domainName
argument_list|)
decl_stmt|;
name|MapMetaData
name|map
init|=
name|domain
operator|.
name|maps
operator|.
name|get
argument_list|(
name|mapName
argument_list|)
decl_stmt|;
if|if
condition|(
name|map
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ProjectException
argument_list|(
literal|"Can't find map: "
operator|+
name|mapName
argument_list|)
throw|;
block|}
return|return
name|map
return|;
block|}
specifier|protected
name|NodeMetaData
name|findNode
parameter_list|(
name|String
name|domainName
parameter_list|,
name|String
name|nodeName
parameter_list|)
block|{
name|DomainMetaData
name|domain
init|=
name|findDomain
argument_list|(
name|domainName
argument_list|)
decl_stmt|;
name|NodeMetaData
name|node
init|=
name|domain
operator|.
name|nodes
operator|.
name|get
argument_list|(
name|nodeName
argument_list|)
decl_stmt|;
if|if
condition|(
name|node
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ProjectException
argument_list|(
literal|"Can't find node: "
operator|+
name|nodeName
argument_list|)
throw|;
block|}
return|return
name|node
return|;
block|}
specifier|public
name|void
name|startedLoading
parameter_list|()
block|{
name|domains
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|finishedLoading
parameter_list|()
block|{
block|}
specifier|public
name|ConfigStatus
name|getStatus
parameter_list|()
block|{
return|return
name|status
return|;
block|}
specifier|public
name|boolean
name|loadError
parameter_list|(
name|Throwable
name|th
parameter_list|)
block|{
name|status
operator|.
name|getOtherFailures
argument_list|()
operator|.
name|add
argument_list|(
name|th
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
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
name|findNode
argument_list|(
name|domainName
argument_list|,
name|nodeName
argument_list|)
operator|.
name|maps
operator|.
name|add
argument_list|(
name|mapName
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|shouldLoadDataDomain
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|domains
operator|.
name|put
argument_list|(
name|name
argument_list|,
operator|new
name|DomainMetaData
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
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
if|if
condition|(
name|properties
operator|==
literal|null
operator|||
name|properties
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|DomainMetaData
name|domain
init|=
name|findDomain
argument_list|(
name|domainName
argument_list|)
decl_stmt|;
name|domain
operator|.
name|properties
operator|.
name|putAll
argument_list|(
name|properties
argument_list|)
expr_stmt|;
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
if|if
condition|(
name|locations
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return;
block|}
name|DomainMetaData
name|domain
init|=
name|findDomain
argument_list|(
name|domainName
argument_list|)
decl_stmt|;
comment|// load DataMaps tree
name|Iterator
name|it
init|=
name|locations
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Map
operator|.
name|Entry
name|entry
init|=
operator|(
name|Map
operator|.
name|Entry
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|name
init|=
operator|(
name|String
operator|)
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|MapMetaData
name|map
init|=
operator|new
name|MapMetaData
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|map
operator|.
name|location
operator|=
operator|(
name|String
operator|)
name|entry
operator|.
name|getValue
argument_list|()
expr_stmt|;
name|domain
operator|.
name|maps
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|map
argument_list|)
expr_stmt|;
block|}
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
parameter_list|)
block|{
name|NodeMetaData
name|node
init|=
operator|new
name|NodeMetaData
argument_list|(
name|nodeName
argument_list|)
decl_stmt|;
name|node
operator|.
name|adapter
operator|=
name|adapter
expr_stmt|;
name|node
operator|.
name|factory
operator|=
name|factory
expr_stmt|;
name|node
operator|.
name|dataSource
operator|=
name|dataSource
expr_stmt|;
name|findDomain
argument_list|(
name|domainName
argument_list|)
operator|.
name|nodes
operator|.
name|put
argument_list|(
name|nodeName
argument_list|,
name|node
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|shouldRegisterDataView
parameter_list|(
name|String
name|dataViewName
parameter_list|,
name|String
name|dataViewLocation
parameter_list|)
block|{
name|Validate
operator|.
name|notNull
argument_list|(
name|dataViewName
argument_list|)
expr_stmt|;
name|Validate
operator|.
name|notNull
argument_list|(
name|dataViewLocation
argument_list|)
expr_stmt|;
if|if
condition|(
name|dataViewLocations
operator|==
literal|null
condition|)
block|{
name|dataViewLocations
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
expr_stmt|;
block|}
name|dataViewLocations
operator|.
name|put
argument_list|(
name|dataViewName
argument_list|,
name|dataViewLocation
argument_list|)
expr_stmt|;
block|}
block|}
class|class
name|SaveDelegate
implements|implements
name|ConfigSaverDelegate
block|{
comment|/**          * @since 1.1          */
specifier|public
name|String
name|projectVersion
parameter_list|()
block|{
return|return
name|projectVersion
return|;
block|}
specifier|public
name|Iterator
name|domainNames
parameter_list|()
block|{
return|return
name|domains
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
return|;
block|}
specifier|public
name|Iterator
name|viewNames
parameter_list|()
block|{
if|if
condition|(
name|dataViewLocations
operator|==
literal|null
condition|)
block|{
name|dataViewLocations
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
expr_stmt|;
block|}
return|return
name|dataViewLocations
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
return|;
block|}
specifier|public
name|String
name|viewLocation
parameter_list|(
name|String
name|dataViewName
parameter_list|)
block|{
if|if
condition|(
name|dataViewLocations
operator|==
literal|null
condition|)
block|{
name|dataViewLocations
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
expr_stmt|;
block|}
return|return
name|dataViewLocations
operator|.
name|get
argument_list|(
name|dataViewName
argument_list|)
return|;
block|}
specifier|public
name|Iterator
name|propertyNames
parameter_list|(
name|String
name|domainName
parameter_list|)
block|{
return|return
name|findDomain
argument_list|(
name|domainName
argument_list|)
operator|.
name|properties
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
return|;
block|}
specifier|public
name|String
name|propertyValue
parameter_list|(
name|String
name|domainName
parameter_list|,
name|String
name|propertyName
parameter_list|)
block|{
return|return
operator|(
name|String
operator|)
name|findDomain
argument_list|(
name|domainName
argument_list|)
operator|.
name|properties
operator|.
name|get
argument_list|(
name|propertyName
argument_list|)
return|;
block|}
specifier|public
name|Iterator
name|linkedMapNames
parameter_list|(
name|String
name|domainName
parameter_list|,
name|String
name|nodeName
parameter_list|)
block|{
return|return
operator|(
name|findDomain
argument_list|(
name|domainName
argument_list|)
operator|.
name|nodes
operator|.
name|get
argument_list|(
name|nodeName
argument_list|)
operator|)
operator|.
name|maps
operator|.
name|iterator
argument_list|()
return|;
block|}
specifier|public
name|String
name|mapLocation
parameter_list|(
name|String
name|domainName
parameter_list|,
name|String
name|mapName
parameter_list|)
block|{
return|return
operator|(
name|findDomain
argument_list|(
name|domainName
argument_list|)
operator|.
name|maps
operator|.
name|get
argument_list|(
name|mapName
argument_list|)
operator|)
operator|.
name|location
return|;
block|}
specifier|public
name|Iterator
name|mapNames
parameter_list|(
name|String
name|domainName
parameter_list|)
block|{
return|return
name|findDomain
argument_list|(
name|domainName
argument_list|)
operator|.
name|maps
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
return|;
block|}
specifier|public
name|String
name|nodeAdapterName
parameter_list|(
name|String
name|domainName
parameter_list|,
name|String
name|nodeName
parameter_list|)
block|{
return|return
operator|(
name|findDomain
argument_list|(
name|domainName
argument_list|)
operator|.
name|nodes
operator|.
name|get
argument_list|(
name|nodeName
argument_list|)
operator|)
operator|.
name|adapter
return|;
block|}
specifier|public
name|String
name|nodeDataSourceName
parameter_list|(
name|String
name|domainName
parameter_list|,
name|String
name|nodeName
parameter_list|)
block|{
return|return
operator|(
name|findDomain
argument_list|(
name|domainName
argument_list|)
operator|.
name|nodes
operator|.
name|get
argument_list|(
name|nodeName
argument_list|)
operator|)
operator|.
name|dataSource
return|;
block|}
specifier|public
name|String
name|nodeFactoryName
parameter_list|(
name|String
name|domainName
parameter_list|,
name|String
name|nodeName
parameter_list|)
block|{
return|return
operator|(
name|findDomain
argument_list|(
name|domainName
argument_list|)
operator|.
name|nodes
operator|.
name|get
argument_list|(
name|nodeName
argument_list|)
operator|)
operator|.
name|factory
return|;
block|}
specifier|public
name|Iterator
name|nodeNames
parameter_list|(
name|String
name|domainName
parameter_list|)
block|{
return|return
name|findDomain
argument_list|(
name|domainName
argument_list|)
operator|.
name|nodes
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

