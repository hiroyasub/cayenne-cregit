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
name|query
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

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
name|map
operator|.
name|EntityResolver
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

begin_class
class|class
name|ProcedureQueryMetadata
extends|extends
name|BaseQueryMetadata
block|{
specifier|transient
name|Procedure
name|procedure
decl_stmt|;
specifier|public
name|Procedure
name|getProcedure
parameter_list|()
block|{
return|return
name|procedure
return|;
block|}
name|void
name|copyFromInfo
parameter_list|(
name|QueryMetadata
name|info
parameter_list|)
block|{
name|procedure
operator|=
literal|null
expr_stmt|;
name|super
operator|.
name|copyFromInfo
argument_list|(
name|info
argument_list|)
expr_stmt|;
block|}
name|void
name|resolve
parameter_list|(
name|Object
name|root
parameter_list|,
name|Object
name|resultRoot
parameter_list|,
name|EntityResolver
name|resolver
parameter_list|,
name|ProcedureQuery
name|query
parameter_list|)
block|{
if|if
condition|(
name|super
operator|.
name|resolve
argument_list|(
name|resultRoot
argument_list|,
name|resolver
argument_list|,
literal|null
argument_list|)
condition|)
block|{
name|procedure
operator|=
literal|null
expr_stmt|;
if|if
condition|(
name|root
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|root
operator|instanceof
name|String
condition|)
block|{
name|this
operator|.
name|procedure
operator|=
name|resolver
operator|.
name|lookupProcedure
argument_list|(
operator|(
name|String
operator|)
name|root
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|root
operator|instanceof
name|Procedure
condition|)
block|{
name|this
operator|.
name|procedure
operator|=
operator|(
name|Procedure
operator|)
name|root
expr_stmt|;
block|}
comment|// theoretically procedure can be in one DataMap, while the Java Class
comment|// - in another.
if|if
condition|(
name|this
operator|.
name|procedure
operator|!=
literal|null
operator|&&
name|this
operator|.
name|dataMap
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|dataMap
operator|=
name|procedure
operator|.
name|getDataMap
argument_list|()
expr_stmt|;
block|}
block|}
comment|// generate unique cache key...
if|if
condition|(
name|QueryMetadata
operator|.
name|NO_CACHE
operator|.
name|equals
argument_list|(
name|getCachePolicy
argument_list|()
argument_list|)
condition|)
block|{
block|}
if|else if
condition|(
name|query
operator|.
name|getName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|cacheKey
operator|=
name|query
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// create a unique key based on procedure and parameters
name|StringBuffer
name|key
init|=
operator|new
name|StringBuffer
argument_list|(
literal|"proc:"
argument_list|)
decl_stmt|;
if|if
condition|(
name|procedure
operator|!=
literal|null
condition|)
block|{
name|key
operator|.
name|append
argument_list|(
name|procedure
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Map
name|parameters
init|=
name|query
operator|.
name|getParameters
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|parameters
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|List
name|keys
init|=
operator|new
name|ArrayList
argument_list|(
name|parameters
operator|.
name|keySet
argument_list|()
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|keys
argument_list|)
expr_stmt|;
name|Iterator
name|it
init|=
name|keys
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Object
name|parameterKey
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|key
operator|.
name|append
argument_list|(
literal|'/'
argument_list|)
operator|.
name|append
argument_list|(
name|parameterKey
argument_list|)
operator|.
name|append
argument_list|(
literal|'='
argument_list|)
operator|.
name|append
argument_list|(
name|parameters
operator|.
name|get
argument_list|(
name|parameterKey
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|this
operator|.
name|cacheKey
operator|=
name|key
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

