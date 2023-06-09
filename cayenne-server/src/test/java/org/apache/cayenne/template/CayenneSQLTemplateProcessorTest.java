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
name|template
package|;
end_package

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Types
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|HashMap
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
name|CayenneDataObject
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
name|DataObject
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
name|ObjectId
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
name|access
operator|.
name|jdbc
operator|.
name|SQLStatement
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
name|access
operator|.
name|translator
operator|.
name|ParameterBinding
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|*
import|;
end_import

begin_comment
comment|/**  * @since 4.1  */
end_comment

begin_class
specifier|public
class|class
name|CayenneSQLTemplateProcessorTest
block|{
specifier|private
name|CayenneSQLTemplateProcessor
name|processor
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|before
parameter_list|()
block|{
name|processor
operator|=
operator|new
name|CayenneSQLTemplateProcessor
argument_list|(
operator|new
name|DefaultTemplateContextFactory
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testProcessTemplateUnchanged1
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
name|processor
operator|.
name|processTemplate
argument_list|(
name|sqlTemplate
argument_list|,
name|Collections
operator|.
name|emptyMap
argument_list|()
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
block|}
annotation|@
name|Test
specifier|public
name|void
name|testProcessTemplateUnchanged2
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|sqlTemplate
init|=
literal|"SELECT a.b as XYZ FROM $SYSTEM_TABLE"
decl_stmt|;
name|SQLStatement
name|compiled
init|=
name|processor
operator|.
name|processTemplate
argument_list|(
name|sqlTemplate
argument_list|,
name|Collections
operator|.
name|emptyMap
argument_list|()
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
block|}
annotation|@
name|Test
specifier|public
name|void
name|testProcessTemplateSimpleDynamicContent
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|sqlTemplate
init|=
literal|"SELECT * FROM ME WHERE $a"
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"a"
argument_list|,
literal|"VALUE_OF_A"
argument_list|)
decl_stmt|;
name|SQLStatement
name|compiled
init|=
name|processor
operator|.
name|processTemplate
argument_list|(
name|sqlTemplate
argument_list|,
name|map
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"SELECT * FROM ME WHERE VALUE_OF_A"
argument_list|,
name|compiled
operator|.
name|getSql
argument_list|()
argument_list|)
expr_stmt|;
comment|// bindings are not populated, since no "bind" macro is used.
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
block|}
annotation|@
name|Test
specifier|public
name|void
name|testProcessTemplateBind
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|sqlTemplate
init|=
literal|"SELECT * FROM ME WHERE "
operator|+
literal|"COLUMN1 = #bind($a 'VARCHAR') AND COLUMN2 = #bind($b 'INTEGER')"
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"a"
argument_list|,
literal|"VALUE_OF_A"
argument_list|)
decl_stmt|;
name|SQLStatement
name|compiled
init|=
name|processor
operator|.
name|processTemplate
argument_list|(
name|sqlTemplate
argument_list|,
name|map
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"SELECT * FROM ME WHERE COLUMN1 = ? AND COLUMN2 = ?"
argument_list|,
name|compiled
operator|.
name|getSql
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|compiled
operator|.
name|getBindings
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertBindingValue
argument_list|(
literal|"VALUE_OF_A"
argument_list|,
name|compiled
operator|.
name|getBindings
argument_list|()
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertBindingValue
argument_list|(
literal|null
argument_list|,
name|compiled
operator|.
name|getBindings
argument_list|()
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testProcessTemplateBindGuessVarchar
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|sqlTemplate
init|=
literal|"SELECT * FROM ME WHERE COLUMN1 = #bind($a)"
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"a"
argument_list|,
literal|"VALUE_OF_A"
argument_list|)
decl_stmt|;
name|SQLStatement
name|compiled
init|=
name|processor
operator|.
name|processTemplate
argument_list|(
name|sqlTemplate
argument_list|,
name|map
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|compiled
operator|.
name|getBindings
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertBindingType
argument_list|(
name|Types
operator|.
name|VARCHAR
argument_list|,
name|compiled
operator|.
name|getBindings
argument_list|()
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testProcessTemplateBindGuessInteger
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|sqlTemplate
init|=
literal|"SELECT * FROM ME WHERE COLUMN1 = #bind($a)"
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"a"
argument_list|,
literal|4
argument_list|)
decl_stmt|;
name|SQLStatement
name|compiled
init|=
name|processor
operator|.
name|processTemplate
argument_list|(
name|sqlTemplate
argument_list|,
name|map
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|compiled
operator|.
name|getBindings
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertBindingType
argument_list|(
name|Types
operator|.
name|INTEGER
argument_list|,
name|compiled
operator|.
name|getBindings
argument_list|()
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testProcessTemplateBindEqual
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|sqlTemplate
init|=
literal|"SELECT * FROM ME WHERE COLUMN #bindEqual($a 'VARCHAR')"
decl_stmt|;
name|SQLStatement
name|compiled
init|=
name|processor
operator|.
name|processTemplate
argument_list|(
name|sqlTemplate
argument_list|,
name|Collections
operator|.
name|emptyMap
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"SELECT * FROM ME WHERE COLUMN IS NULL"
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
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"a"
argument_list|,
literal|"VALUE_OF_A"
argument_list|)
decl_stmt|;
name|compiled
operator|=
name|processor
operator|.
name|processTemplate
argument_list|(
name|sqlTemplate
argument_list|,
name|map
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"SELECT * FROM ME WHERE COLUMN = ?"
argument_list|,
name|compiled
operator|.
name|getSql
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|compiled
operator|.
name|getBindings
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertBindingValue
argument_list|(
literal|"VALUE_OF_A"
argument_list|,
name|compiled
operator|.
name|getBindings
argument_list|()
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testProcessTemplateBindNotEqual
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|sqlTemplate
init|=
literal|"SELECT * FROM ME WHERE COLUMN #bindNotEqual($a 'VARCHAR')"
decl_stmt|;
name|SQLStatement
name|compiled
init|=
name|processor
operator|.
name|processTemplate
argument_list|(
name|sqlTemplate
argument_list|,
name|Collections
operator|.
name|emptyMap
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"SELECT * FROM ME WHERE COLUMN IS NOT NULL"
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
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"a"
argument_list|,
literal|"VALUE_OF_A"
argument_list|)
decl_stmt|;
name|compiled
operator|=
name|processor
operator|.
name|processTemplate
argument_list|(
name|sqlTemplate
argument_list|,
name|map
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"SELECT * FROM ME WHERE COLUMN<> ?"
argument_list|,
name|compiled
operator|.
name|getSql
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|compiled
operator|.
name|getBindings
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertBindingValue
argument_list|(
literal|"VALUE_OF_A"
argument_list|,
name|compiled
operator|.
name|getBindings
argument_list|()
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testProcessTemplateID
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|sqlTemplate
init|=
literal|"SELECT * FROM ME WHERE COLUMN1 = #bind($helper.cayenneExp($a, 'db:ID_COLUMN'))"
decl_stmt|;
name|DataObject
name|dataObject
init|=
operator|new
name|CayenneDataObject
argument_list|()
decl_stmt|;
name|dataObject
operator|.
name|setObjectId
argument_list|(
name|ObjectId
operator|.
name|of
argument_list|(
literal|"T"
argument_list|,
literal|"ID_COLUMN"
argument_list|,
literal|5
argument_list|)
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"a"
argument_list|,
name|dataObject
argument_list|)
decl_stmt|;
name|SQLStatement
name|compiled
init|=
name|processor
operator|.
name|processTemplate
argument_list|(
name|sqlTemplate
argument_list|,
name|map
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"SELECT * FROM ME WHERE COLUMN1 = ?"
argument_list|,
name|compiled
operator|.
name|getSql
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|compiled
operator|.
name|getBindings
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertBindingValue
argument_list|(
literal|5
argument_list|,
name|compiled
operator|.
name|getBindings
argument_list|()
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testProcessTemplateNotEqualID
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|sqlTemplate
init|=
literal|"SELECT * FROM ME WHERE "
operator|+
literal|"COLUMN1 #bindNotEqual($helper.cayenneExp($a, 'db:ID_COLUMN1')) "
operator|+
literal|"AND COLUMN2 #bindNotEqual($helper.cayenneExp($a, 'db:ID_COLUMN2'))"
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|idMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|idMap
operator|.
name|put
argument_list|(
literal|"ID_COLUMN1"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|idMap
operator|.
name|put
argument_list|(
literal|"ID_COLUMN2"
argument_list|,
literal|"aaa"
argument_list|)
expr_stmt|;
name|ObjectId
name|id
init|=
name|ObjectId
operator|.
name|of
argument_list|(
literal|"T"
argument_list|,
name|idMap
argument_list|)
decl_stmt|;
name|DataObject
name|dataObject
init|=
operator|new
name|CayenneDataObject
argument_list|()
decl_stmt|;
name|dataObject
operator|.
name|setObjectId
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"a"
argument_list|,
name|dataObject
argument_list|)
decl_stmt|;
name|SQLStatement
name|compiled
init|=
name|processor
operator|.
name|processTemplate
argument_list|(
name|sqlTemplate
argument_list|,
name|map
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"SELECT * FROM ME WHERE COLUMN1<> ? AND COLUMN2<> ?"
argument_list|,
name|compiled
operator|.
name|getSql
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|compiled
operator|.
name|getBindings
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertBindingValue
argument_list|(
literal|3
argument_list|,
name|compiled
operator|.
name|getBindings
argument_list|()
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertBindingValue
argument_list|(
literal|"aaa"
argument_list|,
name|compiled
operator|.
name|getBindings
argument_list|()
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testProcessTemplateConditions
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|sqlTemplate
init|=
literal|"SELECT * FROM ME #if($a) WHERE COLUMN1> #bind($a)#end"
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"a"
argument_list|,
literal|"VALUE_OF_A"
argument_list|)
decl_stmt|;
name|SQLStatement
name|compiled
init|=
name|processor
operator|.
name|processTemplate
argument_list|(
name|sqlTemplate
argument_list|,
name|map
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"SELECT * FROM ME  WHERE COLUMN1> ?"
argument_list|,
name|compiled
operator|.
name|getSql
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|compiled
operator|.
name|getBindings
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertBindingValue
argument_list|(
literal|"VALUE_OF_A"
argument_list|,
name|compiled
operator|.
name|getBindings
argument_list|()
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|compiled
operator|=
name|processor
operator|.
name|processTemplate
argument_list|(
name|sqlTemplate
argument_list|,
name|Collections
operator|.
name|emptyMap
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"SELECT * FROM ME "
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
block|}
annotation|@
name|Test
specifier|public
name|void
name|testProcessTemplateBindCollection
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|sqlTemplate
init|=
literal|"SELECT * FROM ME WHERE COLUMN IN (#bind($list 'VARCHAR'))"
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"list"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"c"
argument_list|)
argument_list|)
decl_stmt|;
name|SQLStatement
name|compiled
init|=
name|processor
operator|.
name|processTemplate
argument_list|(
name|sqlTemplate
argument_list|,
name|map
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"SELECT * FROM ME WHERE COLUMN IN (?,?,?)"
argument_list|,
name|compiled
operator|.
name|getSql
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|compiled
operator|.
name|getBindings
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertBindingValue
argument_list|(
literal|"a"
argument_list|,
name|compiled
operator|.
name|getBindings
argument_list|()
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertBindingValue
argument_list|(
literal|"b"
argument_list|,
name|compiled
operator|.
name|getBindings
argument_list|()
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|assertBindingValue
argument_list|(
literal|"c"
argument_list|,
name|compiled
operator|.
name|getBindings
argument_list|()
index|[
literal|2
index|]
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testUnknownDirective
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|sqlTemplate
init|=
literal|"SELECT #from(1) FROM a"
decl_stmt|;
name|SQLStatement
name|compiled
init|=
name|processor
operator|.
name|processTemplate
argument_list|(
name|sqlTemplate
argument_list|,
name|Collections
operator|.
name|emptyMap
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"SELECT  FROM a"
argument_list|,
name|compiled
operator|.
name|getSql
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertBindingValue
parameter_list|(
name|Object
name|expectedValue
parameter_list|,
name|Object
name|binding
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|"Not a binding!"
argument_list|,
name|binding
operator|instanceof
name|ParameterBinding
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedValue
argument_list|,
operator|(
operator|(
name|ParameterBinding
operator|)
name|binding
operator|)
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertBindingType
parameter_list|(
name|Integer
name|expectedType
parameter_list|,
name|Object
name|binding
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|"Not a binding!"
argument_list|,
name|binding
operator|instanceof
name|ParameterBinding
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedType
argument_list|,
operator|(
operator|(
name|ParameterBinding
operator|)
name|binding
operator|)
operator|.
name|getJdbcType
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

