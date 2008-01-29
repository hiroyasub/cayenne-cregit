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
name|InputStream
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
name|List
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
name|MapLoader
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

begin_comment
comment|/**  * Cayenne project that consists of a single DataMap.  *   * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|DataMapProject
extends|extends
name|Project
block|{
specifier|protected
name|DataMap
name|map
decl_stmt|;
comment|/**      * Constructor for MapProject.      *       * @param projectFile      */
specifier|public
name|DataMapProject
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
literal|"'DataMapProject' does not support upgrades."
argument_list|)
throw|;
block|}
comment|/**      * Does nothing.      */
annotation|@
name|Override
specifier|public
name|void
name|checkForUpgrades
parameter_list|()
block|{
comment|// do nothing
block|}
comment|/**      * Initializes internal<code>map</code> object and then calls super.      */
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
if|if
condition|(
name|projectFile
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|InputStream
name|in
init|=
operator|new
name|FileInputStream
argument_list|(
name|projectFile
operator|.
name|getCanonicalFile
argument_list|()
argument_list|)
decl_stmt|;
name|map
operator|=
operator|new
name|MapLoader
argument_list|()
operator|.
name|loadDataMap
argument_list|(
operator|new
name|InputSource
argument_list|(
name|in
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|fileName
init|=
name|resolveSymbolicName
argument_list|(
name|projectFile
argument_list|)
decl_stmt|;
name|String
name|mapName
init|=
operator|(
name|fileName
operator|!=
literal|null
operator|&&
name|fileName
operator|.
name|endsWith
argument_list|(
name|DataMapFile
operator|.
name|LOCATION_SUFFIX
argument_list|)
operator|)
condition|?
name|fileName
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|fileName
operator|.
name|length
argument_list|()
operator|-
name|DataMapFile
operator|.
name|LOCATION_SUFFIX
operator|.
name|length
argument_list|()
argument_list|)
else|:
literal|"UntitledMap"
decl_stmt|;
name|map
operator|.
name|setName
argument_list|(
name|mapName
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|dme
parameter_list|)
block|{
throw|throw
operator|new
name|ProjectException
argument_list|(
literal|"Error creating "
operator|+
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|dme
argument_list|)
throw|;
block|}
block|}
else|else
block|{
name|map
operator|=
operator|(
name|DataMap
operator|)
name|NamedObjectFactory
operator|.
name|createObject
argument_list|(
name|DataMap
operator|.
name|class
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|postInitialize
argument_list|(
name|projectFile
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns a list that contains project DataMap as a single object.      */
annotation|@
name|Override
specifier|public
name|List
name|getChildren
parameter_list|()
block|{
name|List
argument_list|<
name|DataMap
argument_list|>
name|entities
init|=
operator|new
name|ArrayList
argument_list|<
name|DataMap
argument_list|>
argument_list|()
decl_stmt|;
name|entities
operator|.
name|add
argument_list|(
name|map
argument_list|)
expr_stmt|;
return|return
name|entities
return|;
block|}
comment|/**      * Returns appropriate ProjectFile or null if object does not require a file of its      * own. In case of DataMapProject, the only object that requires a file is the project      * itself.      */
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
operator|==
name|this
condition|)
block|{
return|return
operator|new
name|DataMapFile
argument_list|(
name|this
argument_list|,
name|map
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Always returns empty status. Map projects do not support status tracking yet.      */
annotation|@
name|Override
specifier|public
name|ConfigStatus
name|getLoadStatus
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

