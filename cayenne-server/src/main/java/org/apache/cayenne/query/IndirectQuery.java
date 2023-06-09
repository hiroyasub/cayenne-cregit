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
name|query
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
name|CayenneRuntimeException
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
name|DataMap
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
name|EntityResolver
import|;
end_import

begin_comment
comment|/**  * A base superclass for queries that resolve into some other queries during the  * routing phase. Provides caching of a replacement query.  *   * @since 1.2  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|IndirectQuery
implements|implements
name|Query
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|974666786498898209L
decl_stmt|;
specifier|protected
name|String
name|name
decl_stmt|;
comment|/** 	 * @since 3.1 	 */
specifier|protected
name|DataMap
name|dataMap
decl_stmt|;
specifier|protected
specifier|transient
name|Query
name|replacementQuery
decl_stmt|;
specifier|protected
specifier|transient
name|EntityResolver
name|lastResolver
decl_stmt|;
comment|/** 	 * Returns the metadata obtained from the replacement query. 	 */
annotation|@
name|Override
specifier|public
name|QueryMetadata
name|getMetaData
parameter_list|(
name|EntityResolver
name|resolver
parameter_list|)
block|{
return|return
name|getReplacementQuery
argument_list|(
name|resolver
argument_list|)
operator|.
name|getMetaData
argument_list|(
name|resolver
argument_list|)
return|;
block|}
comment|/** 	 * Delegates routing to a replacement query. 	 */
annotation|@
name|Override
specifier|public
name|void
name|route
parameter_list|(
name|QueryRouter
name|router
parameter_list|,
name|EntityResolver
name|resolver
parameter_list|,
name|Query
name|substitutedQuery
parameter_list|)
block|{
name|getReplacementQuery
argument_list|(
name|resolver
argument_list|)
operator|.
name|route
argument_list|(
name|router
argument_list|,
name|resolver
argument_list|,
name|substitutedQuery
operator|!=
literal|null
condition|?
name|substitutedQuery
else|:
name|this
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * Creates a substitute query. An implementor is free to provide an 	 * arbitrary replacement query. 	 */
specifier|protected
specifier|abstract
name|Query
name|createReplacementQuery
parameter_list|(
name|EntityResolver
name|resolver
parameter_list|)
function_decl|;
comment|/** 	 * Returns a replacement query, creating it on demand and caching it for 	 * reuse. 	 */
specifier|protected
name|Query
name|getReplacementQuery
parameter_list|(
name|EntityResolver
name|resolver
parameter_list|)
block|{
if|if
condition|(
name|replacementQuery
operator|==
literal|null
operator|||
name|lastResolver
operator|!=
name|resolver
condition|)
block|{
name|this
operator|.
name|replacementQuery
operator|=
name|createReplacementQuery
argument_list|(
name|resolver
argument_list|)
expr_stmt|;
name|this
operator|.
name|lastResolver
operator|=
name|resolver
expr_stmt|;
block|}
return|return
name|replacementQuery
return|;
block|}
comment|/** 	 * Throws an exception as indirect query should not be executed directly. 	 */
annotation|@
name|Override
specifier|public
name|SQLAction
name|createSQLAction
parameter_list|(
name|SQLActionVisitor
name|visitor
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"%s is an indirect query and doesn't support its own sql actions. "
operator|+
literal|"It should've been delegated to another query during resolution or routing phase."
argument_list|,
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

