begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  *    or more contributor license agreements.  See the NOTICE file  *    distributed with this work for additional information  *    regarding copyright ownership.  The ASF licenses this file  *    to you under the Apache License, Version 2.0 (the  *    "License"); you may not use this file except in compliance  *    with the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  *    Unless required by applicable law or agreed to in writing,  *    software distributed under the License is distributed on an  *    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *    KIND, either express or implied.  See the License for the  *    specific language governing permissions and limitations  *    under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|tools
operator|.
name|dbimport
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|access
operator|.
name|DbLoader
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
name|configuration
operator|.
name|ConfigurationTree
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
name|configuration
operator|.
name|DataNodeDescriptor
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
name|configuration
operator|.
name|server
operator|.
name|DataSourceFactory
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
name|configuration
operator|.
name|server
operator|.
name|DbAdapterFactory
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
name|dba
operator|.
name|DbAdapter
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
name|di
operator|.
name|Inject
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
name|EntityResolver
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
name|MapLoader
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
name|merge
operator|.
name|DbMerger
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
name|merge
operator|.
name|ExecutingMergerContext
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
name|merge
operator|.
name|MergerContext
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
name|merge
operator|.
name|MergerFactory
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
name|merge
operator|.
name|MergerToken
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
name|merge
operator|.
name|ModelMergeDelegate
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
name|project
operator|.
name|Project
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
name|project
operator|.
name|ProjectSaver
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
name|resource
operator|.
name|URLResource
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
name|validation
operator|.
name|SimpleValidationFailure
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
name|validation
operator|.
name|ValidationFailure
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
name|validation
operator|.
name|ValidationResult
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
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|InputSource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|sql
operator|.
name|DataSource
import|;
end_import

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
name|FileNotFoundException
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
name|sql
operator|.
name|Connection
import|;
end_import

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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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
import|import static
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|lang
operator|.
name|StringUtils
operator|.
name|isBlank
import|;
end_import

