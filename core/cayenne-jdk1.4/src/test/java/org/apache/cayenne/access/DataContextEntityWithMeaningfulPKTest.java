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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|art
operator|.
name|MeaningfulPKDep
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|art
operator|.
name|MeaningfulPKTest1
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
name|DataObjectUtils
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
name|query
operator|.
name|ObjectIdQuery
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
name|unit
operator|.
name|CayenneCase
import|;
end_import

begin_comment
comment|/**  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|DataContextEntityWithMeaningfulPKTest
extends|extends
name|CayenneCase
block|{
specifier|protected
name|DataContext
name|context
decl_stmt|;
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|deleteTestData
argument_list|()
expr_stmt|;
name|context
operator|=
name|getDomain
argument_list|()
operator|.
name|createDataContext
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testInsertWithMeaningfulPK
parameter_list|()
throws|throws
name|Exception
block|{
name|MeaningfulPKTest1
name|obj
init|=
operator|(
name|MeaningfulPKTest1
operator|)
name|context
operator|.
name|newObject
argument_list|(
literal|"MeaningfulPKTest1"
argument_list|)
decl_stmt|;
name|obj
operator|.
name|setPkAttribute
argument_list|(
operator|new
name|Integer
argument_list|(
literal|1000
argument_list|)
argument_list|)
expr_stmt|;
name|obj
operator|.
name|setDescr
argument_list|(
literal|"aaa-aaa"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|ObjectIdQuery
name|q
init|=
operator|new
name|ObjectIdQuery
argument_list|(
operator|new
name|ObjectId
argument_list|(
literal|"MeaningfulPKTest1"
argument_list|,
name|MeaningfulPKTest1
operator|.
name|PK_ATTRIBUTE_PK_COLUMN
argument_list|,
literal|1000
argument_list|)
argument_list|,
literal|true
argument_list|,
name|ObjectIdQuery
operator|.
name|CACHE_REFRESH
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|context
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGeneratedKey
parameter_list|()
throws|throws
name|Exception
block|{
name|MeaningfulPKTest1
name|obj
init|=
operator|(
name|MeaningfulPKTest1
operator|)
name|context
operator|.
name|newObject
argument_list|(
literal|"MeaningfulPKTest1"
argument_list|)
decl_stmt|;
name|obj
operator|.
name|setDescr
argument_list|(
literal|"aaa-aaa"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|obj
operator|.
name|getPkAttribute
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|obj
argument_list|,
name|DataObjectUtils
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|MeaningfulPKTest1
operator|.
name|class
argument_list|,
name|obj
operator|.
name|getPkAttribute
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|int
name|id
init|=
name|DataObjectUtils
operator|.
name|intPKForObject
argument_list|(
name|obj
argument_list|)
decl_stmt|;
name|Map
name|snapshot
init|=
name|context
operator|.
name|getObjectStore
argument_list|()
operator|.
name|getDataRowCache
argument_list|()
operator|.
name|getCachedSnapshot
argument_list|(
name|obj
operator|.
name|getObjectId
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|snapshot
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|snapshot
operator|.
name|containsKey
argument_list|(
name|MeaningfulPKTest1
operator|.
name|PK_ATTRIBUTE_PK_COLUMN
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Integer
argument_list|(
name|id
argument_list|)
argument_list|,
name|snapshot
operator|.
name|get
argument_list|(
name|MeaningfulPKTest1
operator|.
name|PK_ATTRIBUTE_PK_COLUMN
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testChangeKey
parameter_list|()
throws|throws
name|Exception
block|{
name|MeaningfulPKTest1
name|obj
init|=
operator|(
name|MeaningfulPKTest1
operator|)
name|context
operator|.
name|newObject
argument_list|(
literal|"MeaningfulPKTest1"
argument_list|)
decl_stmt|;
name|obj
operator|.
name|setPkAttribute
argument_list|(
operator|new
name|Integer
argument_list|(
literal|1000
argument_list|)
argument_list|)
expr_stmt|;
name|obj
operator|.
name|setDescr
argument_list|(
literal|"aaa-aaa"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|obj
operator|.
name|setPkAttribute
argument_list|(
operator|new
name|Integer
argument_list|(
literal|2000
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
comment|// assert that object id got fixed
name|ObjectId
name|id
init|=
name|obj
operator|.
name|getObjectId
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Integer
argument_list|(
literal|2000
argument_list|)
argument_list|,
name|id
operator|.
name|getIdSnapshot
argument_list|()
operator|.
name|get
argument_list|(
literal|"PK_ATTRIBUTE"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testToManyRelationshipWithMeaningfulPK1
parameter_list|()
throws|throws
name|Exception
block|{
name|MeaningfulPKTest1
name|obj
init|=
operator|(
name|MeaningfulPKTest1
operator|)
name|context
operator|.
name|newObject
argument_list|(
literal|"MeaningfulPKTest1"
argument_list|)
decl_stmt|;
name|obj
operator|.
name|setPkAttribute
argument_list|(
operator|new
name|Integer
argument_list|(
literal|1000
argument_list|)
argument_list|)
expr_stmt|;
name|obj
operator|.
name|setDescr
argument_list|(
literal|"aaa-aaa"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
comment|// must be able to resolve to-many relationship
name|context
operator|=
name|createDataContext
argument_list|()
expr_stmt|;
name|List
name|objects
init|=
name|context
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|MeaningfulPKTest1
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|obj
operator|=
operator|(
name|MeaningfulPKTest1
operator|)
name|objects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|obj
operator|.
name|getMeaningfulPKDepArray
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testToManyRelationshipWithMeaningfulPK2
parameter_list|()
throws|throws
name|Exception
block|{
name|MeaningfulPKTest1
name|obj
init|=
operator|(
name|MeaningfulPKTest1
operator|)
name|context
operator|.
name|newObject
argument_list|(
literal|"MeaningfulPKTest1"
argument_list|)
decl_stmt|;
name|obj
operator|.
name|setPkAttribute
argument_list|(
operator|new
name|Integer
argument_list|(
literal|1000
argument_list|)
argument_list|)
expr_stmt|;
name|obj
operator|.
name|setDescr
argument_list|(
literal|"aaa-aaa"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
comment|// must be able to set reverse relationship
name|MeaningfulPKDep
name|dep
init|=
operator|(
name|MeaningfulPKDep
operator|)
name|context
operator|.
name|newObject
argument_list|(
literal|"MeaningfulPKDep"
argument_list|)
decl_stmt|;
name|dep
operator|.
name|setToMeaningfulPK
argument_list|(
name|obj
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

