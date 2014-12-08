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
name|tools
operator|.
name|dbimport
operator|.
name|config
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

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|lang
operator|.
name|StringUtils
operator|.
name|join
import|;
end_import

begin_comment
comment|/**  * @since 3.2.  */
end_comment

begin_class
specifier|public
class|class
name|IncludeTable
extends|extends
name|PatternParam
block|{
specifier|private
name|Collection
argument_list|<
name|IncludeColumn
argument_list|>
name|includeColumns
init|=
operator|new
name|LinkedList
argument_list|<
name|IncludeColumn
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|Collection
argument_list|<
name|ExcludeColumn
argument_list|>
name|excludeColumns
init|=
operator|new
name|LinkedList
argument_list|<
name|ExcludeColumn
argument_list|>
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
operator|=
name|includeColumns
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
operator|=
name|excludeColumns
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
name|String
name|toString
parameter_list|()
block|{
name|String
name|str
init|=
literal|"+("
operator|+
name|getPattern
argument_list|()
operator|+
literal|") "
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
name|str
operator|+=
literal|"+Columns("
operator|+
name|join
argument_list|(
name|includeColumns
argument_list|,
literal|", "
argument_list|)
operator|+
literal|") "
expr_stmt|;
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
name|str
operator|+=
literal|"-Columns("
operator|+
name|join
argument_list|(
name|excludeColumns
argument_list|,
literal|", "
argument_list|)
operator|+
literal|") "
expr_stmt|;
block|}
return|return
name|str
return|;
block|}
block|}
end_class

end_unit

