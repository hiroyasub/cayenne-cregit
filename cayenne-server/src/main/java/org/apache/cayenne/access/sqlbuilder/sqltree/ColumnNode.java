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
name|sqlbuilder
operator|.
name|sqltree
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
name|access
operator|.
name|sqlbuilder
operator|.
name|QuotingAppendable
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

begin_comment
comment|/**  * @since 4.2  */
end_comment

begin_class
specifier|public
class|class
name|ColumnNode
extends|extends
name|Node
block|{
specifier|protected
specifier|final
name|String
name|table
decl_stmt|;
specifier|protected
specifier|final
name|String
name|column
decl_stmt|;
specifier|protected
specifier|final
name|DbAttribute
name|attribute
decl_stmt|;
specifier|protected
name|String
name|alias
decl_stmt|;
specifier|public
name|ColumnNode
parameter_list|(
name|String
name|table
parameter_list|,
name|String
name|column
parameter_list|,
name|String
name|alias
parameter_list|,
name|DbAttribute
name|attribute
parameter_list|)
block|{
name|super
argument_list|(
name|NodeType
operator|.
name|COLUMN
argument_list|)
expr_stmt|;
name|this
operator|.
name|table
operator|=
name|table
expr_stmt|;
name|this
operator|.
name|column
operator|=
name|column
expr_stmt|;
name|this
operator|.
name|alias
operator|=
name|alias
expr_stmt|;
name|this
operator|.
name|attribute
operator|=
name|attribute
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|QuotingAppendable
name|append
parameter_list|(
name|QuotingAppendable
name|buffer
parameter_list|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
if|if
condition|(
name|table
operator|!=
literal|null
condition|)
block|{
name|buffer
operator|.
name|appendQuoted
argument_list|(
name|table
argument_list|)
operator|.
name|append
argument_list|(
literal|'.'
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|appendQuoted
argument_list|(
name|column
argument_list|)
expr_stmt|;
if|if
condition|(
name|alias
operator|!=
literal|null
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
operator|.
name|appendQuoted
argument_list|(
name|alias
argument_list|)
expr_stmt|;
block|}
return|return
name|buffer
return|;
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
name|String
name|getColumn
parameter_list|()
block|{
return|return
name|column
return|;
block|}
specifier|public
name|String
name|getAlias
parameter_list|()
block|{
return|return
name|alias
return|;
block|}
specifier|public
name|void
name|setAlias
parameter_list|(
name|String
name|alias
parameter_list|)
block|{
name|this
operator|.
name|alias
operator|=
name|alias
expr_stmt|;
block|}
specifier|public
name|DbAttribute
name|getAttribute
parameter_list|()
block|{
return|return
name|attribute
return|;
block|}
annotation|@
name|Override
specifier|public
name|Node
name|copy
parameter_list|()
block|{
return|return
operator|new
name|ColumnNode
argument_list|(
name|table
argument_list|,
name|column
argument_list|,
name|alias
argument_list|,
name|attribute
argument_list|)
return|;
block|}
block|}
end_class

end_unit

