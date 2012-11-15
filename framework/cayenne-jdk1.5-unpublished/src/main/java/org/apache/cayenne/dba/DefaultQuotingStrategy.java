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
name|dba
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
name|map
operator|.
name|DbEntity
import|;
end_import

begin_comment
comment|/**  * @since 3.2 this is a top-level class.  */
end_comment

begin_class
class|class
name|DefaultQuotingStrategy
implements|implements
name|QuotingStrategy
block|{
specifier|private
name|String
name|endQuote
decl_stmt|;
specifier|private
name|String
name|startQuote
decl_stmt|;
name|DefaultQuotingStrategy
parameter_list|(
name|String
name|startQuote
parameter_list|,
name|String
name|endQuote
parameter_list|)
block|{
name|this
operator|.
name|startQuote
operator|=
name|startQuote
expr_stmt|;
name|this
operator|.
name|endQuote
operator|=
name|endQuote
expr_stmt|;
block|}
comment|/**      * @deprecated since 3.2      */
annotation|@
name|Deprecated
specifier|public
name|String
name|quoteString
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|quotedIdentifier
argument_list|(
name|name
argument_list|)
return|;
block|}
annotation|@
name|Deprecated
specifier|public
name|String
name|quoteFullyQualifiedName
parameter_list|(
name|DbEntity
name|entity
parameter_list|)
block|{
return|return
name|quotedFullyQualifiedName
argument_list|(
name|entity
argument_list|)
return|;
block|}
specifier|public
name|String
name|quotedFullyQualifiedName
parameter_list|(
name|DbEntity
name|entity
parameter_list|)
block|{
return|return
name|quotedIdentifier
argument_list|(
name|entity
operator|.
name|getCatalog
argument_list|()
argument_list|,
name|entity
operator|.
name|getSchema
argument_list|()
argument_list|,
name|entity
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|String
name|quotedIdentifier
parameter_list|(
name|String
modifier|...
name|fqnParts
parameter_list|)
block|{
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|part
range|:
name|fqnParts
control|)
block|{
if|if
condition|(
name|part
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|buffer
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|"."
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
name|startQuote
argument_list|)
operator|.
name|append
argument_list|(
name|part
argument_list|)
operator|.
name|append
argument_list|(
name|endQuote
argument_list|)
expr_stmt|;
block|}
return|return
name|buffer
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

