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
name|ObjectContext
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
name|exp
operator|.
name|Expression
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
name|exp
operator|.
name|parser
operator|.
name|ASTBitwiseAnd
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
name|exp
operator|.
name|parser
operator|.
name|ASTBitwiseNot
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
name|exp
operator|.
name|parser
operator|.
name|ASTBitwiseOr
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
name|exp
operator|.
name|parser
operator|.
name|ASTBitwiseXor
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
name|exp
operator|.
name|parser
operator|.
name|ASTEqual
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
name|exp
operator|.
name|parser
operator|.
name|ASTGreater
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
name|exp
operator|.
name|parser
operator|.
name|ASTObjPath
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
name|exp
operator|.
name|parser
operator|.
name|ASTScalar
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
name|test
operator|.
name|jdbc
operator|.
name|DBHelper
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
name|test
operator|.
name|jdbc
operator|.
name|TableHelper
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
name|testdo
operator|.
name|return_types
operator|.
name|ReturnTypesMap1
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
name|UnitDbAdapter
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
name|Test
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

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|CayenneProjects
operator|.
name|RETURN_TYPES_PROJECT
argument_list|)
specifier|public
class|class
name|SelectQueryReturnTypesIT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|private
name|ObjectContext
name|context
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|DBHelper
name|dbHelper
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|UnitDbAdapter
name|accessStackAdapter
decl_stmt|;
specifier|protected
name|void
name|createNumericsDataSet
parameter_list|()
throws|throws
name|Exception
block|{
name|TableHelper
name|tNumerics
init|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"TYPES_MAPPING_TEST1"
argument_list|)
decl_stmt|;
name|tNumerics
operator|.
name|setColumns
argument_list|(
literal|"AAAID"
argument_list|,
literal|"INTEGER_COLUMN"
argument_list|)
expr_stmt|;
name|tNumerics
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|tNumerics
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|tNumerics
operator|.
name|insert
argument_list|(
literal|3
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|tNumerics
operator|.
name|insert
argument_list|(
literal|4
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|tNumerics
operator|.
name|insert
argument_list|(
literal|5
argument_list|,
literal|4
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectBitwiseNot
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|accessStackAdapter
operator|.
name|supportsBitwiseOps
argument_list|()
condition|)
block|{
return|return;
block|}
name|createNumericsDataSet
argument_list|()
expr_stmt|;
comment|// to simplify result checking, do double NOT
name|Expression
name|left
init|=
operator|new
name|ASTBitwiseNot
argument_list|(
operator|new
name|ASTBitwiseNot
argument_list|(
operator|new
name|ASTObjPath
argument_list|(
name|ReturnTypesMap1
operator|.
name|INTEGER_COLUMN_PROPERTY
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|Expression
name|right
init|=
operator|new
name|ASTScalar
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|Expression
name|greater
init|=
operator|new
name|ASTGreater
argument_list|()
decl_stmt|;
name|greater
operator|.
name|setOperand
argument_list|(
literal|0
argument_list|,
name|left
argument_list|)
expr_stmt|;
name|greater
operator|.
name|setOperand
argument_list|(
literal|1
argument_list|,
name|right
argument_list|)
expr_stmt|;
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|ReturnTypesMap1
operator|.
name|class
argument_list|)
decl_stmt|;
name|query
operator|.
name|setQualifier
argument_list|(
name|greater
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ReturnTypesMap1
argument_list|>
name|objects
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectBitwiseOr
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|accessStackAdapter
operator|.
name|supportsBitwiseOps
argument_list|()
condition|)
block|{
return|return;
block|}
name|createNumericsDataSet
argument_list|()
expr_stmt|;
comment|// to simplify result checking, do double NOT
name|Expression
name|left
init|=
operator|new
name|ASTBitwiseOr
argument_list|(
operator|new
name|Object
index|[]
block|{
operator|new
name|ASTObjPath
argument_list|(
name|ReturnTypesMap1
operator|.
name|INTEGER_COLUMN_PROPERTY
argument_list|)
block|,
operator|new
name|ASTScalar
argument_list|(
literal|1
argument_list|)
block|}
argument_list|)
decl_stmt|;
name|Expression
name|right
init|=
operator|new
name|ASTScalar
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|Expression
name|equal
init|=
operator|new
name|ASTEqual
argument_list|()
decl_stmt|;
name|equal
operator|.
name|setOperand
argument_list|(
literal|0
argument_list|,
name|left
argument_list|)
expr_stmt|;
name|equal
operator|.
name|setOperand
argument_list|(
literal|1
argument_list|,
name|right
argument_list|)
expr_stmt|;
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|ReturnTypesMap1
operator|.
name|class
argument_list|)
decl_stmt|;
name|query
operator|.
name|setQualifier
argument_list|(
name|equal
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ReturnTypesMap1
argument_list|>
name|objects
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectBitwiseAnd
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|accessStackAdapter
operator|.
name|supportsBitwiseOps
argument_list|()
condition|)
block|{
return|return;
block|}
name|createNumericsDataSet
argument_list|()
expr_stmt|;
comment|// to simplify result checking, do double NOT
name|Expression
name|left
init|=
operator|new
name|ASTBitwiseAnd
argument_list|(
operator|new
name|Object
index|[]
block|{
operator|new
name|ASTObjPath
argument_list|(
name|ReturnTypesMap1
operator|.
name|INTEGER_COLUMN_PROPERTY
argument_list|)
block|,
operator|new
name|ASTScalar
argument_list|(
literal|1
argument_list|)
block|}
argument_list|)
decl_stmt|;
name|Expression
name|right
init|=
operator|new
name|ASTScalar
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Expression
name|equal
init|=
operator|new
name|ASTEqual
argument_list|()
decl_stmt|;
name|equal
operator|.
name|setOperand
argument_list|(
literal|0
argument_list|,
name|left
argument_list|)
expr_stmt|;
name|equal
operator|.
name|setOperand
argument_list|(
literal|1
argument_list|,
name|right
argument_list|)
expr_stmt|;
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|ReturnTypesMap1
operator|.
name|class
argument_list|)
decl_stmt|;
name|query
operator|.
name|setQualifier
argument_list|(
name|equal
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ReturnTypesMap1
argument_list|>
name|objects
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectBitwiseXor
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|accessStackAdapter
operator|.
name|supportsBitwiseOps
argument_list|()
condition|)
block|{
return|return;
block|}
name|createNumericsDataSet
argument_list|()
expr_stmt|;
comment|// to simplify result checking, do double NOT
name|Expression
name|left
init|=
operator|new
name|ASTBitwiseXor
argument_list|(
operator|new
name|Object
index|[]
block|{
operator|new
name|ASTObjPath
argument_list|(
name|ReturnTypesMap1
operator|.
name|INTEGER_COLUMN_PROPERTY
argument_list|)
block|,
operator|new
name|ASTScalar
argument_list|(
literal|1
argument_list|)
block|}
argument_list|)
decl_stmt|;
name|Expression
name|right
init|=
operator|new
name|ASTScalar
argument_list|(
literal|5
argument_list|)
decl_stmt|;
name|Expression
name|equal
init|=
operator|new
name|ASTEqual
argument_list|()
decl_stmt|;
name|equal
operator|.
name|setOperand
argument_list|(
literal|0
argument_list|,
name|left
argument_list|)
expr_stmt|;
name|equal
operator|.
name|setOperand
argument_list|(
literal|1
argument_list|,
name|right
argument_list|)
expr_stmt|;
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|ReturnTypesMap1
operator|.
name|class
argument_list|)
decl_stmt|;
name|query
operator|.
name|setQualifier
argument_list|(
name|equal
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ReturnTypesMap1
argument_list|>
name|objects
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|objects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIntegerColumn
argument_list|()
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

