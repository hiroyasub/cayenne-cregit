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
operator|.
name|access
operator|.
name|flush
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Objects
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Supplier
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

begin_comment
comment|/**  * Deferred value extracted from ObjectId  *  * @since 4.2  */
end_comment

begin_class
class|class
name|ObjectIdValueSupplier
implements|implements
name|Supplier
argument_list|<
name|Object
argument_list|>
block|{
specifier|private
specifier|final
name|ObjectId
name|id
decl_stmt|;
specifier|private
specifier|final
name|String
name|attribute
decl_stmt|;
specifier|static
name|Object
name|getFor
parameter_list|(
name|ObjectId
name|id
parameter_list|,
name|String
name|attribute
parameter_list|)
block|{
comment|// resolve eagerly, if value is already present
comment|// TODO: what if this is a meaningful part of an ID and it will change?
name|Object
name|value
init|=
name|id
operator|.
name|getIdSnapshot
argument_list|()
operator|.
name|get
argument_list|(
name|attribute
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
return|return
name|value
return|;
block|}
return|return
operator|new
name|ObjectIdValueSupplier
argument_list|(
name|id
argument_list|,
name|attribute
argument_list|)
return|;
block|}
specifier|private
name|ObjectIdValueSupplier
parameter_list|(
name|ObjectId
name|id
parameter_list|,
name|String
name|attribute
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|this
operator|.
name|attribute
operator|=
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|attribute
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Object
name|get
parameter_list|()
block|{
return|return
name|id
operator|.
name|getIdSnapshot
argument_list|()
operator|.
name|get
argument_list|(
name|attribute
argument_list|)
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
name|ObjectIdValueSupplier
name|that
init|=
operator|(
name|ObjectIdValueSupplier
operator|)
name|o
decl_stmt|;
if|if
condition|(
operator|!
name|id
operator|.
name|equals
argument_list|(
name|that
operator|.
name|id
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|attribute
operator|.
name|equals
argument_list|(
name|that
operator|.
name|attribute
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
return|return
literal|31
operator|*
name|id
operator|.
name|hashCode
argument_list|()
operator|+
name|attribute
operator|.
name|hashCode
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
literal|"{id="
operator|+
name|id
operator|+
literal|", attr="
operator|+
name|attribute
operator|+
literal|'}'
return|;
block|}
block|}
end_class

end_unit

