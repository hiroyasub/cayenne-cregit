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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|conf
operator|.
name|Configuration
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
name|FileConfiguration
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
name|ResourceLocator
import|;
end_import

begin_comment
comment|/**  * Subclass of FileConfiguration used in the project model.  *   * @author Misha Shengaout  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|ProjectConfiguration
extends|extends
name|FileConfiguration
block|{
comment|/**      * Override parent implementation to ignore loading failures.      *       * @see FileConfiguration#FileConfiguration(File)      */
specifier|public
name|ProjectConfiguration
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
comment|// ignore loading failures
name|this
operator|.
name|setIgnoringLoadFailures
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// configure deterministic file opening rules
name|ResourceLocator
name|locator
init|=
name|this
operator|.
name|getResourceLocator
argument_list|()
decl_stmt|;
name|locator
operator|.
name|setSkipAbsolutePath
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|locator
operator|.
name|setSkipClasspath
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|locator
operator|.
name|setSkipCurrentDirectory
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|locator
operator|.
name|setSkipHomeDirectory
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
comment|/**      * Override parent implementation to prevent loading of nonexisting files.      *       * @see FileConfiguration#canInitialize()      */
annotation|@
name|Override
specifier|public
name|boolean
name|canInitialize
parameter_list|()
block|{
return|return
operator|(
name|super
operator|.
name|canInitialize
argument_list|()
operator|&&
name|this
operator|.
name|getProjectFile
argument_list|()
operator|.
name|isFile
argument_list|()
operator|)
return|;
block|}
comment|/**      * Override parent implementation to allow for null files.      *       * @see FileConfiguration#setProjectFile(File)      */
annotation|@
name|Override
specifier|protected
name|void
name|setProjectFile
parameter_list|(
name|File
name|projectFile
parameter_list|)
block|{
if|if
condition|(
operator|(
name|projectFile
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|projectFile
operator|.
name|exists
argument_list|()
operator|)
condition|)
block|{
name|super
operator|.
name|setProjectFile
argument_list|(
name|projectFile
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|super
operator|.
name|projectFile
operator|=
name|projectFile
expr_stmt|;
name|this
operator|.
name|setDomainConfigurationName
argument_list|(
name|Configuration
operator|.
name|DEFAULT_DOMAIN_FILE
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|DataSourceFactory
name|getDataSourceFactory
parameter_list|(
name|String
name|userFactoryName
parameter_list|)
block|{
name|File
name|projectDir
init|=
name|getProjectDirectory
argument_list|()
decl_stmt|;
name|boolean
name|canLoad
init|=
name|DriverDataSourceFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|userFactoryName
argument_list|)
decl_stmt|;
name|ProjectDataSourceFactory
name|factory
init|=
operator|new
name|ProjectDataSourceFactory
argument_list|(
name|projectDir
argument_list|,
name|canLoad
argument_list|)
decl_stmt|;
return|return
name|factory
return|;
block|}
comment|/**      * Returns an instance of {@link ProjectDataSourceFactory}.      *       * @deprecated since 3.0 as the super method is deprecated as well.      */
annotation|@
name|Override
specifier|public
name|DataSourceFactory
name|getDataSourceFactory
parameter_list|()
block|{
try|try
block|{
return|return
operator|new
name|ProjectDataSourceFactory
argument_list|(
name|getProjectDirectory
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ProjectException
argument_list|(
literal|"Error creating DataSourceFactory."
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

