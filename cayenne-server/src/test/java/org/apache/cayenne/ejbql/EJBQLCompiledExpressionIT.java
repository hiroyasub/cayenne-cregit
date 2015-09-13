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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertSame
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
name|configuration
operator|.
name|server
operator|.
name|ServerRuntime
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
name|di
operator|.
name|Inject
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
name|unit
operator|.
name|di
operator|.
name|server
operator|.
name|CayenneProjects
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
name|di
operator|.
name|server
operator|.
name|ServerCase
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
name|di
operator|.
name|server
operator|.
name|UseServerRuntime
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|CayenneProjects
operator|.
name|TESTMAP_PROJECT
argument_list|)
specifier|public
class|class
name|EJBQLCompiledExpressionIT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|protected
name|ServerRuntime
name|runtime
decl_stmt|;
specifier|private
name|EJBQLParser
name|parser
decl_stmt|;
specifier|private
name|EntityResolver
name|resolver
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|before
parameter_list|()
block|{
name|resolver
operator|=
name|runtime
operator|.
name|getDataDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
expr_stmt|;
name|parser
operator|=
name|EJBQLParserFactory
operator|.
name|getParser
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetSource
parameter_list|()
block|{
name|String
name|source
init|=
literal|"select a from Artist a"
decl_stmt|;
name|EJBQLCompiledExpression
name|select
init|=
name|parser
operator|.
name|compile
argument_list|(
name|source
argument_list|,
name|resolver
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|source
argument_list|,
name|select
operator|.
name|getSource
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetExpression
parameter_list|()
block|{
name|String
name|source
init|=
literal|"select a from Artist a"
decl_stmt|;
name|EJBQLCompiledExpression
name|select
init|=
name|parser
operator|.
name|compile
argument_list|(
name|source
argument_list|,
name|resolver
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|select
operator|.
name|getExpression
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetEntityDescriptor
parameter_list|()
block|{
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
name|assertNotNull
argument_list|(
name|select
operator|.
name|getEntityDescriptor
argument_list|(
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|resolver
operator|.
name|getClassDescriptor
argument_list|(
literal|"Artist"
argument_list|)
argument_list|,
name|select
operator|.
name|getEntityDescriptor
argument_list|(
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
name|EJBQLCompiledExpression
name|select1
init|=
name|parser
operator|.
name|compile
argument_list|(
literal|"select p from Painting p WHERE p.toArtist.artistName = 'a'"
argument_list|,
name|resolver
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|select1
operator|.
name|getEntityDescriptor
argument_list|(
literal|"p"
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|resolver
operator|.
name|getClassDescriptor
argument_list|(
literal|"Painting"
argument_list|)
argument_list|,
name|select1
operator|.
name|getEntityDescriptor
argument_list|(
literal|"p"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|select1
operator|.
name|getEntityDescriptor
argument_list|(
literal|"p.toArtist"
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|resolver
operator|.
name|getClassDescriptor
argument_list|(
literal|"Artist"
argument_list|)
argument_list|,
name|select1
operator|.
name|getEntityDescriptor
argument_list|(
literal|"p.toArtist"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetRootDescriptor
parameter_list|()
block|{
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
name|assertSame
argument_list|(
literal|"Root is not detected: "
operator|+
name|select
operator|.
name|getExpression
argument_list|()
argument_list|,
name|resolver
operator|.
name|getClassDescriptor
argument_list|(
literal|"Artist"
argument_list|)
argument_list|,
name|select
operator|.
name|getRootDescriptor
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetEntityDescriptorCaseSensitivity
parameter_list|()
block|{
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
name|assertNotNull
argument_list|(
name|select1
operator|.
name|getEntityDescriptor
argument_list|(
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|select1
operator|.
name|getEntityDescriptor
argument_list|(
literal|"A"
argument_list|)
argument_list|)
expr_stmt|;
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
name|assertNotNull
argument_list|(
name|select2
operator|.
name|getEntityDescriptor
argument_list|(
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|select2
operator|.
name|getEntityDescriptor
argument_list|(
literal|"A"
argument_list|)
argument_list|)
expr_stmt|;
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
name|assertNotNull
argument_list|(
name|select3
operator|.
name|getEntityDescriptor
argument_list|(
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|select3
operator|.
name|getEntityDescriptor
argument_list|(
literal|"A"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

