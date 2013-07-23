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
name|access
operator|.
name|jdbc
package|;
end_package

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
name|SQLTemplateProcessorSelectTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testProcessTemplateUnchanged
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|sqlTemplate
init|=
literal|"SELECT * FROM ME"
decl_stmt|;
name|SQLStatement
name|compiled
init|=
operator|new
name|SQLTemplateProcessor
argument_list|()
operator|.
name|processTemplate
argument_list|(
name|sqlTemplate
argument_list|,
name|Collections
operator|.
name|EMPTY_MAP
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|sqlTemplate
argument_list|,
name|compiled
operator|.
name|getSql
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|compiled
operator|.
name|getBindings
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|compiled
operator|.
name|getResultColumns
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testProcessSelectTemplate1
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|sqlTemplate
init|=
literal|"SELECT #result('A') FROM ME"
decl_stmt|;
name|SQLStatement
name|compiled
init|=
operator|new
name|SQLTemplateProcessor
argument_list|()
operator|.
name|processTemplate
argument_list|(
name|sqlTemplate
argument_list|,
name|Collections
operator|.
name|EMPTY_MAP
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"SELECT A FROM ME"
argument_list|,
name|compiled
operator|.
name|getSql
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|compiled
operator|.
name|getBindings
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|compiled
operator|.
name|getResultColumns
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"A"
argument_list|,
name|compiled
operator|.
name|getResultColumns
argument_list|()
index|[
literal|0
index|]
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|compiled
operator|.
name|getResultColumns
argument_list|()
index|[
literal|0
index|]
operator|.
name|getJavaClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testProcessSelectTemplate2
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|sqlTemplate
init|=
literal|"SELECT #result('A' 'String') FROM ME"
decl_stmt|;
name|SQLStatement
name|compiled
init|=
operator|new
name|SQLTemplateProcessor
argument_list|()
operator|.
name|processTemplate
argument_list|(
name|sqlTemplate
argument_list|,
name|Collections
operator|.
name|EMPTY_MAP
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"SELECT A FROM ME"
argument_list|,
name|compiled
operator|.
name|getSql
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|compiled
operator|.
name|getBindings
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|compiled
operator|.
name|getResultColumns
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"A"
argument_list|,
name|compiled
operator|.
name|getResultColumns
argument_list|()
index|[
literal|0
index|]
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"java.lang.String"
argument_list|,
name|compiled
operator|.
name|getResultColumns
argument_list|()
index|[
literal|0
index|]
operator|.
name|getJavaClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testProcessSelectTemplate3
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|sqlTemplate
init|=
literal|"SELECT #result('A' 'String' 'B') FROM ME"
decl_stmt|;
name|SQLStatement
name|compiled
init|=
operator|new
name|SQLTemplateProcessor
argument_list|()
operator|.
name|processTemplate
argument_list|(
name|sqlTemplate
argument_list|,
name|Collections
operator|.
name|EMPTY_MAP
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"SELECT A AS B FROM ME"
argument_list|,
name|compiled
operator|.
name|getSql
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|compiled
operator|.
name|getBindings
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|compiled
operator|.
name|getResultColumns
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|ColumnDescriptor
name|column
init|=
name|compiled
operator|.
name|getResultColumns
argument_list|()
index|[
literal|0
index|]
decl_stmt|;
name|assertEquals
argument_list|(
literal|"A"
argument_list|,
name|column
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"B"
argument_list|,
name|column
operator|.
name|getDataRowKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"java.lang.String"
argument_list|,
name|column
operator|.
name|getJavaClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testProcessSelectTemplate4
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|sqlTemplate
init|=
literal|"SELECT #result('A'), #result('B'), #result('C') FROM ME"
decl_stmt|;
name|SQLStatement
name|compiled
init|=
operator|new
name|SQLTemplateProcessor
argument_list|()
operator|.
name|processTemplate
argument_list|(
name|sqlTemplate
argument_list|,
name|Collections
operator|.
name|EMPTY_MAP
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"SELECT A, B, C FROM ME"
argument_list|,
name|compiled
operator|.
name|getSql
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|compiled
operator|.
name|getBindings
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|compiled
operator|.
name|getResultColumns
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"A"
argument_list|,
name|compiled
operator|.
name|getResultColumns
argument_list|()
index|[
literal|0
index|]
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"B"
argument_list|,
name|compiled
operator|.
name|getResultColumns
argument_list|()
index|[
literal|1
index|]
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"C"
argument_list|,
name|compiled
operator|.
name|getResultColumns
argument_list|()
index|[
literal|2
index|]
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
