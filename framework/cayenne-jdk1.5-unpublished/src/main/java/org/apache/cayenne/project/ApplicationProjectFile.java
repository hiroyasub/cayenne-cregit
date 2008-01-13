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
name|PrintWriter
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
name|ConfigSaver
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
name|RuntimeSaveDelegate
import|;
end_import

begin_comment
comment|/**  * ApplicationProjectFile is a ProjectFile abstraction of the main project file in a  * Cayenne project. Right now Cayenne projects can not be renamed, so all the name  * tracking functionality is pretty much noop.  *   * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|ApplicationProjectFile
extends|extends
name|ProjectFile
block|{
specifier|protected
name|ConfigSaverDelegate
name|saveDelegate
decl_stmt|;
specifier|private
name|String
name|objectName
init|=
literal|null
decl_stmt|;
specifier|private
name|ApplicationProjectFile
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
comment|/**      * Constructor for default ApplicationProjectFile.      */
specifier|public
name|ApplicationProjectFile
parameter_list|(
name|Project
name|project
parameter_list|)
block|{
name|this
argument_list|(
name|project
argument_list|,
name|Configuration
operator|.
name|DEFAULT_DOMAIN_FILE
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor for ApplicationProjectFile with an existing file.      */
specifier|public
name|ApplicationProjectFile
parameter_list|(
name|Project
name|project
parameter_list|,
name|String
name|fileName
parameter_list|)
block|{
name|super
argument_list|(
name|project
argument_list|,
name|fileName
argument_list|)
expr_stmt|;
name|this
operator|.
name|objectName
operator|=
name|fileName
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|fileName
operator|.
name|lastIndexOf
argument_list|(
name|this
operator|.
name|getLocationSuffix
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns suffix to append to object name when creating a file name. Default      * implementation returns empty string.      */
annotation|@
name|Override
specifier|public
name|String
name|getLocationSuffix
parameter_list|()
block|{
return|return
literal|".xml"
return|;
block|}
comment|/**      * Returns a project.      */
annotation|@
name|Override
specifier|public
name|Object
name|getObject
parameter_list|()
block|{
return|return
name|getProject
argument_list|()
return|;
block|}
comment|/**      * @see org.apache.cayenne.project.ProjectFile#getObjectName()      */
annotation|@
name|Override
specifier|public
name|String
name|getObjectName
parameter_list|()
block|{
return|return
name|this
operator|.
name|objectName
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|save
parameter_list|(
name|PrintWriter
name|out
parameter_list|)
throws|throws
name|Exception
block|{
name|ConfigSaverDelegate
name|localDelegate
init|=
operator|(
name|saveDelegate
operator|!=
literal|null
operator|)
condition|?
name|saveDelegate
else|:
operator|new
name|RuntimeSaveDelegate
argument_list|(
operator|(
operator|(
name|ApplicationProject
operator|)
name|projectObj
operator|)
operator|.
name|getConfiguration
argument_list|()
argument_list|)
decl_stmt|;
operator|new
name|ConfigSaver
argument_list|(
name|localDelegate
argument_list|)
operator|.
name|storeDomains
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|canHandle
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
return|return
name|obj
operator|instanceof
name|ApplicationProject
return|;
block|}
comment|/**      * Returns the saveDelegate.      *       * @return ConfigSaverDelegate      */
specifier|public
name|ConfigSaverDelegate
name|getSaveDelegate
parameter_list|()
block|{
return|return
name|saveDelegate
return|;
block|}
comment|/**      * Sets the saveDelegate.      *       * @param saveDelegate The saveDelegate to set      */
specifier|public
name|void
name|setSaveDelegate
parameter_list|(
name|ConfigSaverDelegate
name|saveDelegate
parameter_list|)
block|{
name|this
operator|.
name|saveDelegate
operator|=
name|saveDelegate
expr_stmt|;
block|}
block|}
end_class

end_unit

