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
name|Collections
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
name|Map
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
name|DataRow
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
name|exp
operator|.
name|Expression
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
name|exp
operator|.
name|ExpressionFactory
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
name|query
operator|.
name|SelectQuery
import|;
end_import

begin_comment
comment|/**  * An exception thrown on optimistic lock failure.  *   * @since 1.1  */
end_comment

begin_class
specifier|public
class|class
name|OptimisticLockException
extends|extends
name|CayenneRuntimeException
block|{
specifier|protected
name|ObjectId
name|failedObjectId
decl_stmt|;
specifier|protected
name|String
name|querySQL
decl_stmt|;
specifier|protected
name|DbEntity
name|rootEntity
decl_stmt|;
specifier|protected
name|Map
name|qualifierSnapshot
decl_stmt|;
specifier|public
name|OptimisticLockException
parameter_list|(
name|ObjectId
name|id
parameter_list|,
name|DbEntity
name|rootEntity
parameter_list|,
name|String
name|querySQL
parameter_list|,
name|Map
name|qualifierSnapshot
parameter_list|)
block|{
name|super
argument_list|(
literal|"Optimistic Lock Failure"
argument_list|)
expr_stmt|;
name|this
operator|.
name|failedObjectId
operator|=
name|id
expr_stmt|;
name|this
operator|.
name|rootEntity
operator|=
name|rootEntity
expr_stmt|;
name|this
operator|.
name|querySQL
operator|=
name|querySQL
expr_stmt|;
name|this
operator|.
name|qualifierSnapshot
operator|=
operator|(
name|qualifierSnapshot
operator|!=
literal|null
operator|)
condition|?
name|qualifierSnapshot
else|:
name|Collections
operator|.
name|EMPTY_MAP
expr_stmt|;
block|}
specifier|public
name|Map
name|getQualifierSnapshot
parameter_list|()
block|{
return|return
name|qualifierSnapshot
return|;
block|}
specifier|public
name|String
name|getQuerySQL
parameter_list|()
block|{
return|return
name|querySQL
return|;
block|}
comment|/**      * Retrieves fresh snapshot for the failed row. Null row indicates that it was      * deleted.      *       * @since 3.0      */
specifier|public
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|getFreshSnapshot
parameter_list|(
name|ObjectContext
name|context
parameter_list|)
block|{
name|Expression
name|qualifier
init|=
literal|null
decl_stmt|;
for|for
control|(
name|DbAttribute
name|attribute
range|:
name|rootEntity
operator|.
name|getPrimaryKeys
argument_list|()
control|)
block|{
name|Expression
name|attributeQualifier
init|=
name|ExpressionFactory
operator|.
name|matchDbExp
argument_list|(
name|attribute
operator|.
name|getName
argument_list|()
argument_list|,
name|qualifierSnapshot
operator|.
name|get
argument_list|(
name|attribute
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|qualifier
operator|=
operator|(
name|qualifier
operator|!=
literal|null
operator|)
condition|?
name|qualifier
operator|.
name|andExp
argument_list|(
name|attributeQualifier
argument_list|)
else|:
name|attributeQualifier
expr_stmt|;
block|}
name|SelectQuery
argument_list|<
name|DataRow
argument_list|>
name|query
init|=
operator|new
name|SelectQuery
argument_list|<
name|DataRow
argument_list|>
argument_list|(
name|rootEntity
argument_list|,
name|qualifier
argument_list|)
decl_stmt|;
name|query
operator|.
name|setFetchingDataRows
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|Cayenne
operator|.
name|objectForQuery
argument_list|(
name|context
argument_list|,
name|query
argument_list|)
return|;
block|}
comment|/**      * Returns descriptive message for this exception.      */
annotation|@
name|Override
specifier|public
name|String
name|getMessage
parameter_list|()
block|{
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|(
name|super
operator|.
name|getMessage
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|querySQL
operator|!=
literal|null
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|", SQL: ["
argument_list|)
operator|.
name|append
argument_list|(
name|querySQL
operator|.
name|trim
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"]"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|qualifierSnapshot
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|", WHERE clause bindings: ["
argument_list|)
expr_stmt|;
name|Iterator
name|it
init|=
name|qualifierSnapshot
operator|.
name|entrySet
argument_list|()
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
name|Map
operator|.
name|Entry
name|entry
init|=
operator|(
name|Map
operator|.
name|Entry
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"="
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
block|}
block|}
name|buffer
operator|.
name|append
argument_list|(
literal|"]"
argument_list|)
expr_stmt|;
block|}
return|return
name|buffer
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Returns the ObjectId of the object that caused the OptimisticLockException.      *       * @since 3.1      */
specifier|public
name|ObjectId
name|getFailedObjectId
parameter_list|()
block|{
return|return
name|failedObjectId
return|;
block|}
block|}
end_class

end_unit

