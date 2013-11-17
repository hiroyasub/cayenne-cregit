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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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

begin_class
specifier|public
class|class
name|ObjectIdRegressionTest
extends|extends
name|TestCase
block|{
comment|// public void testX() {
comment|// for (int i = 0; i< 10000; i++) {
comment|// byte[] bytes = IDUtil.pseudoUniqueByteSequence8();
comment|// StringBuffer buffer = new StringBuffer(16);
comment|// for(int j = 0; j< 8; j++) {
comment|// IDUtil.appendFormattedByte(buffer, bytes[j]);
comment|// }
comment|//
comment|// System.out.println(buffer);
comment|// }
comment|// }
specifier|public
name|void
name|testIdPool
parameter_list|()
throws|throws
name|Exception
block|{
comment|// testing uniqueness of a sequence of ObjectIds generated quickly one after the
comment|// other...
name|int
name|size
init|=
literal|100000
decl_stmt|;
operator|new
name|ObjectId
argument_list|(
literal|"Artist"
argument_list|)
expr_stmt|;
name|Object
index|[]
name|pool
init|=
operator|new
name|Object
index|[
name|size
index|]
decl_stmt|;
name|long
name|t0
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
comment|// fill in
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|size
condition|;
name|i
operator|++
control|)
block|{
name|pool
index|[
name|i
index|]
operator|=
operator|new
name|ObjectId
argument_list|(
literal|"Artist"
argument_list|)
expr_stmt|;
block|}
name|long
name|t1
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"This machine is too fast to run such test!"
argument_list|,
name|t1
operator|-
name|t0
operator|>
literal|1
argument_list|)
expr_stmt|;
name|Set
name|idSet
init|=
operator|new
name|HashSet
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
name|size
condition|;
name|i
operator|++
control|)
block|{
name|assertTrue
argument_list|(
literal|"Failed to generate unique id #"
operator|+
name|i
argument_list|,
name|idSet
operator|.
name|add
argument_list|(
name|pool
index|[
name|i
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

