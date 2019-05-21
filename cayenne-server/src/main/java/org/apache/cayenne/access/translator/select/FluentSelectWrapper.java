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
name|translator
operator|.
name|select
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
name|Objects
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
name|property
operator|.
name|BaseProperty
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
name|FluentSelect
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
name|Ordering
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
name|QueryMetadata
import|;
end_import

begin_comment
comment|/**  * @since 4.2  */
end_comment

begin_class
specifier|public
class|class
name|FluentSelectWrapper
implements|implements
name|TranslatableQueryWrapper
block|{
specifier|private
specifier|final
name|FluentSelect
argument_list|<
name|?
argument_list|>
name|select
decl_stmt|;
specifier|public
name|FluentSelectWrapper
parameter_list|(
name|FluentSelect
argument_list|<
name|?
argument_list|>
name|select
parameter_list|)
block|{
name|this
operator|.
name|select
operator|=
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|select
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isDistinct
parameter_list|()
block|{
return|return
name|select
operator|.
name|isDistinct
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|QueryMetadata
name|getMetaData
parameter_list|(
name|EntityResolver
name|resolver
parameter_list|)
block|{
return|return
name|select
operator|.
name|getMetaData
argument_list|(
name|resolver
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Expression
name|getQualifier
parameter_list|()
block|{
return|return
name|select
operator|.
name|getWhere
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Collection
argument_list|<
name|Ordering
argument_list|>
name|getOrderings
parameter_list|()
block|{
return|return
name|select
operator|.
name|getOrderings
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Collection
argument_list|<
name|BaseProperty
argument_list|<
name|?
argument_list|>
argument_list|>
name|getColumns
parameter_list|()
block|{
return|return
name|select
operator|.
name|getColumns
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Expression
name|getHavingQualifier
parameter_list|()
block|{
return|return
name|select
operator|.
name|getHaving
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|FluentSelect
argument_list|<
name|?
argument_list|>
name|unwrap
parameter_list|()
block|{
return|return
name|select
return|;
block|}
block|}
end_class

end_unit

