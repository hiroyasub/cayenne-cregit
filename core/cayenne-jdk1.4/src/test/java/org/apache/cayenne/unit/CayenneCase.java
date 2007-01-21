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
name|unit
package|;
end_package

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Connection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|access
operator|.
name|DataNode
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
name|UnitTestDomain
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
name|conf
operator|.
name|Configuration
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
name|conf
operator|.
name|DefaultConfiguration
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
name|conn
operator|.
name|DataSourceInfo
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
name|ObjEntity
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
name|util
operator|.
name|SQLTemplateCustomizer
import|;
end_import

begin_comment
comment|/**  * Superclass of Cayenne test cases. Provides access to shared connection resources.  *   * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|CayenneCase
extends|extends
name|BasicCase
block|{
specifier|public
specifier|static
specifier|final
name|String
name|TEST_ACCESS_STACK
init|=
literal|"TestStack"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|MULTI_TIER_ACCESS_STACK
init|=
literal|"MultiTierStack"
decl_stmt|;
static|static
block|{
comment|// create dummy shared config
name|Configuration
name|config
init|=
operator|new
name|DefaultConfiguration
argument_list|()
block|{
specifier|public
name|void
name|initialize
parameter_list|()
block|{
block|}
block|}
decl_stmt|;
name|Configuration
operator|.
name|initializeSharedConfiguration
argument_list|(
name|config
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|AccessStack
name|accessStack
decl_stmt|;
specifier|public
name|CayenneCase
parameter_list|()
block|{
comment|// make sure CayenneTestResources shared instance is loaded
name|CayenneResources
operator|.
name|getResources
argument_list|()
expr_stmt|;
name|this
operator|.
name|accessStack
operator|=
name|buildAccessStack
argument_list|()
expr_stmt|;
block|}
comment|/**      * A helper method that allows to block any query passing through the DataDomain, thus      * allowing to check for stray fault firings during the test case. Must be paired with      *<em>unblockQueries</em>.      */
specifier|protected
name|void
name|blockQueries
parameter_list|()
block|{
name|getDomain
argument_list|()
operator|.
name|setBlockingQueries
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|unblockQueries
parameter_list|()
block|{
name|getDomain
argument_list|()
operator|.
name|setBlockingQueries
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|AccessStack
name|buildAccessStack
parameter_list|()
block|{
return|return
name|CayenneResources
operator|.
name|getResources
argument_list|()
operator|.
name|getAccessStack
argument_list|(
name|TEST_ACCESS_STACK
argument_list|)
return|;
block|}
specifier|protected
name|AccessStackAdapter
name|getAccessStackAdapter
parameter_list|()
block|{
return|return
name|accessStack
operator|.
name|getAdapter
argument_list|(
name|getNode
argument_list|()
argument_list|)
return|;
block|}
specifier|protected
name|DataSourceInfo
name|getConnectionInfo
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|CayenneResources
operator|.
name|getResources
argument_list|()
operator|.
name|getConnectionInfo
argument_list|()
return|;
block|}
specifier|protected
name|Connection
name|getConnection
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|getNode
argument_list|()
operator|.
name|getDataSource
argument_list|()
operator|.
name|getConnection
argument_list|()
return|;
block|}
specifier|protected
name|UnitTestDomain
name|getDomain
parameter_list|()
block|{
return|return
name|accessStack
operator|.
name|getDataDomain
argument_list|()
return|;
block|}
specifier|protected
name|SQLTemplateCustomizer
name|getSQLTemplateBuilder
parameter_list|()
block|{
name|SQLTemplateCustomizer
name|customizer
init|=
name|CayenneResources
operator|.
name|getResources
argument_list|()
operator|.
name|getSQLTemplateCustomizer
argument_list|()
decl_stmt|;
comment|// make sure adapter is correct
name|customizer
operator|.
name|setAdapter
argument_list|(
name|getAccessStackAdapter
argument_list|()
operator|.
name|getAdapter
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|customizer
return|;
block|}
comment|/**      * Creates test data via a mechanism preconfigured in the access stack. Default      * mechanism is loading test data DML from XML file.      */
specifier|protected
name|void
name|createTestData
parameter_list|(
name|String
name|testName
parameter_list|)
throws|throws
name|Exception
block|{
name|accessStack
operator|.
name|createTestData
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
argument_list|,
name|testName
argument_list|,
name|Collections
operator|.
name|EMPTY_MAP
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|createTestData
parameter_list|(
name|String
name|testName
parameter_list|,
name|Map
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|accessStack
operator|.
name|createTestData
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
argument_list|,
name|testName
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|DataNode
name|getNode
parameter_list|()
block|{
return|return
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
return|;
block|}
specifier|protected
name|DataContext
name|createDataContext
parameter_list|()
block|{
return|return
name|createDataContextWithSharedCache
argument_list|()
return|;
block|}
comment|/**      * Creates a DataContext that uses shared snapshot cache and is based on default test      * domain.      */
specifier|protected
name|DataContext
name|createDataContextWithSharedCache
parameter_list|()
block|{
comment|// remove listeners for snapshot events
name|getDomain
argument_list|()
operator|.
name|getEventManager
argument_list|()
operator|.
name|removeAllListeners
argument_list|(
name|getDomain
argument_list|()
operator|.
name|getSharedSnapshotCache
argument_list|()
operator|.
name|getSnapshotEventSubject
argument_list|()
argument_list|)
expr_stmt|;
comment|// clear cache...
name|getDomain
argument_list|()
operator|.
name|getSharedSnapshotCache
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
name|getDomain
argument_list|()
operator|.
name|getQueryCache
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
name|DataContext
name|context
init|=
name|getDomain
argument_list|()
operator|.
name|createDataContext
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|getDomain
argument_list|()
operator|.
name|getSharedSnapshotCache
argument_list|()
argument_list|,
name|context
operator|.
name|getObjectStore
argument_list|()
operator|.
name|getDataRowCache
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
comment|/**      * Creates a DataContext that uses local snapshot cache and is based on default test      * domain.      */
specifier|protected
name|DataContext
name|createDataContextWithLocalCache
parameter_list|()
block|{
name|DataContext
name|context
init|=
name|getDomain
argument_list|()
operator|.
name|createDataContext
argument_list|(
literal|false
argument_list|)
decl_stmt|;
name|assertNotSame
argument_list|(
name|getDomain
argument_list|()
operator|.
name|getSharedSnapshotCache
argument_list|()
argument_list|,
name|context
operator|.
name|getObjectStore
argument_list|()
operator|.
name|getDataRowCache
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
comment|/**      * Returns AccessStack.      */
specifier|protected
name|AccessStack
name|getAccessStack
parameter_list|()
block|{
return|return
name|accessStack
return|;
block|}
specifier|protected
name|void
name|deleteTestData
parameter_list|()
throws|throws
name|Exception
block|{
name|accessStack
operator|.
name|deleteTestData
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|DbEntity
name|getDbEntity
parameter_list|(
name|String
name|dbEntityName
parameter_list|)
block|{
comment|// retrieve DbEntity the hard way, bypassing the resolver...
name|Iterator
name|it
init|=
name|getDomain
argument_list|()
operator|.
name|getDataMaps
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|DataMap
name|map
init|=
operator|(
name|DataMap
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|Iterator
name|dbEntities
init|=
name|map
operator|.
name|getDbEntities
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|dbEntities
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|DbEntity
name|e
init|=
operator|(
name|DbEntity
operator|)
name|dbEntities
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|dbEntityName
operator|.
name|equals
argument_list|(
name|e
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|e
return|;
block|}
block|}
block|}
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"No DbEntity found: "
operator|+
name|dbEntityName
argument_list|)
throw|;
block|}
specifier|protected
name|ObjEntity
name|getObjEntity
parameter_list|(
name|String
name|objEntityName
parameter_list|)
block|{
comment|// retrieve ObjEntity the hard way, bypassing the resolver...
name|Iterator
name|it
init|=
name|getDomain
argument_list|()
operator|.
name|getDataMaps
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|DataMap
name|map
init|=
operator|(
name|DataMap
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|Iterator
name|objEntities
init|=
name|map
operator|.
name|getObjEntities
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|objEntities
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ObjEntity
name|e
init|=
operator|(
name|ObjEntity
operator|)
name|objEntities
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|objEntityName
operator|.
name|equals
argument_list|(
name|e
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|e
return|;
block|}
block|}
block|}
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"No ObjEntity found: "
operator|+
name|objEntityName
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

