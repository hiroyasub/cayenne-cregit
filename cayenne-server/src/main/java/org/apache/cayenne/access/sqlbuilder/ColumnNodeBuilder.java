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
operator|.
name|ColumnNode
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
name|access
operator|.
name|sqlbuilder
operator|.
name|sqltree
operator|.
name|Node
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
name|access
operator|.
name|sqlbuilder
operator|.
name|sqltree
operator|.
name|UnescapedColumnNode
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
name|ColumnNodeBuilder
implements|implements
name|ExpressionTrait
block|{
specifier|private
specifier|final
name|String
name|table
decl_stmt|;
specifier|private
specifier|final
name|String
name|column
decl_stmt|;
specifier|private
name|boolean
name|unescaped
decl_stmt|;
specifier|private
name|String
name|alias
decl_stmt|;
specifier|private
name|DbAttribute
name|attribute
decl_stmt|;
name|ColumnNodeBuilder
parameter_list|(
name|String
name|table
parameter_list|,
name|String
name|field
parameter_list|)
block|{
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
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|field
argument_list|)
expr_stmt|;
block|}
name|ColumnNodeBuilder
parameter_list|(
name|String
name|table
parameter_list|,
name|DbAttribute
name|attribute
parameter_list|)
block|{
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
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|attribute
argument_list|)
operator|.
name|getName
argument_list|()
expr_stmt|;
name|this
operator|.
name|attribute
operator|=
name|attribute
expr_stmt|;
block|}
specifier|public
name|ColumnNodeBuilder
name|as
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
return|return
name|this
return|;
block|}
specifier|public
name|ColumnNodeBuilder
name|unescaped
parameter_list|()
block|{
name|this
operator|.
name|unescaped
operator|=
literal|true
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|ColumnNodeBuilder
name|attribute
parameter_list|(
name|DbAttribute
name|attribute
parameter_list|)
block|{
name|this
operator|.
name|attribute
operator|=
name|attribute
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|OrderingNodeBuilder
name|desc
parameter_list|()
block|{
return|return
operator|new
name|OrderingNodeBuilder
argument_list|(
name|this
argument_list|)
operator|.
name|desc
argument_list|()
return|;
block|}
specifier|public
name|OrderingNodeBuilder
name|asc
parameter_list|()
block|{
return|return
operator|new
name|OrderingNodeBuilder
argument_list|(
name|this
argument_list|)
operator|.
name|asc
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Node
name|build
parameter_list|()
block|{
name|ColumnNode
name|columnNode
decl_stmt|;
if|if
condition|(
name|unescaped
condition|)
block|{
name|columnNode
operator|=
operator|new
name|UnescapedColumnNode
argument_list|(
name|table
argument_list|,
name|column
argument_list|,
name|alias
argument_list|,
name|attribute
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|columnNode
operator|=
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
expr_stmt|;
block|}
return|return
name|columnNode
return|;
block|}
block|}
end_class

end_unit

