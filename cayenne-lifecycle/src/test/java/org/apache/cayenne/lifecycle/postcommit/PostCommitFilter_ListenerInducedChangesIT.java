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
name|lifecycle
operator|.
name|postcommit
package|;
end_package

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
name|assertNotNull
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
name|assertSame
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Matchers
operator|.
name|any
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|doAnswer
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|mock
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|verify
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
name|ArrayList
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
name|Comparator
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
name|annotation
operator|.
name|PrePersist
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
name|annotation
operator|.
name|PreUpdate
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
name|ServerRuntimeBuilder
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
name|lifecycle
operator|.
name|changemap
operator|.
name|AttributeChange
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
name|lifecycle
operator|.
name|changemap
operator|.
name|ChangeMap
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
name|lifecycle
operator|.
name|changemap
operator|.
name|ObjectChange
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
name|lifecycle
operator|.
name|changemap
operator|.
name|ObjectChangeType
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
name|lifecycle
operator|.
name|db
operator|.
name|Auditable1
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
name|lifecycle
operator|.
name|db
operator|.
name|AuditableChild1
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
name|lifecycle
operator|.
name|unit
operator|.
name|AuditableServerCase
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
name|SelectById
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
name|org
operator|.
name|mockito
operator|.
name|invocation
operator|.
name|InvocationOnMock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|stubbing
operator|.
name|Answer
import|;
end_import

begin_comment
comment|/**  * Testing capturing changes introduced by the pre-commit listeners.  */
end_comment

