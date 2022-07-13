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
name|dba
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
name|ObjectContext
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
name|configuration
operator|.
name|server
operator|.
name|ServerRuntime
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
name|di
operator|.
name|Inject
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
name|query
operator|.
name|ObjectSelect
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
name|testdo
operator|.
name|qualified
operator|.
name|Qualified1
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
name|DerbyUnitDbAdapter
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
name|UnitDbAdapter
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
name|CayenneProjects
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
name|sql
operator|.
name|CallableStatement
import|;
end_import

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
name|sql
operator|.
name|SQLException
import|;
end_import

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
name|concurrent
operator|.
name|ExecutorService
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Executors
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
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
name|fail
import|;
end_import

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|CayenneProjects
operator|.
name|QUALIFIED_PROJECT
argument_list|)
specifier|public
class|class
name|ConcurrentPkGeneratorIT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|private
name|ServerRuntime
name|runtime
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|UnitDbAdapter
name|unitDbAdapter
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|prepareDerbyDb
parameter_list|()
block|{
comment|//use to fix random test failures on derby db
if|if
condition|(
name|unitDbAdapter
operator|instanceof
name|DerbyUnitDbAdapter
condition|)
block|{
try|try
init|(
name|Connection
name|connection
init|=
name|runtime
operator|.
name|getDataDomain
argument_list|()
operator|.
name|getDataNode
argument_list|(
literal|"qualified"
argument_list|)
operator|.
name|getDataSource
argument_list|()
operator|.
name|getConnection
argument_list|()
init|)
block|{
name|CallableStatement
name|cs
init|=
name|connection
operator|.
name|prepareCall
argument_list|(
literal|"CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY(?, ?)"
argument_list|)
decl_stmt|;
name|cs
operator|.
name|setString
argument_list|(
literal|1
argument_list|,
literal|"derby.language.sequence.preallocator"
argument_list|)
expr_stmt|;
name|cs
operator|.
name|setString
argument_list|(
literal|2
argument_list|,
literal|"1000"
argument_list|)
expr_stmt|;
name|cs
operator|.
name|execute
argument_list|()
expr_stmt|;
name|cs
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SQLException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
block|}
comment|/*      * Attempts to discover any problems regarding thread locking in the PkGenerator      */
annotation|@
name|Test
specifier|public
name|void
name|testConcurrentInserts
parameter_list|()
block|{
if|if
condition|(
operator|!
name|unitDbAdapter
operator|.
name|supportsPKGeneratorConcurrency
argument_list|()
condition|)
block|{
return|return;
block|}
specifier|final
name|DataMap
name|dataMap
init|=
name|runtime
operator|.
name|getDataDomain
argument_list|()
operator|.
name|getDataMap
argument_list|(
literal|"qualified"
argument_list|)
decl_stmt|;
comment|// clear out the table
name|ObjectContext
name|context
init|=
name|runtime
operator|.
name|newContext
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Qualified1
argument_list|>
name|qualified1s
init|=
name|context
operator|.
name|select
argument_list|(
name|ObjectSelect
operator|.
name|query
argument_list|(
name|Qualified1
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|context
operator|.
name|deleteObjects
argument_list|(
name|qualified1s
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
comment|// perform concurrent inserts
name|int
name|numThreads
init|=
literal|2
decl_stmt|;
name|int
name|insertsPerThread
init|=
literal|100
decl_stmt|;
name|ExecutorService
name|executor
init|=
name|Executors
operator|.
name|newFixedThreadPool
argument_list|(
name|numThreads
argument_list|)
decl_stmt|;
name|Runnable
name|task
init|=
parameter_list|()
lambda|->
block|{
try|try
block|{
name|ObjectContext
name|context1
init|=
name|runtime
operator|.
name|newContext
argument_list|()
decl_stmt|;
name|EntityResolver
name|entityResolver
init|=
name|context1
operator|.
name|getEntityResolver
argument_list|()
decl_stmt|;
for|for
control|(
name|ObjEntity
name|entity
range|:
name|dataMap
operator|.
name|getObjEntities
argument_list|()
control|)
block|{
name|context1
operator|.
name|newObject
argument_list|(
name|entityResolver
operator|.
name|getObjectFactory
argument_list|()
operator|.
name|getJavaClass
argument_list|(
name|entity
operator|.
name|getJavaClassName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|context1
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
decl_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|insertsPerThread
condition|;
name|j
operator|++
control|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|numThreads
condition|;
name|i
operator|++
control|)
block|{
name|executor
operator|.
name|submit
argument_list|(
name|task
argument_list|)
expr_stmt|;
block|}
block|}
comment|// check for completion or deadlock
name|executor
operator|.
name|shutdown
argument_list|()
expr_stmt|;
try|try
block|{
comment|// normally this completes in less than 2 seconds. If it takes 30 then it failed.
name|boolean
name|didFinish
init|=
name|executor
operator|.
name|awaitTermination
argument_list|(
literal|30
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|didFinish
condition|)
block|{
name|fail
argument_list|(
literal|"Concurrent inserts either deadlocked or contended over the lock too long."
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
comment|// check for gaps in the generated sequence numbers
name|qualified1s
operator|=
name|context
operator|.
name|select
argument_list|(
name|ObjectSelect
operator|.
name|query
argument_list|(
name|Qualified1
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|insertsPerThread
operator|*
name|numThreads
argument_list|,
name|qualified1s
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// PKs will be used in order most of the time, but the implementation doesn't guarantee it.
comment|//		qualified1s.sort(Comparator.comparing(Cayenne::intPKForObject));
comment|//
comment|//		int lastPk = Cayenne.intPKForObject(qualified1s.get(0)) - 1;
comment|//		for (Qualified1 qualified1 : qualified1s) {
comment|//			if (lastPk+1 != Cayenne.intPKForObject(qualified1)) {
comment|//				fail("Found gap in sequence number: " + lastPk + " - " + Cayenne.intPKForObject(qualified1));
comment|//			}
comment|//			lastPk++;
comment|//		}
block|}
block|}
end_class

end_unit

