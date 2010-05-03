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
name|HashMap
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
name|cayenne
operator|.
name|DataRow
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
name|event
operator|.
name|MockEventManager
import|;
end_import

begin_comment
comment|/**  * A "lightweight" DataRowStore.  */
end_comment

begin_class
specifier|public
class|class
name|MockDataRowStore
extends|extends
name|DataRowStore
block|{
specifier|private
specifier|static
specifier|final
name|Map
name|TEST_DEFAULTS
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
static|static
block|{
name|TEST_DEFAULTS
operator|.
name|put
argument_list|(
name|DataRowStore
operator|.
name|SNAPSHOT_CACHE_SIZE_PROPERTY
argument_list|,
operator|new
name|Integer
argument_list|(
literal|10
argument_list|)
argument_list|)
expr_stmt|;
name|TEST_DEFAULTS
operator|.
name|put
argument_list|(
name|DataRowStore
operator|.
name|REMOTE_NOTIFICATION_PROPERTY
argument_list|,
name|Boolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
block|}
specifier|public
name|MockDataRowStore
parameter_list|()
block|{
name|super
argument_list|(
literal|"mock DataRowStore"
argument_list|,
name|TEST_DEFAULTS
argument_list|,
operator|new
name|MockEventManager
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * A backdoor to add test snapshots.      */
specifier|public
name|void
name|putSnapshot
parameter_list|(
name|ObjectId
name|id
parameter_list|,
name|DataRow
name|snapshot
parameter_list|)
block|{
name|snapshots
operator|.
name|put
argument_list|(
name|id
argument_list|,
name|snapshot
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|putSnapshot
parameter_list|(
name|ObjectId
name|id
parameter_list|,
name|Map
name|snapshot
parameter_list|)
block|{
name|snapshots
operator|.
name|put
argument_list|(
name|id
argument_list|,
operator|new
name|DataRow
argument_list|(
name|snapshot
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|putEmptySnapshot
parameter_list|(
name|ObjectId
name|id
parameter_list|)
block|{
name|snapshots
operator|.
name|put
argument_list|(
name|id
argument_list|,
operator|new
name|DataRow
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

