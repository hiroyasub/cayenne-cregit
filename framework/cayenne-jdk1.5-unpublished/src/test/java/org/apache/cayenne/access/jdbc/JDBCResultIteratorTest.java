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
name|sql
operator|.
name|Connection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Statement
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
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|types
operator|.
name|ExtendedTypeMap
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
name|MockResultSet
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
name|MockStatement
import|;
end_import

begin_comment
comment|/**  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|JDBCResultIteratorTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testClosingConnection
parameter_list|()
throws|throws
name|Exception
block|{
name|JDBCResultIterator
name|it
init|=
name|makeIterator
argument_list|()
decl_stmt|;
name|it
operator|.
name|setClosingConnection
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|it
operator|.
name|isClosingConnection
argument_list|()
argument_list|)
expr_stmt|;
name|it
operator|.
name|setClosingConnection
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|it
operator|.
name|isClosingConnection
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testNextDataRow
parameter_list|()
throws|throws
name|Exception
block|{
name|JDBCResultIterator
name|it
init|=
name|makeIterator
argument_list|()
decl_stmt|;
name|Map
name|row
init|=
name|it
operator|.
name|nextDataRow
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|row
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|row
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1"
argument_list|,
name|row
operator|.
name|get
argument_list|(
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testClose
parameter_list|()
throws|throws
name|Exception
block|{
name|MockConnection
name|c
init|=
operator|new
name|MockConnection
argument_list|()
decl_stmt|;
name|MockStatement
name|s
init|=
operator|new
name|MockStatement
argument_list|(
name|c
argument_list|)
decl_stmt|;
name|MockResultSet
name|rs
init|=
operator|new
name|MockResultSet
argument_list|(
literal|"rs"
argument_list|)
decl_stmt|;
name|rs
operator|.
name|addColumn
argument_list|(
literal|"a"
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|"1"
block|,
literal|"2"
block|,
literal|"3"
block|}
argument_list|)
expr_stmt|;
name|RowDescriptor
name|descriptor
init|=
operator|new
name|RowDescriptorBuilder
argument_list|()
operator|.
name|setResultSet
argument_list|(
name|rs
argument_list|)
operator|.
name|getDescriptor
argument_list|(
operator|new
name|ExtendedTypeMap
argument_list|()
argument_list|)
decl_stmt|;
name|JDBCResultIterator
name|it
init|=
operator|new
name|JDBCResultIterator
argument_list|(
name|c
argument_list|,
name|s
argument_list|,
name|rs
argument_list|,
name|descriptor
argument_list|,
literal|0
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|rs
operator|.
name|isClosed
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|s
operator|.
name|isClosed
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|c
operator|.
name|isClosed
argument_list|()
argument_list|)
expr_stmt|;
name|it
operator|.
name|setClosingConnection
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|it
operator|.
name|close
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|rs
operator|.
name|isClosed
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|s
operator|.
name|isClosed
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|c
operator|.
name|isClosed
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|JDBCResultIterator
name|makeIterator
parameter_list|()
throws|throws
name|Exception
block|{
name|Connection
name|c
init|=
operator|new
name|MockConnection
argument_list|()
decl_stmt|;
name|Statement
name|s
init|=
operator|new
name|MockStatement
argument_list|(
name|c
argument_list|)
decl_stmt|;
name|MockResultSet
name|rs
init|=
operator|new
name|MockResultSet
argument_list|(
literal|"rs"
argument_list|)
decl_stmt|;
name|rs
operator|.
name|addColumn
argument_list|(
literal|"a"
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|"1"
block|,
literal|"2"
block|,
literal|"3"
block|}
argument_list|)
expr_stmt|;
name|RowDescriptor
name|descriptor
init|=
operator|new
name|RowDescriptorBuilder
argument_list|()
operator|.
name|setResultSet
argument_list|(
name|rs
argument_list|)
operator|.
name|getDescriptor
argument_list|(
operator|new
name|ExtendedTypeMap
argument_list|()
argument_list|)
decl_stmt|;
return|return
operator|new
name|JDBCResultIterator
argument_list|(
name|c
argument_list|,
name|s
argument_list|,
name|rs
argument_list|,
name|descriptor
argument_list|,
literal|0
argument_list|)
return|;
block|}
block|}
end_class

end_unit

