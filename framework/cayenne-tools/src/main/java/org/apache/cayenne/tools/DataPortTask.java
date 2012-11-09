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
name|tools
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
name|HashSet
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
name|access
operator|.
name|DataDomain
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
name|access
operator|.
name|DataNode
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
name|access
operator|.
name|DataPort
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
name|ServerRuntime
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
name|Binder
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
name|Module
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
name|DbEntity
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
name|FilesystemResourceLocator
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
name|ResourceLocator
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
name|util
operator|.
name|Util
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tools
operator|.
name|ant
operator|.
name|BuildException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tools
operator|.
name|ant
operator|.
name|Project
import|;
end_import

begin_comment
comment|/**  * A "cdataport" Ant task implementing a frontend to DataPort allowing porting  * database data using Ant build scripts.  *   * @since 1.2: Prior to 1.2 DataPort classes were a part of cayenne-examples  *        package.  * @deprecated since 3.2  */
end_comment

begin_class
annotation|@
name|Deprecated
specifier|public
class|class
name|DataPortTask
extends|extends
name|CayenneTask
block|{
specifier|protected
name|File
name|projectFile
decl_stmt|;
specifier|protected
name|String
name|maps
decl_stmt|;
specifier|protected
name|String
name|srcNode
decl_stmt|;
specifier|protected
name|String
name|destNode
decl_stmt|;
specifier|protected
name|String
name|includeTables
decl_stmt|;
specifier|protected
name|String
name|excludeTables
decl_stmt|;
specifier|protected
name|boolean
name|cleanDest
init|=
literal|true
decl_stmt|;
specifier|public
name|DataPortTask
parameter_list|()
block|{
comment|// set defaults
name|this
operator|.
name|cleanDest
operator|=
literal|true
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|execute
parameter_list|()
throws|throws
name|BuildException
block|{
name|log
argument_list|(
literal|"*** 'cdataport' task is deprecated and will be removed after 3.2"
argument_list|,
name|Project
operator|.
name|MSG_WARN
argument_list|)
expr_stmt|;
name|validateParameters
argument_list|()
expr_stmt|;
name|String
name|projectFileLocation
init|=
name|projectFile
operator|.
name|getName
argument_list|()
decl_stmt|;
name|Module
name|dataPortModule
init|=
operator|new
name|Module
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|(
name|Binder
name|binder
parameter_list|)
block|{
name|binder
operator|.
name|bind
argument_list|(
name|ResourceLocator
operator|.
name|class
argument_list|)
operator|.
name|toInstance
argument_list|(
operator|new
name|FilesystemResourceLocator
argument_list|(
name|projectFile
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|ServerRuntime
name|runtime
init|=
operator|new
name|ServerRuntime
argument_list|(
name|projectFileLocation
argument_list|,
name|dataPortModule
argument_list|)
decl_stmt|;
name|DataDomain
name|domain
decl_stmt|;
name|ClassLoader
name|threadContextClassLoader
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
decl_stmt|;
try|try
block|{
comment|// need to set context class loader so that cayenne can find jdbc
comment|// driver and
comment|// PasswordEncoder
comment|// TODO: andrus 04/11/2010 is this still relevant in 3.1?
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
name|domain
operator|=
name|runtime
operator|.
name|getDataDomain
argument_list|()
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
name|BuildException
argument_list|(
literal|"Error loading Cayenne configuration from "
operator|+
name|projectFile
argument_list|,
name|ex
argument_list|)
throw|;
block|}
finally|finally
block|{
comment|// set back to original ClassLoader
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|threadContextClassLoader
argument_list|)
expr_stmt|;
block|}
comment|// perform project validation
name|DataNode
name|source
init|=
name|domain
operator|.
name|getDataNode
argument_list|(
name|srcNode
argument_list|)
decl_stmt|;
if|if
condition|(
name|source
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"srcNode not found in the project: "
operator|+
name|srcNode
argument_list|)
throw|;
block|}
name|DataNode
name|destination
init|=
name|domain
operator|.
name|getDataNode
argument_list|(
name|destNode
argument_list|)
decl_stmt|;
if|if
condition|(
name|destination
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"destNode not found in the project: "
operator|+
name|destNode
argument_list|)
throw|;
block|}
name|log
argument_list|(
literal|"Porting from '"
operator|+
name|srcNode
operator|+
literal|"' to '"
operator|+
name|destNode
operator|+
literal|"'."
argument_list|)
expr_stmt|;
name|AntDataPortDelegate
name|portDelegate
init|=
operator|new
name|AntDataPortDelegate
argument_list|(
name|this
argument_list|,
name|maps
argument_list|,
name|includeTables
argument_list|,
name|excludeTables
argument_list|)
decl_stmt|;
name|DataPort
name|dataPort
init|=
operator|new
name|DataPort
argument_list|(
name|portDelegate
argument_list|)
decl_stmt|;
name|dataPort
operator|.
name|setEntities
argument_list|(
name|getAllEntities
argument_list|(
name|source
argument_list|,
name|destination
argument_list|)
argument_list|)
expr_stmt|;
name|dataPort
operator|.
name|setCleaningDestination
argument_list|(
name|cleanDest
argument_list|)
expr_stmt|;
name|dataPort
operator|.
name|setSourceNode
argument_list|(
name|source
argument_list|)
expr_stmt|;
name|dataPort
operator|.
name|setDestinationNode
argument_list|(
name|destination
argument_list|)
expr_stmt|;
try|try
block|{
name|dataPort
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|Throwable
name|topOfStack
init|=
name|Util
operator|.
name|unwindException
argument_list|(
name|e
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"Error porting data: "
operator|+
name|topOfStack
operator|.
name|getMessage
argument_list|()
argument_list|,
name|topOfStack
argument_list|)
throw|;
block|}
block|}
specifier|protected
name|Collection
argument_list|<
name|DbEntity
argument_list|>
name|getAllEntities
parameter_list|(
name|DataNode
name|source
parameter_list|,
name|DataNode
name|target
parameter_list|)
block|{
comment|// use a set to exclude duplicates, though a valid project will probably
comment|// have
comment|// none...
name|Collection
argument_list|<
name|DbEntity
argument_list|>
name|allEntities
init|=
operator|new
name|HashSet
argument_list|<
name|DbEntity
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|DataMap
name|map
range|:
name|source
operator|.
name|getDataMaps
argument_list|()
control|)
block|{
name|allEntities
operator|.
name|addAll
argument_list|(
name|map
operator|.
name|getDbEntities
argument_list|()
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|DataMap
name|map
range|:
name|target
operator|.
name|getDataMaps
argument_list|()
control|)
block|{
name|allEntities
operator|.
name|addAll
argument_list|(
name|map
operator|.
name|getDbEntities
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|log
argument_list|(
literal|"Number of entities: "
operator|+
name|allEntities
operator|.
name|size
argument_list|()
argument_list|,
name|Project
operator|.
name|MSG_VERBOSE
argument_list|)
expr_stmt|;
if|if
condition|(
name|allEntities
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
name|log
argument_list|(
literal|"No entities found for either source or target."
argument_list|)
expr_stmt|;
block|}
return|return
name|allEntities
return|;
block|}
specifier|protected
name|void
name|validateParameters
parameter_list|()
throws|throws
name|BuildException
block|{
if|if
condition|(
name|projectFile
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"Required 'projectFile' parameter is missing."
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|projectFile
operator|.
name|exists
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"'projectFile' does not exist: "
operator|+
name|projectFile
argument_list|)
throw|;
block|}
if|if
condition|(
name|srcNode
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"Required 'srcNode' parameter is missing."
argument_list|)
throw|;
block|}
if|if
condition|(
name|destNode
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"Required 'destNode' parameter is missing."
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|setDestNode
parameter_list|(
name|String
name|destNode
parameter_list|)
block|{
name|this
operator|.
name|destNode
operator|=
name|destNode
expr_stmt|;
block|}
specifier|public
name|void
name|setExcludeTables
parameter_list|(
name|String
name|excludeTables
parameter_list|)
block|{
name|this
operator|.
name|excludeTables
operator|=
name|excludeTables
expr_stmt|;
block|}
specifier|public
name|void
name|setIncludeTables
parameter_list|(
name|String
name|includeTables
parameter_list|)
block|{
name|this
operator|.
name|includeTables
operator|=
name|includeTables
expr_stmt|;
block|}
specifier|public
name|void
name|setMaps
parameter_list|(
name|String
name|maps
parameter_list|)
block|{
name|this
operator|.
name|maps
operator|=
name|maps
expr_stmt|;
block|}
specifier|public
name|void
name|setProjectFile
parameter_list|(
name|File
name|projectFile
parameter_list|)
block|{
name|this
operator|.
name|projectFile
operator|=
name|projectFile
expr_stmt|;
block|}
specifier|public
name|void
name|setSrcNode
parameter_list|(
name|String
name|srcNode
parameter_list|)
block|{
name|this
operator|.
name|srcNode
operator|=
name|srcNode
expr_stmt|;
block|}
specifier|public
name|void
name|setCleanDest
parameter_list|(
name|boolean
name|flag
parameter_list|)
block|{
name|this
operator|.
name|cleanDest
operator|=
name|flag
expr_stmt|;
block|}
block|}
end_class

end_unit

