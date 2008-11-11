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
name|conf
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
name|util
operator|.
name|Properties
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
name|ConfigurationException
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
name|ResourceLocator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|pool
operator|.
name|impl
operator|.
name|GenericObjectPool
import|;
end_import

begin_comment
comment|/**  */
end_comment

begin_class
specifier|public
class|class
name|DBCPDataSourcePropertiesTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testLoadProperties
parameter_list|()
throws|throws
name|Exception
block|{
name|ResourceLocator
name|locator
init|=
operator|new
name|ResourceLocator
argument_list|()
decl_stmt|;
name|locator
operator|.
name|setSkipClasspath
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|Properties
name|props1
init|=
name|DBCPDataSourceProperties
operator|.
name|loadProperties
argument_list|(
name|locator
argument_list|,
literal|"dbcp"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|props1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a"
argument_list|,
name|props1
operator|.
name|get
argument_list|(
literal|"b"
argument_list|)
argument_list|)
expr_stmt|;
name|Properties
name|props2
init|=
name|DBCPDataSourceProperties
operator|.
name|loadProperties
argument_list|(
name|locator
argument_list|,
literal|"dbcp.properties"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|props2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a"
argument_list|,
name|props2
operator|.
name|get
argument_list|(
literal|"b"
argument_list|)
argument_list|)
expr_stmt|;
name|Properties
name|props3
init|=
name|DBCPDataSourceProperties
operator|.
name|loadProperties
argument_list|(
name|locator
argument_list|,
literal|"dbcp.driver"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|props3
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"d"
argument_list|,
name|props3
operator|.
name|get
argument_list|(
literal|"c"
argument_list|)
argument_list|)
expr_stmt|;
name|Properties
name|props4
init|=
name|DBCPDataSourceProperties
operator|.
name|loadProperties
argument_list|(
name|locator
argument_list|,
literal|"dbcp.driver.properties"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|props4
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"d"
argument_list|,
name|props4
operator|.
name|get
argument_list|(
literal|"c"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testStringProperty
parameter_list|()
block|{
name|Properties
name|props
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"a"
argument_list|,
literal|"X"
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"cayenne.dbcp.c"
argument_list|,
literal|"Y"
argument_list|)
expr_stmt|;
name|DBCPDataSourceProperties
name|factory
init|=
operator|new
name|DBCPDataSourceProperties
argument_list|(
name|props
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|factory
operator|.
name|getString
argument_list|(
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|factory
operator|.
name|getString
argument_list|(
literal|"b"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Y"
argument_list|,
name|factory
operator|.
name|getString
argument_list|(
literal|"c"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testIntProperty
parameter_list|()
block|{
name|Properties
name|props
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"a"
argument_list|,
literal|"10"
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"cayenne.dbcp.b"
argument_list|,
literal|"11"
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"cayenne.dbcp.d"
argument_list|,
literal|"**"
argument_list|)
expr_stmt|;
name|DBCPDataSourceProperties
name|factory
init|=
operator|new
name|DBCPDataSourceProperties
argument_list|(
name|props
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|11
argument_list|,
name|factory
operator|.
name|getInt
argument_list|(
literal|"b"
argument_list|,
operator|-
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|-
literal|1
argument_list|,
name|factory
operator|.
name|getInt
argument_list|(
literal|"a"
argument_list|,
operator|-
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|-
literal|1
argument_list|,
name|factory
operator|.
name|getInt
argument_list|(
literal|"c"
argument_list|,
operator|-
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|-
literal|2
argument_list|,
name|factory
operator|.
name|getInt
argument_list|(
literal|"d"
argument_list|,
operator|-
literal|2
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testWhenExhaustedAction
parameter_list|()
throws|throws
name|Exception
block|{
name|Properties
name|props
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"cayenne.dbcp.a"
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"cayenne.dbcp.b"
argument_list|,
literal|"WHEN_EXHAUSTED_BLOCK"
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"cayenne.dbcp.c"
argument_list|,
literal|"WHEN_EXHAUSTED_GROW"
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"cayenne.dbcp.d"
argument_list|,
literal|"WHEN_EXHAUSTED_FAIL"
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"cayenne.dbcp.e"
argument_list|,
literal|"garbage"
argument_list|)
expr_stmt|;
name|DBCPDataSourceProperties
name|factory
init|=
operator|new
name|DBCPDataSourceProperties
argument_list|(
name|props
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|factory
operator|.
name|getWhenExhaustedAction
argument_list|(
literal|"a"
argument_list|,
operator|(
name|byte
operator|)
literal|100
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|GenericObjectPool
operator|.
name|WHEN_EXHAUSTED_BLOCK
argument_list|,
name|factory
operator|.
name|getWhenExhaustedAction
argument_list|(
literal|"b"
argument_list|,
operator|(
name|byte
operator|)
literal|100
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|GenericObjectPool
operator|.
name|WHEN_EXHAUSTED_GROW
argument_list|,
name|factory
operator|.
name|getWhenExhaustedAction
argument_list|(
literal|"c"
argument_list|,
operator|(
name|byte
operator|)
literal|100
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|GenericObjectPool
operator|.
name|WHEN_EXHAUSTED_FAIL
argument_list|,
name|factory
operator|.
name|getWhenExhaustedAction
argument_list|(
literal|"d"
argument_list|,
operator|(
name|byte
operator|)
literal|100
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|factory
operator|.
name|getWhenExhaustedAction
argument_list|(
literal|"e"
argument_list|,
operator|(
name|byte
operator|)
literal|100
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"must throw on invalid key"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ConfigurationException
name|ex
parameter_list|)
block|{
comment|// expected
block|}
name|assertEquals
argument_list|(
literal|100
argument_list|,
name|factory
operator|.
name|getWhenExhaustedAction
argument_list|(
literal|"f"
argument_list|,
operator|(
name|byte
operator|)
literal|100
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testTransactionIsolation
parameter_list|()
throws|throws
name|Exception
block|{
name|Properties
name|props
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"cayenne.dbcp.a"
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"cayenne.dbcp.b"
argument_list|,
literal|"TRANSACTION_NONE"
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"cayenne.dbcp.c"
argument_list|,
literal|"TRANSACTION_READ_UNCOMMITTED"
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"cayenne.dbcp.d"
argument_list|,
literal|"TRANSACTION_SERIALIZABLE"
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"cayenne.dbcp.e"
argument_list|,
literal|"garbage"
argument_list|)
expr_stmt|;
name|DBCPDataSourceProperties
name|factory
init|=
operator|new
name|DBCPDataSourceProperties
argument_list|(
name|props
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|factory
operator|.
name|getTransactionIsolation
argument_list|(
literal|"a"
argument_list|,
operator|(
name|byte
operator|)
literal|100
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Connection
operator|.
name|TRANSACTION_NONE
argument_list|,
name|factory
operator|.
name|getTransactionIsolation
argument_list|(
literal|"b"
argument_list|,
operator|(
name|byte
operator|)
literal|100
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Connection
operator|.
name|TRANSACTION_READ_UNCOMMITTED
argument_list|,
name|factory
operator|.
name|getTransactionIsolation
argument_list|(
literal|"c"
argument_list|,
operator|(
name|byte
operator|)
literal|100
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Connection
operator|.
name|TRANSACTION_SERIALIZABLE
argument_list|,
name|factory
operator|.
name|getTransactionIsolation
argument_list|(
literal|"d"
argument_list|,
operator|(
name|byte
operator|)
literal|100
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|factory
operator|.
name|getTransactionIsolation
argument_list|(
literal|"e"
argument_list|,
operator|(
name|byte
operator|)
literal|100
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"must throw on invalid key"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ConfigurationException
name|ex
parameter_list|)
block|{
comment|// expected
block|}
name|assertEquals
argument_list|(
literal|100
argument_list|,
name|factory
operator|.
name|getTransactionIsolation
argument_list|(
literal|"f"
argument_list|,
operator|(
name|byte
operator|)
literal|100
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

