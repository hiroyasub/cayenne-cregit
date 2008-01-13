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
name|org
operator|.
name|apache
operator|.
name|art
operator|.
name|Artist
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

begin_class
specifier|public
class|class
name|NestedDataContextRollbackTest
extends|extends
name|CayenneCase
block|{
specifier|public
name|void
name|testRollbackChanges
parameter_list|()
block|{
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|DataContext
name|child1
init|=
name|context
operator|.
name|createChildDataContext
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|context
operator|.
name|hasChanges
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|child1
operator|.
name|hasChanges
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|newObject
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
expr_stmt|;
name|child1
operator|.
name|newObject
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|hasChanges
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|child1
operator|.
name|hasChanges
argument_list|()
argument_list|)
expr_stmt|;
name|child1
operator|.
name|rollbackChanges
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|context
operator|.
name|hasChanges
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|child1
operator|.
name|hasChanges
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testRollbackChangesLocally
parameter_list|()
block|{
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|DataContext
name|child1
init|=
name|context
operator|.
name|createChildDataContext
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|context
operator|.
name|hasChanges
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|child1
operator|.
name|hasChanges
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|newObject
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
expr_stmt|;
name|child1
operator|.
name|newObject
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|hasChanges
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|child1
operator|.
name|hasChanges
argument_list|()
argument_list|)
expr_stmt|;
name|child1
operator|.
name|rollbackChangesLocally
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|hasChanges
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|child1
operator|.
name|hasChanges
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

