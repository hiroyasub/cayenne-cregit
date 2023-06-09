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
name|JoinNode
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

begin_comment
comment|/**  * @since 4.2  */
end_comment

begin_class
specifier|public
class|class
name|JoinNodeBuilder
implements|implements
name|NodeBuilder
block|{
specifier|private
specifier|final
name|JoinType
name|joinType
decl_stmt|;
specifier|private
specifier|final
name|NodeBuilder
name|table
decl_stmt|;
specifier|private
name|NodeBuilder
name|joinExp
decl_stmt|;
name|JoinNodeBuilder
parameter_list|(
name|JoinType
name|joinType
parameter_list|,
name|NodeBuilder
name|table
parameter_list|)
block|{
name|this
operator|.
name|joinType
operator|=
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|joinType
argument_list|)
expr_stmt|;
name|this
operator|.
name|table
operator|=
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|table
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JoinNodeBuilder
name|on
parameter_list|(
name|NodeBuilder
name|joinExp
parameter_list|)
block|{
name|this
operator|.
name|joinExp
operator|=
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|joinExp
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|Node
name|build
parameter_list|()
block|{
name|Node
name|node
init|=
operator|new
name|JoinNode
argument_list|(
name|joinType
argument_list|)
decl_stmt|;
name|node
operator|.
name|addChild
argument_list|(
name|table
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
name|node
operator|.
name|addChild
argument_list|(
name|joinExp
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|node
return|;
block|}
block|}
end_class

end_unit

