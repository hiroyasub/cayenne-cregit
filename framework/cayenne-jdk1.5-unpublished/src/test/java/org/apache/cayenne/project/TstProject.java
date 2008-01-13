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

begin_comment
comment|/**  * Concrete subclass of Project used for testing purposes.  *   * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|TstProject
extends|extends
name|Project
block|{
comment|/**      * Constructor for TstProject.      * @param name      * @param projectFile      */
specifier|public
name|TstProject
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
comment|/**      * @see org.apache.cayenne.project.Project#checkForUpgrades()      */
annotation|@
name|Override
specifier|public
name|void
name|checkForUpgrades
parameter_list|()
block|{
block|}
comment|/**      * @see org.apache.cayenne.project.Project#treeNodes()      */
annotation|@
name|Override
specifier|public
name|Iterator
name|treeNodes
parameter_list|()
block|{
return|return
operator|new
name|ArrayList
argument_list|()
operator|.
name|iterator
argument_list|()
return|;
block|}
comment|/**      * @see org.apache.cayenne.project.Project#getChildren()      */
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
argument_list|()
return|;
block|}
comment|/**      * @see org.apache.cayenne.project.Project#projectFileForObject(Object)      */
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
return|return
literal|null
return|;
block|}
comment|/**      * @see org.apache.cayenne.project.Project#projectLoadStatus()      */
annotation|@
name|Override
specifier|public
name|ConfigStatus
name|getLoadStatus
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|upgrade
parameter_list|()
throws|throws
name|ProjectException
block|{
block|}
block|}
end_class

end_unit

