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
name|log
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
name|configuration
operator|.
name|DefaultRuntimeProperties
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
name|IDUtil
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
specifier|public
class|class
name|Slf4jJdbcEventLoggerTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testAppendFormattedByte
parameter_list|()
throws|throws
name|Exception
block|{
name|assertFormatting
argument_list|(
operator|(
name|byte
operator|)
literal|0
argument_list|,
literal|"00"
argument_list|)
expr_stmt|;
name|assertFormatting
argument_list|(
operator|(
name|byte
operator|)
literal|1
argument_list|,
literal|"01"
argument_list|)
expr_stmt|;
name|assertFormatting
argument_list|(
operator|(
name|byte
operator|)
literal|10
argument_list|,
literal|"0A"
argument_list|)
expr_stmt|;
name|assertFormatting
argument_list|(
name|Byte
operator|.
name|MAX_VALUE
argument_list|,
literal|"7F"
argument_list|)
expr_stmt|;
name|assertFormatting
argument_list|(
operator|(
name|byte
operator|)
operator|-
literal|1
argument_list|,
literal|"FF"
argument_list|)
expr_stmt|;
name|assertFormatting
argument_list|(
name|Byte
operator|.
name|MIN_VALUE
argument_list|,
literal|"80"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertFormatting
parameter_list|(
name|byte
name|b
parameter_list|,
name|String
name|formatted
parameter_list|)
throws|throws
name|Exception
block|{
name|StringBuffer
name|buffer
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|IDUtil
operator|.
name|appendFormattedByte
argument_list|(
name|buffer
argument_list|,
name|b
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|formatted
argument_list|,
name|buffer
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

