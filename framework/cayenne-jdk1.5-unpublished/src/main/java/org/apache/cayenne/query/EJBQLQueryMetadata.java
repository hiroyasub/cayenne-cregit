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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|ejbql
operator|.
name|EJBQLCompiledExpression
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
name|ObjEntity
import|;
end_import

begin_comment
comment|/**  * A metadata object for the {@link EJBQLQuery}.  *   * @since 3.0  */
end_comment

begin_class
class|class
name|EJBQLQueryMetadata
extends|extends
name|BaseQueryMetadata
block|{
name|boolean
name|resolve
parameter_list|(
name|EntityResolver
name|resolver
parameter_list|,
name|EJBQLQuery
name|query
parameter_list|)
block|{
name|EJBQLCompiledExpression
name|expression
init|=
name|query
operator|.
name|getExpression
argument_list|(
name|resolver
argument_list|)
decl_stmt|;
name|resultSetMapping
operator|=
name|expression
operator|.
name|getResult
argument_list|()
operator|!=
literal|null
condition|?
operator|new
name|DefaultResultSetMetadata
argument_list|(
name|expression
operator|.
name|getResult
argument_list|()
argument_list|,
name|resolver
argument_list|)
else|:
literal|null
expr_stmt|;
name|ObjEntity
name|root
init|=
name|expression
operator|.
name|getRootDescriptor
argument_list|()
operator|.
name|getEntity
argument_list|()
decl_stmt|;
comment|// TODO: andrus, 4/3/2007 - generate cache key based on EJBQL statement
return|return
name|super
operator|.
name|resolve
argument_list|(
name|root
argument_list|,
name|resolver
argument_list|,
literal|null
argument_list|)
return|;
block|}
block|}
end_class

end_unit

