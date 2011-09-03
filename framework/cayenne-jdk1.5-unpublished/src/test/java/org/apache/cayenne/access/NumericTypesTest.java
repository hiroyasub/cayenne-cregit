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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|math
operator|.
name|BigDecimal
import|;
end_import

begin_import
import|import
name|java
operator|.
name|math
operator|.
name|BigInteger
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
name|ExpressionFactory
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
name|SelectQuery
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
name|testmap
operator|.
name|BigDecimalEntity
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
name|testmap
operator|.
name|BigIntegerEntity
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
name|testmap
operator|.
name|BitTestEntity
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
name|testmap
operator|.
name|BooleanTestEntity
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
name|testmap
operator|.
name|DecimalPKTest1
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
name|testmap
operator|.
name|DecimalPKTestEntity
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
name|testmap
operator|.
name|LongEntity
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
name|testmap
operator|.
name|SmallintTestEntity
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
name|testmap
operator|.
name|TinyintTestEntity
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

begin_comment
comment|/**  */
end_comment

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|ServerCase
operator|.
name|TESTMAP_PROJECT
argument_list|)
specifier|public
class|class
name|NumericTypesTest
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|protected
name|DataContext
name|context
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|DataContext
name|context1
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|ServerRuntime
name|runtime
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|DBHelper
name|dbHelper
decl_stmt|;
specifier|protected
name|TableHelper
name|tSmallintTest
decl_stmt|;
specifier|protected
name|TableHelper
name|tTinyintTest
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|void
name|setUpAfterInjection
parameter_list|()
throws|throws
name|Exception
block|{
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"BOOLEAN_TEST"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"SMALLINT_TEST"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"TINYINT_TEST"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"DECIMAL_PK_TST"
argument_list|)
expr_stmt|;
name|tSmallintTest
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"SMALLINT_TEST"
argument_list|)
expr_stmt|;
name|tSmallintTest
operator|.
name|setColumns
argument_list|(
literal|"ID"
argument_list|,
literal|"SMALLINT_COL"
argument_list|)
expr_stmt|;
name|tTinyintTest
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"TINYINT_TEST"
argument_list|)
expr_stmt|;
name|tTinyintTest
operator|.
name|setColumns
argument_list|(
literal|"ID"
argument_list|,
literal|"TINYINT_COL"
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|createShortDataSet
parameter_list|()
throws|throws
name|Exception
block|{
name|tSmallintTest
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|9999
argument_list|)
expr_stmt|;
name|tSmallintTest
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|3333
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|createTinyintDataSet
parameter_list|()
throws|throws
name|Exception
block|{
name|tTinyintTest
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|81
argument_list|)
expr_stmt|;
name|tTinyintTest
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|50
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testLong
parameter_list|()
throws|throws
name|Exception
block|{
name|LongEntity
name|test
init|=
name|context
operator|.
name|newObject
argument_list|(
name|LongEntity
operator|.
name|class
argument_list|)
decl_stmt|;
name|Long
name|i
init|=
operator|new
name|Long
argument_list|(
name|Integer
operator|.
name|MAX_VALUE
operator|+
literal|10l
argument_list|)
decl_stmt|;
name|test
operator|.
name|setLongField
argument_list|(
name|i
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
name|LongEntity
operator|.
name|class
argument_list|)
decl_stmt|;
name|LongEntity
name|testRead
init|=
operator|(
name|LongEntity
operator|)
name|context
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|testRead
operator|.
name|getLongField
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|i
argument_list|,
name|testRead
operator|.
name|getLongField
argument_list|()
argument_list|)
expr_stmt|;
name|test
operator|.
name|setLongField
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testBigInteger
parameter_list|()
throws|throws
name|Exception
block|{
name|BigIntegerEntity
name|test
init|=
name|context
operator|.
name|newObject
argument_list|(
name|BigIntegerEntity
operator|.
name|class
argument_list|)
decl_stmt|;
name|BigInteger
name|i
init|=
operator|new
name|BigInteger
argument_list|(
literal|"1234567890"
argument_list|)
decl_stmt|;
name|test
operator|.
name|setBigIntegerField
argument_list|(
name|i
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
name|BigIntegerEntity
operator|.
name|class
argument_list|)
decl_stmt|;
name|BigIntegerEntity
name|testRead
init|=
operator|(
name|BigIntegerEntity
operator|)
name|context
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|testRead
operator|.
name|getBigIntegerField
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|i
argument_list|,
name|testRead
operator|.
name|getBigIntegerField
argument_list|()
argument_list|)
expr_stmt|;
name|test
operator|.
name|setBigIntegerField
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testBigDecimal
parameter_list|()
throws|throws
name|Exception
block|{
name|BigDecimalEntity
name|test
init|=
name|context
operator|.
name|newObject
argument_list|(
name|BigDecimalEntity
operator|.
name|class
argument_list|)
decl_stmt|;
name|BigDecimal
name|i
init|=
operator|new
name|BigDecimal
argument_list|(
literal|"1234567890.44"
argument_list|)
decl_stmt|;
name|test
operator|.
name|setBigDecimalField
argument_list|(
name|i
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
name|BigDecimalEntity
operator|.
name|class
argument_list|)
decl_stmt|;
name|BigDecimalEntity
name|testRead
init|=
operator|(
name|BigDecimalEntity
operator|)
name|context
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|testRead
operator|.
name|getBigDecimalField
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|i
argument_list|,
name|testRead
operator|.
name|getBigDecimalField
argument_list|()
argument_list|)
expr_stmt|;
name|test
operator|.
name|setBigDecimalField
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testShortInQualifier
parameter_list|()
throws|throws
name|Exception
block|{
name|createShortDataSet
argument_list|()
expr_stmt|;
comment|// test
name|Expression
name|qual
init|=
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"smallintCol"
argument_list|,
operator|new
name|Short
argument_list|(
literal|"9999"
argument_list|)
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|objects
init|=
name|context
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|SmallintTestEntity
operator|.
name|class
argument_list|,
name|qual
argument_list|)
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
name|SmallintTestEntity
name|object
init|=
operator|(
name|SmallintTestEntity
operator|)
name|objects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Short
argument_list|(
literal|"9999"
argument_list|)
argument_list|,
name|object
operator|.
name|getSmallintCol
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testShortInInsert
parameter_list|()
throws|throws
name|Exception
block|{
name|SmallintTestEntity
name|object
init|=
operator|(
name|SmallintTestEntity
operator|)
operator|(
name|context
operator|)
operator|.
name|newObject
argument_list|(
literal|"SmallintTestEntity"
argument_list|)
decl_stmt|;
name|object
operator|.
name|setSmallintCol
argument_list|(
operator|new
name|Short
argument_list|(
literal|"1"
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testTinyintInQualifier
parameter_list|()
throws|throws
name|Exception
block|{
name|createTinyintDataSet
argument_list|()
expr_stmt|;
comment|// test
name|Expression
name|qual
init|=
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"tinyintCol"
argument_list|,
operator|new
name|Byte
argument_list|(
operator|(
name|byte
operator|)
literal|81
argument_list|)
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|objects
init|=
name|context
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|TinyintTestEntity
operator|.
name|class
argument_list|,
name|qual
argument_list|)
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
name|TinyintTestEntity
name|object
init|=
operator|(
name|TinyintTestEntity
operator|)
name|objects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Byte
argument_list|(
operator|(
name|byte
operator|)
literal|81
argument_list|)
argument_list|,
name|object
operator|.
name|getTinyintCol
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testTinyintInInsert
parameter_list|()
throws|throws
name|Exception
block|{
name|TinyintTestEntity
name|object
init|=
operator|(
name|TinyintTestEntity
operator|)
operator|(
name|context
operator|)
operator|.
name|newObject
argument_list|(
literal|"TinyintTestEntity"
argument_list|)
decl_stmt|;
name|object
operator|.
name|setTinyintCol
argument_list|(
operator|new
name|Byte
argument_list|(
operator|(
name|byte
operator|)
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testBooleanBit
parameter_list|()
throws|throws
name|Exception
block|{
name|BitTestEntity
name|trueObject
init|=
operator|(
name|BitTestEntity
operator|)
name|context
operator|.
name|newObject
argument_list|(
literal|"BitTestEntity"
argument_list|)
decl_stmt|;
name|trueObject
operator|.
name|setBitColumn
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|BitTestEntity
name|falseObject
init|=
operator|(
name|BitTestEntity
operator|)
name|context
operator|.
name|newObject
argument_list|(
literal|"BitTestEntity"
argument_list|)
decl_stmt|;
name|falseObject
operator|.
name|setBitColumn
argument_list|(
name|Boolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|context
operator|.
name|invalidateObjects
argument_list|(
name|trueObject
argument_list|,
name|falseObject
argument_list|)
expr_stmt|;
comment|// fetch true...
name|Expression
name|trueQ
init|=
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"bitColumn"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|trueResult
init|=
name|context1
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|BitTestEntity
operator|.
name|class
argument_list|,
name|trueQ
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|trueResult
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|BitTestEntity
name|trueRefetched
init|=
operator|(
name|BitTestEntity
operator|)
name|trueResult
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|,
name|trueRefetched
operator|.
name|getBitColumn
argument_list|()
argument_list|)
expr_stmt|;
comment|// CAY-320. Simplifying the use of booleans to allow identity comparison.
name|assertNotSame
argument_list|(
name|trueRefetched
argument_list|,
name|trueObject
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|,
name|trueRefetched
operator|.
name|getBitColumn
argument_list|()
argument_list|)
expr_stmt|;
comment|// fetch false
name|Expression
name|falseQ
init|=
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"bitColumn"
argument_list|,
name|Boolean
operator|.
name|FALSE
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|falseResult
init|=
name|context1
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|BitTestEntity
operator|.
name|class
argument_list|,
name|falseQ
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|falseResult
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|BitTestEntity
name|falseRefetched
init|=
operator|(
name|BitTestEntity
operator|)
name|falseResult
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|FALSE
argument_list|,
name|falseRefetched
operator|.
name|getBitColumn
argument_list|()
argument_list|)
expr_stmt|;
comment|// CAY-320. Simplifying the use of booleans to allow identity comparison.
name|assertNotSame
argument_list|(
name|falseRefetched
argument_list|,
name|falseObject
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|Boolean
operator|.
name|FALSE
argument_list|,
name|falseRefetched
operator|.
name|getBitColumn
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testBooleanBoolean
parameter_list|()
throws|throws
name|Exception
block|{
comment|// populate (testing insert as well)
name|BooleanTestEntity
name|trueObject
init|=
operator|(
name|BooleanTestEntity
operator|)
name|context
operator|.
name|newObject
argument_list|(
literal|"BooleanTestEntity"
argument_list|)
decl_stmt|;
name|trueObject
operator|.
name|setBooleanColumn
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|BooleanTestEntity
name|falseObject
init|=
operator|(
name|BooleanTestEntity
operator|)
name|context
operator|.
name|newObject
argument_list|(
literal|"BooleanTestEntity"
argument_list|)
decl_stmt|;
name|falseObject
operator|.
name|setBooleanColumn
argument_list|(
name|Boolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|context
operator|.
name|invalidateObjects
argument_list|(
name|trueObject
argument_list|,
name|falseObject
argument_list|)
expr_stmt|;
comment|// fetch true...
name|Expression
name|trueQ
init|=
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"booleanColumn"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|trueResult
init|=
name|context1
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|BooleanTestEntity
operator|.
name|class
argument_list|,
name|trueQ
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|trueResult
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|BooleanTestEntity
name|trueRefetched
init|=
operator|(
name|BooleanTestEntity
operator|)
name|trueResult
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|,
name|trueRefetched
operator|.
name|getBooleanColumn
argument_list|()
argument_list|)
expr_stmt|;
comment|// CAY-320. Simplifying the use of booleans to allow identity comparison.
name|assertNotSame
argument_list|(
name|trueRefetched
argument_list|,
name|trueObject
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|,
name|trueRefetched
operator|.
name|getBooleanColumn
argument_list|()
argument_list|)
expr_stmt|;
comment|// fetch false
name|Expression
name|falseQ
init|=
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"booleanColumn"
argument_list|,
name|Boolean
operator|.
name|FALSE
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|falseResult
init|=
name|context1
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|BooleanTestEntity
operator|.
name|class
argument_list|,
name|falseQ
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|falseResult
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|BooleanTestEntity
name|falseRefetched
init|=
operator|(
name|BooleanTestEntity
operator|)
name|falseResult
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|FALSE
argument_list|,
name|falseRefetched
operator|.
name|getBooleanColumn
argument_list|()
argument_list|)
expr_stmt|;
comment|// CAY-320. Simplifying the use of booleans to allow identity comparison.
name|assertNotSame
argument_list|(
name|falseRefetched
argument_list|,
name|falseObject
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|Boolean
operator|.
name|FALSE
argument_list|,
name|falseRefetched
operator|.
name|getBooleanColumn
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testDecimalPK
parameter_list|()
throws|throws
name|Exception
block|{
comment|// populate (testing insert as well)
name|DecimalPKTestEntity
name|object
init|=
name|context
operator|.
name|newObject
argument_list|(
name|DecimalPKTestEntity
operator|.
name|class
argument_list|)
decl_stmt|;
name|object
operator|.
name|setName
argument_list|(
literal|"o1"
argument_list|)
expr_stmt|;
name|object
operator|.
name|setDecimalPK
argument_list|(
operator|new
name|BigDecimal
argument_list|(
literal|"1.25"
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|BigDecimal
argument_list|>
name|map
init|=
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"DECIMAL_PK"
argument_list|,
operator|new
name|BigDecimal
argument_list|(
literal|"1.25"
argument_list|)
argument_list|)
decl_stmt|;
name|ObjectId
name|syntheticId
init|=
operator|new
name|ObjectId
argument_list|(
literal|"DecimalPKTestEntity"
argument_list|,
name|map
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|object
argument_list|,
name|context
operator|.
name|localObject
argument_list|(
name|syntheticId
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|deleteObjects
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testDecimalPK1
parameter_list|()
throws|throws
name|Exception
block|{
comment|// populate (testing insert as well)
name|DecimalPKTest1
name|object
init|=
name|context
operator|.
name|newObject
argument_list|(
name|DecimalPKTest1
operator|.
name|class
argument_list|)
decl_stmt|;
name|object
operator|.
name|setName
argument_list|(
literal|"o2"
argument_list|)
expr_stmt|;
name|object
operator|.
name|setDecimalPK
argument_list|(
operator|new
name|Double
argument_list|(
literal|1.25
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Double
argument_list|>
name|map
init|=
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"DECIMAL_PK"
argument_list|,
operator|new
name|Double
argument_list|(
literal|1.25
argument_list|)
argument_list|)
decl_stmt|;
name|ObjectId
name|syntheticId
init|=
operator|new
name|ObjectId
argument_list|(
literal|"DecimalPKTest1"
argument_list|,
name|map
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|object
argument_list|,
name|context
operator|.
name|localObject
argument_list|(
name|syntheticId
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

