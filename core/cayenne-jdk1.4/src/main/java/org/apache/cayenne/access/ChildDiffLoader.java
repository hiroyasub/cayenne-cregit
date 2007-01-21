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
name|access
package|;
end_package

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
name|CayenneRuntimeException
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
name|ObjectContext
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
name|ObjectId
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
name|Persistent
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
name|QueryResponse
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
name|graph
operator|.
name|GraphChangeHandler
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
name|query
operator|.
name|ObjectIdQuery
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
name|query
operator|.
name|Query
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
name|Property
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
comment|/**  * A GraphChangeHandler that loads child ObjectContext diffs into a parent DataContext.  * Graph node ids are expected to be ObjectIds.  *   * @since 1.2  * @author Andrus Adamchik  */
end_comment

begin_class
class|class
name|ChildDiffLoader
implements|implements
name|GraphChangeHandler
block|{
name|ObjectContext
name|context
decl_stmt|;
name|ChildDiffLoader
parameter_list|(
name|ObjectContext
name|context
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
block|}
specifier|public
name|void
name|nodeIdChanged
parameter_list|(
name|Object
name|nodeId
parameter_list|,
name|Object
name|newId
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Not supported"
argument_list|)
throw|;
block|}
specifier|public
name|void
name|nodeCreated
parameter_list|(
name|Object
name|nodeId
parameter_list|)
block|{
name|ObjectId
name|id
init|=
operator|(
name|ObjectId
operator|)
name|nodeId
decl_stmt|;
if|if
condition|(
name|id
operator|.
name|getEntityName
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"Null entity name in id "
operator|+
name|id
argument_list|)
throw|;
block|}
name|ObjEntity
name|entity
init|=
name|context
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|lookupObjEntity
argument_list|(
name|id
operator|.
name|getEntityName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|entity
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Entity not mapped with Cayenne: "
operator|+
name|id
argument_list|)
throw|;
block|}
name|Persistent
name|dataObject
init|=
literal|null
decl_stmt|;
try|try
block|{
name|dataObject
operator|=
operator|(
name|Persistent
operator|)
name|entity
operator|.
name|getJavaClass
argument_list|()
operator|.
name|newInstance
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
name|CayenneRuntimeException
argument_list|(
literal|"Error instantiating object."
argument_list|,
name|ex
argument_list|)
throw|;
block|}
name|dataObject
operator|.
name|setObjectId
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|context
operator|.
name|registerNewObject
argument_list|(
name|dataObject
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|nodeRemoved
parameter_list|(
name|Object
name|nodeId
parameter_list|)
block|{
name|context
operator|.
name|deleteObject
argument_list|(
name|findObject
argument_list|(
name|nodeId
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
comment|// this change is for simple property, so no need to convert targets to server
comment|// objects...
name|Persistent
name|object
init|=
name|findObject
argument_list|(
name|nodeId
argument_list|)
decl_stmt|;
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
try|try
block|{
name|descriptor
operator|.
name|getProperty
argument_list|(
name|property
argument_list|)
operator|.
name|writeProperty
argument_list|(
name|object
argument_list|,
literal|null
argument_list|,
name|newValue
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Error setting property: "
operator|+
name|property
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
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
name|Property
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
name|addTarget
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
block|}
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
name|Property
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
name|Persistent
name|target
init|=
name|findObject
argument_list|(
name|targetNodeId
argument_list|)
decl_stmt|;
name|property
operator|.
name|removeTarget
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
block|}
name|Persistent
name|findObject
parameter_list|(
name|Object
name|nodeId
parameter_list|)
block|{
comment|// first do a lookup in ObjectStore; if even a hollow object is found, return it;
comment|// if not - fetch.
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
if|if
condition|(
name|object
operator|!=
literal|null
condition|)
block|{
return|return
name|object
return|;
block|}
comment|// skip context cache lookup, go directly to its channel
name|Query
name|query
init|=
operator|new
name|ObjectIdQuery
argument_list|(
operator|(
name|ObjectId
operator|)
name|nodeId
argument_list|)
decl_stmt|;
name|QueryResponse
name|response
init|=
name|context
operator|.
name|getChannel
argument_list|()
operator|.
name|onQuery
argument_list|(
name|context
argument_list|,
name|query
argument_list|)
decl_stmt|;
name|List
name|objects
init|=
name|response
operator|.
name|firstList
argument_list|()
decl_stmt|;
if|if
condition|(
name|objects
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"No object for ID exists: "
operator|+
name|nodeId
argument_list|)
throw|;
block|}
if|else if
condition|(
name|objects
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Expected zero or one object, instead query matched: "
operator|+
name|objects
operator|.
name|size
argument_list|()
argument_list|)
throw|;
block|}
return|return
operator|(
name|Persistent
operator|)
name|objects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
block|}
end_class

end_unit

