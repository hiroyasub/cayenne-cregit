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
name|Calendar
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
name|org
operator|.
name|apache
operator|.
name|art
operator|.
name|Artist
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|art
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
name|art
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
name|art
operator|.
name|DateTestEntity
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|art
operator|.
name|Painting
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
name|query
operator|.
name|EJBQLQuery
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
name|QueryChain
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|util
operator|.
name|Cayenne
import|;
end_import

begin_class
specifier|public
class|class
name|DataContextEJBQLFunctionalExpressions
extends|extends
name|CayenneCase
block|{
annotation|@
name|Override
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteTestData
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testCURRENT_DATE
parameter_list|()
block|{
name|ObjectContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|Calendar
name|cal
init|=
name|Calendar
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|int
name|year
init|=
name|cal
operator|.
name|get
argument_list|(
name|Calendar
operator|.
name|YEAR
argument_list|)
decl_stmt|;
name|DateTestEntity
name|o1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|DateTestEntity
operator|.
name|class
argument_list|)
decl_stmt|;
name|cal
operator|.
name|set
argument_list|(
name|year
operator|-
literal|3
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|o1
operator|.
name|setDateColumn
argument_list|(
name|cal
operator|.
name|getTime
argument_list|()
argument_list|)
expr_stmt|;
name|DateTestEntity
name|o2
init|=
name|context
operator|.
name|newObject
argument_list|(
name|DateTestEntity
operator|.
name|class
argument_list|)
decl_stmt|;
name|cal
operator|.
name|set
argument_list|(
name|year
operator|+
literal|3
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|o2
operator|.
name|setDateColumn
argument_list|(
name|cal
operator|.
name|getTime
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
literal|"SELECT d FROM DateTestEntity d WHERE d.dateColumn> CURRENT_DATE"
argument_list|)
decl_stmt|;
name|List
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
name|assertTrue
argument_list|(
name|objects
operator|.
name|contains
argument_list|(
name|o2
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCURRENT_TIME
parameter_list|()
block|{
name|ObjectContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|Calendar
name|cal
init|=
name|Calendar
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|int
name|year
init|=
name|cal
operator|.
name|get
argument_list|(
name|Calendar
operator|.
name|YEAR
argument_list|)
decl_stmt|;
name|DateTestEntity
name|o1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|DateTestEntity
operator|.
name|class
argument_list|)
decl_stmt|;
name|cal
operator|.
name|set
argument_list|(
name|year
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|o1
operator|.
name|setTimeColumn
argument_list|(
name|cal
operator|.
name|getTime
argument_list|()
argument_list|)
expr_stmt|;
name|DateTestEntity
name|o2
init|=
name|context
operator|.
name|newObject
argument_list|(
name|DateTestEntity
operator|.
name|class
argument_list|)
decl_stmt|;
name|cal
operator|.
name|set
argument_list|(
name|year
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|,
literal|23
argument_list|,
literal|59
argument_list|,
literal|59
argument_list|)
expr_stmt|;
name|o2
operator|.
name|setTimeColumn
argument_list|(
name|cal
operator|.
name|getTime
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
literal|"SELECT d FROM DateTestEntity d WHERE d.timeColumn< CURRENT_TIME"
argument_list|)
decl_stmt|;
name|List
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
name|assertTrue
argument_list|(
name|objects
operator|.
name|contains
argument_list|(
name|o1
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCURRENT_TIMESTAMP
parameter_list|()
block|{
name|ObjectContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|Calendar
name|cal
init|=
name|Calendar
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|int
name|year
init|=
name|cal
operator|.
name|get
argument_list|(
name|Calendar
operator|.
name|YEAR
argument_list|)
decl_stmt|;
name|int
name|month
init|=
name|cal
operator|.
name|get
argument_list|(
name|Calendar
operator|.
name|MONTH
argument_list|)
decl_stmt|;
name|int
name|date
init|=
name|cal
operator|.
name|get
argument_list|(
name|Calendar
operator|.
name|DATE
argument_list|)
decl_stmt|;
name|DateTestEntity
name|o1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|DateTestEntity
operator|.
name|class
argument_list|)
decl_stmt|;
name|cal
operator|.
name|set
argument_list|(
name|year
argument_list|,
name|month
argument_list|,
name|date
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|o1
operator|.
name|setTimestampColumn
argument_list|(
name|cal
operator|.
name|getTime
argument_list|()
argument_list|)
expr_stmt|;
name|DateTestEntity
name|o2
init|=
name|context
operator|.
name|newObject
argument_list|(
name|DateTestEntity
operator|.
name|class
argument_list|)
decl_stmt|;
name|cal
operator|.
name|set
argument_list|(
name|year
argument_list|,
name|month
argument_list|,
name|date
argument_list|,
literal|23
argument_list|,
literal|59
argument_list|,
literal|59
argument_list|)
expr_stmt|;
name|o2
operator|.
name|setTimestampColumn
argument_list|(
name|cal
operator|.
name|getTime
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
literal|"SELECT d FROM DateTestEntity d WHERE d.timestampColumn< CURRENT_TIMESTAMP"
argument_list|)
decl_stmt|;
name|List
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
name|assertTrue
argument_list|(
name|objects
operator|.
name|contains
argument_list|(
name|o1
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testABS
parameter_list|()
block|{
name|ObjectContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|BigDecimalEntity
name|o1
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
name|o1
operator|.
name|setBigDecimalField
argument_list|(
operator|new
name|BigDecimal
argument_list|(
literal|"4.1"
argument_list|)
argument_list|)
expr_stmt|;
name|BigDecimalEntity
name|o2
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
name|o2
operator|.
name|setBigDecimalField
argument_list|(
operator|new
name|BigDecimal
argument_list|(
literal|"-5.1"
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
literal|"SELECT d FROM BigDecimalEntity d WHERE ABS(d.bigDecimalField)> 4.5"
argument_list|)
decl_stmt|;
name|List
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
name|assertTrue
argument_list|(
name|objects
operator|.
name|contains
argument_list|(
name|o2
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSQRT
parameter_list|()
block|{
name|ObjectContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|BigDecimalEntity
name|o1
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
name|o1
operator|.
name|setBigDecimalField
argument_list|(
operator|new
name|BigDecimal
argument_list|(
literal|"9"
argument_list|)
argument_list|)
expr_stmt|;
name|BigDecimalEntity
name|o2
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
name|o2
operator|.
name|setBigDecimalField
argument_list|(
operator|new
name|BigDecimal
argument_list|(
literal|"16"
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
literal|"SELECT d FROM BigDecimalEntity d WHERE SQRT(d.bigDecimalField)> 3.1"
argument_list|)
decl_stmt|;
name|List
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
name|assertTrue
argument_list|(
name|objects
operator|.
name|contains
argument_list|(
name|o2
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testMOD
parameter_list|()
block|{
name|ObjectContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|BigIntegerEntity
name|o1
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
name|o1
operator|.
name|setBigIntegerField
argument_list|(
operator|new
name|BigInteger
argument_list|(
literal|"9"
argument_list|)
argument_list|)
expr_stmt|;
name|BigIntegerEntity
name|o2
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
name|o2
operator|.
name|setBigIntegerField
argument_list|(
operator|new
name|BigInteger
argument_list|(
literal|"10"
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
literal|"SELECT d FROM BigIntegerEntity d WHERE MOD(d.bigIntegerField, 4) = 2"
argument_list|)
decl_stmt|;
name|List
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
name|assertTrue
argument_list|(
name|objects
operator|.
name|contains
argument_list|(
name|o2
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSIZE
parameter_list|()
block|{
name|ObjectContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|Artist
name|a1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|a1
operator|.
name|setArtistName
argument_list|(
literal|"a1"
argument_list|)
expr_stmt|;
name|Artist
name|a2
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|a2
operator|.
name|setArtistName
argument_list|(
literal|"a2"
argument_list|)
expr_stmt|;
name|Painting
name|p12
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
decl_stmt|;
name|p12
operator|.
name|setPaintingTitle
argument_list|(
literal|"p12"
argument_list|)
expr_stmt|;
name|a2
operator|.
name|addToPaintingArray
argument_list|(
name|p12
argument_list|)
expr_stmt|;
name|Painting
name|p22
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
decl_stmt|;
name|p22
operator|.
name|setPaintingTitle
argument_list|(
literal|"p22"
argument_list|)
expr_stmt|;
name|a2
operator|.
name|addToPaintingArray
argument_list|(
name|p22
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
literal|"SELECT d FROM Artist d WHERE SIZE(d.paintingArray) = 2"
argument_list|)
decl_stmt|;
name|List
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
name|assertTrue
argument_list|(
name|objects
operator|.
name|contains
argument_list|(
name|a2
argument_list|)
argument_list|)
expr_stmt|;
name|EJBQLQuery
name|query2
init|=
operator|new
name|EJBQLQuery
argument_list|(
literal|"SELECT d FROM Artist d WHERE SIZE(d.paintingArray) = 0"
argument_list|)
decl_stmt|;
name|List
name|objects2
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query2
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|objects2
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|objects2
operator|.
name|contains
argument_list|(
name|a1
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCONCAT
parameter_list|()
block|{
name|ObjectContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|Artist
name|a1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|a1
operator|.
name|setArtistName
argument_list|(
literal|"a1"
argument_list|)
expr_stmt|;
name|Artist
name|a2
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|a2
operator|.
name|setArtistName
argument_list|(
literal|"a2"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
literal|"SELECT a FROM Artist a WHERE CONCAT(a.artistName, a.artistName) = 'a1a1'"
argument_list|)
decl_stmt|;
name|List
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
name|assertTrue
argument_list|(
name|objects
operator|.
name|contains
argument_list|(
name|a1
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSUBSTRING
parameter_list|()
block|{
name|ObjectContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|Artist
name|a1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|a1
operator|.
name|setArtistName
argument_list|(
literal|"12345678"
argument_list|)
expr_stmt|;
name|Artist
name|a2
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|a2
operator|.
name|setArtistName
argument_list|(
literal|"abcdefg"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
literal|"SELECT a FROM Artist a WHERE SUBSTRING(a.artistName, 2, 3) = 'bcd'"
argument_list|)
decl_stmt|;
name|List
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
name|assertTrue
argument_list|(
name|objects
operator|.
name|contains
argument_list|(
name|a2
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testLOWER
parameter_list|()
block|{
name|ObjectContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|Artist
name|a1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|a1
operator|.
name|setArtistName
argument_list|(
literal|"ABCDEFG"
argument_list|)
expr_stmt|;
name|Artist
name|a2
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|a2
operator|.
name|setArtistName
argument_list|(
literal|"abcdefg"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|Artist
name|a3
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|a3
operator|.
name|setArtistName
argument_list|(
literal|"Xabcdefg"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
literal|"SELECT a FROM Artist a WHERE LOWER(a.artistName) = 'abcdefg'"
argument_list|)
decl_stmt|;
name|List
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
name|assertTrue
argument_list|(
name|objects
operator|.
name|contains
argument_list|(
name|a1
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|objects
operator|.
name|contains
argument_list|(
name|a2
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testUPPER
parameter_list|()
block|{
name|ObjectContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|Artist
name|a1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|a1
operator|.
name|setArtistName
argument_list|(
literal|"ABCDEFG"
argument_list|)
expr_stmt|;
name|Artist
name|a2
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|a2
operator|.
name|setArtistName
argument_list|(
literal|"abcdefg"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|Artist
name|a3
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|a3
operator|.
name|setArtistName
argument_list|(
literal|"Xabcdefg"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
literal|"SELECT a FROM Artist a WHERE UPPER(a.artistName) = UPPER('abcdefg')"
argument_list|)
decl_stmt|;
name|List
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
name|assertTrue
argument_list|(
name|objects
operator|.
name|contains
argument_list|(
name|a1
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|objects
operator|.
name|contains
argument_list|(
name|a2
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testLENGTH
parameter_list|()
block|{
name|ObjectContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|Artist
name|a1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|a1
operator|.
name|setArtistName
argument_list|(
literal|"1234567"
argument_list|)
expr_stmt|;
name|Artist
name|a2
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|a2
operator|.
name|setArtistName
argument_list|(
literal|"1234567890"
argument_list|)
expr_stmt|;
name|Artist
name|a3
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|a3
operator|.
name|setArtistName
argument_list|(
literal|"1234567890-="
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
literal|"SELECT a FROM Artist a WHERE LENGTH(a.artistName)> 7"
argument_list|)
decl_stmt|;
name|List
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
name|assertTrue
argument_list|(
name|objects
operator|.
name|contains
argument_list|(
name|a3
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|objects
operator|.
name|contains
argument_list|(
name|a2
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testLOCATE
parameter_list|()
block|{
name|ObjectContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|Artist
name|a1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|a1
operator|.
name|setArtistName
argument_list|(
literal|"___A___"
argument_list|)
expr_stmt|;
name|Artist
name|a2
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|a2
operator|.
name|setArtistName
argument_list|(
literal|"_A_____"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
literal|"SELECT a FROM Artist a WHERE LOCATE('A', a.artistName) = 2"
argument_list|)
decl_stmt|;
name|List
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
name|assertTrue
argument_list|(
name|objects
operator|.
name|contains
argument_list|(
name|a2
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testTRIM
parameter_list|()
block|{
name|ObjectContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
comment|// insert via a SQL template to prevent adapter trimming and such...
name|QueryChain
name|inserts
init|=
operator|new
name|QueryChain
argument_list|()
decl_stmt|;
name|inserts
operator|.
name|addQuery
argument_list|(
operator|new
name|SQLTemplate
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
literal|"INSERT INTO ARTIST (ARTIST_ID,ARTIST_NAME) VALUES(1, '  A')"
argument_list|)
argument_list|)
expr_stmt|;
name|inserts
operator|.
name|addQuery
argument_list|(
operator|new
name|SQLTemplate
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
literal|"INSERT INTO ARTIST (ARTIST_ID,ARTIST_NAME) VALUES(2, 'A  ')"
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|performGenericQuery
argument_list|(
name|inserts
argument_list|)
expr_stmt|;
name|Artist
name|a1
init|=
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|Artist
operator|.
name|class
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|Artist
name|a2
init|=
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|Artist
operator|.
name|class
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
literal|"SELECT a FROM Artist a WHERE TRIM(a.artistName) = 'A'"
argument_list|)
decl_stmt|;
name|List
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
name|assertTrue
argument_list|(
name|objects
operator|.
name|contains
argument_list|(
name|a1
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|objects
operator|.
name|contains
argument_list|(
name|a2
argument_list|)
argument_list|)
expr_stmt|;
name|query
operator|=
operator|new
name|EJBQLQuery
argument_list|(
literal|"SELECT a FROM Artist a WHERE TRIM(LEADING FROM a.artistName) = 'A'"
argument_list|)
expr_stmt|;
name|objects
operator|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
expr_stmt|;
comment|// this is fuzzy cause some DB trim trailing data by default
name|assertTrue
argument_list|(
name|objects
operator|.
name|size
argument_list|()
operator|==
literal|1
operator|||
name|objects
operator|.
name|size
argument_list|()
operator|==
literal|2
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|objects
operator|.
name|contains
argument_list|(
name|a1
argument_list|)
argument_list|)
expr_stmt|;
name|query
operator|=
operator|new
name|EJBQLQuery
argument_list|(
literal|"SELECT a FROM Artist a WHERE TRIM(TRAILING FROM a.artistName) = 'A'"
argument_list|)
expr_stmt|;
name|objects
operator|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
expr_stmt|;
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
name|assertTrue
argument_list|(
name|objects
operator|.
name|contains
argument_list|(
name|a2
argument_list|)
argument_list|)
expr_stmt|;
name|query
operator|=
operator|new
name|EJBQLQuery
argument_list|(
literal|"SELECT a FROM Artist a WHERE TRIM(BOTH FROM a.artistName) = 'A'"
argument_list|)
expr_stmt|;
name|objects
operator|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
expr_stmt|;
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
name|assertTrue
argument_list|(
name|objects
operator|.
name|contains
argument_list|(
name|a1
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|objects
operator|.
name|contains
argument_list|(
name|a2
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testTRIMChar
parameter_list|()
block|{
if|if
condition|(
operator|!
name|getAccessStackAdapter
argument_list|()
operator|.
name|supportsTrimChar
argument_list|()
condition|)
block|{
return|return;
block|}
name|ObjectContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|Artist
name|a1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|a1
operator|.
name|setArtistName
argument_list|(
literal|"XXXA"
argument_list|)
expr_stmt|;
name|Artist
name|a2
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|a2
operator|.
name|setArtistName
argument_list|(
literal|"AXXX"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
literal|"SELECT a FROM Artist a WHERE TRIM('X' FROM a.artistName) = 'A'"
argument_list|)
decl_stmt|;
name|List
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
name|assertTrue
argument_list|(
name|objects
operator|.
name|contains
argument_list|(
name|a1
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|objects
operator|.
name|contains
argument_list|(
name|a2
argument_list|)
argument_list|)
expr_stmt|;
name|query
operator|=
operator|new
name|EJBQLQuery
argument_list|(
literal|"SELECT a FROM Artist a WHERE TRIM(LEADING 'X' FROM a.artistName) = 'A'"
argument_list|)
expr_stmt|;
name|objects
operator|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
expr_stmt|;
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
name|assertTrue
argument_list|(
name|objects
operator|.
name|contains
argument_list|(
name|a1
argument_list|)
argument_list|)
expr_stmt|;
name|query
operator|=
operator|new
name|EJBQLQuery
argument_list|(
literal|"SELECT a FROM Artist a WHERE TRIM(TRAILING 'X' FROM a.artistName) = 'A'"
argument_list|)
expr_stmt|;
name|objects
operator|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
expr_stmt|;
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
name|assertTrue
argument_list|(
name|objects
operator|.
name|contains
argument_list|(
name|a2
argument_list|)
argument_list|)
expr_stmt|;
name|query
operator|=
operator|new
name|EJBQLQuery
argument_list|(
literal|"SELECT a FROM Artist a WHERE TRIM(BOTH 'X' FROM a.artistName) = 'A'"
argument_list|)
expr_stmt|;
name|objects
operator|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
expr_stmt|;
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
name|assertTrue
argument_list|(
name|objects
operator|.
name|contains
argument_list|(
name|a1
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|objects
operator|.
name|contains
argument_list|(
name|a2
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

