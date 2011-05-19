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
package|;
end_package

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
name|query
operator|.
name|SelectQuery
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
name|test
operator|.
name|jdbc
operator|.
name|DBHelper
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
name|testmap
operator|.
name|ClobTestEntity
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
name|AccessStackAdapter
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
name|UseServerRuntime
import|;
end_import

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|ServerCase
operator|.
name|TESTMAP_PROJECT
argument_list|)
specifier|public
class|class
name|DataContextClobTest
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|private
name|DataContext
name|context
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|DataContext
name|context2
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|DataContext
name|context3
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|AccessStackAdapter
name|accessStackAdapter
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|DBHelper
name|dbHelper
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|void
name|setUpAfterInjection
parameter_list|()
throws|throws
name|Exception
block|{
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"CLOB_TEST"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|boolean
name|skipTests
parameter_list|()
block|{
return|return
operator|!
name|accessStackAdapter
operator|.
name|supportsLobs
argument_list|()
return|;
block|}
specifier|private
name|boolean
name|skipEmptyLOBTests
parameter_list|()
block|{
return|return
operator|!
name|accessStackAdapter
operator|.
name|handlesNullVsEmptyLOBs
argument_list|()
return|;
block|}
specifier|public
name|void
name|testEmptyClob
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|skipEmptyLOBTests
argument_list|()
condition|)
block|{
return|return;
block|}
name|runWithClobSize
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|test5ByteClob
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|skipTests
argument_list|()
condition|)
block|{
return|return;
block|}
name|runWithClobSize
argument_list|(
literal|5
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|test5KByteClob
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|skipTests
argument_list|()
condition|)
block|{
return|return;
block|}
name|runWithClobSize
argument_list|(
literal|5
operator|*
literal|1024
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|test1MBClob
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|skipTests
argument_list|()
condition|)
block|{
return|return;
block|}
name|runWithClobSize
argument_list|(
literal|1024
operator|*
literal|1024
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testNullClob
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|skipTests
argument_list|()
condition|)
block|{
return|return;
block|}
comment|// insert new clob
name|context
operator|.
name|newObject
argument_list|(
name|ClobTestEntity
operator|.
name|class
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
comment|// read the CLOB in the new context
name|List
argument_list|<
name|?
argument_list|>
name|objects2
init|=
name|context2
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|ClobTestEntity
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|objects2
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ClobTestEntity
name|clobObj2
init|=
operator|(
name|ClobTestEntity
operator|)
name|objects2
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
literal|"Expected null, got: '"
operator|+
name|clobObj2
operator|.
name|getClobCol
argument_list|()
operator|+
literal|"'"
argument_list|,
name|clobObj2
operator|.
name|getClobCol
argument_list|()
argument_list|)
expr_stmt|;
comment|// update and save Clob
name|clobObj2
operator|.
name|setClobCol
argument_list|(
literal|"updated rather small clob..."
argument_list|)
expr_stmt|;
name|context2
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
comment|// read into yet another context and check for changes
name|List
argument_list|<
name|?
argument_list|>
name|objects3
init|=
name|context3
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|ClobTestEntity
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|objects3
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ClobTestEntity
name|clobObj3
init|=
operator|(
name|ClobTestEntity
operator|)
name|objects3
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|clobObj2
operator|.
name|getClobCol
argument_list|()
argument_list|,
name|clobObj3
operator|.
name|getClobCol
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|runWithClobSize
parameter_list|(
name|int
name|sizeBytes
parameter_list|)
throws|throws
name|Exception
block|{
comment|// insert new clob
name|ClobTestEntity
name|clobObj1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|ClobTestEntity
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// init CLOB of a specified size
if|if
condition|(
name|sizeBytes
operator|==
literal|0
condition|)
block|{
name|clobObj1
operator|.
name|setClobCol
argument_list|(
literal|""
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|byte
index|[]
name|bytes
init|=
operator|new
name|byte
index|[
name|sizeBytes
index|]
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
name|sizeBytes
condition|;
name|i
operator|++
control|)
block|{
name|bytes
index|[
name|i
index|]
operator|=
operator|(
name|byte
operator|)
operator|(
literal|65
operator|+
operator|(
literal|50
operator|+
name|i
operator|)
operator|%
literal|50
operator|)
expr_stmt|;
block|}
name|clobObj1
operator|.
name|setClobCol
argument_list|(
operator|new
name|String
argument_list|(
name|bytes
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
comment|// read the CLOB in the new context
name|List
argument_list|<
name|?
argument_list|>
name|objects2
init|=
name|context2
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|ClobTestEntity
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|objects2
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ClobTestEntity
name|clobObj2
init|=
operator|(
name|ClobTestEntity
operator|)
name|objects2
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|clobObj1
operator|.
name|getClobCol
argument_list|()
argument_list|,
name|clobObj2
operator|.
name|getClobCol
argument_list|()
argument_list|)
expr_stmt|;
comment|// update and save Clob
name|clobObj2
operator|.
name|setClobCol
argument_list|(
literal|"updated rather small clob..."
argument_list|)
expr_stmt|;
name|context2
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
comment|// read into yet another context and check for changes
name|List
argument_list|<
name|?
argument_list|>
name|objects3
init|=
name|context3
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|ClobTestEntity
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|objects3
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ClobTestEntity
name|clobObj3
init|=
operator|(
name|ClobTestEntity
operator|)
name|objects3
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|clobObj2
operator|.
name|getClobCol
argument_list|()
argument_list|,
name|clobObj3
operator|.
name|getClobCol
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