begin_class
specifier|public
class|class
name|PostCommitFilter_ListenerInducedChangesIT
extends|extends
name|AuditableServerCase
block|{
specifier|protected
name|ObjectContext
name|context
decl_stmt|;
specifier|protected
name|PostCommitListener
name|mockListener
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|ServerRuntimeBuilder
name|configureCayenne
parameter_list|()
block|{
name|this
operator|.
name|mockListener
operator|=
name|mock
argument_list|(
name|PostCommitListener
operator|.
name|class
argument_list|)
expr_stmt|;
return|return
name|super
operator|.
name|configureCayenne
argument_list|()
operator|.
name|addModule
argument_list|(
name|PostCommitModuleBuilder
operator|.
name|builder
argument_list|()
operator|.
name|listener
argument_list|(
name|mockListener
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Before
specifier|public
name|void
name|before
parameter_list|()
block|{
name|context
operator|=
name|runtime
operator|.
name|newContext
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPostCommit_Insert
parameter_list|()
throws|throws
name|SQLException
block|{
specifier|final
name|InsertListener
name|listener
init|=
operator|new
name|InsertListener
argument_list|()
decl_stmt|;
name|runtime
operator|.
name|getDataDomain
argument_list|()
operator|.
name|addListener
argument_list|(
name|listener
argument_list|)
expr_stmt|;
specifier|final
name|Auditable1
name|a1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Auditable1
operator|.
name|class
argument_list|)
decl_stmt|;
name|a1
operator|.
name|setCharProperty1
argument_list|(
literal|"yy"
argument_list|)
expr_stmt|;
name|doAnswer
argument_list|(
operator|new
name|Answer
argument_list|<
name|Object
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Object
name|answer
parameter_list|(
name|InvocationOnMock
name|invocation
parameter_list|)
throws|throws
name|Throwable
block|{
name|assertNotNull
argument_list|(
name|listener
operator|.
name|c
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ObjectChange
argument_list|>
name|sortedChanges
init|=
name|sortedChanges
argument_list|(
name|invocation
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|sortedChanges
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|a1
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|sortedChanges
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getPostCommitId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ObjectChangeType
operator|.
name|INSERT
argument_list|,
name|sortedChanges
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|listener
operator|.
name|c
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|sortedChanges
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getPostCommitId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ObjectChangeType
operator|.
name|INSERT
argument_list|,
name|sortedChanges
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|AttributeChange
name|listenerInducedChange
init|=
name|sortedChanges
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getAttributeChanges
argument_list|()
operator|.
name|get
argument_list|(
name|AuditableChild1
operator|.
name|CHAR_PROPERTY1
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|listenerInducedChange
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"c1"
argument_list|,
name|listenerInducedChange
operator|.
name|getNewValue
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
argument_list|)
operator|.
name|when
argument_list|(
name|mockListener
argument_list|)
operator|.
name|onPostCommit
argument_list|(
name|any
argument_list|(
name|ObjectContext
operator|.
name|class
argument_list|)
argument_list|,
name|any
argument_list|(
name|ChangeMap
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|verify
argument_list|(
name|mockListener
argument_list|)
operator|.
name|onPostCommit
argument_list|(
name|any
argument_list|(
name|ObjectContext
operator|.
name|class
argument_list|)
argument_list|,
name|any
argument_list|(
name|ChangeMap
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPostCommit_Delete
parameter_list|()
throws|throws
name|SQLException
block|{
name|auditable1
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"yy"
argument_list|)
expr_stmt|;
name|auditableChild1
operator|.
name|insert
argument_list|(
literal|31
argument_list|,
literal|1
argument_list|,
literal|"yyc"
argument_list|)
expr_stmt|;
specifier|final
name|DeleteListener
name|listener
init|=
operator|new
name|DeleteListener
argument_list|()
decl_stmt|;
name|runtime
operator|.
name|getDataDomain
argument_list|()
operator|.
name|addListener
argument_list|(
name|listener
argument_list|)
expr_stmt|;
specifier|final
name|Auditable1
name|a1
init|=
name|SelectById
operator|.
name|query
argument_list|(
name|Auditable1
operator|.
name|class
argument_list|,
literal|1
argument_list|)
operator|.
name|prefetch
argument_list|(
name|Auditable1
operator|.
name|CHILDREN1
operator|.
name|joint
argument_list|()
argument_list|)
operator|.
name|selectFirst
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|a1
operator|.
name|setCharProperty1
argument_list|(
literal|"zz"
argument_list|)
expr_stmt|;
name|doAnswer
argument_list|(
operator|new
name|Answer
argument_list|<
name|Object
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Object
name|answer
parameter_list|(
name|InvocationOnMock
name|invocation
parameter_list|)
throws|throws
name|Throwable
block|{
name|assertNotNull
argument_list|(
name|listener
operator|.
name|toDelete
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|listener
operator|.
name|toDelete
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ObjectChange
argument_list|>
name|sortedChanges
init|=
name|sortedChanges
argument_list|(
name|invocation
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|sortedChanges
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ObjectChangeType
operator|.
name|UPDATE
argument_list|,
name|sortedChanges
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|a1
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|sortedChanges
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getPostCommitId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ObjectChangeType
operator|.
name|DELETE
argument_list|,
name|sortedChanges
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|listener
operator|.
name|toDelete
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|sortedChanges
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getPostCommitId
argument_list|()
argument_list|)
expr_stmt|;
name|AttributeChange
name|listenerInducedChange
init|=
name|sortedChanges
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getAttributeChanges
argument_list|()
operator|.
name|get
argument_list|(
name|AuditableChild1
operator|.
name|CHAR_PROPERTY1
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|listenerInducedChange
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"yyc"
argument_list|,
name|listenerInducedChange
operator|.
name|getOldValue
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
argument_list|)
operator|.
name|when
argument_list|(
name|mockListener
argument_list|)
operator|.
name|onPostCommit
argument_list|(
name|any
argument_list|(
name|ObjectContext
operator|.
name|class
argument_list|)
argument_list|,
name|any
argument_list|(
name|ChangeMap
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|verify
argument_list|(
name|mockListener
argument_list|)
operator|.
name|onPostCommit
argument_list|(
name|any
argument_list|(
name|ObjectContext
operator|.
name|class
argument_list|)
argument_list|,
name|any
argument_list|(
name|ChangeMap
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPostCommit_Update
parameter_list|()
throws|throws
name|SQLException
block|{
name|auditable1
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"yy"
argument_list|)
expr_stmt|;
name|auditableChild1
operator|.
name|insert
argument_list|(
literal|31
argument_list|,
literal|1
argument_list|,
literal|"yyc"
argument_list|)
expr_stmt|;
specifier|final
name|UpdateListener
name|listener
init|=
operator|new
name|UpdateListener
argument_list|()
decl_stmt|;
name|runtime
operator|.
name|getDataDomain
argument_list|()
operator|.
name|addListener
argument_list|(
name|listener
argument_list|)
expr_stmt|;
specifier|final
name|Auditable1
name|a1
init|=
name|SelectById
operator|.
name|query
argument_list|(
name|Auditable1
operator|.
name|class
argument_list|,
literal|1
argument_list|)
operator|.
name|prefetch
argument_list|(
name|Auditable1
operator|.
name|CHILDREN1
operator|.
name|joint
argument_list|()
argument_list|)
operator|.
name|selectFirst
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|a1
operator|.
name|setCharProperty1
argument_list|(
literal|"zz"
argument_list|)
expr_stmt|;
name|doAnswer
argument_list|(
operator|new
name|Answer
argument_list|<
name|Object
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Object
name|answer
parameter_list|(
name|InvocationOnMock
name|invocation
parameter_list|)
throws|throws
name|Throwable
block|{
name|assertNotNull
argument_list|(
name|listener
operator|.
name|toUpdate
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|listener
operator|.
name|toUpdate
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ObjectChange
argument_list|>
name|sortedChanges
init|=
name|sortedChanges
argument_list|(
name|invocation
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|sortedChanges
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ObjectChangeType
operator|.
name|UPDATE
argument_list|,
name|sortedChanges
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|a1
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|sortedChanges
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getPostCommitId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ObjectChangeType
operator|.
name|UPDATE
argument_list|,
name|sortedChanges
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|listener
operator|.
name|toUpdate
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|sortedChanges
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getPostCommitId
argument_list|()
argument_list|)
expr_stmt|;
name|AttributeChange
name|listenerInducedChange
init|=
name|sortedChanges
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getAttributeChanges
argument_list|()
operator|.
name|get
argument_list|(
name|AuditableChild1
operator|.
name|CHAR_PROPERTY1
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|listenerInducedChange
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"yyc"
argument_list|,
name|listenerInducedChange
operator|.
name|getOldValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"yyc_"
argument_list|,
name|listenerInducedChange
operator|.
name|getNewValue
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
argument_list|)
operator|.
name|when
argument_list|(
name|mockListener
argument_list|)
operator|.
name|onPostCommit
argument_list|(
name|any
argument_list|(
name|ObjectContext
operator|.
name|class
argument_list|)
argument_list|,
name|any
argument_list|(
name|ChangeMap
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|verify
argument_list|(
name|mockListener
argument_list|)
operator|.
name|onPostCommit
argument_list|(
name|any
argument_list|(
name|ObjectContext
operator|.
name|class
argument_list|)
argument_list|,
name|any
argument_list|(
name|ChangeMap
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|List
argument_list|<
name|ObjectChange
argument_list|>
name|sortedChanges
parameter_list|(
name|InvocationOnMock
name|invocation
parameter_list|)
block|{
name|assertSame
argument_list|(
name|context
argument_list|,
name|invocation
operator|.
name|getArguments
argument_list|()
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|ChangeMap
name|changes
init|=
operator|(
name|ChangeMap
operator|)
name|invocation
operator|.
name|getArguments
argument_list|()
index|[
literal|1
index|]
decl_stmt|;
name|List
argument_list|<
name|ObjectChange
argument_list|>
name|sortedChanges
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|changes
operator|.
name|getUniqueChanges
argument_list|()
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|sortedChanges
argument_list|,
operator|new
name|Comparator
argument_list|<
name|ObjectChange
argument_list|>
argument_list|()
block|{
specifier|public
name|int
name|compare
parameter_list|(
name|ObjectChange
name|o1
parameter_list|,
name|ObjectChange
name|o2
parameter_list|)
block|{
return|return
name|o1
operator|.
name|getPostCommitId
argument_list|()
operator|.
name|getEntityName
argument_list|()
operator|.
name|compareTo
argument_list|(
name|o2
operator|.
name|getPostCommitId
argument_list|()
operator|.
name|getEntityName
argument_list|()
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
return|return
name|sortedChanges
return|;
block|}
specifier|static
class|class
name|InsertListener
block|{
specifier|private
name|AuditableChild1
name|c
decl_stmt|;
annotation|@
name|PrePersist
argument_list|(
name|Auditable1
operator|.
name|class
argument_list|)
specifier|public
name|void
name|prePersist
parameter_list|(
name|Auditable1
name|a
parameter_list|)
block|{
name|c
operator|=
name|a
operator|.
name|getObjectContext
argument_list|()
operator|.
name|newObject
argument_list|(
name|AuditableChild1
operator|.
name|class
argument_list|)
expr_stmt|;
name|c
operator|.
name|setCharProperty1
argument_list|(
literal|"c1"
argument_list|)
expr_stmt|;
name|c
operator|.
name|setParent
argument_list|(
name|a
argument_list|)
expr_stmt|;
block|}
block|}
specifier|static
class|class
name|DeleteListener
block|{
specifier|private
name|List
argument_list|<
name|AuditableChild1
argument_list|>
name|toDelete
decl_stmt|;
annotation|@
name|PreUpdate
argument_list|(
name|Auditable1
operator|.
name|class
argument_list|)
specifier|public
name|void
name|prePersist
parameter_list|(
name|Auditable1
name|a
parameter_list|)
block|{
name|toDelete
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|a
operator|.
name|getChildren1
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|AuditableChild1
name|c
range|:
name|toDelete
control|)
block|{
name|c
operator|.
name|getObjectContext
argument_list|()
operator|.
name|deleteObject
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|static
class|class
name|UpdateListener
block|{
specifier|private
name|List
argument_list|<
name|AuditableChild1
argument_list|>
name|toUpdate
decl_stmt|;
annotation|@
name|PreUpdate
argument_list|(
name|Auditable1
operator|.
name|class
argument_list|)
specifier|public
name|void
name|prePersist
parameter_list|(
name|Auditable1
name|a
parameter_list|)
block|{
name|toUpdate
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|a
operator|.
name|getChildren1
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|AuditableChild1
name|c
range|:
name|toUpdate
control|)
block|{
name|c
operator|.
name|setCharProperty1
argument_list|(
name|c
operator|.
name|getCharProperty1
argument_list|()
operator|+
literal|"_"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

