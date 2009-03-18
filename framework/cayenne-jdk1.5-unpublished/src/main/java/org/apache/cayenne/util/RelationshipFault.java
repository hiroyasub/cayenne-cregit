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
name|util
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
name|PersistenceState
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
name|query
operator|.
name|RelationshipQuery
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
comment|/**  * An abstract superclass of lazily faulted to-one and to-many relationships.  *   * @since 1.2  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|RelationshipFault
block|{
specifier|protected
name|Persistent
name|relationshipOwner
decl_stmt|;
specifier|protected
name|String
name|relationshipName
decl_stmt|;
specifier|protected
name|RelationshipFault
parameter_list|()
block|{
block|}
specifier|public
name|RelationshipFault
parameter_list|(
name|Persistent
name|relationshipOwner
parameter_list|,
name|String
name|relationshipName
parameter_list|)
block|{
if|if
condition|(
name|relationshipOwner
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"'relationshipOwner' can't be null."
argument_list|)
throw|;
block|}
if|if
condition|(
name|relationshipName
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"'relationshipName' can't be null."
argument_list|)
throw|;
block|}
name|this
operator|.
name|relationshipOwner
operator|=
name|relationshipOwner
expr_stmt|;
name|this
operator|.
name|relationshipName
operator|=
name|relationshipName
expr_stmt|;
block|}
specifier|public
name|String
name|getRelationshipName
parameter_list|()
block|{
return|return
name|relationshipName
return|;
block|}
specifier|public
name|Persistent
name|getRelationshipOwner
parameter_list|()
block|{
return|return
name|relationshipOwner
return|;
block|}
specifier|protected
name|boolean
name|isTransientParent
parameter_list|()
block|{
name|int
name|state
init|=
name|relationshipOwner
operator|.
name|getPersistenceState
argument_list|()
decl_stmt|;
return|return
name|state
operator|==
name|PersistenceState
operator|.
name|NEW
operator|||
name|state
operator|==
name|PersistenceState
operator|.
name|TRANSIENT
return|;
block|}
specifier|protected
name|boolean
name|isUncommittedParent
parameter_list|()
block|{
name|int
name|state
init|=
name|relationshipOwner
operator|.
name|getPersistenceState
argument_list|()
decl_stmt|;
return|return
name|state
operator|==
name|PersistenceState
operator|.
name|MODIFIED
operator|||
name|state
operator|==
name|PersistenceState
operator|.
name|DELETED
return|;
block|}
comment|/**      * Executes a query that returns related objects. Subclasses would invoke this method      * whenever they need to resolve a fault.      */
specifier|protected
name|List
name|resolveFromDB
parameter_list|()
block|{
comment|// non-persistent objects shouldn't trigger a fetch
if|if
condition|(
name|isTransientParent
argument_list|()
condition|)
block|{
return|return
operator|new
name|ArrayList
argument_list|()
return|;
block|}
name|List
name|resolved
init|=
name|relationshipOwner
operator|.
name|getObjectContext
argument_list|()
operator|.
name|performQuery
argument_list|(
operator|new
name|RelationshipQuery
argument_list|(
name|relationshipOwner
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|relationshipName
argument_list|,
literal|false
argument_list|)
argument_list|)
decl_stmt|;
comment|/**          * Duplicating the list (see CAY-1194). Doing that only for RelationshipFault          * query results, so only for nested DataContexts          */
if|if
condition|(
name|resolved
operator|instanceof
name|RelationshipFault
condition|)
block|{
name|resolved
operator|=
operator|new
name|ArrayList
argument_list|(
name|resolved
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|resolved
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|resolved
return|;
block|}
name|updateReverse
argument_list|(
name|resolved
argument_list|)
expr_stmt|;
return|return
name|resolved
return|;
block|}
comment|// see if reverse relationship is to-one and we can connect source to results....
specifier|protected
name|void
name|updateReverse
parameter_list|(
name|List
name|resolved
parameter_list|)
block|{
name|EntityResolver
name|resolver
init|=
name|relationshipOwner
operator|.
name|getObjectContext
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
decl_stmt|;
name|ObjEntity
name|sourceEntity
init|=
name|resolver
operator|.
name|getObjEntity
argument_list|(
name|relationshipOwner
operator|.
name|getObjectId
argument_list|()
operator|.
name|getEntityName
argument_list|()
argument_list|)
decl_stmt|;
name|ObjRelationship
name|relationship
init|=
operator|(
name|ObjRelationship
operator|)
name|sourceEntity
operator|.
name|getRelationship
argument_list|(
name|relationshipName
argument_list|)
decl_stmt|;
name|ObjRelationship
name|reverse
init|=
name|relationship
operator|.
name|getReverseRelationship
argument_list|()
decl_stmt|;
if|if
condition|(
name|reverse
operator|!=
literal|null
operator|&&
operator|!
name|reverse
operator|.
name|isToMany
argument_list|()
condition|)
block|{
name|Property
name|property
init|=
name|resolver
operator|.
name|getClassDescriptor
argument_list|(
name|reverse
operator|.
name|getSourceEntity
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|getProperty
argument_list|(
name|reverse
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|Iterator
name|it
init|=
name|resolved
operator|.
name|iterator
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
name|property
operator|.
name|writePropertyDirectly
argument_list|(
name|it
operator|.
name|next
argument_list|()
argument_list|,
literal|null
argument_list|,
name|relationshipOwner
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

