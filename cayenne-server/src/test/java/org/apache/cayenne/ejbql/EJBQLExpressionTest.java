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
name|junit
operator|.
name|Test
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
name|fail
import|;
end_import

begin_class
specifier|public
class|class
name|EJBQLExpressionTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testDbPath
parameter_list|()
block|{
name|EJBQLParser
name|parser
init|=
name|EJBQLParserFactory
operator|.
name|getParser
argument_list|()
decl_stmt|;
name|EJBQLExpression
name|select
init|=
name|parser
operator|.
name|parse
argument_list|(
literal|"select p from Painting p WHERE db:p.toArtist.ARTIST_NAME = 'a'"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|select
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEnumPath
parameter_list|()
block|{
name|EJBQLParser
name|parser
init|=
name|EJBQLParserFactory
operator|.
name|getParser
argument_list|()
decl_stmt|;
name|EJBQLExpression
name|select
init|=
name|parser
operator|.
name|parse
argument_list|(
literal|"select p from Painting p WHERE p.toArtist.ARTIST_NAME = enum:org.apache.cayenne.ejbql.EJBQLEnum1.X"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|select
argument_list|)
expr_stmt|;
block|}
comment|/** 	 *<p> 	 * This should not parse because there are multiple non-bracketed 	 * parameters. 	 *</p> 	 */
annotation|@
name|Test
specifier|public
name|void
name|testInWithMultipleStringPositionalParameter_withoutBrackets
parameter_list|()
block|{
name|EJBQLParser
name|parser
init|=
name|EJBQLParserFactory
operator|.
name|getParser
argument_list|()
decl_stmt|;
try|try
block|{
name|parser
operator|.
name|parse
argument_list|(
literal|"select p from Painting p WHERE p.toArtist IN ?1, ?2"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"a test in clause with multiple unbracketed parameters parsed; should not be possible"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|EJBQLException
name|ejbqlE
parameter_list|)
block|{
comment|// expected; should not have parsed
block|}
catch|catch
parameter_list|(
name|Throwable
name|th
parameter_list|)
block|{
name|fail
argument_list|(
literal|"expected an instance of "
operator|+
name|EJBQLException
operator|.
name|class
operator|.
name|getSimpleName
argument_list|()
operator|+
literal|" to be thrown, but; "
operator|+
name|th
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
operator|+
literal|" was thrown"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

