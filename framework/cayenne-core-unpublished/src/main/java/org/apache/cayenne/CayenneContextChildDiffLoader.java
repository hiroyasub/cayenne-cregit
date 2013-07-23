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
name|graph
operator|.
name|ChildDiffLoader
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
name|reflect
operator|.
name|ArcProperty
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
name|reflect
operator|.
name|AttributeProperty
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
name|reflect
operator|.
name|ClassDescriptor
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
name|reflect
operator|.
name|PropertyDescriptor
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
name|reflect
operator|.
name|PropertyVisitor
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
name|reflect
operator|.
name|ToManyProperty
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
name|reflect
operator|.
name|ToOneProperty
import|;
end_import

begin_comment
comment|/**  * Used for loading child's CayenneContext changes to parent context.  *   * @since 3.0  */
end_comment

begin_class
class|class
name|CayenneContextChildDiffLoader
extends|extends
name|ChildDiffLoader
block|{
specifier|public
name|CayenneContextChildDiffLoader
parameter_list|(
name|CayenneContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|nodePropertyChanged
parameter_list|(
name|Object
name|nodeId
parameter_list|,
name|String
name|property
parameter_list|,
name|Object
name|oldValue
parameter_list|,
name|Object
name|newValue
parameter_list|)
block|{
name|super
operator|.
name|nodePropertyChanged
argument_list|(
name|nodeId
argument_list|,
name|property
argument_list|,
name|oldValue
argument_list|,
name|newValue
argument_list|)
expr_stmt|;
name|Persistent
name|object
init|=
operator|(
name|Persistent
operator|)
name|context
operator|.
name|getGraphManager
argument_list|()
operator|.
name|getNode
argument_list|(
name|nodeId
argument_list|)
decl_stmt|;
name|context
operator|.
name|propertyChanged
argument_list|(
name|object
argument_list|,
name|property
argument_list|,
name|oldValue
argument_list|,
name|newValue
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|arcCreated
parameter_list|(
name|Object
name|nodeId
parameter_list|,
name|Object
name|targetNodeId
parameter_list|,
name|Object
name|arcId
parameter_list|)
block|{
specifier|final
name|Persistent
name|source
init|=
name|findObject
argument_list|(
name|nodeId
argument_list|)
decl_stmt|;
specifier|final
name|Persistent
name|target
init|=
name|findObject
argument_list|(
name|targetNodeId
argument_list|)
decl_stmt|;
comment|// if a target was later deleted, the diff for arcCreated is still preserved and
comment|// can result in NULL target here.
if|if
condition|(
name|target
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|ClassDescriptor
name|descriptor
init|=
name|context
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getClassDescriptor
argument_list|(
operator|(
operator|(
name|ObjectId
operator|)
name|nodeId
operator|)
operator|.
name|getEntityName
argument_list|()
argument_list|)
decl_stmt|;
name|ArcProperty
name|property
init|=
operator|(
name|ArcProperty
operator|)
name|descriptor
operator|.
name|getProperty
argument_list|(
name|arcId
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|property
operator|.
name|visit
argument_list|(
operator|new
name|PropertyVisitor
argument_list|()
block|{
specifier|public
name|boolean
name|visitAttribute
parameter_list|(
name|AttributeProperty
name|property
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|boolean
name|visitToMany
parameter_list|(
name|ToManyProperty
name|property
parameter_list|)
block|{
name|property
operator|.
name|addTargetDirectly
argument_list|(
name|source
argument_list|,
name|target
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
specifier|public
name|boolean
name|visitToOne
parameter_list|(
name|ToOneProperty
name|property
parameter_list|)
block|{
name|property
operator|.
name|setTarget
argument_list|(
name|source
argument_list|,
name|target
argument_list|,
literal|false
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|propertyChanged
argument_list|(
name|source
argument_list|,
operator|(
name|String
operator|)
name|arcId
argument_list|,
literal|null
argument_list|,
name|target
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|arcDeleted
parameter_list|(
name|Object
name|nodeId
parameter_list|,
specifier|final
name|Object
name|targetNodeId
parameter_list|,
name|Object
name|arcId
parameter_list|)
block|{
specifier|final
name|Persistent
name|source
init|=
name|findObject
argument_list|(
name|nodeId
argument_list|)
decl_stmt|;
comment|// needed as sometime temporary objects are evoked from the context before
comment|// changing their relationships
if|if
condition|(
name|source
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|ClassDescriptor
name|descriptor
init|=
name|context
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getClassDescriptor
argument_list|(
operator|(
operator|(
name|ObjectId
operator|)
name|nodeId
operator|)
operator|.
name|getEntityName
argument_list|()
argument_list|)
decl_stmt|;
name|PropertyDescriptor
name|property
init|=
name|descriptor
operator|.
name|getProperty
argument_list|(
name|arcId
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|Persistent
index|[]
name|target
init|=
operator|new
name|Persistent
index|[
literal|1
index|]
decl_stmt|;
name|target
index|[
literal|0
index|]
operator|=
name|findObject
argument_list|(
name|targetNodeId
argument_list|)
expr_stmt|;
name|property
operator|.
name|visit
argument_list|(
operator|new
name|PropertyVisitor
argument_list|()
block|{
specifier|public
name|boolean
name|visitAttribute
parameter_list|(
name|AttributeProperty
name|property
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|boolean
name|visitToMany
parameter_list|(
name|ToManyProperty
name|property
parameter_list|)
block|{
if|if
condition|(
name|target
index|[
literal|0
index|]
operator|==
literal|null
condition|)
block|{
comment|// this is usually the case when a NEW object was deleted and then
comment|// its relationships were manipulated; so try to locate the object
comment|// in the collection ... the performance of this is rather dubious
comment|// of course...
name|target
index|[
literal|0
index|]
operator|=
name|findObjectInCollection
argument_list|(
name|targetNodeId
argument_list|,
name|property
operator|.
name|readProperty
argument_list|(
name|source
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|target
index|[
literal|0
index|]
operator|==
literal|null
condition|)
block|{
comment|// ignore?
block|}
else|else
block|{
name|property
operator|.
name|removeTargetDirectly
argument_list|(
name|source
argument_list|,
name|target
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
return|return
literal|false
return|;
block|}
specifier|public
name|boolean
name|visitToOne
parameter_list|(
name|ToOneProperty
name|property
parameter_list|)
block|{
name|property
operator|.
name|setTarget
argument_list|(
name|source
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|propertyChanged
argument_list|(
name|source
argument_list|,
operator|(
name|String
operator|)
name|arcId
argument_list|,
name|target
index|[
literal|0
index|]
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
