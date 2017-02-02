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
name|dba
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
name|MockSerializable
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
name|ServerCaseDataSourceFactory
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
name|math
operator|.
name|BigInteger
import|;
end_import

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
name|DatabaseMetaData
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|ResultSet
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
name|GregorianCalendar
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
name|EMPTY_PROJECT
argument_list|)
specifier|public
class|class
name|TypesMappingIT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|private
name|ServerCaseDataSourceFactory
name|dataSourceFactory
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testGetSqlTypeByJava
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
name|Types
operator|.
name|VARCHAR
argument_list|,
name|TypesMapping
operator|.
name|getSqlTypeByJava
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
comment|// make sure we can handle arrays...
name|assertEquals
argument_list|(
name|Types
operator|.
name|BINARY
argument_list|,
name|TypesMapping
operator|.
name|getSqlTypeByJava
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Types
operator|.
name|TIMESTAMP
argument_list|,
name|TypesMapping
operator|.
name|getSqlTypeByJava
argument_list|(
name|Calendar
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Types
operator|.
name|TIMESTAMP
argument_list|,
name|TypesMapping
operator|.
name|getSqlTypeByJava
argument_list|(
name|GregorianCalendar
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Types
operator|.
name|BIGINT
argument_list|,
name|TypesMapping
operator|.
name|getSqlTypeByJava
argument_list|(
name|BigInteger
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Types
operator|.
name|VARBINARY
argument_list|,
name|TypesMapping
operator|.
name|getSqlTypeByJava
argument_list|(
name|MockSerializable
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Types
operator|.
name|VARCHAR
argument_list|,
name|TypesMapping
operator|.
name|getSqlTypeByJava
argument_list|(
name|char
index|[]
operator|.
expr|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Types
operator|.
name|VARCHAR
argument_list|,
name|TypesMapping
operator|.
name|getSqlTypeByJava
argument_list|(
name|Character
index|[]
operator|.
expr|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Types
operator|.
name|VARBINARY
argument_list|,
name|TypesMapping
operator|.
name|getSqlTypeByJava
argument_list|(
name|Byte
index|[]
operator|.
expr|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetSqlTypeByJavaString
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
name|Types
operator|.
name|VARCHAR
argument_list|,
name|TypesMapping
operator|.
name|getSqlTypeByJava
argument_list|(
name|String
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
comment|// make sure we can handle arrays...
name|assertEquals
argument_list|(
name|Types
operator|.
name|BINARY
argument_list|,
name|TypesMapping
operator|.
name|getSqlTypeByJava
argument_list|(
literal|"byte[]"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Types
operator|.
name|TIMESTAMP
argument_list|,
name|TypesMapping
operator|.
name|getSqlTypeByJava
argument_list|(
name|Calendar
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Types
operator|.
name|TIMESTAMP
argument_list|,
name|TypesMapping
operator|.
name|getSqlTypeByJava
argument_list|(
name|GregorianCalendar
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Types
operator|.
name|BIGINT
argument_list|,
name|TypesMapping
operator|.
name|getSqlTypeByJava
argument_list|(
name|BigInteger
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Types
operator|.
name|VARBINARY
argument_list|,
name|TypesMapping
operator|.
name|getSqlTypeByJava
argument_list|(
name|MockSerializable
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Types
operator|.
name|VARCHAR
argument_list|,
name|TypesMapping
operator|.
name|getSqlTypeByJava
argument_list|(
literal|"char[]"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Types
operator|.
name|VARCHAR
argument_list|,
name|TypesMapping
operator|.
name|getSqlTypeByJava
argument_list|(
literal|"java.lang.Character[]"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Types
operator|.
name|VARBINARY
argument_list|,
name|TypesMapping
operator|.
name|getSqlTypeByJava
argument_list|(
literal|"java.lang.Byte[]"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetSqlTypeByJavaPrimitive
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
name|Types
operator|.
name|INTEGER
argument_list|,
name|TypesMapping
operator|.
name|getSqlTypeByJava
argument_list|(
name|Integer
operator|.
name|TYPE
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Types
operator|.
name|BIGINT
argument_list|,
name|TypesMapping
operator|.
name|getSqlTypeByJava
argument_list|(
name|Long
operator|.
name|TYPE
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testTypeInfoCompleteness
parameter_list|()
throws|throws
name|Exception
block|{
comment|// check counts
comment|// since more then 1 database type can map to a single JDBC type
name|int
name|len
init|=
literal|0
decl_stmt|;
try|try
init|(
name|Connection
name|conn
init|=
name|dataSourceFactory
operator|.
name|getSharedDataSource
argument_list|()
operator|.
name|getConnection
argument_list|()
init|)
block|{
name|DatabaseMetaData
name|md
init|=
name|conn
operator|.
name|getMetaData
argument_list|()
decl_stmt|;
try|try
init|(
name|ResultSet
name|rs
init|=
name|md
operator|.
name|getTypeInfo
argument_list|()
init|)
block|{
while|while
condition|(
name|rs
operator|.
name|next
argument_list|()
condition|)
block|{
name|len
operator|++
expr_stmt|;
block|}
block|}
block|}
name|int
name|actualLen
init|=
literal|0
decl_stmt|;
name|TypesMapping
name|map
init|=
name|createTypesMapping
argument_list|()
decl_stmt|;
for|for
control|(
name|List
argument_list|<
name|TypesMapping
operator|.
name|TypeInfo
argument_list|>
name|entry
range|:
name|map
operator|.
name|databaseTypes
operator|.
name|values
argument_list|()
control|)
block|{
name|actualLen
operator|+=
name|entry
operator|.
name|size
argument_list|()
expr_stmt|;
block|}
comment|// this is bad assertion, since due to some hacks
comment|// the same database types may map more then once,
comment|// so we have to use<=
name|assertTrue
argument_list|(
name|len
operator|<=
name|actualLen
argument_list|)
expr_stmt|;
block|}
specifier|private
name|TypesMapping
name|createTypesMapping
parameter_list|()
throws|throws
name|Exception
block|{
try|try
init|(
name|Connection
name|conn
init|=
name|dataSourceFactory
operator|.
name|getSharedDataSource
argument_list|()
operator|.
name|getConnection
argument_list|()
init|)
block|{
name|DatabaseMetaData
name|md
init|=
name|conn
operator|.
name|getMetaData
argument_list|()
decl_stmt|;
return|return
operator|new
name|TypesMapping
argument_list|(
name|md
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

