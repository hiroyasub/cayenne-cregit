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
name|jpa
operator|.
name|map
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|Column
import|;
end_import

begin_class
specifier|public
class|class
name|JpaColumn
block|{
specifier|protected
name|String
name|name
decl_stmt|;
specifier|protected
name|boolean
name|unique
decl_stmt|;
specifier|protected
name|boolean
name|nullable
decl_stmt|;
specifier|protected
name|boolean
name|insertable
decl_stmt|;
specifier|protected
name|boolean
name|updatable
decl_stmt|;
specifier|protected
name|String
name|columnDefinition
decl_stmt|;
specifier|protected
name|String
name|table
decl_stmt|;
specifier|protected
name|int
name|length
decl_stmt|;
specifier|protected
name|int
name|precision
decl_stmt|;
specifier|protected
name|int
name|scale
decl_stmt|;
specifier|public
name|JpaColumn
parameter_list|()
block|{
block|}
specifier|public
name|JpaColumn
parameter_list|(
name|Column
name|annotation
parameter_list|)
block|{
if|if
condition|(
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|annotation
operator|.
name|name
argument_list|()
argument_list|)
condition|)
block|{
name|name
operator|=
name|annotation
operator|.
name|name
argument_list|()
expr_stmt|;
block|}
name|unique
operator|=
name|annotation
operator|.
name|unique
argument_list|()
expr_stmt|;
name|nullable
operator|=
name|annotation
operator|.
name|nullable
argument_list|()
expr_stmt|;
name|insertable
operator|=
name|annotation
operator|.
name|insertable
argument_list|()
expr_stmt|;
name|updatable
operator|=
name|annotation
operator|.
name|updatable
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|annotation
operator|.
name|columnDefinition
argument_list|()
argument_list|)
condition|)
block|{
name|columnDefinition
operator|=
name|annotation
operator|.
name|columnDefinition
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|annotation
operator|.
name|table
argument_list|()
argument_list|)
condition|)
block|{
name|table
operator|=
name|annotation
operator|.
name|table
argument_list|()
expr_stmt|;
block|}
name|length
operator|=
name|annotation
operator|.
name|length
argument_list|()
expr_stmt|;
name|precision
operator|=
name|annotation
operator|.
name|precision
argument_list|()
expr_stmt|;
name|scale
operator|=
name|annotation
operator|.
name|scale
argument_list|()
expr_stmt|;
block|}
specifier|public
name|String
name|getColumnDefinition
parameter_list|()
block|{
return|return
name|columnDefinition
return|;
block|}
specifier|public
name|void
name|setColumnDefinition
parameter_list|(
name|String
name|columnDefinition
parameter_list|)
block|{
name|this
operator|.
name|columnDefinition
operator|=
name|columnDefinition
expr_stmt|;
block|}
specifier|public
name|boolean
name|isInsertable
parameter_list|()
block|{
return|return
name|insertable
return|;
block|}
specifier|public
name|void
name|setInsertable
parameter_list|(
name|boolean
name|insertable
parameter_list|)
block|{
name|this
operator|.
name|insertable
operator|=
name|insertable
expr_stmt|;
block|}
specifier|public
name|int
name|getLength
parameter_list|()
block|{
return|return
name|length
return|;
block|}
specifier|public
name|void
name|setLength
parameter_list|(
name|int
name|length
parameter_list|)
block|{
name|this
operator|.
name|length
operator|=
name|length
expr_stmt|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
specifier|public
name|boolean
name|isNullable
parameter_list|()
block|{
return|return
name|nullable
return|;
block|}
specifier|public
name|void
name|setNullable
parameter_list|(
name|boolean
name|nullable
parameter_list|)
block|{
name|this
operator|.
name|nullable
operator|=
name|nullable
expr_stmt|;
block|}
specifier|public
name|int
name|getPrecision
parameter_list|()
block|{
return|return
name|precision
return|;
block|}
specifier|public
name|void
name|setPrecision
parameter_list|(
name|int
name|precision
parameter_list|)
block|{
name|this
operator|.
name|precision
operator|=
name|precision
expr_stmt|;
block|}
specifier|public
name|int
name|getScale
parameter_list|()
block|{
return|return
name|scale
return|;
block|}
specifier|public
name|void
name|setScale
parameter_list|(
name|int
name|scale
parameter_list|)
block|{
name|this
operator|.
name|scale
operator|=
name|scale
expr_stmt|;
block|}
specifier|public
name|String
name|getTable
parameter_list|()
block|{
return|return
name|table
return|;
block|}
specifier|public
name|void
name|setTable
parameter_list|(
name|String
name|table
parameter_list|)
block|{
name|this
operator|.
name|table
operator|=
name|table
expr_stmt|;
block|}
specifier|public
name|boolean
name|isUnique
parameter_list|()
block|{
return|return
name|unique
return|;
block|}
specifier|public
name|void
name|setUnique
parameter_list|(
name|boolean
name|unique
parameter_list|)
block|{
name|this
operator|.
name|unique
operator|=
name|unique
expr_stmt|;
block|}
specifier|public
name|boolean
name|isUpdatable
parameter_list|()
block|{
return|return
name|updatable
return|;
block|}
specifier|public
name|void
name|setUpdatable
parameter_list|(
name|boolean
name|updateable
parameter_list|)
block|{
name|this
operator|.
name|updatable
operator|=
name|updateable
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|String
name|className
init|=
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
return|return
name|className
operator|.
name|substring
argument_list|(
name|className
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
operator|+
literal|1
argument_list|)
operator|+
literal|":"
operator|+
name|name
return|;
block|}
block|}
end_class

end_unit