begin_comment
comment|/**  * A thin wrapper around {@link DbLoader} that encapsulates DB import logic for  * the benefit of Ant and Maven db importers.  *   * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|DbImportAction
block|{
specifier|private
specifier|final
name|ProjectSaver
name|projectSaver
decl_stmt|;
specifier|private
specifier|final
name|Log
name|logger
decl_stmt|;
specifier|private
specifier|final
name|DataSourceFactory
name|dataSourceFactory
decl_stmt|;
specifier|private
specifier|final
name|DbAdapterFactory
name|adapterFactory
decl_stmt|;
specifier|private
specifier|final
name|MapLoader
name|mapLoader
decl_stmt|;
specifier|public
name|DbImportAction
parameter_list|(
annotation|@
name|Inject
name|Log
name|logger
parameter_list|,
annotation|@
name|Inject
name|ProjectSaver
name|projectSaver
parameter_list|,
annotation|@
name|Inject
name|DataSourceFactory
name|dataSourceFactory
parameter_list|,
annotation|@
name|Inject
name|DbAdapterFactory
name|adapterFactory
parameter_list|,
annotation|@
name|Inject
name|MapLoader
name|mapLoader
parameter_list|)
block|{
name|this
operator|.
name|logger
operator|=
name|logger
expr_stmt|;
name|this
operator|.
name|projectSaver
operator|=
name|projectSaver
expr_stmt|;
name|this
operator|.
name|dataSourceFactory
operator|=
name|dataSourceFactory
expr_stmt|;
name|this
operator|.
name|adapterFactory
operator|=
name|adapterFactory
expr_stmt|;
name|this
operator|.
name|mapLoader
operator|=
name|mapLoader
expr_stmt|;
block|}
specifier|public
name|void
name|execute
parameter_list|(
name|DbImportConfiguration
name|config
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|logger
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|logger
operator|.
name|debug
argument_list|(
literal|"DB connection: "
operator|+
name|config
operator|.
name|getDataSourceInfo
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|logger
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|logger
operator|.
name|debug
argument_list|(
name|config
argument_list|)
expr_stmt|;
block|}
name|DataNodeDescriptor
name|dataNodeDescriptor
init|=
name|config
operator|.
name|createDataNodeDescriptor
argument_list|()
decl_stmt|;
name|DataSource
name|dataSource
init|=
name|dataSourceFactory
operator|.
name|getDataSource
argument_list|(
name|dataNodeDescriptor
argument_list|)
decl_stmt|;
name|DbAdapter
name|adapter
init|=
name|adapterFactory
operator|.
name|createAdapter
argument_list|(
name|dataNodeDescriptor
argument_list|,
name|dataSource
argument_list|)
decl_stmt|;
name|DataMap
name|loadedFomDb
init|=
name|load
argument_list|(
name|config
argument_list|,
name|adapter
argument_list|,
name|dataSource
operator|.
name|getConnection
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|loadedFomDb
operator|==
literal|null
condition|)
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"Nothing was loaded from db."
argument_list|)
expr_stmt|;
return|return;
block|}
name|DataMap
name|existing
init|=
name|loadExistingDataMap
argument_list|(
name|config
operator|.
name|getDataMapFile
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|existing
operator|==
literal|null
condition|)
block|{
name|saveLoaded
argument_list|(
name|config
operator|.
name|initializeDataMap
argument_list|(
name|loadedFomDb
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|MergerFactory
name|mergerFactory
init|=
name|adapter
operator|.
name|mergerFactory
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|MergerToken
argument_list|>
name|mergeTokens
init|=
operator|new
name|DbMerger
argument_list|(
name|mergerFactory
argument_list|)
operator|.
name|createMergeTokens
argument_list|(
name|existing
argument_list|,
name|loadedFomDb
argument_list|,
name|config
operator|.
name|getDbLoaderConfig
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|mergeTokens
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"No changes to import."
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
operator|!
name|isBlank
argument_list|(
name|config
operator|.
name|getDefaultPackage
argument_list|()
argument_list|)
condition|)
block|{
name|existing
operator|.
name|setDefaultPackage
argument_list|(
name|config
operator|.
name|getDefaultPackage
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|saveLoaded
argument_list|(
name|execute
argument_list|(
name|config
operator|.
name|createMergeDelegate
argument_list|()
argument_list|,
name|existing
argument_list|,
name|log
argument_list|(
name|reverse
argument_list|(
name|mergerFactory
argument_list|,
name|mergeTokens
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|Collection
argument_list|<
name|MergerToken
argument_list|>
name|log
parameter_list|(
name|List
argument_list|<
name|MergerToken
argument_list|>
name|tokens
parameter_list|)
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"Detected changes: "
argument_list|)
expr_stmt|;
for|for
control|(
name|MergerToken
name|token
range|:
name|tokens
control|)
block|{
name|logger
operator|.
name|info
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"    %-20s %s"
argument_list|,
name|token
operator|.
name|getTokenName
argument_list|()
argument_list|,
name|token
operator|.
name|getTokenValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|logger
operator|.
name|info
argument_list|(
literal|""
argument_list|)
expr_stmt|;
return|return
name|tokens
return|;
block|}
specifier|private
name|DataMap
name|loadExistingDataMap
parameter_list|(
name|File
name|dataMapFile
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|dataMapFile
operator|!=
literal|null
operator|&&
name|dataMapFile
operator|.
name|exists
argument_list|()
operator|&&
name|dataMapFile
operator|.
name|canRead
argument_list|()
condition|)
block|{
name|DataMap
name|dataMap
init|=
name|mapLoader
operator|.
name|loadDataMap
argument_list|(
operator|new
name|InputSource
argument_list|(
name|dataMapFile
operator|.
name|getCanonicalPath
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|dataMap
operator|.
name|setNamespace
argument_list|(
operator|new
name|EntityResolver
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|dataMap
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|dataMap
operator|.
name|setConfigurationSource
argument_list|(
operator|new
name|URLResource
argument_list|(
name|dataMapFile
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|dataMap
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|List
argument_list|<
name|MergerToken
argument_list|>
name|reverse
parameter_list|(
name|MergerFactory
name|mergerFactory
parameter_list|,
name|Iterable
argument_list|<
name|MergerToken
argument_list|>
name|mergeTokens
parameter_list|)
throws|throws
name|IOException
block|{
name|List
argument_list|<
name|MergerToken
argument_list|>
name|tokens
init|=
operator|new
name|LinkedList
argument_list|<
name|MergerToken
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|MergerToken
name|token
range|:
name|mergeTokens
control|)
block|{
name|tokens
operator|.
name|add
argument_list|(
name|token
operator|.
name|createReverse
argument_list|(
name|mergerFactory
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|tokens
return|;
block|}
comment|/**      * Performs configured schema operations via DbGenerator.      */
specifier|public
name|DataMap
name|execute
parameter_list|(
name|ModelMergeDelegate
name|mergeDelegate
parameter_list|,
name|DataMap
name|dataMap
parameter_list|,
name|Collection
argument_list|<
name|MergerToken
argument_list|>
name|tokens
parameter_list|)
block|{
name|MergerContext
name|mergerContext
init|=
operator|new
name|ExecutingMergerContext
argument_list|(
name|dataMap
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|mergeDelegate
argument_list|)
decl_stmt|;
for|for
control|(
name|MergerToken
name|tok
range|:
name|tokens
control|)
block|{
try|try
block|{
name|tok
operator|.
name|execute
argument_list|(
name|mergerContext
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|th
parameter_list|)
block|{
name|String
name|message
init|=
literal|"Migration Error. Can't apply changes from token: "
operator|+
name|tok
operator|.
name|getTokenName
argument_list|()
operator|+
literal|" ("
operator|+
name|tok
operator|.
name|getTokenValue
argument_list|()
operator|+
literal|")"
decl_stmt|;
name|logger
operator|.
name|error
argument_list|(
name|message
argument_list|,
name|th
argument_list|)
expr_stmt|;
name|mergerContext
operator|.
name|getValidationResult
argument_list|()
operator|.
name|addFailure
argument_list|(
operator|new
name|SimpleValidationFailure
argument_list|(
name|th
argument_list|,
name|message
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|ValidationResult
name|failures
init|=
name|mergerContext
operator|.
name|getValidationResult
argument_list|()
decl_stmt|;
if|if
condition|(
name|failures
operator|==
literal|null
operator|||
operator|!
name|failures
operator|.
name|hasFailures
argument_list|()
condition|)
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"Migration Complete Successfully."
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"Migration Complete."
argument_list|)
expr_stmt|;
name|logger
operator|.
name|warn
argument_list|(
literal|"Migration finished. The following problem(s) were ignored."
argument_list|)
expr_stmt|;
for|for
control|(
name|ValidationFailure
name|failure
range|:
name|failures
operator|.
name|getFailures
argument_list|()
control|)
block|{
name|logger
operator|.
name|warn
argument_list|(
name|failure
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|dataMap
return|;
block|}
name|void
name|saveLoaded
parameter_list|(
name|DataMap
name|dataMap
parameter_list|)
throws|throws
name|FileNotFoundException
block|{
name|ConfigurationTree
argument_list|<
name|DataMap
argument_list|>
name|projectRoot
init|=
operator|new
name|ConfigurationTree
argument_list|<
name|DataMap
argument_list|>
argument_list|(
name|dataMap
argument_list|)
decl_stmt|;
name|Project
name|project
init|=
operator|new
name|Project
argument_list|(
name|projectRoot
argument_list|)
decl_stmt|;
name|projectSaver
operator|.
name|save
argument_list|(
name|project
argument_list|)
expr_stmt|;
block|}
specifier|private
name|DataMap
name|load
parameter_list|(
name|DbImportConfiguration
name|config
parameter_list|,
name|DbAdapter
name|adapter
parameter_list|,
name|Connection
name|connection
parameter_list|)
throws|throws
name|Exception
block|{
name|DataMap
name|dataMap
init|=
name|config
operator|.
name|createDataMap
argument_list|()
decl_stmt|;
try|try
block|{
name|DbLoader
name|loader
init|=
name|config
operator|.
name|createLoader
argument_list|(
name|adapter
argument_list|,
name|connection
argument_list|,
name|config
operator|.
name|createLoaderDelegate
argument_list|()
argument_list|)
decl_stmt|;
name|loader
operator|.
name|load
argument_list|(
name|dataMap
argument_list|,
name|config
operator|.
name|getDbLoaderConfig
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|connection
operator|!=
literal|null
condition|)
block|{
name|connection
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|dataMap
return|;
block|}
block|}
end_class

end_unit

