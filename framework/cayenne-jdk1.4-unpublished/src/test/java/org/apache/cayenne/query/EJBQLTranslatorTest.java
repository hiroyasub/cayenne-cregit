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
name|EJBQLExpression
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
name|ejbql
operator|.
name|EJBQLParser
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
name|ejbql
operator|.
name|EJBQLParserFactory
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_class
specifier|public
class|class
name|EJBQLTranslatorTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testSelect1
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
literal|"select a from Artist a"
argument_list|)
decl_stmt|;
name|EJBQLTranslator
name|tr
init|=
operator|new
name|EJBQLTranslator
argument_list|()
decl_stmt|;
name|select
operator|.
name|visit
argument_list|(
name|tr
argument_list|)
expr_stmt|;
comment|//        assertEquals(
comment|//                "Failed to translate: " + select,
comment|//                "SELECT t0.ARTIST_ID, t0.ARTIST_NAME, t0.DATE_OF_BIRTH FROM ARTIST t0",
comment|//                tr.getSql());
block|}
block|}
end_class

end_unit

