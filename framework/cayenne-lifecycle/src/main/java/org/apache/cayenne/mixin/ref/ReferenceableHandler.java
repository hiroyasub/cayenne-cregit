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
name|mixin
operator|.
name|ref
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentHashMap
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
name|Cayenne
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
name|DataObject
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
name|annotation
operator|.
name|PostLoad
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
name|annotation
operator|.
name|PostPersist
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
name|mixin
operator|.
name|uuid
operator|.
name|UuidCoder
import|;
end_import

begin_comment
comment|/**  * A {@link MixinHandler} that injects {@link Referenceable#UUID_PROPERTY} into  * DataObjects and provides methods to lookup objects by UUID, as well as read UUID of the  * existing objects.  */
end_comment

begin_class
specifier|public
class|class
name|ReferenceableHandler
block|{
specifier|protected
name|EntityResolver
name|entityResolver
decl_stmt|;
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|UuidCoder
argument_list|>
name|coders
decl_stmt|;
specifier|public
name|ReferenceableHandler
parameter_list|(
name|EntityResolver
name|entityResolver
parameter_list|)
block|{
name|this
operator|.
name|entityResolver
operator|=
name|entityResolver
expr_stmt|;
name|this
operator|.
name|coders
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|UuidCoder
argument_list|>
argument_list|()
expr_stmt|;
block|}
comment|/**      * Returns a Referenceable object matching provided UUID and bound in provided      * {@link ObjectContext}.      */
specifier|public
name|Object
name|getReferenceable
parameter_list|(
name|ObjectContext
name|context
parameter_list|,
name|String
name|uuid
parameter_list|)
block|{
name|String
name|entityName
init|=
name|UuidCoder
operator|.
name|getEntityName
argument_list|(
name|uuid
argument_list|)
decl_stmt|;
name|UuidCoder
name|coder
init|=
name|getCoder
argument_list|(
name|entityName
argument_list|)
decl_stmt|;
name|ObjectId
name|oid
init|=
name|coder
operator|.
name|toObjectId
argument_list|(
name|uuid
argument_list|)
decl_stmt|;
return|return
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|oid
argument_list|)
return|;
block|}
specifier|public
name|String
name|getUuid
parameter_list|(
name|Object
name|referenceable
parameter_list|)
block|{
if|if
condition|(
name|referenceable
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"Null object"
argument_list|)
throw|;
block|}
if|if
condition|(
name|referenceable
operator|instanceof
name|DataObject
condition|)
block|{
comment|// even if this is not a registered Referenceable, don't see a
comment|// problem if we return a UUID.
name|DataObject
name|dataObject
init|=
operator|(
name|DataObject
operator|)
name|referenceable
decl_stmt|;
name|String
name|uuid
init|=
operator|(
name|String
operator|)
name|dataObject
operator|.
name|readPropertyDirectly
argument_list|(
name|Referenceable
operator|.
name|UUID_PROPERTY
argument_list|)
decl_stmt|;
if|if
condition|(
name|uuid
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"No UUID set. An object is either not a Referenceable "
operator|+
literal|"or is NEW or TRANSIENT."
argument_list|)
throw|;
block|}
return|return
name|uuid
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Object is not a DataObject: "
operator|+
name|referenceable
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
block|}
comment|/**      * A lifecycle listener method that initialzes DataObject UUID property.      */
annotation|@
name|PostLoad
argument_list|(
name|entityAnnotations
operator|=
name|Referenceable
operator|.
name|class
argument_list|)
annotation|@
name|PostPersist
argument_list|(
name|entityAnnotations
operator|=
name|Referenceable
operator|.
name|class
argument_list|)
specifier|protected
name|void
name|initProperties
parameter_list|(
name|DataObject
name|object
parameter_list|)
block|{
name|UuidCoder
name|coder
init|=
name|getCoder
argument_list|(
name|object
operator|.
name|getObjectId
argument_list|()
operator|.
name|getEntityName
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|uuid
init|=
name|coder
operator|.
name|toUuid
argument_list|(
name|object
operator|.
name|getObjectId
argument_list|()
argument_list|)
decl_stmt|;
name|object
operator|.
name|writePropertyDirectly
argument_list|(
name|Referenceable
operator|.
name|UUID_PROPERTY
argument_list|,
name|uuid
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|UuidCoder
name|getCoder
parameter_list|(
name|String
name|entityName
parameter_list|)
block|{
name|UuidCoder
name|coder
init|=
name|coders
operator|.
name|get
argument_list|(
name|entityName
argument_list|)
decl_stmt|;
if|if
condition|(
name|coder
operator|==
literal|null
condition|)
block|{
comment|// TODO: check @Referenceable annotation?
name|ObjEntity
name|entity
init|=
name|entityResolver
operator|.
name|getObjEntity
argument_list|(
name|entityName
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
literal|"Entity '"
operator|+
name|entityName
operator|+
literal|"' is not a known referenceable"
argument_list|)
throw|;
block|}
name|coder
operator|=
operator|new
name|UuidCoder
argument_list|(
name|entity
argument_list|)
expr_stmt|;
name|coders
operator|.
name|put
argument_list|(
name|entity
operator|.
name|getName
argument_list|()
argument_list|,
name|coder
argument_list|)
expr_stmt|;
block|}
return|return
name|coder
return|;
block|}
block|}
end_class

end_unit

