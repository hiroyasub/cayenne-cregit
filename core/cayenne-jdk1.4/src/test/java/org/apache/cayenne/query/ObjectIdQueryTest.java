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
name|query
package|;
end_package

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
name|ObjectId
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
name|map
operator|.
name|EntityResolver
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
name|remote
operator|.
name|hessian
operator|.
name|service
operator|.
name|HessianUtil
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
name|Util
import|;
end_import

begin_class
specifier|public
class|class
name|ObjectIdQueryTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testConstructorObjectId
parameter_list|()
block|{
name|ObjectId
name|oid
init|=
operator|new
name|ObjectId
argument_list|(
literal|"MockDataObject"
argument_list|,
literal|"a"
argument_list|,
literal|"b"
argument_list|)
decl_stmt|;
name|ObjectIdQuery
name|query
init|=
operator|new
name|ObjectIdQuery
argument_list|(
name|oid
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|oid
argument_list|,
name|query
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSerializability
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjectId
name|oid
init|=
operator|new
name|ObjectId
argument_list|(
literal|"test"
argument_list|,
literal|"a"
argument_list|,
literal|"b"
argument_list|)
decl_stmt|;
name|ObjectIdQuery
name|query
init|=
operator|new
name|ObjectIdQuery
argument_list|(
name|oid
argument_list|)
decl_stmt|;
name|Object
name|o
init|=
name|Util
operator|.
name|cloneViaSerialization
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|o
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|o
operator|instanceof
name|ObjectIdQuery
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|oid
argument_list|,
operator|(
operator|(
name|ObjectIdQuery
operator|)
name|o
operator|)
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSerializabilityWithHessian
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjectId
name|oid
init|=
operator|new
name|ObjectId
argument_list|(
literal|"test"
argument_list|,
literal|"a"
argument_list|,
literal|"b"
argument_list|)
decl_stmt|;
name|ObjectIdQuery
name|query
init|=
operator|new
name|ObjectIdQuery
argument_list|(
name|oid
argument_list|)
decl_stmt|;
name|Object
name|o
init|=
name|HessianUtil
operator|.
name|cloneViaClientServerSerialization
argument_list|(
name|query
argument_list|,
operator|new
name|EntityResolver
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|o
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|o
operator|instanceof
name|ObjectIdQuery
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|oid
argument_list|,
operator|(
operator|(
name|ObjectIdQuery
operator|)
name|o
operator|)
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Proper 'equals' and 'hashCode' implementations are important when mapping results      * obtained in a QueryChain back to the query.      */
specifier|public
name|void
name|testEquals
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjectIdQuery
name|q1
init|=
operator|new
name|ObjectIdQuery
argument_list|(
operator|new
name|ObjectId
argument_list|(
literal|"abc"
argument_list|,
literal|"a"
argument_list|,
literal|1
argument_list|)
argument_list|)
decl_stmt|;
name|ObjectIdQuery
name|q2
init|=
operator|new
name|ObjectIdQuery
argument_list|(
operator|new
name|ObjectId
argument_list|(
literal|"abc"
argument_list|,
literal|"a"
argument_list|,
literal|1
argument_list|)
argument_list|)
decl_stmt|;
name|ObjectIdQuery
name|q3
init|=
operator|new
name|ObjectIdQuery
argument_list|(
operator|new
name|ObjectId
argument_list|(
literal|"abc"
argument_list|,
literal|"a"
argument_list|,
literal|3
argument_list|)
argument_list|)
decl_stmt|;
name|ObjectIdQuery
name|q4
init|=
operator|new
name|ObjectIdQuery
argument_list|(
operator|new
name|ObjectId
argument_list|(
literal|"123"
argument_list|,
literal|"a"
argument_list|,
literal|1
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|q1
operator|.
name|equals
argument_list|(
name|q2
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|q1
operator|.
name|hashCode
argument_list|()
argument_list|,
name|q2
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|q1
operator|.
name|equals
argument_list|(
name|q3
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|q1
operator|.
name|hashCode
argument_list|()
operator|==
name|q3
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|q1
operator|.
name|equals
argument_list|(
name|q4
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|q1
operator|.
name|hashCode
argument_list|()
operator|==
name|q4
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testMetadata
parameter_list|()
block|{
name|ObjectIdQuery
name|q1
init|=
operator|new
name|ObjectIdQuery
argument_list|(
operator|new
name|ObjectId
argument_list|(
literal|"abc"
argument_list|,
literal|"a"
argument_list|,
literal|1
argument_list|)
argument_list|,
literal|true
argument_list|,
name|ObjectIdQuery
operator|.
name|CACHE_REFRESH
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|q1
operator|.
name|isFetchAllowed
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|q1
operator|.
name|isFetchMandatory
argument_list|()
argument_list|)
expr_stmt|;
name|QueryMetadata
name|md1
init|=
name|q1
operator|.
name|getMetaData
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|md1
operator|.
name|isFetchingDataRows
argument_list|()
argument_list|)
expr_stmt|;
name|ObjectIdQuery
name|q2
init|=
operator|new
name|ObjectIdQuery
argument_list|(
operator|new
name|ObjectId
argument_list|(
literal|"abc"
argument_list|,
literal|"a"
argument_list|,
literal|1
argument_list|)
argument_list|,
literal|false
argument_list|,
name|ObjectIdQuery
operator|.
name|CACHE
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|q2
operator|.
name|isFetchAllowed
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|q2
operator|.
name|isFetchMandatory
argument_list|()
argument_list|)
expr_stmt|;
name|QueryMetadata
name|md2
init|=
name|q2
operator|.
name|getMetaData
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|md2
operator|.
name|isFetchingDataRows
argument_list|()
argument_list|)
expr_stmt|;
name|ObjectIdQuery
name|q3
init|=
operator|new
name|ObjectIdQuery
argument_list|(
operator|new
name|ObjectId
argument_list|(
literal|"abc"
argument_list|,
literal|"a"
argument_list|,
literal|1
argument_list|)
argument_list|,
literal|false
argument_list|,
name|ObjectIdQuery
operator|.
name|CACHE_NOREFRESH
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|q3
operator|.
name|isFetchAllowed
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|q3
operator|.
name|isFetchMandatory
argument_list|()
argument_list|)
expr_stmt|;
name|QueryMetadata
name|md3
init|=
name|q3
operator|.
name|getMetaData
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|md3
operator|.
name|isFetchingDataRows
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

