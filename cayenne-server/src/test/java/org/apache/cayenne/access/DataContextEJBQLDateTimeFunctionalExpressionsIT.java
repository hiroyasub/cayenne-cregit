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
name|assertTrue
import|;
end_import

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|CayenneProjects
operator|.
name|DATE_TIME_PROJECT
argument_list|)
specifier|public
class|class
name|DataContextEJBQLDateTimeFunctionalExpressionsIT
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
name|Test
specifier|public
name|void
name|testCURRENT_DATE
parameter_list|()
block|{
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
argument_list|<
name|?
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
annotation|@
name|Test
specifier|public
name|void
name|testCURRENT_TIME
parameter_list|()
block|{
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
argument_list|<
name|?
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
annotation|@
name|Test
specifier|public
name|void
name|testCURRENT_TIMESTAMP
parameter_list|()
block|{
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
argument_list|<
name|?
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
block|}
end_class

end_unit

