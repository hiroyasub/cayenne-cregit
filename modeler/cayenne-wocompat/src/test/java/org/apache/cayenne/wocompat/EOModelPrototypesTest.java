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
name|wocompat
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
name|map
operator|.
name|DataMap
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
name|DbAttribute
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
name|DbEntity
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
name|ObjAttribute
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
name|ObjEntity
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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
name|net
operator|.
name|URL
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
name|assertNotNull
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

begin_class
specifier|public
class|class
name|EOModelPrototypesTest
block|{
specifier|private
name|URL
name|url
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|url
operator|=
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"prototypes.eomodeld/"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|url
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSkipPrototypes
parameter_list|()
throws|throws
name|Exception
block|{
name|DataMap
name|map
init|=
operator|new
name|EOModelProcessor
argument_list|()
operator|.
name|loadEOModel
argument_list|(
name|url
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|map
operator|.
name|getObjEntity
argument_list|(
literal|"Document"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|map
operator|.
name|getObjEntity
argument_list|(
literal|"EOPrototypes"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|map
operator|.
name|getObjEntity
argument_list|(
literal|"EOXYZPrototypes"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDbAttributeType
parameter_list|()
throws|throws
name|Exception
block|{
name|DataMap
name|map
init|=
operator|new
name|EOModelProcessor
argument_list|()
operator|.
name|loadEOModel
argument_list|(
name|url
argument_list|)
decl_stmt|;
name|DbEntity
name|dbe
init|=
name|map
operator|.
name|getDbEntity
argument_list|(
literal|"DOCUMENT"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|dbe
argument_list|)
expr_stmt|;
comment|// test that an attribute that has ObjAttribute has its type configured
name|DbAttribute
name|dba1
init|=
name|dbe
operator|.
name|getAttribute
argument_list|(
literal|"DOCUMENT_TYPE"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Types
operator|.
name|VARCHAR
argument_list|,
name|dba1
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
comment|// test that a numeric attribute has its type configured
name|DbAttribute
name|dba2
init|=
name|dbe
operator|.
name|getAttribute
argument_list|(
literal|"TEST_NUMERIC"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Types
operator|.
name|INTEGER
argument_list|,
name|dba2
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
comment|// test that an attribute that has no ObjAttribute has its type configured
name|DbAttribute
name|dba3
init|=
name|dbe
operator|.
name|getAttribute
argument_list|(
literal|"DOCUMENT_ID"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Types
operator|.
name|INTEGER
argument_list|,
name|dba3
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// TODO: move this test to EOModelProcessorInheritanceTst. The original problem had
comment|// nothing
comment|// to do with prototypes...
annotation|@
name|Test
specifier|public
name|void
name|testSameColumnMapping
parameter_list|()
throws|throws
name|Exception
block|{
name|DataMap
name|map
init|=
operator|new
name|EOModelProcessor
argument_list|()
operator|.
name|loadEOModel
argument_list|(
name|url
argument_list|)
decl_stmt|;
name|ObjEntity
name|estimateOE
init|=
name|map
operator|.
name|getObjEntity
argument_list|(
literal|"Estimate"
argument_list|)
decl_stmt|;
name|ObjEntity
name|invoiceOE
init|=
name|map
operator|.
name|getObjEntity
argument_list|(
literal|"Invoice"
argument_list|)
decl_stmt|;
name|ObjEntity
name|vendorOE
init|=
name|map
operator|.
name|getObjEntity
argument_list|(
literal|"VendorPO"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|estimateOE
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|invoiceOE
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|vendorOE
argument_list|)
expr_stmt|;
name|ObjAttribute
name|en
init|=
operator|(
name|ObjAttribute
operator|)
name|estimateOE
operator|.
name|getAttribute
argument_list|(
literal|"estimateNumber"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"DOCUMENT_NUMBER"
argument_list|,
name|en
operator|.
name|getDbAttributePath
argument_list|()
argument_list|)
expr_stmt|;
name|ObjAttribute
name|in
init|=
operator|(
name|ObjAttribute
operator|)
name|invoiceOE
operator|.
name|getAttribute
argument_list|(
literal|"invoiceNumber"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"DOCUMENT_NUMBER"
argument_list|,
name|in
operator|.
name|getDbAttributePath
argument_list|()
argument_list|)
expr_stmt|;
name|ObjAttribute
name|vn
init|=
operator|(
name|ObjAttribute
operator|)
name|vendorOE
operator|.
name|getAttribute
argument_list|(
literal|"purchaseOrderNumber"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"DOCUMENT_NUMBER"
argument_list|,
name|vn
operator|.
name|getDbAttributePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// TODO: move this test to EOModelProcessorInheritanceTst. The original problem had
comment|// nothing to do with prototypes...
annotation|@
name|Test
specifier|public
name|void
name|testOverridingAttributes
parameter_list|()
throws|throws
name|Exception
block|{
name|DataMap
name|map
init|=
operator|new
name|EOModelProcessor
argument_list|()
operator|.
name|loadEOModel
argument_list|(
name|url
argument_list|)
decl_stmt|;
name|ObjEntity
name|estimateOE
init|=
name|map
operator|.
name|getObjEntity
argument_list|(
literal|"Estimate"
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|estimateOE
argument_list|,
name|estimateOE
operator|.
name|getAttribute
argument_list|(
literal|"created"
argument_list|)
operator|.
name|getEntity
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|estimateOE
argument_list|,
name|estimateOE
operator|.
name|getAttribute
argument_list|(
literal|"estimateNumber"
argument_list|)
operator|.
name|getEntity
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

