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

begin_comment
comment|/**  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|TstProjectTraversalHelper
implements|implements
name|ProjectTraversalHandler
block|{
specifier|protected
name|List
name|nodes
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
comment|/**      * Constructor for TstProjectTraversalHelper.      */
specifier|public
name|TstProjectTraversalHelper
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
comment|/**      * @see org.apache.cayenne.project.ProjectTraversalHandler#projectNode(Object[])      */
specifier|public
name|void
name|projectNode
parameter_list|(
name|ProjectPath
name|nodePath
parameter_list|)
block|{
name|nodes
operator|.
name|add
argument_list|(
name|nodePath
operator|.
name|getObject
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * @see org.apache.cayenne.project.ProjectTraversalHandler#shouldReadChildren(Object, Object[])      */
specifier|public
name|boolean
name|shouldReadChildren
parameter_list|(
name|Object
name|node
parameter_list|,
name|ProjectPath
name|parentPath
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
comment|/**      * Returns the nodes.      * @return List      */
specifier|public
name|List
name|getNodes
parameter_list|()
block|{
return|return
name|nodes
return|;
block|}
block|}
end_class

end_unit

