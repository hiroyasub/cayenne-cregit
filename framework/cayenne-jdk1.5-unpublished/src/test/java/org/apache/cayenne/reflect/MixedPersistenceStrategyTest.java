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
name|reflect
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|art
operator|.
name|MixedPersistenceStrategy
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
name|MixedPersistenceStrategy2
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
name|ValueHolder
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
name|DataContext
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
comment|/**  * Tests conflicts between field and map-based persistence.  *   * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|MixedPersistenceStrategyTest
extends|extends
name|CayenneCase
block|{
specifier|public
name|void
name|testConflictingField1
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteTestData
argument_list|()
expr_stmt|;
name|createTestData
argument_list|(
literal|"testConflictingField"
argument_list|)
expr_stmt|;
name|DataContext
name|c
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|MixedPersistenceStrategy
name|object
init|=
name|DataObjectUtils
operator|.
name|objectForPK
argument_list|(
name|c
argument_list|,
name|MixedPersistenceStrategy
operator|.
name|class
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|object
operator|.
name|getDetails
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|object
operator|.
name|getDetails
argument_list|()
operator|instanceof
name|ValueHolder
argument_list|)
expr_stmt|;
block|}
comment|/**      * This test case reproduces CAY-582 bug.      */
specifier|public
name|void
name|testConflictingField2
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteTestData
argument_list|()
expr_stmt|;
name|createTestData
argument_list|(
literal|"testConflictingField"
argument_list|)
expr_stmt|;
name|DataContext
name|c
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|MixedPersistenceStrategy2
name|detail1
init|=
name|DataObjectUtils
operator|.
name|objectForPK
argument_list|(
name|c
argument_list|,
name|MixedPersistenceStrategy2
operator|.
name|class
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|MixedPersistenceStrategy2
name|detail2
init|=
name|DataObjectUtils
operator|.
name|objectForPK
argument_list|(
name|c
argument_list|,
name|MixedPersistenceStrategy2
operator|.
name|class
argument_list|,
literal|2
argument_list|)
decl_stmt|;
comment|// resolve master (this is where CAY-582 exception happens)
name|assertEquals
argument_list|(
literal|"n1"
argument_list|,
name|detail1
operator|.
name|getMaster
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|detail2
operator|.
name|getMaster
argument_list|()
operator|.
name|getDetails
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|detail2
operator|.
name|getMaster
argument_list|()
operator|.
name|getDetails
argument_list|()
operator|instanceof
name|ValueHolder
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

