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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|DeleteDenyException
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
name|PersistenceState
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
name|ObjectDiff
operator|.
name|ArcOperation
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
name|graph
operator|.
name|NodeDiff
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
name|DeleteRule
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
name|map
operator|.
name|ObjRelationship
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
name|test
operator|.
name|jdbc
operator|.
name|DBHelper
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
name|relationship
operator|.
name|DeleteRuleFlatA
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
name|relationship
operator|.
name|DeleteRuleFlatB
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
name|relationship
operator|.
name|DeleteRuleTest1
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
name|relationship
operator|.
name|DeleteRuleTest2
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
name|relationship
operator|.
name|DeleteRuleTest3
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
name|RELATIONSHIPS_PROJECT
argument_list|)
specifier|public
class|class
name|DeleteRulesTest
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|private
name|DataContext
name|context
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|DBHelper
name|dbHelper
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|void
name|setUpAfterInjection
parameter_list|()
throws|throws
name|Exception
block|{
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"DELETE_RULE_TEST3"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"DELETE_RULE_TEST1"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"DELETE_RULE_TEST2"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"DELETE_RULE_JOIN"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"DELETE_RULE_FLATB"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"DELETE_RULE_FLATA"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testDenyToOne
parameter_list|()
block|{
name|DeleteRuleTest1
name|test1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|DeleteRuleTest1
operator|.
name|class
argument_list|)
decl_stmt|;
name|DeleteRuleTest2
name|test2
init|=
name|context
operator|.
name|newObject
argument_list|(
name|DeleteRuleTest2
operator|.
name|class
argument_list|)
decl_stmt|;
name|test1
operator|.
name|setTest2
argument_list|(
name|test2
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
try|try
block|{
name|context
operator|.
name|deleteObjects
argument_list|(
name|test1
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// GOOD!
block|}
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testNoActionToOne
parameter_list|()
block|{
name|DeleteRuleTest2
name|test2
init|=
name|context
operator|.
name|newObject
argument_list|(
name|DeleteRuleTest2
operator|.
name|class
argument_list|)
decl_stmt|;
name|DeleteRuleTest3
name|test3
init|=
name|context
operator|.
name|newObject
argument_list|(
name|DeleteRuleTest3
operator|.
name|class
argument_list|)
decl_stmt|;
name|test3
operator|.
name|setToDeleteRuleTest2
argument_list|(
name|test2
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
comment|// must go on without exceptions...
name|context
operator|.
name|deleteObjects
argument_list|(
name|test3
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testNoActionToMany
parameter_list|()
block|{
name|DeleteRuleTest2
name|test2
init|=
name|context
operator|.
name|newObject
argument_list|(
name|DeleteRuleTest2
operator|.
name|class
argument_list|)
decl_stmt|;
name|DeleteRuleTest3
name|test3
init|=
name|context
operator|.
name|newObject
argument_list|(
name|DeleteRuleTest3
operator|.
name|class
argument_list|)
decl_stmt|;
name|test3
operator|.
name|setToDeleteRuleTest2
argument_list|(
name|test2
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
comment|// must go on without exceptions...
name|context
operator|.
name|deleteObjects
argument_list|(
name|test2
argument_list|)
expr_stmt|;
comment|// don't commit, since this will cause a constraint exception
block|}
specifier|public
name|void
name|testNoActionFlattened
parameter_list|()
block|{
comment|// temporarily set delete rule to NOACTION...
name|int
name|oldRule
init|=
name|changeDeleteRule
argument_list|(
name|DeleteRule
operator|.
name|NO_ACTION
argument_list|)
decl_stmt|;
try|try
block|{
name|DeleteRuleFlatA
name|a
init|=
name|context
operator|.
name|newObject
argument_list|(
name|DeleteRuleFlatA
operator|.
name|class
argument_list|)
decl_stmt|;
name|DeleteRuleFlatB
name|b
init|=
name|context
operator|.
name|newObject
argument_list|(
name|DeleteRuleFlatB
operator|.
name|class
argument_list|)
decl_stmt|;
name|a
operator|.
name|addToFlatB
argument_list|(
name|b
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
comment|// must go on without exceptions...
name|context
operator|.
name|deleteObjects
argument_list|(
name|a
argument_list|)
expr_stmt|;
comment|// assert that join is deleted
name|assertJoinDeleted
argument_list|(
name|a
argument_list|,
name|b
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|DELETED
argument_list|,
name|a
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|b
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|b
operator|.
name|getUntitledRel
argument_list|()
operator|.
name|contains
argument_list|(
name|a
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|changeDeleteRule
argument_list|(
name|oldRule
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testNoActionFlattenedNoReverse
parameter_list|()
block|{
comment|// temporarily set delete rule to NOACTION...
name|int
name|oldRule
init|=
name|changeDeleteRule
argument_list|(
name|DeleteRule
operator|.
name|NO_ACTION
argument_list|)
decl_stmt|;
name|ObjRelationship
name|reverse
init|=
name|unsetReverse
argument_list|()
decl_stmt|;
try|try
block|{
name|DeleteRuleFlatA
name|a
init|=
name|context
operator|.
name|newObject
argument_list|(
name|DeleteRuleFlatA
operator|.
name|class
argument_list|)
decl_stmt|;
name|DeleteRuleFlatB
name|b
init|=
name|context
operator|.
name|newObject
argument_list|(
name|DeleteRuleFlatB
operator|.
name|class
argument_list|)
decl_stmt|;
name|a
operator|.
name|addToFlatB
argument_list|(
name|b
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
comment|// must go on without exceptions...
name|context
operator|.
name|deleteObjects
argument_list|(
name|a
argument_list|)
expr_stmt|;
comment|// assert that join is deleted
name|assertJoinDeleted
argument_list|(
name|a
argument_list|,
name|b
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|DELETED
argument_list|,
name|a
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|b
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|changeDeleteRule
argument_list|(
name|oldRule
argument_list|)
expr_stmt|;
name|restoreReverse
argument_list|(
name|reverse
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testCascadeFlattened
parameter_list|()
block|{
comment|// temporarily set delete rule to CASCADE...
name|int
name|oldRule
init|=
name|changeDeleteRule
argument_list|(
name|DeleteRule
operator|.
name|CASCADE
argument_list|)
decl_stmt|;
try|try
block|{
name|DeleteRuleFlatA
name|a
init|=
name|context
operator|.
name|newObject
argument_list|(
name|DeleteRuleFlatA
operator|.
name|class
argument_list|)
decl_stmt|;
name|DeleteRuleFlatB
name|b
init|=
name|context
operator|.
name|newObject
argument_list|(
name|DeleteRuleFlatB
operator|.
name|class
argument_list|)
decl_stmt|;
name|a
operator|.
name|addToFlatB
argument_list|(
name|b
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
comment|// must go on without exceptions...
name|context
operator|.
name|deleteObjects
argument_list|(
name|a
argument_list|)
expr_stmt|;
comment|// assert that join is deleted
name|assertJoinDeleted
argument_list|(
name|a
argument_list|,
name|b
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|TRANSIENT
argument_list|,
name|a
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|TRANSIENT
argument_list|,
name|b
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|changeDeleteRule
argument_list|(
name|oldRule
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testCascadeFlattenedNoReverse
parameter_list|()
block|{
comment|// temporarily set delete rule to CASCADE...
name|int
name|oldRule
init|=
name|changeDeleteRule
argument_list|(
name|DeleteRule
operator|.
name|CASCADE
argument_list|)
decl_stmt|;
name|ObjRelationship
name|reverse
init|=
name|unsetReverse
argument_list|()
decl_stmt|;
try|try
block|{
name|DeleteRuleFlatA
name|a
init|=
name|context
operator|.
name|newObject
argument_list|(
name|DeleteRuleFlatA
operator|.
name|class
argument_list|)
decl_stmt|;
name|DeleteRuleFlatB
name|b
init|=
name|context
operator|.
name|newObject
argument_list|(
name|DeleteRuleFlatB
operator|.
name|class
argument_list|)
decl_stmt|;
name|a
operator|.
name|addToFlatB
argument_list|(
name|b
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
comment|// must go on without exceptions...
name|context
operator|.
name|deleteObjects
argument_list|(
name|a
argument_list|)
expr_stmt|;
comment|// assert that join is deleted
name|assertJoinDeleted
argument_list|(
name|a
argument_list|,
name|b
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|TRANSIENT
argument_list|,
name|a
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|TRANSIENT
argument_list|,
name|b
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|changeDeleteRule
argument_list|(
name|oldRule
argument_list|)
expr_stmt|;
name|restoreReverse
argument_list|(
name|reverse
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testNullifyFlattened
parameter_list|()
block|{
comment|// temporarily set delete rule to NULLIFY...
name|int
name|oldRule
init|=
name|changeDeleteRule
argument_list|(
name|DeleteRule
operator|.
name|NULLIFY
argument_list|)
decl_stmt|;
try|try
block|{
name|DeleteRuleFlatA
name|a
init|=
name|context
operator|.
name|newObject
argument_list|(
name|DeleteRuleFlatA
operator|.
name|class
argument_list|)
decl_stmt|;
name|DeleteRuleFlatB
name|b
init|=
name|context
operator|.
name|newObject
argument_list|(
name|DeleteRuleFlatB
operator|.
name|class
argument_list|)
decl_stmt|;
name|a
operator|.
name|addToFlatB
argument_list|(
name|b
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
comment|// must go on without exceptions...
name|context
operator|.
name|deleteObjects
argument_list|(
name|a
argument_list|)
expr_stmt|;
comment|// assert that join is deleted
name|assertJoinDeleted
argument_list|(
name|a
argument_list|,
name|b
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|DELETED
argument_list|,
name|a
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|MODIFIED
argument_list|,
name|b
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|b
operator|.
name|getUntitledRel
argument_list|()
operator|.
name|contains
argument_list|(
name|a
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|changeDeleteRule
argument_list|(
name|oldRule
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testNullifyFlattenedNoReverse
parameter_list|()
block|{
comment|// temporarily set delete rule to NULLIFY...
name|int
name|oldRule
init|=
name|changeDeleteRule
argument_list|(
name|DeleteRule
operator|.
name|NULLIFY
argument_list|)
decl_stmt|;
name|ObjRelationship
name|reverse
init|=
name|unsetReverse
argument_list|()
decl_stmt|;
try|try
block|{
name|DeleteRuleFlatA
name|a
init|=
name|context
operator|.
name|newObject
argument_list|(
name|DeleteRuleFlatA
operator|.
name|class
argument_list|)
decl_stmt|;
name|DeleteRuleFlatB
name|b
init|=
name|context
operator|.
name|newObject
argument_list|(
name|DeleteRuleFlatB
operator|.
name|class
argument_list|)
decl_stmt|;
name|a
operator|.
name|addToFlatB
argument_list|(
name|b
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
comment|// must go on without exceptions...
name|context
operator|.
name|deleteObjects
argument_list|(
name|a
argument_list|)
expr_stmt|;
comment|// assert that join is deleted
name|assertJoinDeleted
argument_list|(
name|a
argument_list|,
name|b
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|DELETED
argument_list|,
name|a
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|b
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|changeDeleteRule
argument_list|(
name|oldRule
argument_list|)
expr_stmt|;
name|restoreReverse
argument_list|(
name|reverse
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testDenyFlattened
parameter_list|()
block|{
comment|// temporarily set delete rule to DENY...
name|int
name|oldRule
init|=
name|changeDeleteRule
argument_list|(
name|DeleteRule
operator|.
name|DENY
argument_list|)
decl_stmt|;
try|try
block|{
name|DeleteRuleFlatA
name|a
init|=
name|context
operator|.
name|newObject
argument_list|(
name|DeleteRuleFlatA
operator|.
name|class
argument_list|)
decl_stmt|;
name|DeleteRuleFlatB
name|b
init|=
name|context
operator|.
name|newObject
argument_list|(
name|DeleteRuleFlatB
operator|.
name|class
argument_list|)
decl_stmt|;
name|a
operator|.
name|addToFlatB
argument_list|(
name|b
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
try|try
block|{
name|context
operator|.
name|deleteObjects
argument_list|(
name|a
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Must have thrown a deny exception.."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|DeleteDenyException
name|ex
parameter_list|)
block|{
comment|// expected... but check further
name|assertJoinNotDeleted
argument_list|(
name|a
argument_list|,
name|b
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|changeDeleteRule
argument_list|(
name|oldRule
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testDenyFlattenedNoReverse
parameter_list|()
block|{
comment|// temporarily set delete rule to DENY...
name|int
name|oldRule
init|=
name|changeDeleteRule
argument_list|(
name|DeleteRule
operator|.
name|DENY
argument_list|)
decl_stmt|;
name|ObjRelationship
name|reverse
init|=
name|unsetReverse
argument_list|()
decl_stmt|;
try|try
block|{
name|DeleteRuleFlatA
name|a
init|=
name|context
operator|.
name|newObject
argument_list|(
name|DeleteRuleFlatA
operator|.
name|class
argument_list|)
decl_stmt|;
name|DeleteRuleFlatB
name|b
init|=
name|context
operator|.
name|newObject
argument_list|(
name|DeleteRuleFlatB
operator|.
name|class
argument_list|)
decl_stmt|;
name|a
operator|.
name|addToFlatB
argument_list|(
name|b
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
try|try
block|{
name|context
operator|.
name|deleteObjects
argument_list|(
name|a
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Must have thrown a deny exception.."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|DeleteDenyException
name|ex
parameter_list|)
block|{
comment|// expected... but check further
name|assertJoinNotDeleted
argument_list|(
name|a
argument_list|,
name|b
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|changeDeleteRule
argument_list|(
name|oldRule
argument_list|)
expr_stmt|;
name|restoreReverse
argument_list|(
name|reverse
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|int
name|changeDeleteRule
parameter_list|(
name|int
name|deleteRule
parameter_list|)
block|{
name|ObjEntity
name|entity
init|=
name|context
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getObjEntity
argument_list|(
name|DeleteRuleFlatA
operator|.
name|class
argument_list|)
decl_stmt|;
name|ObjRelationship
name|relationship
init|=
name|entity
operator|.
name|getRelationship
argument_list|(
name|DeleteRuleFlatA
operator|.
name|FLAT_B_PROPERTY
argument_list|)
decl_stmt|;
name|int
name|oldRule
init|=
name|relationship
operator|.
name|getDeleteRule
argument_list|()
decl_stmt|;
name|relationship
operator|.
name|setDeleteRule
argument_list|(
name|deleteRule
argument_list|)
expr_stmt|;
return|return
name|oldRule
return|;
block|}
specifier|private
name|ObjRelationship
name|unsetReverse
parameter_list|()
block|{
name|ObjEntity
name|entity
init|=
name|context
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getObjEntity
argument_list|(
name|DeleteRuleFlatA
operator|.
name|class
argument_list|)
decl_stmt|;
name|ObjRelationship
name|relationship
init|=
name|entity
operator|.
name|getRelationship
argument_list|(
name|DeleteRuleFlatA
operator|.
name|FLAT_B_PROPERTY
argument_list|)
decl_stmt|;
name|ObjRelationship
name|reverse
init|=
name|relationship
operator|.
name|getReverseRelationship
argument_list|()
decl_stmt|;
if|if
condition|(
name|reverse
operator|!=
literal|null
condition|)
block|{
name|reverse
operator|.
name|getSourceEntity
argument_list|()
operator|.
name|removeRelationship
argument_list|(
name|reverse
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getClassDescriptorMap
argument_list|()
operator|.
name|removeDescriptor
argument_list|(
literal|"DeleteRuleFlatA"
argument_list|)
expr_stmt|;
name|context
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getClassDescriptorMap
argument_list|()
operator|.
name|removeDescriptor
argument_list|(
literal|"DeleteRuleFlatB"
argument_list|)
expr_stmt|;
block|}
return|return
name|reverse
return|;
block|}
specifier|private
name|void
name|restoreReverse
parameter_list|(
name|ObjRelationship
name|reverse
parameter_list|)
block|{
name|ObjEntity
name|entity
init|=
name|context
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getObjEntity
argument_list|(
name|DeleteRuleFlatA
operator|.
name|class
argument_list|)
decl_stmt|;
name|ObjRelationship
name|relationship
init|=
name|entity
operator|.
name|getRelationship
argument_list|(
name|DeleteRuleFlatA
operator|.
name|FLAT_B_PROPERTY
argument_list|)
decl_stmt|;
name|relationship
operator|.
name|getTargetEntity
argument_list|()
operator|.
name|addRelationship
argument_list|(
name|reverse
argument_list|)
expr_stmt|;
name|context
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getClassDescriptorMap
argument_list|()
operator|.
name|removeDescriptor
argument_list|(
literal|"DeleteRuleFlatA"
argument_list|)
expr_stmt|;
name|context
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getClassDescriptorMap
argument_list|()
operator|.
name|removeDescriptor
argument_list|(
literal|"DeleteRuleFlatB"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertJoinDeleted
parameter_list|(
name|DeleteRuleFlatA
name|a
parameter_list|,
name|DeleteRuleFlatB
name|b
parameter_list|)
block|{
name|ObjectDiff
name|changes
init|=
name|context
operator|.
name|getObjectStore
argument_list|()
operator|.
name|changes
operator|.
name|get
argument_list|(
name|a
operator|.
name|getObjectId
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|changes
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|NodeDiff
argument_list|>
name|diffs
init|=
operator|new
name|ArrayList
argument_list|<
name|NodeDiff
argument_list|>
argument_list|()
decl_stmt|;
name|changes
operator|.
name|appendDiffs
argument_list|(
name|diffs
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|?
argument_list|>
name|it
init|=
name|diffs
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
name|Object
name|diff
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|diff
operator|instanceof
name|ArcOperation
condition|)
block|{
name|ArcOperation
name|arcDelete
init|=
operator|(
name|ArcOperation
operator|)
name|diff
decl_stmt|;
if|if
condition|(
name|arcDelete
operator|.
name|getNodeId
argument_list|()
operator|.
name|equals
argument_list|(
name|a
operator|.
name|getObjectId
argument_list|()
argument_list|)
operator|&&
name|arcDelete
operator|.
name|getTargetNodeId
argument_list|()
operator|.
name|equals
argument_list|(
name|b
operator|.
name|getObjectId
argument_list|()
argument_list|)
operator|&&
name|arcDelete
operator|.
name|getArcId
argument_list|()
operator|.
name|equals
argument_list|(
name|DeleteRuleFlatA
operator|.
name|FLAT_B_PROPERTY
argument_list|)
operator|&&
name|arcDelete
operator|.
name|isDelete
argument_list|()
condition|)
block|{
return|return;
block|}
block|}
block|}
name|fail
argument_list|(
literal|"Join was not deleted for flattened relationship"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertJoinNotDeleted
parameter_list|(
name|DeleteRuleFlatA
name|a
parameter_list|,
name|DeleteRuleFlatB
name|b
parameter_list|)
block|{
name|ObjectDiff
name|changes
init|=
name|context
operator|.
name|getObjectStore
argument_list|()
operator|.
name|changes
operator|.
name|get
argument_list|(
name|a
operator|.
name|getObjectId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|changes
operator|!=
literal|null
condition|)
block|{
name|Collection
argument_list|<
name|NodeDiff
argument_list|>
name|diffs
init|=
operator|new
name|ArrayList
argument_list|<
name|NodeDiff
argument_list|>
argument_list|()
decl_stmt|;
name|changes
operator|.
name|appendDiffs
argument_list|(
name|diffs
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|?
argument_list|>
name|it
init|=
name|diffs
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
name|Object
name|diff
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|diff
operator|instanceof
name|ArcOperation
condition|)
block|{
name|ArcOperation
name|arcDelete
init|=
operator|(
name|ArcOperation
operator|)
name|diff
decl_stmt|;
if|if
condition|(
name|arcDelete
operator|.
name|getNodeId
argument_list|()
operator|.
name|equals
argument_list|(
name|a
operator|.
name|getObjectId
argument_list|()
argument_list|)
operator|&&
name|arcDelete
operator|.
name|getTargetNodeId
argument_list|()
operator|.
name|equals
argument_list|(
name|b
operator|.
name|getObjectId
argument_list|()
argument_list|)
operator|&&
name|arcDelete
operator|.
name|getArcId
argument_list|()
operator|.
name|equals
argument_list|(
name|DeleteRuleFlatA
operator|.
name|FLAT_B_PROPERTY
argument_list|)
operator|&&
operator|!
name|arcDelete
operator|.
name|isDelete
argument_list|()
condition|)
block|{
name|fail
argument_list|(
literal|"Join was  deleted for flattened relationship"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

