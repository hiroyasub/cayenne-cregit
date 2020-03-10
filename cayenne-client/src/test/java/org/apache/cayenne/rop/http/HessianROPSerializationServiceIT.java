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
name|rop
operator|.
name|http
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
name|CayenneContext
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
name|rop
operator|.
name|ROPSerializationService
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
name|rop
operator|.
name|ServerHessianSerializationServiceProvider
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
name|mt
operator|.
name|ClientMtTable1
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
name|mt
operator|.
name|ClientMtTable2
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
name|client
operator|.
name|ClientCase
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
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
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

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|CayenneProjects
operator|.
name|MULTI_TIER_PROJECT
argument_list|)
specifier|public
class|class
name|HessianROPSerializationServiceIT
extends|extends
name|ClientCase
block|{
annotation|@
name|Inject
specifier|private
name|CayenneContext
name|context
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testByteArraySerialization
parameter_list|()
throws|throws
name|Exception
block|{
name|ClientMtTable1
name|table1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|ClientMtTable1
operator|.
name|class
argument_list|)
decl_stmt|;
name|table1
operator|.
name|setGlobalAttribute1
argument_list|(
literal|"Test table1"
argument_list|)
expr_stmt|;
name|ClientMtTable2
name|table2
init|=
name|context
operator|.
name|newObject
argument_list|(
name|ClientMtTable2
operator|.
name|class
argument_list|)
decl_stmt|;
name|table2
operator|.
name|setGlobalAttribute
argument_list|(
literal|"Test table2"
argument_list|)
expr_stmt|;
name|table2
operator|.
name|setTable1
argument_list|(
name|table1
argument_list|)
expr_stmt|;
name|ROPSerializationService
name|clientService
init|=
name|createClientSerializationService
argument_list|()
decl_stmt|;
name|ROPSerializationService
name|serverService
init|=
name|createServerSerializationService
argument_list|()
decl_stmt|;
comment|// test client to server serialization
name|byte
index|[]
name|data
init|=
name|clientService
operator|.
name|serialize
argument_list|(
name|table2
argument_list|)
decl_stmt|;
name|ClientMtTable2
name|serverTable2
init|=
name|serverService
operator|.
name|deserialize
argument_list|(
name|data
argument_list|,
name|ClientMtTable2
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Test table2"
argument_list|,
name|serverTable2
operator|.
name|getGlobalAttribute
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Test table1"
argument_list|,
name|serverTable2
operator|.
name|getTable1
argument_list|()
operator|.
name|getGlobalAttribute1
argument_list|()
argument_list|)
expr_stmt|;
comment|// test server to client serialization
name|data
operator|=
name|serverService
operator|.
name|serialize
argument_list|(
name|table2
argument_list|)
expr_stmt|;
name|ClientMtTable2
name|clientTable2
init|=
name|clientService
operator|.
name|deserialize
argument_list|(
name|data
argument_list|,
name|ClientMtTable2
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Test table2"
argument_list|,
name|clientTable2
operator|.
name|getGlobalAttribute
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Test table1"
argument_list|,
name|clientTable2
operator|.
name|getTable1
argument_list|()
operator|.
name|getGlobalAttribute1
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testStreamSerialization
parameter_list|()
throws|throws
name|Exception
block|{
name|ClientMtTable1
name|table1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|ClientMtTable1
operator|.
name|class
argument_list|)
decl_stmt|;
name|table1
operator|.
name|setGlobalAttribute1
argument_list|(
literal|"Test table1"
argument_list|)
expr_stmt|;
name|ClientMtTable2
name|table2
init|=
name|context
operator|.
name|newObject
argument_list|(
name|ClientMtTable2
operator|.
name|class
argument_list|)
decl_stmt|;
name|table2
operator|.
name|setGlobalAttribute
argument_list|(
literal|"Test table2"
argument_list|)
expr_stmt|;
name|table2
operator|.
name|setTable1
argument_list|(
name|table1
argument_list|)
expr_stmt|;
name|ROPSerializationService
name|clientService
init|=
name|createClientSerializationService
argument_list|()
decl_stmt|;
name|ROPSerializationService
name|serverService
init|=
name|createServerSerializationService
argument_list|()
decl_stmt|;
name|ByteArrayOutputStream
name|out
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
comment|// test client to server serialization
name|clientService
operator|.
name|serialize
argument_list|(
name|table2
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
name|ClientMtTable2
name|serverTable2
init|=
name|serverService
operator|.
name|deserialize
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|out
operator|.
name|toByteArray
argument_list|()
argument_list|)
argument_list|,
name|ClientMtTable2
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Test table2"
argument_list|,
name|serverTable2
operator|.
name|getGlobalAttribute
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Test table1"
argument_list|,
name|serverTable2
operator|.
name|getTable1
argument_list|()
operator|.
name|getGlobalAttribute1
argument_list|()
argument_list|)
expr_stmt|;
comment|// test server to client serialization
name|out
operator|=
operator|new
name|ByteArrayOutputStream
argument_list|()
expr_stmt|;
name|serverService
operator|.
name|serialize
argument_list|(
name|table2
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
name|ClientMtTable2
name|clientTable2
init|=
name|clientService
operator|.
name|deserialize
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|out
operator|.
name|toByteArray
argument_list|()
argument_list|)
argument_list|,
name|ClientMtTable2
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Test table2"
argument_list|,
name|clientTable2
operator|.
name|getGlobalAttribute
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Test table1"
argument_list|,
name|clientTable2
operator|.
name|getTable1
argument_list|()
operator|.
name|getGlobalAttribute1
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|ROPSerializationService
name|createClientSerializationService
parameter_list|()
block|{
return|return
operator|new
name|ClientHessianSerializationServiceProvider
argument_list|()
operator|.
name|get
argument_list|()
return|;
block|}
specifier|private
name|ROPSerializationService
name|createServerSerializationService
parameter_list|()
block|{
return|return
operator|new
name|ServerHessianSerializationServiceProvider
argument_list|(
parameter_list|()
lambda|->
name|context
operator|.
name|getChannel
argument_list|()
argument_list|,
name|Collections
operator|.
name|emptyList
argument_list|()
argument_list|)
operator|.
name|get
argument_list|()
return|;
block|}
block|}
end_class

end_unit

