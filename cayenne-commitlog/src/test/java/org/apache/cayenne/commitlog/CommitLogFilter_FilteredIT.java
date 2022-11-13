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
name|commitlog
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
name|commitlog
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
name|commitlog
operator|.
name|db
operator|.
name|Auditable2
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
name|commitlog
operator|.
name|db
operator|.
name|Auditable3
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
name|commitlog
operator|.
name|db
operator|.
name|Auditable4
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
name|commitlog
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
name|commitlog
operator|.
name|model
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
name|commitlog
operator|.
name|model
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
name|commitlog
operator|.
name|model
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
name|commitlog
operator|.
name|model
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
name|commitlog
operator|.
name|model
operator|.
name|ToManyRelationshipChange
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
name|commitlog
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
name|stubbing
operator|.
name|Answer
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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|*
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|ArgumentMatchers
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
name|*
import|;
end_import

begin_class
specifier|public
class|class
name|CommitLogFilter_FilteredIT
extends|extends
name|AuditableServerCase
block|{
specifier|protected
name|ObjectContext
name|context
decl_stmt|;
specifier|protected
name|CommitLogListener
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
name|CommitLogListener
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
name|b
lambda|->
name|CommitLogModule
operator|.
name|extend
argument_list|(
name|b
argument_list|)
operator|.
name|commitLogAnnotationEntitiesOnly
argument_list|()
operator|.
name|addListener
argument_list|(
name|mockListener
argument_list|)
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
block|{
name|doAnswer
argument_list|(
operator|(
name|Answer
argument_list|<
name|Object
argument_list|>
operator|)
name|invocation
lambda|->
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
name|assertNotNull
argument_list|(
name|changes
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|changes
operator|.
name|getChanges
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|changes
operator|.
name|getUniqueChanges
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ObjectChange
name|c
init|=
name|changes
operator|.
name|getUniqueChanges
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|c
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ObjectChangeType
operator|.
name|INSERT
argument_list|,
name|c
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|c
operator|.
name|getAttributeChanges
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Confidential
operator|.
name|getInstance
argument_list|()
argument_list|,
name|c
operator|.
name|getAttributeChanges
argument_list|()
operator|.
name|get
argument_list|(
name|Auditable2
operator|.
name|CHAR_PROPERTY2
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|getNewValue
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
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
name|Auditable2
name|a1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Auditable2
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
name|a1
operator|.
name|setCharProperty2
argument_list|(
literal|"zz"
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
name|auditable2
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"P1_1"
argument_list|,
literal|"P2_1"
argument_list|)
expr_stmt|;
name|doAnswer
argument_list|(
operator|(
name|Answer
argument_list|<
name|Object
argument_list|>
operator|)
name|invocation
lambda|->
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
name|assertNotNull
argument_list|(
name|changes
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|changes
operator|.
name|getUniqueChanges
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ObjectChange
name|c
init|=
name|changes
operator|.
name|getChanges
argument_list|()
operator|.
name|get
argument_list|(
name|ObjectId
operator|.
name|of
argument_list|(
literal|"Auditable2"
argument_list|,
name|Auditable2
operator|.
name|ID_PK_COLUMN
argument_list|,
literal|1
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|c
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ObjectChangeType
operator|.
name|UPDATE
argument_list|,
name|c
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|c
operator|.
name|getAttributeChanges
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|AttributeChange
name|pc
init|=
name|c
operator|.
name|getAttributeChanges
argument_list|()
operator|.
name|get
argument_list|(
name|Auditable2
operator|.
name|CHAR_PROPERTY2
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|pc
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Confidential
operator|.
name|getInstance
argument_list|()
argument_list|,
name|pc
operator|.
name|getOldValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Confidential
operator|.
name|getInstance
argument_list|()
argument_list|,
name|pc
operator|.
name|getNewValue
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
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
name|Auditable2
name|a1
init|=
name|SelectById
operator|.
name|query
argument_list|(
name|Auditable2
operator|.
name|class
argument_list|,
literal|1
argument_list|)
operator|.
name|selectOne
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|a1
operator|.
name|setCharProperty1
argument_list|(
literal|"P1_2"
argument_list|)
expr_stmt|;
name|a1
operator|.
name|setCharProperty2
argument_list|(
literal|"P2_2"
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
name|auditable2
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"P1_1"
argument_list|,
literal|"P2_1"
argument_list|)
expr_stmt|;
name|doAnswer
argument_list|(
operator|(
name|Answer
argument_list|<
name|Object
argument_list|>
operator|)
name|invocation
lambda|->
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
name|assertNotNull
argument_list|(
name|changes
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|changes
operator|.
name|getUniqueChanges
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ObjectChange
name|c
init|=
name|changes
operator|.
name|getChanges
argument_list|()
operator|.
name|get
argument_list|(
name|ObjectId
operator|.
name|of
argument_list|(
literal|"Auditable2"
argument_list|,
name|Auditable2
operator|.
name|ID_PK_COLUMN
argument_list|,
literal|1
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|c
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ObjectChangeType
operator|.
name|DELETE
argument_list|,
name|c
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|c
operator|.
name|getAttributeChanges
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Confidential
operator|.
name|getInstance
argument_list|()
argument_list|,
name|c
operator|.
name|getAttributeChanges
argument_list|()
operator|.
name|get
argument_list|(
name|Auditable2
operator|.
name|CHAR_PROPERTY2
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|getOldValue
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
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
name|Auditable2
name|a1
init|=
name|SelectById
operator|.
name|query
argument_list|(
name|Auditable2
operator|.
name|class
argument_list|,
literal|1
argument_list|)
operator|.
name|selectOne
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|context
operator|.
name|deleteObject
argument_list|(
name|a1
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
name|testPostCommit_UpdateToMany
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
literal|"xx"
argument_list|)
expr_stmt|;
name|auditableChild1
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|,
literal|"cc1"
argument_list|)
expr_stmt|;
name|auditableChild1
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|null
argument_list|,
literal|"cc2"
argument_list|)
expr_stmt|;
name|auditableChild1
operator|.
name|insert
argument_list|(
literal|3
argument_list|,
literal|null
argument_list|,
literal|"cc3"
argument_list|)
expr_stmt|;
specifier|final
name|AuditableChild1
name|ac1
init|=
name|SelectById
operator|.
name|query
argument_list|(
name|AuditableChild1
operator|.
name|class
argument_list|,
literal|1
argument_list|)
operator|.
name|selectOne
argument_list|(
name|context
argument_list|)
decl_stmt|;
specifier|final
name|AuditableChild1
name|ac2
init|=
name|SelectById
operator|.
name|query
argument_list|(
name|AuditableChild1
operator|.
name|class
argument_list|,
literal|2
argument_list|)
operator|.
name|selectOne
argument_list|(
name|context
argument_list|)
decl_stmt|;
specifier|final
name|AuditableChild1
name|ac3
init|=
name|SelectById
operator|.
name|query
argument_list|(
name|AuditableChild1
operator|.
name|class
argument_list|,
literal|3
argument_list|)
operator|.
name|selectOne
argument_list|(
name|context
argument_list|)
decl_stmt|;
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
name|selectOne
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|doAnswer
argument_list|(
operator|(
name|Answer
argument_list|<
name|Object
argument_list|>
operator|)
name|invocation
lambda|->
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
name|assertNotNull
argument_list|(
name|changes
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|changes
operator|.
name|getUniqueChanges
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ObjectChange
name|a1c
init|=
name|changes
operator|.
name|getChanges
argument_list|()
operator|.
name|get
argument_list|(
name|ObjectId
operator|.
name|of
argument_list|(
literal|"Auditable1"
argument_list|,
name|Auditable1
operator|.
name|ID_PK_COLUMN
argument_list|,
literal|1
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|a1c
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ObjectChangeType
operator|.
name|UPDATE
argument_list|,
name|a1c
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|a1c
operator|.
name|getAttributeChanges
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|a1c
operator|.
name|getToManyRelationshipChanges
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ToManyRelationshipChange
name|a1c1
init|=
name|a1c
operator|.
name|getToManyRelationshipChanges
argument_list|()
operator|.
name|get
argument_list|(
name|Auditable1
operator|.
name|CHILDREN1
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|a1c1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|a1c1
operator|.
name|getAdded
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|a1c1
operator|.
name|getAdded
argument_list|()
operator|.
name|contains
argument_list|(
name|ac2
operator|.
name|getObjectId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|a1c1
operator|.
name|getAdded
argument_list|()
operator|.
name|contains
argument_list|(
name|ac3
operator|.
name|getObjectId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|a1c1
operator|.
name|getRemoved
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|a1c1
operator|.
name|getRemoved
argument_list|()
operator|.
name|contains
argument_list|(
name|ac1
operator|.
name|getObjectId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
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
name|a1
operator|.
name|removeFromChildren1
argument_list|(
name|ac1
argument_list|)
expr_stmt|;
name|a1
operator|.
name|addToChildren1
argument_list|(
name|ac2
argument_list|)
expr_stmt|;
name|a1
operator|.
name|addToChildren1
argument_list|(
name|ac3
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
name|testPostCommit_IgnoreAttributes
parameter_list|()
throws|throws
name|SQLException
block|{
name|auditable3
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"31"
argument_list|,
literal|"32"
argument_list|)
expr_stmt|;
specifier|final
name|Auditable3
name|a3
init|=
name|SelectById
operator|.
name|query
argument_list|(
name|Auditable3
operator|.
name|class
argument_list|,
literal|1
argument_list|)
operator|.
name|selectOne
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|doAnswer
argument_list|(
operator|(
name|Answer
argument_list|<
name|Object
argument_list|>
operator|)
name|invocation
lambda|->
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
name|assertNull
argument_list|(
name|changes
operator|.
name|getChanges
argument_list|()
operator|.
name|get
argument_list|(
name|ObjectId
operator|.
name|of
argument_list|(
literal|"Auditable3"
argument_list|,
name|Auditable3
operator|.
name|ID_PK_COLUMN
argument_list|,
literal|1
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
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
name|a3
operator|.
name|setCharProperty1
argument_list|(
literal|"33"
argument_list|)
expr_stmt|;
name|a3
operator|.
name|setCharProperty2
argument_list|(
literal|"34"
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
name|testPostCommit_IgnoreToMany
parameter_list|()
throws|throws
name|SQLException
block|{
name|auditable3
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"31"
argument_list|,
literal|"32"
argument_list|)
expr_stmt|;
name|auditable4
operator|.
name|insert
argument_list|(
literal|11
argument_list|,
literal|"41"
argument_list|,
literal|"42"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|auditable4
operator|.
name|insert
argument_list|(
literal|12
argument_list|,
literal|"43"
argument_list|,
literal|"44"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
specifier|final
name|Auditable3
name|a3
init|=
name|SelectById
operator|.
name|query
argument_list|(
name|Auditable3
operator|.
name|class
argument_list|,
literal|1
argument_list|)
operator|.
name|selectOne
argument_list|(
name|context
argument_list|)
decl_stmt|;
specifier|final
name|Auditable4
name|a41
init|=
name|SelectById
operator|.
name|query
argument_list|(
name|Auditable4
operator|.
name|class
argument_list|,
literal|11
argument_list|)
operator|.
name|selectOne
argument_list|(
name|context
argument_list|)
decl_stmt|;
specifier|final
name|Auditable4
name|a42
init|=
name|SelectById
operator|.
name|query
argument_list|(
name|Auditable4
operator|.
name|class
argument_list|,
literal|12
argument_list|)
operator|.
name|selectOne
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|doAnswer
argument_list|(
operator|(
name|Answer
argument_list|<
name|Object
argument_list|>
operator|)
name|invocation
lambda|->
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
name|assertNull
argument_list|(
name|changes
operator|.
name|getChanges
argument_list|()
operator|.
name|get
argument_list|(
name|ObjectId
operator|.
name|of
argument_list|(
literal|"Auditable3"
argument_list|,
name|Auditable3
operator|.
name|ID_PK_COLUMN
argument_list|,
literal|1
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
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
name|a3
operator|.
name|removeFromAuditable4s
argument_list|(
name|a41
argument_list|)
expr_stmt|;
name|a3
operator|.
name|addToAuditable4s
argument_list|(
name|a42
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
name|testPostCommit_IgnoreToOne
parameter_list|()
throws|throws
name|SQLException
block|{
name|auditable3
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"31"
argument_list|,
literal|"32"
argument_list|)
expr_stmt|;
name|auditable3
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|"33"
argument_list|,
literal|"34"
argument_list|)
expr_stmt|;
name|auditable4
operator|.
name|insert
argument_list|(
literal|11
argument_list|,
literal|"41"
argument_list|,
literal|"41"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
specifier|final
name|Auditable3
name|a32
init|=
name|SelectById
operator|.
name|query
argument_list|(
name|Auditable3
operator|.
name|class
argument_list|,
literal|2
argument_list|)
operator|.
name|selectOne
argument_list|(
name|context
argument_list|)
decl_stmt|;
specifier|final
name|Auditable4
name|a4
init|=
name|SelectById
operator|.
name|query
argument_list|(
name|Auditable4
operator|.
name|class
argument_list|,
literal|11
argument_list|)
operator|.
name|selectOne
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|doAnswer
argument_list|(
operator|(
name|Answer
argument_list|<
name|Object
argument_list|>
operator|)
name|invocation
lambda|->
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
name|assertNull
argument_list|(
name|changes
operator|.
name|getChanges
argument_list|()
operator|.
name|get
argument_list|(
name|ObjectId
operator|.
name|of
argument_list|(
literal|"Auditable4"
argument_list|,
name|Auditable4
operator|.
name|ID_PK_COLUMN
argument_list|,
literal|11
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
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
name|a4
operator|.
name|setAuditable3
argument_list|(
name|a32
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
block|}
end_class

end_unit

