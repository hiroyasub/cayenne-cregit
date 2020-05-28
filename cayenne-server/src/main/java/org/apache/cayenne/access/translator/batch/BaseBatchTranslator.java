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
name|translator
operator|.
name|batch
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|ExpressionNodeBuilder
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
name|NodeBuilder
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
name|SQLBuilder
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
name|SQLGenerationVisitor
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
name|translator
operator|.
name|DbAttributeBinding
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
name|translator
operator|.
name|select
operator|.
name|DefaultQuotingAppendable
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
name|dba
operator|.
name|DbAdapter
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|query
operator|.
name|BatchQuery
import|;
end_import

begin_comment
comment|/**  * @since 4.2  * @param<T> type of the batch query to translate  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|BaseBatchTranslator
parameter_list|<
name|T
extends|extends
name|BatchQuery
parameter_list|>
block|{
specifier|protected
specifier|final
name|BatchTranslatorContext
argument_list|<
name|T
argument_list|>
name|context
decl_stmt|;
specifier|protected
name|DbAttributeBinding
index|[]
name|bindings
decl_stmt|;
specifier|public
name|BaseBatchTranslator
parameter_list|(
name|T
name|query
parameter_list|,
name|DbAdapter
name|adapter
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
operator|new
name|BatchTranslatorContext
argument_list|<>
argument_list|(
name|query
argument_list|,
name|adapter
argument_list|)
expr_stmt|;
block|}
specifier|public
name|DbAttributeBinding
index|[]
name|getBindings
parameter_list|()
block|{
return|return
name|bindings
return|;
block|}
comment|/**      * This method applies {@link org.apache.cayenne.access.translator.select.BaseSQLTreeProcessor} to the      * provided SQL tree node and generates SQL string from it.      *      * @param nodeBuilder SQL tree node builder      * @return SQL string      */
specifier|protected
name|String
name|doTranslate
parameter_list|(
name|NodeBuilder
name|nodeBuilder
parameter_list|)
block|{
name|Node
name|node
init|=
name|nodeBuilder
operator|.
name|build
argument_list|()
decl_stmt|;
comment|// convert to database flavour
name|node
operator|=
name|context
operator|.
name|getAdapter
argument_list|()
operator|.
name|getSqlTreeProcessor
argument_list|()
operator|.
name|apply
argument_list|(
name|node
argument_list|)
expr_stmt|;
comment|// generate SQL
name|SQLGenerationVisitor
name|visitor
init|=
operator|new
name|SQLGenerationVisitor
argument_list|(
operator|new
name|DefaultQuotingAppendable
argument_list|(
name|context
argument_list|)
argument_list|)
decl_stmt|;
name|node
operator|.
name|visit
argument_list|(
name|visitor
argument_list|)
expr_stmt|;
name|bindings
operator|=
name|context
operator|.
name|getBindings
argument_list|()
operator|.
name|toArray
argument_list|(
operator|new
name|DbAttributeBinding
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
return|return
name|visitor
operator|.
name|getSQLString
argument_list|()
return|;
block|}
specifier|abstract
specifier|protected
name|boolean
name|isNullAttribute
parameter_list|(
name|DbAttribute
name|attribute
parameter_list|)
function_decl|;
specifier|protected
name|ExpressionNodeBuilder
name|buildQualifier
parameter_list|(
name|List
argument_list|<
name|DbAttribute
argument_list|>
name|attributeList
parameter_list|)
block|{
name|ExpressionNodeBuilder
name|eq
init|=
literal|null
decl_stmt|;
for|for
control|(
name|DbAttribute
name|attr
range|:
name|attributeList
control|)
block|{
name|Integer
name|value
init|=
name|isNullAttribute
argument_list|(
name|attr
argument_list|)
condition|?
literal|null
else|:
literal|1
decl_stmt|;
name|ExpressionNodeBuilder
name|next
init|=
name|SQLBuilder
operator|.
name|column
argument_list|(
name|attr
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|attribute
argument_list|(
name|attr
argument_list|)
operator|.
name|eq
argument_list|(
name|SQLBuilder
operator|.
name|value
argument_list|(
name|value
argument_list|)
operator|.
name|attribute
argument_list|(
name|attr
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|eq
operator|==
literal|null
condition|)
block|{
name|eq
operator|=
name|next
expr_stmt|;
block|}
else|else
block|{
name|eq
operator|=
name|eq
operator|.
name|and
argument_list|(
name|next
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|eq
return|;
block|}
block|}
end_class

end_unit

