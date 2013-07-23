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
name|access
operator|.
name|types
operator|.
name|ByteArrayTypeTest
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
name|BlobTestEntity
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
name|UnitDbAdapter
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
name|DataContextBlobTest
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
name|UnitDbAdapter
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
if|if
condition|(
name|accessStackAdapter
operator|.
name|supportsLobs
argument_list|()
condition|)
block|{
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"BLOB_TEST"
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
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
specifier|protected
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
name|testEmptyBlob
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
if|if
condition|(
name|skipEmptyLOBTests
argument_list|()
condition|)
block|{
return|return;
block|}
name|runWithBlobSize
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|test5ByteBlob
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
name|runWithBlobSize
argument_list|(
literal|5
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|test5KByteBlob
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
name|runWithBlobSize
argument_list|(
literal|5
operator|*
literal|1024
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|test1MBBlob
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
name|runWithBlobSize
argument_list|(
literal|1024
operator|*
literal|1024
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testNullBlob
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
name|byte
index|[]
name|bytes2
init|=
operator|new
name|byte
index|[]
block|{
literal|'a'
block|,
literal|'b'
block|,
literal|'c'
block|,
literal|'d'
block|}
decl_stmt|;
comment|// insert new blob
name|context
operator|.
name|newObject
argument_list|(
name|BlobTestEntity
operator|.
name|class
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
comment|// read the BLOB in the new context
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
name|BlobTestEntity
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
name|BlobTestEntity
name|blobObj2
init|=
operator|(
name|BlobTestEntity
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
name|blobObj2
operator|.
name|getBlobCol
argument_list|()
argument_list|)
expr_stmt|;
comment|// update and save Blob
name|blobObj2
operator|.
name|setBlobCol
argument_list|(
name|bytes2
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
name|BlobTestEntity
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
name|BlobTestEntity
name|blobObj3
init|=
operator|(
name|BlobTestEntity
operator|)
name|objects3
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|ByteArrayTypeTest
operator|.
name|assertByteArraysEqual
argument_list|(
name|blobObj2
operator|.
name|getBlobCol
argument_list|()
argument_list|,
name|blobObj3
operator|.
name|getBlobCol
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|runWithBlobSize
parameter_list|(
name|int
name|sizeBytes
parameter_list|)
throws|throws
name|Exception
block|{
comment|// insert new clob
name|BlobTestEntity
name|blobObj1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|BlobTestEntity
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// init BLOB of a specified size
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
name|blobObj1
operator|.
name|setBlobCol
argument_list|(
name|bytes
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
name|BlobTestEntity
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
name|BlobTestEntity
name|blobObj2
init|=
operator|(
name|BlobTestEntity
operator|)
name|objects2
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|ByteArrayTypeTest
operator|.
name|assertByteArraysEqual
argument_list|(
name|blobObj1
operator|.
name|getBlobCol
argument_list|()
argument_list|,
name|blobObj2
operator|.
name|getBlobCol
argument_list|()
argument_list|)
expr_stmt|;
comment|// update and save Blob
name|blobObj2
operator|.
name|setBlobCol
argument_list|(
operator|new
name|byte
index|[]
block|{
literal|'1'
block|,
literal|'2'
block|}
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
name|BlobTestEntity
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
name|BlobTestEntity
name|blobObj3
init|=
operator|(
name|BlobTestEntity
operator|)
name|objects3
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|ByteArrayTypeTest
operator|.
name|assertByteArraysEqual
argument_list|(
name|blobObj2
operator|.
name|getBlobCol
argument_list|()
argument_list|,
name|blobObj3
operator|.
name|getBlobCol
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
