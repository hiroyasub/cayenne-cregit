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
name|Iterator
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
name|DataNodeConfigInfo
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
name|ProjectConfigInfo
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
name|ProjectConfigurator
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
name|ProjectException
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
name|Task
import|;
end_import

begin_comment
comment|/**  * A "cdeploy" Ant task providing an Ant frontend to  * org.apache.cayenne.project.ProjectConfigurator.  *   * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|DeploymentConfigurator
extends|extends
name|CayenneTask
block|{
specifier|protected
name|ProjectConfigInfo
name|info
decl_stmt|;
comment|/**      * Constructor for DeploymentConfigurator.      */
specifier|public
name|DeploymentConfigurator
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
name|info
operator|=
operator|new
name|ProjectConfigInfo
argument_list|()
expr_stmt|;
block|}
specifier|public
name|ProjectConfigInfo
name|getInfo
parameter_list|()
block|{
return|return
name|info
return|;
block|}
comment|/**      * Executes the task. It will be called by ant framework.      */
specifier|public
name|void
name|execute
parameter_list|()
throws|throws
name|BuildException
block|{
name|validateAttributes
argument_list|()
expr_stmt|;
try|try
block|{
name|processProject
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|Throwable
name|th
init|=
name|Util
operator|.
name|unwindException
argument_list|(
name|ex
argument_list|)
decl_stmt|;
name|String
name|message
init|=
name|th
operator|.
name|getMessage
argument_list|()
decl_stmt|;
name|StringBuffer
name|buf
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
if|if
condition|(
name|message
operator|!=
literal|null
operator|&&
name|message
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
literal|"Error: ["
argument_list|)
operator|.
name|append
argument_list|(
name|message
argument_list|)
operator|.
name|append
argument_list|(
literal|"]."
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|buf
operator|.
name|append
argument_list|(
literal|"Error reconfiguring jar file."
argument_list|)
expr_stmt|;
block|}
name|buf
operator|.
name|append
argument_list|(
literal|" Source: "
argument_list|)
operator|.
name|append
argument_list|(
name|info
operator|.
name|getSourceJar
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"; target: "
argument_list|)
operator|.
name|append
argument_list|(
name|info
operator|.
name|getDestJar
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|errorMessage
init|=
name|buf
operator|.
name|toString
argument_list|()
decl_stmt|;
name|super
operator|.
name|log
argument_list|(
name|errorMessage
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|BuildException
argument_list|(
name|errorMessage
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
comment|/**      * Performs validation of task attributes. Throws BuildException if validation fails.      */
specifier|protected
name|void
name|validateAttributes
parameter_list|()
throws|throws
name|BuildException
block|{
if|if
condition|(
name|info
operator|.
name|getSourceJar
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"'src' attribute is required."
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|info
operator|.
name|getSourceJar
argument_list|()
operator|.
name|isFile
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"'src' must be a valid file: "
operator|+
name|info
operator|.
name|getSourceJar
argument_list|()
argument_list|)
throw|;
block|}
if|if
condition|(
name|info
operator|.
name|getAltProjectFile
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|info
operator|.
name|getAltProjectFile
argument_list|()
operator|.
name|isFile
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"'altProjectFile' must be a valid file: "
operator|+
name|info
operator|.
name|getAltProjectFile
argument_list|()
argument_list|)
throw|;
block|}
name|Iterator
name|nodes
init|=
name|info
operator|.
name|getNodes
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|nodes
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|DataNodeConfigInfo
name|node
init|=
operator|(
name|DataNodeConfigInfo
operator|)
name|nodes
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|node
operator|.
name|getName
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"'node.name' attribute is required."
argument_list|)
throw|;
block|}
if|if
condition|(
name|node
operator|.
name|getDataSource
argument_list|()
operator|!=
literal|null
operator|&&
name|node
operator|.
name|getDriverFile
argument_list|()
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"'node.dataSource' and 'node.driverFile' are mutually exclusive."
argument_list|)
throw|;
block|}
if|if
condition|(
name|node
operator|.
name|getDriverFile
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|node
operator|.
name|getDriverFile
argument_list|()
operator|.
name|isFile
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"'node.driverFile' does not exist."
argument_list|)
throw|;
block|}
block|}
block|}
comment|/**      * Performs the actual work on the project.      */
specifier|protected
name|void
name|processProject
parameter_list|()
throws|throws
name|ProjectException
block|{
name|ProjectConfigurator
name|conf
init|=
operator|new
name|ProjectConfigurator
argument_list|(
name|info
argument_list|)
decl_stmt|;
name|conf
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|setSrc
parameter_list|(
name|File
name|file
parameter_list|)
block|{
name|info
operator|.
name|setSourceJar
argument_list|(
name|file
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setDest
parameter_list|(
name|File
name|file
parameter_list|)
block|{
name|info
operator|.
name|setDestJar
argument_list|(
name|file
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setAltProjectFile
parameter_list|(
name|File
name|file
parameter_list|)
block|{
name|info
operator|.
name|setAltProjectFile
argument_list|(
name|file
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addNode
parameter_list|(
name|DataNodeConfigInfo
name|node
parameter_list|)
block|{
name|info
operator|.
name|addToNodes
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

