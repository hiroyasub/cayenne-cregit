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
name|sqlbuilder
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
name|sqltree
operator|.
name|DeleteNode
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
name|TableNode
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
name|WhereNode
import|;
end_import

begin_comment
comment|/**  * @since 4.2  */
end_comment

begin_class
specifier|public
class|class
name|DeleteBuilder
extends|extends
name|BaseBuilder
block|{
specifier|private
specifier|static
specifier|final
name|int
name|TABLE_NODE
init|=
literal|0
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|WHERE_NODE
init|=
literal|1
decl_stmt|;
specifier|public
name|DeleteBuilder
parameter_list|(
name|String
name|table
parameter_list|)
block|{
name|super
argument_list|(
operator|new
name|DeleteNode
argument_list|()
argument_list|,
name|WHERE_NODE
operator|+
literal|1
argument_list|)
expr_stmt|;
name|node
argument_list|(
name|TABLE_NODE
argument_list|,
parameter_list|()
lambda|->
operator|new
name|TableNode
argument_list|(
name|table
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|DeleteBuilder
name|where
parameter_list|(
name|NodeBuilder
name|expression
parameter_list|)
block|{
if|if
condition|(
name|expression
operator|!=
literal|null
condition|)
block|{
name|node
argument_list|(
name|WHERE_NODE
argument_list|,
name|WhereNode
operator|::
operator|new
argument_list|)
operator|.
name|addChild
argument_list|(
name|expression
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
block|}
end_class

end_unit

