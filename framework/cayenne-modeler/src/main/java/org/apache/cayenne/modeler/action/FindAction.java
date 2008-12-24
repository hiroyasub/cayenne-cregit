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
name|modeler
operator|.
name|action
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Color
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|event
operator|.
name|ActionEvent
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
name|regex
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JButton
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JTextField
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|tree
operator|.
name|TreePath
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
name|map
operator|.
name|Attribute
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
name|DbAttribute
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
name|map
operator|.
name|DbRelationship
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
name|Entity
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
name|ObjAttribute
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
name|ObjEntity
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
name|ObjRelationship
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
name|Relationship
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
name|modeler
operator|.
name|Application
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
name|modeler
operator|.
name|CayenneModelerFrame
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
name|modeler
operator|.
name|dialog
operator|.
name|FindDialog
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
name|modeler
operator|.
name|editor
operator|.
name|EditorView
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
name|modeler
operator|.
name|util
operator|.
name|CayenneAction
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
name|modeler
operator|.
name|util
operator|.
name|CayenneController
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
name|ProjectPath
import|;
end_import

begin_class
specifier|public
class|class
name|FindAction
extends|extends
name|CayenneAction
block|{
specifier|private
name|java
operator|.
name|util
operator|.
name|List
name|paths
decl_stmt|;
specifier|public
specifier|static
name|String
name|getActionName
parameter_list|()
block|{
return|return
literal|"Find"
return|;
block|}
specifier|public
name|FindAction
parameter_list|(
name|Application
name|application
parameter_list|)
block|{
name|super
argument_list|(
name|getActionName
argument_list|()
argument_list|,
name|application
argument_list|)
expr_stmt|;
block|}
comment|/**      * All entities that contain a pattern substring (case-indifferent) in the name are produced.      * @param e      */
specifier|public
name|void
name|performAction
parameter_list|(
name|ActionEvent
name|e
parameter_list|)
block|{
name|JTextField
name|source
init|=
operator|(
name|JTextField
operator|)
name|e
operator|.
name|getSource
argument_list|()
decl_stmt|;
name|paths
operator|=
operator|new
name|ArrayList
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|source
operator|.
name|getText
argument_list|()
operator|.
name|trim
argument_list|()
operator|.
name|equals
argument_list|(
literal|""
argument_list|)
condition|)
block|{
name|Pattern
name|pattern
init|=
name|Pattern
operator|.
name|compile
argument_list|(
name|source
operator|.
name|getText
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|,
name|Pattern
operator|.
name|CASE_INSENSITIVE
argument_list|)
decl_stmt|;
name|Iterator
name|it
init|=
name|getProjectController
argument_list|()
operator|.
name|getProject
argument_list|()
operator|.
name|treeNodes
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
name|ProjectPath
name|path
init|=
operator|(
name|ProjectPath
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|Object
name|o
init|=
name|path
operator|.
name|getObject
argument_list|()
decl_stmt|;
if|if
condition|(
operator|(
name|o
operator|instanceof
name|ObjEntity
operator|||
name|o
operator|instanceof
name|DbEntity
operator|)
operator|&&
name|matchFound
argument_list|(
operator|(
operator|(
name|Entity
operator|)
name|o
operator|)
operator|.
name|getName
argument_list|()
argument_list|,
name|pattern
argument_list|)
condition|)
name|paths
operator|.
name|add
argument_list|(
name|path
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
if|else if
condition|(
name|o
operator|instanceof
name|Attribute
operator|&&
name|matchFound
argument_list|(
operator|(
operator|(
name|Attribute
operator|)
name|o
operator|)
operator|.
name|getName
argument_list|()
argument_list|,
name|pattern
argument_list|)
condition|)
name|paths
operator|.
name|add
argument_list|(
name|path
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
if|else if
condition|(
name|o
operator|instanceof
name|Relationship
operator|&&
name|matchFound
argument_list|(
operator|(
operator|(
name|Relationship
operator|)
name|o
operator|)
operator|.
name|getName
argument_list|()
argument_list|,
name|pattern
argument_list|)
condition|)
name|paths
operator|.
name|add
argument_list|(
name|path
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|paths
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
name|source
operator|.
name|setBackground
argument_list|(
name|Color
operator|.
name|pink
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|paths
operator|.
name|size
argument_list|()
operator|!=
literal|1
condition|)
block|{
operator|new
name|FindDialog
argument_list|(
name|getApplication
argument_list|()
operator|.
name|getFrameController
argument_list|()
argument_list|,
name|paths
argument_list|)
operator|.
name|startupAction
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|Iterator
name|it
init|=
name|paths
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|int
name|index
init|=
literal|0
decl_stmt|;
if|if
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Object
index|[]
name|path
init|=
operator|(
name|Object
index|[]
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|EditorView
name|editor
init|=
operator|(
operator|(
name|CayenneModelerFrame
operator|)
name|getApplication
argument_list|()
operator|.
name|getFrameController
argument_list|()
operator|.
name|getView
argument_list|()
operator|)
operator|.
name|getView
argument_list|()
decl_stmt|;
name|FindDialog
operator|.
name|jumpToResult
argument_list|(
name|path
argument_list|,
name|editor
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|boolean
name|matchFound
parameter_list|(
name|String
name|entityName
parameter_list|,
name|Pattern
name|pattern
parameter_list|)
block|{
name|Matcher
name|m
init|=
name|pattern
operator|.
name|matcher
argument_list|(
name|entityName
argument_list|)
decl_stmt|;
return|return
name|m
operator|.
name|find
argument_list|()
return|;
block|}
block|}
end_class

end_unit

