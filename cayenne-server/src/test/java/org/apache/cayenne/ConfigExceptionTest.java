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
package|;
end_package

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
name|assertNull
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
name|assertSame
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
comment|/**  */
end_comment

begin_class
specifier|public
class|class
name|ConfigExceptionTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testConstructor1
parameter_list|()
throws|throws
name|Exception
block|{
name|ConfigurationException
name|ex
init|=
operator|new
name|ConfigurationException
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
name|ex
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ex
operator|.
name|getMessage
argument_list|()
operator|.
name|startsWith
argument_list|(
name|CayenneRuntimeException
operator|.
name|getExceptionLabel
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testConstructor2
parameter_list|()
throws|throws
name|Exception
block|{
name|ConfigurationException
name|ex
init|=
operator|new
name|ConfigurationException
argument_list|(
literal|"abc"
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|ex
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|CayenneRuntimeException
operator|.
name|getExceptionLabel
argument_list|()
operator|+
literal|"abc"
argument_list|,
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testConstructor3
parameter_list|()
throws|throws
name|Exception
block|{
name|Throwable
name|cause
init|=
operator|new
name|Throwable
argument_list|()
decl_stmt|;
name|ConfigurationException
name|ex
init|=
operator|new
name|ConfigurationException
argument_list|(
name|cause
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|cause
argument_list|,
name|ex
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|CayenneRuntimeException
operator|.
name|getExceptionLabel
argument_list|()
operator|+
name|cause
operator|.
name|toString
argument_list|()
argument_list|,
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testConstructor4
parameter_list|()
throws|throws
name|Exception
block|{
name|Throwable
name|cause
init|=
operator|new
name|Throwable
argument_list|()
decl_stmt|;
name|ConfigurationException
name|ex
init|=
operator|new
name|ConfigurationException
argument_list|(
literal|"abc"
argument_list|,
name|cause
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|cause
argument_list|,
name|ex
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|CayenneRuntimeException
operator|.
name|getExceptionLabel
argument_list|()
operator|+
literal|"abc"
argument_list|,
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

