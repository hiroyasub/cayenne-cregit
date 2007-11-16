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
name|java
operator|.
name|util
operator|.
name|Collection
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
name|graph
operator|.
name|GraphManager
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
name|EntityResolver
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
name|Property
import|;
end_import

begin_comment
comment|/**  * A common base superclass for Cayenne ObjectContext implementors.  *   * @since 3.0  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|BaseContext
implements|implements
name|ObjectContext
block|{
comment|// if we are to pass CayenneContext around, channel should be left alone and
comment|// reinjected later if needed
specifier|protected
specifier|transient
name|DataChannel
name|channel
decl_stmt|;
specifier|public
specifier|abstract
name|void
name|commitChanges
parameter_list|()
function_decl|;
specifier|public
specifier|abstract
name|void
name|commitChangesToParent
parameter_list|()
function_decl|;
specifier|public
specifier|abstract
name|void
name|deleteObject
parameter_list|(
name|Object
name|object
parameter_list|)
throws|throws
name|DeleteDenyException
function_decl|;
specifier|public
specifier|abstract
name|Collection
name|deletedObjects
parameter_list|()
function_decl|;
specifier|public
name|DataChannel
name|getChannel
parameter_list|()
block|{
return|return
name|channel
return|;
block|}
specifier|public
specifier|abstract
name|EntityResolver
name|getEntityResolver
parameter_list|()
function_decl|;
specifier|public
specifier|abstract
name|GraphManager
name|getGraphManager
parameter_list|()
function_decl|;
specifier|public
specifier|abstract
name|Persistent
name|localObject
parameter_list|(
name|ObjectId
name|id
parameter_list|,
name|Object
name|prototype
parameter_list|)
function_decl|;
specifier|public
specifier|abstract
name|Collection
name|modifiedObjects
parameter_list|()
function_decl|;
specifier|public
specifier|abstract
parameter_list|<
name|T
parameter_list|>
name|T
name|newObject
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|persistentClass
parameter_list|)
function_decl|;
specifier|public
specifier|abstract
name|void
name|registerNewObject
parameter_list|(
name|Object
name|object
parameter_list|)
function_decl|;
specifier|public
specifier|abstract
name|Collection
name|newObjects
parameter_list|()
function_decl|;
specifier|public
specifier|abstract
name|QueryResponse
name|performGenericQuery
parameter_list|(
name|Query
name|query
parameter_list|)
function_decl|;
specifier|public
specifier|abstract
name|List
name|performQuery
parameter_list|(
name|Query
name|query
parameter_list|)
function_decl|;
comment|/**      * @deprecated since 3.0 this method is replaced by      *             {@link #prepareForAccess(Persistent, String, boolean)}.      */
specifier|public
name|void
name|prepareForAccess
parameter_list|(
name|Persistent
name|object
parameter_list|,
name|String
name|property
parameter_list|)
block|{
name|prepareForAccess
argument_list|(
name|object
argument_list|,
name|property
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|prepareForAccess
parameter_list|(
name|Persistent
name|object
parameter_list|,
name|String
name|property
parameter_list|,
name|boolean
name|lazyFaulting
parameter_list|)
block|{
if|if
condition|(
name|object
operator|.
name|getPersistenceState
argument_list|()
operator|==
name|PersistenceState
operator|.
name|HOLLOW
condition|)
block|{
name|ObjectId
name|oid
init|=
name|object
operator|.
name|getObjectId
argument_list|()
decl_stmt|;
name|List
name|objects
init|=
name|performQuery
argument_list|(
operator|new
name|ObjectIdQuery
argument_list|(
name|oid
argument_list|,
literal|false
argument_list|,
name|ObjectIdQuery
operator|.
name|CACHE
argument_list|)
argument_list|)
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
name|FaultFailureException
argument_list|(
literal|"Error resolving fault, no matching row exists in the database for ObjectId: "
operator|+
name|oid
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
name|FaultFailureException
argument_list|(
literal|"Error resolving fault, more than one row exists in the database for ObjectId: "
operator|+
name|oid
argument_list|)
throw|;
block|}
comment|// sanity check...
if|if
condition|(
name|object
operator|.
name|getPersistenceState
argument_list|()
operator|!=
name|PersistenceState
operator|.
name|COMMITTED
condition|)
block|{
name|String
name|state
init|=
name|PersistenceState
operator|.
name|persistenceStateName
argument_list|(
name|object
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
decl_stmt|;
comment|// TODO: andrus 4/13/2006, modified and deleted states are possible due to
comment|// a race condition, should we handle them here?
throw|throw
operator|new
name|FaultFailureException
argument_list|(
literal|"Error resolving fault for ObjectId: "
operator|+
name|oid
operator|+
literal|" and state ("
operator|+
name|state
operator|+
literal|"). Possible cause - matching row is missing from the database."
argument_list|)
throw|;
block|}
block|}
comment|// resolve relationship fault
if|if
condition|(
name|lazyFaulting
operator|&&
name|property
operator|!=
literal|null
condition|)
block|{
name|Property
name|propertyDescriptor
init|=
name|getEntityResolver
argument_list|()
operator|.
name|getClassDescriptor
argument_list|(
name|object
operator|.
name|getObjectId
argument_list|()
operator|.
name|getEntityName
argument_list|()
argument_list|)
operator|.
name|getProperty
argument_list|(
name|property
argument_list|)
decl_stmt|;
comment|// this should trigger fault resolving
name|propertyDescriptor
operator|.
name|readProperty
argument_list|(
name|object
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|abstract
name|void
name|propertyChanged
parameter_list|(
name|Persistent
name|object
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
function_decl|;
specifier|public
specifier|abstract
name|void
name|rollbackChanges
parameter_list|()
function_decl|;
specifier|public
specifier|abstract
name|void
name|rollbackChangesLocally
parameter_list|()
function_decl|;
specifier|public
specifier|abstract
name|Collection
name|uncommittedObjects
parameter_list|()
function_decl|;
block|}
end_class

end_unit

