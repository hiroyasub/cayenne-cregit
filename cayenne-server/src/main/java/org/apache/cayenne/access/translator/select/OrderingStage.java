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
name|select
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
name|OrderingNodeBuilder
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
name|exp
operator|.
name|parser
operator|.
name|ASTAggregateFunctionCall
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
name|Ordering
import|;
end_import

begin_import
import|import static
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
operator|.
name|*
import|;
end_import

begin_comment
comment|/**  * @since 4.2  */
end_comment

begin_class
class|class
name|OrderingStage
implements|implements
name|TranslationStage
block|{
annotation|@
name|Override
specifier|public
name|void
name|perform
parameter_list|(
name|TranslatorContext
name|context
parameter_list|)
block|{
if|if
condition|(
name|context
operator|.
name|getQuery
argument_list|()
operator|.
name|getOrderings
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|QualifierTranslator
name|qualifierTranslator
init|=
name|context
operator|.
name|getQualifierTranslator
argument_list|()
decl_stmt|;
for|for
control|(
name|Ordering
name|ordering
range|:
name|context
operator|.
name|getQuery
argument_list|()
operator|.
name|getOrderings
argument_list|()
control|)
block|{
name|processOrdering
argument_list|(
name|qualifierTranslator
argument_list|,
name|context
argument_list|,
name|ordering
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|processOrdering
parameter_list|(
name|QualifierTranslator
name|qualifierTranslator
parameter_list|,
name|TranslatorContext
name|context
parameter_list|,
name|Ordering
name|ordering
parameter_list|)
block|{
name|Expression
name|exp
init|=
name|ordering
operator|.
name|getSortSpec
argument_list|()
decl_stmt|;
name|Node
name|translatedNode
init|=
name|qualifierTranslator
operator|.
name|translate
argument_list|(
name|exp
argument_list|)
decl_stmt|;
name|NodeBuilder
name|nodeBuilder
init|=
name|node
argument_list|(
name|translatedNode
argument_list|)
decl_stmt|;
if|if
condition|(
name|ordering
operator|.
name|isCaseInsensitive
argument_list|()
condition|)
block|{
name|nodeBuilder
operator|=
name|function
argument_list|(
literal|"UPPER"
argument_list|,
name|nodeBuilder
argument_list|)
expr_stmt|;
block|}
comment|// If query is DISTINCT then we need to add all ORDER BY clauses as result columns
if|if
condition|(
operator|!
name|context
operator|.
name|isDistinctSuppression
argument_list|()
condition|)
block|{
comment|// TODO: need to check duplicates?
comment|// need UPPER() function here too, as some DB expect exactly the same expression in select and in ordering
name|ResultNodeDescriptor
name|descriptor
init|=
name|context
operator|.
name|addResultNode
argument_list|(
name|nodeBuilder
operator|.
name|build
argument_list|()
operator|.
name|deepCopy
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|exp
operator|instanceof
name|ASTAggregateFunctionCall
condition|)
block|{
name|descriptor
operator|.
name|setAggregate
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
name|OrderingNodeBuilder
name|orderingNodeBuilder
init|=
name|order
argument_list|(
name|nodeBuilder
argument_list|)
decl_stmt|;
if|if
condition|(
name|ordering
operator|.
name|isDescending
argument_list|()
condition|)
block|{
name|orderingNodeBuilder
operator|.
name|desc
argument_list|()
expr_stmt|;
block|}
name|context
operator|.
name|getSelectBuilder
argument_list|()
operator|.
name|orderBy
argument_list|(
name|orderingNodeBuilder
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

