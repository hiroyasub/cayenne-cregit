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
name|ejbql
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
name|unit
operator|.
name|CayenneCase
import|;
end_import

begin_class
specifier|public
class|class
name|EJBQLCompiledExpressionTest
extends|extends
name|CayenneCase
block|{
specifier|public
name|void
name|testGetEntityDescriptor
parameter_list|()
block|{
name|EntityResolver
name|resolver
init|=
name|getDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
decl_stmt|;
name|EJBQLParser
name|parser
init|=
name|EJBQLParserFactory
operator|.
name|getParser
argument_list|()
decl_stmt|;
name|EJBQLCompiledExpression
name|select
init|=
name|parser
operator|.
name|compile
argument_list|(
literal|"select a from Artist a"
argument_list|,
name|resolver
argument_list|)
decl_stmt|;
comment|// assertNotNull(select.getEntityDescriptor("a"));
comment|// assertSame(resolver.getObjEntity("Artist"), select.getEntityDescriptor("a"));
block|}
specifier|public
name|void
name|testGetEntityDescriptorCaseSensitivity
parameter_list|()
block|{
name|EntityResolver
name|resolver
init|=
name|getDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
decl_stmt|;
name|EJBQLParser
name|parser
init|=
name|EJBQLParserFactory
operator|.
name|getParser
argument_list|()
decl_stmt|;
name|EJBQLCompiledExpression
name|select1
init|=
name|parser
operator|.
name|compile
argument_list|(
literal|"select a from Artist a"
argument_list|,
name|resolver
argument_list|)
decl_stmt|;
comment|// assertNotNull(select1.getEntityDescriptor("a"));
comment|// assertNotNull(select1.getEntityDescriptor("A"));
name|EJBQLCompiledExpression
name|select2
init|=
name|parser
operator|.
name|compile
argument_list|(
literal|"select A from Artist A"
argument_list|,
name|resolver
argument_list|)
decl_stmt|;
comment|// assertNotNull(select2.getEntityDescriptor("a"));
comment|// assertNotNull(select2.getEntityDescriptor("A"));
name|EJBQLCompiledExpression
name|select3
init|=
name|parser
operator|.
name|compile
argument_list|(
literal|"select a from Artist A"
argument_list|,
name|resolver
argument_list|)
decl_stmt|;
comment|// assertNotNull(select3.getEntityDescriptor("a"));
comment|// assertNotNull(select3.getEntityDescriptor("A"));
block|}
block|}
end_class

end_unit

