begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|joda
operator|.
name|access
operator|.
name|types
package|;
end_package

begin_import
import|import
name|com
operator|.
name|mockrunner
operator|.
name|mock
operator|.
name|jdbc
operator|.
name|MockConnection
import|;
end_import

begin_import
import|import
name|com
operator|.
name|mockrunner
operator|.
name|mock
operator|.
name|jdbc
operator|.
name|MockPreparedStatement
import|;
end_import

begin_import
import|import
name|org
operator|.
name|joda
operator|.
name|time
operator|.
name|DateTime
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|PreparedStatement
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Timestamp
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Types
import|;
end_import

begin_comment
comment|/***************************************************************** *   Licensed to the Apache Software Foundation (ASF) under one *  or more contributor license agreements.  See the NOTICE file *  distributed with this work for additional information *  regarding copyright ownership.  The ASF licenses this file *  to you under the Apache License, Version 2.0 (the *  "License"); you may not use this file except in compliance *  with the License.  You may obtain a copy of the License at * *    https://www.apache.org/licenses/LICENSE-2.0 * *  Unless required by applicable law or agreed to in writing, *  software distributed under the License is distributed on an *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY *  KIND, either express or implied.  See the License for the *  specific language governing permissions and limitations *  under the License. ****************************************************************/
end_comment

begin_class
specifier|public
class|class
name|DateTimeTypeTest
extends|extends
name|JodaTestCase
block|{
specifier|private
name|DateTimeType
name|type
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|type
operator|=
operator|new
name|DateTimeType
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testMaterializeObjectTimestamp
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|o
init|=
name|type
operator|.
name|materializeObject
argument_list|(
name|resultSet
argument_list|(
operator|new
name|Timestamp
argument_list|(
literal|0
argument_list|)
argument_list|)
argument_list|,
literal|1
argument_list|,
name|Types
operator|.
name|TIMESTAMP
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
operator|new
name|DateTime
argument_list|(
literal|0
argument_list|)
argument_list|,
name|o
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSetJdbcObject
parameter_list|()
throws|throws
name|Exception
block|{
name|PreparedStatement
name|statement
init|=
operator|new
name|MockPreparedStatement
argument_list|(
operator|new
name|MockConnection
argument_list|()
argument_list|,
literal|"update t set c = ?"
argument_list|)
decl_stmt|;
name|DateTime
name|date
init|=
operator|new
name|DateTime
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|type
operator|.
name|setJdbcObject
argument_list|(
name|statement
argument_list|,
name|date
argument_list|,
literal|1
argument_list|,
name|Types
operator|.
name|TIMESTAMP
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|Object
name|object
init|=
operator|(
operator|(
name|MockPreparedStatement
operator|)
name|statement
operator|)
operator|.
name|getParameter
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Timestamp
operator|.
name|class
argument_list|,
name|object
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|date
operator|.
name|getMillis
argument_list|()
argument_list|,
operator|(
operator|(
name|Timestamp
operator|)
name|object
operator|)
operator|.
name|getTime
argument_list|()
argument_list|)
expr_stmt|;
name|type
operator|.
name|setJdbcObject
argument_list|(
name|statement
argument_list|,
literal|null
argument_list|,
literal|1
argument_list|,
name|Types
operator|.
name|TIMESTAMP
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|object
operator|=
operator|(
operator|(
name|MockPreparedStatement
operator|)
name|statement
operator|)
operator|.
name|getParameter
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|object
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

