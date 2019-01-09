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
name|AliasedNode
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
class|class
name|AliasedNodeBuilder
implements|implements
name|NodeBuilder
block|{
specifier|private
specifier|final
name|NodeBuilder
name|nodeBuilder
decl_stmt|;
specifier|private
specifier|final
name|String
name|alias
decl_stmt|;
name|AliasedNodeBuilder
parameter_list|(
name|NodeBuilder
name|nodeBuilder
parameter_list|,
name|String
name|alias
parameter_list|)
block|{
name|this
operator|.
name|nodeBuilder
operator|=
name|nodeBuilder
expr_stmt|;
name|this
operator|.
name|alias
operator|=
name|alias
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Node
name|build
parameter_list|()
block|{
name|Node
name|root
init|=
operator|new
name|AliasedNode
argument_list|(
name|alias
argument_list|)
decl_stmt|;
name|root
operator|.
name|addChild
argument_list|(
name|nodeBuilder
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|root
return|;
block|}
block|}
end_class

end_unit

