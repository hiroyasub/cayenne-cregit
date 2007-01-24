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
name|sql
operator|.
name|Date
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
name|oneway
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
name|CayenneRuntimeException
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
name|dba
operator|.
name|mysql
operator|.
name|MySQLAdapter
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
name|LifecycleEventCallback
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
name|OneWayMappingCase
import|;
end_import

begin_comment
comment|/**  * @author Holger Hoffstaette  * @author Andrus Adamchik  * @deprecated since 3.0M1 in favor of {@link LifecycleEventCallback}. Will be removed in  *             later 3.0 milestones.  */
end_comment

begin_class
specifier|public
class|class
name|DataContextEventsTest
extends|extends
name|OneWayMappingCase
block|{
specifier|protected
name|DataContext
name|context
decl_stmt|;
specifier|protected
name|Artist
name|artist
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
name|context
operator|.
name|setTransactionEventsEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|artist
operator|=
operator|(
name|Artist
operator|)
name|context
operator|.
name|newObject
argument_list|(
literal|"Artist"
argument_list|)
expr_stmt|;
name|artist
operator|.
name|setArtistName
argument_list|(
literal|"artist1"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|artist
operator|.
name|resetEvents
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testDataContext
parameter_list|()
throws|throws
name|Exception
block|{
name|assertTrue
argument_list|(
name|context
operator|.
name|isTransactionEventsEnabled
argument_list|()
argument_list|)
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
name|artist
operator|.
name|receivedWillCommit
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|artist
operator|.
name|receivedDidCommit
argument_list|()
argument_list|)
expr_stmt|;
comment|// modify artist
name|artist
operator|.
name|setDateOfBirth
argument_list|(
operator|new
name|Date
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
comment|// commit the pending changes
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|artist
operator|.
name|receivedWillCommit
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|artist
operator|.
name|receivedDidCommit
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testDataContextRolledBackTransaction
parameter_list|()
throws|throws
name|Exception
block|{
comment|// This test will not work on MySQL, since transaction support
comment|// is either non-existent or dubious (depending on your view).
comment|// See: http://www.mysql.com/doc/en/Design_Limitations.html
if|if
condition|(
operator|(
operator|(
name|DataNode
operator|)
name|getDomain
argument_list|()
operator|.
name|getDataNodes
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|)
operator|.
name|getAdapter
argument_list|()
operator|.
name|getClass
argument_list|()
operator|==
name|MySQLAdapter
operator|.
name|class
condition|)
block|{
return|return;
block|}
comment|// turn off cayenne validation
name|context
operator|.
name|setValidatingObjectsOnCommit
argument_list|(
literal|false
argument_list|)
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
name|artist
operator|.
name|receivedWillCommit
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|artist
operator|.
name|receivedDidCommit
argument_list|()
argument_list|)
expr_stmt|;
comment|// modify artist so that it cannot be saved correctly anymore
name|artist
operator|.
name|setArtistName
argument_list|(
literal|null
argument_list|)
expr_stmt|;
comment|// name is mandatory
try|try
block|{
comment|// commit the pending changes
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"No exception on saving invalid data."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CayenneRuntimeException
name|ex
parameter_list|)
block|{
comment|// expected
block|}
name|assertTrue
argument_list|(
name|artist
operator|.
name|receivedWillCommit
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|artist
operator|.
name|receivedDidCommit
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// tests that no notifications are sent to objects that won't be updated/inserted into
comment|// database
specifier|public
name|void
name|testDataContextNoModifications
parameter_list|()
block|{
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
name|artist
operator|.
name|receivedWillCommit
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|artist
operator|.
name|receivedDidCommit
argument_list|()
argument_list|)
expr_stmt|;
comment|// commit without any pending changes
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|artist
operator|.
name|receivedDidCommit
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|artist
operator|.
name|receivedWillCommit
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

