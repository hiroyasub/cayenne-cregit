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
operator|.
name|util
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
name|DefaultOperationObserverTest
extends|extends
name|ServerCase
block|{
specifier|public
name|void
name|testHasExceptions1
parameter_list|()
throws|throws
name|Exception
block|{
name|DefaultOperationObserver
name|observer
init|=
operator|new
name|DefaultOperationObserver
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|observer
operator|.
name|hasExceptions
argument_list|()
argument_list|)
expr_stmt|;
name|observer
operator|.
name|nextGlobalException
argument_list|(
operator|new
name|Exception
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|observer
operator|.
name|hasExceptions
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testHasExceptions2
parameter_list|()
throws|throws
name|Exception
block|{
name|DefaultOperationObserver
name|observer
init|=
operator|new
name|DefaultOperationObserver
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|observer
operator|.
name|hasExceptions
argument_list|()
argument_list|)
expr_stmt|;
name|observer
operator|.
name|nextQueryException
argument_list|(
operator|new
name|SelectQuery
argument_list|()
argument_list|,
operator|new
name|Exception
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|observer
operator|.
name|hasExceptions
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
