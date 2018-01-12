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
name|dbsync
operator|.
name|reverse
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
specifier|abstract
class|class
name|FilterContainer
block|{
specifier|private
name|String
name|name
decl_stmt|;
specifier|private
specifier|final
name|Collection
argument_list|<
name|IncludeTable
argument_list|>
name|includeTableCollection
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
name|ExcludeTable
argument_list|>
name|excludeTableCollection
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
name|IncludeColumn
argument_list|>
name|includeColumnCollection
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
name|excludeColumnCollection
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
name|IncludeProcedure
argument_list|>
name|includeProcedureCollection
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
name|ExcludeProcedure
argument_list|>
name|excludeProcedureCollection
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
name|ExcludeRelationship
argument_list|>
name|excludeRelationshipCollection
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
name|Collection
argument_list|<
name|IncludeTable
argument_list|>
name|getIncludeTables
parameter_list|()
block|{
return|return
name|includeTableCollection
return|;
block|}
specifier|public
name|Collection
argument_list|<
name|ExcludeTable
argument_list|>
name|getExcludeTables
parameter_list|()
block|{
return|return
name|excludeTableCollection
return|;
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
name|includeColumnCollection
return|;
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
name|excludeColumnCollection
return|;
block|}
specifier|public
name|Collection
argument_list|<
name|IncludeProcedure
argument_list|>
name|getIncludeProcedures
parameter_list|()
block|{
return|return
name|includeProcedureCollection
return|;
block|}
specifier|public
name|Collection
argument_list|<
name|ExcludeProcedure
argument_list|>
name|getExcludeProcedures
parameter_list|()
block|{
return|return
name|excludeProcedureCollection
return|;
block|}
comment|/**      * @since 4.1      */
specifier|public
name|Collection
argument_list|<
name|ExcludeRelationship
argument_list|>
name|getExcludeRelationship
parameter_list|()
block|{
return|return
name|excludeRelationshipCollection
return|;
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
name|includeColumnCollection
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
name|excludeColumnCollection
operator|.
name|add
argument_list|(
name|excludeColumn
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addIncludeTable
parameter_list|(
name|IncludeTable
name|includeTable
parameter_list|)
block|{
name|this
operator|.
name|includeTableCollection
operator|.
name|add
argument_list|(
name|includeTable
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addExcludeTable
parameter_list|(
name|ExcludeTable
name|excludeTable
parameter_list|)
block|{
name|this
operator|.
name|excludeTableCollection
operator|.
name|add
argument_list|(
name|excludeTable
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addIncludeProcedure
parameter_list|(
name|IncludeProcedure
name|includeProcedure
parameter_list|)
block|{
name|this
operator|.
name|includeProcedureCollection
operator|.
name|add
argument_list|(
name|includeProcedure
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addExcludeProcedure
parameter_list|(
name|ExcludeProcedure
name|excludeProcedure
parameter_list|)
block|{
name|this
operator|.
name|excludeProcedureCollection
operator|.
name|add
argument_list|(
name|excludeProcedure
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
name|excludeRelationshipCollection
operator|.
name|add
argument_list|(
name|excludeRelationship
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|clearIncludeTables
parameter_list|()
block|{
name|includeTableCollection
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|clearExcludeTables
parameter_list|()
block|{
name|excludeTableCollection
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|clearIncludeProcedures
parameter_list|()
block|{
name|includeProcedureCollection
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|clearExcludeProcedures
parameter_list|()
block|{
name|excludeProcedureCollection
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|clearIncludeColumns
parameter_list|()
block|{
name|includeColumnCollection
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|clearExcludeColumns
parameter_list|()
block|{
name|excludeColumnCollection
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
comment|/**      * @since 4.1      */
specifier|public
name|void
name|clearExcludeRelationship
parameter_list|()
block|{
name|excludeRelationshipCollection
operator|.
name|clear
argument_list|()
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
name|void
name|set
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addConfiguredName
parameter_list|(
name|AntNestedElement
name|name
parameter_list|)
block|{
name|setName
argument_list|(
name|name
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addText
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|name
operator|.
name|trim
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|isEmptyContainer
parameter_list|()
block|{
return|return
name|includeColumnCollection
operator|.
name|isEmpty
argument_list|()
operator|&&
name|excludeColumnCollection
operator|.
name|isEmpty
argument_list|()
operator|&&
name|includeTableCollection
operator|.
name|isEmpty
argument_list|()
operator|&&
name|excludeTableCollection
operator|.
name|isEmpty
argument_list|()
operator|&&
name|includeProcedureCollection
operator|.
name|isEmpty
argument_list|()
operator|&&
name|excludeProcedureCollection
operator|.
name|isEmpty
argument_list|()
operator|&&
name|excludeRelationshipCollection
operator|.
name|isEmpty
argument_list|()
return|;
block|}
specifier|static
name|boolean
name|isBlank
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|collection
parameter_list|)
block|{
return|return
name|collection
operator|==
literal|null
operator|||
name|collection
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
name|toString
argument_list|(
operator|new
name|StringBuilder
argument_list|()
argument_list|,
literal|""
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
name|StringBuilder
name|toString
parameter_list|(
name|StringBuilder
name|res
parameter_list|,
name|String
name|prefix
parameter_list|)
block|{
name|appendCollection
argument_list|(
name|res
argument_list|,
name|prefix
argument_list|,
name|includeTableCollection
argument_list|)
expr_stmt|;
name|appendCollection
argument_list|(
name|res
argument_list|,
name|prefix
argument_list|,
name|excludeTableCollection
argument_list|)
expr_stmt|;
name|appendCollection
argument_list|(
name|res
argument_list|,
name|prefix
argument_list|,
name|includeColumnCollection
argument_list|)
expr_stmt|;
name|appendCollection
argument_list|(
name|res
argument_list|,
name|prefix
argument_list|,
name|excludeColumnCollection
argument_list|)
expr_stmt|;
name|appendCollection
argument_list|(
name|res
argument_list|,
name|prefix
argument_list|,
name|includeProcedureCollection
argument_list|)
expr_stmt|;
name|appendCollection
argument_list|(
name|res
argument_list|,
name|prefix
argument_list|,
name|excludeProcedureCollection
argument_list|)
expr_stmt|;
name|appendCollection
argument_list|(
name|res
argument_list|,
name|prefix
argument_list|,
name|excludeRelationshipCollection
argument_list|)
expr_stmt|;
return|return
name|res
return|;
block|}
specifier|protected
name|void
name|appendCollection
parameter_list|(
name|StringBuilder
name|res
parameter_list|,
name|String
name|prefix
parameter_list|,
name|Collection
argument_list|<
name|?
extends|extends
name|PatternParam
argument_list|>
name|collection
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isBlank
argument_list|(
name|collection
argument_list|)
condition|)
block|{
for|for
control|(
name|PatternParam
name|item
range|:
name|collection
control|)
block|{
name|item
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
block|}
block|}
end_class

end_unit

