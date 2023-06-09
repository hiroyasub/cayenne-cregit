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
name|dbsync
operator|.
name|reverse
operator|.
name|dbimport
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|configuration
operator|.
name|ConfigurationNodeVisitor
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
name|XMLEncoder
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
name|XMLSerializable
import|;
end_import

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

begin_comment
comment|/**  * @since 4.0.  */
end_comment

begin_class
specifier|public
class|class
name|IncludeTable
extends|extends
name|PatternParam
implements|implements
name|XMLSerializable
block|{
specifier|private
specifier|final
name|List
argument_list|<
name|IncludeColumn
argument_list|>
name|includeColumns
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|List
argument_list|<
name|ExcludeColumn
argument_list|>
name|excludeColumns
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|List
argument_list|<
name|ExcludeRelationship
argument_list|>
name|excludeRelationship
init|=
operator|new
name|ArrayList
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
name|IncludeTable
parameter_list|(
name|IncludeTable
name|original
parameter_list|)
block|{
name|super
argument_list|(
name|original
argument_list|)
expr_stmt|;
for|for
control|(
name|IncludeColumn
name|includeColumn
range|:
name|original
operator|.
name|getIncludeColumns
argument_list|()
control|)
block|{
name|this
operator|.
name|addIncludeColumn
argument_list|(
operator|new
name|IncludeColumn
argument_list|(
name|includeColumn
argument_list|)
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|ExcludeColumn
name|excludeColumn
range|:
name|original
operator|.
name|getExcludeColumns
argument_list|()
control|)
block|{
name|this
operator|.
name|addExcludeColumn
argument_list|(
operator|new
name|ExcludeColumn
argument_list|(
name|excludeColumn
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|List
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
name|List
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
comment|/**      * @since 4.1      */
specifier|public
name|List
argument_list|<
name|ExcludeRelationship
argument_list|>
name|getExcludeRelationship
parameter_list|()
block|{
return|return
name|excludeRelationship
return|;
block|}
comment|/**      * @since 4.1      */
specifier|public
name|void
name|setExcludeRelationship
parameter_list|(
name|Collection
argument_list|<
name|ExcludeRelationship
argument_list|>
name|excludeRelationship
parameter_list|)
block|{
name|this
operator|.
name|excludeRelationship
operator|.
name|addAll
argument_list|(
name|excludeRelationship
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
comment|/**      * @since 4.1      */
specifier|public
name|void
name|addExcludeRelationship
parameter_list|(
name|ExcludeRelationship
name|excludeRelationship
parameter_list|)
block|{
name|this
operator|.
name|excludeRelationship
operator|.
name|add
argument_list|(
name|excludeRelationship
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|encodeAsXML
parameter_list|(
name|XMLEncoder
name|encoder
parameter_list|,
name|ConfigurationNodeVisitor
name|delegate
parameter_list|)
block|{
name|encoder
operator|.
name|start
argument_list|(
literal|"includeTable"
argument_list|)
operator|.
name|simpleTag
argument_list|(
literal|"name"
argument_list|,
name|this
operator|.
name|getPattern
argument_list|()
argument_list|)
operator|.
name|nested
argument_list|(
name|this
operator|.
name|getIncludeColumns
argument_list|()
argument_list|,
name|delegate
argument_list|)
operator|.
name|nested
argument_list|(
name|this
operator|.
name|getExcludeColumns
argument_list|()
argument_list|,
name|delegate
argument_list|)
operator|.
name|end
argument_list|()
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
if|if
condition|(
name|excludeRelationship
operator|!=
literal|null
operator|&&
operator|!
name|excludeRelationship
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|ExcludeRelationship
name|excludeRelationship
range|:
name|excludeRelationship
control|)
block|{
name|excludeRelationship
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

