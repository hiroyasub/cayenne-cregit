begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    https://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
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
name|Collections
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
name|util
operator|.
name|HashCodeBuilder
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
name|SingleEntryMap
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
comment|/**  * {@link ObjectId} with single non-numeric value  * @since 4.2  */
end_comment

begin_class
class|class
name|ObjectIdSingle
implements|implements
name|ObjectId
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|3968183354758914938L
decl_stmt|;
specifier|private
specifier|final
name|String
name|entityName
decl_stmt|;
specifier|private
specifier|final
name|String
name|keyName
decl_stmt|;
specifier|private
specifier|final
name|Object
name|value
decl_stmt|;
specifier|private
specifier|transient
name|int
name|hashCode
decl_stmt|;
specifier|private
name|SingleEntryMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|replacementId
decl_stmt|;
comment|// exists for deserialization with Hessian and similar
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
specifier|private
name|ObjectIdSingle
parameter_list|()
block|{
name|this
operator|.
name|entityName
operator|=
literal|""
expr_stmt|;
name|this
operator|.
name|keyName
operator|=
literal|""
expr_stmt|;
name|this
operator|.
name|value
operator|=
literal|null
expr_stmt|;
block|}
name|ObjectIdSingle
parameter_list|(
name|String
name|entityName
parameter_list|,
name|String
name|keyName
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|this
operator|.
name|entityName
operator|=
name|entityName
expr_stmt|;
name|this
operator|.
name|keyName
operator|=
name|keyName
expr_stmt|;
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isTemporary
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getEntityName
parameter_list|()
block|{
return|return
name|entityName
return|;
block|}
annotation|@
name|Override
specifier|public
name|byte
index|[]
name|getKey
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getIdSnapshot
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|singletonMap
argument_list|(
name|keyName
argument_list|,
name|value
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getReplacementIdMap
parameter_list|()
block|{
if|if
condition|(
name|replacementId
operator|==
literal|null
condition|)
block|{
name|replacementId
operator|=
operator|new
name|SingleEntryMap
argument_list|<>
argument_list|(
name|keyName
argument_list|)
expr_stmt|;
block|}
return|return
name|replacementId
return|;
block|}
annotation|@
name|Override
specifier|public
name|ObjectId
name|createReplacementId
parameter_list|()
block|{
name|Object
name|newValue
init|=
name|replacementId
operator|==
literal|null
condition|?
literal|null
else|:
name|replacementId
operator|.
name|getValue
argument_list|()
decl_stmt|;
return|return
name|newValue
operator|==
literal|null
condition|?
name|this
else|:
name|ObjectId
operator|.
name|of
argument_list|(
name|entityName
argument_list|,
name|keyName
argument_list|,
name|newValue
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isReplacementIdAttached
parameter_list|()
block|{
return|return
name|replacementId
operator|!=
literal|null
operator|&&
operator|!
name|replacementId
operator|.
name|isEmpty
argument_list|()
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
literal|"<ObjectId:"
operator|+
name|entityName
operator|+
literal|", "
operator|+
name|keyName
operator|+
literal|"="
operator|+
name|value
operator|+
literal|">"
return|;
block|}
name|String
name|getKeyName
parameter_list|()
block|{
return|return
name|keyName
return|;
block|}
name|Object
name|getValue
parameter_list|()
block|{
return|return
name|value
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|o
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|o
operator|==
literal|null
operator|||
name|getClass
argument_list|()
operator|!=
name|o
operator|.
name|getClass
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|ObjectIdSingle
name|that
init|=
operator|(
name|ObjectIdSingle
operator|)
name|o
decl_stmt|;
if|if
condition|(
operator|!
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|entityName
argument_list|,
name|that
operator|.
name|entityName
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|value
argument_list|,
name|that
operator|.
name|value
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
if|if
condition|(
name|hashCode
operator|==
literal|0
condition|)
block|{
name|hashCode
operator|=
operator|new
name|HashCodeBuilder
argument_list|()
operator|.
name|append
argument_list|(
name|entityName
argument_list|)
operator|.
name|append
argument_list|(
name|value
argument_list|)
operator|.
name|toHashCode
argument_list|()
expr_stmt|;
block|}
return|return
name|hashCode
return|;
block|}
block|}
end_class

end_unit

