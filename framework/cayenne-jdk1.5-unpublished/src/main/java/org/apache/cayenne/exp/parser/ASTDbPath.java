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
name|exp
operator|.
name|parser
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|ObjectId
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
name|Persistent
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
name|map
operator|.
name|Entity
import|;
end_import

begin_comment
comment|/**  * Path expression traversing DB relationships and attributes.  *   * @since 1.1  */
end_comment

begin_class
specifier|public
class|class
name|ASTDbPath
extends|extends
name|ASTPath
block|{
specifier|public
specifier|static
specifier|final
name|String
name|DB_PREFIX
init|=
literal|"db:"
decl_stmt|;
name|ASTDbPath
parameter_list|(
name|int
name|id
parameter_list|)
block|{
name|super
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ASTDbPath
parameter_list|()
block|{
name|super
argument_list|(
name|ExpressionParserTreeConstants
operator|.
name|JJTDBPATH
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ASTDbPath
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
name|super
argument_list|(
name|ExpressionParserTreeConstants
operator|.
name|JJTDBPATH
argument_list|)
expr_stmt|;
name|setPath
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|Object
name|evaluateNode
parameter_list|(
name|Object
name|o
parameter_list|)
throws|throws
name|Exception
block|{
comment|// TODO: implement resolving DB_PATH for DataObjects
if|if
condition|(
name|o
operator|instanceof
name|Entity
condition|)
block|{
return|return
name|evaluateEntityNode
argument_list|(
operator|(
name|Entity
operator|)
name|o
argument_list|)
return|;
block|}
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|map
init|=
name|toMap
argument_list|(
name|o
argument_list|)
decl_stmt|;
return|return
operator|(
name|map
operator|!=
literal|null
operator|)
condition|?
name|map
operator|.
name|get
argument_list|(
name|path
argument_list|)
else|:
literal|null
return|;
block|}
specifier|protected
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|toMap
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|o
operator|instanceof
name|Map
condition|)
block|{
return|return
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|o
return|;
block|}
if|else if
condition|(
name|o
operator|instanceof
name|ObjectId
condition|)
block|{
return|return
operator|(
operator|(
name|ObjectId
operator|)
name|o
operator|)
operator|.
name|getIdSnapshot
argument_list|()
return|;
block|}
if|else if
condition|(
name|o
operator|instanceof
name|Persistent
condition|)
block|{
name|Persistent
name|persistent
init|=
operator|(
name|Persistent
operator|)
name|o
decl_stmt|;
comment|// TODO: returns ObjectId snapshot for now.. should probably
comment|// retrieve full snapshot...
name|ObjectId
name|oid
init|=
name|persistent
operator|.
name|getObjectId
argument_list|()
decl_stmt|;
return|return
operator|(
name|oid
operator|!=
literal|null
operator|)
condition|?
name|oid
operator|.
name|getIdSnapshot
argument_list|()
else|:
literal|null
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
comment|/**      * Creates a copy of this expression node, without copying children.      */
annotation|@
name|Override
specifier|public
name|Expression
name|shallowCopy
parameter_list|()
block|{
name|ASTDbPath
name|copy
init|=
operator|new
name|ASTDbPath
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|copy
operator|.
name|path
operator|=
name|path
expr_stmt|;
return|return
name|copy
return|;
block|}
comment|/**      * @since 3.2      */
annotation|@
name|Override
specifier|public
name|void
name|appendAsEJBQL
parameter_list|(
name|Appendable
name|out
parameter_list|,
name|String
name|rootId
parameter_list|)
throws|throws
name|IOException
block|{
comment|// warning: non-standard EJBQL...
name|out
operator|.
name|append
argument_list|(
name|DB_PREFIX
argument_list|)
expr_stmt|;
name|out
operator|.
name|append
argument_list|(
name|rootId
argument_list|)
expr_stmt|;
name|out
operator|.
name|append
argument_list|(
literal|'.'
argument_list|)
expr_stmt|;
name|out
operator|.
name|append
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
comment|/**      * @since 3.2      */
annotation|@
name|Override
specifier|public
name|void
name|appendAsString
parameter_list|(
name|Appendable
name|out
parameter_list|)
throws|throws
name|IOException
block|{
name|out
operator|.
name|append
argument_list|(
name|DB_PREFIX
argument_list|)
operator|.
name|append
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getType
parameter_list|()
block|{
return|return
name|Expression
operator|.
name|DB_PATH
return|;
block|}
block|}
end_class

end_unit

