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
name|crypto
operator|.
name|transformer
operator|.
name|bytes
package|;
end_package

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
name|assertFalse
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|crypto
operator|.
name|CayenneCryptoException
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

begin_class
specifier|public
class|class
name|HeaderTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testSetCompressed
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|Header
operator|.
name|setCompressed
argument_list|(
operator|(
name|byte
operator|)
literal|0
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|Header
operator|.
name|setCompressed
argument_list|(
operator|(
name|byte
operator|)
literal|0
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|Header
operator|.
name|setCompressed
argument_list|(
operator|(
name|byte
operator|)
literal|1
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|Header
operator|.
name|setCompressed
argument_list|(
operator|(
name|byte
operator|)
literal|1
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|Header
operator|.
name|setHaveHMAC
argument_list|(
operator|(
name|byte
operator|)
literal|0
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|Header
operator|.
name|setHaveHMAC
argument_list|(
operator|(
name|byte
operator|)
literal|0
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|Header
operator|.
name|setHaveHMAC
argument_list|(
operator|(
name|byte
operator|)
literal|3
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|Header
operator|.
name|setHaveHMAC
argument_list|(
operator|(
name|byte
operator|)
literal|2
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCreate_WithKeyName
parameter_list|()
block|{
name|Header
name|h1
init|=
name|Header
operator|.
name|create
argument_list|(
literal|"bcd"
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|Header
name|h2
init|=
name|Header
operator|.
name|create
argument_list|(
literal|"bc"
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|Header
name|h3
init|=
name|Header
operator|.
name|create
argument_list|(
literal|"b"
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|Header
name|h4
init|=
name|Header
operator|.
name|create
argument_list|(
literal|"e"
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"bcd"
argument_list|,
name|h1
operator|.
name|getKeyName
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|h1
operator|.
name|isCompressed
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bc"
argument_list|,
name|h2
operator|.
name|getKeyName
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|h2
operator|.
name|isCompressed
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"b"
argument_list|,
name|h3
operator|.
name|getKeyName
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|h3
operator|.
name|isCompressed
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|h3
operator|.
name|haveHMAC
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"e"
argument_list|,
name|h4
operator|.
name|getKeyName
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|h4
operator|.
name|isCompressed
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|h4
operator|.
name|haveHMAC
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|CayenneCryptoException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testCreate_WithKeyName_TooLong
parameter_list|()
block|{
name|StringBuilder
name|buf
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|Byte
operator|.
name|MAX_VALUE
operator|+
literal|1
condition|;
name|i
operator|++
control|)
block|{
name|buf
operator|.
name|append
argument_list|(
literal|"a"
argument_list|)
expr_stmt|;
block|}
name|Header
operator|.
name|create
argument_list|(
name|buf
operator|.
name|toString
argument_list|()
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCreate_WithData
parameter_list|()
block|{
name|byte
index|[]
name|input1
init|=
block|{
literal|'C'
block|,
literal|'C'
block|,
literal|'1'
block|,
literal|9
block|,
literal|0
block|,
literal|'a'
block|,
literal|'b'
block|,
literal|'c'
block|,
literal|'d'
block|,
literal|'e'
block|}
decl_stmt|;
name|Header
name|h1
init|=
name|Header
operator|.
name|create
argument_list|(
name|input1
argument_list|,
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"abcd"
argument_list|,
name|h1
operator|.
name|getKeyName
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|h1
operator|.
name|isCompressed
argument_list|()
argument_list|)
expr_stmt|;
name|byte
index|[]
name|input2
init|=
block|{
literal|0
block|,
literal|'C'
block|,
literal|'C'
block|,
literal|'1'
block|,
literal|9
block|,
literal|1
block|,
literal|'a'
block|,
literal|'b'
block|,
literal|'c'
block|,
literal|'d'
block|,
literal|'e'
block|}
decl_stmt|;
name|Header
name|h2
init|=
name|Header
operator|.
name|create
argument_list|(
name|input2
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"abcd"
argument_list|,
name|h2
operator|.
name|getKeyName
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|h2
operator|.
name|isCompressed
argument_list|()
argument_list|)
expr_stmt|;
name|byte
index|[]
name|input3
init|=
block|{
literal|0
block|,
literal|0
block|,
literal|'C'
block|,
literal|'C'
block|,
literal|'1'
block|,
literal|9
block|,
literal|2
block|,
literal|'a'
block|,
literal|'b'
block|,
literal|'c'
block|,
literal|'d'
block|,
literal|'e'
block|}
decl_stmt|;
name|Header
name|h3
init|=
name|Header
operator|.
name|create
argument_list|(
name|input3
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"abcd"
argument_list|,
name|h3
operator|.
name|getKeyName
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|h3
operator|.
name|isCompressed
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|h3
operator|.
name|haveHMAC
argument_list|()
argument_list|)
expr_stmt|;
name|byte
index|[]
name|input4
init|=
block|{
literal|0
block|,
literal|0
block|,
literal|0
block|,
literal|'C'
block|,
literal|'C'
block|,
literal|'1'
block|,
literal|9
block|,
literal|3
block|,
literal|'a'
block|,
literal|'b'
block|,
literal|'c'
block|,
literal|'d'
block|,
literal|'e'
block|}
decl_stmt|;
name|Header
name|h4
init|=
name|Header
operator|.
name|create
argument_list|(
name|input4
argument_list|,
literal|3
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"abcd"
argument_list|,
name|h4
operator|.
name|getKeyName
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|h4
operator|.
name|isCompressed
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|h4
operator|.
name|haveHMAC
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

