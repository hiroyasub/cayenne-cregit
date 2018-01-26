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
operator|.
name|translator
operator|.
name|ejbql
package|;
end_package

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
name|util
operator|.
name|Util
import|;
end_import

begin_comment
comment|/**  * A helper class representing an "id" of a database table during EJBQL translation. EJBQL  * "ids" point to ObjEntities, but during translation we need id's that represent both  * "root" tables that map back to an ObjEntity, as well as joined tables for flattened  * attributes and relationships. EJBQLTableId is intended to represent both types of  * tables.  *   * @since 3.0  */
end_comment

begin_class
class|class
name|EJBQLTableId
block|{
specifier|private
specifier|static
name|String
name|appendPath
parameter_list|(
name|EJBQLTableId
name|baseId
parameter_list|,
name|String
name|dbPathSuffix
parameter_list|)
block|{
if|if
condition|(
name|baseId
operator|.
name|getDbPath
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
name|dbPathSuffix
return|;
block|}
if|if
condition|(
name|dbPathSuffix
operator|==
literal|null
condition|)
block|{
return|return
name|baseId
operator|.
name|getDbPath
argument_list|()
return|;
block|}
return|return
name|baseId
operator|.
name|getDbPath
argument_list|()
operator|+
literal|"."
operator|+
name|dbPathSuffix
return|;
block|}
specifier|private
name|String
name|entityId
decl_stmt|;
specifier|private
name|String
name|dbPath
decl_stmt|;
name|EJBQLTableId
parameter_list|(
name|String
name|entityId
parameter_list|)
block|{
name|this
argument_list|(
name|entityId
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
name|EJBQLTableId
parameter_list|(
name|EJBQLTableId
name|baseId
parameter_list|,
name|String
name|dbPathSuffix
parameter_list|)
block|{
name|this
argument_list|(
name|baseId
operator|.
name|getEntityId
argument_list|()
argument_list|,
name|appendPath
argument_list|(
name|baseId
argument_list|,
name|dbPathSuffix
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|EJBQLTableId
parameter_list|(
name|String
name|entityId
parameter_list|,
name|String
name|dbPath
parameter_list|)
block|{
if|if
condition|(
name|entityId
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"Null entityId"
argument_list|)
throw|;
block|}
name|this
operator|.
name|entityId
operator|=
name|entityId
expr_stmt|;
name|this
operator|.
name|dbPath
operator|=
name|dbPath
expr_stmt|;
block|}
name|boolean
name|isPrimaryTable
parameter_list|()
block|{
return|return
name|dbPath
operator|==
literal|null
return|;
block|}
comment|/**      * Returns a DbEntity corresponding to the ID, that could be a root entity for the id,      * or a joined entity.      */
name|DbEntity
name|getDbEntity
parameter_list|(
name|EJBQLTranslationContext
name|context
parameter_list|)
block|{
name|ClassDescriptor
name|descriptor
init|=
name|context
operator|.
name|getEntityDescriptor
argument_list|(
name|entityId
argument_list|)
decl_stmt|;
name|DbEntity
name|rootEntity
init|=
name|descriptor
operator|.
name|getEntity
argument_list|()
operator|.
name|getDbEntity
argument_list|()
decl_stmt|;
if|if
condition|(
name|dbPath
operator|==
literal|null
condition|)
block|{
return|return
name|rootEntity
return|;
block|}
name|DbRelationship
name|r
init|=
literal|null
decl_stmt|;
name|Iterator
argument_list|<
name|?
argument_list|>
name|it
init|=
name|rootEntity
operator|.
name|resolvePathComponents
argument_list|(
name|dbPath
argument_list|)
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|r
operator|=
operator|(
name|DbRelationship
operator|)
name|it
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
return|return
operator|(
name|DbEntity
operator|)
name|r
operator|.
name|getTargetEntity
argument_list|()
return|;
block|}
name|String
name|getEntityId
parameter_list|()
block|{
return|return
name|entityId
return|;
block|}
name|String
name|getDbPath
parameter_list|()
block|{
return|return
name|dbPath
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
name|int
name|hash
init|=
name|entityId
operator|.
name|hashCode
argument_list|()
decl_stmt|;
if|if
condition|(
name|dbPath
operator|!=
literal|null
condition|)
block|{
name|hash
operator|+=
name|dbPath
operator|.
name|hashCode
argument_list|()
operator|*
literal|7
expr_stmt|;
block|}
return|return
name|hash
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|object
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
operator|!
operator|(
name|object
operator|instanceof
name|EJBQLTableId
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|EJBQLTableId
name|id
init|=
operator|(
name|EJBQLTableId
operator|)
name|object
decl_stmt|;
if|if
condition|(
operator|!
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|entityId
argument_list|,
name|id
operator|.
name|entityId
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
operator|!
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|dbPath
argument_list|,
name|id
operator|.
name|dbPath
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|dbPath
operator|!=
literal|null
condition|?
name|entityId
operator|+
literal|"/"
operator|+
name|dbPath
else|:
name|entityId
return|;
block|}
block|}
end_class

end_unit

