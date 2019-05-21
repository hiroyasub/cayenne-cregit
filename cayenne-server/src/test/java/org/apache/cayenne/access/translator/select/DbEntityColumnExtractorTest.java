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
name|translator
operator|.
name|select
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
name|access
operator|.
name|sqlbuilder
operator|.
name|sqltree
operator|.
name|ColumnNode
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
name|DbRelationship
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
name|JoinType
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
name|sql
operator|.
name|Types
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|instanceOf
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
specifier|public
class|class
name|DbEntityColumnExtractorTest
extends|extends
name|BaseColumnExtractorTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testExtractNoPrefix
parameter_list|()
block|{
name|TranslatableQueryWrapper
name|wrapper
init|=
operator|new
name|MockQueryWrapperBuilder
argument_list|()
operator|.
name|withMetaData
argument_list|(
operator|new
name|MockQueryMetadataBuilder
argument_list|()
operator|.
name|withDbEntity
argument_list|(
name|createMockDbEntity
argument_list|(
literal|"mock"
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|TranslatorContext
name|context
init|=
operator|new
name|MockTranslatorContext
argument_list|(
name|wrapper
argument_list|)
decl_stmt|;
name|DbEntityColumnExtractor
name|extractor
init|=
operator|new
name|DbEntityColumnExtractor
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|extractor
operator|.
name|extract
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|context
operator|.
name|getResultNodeList
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ResultNodeDescriptor
name|descriptor0
init|=
name|context
operator|.
name|getResultNodeList
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|ResultNodeDescriptor
name|descriptor1
init|=
name|context
operator|.
name|getResultNodeList
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|descriptor0
operator|.
name|getProperty
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|descriptor0
operator|.
name|getNode
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|descriptor0
operator|.
name|getNode
argument_list|()
argument_list|,
name|instanceOf
argument_list|(
name|ColumnNode
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|descriptor0
operator|.
name|isAggregate
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|descriptor0
operator|.
name|isInDataRow
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"id"
argument_list|,
name|descriptor0
operator|.
name|getDataRowKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|descriptor0
operator|.
name|getDbAttribute
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Types
operator|.
name|BIGINT
argument_list|,
name|descriptor0
operator|.
name|getJdbcType
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|descriptor1
operator|.
name|getProperty
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|descriptor1
operator|.
name|getNode
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|descriptor1
operator|.
name|getNode
argument_list|()
argument_list|,
name|instanceOf
argument_list|(
name|ColumnNode
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|descriptor1
operator|.
name|isAggregate
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|descriptor1
operator|.
name|isInDataRow
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|descriptor1
operator|.
name|getDbAttribute
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"name"
argument_list|,
name|descriptor1
operator|.
name|getDataRowKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Types
operator|.
name|VARBINARY
argument_list|,
name|descriptor1
operator|.
name|getJdbcType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"byte[]"
argument_list|,
name|descriptor1
operator|.
name|getJavaType
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testExtractWithPrefix
parameter_list|()
block|{
name|DbEntity
name|mockDbEntity
init|=
name|createMockDbEntity
argument_list|(
literal|"mock1"
argument_list|)
decl_stmt|;
name|DbEntity
name|mock2DbEntity
init|=
name|createMockDbEntity
argument_list|(
literal|"mock2"
argument_list|)
decl_stmt|;
name|DataMap
name|dataMap
init|=
operator|new
name|DataMap
argument_list|()
decl_stmt|;
name|dataMap
operator|.
name|addDbEntity
argument_list|(
name|mockDbEntity
argument_list|)
expr_stmt|;
name|dataMap
operator|.
name|addDbEntity
argument_list|(
name|mock2DbEntity
argument_list|)
expr_stmt|;
name|mockDbEntity
operator|.
name|setDataMap
argument_list|(
name|dataMap
argument_list|)
expr_stmt|;
name|TranslatableQueryWrapper
name|wrapper
init|=
operator|new
name|MockQueryWrapperBuilder
argument_list|()
operator|.
name|withMetaData
argument_list|(
operator|new
name|MockQueryMetadataBuilder
argument_list|()
operator|.
name|withDbEntity
argument_list|(
name|mockDbEntity
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|TranslatorContext
name|context
init|=
operator|new
name|MockTranslatorContext
argument_list|(
name|wrapper
argument_list|)
decl_stmt|;
name|DbRelationship
name|relationship
init|=
operator|new
name|DbRelationship
argument_list|()
decl_stmt|;
name|relationship
operator|.
name|setSourceEntity
argument_list|(
name|mockDbEntity
argument_list|)
expr_stmt|;
name|relationship
operator|.
name|setTargetEntityName
argument_list|(
literal|"mock1"
argument_list|)
expr_stmt|;
name|context
operator|.
name|getTableTree
argument_list|()
operator|.
name|addJoinTable
argument_list|(
literal|"prefix"
argument_list|,
name|relationship
argument_list|,
name|JoinType
operator|.
name|INNER
argument_list|)
expr_stmt|;
name|DbEntityColumnExtractor
name|extractor
init|=
operator|new
name|DbEntityColumnExtractor
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|extractor
operator|.
name|extract
argument_list|(
literal|"prefix"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|context
operator|.
name|getResultNodeList
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ResultNodeDescriptor
name|descriptor0
init|=
name|context
operator|.
name|getResultNodeList
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|ResultNodeDescriptor
name|descriptor1
init|=
name|context
operator|.
name|getResultNodeList
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|descriptor0
operator|.
name|getProperty
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|descriptor0
operator|.
name|getNode
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|descriptor0
operator|.
name|getNode
argument_list|()
argument_list|,
name|instanceOf
argument_list|(
name|ColumnNode
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|descriptor0
operator|.
name|isAggregate
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|descriptor0
operator|.
name|isInDataRow
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"prefix.id"
argument_list|,
name|descriptor0
operator|.
name|getDataRowKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|descriptor0
operator|.
name|getDbAttribute
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Types
operator|.
name|BIGINT
argument_list|,
name|descriptor0
operator|.
name|getJdbcType
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|descriptor1
operator|.
name|getProperty
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|descriptor1
operator|.
name|getNode
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|descriptor1
operator|.
name|getNode
argument_list|()
argument_list|,
name|instanceOf
argument_list|(
name|ColumnNode
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|descriptor1
operator|.
name|isAggregate
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|descriptor1
operator|.
name|isInDataRow
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|descriptor1
operator|.
name|getDbAttribute
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"prefix.name"
argument_list|,
name|descriptor1
operator|.
name|getDataRowKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Types
operator|.
name|VARBINARY
argument_list|,
name|descriptor1
operator|.
name|getJdbcType
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

