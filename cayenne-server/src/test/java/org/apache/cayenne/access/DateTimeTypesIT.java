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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|DataRow
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
name|query
operator|.
name|NamedQuery
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
name|testdo
operator|.
name|date_time
operator|.
name|CalendarEntity
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
name|date_time
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
name|sql
operator|.
name|Time
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
name|Date
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
name|assertTrue
import|;
end_import

begin_comment
comment|/**  * Tests Date handling in Cayenne.  */
end_comment

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|ServerCase
operator|.
name|DATE_TIME_PROJECT
argument_list|)
specifier|public
class|class
name|DateTimeTypesIT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|private
name|DataContext
name|context
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|DBHelper
name|dbHelper
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
literal|"CALENDAR_TEST"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"DATE_TEST"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCalendar
parameter_list|()
throws|throws
name|Exception
block|{
name|CalendarEntity
name|test
init|=
name|context
operator|.
name|newObject
argument_list|(
name|CalendarEntity
operator|.
name|class
argument_list|)
decl_stmt|;
name|Calendar
name|cal
init|=
name|Calendar
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|cal
operator|.
name|clear
argument_list|()
expr_stmt|;
name|cal
operator|.
name|set
argument_list|(
literal|2002
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|test
operator|.
name|setCalendarField
argument_list|(
name|cal
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
name|CalendarEntity
operator|.
name|class
argument_list|)
decl_stmt|;
name|CalendarEntity
name|testRead
init|=
operator|(
name|CalendarEntity
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
name|getCalendarField
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|cal
argument_list|,
name|testRead
operator|.
name|getCalendarField
argument_list|()
argument_list|)
expr_stmt|;
name|test
operator|.
name|setCalendarField
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
annotation|@
name|Test
specifier|public
name|void
name|testDate
parameter_list|()
throws|throws
name|Exception
block|{
name|DateTestEntity
name|test
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
name|Calendar
name|cal
init|=
name|Calendar
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|cal
operator|.
name|clear
argument_list|()
expr_stmt|;
name|cal
operator|.
name|set
argument_list|(
literal|2002
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|Date
name|nowDate
init|=
name|cal
operator|.
name|getTime
argument_list|()
decl_stmt|;
name|test
operator|.
name|setDateColumn
argument_list|(
name|nowDate
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
name|DateTestEntity
operator|.
name|class
argument_list|)
decl_stmt|;
name|DateTestEntity
name|testRead
init|=
operator|(
name|DateTestEntity
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
name|getDateColumn
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|nowDate
argument_list|,
name|testRead
operator|.
name|getDateColumn
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Date
operator|.
name|class
argument_list|,
name|testRead
operator|.
name|getDateColumn
argument_list|()
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testTime
parameter_list|()
throws|throws
name|Exception
block|{
name|DateTestEntity
name|test
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
name|Calendar
name|cal
init|=
name|Calendar
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|cal
operator|.
name|clear
argument_list|()
expr_stmt|;
name|cal
operator|.
name|set
argument_list|(
literal|1970
argument_list|,
literal|0
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|,
literal|20
argument_list|,
literal|30
argument_list|)
expr_stmt|;
name|Date
name|nowTime
init|=
name|cal
operator|.
name|getTime
argument_list|()
decl_stmt|;
name|test
operator|.
name|setTimeColumn
argument_list|(
name|nowTime
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
name|DateTestEntity
operator|.
name|class
argument_list|)
decl_stmt|;
name|DateTestEntity
name|testRead
init|=
operator|(
name|DateTestEntity
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
name|getTimeColumn
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Date
operator|.
name|class
argument_list|,
name|testRead
operator|.
name|getTimeColumn
argument_list|()
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
comment|// OpenBase fails to store seconds for the time
comment|// FrontBase returns time with 1 hour offset (I guess "TIME WITH TIMEZONE" may
comment|// need to be used as a default FB type?)
comment|// so this test is approximate...
name|long
name|delta
init|=
name|nowTime
operator|.
name|getTime
argument_list|()
operator|-
name|testRead
operator|.
name|getTimeColumn
argument_list|()
operator|.
name|getTime
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|""
operator|+
name|delta
argument_list|,
name|Math
operator|.
name|abs
argument_list|(
name|delta
argument_list|)
operator|<=
literal|1000
operator|*
literal|60
operator|*
literal|60
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testTimestamp
parameter_list|()
throws|throws
name|Exception
block|{
name|DateTestEntity
name|test
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
name|Calendar
name|cal
init|=
name|Calendar
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|cal
operator|.
name|clear
argument_list|()
expr_stmt|;
name|cal
operator|.
name|set
argument_list|(
literal|2003
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|,
literal|20
argument_list|,
literal|30
argument_list|)
expr_stmt|;
comment|// most databases fail millisecond accuracy
comment|// cal.set(Calendar.MILLISECOND, 55);
name|Date
name|now
init|=
name|cal
operator|.
name|getTime
argument_list|()
decl_stmt|;
name|test
operator|.
name|setTimestampColumn
argument_list|(
name|now
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
name|DateTestEntity
operator|.
name|class
argument_list|)
decl_stmt|;
name|DateTestEntity
name|testRead
init|=
operator|(
name|DateTestEntity
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
name|getTimestampColumn
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|now
argument_list|,
name|testRead
operator|.
name|getTimestampColumn
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSQLTemplateTimestamp
parameter_list|()
throws|throws
name|Exception
block|{
name|DateTestEntity
name|test
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
name|Calendar
name|cal
init|=
name|Calendar
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|cal
operator|.
name|clear
argument_list|()
expr_stmt|;
name|cal
operator|.
name|set
argument_list|(
literal|2003
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|,
literal|20
argument_list|,
literal|30
argument_list|)
expr_stmt|;
comment|// most databases fail millisecond accuracy
comment|// cal.set(Calendar.MILLISECOND, 55);
name|Date
name|now
init|=
name|cal
operator|.
name|getTime
argument_list|()
decl_stmt|;
name|test
operator|.
name|setTimestampColumn
argument_list|(
name|now
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|NamedQuery
name|q
init|=
operator|new
name|NamedQuery
argument_list|(
literal|"SelectDateTest"
argument_list|)
decl_stmt|;
name|DataRow
name|testRead
init|=
operator|(
name|DataRow
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
name|Date
name|columnValue
init|=
operator|(
name|Date
operator|)
name|testRead
operator|.
name|get
argument_list|(
literal|"TIMESTAMP_COLUMN"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|columnValue
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|now
argument_list|,
name|columnValue
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSQLTemplateDate
parameter_list|()
throws|throws
name|Exception
block|{
name|DateTestEntity
name|test
init|=
operator|(
name|DateTestEntity
operator|)
name|context
operator|.
name|newObject
argument_list|(
literal|"DateTestEntity"
argument_list|)
decl_stmt|;
name|Calendar
name|cal
init|=
name|Calendar
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|cal
operator|.
name|clear
argument_list|()
expr_stmt|;
name|cal
operator|.
name|set
argument_list|(
literal|2003
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|,
literal|20
argument_list|,
literal|30
argument_list|)
expr_stmt|;
comment|// most databases fail millisecond accuracy
comment|// cal.set(Calendar.MILLISECOND, 55);
name|java
operator|.
name|sql
operator|.
name|Date
name|now
init|=
operator|new
name|java
operator|.
name|sql
operator|.
name|Date
argument_list|(
name|cal
operator|.
name|getTime
argument_list|()
operator|.
name|getTime
argument_list|()
argument_list|)
decl_stmt|;
name|test
operator|.
name|setDateColumn
argument_list|(
name|now
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|NamedQuery
name|q
init|=
operator|new
name|NamedQuery
argument_list|(
literal|"SelectDateTest"
argument_list|)
decl_stmt|;
name|DataRow
name|testRead
init|=
operator|(
name|DataRow
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
name|Date
name|columnValue
init|=
operator|(
name|Date
operator|)
name|testRead
operator|.
name|get
argument_list|(
literal|"DATE_COLUMN"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|columnValue
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|now
operator|.
name|toString
argument_list|()
argument_list|,
operator|new
name|java
operator|.
name|sql
operator|.
name|Date
argument_list|(
name|columnValue
operator|.
name|getTime
argument_list|()
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSQLTemplateTime
parameter_list|()
throws|throws
name|Exception
block|{
name|DateTestEntity
name|test
init|=
operator|(
name|DateTestEntity
operator|)
name|context
operator|.
name|newObject
argument_list|(
literal|"DateTestEntity"
argument_list|)
decl_stmt|;
name|Calendar
name|cal
init|=
name|Calendar
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|cal
operator|.
name|clear
argument_list|()
expr_stmt|;
name|cal
operator|.
name|set
argument_list|(
literal|2003
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|,
literal|20
argument_list|,
literal|30
argument_list|)
expr_stmt|;
comment|// most databases fail millisecond accuracy
comment|// cal.set(Calendar.MILLISECOND, 55);
name|Time
name|now
init|=
operator|new
name|Time
argument_list|(
name|cal
operator|.
name|getTime
argument_list|()
operator|.
name|getTime
argument_list|()
argument_list|)
decl_stmt|;
name|test
operator|.
name|setTimeColumn
argument_list|(
name|now
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|NamedQuery
name|q
init|=
operator|new
name|NamedQuery
argument_list|(
literal|"SelectDateTest"
argument_list|)
decl_stmt|;
name|DataRow
name|testRead
init|=
operator|(
name|DataRow
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
name|Date
name|columnValue
init|=
operator|(
name|Date
operator|)
name|testRead
operator|.
name|get
argument_list|(
literal|"TIME_COLUMN"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|testRead
operator|.
name|toString
argument_list|()
argument_list|,
name|columnValue
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|columnValue
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|now
operator|.
name|toString
argument_list|()
argument_list|,
operator|new
name|Time
argument_list|(
name|columnValue
operator|.
name|getTime
argument_list|()
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

