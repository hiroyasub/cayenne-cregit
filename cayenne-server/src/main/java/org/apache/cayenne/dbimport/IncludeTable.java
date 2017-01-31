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
name|dbimport
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
name|LinkedList
import|;
end_import

begin_comment
comment|/**  * @since 4.0.  */
end_comment

begin_class
specifier|public
class|class
name|IncludeTable
extends|extends
name|PatternParam
block|{
specifier|private
specifier|final
name|Collection
argument_list|<
name|IncludeColumn
argument_list|>
name|includeColumns
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|Collection
argument_list|<
name|ExcludeColumn
argument_list|>
name|excludeColumns
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
name|IncludeTable
parameter_list|()
block|{
block|}
specifier|public
name|IncludeTable
parameter_list|(
name|String
name|pattern
parameter_list|)
block|{
name|super
argument_list|(
name|pattern
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Collection
argument_list|<
name|IncludeColumn
argument_list|>
name|getIncludeColumns
parameter_list|()
block|{
return|return
name|includeColumns
return|;
block|}
specifier|public
name|void
name|setIncludeColumns
parameter_list|(
name|Collection
argument_list|<
name|IncludeColumn
argument_list|>
name|includeColumns
parameter_list|)
block|{
name|this
operator|.
name|includeColumns
operator|.
name|addAll
argument_list|(
name|includeColumns
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Collection
argument_list|<
name|ExcludeColumn
argument_list|>
name|getExcludeColumns
parameter_list|()
block|{
return|return
name|excludeColumns
return|;
block|}
specifier|public
name|void
name|setExcludeColumns
parameter_list|(
name|Collection
argument_list|<
name|ExcludeColumn
argument_list|>
name|excludeColumns
parameter_list|)
block|{
name|this
operator|.
name|excludeColumns
operator|.
name|addAll
argument_list|(
name|excludeColumns
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addIncludeColumn
parameter_list|(
name|IncludeColumn
name|includeColumn
parameter_list|)
block|{
name|this
operator|.
name|includeColumns
operator|.
name|add
argument_list|(
name|includeColumn
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addExcludeColumn
parameter_list|(
name|ExcludeColumn
name|excludeColumn
parameter_list|)
block|{
name|this
operator|.
name|excludeColumns
operator|.
name|add
argument_list|(
name|excludeColumn
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|StringBuilder
name|toString
parameter_list|(
name|StringBuilder
name|res
parameter_list|,
name|String
name|s
parameter_list|)
block|{
name|super
operator|.
name|toString
argument_list|(
name|res
argument_list|,
name|s
argument_list|)
expr_stmt|;
name|String
name|prefix
init|=
name|s
operator|+
literal|"  "
decl_stmt|;
if|if
condition|(
name|includeColumns
operator|!=
literal|null
operator|&&
operator|!
name|includeColumns
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|IncludeColumn
name|includeColumn
range|:
name|includeColumns
control|)
block|{
name|includeColumn
operator|.
name|toString
argument_list|(
name|res
argument_list|,
name|prefix
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|excludeColumns
operator|!=
literal|null
operator|&&
operator|!
name|excludeColumns
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|ExcludeColumn
name|excludeColumn
range|:
name|excludeColumns
control|)
block|{
name|excludeColumn
operator|.
name|toString
argument_list|(
name|res
argument_list|,
name|prefix
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|res
return|;
block|}
block|}
end_class

end_unit

