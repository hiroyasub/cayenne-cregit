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
name|tools
package|;
end_package

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
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|groovy
operator|.
name|runtime
operator|.
name|ResourceGroovyMethods
import|;
end_import

begin_import
import|import
name|org
operator|.
name|gradle
operator|.
name|api
operator|.
name|GradleException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|gradle
operator|.
name|api
operator|.
name|Project
import|;
end_import

begin_import
import|import
name|org
operator|.
name|gradle
operator|.
name|api
operator|.
name|artifacts
operator|.
name|Dependency
import|;
end_import

begin_import
import|import
name|org
operator|.
name|gradle
operator|.
name|api
operator|.
name|artifacts
operator|.
name|dsl
operator|.
name|DependencyHandler
import|;
end_import

begin_comment
comment|/**  * Cayenne DSL for gradle  *<p>  * - dependency management  * - utility methods  *  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|GradleCayenneExtension
block|{
specifier|public
specifier|static
specifier|final
name|String
name|GROUP
init|=
literal|"org.apache.cayenne"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|VERSION_FILE
init|=
literal|"/cayenne.version"
decl_stmt|;
specifier|private
name|String
name|version
decl_stmt|;
specifier|private
specifier|final
name|DependencyHandler
name|dependencies
decl_stmt|;
comment|/**      * Shortcut for the cgen task.      * Can be used in defining additional tasks like:      *<pre>{@code      * task customCgen(type: cayenne.cgen) {      *     //...      * }      * }</pre>      */
specifier|private
specifier|final
name|Class
argument_list|<
name|CgenTask
argument_list|>
name|cgen
init|=
name|CgenTask
operator|.
name|class
decl_stmt|;
comment|/**      * Shortcut for the cdbimport task.      * Can be used in defining additional tasks like:      *<pre>{@code      * task customCdbimport(type: cayenne.cdbimport) {      *     //...      * }      * }</pre>      */
specifier|private
specifier|final
name|Class
argument_list|<
name|DbImportTask
argument_list|>
name|cdbimport
init|=
name|DbImportTask
operator|.
name|class
decl_stmt|;
comment|/**      * Shortcut for the cdbgen task.      * Can be used in defining additional tasks like:      *<pre>{@code      * task customCdbgen(type: cayenne.cdbgen) {      *     //...      * }      * }</pre>      */
specifier|private
specifier|final
name|Class
argument_list|<
name|DbGenerateTask
argument_list|>
name|cdbgen
init|=
name|DbGenerateTask
operator|.
name|class
decl_stmt|;
comment|/**      * Default data map that will be used in all tasks.      * Can be overridden per task.      */
specifier|private
name|String
name|defaultDataMap
decl_stmt|;
specifier|public
name|GradleCayenneExtension
parameter_list|(
name|Project
name|project
parameter_list|)
block|{
name|this
operator|.
name|dependencies
operator|=
name|project
operator|.
name|getDependencies
argument_list|()
expr_stmt|;
try|try
block|{
name|readVersion
argument_list|(
name|project
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|GradleException
argument_list|(
literal|"Cayenne version not found"
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|private
name|void
name|readVersion
parameter_list|(
name|Project
name|project
parameter_list|)
throws|throws
name|IOException
block|{
name|URL
name|versionFileUrl
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
name|VERSION_FILE
argument_list|)
decl_stmt|;
if|if
condition|(
name|versionFileUrl
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|version
operator|=
name|project
operator|.
name|getVersion
argument_list|()
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|version
operator|=
name|ResourceGroovyMethods
operator|.
name|getText
argument_list|(
name|versionFileUrl
argument_list|)
operator|.
name|trim
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|Dependency
name|dependency
parameter_list|(
specifier|final
name|String
name|name
parameter_list|)
block|{
return|return
name|dependencies
operator|.
name|create
argument_list|(
name|GROUP
operator|+
literal|":cayenne-"
operator|+
name|name
operator|+
literal|":"
operator|+
name|version
argument_list|)
return|;
block|}
specifier|public
name|String
name|getDefaultDataMap
parameter_list|()
block|{
return|return
name|defaultDataMap
return|;
block|}
specifier|public
name|void
name|setDefaultDataMap
parameter_list|(
name|String
name|defaultDataMap
parameter_list|)
block|{
name|this
operator|.
name|defaultDataMap
operator|=
name|defaultDataMap
expr_stmt|;
block|}
specifier|public
name|Class
argument_list|<
name|CgenTask
argument_list|>
name|getCgen
parameter_list|()
block|{
return|return
name|cgen
return|;
block|}
specifier|public
name|Class
argument_list|<
name|DbImportTask
argument_list|>
name|getCdbimport
parameter_list|()
block|{
return|return
name|cdbimport
return|;
block|}
specifier|public
name|Class
argument_list|<
name|DbGenerateTask
argument_list|>
name|getCdbgen
parameter_list|()
block|{
return|return
name|cdbgen
return|;
block|}
specifier|public
name|String
name|getVersion
parameter_list|()
block|{
return|return
name|version
return|;
block|}
block|}
end_class

end_unit

