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
name|map
package|;
end_package

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
name|query
operator|.
name|Query
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
name|SQLTemplate
import|;
end_import

begin_comment
comment|/**  * QueryBuilder for the SQLTemplates.  *   * @since 1.1  * @author Andrus Adamchik  */
end_comment

begin_class
class|class
name|SQLTemplateBuilder
extends|extends
name|QueryBuilder
block|{
comment|/**      * Builds a SQLTemplate query.      */
specifier|public
name|Query
name|getQuery
parameter_list|()
block|{
name|SQLTemplate
name|template
init|=
operator|new
name|SQLTemplate
argument_list|()
decl_stmt|;
name|Object
name|root
init|=
name|getRoot
argument_list|()
decl_stmt|;
if|if
condition|(
name|root
operator|!=
literal|null
condition|)
block|{
name|template
operator|.
name|setRoot
argument_list|(
name|root
argument_list|)
expr_stmt|;
block|}
name|template
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|template
operator|.
name|initWithProperties
argument_list|(
name|properties
argument_list|)
expr_stmt|;
comment|// init SQL
name|template
operator|.
name|setDefaultTemplate
argument_list|(
name|sql
argument_list|)
expr_stmt|;
if|if
condition|(
name|adapterSql
operator|!=
literal|null
condition|)
block|{
name|Iterator
name|it
init|=
name|adapterSql
operator|.
name|entrySet
argument_list|()
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
name|Map
operator|.
name|Entry
name|entry
init|=
operator|(
name|Map
operator|.
name|Entry
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|Object
name|key
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|Object
name|value
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|key
operator|!=
literal|null
operator|&&
name|value
operator|!=
literal|null
condition|)
block|{
name|template
operator|.
name|setTemplate
argument_list|(
name|key
operator|.
name|toString
argument_list|()
argument_list|,
name|value
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|template
return|;
block|}
block|}
end_class

end_unit

