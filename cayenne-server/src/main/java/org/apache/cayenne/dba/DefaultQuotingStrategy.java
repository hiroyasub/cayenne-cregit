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
name|dba
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
name|map
operator|.
name|DbEntity
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
name|DbJoin
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
name|Procedure
import|;
end_import

begin_comment
comment|/**  * @since 4.0 this is a top-level class.  */
end_comment

begin_class
specifier|public
class|class
name|DefaultQuotingStrategy
implements|implements
name|QuotingStrategy
block|{
specifier|private
specifier|final
name|String
name|endQuote
decl_stmt|;
specifier|private
specifier|final
name|String
name|startQuote
decl_stmt|;
specifier|public
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
annotation|@
name|Override
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
name|getDataMap
argument_list|()
argument_list|,
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
annotation|@
name|Override
specifier|public
name|String
name|quotedName
parameter_list|(
name|DbAttribute
name|attribute
parameter_list|)
block|{
return|return
name|quotedIdentifier
argument_list|(
name|attribute
operator|.
name|getEntity
argument_list|()
operator|.
name|getDataMap
argument_list|()
argument_list|,
name|attribute
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|quotedSourceName
parameter_list|(
name|DbJoin
name|join
parameter_list|)
block|{
name|DataMap
name|dataMap
init|=
name|join
operator|.
name|getSource
argument_list|()
operator|.
name|getEntity
argument_list|()
operator|.
name|getDataMap
argument_list|()
decl_stmt|;
return|return
name|quotedIdentifier
argument_list|(
name|dataMap
argument_list|,
name|join
operator|.
name|getSourceName
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|quotedTargetName
parameter_list|(
name|DbJoin
name|join
parameter_list|)
block|{
name|DataMap
name|dataMap
init|=
name|join
operator|.
name|getTarget
argument_list|()
operator|.
name|getEntity
argument_list|()
operator|.
name|getDataMap
argument_list|()
decl_stmt|;
return|return
name|quotedIdentifier
argument_list|(
name|dataMap
argument_list|,
name|join
operator|.
name|getTargetName
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * @since 4.2      */
annotation|@
name|Override
specifier|public
name|void
name|quotedIdentifier
parameter_list|(
name|DataMap
name|dataMap
parameter_list|,
name|CharSequence
name|identifier
parameter_list|,
name|Appendable
name|appender
parameter_list|)
block|{
if|if
condition|(
name|identifier
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|boolean
name|quoting
init|=
name|dataMap
operator|!=
literal|null
operator|&&
name|dataMap
operator|.
name|isQuotingSQLIdentifiers
argument_list|()
decl_stmt|;
try|try
block|{
if|if
condition|(
name|quoting
condition|)
block|{
name|appender
operator|.
name|append
argument_list|(
name|startQuote
argument_list|)
operator|.
name|append
argument_list|(
name|identifier
argument_list|)
operator|.
name|append
argument_list|(
name|endQuote
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|appender
operator|.
name|append
argument_list|(
name|identifier
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Failed to append quoted identifier"
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|String
name|quotedIdentifier
parameter_list|(
name|DataMap
name|dataMap
parameter_list|,
name|String
modifier|...
name|identifierParts
parameter_list|)
block|{
name|boolean
name|quoting
init|=
name|dataMap
operator|!=
literal|null
operator|&&
name|dataMap
operator|.
name|isQuotingSQLIdentifiers
argument_list|()
decl_stmt|;
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
name|identifierParts
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
literal|'.'
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|quoting
condition|)
block|{
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
else|else
block|{
name|buffer
operator|.
name|append
argument_list|(
name|part
argument_list|)
expr_stmt|;
block|}
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

