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
name|access
operator|.
name|types
package|;
end_package

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|SQLException
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
name|assertSame
import|;
end_import

begin_comment
comment|/**  */
end_comment

begin_class
specifier|public
class|class
name|BooleanTypeTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testClassName
parameter_list|()
block|{
name|BooleanType
name|type
init|=
operator|new
name|BooleanType
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|type
operator|.
name|getClassName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMaterializeObjectFromResultSet
parameter_list|()
throws|throws
name|Exception
block|{
name|MockResultSet
name|rs
init|=
operator|new
name|MockResultSet
argument_list|(
literal|""
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|getBoolean
parameter_list|(
name|int
name|i
parameter_list|)
throws|throws
name|SQLException
block|{
return|return
operator|(
name|i
operator|+
literal|2
operator|)
operator|%
literal|2
operator|==
literal|0
return|;
block|}
block|}
decl_stmt|;
name|BooleanType
name|type
init|=
operator|new
name|BooleanType
argument_list|()
decl_stmt|;
comment|// assert identity as well as equality (see CAY-320)
name|assertSame
argument_list|(
name|Boolean
operator|.
name|FALSE
argument_list|,
name|type
operator|.
name|materializeObject
argument_list|(
name|rs
argument_list|,
literal|1
argument_list|,
name|Types
operator|.
name|BIT
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|,
name|type
operator|.
name|materializeObject
argument_list|(
name|rs
argument_list|,
literal|2
argument_list|,
name|Types
operator|.
name|BIT
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

